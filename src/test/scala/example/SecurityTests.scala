package example

import org.scalatest.{FunSpec, Matchers}

object TestUtils {

  implicit class RichNode(val node: Node) extends AnyVal {

    def withLeft(that: Node): Node = node.copy(left = Some(that))
    def withRight(that: Node): Node = node.copy(right = Some(that))

    def withPredicate(fn: T => Boolean) = node.copy(predicate = Some(fn))

    def successful = node.copy(predicate = success)
    def failed = node.copy(predicate = failure)

    def forAdmin = node.copy(predicate = Some({
      case SuperUser | Admin => true
      case _ => false
    }))

    def forSuperUser = node.copy(predicate = Some({
      case SuperUser => true
      case _ => false
    }))

  }

  val failure: Option[T => Boolean] = Some { _: T => false }
  val success: Option[T => Boolean] = Some { _: T => true }

  def emptyNode(a: A): Node = Node(None, None, None, a)

}

object Fixture {

  import TestUtils._

  val graph =
    emptyNode(A(":"))

      .withRight {
        emptyNode(A("R"))
          .withRight {
            emptyNode(A("R")).forSuperUser
              .withRight(emptyNode(A("R")))
              .withLeft(emptyNode(A("L")))
          }
          .withLeft {
            emptyNode(A("L")).forAdmin
              .withRight(emptyNode(A("R")))
              .withLeft(emptyNode(A("L")))
          }
      }

      .withLeft {
        emptyNode(A("L"))
          .withRight {
            emptyNode(A("R")).forAdmin
              .withRight(emptyNode(A("R")))
              .withLeft(emptyNode(A("L")))
          }
          .withLeft {
            emptyNode(A("L"))
              .withRight(emptyNode(A("R")))
              .withLeft(emptyNode(A("L")))
          }
      }


}

class SecurityTests extends FunSpec with Matchers {

  import TestUtils._
  import Fixture._

  describe("Nodes should") {
    it("work") {
      graph.reduce(User, A.empty).str shouldBe ":RLLLLLRL"
      graph.reduce(Admin, A.empty).str shouldBe ":RLLRLLRRLLRL"
      graph.reduce(SuperUser, A.empty).str shouldBe ":RRRLLRLLRRLLRL"
    }
  }

}
