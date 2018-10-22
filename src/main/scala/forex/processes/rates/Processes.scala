package forex.processes.rates

import cats.Monad
import cats.data.EitherT
import forex.domain._
import forex.services._

object Processes {
  def apply[F[_]]: Processes[F] =
    new Processes[F] {}
}

trait Processes[F[_]] {
  import messages._
  import converters._

  def get(
      request: GetRequest
  )(
      implicit
      M: Monad[F],
      OneForge: OneForge[F]
  ): F[Error Either Rate] =
    (for {
      result ← EitherT(
        OneForge.get(
          Pair(request.from, request.to),
          request.quantity
        )
      ).leftMap(toProcessError)
    } yield result).value

  def allQuotes(
    implicit M: Monad[F],
    OneForge: OneForge[F]
  ): F[Error Either Vector[Quote]] =
    (for {
      result <- EitherT(
        OneForge.allQuotes()
      ).leftMap(toProcessError)
    } yield result).value
}
