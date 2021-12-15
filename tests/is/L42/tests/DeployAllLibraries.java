package is.L42.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.main.Main;
import is.L42.platformSpecific.javaTranslation.L42£TrustedIO;
import is.L42.platformSpecific.javaTranslation.Resources;
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
      "class method S #$token()=S\""+contentToken+"\"\n"+
      "class method S repo()=S\""+contentRepo+"\"\n"+
      "class method S version()=S\""+Main.l42IsRepoVersion+"\"\n";
    Files.write(sPath,content.getBytes());
    try{return TestL42Bridge.name(path);}
    finally{Files.delete(sPath);}
    }
  public static void main(String[]a) throws IOException, URISyntaxException {
    System.out.println("Do not accidentally run this;\n fix the version number in "+Main.l42IsRepoVersion+" comment this and run. Then uncomment this");
    //System.exit(0);//uncomment this line after any run!
    String tests;try{tests=deployAllLibraries().collect(Collectors.joining("\n"));}
    finally{Resources.clearResKeepReuse();}//did this make it terminating?
    System.out.println("\n\nDeployAllLibraries completed\n\n");
    String split=L42£TrustedIO.testsSeparator;
    String splitP=Pattern.quote(split);
    List<String> res=List.of(tests.split(splitP));
    List<String> fRes=res.stream().filter(s->s.startsWith("#Fail")).toList();
    if(!fRes.isEmpty()) {System.out.println("Errors deploying libraries:");}
    else {System.out.println("\n\nAll tests passed\n\n");}
    for(var s:fRes) {System.out.println(split);System.out.println(s);}
    //Why it is not terminating here?    
    }  
  public static Stream<String> deployAllLibraries() throws IOException, URISyntaxException {
    return Stream.of(
      nameSecret("L42Source/TestStandardDeployment"),
      nameSecret("L42Source/TestLoad"),//Ok deployment by hand not with WebIntegrated
      nameSecret("L42Source/TestJavaServer"),
      nameSecret("L42Source/TestRawQuery"),
      nameSecret("L42Source/TestQuery"),
      nameSecret("L42Source/TestGuiBuilder"),      
      nameSecret("L42Source/TestSifoLib"),//Ok deployment by hand until we work on it with Tobias
      nameSecret("L42Source/TestUnit"),
      nameSecret("L42Source/TestVoxelMap"),
      nameSecret("L42Source/DeployJSon"),
      nameSecret("L42Source/TestTime")
      );
    }
  }
