package forex.domain

import scala.util.Try
import io.circe._
import io.circe.generic.extras.semiauto._

case class Price(value: BigDecimal) extends AnyVal

object Price {
  def apply(value: Double): Price =
    Price(BigDecimal(value))

  implicit val encoder: Encoder[Price] = deriveUnwrappedEncoder[Price]
  implicit val decoder: Decoder[Price] =
    deriveUnwrappedDecoder 

  def fromString(value: String): Price =
    Price(
      BigDecimal(
        Try { value.toDouble }.toOption.getOrElse(100.0)
      )
    )
}
