package domain.entity

import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.{MaxSize, MinSize, NonEmpty}
import eu.timepit.refined.numeric.Interval
import eu.timepit.refined.string.MatchesRegex
import io.estatico.newtype.macros.newtype
import eu.timepit.refined.refineV

package object user {



  type UserNameRule = MinSize[3] And MaxSize[10]
  type EmailRule = MatchesRegex["""[a-z0-9]+@[a-z0-9]+\.[a-z0-9]{2,}"""]
  type PasswordRule = MinSize[3] And MaxSize[10]
  type UserIdRule = NonEmpty //ここはあとでUUIDのルールにするといいかも

  type UserNameParam = String Refined UserNameRule
  type EmailParam = String Refined EmailRule
  type PasswordParam = String Refined PasswordRule
  type UserIdParam = String Refined UserIdRule

  @newtype case class UserId(value:String)

//  object UserId {
//    def apply(rawUserId:String): UserId={
//      UserId(rawUserId)
////      refineV[UserIdRule](rawUserId).map(UserId(_)).left.map(UserIdInValid)
//    }
//  }

  object UserId {
    def newId(implicit idGen: EntityIdGenerator): UserId = UserId(idGen.genId())
  }


  //validationのときにDBを使ってしかできないバリデーションはDomainServiceでやる
  //例えここでEmailがOkだったとしてもDBに自分以外で同じやつがあったらダメとかやりたいときもあるので別に二度手間ではない
  @newtype case class UserName(value: UserNameParam)

  //    object UserName {
  //      def apply(userName: String): Option[UserName] = {
  //        Some(userName).filter(isValidName).map(new UserName(_))
  //      }
  //    }
  object UserName {
    def apply(rawUserName: String): Either[DomainError, UserName] = {

      refineV[UserNameRule](rawUserName).map(UserName(_)).left.map((_)=>UserNameInValid(rawUserName))
    }
  }
  //@newtypeはextends禁止なのでどうにかしたい、uuidはどうしよう・・・entityと紐づいた形でいいか？
  @newtype case class Password(value: PasswordParam)

  //    object Password {
  //      def apply(password: String): Option[Password] = {
  //        Some(password).filter(isValidPassword).map(new Password(_))
  //      }
  //    }

      object Password {
        def apply(rawPassword: String): Either[DomainError, Password]= {
          refineV[PasswordRule](rawPassword).map(Password(_)).left.map((_) => UserPasswordInValid(rawPassword))
        }
      }

  @newtype case class Email(value: EmailParam)

  //    object Email {
  //      def apply(email: String): Option[Email] = {
  //        Some(email).filter(isValidEmail).map(new Email(_))
  //      }
  //    }

  object Email {
    def apply(rawEmail: String): Either[UserEmailInValid, Email] =
      refineV[EmailRule](rawEmail).map(Email(_)).left.map((_) => UserEmailInValid(rawEmail))
  }


  //ValidateNel[DomainError,User]とする
  //fix createとか特定のメソッドでしか生成できないようにしないと、せっかくバリデーションしても、別メソッドで作られてしまうので、そこには注意
  //例えばEmail()としただけでは
  //    def isValidEmail(email: String): Boolean = {
  //      if (email.length <= 2 || email.length >= 20) {
  //        false
  //      } else {
  //        true
  //      }
  //    }
  //
  //    def isValidPassword(password: String): Boolean = {
  //      if (password.length <= 2 || password.length >= 10) {
  //        false
  //      } else {
  //        true
  //      }
  //    }
  //
  //    def isValidName(name: String): Boolean = {
  //      if (name.length <= 2 || name.length >= 10) {
  //        false
  //      } else {
  //        true
  //      }
  //    }

}
