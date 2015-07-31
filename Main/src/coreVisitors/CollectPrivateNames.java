package coreVisitors;

import introspection.ConsistentRenaming;

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
  public NestedClass visit(NestedClass nc){
    cs.add(nc.getName());
    try{
      if(nc.getDoc().isPrivate()){
        Path name=Path.outer(0, cs);
        Path privateName=Functions.freshPathName(name,L42.usedNames);
        if(!name.equals(privateName)){//depending on how we clean up private names, == can happens
          mapPath.add(new PathPath(name,privateName));
        }
      }
      return  super.visit(nc);
      }
    finally{
      cs.remove(cs.size()-1);
      }
    }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    // consistent names are collected later
    if(!mt.getDoc().isPrivate() || ConsistentRenaming.isAnnotatedConsistent(mt.getDoc())){ return super.visit(mt);}
    MethodSelector msPr = usePrivateNames(mt.getMs());
    if(!msPr.equals(mt.getMs())){//depending on how we clean up private names, == can happens
      mapMx.add(new PathMxMx(Path.outer(0, cs),mt.getMs(),msPr));
    }
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
