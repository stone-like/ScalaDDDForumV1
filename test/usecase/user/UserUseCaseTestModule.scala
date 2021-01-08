package usecase.user

import cats.Id
import domain.repository.user.{UserNotFound, UserRepository}
import domain.usecase.user.{UserService, UserServiceImpl}
import com.softwaremill.macwire._
import domain.FakeIdGenerator
import domain.entity.EntityIdGenerator
import domain.entity.user.{Email, User, UserFactory, UserId, UserName}

import scala.collection.immutable.Map

class MockUserRepository extends UserRepository[Id] {

  import helper.user.CreateUser._

  var db = Map[String, User](
    "1" -> createUser("test","test@email.com","password")
  )

  def resetMap():Unit = {
    db = Map[String, User](
      "1" -> createUser("test","test@email.com","password")
    )
  }

  override def findById(id: String): Id[Either[UserNotFound, User]] = {

      db.get(id) match {
        case Some(value) => Right(value)
        case None => Left(UserNotFound(id))
      }

  }

  override def create(user: User): Id[Int] = {
    db = db updated((db.size+1).toString,user)
    1
  }

  override def update(user: User):Id[Int] = {
      db = db updated(user.id.value, user)
      1
  }
}

object UserUseCaseTestModule {
  implicit val idGenerator:EntityIdGenerator = new  FakeIdGenerator
  lazy val userRepository:UserRepository[Id] = wire[MockUserRepository]
  lazy val userService:UserService[Id] = wire[UserServiceImpl[Id]]
}
