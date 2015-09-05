package testAux;

import helpers.TestHelper;

import static helpers.TestHelper.lineNumber;

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

import facade.Configuration;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import typeSystem.TypeSystemOK;
import ast.Ast.Stage;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class TestTypeSystemOk {
  @RunWith(Parameterized.class)
  public static class TestOk {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String s1;
    @Parameter(2) public String s2;
    @Parameter(3) public String s3;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
 {lineNumber(),"Outer0::C","{C:{method Void()}}","{C:{method Void()}##plus^##}##plus^##"
},{lineNumber(),"Outer0::C","{C:{method Void()} D:{}}","{C:{method Void()}##plus^## D:{}##star ^##}##plus^##"
},{lineNumber(),"Outer0::C",
  "{C:{type method Void foo() (Outer0.foo())} }",
  "{C:{type method Void foo() (Outer0.foo())}##star^## }##star^##"
},{lineNumber(),"Outer0::C",
  "{C:{type method Void foo() (D.foo())} D:{method Void() type method Void foo() (void)}}",
  "{C:{type method Void foo() (D.foo())}##plus^## D:{method Void()type method Void foo() (void)}##plus ^##}##plus^##"
},{lineNumber(),"Outer0::C",
  "{C:{E:{type method Void foo() (Outer1.foo())} type method Void foo() (D.foo())} D:{type method Void foo() (C::E.foo())}}",
  "{C:{E:{type method Void foo() (Outer1.foo())}##star^## type method Void foo() (D.foo())}##star^## D:{type method Void foo() (C::E.foo())}##star^##}##star^##"
},{lineNumber(),"Outer0::C",
  "{C:{E:{type method Void foo() (Outer1.foo())} type method Void foo() (D.foo())} D:{method Void() type method Void foo() (C::E.foo())}}",
  "{C:{E:{type method Void foo() (Outer1.foo())}##plus^## type method Void foo() (D.foo())}##plus^## D:{method Void()  type method Void foo() (C::E.foo())}##plus^##}##plus^##"

},{lineNumber(),"Outer0::C",
  "{K:{E:{type method Void foo() (Outer2::C.foo())}} C:{type method Void foo() (D.foo())} D:{type method Void foo() (K::E.foo())}}",
  "{K:{E:{type method Void foo() (Outer2::C.foo())}##star^##}##star ^## C:{type method Void foo() (D.foo())}##star^## D:{type method Void foo() (K::E.foo())}##star^##}##star^##"
},{lineNumber(),"Outer0::C",
  "{K:{method Void() E:{type method C foo() (C.foo())}} C:{type method C foo() (D.foo())} D:{type method C foo() (K::E.foo())}}",
  "{K:{method Void() E:{type method C foo() (C.foo())}##star^##}##plus ^## C:{type method C foo() (D.foo())}##star^## D:{type method C foo() (K::E.foo())}##star^##}##plus^##"
  //norm//NO, Norm is executed only in the extracted method
//},{"Outer0::C",
//  "{K:{E:{type method C::foo() foo() (C.foo())}} C:{type method C foo() (D.foo())} D:{type method C foo() (K::E.foo())}}",
//  "{K:{E:{type method C foo() (C.foo())}##plus^##}##plus ^## C:{type method C foo() (D.foo())}##plus^## D:{type method C foo() (K::E.foo())}##plus^##}##plus^##"
//},{"Outer0::C",
//  "{K:{E:{type method C::foo()::foo() foo() (C.foo())}} C:{type method C foo() (D.foo())} D:{type method C foo() (K::E.foo())}}",
//  "{K:{E:{type method C foo() (C.foo())}##plus^##}##plus^## C:{type method C foo() (D.foo())}##plus^## D:{type method C foo() (K::E.foo())}##plus^##}##plus^##"
},{lineNumber(),"Outer0::C",
  "{C:{ method Void foo() (Outer0 x= this void)} }",
  "{C:{ method Void foo() (Outer0 x= this void)}##star^## }##star^##"
},{lineNumber(),"Outer0::C",
  "{C:{ method Void foo() (C x= this void)} }",
  "{C:{ method Void foo() (C x= this void)}##star^## }##star^##"


}});}

@Test()
public void testAllSteps() {//s1 unused :(
  ClassB cb1=runTypeSystem(s2);
  ClassB cb2=(ClassB)Desugar.of(Parser.parse(null,s3)).accept(new InjectionOnCore());
  TestHelper.assertEqualExp(cb1,cb2);
  }
}


@RunWith(Parameterized.class)
public static class TesFail {
  @Parameter(0) public String s1;
  @Parameter(1) public String s2;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {"Outer0::C",
    "{C:{type method Void foo() (D.foo())} D:{type method Void bar() (void)}}"
  },{"Outer0::C",
   "{C:{E:{type method Void foo() (Outer1.foo())} type method Library foo() (D.foo())} D:{type method Void foo() (C::E.foo())}}"
  },{"Outer0::C",
    "{C:{E:{type method Void foo() (Outer1.foo())} type method Library foo() (D.foo())} D:{type method Void foo() (C::E.foo())}}"
 },{"Outer0::C",
  "{K:{E:{type method Any  foo() (Outer1.foo())}} C:{type method Void foo() (D.foo())} D:{type method Library foo() (K::E.foo())}}"

       }});}

      @Test(expected=ErrorMessage.TypeError.class)
      public void testAllSteps() {//s1 unused :(
        runTypeSystem(s2);
        assert false;
      }

      }

   static ClassB runTypeSystem(String scb1) {
        TestHelper.configureForTest();
        ClassB cb1=(ClassB)Desugar.of(Parser.parse(null,scb1)).accept(new InjectionOnCore());
        Program p=Program.empty();
        Configuration.typeSystem.computeStage(p,cb1);
        //ClassB cb1t=TypeExtraction.etFull(p,cb1);
        p=p.addAtTop(cb1);
        //assert p.checkComplete():cb1;//Not in this test?
        TypeSystemOK.checkAll(p);
        return cb1;
      }
}

