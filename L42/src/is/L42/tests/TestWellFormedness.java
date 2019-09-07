package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.generated.Full;
import is.L42.tools.AtomicTest;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness.NotWellFormed;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestWellFormedness
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("foo")
   ),new AtomicTest(()->
   pass("((var mut A x=y x))")
   ),new AtomicTest(()->
   pass("((capsule A x=y x))")
   ),new AtomicTest(()->
   fail("((var capsule A x=y x))",Err.varBindingCanNotBe("capsule"))
   ),new AtomicTest(()->
   fail("((var fwd imm A x=y x))",Err.varBindingCanNotBe("fwd imm"))
   ),new AtomicTest(()->
   fail("((var fwd mut A x=y x))",Err.varBindingCanNotBe("fwd mut"))
   ),new AtomicTest(()->
   fail("(((var capsule a)=y x))",Err.varBindingCanNotBe("capsule"))
   ),new AtomicTest(()->
   pass("(((capsule a)=y x))")

   ),new AtomicTest(()->
   pass(inCore("(var mut This0 x=void x)"))
   ),new AtomicTest(()->
   pass(inCore("(capsule This0 x=void x)"))
   ),new AtomicTest(()->
   fail(inCore("(var capsule This0 x=void x)"),Err.varBindingCanNotBe(hole))
   ),new AtomicTest(()->
   fail(inCore("(var fwd imm This0 x=void x)"),Err.varBindingCanNotBe(hole))
   ),new AtomicTest(()->
   fail(inCore("(var fwd mut This0 x=void x)"),Err.varBindingCanNotBe(hole))

   ),new AtomicTest(()->
   pass("{method (that,foo)=((var y=foo void), (y=foo,void.foo(y+void)) void)}")
   ),new AtomicTest(()->
   pass("{C=(x=void x+x)}")

   ),new AtomicTest(()->
   pass("{method Void (Void that,Void foo)=foo}")
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo)=e}",Err.nameUsedNotInScope("e"))
   ),new AtomicTest(()->
   fail("{method (that,foo)=e}",Err.nameUsedNotInScope("e"))
   ),new AtomicTest(()->
   fail("{C=e}",Err.nameUsedNotInScope("e"))
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
   fail("((a.foo(x=a,x=b)))",Err.duplicatedName("[x]"))
   ),new AtomicTest(()->
   fail("((a.foo(x=a,y=b,x=c)))",Err.duplicatedName("[x]"))
   ),new AtomicTest(()->
   fail("((a.foo(a,y=b,that=c)))",Err.duplicatedNameThat())
   ),new AtomicTest(()->
   fail("{method (that,foo,that)=e}",Err.duplicatedName(hole))
   ),new AtomicTest(()->
   fail("{method (that,foo,this)=e}",Err.duplicatedNameThis())
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo,Void that)=e}",Err.duplicatedName(hole))
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo,Void this)=e}",Err.duplicatedNameThis())
   ),new AtomicTest(()->
   fail("((@Foo.a(a,a,b)A a=y x))",Err.duplicatedName(hole))

   ),new AtomicTest(()->
   fail("((A a=y A a=z x))",Err.duplicatedName(hole))
   ),new AtomicTest(()->
   fail("((A a=y (A a=z x)))",Err.redefinedName("[a]"))
    ),new AtomicTest(()->
   fail("(This0 a=y catch error This0 a error x x)",Err.redefinedName("[a]"))
   ),new AtomicTest(()->
   fail("((A a=y catch T x a x))",Err.nameUsedInCatch("a"))

   ),new AtomicTest(()->
   fail("{A a=y x.foo()}","last statement does not guarantee block termination")
   ),new AtomicTest(()->
   fail("{A a=y error x}","curly block do not have any return statement")
   ),new AtomicTest(()->
   fail("{return y A b=bb error x}","dead code after the statement 0 of the block")
   ),new AtomicTest(()->
   fail("{A b=bb catch T y y error x}","catch statement 0 does not guarantee block termination")
   ),new AtomicTest(()->
   fail("(return a A a=y x)","dead code after the statement 0 of the block")

   ),new AtomicTest(()->
   fail("(if a b catch T x x x)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail("(if a b c.foo() catch T x x x)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail("(while a b c.foo() catch T x x x)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail("(if a b else c c.foo() catch T x x x)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail("(a.foo() catch T x x x)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail("(a.foo() catch T x x x.foo() x)",Err.needBlock(hole))

   ),new AtomicTest(()->
   fail(inCore("(This0 a=y This0 a=z x)"),Err.duplicatedName(hole))
   ),new AtomicTest(()->
   fail(inCore("(This0 a=y (This0 a=z x))"),Err.redefinedName("[a]"))
   ),new AtomicTest(()->
   fail(inCore("(This0 a=y catch error This0 x a x)"),Err.nameUsedInCatch(hole))


    ),new AtomicTest(()->
   fail("(This0 a=y catch error This0 b (This0 b=c void) x)",Err.redefinedName("[b]"))
   ),new AtomicTest(()->
   fail("(This0 a=y catch error This0 this error x x)",Err.duplicatedNameThis())
   ),new AtomicTest(()->
   fail(inCore("(This0 a=y catch error This0 b (This0 b=c void) x)"),Err.redefinedName("[b]"))
   ),new AtomicTest(()->
   fail(inCore("(This0 a=y catch error This0 this error x x)"),Err.duplicatedNameThis())

   ),new AtomicTest(()->
   pass("(Any a={} void)")
   ),new AtomicTest(()->
   fail("{interface[mut@Foo Bar]E fName}",Err.tsMustBeImm())
   ),new AtomicTest(()->
   fail("{ method m()=(Any a={} void)}",Err.noFullL(hole))
   ),new AtomicTest(()->
   fail("{ class method Void m()=(Any a={} void)}",Err.noFullL(hole))
   
   ),new AtomicTest(()->
   fail("{ fwd imm method Void m()}",Err.methodTypeMdfNoFwd())
   ),new AtomicTest(()->
   fail("{ method read Any m(fwd imm Any x)}",Err.methodTypeNoFwdPar(hole))
   ),new AtomicTest(()->
   fail("{ method imm Any m(fwd mut Any x)}",Err.methodTypeNoFwdPar(hole))
   ),new AtomicTest(()->
   fail("{ method fwd mut Any m(mut Any x)}",Err.methodTypeNoFwdReturn())

   ),new AtomicTest(()->
   fail("{ fwd imm method Void m() #norm{}}",Err.methodTypeMdfNoFwd())
   ),new AtomicTest(()->
   fail("{ method read Any m(fwd imm Any x) #norm{}}",Err.methodTypeNoFwdPar(hole))
   ),new AtomicTest(()->
   fail("{ method imm Any m(fwd mut Any x) #norm{}}",Err.methodTypeNoFwdPar(hole))
   ),new AtomicTest(()->
   fail("{ method fwd mut Any m(mut Any x) #norm{}}",Err.methodTypeNoFwdReturn())

   ),new AtomicTest(()->
   pass("{ method Any m(Any x)=Any<:class Any.meth(a=x, b=x) #norm{}}")

   ),new AtomicTest(()->
   fail("{ method Any m(capsule Any x)=Any<:class Any.meth(a=x, b=x) #norm{}}",Err.capsuleBindingUsedOnce(hole))

   ),new AtomicTest(()->
   fail("{ method Any m(capsule Any x)=Any<:class Any.meth(a=z, b=x) #norm{}}",Err.nameUsedNotInScope("z"))

   ),new AtomicTest(()->
   fail("if (x,y)=z z.foo()","invalid 'if match': no type selected in (x, y)=z")
   ),new AtomicTest(()->
   pass("if (x, Any y)=z z.foo()")

   ),new AtomicTest(()->
   pass("for var z in bla, (x,y) in bla beer")
   ),new AtomicTest(()->
   fail("for var z in bla, (x,var y) in bla beer",Err.forMatchNoVar("y"))

   ),new AtomicTest(()->
   fail("{method A m() method B m()}",Err.duplicatedName("[m()]"))
   ),new AtomicTest(()->
   fail("{A m method B m()}",Err.duplicatedName("[m()]"))
   ),new AtomicTest(()->
   fail("{method m()=this method B m()}",Err.duplicatedName("[m()]"))
   ),new AtomicTest(()->
   fail("{[A,B,A]}",Err.duplicatedName("[A]"))
   ),new AtomicTest(()->
   fail("{[A,B,Any]}",Err.duplicatedNameAny())
   ),new AtomicTest(()->
   pass("{method Void m::1() method B k::1() method B b::2()=this}")
   ),new AtomicTest(()->
   fail("{method Void m::1() method B k::2() method B b::2()=this}",Err.singlePrivateState("[1, 2]"))
   ),new AtomicTest(()->
   fail("{interface method Void m()=this method k()=this}",Err.methodImplementedInInterface("[m(), k()]"))
   ),new AtomicTest(()->
   pass("{C::1={#norm{}}}")
   ),new AtomicTest(()->
   fail("{C::1={}}",Err.privateNestedNotCore("C::1"))
   ),new AtomicTest(()->
   fail("{C::1={D={#norm{}} #norm{}}}",Err.privateNestedPrivateMember("D"))
   ),new AtomicTest(()->
   fail("{C::1={ method Void foo()=void #norm{}}}",Err.privateNestedPrivateMember("foo()"))
   ),new AtomicTest(()->
   pass("{C::1={ method Void foo::2()=void #norm{}}}")
   ),new AtomicTest(()->
   pass("{C::1={ method Void foo()=void #norm{refined=foo()}}}")
   ),new AtomicTest(()->
   fail("{C::1={ method Void foo()=void #norm{refined=foof()}}}",Err.privateNestedPrivateMember("foo()"))

   ),new AtomicTest(()->
   fail("(ok, this is degenerate)",Err.degenerateStatement(hole))

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