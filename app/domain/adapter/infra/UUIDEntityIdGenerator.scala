package domain.adapter.infra

import java.util.UUID

import domain.entity.EntityIdGenerator

class UUIDEntityIdGenerator extends EntityIdGenerator{
  override def genId(): String = UUID.randomUUID().toString
}
