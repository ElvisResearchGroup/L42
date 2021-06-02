package is.L42.tests;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import is.L42.common.Constants;
import is.L42.common.EndError;
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
@Test void base2(){pass("{A0={B1={} B2=({}) B3={}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
    "");}
@Test void nameChangePar(){pass(
    "{AA={class method Void v()=void} Main=AA.v()}",
    "{A={class method Void v()=(void)} Main=A.v()}",
    "topO:0,NCiO:AA,topO:1,topC:1,NCiC:AA,NCiO:Main,NCiC:Main,topC:0,",
    "NCiO:A,topO:1,topC:1,NCiC:A,NCiO:Main,NCiC:Main,topC:0,");}
@Test void nameChange(){pass(
    "{AA={class method Void v()=void} Main=AA.v()}",
    "{A={class method Void v()=void} Main=A.v()}",
    "topO:0,NCiO:AA,topO:1,topC:1,NCiC:AA,NCiO:Main,NCiC:Main,topC:0,",
    "NCiO:A,topO:1,topC:1,NCiC:A,NCiO:Main,NCiC:Main,topC:0,");}

@Test void baseChange(){pass(
  "{A0={B1={D={}} B2={} B3={}}}",
  "{A0={B1={D={}} B2={C={}} B3={}}}",
  "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
  "NCiO:A0,NCiO:B2,NCiO:C,topO:3,topC:3,NCiC:C,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,");}
@Test void changeLater(){pass(
    "{A0={B1={D={}} B2={} B3={}}}",
    "{A0={B1={D={}} B2={} B3={C={}}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
    "NCiO:A0,NCiO:B3,NCiO:C,topO:3,topC:3,NCiC:C,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,");}
@Test void changeRemove(){pass(
    "{A0={B1={D={}} B2={C={}} B3={}}}",
    "{A0={B1={D={}} B2={} B3={}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,NCiO:B2,topO:2,NCiO:C,topO:3,topC:3,NCiC:C,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
    "NCiO:A0,NCiO:B2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,");}
@Test void changeMethod(){pass(
    "{A0={B1={D={}} B2={} B3={}}}",
    "{A0={B1={D={}} B2={method Void foo()=void} B3={}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
    "NCiO:A0,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,");}
@Test void changeMethodType(){pass(
    "{A0={B1={D={}} B2={method Void foo()=void} B3={method Void m(B2 b)=(_=b.foo() void)}}}",
    "{A0={B1={D={}} B2={method Library foo()={#norm{}}} B3={method Void m(B2 b)=(_=b.foo() void)}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,",
    "NCiO:A0,NCiO:B2,topO:2,topC:2,NCiC:B2,NCiO:B3,topO:2,topC:2,NCiC:B3,topC:1,NCiC:A0,topC:0,");}
@Test void changeTrashIllTyped(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar()} E={}} {}) Late={}}}",
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar()} E={K={}}} {}) Late={}}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,NCiO:E,topO:3,topC:3,NCiC:E,topC:2,topO:2,topC:2,NCiC:B1,NCiO:Late,topO:2,topC:2,NCiC:Late,topC:1,NCiC:A0,topC:0,",
    "NCiO:A0,NCiO:B1,NCiO:E,NCiO:K,topO:4,topC:4,NCiC:K,topC:3,NCiC:E,topC:2,topO:2,topC:2,NCiC:B1,NCiO:Late,topO:2,topC:2,NCiC:Late,topC:1,NCiC:A0,topC:0,");}
@Test void changeTrashIllTypedCTz1(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() method Void diff(D d)=(_=d.bar() void)}} {})} Late={}}",
    "{A0={B1=(_aux={D={method Library foo()[Late]=this.bar() method Void diff(D d)=(_=d.bar() void)}} {})} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,topO:2,topC:2,NCiC:B1,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "NCiO:A0,NCiO:B1,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,NCiC:B1,");}
//TODO: caching now seams to be able to go "back on track" while still saving the new improved cache at the end.
//is this always working? is this desirable?
@Test void changeTrashIllTypedCTz2(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() method Void diff(D d)=(_=d.bar() void)}} {})} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,topC:3,NCiC:D,topC:2,topO:2,topC:2,NCiC:B1,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "");}
@Test void changeTrashIllTypedCTzDeep(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() Inner={method Void diff(D d)=(_=d.bar() void)}}} {})} Late={}}",
    "{A0={B1=(_aux={D={method Library foo()[Late]=this.bar() Inner={method Void diff(D d)=(_=d.bar() void)}}} {})} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,NCiO:Inner,topO:4,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,topO:2,topC:2,NCiC:B1,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "NCiO:A0,NCiO:B1,NCiO:D,topO:3,NCiO:Inner,topO:4,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,NCiC:B1,");}
