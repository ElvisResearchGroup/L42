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
   /*),new AtomicTest(()->
   pass("Any.Foo","P|")//Any/Thisn will be handled by well formedness
   ),new AtomicTest(()->
   pass("A(B)","PFCall(|P|)|")   
   ),new AtomicTest(()->
   pass("A(B)(C)","PFCall(|P|)FCall(|P|)|")   
   ),new AtomicTest(()->
   pass("A(B x=C)","PFCall(|Px|P|)|")   
   ),new AtomicTest(()->
   pass("(A)","[|P|]|")   
   ),new AtomicTest(()->
   pass("(A(B))","[|PFCall(|P|)|]|")   
   ),new AtomicTest(()->
   pass("(A(B) C(D))","[|D(PFCall(|P|))PFCall(|P|)|]|")   
   ),new AtomicTest(()->
   pass("(A (B) C(D))","[|D(P)D([|P|])PFCall(|P|)|]|")
   ),new AtomicTest(()->
   pass("(A (B) C (D))","[|D(P)D([|P|])D(P)[|P|]|]|")
   ),new AtomicTest(()->
   pass("(A x=A x)","[|D(t(P)x|P)x|]|")
   ),new AtomicTest(()->
   pass("( x=A x)","[|D(x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@Foo A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@Foo @Bar A x=A x)","[|D(t(docdocP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@Foo{some text like a comment @Bar.foo(} A x=A x)","[|D(t(docP)x|P)x|]|")
   //here it just stops pathSel at @Bar
   ),new AtomicTest(()->
   pass("(@Foo{some text like a comment @Bar.foo(x,y) and more text} A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@Foo.foo() A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@Foo.foo(x) A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@Foo.foo(x y) A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@Foo.foo(x,y) A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@Foo(x,y) A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@Foo() A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@foo() A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@foo(x) A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@() A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@(x) A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(@(x,y) A x=A x)","[|D(t(docP)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(read A x=A x)","[|D(t(|P)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(read x=A x)","[|D(|x|P)x|]|")
   ),new AtomicTest(()->
   pass("(_=A x)","[|D(||P)x|]|")
   ),new AtomicTest(()->
   pass("(imm A _=A x)","[|D(t(|P)||P)x|]|")
   ),new AtomicTest(()->
   pass("(mut _=A x)","[|D(|||P)x|]|")
   ),new AtomicTest(()->
   pass("(fwd imm A x=A x)","[|D(t(|P)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(fwd mut x=A x)","[|D(|x|P)x|]|")
   ),new AtomicTest(()->
   pass("(fwd imm A _=A x)","[|D(t(|P)||P)x|]|")
   ),new AtomicTest(()->
   pass("(fwd mut _=A x)","[|D(|||P)x|]|")
   ),new AtomicTest(()->
   pass("(var A x=A x)","[|D(|t(P)x|P)x|]|")
   ),new AtomicTest(()->
   pass("(A(A y, A z) x=A x)",//note the x=, thus it can not be a matching
   "[|D(P)D([|D(P)D(x)D(P)x|])D(x|P)x|]|")
   ),new AtomicTest(()->
   pass("((A y, A z) x=A x)","[|D([|D(P)D(x)D(P)x|])D(x|P)x|]|")
   ),new AtomicTest(()->
   pass("(A(A y, A z)=A x)","[|D(t(P)|t(P)xt(P)x||P)x|]|")
   ),new AtomicTest(()->
   pass("((A y, A z)=A x)","[|D(|t(P)xt(P)x||P)x|]|")
   ),new AtomicTest(()->
   pass("((y,z)=A x)","[|D(|xx||P)x|]|")
   ),new AtomicTest(()->
   pass("((y)=A x)","[|D(|x||P)x|]|")
   ),new AtomicTest(()->
   pass("((var y)=A x)","[|D(||x||P)x|]|")
   ),new AtomicTest(()->
   pass("((z, var y)=A x)","[|D(|x|x||P)x|]|")
   ),new AtomicTest(()->
   pass("((z, var A y)=A x)","[|D(|x|t(P)x||P)x|]|")
   ),new AtomicTest(()->
   pass("(A catch return A x x y)","[|D(P)K(||t(P)xx)x|]|")
   ),new AtomicTest(()->
   pass("(A catch return A x x)","[|D(P)K(||t(P)xx)|]|")
   ),new AtomicTest(()->
   pass("(A catch return A _ x)","[|D(P)K(||t(P)|x)|]|")
   ),new AtomicTest(()->
   pass("(A catch return A _ x catch error A x y z)",
   "[|D(P)K(||t(P)|x)K(||t(P)xx)x|]|")
   ),new AtomicTest(()->
   pass("(A catch return A _ x catch error A x y z)",
   "[|D(P)K(||t(P)|x)K(||t(P)xx)x|]|")
   ),new AtomicTest(()->
   pass("(A whops B C D z)",
   "[|D(P)D(x)D(P)D(P)D(P)x|]|")//well formedness will check for those issues
   ),new AtomicTest(()->
   pass("(A whoops B C D x)",
   "[|D(P)Whoops(|t(P)t(P)t(P))x|]|")
   ),new AtomicTest(()->
   pass("(A whoops B C D)",
   "[|D(P)Whoops(|t(P)t(P)t(P))|]|")
   ),new AtomicTest(()->
   pass("{A whoops B C D}",
   "[|D(P)Whoops(|t(P)t(P)t(P))|]|")
   ),new AtomicTest(()->
   pass("{x=A catch T _ x y Z}",
   "[|D(x|P)K(|t(P)|x)D(x)D(P)|]|")
   ),new AtomicTest(()->
   pass("{}","{|Header()|}|")
   ),new AtomicTest(()->
   pass("{T x=void x}","[|D(t(P)x|void)D(x)|]|")
   ),new AtomicTest(()->
   pass("{T x=void}","[|D(t(P)x|void)|]|")
   ),new AtomicTest(()->
   pass("{T x}","{|Header()FullF(t(P)x)|}|")
   ),new AtomicTest(()->
   pass("{T x y}","[|D(P)D(x)D(x)|]|")
   ),new AtomicTest(()->
   pass("{T x T y}",
   "{|Header()FullF(t(P)x)FullF(t(P)x)|}|")
   ),new AtomicTest(()->
   pass("({})","[|{|Header()|}|]|")
   ),new AtomicTest(()->
   pass("({interface #norm{hey, I can write stuff here}})","[|{|Header(|)info|}|]|")
   ),new AtomicTest(()->
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
public static void pass(String input,String output) {
  var r=Parse.e(input);
  assertFalse(r.hasErr());
  Full.E e=new FullL42Visitor("-dummy-").visitNudeE(r.res);
  assertEquals(output,e.toString());
  }
}
