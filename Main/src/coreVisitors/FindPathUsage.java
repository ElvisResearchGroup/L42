package coreVisitors;

import java.util.Collections;
import java.util.List;

import ast.Ast;
import ast.Ast.NormType;
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
  public static Ast.Position _locate(Program p,ExpCore e, Path pi){
    assert IsCompiled.of(e);
    FindPathUsage fpu=new FindPathUsage(p,pi);
    e.accept(fpu);
    return fpu.located;
  } 
  public ExpCore visit(ClassB s) {
    Path oldP=searchingFor;
    searchingFor=searchingFor.setNewOuter(searchingFor.outerNumber()+1);
    try{return super.visit(s);}
    finally{searchingFor=oldP;}
    }
  public void locate(Path path,Ast.Position pos){
    if (p.equiv(path,searchingFor)){
      this.located=pos;
      }
    }
  public ExpCore visit(ExpCore.EPath s) {
    locate(s.getInner(),s.getP());
    return s;
    }
  public ExpCore visit(ExpCore.Using s) {
  locate(s.getPath(),ctxPos());
  return super.visit(s);
  }

  public NormType liftT(NormType s) {
  Path inner=s.getPath();
  if(s.toString().endsWith("Location")){
    System.out.println(s);
    }
  Ast.Position pos = ctxPos();
  locate(inner,pos);  return s;
  //add using
  }
private Ast.Position ctxPos() {
Ast.Position pos=p.top().getP();
  if (getLastCMs()!=null){
  if (getLastCMs()instanceof Ast.C){
    List<Ast.C> cs=Collections.singletonList((Ast.C)getLastCMs());
    pos=p.top().getNested(cs).getP();
    }
  if (getLastCMs()instanceof Ast.MethodSelector){
    Member m=p.top()._getMember((Ast.MethodSelector) getLastCMs());
    assert m!=null;
    pos=m.getP();
    }
  }
return pos;
}
  }
