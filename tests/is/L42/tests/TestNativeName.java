package is.L42.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;
import java.util.stream.Stream;

import is.L42.meta.L42£Name;
import is.L42.tools.AtomicTest;

public class TestNativeName extends AtomicTest.Tester{
  public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("This",n->""+n,"This")
   ),new AtomicTest(()->
   pass("This")
   ),new AtomicTest(()->
   pass("This0",n->""+n,"This")
   ),new AtomicTest(()->
   pass("",n->""+n,"<hidden name>")
   ),new AtomicTest(()->
   pass("C")
   ),new AtomicTest(()->
   pass("C.D")
   ),new AtomicTest(()->
   fail("Any.D")
   ),new AtomicTest(()->
   fail("Any.Void")
   ),new AtomicTest(()->
   fail("C.Void")
   ),new AtomicTest(()->
   fail("C.Void.foo()")
   ),new AtomicTest(()->
   pass("C.VoidD.foo()")
   ),new AtomicTest(()->
   fail("C.foo().x")
   ),new AtomicTest(()->
   pass("C.foo().this")
   ),new AtomicTest(()->
   pass("C.foo(that).that")
   ),new AtomicTest(()->
   pass("C.foo(that,such).that")
   ),new AtomicTest(()->
   pass("C.foo(that such).that",n->""+n,"C.foo(that,such).that")
   ),new AtomicTest(()->
   pass("A.C.foo(that such).that",n->n.path(),"A.C")
   ),new AtomicTest(()->
   pass("A.C.foo(that such).that",n->n.selector(),"foo(that,such)")
   ),new AtomicTest(()->
   pass("A.C.foo(that such).that",n->n.x(),"that")
   ),new AtomicTest(()->
   pass("A.C.foo(that such).that",n->n.withPath("K.A.B").toString(),"K.A.B.foo(that,such).that")
   ),new AtomicTest(()->
   pass("A.C.foo(that such).that",n->n.withSelector("bar(beer)").toString(),"A.C.bar(beer)")
   ),new AtomicTest(()->
   pass("A.C.foo(that such).that",n->n.withSelector("bar(beer,that)").toString(),"A.C.bar(beer,that).that")
   ),new AtomicTest(()->
   pass("A.C.foo(that such).that",n->n.withX("such").toString(),"A.C.foo(that,such).such")

  ));}
public static void pass(String s,Function<L42£Name,String>f,String out){
  assertEquals(out,f.apply(L42£Name.parse(s)));
  }
public static void pass(String s){
  assertEquals(s,L42£Name.parse(s).toString());
  }
public static void fail(String s){
  try{L42£Name.parse(s);fail("Error expected for "+s);}
  catch(NumberFormatException nfe){}
  }
}