@Test void deep2Level(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() Inner={Inner2={method Void diff(D d)=(_=d.bar() void)}}}} {})} Late={}}",
    "{A0={B1=(_aux={D={method Library foo()[Late]=this.bar() Inner={Inner2={method Void diff(D d)=(_=d.bar() void)}}}} {})} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,NCiO:Inner,topO:4,NCiO:Inner2,topO:5,topC:5,NCiC:Inner2,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,topO:2,topC:2,NCiC:B1,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "NCiO:A0,NCiO:B1,NCiO:D,topO:3,NCiO:Inner,topO:4,NCiO:Inner2,topO:5,topC:5,NCiC:Inner2,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,NCiC:B1,");}
@Test void deep3Level(){pass(
    "{A0={B1=(_aux={D={method Void foo()[Late]=this.bar() Inner={Inner2={Inner3={method Void diff(D d)=(_=d.bar() void)}}}}} {})} Late={}}",
    "{A0={B1=(_aux={D={method Library foo()[Late]=this.bar() Inner={Inner2={Inner3={method Void diff(D d)=(_=d.bar() void)}}}}} {})} Late={}}",
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,NCiO:D,topO:3,NCiO:Inner,topO:4,NCiO:Inner2,topO:5,NCiO:Inner3,topO:6,topC:6,NCiC:Inner3,topC:5,NCiC:Inner2,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,topO:2,topC:2,NCiC:B1,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "NCiO:A0,NCiO:B1,NCiO:D,topO:3,NCiO:Inner,topO:4,NCiO:Inner2,topO:5,NCiO:Inner3,topO:6,topC:6,NCiC:Inner3,topC:5,NCiC:Inner2,topC:4,NCiC:Inner,topC:3,NCiC:D,topC:2,NCiC:B1,");}
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
    "NCiO:A0,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:R,NCiC:R,NCiO:C,topO:2,topC:2,NCiC:C,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,"
    );}

@Test void hashDollarESame(){pass(
    "{A0={B1={class method Library #$foo()={#norm{}}} R=B1.#$foo() C={}} Late={}}",
    "{A0={B1={class method Library #$foo()={#norm{}}} R=B1.#$foo() C={}} Late={}}",//same
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:R,NCiC:R,NCiO:C,topO:2,topC:2,NCiC:C,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "NCiC:R,",
    "NCiC:R,",
    null);}
@Test void hashDollarESameButTyped(){pass(
    "{A0={B1={class method Library #$foo()={#norm{}}} R=B1.#$foo() C={}} Late={}}",
    "{A0={B1={class method Library #$foo()={#typed{}}} R=B1.#$foo() C={}} Late={}}",//same
    "topO:0,NCiO:A0,topO:1,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiO:R,NCiC:R,NCiO:C,topO:2,topC:2,NCiC:C,topC:1,NCiC:A0,NCiO:Late,topO:1,topC:1,NCiC:Late,topC:0,",
    "NCiO:A0,NCiO:B1,topO:2,topC:2,NCiC:B1,NCiC:R,",
    "NCiC:R,",
    null);}

@Test void threeChanges(){passMany(
    List.of(
      "{A={class method class A a()=this} Main2=(_=A.a() {})}",
      "{A={class method class A a()=this} Main2=(_=A.a().a() {})}",
      "{A={class method class A a()=this} Main2=(_=A.a().a().a() {})}",
      "{A={class method class A a()=this} Main2=(_=A.a().a().a().a() {})}"
    ),List.of(
      "topO:0,NCiO:A,topO:1,topC:1,NCiC:A,NCiO:Main2,topO:1,topC:1,NCiC:Main2,topC:0,",
      "NCiO:Main2,topO:1,topC:1,NCiC:Main2,topC:0,",
      "NCiO:Main2,topO:1,topC:1,NCiC:Main2,topC:0,",
      "NCiO:Main2,topO:1,topC:1,NCiC:Main2,topC:0,"
    ));}
@Test void adamChangeVarLength1(){passMany(
  List.of(
    "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main2\")}",
    "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main3\")}",
    "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main34\")}"
  ),List.of(
    "topO:0,NCiO:Main1,NCiC:Main1,NCiO:Main2,NCiC:Main2,topC:0,",
    "NCiO:Main2,NCiC:Main2,",
    "NCiO:Main2,NCiC:Main2,topC:0,"//because 1 extra char, thus 1 extra freevar is generated
  ));}
@Test void adamChangeVarLength2(){passMany(
    List.of(
      "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main2\")}",
      "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main34\")}",
      "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main4\")}",
      "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main2\")}"
    ),List.of(
      "topO:0,NCiO:Main1,NCiC:Main1,NCiO:Main2,NCiC:Main2,topC:0,",
      "NCiO:Main2,NCiC:Main2,topC:0,",
      "NCiO:Main2,NCiC:Main2,topC:0,",
      "NCiO:Main2,NCiC:Main2,"
    ));}

