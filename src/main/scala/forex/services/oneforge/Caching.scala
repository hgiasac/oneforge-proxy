package forex.services.oneforge

import forex.config._
import forex.domain._

import monix.eval.Task

import scalacache._
import scalacache.Monix.modes._

trait OneForgeCaching {

  implicit val cacheStore: Cache[QuoteCacheItem]

  def getAllCacheQuotes: Task[Option[QuoteCacheItem]] =
    get("quotes")


  def getQuoteFromList(
    quotes: Vector[Quote],
    pair: Pair
  ): Option[Quote] =
    quotes.find(q => q.symbol == pair)

  def getQuoteByPair(pair: Pair): Task[Option[Quote]] =
    get("quotes").map(
      _.flatMap(r => getQuoteFromList(r.data, pair))
    )

  def changeCacheLock(item: QuoteCacheItem, locked: Boolean): Task[Any] =
    put("quotes")(QuoteCacheItem(
      item.data,
      locked,
      item.timestamp
    ))
}
