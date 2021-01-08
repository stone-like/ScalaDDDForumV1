package domain.repository.comment

import domain.repository.InfraError

sealed trait CommentError extends InfraError

case class CommentNotFound(commentId:String) extends CommentError

