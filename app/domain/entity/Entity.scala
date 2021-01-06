package domain.entity
trait Entity[Id <: EntityId]{
  val id:Id

  override def equals(obj:Any):Boolean =
    obj match {
      case that:Entity[_] => this.getClass == that.getClass && this.id == that.id
      case _ => false
    }

  override def hashCode(): Int = 31 + id.hashCode()
}

trait EntityId extends Any with Value[String]

trait EntityIdGenerator{
  def genId():String
}