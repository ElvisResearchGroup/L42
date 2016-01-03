package reduction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Translator;
import profiling.Timer;
import sugarVisitors.ToFormattedText;
import coreVisitors.IsCompiled;
import coreVisitors.IsValue;
import coreVisitors.NormalizeBlocks;
import facade.L42;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Program;

public class CompiledStep extends SmallStep{
  @Override protected void log(String s) { }
  protected ExpCore executeAtomicStep(Program p1, ExpCore _e1) {
    if(!IsCompiled.of(_e1)){return step(p1, _e1);}
    return Resources.withPDo(p1,()->{
      ExpCore e1=_e1;
      e1=NormalizeBlocks.of(e1);
      if(e1 instanceof ExpCore.Signal){
        //ExpCore.Signal s=(ExpCore.Signal)e1;
        //TODO: if(IsValue.of(p1,s.getInner())){//too hard to give great error here!
        throw new ErrorMessage.CtxExtractImpossible(e1,p1.getInnerData());
        //}
        }
      String code;
      Timer.activate("Translator.translateProgram");try{
      code=Translator.translateProgram(p1, e1);
      }finally{Timer.deactivate("Translator.translateProgram");}
      //String code=Translator.translateProgram(p1, e1);
      //System.out.println(code);
      //TODO: for testng only
     //try {Files.write(Paths.get("C:\\Users\\marco\\Desktop\\eclipseMars\\L42Local\\Tests\\src\\generated\\Program42.java"), code.getBytes());}catch (IOException e) {throw new Error(e);}
      try{
        L42.compilationRounds++;
        System.out.println("Compilation Iteration: "+L42.compilationRounds+ "");
        //System.out.println("In Compilation Iteration: "+sugarVisitors.ToFormattedText.of(e1)+ "!!");
        Object o=Translator.runString(code);
        System.out.println("Compilation Iteration complete: "+L42.compilationRounds+ "");
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
