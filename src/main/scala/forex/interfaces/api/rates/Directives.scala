package forex.interfaces.api.rates

import akka.http.scaladsl._
import forex.domain._

trait Directives {
  import server.Directives._
  import unmarshalling.Unmarshaller
  import Protocol._

  def getApiRequest: server.Directive1[GetApiRequest] =
    for {
      from ← parameter('from.as(currency))
      to ← parameter('to.as(currency))
      quantity <- parameter('quantity.as(quantity))
    } yield GetApiRequest(from, to, quantity)

  private val currency =
    Unmarshaller.strict[String, Currency](Currency.fromString)

  private val quantity =
    Unmarshaller.strict[String, Price](Price.fromString)
}

object Directives extends Directives
