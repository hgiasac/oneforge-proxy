package forex.interfaces.api.utils

import akka.http.scaladsl._
import forex.processes._

object ApiExceptionHandler {

  def apply(): server.ExceptionHandler =
    server.ExceptionHandler {
      case _: RatesError ⇒
        ctx ⇒
          ctx.complete("Something went wrong in the rates process")
      case e: Throwable ⇒
        ctx ⇒
          ctx.complete(e.getMessage)
    }

}
