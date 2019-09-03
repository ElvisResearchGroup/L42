package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;

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
   pass(inCore("(var mut This0 x=void x)"))
   ),new AtomicTest(()->
   pass(inCore("(capsule This0 x=void x)"))
   ),new AtomicTest(()->
   fail(inCore("(var capsule This0 x=void x)"),"var bindings can not be ")
   ),new AtomicTest(()->
   fail(inCore("(var fwd imm This0 x=void x)"),"var bindings can not be ")
   ),new AtomicTest(()->
   fail(inCore("(var fwd mut This0 x=void x)"),"var bindings can not be ")

   ),new AtomicTest(()->
   pass("{method (that,foo)=((var y=foo void), (y=foo,void.foo(y+void)) void)}")
   ),new AtomicTest(()->
   pass("{C=(x=void x+x)}")

   ),new AtomicTest(()->
   pass("{method Void (Void that,Void foo)=foo}")
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo)=e}","Used binding is not in scope: e")
   ),new AtomicTest(()->
   fail("{method (that,foo)=e}","Used binding is not in scope: e")
   ),new AtomicTest(()->
   fail("{C=e}","Used binding is not in scope: e")
   ),new AtomicTest(()->
   pass("{method Void (Void that,Void foo)=foo.bar(\\x)}")
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo)=\\x.foo()}","term \\x can only be used inside parameters")
   ),new AtomicTest(()->
   fail("{method (that,foo)=foo+\\x.foo()}","term \\x can only be used inside parameters")
   ),new AtomicTest(()->
   fail("{C=\\x}","term \\x can only be used inside parameters")
   ),new AtomicTest(()->
   pass("{method Void (Void that,Void foo)=(var y=foo, void.foo(y+=void) void)}")
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo)=(y=foo, void.foo(y+=void) void)}","name y is not declared as var, thus it can not be updated")
   ),new AtomicTest(()->
   fail("{method (that,foo)=((var y=foo void), (y=foo,void.foo(y+=void)) void)}","name y is not declared as var, thus it can not be updated")
   ),new AtomicTest(()->
   fail("{C=(x=void x:=x)}","name x is not declared as var, thus it can not be updated")

   ),new AtomicTest(()->
   fail("((a.foo(x=a,x=b)))","duplicated name in [")
   ),new AtomicTest(()->
   fail("((a.foo(x=a,y=b,x=c)))","duplicated name in [")
   ),new AtomicTest(()->
   fail("((a.foo(a,y=b,that=c)))","duplicated name in [","'that' is already passed as first argument")
   ),new AtomicTest(()->
   fail("{method (that,foo,that)=e}","duplicated name in [")
   ),new AtomicTest(()->
   fail("{method (that,foo,this)=e}","'this' can not be used as a name")
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo,Void that)=e}","duplicated name in [")
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo,Void this)=e}","'this' can not be used as a name")
   ),new AtomicTest(()->
   fail("((@Foo.a(a,a,b)A a=y x))","duplicated name in [")

   ),new AtomicTest(()->
   fail("((A a=y A a=z x))","duplicated name in [")
   ),new AtomicTest(()->
   fail("((A a=y (A a=z x)))","binding a is internally redefined")
    ),new AtomicTest(()->
   fail("(This0 a=y catch error This0 a error x x)","binding a is internally redefined")
   ),new AtomicTest(()->
   fail("((A a=y catch T x a x))","binding a used in catch; it may not be initialized")

   ),new AtomicTest(()->
   fail("{A a=y x}","last statement does not guarantee block termination")
   ),new AtomicTest(()->
   fail("{A a=y error x}","curly block do not have any return statement")
   ),new AtomicTest(()->
   fail("{return y A b=bb error x}","dead code after the statement 0 of the block")
   ),new AtomicTest(()->
   fail("{A b=bb catch T y y error x}","catch statement 0 does not guarantee block termination")
   ),new AtomicTest(()->
   fail("(return a A a=y x)","dead code after the statement 0 of the block")

   ),new AtomicTest(()->
   fail("(if a b catch T x x x)","expression need to be enclose in block to avoid ambiguities")
   ),new AtomicTest(()->
   fail("(if a b c.foo() catch T x x x)","expression need to be enclose in block to avoid ambiguities")
   ),new AtomicTest(()->
   fail("(while a b c.foo() catch T x x x)","expression need to be enclose in block to avoid ambiguities")
   ),new AtomicTest(()->
   fail("(if a b else c c.foo() catch T x x x)","expression need to be enclose in block to avoid ambiguities")
   ),new AtomicTest(()->
   fail("(a.foo() catch T x x x)","expression need to be enclose in block to avoid ambiguities")
   ),new AtomicTest(()->
   fail("(a.foo() catch T x x x.foo() x)","expression need to be enclose in block to avoid ambiguities")

   ),new AtomicTest(()->
   fail(inCore("(This0 a=y This0 a=z x)"),"duplicated name in [")
   ),new AtomicTest(()->
   fail(inCore("(This0 a=y (This0 a=z x))"),"binding a is internally redefined")
   ),new AtomicTest(()->
   fail(inCore("(This0 a=y catch error This0 x a x)"),"binding a used in catch; it may not be initialized")


    ),new AtomicTest(()->
   fail("(This0 a=y catch error This0 b (This0 b=c void) x)","binding b is internally redefined")
   ),new AtomicTest(()->
   fail("(This0 a=y catch error This0 this error x x)","'this' can not be used as a name")
   ),new AtomicTest(()->
   fail(inCore("(This0 a=y catch error This0 b (This0 b=c void) x)"),"binding b is internally redefined")
   ),new AtomicTest(()->
   fail(inCore("(This0 a=y catch error This0 this error x x)"),"'this' can not be used as a name")

   ),new AtomicTest(()->
   pass("(Any a={} void)")

   ),new AtomicTest(()->
   fail("{ method m()=(Any a={} void)}","Method body can not contain a full library literal")
   ),new AtomicTest(()->
   fail("{ class method Void m()=(Any a={} void)}","Method body can not contain a full library literal")
   
   ),new AtomicTest(()->
   fail("{ fwd imm method Void m()}","method modifier can not be fwd imm or fwd mut")
   ),new AtomicTest(()->
   fail("{ method read Any m(fwd imm Any x)}","unusable fwd parameter given")
   ),new AtomicTest(()->
   fail("{ method imm Any m(fwd mut Any x)}","unusable fwd parameter given")
   ),new AtomicTest(()->
   fail("{ method fwd mut Any m(mut Any x)}","invalid fwd return type since there is no fwd parameter")

   ),new AtomicTest(()->
   fail("{ fwd imm method Void m() #norm{}}","method modifier can not be fwd imm or fwd mut")
   ),new AtomicTest(()->
   fail("{ method read Any m(fwd imm Any x) #norm{}}","unusable fwd parameter given")
   ),new AtomicTest(()->
   fail("{ method imm Any m(fwd mut Any x) #norm{}}","unusable fwd parameter given")
   ),new AtomicTest(()->
   fail("{ method fwd mut Any m(mut Any x) #norm{}}","invalid fwd return type since there is no fwd parameter")

   ),new AtomicTest(()->
   pass("{ method Any m(Any x)=Any<:class Any.meth(a=x, b=x) #norm{}}")

   ),new AtomicTest(()->
   fail("{ method Any m(capsule Any x)=Any<:class Any.meth(a=x, b=x) #norm{}}","capsule binding "," used more then once")

   ),new AtomicTest(()->
   fail("{ method Any m(capsule Any x)=Any<:class Any.meth(a=z, b=x) #norm{}}","Used binding is not in scope: z")

   ),new AtomicTest(()->
   fail("if (x,y)=z z.foo()","invalid 'if match': no type selected in (x, y)=z")
   ),new AtomicTest(()->
   pass("if (x, Any y)=z z.foo()")


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
    return;
    }
  Assertions.fail();
  }
}