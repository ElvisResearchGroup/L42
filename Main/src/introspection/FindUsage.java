package introspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import tools.Map;
import ast.Ast;
import ast.Ast.MethodSelectorX;
import ast.ExpCore;
import ast.Ast.MethodSelector;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.MCall;
import ast.ExpCore.Block.Catch;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.PathMx;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import coreVisitors.GuessTypeCore;

public class FindUsage extends MethodPathCloneVisitor {
  Set<PathMx> used=new HashSet<PathMx>();
  List<PathMx> pMxs;
  FindUsage(Program p,List<PathMx> pMxs) {
    super(p);
    this.pMxs=pMxs;
  }
  @Override public MethodSelector visitMS(MethodSelector original,Path src){
      assert src!=null;
      List<PathMx> filtered=new ArrayList<>();
      for(PathMx pMx:pMxs){
        if(original.equals(pMx.getMs())){filtered.add(pMx);}
      }
      if(filtered.isEmpty()){return original;}
      for(PathMx pMx:filtered){
          Path path=Norm.of(p,pMx.getPath());
          if(!equalOrSubtype(src,path)){continue;}
          used.add(pMx);
          }
        return original;
    }
  private boolean equalOrSubtype(Path guessed, Path path) {
   if(guessed.equals(path)){return true;}
   ClassB ct=p.extract(guessed);
   List<Path> sup = ct.getSupertypes();
   sup=Map.of(pi->(Path)From.fromP(pi,guessed),sup);
   if(sup.contains(path)){return true;}
    return false;
  }
  public ExpCore visit(ClassB s) {
    List<PathMx> newPs=Map.of(pi->
      pi.withPath(IntrospectionAdapt.add1Outer(pi.getPath())),pMxs);
    List<PathMx> oldPs=pMxs;
    pMxs=newPs;
    try{return super.visit(s);}
    finally{pMxs=oldPs;}
    }
  public static Set<PathMx> of(Program p, List<PathMx> mapMx, ClassB cb) {
    FindUsage fu= new FindUsage(p, mapMx);
    fu.startVisit(cb);
    return fu.used;
  }
}