package is.L42.tests;

import static is.L42.tools.General.L;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import is.L42.constraints.FreshNames;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.top.CachedTop;
import is.L42.top.Init;
import is.L42.top.State;
import is.L42.top.Top;
import is.L42.translationToJava.Loader;

public class TestCachingCases {
@Test void base(){pass("{A0={B1={} B2={} B3={}}}",
  "B1\nB2\nB3\nA0\n","");}
@Test void baseChange(){pass(
  "{A0={B1={D={}} B2={} B3={}}}",
  "{A0={B1={D={}} B2={C={}} B3={}}}",
  "D\nB1\nB2\nB3\nA0\n",
  "C\nB2\nB3\nA0\n");}
@Test void changeLater(){pass(
    "{A0={B1={D={}} B2={} B3={}}}",
    "{A0={B1={D={}} B2={} B3={C={}}}}",
    "D\nB1\nB2\nB3\nA0\n",
    "C\nB3\nA0\n");}
@Test void changeMethod(){pass(
    "{A0={B1={D={}} B2={} B3={}}}",
    "{A0={B1={D={}} B2={method Void foo()=void} B3={}}}",
    "D\nB1\nB2\nB3\nA0\n",
    "B2\nB3\nA0\n");}
@Test void changeMethodType(){pass(
    "{A0={B1={D={}} B2={method Void foo()=void} B3={method Void m(B2 b)=(_=b.foo() void)}}}",
    "{A0={B1={D={}} B2={method Library foo()={#norm{}}} B3={method Void m(B2 b)=(_=b.foo() void)}}}",
    "D\nB1\nB2\nB3\nA0\n",
    "B2\nB3\nA0\n");}
@Test void changeTrashIllTyped(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar()} E={}} {}) Late={}}}",
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar()} E={K={}}} {}) Late={}}}",
    "D\nE\nB1\nLate\nA0\n",
    "K\nE\nB1\nLate\nA0\n");}
@Test void changeTrashIllTypedCTz1(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() method Void diff(D d)=(_=d.bar() void)}} {})} Late={}}",
    "{A0={B1=(_aux={D={method Library foo()[Late]=this.bar() method Void diff(D d)=(_=d.bar() void)}} {})} Late={}}",
    "D\nE\nB1\nLate\nA0\n",
    "K\nE\nB1\nLate\nA0\n");}
/*
So, what if CTz is a tree?
in any L we can use only the CTzs from the method in the L
and in the outer L? no informations about other nesteds of the outer L?
good:
  -avoids exponential worst case performance
  -we can still declare an extra private method in an outer L to force assumptions
  -now trashing CTzs after metaprogramming makes sense


*/

void pass(String code,String expectedExe1,String expectedExe2){
  pass(code,code,expectedExe1,expectedExe2);
  }
void pass(String code1,String code2,String expectedExe1,String expectedExe2){
  Resources.clearRes();
  var cache1=new CachedTop(L(),L());
  Top.topCache(cache1,code1);
  String exe=Resources.notifiedCompiledNC();
  Resources.clearRes();
  var cache2=cache1.toNextCache();
  @SuppressWarnings("unused")//for the debugger
  var res2=Top.topCache(cache2,code2);
  String exe2=Resources.notifiedCompiledNC();
  assertEquals(expectedExe1,exe);
  assertEquals(expectedExe2,exe2);  
  }
}
