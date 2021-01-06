package domain

import domain.entity.EntityIdGenerator

class FakeIdGenerator extends EntityIdGenerator {
  var id = 0
  override def genId(): String = {
    id = id+1
    id.toString
  }

}
