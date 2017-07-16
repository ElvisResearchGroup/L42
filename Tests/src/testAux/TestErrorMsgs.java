package testAux;

import java.io.IOException;



import org.junit.Assert;

import ast.ErrorMessage;
import ast.ErrorMessage.UserLevelError;
import auxiliaryGrammar.Functions;
import programReduction.Program;

import static ast.ErrorMessage.UserLevelError.Kind.*;
import facade.ErrorFormatter;
import facade.L42;

public class TestErrorMsgs {
  public void testCode(String fileName,UserLevelError.Kind expectedKind,int expectedLine,String ...src){
    String code=Functions.multiLine(src);
    try{L42.runSlow(fileName,code);}
    catch(ErrorMessage msg){
      UserLevelError err = ErrorFormatter.formatError(Program.emptyLibraryProgram(),msg);
      Assert.assertEquals(err.getKind(), expectedKind);
      Assert.assertEquals(err.getPos().getFile(),fileName);
      Assert.assertEquals(err.getPos().getLine1(),expectedLine);
      }
  }
  //@Test(singleThreaded=false)
  public void test1() throws IOException{
   assert false;
    //testCode("a/b/c",TypeError,10,




      //  );
  }


}
