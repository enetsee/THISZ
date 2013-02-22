package com.formalconcepts
package thisz
package haskell

import scalaz.Monad
import scalaz.EitherT
import scalaz.\/

import substitutions.Substitutions
import types.Types
import unification.Unification

trait HaskellTypes extends Types with HaskellKinds with Substitutions with Unification {

  type Type = HType

  sealed trait HType extends TypeLike
  final case class TVar(tyvar: Tyvar) extends HType
  final case class TCon(tycon: Tycon) extends HType
  final case class TAp(l: Type, r: Type) extends HType
  final case class TGen(n: Int) extends Type

  final case class Tyvar(id: String, kind: Kind)
  final case class Tycon(id: String, kind: Kind)
  def liftTyvar: Tyvar => Type = TVar(_)

  implicit def unifyType: Unify[Type] = new Unify[Type] {
    def mgu[M[+_]](e: Type, f: Type)(implicit ev: Monad[M]): EitherT[M, String, Subst] = (e, f) match {
      case (TAp(l, r), TAp(l2, r2)) => for {
        s1 <- mgu(l, l2)
        s2 <- mgu(applySubst(s1, r), applySubst(s1, r2))
      } yield (compose(s2, s1))

      case (TVar(u), t) => varBind(u, t)
      case (t, TVar(u)) => varBind(u, t)

      case (TCon(tc1), TCon(tc2)) if (tc1 == tc2) => EitherT.eitherT { ev.pure(\/.right(nullSubst)) }

      case (_, _) => EitherT.eitherT { ev.pure(\/.left(s"Types do not unify: ${e.toString} and ${f.toString}")) }
    }
  }
  
  

//  infixr      4 `fn`
//fn         :: Type -> Type -> Type
//a `fn` b    = TAp (TAp tArrow a) b
  
  // Built-in types
  val tUnit : Type = TCon(Tycon("()",KStar))
  val tChar : Type = TCon(Tycon("Char",KStar))
  val tInt : Type = TCon(Tycon("Int",KStar))
  val tInteger : Type = TCon(Tycon("Integer",KStar))
  val tFloat : Type = TCon(Tycon("Float",KStar))
  val tDouble : Type = TCon(Tycon("Double",KStar))

  val tList	: Type  = TCon(Tycon("[]",KFun(KStar,KStar)))
  val tArrow : Type  = TCon(Tycon("(->)",KFun(KStar,KFun(KStar,KStar))))
  
  val tTuple2 : Type = TCon(Tycon("(,)",KFun(KStar,KFun(KStar,KStar))))
  val tTuple3 : Type = TCon(Tycon("(,,)",KFun(KStar,KFun(KStar,KFun(KStar,KStar)))))
  val tTuple4 : Type = TCon(Tycon("(,,,)",KFun(KStar,KFun(KStar,KFun(KStar,KFun(KStar,KStar))))))
  val tTuple5 : Type = TCon(Tycon("(,,,,)",KFun(KStar,KFun(KStar,KFun(KStar,KFun(KStar,KFun(KStar,KStar)))))))
  val tTuple6 : Type = TCon(Tycon("(,,,,,)",KFun(KStar,KFun(KStar,KFun(KStar,KFun(KStar,KFun(KStar,KFun(KStar,KStar))))))))
  val tTuple7 : Type = TCon(Tycon("(,,,,,,)",KFun(KStar,KFun(KStar,KFun(KStar,KFun(KStar,KFun(KStar,KFun(KStar,KFun(KStar,KStar)))))))))


  val tString : Type = list(tChar)

  def list(t: Type) : Type = TAp(tList,t)
  def pair(e:Type,f:Type) : Type = TAp(TAp(tTuple2,e),f)
  
  
    
  // HasKind instances
  implicit val tyvarHasKind : HasKind[Tyvar] = new HasKind[Tyvar] {
    def kind : Tyvar => Kind = { case Tyvar(_,k) => k}
  }
  
  implicit val tyconHasKind: HasKind[Tycon] = new HasKind[Tycon] {
    def kind: Tycon => Kind = { case Tycon(_,k) => k}
  }
  
  implicit val typeHasKind : HasKind[Type] = new HasKind[Type] {
    def kind : Type => Kind = {
      case TCon(tc) => kindOf(tc)
      case TVar(v) => kindOf(v)
      case TAp(t,_) => kind(t) match {
        case KFun(_,k) => k
      }
    }
  }

}