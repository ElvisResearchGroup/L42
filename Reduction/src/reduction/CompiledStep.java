package reduction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Translator;
import sugarVisitors.ToFormattedText;
import coreVisitors.IsCompiled;
import coreVisitors.IsValue;
import coreVisitors.NormalizeBlocks;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Program;

public class CompiledStep extends SmallStep{

  @Override protected void log(String s) { }
  private static int rounds=0;
  protected ExpCore executeAtomicStep(Program p1, ExpCore _e1) {
    if(!IsCompiled.of(_e1)){return step(p1, _e1);}
    return Resources.withPDo(p1,()->{
      ExpCore e1=_e1;
      e1=NormalizeBlocks.of(e1);
      if(e1 instanceof ExpCore.Signal){
        ExpCore.Signal s=(ExpCore.Signal)e1;
        //TODO: if(IsValue.of(p1,s.getInner())){//too hard to give great error here!
        throw new ErrorMessage.CtxExtractImpossible(e1,p1.getInnerData());
        //}
        }
      String code=Translator.translateProgram(p1, e1);
      //System.out.println(code);
      //TODO: for testng only
      //try {Files.write(Paths.get("C:\\Users\\marco\\Desktop\\WorkspaceLuglio2014\\Test42ToJava\\src\\generated\\Program42.java"), code.getBytes());}catch (IOException e) {throw new Error(e);}
      try{
        rounds++;
        System.out.println("Compilation Iteration: "+rounds+ "");
        Object o=Translator.runString(code);
        //System.out.println("Compilation Iteration complete: "+rounds);
        assert o instanceof ClassB;
        return (ClassB)o;
        }
      catch(Resources.Error err){
        return EncodingHelper.wrapResource(err);
        }
      });
    }
  }
