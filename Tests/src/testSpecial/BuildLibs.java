package testSpecial;

import helpers.TestHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;



import ast.ErrorMessage;
import ast.ErrorMessage.FinalResult;
import auxiliaryGrammar.Program;
import facade.ErrorFormatter;
import facade.L42;

public class BuildLibs {
  public static void main(String [] arg) throws IOException{
    L42.setRootPath(Paths.get("dummy"));
    TestHelper.configureForTest();
    try{
      FinalResult res = L42.runSlow(null,TestHelper.multiLine(""
//,"{reuse L42.is/nanoBase1"
,"{reuse L42.is/deployMini"
,"Main:{"
//,"  l=Introspection({reuse L42.is/tinyBase1})"
//,"  Debug(fileName:S\"localhost/miniBase.L42\""
//,"    content:l.get()"
//,"    )"
,"  Deploy("
,"    fileName:S\"localhost/miniBase.L42\","
,"    code:{reuse L42.is/tinyBase1},"
//,"    code:{reuse L42.is/nanoBase1},"
,"    node:S\"This0\")"
,"  return ExitCode.normal()"
,"  }"
,"}"
));
      System.out.println("------------------------------");
      System.out.println("END: "+res.getErrCode());
    }
    catch(ErrorMessage msg){
      //System.out.println(L42.record.toString());
      msg.printStackTrace();
     //Executor.reportError(msg);
      System.out.println(ErrorFormatter.formatError(Program.empty(),msg).getErrorTxt());
      }
  }
}
