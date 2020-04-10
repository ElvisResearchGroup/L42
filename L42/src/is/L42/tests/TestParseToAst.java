package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;

import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.generated.Full;
import is.L42.tools.AtomicTest;
import is.L42.visitors.FullL42Visitor;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.range;
import static org.junit.jupiter.api.Assertions.*;

public class TestParseToAst
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass(" foo ","foo")
   ),new AtomicTest(()->
   pass("Bar.Baz","Bar.Baz")
   ),new AtomicTest(()->
   fail("Any.Foo","Any","Error")//Any/Thisn handled now
   ),new AtomicTest(()->
   fail("Foo.This3","This","Error")
   ),new AtomicTest(()->
   fail("Void.This3",Err.notValidC("Void"))
   ),new AtomicTest(()->
   pass("A(B)","A(B)")   
   ),new AtomicTest(()->
   pass("!A(B)","!A(B)")   
   ),new AtomicTest(()->
   pass("~!!A(B)","~!!A(B)")   
   ),new AtomicTest(()->
   pass("12N","12N")   
   ),new AtomicTest(()->
   pass("12 12 13 N","12 12 13N")   
   ),new AtomicTest(()->//asserting parse roundtrip identical from now on
   pass("~!12~N")   
   ),new AtomicTest(()->
   pass("A<:B")   
   ),new AtomicTest(()->
   pass("A<:mut B")   
   ),new AtomicTest(()->
   pass("A<:mut@C.D B")
   ),new AtomicTest(()->
   pass("A<:mut@C.D{}B")
   ),new AtomicTest(()->
   pass("A<:mut@C.D{hi}B")
   ),new AtomicTest(()->
   pass("A<:mut@C.D{@C}B")
   ),new AtomicTest(()->
   pass("A<:mut@C.D{{}@C}B")
   ),new AtomicTest(()->
   pass("A<:mut@C.D{hi with spaces \n and new lines \n\n bye!}B")
   ),new AtomicTest(()->
   pass("A<:mut@C.D{hi {with nest}ed curlie\n{\ns}}B")
   ),new AtomicTest(()->
   pass("A<:mut@C.D{hi @DD{with @D nest}ed @Dcurlie@D\n{@D\ns}}B")
   ),new AtomicTest(()->
   pass("A<:mut@C.D{hi @DD{with @D nest}ed @Dcurlie@D\n{@D\ns}@D}B")
   ),new AtomicTest(()->
   pass("A(B)(C)")   
   ),new AtomicTest(()->
   pass("A(B x=C)","A(B, x=C)")   
   ),new AtomicTest(()->
   pass("A(B, x=C)")   
   ),new AtomicTest(()->
   pass("A[B, x=C]")   
   ),new AtomicTest(()->
   pass("A[B, x=C; C; D]")
   ),new AtomicTest(()->
   pass("A.foo[B, x=C]")
   ),new AtomicTest(()->
   pass("a+b+c")
   ),new AtomicTest(()->
   pass("a:b:c.f(D)")
   ),new AtomicTest(()->
   pass("(A)")   
   ),new AtomicTest(()->
   pass("(A(B))")   
   ),new AtomicTest(()->
   pass("(\n  A(B)\n  C(D)\n  )\n")   
   ),new AtomicTest(()->
   pass("(A (Void) C(Library))","(\n  A\n  (Void)\n  C(Library)\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A\n  (B)\n  C(D)\n  )\n")
   ),new AtomicTest(()->
   pass("(A (B) C (D))","(\n  A\n  (B)\n  C\n  (D)\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A\n  (B)\n  C\n  (D)\n  )\n")
   ),new AtomicTest(()->
   pass("(A x=A x)","(\n  A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("( x=A x)","(\n  x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(@Foo A x=A x)","(\n  @Foo A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  @Foo@Bar A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   {try{pass("(A a=A()(B))");Assert.fail();}catch(EndError.NotWellFormed nwf){}}
   ),new AtomicTest(()->
   {try{pass("(A a=A() (B))");Assert.fail();}catch(EndError.NotWellFormed nwf){}}
   ),new AtomicTest(()->
   pass("(A a=A()    (B))","(\n  A a=A()\n  (B)\n  )\n")
   ),new AtomicTest(()->
   fail("(\n  @Foo{some text like a comment @Bar.foo(} A x=A\n  x\n  )\n","Error","doc")
   ),new AtomicTest(()->
   pass("(\n  @Foo{some text like a comment @Bar.foo(x,y) and more text}A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(@Foo.foo() A x=A x)","(\n  @Foo.foo()A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  @Foo.foo(x)A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  @Foo.foo(x y)A x=A\n  x\n  )\n","(\n  @Foo.foo(x,y)A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  @Foo.foo(x,y)A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(@Foo(x,y) A x=A x)","(\n  @Foo(x,y)A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  @Foo()A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  @foo()A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  @foo(x)A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  @()A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  @(x)A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  @(x,y)A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(read A x=A x)","(\n  read A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  read A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  read x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  _=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A _=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  mut _=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  fwd imm A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  fwd mut x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  fwd imm A _=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  fwd mut _=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  var A x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(A(A y, A z) x=A x)",//note the x=, thus it can not be a matching
   "(\n  A\n  (\n    A\n    y\n    A\n    z\n    )\n  x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  (\n    A\n    y\n    A\n    z\n    )\n  x=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A(A y, A z)=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(A(A y A z)=A x)","(\n  A(A y, A z)=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  (A y, A z)=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  (y, z)=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  (y)=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  (var y)=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  (z, var y)=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  (z, var A y)=A\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A\n  catch return A x x\n  y\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A\n  catch return A x x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A\n  catch return A _ x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A\n  catch return A _ x\n  catch error A x y\n  z\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A\n  whoops B, C, D\n  x\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  A\n  whoops B, C, D\n  )\n")
   ),new AtomicTest(()->
   pass("{\n  A\n  whoops B, C, D\n  }\n")
   ),new AtomicTest(()->
   pass("{\n  x=A\n  catch T _ x\n  y\n  Z\n  }\n")
   ),new AtomicTest(()->
   pass("{}","{}")
   ),new AtomicTest(()->
   pass("{T x=void x}","{\n  T x=void\n  x\n  }\n")
   ),new AtomicTest(()->
   pass("{\n  T x=void\n  }\n")
   ),new AtomicTest(()->
   pass("{T x}")
   ),new AtomicTest(()->
   pass("{T x y}","{\n  T\n  x\n  y\n  }\n")
   ),new AtomicTest(()->
   pass("{T x T y}")
   ),new AtomicTest(()->
   pass("({})")
   ),new AtomicTest(()->
   fail("({interface #norm{hey, I can write stuff here}})","Error","expecting {'}'")
   ),new AtomicTest(()->
   pass("({interface #norm{}})")
   ),new AtomicTest(()->
   pass("({interface #typed{}})")
   ),new AtomicTest(()->
   pass("({interface #norm{typeDep=This.A, This2.B, This1.C}})")
   ),new AtomicTest(()->
   pass("({interface #norm{coherentDep=This1.C}})")
   ),new AtomicTest(()->
   pass("({interface #norm{watched=This1.C}})")
   ),new AtomicTest(()->
   pass("({interface #norm{usedMethods=This1.C.foo(x,y)}})")
   ),new AtomicTest(()->
   pass("({interface #norm{hiddenSupertypes=This1.C}})")
   ),new AtomicTest(()->
   pass("({interface #norm{refined=bar(x,y)}})")
   ),new AtomicTest(()->
   pass("({interface #norm{watched=This1.C}})")
   ),new AtomicTest(()->
   pass("({interface #norm{}})")

   ),new AtomicTest(()->
   pass("({method Any m()={#norm{}}#norm{}})")
   ),new AtomicTest(()->
   pass("({C={#norm{}}#norm{}})")
   ),new AtomicTest(()->
   fail("({method Any m()={} #norm{}})",Err.malformedCoreFullL()+Err.hole+Err.malformedCoreMWT("#norm{}","[m()]"))
   ),new AtomicTest(()->
   fail("({C={} #norm{}})",Err.malformedCoreNC(Err.hole))

   ),new AtomicTest(()->
   pass("{method Any m(capsule Any x)=Any<:class Any.meth(a=x, b=x)#norm{}}")
   ),new AtomicTest(()->
   fail("{ method Any m(capsule Any x)=Any.meth(a=x, b=x) #norm{}}",Err.malformedCoreMWT(Err.hole,Err.hole))
   ),new AtomicTest(()->
   pass("({@This.Bar method This.T f()#norm{}})")
   ),new AtomicTest(()->
   pass("({mut@Bar T f})")
   ),new AtomicTest(()->
   pass("{interface mut@Foo Bar fName}")
   ),new AtomicTest(()->
   pass("{interface[mut@Foo Bar]E fName}")//well formedness will take care of it
   ),new AtomicTest(()->
   pass("{[@Foo Bar]E fName}")
   ),new AtomicTest(()->
   pass("{E f method bar()=x}")
   ),new AtomicTest(()->
   pass("{method A.B bar(C.D x)=x}")
   
   ),new AtomicTest(()->
   pass("{E f method+()=x}")
   ),new AtomicTest(()->
   pass("{method A.B+(C.D x)=x}")

   ),new AtomicTest(()->
   pass("{E f method+1()=x}")
   ),new AtomicTest(()->
   pass("{method A.B+1(C.D x)=x}")

   ),new AtomicTest(()->
   fail("{E f method []()=x}","","no viable alternative at input 'method [")
   ),new AtomicTest(()->
   fail("{method A.B Void(C.D x)=x}","","no viable alternative at input 'Void'")

   ),new AtomicTest(()->
   pass("{E f method in()=x}")
   ),new AtomicTest(()->
   pass("{method A.B in(C.D x)=x}")

   ),new AtomicTest(()->
   pass("{E f method in1()=x}")//in1 identifier , not op 1
   ),new AtomicTest(()->
   pass("{method A.B in1(C.D x)=x}")

   ),new AtomicTest(()->
   pass("{E f method #in0()=x}")
   ),new AtomicTest(()->
   pass("{method A.B #in1(C.D x)=x}")
   
   ),new AtomicTest(()->
   pass("{method A if(B x)=x.if(x)}")
   ),new AtomicTest(()->
   pass("{method A mut(B x)=x.imm(x)}")
   ),new AtomicTest(()->
   fail("{method A fwd imm(B x)=x.imm(x)}",Err.invalidMethodName("fwd imm"))

   ),new AtomicTest(()->
   pass("{method A if(B x)=native{hi this is a native}error void}")
   ),new AtomicTest(()->
   pass("""
   {method A if(B x)=native{hi 
   this is a 
   native}error void}""")
   ),new AtomicTest(()->
   pass("""
   {method A if(B x)=native{hi 
   {t{hi}}s is{} a 
   native}error void}""")

   ),new AtomicTest(()->
   pass("{CCc=x}")
   ),new AtomicTest(()->
   pass("(\\ void)","(\n  \\\n  void\n  )\n")
   ),new AtomicTest(()->
   pass("(\n  'hi()\n  'This\n  'Is.A\n  'Series.of()\n  'Path.lits(x).x\n  and\n  a\n  slash\n  \\\n  End.Here\n  )\n")
   ),new AtomicTest(()->
   pass("bar<:Foo")
   ),new AtomicTest(()->
   pass("x.foo().bar()")
   ),new AtomicTest(()->
   pass("x.foo(a, b=c)")
   ),new AtomicTest(()->
   pass("x.bar[a; b; c=d]")
  ),new AtomicTest(()->
   pass("S\"aa\"")
  ),new AtomicTest(()->
   pass("!~!S\"aa\"")
  ),new AtomicTest(()->
   pass("~12S")
  ),new AtomicTest(()->
   pass("~1~2!S")
  ),new AtomicTest(()->
   pass("""
     S"aa"
     """.trim())
     ),new AtomicTest(()->
   pass("""
     S\"""
       |aa
       \"""
     """.trim())
     ),new AtomicTest(()->
   pass("""
     S\"""%
       |aa
       |bbb
       \"""
     """.trim())
     ),new AtomicTest(()->
   pass("""
     S\"""%
       |aa%foo cc
       |bbb %a.b() dd
       \"""
     """.trim())
     ),new AtomicTest(()->
   pass("""
     S\"""%%%
       |aa%%%foo cc
       |bbb %%%a.b() dd
       |bbb %%a.b() dd
       |bbb %a.b() dd
       |bbb a.b() dd
       \"""
     """.trim())
  ),new AtomicTest(()->
   pass("a+b&&c")
  ),new AtomicTest(()->
   pass("a+b&&c:d")
  ),new AtomicTest(()->
   pass("a+b:c&&d")
  ),new AtomicTest(()->
   pass("a in b&&c>=d")
  ),new AtomicTest(()->
   pass("for a in b in c, x in y Foo()")
  ),new AtomicTest(()->
   pass("if a<=0N Debug(S\"hi\")")
  ),new AtomicTest(()->
   pass("while a<=0N if z a.b()\nelse c.d()")
  ),new AtomicTest(()->
   pass("for var mut x in xs x:=x*2Num")
  ),new AtomicTest(()->
   pass("loop a(C[])")
  ),new AtomicTest(()->
   pass("if a&&b return X\"oh\"")
  ),new AtomicTest(()->
   pass("if(a, b)=c a.foo(b)")

  ),new AtomicTest(()->
   pass("S\"oh no! ( a )) [ non trivial }} string just!! ]] in Case\"")
  ),new AtomicTest(()->
   passI("S\"%x\"","x")
  ),new AtomicTest(()->
   pass("S\"aa%x bb\"")
  ),new AtomicTest(()->
   pass("S\"aa%x.foo() bb\"")
 ),new AtomicTest(()->
   pass("S\"aa%X.foo() bb\"")
 ),new AtomicTest(()->
   passI("S\"aa%x(a, b=c.d()) bb\"","x(a, b=c.d())")
 ),new AtomicTest(()->
   pass("S\"aa%(x)bb\"")
 ),new AtomicTest(()->
   pass("S\"aa%(x[])bb\"")
 ),new AtomicTest(()->
   passI("S\"aa%x[].foo()bb\"","x[].foo()")
 ),new AtomicTest(()->
   fail("S\"aa%x[].foo().bb\"","ill formed string interpolation"," input '.bb'")
 ),new AtomicTest(()->
   passI("S\"a1%(x1)a2%(x2)a3%(x3)\"","(x1)","(x2)","(x3)")
 ),new AtomicTest(()->
   passI("S\"%(x1)%(x2)%(x3)\"","(x1)","(x2)","(x3)")
 ),new AtomicTest(()->
   passI("S\"%x1%x2%x3\"","x1","x2","x3")
 ),new AtomicTest(()->
   passI("S\"%x1%(x2)%x3%(x4)\"","x1","(x2)","x3","(x4)")
 ),new AtomicTest(()->
   passI("S\"%x1%x2.foo()[]%x3()[] ()%(x4)\"","x1","x2.foo()[]","x3()[]","(x4)")


  ));}
public static void pass(String input) {pass(input,input);}
public static void pass(String input,String output) {
  var r=Parse.e(Constants.dummy,input);
  if(r.hasErr()){throw new Error(r.errorsParser+r.errorsTokenizer+r.errorsVisitor);}
  assertFalse(r.hasErr());
  assertEquals(output,r.res.toString());
  }

public static void passI(String input,String ...outputs) {
  var r=Parse.e(Constants.dummy,input);
  if(r.hasErr()){throw new Error(r.errorsParser+r.errorsTokenizer+r.errorsVisitor);}
  assertFalse(r.hasErr());
  assertEquals(input,r.res.toString());
  Full.EString e=(Full.EString)r.res;
  assertEquals(outputs.length,e.es().size()-1);
  for(var i:range(outputs.length)){
    var ei=e.es().get(i+1);
    var oi=outputs[i];
    assertEquals(oi,ei.toString());
    }
  }
public static void fail(String input,String ...output) {
  var r=Parse.e(Constants.dummy,input);
  assertTrue(r.hasErr());
  String msg=r.errorsTokenizer+" "+r.errorsParser+" "+r.errorsVisitor;
  if(output.length==1){
    msg=msg.substring(msg.indexOf("\n")+1);
    msg=msg.substring(msg.indexOf("\n")+1);
    Err.strCmp(msg, output[0]);
    return;
    }
  for(var s:output){if(!msg.contains(s)){throw new Error(msg);}}
  for(var s:output){assertTrue(msg.contains(s));}
  }
}
