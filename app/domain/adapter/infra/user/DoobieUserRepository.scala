package domain.adapter.infra.user


import cats.data.Kleisli
import domain.entity.user.User
import domain.repository.user.{UserNotFound, UserRepository}
import doobie.implicits._
import helper.TypeHelpers.KleisliDoobie
import doobie.refined.implicits._
import helper.Circe.CoercibleDoobieCodec._

//Serviceとかで複数つなげるときに一個一個transactとやってたら別々のtransactionになってしまうっぽいのが・・・
//それもあとでやる(機能自体はしているので)
class DoobieUserRepository extends UserRepository[KleisliDoobie]{
  //とりあえずsql関連で発生するエラーは拾わないようにする
  override def findById(id:String):KleisliDoobie[Either[UserNotFound,User]] = {
    Kleisli{
      implicit transactor =>
        sql"""SELECT id,name,email,password
             |FROM users
             |WHERE id=$id
             |""".stripMargin
          .query[User]
          .option
          .transact(transactor).map{
          case Some(a) => Right(a)
          case None => Left(UserNotFound(id))
        }
    }
  }


  //passwordの暗号化はまた後で,VOとprimitiveのwrap,unwrapはほしいかも
  override def create(user:User):KleisliDoobie[Int] = {
    Kleisli{
      implicit transactor =>
        //refinedを使っているため余計にvalueが必要・・・implicitでconverterみたいの作ってとってくるのもありか・・・？
        val id = user.id.value
        val name = user.name.value.value
        val email = user.email.value.value
        val password = user.password.value.value

        sql"""insert into users (id,name,email,password)
              values ($id,$name,$email,$password)""".stripMargin
          .update
          .run
          .transact(transactor)
    }
  }

  override def update(user: User): KleisliDoobie[Int] = {
    Kleisli{
      implicit transactor =>
        val id = user.id.value
        val name = user.name.value.value
        val email = user.email.value.value
        val password = user.password.value.value

        sql"""update  users set name = $name ,email = $email , password = $password where id = $id""".stripMargin
          .update
          .run
          .transact(transactor)
    }
  }
}
