package domain.usecase.post

import cats.Monad
import cats.data.EitherT
import domain.entity.EntityIdGenerator
import domain.entity.post.{Post, PostFactory}
import domain.repository.post.PostRepository
import domain.repository.user.UserRepository
import domain.usecase.UseCaseError
import helper.ToUseCaseErrorHelpers._
import domain.entity.post.PostSpecification._
import domain.entity.user.UserId

class PostServiceImpl[F[_]:Monad](postRepository: PostRepository[F],userRepository: UserRepository[F])(implicit idGenerator: EntityIdGenerator)  extends PostService[F] {
  def findById(id: String): F[Either[UseCaseError, Post]] = {
    postRepository.findById(id).toUCErrorIfRepoLeft
  }


  //Validationが絡むため、repositoryでエラーが起こらなくてもuseCaseとしてはEitherで返す
  def create(title:String,content:String,userId:String): F[Either[UseCaseError, Int]] = {
    val eitherTRet = for {
      _ <- EitherT(userRepository.findById(userId).toUCErrorIfRepoLeft)
      postData <- EitherT.fromEither[F](validatePost(title,content).toEither.toUCErrorIfLeft)
      post = PostFactory.create(postData._1, postData._2,UserId(userId))
      ret <- EitherT.right[UseCaseError](postRepository.create(post))
    } yield ret

    eitherTRet.value
  }

  def update(id: String, title:String,content:String,userId:String): F[Either[UseCaseError, Int]] = {
    val eitherTRet = for {
      _ <- EitherT(userRepository.findById(userId).toUCErrorIfRepoLeft)
      post <- EitherT(postRepository.findById(id).toUCErrorIfRepoLeft)
      postData <- EitherT.fromEither[F](validatePost(title,content).toEither.toUCErrorIfLeft)
      ret <- EitherT.right[UseCaseError](postRepository.update(post.changePost(postData._1, postData._2)))
    } yield ret
    eitherTRet.value
  }
}