@Test void adamChangeSameLength(){passMany(
    List.of(
      "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main2\")}",
      "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main3\")}",
      "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main4\")}"
    ),List.of(
      "topO:0,NCiO:Main1,NCiC:Main1,NCiO:Main2,NCiC:Main2,topC:0,",
      "NCiO:Main2,NCiC:Main2,",
      "NCiO:Main2,NCiC:Main2,"
    ));}


@Test void adamChangeMiddle(){passMany(
    List.of(
      "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world1\") ) Main2=Debug(S\"Main\")}",
      "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world2\") ) Main2=Debug(S\"Main\")}",
      "{reuse [AdamsTowel] Main1=(Debug(S\"Hello world3\") ) Main2=Debug(S\"MaiW\")}"
    ),List.of(
      "topO:0,NCiO:Main1,NCiC:Main1,NCiO:Main2,NCiC:Main2,topC:0,",
      "NCiO:Main1,NCiC:Main1,",
      "NCiO:Main1,NCiC:Main1,NCiO:Main2,NCiC:Main2,"
    ));}

@Test void repetedName(){
  var code0="{reuse [AdamsTowel] Foo=Debug(S\"@@@@foo\")}";
  var code1="{reuse [AdamsTowel] Test={} Bar=Debug(S\"####bar\") Foo=Debug(S\"@@foo@@\")}";
  var code2="{reuse [AdamsTowel] Bar=Debug(S\"####bar\") Foo=Debug(S\"@@foo@@\")}";
  var cache=new CachedTop(L(),L());//round 0
  Resources.clearResKeepReuse();
  Init.topCache(cache,code0);
  assertEquals(Resources.out(),"@@@@foo\n");
  cache=cache.toNextCache();//round 1
  Resources.clearResKeepReuse();
  try{Init.topCache(cache,code1);fail();}
  catch(EndError.InvalidHeader ie){}
  assertEquals(Resources.out(),"");
  cache=cache.toNextCache();//round 2
  Resources.clearResKeepReuse();
  Init.topCache(cache,code2);
  assertEquals(Resources.out(),"####bar\n@@foo@@\n");
  }
@Test void repetedNameSelector(){
  var code0="{reuse[L42.is/MiniLib] A={}}";
  var code1="{reuse[L42.is/MiniLib] class method Void hello(Void that)=that B={} A={}}";
  var code2="{reuse[L42.is/MiniLib] B={} A={}}";
  var cache=new CachedTop(L(),L());//round 0
  Resources.clearResKeepReuse();
  Init.topCache(cache,code0);
  assertEquals(Resources.notifiedCompiledNC(),
    "topO:0,NCiO:A,topO:1,topC:1,NCiC:A,topC:0,");
  cache=cache.toNextCache();//round 1
  Resources.clearResKeepReuse();
  try{Init.topCache(cache,code1);fail();}
  catch(EndError.InvalidHeader ie){}
  assertEquals(Resources.notifiedCompiledNC(),"topO:0,");
  cache=cache.toNextCache();//round 2
  Resources.clearResKeepReuse();
  Init.topCache(cache,code2);
  assertEquals(Resources.notifiedCompiledNC(),
    "topO:0,NCiO:B,topO:1,topC:1,NCiC:B,NCiO:A,topO:1,topC:1,NCiC:A,topC:0,");
  }

@Test void cacheOnFile(){
  //IntStream.range(0, 10).forEach(i->cacheOnFile1());
  cacheOnFile1();
  }
public static long last=0;
public static void timeNow(String msg) {  
  long now=System.currentTimeMillis();
  System.out.println("Time:"+(now-last)+"   "+msg);
  last=now;
  }
void cacheOnFile1(){
  String code="""
      {reuse[AdamsTowel]
      A=Log"A".clear()
      B=Log"A".write(S"B")
      C=Debug(S"DoingC:"++Log"A".#$reader().read())}
      """;  
  Resources.clearResKeepReuse();
  var cache1=new CachedTop(L(),L());
  long start1=System.currentTimeMillis();
  TestCachingCases.last=start1;
  Init.topCache(cache1,code);
  long end1=System.currentTimeMillis();
  String exe=Resources.notifiedCompiledNC();
  String out=Resources.out();
  Resources.clearResKeepReuse();
  var cache2=cache1.toNextCache();
  cache2.saveCache(Constants.localhost.resolve("TestCaching"));
  System.out.println("Now with Cache");
  cache2=CachedTop.loadCache(Constants.localhost.resolve("TestCaching"));
  long start2=System.currentTimeMillis();
  last=start2;
  Init.topCache(cache2,code);
  long end2=System.currentTimeMillis();
  assertTrue(end1-start1>end2-start2);
  System.out.println("Full Time: "+(end1-start1));
  System.out.println("Cached Time: "+(end2-start2));
  String exe2=Resources.notifiedCompiledNC();
  String out2=Resources.out();
  assertEquals("topO:0,NCiO:A,NCiC:A,NCiO:B,NCiC:B,NCiO:C,NCiC:C,topC:0,",exe);
  assertEquals("NCiC:C,",exe2);
  assertEquals("DoingC:B\n",out);
  assertEquals("DoingC:\n",out2);
  }
