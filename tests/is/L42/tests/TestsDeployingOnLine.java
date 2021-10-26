package is.L42.tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.TestL42Bridge;
import is.L42.tools.TestL42Bridge.L42Test;

/*
Ok the code below works to accentrate the tests, to enable after 3Nov 

-marco developing 42 on ide // adamsTowel +Lib1..lib10
-adam developing 42 on ide Lib11
-bob using 42 on ide //appilication
-charles using 42 on portable zip //appilication
-david using 42 on portable zip Lib12


if version of form r002 refuses to run in eclipse
if version of form v002 the GW will write in /r002/
if version of form v002 the reader will read from git hub /r002/
if version of form r002 the reader will read from L42.is/r002/

AdamsTowel/r004.L42

*/
//TODO: In ToNameUrl is.L42.override.L42IsRepoPathOverride
//remove duplicate content

//Note: to make this file compile you need to add
//(and never commit)a class as follows:
//public class SecretHolder {
//public final static String token="ghp_...";
//public final static String repo="<github username>/<github project>"; //example: "Language42/is";
//}

/*public class TestsDeployingOnLine extends TestL42Bridge {
  public static String nameSecret(String name)throws IOException, URISyntaxException{
    var res=TestsDeployingOnLine.class.getResource(name);
    assert res!=null:"The resource "+name+" is invalid";
    var path=Path.of(res.toURI());
    var sPath=path.resolve("Secret.L42");
    var contentToken=SecretHolder.token;
    var contentRepo=SecretHolder.repo;
    var content=
      "class method S #$of()=S\""+contentToken+"\"\n"+
      "class method S repo()=S\""+contentRepo+"\"\n";
    Files.write(sPath,content.getBytes());
    try{return name(path);}
    finally{/*Files.delete(path);* /}//TODO: after 3 nov
    }
  public static Stream<L42Test> test() throws IOException, URISyntaxException {
    return fromStream(Stream.of(
      nameSecret("L42Source/TestHttpRequest"),
      nameSecret("L42Source/TestLoad"),
      nameSecret("L42Source/TestFileSystem"),
      nameSecret("L42Source/TestJavaServer"),
      nameSecret("L42Source/TestRawQuery"),
      nameSecret("L42Source/TestQuery"),
      nameSecret("L42Source/TestGuiBuilder"),      
      nameSecret("L42Source/TestSifoLib"),
      nameSecret("L42Source/TestUnit"),
      nameSecret("L42Source/TestVoxelMap")
      ));}
  }
*/