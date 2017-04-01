package coreVisitors;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import tools.Assertions;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import programReduction.Program;
import auxiliaryGrammar.Functions;

public class ExtractThrow implements Visitor<ExpCore>{
  public static ExpCore of(Program p,ExpCore e) {
    return e.accept(new ExtractThrow(p));
  }
  Program p;
  ExtractThrow(Program p){this.p=p;}

  public ExpCore visit(Signal s) {
    if(IsValue.of(p, s.getInner())){return s;}
    return s.getInner().accept(this);
  }

  public ExpCore visit(MCall s) {
    if(!IsValue.isAtom(s.getInner())){return s.getInner().accept(this);}
    for(ExpCore ei:s.getEs()){
      if(IsValue.isAtom(ei)){continue;}
      return ei.accept(this);
        }
    return new ExpCore.WalkBy();
    }
  public ExpCore visit(Using s) {
    for(ExpCore ei:s.getEs()){
      if(IsValue.isAtom(ei)){continue;}
      return ei.accept(this);
        }
    return new ExpCore.WalkBy();
    }

  public ExpCore visit(Block s) {
    if(!s.getOns().isEmpty()){return new ExpCore.WalkBy();}
    for(int i=0;i<s.getDecs().size();i++){
      if(new IsValue(p).validDv(s.getDecs().get(i))){continue;}
      //otherwise, i is the first non dv
      ExpCore res=s.getDecs().get(i).getInner().accept(this);
      if(res instanceof ExpCore.WalkBy){return res;}
      Signal res_=(Signal)res;
      List<Block.Dec> decs = s.getDecs().subList(0, i);
      Block newInner=new Block(s.getDoc(),decs,res_.getInner(),Collections.emptyList(),s.getP());
      newInner=Functions.garbage(newInner,i);
      return res_.withInner(newInner);
      }
      ExpCore res=s.getInner().accept(this);
      if(res instanceof ExpCore.WalkBy){return res;}
      Signal res_=(Signal)res;
      Block newInner=s.withInner(res_.getInner());
      return res_.withInner(newInner);
    }

  public ExpCore visit(ClassB s)  { return new ExpCore.WalkBy();}
  public ExpCore visit(ExpCore.EPath s) { return new ExpCore.WalkBy();}
  public ExpCore visit(X s) { return new ExpCore.WalkBy();}
  public ExpCore visit(_void s) { return new ExpCore.WalkBy();}
  public ExpCore visit(WalkBy s) {throw Assertions.codeNotReachable();}
  public ExpCore visit(Loop s) { return new ExpCore.WalkBy(); }
  }