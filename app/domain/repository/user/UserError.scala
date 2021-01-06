package domain.repository.user

import domain.entity.user.UserId
import domain.repository.InfraError

sealed trait UserError extends InfraError

case class UserNotFound(userId:String) extends UserError
