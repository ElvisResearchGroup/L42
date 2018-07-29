package newTypeSystem;

import helpers.TestHelper;

import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Ast.Stage;
import ast.ErrorMessage;
import ast.ErrorMessage.MethodNotPresent;
import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Phase;
import programReduction.Program;

public class TestTypeSystemOk {
  @RunWith(Parameterized.class)
  public static class TestOk {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String original;
    @Parameter(2) public String annotated;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
  {lineNumber(),
  "{C:{class method Void foo() (This0.foo())} }",
  "{C:{class method Void foo() (This0.foo())}##star^## }##star^##"
},{lineNumber(),
  "{C:{E:{class method Void foo() (This1.foo())} class method Void foo() (D.foo())} D:{class method Void foo() (C.E.foo())}}",
  "{C:{class method Void foo() (D.foo()) E:{class method Void foo() (This1.foo())}} D:{class method Void foo() (C.E.foo())}}"
},{lineNumber(),
  "{K:{E:{class method Void foo() (This2.C.foo())}} C:{class method Void foo() (D.foo())} D:{class method Void foo() (K.E.foo())}}",
  "{K:{E:{class method Void foo() (This2.C.foo())}##star^##}##star ^## C:{class method Void foo() (D.foo())}##star^## D:{class method Void foo() (K.E.foo())}##star^##}##star^##"
},{lineNumber(),
  "{K:{ E:{class method C foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",
  "{K:{ E:{class method C foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}"
  //norm//NO, Norm is executed only in the extracted method
//},{"This0.C",
//  "{K:{E:{class method C.foo() foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",
//  "{K:{E:{class method C foo() (C.foo())}##plus^##}##plus ^## C:{class method C foo() (D.foo())}##plus^## D:{class method C foo() (K.E.foo())}##plus^##}##plus^##"
//},{"This0.C",
//  "{K:{E:{class method C.foo().foo() foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",
//  "{K:{E:{class method C foo() (C.foo())}##plus^##}##plus^## C:{class method C foo() (D.foo())}##plus^## D:{class method C foo() (K.E.foo())}##plus^##}##plus^##"
},{lineNumber(),
  "{C:{ method Void foo() (This0 x= this void)} }",
  "{C:{ method Void foo() (This0 x= this void)}##star^## }##star^##"
},{lineNumber(),
  "{C:{ method Void foo() (C x= this void)} }",
  "{C:{ method Void foo() (C x= this void)}##star^## }##star^##"

},{lineNumber(),
"{"
+ "method Void (class A a, class B b)#?bin(a:a,b:b)"
+ "A:{class method Void #bin#0a(class B b)void} B:{class method Void #bin#0b(class A a)void}\n"
+ "}",
"{"
+ "method Void #apply(class A a, class B b) a.#bin#0a(b:b)"
+ "A:{class method Void #bin#0a(class B b)void} B:{class method Void #bin#0b(class A a)void}\n"
+ "}",

},{lineNumber(),
"{"
+ "method Void (class A a, class B b)#?bin(a:a,b:b)"
+ "A:{class method Void #bin#1a(class B b)void} B:{class method Void #bin#0b(class A a)void}\n"
+ "}",
"{"
+ "method Void #apply(class A a, class B b) b.#bin#0b(a:a)"
+ "A:{class method Void #bin#1a(class B b)void} B:{class method Void #bin#0b(class A a)void}\n"
+ "}",

},{lineNumber(),
"{"
+ "method Void (class A a, class B b)#?bin(a:a,b:b)"
+ "A:{class method Void #bin#1a(class B b)void} B:{class method Void #bin#0b(read A a)void}\n"
+ "}",
"{"
+ "method Void #apply(class A a, class B b) a.#bin#1a(b:b)"
+ "A:{class method Void #bin#1a(class B b)void} B:{class method Void #bin#0b(read A a)void}\n"
+ "}",

},{lineNumber(),
"{"
+ "method Void (class A a, class B b)#?bin(a:a,b:b)"
+ "A:{class method Void #bin#1a(read B b)void} B:{class method Void #bin#0b(read A a)void class method Void #bin#10b(class A a)void}\n"
+ "}",
"{"
+ "method Void #apply(class A a, class B b) b.#bin#10b(a:a)"
+ "A:{class method Void #bin#1a(read B b)void} B:{class method Void #bin#0b(read A a)void class method Void #bin#10b(class A a)void}\n"
+ "}",

},{lineNumber(),
"{"
+ "method Void (class A a, class B b,class C c)#?m3(a:a,b:b,c:c)"
+ "A:{class method Void #m3#0a(class B b,class C c)void}"
+ "B:{class method Void #m3#0b(class A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}",
"{"
+ "method Void (class A a, class B b,class C c) a.#m3#0a(b:b,c:c)"
+ "A:{class method Void #m3#0a(class B b,class C c)void}"
+ "B:{class method Void #m3#0b(class A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}"

},{lineNumber(),
"{"
+ "method Void (class A a, class B b,class C c)#?m3(a:a,b:b,c:c)"
+ "A:{class method Void #m3#0a(read B b,class C c)void}"
+ "B:{class method Void #m3#0b(class A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}",
"{"
+ "method Void (class A a, class B b,class C c) b.#m3#0b(a:a,c:c)"
+ "A:{class method Void #m3#0a(read B b,class C c)void}"
+ "B:{class method Void #m3#0b(class A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}"

},{lineNumber(),
"{"
+ "method Void (class A a, class B b,class C c)#?m3(a:a,b:b,c:c)"
+ "A:{class method Void #m3#0a(read B b,class C c)void}"
+ "B:{class method Void #m3#0b(read A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}",
"{"
+ "method Void (class A a, class B b,class C c) c.#m3#0c(a:a,b:b)"
+ "A:{class method Void #m3#0a(read B b,class C c)void}"
+ "B:{class method Void #m3#0b(read A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}"

}});}

    
    
