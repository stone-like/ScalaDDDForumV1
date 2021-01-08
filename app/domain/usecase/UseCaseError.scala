package domain.usecase

import cats.data.NonEmptyList

case class UseCaseError(error: NonEmptyList[Throwable])

object UseCaseError {
  def create(message:Throwable): UseCaseError             = apply(NonEmptyList.one(message))
  def create(error: NonEmptyList[Throwable]): UseCaseError = apply(error)
}