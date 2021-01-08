package domain.adapter.infra.comment

import cats.data.Kleisli
import domain.entity.comment.Comment
import domain.repository.comment.{CommentNotFound, CommentRepository}
import helper.TypeHelpers.KleisliDoobie
import doobie.implicits._
import doobie.refined.implicits._
import helper.Circe.CoercibleDoobieCodec._

class DoobieCommentRepository extends CommentRepository[KleisliDoobie] {
  override def findById(id: String): KleisliDoobie[Either[CommentNotFound, Comment]] = {
    Kleisli{
      implicit transactor =>
        sql"""SELECT id,content,post_id,user_id
             |FROM comments
             |WHERE id=$id
             |""".stripMargin
          .query[Comment]
          .option
          .transact(transactor).map{
          case Some(a) => Right(a)
          case None => Left(CommentNotFound(id))
        }
    }
  }
  override def create(comment: Comment): KleisliDoobie[Int] = {
    Kleisli{
      implicit transactor =>
        //refinedを使っているため余計にvalueが必要・・・implicitでconverterみたいの作ってとってくるのもありか・・・？
        val id = comment.id.value
        val content = comment.content.value.value
        val postId = comment.postId.value
        val userId = comment.userId.value

        sql"""insert into comments (id,content,post_id,user_id)
              values ($id,$content,$postId,$userId)""".stripMargin
          .update
          .run
          .transact(transactor)
    }
  }
  override def update(comment: Comment): KleisliDoobie[Int] = {
    Kleisli{
      implicit transactor =>
        val id = comment.id.value
        val content = comment.content.value.value
        val postId = comment.postId.value
        val userId = comment.userId.value

        sql"""update  comments set  content = $content  where id = $id""".stripMargin
          .update
          .run
          .transact(transactor)
    }
  }
}
