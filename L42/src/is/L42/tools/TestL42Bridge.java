package is.L42.tools;

import static is.L42.tools.General.L;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TestL42Bridge{
  static final String separator = "<<<<STRCMP>>>>";
  @MethodSource
  @ParameterizedTest(name = "{index}: {0}")
  public void test(L42Test input) {input.checkPass();}
  public static Stream<L42Test> fromString(String s) { 
    List<String> strings = L(s.lines());
    List<L42Test> tests = new ArrayList<>(); 
    for(String t : strings) {
      if(t.length() == 0 || t.charAt(0) != '#') { throw new Error("Each line in test descriptor should start with a '#'"); }
      t = t.substring(1);
      if(isSep(t)) { tests.add(new L42Test()); continue; }
      else if(t.startsWith("Pass") || t.startsWith("Fail")) {
        tests.get(tests.size() - 1).pass = t.substring(0, 4).equals("Pass");
        tests.get(tests.size() - 1).testName = t.substring(t.lastIndexOf(' ') + 1);      
        }
      else if(t.startsWith("line:")) {
        t = t.substring(6);
        tests.get(tests.size() - 1).lineNumber = Integer.parseInt(t.substring(0, t.indexOf(' ')));
        tests.get(tests.size() - 1).fileName = t.substring(t.lastIndexOf(' ') + 1);   
        }
      else if(t.equals("Bool") || t.equals("StrCompare")) { tests.get(tests.size() - 1).type = t; }
      else if(t.equals("Expected")) { tests.get(tests.size() - 1).message += separator; }
      else if(t.charAt(0) == '|') { tests.get(tests.size() - 1).message += t.substring(1) + "\n";}
      }
    return tests.stream();
    }
  
  private static boolean isSep(String str) {
    for(char c : str.toCharArray()) { 
      if(c != '#' && c != ' ' && c != '\n') { return false; }
      }
    return true;
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
        case "Bool": assertTrue(pass, message);
        case "StrCompare":
          String[] expactual = message.split(TestL42Bridge.separator);
          assertEquals(expactual[1], expactual[0]);
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