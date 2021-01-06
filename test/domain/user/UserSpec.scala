package domain.user

import cats.data.NonEmptyList
import cats.data.Validated.{Invalid, Valid}
import domain.FakeIdGenerator
import domain.entity.EntityIdGenerator
import domain.entity.user.{Email, Password, User, UserFactory, UserName, UserNameInValid, UserPasswordInValid}
import domain.entity.user.UserSpecification.validateUser
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class UserSpec extends AnyWordSpecLike with Matchers {

//  //生成を制限できるようになったのはいいけどtestがすごくやりづらい・・・

  implicit val idGenerator: EntityIdGenerator = new FakeIdGenerator


  "UserEntity" should {
    "can validate" in {
      val test = validateUser("test", "password", "dummy@email.com")
      for {
        name <- UserName("test")
        pass <- Password("password")
        email <- Email("dummy@email.com")
      } yield assert(test == Valid(name, pass, email))

    }


    "invalid when invalid input" in {
      val test = validateUser("t", "password", "dummy@email.com")
      assert(test == Invalid(NonEmptyList.fromList(
        List(UserNameInValid("t"))
      ).head))
    }

    "invalid accumulated when multiple invalid input" in {
      val test = validateUser("t", "p", "dummy@email.com")
      assert(test == Invalid(NonEmptyList.fromList(
        List(UserNameInValid("t"), UserPasswordInValid("p"))
      ).head))
    }

    "can create user" in {

      val user = validateUser("test", "password", "dummy@email.com").toEither
        .map((userData) => UserFactory.create(userData._1, userData._2, userData._3))
      user match {
        case Right(user) => assert("1" == user.id.value)
      }
    }

    "can not create user" in {

      val user = validateUser("t", "password", "dummy@email.com").toEither
        .map((userData) => UserFactory.create(userData._1, userData._2, userData._3))
      user match {
        case Left(l) => assert(l == NonEmptyList.fromList(
          List(UserNameInValid("t"))
        ).head)

      }

    }
  }
}