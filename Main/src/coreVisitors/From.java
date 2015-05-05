package coreVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import tools.Map;
import ast.Ast.*;
import ast.ExpCore.ClassB;
import ast.ExpCore;

public class From extends CloneVisitor {
  private Path path;
  private From(Path path){  this.path=path;  }
  
  @Override public ExpCore visit(Path p){
    if(p.isPrimitive()){return p;}
    return fromP(p,path);
    }
  public ExpCore visit(ClassB s) {
    //return s.accept(new FromInClass(0,path.setNewOuter(path.outerNumber()+1).pushC("__IRRELEVANT__")));
    return FromInClass.of(s,path);
  }
  public static Path fromP(Path p0, Path source){
    if(p0.isPrimitive()){return p0;}
    LinkedList<String> cs0=new LinkedList<String>(p0.getRowData());
    LinkedList<String> cs1=new LinkedList<String>(source.getRowData());
    cs0.removeFirst();
    cs1.removeFirst();
    int n=p0.outerNumber();
    int m=source.outerNumber();
    int k=cs1.size();
    List<String> result=new ArrayList<String>();
    if(n<=k){
      result.add("Outer"+m);
      result.addAll(cs1.subList(0,cs1.size()-n));
      result.addAll(cs0);
    }
    else{
      result.add("Outer"+(m+n-k));
      result.addAll(cs0);
    }
  return new Path(result);
  }
  /*public static ExpCore of(ExpCore e, Path source){
    return e.accept(new From(source));
  }*/
  public static Type fromT(Type t, Path source){
    return new From(source).liftT(t);
  }
  public static List<ExpCore.ClassB.Member> from(List<ExpCore.ClassB.Member> ms, Path source){
    return Map.of(new From(source)::liftM, ms);
  }
  @SuppressWarnings("unchecked")
  public static <T extends ExpCore.ClassB.Member> T from(T member, Path source){
    return (T)new From(source).liftM(member);
  }
}
