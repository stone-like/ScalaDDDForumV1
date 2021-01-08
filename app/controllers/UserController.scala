package controllers

import domain.entity.user.{CreateUserParam, UpdateUserParam, UserEmailInValid, UserNameInValid, UserPasswordInValid}
import domain.repository.user.UserNotFound
import io.circe
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import io.circe.jawn.decode



@Singleton
class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with Circe{

  import dependecy.DBModule._
  import dependecy.UserServiceModule._

  def returnErrorToString(error:Throwable):String = error match {
    case UserEmailInValid(v) =>  "UserEmailInvalid "+v
    case UserNameInValid(v) =>  "UserNameInvalid "+v
    case UserPasswordInValid(v) =>  "UserPasswordInvalid "+v
    case UserNotFound(v) => "UserNotFound "+v
    case _ => "UnknownError Probably Related DB Error"
  }

  def convertListErrorToErrorString(list:List[Throwable]):String = {
     list.foldRight[String]("")((error,prev) => prev+" "+returnErrorToString(error))
  }
  //userCreateParamを作ってエンコードする
  def create:Action[CreateUserParam] =  Action(circe.json[CreateUserParam]){ implicit request: Request[CreateUserParam] =>
   //Action(parser)でparserを指定すればもしダメだったらBadRequestを返してくれるらしい

    userService.create(request.body.name,request.body.email,request.body.password).run(ioContext.context).unsafeRunSync()
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

  def update:Action[UpdateUserParam] =  Action(circe.json[UpdateUserParam]){ implicit request: Request[UpdateUserParam] =>
    //Action(parser)でparserを指定すればもしダメだったらBadRequestを返してくれるらしい

    userService.update(request.body.id,request.body.name,request.body.email,request.body.password).run(ioContext.context).unsafeRunSync()
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
