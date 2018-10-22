package forex.services.oneforge

import forex.domain._

trait Algebra[F[_]] {
  def get(pair: Pair, price: Price): F[Error Either Rate]
  def allQuotes(): F[Error Either Vector[Quote]]
}