@Test void testSpeedChange(){
  //ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(false);
  //"".getClass().getClassLoader()
  String code1="""
    {reuse [AdamsTowel]
    Main1=(Debug(S"Hello world1") )
    Main2=Debug(S"Main41")}
    """;
  String code2="""
      {reuse [AdamsTowel]
      Main1=(Debug(S"Hello world1") )
      Main2=Debug(S"Main42")}
      """;
    System.out.println("START WARM UP");
    last=System.currentTimeMillis();
    Init.topCache(new CachedTop(L(),L()),code1);
    last=System.currentTimeMillis();
    long start1=last;
    Resources.clearResKeepReuse();
    System.out.println("START FIRST EXECUTION");
    var cache1=new CachedTop(L(),L());
    Init.topCache(cache1,code1);
    long end1=System.currentTimeMillis();
    System.out.println(Resources.notifiedCompiledNC());
    System.out.println("START CACHED EXECUTION");
    long start2=System.currentTimeMillis();
    Resources.clearResKeepReuse();
    var cache2=cache1.toNextCache();
    Init.topCache(cache2,code2);
    long end2=System.currentTimeMillis();
    System.out.println(Resources.notifiedCompiledNC());
    System.out.println("TimeBase= "+(end1-start1));
    System.out.println("TimeCache= "+(end2-start2));
    assertTrue(end1-start1>end2-start2);
  }
@Test void hashDollarEDiff(){
  String code="""
      {reuse[AdamsTowel]
      A=Log"A".clear()//also the test environment clears the log
      B=Log"A".write(S"B")
      C=Debug(S"DoingC:"++Log"A".#$reader().read())}
      """;  
  Resources.clearResKeepReuse();
  var cache1=new CachedTop(L(),L());
  Init.topCache(cache1,code);
  String exe=Resources.notifiedCompiledNC();
  String out=Resources.out();
  Resources.clearResKeepReuse();
  var cache2=cache1.toNextCache();
  Init.topCache(cache2,code);
  String exe2=Resources.notifiedCompiledNC();
  String out2=Resources.out();
  assertEquals("topO:0,NCiO:A,NCiC:A,NCiO:B,NCiC:B,NCiO:C,NCiC:C,topC:0,",exe);
  assertEquals("NCiC:C,",exe2);
  assertEquals("DoingC:B\n",out);
  assertEquals("DoingC:\n",out2);
  }

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
  pass(code1,code2,expectedExe1,expectedExe2,"",null);
  }
void pass(String code1,String code2,String expectedExe1,String expectedExe2,String expectedExe3,Core.L resetReuseds){
  Resources.clearResKeepReuse();
  var cache1=new CachedTop(L(),L());
  Init.topCache(cache1,code1);
  String exe=Resources.notifiedCompiledNC();
  Resources.clearResKeepReuse();
  
  var tmp=Constants.readURL;try{
    if(resetReuseds!=null){Constants.readURL=(ignored,l)->resetReuseds;}
  
  var cache2=cache1.toNextCache();
  @SuppressWarnings("unused")//for the debugger
  var res2=Init.topCache(cache2,code2);
  String exe2=Resources.notifiedCompiledNC();
  
  Resources.clearResKeepReuse();
  var cache3=cache2.toNextCache();
  @SuppressWarnings("unused")//for the debugger
  var res3=Init.topCache(cache3,code2);
  String exe3=Resources.notifiedCompiledNC();
  assertEquals(expectedExe1,exe);
  assertEquals(expectedExe2,exe2);
  assertEquals(expectedExe3,exe3);
  }finally{Constants.readURL=tmp;}
  }
void passMany(List<String>codes,List<String> expecteds){
  var cache=new CachedTop(L(),L());
  assert codes.size()==expecteds.size();
  for(var i:range(codes.size())){
    Resources.clearResKeepReuse();
    Init.topCache(cache,codes.get(i));
    cache=cache.toNextCache();
    String exe=Resources.notifiedCompiledNC();
    System.out.println("Now at iteration "+i);
    assertEquals(expecteds.get(i),exe);
    }
  }
}
