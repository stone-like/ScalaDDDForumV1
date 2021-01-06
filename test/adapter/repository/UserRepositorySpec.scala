package adapter.repository

import cats.data.NonEmptyList
import cats.effect.{Blocker, IO, Resource}
import domain.FakeIdGenerator
import domain.adapter.infra.user.DoobieUserRepository
import domain.entity.EntityIdGenerator
import domain.entity.user.{UserFactory, UserNameInValid}
import domain.entity.user.UserSpecification.validateUser
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import suite.{PostgresSuite, ResourceSuite}

//DockerをつかってやるならH2のinmemoryとかはいらなさそう、inMemoryを使うなら開始時にDBのsettingがいるから結局evolveを使いそう
class UserRepositorySpec extends PostgresSuite{
//  val resources: Resource[IO, HikariTransactor[IO]] =
//    for {
//      ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
//      be <- Blocker[IO] // our blocking EC
//      xa <- HikariTransactor.newHikariTransactor[IO](
//        "org.postgresql.Driver",                        // driver classname
//        "jdbc:postgresql://localhost:9999/forum1",   // connect URL
//        "root",                                   // username
//        "root",                                     // password
//        ce,                                     // await connection here
//        be                                      // execute JDBC operations here
//      )
//    } yield xa

  val repo = new DoobieUserRepository
//  //これテスト遅いしevolvingつかってinmemorytestした方がいい気もするけど・・・
//  //そうなるとResourceを使って管理するようなdoobieは不向きか・・・？
//  //evolingを使うとなると、datasourceをつくってそこからやるんだけど、scalikejdbcとうまくいかなかった
//  //scalikeJdbcのAutoRollBackが一番楽なんだけど、InntelliJではエラーが出てしまい使えなかった
  "userRepository" should {
    implicit val idGenerator: EntityIdGenerator = new FakeIdGenerator
    "findById" in {

      val user = validateUser("test", "password", "dummy@email.com").toEither
        .map((userData) => UserFactory.create(userData._1, userData._2, userData._3))

      //これControllerなりServiceなりKleisliのrunを使うところではF[_]:Kleisliとしてあげないといけないっぽい
      val retDBKleisli =  user match {

        case Right(user) => {
          for{
            _ <- repo.create(user)
            ret <- repo.findById("1")
          } yield ret
        }
      }

     retDBKleisli.run(transactor).unsafeRunSync() match {
       case Right(user) => {
         assert("test" == user.name.value.value)
       }

     }

    }
  }
}
