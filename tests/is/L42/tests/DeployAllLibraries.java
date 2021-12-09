package is.L42.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.main.Main;
import is.L42.tools.TestL42Bridge;

//Note: to make this file compile you need to add
//(and never commit) a class as follows:
//public class SecretHolder {
//public final static String token(){ return "ghp_..."; } 
//}
//Or with any kind of security you want to implement to save/store/retrive your access token

public class DeployAllLibraries {
  public static String nameSecret(String name)throws IOException, URISyntaxException{
    var res=DeployAllLibraries.class.getResource(name);
    assert res!=null:"The resource "+name+" is invalid";
    var path=Path.of(res.toURI());
    var sPath=path.resolve("Secret.L42");
    var contentToken=SecretHolder.token();
    var contentRepo=Main.l42IsRepoPath;
    var content=
      "class method S #$of()=S\""+contentToken+"\"\n"+
      "class method S repo()=S\""+contentRepo+"\"\n"+
      "class method S version()=S\""+Main.l42IsRepoVersion+"\"\n";
    Files.write(sPath,content.getBytes());
    try{return TestL42Bridge.name(path);}
    finally{Files.delete(sPath);}
    }
  public static void main(String[]a) throws IOException, URISyntaxException {
    var tests=deployAllLibraries().collect(Collectors.joining("\n"));
    System.out.println("DeployAllLibraries completed\n\n");
    System.out.println(tests);
    String split="\\#\\#\\#\\#\\#\\#\\#\\#\\#\\#\\#\\#\\#\\#\\#\\#\\#\\#\\#\\n";
    List<String> res=List.of(tests.split(split));
    List<String> fRes=res.stream().filter(s->s.startsWith("#Fail")).toList();
    if(!fRes.isEmpty()) {System.out.println("Errors deploying libraries:");}
    for(var s:fRes) {System.out.println(split);System.out.println(s);}
    //Why it is not terminating here?
    }  
  public static Stream<String> deployAllLibraries() throws IOException, URISyntaxException {
    return Stream.of(
      nameSecret("L42Source/TestHttpRequest"),
      nameSecret("L42Source/TestLoad"),
      nameSecret("L42Source/TestFileSystem"),
      nameSecret("L42Source/TestJavaServer"),
      nameSecret("L42Source/TestRawQuery"),
      nameSecret("L42Source/TestQuery"),
      nameSecret("L42Source/TestGuiBuilder"),      
      nameSecret("L42Source/TestSifoLib"),
      nameSecret("L42Source/TestUnit"),
      nameSecret("L42Source/TestVoxelMap"),
      nameSecret("L42Source/DeployJSon"),
      nameSecret("L42Source/TestTime"),
      nameSecret("L42Source/TestProcess")
      );
    }
  }
