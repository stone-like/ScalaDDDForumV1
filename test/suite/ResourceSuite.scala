package suite

import cats.effect.{ContextShift, IO, Resource, Timer}
import cats.effect.concurrent.Deferred
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.ExecutionContext

trait ResourceSuite[A] extends AnyWordSpecLike with  BeforeAndAfterAll {
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit val timer: Timer[IO]     = IO.timer(ExecutionContext.global)

  def resources:Resource[IO,A]
  //問題は各TestClassの始めと終わりにDBの初期化で平気かどうか、まぁ初期化しないでも平気なtestを書けばよさそうだけど・・・

  private[this] var res: A            = _
  private[this] var cleanUp: IO[Unit] = _

  private[this] val latch = Deferred[IO, Unit].unsafeRunSync()

  override def beforeAll(): Unit = {
    super.beforeAll()
    val (r, h) = resources.allocated.unsafeRunSync()
    res = r
    cleanUp = h
    latch.complete(()).unsafeRunSync()
  }

  override def afterAll(): Unit = {
    //このcleanupではおそらくconnectionを切ってるだけでDBの初期化処理はしていない
    print(cleanUp)
    cleanUp.unsafeRunSync()
    super.afterAll()
  }

  def withResources(f: (=> A) => Unit): Unit = f {
    //to ensure that the resource has been allocated even before any spec(...) bodies
    latch.get.unsafeRunSync()
    res
  }
}