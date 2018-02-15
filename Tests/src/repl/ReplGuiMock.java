package repl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import caching.Loader;

public class ReplGuiMock {
  ReplGuiMock(){
    try {Files.deleteIfExists(Paths.get("localhost","ReplCache.C42"));}
    catch (IOException e) {throw new Error(e);}
  }
  ReplState repl=null;
  StringBuffer err=new StringBuffer();{
    System.setOut(ReplGui.delegatePrintStream(err,System.out));
    System.setErr(ReplGui.delegatePrintStream(err,System.err));
    }
  boolean running=false;
  void defaultStart() {
    auxRunCode(
      "reuse L42.is/AdamTowel02\n"+
      "Main: {\n"+
      "  Debug(S\"Hello world\")\n"+
      "  return ExitCode.normal()\n"+
      "  }"
      );
  }
  void auxRunCode(String code){
    if(repl==null){
      repl=ReplState.start("{"+code+"}", new Loader(Paths.get("localhost","ReplCache.C42")));
      }
    else{
      ReplState newR=repl.add(code);
      if(newR!=null){repl=newR;}
      }
    }

//          "Main"+iterations+":{//make more stuff happen!\n"+
//          "  return ExitCode.normal()\n  }"
  }