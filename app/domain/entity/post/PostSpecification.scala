package domain.entity.post

import cats.Applicative
import cats.data.ValidatedNel
import domain.entity.DomainError
import cats.implicits._
import cats.data._

object PostSpecification {
  type ValidPostData = (PostTitle,PostContent)
  type ErrorOr[A] = ValidatedNel[DomainError,A]

  //最後に利用側でtoEitherにしてあげればいい
  def validatePost(title:String,content:String): ValidatedNel[DomainError, ValidPostData] = {
    Applicative[ErrorOr].map2(
      PostTitle(title).toValidatedNel,
      PostContent(content).toValidatedNel
    )((_,_))
  }
}
