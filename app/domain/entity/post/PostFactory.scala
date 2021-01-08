package domain.entity.post

import domain.entity.EntityIdGenerator
import domain.entity.user.UserId

object PostFactory {
  def create(title:PostTitle,content: PostContent,userId:UserId)(implicit idGen:EntityIdGenerator):Post = {
    Post(PostId.newId,title,content,userId)
  }
}
