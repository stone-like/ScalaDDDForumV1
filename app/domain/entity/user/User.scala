package domain.entity.user

import domain.entity.{Entity, EntityIdGenerator}

//多分ユーザーはエラーを蓄積させた方が良さげ
case class User(id:UserId,name:UserName,email:Email,password:Password){
  def changeUser(name:UserName,password: Password,email: Email):User= {
    copy(name = name,password=password,email=email)
  }
}

//case class User(name:UserName,password:Password,email:Email,id:UserId)
  //passwordのcompare機能つけたい,そしてこのままだとpasswordが露出してしまうのでcontrollerで変換すること
//object User {
//  def create(name:UserName,password: Password,email: Email)(implicit idGen:EntityIdGenerator):User = {
//    new User(name,password,email,UserId.newId)
//  }
//}
