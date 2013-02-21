package com.formalconcepts
package thisz
package assumptions

import scalaz.Monad
import scalaz.EitherT
import scalaz.\/

import schemes.Schemes
import types.Types
import substitutions.Substitutions

trait Assumptions {
 self: Types with Substitutions with Schemes with Assumptions =>
   
   case class Assumption(id:String,sc:Scheme)

   
   implicit def assumptionHasType : HasType[Assumption]  = new HasType[Assumption] { 
     def applySub(s:Subst) = {case Assumption(id,sc) => Assumption(id,applySubst(s,sc))}
     def ftv : Assumption => Set[Tyvar] = {case Assumption(_,sc) => fTyvar(sc) }
   }
   
   def find[M[+_]](id:String,asms:List[Assumption])(implicit ev:Monad[M]) : EitherT[M,String,Scheme] =  asms match {
     case Nil => EitherT.eitherT { ev.pure(\/.left("Unbound identifier."))  }
     case  (Assumption(id2,sc)::as) => 
       if (id == id2) EitherT.eitherT { ev.pure(\/.right(sc)) } 
       else find(id,as)
   }
   
}
