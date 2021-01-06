package domain.entity.user

import cats.Applicative
import cats.data.ValidatedNel
import domain.entity.DomainError
import cats.implicits._
import cats.data._

object UserSpecification {
  type ValidUserData = (UserName, Password, Email)
  type ErrorOr[A] = ValidatedNel[DomainError,A]

  //最後に利用側でtoEitherにしてあげればいい
  def validateUser(userName: String, password: String, email: String): ValidatedNel[DomainError, ValidUserData] = {

    Applicative[ErrorOr].map3(
      UserName(userName).toValidatedNel,
      Password(password).toValidatedNel,
      Email(email).toValidatedNel
    )((_,_,_))
  }
}
