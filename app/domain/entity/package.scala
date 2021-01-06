package domain

package object entity {
  trait Value[T] extends Any{
    def value:T
  }
}
