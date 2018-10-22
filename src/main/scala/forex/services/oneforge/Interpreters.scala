package forex.services.oneforge

import java.time._

import forex.domain._
import forex.config._
import forex.main._

import monix.eval.Task

import com.typesafe.scalalogging._

import org.atnos.eff._
import org.atnos.eff.addon.monix.task._

import cats._
import cats.data.OptionT
import cats.implicits._

import scala.util.{Success, Failure}
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.Future._

import io.circe.syntax._
import io.circe.parser.decode

import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.stream.Materializer

import scalacache._
import scalacache.Monix.modes._


object Interpreters {

  def dummy[R](implicit m1: _task[R]): Algebra[Eff[R, ?]] =
    new Dummy[R]

  def live[R](
    config: OneForgeConfig,
    actorSystems: ActorSystems,
    appCache: AppCache,
    executors: Executors
  )(implicit m1: _task[R]): Algebra[Eff[R, ?]] =
    new OneForge[R](
      config,
      actorSystems,
      appCache,
      executors

    )
}

final class Dummy[R] private[oneforge] (
  implicit m1: _task[R]
) extends Algebra[Eff[R, ?]] {
  override def get(
    pair: Pair,
    price: Price
  ): Eff[R, Error Either Rate] =
    for {
      result ← fromTask(Task.now(Rate(pair, price, Timestamp.now)))
    } yield Right(result)

  override def allQuotes(): Eff[R, Error Either Vector[Quote]] =
    fromTask(
      Task.now(
        Right(Vector())
      )
    )
}

final class OneForge[R] private[oneforge] (
  config: OneForgeConfig,
  actorSystems: ActorSystems,
  appCache: AppCache,
  executors: Executors
)(implicit m1: _task[R]) extends Algebra[Eff[R, ?]]
    with LazyLogging with OneForgeCaching {

  import actorSystems._
  import Currency._

  implicit val ec = executors.default
  implicit val cacheStore = appCache.default

  val supportedCurrencies: Vector[Currency] =
    Vector(AUD, CAD, CHF, EUR, GBP, NZD, JPY, SGD, USD)

  val supportedCurrencyPairs: Vector[Pair] =
    supportedCurrencies
      .combinations(2)
      .toVector
      .flatMap {
        case Vector(a, b) => Vector((a, b), (b, a))
      }
      .map {
        case (a, b) => Pair(a, b)
      }

  lazy val baseUrl = s"${config.baseUrl}/${config.version}"

  def buildUrl(url: String): String =
    s"${baseUrl}/${url}&api_key=${config.apiKey}"

  override def get(pair: Pair, quantity: Price): Eff[R, Error Either Rate] = {

    for {
      quote <- getQuoteFromPair(pair)
      res <- fromTask(
        Task.now(
          quote.right.map(q => Rate(
                pair,
                Price(BigDecimal(quantity.value.doubleValue * q.bid.value)),
                q.timestamp)
              )
        )
      )
    } yield res
  }

  def getQuoteFromPair(pair: Pair): Eff[R, Error Either Quote] = 
    allQuotes.map(
      _.right
      .map(getQuoteFromList(_, pair))
      .flatMap {
        case Some(q) => q.asRight[Error]
        case None => Error.NotFound(s"Can not find quote by pair ${pair.show}").asLeft
      }
    )
      

  def listQuotes(): Eff[R, Error Either Vector[Quote]] = {

    val uri = buildUrl("quotes?pairs=" + supportedCurrencyPairs.map(_.show).mkString(","))

    fromTask(
      Task.fromFuture(
        Http().singleRequest(HttpRequest(uri = uri)).flatMap {
          case HttpResponse(StatusCodes.OK, _, entity, _) =>
            entity
              .toStrict(3.seconds)
              .map(b =>
                decode[Vector[Quote]](b.data.utf8String)
                  .left.map (_ => Error.Generic)
              )

          case HttpResponse(code, _, _, _) =>
            Future { Left(Error.Generic) }
        }
      )
    )
  }

  def fetchAndSaveQuotes(): Eff[R, Error Either Vector[Quote]] =
    listQuotes.flatMap {
      case Left(e) => Eff.pure(e.asLeft)
      case Right(quotes) => fromTask(
        put("quotes")(QuoteCacheItem(quotes, false, quotes.head.timestamp))
          .map(_ => quotes.asRight[Error])
      )

    }

  override def allQuotes(): Eff[R, Error Either Vector[Quote]] = {

      for {
        cacheResult <- fromTask(getAllCacheQuotes)
        result <- cacheResult match {
          case Some(item) => 
            if (!item.timestamp.isExpired(appCache.secondToLive) || item.locked)
              fromTask(Task.now(item.data.asRight[Error]))
            else for {
              _ <- fromTask(changeCacheLock(item, true))
              res <- fetchAndSaveQuotes
                      .flatMap {
                        case Right(r) => Eff.pure(r.asRight[Error])
                        case Left(e) => fromTask(
                          changeCacheLock(item, false).map(_ => e.asLeft[Vector[Quote]])
                        )
                      }
            } yield res

          case None => fetchAndSaveQuotes
        }
      } yield result
  }

}
