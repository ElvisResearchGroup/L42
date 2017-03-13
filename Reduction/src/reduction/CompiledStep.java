package reduction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Translator;
import profiling.Timer;
import sugarVisitors.ToFormattedText;
import coreVisitors.IsCompiled;
import coreVisitors.IsValue;
import coreVisitors.NormalizeBlocks;
import facade.L42;
import facade.PData;
import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import auxiliaryGrammar.EncodingHelper;
import programReduction.Program;

public class CompiledStep extends SmallStep{
  @Override protected void log(String s) { }

  @Override
  protected ExpCore executeAtomicStep(PData p1, ExpCore _e1,Ast.C nestedName) {
    if(!IsCompiled.of(_e1)){
      return step(p1, _e1);
      }
    return Resources.withPDo(p1,()->{
      ExpCore e1=NormalizeBlocks.of(_e1);
      if(e1 instanceof ExpCore.Signal){
        throw new ErrorMessage.CtxExtractImpossible(e1,null);
        }
      Translator code=Timer.record("Translator.translateProgram",()->Translator.translateProgram(p1.p, e1));
      try{
        L42.compilationRounds++;
        System.out.println("Compilation Iteration-- "+nestedName+":"+L42.compilationRounds+ "");
        Timer.activate("code.runMap");
        Object o=code.runMap();
        Timer.deactivate("code.runMap");
        System.out.println("Compilation Iteration complete-- "+nestedName+":"+L42.compilationRounds+ "");
        assert o instanceof ClassB;
        return (ClassB)o;
        }
      catch(Resources.Error err){
        Resources.cacheMessage(err);
        return EncodingHelper.wrapResource(err);
        }
      catch(Resources.Exception err){
        Resources.cacheMessage(err);
        return EncodingHelper.wrapResource(err);
        }
      catch(Resources.Return err){//it can happen if other stuff is wrong, in this way we can see the error.
        Resources.cacheMessage(err);
        return EncodingHelper.wrapResource(err);
        }
      });
    }
  }
