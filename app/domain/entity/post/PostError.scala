package domain.entity.post

import domain.entity.DomainError

sealed trait PostError extends DomainError

case class PostIdInvalid(id: String) extends PostError
case class PostTitleInvalid(title: String) extends PostError
case class PostContentInvalid(content: String) extends PostError

