package domain.adapter.infra.post

import cats.data.Kleisli
import domain.entity.post.Post
import domain.repository.post.{PostNotFound, PostRepository}
import helper.TypeHelpers.KleisliDoobie
import doobie.implicits._
import doobie.refined.implicits._
import helper.Circe.CoercibleDoobieCodec._

class DoobiePostRepository extends PostRepository[KleisliDoobie] {
  override def findById(id: String): KleisliDoobie[Either[PostNotFound, Post]] = {
    Kleisli{
      implicit transactor =>
        sql"""SELECT id,title,content,user_id
             |FROM posts
             |WHERE id=$id
             |""".stripMargin
          .query[Post]
          .option
          .transact(transactor).map{
          case Some(a) => Right(a)
          case None => Left(PostNotFound(id))
        }
    }
  }
  override def create(post: Post): KleisliDoobie[Int] = {
    Kleisli{
      implicit transactor =>
        //refinedを使っているため余計にvalueが必要・・・implicitでconverterみたいの作ってとってくるのもありか・・・？
        val id = post.id.value
        val title = post.title.value.value
        val content = post.content.value.value
        val userId = post.userId.value

        sql"""insert into posts (id,title,content,user_id)
              values ($id,$title,$content,$userId)""".stripMargin
          .update
          .run
          .transact(transactor)
    }
  }
  override def update(post: Post): KleisliDoobie[Int] = {
    Kleisli{
      implicit transactor =>
        val id = post.id.value
        val title = post.title.value.value
        val content = post.content.value.value
        val userId = post.userId.value

        sql"""update  posts set title = $title ,content = $content  where id = $id""".stripMargin
          .update
          .run
          .transact(transactor)
    }
  }
}
