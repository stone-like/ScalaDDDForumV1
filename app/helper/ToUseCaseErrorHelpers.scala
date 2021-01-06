package helper

import cats.data.NonEmptyList
import domain.entity.DomainError
import domain.usecase.UseCaseError

object ToUseCaseErrorHelpers {
  implicit class DomainErrorToUseCaseError[R](domainResult: Either[NonEmptyList[DomainError], R]) {
    def toUCErrorIfLeft: Either[UseCaseError, R] = {
      domainResult.left.map(err => UseCaseError.create(err))
    }
  }

//  implicit class RepoErrorToUseCaseError[R](repoResult: Future[Either[RepoError, R]])(implicit ctx: ExecutionContext) {
//    def toUCErrorIfRepoLeft: Future[Either[UseCaseError, R]] = {
//      repoResult.map(
//        _.left.map(err => UseCaseError.create(err))
//      )
//    }
//  }
}
