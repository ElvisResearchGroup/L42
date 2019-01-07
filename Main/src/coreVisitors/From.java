package coreVisitors;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tools.Map;
import ast.Ast.*;
import ast.ExpCore.ClassB;
import ast.Ast;
import ast.ExpCore;

public class From extends CloneVisitor {
  private Path path;
  private From(Path path){  this.path=path;  }
  @Override protected Path liftP(Path p){
    if(p.isPrimitive()){return p;}
    return fromP(p,path);
    }
  public ExpCore visit(ClassB s) {
    //return s.accept(new FromInClass(0,path.setNewOuter(path.outerNumber()+1).pushC("__IRRELEVANT__")));
    return FromInClass.of(s,path);
  }
  public static Path normalizeShort(Path p0,List<Ast.C>explored){
    if(p0.isPrimitive()){return p0;}
    int n=Math.min(p0.outerNumber(),explored.size());
    if(n==0){return p0;}
    List<Ast.C> cs = p0.getCBar();
    n=Math.min(n,cs.size());
    int i=0;
    while(i<n){
      if (cs.get(i).equals(explored.get(i))){ i++;}
      else{break;}
    }
    return Path.outer(p0.outerNumber()-i,cs.subList(i,cs.size()));
  }

  public static Path fromP(Path p0, Path source){
    if(p0.isPrimitive()){return p0;}
    LinkedList<Ast.C> cs0=new LinkedList<>(p0.getCBar());
    LinkedList<Ast.C> cs1=new LinkedList<>(source.getCBar());
    int n=p0.outerNumber();
    int m=source.outerNumber();
    int k=cs1.size();
    List<Ast.C> result=new ArrayList<>();
    int resN=0;
    if(n<=k){
      resN=m;
      result.addAll(cs1.subList(0,cs1.size()-n));
      result.addAll(cs0);
    }
    else{
      resN=m+n-k;
      result.addAll(cs0);
    }
  return Path.outer(resN,result);
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

  public static MethodType from(MethodType mt,Path source){
    return new From(source).liftMT(mt);
    }
  @SuppressWarnings("unchecked")
  public static <T extends ExpCore.ClassB.Member> T from(T member, Path source){
    return (T)new From(source).liftM(member);
  }
  @SuppressWarnings("unchecked")
  public static <T extends ExpCore> T from(T exp, Path source){
    // This cast is safe, since we never change the type of anything
    return (T)exp.accept(new From(source));
  }

}
