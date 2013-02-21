package com.formalconcepts
package thisz
package haskell

import schemes.Schemes
import assumptions.Assumptions
import ast.ASTs

trait HaskellAST extends ASTs  with HaskellLiteral with HaskellPattern {
  self: HaskellAST with Schemes with Assumptions =>
  type AST = BindGroup
  
  type BindGroup  = (List[Expl], List[List[Impl]])
  
  type Expl = (String, Scheme, List[Alt])
  type Impl = (String, List[Alt])
  
  type Alt = (List[Pat], Expr)
  
  
  sealed trait Expr
  case class Var(id:String) extends Expr
  case class Lit(lit:Literal) extends Expr
  case class Const(asm: Assumption) extends Expr
  case class Ap(e:Expr,f:Expr) extends Expr
  case class Let(bg:BindGroup,body:Expr) extends Expr
  case class Lam(alt:Alt) extends Expr
  case class If(cond:Expr,e:Expr,f:Expr) extends Expr
  case class Case(scr:Expr,cases:List[(Pat,Expr)]) extends Expr


}