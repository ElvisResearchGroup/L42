package testAux;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import helpers.TestHelper;
import newTypeSystem.ErrorKind;
import newTypeSystem.FormattedError;
import platformSpecific.javaTranslation.Resources;
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

/**
 * Aden's tests.
 */

@Test(expected = ErrorMessage.UnclosedParenthesis.class)
public void testUnclosedParen() {
	tp("{"
	,"C: ("
	,"{}");
}

//If the test above throws an UnclosedParenthesis should this one throw an unopenedParenthesis?
//This throws the correct error in the IDE. It throws ParenthesisMismatchRange instead
// Was a bug as of 18/05/18.
@Test(expected = ErrorMessage.UnopenedParenthesis.class)
public void testUnopenedParen() {
	tp("{"
	,"C:"
	,"{})"
	,"}");
}

// If run in the IDE this will break the IDE until you use a text editor to change the file.
// Kind of a bug, was a bug as of 18/05/18.
@Test
public void testLocalhost() {
	tp("{reuse localhost/nanoBase0"
			,"C:{"
				,"return ExitCode.normal()"
			,"}"
	,"}");
}


@Test
public void testNanoBase() {
	tp("{reuse L42.is/nanoBase0"
			,"C:{"
				,"return ExitCode.normal()"
			,"}"
	,"}");
}


// These towels are no longer supported.

// We should be able to use this towel?
/*@Test
public void testMicroBase() {
	tp("{reuse L42.is/microBase"
			,"A: {"
				,"return ExitCode.normal()"
			,"}"
	,"}");
}

// We should also be able to use this towel?
@Test
public void testOtherNanobase() {
	tp("{reuse L42.is/nanoBase2"
		,"C:{"
			,"return ExitCode.normal()"
		,"}"
	,"}");
}*/


@Test(expected=ErrorMessage.VariableUsedNotInScope.class)
public void testUnderscoreVariableName(){
	tp("{"
	," C:{class method Any m(Any that) that}"
	," Main: ("
	,"  arr = C.m(_)"
	,"  {})"
	,"}");
}

//This should be correct??
// This was current as of 18/05/18
@Test//(expected = ErrorMessage.NotWellFormedMsk.class)
public void testAssignExitCode() {
	tp("{"
	,"C:{ s = return ExitCode.normal()"
	,"}"
	,"}");
}//TODO: marco think:
//-according to speck this should work, but
//look so horrible that may be a bug in the speck?
//and... today do not work, so, there is a delta between impl and speck?

@Test(expected = ErrorMessage.InvalidCharacter.class)
public void testTabBadCharacter() {
	tp("{"
	,"C:{ s = 12Num"
	,"	s = 13Num"
	,"}"
	,"}");
}

//This should work as nanoBase doesn't load in the string class.
@Test
public void testClassnameSOk() {
	tp("{reuse L42.is/nanoBase0"
		,"S: {"
		,  "return ExitCode.normal()"
		,"}"
		,""
		,"}");
}


//This probably shouldn't work, but it throws a Java error.
//Expecting some sort of parsing error? Or prehaps a meta error.
// Was a bug as of 18/05/18.
@Test 
public void testNoClassname() {
	tp("{"
	,"{Debug()}"
	,"}");
}


/**
 * These tests use AdamTowel02, therefore the first one may take a while to run before the towel
 * is cached.
 */

@Test
public void testCollectionNotEmpty() {
	tp("{reuse L42.is/AdamTowel02"
	,"CacheAdamTowel02:Load.cacheTowel()"
	,"Strs: Collections.vector(of: S)"
	,"C: {"
		,"Strs t = Strs[S\"1\"; S\"2\"; S\"3\"]"
		,"X[t.isEmpty() == Bool.false()]"
		,"return ExitCode.normal()"
	,"}"
	,"}");
}

// Test collection size == number of elements.
@Test
public void testCollectionSize() {
	tp("{reuse L42.is/AdamTowel02"
	,"CacheAdamTowel02:Load.cacheTowel()"
	,"Strs: Collections.vector(of: S)"
	,"C: {"
		,"Strs t = Strs[S\"1\"; S\"2\"; S\"3\"]"
		,"X[t.size() == 3Size]"
		,"return ExitCode.normal()"
	,"}"
	,"}");
}

