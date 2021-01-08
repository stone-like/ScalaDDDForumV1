package domain.repository.post

import domain.repository.InfraError

sealed trait PostError extends InfraError

case class PostNotFound(postId:String) extends PostError
