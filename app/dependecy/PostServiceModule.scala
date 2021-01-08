package dependecy

import domain.adapter.infra.post.DoobiePostRepository
import domain.repository.post.PostRepository
import domain.usecase.post.{PostService, PostServiceImpl}
import helper.TypeHelpers.KleisliDoobie
import com.softwaremill.macwire._
import domain.adapter.infra.user.DoobieUserRepository
import domain.repository.user.UserRepository

object PostServiceModule {
  import dependecy.IdManagerModule._
  lazy val postRepository:PostRepository[KleisliDoobie] = wire[DoobiePostRepository]
  lazy val userRepository:UserRepository[KleisliDoobie] = wire[DoobieUserRepository]
  lazy val postService:PostService[KleisliDoobie] = wire[PostServiceImpl[KleisliDoobie]]
}
