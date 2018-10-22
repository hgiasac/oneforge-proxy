package forex.interfaces.api.quotes

import cats.implicits._

import akka.http.scaladsl._
import forex.domain._

trait Directives {
  import server.Directives._
  import unmarshalling.Unmarshaller
  import Protocol._

  def getApiRequest: server.Directive1[GetApiRequest] = 
    parameter('symbol.as[String])
      .map(sym => GetApiRequest(sym))
}

object Directives extends Directives