// Test that an empty collection has a size of 0.
@Test
public void testEmptyCollection() {
	tp("{reuse L42.is/AdamTowel02"
	,"CacheAdamTowel02:Load.cacheTowel()"
	,"Strs: Collections.vector(of: S)"
	,"C: {"
		,"Strs t = Strs[]"
		,"X[t.size() == 0Size]"
		,"return ExitCode.normal()"
	,"}"
	,"}");
}

//This needs to cache the towel to use S (string) as the class name.
// This should fail as we're using S as a class name which is already in the towel.
@Test(expected = ErrorMessage.NotWellFormedMsk.class)
public void testClassnameS() {
	tp("{reuse L42.is/AdamTowel02"
	,""
	,"S: ("
	,  "return ExitCode.normal()"
	,"{})"
	,""
	,"}");
}

// Should throw a type error, not an assertion error as we shouldn't be able to update it.
// This was a bug as of 18/05/18.
@Test
public void testBadUpdate() {
	tp("{"
	,"reuse L42.is/AdamTowel02"
	,"C:( s = 12Num"
	,"s := s.#plus(30Num)"
	,"{})"
	,"}");
}

@Test
public void testCollectionMethod() {
	tp("{"
	,"reuse L42.is/AdamTowel02"
	,"Nums: Collections.vector(of: Num)"
	,"A: {"
	,"class method Nums give() {"
		,"return Nums[1Num; 2Num; 3Num]"
	,"}"
	,"}"
	,"C: {"
		,"Nums t = A.give()"
		,"Nums t2 = Nums[4Num]"
		,"X[t.size() != t2.size()]"
		,"return ExitCode.normal()"
	,"}"
	,"}");
}

//Test equality of collections and test having the exitCode on the only reachable path.
@Test
public void testCollectionsEqual() {
	tp("{reuse L42.is/AdamTowel02"
	,"CacheAdamTowel02:Load.cacheTowel()"
	,"Nums: Collections.vector(of: Num)"
	,"C: {"
		,"t1 = Nums[1Num; 2Num]"
		,"Nums t2 = Nums[1Num; 2Num]"
		,"if !t1.equals(t2) ("
			,""
		,")"
		,"else ("
		,"return ExitCode.normal()"
		,")"
	,"}"
	,"}");
}

//Test equality of collections which are not equal.
@Test
public void testCollectionsNotEqual() {
	tp("{reuse L42.is/AdamTowel02"
	,"CacheAdamTowel02:Load.cacheTowel()"
	,"Nums: Collections.vector(of: Num)"
	,"C: {"
		,"t1 = Nums[1Num; 2Num]"
		,"Nums t2 = Nums[1Num]"
		,"if !t1.equals(t2) ("
			,"return ExitCode.normal()"
		,")"
		,"error X\"\""
	,"}"
	,"}");
}

// If this code is run twice in the IDE it will fail, but that is not the case here.
@Test
public void testSymbol() {
	tp("{reuse L42.is/AdamTowel02"
	,"C: {"
		,"class method S m(S that) {"
			,"return that"
		,"}"
	,"}"
	,"Main: {"
		,"t = C.m(that: S\"t\")"
		,"return ExitCode.normal()"
	,"}"
	,"}");
}

@Test //We should expect some sort of 42 stackoverflow error. We can count to about 5800.
// This was a bug as of 18/05/2018. Note: The equivalent Java program counts to about 23000.
public void testStackOverflow() {
	tp("{reuse L42.is/AdamTowel02"
		,"A: {"
			,"class method Num a(Num that) {"
				,"if that == 10000Num ("
					,"return that"
				,")"
				,"else ("
					,"return A.a(that + 1Num)"
				,")"
			,"}"
		,"}"
		,"B: {"
			,"Num s = A.a(1Num)"
			,"return ExitCode.normal()"
		,"}"
	,"}");
}

