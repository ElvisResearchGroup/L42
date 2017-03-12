package reduction;

import platformSpecific.javaTranslation.Resources;
import sugarVisitors.ToFormattedText;
import coreVisitors.InjectionOnSugar;
import coreVisitors.IsCompiled;
import coreVisitors.NormalizeBlocks;
import facade.PData;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import programReduction.Program;

public class BigStep extends SmallStep{

  
  protected ExpCore executeAtomicStep(PData p1, ExpCore _e1) {
    if(!IsCompiled.of(_e1)){return step(p1, _e1);}
    return Resources.withPDo(p1,()->{
      ExpCore e1=_e1;
      boolean runned=false;
      e1=NormalizeBlocks.of(e1);
      try{while(!(e1 instanceof ClassB)){
        log(ToFormattedText.ofCompact(e1.accept(new InjectionOnSugar()),false));
        e1=step(p1,e1);
        e1=NormalizeBlocks.of(e1);
        runned=true;
        }}catch(Throwable t){
          if(!runned){ throw t;}
        }
      log(ToFormattedText.ofCompact(e1.accept(new InjectionOnSugar()),false));
      return e1;
    });
  }
}
