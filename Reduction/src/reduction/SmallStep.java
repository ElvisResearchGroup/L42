package reduction;

import java.util.ArrayList;
import java.util.HashMap;

import platformSpecific.javaTranslation.Resources;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import ast.ExpCore.WalkBy;
import ast.Util.CachedStage;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import auxiliaryGrammar.Ctx;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import coreVisitors.CollectPaths0;
import coreVisitors.ExtractCtxCompiled;
import coreVisitors.IsCompiled;
import coreVisitors.ReplaceCtx;
import facade.Configuration;
import facade.ErrorFormatter;

public class SmallStep extends Executor{

  protected ClassB meta1(Program p, ClassB cb, NestedClass m) {
    log("---meta1--");
    ExpCore e1=m.getInner();
    //get cb-->ct
    Configuration.typeSystem.computeStage(p,cb);
    //ct=ct.withMember(m.withBody(new WalkBy()));
    //get p'
    //ct=ct.withStage(new CachedStage());
    //ct.getStage().setStage(Stage.Less);//TODO: boh? still ok?
    Program p1=p.addAtTop(cb);
    //assert ct.getStage()==Stage.Less;
    //Program p1=p.addAtTop(ct);
    ErrorFormatter.printType(p1);
    //check p'
    Configuration.typeSystem.checkAll(p1);
    e1=Norm.of(p1,e1);
    //check e1
    Configuration.typeSystem.checkMetaExpr(p1.getExecutableStar(),e1);
    //forall path inside e1 executableAndComplete(p1,path)
    //for(Path path:CollectPaths0.of(e1)){
     // if(p1.executable(path)){continue;}
     // String reason=ErrorFormatter.whyIsNotExecutable(path,p1);// should be in the final error reporting
      //TODO: throw new ErrorMessage.IncompleteClassIsRequired(reason, e1, path, p1.getInnerData());
      //}
    //run m.e1-->e2
    ExpCore e2=executeAtomicStep(p1,e1);
    Configuration.typeSystem.checkMetaExpr(p1.getExecutableStar(),e2);//as assert
    ClassB cbRes=cb.withMember(m.withBody(e2));
    //TODO: if e2 is an error, terminate with error, do it better?
    //is it already stopping if that happens?
    //if(e2 instanceof ExpCore.Signal){throw new ErrorMessage.MalformedFinalResult(cbRes, "error raised in metaexpression evauation");}
    //replace cb[m.e2]
    return cbRes;
  }
  protected ExpCore executeAtomicStep(Program p1, ExpCore e1) {
    //return step(p1,e1);//TODO push withP in compiledStep
    if(!IsCompiled.of(e1)){return step(p1, e1);}
    return Resources.withPDo(p1,()-> step(p1, e1));
  }
  @Override
  protected void log(String s) {
    //System.out.println(s);
  }

}
