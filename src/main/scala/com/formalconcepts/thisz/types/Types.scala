package com.formalconcepts
package thisz
package types

import scalaz.Show

import kinds.Kinds
import substitutions.Substitutions

trait Types {
  self: Types with Kinds with Substitutions =>

  type Type <: TypeLike

  trait TypeLike
  
  type Tyvar
  def liftTyvar : Tyvar => Type
  
     
  trait HasType[T] {
    def applySub(s: Subst): (T => T)
    def ftv: (T => Set[Tyvar])
  }

  def fTyvar[T](x: T)(implicit ev: HasType[T]): Set[Tyvar] = ev.ftv(x)
  def applySubst[T](s: Subst, x: T)(implicit ev: HasType[T]): T = ev.applySub(s)(x)
  
  
  implicit def listHasType[T](implicit ev: HasType[T]) : HasType[List[T]] = new HasType[List[T]] {
    def applySub(s:Subst) = _.map(ev.applySub(s))
    def ftv = _.foldLeft(Set.empty[Tyvar])({ case (accu,x) => accu union ev.ftv(x) })
  }
  
  implicit def showType: Show[Type]
  implicit def tyvarHasKind: HasKind[Tyvar]
  implicit def typeHasKind: HasKind[Type]
  implicit def typeHasTypes: HasType[Type]
  

}

