package domain.usecase.comment

import cats.Monad
import cats.data.EitherT
import domain.entity.EntityIdGenerator
import domain.entity.comment.{Comment, CommentFactory}
import domain.entity.user.UserId
import domain.repository.comment.CommentRepository
import domain.repository.post.PostRepository
import domain.repository.user.UserRepository
import domain.usecase.UseCaseError
import helper.ToUseCaseErrorHelpers._
import domain.entity.comment.CommentSpecification._
import domain.entity.post.PostId

class CommentServiceImpl[F[_] : Monad](commentRepository: CommentRepository[F], userRepository: UserRepository[F], postRepository: PostRepository[F])(implicit idGenerator: EntityIdGenerator) extends CommentService[F] {
  def findById(id: String): F[Either[UseCaseError, Comment]] = {
    commentRepository.findById(id).toUCErrorIfRepoLeft
  }


  //Validationが絡むため、repositoryでエラーが起こらなくてもuseCaseとしてはEitherで返す
  def create(content: String, postId: String, userId: String): F[Either[UseCaseError, Int]] = {
    val eitherTRet = for {
      _ <- EitherT(postRepository.findById(postId).toUCErrorIfRepoLeft)
      _ <- EitherT(userRepository.findById(userId).toUCErrorIfRepoLeft)
      commentData <- EitherT.fromEither[F](validateComment(content).toEither.toUCErrorIfLeft)
      comment = CommentFactory.create(commentData, PostId(postId), UserId(userId))
      ret <- EitherT.right[UseCaseError](commentRepository.create(comment))
    } yield ret

    eitherTRet.value
  }

  def update(id: String, content: String, postId: String, userId: String): F[Either[UseCaseError, Int]] = {
    val eitherTRet = for {
      _ <- EitherT(postRepository.findById(postId).toUCErrorIfRepoLeft)
      _ <- EitherT(userRepository.findById(userId).toUCErrorIfRepoLeft)
      comment <- EitherT(commentRepository.findById(id).toUCErrorIfRepoLeft)
      commentData <- EitherT.fromEither[F](validateComment(content).toEither.toUCErrorIfLeft)
      ret <- EitherT.right[UseCaseError](commentRepository.update(comment.changeComment(commentData)))
    } yield ret
    eitherTRet.value
  }

}
