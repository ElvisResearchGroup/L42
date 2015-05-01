package coreVisitors;

import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;

public class IsCompiled extends CloneVisitor{
  private static class Found extends RuntimeException{}
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    if(nc.getInner() instanceof ClassB){
      return super.visit(nc);
      }
    throw new Found();
    }
  private IsCompiled(){}
  public static boolean of(ExpCore e){
    try{e.accept(new IsCompiled());return true;}
    catch(Found f){return false;}
  }
  public static boolean of(Member m){
    try{
      IsCompiled c=new IsCompiled();
      m.match(c::visit,c::visit, c::visit);
      return true;
      }
    catch(Found f){return false;}
  }

}
