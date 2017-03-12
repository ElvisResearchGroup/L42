package coreVisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import tools.Map;
import ast.Ast;
import ast.ExpCore;
import ast.Ast.FieldDec;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.WalkBy;
import auxiliaryGrammar.Functions;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
public class RemoveMethod extends CloneVisitor{
  private List<Ast.C> path;
  private MethodSelector ms;
  public RemoveMethod(Path path, MethodSelector ms) {
    this.path = new ArrayList<>(path.getCBar());this.ms=ms;
    }  
  public List<Member> liftMembers(List<Member> s) {
    if(!path.isEmpty()){return Map.of(this::liftM,s);}
    List<Member> result=new ArrayList<>(s);
    Optional<Member> mOpt =Functions.getIfInDom(s,ms);
    if( mOpt.isPresent()){
      Member m=mOpt.get();
      result.remove(m);
      }
    return result;
  }
  
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    if(!path.get(0).equals(nc.getName())){
      return nc;
      }
    List<Ast.C> aux=new ArrayList<>(path);
    path.remove(0);
    try{return super.visit(nc);}
    finally{path=aux;}
    }
  public static ClassB of(ClassB s,Path path, MethodSelector ms) {
    RemoveMethod rm=new RemoveMethod(path, ms);
    assert path.outerNumber()==0;
    return (ClassB)s.accept(rm);
  }
}