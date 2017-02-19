package privateMangling;

import ast.ExpCore;
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
  public static C newC(HashMap<C,C> mapC, C c){
    if(!c.isUnique()){return c;}
    C newC=mapC.get(c);
    if(newC==null){
      mapC.put(c, newC=c.withUniqueNum(L42.freshPrivate()));
      }
    return newC;
  }
  public static ClassB refreshTopLevel(ClassB e){
    HashMap<MethodSelector,MethodSelector> mapS=new HashMap<>();
    HashMap<C,C> mapC=new HashMap<>();
    return (ClassB)e.accept(new coreVisitors.CloneVisitor(){
      protected MethodSelector liftMs(MethodSelector ms){
        if(!ms.isUnique()){return ms;}
        MethodSelector newMs=mapS.get(ms);
        if(newMs==null){mapS.put(ms,newMs=ms.withUniqueNum(L42.freshPrivate()));}
        return newMs;
        }
      public ClassB.NestedClass visit(ClassB.NestedClass nc){
        C name=newC(mapC,nc.getName());
        return super.visit(nc.withName(name));
        //I need to collect the DECLARED C,ms and those are the only that I need to refresh.
        //refresh all can work only at top level
        }
      public ExpCore visit(Path s) {
        if(s.isPrimitive()){return s;}
        List<C> cs = s.getCBar();
        List<C> newCs =new ArrayList<>();
        for(C c:cs){ newCs.add(newC(mapC,c)); }
        return Path.outer(s.outerNumber(),newCs);
        }
    });
    }
  
  public static ExpCore refresh(ExpCore e){
  HashMap<MethodSelector,MethodSelector> mapS=new HashMap<>();
  HashMap<C,C> mapC=new HashMap<>();
  //load up maps
  e.accept(new coreVisitors.PropagatorVisitor(){
    public void visit(ClassB.MethodWithType mwt){
      MethodSelector ms=mwt.getMs();
      if(!ms.isUnique()){super.visit(mwt);return;}
      MethodSelector newMs=mapS.get(ms);
      if(newMs==null){mapS.put(ms,newMs=ms.withUniqueNum(L42.freshPrivate()));}
      super.visit(mwt);
      return;
      }
    public void visit(ClassB.NestedClass nc){
      C name=newC(mapC,nc.getName());
      super.visit(nc);
      }
    });
  return (ClassB)e.accept(new coreVisitors.CloneVisitor(){
  protected MethodSelector liftMs(MethodSelector ms){
    if(!ms.isUnique()){return ms;}
    MethodSelector newMs=mapS.get(ms);
    if(newMs!=null){return newMs;}
    return ms;
    }
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    if(!nc.getName().isUnique()){return super.visit(nc);}
    C name=mapC.get(nc.getName());
    if(name!=null){
      return super.visit(nc.withName(name));
      }
    return super.visit(nc);
    }
  public ExpCore visit(Path s) {
    if(s.isPrimitive()){return s;}
    List<C> cs = s.getCBar();
    List<C> newCs =new ArrayList<>();
    for(C c:cs){
      if(!c.isUnique()){newCs.add(c);continue;}
      C newC=mapC.get(c);
      if(newC==null){newCs.add(c);continue;}
      newCs.add(newC); 
      }
    return Path.outer(s.outerNumber(),newCs);
    }
});
}
  }

