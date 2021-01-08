package controllers

import domain.entity.post.{CreatePostParam, PostContentInvalid, PostTitleInvalid, UpdatePostParam}
import domain.repository.post.PostNotFound
import domain.repository.user.UserNotFound
import javax.inject._
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, ControllerComponents, Request}
import io.circe.generic.auto._

@Singleton
class PostController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with Circe{

  import dependecy.DBModule._
  import dependecy.PostServiceModule._

  def returnErrorToString(error:Throwable):String = error match {
    case PostTitleInvalid(v) =>  "PostTitleInvalid "+v
    case PostContentInvalid(v) =>  "PostContentInvalid "+v
    case PostNotFound(v) =>  "PostNotFount "+v
    case UserNotFound(v) => "UserNotFound "+v
    case _ => "UnknownError Probably Related DB Error"
  }

  def convertListErrorToErrorString(list:List[Throwable]):String = {
    list.foldRight[String]("")((error,prev) => prev+" "+returnErrorToString(error))
  }
  //userCreateParamを作ってエンコードする
  def create:Action[CreatePostParam] =  Action(circe.json[CreatePostParam]){ implicit request: Request[CreatePostParam] =>
    //Action(parser)でparserを指定すればもしダメだったらBadRequestを返してくれるらしい

    postService.create(request.body.title,request.body.content,request.body.userId).run(ioContext.context).unsafeRunSync()
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

  def update:Action[UpdatePostParam] =  Action(circe.json[UpdatePostParam]){ implicit request: Request[UpdatePostParam] =>
    //Action(parser)でparserを指定すればもしダメだったらBadRequestを返してくれるらしい

    postService.update(request.body.id,request.body.title,request.body.content,request.body.userId).run(ioContext.context).unsafeRunSync()
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
