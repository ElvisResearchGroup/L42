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

  private String testMArg(String in) {
    ParseMArg r = new ParseMArg(FromDotToPath.reverse(in));
    return r.xs.toString();
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

  @Test public void testNew13() { testMs("P.mghhgj(tjytytj)","[mghhgj(that)]",0); }

  @Test public void testNew14() { testMs("P.mghj(x:tjytytj, y:90808)","[mghj(x,y)]",0); }

  @Test public void testNew15() { testMs("P.mghhgj()","[mghhgj()]",0); }

  @Test public void testNew16() { testMs("P.mghhgj($o:foo)","[mghhgj(o)]",0); }

  @Test public void testNew17() { testMs("P.mghj(_:500)","[mghj(_)]",0); }

  @Test public void testNew18() { testMs(";{[}]P.mghhgj()","[mghhgj()]",0); }

  @Test public void testNew19() { testMs("P.hi().hello().no()","[hi(), hello(), no()]",0); }

  @Test public void testNew20() { testMs("P.hi(dhfkjfkfh).hello(fjiffi,fjfo9).no($$)","[hi(that), hello(that), no(that)]",0); }

  @Test(expected=IllegalArgumentException.class)
  public void testNew21() { testMs("hi(x:hello())","[hi()]",0); }

  @Test(expected=IllegalArgumentException.class)
  public void testNew22() { testMs("hhhh(hi(x:hello()))","[hhhh()]",0); }

  @Test public void testNew23() { testMs("P.hi(x:hello())","[hi(x)]",0); }

  //TODO:fix
  @Test public void testNew24() { testMs("P.hhhh(hi(x:hello()))","[hhhh(that)]",0); }

  @Test public void testNew25() { assertEquals("[]", testMArg("")); }

  @Test public void testNew26() { assertEquals("[x]", testMArg("x:y")); }

  @Test public void testNew27() { assertEquals("[that, x]", testMArg("z  x : y")); }

  @Test public void testNew28() { assertEquals("[that]", testMArg("x, y")); }

  @Test public void testNew29() { assertEquals("[that, x, z]", testMArg("hello x:y z:p")); }

  @Test public void testNew30() { assertEquals("[x]", testMArg("x:S\"hello test\"")); }

  @Test public void testNew31() { assertEquals("[x]", testMArg("x:S\"hello /*comment hi*/ test\"")); }

  @Test public void testNew32() { assertEquals("[]", testMArg("/*x:S\"hello test\"*/")); }

  @Test(expected=IllegalArgumentException.class) //for now - hard to accept
  public void testNew33() { assertEquals("[x]", testMArg("x/*see*/:S\"hello test\"")); }

  @Test public void testNew34() { assertEquals("[x]", testMArg("x:/*saw*/S\"hello test\"")); }

  @Test public void testNew35() { assertEquals("[x]", testMArg("x:S\"hello test\"/*hi*/")); }

  @Test public void testNew36() { assertEquals("[x]", testMArg("/*hey*/x:S\"hello test\"")); }

  @Test(expected=IllegalArgumentException.class)
  public void testNew37() { assertEquals("[x]", testMArg("\"world\"x:S\"hello test\"")); }

  @Test public void testNew38() { assertEquals("[that, x]", testMArg("\"world\" x:S\"hello test\"")); }

  @Test(expected=IllegalArgumentException.class)
  public void testNew39() { assertEquals("[x]", testMArg("x::S\"hello test\"")); }

  //TODO: gives [x,y] should only be [x]
  @Test public void testNew40() { assertEquals("[x]", testMArg("x:A.m(y:z)")); }

}
