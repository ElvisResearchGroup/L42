package is.L42.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TestL42Bridge{
  @MethodSource
  @ParameterizedTest(name = "{index}: {0}")
  public void test(L42Test input) {input.checkPass();}
  public static Stream<L42Test> fromString(String s) { 
    s += "\n##################################";
    Stream<String> strings = s.lines();
    List<L42Test> tests = new ArrayList<>();
    
    strings.forEach(new Consumer<String>() {

      L42Test current = null;
      
      public void accept(String t) { 
        if(t.length() == 0 || t.charAt(0) != '#')
          return;
        t = t.substring(1);
        
        if(isSep(t)) {
          if(current == null) {
            current = new L42Test();
            } else {
            tests.add(current);
            current = new L42Test();
            }
          return;
          }
        
        if(t.startsWith("PASS") || t.startsWith("FAIL")) {
          current.pass = t.substring(0, 4).equals("PASS");
          String[] split = t.split(" ");
          current.testName = split[split.length - 1];
          return;
          }
        
        if(t.charAt(0) == '|') {
          current.message += t.substring(1) + "\n";
          return;
          }
        
        String[] split = t.split(" ");
        
        try {
          current.lineNumber = Integer.parseInt(split[0]);
          
          current.fileName = split[split.length - 1];
          } catch(NumberFormatException e) {}
        }
      
      boolean isSep(String str) {
        AtomicBoolean bool = new AtomicBoolean(true);
        str.chars().forEach((int val) -> {
          char ascii = (char) val;
          if(ascii != '#' && ascii != ' ' && ascii != '\n')
            bool.set(false);
          });
        return bool.get();
        }
    
      });
    
    return tests.stream();
    }
  }
  
class L42Test{
  int lineNumber;
  String testName;
  String fileName;
  String message = "";
  boolean pass;
  
  L42Test() {}
  L42Test(int lineNumber, String fileName, boolean pass) {
    this.lineNumber=lineNumber;this.fileName=fileName;this.pass=pass;
    }
  
  public String toString() { return testName + ": line " + lineNumber + " in " + fileName ; }
  public void checkPass() { 
    assertTrue(pass, message);
    }
  
  }

public class TestL42TestBridge extends TestL42Bridge {
  public static Stream<L42Test> test() {return fromString("""
###################
#PASS    TestName
#45    FileName
###################
#FAIL    TestName
#71    FileName
#| any text
#| any text
#| ...
#| any text
###################
#PASS    TestName
#42    FileName
  """);
  }

}