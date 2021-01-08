package domain.repository.comment

import domain.entity.comment.Comment

trait CommentRepository[F[_]] {
  def findById(id: String): F[Either[CommentNotFound, Comment]]

  def create(comment: Comment): F[Int]

  def update(comment: Comment): F[Int]
}
