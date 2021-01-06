package helper.db

trait IOContextManager[F[_],Ctx] {
  //ここのCｔｘはDBに固有もののでなく、例えばScalikeだったらDBSession型だし、doobieだったらTransactor
  def context:Ctx
  //  def transactionalContext[T](execution:(Ctx) => F[T]):F[T]
}
