# Newtype

Named after the [Haskell Newtype](https://wiki.haskell.org/Newtype), a
Scala module to help creating non-allocating wrapper types for existing
types, if you're looking for an alternative to `AnyVal` and the
'tagging' technique introduced by Scalaz and others.

## Use

Define a new wrapper:

```scala
import newtype.Newtype

object Types {
  val Id = Newtype[Long](0.<)
  type Id = Id.Type
}
```

The `Newtype.apply` smart constructor provides a module containing the
newly-minted type and a set of operations to create and extract values
of the type:

```scala
object TypesTest {
  import Types._

  /* The constraint will prevent creating illegal values:
  val invalidId = Id(0) */

  val bobId: Id = Id(1)
  val 1L = Id.value(bobId)
}
```

## Test

    ./gradlew --build-cache -t test
