package com.formalconcepts.thisz.haskell


trait HaskellLiteral {
	self: HaskellAST with HaskellLiteral =>
	  
	sealed trait Literal
	case class LitInt(i:Int) extends Literal 
	case class LitChar(c:Char) extends Literal 
	case class LitRat(r:math.BigDecimal) extends Literal 
	case class LitStr(s:String) extends Literal 

}