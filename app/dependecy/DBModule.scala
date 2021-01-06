package dependecy

import cats.effect.IO
import com.softwaremill.macwire.wire
import helper.db.{IOContextManager, IOContextManagerOnDoobie}
import helper.db.IOContextManagerOnDoobie.DoobieCtx

class DBModule {
  lazy val ioContext:IOContextManager[IO,DoobieCtx] = wire[IOContextManagerOnDoobie]
}
