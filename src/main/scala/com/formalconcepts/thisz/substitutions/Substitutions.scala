package com.formalconcepts
package thisz
package substitutions

import scalaz.Monad
import scalaz.syntax.monad._
import scalaz.\/
import scalaz.EitherT
import com.formalconcepts.thisz.types.Types

trait Substitutions {
  self: Substitutions with Types =>

  type Subst = List[(Tyvar,Type)]
 
  val nullSubst: Subst = List.empty

  def compose(s1: Subst, s2: Subst): Subst = 
    (for { (u,t) <- s2 } yield (u,applySubst(s1,t))) ++ s1
      
    
  def merge[M[+_]: Monad](s1: Subst, s2: Subst)(implicit ev:Monad[M]): EitherT[M,String,Subst] = {    
      val agree = s1.map(_._1).intersect(s2.map(_._1)).forall({ v => applySubst(s1,liftTyvar(v)) == applySubst(s2,liftTyvar(v))  })       
      EitherT.eitherT {
    	  if ( agree   ) ev.pure(\/.right(s1 ++ s2))
    	  else ev.pure(\/.left("Substitutions do not agree."))
      }
  }
   
  
}