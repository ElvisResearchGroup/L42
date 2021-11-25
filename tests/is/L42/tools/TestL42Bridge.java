package is.L42.tools;

import static is.L42.tools.General.L;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import is.L42.common.PerfCounted;
import is.L42.platformSpecific.javaTranslation.Resources;

public class TestL42Bridge extends PerfCounted {
  static final String separator = "<<<<STRCMP>>>>";
  @MethodSource
  @ParameterizedTest(name = "{index}: {0}")
  public void test(L42Test input) {input.checkPass();}
  public static Stream<L42Test> fromString(String s) {
    List<String> strings = L(s.lines());
    List<L42Test> tests = new ArrayList<>(); 
    for(String t : strings){
      assert t.length()!=0 && t.startsWith("#"):
        "InvalidTestOutput\nInvalid line `"+t+"`.\nEach line in test descriptor should start with a '#'"; 
      t = t.substring(1);
      if(isSep(t)){tests.add(new L42Test());continue;}
      var current=tests.get(tests.size() - 1);
      if(t.startsWith("Pass") || t.startsWith("Fail")){
        assert current.testName == null:
          "InvalidTestOutput\nRepeated [Pass/Fail] [Test Name] declaration:"+t;
        current.pass = t.startsWith("Pass");
        current.testName = t.substring(t.lastIndexOf(' ') + 1);
        continue;    
        }
      if(t.startsWith("line:")) {
        assert current.fileName == null:
          "InvalidTestOutput\nRepeated Line: [Line#] [File Name] declaration.";
        t = t.substring(6);
        current.lineNumber = Integer.parseInt(t.substring(0, t.indexOf(' ')));
        current.fileName = t.substring(t.lastIndexOf(' ') + 1);
        continue;
        }
      if(t.equals("Bool") || t.equals("StrCompare")){
        current.type = t;
        continue;
        }
      if(t.equals("Expected")){
        current.message += separator;
        continue;
        }
      if(t.charAt(0) == '|'){
        current.message += t.substring(1) + "\n";
        continue;
        }
      assert t.equals("Actual"):
        "InvalidTestOutput\nline `" + t + "` does not conform to any known specification.";
      }
    return tests.stream();
    }
  public static String name(Path name)throws IOException, URISyntaxException{
    Resources.clearResKeepReuse();
    is.L42.main.Main.main(name.toString());
    return Resources.tests();
    }
  public static Stream<L42Test> fromStream(Stream<String> ss){
    return ss.flatMap(s->{
      if(!s.isEmpty()){ return fromString(s); }
      return Stream.of(new TestL42Bridge.L42TestNoTests(s));      
      });
    }
  private static boolean isSep(String str) {
    for(char c : str.toCharArray()) { 
      if(c != '#' && c != ' ' && c != '\n') { return false; }
      }
    return true;
    } 
  public static class L42TestNoTests extends L42Test{
    public L42TestNoTests(String fileName){
      this.testName="No Tests executed in file "+fileName;
      this.fileName=fileName;
      }
    }
  public static class L42Test{
    int lineNumber;
    String type;
    String testName;
    String fileName;
    String message = "";
    boolean pass;  
    L42Test() {}  
    public String toString() { return testName + ": line " + lineNumber + " in " + fileName ; }
    public void checkPass() {
      if(pass || type == null) { assertTrue(pass); return; }
      switch(type) {
        case "Bool": assertTrue(pass, message); break;
        case "StrCompare":
          String[] expactual = message.split(TestL42Bridge.separator);
          assertEquals(expactual[1], expactual[0]);
          break;
        default:
          throw new NullPointerException("Type " + type + " is invalid.");
        }
      }
    }
  }
/*//example of use
public class TestL42TestBridge extends TestL42Bridge {
  public static Stream<L42Test> test() {return fromString("""
###############
#Pass TestName
#line: 12   fileName
###############
#Fail TestName
#line: 12   fileName
#Bool
#|message
#|
###############
#Fail TestName
#line: 12   fileName
#StrCompare
#Actual
#|foo(
#|  bar
#|  )
#Expected
#|expectede
""");
  }
}
*/