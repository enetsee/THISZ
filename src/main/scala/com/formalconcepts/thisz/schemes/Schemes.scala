package com.formalconcepts
package thisz
package schemes

import kinds.Kinds
import types.Types
import substitutions.Substitutions
import predicates.Predicates

trait Schemes {
  self: Kinds with Types with Substitutions with Predicates with Schemes =>

  sealed trait Scheme
  case class Forall(ks: List[Kind], qt: Qualified[Type]) extends Scheme

  implicit def schemaHasType: HasType[Scheme] = new HasType[Scheme] {
    def applySub(s: Subst) = {
      case Forall(ks, qt) => Forall(ks, applySubst(s, qt))
    }

    def ftv = { case Forall(ks, qt) => fTyvar(qt) }
  }

  implicit def toScheme(t: Type): Scheme = Forall(List.empty, Qualified(List.empty, t))
  def quantify(vs: List[Tyvar], qt: Qualified[Type]): Scheme

}