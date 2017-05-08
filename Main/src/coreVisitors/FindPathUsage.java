package coreVisitors;

import java.util.Collections;
import java.util.List;

import ast.Ast;
import ast.Ast.Type;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import programReduction.Program;

public class FindPathUsage extends CloneVisitorWithProgram{
  public FindPathUsage(Program p,Path pi) {
    super(p);
    searchingFor=pi;
    }
Ast.Position located=null;
  Path searchingFor;
  public static Ast.Position _locate(Program p,ClassB cb, Path pi){
    FindPathUsage fpu=new FindPathUsage(p,pi);
    cb.accept(fpu);
    return fpu.located;
  } 
  public ExpCore visit(ClassB s) {
    Path oldP=searchingFor;
    searchingFor=searchingFor.setNewOuter(searchingFor.outerNumber()+1);
    try{return super.visit(s);}
    finally{searchingFor=oldP;}
    }
  public ExpCore visit(ExpCore.EPath s) {
    if(s.toString().endsWith("SafeOperatorsAccess")){
      System.out.println(s);
    }
    if (p.equiv(s.getInner(),searchingFor)){
      this.located=s.getP();
      }
    return s;
    //add using, type... type has no pos..
    }
  public Type liftT(Type s) {
  Path inner=s.match(nt->nt.getPath(), hType->hType.getPath());
  if (p.equiv(inner,searchingFor)){
    if (getLastCMs()==null){
      this.located=p.top().getP();
      return s;
      }
    if (getLastCMs()instanceof Ast.C){
      List<Ast.C> cs=Collections.singletonList((Ast.C)getLastCMs());
      this.located=p.top().getNested(cs).getP();
      return s;
      }
    if (getLastCMs()instanceof Ast.MethodSelector){
      Member m=p.top()._getMember((Ast.MethodSelector) getLastCMs());
      assert m!=null;
      this.located=m.getP();
      return s;
      }    
    }
  return s;
  //add using, type... type has no pos..
  }
  }
