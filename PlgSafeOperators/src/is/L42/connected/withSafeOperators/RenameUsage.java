package is.L42.connected.withSafeOperators;

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
import ast.Util;
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
  RenameUsage(Program p,CollectedPrivates maps) {
    super(p,maps);
  }
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    return potentiallyRenameMethodImplementedHeader(super.visit(mi));
  }

  private MethodImplemented potentiallyRenameMethodImplementedHeader(MethodImplemented mi) {
    ClassB currentCb=this.getAstCbPath().get(this.getAstCbPath().size()-1);
    Program ep=Program.getExtendedProgram(this._p, this.getAstCbPath());
    List<Path> supers = Program.getAllSupertypes(ep, currentCb);
    assert !supers.isEmpty();
    for(MethodLocator pMx :maps.privateSelectors){
      if(!mi.getS().equals(pMx.getThat().getMs())){continue;}
      int cutSize=pMx.getMTail().size()-1;
      NodeLocator mloc = new NodeLocator(
          pMx.getMTail().subList(0,cutSize),
          pMx.getMPos().subList(0,cutSize),
          pMx.getMOuters().subList(0,cutSize));
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
  private boolean compatibleNodeLocator(Path guessed, Path path) {
   if(guessed.equals(path)){return true;}
   ClassB ct=p.extractCt(guessed);
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