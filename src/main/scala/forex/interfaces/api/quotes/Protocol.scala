package forex.interfaces.api.quotes

import forex.domain._
import io.circe._

object Protocol {
  case class GetApiRequest(
    symbol: String
  )
}
