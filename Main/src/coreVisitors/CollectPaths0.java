package coreVisitors;

import java.util.ArrayList;
import java.util.List;
import tools.Map;
import ast.ExpCore;
import ast.Ast.*;
import ast.ExpCore.ClassB;

public class CollectPaths0 extends CloneVisitor{

  List<Path> paths=new ArrayList<Path>();
  public static List<Path> of(ExpCore e){
    CollectPaths0 cp=new CollectPaths0();
    e.accept(cp);
    return cp.paths;
  }
  public static List<Path> of(ClassB.MethodWithType mwt){
    CollectPaths0 cp=new CollectPaths0();
    cp.visit(mwt);
    return cp.paths;
  }
  public ExpCore visit(Path s) { 
    if(!s.isPrimitive()){paths.add(s);}
    return s;
    }
  public ExpCore visit(ClassB s) {return s;}  
}
