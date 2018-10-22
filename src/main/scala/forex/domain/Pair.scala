package forex.domain

import io.circe._
import io.circe.generic.semiauto._

import cats.Show
import cats.implicits._

final case class Pair(
  from: Currency,
  to: Currency
)

object Pair {

  import Currency._

  implicit val encoder: Encoder[Pair] =
    deriveEncoder[Pair]

  implicit val show: Show[Pair] = Show.show {
    p => p.from.show + p.to.show
  }

  implicit val decoder: Decoder[Pair] =
    Decoder.decodeString.emap { sym =>
      if (sym.length == 6) {
        val (from, to) = sym.splitAt(3)
        Right(
          Pair(
            Currency.fromString(from),
            Currency.fromString(to)
          )
        )
      }
      else
        Left("Pair")
    }
}