@Test()
public void testAllSteps() {
  ClassB cb1=runTypeSystem(original);
  ClassB cb2=(ClassB)Desugar.of(Parser.parse(null,annotated)).accept(new InjectionOnCore());
  TestHelper.assertEqualExp(cb1,cb2);
  }
}


public static class TesFail {
@Test(expected=MethodNotPresent.class)
public void test2() {runTypeSystem(
    "{C:{class method Void foo() (D.foo())} "
    +"D:{class method Void bar() (void)}}"
    );}
@Test(expected=FormattedError.class)
public void test3() {runTypeSystem(
   "{C:{E:{class method Void foo() (This1.foo())} class method Library foo() (D.foo())} D:{class method Void foo() (C.E.foo())}}"
    );}
@Test(expected=FormattedError.class)
public void test4() {runTypeSystem(
    "{C:{E:{class method Void foo() (This1.foo())} class method Library foo() (D.foo())} D:{class method Void foo() (C.E.foo())}}"
    );}
@Test(expected=MethodNotPresent.class)
public void test5() {runTypeSystem(
  "{K:{E:{class method Any  foo() (This1.foo())}} C:{class method Void foo() (D.foo())} D:{class method Library foo() (K.E.foo())}}"
   );}

@Test()
public void test6() {try{runTypeSystem(
  "{class method Any  foo() (exception void)}"
   );fail();}catch(FormattedError fe){assertEquals(
  ErrorKind.MethodLeaksExceptions,
  fe.kind);};}

@Test()
public void test7() {try{runTypeSystem(
  "{class method Any  foo()exception This (exception void)}"
   );fail();}catch(FormattedError fe){assertEquals(
  ErrorKind.MethodLeaksExceptions,
  fe.kind);};}

@Test()
public void test8() {try{runTypeSystem(
  "{class method Any  foo()exception This "
  + "{return (exception void {})}"
  + "}"
   );fail();}catch(FormattedError fe){assertEquals(
  ErrorKind.MethodLeaksExceptions,
  fe.kind);};}

}
   static ClassB runTypeSystem(String scb1) {
        TestHelper.configureForTest();
        ClassB cb1=(ClassB)Desugar.of(Parser.parse(null,scb1)).accept(new InjectionOnCore());
        Program p=Program.emptyLibraryProgram();
        return TypeSystem.instance().topTypeLib(Phase.Coherent, p.evilPush(cb1));
      }
}

