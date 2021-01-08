package domain.repository.post

import domain.entity.post.Post

trait PostRepository[F[_]] {
  def findById(id:String):F[Either[PostNotFound,Post]]
  def create(post: Post):F[Int]
  def update(post:  Post):F[Int]
}
