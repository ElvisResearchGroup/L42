package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.tools.AtomicTest;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness.NotWellFormed;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestProgram
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
//WellFormedness
   passWF("{method Void (Void that,Void foo)=foo}")
   ),new AtomicTest(()->
   failWF("{method Void a::1(Void that,Void foo)=foo D={C::1={}}}",Err.nonUniqueNumber("1",hole))
   ),new AtomicTest(()->
   failWF("{method Void a::1(Void that,Void foo)=foo} A={ A=void C::1={}}",Err.nonUniqueNumber("1",hole))
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo}","This0.Foo","This0.Foo")
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo}","Library","Library")
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo} A={A={}}","This1.B.C","This1.B.C")
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo} A={A={}}","This1.A.C","This0.C")
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo} A={A={}} B={B={}}","This2.B.A.C","This0.C")


  ));}
public static String inCore(String s){
  return "{imm method imm Void a()="+s+" #norm{}}";
  }
public static void minimize(String program,String pathIn,String pathOut){
  var r=Parse.program("-dummy-",program);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  assertTrue(r.res.wf());
  P pIn=P.parse(pathIn);
  P pOut=P.parse(pathIn);
  P res=r.res.minimize(pIn);
  assertEquals(pIn,pOut);
  }

public static void passWF(String input){
  var r=Parse.program("-dummy-",input);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  assertTrue(r.res.wf());
  }
public static void failWF(String input,String ...output){
  var r=Parse.program("-dummy-",input);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  try{r.res.wf();}
  catch(NotWellFormed nwf){
    String msg=nwf.getMessage();
    if(output.length==1){
      msg=msg.substring(msg.indexOf("\n")+1);
      Err.strCmp(msg, output[0]);
      return;
      }
    for(var s:output){if(!msg.contains(s)){throw nwf;}}
    for(var s:output){assertTrue(msg.contains(s));}
    return;
    }
  Assertions.fail("error expected");
  }
}