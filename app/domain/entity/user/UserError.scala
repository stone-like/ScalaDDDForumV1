package domain.entity.user

import domain.entity.DomainError


sealed trait UserError extends DomainError


case class UserIdInValid(id: String) extends UserError

case class UserNameInValid(name: String) extends UserError

case class UserEmailInValid(email: String) extends UserError

case class UserPasswordInValid(password: String) extends UserError

