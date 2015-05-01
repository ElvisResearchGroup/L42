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
  private int j;//with -1 as a special "useFromP for first level"
  private Path path;
  private From(int j,Path path){
    this.j=j;this.path=path;
    }
  @Override public ExpCore visit(Path p){
    if(p.isPrimitive()){return p;}
    if(j==-1){return fromP(p,path);}
    int n=p.outerNumber();
    if(n<j){return p;}
    Path nLessJ=addOuter(-j,p);
    nLessJ=fromP(nLessJ,path);
    Path result= addOuter(j,nLessJ);
    return result;
    }
  public ExpCore visit(ClassB s) {
    int oldJ=this.j;
    if(this.j<0){this.j=1;}
    //TODO: looks crazy, in formalism, we have  e[from path] and e[from path]_i
    //and they should be two different visitors in the code!
    //with a local inner class should be simple to do e[from path] locally
    else{this.j+=1;}
    try{return super.visit(s);}
    finally{this.j=oldJ;}
  }
  private static Path addOuter(int k,Path p){
    List<String> inner=new ArrayList<String>(p.getRowData());
    int newOuters=p.outerNumber()+k;
    assert newOuters>=0;
    inner.set(0,"Outer"+newOuters);
    return new Path(inner);
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
  public static ExpCore from(ExpCore e, Path source){
    return e.accept(new From(0,source));
  }
  
  /*public static ClassB fromClass(ClassB cb, Path source){
    return (ClassB)cb.accept(new From(-1,source));
    }*///NO! never needed
  
  public static Type fromT(Type t, Path source){
    return new From(0,source).liftT(t);
  }
  public static List<ExpCore.ClassB.Member> from(List<ExpCore.ClassB.Member> ms, Path source){
    return Map.of(new From(-1,source)::liftM, ms);
    //NO, subtile bug for Outer0
    //ExpCore.ClassB cb=new ExpCore.ClassB("","",new ast.Ast.TraitHeader(),Collections.emptyList(),ms,ast.Ast.Stage.None,Collections.emptyList());
    //cb=(ExpCore.ClassB)cb.accept(new From(-1,source));
    //cb=(ExpCore.ClassB)from(cb,source);
    //return cb.getMs();
  }
  @SuppressWarnings("unchecked")
  public static <T extends ExpCore.ClassB.Member> T from(T member, Path source){
    return (T)new From(-1,source).liftM(member);
  }
}
