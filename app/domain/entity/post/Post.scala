package domain.entity.post

import domain.entity.user.UserId


//もしnestedになるような集約となる場合はlensを使うことも検討
//もしpostの中にcommentを入れて集約を作る場合はPostRepositoryのみとなる,ver2ではこっちにしてみる
case class Post(id:PostId,title:PostTitle,content: PostContent,userId:UserId){
  def changePost(title: PostTitle,content: PostContent):Post= {
    copy(title = title,content=content)
  }
}