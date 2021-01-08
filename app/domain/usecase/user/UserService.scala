package domain.usecase.user

import domain.entity.user.User
import domain.repository.user.UserNotFound
import domain.usecase.UseCaseError

trait UserService[F[_]]{
  def findById(id:String):F[Either[UseCaseError,User]]
  def create(name:String,email:String,password:String):F[Either[UseCaseError, Int]]
  def update(id:String,name:String,email:String,password:String):F[Either[UseCaseError, Int]]
}
