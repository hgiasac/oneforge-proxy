package forex.domain

import io.circe._
import io.circe.generic.extras.semiauto._
import io.circe.java8.time._

import java.time._

case class Timestamp(value: OffsetDateTime) extends AnyVal {

  def isExpired(ttl: Int): Boolean =
    value
      .plusSeconds(ttl)
      .isAfter(Timestamp.now.value)
}

object Timestamp {
  def now: Timestamp =
    Timestamp(OffsetDateTime.now)

  implicit val encoder: Encoder[Timestamp] =
    deriveUnwrappedEncoder[Timestamp]

  implicit val decoder: Decoder[Timestamp] =
    Decoder.decodeInt.emap { num =>
      Right(
        Timestamp(
          OffsetDateTime.ofInstant(
            Instant.ofEpochSecond(num),
            ZoneId.systemDefault()
          )
        )
      )
    }
}
