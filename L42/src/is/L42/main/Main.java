package is.L42.main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import is.L42.common.CTz;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.generated.Full;
import is.L42.top.Init;

public class Main {
  public static void main(String...arg) throws IOException {
    String name = arg[0];
    assert name != null;
    Path path=Paths.get(name);
    if(Files.exists(path) && Files.isDirectory(path)){
      path=path.resolve("This.L42");
      }
    else if(!Files.exists(path)){path=Paths.get(name+".L42");}
    var code=Parse.fromPath(path);
    Init init=new Init((Full.L)code);
    init.top.top(init.p);
    }
}
