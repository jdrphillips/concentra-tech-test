package example

trait T

case class A(str: String) {
  def +(that: A): A = A(this.str + that.str)
}

object A {
  val empty: A = A("")
}

case class Node(
  left: Option[Node],
  right: Option[Node],
  predicate: Option[T => Boolean],
  actions: A
) {

  def reduce(t: T, a: A): A = ???

}
