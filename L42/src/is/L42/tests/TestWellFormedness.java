package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.Parse;
import is.L42.generated.Full;
import is.L42.tools.AtomicTest;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness.NotWellFormed;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.range;
import static org.junit.jupiter.api.Assertions.*;

public class TestWellFormedness
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("foo")
   ),new AtomicTest(()->
   pass("((var mut A x=y x))")
   ),new AtomicTest(()->
   pass("((capsule A x=y x))")
   ),new AtomicTest(()->
   fail("((var capsule A x=y x))","var bindings can not be ")
   ),new AtomicTest(()->
   fail("((var fwd imm A x=y x))","var bindings can not be ")
   ),new AtomicTest(()->
   fail("((var fwd mut A x=y x))","var bindings can not be ")
   ),new AtomicTest(()->
   fail("(((var capsule a)=y x))","var bindings can not be ")
   ),new AtomicTest(()->
   pass("(((capsule a)=y x))")

   ),new AtomicTest(()->
   pass(inCore("(var mut This0 x=y x)"))
   ),new AtomicTest(()->
   pass(inCore("(capsule This0 x=y x)"))
   ),new AtomicTest(()->
   fail(inCore("(var capsule This0 x=y x)"),"var bindings can not be ")
   ),new AtomicTest(()->
   fail(inCore("(var fwd imm This0 x=y x)"),"var bindings can not be ")
   ),new AtomicTest(()->
   fail(inCore("(var fwd mut This0 x=y x)"),"var bindings can not be ")

   ),new AtomicTest(()->
   fail("((a.foo(x=a,x=b)))","duplicated parameter name in [")
   ),new AtomicTest(()->
   fail("((a.foo(x=a,y=b,x=c)))","duplicated parameter name in [")
   ),new AtomicTest(()->
   fail("((a.foo(a,y=b,that=c)))","duplicated parameter name in [","'that' is already passed as first argument")
   ),new AtomicTest(()->
   fail("{method (that,foo,that)=e}","duplicated parameter name in [")
   ),new AtomicTest(()->
   fail("{method (that,foo,this)=e}","'this' can not be used as a parameter name")
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo,Void that)=e}","duplicated parameter name in [")
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo,Void this)=e}","'this' can not be used as a parameter name")
   ),new AtomicTest(()->
   fail("((@Foo.a(a,a,b)A a=y x))","duplicated parameter name in [")


  ));}
public static String inCore(String s){
  return "{imm method imm Void a()="+s+" #norm{}}";
  }
public static void pass(String input){
  var r=Parse.e("-dummy-",input);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  assertTrue(r.res.wf());
  }
public static void fail(String input,String ...output){
  var r=Parse.e("-dummy-",input);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  try{r.res.wf();}
  catch(NotWellFormed nwf){
    String msg=nwf.getMessage();
    for(var s:output){if(!msg.contains(s)){throw nwf;}}
    for(var s:output){assertTrue(msg.contains(s));}
    }
  }
}