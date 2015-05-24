package coreVisitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import tools.Map;
import facade.L42;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.*;
import ast.Util.PathMxMx;
import ast.Util.PathPath;
import auxiliaryGrammar.Functions;

public class CollectPrivateNames extends CloneVisitor{
  public final List<PathMxMx> mapMx=new ArrayList<>();
  public final List<PathPath> mapPath=new ArrayList<>();
  List<String> cs=new ArrayList<>();
  public static CollectPrivateNames of(ExpCore e){
    CollectPrivateNames cdv=new CollectPrivateNames();
    e.accept(cdv);
    return cdv;
  }
  /*public ExpCore visit(ClassB s) {
    if(isPrivate(s.getDoc2())){
      if(!(s.getH() instanceof Ast.ConcreteHeader)){return super.visit(s);}
      Ast.ConcreteHeader h=(Ast.ConcreteHeader)s.getH();
      
      List<String>names=new ArrayList<>();
      List<String>namesPr=new ArrayList<>();
     for(FieldDec f:h.getFs()){
       names.add(f.getName());
       namesPr.add(Functions.freshName(f.getName(),L42.usedNames));
       }
      String name=h.getName();
      String namePr=Functions.freshName(h.getName(),L42.usedNames);
      //constructor
      mapMx.add(new PathMxMx(Path.outer(0, cs),
          new MethodSelector(name,names),
          new MethodSelector(namePr,namesPr)
          ));
      {int i=-1;for(String ni:names){i+=1;
        String npi=namesPr.get(i);
        boolean isVari=h.getFs().get(i).isVar();
        //getter
        mapMx.add(new PathMxMx(Path.outer(0, cs),
            new MethodSelector(ni,Collections.emptyList()),
            new MethodSelector(npi,Collections.emptyList())
            ));
        //exposer
        mapMx.add(new PathMxMx(Path.outer(0, cs),
            new MethodSelector("#"+ni,Collections.emptyList()),
            new MethodSelector("#"+npi,Collections.emptyList())
            ));
        //setter
        if(isVari){
          mapMx.add(new PathMxMx(Path.outer(0, cs),
              new MethodSelector(ni,new ArrayList<>(Arrays.asList("that"))),
              new MethodSelector(npi,new ArrayList<>(Arrays.asList("that")))
              ));
        }
      }}      
      }
    return super.visit(s);
  }*/
  public NestedClass visit(NestedClass nc){
    cs.add(nc.getName());
    try{
      if(nc.getDoc().isPrivate()){
        Path name=Path.outer(0, cs);
        Path privateName=Functions.freshPathName(name,L42.usedNames);
        //Path privateName=name.popC().pushC(_privateName);
        mapPath.add(new PathPath(name,privateName));
      }
      return  super.visit(nc);
      }
    finally{
      cs.remove(cs.size()-1);
      }
    }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    if(!mt.getDoc().isPrivate()){ return super.visit(mt);}
    mapMx.add(new PathMxMx(Path.outer(0, cs),mt.getMs(),usePrivateNames(mt.getMs())));
    return super.visit(mt);    
    }
  private MethodSelector usePrivateNames(MethodSelector ms) {
    String name=ms.getName();
    String privateName=Functions.freshName(name,L42.usedNames);
    List<String> ns=new ArrayList<>();
    for( String ni:ms.getNames()){ns.add(Functions.freshName(ni,L42.usedNames));}
    return new MethodSelector(privateName,ns);
  }
  //TODO: case for contructor  
  
}
