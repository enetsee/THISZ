package com.formalconcepts
package thisz
package haskell

import scalaz.Show
import com.formalconcepts.thisz.kinds.Kinds

trait HaskellKinds extends Kinds {
  
  type Kind = HKind
  
  sealed trait HKind extends KindLike
  case object KStar extends HKind { override def toString() = "*" }
  case class  KFun(e:HKind,f:HKind) extends HKind { override def toString() = s"${e.toString}->${f.toString}"}
  
  implicit val showKind : Show[Kind] = Show.showA
  
  
}