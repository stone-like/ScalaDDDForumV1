package helper

import cats.data.Kleisli
import cats.effect.IO
import doobie.util.transactor.Transactor

object TypeHelpers {
  type KleisliDoobie[A] = Kleisli[IO,Transactor[IO],A]
}
