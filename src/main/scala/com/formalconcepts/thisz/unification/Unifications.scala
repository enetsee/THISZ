package com.formalconcepts
package thisz
package unification

import collection.Traversable._

import scalaz.Monad
import scalaz.syntax.monad._
import scalaz.\/
import scalaz.EitherT

import kinds.Kinds
import types.Types
import substitutions.Substitutions


trait Unification {
  self: Kinds with Types with Substitutions with Unification =>

  trait Unify[T] {
    def mgu[M[+_]](e: T, f: T)(implicit ev: Monad[M]): EitherT[M, String, Subst]
  }

  def mgu[T](e: T, f: T)(implicit ev0: Unify[T]) = ev0.mgu(e, f)

  implicit def unifyTypes: Unify[Type]

  implicit def unifyList[T](implicit ev0: Unify[T], ev1: HasType[T]): Unify[List[T]] = new Unify[List[T]] {
    def mgu[M[+_]](e: List[T], f: List[T])(implicit ev: Monad[M]): EitherT[M, String, Subst] = (e, f) match {
      case ((x :: xs), (y :: ys)) => for {
        s1 <- ev0.mgu(x, y)
        s2 <- mgu(applySubst(s1, xs), applySubst(s1, ys))
      } yield compose(s1, s2)
      case (Nil, Nil) => EitherT.eitherT({ ev.pure(\/.right(nullSubst)) })
      case (_, _) => EitherT.eitherT({ ev.pure(\/.left("Lists do not unify.")) })
    }
  }
    
  def varBind[M[+_]](v: Tyvar, t: Type)(implicit ev: Monad[M]): EitherT[M, String, Subst] = EitherT.eitherT({
    if (t == (liftTyvar(v))) ev.pure(\/.right(nullSubst))
    else if (fTyvar(t) contains v) ev.pure(\/.left("Occurs check fails."))
    else if (kind(v) != kind(t)) ev.pure(\/.left("Kinds do not match."))
    else ev.pure(\/.right(List((v -> t))))
  })

  
  
  
  trait Match[T] {
    def onewayMatch[M[+_], T](e: T, f: T)(implicit ev: Monad[M]): EitherT[M, String, Subst]
  }

  implicit val matchTypes: Match[Type]
  
//  implicit def matchList[T](implicit ev0:Match[T]) : Match[List[T]] = new Match[List[T]] {
//    def onewayMatch[M[+_]](xs:List[T],ys:List[T])(implicit ev:Monad[M]) : EitherT[M,String,Subst] = for {
//      ss <- (xs.zip(ys).map({ case (x,y) => ev0.onewayMatch(x,y)})).sequence
//    } yield ( ss.foldM(nullSubst)({case (accu,s) => merge(accu,s) }))
//  
//  }
  
  

}