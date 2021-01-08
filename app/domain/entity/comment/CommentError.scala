package domain.entity.comment

import domain.entity.DomainError

sealed trait CommentError extends DomainError

case class CommentContentInvalid(content: String) extends CommentError