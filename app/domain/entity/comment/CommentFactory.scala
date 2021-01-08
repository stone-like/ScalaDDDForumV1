package domain.entity.comment

import domain.entity.EntityIdGenerator
import domain.entity.post.PostId
import domain.entity.user.UserId

object CommentFactory {
  def create(content: CommentContent,postId:PostId,userId:UserId)(implicit idGen:EntityIdGenerator):Comment = {
    Comment(CommentId.newId,content,postId,userId)
  }
}
