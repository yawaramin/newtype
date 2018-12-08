package newtype

import org.scalatest.refspec.RefSpec

class NewtypeSpec extends RefSpec {
  import NewtypeTest._

  def `preserves value` {
    val one = 1
    assertResult(one)(Id.value(Id(one)))
  }

  def `errors if invariants violated` {
    assertThrows[IllegalArgumentException](Id(Zero))
  }
}

object NewtypeTest {
  val Zero = 0L
  val Id = Newtype[Long](Zero.<)
}
