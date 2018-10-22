package forex.main

import cats._

import forex.config._
import forex.domain._
import org.zalando.grafter._
import org.zalando.grafter.macros._

import scalacache._
import scalacache.caffeine._
import com.github.benmanes.caffeine.cache.Caffeine

@readerOf[ApplicationConfig]
case class AppCache(cache: AppCacheConfig) extends Start {

  lazy val secondToLive = cache.stl

  lazy val underlyingCaffeineCache = Caffeine.newBuilder()
    .maximumSize(1000L)
    .build[String, Entry[QuoteCacheItem]]

  lazy val default: Cache[QuoteCacheItem] =
    CaffeineCache(underlyingCaffeineCache)

  override def start: Eval[StartResult] =
    StartResult.eval("Cache") {
      default
    }

}
