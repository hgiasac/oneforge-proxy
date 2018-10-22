package forex.main

import forex.config._
import forex.{ services ⇒ s }
import forex.{ processes ⇒ p }
import org.zalando.grafter.macros._

@readerOf[ApplicationConfig]
case class Processes(
  oneForge: OneForgeConfig,
  actorSystems: ActorSystems,
  appCache: AppCache,
  executors: Executors
) {


  implicit final lazy val _oneForge: s.OneForge[AppEffect] =
    s.OneForge.live[AppStack](
      oneForge,
      actorSystems,
      appCache,
      executors
    )

  final val Rates = p.Rates[AppEffect]

}
