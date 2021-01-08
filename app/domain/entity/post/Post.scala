package domain.entity.post

import domain.entity.user.UserId


//もしnestedになるような集約となる場合はlensを使うことも検討
case class Post(id:PostId,title:PostTitle,content: PostContent,userId:UserId){
  def changePost(title: PostTitle,content: PostContent):Post= {
    copy(title = title,content=content)
  }
}