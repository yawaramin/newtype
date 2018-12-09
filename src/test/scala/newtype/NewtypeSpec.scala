package newtype

import org.scalatest.refspec.RefSpec

class NewtypeSpec extends RefSpec {
  import NewtypeTest._

  def `preserves value` = assertResult(One)(Id.value(Id(One)))
  def `wrapper has identical memory layout to input value` =
    assertResult(bytes(One))(bytes(Id(One)))
  def `errors if invariants violated` =
    assertThrows[IllegalArgumentException](Id(Zero))
}

object NewtypeTest {
  val Zero = "0"
  val One = "1"
  val Id = Newtype[String](Zero.!=)

  // Thanks to https://stackoverflow.com/a/39371571/20371
  def bytes(any: Any): Array[Byte] = {
    import java.io.{ByteArrayOutputStream, ObjectOutputStream}

    val byteArrayOutputStream = new ByteArrayOutputStream
    val objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)

    objectOutputStream.writeObject(any)
    objectOutputStream.close()
    byteArrayOutputStream.toByteArray
  }
}
