package repl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FromDotToPathTests {

  private FromDotToPath testAux(String in,String expected,int col) {
    String[] lines=in.split("\\r?\\n");
    FromDotToPath r = new FromDotToPath(in, col, lines[lines.length-1].length());
    assertEquals(expected, r.pathString.toString());
    return r;
  }

  private FromDotToPath testMs(String in, String expected,int col) {
    String[] lines=in.split("\\r?\\n");
    FromDotToPath r = new FromDotToPath(in, col, lines[lines.length-1].length());
    assertEquals(expected, r.ms.toString());
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

  @Test public void testNew13() { testMs("P.mghhgj(tjytytj)","[mghhgj()]",0); }

  @Test public void testNew14() { testMs("P.mghj(x:tjytytj, y:90808)","[mghj()]",0); }

  @Test public void testNew15() { testMs("P.mghhgj()","[mghhgj()]",0); }

  @Test public void testNew16() { testMs("P.mghhgj($o:foo)","[mghhgj()]",0); }

  @Test public void testNew17() { testMs("P.mghj(_:500)","[mghj()]",0); }

  @Test public void testNew18() { testMs(";{[}]P.mghhgj()","[mghhgj()]",0); }

  @Test public void testNew19() { testMs("P.hi().hello().no()","[hi(), hello(), no()]",0); }

  @Test public void testNew20() { testMs("P.hi(dhfkjfkfh).hello(fjiffi,fjfo9).no($$)","[hi(), hello(), no()]",0); }

  @Test(expected=IllegalArgumentException.class) public void testNew21() { testMs("hi(x:hello())","[hi()]",0); }

  @Test(expected=IllegalArgumentException.class) public void testNew22() { testMs("hhhh(hi(x:hello()))","[hhhh()]",0); }

  @Test public void testNew23() { testMs("P.hi(x:hello())","[hi()]",0); }

  @Test public void testNew24() { testMs("P.hhhh(hi(x:hello()))","[hhhh()]",0); }
}
