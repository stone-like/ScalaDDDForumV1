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
import cats.implicits._
import cats.data._

//DockerをつかってやるならH2のinmemoryとかはいらなさそう、inMemoryを使うなら開始時にDBのsettingがいるから結局evolveを使いそう
class UserRepositorySpec extends PostgresSuite{

  val repo = new DoobieUserRepository
//  //これテスト遅いしevolvingつかってinmemorytestした方がいい気もするけど・・・
//  //そうなるとResourceを使って管理するようなdoobieは不向きか・・・？
//  //evolingを使うとなると、datasourceをつくってそこからやるんだけど、scalikejdbcとうまくいかなかった
//  //scalikeJdbcのAutoRollBackが一番楽なんだけど、InntelliJではエラーが出てしまい使えなかった
  "userRepository" should {
    implicit val idGenerator: EntityIdGenerator = new FakeIdGenerator
    "findById" in {

      val user = validateUser("test", "dummy@email.com","password").toEither
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

    "updateUser" in {
      for{
        userData <- validateUser("updated","updated@email.com","updatedP").toEither
        user <- repo.findById("1").run(transactor).unsafeRunSync() //eitherのレーン
        _ <- Either.right(repo.update(user.changeUser(userData._1,userData._2,userData._3)).run(transactor).unsafeRunSync())
        updatedUser <- repo.findById("1").run(transactor).unsafeRunSync()
      } yield assert("updated" == updatedUser.name.value.value)



    }
  }
}
