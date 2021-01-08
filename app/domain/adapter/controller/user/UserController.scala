package domain.adapter.controller.user

import domain.usecase.UseCaseError


object UserController extends App{
  import dependecy.DBModule._
  import dependecy.UserServiceModule._

  userService.update("aee0e437-7b79-44a9-a0ea-94e1755d379a","updated","test@email.com","password").run(ioContext.context).unsafeRunSync() match {
    case Right(v) => println(v)
    case Left(UseCaseError(error)) => println(error)
  }

}
