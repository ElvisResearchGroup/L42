package testAux;

import org.junit.Test;

import helpers.TestHelper;
import facade.Configuration;
import facade.ErrorFormatter;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Ast;
import ast.ErrorMessage;
import ast.ErrorMessage.FinalResult;
import ast.ErrorMessage.PathNonExistant;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;

public class TestShortPrograms {
  public void tp(String ...code) {
    TestHelper.configureForTest();
    FinalResult res0;
    try{
      res0=facade.L42.runSlow(null,TestHelper.multiLine(code));
    }catch(ErrorMessage msg){
      ErrorFormatter.topFormatErrorMessage(msg);
      throw msg;
    }
    ClassB res=res0.getTopLevelProgram();
    ClassB.NestedClass nc=(ClassB.NestedClass)res.getMs().get(res.getMs().size()-1);
    ExpCore ee2=Desugar.of(Parser.parse(null,"{//@exitStatus\n//0\n\n}")).accept(new InjectionOnCore());
    TestHelper.assertEqualExp(Functions.clearCache(nc.getInner(),Ast.Stage.None),ee2);
  }


@Test public void test1(){tp(""
,"{() C:{//@exitStatus\n//0\n"
,"}}"
);}
@Test public void test2(){tp("{()"
,"  C:{k() class method Library m() ({//@exitStatus\n//0\n} )}"
,"  D:C.m()"
,"}");}

@Test public void test3(){tp("{()"
,"  C:{k()"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{k()<:I}"
,"  D:("
,"    Any z=error AI.k()"
,"    catch error AI x ("
,"      C.ok()"
,"      )"
,"    C.ko()"
,"    )"
,"}");}

@Test public void test4(){tp("{()"
,"  C:{k()"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{k()<:I}"
,"  D:("
,"    Any z=error AI.k()"
,"    catch error I x ("
,"      C.ok()"//here it was AI
,"      )"
,"    C.ko()"
,"    )"
,"}");}

@Test public void test5(){tp("{()"
,"  C:{k()"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{k()}"//removed <:I
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

@Test public void test6(){tp("{()"
,"  C:{k()"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{k()}"//removed <:I
,"  D:("
,"    Any z=error AI.k()"
,"    catch error I x ("
,"       C.ko())"
,"    catch error AI x C.ok()"
,"      "
,"    C.ko()"
,"    )"
,"}");}

@Test public void test7(){tp("{()"
,"  C:{k()"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  Box:{mut k(var fwd mut Any f)}"
,"  AI:{k()<:I}"
,"  D:("
,"    mut Box box=Box.k(f:box)"
,"    catch error I x ("
,"      C.ko()"
,"      )"
,"    C.ok()"
,"    )"
,"}");}

@Test public void test7b(){tp("{()"
,"  C:{k()"
,"    class method Library ok() ({//@exitStatus\n//0\n\n} )"
,"    class method Library ko() ({//@exitStatus\n//42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  Box:{lent k(var fwd read Any f)}"
,"  AI:{k()<:I}"
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

@Test public void test8(){tp("{()"
  ," D: {() class method Library id(Library that) (that)}"
  ," C: D.id({()  method Void foo() (C x= this void)}) "
  ," E: ( c=C {//@exitStatus\n//0\n\n})"
  ,"}");}

@Test(expected=ErrorMessage.PathsNotSubtype.class)
public void test8b(){tp("{()"
    ," D: {() class method Library id(Library that) (that)}"
    ," C: {()  method Void foo() (D x= this void)} "
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test(expected=ErrorMessage.PathsNotSubtype.class)
public void test8c(){tp("{()"
    ," D: {() class method Library id(Library that) (that)}"
    ," C: D.id({() method Void foo() (D x= this void)}) "
    ," E:( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test(expected=ErrorMessage.PathNonExistant.class)
public void test8d(){tp("{()"
    ," A: {Bla:{}}"
    ," D: {() class method Void wrongParameter(A.BlaWrong that)void class method Library id(Library that) that}"
    ," C: D.id({()  method Void foo() void} )"
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test(expected=ErrorMessage.PathNonExistant.class)
public void test8e(){tp("{"
    ," A:{"
    ," B:{(C.D d) }"
    ," C:{ DPr:{}  }"
    ," }"
    ,"Main:( c=C {//@exitStatus"
    ," //0"
    ," })}");}
@Test(expected=ErrorMessage.PathNonExistant.class)
public void test8f(){tp("{"
    ," A:{"
    ," B:{method Void foo() (class Any unused=C.Dpr void)}"
    ," C:{ DPr:{}  }"
    ," }"
    ," Main:( c=C {//@exitStatus"
    ," //0"
    ," })}");}



@Test(expected=PathNonExistant.class)
public void test9b(){tp("{()"
    ," D: {() class method Library id(Library that) (that)}"
    ," C: {()  H:{() method Void foo() (This2.C.E x= this void)}}"
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}


@Test(expected=PathNonExistant.class)
public void test9c1(){tp("{()"//focus on the difference between c1 and c2. This is the expected behaviour.
    ," D: {() class method Library id(Library that) (that)}"
    ," C: D.id({()  H:{() method Void foo() (This2.C.E x= this void)}}) "
    ," F:( c=C {//@exitStatus\n//0\n\n})"//otherwise it does not fails with optimizations on
    ,"}");}
@Test()
public void test9c2(){tp("{()"
    ," D: {() class method Library id(Library that) (that)}"
    ," C: D.id({()  H:{() method Void foo() (This2.C.H x= this void)}}) "
    ," F:( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}
@Test(/*expected=ErrorMessage.PathsNotSubtype.class/*PathNonExistant.class*/)//correctly no error for trashing the error.
public void test9d(){tp("{()"
    ," D: {() class method Library trash(Library that) ({()})}"
    ," C: D.trash({()  H:{() method Void foo() (This2.C.E x= this void)}}) "
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}


@Test public void test9(){tp("{()"
    ," D: {() class method Library id(Library that) (that)}"
    ," C: D.id({()  H:{() method Void foo() (This2.C.H x= this void)}}) "
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test(expected=ErrorMessage.MethodNotPresent.class)
public void test10(){tp("{()"
    ," D: {() class method Library id(Library that) (that)}"
    ," C: D.id({()  method Void foo(D x) ( x.foo(x))}) "
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}
@Test//(expectedExceptions=ErrorMessage.MethodNotPresent.class)
public void test11(){tp("{()"
    ," D: {() class method Library id(Library that) (that)}"
    ," C: D.id({()  method Void foo(C x) ( x.foo(x:x))}) "
    ," E: ( c=C {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test(expected=ErrorMessage.PathNonExistant.class)
public void test12(){tp("{()"
,"LibList:{ #apply()"
,"  T:{() }"
,"  class method"
,"  This0.GenericId.T id(This0.GenericId.T that) (that)}"
,"E:( c=LibList {//@exitStatus\n//0\n\n})"
,"}"
);}


@Test
public void testClassMethods1(){tp("{"
    ," D: { class method Library a() This.b()   class method Library b() {()} }"
    ," E: ( c=D.a() {//@exitStatus\n//0\n\n})"
    ,"}");}

@Test
public void testClassMethods2(){tp("{"
    ," I: { interface class method Library a()  class method Library b() }"
    ," D:{ <:I  method a() This.b()   method  b() {()} }"
    ," E: ( c=D.a() {//@exitStatus\n//0\n\n})"
    ,"}");}



@Test public void testPlaceHolder(){tp(""
,"{"
,"A:{(fwd A x)}"
," C:( A myA=A(x:myA)  {//@exitStatus\n//0\n})"
,"}"
);}
@Test public void testPlaceHolderFactory(){tp(""
,"{"
,"A:{(fwd A x)}"
,"Factory:{ class method A (fwd A a) A(x:a)}"
/*,"C: {//@exitStatus\n//0\n\n}"*/," C:( A myA=Factory(a:myA)  {//@exitStatus\n//0\n})"
,"}"
);}

@Test public void testTwoKindExc1(){tp(""
,"{"
,"A:{()}"
,"B:{()}"
," C:( "
,"  A myA=A()"
,"  exception A()"
,"  catch exception A x ( "
," {//@exitStatus\n//0\n})"
," {//@exitStatus\n//2\n})"
,"}"
);}

@Test(expected=ErrorMessage.MalformedFinalResult.class)
public void testTwoKindExc2(){tp(""
,"{"
,"A:{()}"
,"B:{()}"
," C:( "
,"  A myA=A()"
,"  exception void"
,"  catch exception A x ( "
," {//@exitStatus\n//0\n})"
," {//@exitStatus\n//0\n})"
,"}"
);}
@Test(expected=ErrorMessage.PathsNotSubtype.class)
public void testPlusNotStar(){tp("{"
,"A:{ () method Library foo() }"
,"E: A().foo()"
,"}"
);}

@Test(expected=ErrorMessage.PathsNotSubtype.class)
public void testDeepTyping1(){tp("{"
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

@Test(expected=ErrorMessage.MalformedFinalResult.class)
public void test13(){tp("{",
    " I:{ interface method I foo() }",
    "A:{ ()<:I  method I beer()}",
    "Main:(x={} {//@exitStatus\n//0\n\n})",
    " }");}

@Test(expected=ErrorMessage.MalformedFinalResult.class)
public void test13b(){tp("{",
    " I:{ interface method I foo() }",
    "A:{ ()<:I  }",
    "Main:(x={} {//@exitStatus\n//0\n\n})",
    " }");}

@Test
public void test14RelaxVarableSameName1(){tp("{",
    " A:{class method Library foo() (   var Library lib={} (x={} lib:=x)  (x={//@exitStatus\n//0\n\n} lib:=x  ) lib   )}",
    "Main:A.foo()",
    " }");}

@Test(expected=ErrorMessage.MalformedFinalResult.class)
public void test14RelaxVarableSameName2(){tp("{",
    " A:{class method Library foo() (   var Library lib={}  (x={//@exitStatus\n//0\n\n} lib:=x  )  (x={} lib:=x) lib   )}",
    "Main:A.foo()",
    " }");}

}