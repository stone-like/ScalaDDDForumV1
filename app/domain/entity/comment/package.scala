package domain.entity

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import io.estatico.newtype.macros.newtype
import eu.timepit.refined.refineV

package object comment {

  type CommentContentRule = NonEmpty
  type CommentContentParam = String Refined CommentContentRule

   @newtype case class CommentId(value:String)
   object CommentId{
    def newId(implicit idGen: EntityIdGenerator): CommentId = CommentId(idGen.genId())
   }

   @newtype case class CommentContent(value:CommentContentParam)
  object CommentContent{
    def apply(rawContent: String): Either[DomainError,CommentContent] = {
      refineV[CommentContentRule](rawContent).map(CommentContent(_)).left.map((_)=>CommentContentInvalid(rawContent))
    }
  }

  case class CreateCommentParam(content:String,postId:String,userId:String)
  case class UpdateCommentParam(id:String,content:String,postId:String,userId:String)


}
