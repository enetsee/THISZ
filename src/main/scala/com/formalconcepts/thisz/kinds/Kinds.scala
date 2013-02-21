package com.formalconcepts
package thisz
package kinds

import scalaz.Show

trait Kinds {

  type Kind <: KindLike

  trait KindLike

  implicit val showKind: Show[Kind]
    
  trait HasKind[T] {
    val kind: T => Kind
  }
  
  def kind[T](x:T)(implicit ev:HasKind[T]) = ev.kind(x)
}
