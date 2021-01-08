package domain.entity

import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.{MaxSize, NonEmpty}
import io.estatico.newtype.macros.newtype
import eu.timepit.refined.refineV


package object post {

  type PostTitleRule = NonEmpty And MaxSize[30]
  type PostContentRule = NonEmpty


  type PostTitleParam = String Refined PostTitleRule
  type PostContentParam = String Refined PostContentRule


   @newtype case class PostId(value:String)

  object PostId{
    def newId(implicit idGen: EntityIdGenerator): PostId = PostId(idGen.genId())
  }

  @newtype case class PostTitle(value:PostTitleParam)

  object PostTitle{
    def apply(rawTitle: String): Either[DomainError,PostTitle] = {
      refineV[PostTitleRule](rawTitle).map(PostTitle(_)).left.map((_)=>PostTitleInvalid(rawTitle))
    }
  }

  @newtype case class PostContent(value:PostContentParam)

  object PostContent{
    def apply(rawContent: String): Either[DomainError,PostContent] = {
      refineV[PostContentRule](rawContent).map(PostContent(_)).left.map((_)=>PostContentInvalid(rawContent))
    }
  }

  case class CreatePostParam(title:String,content:String,userId:String)
  case class UpdatePostParam(id:String,title:String,content:String,userId:String)
}
