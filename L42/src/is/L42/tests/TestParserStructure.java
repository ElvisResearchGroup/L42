package is.L42.tests;

import java.util.stream.Stream;

import is.L42.tools.AtomicTest;
import static is.L42.tests.TestHelpers.*;

import static org.junit.jupiter.api.Assertions.*;
public class TestParserStructure
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("x","NudeE(E(EAtomic(X(|)))|)")
   ),new AtomicTest(()->
   pass("Bar.Baz","NudeE(E(EAtomic(CsP(|)))|)")
   ),new AtomicTest(()->
   pass("Any.Foo","NudeE(E(EAtomic(CsP(|)))|)")//Any/Thisn will be handled by well formedness
   ));}
public static void pass(String input,String output) {
  assertEquals(output,parseStructure(getPositiveParser(input).nudeE()));

  }
}
