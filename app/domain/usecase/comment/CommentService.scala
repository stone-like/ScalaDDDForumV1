package domain.usecase.comment

import domain.entity.comment.Comment
import domain.usecase.UseCaseError


trait CommentService[F[_]] {
  def findById(id:String):F[Either[UseCaseError,Comment]]
  def create(content:String,postId:String,userId:String):F[Either[UseCaseError, Int]]
  def update(id:String,content:String,postId:String,userId:String):F[Either[UseCaseError, Int]]
}
