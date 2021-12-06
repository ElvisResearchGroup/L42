package is.L42.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import is.L42.common.ToNameUrl;
import is.L42.tools.TestL42Bridge;

//Note: to make this file compile you need to add
//(and never commit) a class as follows:
//public class SecretHolder {
//public final static String token(){ return "ghp_..."; } 
//}
//Or with any kind of security you want to implement to save/store/retrive your access token

public class IntegrationTests extends TestL42Bridge {
  public static String name(String name)throws IOException, URISyntaxException{
    var res=IntegrationTests.class.getResource(name);
    assert res!=null:"The resource "+name+" is invalid";
    var path=Path.of(res.toURI());
    return name(path);
    }
  public static String nameSecret(String name)throws IOException, URISyntaxException{
    var res=IntegrationTests.class.getResource(name);
    assert res!=null:"The resource "+name+" is invalid";
    var path=Path.of(res.toURI());
    var sPath=path.resolve("Secret.L42");
    var contentToken=SecretHolder.token();
    var contentRepo=ToNameUrl.l42IsRepoPath;
    var contentVersion=ToNameUrl.l42IsRepoVersion;
    var content=
      "class method S #$of()=S\""+contentToken+"\"\n"+
      "class method S repo()=S\""+contentRepo+"\"\n"+
      "class method S version()=S\""+contentVersion+"\"\n";
    Files.write(sPath,content.getBytes());
    try{return name(path);}
    finally{Files.delete(sPath);}
    }
  public static Stream<L42Test> test() throws IOException, URISyntaxException {
    return fromStream(Stream.of(
      name("L42Source/AdamsTowel"),
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
      name("L42Source/TestAST"),
      name("L42Source/TestCacheCall"),
      name("L42Source/TestCoherent"),
      name("L42Source/TestData"),
      name("L42Source/TestDecoratorsErrors"),
      name("L42Source/TestEagerCache"),
      name("L42Source/TestEnum"),
      name("L42Source/TestForkJoin"),
      name("L42Source/TestFullMergePattern"),
      name("L42Source/TestIntrospection"),
      name("L42Source/TestJavaSlaveDirect"),
      name("L42Source/TestLazyCache"),
      name("L42Source/TestList"),
      name("L42Source/TestMap"),
      name("L42Source/TestNonDeterministicError"),
      name("L42Source/TestOpt"),
      name("L42Source/TestOrganize"),
      name("L42Source/TestResetDocs"),
      name("L42Source/TestSelfCollection"),
      name("L42Source/TestSUtils"),
      name("L42Source/TestTutorialExercises"),
      name("L42Source/TestTutorialJavaSlave"),
      name("L42Source/TestView"),
      name("L42Source/TestDecoratorDecorator"),
      name("L42Source/TestSifoExamples"),
      name("L42Source/BattleShip")
      //name("L42Source/TestAdventOfCode2021")
      ));}
  }
