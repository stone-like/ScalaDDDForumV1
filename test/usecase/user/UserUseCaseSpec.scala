package usecase.user

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.wordspec.AnyWordSpecLike

class UserUseCaseSpec extends AnyWordSpecLike with BeforeAndAfterEach{


  "userUsecase" should {
    import UserUseCaseTestModule._
    import helper.user.CreateUser._
    "can findById" in {
      for {
        _ <-userService.create("test2","test2@email","password")
        user <-userService.findById("2")
      } yield assert("test2" == user.name.value.value)
    }
  }
}
