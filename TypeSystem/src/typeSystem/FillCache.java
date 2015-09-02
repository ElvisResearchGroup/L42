package typeSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.PathMwt;
import auxiliaryGrammar.Program;

public class FillCache {
 public static void computeInherited(Program p,ClassB cb){
    p=p.addAtTop(cb, null);
    List<Path> allSup = Program.getAllSupertypes(p, cb);
    List<PathMwt> mwts=computeMwts(p, allSup);
    //TODO:where I check if mwts is coherent? 
    //- no two mwt are the same
    //-forall mwti, cb do not define them as mwt.
    cb.getStage().setInherited(mwts);
    cb.getSupertypes().clear();
    cb.getSupertypes().addAll(allSup);
  }
private static  List<PathMwt> computeMwts(Program p, List<Path> allSup) {
  List<PathMwt> mwts=new ArrayList<>();
  for(Path pi:allSup){
    ClassB cbi=p.extractCb(pi);
    assert cbi.isInterface();
    for(Member mij:cbi.getMs()){
      if (mij instanceof ClassB.NestedClass){continue;}
      MethodWithType mwt=(MethodWithType) mij;
      mwts.add(new PathMwt(pi,mwt));
    }
  }
  return mwts;
}
  public static void computeStage(Program p,ClassB cb) {//requires inherited
    assert cb.getStage().getInherited()!=null;
    
  }
}
