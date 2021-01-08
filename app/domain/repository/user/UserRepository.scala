package domain.repository.user

import domain.entity.user.User

//いっそEitherTでMonadErrorとしてそろえてもいいかも・・・？doobieともMonadErrorは相性もいいし
trait UserRepository[F[_]] {
  def findById(id: String): F[Either[UserNotFound, User]]


  def create(user: User): F[Int]

  def update(user:User):F[Int]
}
