package helper.db

import cats.effect.{Blocker, ContextShift, IO}
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import helper.db.IOContextManagerOnDoobie.DoobieCtx

object IOContextManagerOnDoobie{
  type DoobieCtx = Transactor[IO]
}
class IOContextManagerOnDoobie extends IOContextManager[IO,DoobieCtx]{
  implicit val cs:ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

  override def context: DoobieCtx = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://localhost:9999/forum1",
    user = "root",
    pass = "root",
    blocker = Blocker.liftExecutionContext(ExecutionContexts.synchronous)
  )


}
