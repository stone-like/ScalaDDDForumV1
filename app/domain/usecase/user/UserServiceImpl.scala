package domain.usecase.user

import cats.Monad
import domain.entity.user.{User, UserFactory}
import domain.repository.InfraError
import domain.repository.user.UserRepository
import domain.usecase.UseCaseError
import helper.ToUseCaseErrorHelpers._
import cats.data._
import cats.implicits._
import domain.entity.EntityIdGenerator
import domain.entity.user.UserSpecification.validateUser

//FのMonad条件はRepoErrorToUseCaseError[G[_]:Monad,R,E<:InfraError]のために必要
class UserServiceImpl[F[_] : Monad](userRepository: UserRepository[F])(implicit idGenerator: EntityIdGenerator) extends UserService[F] {
  //  //KleisliにIdGeneratorを入れてあげるかはまだちょっとわからないけど、Kleisliのせいで複雑性が増してしまっていることも事実
  //  //たしかに隠蔽した方が見栄えはいいけど代償が大きすぎる気もする

   def findById(id: String): F[Either[UseCaseError, User]] = {
    userRepository.findById(id).toUCErrorIfRepoLeft
  }


  //Validationが絡むため、repositoryでエラーが起こらなくてもuseCaseとしてはEitherで返す
   def create(name: String, email: String, password: String): F[Either[UseCaseError, Int]] = {
    val eitherTRet = for {
      userData <- EitherT.fromEither[F](validateUser(name, email, password).toEither.toUCErrorIfLeft)
      user = UserFactory.create(userData._1, userData._2, userData._3)
      ret <- EitherT.right[UseCaseError](userRepository.create(user))
    } yield ret

     eitherTRet.value
  }

  def update(id: String, name: String, email: String, password: String): F[Either[UseCaseError, Int]] = {
    val eitherTRet = for {
      user <- EitherT(findById(id))
      userData <- EitherT.fromEither[F](validateUser(name, email, password).toEither.toUCErrorIfLeft)
      ret <- EitherT.right[UseCaseError](userRepository.update(user.changeUser(userData._1, userData._2, userData._3)))
    } yield ret
    eitherTRet.value
  }

}
