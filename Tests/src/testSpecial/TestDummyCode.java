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

public class TestDummyCode {
  public static void main(String [] arg) throws IOException{
    L42.setRootPath(Paths.get("dummy"));
    System.out.println(Paths.get("dummy").toUri());
    TestHelper.configureForTest();
    try{
      Path path = Paths.get("dummy","dummy.l42");
      FinalResult res = L42.runSlow(path.toString(),L42.pathToString(path));
      System.out.println("------------------------------");
      System.out.println("END: "+res.getErrCode());
    }
    catch(ErrorMessage msg){
      //System.out.println(L42.record.toString());
      msg.printStackTrace();
     //Executor.reportError(msg);
      System.out.println(ErrorFormatter.formatError(Program.empty(),msg).getErrorTxt());
      }
/*    try{
      FinalResult res = L42.runSlow(L42.pathToString(Paths.get("dummy","dummy.l42")));
      System.out.println(L42.record.toString());
      if(res.getResult()!=0){
        System.out.println(res);
        Executor.reportError(null);
        }
      }
    catch(ErrorMessage msg){
      System.out.println(L42.record.toString());
      msg.printStackTrace();
      Executor.reportError(msg);
      }*/
  }
}
