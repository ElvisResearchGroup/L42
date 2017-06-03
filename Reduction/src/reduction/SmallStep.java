package reduction;

import java.util.ArrayList;
import java.util.HashMap;

import platformSpecific.javaTranslation.Resources;
import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Ast.Mdf;
import ast.Ast.Type;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import ast.ExpCore.WalkBy;

import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import auxiliaryGrammar.Ctx;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import coreVisitors.CloneVisitor;
import coreVisitors.CollectPaths0;
import coreVisitors.ExtractCtxCompiled;
import coreVisitors.IsCompiled;
import coreVisitors.ReplaceCtx;
import facade.Configuration;
import facade.ErrorFormatter;
import facade.PData;

public class SmallStep extends Executor{

  protected ClassB meta1(Program p, ClassB cb, NestedClass m) {
    log("---meta1--");
    ExpCore e1=m.getInner();
    //get cb-->ct
    //?Functions.clearCache(cb, Stage.Plus);
    //?for(ClassB cbi:p.getInnerData()){Functions.clearCache(cb, Stage.Plus);}
    //?p.recomputeStage();
    //get p'
    try{
      Program p1=p.evilPush(cb);
      ErrorFormatter.printType(p1);
      //check p'
      //Configuration.typeSystem.checkAll(p1);
      //e1=Norm.of(p1,e1);
      //check e1
      //Configuration.typeSystem.checkMetaExpr(p1.getExecutableStar(),e1);
      //run m.e1-->e2
      ExpCore e2=executeAtomicStep(new PData(p1),e1,m.getName());
      //if(!(e2 instanceof ClassB)){Configuration.typeSystem.checkMetaExpr(p1.getExecutableStar(),e2);}//TODO: as assert
      ClassB cbRes=cb.withMember(m.withInner(e2));
      //TODO: if e2 is an error, terminate with error, do it better?
      //is it already stopping if that happens?
      //if(e2 instanceof ExpCore.Signal){throw new ErrorMessage.MalformedFinalResult(cbRes, "error raised in metaexpression evauation");}
      //replace cb[m.e2]
      return cbRes;
    }finally{
 
      }
  }
  protected ExpCore executeAtomicStep(PData p1, ExpCore e1,Ast.C nestedName) {
    //return step(p1,e1);//TODO push withP in compiledStep
    if(!IsCompiled.of(e1)){return step(p1, e1);}
    return Resources.withPDo(p1,()-> step(p1, e1));
  }
  @Override
  protected void log(String s) {
    //System.out.println(s);
  }

}
