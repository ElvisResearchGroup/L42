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
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
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
    Configuration.typeSystem.computeStage(p,cb);
    //get p'
    Stage oldSt=cb.getStage().getStage();
    try{
      cb.getStage().setStage(Stage.Less);//TODO: ok according with Formal. should we make formal nicer?
      Program p1=p.addAtTop(cb);
      //assert ct.getStage()==Stage.Less;
      //Program p1=p.addAtTop(ct);
      ErrorFormatter.printType(p1);
      //check p'
      Configuration.typeSystem.checkAll(p1);
      e1=Norm.of(p1,e1);
      //check e1
      Configuration.typeSystem.checkMetaExpr(p1.getExecutableStar(),e1);
      e1.accept(new CloneVisitor(){
        @Override public ExpCore visit(ClassB cb){
          assert cb.getStage().isInheritedComputed():
            "foo";
          return cb;//we must only check the outermost layer or cb
        }
      });
      //run m.e1-->e2
      ExpCore e2=executeAtomicStep(new PData(p1),e1,m.getName());
      e2=Functions.clearCache(e2,Stage.Less);
      if(!(e2 instanceof ClassB)){Configuration.typeSystem.checkMetaExpr(p1.getExecutableStar(),e2);}//TODO: as assert
      ClassB cbRes=cb.withMember(m.withInner(e2));
      //TODO: if e2 is an error, terminate with error, do it better?
      //is it already stopping if that happens?
      //if(e2 instanceof ExpCore.Signal){throw new ErrorMessage.MalformedFinalResult(cbRes, "error raised in metaexpression evauation");}
      //replace cb[m.e2]
      return cbRes;
    }finally{
      cb.getStage().setStage(oldSt);
      }
  }
  protected ExpCore executeAtomicStep(PData p1, ExpCore e1,String nestedName) {
    //return step(p1,e1);//TODO push withP in compiledStep
    if(!IsCompiled.of(e1)){return step(p1, e1);}
    return Resources.withPDo(p1,()-> step(p1, e1));
  }
  @Override
  protected void log(String s) {
    //System.out.println(s);
  }

}
