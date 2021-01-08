package controllers

import domain.entity.comment.{CommentContentInvalid, CreateCommentParam, UpdateCommentParam}
import domain.repository.comment.CommentNotFound
import domain.repository.post.PostNotFound
import domain.repository.user.UserNotFound
import javax.inject._
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, ControllerComponents, Request}
import io.circe.generic.auto._

@Singleton
class CommentController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with Circe{

  import dependecy.DBModule._
  import dependecy.CommentServiceModule._

  def returnErrorToString(error:Throwable):String = error match {
    case CommentContentInvalid(v) =>  "CommentContentInvalid "+v
    case CommentNotFound(v) =>  "CommentNotFount "+v
    case UserNotFound(v) => "UserNotFound "+v
    case PostNotFound(v) => "PostNotFound "+v
    case _ => "UnknownError Probably Related DB Error"
  }

  def convertListErrorToErrorString(list:List[Throwable]):String = {
    list.foldRight[String]("")((error,prev) => prev+" "+returnErrorToString(error))
  }
  //userCreateParamを作ってエンコードする
  def create:Action[CreateCommentParam] =  Action(circe.json[CreateCommentParam]){ implicit request: Request[CreateCommentParam] =>
    //Action(parser)でparserを指定すればもしダメだったらBadRequestを返してくれるらしい

    commentService.create(request.body.content,request.body.postId,request.body.userId).run(ioContext.context).unsafeRunSync()
    match {
      case Right(_) => Ok("successfully usercreated")
      case Left(error) =>  {
        //errorをthrowableからstringにしないとjsonにできないっぽい？
        //        BadRequest(decode[cats.data.NonEmptyList[Throwable]](error.error.toList))
        //本当は回復処理とかいろいろあるけどここではjsonで返すだけにする,caseでUserNameInvalidとかしないと具体的な奴がとってこれないっぽい...
        if(error.error.size > 1){
          val errorMessage = convertListErrorToErrorString(error.error.toList)
          BadRequest(errorMessage)
        }else{
          BadRequest(returnErrorToString(error.error.head))
        }

      }
    }
  }

  def update:Action[UpdateCommentParam] =  Action(circe.json[UpdateCommentParam]){ implicit request: Request[UpdateCommentParam] =>
    //Action(parser)でparserを指定すればもしダメだったらBadRequestを返してくれるらしい

    commentService.update(request.body.id,request.body.content,request.body.postId,request.body.userId).run(ioContext.context).unsafeRunSync()
    match {
      case Right(_) => Ok("successfully usercreated")
      case Left(error) =>  {
        //errorをthrowableからstringにしないとjsonにできないっぽい？
        //        BadRequest(decode[cats.data.NonEmptyList[Throwable]](error.error.toList))
        //本当は回復処理とかいろいろあるけどここではjsonで返すだけにする,caseでUserNameInvalidとかしないと具体的な奴がとってこれないっぽい...
        if(error.error.size > 1){
          val errorMessage = convertListErrorToErrorString(error.error.toList)
          BadRequest(errorMessage)
        }else{
          BadRequest(returnErrorToString(error.error.head))
        }

      }
    }
  }
}
