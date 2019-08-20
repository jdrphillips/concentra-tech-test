package example

sealed trait T

case object SuperUser extends T
case object Admin extends T
case object User extends T

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


/*
  - Combine a and actions.
  - Reduce right with this value, and pass the value to left's reduction. Return this.
  - If predicate is false, ignore actions and right entirely
 */
