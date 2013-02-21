package com.formalconcepts
package thisz
package predicates

import kinds.Kinds
import types.Types
import substitutions.Substitutions
import unification.Unification

trait Predicates {
  self: Kinds with Types with Substitutions with Predicates with Unification =>
  type Predicate <: PredicateLike
  trait PredicateLike

  case class Qualified[T](ps: List[Predicate], x: T)

  implicit def qualifiedHasType[T](implicit ev: HasType[T]): HasType[Qualified[T]] = new HasType[Qualified[T]] {
    
    def applySub(s: Subst) : Qualified[T]  => Qualified[T] = {
      case Qualified(ps, t) => Qualified(applySubst(s,ps), applySubst(s,t))
    }

    def ftv : Qualified[T] => Set[Tyvar]  = { case Qualified(ps, t) => fTyvar(ps) union fTyvar(t) }
  }

  
  
  implicit def predHasType: HasType[Predicate]
  implicit def uniftPred: Unify[Predicate]
  implicit def matchPred: Match[Predicate]

}