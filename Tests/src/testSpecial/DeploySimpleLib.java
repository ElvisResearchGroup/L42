package testSpecial;

import helpers.TestHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


import ast.ErrorMessage;
import ast.ErrorMessage.FinalResult;
import facade.ErrorFormatter;
import facade.L42;

public class DeploySimpleLib {
  public static void main(String [] arg) throws IOException{
    L42.setRootPath(Paths.get("dummy"));
    TestHelper.configureForTest();
    //testLibCreationGui();
    //testLibCreation();
    testLibUse();
  }

  private static void testLibCreation() {
    try{
      FinalResult res = L42.runSlow(null,TestHelper.multiLine(""
,"          {reuse L42.is/deployMini"
,"            Main:Deploy[S\"Top\", fileName:S\"localhost/SimpleLib.L42\"]<{reuse L42.is/templateBNS"
,"              Top:Generalize[]<{reuse L42.is/miniBase"
,"                Exported:{"
,"                  type method Void printHelloWorld()"
,"                    Debug(S\"Hello World \"++42N)"
,"                  }"
,"                }"
,"              }"
,"            }"
));
      System.out.println("------------------------------");
      System.out.println("END: "+res.getErrCode());
    }
    catch(ErrorMessage msg){
      //System.out.println(L42.record.toString());
      msg.printStackTrace();
     //Executor.reportError(msg);
      System.out.println(ErrorFormatter.formatError(msg).getErrorTxt());
      }
  }

  private static void testLibUse() {
    try{
      FinalResult res = L42.runSlow(null,TestHelper.multiLine(""
,"          {reuse L42.is/miniBase"
,"            Lib:Load[]<{reuse L42.is/SimpleLib}"
,"            Main:{"
,"              Lib.printHelloWorld()"
,"              return ExitCode.normal()"
,"              }"
,"          }"
));
      System.out.println("------------------------------");
      System.out.println("END: "+res.getErrCode());
    }
    catch(ErrorMessage msg){
      //System.out.println(L42.record.toString());
      msg.printStackTrace();
     //Executor.reportError(msg);
      System.out.println(ErrorFormatter.formatError(msg).getErrorTxt());
      }
  }
  private static void testLibCreationGui() {
    try{
      FinalResult res = L42.runSlow(null,TestHelper.multiLine(""
,"          {reuse L42.is/deployMini"
,"            Main:Deploy[S\"Top::Exported\", fileName:S\"localhost/GuiLib.L42\"]<{reuse L42.is/templateBNS"
,"              Top:Outer0::Generalize[]<{reuse L42.is/miniBase"
,"                Exported:{(S id)"
,""
,"                  GuiPlugin:{'@plugin"
,"                    'L42.is/connected/withHtml"
,"                    }"
,"              "
,"                  type method"
,"                  S jsEscape(S that) ("
,"                    tmp=that.replace(S.doubleQuote(),into:S\"\\\"++S.doubleQuote())"
,"                    tmp.replace(S\"\'\",into:S\"\\\'\")"
,"                    )"
,"                  method"
,"                  Void close()"
,"                    using GuiPlugin"
,"                      check close(wName:this.id().binaryRepr())"
,"                      error void"
,"                "
,"                  method"
,"                  Void open(S html, N x, N y)"
,"                    using GuiPlugin"
,"                      check open("
,"                        wName:this.id().binaryRepr()"
,"                        html:html.binaryRepr()"
,"                        x:x.binaryRepr()"
,"                        y:y.binaryRepr()"
,"                        )"
,"                      error void"
,"                      "
,"                  method"
,"                  Void set(S that,S id) {"
,"                    cmd=S\"$(\'#\"++Outer0.jsEscape(id)"
,"                    ++S\"\').replaceWith(\'\"++Outer0.jsEscape(that)++S\"\');\""
,"                    x=this.executeJs(cmd)"
,"                    Debug(cmd)"
,"                    if x!=S\"\" (Debug(x))"
,"                    return void"
,"                    }"
,"                    "
,"                  method"
,"                  S executeJs(S that)"
,"                    S.#stringParser(using GuiPlugin"
,"                      check executeJs(wName:this.id().binaryRepr(),command:that.binaryRepr())"
,"                      error void)"
,"                "
,"                  method"
,"                  mut Iterator events()"
,"                    Iterator(id: this.id(), current:S\"\")"
,"              "
,"                  Iterator:{mut(S id, var S current)"
,"                  "
,"                    type method S auxFetchEvent(S id) exception Void {"
,"                      Library s=using GuiPlugin"
,"                        check eventPending(wName:id.binaryRepr())"
,"                        exception void"
,"                      catch error x (on Library exception void)"
,"                      return S.#stringParser(s)"
,"                      }"
,"                      "
,"                    mut method"
,"                    Void #next() exception Void {"
,"                      s=Iterator.auxFetchEvent(id:this.id())"
,"                      return this.current(s)"
,"                      }"
,"                      "
,"                    read method"
,"                    Void #checkEnd() void"
,"                    "
,"                    read method"
,"                    S #inner() (this.current())"
,""
,"                    read method"
,"                    Void #close() void"
,"                    }"
,"            '----------------------"
,"                  }"
,"                }"
,"              }"
,"            }"

          ));
      System.out.println("------------------------------");
      System.out.println("END: "+res.getErrCode());
    }
    catch(ErrorMessage msg){
      //System.out.println(L42.record.toString());
      msg.printStackTrace();
     //Executor.reportError(msg);
      System.out.println(ErrorFormatter.formatError(msg).getErrorTxt());
      }
  }

}
