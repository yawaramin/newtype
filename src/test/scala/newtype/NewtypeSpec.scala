package newtype

import org.scalatest.refspec.RefSpec

class NewtypeSpec extends RefSpec {
  import NewtypeSpec._

  /* This proves that putting the wrapped values into an array doesn't
     box them. */
  def `array of wrapped values has identical memory layout to array of input values` =
    assertResult(bytes(Array(One)))(bytes(Array(idOne)))
  def `errors if invariants violated` =
    assertThrows[IllegalArgumentException](Id(Zero))
  def `wrapper has identical memory layout to input value` =
    assertResult(bytes(One))(bytes(idOne))
  def `wrapper is type-incompatible with input value` =
    assertTypeError("idOne.capitalize")
}

object NewtypeSpec {
  import scala.reflect.ClassTag

  val Zero = "0"
  val Id: Newtype[String] = Newtype(Zero.!=)
  type Id = Id.Type

  // Thanks to https://stackoverflow.com/a/39371571/20371
  def bytes(any: Any): Array[Byte] = {
    import java.io.{ByteArrayOutputStream, ObjectOutputStream}

    val byteArrayOutputStream = new ByteArrayOutputStream
    val objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)

    objectOutputStream.writeObject(any)
    objectOutputStream.close()
    byteArrayOutputStream.toByteArray
  }

  implicit val classTagId: ClassTag[Id] = Id.classTag
  val One = "1"
  val idOne = Id(One)
}
