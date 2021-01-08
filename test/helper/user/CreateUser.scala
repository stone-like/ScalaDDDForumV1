package helper.user

import cats.Id
import cats.data.EitherT
import domain.FakeIdGenerator
import domain.entity.{DomainError, EntityIdGenerator}
import domain.entity.user.{Email, Password, User, UserFactory, UserName}

object CreateUser {
    implicit val idGenerator:EntityIdGenerator = new FakeIdGenerator
    def createUser(name:String,email:String,password:String):User ={
        val eitherTUser = for {
         name <- EitherT.fromEither[Id](UserName(name))
         email <-  EitherT.fromEither[Id](Email(email))
         password <-  EitherT.fromEither[Id](Password(password))
       } yield UserFactory.create(name,email,password)

      eitherTUser.value.fold(_ => None,Some(_)).head
    }
}
