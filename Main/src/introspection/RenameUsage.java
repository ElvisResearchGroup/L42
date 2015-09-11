package introspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
import ast.Util.PathMxMx;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import coreVisitors.GuessTypeCore;

public class RenameUsage extends MethodPathCloneVisitor {
  List<PathMxMx> pMxs;
  RenameUsage(Program p,List<PathMxMx> pMxs) {
    super(p);
    this.pMxs=pMxs;
  }
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    return potentiallyRenameMethodImplementedHeader(super.visit(mi));
  }
  private MethodImplemented potentiallyRenameMethodImplementedHeader(MethodImplemented mi) {
    for(PathMxMx pMx :pMxs){
      if(!mi.getS().equals(pMx.getMs1())){continue;}
      Path renamedP = Norm.of(p,pMx.getPath());
      if(!equalOrSubtype(Path.outer(0),renamedP)){continue;}
      return mi.withS(pMx.getMs2());
      }
    return mi;
  }
  @Override public MethodSelector visitMS(MethodSelector original,Path src){
      assert src!=null;
      List<PathMxMx> filtered=new ArrayList<>();
      for(PathMxMx pMx:pMxs){
        if(original.equals(pMx.getMs1())){filtered.add(pMx);}
      }
      if(filtered.isEmpty()){return original;}
      for(PathMxMx pMx:filtered){
          Path path=Norm.of(p,pMx.getPath());
          if(!equalOrSubtype(src,path)){continue;}
          return pMx.getMs2();
          }
        return original;
    }
  private boolean equalOrSubtype(Path guessed, Path path) {
   if(guessed.equals(path)){return true;}
   ClassB ct=p.extractCb(guessed);
   List<Path> sup = ct.getSupertypes();
   sup=Map.of(pi->(Path)From.fromP(pi,guessed),sup);
   if(sup.contains(path)){return true;}
    return false;
  }
  public ExpCore visit(ClassB s) {
    List<PathMxMx> newPs=Map.of(pi->
      pi.withPath(IntrospectionAdapt.add1Outer(pi.getPath())),pMxs);
    List<PathMxMx> oldPs=pMxs;
    pMxs=newPs;
    try{return super.visit(s);}
    finally{pMxs=oldPs;}
    }
  public static ClassB of(Program p, List<PathMxMx> mapMx, ClassB cb) {
    return new RenameUsage(p, mapMx).startVisit(cb);
  }
}