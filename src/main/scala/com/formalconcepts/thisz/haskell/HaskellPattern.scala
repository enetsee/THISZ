package com.formalconcepts
package thisz
package haskell

import assumptions.Assumptions

trait HaskellPattern {
self : HaskellAST with Assumptions with HaskellLiteral with HaskellPattern =>
  
  sealed trait Pat
  case class PVar(id:String) extends Pat
  case object PWildcard extends Pat
  case class PAs(id:String,pat:Pat) extends Pat
  case class PLiteral(lit:Literal) extends Pat
  case class PNpk(id:String,n:Int) extends Pat
  case class PCon(as:Assumption,pats:List[Pat]) extends Pat
  case class PLazy(pat:Pat) extends Pat
                
}