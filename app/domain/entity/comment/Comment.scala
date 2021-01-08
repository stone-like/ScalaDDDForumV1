package domain.entity.comment

import domain.entity.post.PostId
import domain.entity.user.UserId

case class Comment(id:CommentId,content: CommentContent,postId:PostId,userId:UserId){
  def changeComment(content: CommentContent):Comment= {
    copy(content=content)
  }
}
