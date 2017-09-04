package testAux;

import org.junit.Before;
import org.junit.Test;

import helpers.TestHelper;
import newTypeSystem.ErrorKind;
import newTypeSystem.FormattedError;
import platformSpecific.javaTranslation.Resources;
import facade.Configuration;
import facade.ErrorFormatter;
import facade.L42;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Ast;
import ast.ErrorMessage;
import ast.ErrorMessage.FinalResult;
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;

public class TestShortPrograms {
  public static void tp(ErrorKind kind,String ...code) {

  try{tp(code);assert false;}
  catch(FormattedError err){
    assert err.kind==kind;
    }
  }
  public static void tp(String ...code) {
    TestHelper.configureForTest();
    FinalResult res0;
    try{
      res0=facade.L42.runSlow(null,Functions.multiLine(code));
    }catch(ErrorMessage msg){
      ErrorFormatter.topFormatErrorMessage(msg);
      throw msg;
    }
    ClassB res=res0.getTopLevelProgram();
    ClassB.NestedClass nc=(ClassB.NestedClass)res.getMs().get(res.getMs().size()-1);
    ExpCore ee2=Desugar.of(Parser.parse(null,"{//@exitStatus\n//0\n\n}")).accept(new InjectionOnCore());
    TestHelper.assertEqualExp(nc.getInner(),ee2);
  }

@Test public void test1(){tp(""
,"{ C:{//@exitStatus\n//0\n"
,"}}"
);}
@Test public void test2(){tp("{"
,"  C:{ class method Library m() ({//@exitStatus\n//0\n} )}"
,"  D:C.m()"
,"}");}

@Test public void test3(){tp("{"
,"  C:{"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{ implements I class method This k()}"
,"  D:("
,"    Any z=error AI.k()"
,"    catch error AI x ("
,"      C.ok()"
,"      )"
,"    C.ko()"
,"    )"
,"}");}

@Test public void test4(){tp("{"
,"  C:{"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{ implements I class method This k()}"
,"  D:("
,"    Any z=error AI.k()"
,"    catch error I x ("
,"      C.ok()"//here it was AI
,"      )"
,"    C.ko()"
,"    )"
,"}");}

@Test public void test5(){tp("{"
,"  C:{"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{class method This k()}"//removed  implements I
,"  D:(Library res=("
,"    Any z=error AI.k()"
,"    catch error I x ("
,"      C.ko()"//here it was AI
,"      )"
,"    C.ko()"
,"    )"
,"    catch error AI y ( C.ok())"
, " res)"
,"}");}

@Test public void test6(){tp("{"
,"  C:{"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{class method This k()}"//removed  implements I
,"  D:("
,"    Any z=error AI.k()"
,"    catch error I x ("
,"       C.ko())"
,"    catch error AI x C.ok()"
,"      "
,"    C.ko()"
,"    )"
,"}");}

@Test public void test7(){tp("{"
,"  C:{"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  Box:{class method mut This k(fwd mut Any f)  mut method mut Any f()}"
,"  AI:{ implements I class method This k()}"
,"  D:("
,"    mut Box box=Box.k(f:box)"
,"    catch error I x ("
,"      C.ko()"
,"      )"
,"    C.ok()"
,"    )"
,"}");}

/* Not working, lent boxes disabled to get single constructor
@Test public void test7b(){tp("{()"
,"  C:{k()"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  Box:{lent k(var fwd read Any f)}"
,"  AI:{k() implements I}"
,"  D:("
,"    lent Box box=Box.k(f:box)"
,"    Any z1=box.f(AI.k())"
,"    Any z2=error box.f()"//plus, even commenting thos lines, still seen as readonly??
,"    catch error I x ("//fixed, it was a huge deal: DISASTER: splittando il blocco vado a richiedere che bx sia readable sotto la dichiarazione!
,"      C.ok()"
,"      )"
,"    C.ko()"
,"    )"
,"}");}
*/
@Test public void test8(){tp("{"
  ," D: { class method Library id(Library that) (that)}"
  ," C: D.id({  method Void foo() (C x= this void)}) "
  ," E: ( c=C {//@exitStatus\n//0\n\n})"
  ,"}");}

@Test//(expected=ErrorMessage.PathsNotSubtype.class)
public void test8b(){tp(ErrorKind.NotSubtypeClass,"{"
    ," D: { class method Library id(Library that) (that)}"
    ," C: { method Void foo() (D x= this void)} "
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test//(expected=ErrorMessage.PathsNotSubtype.class)
public void test8c(){tp(ErrorKind.NotSubtypeClass,"{"
    ," D: { class method Library id(Library that) (that)}"
    ," C: D.id({ method Void foo() (D x= this void)}) "
    ," E:( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test(expected=ErrorMessage.PathMetaOrNonExistant.class)
public void test8d(){tp("{"
    ," A: {Bla:{}}"
    ," D: { class method Void wrongParameter(A.BlaWrong that)void class method Library id(Library that) that}"
    ," C: D.id({  method Void foo() void} )"
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test(expected=ErrorMessage.PathMetaOrNonExistant.class)
public void test8e(){tp("{"
    ," A:{"
    ," B:{C.D d }"
    ," C:{ DPr:{}  }"
    ," }"
    ,"Main:( c=C {//@exitStatus"
    ," //0"
    ," })}");}
@Test(expected=ErrorMessage.PathMetaOrNonExistant.class)
public void test8f(){tp("{"
    ," A:{"
    ," B:{method Void foo() (class Any unused=C.Dpr void)}"
    ," C:{ DPr:{}  }"
    ," }"
    ," Main:( c=C {//@exitStatus"
    ," //0"
    ," })}");}


@Test(expected=PathMetaOrNonExistant.class)
public void test9b(){tp("{"
    ," D: { class method Library id(Library that) (that)}"
    ," C: {  H:{ method Void foo() (This2.C.E x= this void)}}"
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}


@Test(expected=PathMetaOrNonExistant.class)
public void test9c1(){tp("{"//focus on the difference between c1 and c2. This is the expected behaviour.
    ," D: { class method Library id(Library that) that}"
    ," C: D.id({ H:{ method Void foo() (This2.C.E x= this void)}}) "
    ," F:( c=C {//@exitStatus\n//0\n\n})"//otherwise it does not fails with optimizations on
    ,"}");}
@Test()
public void test9c2(){tp("{"
    ," D: { class method Library id(Library that) that}"
    ," C: D.id({ H:{ method Void foo() (This2.C.H x= this void)}}) "//here C.H vs C.E is the only difference
    ," F:( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}
@Test(/*expected=ErrorMessage.PathsNotSubtype.class/*PathNonExistant.class*/)//correctly no error for trashing the error.
public void test9d(){tp("{"
    ," D: { class method Library trash(Library that) ({})}"
    ," C: D.trash({  H:{ method Void foo() (This2.C.E x= this void)}}) "
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}


@Test public void test9(){tp("{"
    ," D: { class method Library id(Library that) (that)}"
    ," C: D.id({  H:{ method Void foo() (This2.C.H x= this void)}}) "
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test(expected=ErrorMessage.MethodNotPresent.class)
public void test10(){tp("{"
    ," D: {class method Library id(Library that) (that)}"
    ," C: D.id({  method Void foo(D x) ( x.foo(x))}) "
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}
@Test//(expectedExceptions=ErrorMessage.MethodNotPresent.class)
public void test11(){tp("{"
    ," D: { class method Library id(Library that) (that)}"
    ," C: D.id({  method Void foo(C x) ( x.foo(x:x))}) "
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test(expected=ErrorMessage.PathMetaOrNonExistant.class)
public void test12(){tp("{"
,"LibList:{"
,"  T:{ }"
,"  class method"
,"  This0.GenericId.T id(This0.GenericId.T that) (that)}"
,"E:( class LibList c=LibList {//@exitStatus\n//0\n\n})"
,"}"
);}


@Test
public void testClassMethods1(){tp("{"
    ," D: { class method Library a() This.b()   class method Library b() {} }"
    ," E: ( c=D.a() {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test
public void testClassMethods2(){tp("{"
    ," I: { interface class method Library a()  class method Library b() }"
    ," D:{  implements I  method a() This.b()   method  b() {} }"
    ," E: ( c=D.a() {//@exitStatus\n//0\n\n})"
    ,"}");}


@Test public void testPlaceHolder(){tp(""
,"{"
,"A:{A x, class method A(fwd A x)}"
," C:( A myA=A(x:myA)  {//@exitStatus\n//0\n})"
,"}"
);}
@Test public void testPlaceHolderFactory(){tp(""
,"{"
//TODO: try with a mut constructor too,"A:{(fwd A x)}"
,"A:{A x, class method A(fwd A x)}"
,"Factory:{ class method A (fwd A a) A(x:a)}"
/*,"C: {//@exitStatus\n//0\n\n}"*/
," C:( A myA=Factory(a:myA)  {//@exitStatus\n//0\n})"
,"}"
);}

@Test public void testTwoKindExc1(){tp(""
,"{"
,"A:{class method This()}"
,"B:{}"
," C:( "
,"  A myA=A()"
,"  exception A()"
,"  catch exception A x ( "
," {//@exitStatus\n//0\n})"
," {//@exitStatus\n//2\n})"
,"}"
);}

@Test()
public void testTwoKindExc2(){try{tp(""
,"{"
,"A:{class method This()}"
,"B:{}"
," C:( "
,"  A myA=A()"
,"  exception void"
,"  catch exception A x ( "
," {//@exitStatus\n//0\n})"
," {//@exitStatus\n//0\n})"
,"}"
);}
catch(ErrorMessage.MalformedFinalResult exc1) {}
catch(Resources.Exception exc2) {}
//both ok, depend on details in reduction strategy

}
@Test(expected=ErrorMessage.NotOkToStar.class)
public void testPlusNotStar(){tp("{"
,"A:{ class method This () method Library foo() }"
,"E: A().foo()"
,"}"
);}

@Test//(expected=ErrorMessage.PathsNotSubtype.class)
public void testDeepTyping1(){tp(ErrorKind.NotSubtypeClass,"{"
    ," D: { class method Library wrong()  { A:{method Void v(Any a) a } } }"
    ," E: ( Library ignore=D.wrong(), {//@exitStatus\n//0\n\n})"
    ,"}");}
@Test(expected=ErrorMessage.MethodNotPresent.class)
public void testDeepTyping2(){tp("{"
    ," D: { class method Library wrong()  { A:{method Void v() this.notDeclared() } } }"
    ," E: ( Library ignore=D.wrong(), {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test(expected=ErrorMessage.MethodNotPresent.class)
public void testDeepTyping3(){tp("{"
    ," D: { class method Library wrong()  { A:{method Void v() this.notDeclared() } } }"
    ," E: ( Void ignore=D.wrong(), {//@exitStatus\n//0\n\n})"//we check that methodNotPresent has priority over PathsNotSubtype in this case
    ,"}");}
@Test//TODO:(expected=ErrorMessage.MalformedFinalResult.class)
public void test13(){tp("{",
    " I:{ interface method I foo() }",
    "A:{  implements I  method I beer()}",
    "Main:(x={} {//@exitStatus\n//0\n\n})",
    " }");}

@Test //TODO: why was (expected=ErrorMessage.MalformedFinalResult.class)
public void test13b(){tp("{",
    " I:{ interface method I foo() }",
    "A:{ implements I  }",
    "Main:(x={} {//@exitStatus\n//0\n\n})",
    " }");}

@Test
public void test14RelaxVarableSameName1(){tp("{",
    " A:{class method Library foo() (   "
    + "  var Library lib={}"
    + "  (x={} lib:=x)"
    + "  (x={//@exitStatus\n//0\n\n} lib:=x  )"
    + "   lib   )}",
    "Main:A.foo()",
    " }");}

@Test(expected=ErrorMessage.MalformedFinalResult.class)
public void test14RelaxVarableSameName2(){tp("{",
    " A:{class method Library foo() (   var Library lib={}  (x={//@exitStatus\n//0\n\n} lib:=x  )  (x={} lib:=x) lib   )}",
    "Main:A.foo()",
    " }");}

@Test
public void test2levels(){tp("{",
    " A:( void {B: ( void {class method Library foo() {//@exitStatus\n//0\n\n}   })})",
    "Main:A.B.foo()",
    " }");}


@Test
public void test5levels(){tp("{",
    " A:( void {B: ( void {C:( void {D:( void {E:( void {class method Library foo() {//@exitStatus\n//0\n\n}   })})})})})",
    "Main:A.B.C.D.E.foo()",
    " }");}


@Test
public void testComposition1(){tp("{",
    "Op:"+operatorAccess(),
    " A:{interface method Void m()}",
    " B:{ class method Library (){  implements A method m()void} }",
    " C:Op.compose(left:B(),right:{ implements A})",
    " C1:( void {})",
    " D1:{D2:Op.compose(left:B(),right:{ implements A})}",
    " D:Op.compose(left:B(),right:{ implements A})",
    "Main:{//@exitStatus\n//0\n\n}",
    " }");}

@Test
public void testComposition2(){tp("{",
    "Op:"+operatorAccess(),
    " A:{interface method A m(A that)}",
    " B:{ class method Library (){  implements A method m(that)that} }",
    " C:Op.compose(left:B(),right:{ implements A})",
    " D1:{D2:Op.compose(left:B(),right:{ implements A})}",
    " D:Op.compose(left:B(),right:{ implements A})",
    "Main:{//@exitStatus\n//0\n\n}",
    " }");}

@Test
public void testComposition3(){tp("{",
    "Op:"+operatorAccess(),
    " A:{interface method A m1(A that)  method A m2(A that)}",
    " B:{ class method Library (){  implements A method m1(that) this.m2(that)} }",
    " C:Op.compose(left:B(),right:{ implements A method m2(that) this})",
    " D1:{D2:Op.compose(left:B(),right:{ implements A method m2(that) this})}",
    " D:Op.compose(left:B(),right:{ implements A method m2(that) this})",
    "Main:{//@exitStatus\n//0\n\n}",
    " }");}


@Test
public void testComposition4(){tp("{",
    "Op:"+operatorAccess(),
    " A:{interface method A m1(A that)  method A m2(A that)}",
    " B:{ class method Library (){  implements A method m1(that) this.m2(that)} }",
    " C:Op.compose(left:B(),right:{ implements A method m2(that) this})",
    " N:{class method Library (){",
    "    Op:"+operatorAccess(),
    "     A:{interface method A m1(A that)  method A m2(A that)}",
    "     B:{ class method Library (){  implements A method m1(that) this.m2(that)} }",
    "     C:Op.compose(left:B(),right:{ implements A method m2(that) this})",
    " }}",
    " D:Op.compose(left:B(),right:{ implements A method m2(that) this})",
    "Main:{//@exitStatus\n//0\n\n}",
    " }");}


@Test
public void testComposition5(){tp("{",
    "Op:"+operatorAccess(),
    " A:{interface method Void m() }",
    " B:{ class method Library (){  implements A method m() void } }",
    " N:Op.compose(left:B(),right:{ ",
    "     A:{interface method Void m() }",
    "     B:{ class method Library (){  implements A method m() void } }",
    "     C:Op.compose(left:B(),right:{ implements A })",
    " })",
    "Main:{//@exitStatus\n//0\n\n}",
    " }");}


@Test
public void testComposition6(){tp("{",
    "Op:"+operatorAccess(),
    " A:{interface method A m1(A that)  method A m2(A that)}",
    " B:{ class method Library (){  implements A method m1(that) this.m2(that)} }",
    " N:Op.compose(left:{},right:{ ",
    "    Op:"+operatorAccess(),
    "     A:{interface method A m1(A that)  method A m2(A that)}",
    "     B:{ class method Library (){  implements A method m1(that) this.m2(that)} }",
    "     C:Op.compose(left:B(),right:{ implements A method m2(that) this})",
    " })",
    "Main:{//@exitStatus\n//0\n\n}",
    " }");}


@Test
public void testComposition7(){tp("{",
    "Op:"+operatorAccess(),
    " Q:{ interface method Void m() }",
    " A:{interface  implements  Q   B:{ class method Library (Library that) Op.compose(left:that,right:{  implements A method m() void }) }}",
    " N:Op.compose(left:{},right:{ ",
    "     Q:{ interface method Void m() }",
    "     A:{interface  implements Q B:{ class method Library (Library that) Op.compose(left:that,right:{  implements A method m() void }) }}",
    "     BB:{C:A.B({ implements A })}",
    " })",
    "Main:{//@exitStatus\n//0\n\n}",
    " }");}
@Test
public void testPluginParts1(){tp("{",
    "L:"+listAccess(),
    "Q:(L l=L() L.N sz=l.size()  ",
    "  l.add(sz) L.N e=l.get(sz) {})",
    "Main:{//@exitStatus\n//0\n\n}",
    " }");}
@Test
public void testPluginParts1Fail(){tp("{",
    "L:"+listAccess(),
    "Q:(L l=L() L.N sz=l.size()  ",
    "  L.N e=l.get(sz)",
    "  catch error Library xx {//@exitStatus\n//0\n\n}",
    " {/*fail*/})",
//    "Main:{//@exitStatus\n//0\n\n}",
    " }");}
@Test
public void testPluginPartsWrapper(){tp("{",
    "W:{//@plugin is.L42.connected.withSafeOperators",
    " //@pluginPart is.L42.connected.withSafeOperators.pluginWrapper.PlgWrapperGenerator",
    "class method Library <><(Library that)use This",
    " check #main(_1_Library:that)",
    " error void}",
    "A:W<><{/*@plugin  toFix @pluginPart "+A.class.getName()+" */",
    " class method Void #pluginUnresponsive(Library binaryRepr) void  ",
    " class method This()",
    " method This m()",
    " class method This0 k()",
    " method Void foo(This bar) exception This",
    " } ",
    "Main:( A a= A() A a0=a.m() A a1=A.k() ",
    " a.foo(bar:a0)",
    " catch exception A x {//@exitStatus\n//0\n\n}",
    " {/*fail*/})}");}
public static class A extends RuntimeException{
  public A m(){return this;}
  public static A k(){return new A();}
  public void foo(A bar){throw bar;}
  }
/*@Test
public void testCompositionAT2() throws Throwable{
  L42.trustPluginsAndFinalProgram=true;
  //new TestBase01()._01_00DeployAdamTowel01();
  try{tp("{TOP:{reuse L42.is/AdamTowel01",
    "Resource:{  reuse L42.is/AdamTowel01",
    "   A:{ Foo:This1.Message.$<><{ implements This2.Message} }    }}",
    "Main:{//@exitStatus\n//0\n\n}",
    " }");}
    finally{L42.trustPluginsAndFinalProgram=false;}
    }
*/



public static final String operatorAccess(){return
"{"+
  "/*@plugin  toFix"+
  "@pluginPart is.L42.connected.withSafeOperators.refactor.Compose*/"+
  "class method Library compose(Library left, Library right) "+
  "  use This0 check #compose(_1_ast%ExpCore$ClassB:left, _2_ast%ExpCore$ClassB:right)"+
  "  error void}";
/*
Redirect: {//@plugin  toFix
  //@pluginPart is.L42.connected.withSafeOperators.refactor.Redirect
class method Library redirectS(Library that, This2.S src, class Any dest)
  use This0 check #redirectS(_1_ast%ExpCore$ClassB:that, _2_java%lang%String:src.#binaryRepr(), _3_ast%Ast$Path:dest)
  error void
*/
}

public static final String listAccess(){return
"{//@plugin\n"//new lines not needed anymore
+"   //someUrlToFix\n"
+"   // @pluginPart java.util.ArrayList\n"
+"class method This (Library repr)"
+"method Library repr()"
+"class method\n"
+"This ()\n"
+"  This(repr:use This\n"
+"    check new()\n"
+"    error void)\n"
+"method\n"
+"Void add(N that) (\n"
+"    Library x=use This\n"//add return a boolean
+"      check add(_this:this.repr(),_1_java%lang%Object:that.repr())\n"
+"      error void"
+ "   void)\n"
+"method\n"
+"N get(N that)\n"
+"    N(repr:use This\n"
+"      check get(_this:this.repr(),_1_int:that.repr())\n"
+"      error void)\n"
+"method\n"
+"N size()\n"
+"  N(repr:use This\n"
+"    check size(_this:this.repr())\n"
+"    error void)\n"
+"N:{//@plugin\n"
+"   //someUrlToFix\n"
+"   //java.util.ArrayList\n"
+"   class method This (Library repr)"
+"   method Library repr()}"
+"  }\n";
}
//TODO: file 2 line 184, to avoid evil push
@Test public void testMultipleImplementSameInterface(){tp("{"+
"  A:{ "+
"    B:{ "+
"       C:{interface class method Void m() "+
"          } "+
"       D:{implements C B.C A.B.C "+
"          method m() void} "+
"       } "+
"    class method Library ident(Library that) (that) "+
"    } "+
"  E:A.ident({//@exitStatus\n//0\n\n}) "+
"  }");
}


}