package is.L42.tests;

import static is.L42.tools.General.L;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import is.L42.common.Constants;
import is.L42.common.Program;
import is.L42.common.ReadURL;
import is.L42.constraints.FreshNames;
import is.L42.generated.Core;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.top.CachedTop;
import is.L42.top.Init;
import is.L42.top.State;
import is.L42.translationToJava.Loader;

public class TestCachingCases {
@Test void base(){pass("{A0={B1={} B2={} B3={}}}",
  "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
  "");}
@Test void baseChange(){pass(
  "{A0={B1={D={}} B2={} B3={}}}",
  "{A0={B1={D={}} B2={C={}} B3={}}}",
  "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
  "topO:0,NCiO:A0,topO:1,NCiO:B2,topO:2,NCiO:C,topO:3,topC:3,NCiC:C,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,");}
@Test void changeLater(){pass(
    "{A0={B1={D={}} B2={} B3={}}}",
    "{A0={B1={D={}} B2={} B3={C={}}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
    "topO:0,NCiO:A0,topO:1,NCiO:B3,topO:2,NCiO:C,topO:3,topC:3,NCiC:C,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,");}
@Test void changeRemove(){pass(
    "{A0={B1={D={}} B2={C={}} B3={}}}",
    "{A0={B1={D={}} B2={} B3={}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,NCiO:B2,topO:2,NCiO:C,topO:3,topC:3,NCiC:C,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
    "topO:0,NCiO:A0,topO:1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,");}
@Test void changeMethod(){pass(
    "{A0={B1={D={}} B2={} B3={}}}",
    "{A0={B1={D={}} B2={method Void foo()=void} B3={}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
    "topO:0,NCiO:A0,topO:1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,");}
@Test void changeMethodType(){pass(
    "{A0={B1={D={}} B2={method Void foo()=void} B3={method Void m(B2 b)=(_=b.foo() void)}}}",
    "{A0={B1={D={}} B2={method Library foo()={#norm{}}} B3={method Void m(B2 b)=(_=b.foo() void)}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
    "topO:0,NCiO:A0,topO:1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,");}
@Test void changeTrashIllTyped(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar()} E={}} {}) Late={}}}",
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar()} E={K={}}} {}) Late={}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,NCiO:E,topO:3,topC:3,NCiC:E,topC:2,topO:2,topC:2,NCiC:B1,NCiO:Late,topO:2,topC:2,NCiC:Late,topC:1,NCiC:A0,topC:0,",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:E,topO:3,NCiO:K,topO:4,topC:4,NCiC:K,topC:3,NCiC:E,topC:2,topO:2,topC:2,NCiC:B1,NCiO:Late,topO:2,topC:2,NCiC:Late,topC:1,NCiC:A0,topC:0,");}
@Test void changeTrashIllTypedCTz1(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() method Void diff(D d)=(_=d.bar() void)}} {})} Late={}}",
    "{A0={B1=(_aux={D={method Library foo()[Late]=this.bar() method Void diff(D d)=(_=d.bar() void)}} {})} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,topO:2,topC:2,NCiC:B1,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,");}
//TODO: caching now seams to be able to go "back on track" while still saving the new improved cache at the end.
//is this always working? is thid desirable?
@Test void changeTrashIllTypedCTz2(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() method Void diff(D d)=(_=d.bar() void)}} {})} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,topO:2,topC:2,NCiC:B1,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "");}
@Test void changeTrashIllTypedCTzDeep(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() Inner={method Void diff(D d)=(_=d.bar() void)}}} {})} Late={}}",
    "{A0={B1=(_aux={D={method Library foo()[Late]=this.bar() Inner={method Void diff(D d)=(_=d.bar() void)}}} {})} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,NCiO:Inner,topO:4,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,topO:2,topC:2,NCiC:B1,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
      "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,NCiO:Inner,topO:4,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,NCiC:B1,");}
@Test void deep2Level(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() Inner={Inner2={method Void diff(D d)=(_=d.bar() void)}}}} {})} Late={}}",
    "{A0={B1=(_aux={D={method Library foo()[Late]=this.bar() Inner={Inner2={method Void diff(D d)=(_=d.bar() void)}}}} {})} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,NCiO:Inner,topO:4,NCiO:Inner2,topO:5,topC:5,NCiC:Inner2,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,topO:2,topC:2,NCiC:B1,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,NCiO:Inner,topO:4,NCiO:Inner2,topO:5,topC:5,NCiC:Inner2,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,NCiC:B1,");}
