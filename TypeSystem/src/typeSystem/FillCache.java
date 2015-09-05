package typeSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.Ast;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ErrorMessage;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.PathMwt;
import auxiliaryGrammar.Program;
import auxiliaryGrammar.UsedPathsPlus;
import coreVisitors.From;

public class FillCache {
 public static void computeInheritedDeep(Program p,ClassB cb){
   assert cb!=null;
   computeInherited(p,cb);
   for(Member m:cb.getMs()){
     if(!(m instanceof NestedClass)){continue;}
     NestedClass nc=(NestedClass)m;
     assert nc.getInner() instanceof ClassB;
     computeInheritedDeep(p.addAtTop(cb), (ClassB)nc.getInner());
   }
 }
 public static void computeInherited(Program p,ClassB cb){
    if(cb.getStage().isInheritedComputed()){return;}
    p=p.addAtTop(cb);
    List<Path> allSup = Program.getAllSupertypes(p, cb);
    List<PathMwt> mwts=computeMwts(p, allSup);
    checkCoherent(mwts,cb);
    cb.getStage().setInherited(mwts);
    cb.getSupertypes().clear();
    cb.getSupertypes().addAll(allSup);
  }
private static void checkCoherent(List<PathMwt> mwts, ClassB cb) {
  //- no two mwt are the same
  //-forall mwti, cb do not define them as mwt.
  for(int i=0;i<mwts.size();i++){
    for(int j=i+1;j<mwts.size();j++){
      if(!mwts.get(i).getMwt().getMs().equals(mwts.get(j).getMwt().getMs())){continue;}
      throw new ErrorMessage.IncoherentMwts(completeMwts(mwts,cb));
    }
  }
  for(PathMwt pmwt: mwts){
   for(Member m:cb.getMs()){
     if(!(m instanceof MethodWithType)){continue;}
     MethodWithType mwt=(MethodWithType) m;
     if(!pmwt.getMwt().getMs().equals(mwt.getMs())){continue;}
     throw new ErrorMessage.IncoherentMwts(completeMwts(mwts,cb));
   } 
  }
}
private static List<PathMwt> completeMwts(List<PathMwt> mwts, ClassB cb) {
  for(Member m:cb.getMs()){
    if(!(m instanceof MethodWithType)){continue;}
    MethodWithType mwt=(MethodWithType) m;
    mwts.add(new PathMwt(Path.outer(0),mwt));
  }
  return mwts;
}
private static  List<PathMwt> computeMwts(Program p, List<Path> allSup) {
  List<PathMwt> mwts=new ArrayList<>();
  for(Path pi:allSup){
    ClassB cbi=p.extractCb(pi);
    assert cbi.isInterface();
    for(Member mij:cbi.getMs()){
      if (mij instanceof ClassB.NestedClass){continue;}
      MethodWithType mwt=(MethodWithType) mij;
      mwts.add(new PathMwt(pi,From.from(mwt,pi)));
    }
  }
  return mwts;
}

public static void collectInnerClasses(Program p,ClassB cb,List<ClassB>result) {
  result.add(cb);
  p=p.addAtTop(cb);
  for(Member m:cb.getMs()){
    if(!(m instanceof NestedClass)){continue;}
    NestedClass nc=(NestedClass)m;
    if (!(nc.getInner() instanceof ClassB)){result.add(null);continue;}
    ClassB cbi=(ClassB)nc.getInner();
    if(cbi.getStage().getStage()==Stage.None){
      computeStage(p,cbi);
    }
    collectInnerClasses(p,cbi,result);
    }
}
  public static void computeStage(Program p,ClassB cb) {//requires inherited
    if(cb.getStage().getStage()!=Stage.None){return;}
    computeInherited(p, cb);
    assert cb.getStage().getInherited()!=null;
    List<ClassB> inner = new ArrayList<>();
    collectInnerClasses(p,cb,inner);
    List<ClassB> es = extractUsedCb(p, cb);
    Stage stage = TypeExtraction.stage(p, cb, es);
    if(stage==null){stage=Stage.Star;}//TODO: when type extraction dies, fix stage.
    assert stage!=null;
    cb.getStage().setStage(stage);
    /*for(ClassB cbni:inner){//includes current class
      assert cbni.getStage().getStage()!=Stage.None:
        cbni;//I'm putting none for not compiled yet
    }*/
  }
  private static List<ClassB> extractUsedCb(Program p, ClassB cb) {
    List<Path> usedPlus = new UsedPathsPlus().of(cb);
    Program p1=p.addAtTop(cb);
    List<ClassB> es=new ArrayList<>();//null represents a meta expression
    for(Path pi:usedPlus){
      if(!pi.isCore()){continue;}
      try{
        ClassB cbi=p1.extractCb(pi);
        if(!es.contains(cbi)){ es.add(cbi);}
        }
      catch(ErrorMessage.ProgramExtractOnMetaExpression meta){es.add(null);}
      catch(ErrorMessage.ProgramExtractOnWalkBy walk){ es.add(null);}
      }
    return es;
  }
}
