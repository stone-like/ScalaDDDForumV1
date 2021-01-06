package suite

import cats.effect.{Blocker, ContextShift, IO}
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import doobie.util.transactor.Transactor
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike
import doobie.postgres.implicits._
import doobie.implicits._
import scala.concurrent.ExecutionContext

trait PostgresSuite extends AnyWordSpecLike  with BeforeAndAfterAll{
  protected var postgres: EmbeddedPostgres = _
  protected var transactor: Transactor[IO] = _
  implicit private val ioContextShift: ContextShift[IO] =
    IO.contextShift(ExecutionContext.global)

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    postgres = EmbeddedPostgres.builder().start()
    transactor = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver",
      postgres.getJdbcUrl("postgres", "postgres"),
      "postgres",
      "postgres",
      Blocker.liftExecutionContext(ExecutionContext.global)
    )
    sql"""CREATE TABLE users (
      id   text primary key,
      name text not null,
      email text not null,
      password text not null
      )""".stripMargin
      .update.run
      .transact(transactor)
      .unsafeRunSync()
  }


  override protected def afterAll(): Unit = {
    postgres.close()
    super.afterAll()
  }


}
