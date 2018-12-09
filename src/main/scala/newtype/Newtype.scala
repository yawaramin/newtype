package newtype

import scala.reflect.ClassTag

/** @see [[newtype.Newtype.apply]] */
sealed trait Newtype[A] extends Serializable {
  type Type

  def apply(a: A): Type

  /** Extract the underlying value from the wrapped newtyped value. This
      could be done with an unapply method and a pattern match except
      for the Scala 2 bug, https://github.com/scala/bug/issues/9247 .
      Looks like that will be fixed in Dotty though. */
  def value(t: Type): A

  /** So that newtype values can be put in arrays. */
  def classTag: ClassTag[Type]
}

/** @see [[newtype.Newtype.apply]] */
object Newtype {

  /** Returns a new opaque value type that wraps a given base type, and
      conversions between the base and wrapper types. The wrapper doesn't
      'box' or allocate any new memory.

      @tparam A the base type to wrap.
      @param valid a rule that all wrapped values must obey. Useful for
        enforcing invariants like for refinement types, but with runtime
        checking. If omitted, is a rule that always passes.
      @param classTagA implicit evidence that the base type has a class
        tag.

      Usage:

      {{{
      val Id = Newtype[Long](0.<)
      type Id = Id.Type
      val bobId = Id(1) // : Id
      val bobIdLong = Id.value(bobId) // : Long = 1
      }}}

      Notice how the definition reads 'new type of Long', that's an
      intentional mnemonic. */
  def apply[A: ClassTag](valid: A => Boolean = (_: A) => true): Newtype[A] =
    new Make(valid)
}

private class Make[A](valid: A => Boolean)(implicit ev: ClassTag[A])
    extends Newtype[A] {
  type Type = A

  def apply(a: A): Type = {
    require(valid(a))
    a
  }

  def value(t: Type): A = t
  val classTag: ClassTag[Type] = ev
}
