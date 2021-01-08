package domain.entity.comment

import cats.Applicative
import cats.data.ValidatedNel
import domain.entity.DomainError
import cats.implicits._
import cats.data._

object CommentSpecification {
  type ValidCommentData = CommentContent
  type ErrorOr[A] = ValidatedNel[DomainError,A]

  //最後に利用側でtoEitherにしてあげればいい
  def validateComment(content:String): ValidatedNel[DomainError, ValidCommentData] = {
      CommentContent(content).toValidatedNel
  }
}
