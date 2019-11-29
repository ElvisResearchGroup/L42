package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.generated.Core.Throw;
import is.L42.generated.Full;
import is.L42.generated.ThrowKind;
import is.L42.tools.AtomicTest;
import is.L42.visitors.FullL42Visitor;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestWellFormedness
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("void")
   ),new AtomicTest(()->
   pass("((var mut A x=void x))")
   ),new AtomicTest(()->
   pass("((capsule A x=void x))")
   ),new AtomicTest(()->
   fail("((var capsule A x=void x))",Err.varBindingCanNotBe("capsule"))
   ),new AtomicTest(()->
   fail("((var fwd imm A x=y x))",Err.varBindingCanNotBe("fwd imm"))
   ),new AtomicTest(()->
   fail("((var fwd mut A x=y x))",Err.varBindingCanNotBe("fwd mut"))
   ),new AtomicTest(()->
   fail("(((var capsule a)=y x))",Err.varBindingCanNotBe("capsule"))
   ),new AtomicTest(()->
   pass("(((capsule a)=void void))")
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
   fail("{C=(Void+void)}",Err.noOperatorOnPrimitive(hole,"Plus"))

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
   fail("{method Void (Void that,Void foo)=(y=foo, void.foo(y+=void) void)}",Err.nonVarBindingOpUpdate(hole, hole))
   ),new AtomicTest(()->
   fail("{method (that,foo)=((var y=foo void), (y=foo,void.foo(y+=void)) void)}",Err.nonVarBindingOpUpdate(hole, hole))
   ),new AtomicTest(()->
   fail("{C=(x=void x:=x)}",Err.nonVarBindingOpUpdate(hole, hole))

   ),new AtomicTest(()->
   fail("{fwd imm Void f }",Err.invalidFieldType(hole))

   ),new AtomicTest(()->
   pass("{... C={}}")
   ),new AtomicTest(()->
   pass("{reuse[GG] C={}}")
   
   ),new AtomicTest(()->
   fail("{... Void f @{hei!}}",Err.noDocWithReuseOrDots("...", "[@{hei!}]"))
   ),new AtomicTest(()->
   fail("{... Void f }",Err.invalidMemberWithReuseOrDots("...", "[Void f]"))
   ),new AtomicTest(()->
   fail("{reuse[GG] Void f @{hei!}}",Err.noDocWithReuseOrDots(hole, "[@{hei!}]"))
   ),new AtomicTest(()->
   fail("{reuse[GG] Void f }",Err.invalidMemberWithReuseOrDots(hole, "[Void f]"))
   ),new AtomicTest(()->
   fail("{reuse[GG] method foo(x)=void }",Err.invalidMemberWithReuseOrDots(hole, hole))


   ),new AtomicTest(()->
   fail("((void.foo(x=void,x=void)))",Err.duplicatedName("[x]"))
   ),new AtomicTest(()->
   fail("((void.foo(x=void,y=void,x=void)))",Err.duplicatedName("[x]"))
   ),new AtomicTest(()->
   fail("((void.foo(void,y=void,that=void)))",Err.duplicatedNameThat())
   ),new AtomicTest(()->
   fail("{method (that,foo,that)=void}",Err.duplicatedName(hole))
   ),new AtomicTest(()->
   fail("{method (that,foo,this)=void}",Err.duplicatedNameThis())
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo,Void that)=e}",Err.duplicatedName(hole))
   ),new AtomicTest(()->
   fail("{method Void (Void that,Void foo,Void this)=e}",Err.duplicatedNameThis())
   ),new AtomicTest(()->
   fail("((@Foo.a(a,a,b)A a=y x))",Err.duplicatedName(hole))

   ),new AtomicTest(()->
   fail("((A a=void A a=void void))",Err.duplicatedName(hole))
   ),new AtomicTest(()->
   fail("((A a=void (A a=void void)))",Err.redefinedName("a"))
   ),new AtomicTest(()->
   fail("(This0 a=void catch error This0 a error void void)",Err.redefinedName("a"))
   ),new AtomicTest(()->
   pass("(var This0 a=void a:=void catch error This0 b error void void)")
   ),new AtomicTest(()->
   fail("(var This0 a=void (a:=void catch error This0 b error void void))",Err.errorVarBindingOpUpdate(hole, hole))  
   ),new AtomicTest(()->
   fail("((A a=void catch T x a x))",Err.nameUsedInCatchOrMatch("a"))
   ),new AtomicTest(()->
   fail("{A a=void void.foo()}","last statement does not guarantee block termination")
   ),new AtomicTest(()->
   fail("{A a=void error void}","curly block do not have any return statement")
   ),new AtomicTest(()->
   fail("{return void A b=void error void}","dead code after the statement 0 of the block")
   ),new AtomicTest(()->
   fail("error error x",Err.deadThrow(ThrowKind.Error))
   ),new AtomicTest(()->
   fail("error exception x",Err.deadThrow(ThrowKind.Error))
   ),new AtomicTest(()->
   fail("exception error x",Err.deadThrow(ThrowKind.Exception))
   ),new AtomicTest(()->
   fail("{A b=void catch T y y error void}","catch statement 0 does not guarantee block termination")
   ),new AtomicTest(()->
   fail("(return void A a=void void)","dead code after the statement 0 of the block")
   ),new AtomicTest(()->
   fail("(if void void catch T x x.foo() void)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail("(if void void void.foo() catch T x x.foo() void)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail("(while void void void.foo() catch T x x.foo() void)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail("(if void void else void void.foo() catch T x x.foo() void)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail("(void.foo() catch T x x.foo() void)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail("(void.foo() catch T x x.foo() void.foo() void)",Err.needBlock(hole))
   ),new AtomicTest(()->
   fail(inCore("(This0 a=void This0 a=void void)"),Err.duplicatedName(hole))
   ),new AtomicTest(()->
   fail(inCore("(This0 a=void (This0 a=void void))"),Err.redefinedName("a"))
   ),new AtomicTest(()->
   fail(inCore("(This0 a=void catch error This0 x a void)"),Err.nameUsedInCatchOrMatch(hole))
    ),new AtomicTest(()->
   fail("(This0 a=void catch error This0 b (This0 b=void void) void)",Err.redefinedName("b"))
   ),new AtomicTest(()->
   fail("(This0 a=void catch error This0 this error void void)",Err.duplicatedNameThis())
   ),new AtomicTest(()->
   fail(inCore("(This0 a=void catch error This0 b (This0 b=void void) void)"),Err.redefinedName("b"))
   ),new AtomicTest(()->
   fail(inCore("(This0 a=void catch error This0 this error x x)"),Err.redefinedName("this"))
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
   pass("{ method fwd mut Any m(fwd mut Any x, mut Any y) #norm{}}")
   ),new AtomicTest(()->
   pass("{ method fwd imm Any m(fwd imm Any x, mut Any y) #norm{}}")
   ),new AtomicTest(()->
   fail("{ method fwd imm Any m(fwd mut Any x, mut Any y) #norm{}}",Err.methodTypeNoFwdPar(hole))
   ),new AtomicTest(()->
   pass("{ method Any m(Any x)=Any<:class Any.meth(a=x, b=x) #norm{}}")
   ),new AtomicTest(()->
   fail("{ method Any m(capsule Any x)=Any<:class Any.meth(a=x, b=x) #norm{}}",Err.capsuleBindingUsedOnce(hole))
   ),new AtomicTest(()->
   fail("{ method capsule Any m()=(capsule Any x=this.m() capsule Any y=x x) #norm{}}",Err.capsuleBindingUsedOnce(hole))
   ),new AtomicTest(()->
   pass("{ method capsule Any m()=(capsule Any x=this.m() catch error This z this x) #norm{}}")
   ),new AtomicTest(()->
   fail("{ method capsule Any m()=(Any x=this.m() catch return capsule This y y.foo(that=y) x) #norm{}}",Err.capsuleBindingUsedOnce(hole))


   ),new AtomicTest(()->
   fail("{ method Any m(capsule Any x)=Any<:class Any.meth(a=z, b=x) #norm{}}",Err.nameUsedNotInScope("z"))
   ),new AtomicTest(()->
   fail("if (x,y)=void void.foo()","invalid 'if match': no type selected in (x, y)=void")
   ),new AtomicTest(()->
   pass("if (x, Any y)=void void.foo()")
   ),new AtomicTest(()->
   pass("for var z in void, (x,y) in void void")
   ),new AtomicTest(()->
   fail("for var z in void, (x,var y) in void void",Err.forMatchNoVar("y"))
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
   fail("(void, void void void)",Err.degenerateStatement(hole))

  ));}
public static String inCore(String s){
  return "{imm method imm Void a()="+s+" #norm{}}";
  }
public static void pass(String input){
  var r=Parse.e(Constants.dummy,input);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  assertTrue(r.res.wf());
  }
public static void fail(String input,String ...output){
  var r=Parse.e(Constants.dummy,input);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  TestHelpers.checkFail(()->r.res.wf(), output, EndError.NotWellFormed.class);
  //for(var s:output){if(!msg.contains(s)){throw nwf;}}
  //for(var s:output){assertTrue(msg.contains(s));}
  }
}