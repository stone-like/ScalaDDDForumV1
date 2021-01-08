package domain.usecase.post

import domain.entity.post.Post
import domain.usecase.UseCaseError

trait PostService[F[_]] {
  def findById(id:String):F[Either[UseCaseError,Post]]
  def create(title:String,content:String,userId:String):F[Either[UseCaseError, Int]]
  def update(id:String,title:String,content:String,userId:String):F[Either[UseCaseError, Int]]
}
