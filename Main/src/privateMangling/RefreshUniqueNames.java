package privateMangling;

import ast.ExpCore;
import ast.Expression;
import ast.Expression.ClassB.MethodWithType;
import ast.Expression.ClassB.NestedClass;
import facade.L42;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;



public class RefreshUniqueNames {
  public static long newN(HashMap<Long,Long> map, long n){
    if(n==-1L){return n;}
    Long newN=map.get(n);
    if(newN==null){
      map.put(n, newN=L42.freshPrivate());
      }
    return newN;
  }
  public static long mappedN(HashMap<Long,Long> map, long n){
    if(n==-1L){return n;}
    Long newN=map.get(n);
    if(newN==null){ return n;}
    return newN;
    }

  public static ClassB refreshTopLevel(ClassB e){
    HashMap<Long,Long> map=new HashMap<>();
    return (ClassB)e.accept(new coreVisitors.CloneVisitor(){
      protected MethodSelector liftMs(MethodSelector ms){
        return ms.withUniqueNum(newN(map,ms.getUniqueNum()));
        }
      public ClassB.NestedClass visit(ClassB.NestedClass nc){
        long newN=newN(map,nc.getName().getUniqueNum());
        return super.visit(nc.withName(nc.getName().withUniqueNum(newN)));
        //I need to collect the DECLARED C,s and those are the only that I need to refresh.
        //refresh all can work only at top level
        }
      @Override protected Path liftP(Path s) {
        if(s.isPrimitive()){return s;}
        List<C> cs = s.getCBar();
        List<C> newCs =new ArrayList<>();
        for(C c:cs){ newCs.add(c.withUniqueNum(newN(map,c.getUniqueNum()))); }
        return Path.outer(s.outerNumber(),newCs);
        }
    });
    }
  
@SuppressWarnings("unchecked")
public static <T extends ExpCore>T refresh(T e){
  HashMap<Long,Long> map=new HashMap<>();
  //load up maps
  e.accept(new coreVisitors.PropagatorVisitor(){
    public void visit(ClassB.MethodWithType mwt){
      MethodSelector ms=mwt.getMs();
      newN(map,ms.getUniqueNum());
      super.visit(mwt);
      return;
      }
    public void visit(ClassB.NestedClass nc){
      newN(map,nc.getName().getUniqueNum());
      super.visit(nc);
      }
    });
  return (T)e.accept(new coreVisitors.CloneVisitor(){
  protected MethodSelector liftMs(MethodSelector ms){
    return ms.withUniqueNum(mappedN(map, ms.getUniqueNum()));
    }
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    C name=nc.getName().withUniqueNum(mappedN(map, nc.getName().getUniqueNum()));
    return super.visit(nc.withName(name));
    }
  @Override protected Path liftP(Path s) {
    if(s.isPrimitive()){return s;}
    List<C> cs = s.getCBar();
    List<C> newCs =new ArrayList<>();
    for(C c:cs){
      newCs.add(c.withUniqueNum(mappedN(map, c.getUniqueNum())));
      }
    return Path.outer(s.outerNumber(),newCs);
    }
  });
}

public static long maxUnique(Expression e){
  HashMap<Long,Long> map=new HashMap<>();
  //load up maps
  e.accept(new sugarVisitors.PropagatorVisitor(){
    @Override public void visit(MethodWithType mwt){
      MethodSelector ms=mwt.getMs();
      newN(map,ms.getUniqueNum());
      super.visit(mwt);
      }
    @Override public void visit(NestedClass nc){
      newN(map,nc.getName().getUniqueNum());
      super.visit(nc);
      }
    });
    return map.keySet().stream().max((a,b)->a>b?1:-1).orElse(0L);
  }
}


/*

Discussions: 
state/factory/constructors

make private an abstract should be error?

make state private on class:
  requires class to be coherent?
  all abstract methods are made unique, 
  all ks/fields are renamed synchronized.
  further refreshing need to keep them sincronized,
    but is possible since abstract and you can distinguish them
    plus abstract has no body, so renaming parameters does not mess in the implementation
    but, what for factory calls?
      since we map to new selectors, should be ok?
    

What if:
-every class can declare 0..n uniqueNum and use them
no other class can use such nums

todo
-sugarVisitor.collect uniqueDecs
-in desugar, call collect +call refreshTop for reuse

*/