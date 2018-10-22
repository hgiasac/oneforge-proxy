package forex.config

import org.zalando.grafter.macros._

import scala.concurrent.duration.FiniteDuration

@readers
case class ApplicationConfig(
  akka: AkkaConfig,
  api: ApiConfig,
  executors: ExecutorsConfig,
  oneForge: OneForgeConfig,
  caching: AppCacheConfig
)

case class AkkaConfig(
  name: String,
  exitJvmTimeout: Option[FiniteDuration]
)

case class ApiConfig(
  interface: String,
  port: Int
)

case class ExecutorsConfig(
  default: String
)

case class OneForgeConfig(
  baseUrl: String,
  version: String,
  apiKey: String
)

case class AppCacheConfig(
  stl: Int,
  maximumSize: Long
)
