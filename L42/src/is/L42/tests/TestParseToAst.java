package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.Parse;
import is.L42.generated.Full;
import is.L42.tools.AtomicTest;
import is.L42.visitors.FullL42Visitor;

import static is.L42.tests.TestHelpers.*;
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
   fail("Void.This3","This","Void","Error")
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
   pass("(\n  imm A _=A\n  x\n  )\n")
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
   pass("({interface #norm{hey, I can write stuff here}})")
   /*),new AtomicTest(()->
   pass("({@Bar T f #norm{hey, I can write stuff here}})","[|{|Header()FullF(doct(P)x)info|}|]|")
   ),new AtomicTest(()->
   pass("({mut @Bar T f })","[|{|Header()FullF(t(|docP)x)|}|]|")

   ),new AtomicTest(()->
   pass("{interface mut@Foo Bar fName}","{|Header(|)FullF(t(|docP)x)|}|")
   ),new AtomicTest(()->
   pass("{interface [mut@Foo Bar] E fName}",
   "{|Header(||t(|docP)|)FullF(t(P)x)|}|")
   ),new AtomicTest(()->
   pass("{[mut@Foo Bar] E fName}",
   "{|Header(|t(|docP)|)FullF(t(P)x)|}|")
   ),new AtomicTest(()->
   pass("{E f method bar()=x}",
   "{|Header()FullF(t(P)x)FullMi(|mOp|||x)|}|")
   ),new AtomicTest(()->
   pass("{method A.B bar(C.D x)=x}",
   "{|Header()FullMH(|t(P)mOp|t(P)x|)|x|}|")
   ),new AtomicTest(()->
   pass("{CCc=x}","{|Header()FullNC(P|x)|}|")
   ),new AtomicTest(()->
   pass("(\\ void)","[|D(\\)void|]|")
   ),new AtomicTest(()->
   pass("('hi() 'This 'Is.A 'Series.of() 'Path.lits(x).x and a slash \\ End.Here)",
   "[|D(pathSel)D(pathSel)D(pathSel)D(pathSel)D(pathSel)D(x)D(x)D(x)D(\\)P|]|")
   ),new AtomicTest(()->
   pass("bar<:Foo","xCast(|t(P))|")
   ),new AtomicTest(()->
   pass("x.foo().bar()","xFCall(|m||)FCall(|m||)|")
   ),new AtomicTest(()->
   pass("x.foo(a b=c)","xFCall(|m|xx|x|)|")
   ),new AtomicTest(()->
   pass("x.bar[a;b;c=d]","xSquareCall(|m|x|x|x|x|)|")
  ),new AtomicTest(()->
   pass("S\"aa\"","P||")
  ),new AtomicTest(()->
   pass("!~!S\"aa\"","|||P||")
  ),new AtomicTest(()->
   pass("~12S","||P|")
  ),new AtomicTest(()->
   pass("~1~2!S","|||||P|")
  ),new AtomicTest(()->
   pass("a+b&&c","<<x|x>|x>|")
  ),new AtomicTest(()->
   pass("a+b&&c:d","<<x|x>|<x|x>>|")
  ),new AtomicTest(()->
   pass("a+b:c&&d","<<x|<x|x>>|x>|")
  ),new AtomicTest(()->
   pass("a in b && c>=d","<<x|x>|<x|x>>|")
  ),new AtomicTest(()->
   pass("for a in b in c x in y Foo()","SFor(|x|<x|x>x|xPFCall(||))|")
  ),new AtomicTest(()->
   pass("if a<=0N Debug(S\"hi\")","SIf(|<x||P>PFCall(|P||))|")
  ),new AtomicTest(()->
   pass("while a<=0N if z a.b() else c.d()",
   "SWhile(|<x||P>SIf(|xxFCall(|m||)|xFCall(|m||)))|")
  ),new AtomicTest(()->
   pass("for var mut x in xs x:=x*2Num",
   "SFor(|||x|xSUpdate(x|<x||P>))|")
  ),new AtomicTest(()->
   pass("loop a(C[])",
   "SLoop(|xFCall(|PSquareCall(||)|))|")
  ),new AtomicTest(()->
   pass("if a&&b return X\"oh\"",
   "SIf(|<x|x>SThrow(|P|))|")
   */
     ));}
public static void pass(String input) {pass(input,input);}

public static void pass(String input,String output) {
  var r=Parse.e("-dummy-",input);
  assertFalse(r.hasErr());
  assertEquals(output,r.res.toString());
  }
public static void fail(String input,String ...output) {
  var r=Parse.e("-dummy-",input);
  assertTrue(r.hasErr());
  String msg=r.errorsTokenizer+"\n"+r.errorsParser+"\n"+r.errorsVisitor;
  for(var s:output){assertTrue(msg.contains(s));}
  }
}
