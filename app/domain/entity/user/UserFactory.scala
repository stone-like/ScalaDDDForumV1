package domain.entity.user

import domain.entity.EntityIdGenerator
import domain.entity.user._

object UserFactory {
  def create(name:UserName,password: Password,email: Email)(implicit idGen:EntityIdGenerator):User = {
    User(UserId.newId,name,password,email)
  }
}