@Test
public void testGoodRecursion() {
	tp("{reuse L42.is/AdamTowel02"
			,"A: {"
				,"class method Num a(Num that) {"
					,"if that == 1000Num ("
						,"return that"
					,")"
					,"else ("
						,"return A.a(that + 1Num)"
					,")"
				,"}"
			,"}"
			,"B: {"
				,"Num s = A.a(1Num)"
				,"return ExitCode.normal()"
			,"}"
		,"}");
}

// Throws an assertion error. It should throw maybe a parsing error or maybe a method not present.
// This was a bug as of 18/05/2018.
@Test
public void testMethodCallOnVariable() {
	tp("{reuse L42.is/nanoBase0"
			,"A: {"
				,"c = c()"
				,"return ExitCode.normal()"
			,"}"
	,"}");
}

@Test
public void testGetCollectionValue() {
	tp("{reuse L42.is/AdamTowel02"
			,"Nums: Collections.vector(of: Num)"
			,"A: Data <>< {"
				,"Nums list"
				,"method Num search(Size index) {"
					,"return this.list().val(index)"
				,"}"
			,"}"
			,""
			,"B: {"
				,"Nums toCheck = Nums[3Num; 2Num; 4Num; 5Num]"
				,"A a = A(list: toCheck)"
				,"X[a.search(index: 1Size) == 2Num]"
				,"return ExitCode.normal()"
			,"}"
	,"}");
}

// This works correctly but there isn't a suitable error to expect.
@Test//(expected = ErrorMessage.)
public void testIndexOutOfBounds() {
	try{tp("{reuse L42.is/AdamTowel02"
			,"Nums: Collections.vector(of: Num)"
			,"A: {"
				,"Nums toCheck = Nums[3Num; 2Num; 4Num; 5Num]"
				,"Num outOfBounds = toCheck.val(12Size)"
				,"return ExitCode.normal()"
			,"}"
	,"}");}
	catch(Resources.Error e) {
	  Object e42 = e.unbox;
	  if(!e42.getClass().getName().contains("Guard£CParameter")) {
              fail();
	  }
	}
}


@Test
public void testNegativeNumbers() {
	tp("{reuse L42.is/AdamTowel02"
			,"A: {"
				,"Num negative = Num\"-1\""
				,"return ExitCode.normal()"
			,"}"
	,"}");
}

// Division by zero. Is it intended behaviour to not throw an error?
// This was a bug as of 18/05/2018. 
@Test
public void testDivideByZero() {
	tp("{reuse L42.is/AdamTowel02"
			,"A: {"
				,"Num negative = Num\"-1\""
				,"Debug(negative / 0Num)"
				,"Debug(0Num / 0Num)"
				,"return ExitCode.normal()"
			,"}"
	,"}");
}

/*// This throws a FileNotFoundException.
@Test
public void testIntDivision() {
	tp("{reuse L42.is/AdamTowel02"
			,"Int: Load <>< {reuse L42.is/Numbers/Int}"
			,"A: {"
				,"Debug(0Int / 0Int)"
				,"return ExitCode.normal()"
			,"}"
	,"}");

}
// Should this work? Should the predictor be able to find the type of 1? And not
// have it as void. It works on methods, does this not count constructors?
// It could also be nice to have predictions on "Num n = 12\".
@Test
public void testSuggestion() {
	tp("{reuse L42.is/AdamTowel02"
			,"A: Data <>< {"
				,"Num n"
			,"}"
			,""
			,"B: {"
				,"A a = A(n: 321312\\)"
				,"return ExitCode.normal()"
			,"}"
	,"}");
}
*/
// Is this error due to the fact that we don't have a field in a class extending
// Data therefore the equals method can't be constructed properly?
@Test
public void testBabelFish() {
	tp("{reuse L42.is/AdamTowel02"
			,"A: Data <>< {"
			,"}"
			,"B: {"
				,"return ExitCode.normal()"
			,"}"
	,"}");
}


@Test
public void testImplementAndBabelFish() {
	tp("{reuse L42.is/AdamTowel02"
			,"A: Data <>< {implements S"
			,"}"
			,"B: {"
				,"return ExitCode.normal()"
			,"}"
	,"}");
}
}