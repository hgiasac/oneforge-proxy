package forex.interfaces.api.quotes

import monix.eval.Task
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.addon.monix.task._


import akka.http.scaladsl._
import forex.config._
import forex.main._
import forex.domain._
import forex.interfaces.api.utils._
import org.zalando.grafter.macros._

@readerOf[ApplicationConfig]
case class Routes(
  processes: Processes,
  runners: Runners
) {
  import server.Directives._
  import ApiMarshallers._
  import Protocol._
  import Directives._
  import processes._
  import runners._
  import Quote._

  lazy val route: server.Route =
    path("quotes") {
      get {
        complete {
          runApp(
            Rates.allQuotes
          )
        }
      }
    }

}