@Test void deep3Level(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() Inner={Inner2={Inner3={method Void diff(D d)=(_=d.bar() void)}}}}} {})} Late={}}",
    "{A0={B1=(_aux={D={method Library foo()[Late]=this.bar() Inner={Inner2={Inner3={method Void diff(D d)=(_=d.bar() void)}}}}} {})} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,NCiO:Inner,topO:4,NCiO:Inner2,topO:5,NCiO:Inner3,topO:6,topC:6,NCiC:Inner3,topC:5,NCiC:Inner2,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,topO:2,topC:2,NCiC:B1,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,NCiO:Inner,topO:4,NCiO:Inner2,topO:5,NCiO:Inner3,topO:6,topC:6,NCiC:Inner3,topC:5,NCiC:Inner2,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,NCiC:B1,");}
@Test void hashDollarReuseSame(){pass(
    "{A0={B1={} R={reuse[#$ba]} C={}} Late={}}",
    "{A0={B1={} R={reuse[#$ba]} C={}} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:R,topO:2,topC:2,NCiC:R,NCiO:C,topO:2,topC:2,NCiC:C,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "topO:2,","topO:2,",null);}
@Test void hashDollarReuseChange(){pass(
    "{A0={B1={} R={reuse[#$ba]} C={}} Late={}}",
    "{A0={B1={} R={reuse[#$ba]} C={}} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:R,topO:2,topC:2,NCiC:R,NCiO:C,topO:2,topC:2,NCiC:C,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "topO:2,topC:2,NCiC:R,NCiO:C,topO:2,topC:2,NCiC:C,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "topO:2,",Program.emptyL);}

@Test void noHashDollarE0(){pass(
    "{A0={B1={class method Library foo()={#norm{}}} R=B1.foo() C={}} Late={}}",
    "{A0={B1={class method Library foo()={#norm{}}} R=B1.foo() C={}} Late={}}",//same
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:R,NCiC:R,NCiO:C,topO:2,topC:2,NCiC:C,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    ""
    );}
@Test void noHashDollarE1(){pass(
    "{A0={B1={class method Library foo()={#norm{}}} R=B1.foo() C={}} Late={}}",
    "{A0={B1={class method Library foo()={method Void v()=void #norm{}}} R=B1.foo() C={}} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:R,NCiC:R,NCiO:C,topO:2,topC:2,NCiC:C,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:R,NCiC:R,NCiO:C,topO:2,topC:2,NCiC:C,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,"
    );}

@Test void hashDollarESame(){pass(
    "{A0={B1={class method Library #$foo()={#norm{}}} R=B1.#$foo() C={}} Late={}}",
    "{A0={B1={class method Library #$foo()={#norm{}}} R=B1.#$foo() C={}} Late={}}",//same
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:R,NCiC:R,NCiO:C,topO:2,topC:2,NCiC:C,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "NCiC:R,",
    "NCiC:R,",
    null);}
@Test void hashDollarEDiff(){
  String code="""
      {reuse[AdamTowel]
      A=Log"A".clear()
      B=Log"A".write(S"B")
      C=Debug(S"DoingC:"++Log"A".#$reader().read())}
      """;  
  Resources.clearRes();
  var cache1=new CachedTop(L(),L());
  Init.topCache(cache1,code);
  String exe=Resources.notifiedCompiledNC();
  String out=Resources.out();
  Resources.clearRes();
  var cache2=cache1.toNextCache();
  Init.topCache(cache2,code);
  String exe2=Resources.notifiedCompiledNC();
  String out2=Resources.out();
  assertEquals("topO:0,NCiO:A,NCiC:A,NCiO:B,NCiC:B,NCiO:C,NCiC:C,topC:0,",exe);
  assertEquals("NCiC:C,",exe2);
  assertEquals("DoingC:B\n",out);
  assertEquals("DoingC:\n",out2);
  }


//TODO: now add hashDollarEdifferet, using a trusted pluging that return {@{num++}}
/*
 TODO: testh #$ reuse, #$ e,
 how is FreshNames used in init? Top should inherit it as before.
 minimize Top, State and the caching ones 
 
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
  pass(code1,code2,expectedExe1,expectedExe2,"",null);
  }
void pass(String code1,String code2,String expectedExe1,String expectedExe2,String expectedExe3,Core.L resetReuseds){
  Resources.clearRes();
  var cache1=new CachedTop(L(),L());
  Init.topCache(cache1,code1);
  String exe=Resources.notifiedCompiledNC();
  Resources.clearRes();
  
  var tmp=Constants.readURL;try{
  if(resetReuseds!=null){Constants.readURL=ignored->resetReuseds;}
  
  var cache2=cache1.toNextCache();
  @SuppressWarnings("unused")//for the debugger
  var res2=Init.topCache(cache2,code2);
  String exe2=Resources.notifiedCompiledNC();
  
  Resources.clearRes();
  var cache3=cache2.toNextCache();
  @SuppressWarnings("unused")//for the debugger
  var res3=Init.topCache(cache3,code2);
  String exe3=Resources.notifiedCompiledNC();
  assertEquals(expectedExe1,exe);
  assertEquals(expectedExe2,exe2);
  assertEquals(expectedExe3,exe3);
  }finally{Constants.readURL=tmp;}
  }
}
