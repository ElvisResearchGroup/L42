package is.L42.tests;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.Parse;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.X;
import is.L42.tools.AtomicTest;
import is.L42.visitors.FV;
import is.L42.visitors.FullL42Visitor;


import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.range;
import static org.junit.jupiter.api.Assertions.*;

class TestL42Bridge{
  @MethodSource
  @ParameterizedTest(name = "{index}: {0}")
  public void test(L42Test input) {input.checkPass();}
  public static Stream<L42Test> fromString(String s){return Stream.of(new L42Test(12,"AA",false),new L42Test(14,"AA",true));}
  }
class L42Test{
  int lineNumber;
  String fileName;
  boolean pass;
  L42Test(int lineNumber,String fileName,boolean pass){
    this.lineNumber=lineNumber;this.fileName=fileName;this.pass=pass;
    }
  public String toString() {return "line "+lineNumber;}
  public void checkPass() {assertTrue(pass);}
  }

public class TestL42TestBridge extends TestL42Bridge{
  public static Stream<L42Test>test(){return fromString("""
###################
#PASS    TestName
#LineNum    FileName
###################
#FAIL    TestName
#LineNum    FileName
#| any text
#| any text
#| ...
#| any text
###################
#PASS    TestName
#LineNum    FileName
  """);}

}