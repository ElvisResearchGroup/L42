package testAux;

import org.junit.Test;

import helpers.TestHelper;
import facade.Configuration;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.ErrorMessage;
import ast.ErrorMessage.FinalResult;
import ast.ErrorMessage.PathNonExistant;
import ast.ExpCore;
import ast.ExpCore.ClassB;

public class TestShortPrograms {
  public void tp(String ...code) {
    TestHelper.configureForTest();
    FinalResult res0=facade.L42.runSlow(null,TestHelper.multiLine(code));
    ClassB res=res0.getTopLevelProgram();
    //ClassB.NestedClass last=(ClassB.NestedClass)res.getMs().get(res.getMs().size()-1);
    //res=(ClassB)last.getInner();
    /*if(!(res1 instanceof ClassB)){
      ExpCore ee2=TestHelper.testParseString("{'@ExpectedClass \n}").accept(new InjectionOnCore());
      TestHelper.assertEqualExp(res1,ee2);
      }*/
    //ClassB res=(ClassB)res1;
    ClassB.NestedClass nc=(ClassB.NestedClass)res.getMs().get(res.getMs().size()-1);
    ExpCore ee2=Desugar.of(Parser.parse(null,"{'@exitStatus\n'0\n\n}##star ^##")).accept(new InjectionOnCore());
    /*if(!nc.getInner().equals(ee2)){
      ExpCore ee3=Parser.parse("{'@something around a OK\n}").accept(new InjectionOnCore());
      TestHelper.assertEqualExp(res,ee3);
    }*/
    TestHelper.assertEqualExp(nc.getInner(),ee2);
  }


@Test public void test1(){tp(""
,"{() C:{'@exitStatus\n'0\n"
,"}}"
);}
@Test public void test2(){tp("{()"
,"  C:{k() type method Library m() ({'@exitStatus\n'0\n} )}"
,"  D:C.m()"
,"}");}

@Test public void test3(){tp("{()"
,"  C:{k()"
,"    type method Library ok() ({'@exitStatus\n'0\n\n} )"
,"    type method Library ko() ({'@exitStatus\n'42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{k()<:I}"
,"  D:("
,"    Any z=error AI.k()"
,"    catch error x ("
,"      on AI C.ok()"
,"      )"
,"    C.ko()"
,"    )"
,"}");}

@Test public void test4(){tp("{()"
,"  C:{k()"
,"    type method Library ok() ({'@exitStatus\n'0\n\n} )"
,"    type method Library ko() ({'@exitStatus\n'42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{k()<:I}"
,"  D:("
,"    Any z=error AI.k()"
,"    catch error x ("
,"      on I C.ok()"//here it was AI
,"      )"
,"    C.ko()"
,"    )"
,"}");}

@Test public void test5(){tp("{()"
,"  C:{k()"
,"    type method Library ok() ({'@exitStatus\n'0\n\n} )"
,"    type method Library ko() ({'@exitStatus\n'42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{k()}"//removed <:I
,"  D:(Library res=("
,"    Any z=error AI.k()"
,"    catch error x ("
,"      on I C.ko()"//here it was AI
,"      )"
,"    C.ko()"
,"    )"
,"    catch error y( on AI C.ok())"
, " res)"
,"}");}

@Test public void test6(){tp("{()"
,"  C:{k()"
,"    type method Library ok() ({'@exitStatus\n'0\n\n} )"
,"    type method Library ko() ({'@exitStatus\n'42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  AI:{k()}"//removed <:I
,"  D:("
,"    Any z=error AI.k()"
,"    catch error x ("
,"      on I C.ko()"
,"      on AI C.ok()"
,"      )"
,"    C.ko()"
,"    )"
,"}");}

@Test public void test7(){tp("{()"
,"  C:{k()"
,"    type method Library ok() ({'@exitStatus\n'0\n\n} )"
,"    type method Library ko() ({'@exitStatus\n'42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  Box:{mut k(var mut Any f)}"
,"  AI:{k()<:I}"
,"  D:("
,"    mut Box box=Box.k(f:box)"
,"    catch error x ("
,"      on I C.ko()"
,"      )"
,"    C.ok()"
,"    )"
,"}");}

@Test public void test7b(){tp("{()"
,"  C:{k()"
,"    type method Library ok() ({'@exitStatus\n'0\n\n} )"
,"    type method Library ko() ({'@exitStatus\n'42000\n\n} )"
,"    }"
,"  I:{interface}"
,"  Box:{lent k(var read Any f)}"
,"  AI:{k()<:I}"
,"  D:("
,"    lent Box box=Box.k(f:box)"
,"    Any z1=box.f(AI.k())"
,"    Any z2=error box.f()"//plus, even commenting thos lines, still seen as readonly??
,"    catch error x ("//fixed, it was a huge deal: DISASTER: splittando il blocco vado a richiedere che bx sia readable sotto la dichiarazione!
,"      on I C.ok()"
,"      )"
,"    C.ko()"
,"    )"
,"}");}

@Test public void test8(){tp("{()"
  ," D: {() type method Library id(Library that) (that)}"
  ," C: D.id({()  method Void foo() (C x= this void)}) "
  ," E: {'@exitStatus\n'0\n\n}"
  ,"}");}

@Test(expected=ErrorMessage.PathsNotSubtype.class)
public void test8b(){tp("{()"
    ," D: {() type method Library id(Library that) (that)}"
    ," C: {()  method Void foo() (D x= this void)} "
    ," E: {'@exitStatus\n'0\n\n}"
    ,"}");}

@Test(expected=ErrorMessage.PathsNotSubtype.class)
public void test8c(){tp("{()"
    ," D: {() type method Library id(Library that) (that)}"
    ," C: D.id({() method Void foo() (D x= this void)}) "
    ," E: {'@exitStatus\n'0\n\n}"
    ,"}");}

@Test(expected=ErrorMessage.PathNonExistant.class)
public void test8d(){tp("{()"
    ," A: {Bla:{}}"
    ," D: {() type method Void wrongParameter(A::BlaWrong that)void type method Library id(Library that) that}"
    ," C: D.id({()  method Void foo() void} )"
    ," E: {'@exitStatus\n'0\n\n}"
    ,"}");}

@Test(expected=ErrorMessage.PathNonExistant.class)
public void test8e(){tp("{"
    ," A:{"
    ," B:{(C::D d) }"
    ," C:{ DPr:{}  }"
    ," }"
    ," Main:{'@exitStatus"
    ," '0"
    ," }}");}
@Test(expected=ErrorMessage.PathNonExistant.class)
public void test8f(){tp("{"
    ," A:{"
    ," B:{method Void foo() (type Any unused=C::Dpr void)}"
    ," C:{ DPr:{}  }"
    ," }"
    ," Main:{'@exitStatus"
    ," '0"
    ," }}");}



@Test(expected=ErrorMessage.PathsNotSubtype.class/*PathNonExistant.class*/)
public void test9b(){tp("{()"
    ," D: {() type method Library id(Library that) (that)}"
    ," C: {()  H:{() method Void foo() (Outer2::C::E x= this void)}}"
    ," E: {'@exitStatus\n'0\n\n}"
    ,"}");}


@Test(expected=ErrorMessage.PathsNotSubtype.class/*PathNonExistant.class*/)
public void test9c(){tp("{()"
    //TODO:here, it pass if you put C::H, is this coherent?
    ," D: {() type method Library id(Library that) (that)}"
    ," C: D.id({()  H:{() method Void foo() (Outer2::C::E x= this void)}}) "
    ," F: {'@exitStatus\n'0\n\n}"
    ,"}");}

@Test(/*expected=ErrorMessage.PathsNotSubtype.class/*PathNonExistant.class*/)//correctly no error for trashing the error.
public void test9d(){tp("{()"
    ," D: {() type method Library trash(Library that) ({()})}"
    ," C: D.trash({()  H:{() method Void foo() (Outer2::C::E x= this void)}}) "
    ," E: {'@exitStatus\n'0\n\n}"
    ,"}");}


@Test public void test9(){tp("{()"
    ," D: {() type method Library id(Library that) (that)}"
    ," C: D.id({()  H:{() method Void foo() (Outer2::C::H x= this void)}}) "
    ," E: {'@exitStatus\n'0\n\n}"
    ,"}");}

@Test(expected=ErrorMessage.MethodNotPresent.class)
public void test10(){tp("{()"
    ," D: {() type method Library id(Library that) (that)}"
    ," C: D.id({()  method Void foo(D x) ( x.foo(x))}) "
    ," E: {'@exitStatus\n'0\n\n}"
    ,"}");}
@Test//(expectedExceptions=ErrorMessage.MethodNotPresent.class)
public void test11(){tp("{()"
    ," D: {() type method Library id(Library that) (that)}"
    ," C: D.id({()  method Void foo(C x) ( x.foo(x:x))}) "
    ," E: {'@exitStatus\n'0\n\n}"
    ,"}");}

@Test(expected=ErrorMessage.PathNonExistant.class)
public void test12(){tp("{()"
,"LibList:{ #apply()"
,"  T:{() }"
,"  type method"
,"  Outer0::GenericId::T id(Outer0::GenericId::T that) (that)}"
,"E: {'@exitStatus\n'0\n\n}"
,"}"
);}

@Test public void testPlaceHolder(){tp(""
,"{"
,"A:{(A x)}"
," C:( A myA=A(x:myA)  {'@exitStatus\n'0\n})"
,"}"
);}
@Test public void testPlaceHolderFactory(){tp(""
,"{"
,"A:{(A x)}"
,"Factory:{ type method A (A^ a) A(x:a)}"
/*,"C: {'@exitStatus\n'0\n\n}"*/," C:( A myA=Factory(a:myA)  {'@exitStatus\n'0\n})"
,"}"
);}
@Test(expected=ErrorMessage.PathsNotSubtype.class)
public void testPlusNotStar(){tp("{"
,"A:{ () method Library foo() }"
,"E: A().foo()"
,"}"
);}

}