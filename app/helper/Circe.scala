package helper

import domain.entity.comment.Comment
import domain.entity.post.Post
import domain.entity.user.User
import io.circe.generic.encoding.DerivedAsObjectEncoder
import io.circe.{Decoder, Encoder, KeyDecoder, KeyEncoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._
import io.circe.refined._

object Circe {
    object CoercibleCirceCodecs{
      //_.coerceとかにはimport io.estatico.newtype.ops._が要る
      implicit def coercibleDecoder[A: Coercible[B, *], B: Decoder]: Decoder[A] =
        Decoder[B].map(_.coerce[A])

      implicit def coercibleEncoder[A: Coercible[B, *], B: Encoder]: Encoder[A] =
        Encoder[B].contramap(_.repr.asInstanceOf[B])

      implicit def coercibleKeyDecoder[A: Coercible[B, *], B: KeyDecoder]
      : KeyDecoder[A] =
        KeyDecoder[B].map(_.coerce[A])

      implicit def coercibleKeyEncoder[A: Coercible[B, *], B: KeyEncoder]
      : KeyEncoder[A] =
        KeyEncoder[B].contramap[A](_.repr.asInstanceOf[B])
      //ここに順次追加していく
      implicit val userEncoder: Encoder[User] = deriveEncoder[User]
      implicit val userDecoder: Decoder[User] = deriveDecoder[User]

      implicit val postEncoder: Encoder[Post] = deriveEncoder[Post]
      implicit val postDecoder: Decoder[Post] = deriveDecoder[Post]

      implicit val commentEncoder: Encoder[Comment] = deriveEncoder[Comment]
      implicit val commentDecoder: Decoder[Comment] = deriveDecoder[Comment]
    }

    object CoercibleDoobieCodec {
      import cats.Eq
      import doobie.{Put, Read}
      import io.estatico.newtype.Coercible

      implicit def newTypePut[R, N](implicit ev: Coercible[Put[R], Put[N]], R: Put[R]): Put[N] = ev(R)
      implicit def newTypeRead[R, N](implicit ev: Coercible[Read[R], Read[N]], R: Read[R]): Read[N] = ev(R)

      /** derive an Eq instance for newtype N from Eq instance for Repr type R */
      implicit def coercibleEq[R, N](implicit ev: Coercible[Eq[R], Eq[N]], R: Eq[R]): Eq[N] = ev(R)
    }

}
