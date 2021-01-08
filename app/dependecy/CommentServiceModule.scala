package dependecy

import domain.adapter.infra.comment.DoobieCommentRepository
import domain.adapter.infra.post.DoobiePostRepository
import domain.adapter.infra.user.DoobieUserRepository
import domain.repository.comment.CommentRepository
import domain.repository.post.PostRepository
import domain.repository.user.UserRepository
import domain.usecase.comment.{CommentService, CommentServiceImpl}
import helper.TypeHelpers.KleisliDoobie
import com.softwaremill.macwire._
object CommentServiceModule {
  import dependecy.IdManagerModule._
  lazy val postRepository:PostRepository[KleisliDoobie] = wire[DoobiePostRepository]
  lazy val userRepository:UserRepository[KleisliDoobie] = wire[DoobieUserRepository]
  lazy val commentRepository:CommentRepository[KleisliDoobie] = wire[DoobieCommentRepository]
  lazy val commentService:CommentService[KleisliDoobie] = wire[CommentServiceImpl[KleisliDoobie]]
}
