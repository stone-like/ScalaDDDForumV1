package domain.repository.user

import domain.entity.user.User

trait UserRepository[F[_]] {
    def findById(id:String):F[Either[UserNotFound,User]]


  def create(user: User): F[Int]
}
