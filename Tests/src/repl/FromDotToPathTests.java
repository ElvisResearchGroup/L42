package repl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FromDotToPathTests {

  private FromDotToPath testAux(String in,String expected,int col) {
    String[] lines=in.split("\\r?\\n");
    FromDotToPath r = new FromDotToPath(in, col, lines[lines.length-1].length());
    assertEquals(expected, r.pathString.toString());
    return r;
  }

  @Test public void testNew1() { testAux("Foo","Foo.",0); }

  @Test public void testNew2() { testAux("hello\nheLlo\nFoo","Foo.",2); }

  @Test public void testNew3() { testAux("Hello; .Foo","Foo.",0); }

  @Test public void testNew4() { testAux("$oo","$oo.",0); }

  @Test public void testNew5() { testAux("%o_o","%o_o.",0); }

  @Test public void testNew6() { testAux("  $oo","$oo.",0); }

  @Test public void testNew7() { testAux("  %o_o","%o_o.",0); }

  @Test public void testNew8() { testAux(".Hello.bye+$oo","$oo.",0); }

  @Test public void testNew9() { testAux(";{[}]Eo_o","Eo_o.",0); }

  @Test public void testNew10() { testAux("A.B.C","A.B.C.",0); }

  @Test public void testNew11() { assertEquals(0,testAux("This0.This1B.C","This1B.C.",0).thisNum); }

  @Test public void testNew12() { assertEquals(42,testAux("This42.B.C","B.C.",0).thisNum); }
}
