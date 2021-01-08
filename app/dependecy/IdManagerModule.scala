package dependecy

import domain.adapter.infra.UUIDEntityIdGenerator
import domain.entity.EntityIdGenerator
import com.softwaremill.macwire._

object IdManagerModule {
  implicit val idManager:EntityIdGenerator = wire[UUIDEntityIdGenerator]
}
