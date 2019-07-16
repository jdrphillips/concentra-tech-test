package example

trait T
trait A

object Utility {
  def combine(a1: A, a2: A): A = a1
}

import Utility.combine

case class Node(
  left: Option[Node],
  right: Option[Node],
  predicate: Option[T => Boolean],
  actions: A
) {

  def reduce(t: T, a: A): A = ???

}
