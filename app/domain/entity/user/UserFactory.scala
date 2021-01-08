package domain.entity.user

import domain.entity.EntityIdGenerator
import domain.entity.user._

object UserFactory {
  def create(name:UserName,email: Email,password: Password)(implicit idGen:EntityIdGenerator):User = {
    User(UserId.newId,name,email,password)
  }
}
