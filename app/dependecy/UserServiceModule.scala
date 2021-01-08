package dependecy

import domain.usecase.user.{UserService, UserServiceImpl}
import helper.TypeHelpers.KleisliDoobie
import com.softwaremill.macwire._
import domain.adapter.infra.user.DoobieUserRepository
import domain.repository.user.UserRepository


object UserServiceModule {
   import dependecy.IdManagerModule._
   lazy val userRepository:UserRepository[KleisliDoobie] = wire[DoobieUserRepository]
   lazy val userService:UserService[KleisliDoobie] = wire[UserServiceImpl[KleisliDoobie]]
}
