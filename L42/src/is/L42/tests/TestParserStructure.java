package is.L42.tests;

import java.util.stream.Stream;
import is.L42.tools.AtomicTest;
import static is.L42.tests.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * P for CsP, [] for blocks, | for symbols
 * */
public class TestParserStructure
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("foo","x")
   ),new AtomicTest(()->
   pass("Bar.Baz","P")
   ),new AtomicTest(()->
   pass("Any.Foo","P")//Any/Thisn will be handled by well formedness
   ),new AtomicTest(()->
   pass("A(B)",
     "E(P FCall(|Par(P)|))")   
   ),new AtomicTest(()->
   pass("A(B x=C)",
     "E(P FCall(|Par(P X|P)|))")   
   ),new AtomicTest(()->
   pass("(A)","[P]")   
   ),new AtomicTest(()->
   pass("(A(B))","[E(P FCall(|Par(P)|))]")   
   ),new AtomicTest(()->
   pass("(A(B) C(D))","[D(E(P FCall(|Par(P)|)))E(P FCall(|Par(P)|))]")   
   ),new AtomicTest(()->
   pass("(A (B) C(D))","[D(P)D([P])E(P FCall(|Par(P)|))]")
   ),new AtomicTest(()->
   pass("(A (B) C (D))","[D(P)D([P])D(P)[P]]")
   ),new AtomicTest(()->
   pass("(A x=A x)","[D(DX(TLocal(T(CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("( x=A x)","[D(DX(TLocal()X)|P)x]")
   ),new AtomicTest(()->
   pass("(@Foo A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@Foo @Bar A x=A x)","[D(DX(TLocal(T(Doc Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@Foo{some text like a comment @Bar.foo(} A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   //here it justs stops PathLit at @Bar
   ),new AtomicTest(()->
   pass("(@Foo{some text like a comment @Bar.foo(x,y) and more text} A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@Foo.foo() A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@Foo.foo(x) A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@Foo.foo(x y) A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@Foo.foo(x,y) A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@Foo(x,y) A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@Foo() A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@foo() A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@foo(x) A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@() A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@(x) A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(@(x,y) A x=A x)","[D(DX(TLocal(T(Doc CsP))X)|P)x]")

   ),new AtomicTest(()->
   pass("(read A x=A x)","[D(DX(TLocal(T(|CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(read x=A x)","[D(DX(TLocal X)|P)x]")
   ),new AtomicTest(()->
   pass("(_=A x)","[D(DX(TLocal()|)|P)x]")
   ),new AtomicTest(()->
   pass("(imm A _=A x)","[D(DX(TLocal(T(|CsP))|)|P)x]")
   ),new AtomicTest(()->
   pass("(mut _=A x)","[D(DX(TLocal|)|P)x]")
   ),new AtomicTest(()->
   pass("(fwd imm A x=A x)","[D(DX(TLocal(T(|CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(fwd mut x=A x)","[D(DX(TLocal X)|P)x]")
   ),new AtomicTest(()->
   pass("(fwd imm A _=A x)","[D(DX(TLocal(T(|CsP))|)|P)x]")
   ),new AtomicTest(()->
   pass("(fwd mut _=A x)","[D(DX(TLocal|)|P)x]")
   ),new AtomicTest(()->
   pass("(var A x=A x)","[D(DX(|TLocal(T(CsP))X)|P)x]")
   ),new AtomicTest(()->
   pass("(A(A y, A z) x=A x)",
   "[D(P)D([D(P)D(x)D(P)x])D(DX(TLocal()X)|P)x]")
   ),new AtomicTest(()->
   pass("((A y, A z) x=A x)","[D([D(P)D(x)D(P)x])D(DX(TLocal()X)|P)x]")
   ),new AtomicTest(()->
   pass("(A(A y, A z)=A x)",
   "[D(DX(TLocal(T(CsP))OR TLocal(T(CsP))X TLocal(T(CsP))X|)|P)x]")
   ),new AtomicTest(()->
   pass("((A y, A z)=A x)",
   "[D(DX(TLocal()OR TLocal(T(CsP))X TLocal(T(CsP))X|)|P)x]")
   ),new AtomicTest(()->
   pass("((y,z)=A x)",
   "[D(DX(TLocal()OR TLocal()X TLocal()X|)|P)x]")
   ),new AtomicTest(()->
   pass("((y)=A x)",
   "[D(DX(TLocal()OR TLocal()X|)|P)x]")
   ),new AtomicTest(()->
   pass("((var y)=A x)",
   "[D(DX(TLocal()OR|TLocal()X|)|P)x]")
   ),new AtomicTest(()->
   pass("((z, var y)=A x)",
   "[D(DX(TLocal()OR TLocal()X|TLocal()X|)|P)x]")
   ),new AtomicTest(()->
   pass("((z, var A y)=A x)",
   "[D(DX(TLocal()OR TLocal()X|TLocal(T(CsP))X|)|P)x]")
   ),new AtomicTest(()->
   pass("(A catch return A x x y)","[D(P)K(||T(CsP)X x)x]")
   ),new AtomicTest(()->
   pass("(A catch return A x x)","[D(P)K(||T(CsP)X x)]")
   ),new AtomicTest(()->
   pass("(A catch return A _ x)","[D(P)K(||T(CsP)|x)]")
   ),new AtomicTest(()->
   pass("(A catch return A _ x catch error A x y z)",
   "[D(P)K(||T(CsP)|x)K(||T(CsP)X x)x]")
   ),new AtomicTest(()->
   pass("(A catch return A _ x catch error A x y z)",
   "[D(P)K(||T(CsP)|x)K(||T(CsP)X x)x]")
   ),new AtomicTest(()->
   pass("(A whops B C D z)",
   "[D(P)D(x)D(P)D(P)D(P)x]")//well formedness will check for those issues
   ),new AtomicTest(()->
   pass("(A whoops B C D x)",
   "[D(P)Whoops(|T(CsP)T(CsP)T(CsP))x]")
   ),new AtomicTest(()->
   pass("(A whoops B C D)",
   "[D(P)Whoops(|T(CsP)T(CsP)T(CsP))]")
   ),new AtomicTest(()->
   pass("{A whoops B C D}",
   "[D(P)Whoops(|T(CsP)T(CsP)T(CsP))]")
   ),new AtomicTest(()->
   pass("{x=A catch T _ x y Z}",
   "[D(DX(TLocal()X)|P)K(|T(CsP)|x)D(x)D(P)]")
     ));}
public static void pass(String input,String output) {
  String res=parseStructure(parseWithException(input));
  assertTrue(res.startsWith("NudeE("));
  assertTrue(res.endsWith("|)"));
  res=res.substring("NudeE(".length(),res.length()-"|)".length());
  res=res.replace("E(EAtomic(CsP(|))))","P)").replace("E(EAtomic(X(|))))","x)");
  res=res.replace("E(EAtomic(CsP(|)))|","P|").replace("E(EAtomic(X(|)))|","x|");
  res=res.replace("E(EAtomic(CsP(|)))","P ").replace("E(EAtomic(X(|)))","x ");
  res=res.replace("(|))", ")").replace("(|)|", "|").replace("(|)", " ");
  res=res.replace("E(EAtomic(Block(OR ","[").replace("E(EAtomic(Block(|","[").replace("|)ENDBLOCK))","]");
  res=res.trim();
  assertEquals(output,res);

  }
}
