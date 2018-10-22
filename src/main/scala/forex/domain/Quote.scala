package forex.domain

import io.circe._
import io.circe.generic.semiauto._
import io.circe.generic.extras.semiauto.{deriveUnwrappedDecoder, deriveUnwrappedEncoder}
import cats._
import cats.implicits._


case class QuoteCacheItem(
  data: Vector[Quote],
  locked: Boolean,
  timestamp: Timestamp
)

case class Quote(
  symbol: Pair,
  price: Price,
  bid: Quote.Bid,
  ask: Quote.Ask,
  timestamp: Timestamp
)

object Quote {

  import Currency._

  case class Bid(value: Double) extends AnyVal
  case class Ask(value: Double) extends AnyVal

  object Bid {
    implicit val decoder: Decoder[Bid] =
      deriveUnwrappedDecoder[Bid]

    implicit val encoder: Encoder[Bid] =
      deriveUnwrappedEncoder[Bid]
  }

  object Ask {
    implicit val decoder: Decoder[Ask] =
      deriveUnwrappedDecoder[Ask]

    implicit val encoder: Encoder[Ask] =
      deriveUnwrappedEncoder[Ask]
  }

  implicit val decoder: Decoder[Quote] =
    deriveDecoder[Quote]

  implicit val encoder: Encoder[Quote] =
    deriveEncoder[Quote]
}
