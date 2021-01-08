package helper

import cats.Monad
import cats.data.NonEmptyList
import domain.entity.DomainError
import domain.repository.InfraError
import domain.usecase.UseCaseError
import cats.data._
import cats.implicits._

object ToUseCaseErrorHelpers {
  implicit class DomainErrorToUseCaseError[R](domainResult: Either[NonEmptyList[DomainError], R]) {
    def toUCErrorIfLeft: Either[UseCaseError, R] = {
      domainResult.left.map(err => UseCaseError.create(err))
    }
  }

  implicit class RepoErrorToUseCaseError[G[_]:Monad,R,E<:InfraError](repoResult: G[Either[E, R]]) {
    def toUCErrorIfRepoLeft: G[Either[UseCaseError, R]] = {
      repoResult.map(
        _.left.map(err => UseCaseError.create(err))
      )
    }
  }
}
