package testAux;

import helpers.TestHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import typeSystem.TypeExtraction;
import typeSystem.TypeSystemOK;
import ast.Ast.Stage;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class TestTypeSystemOk {
  @Test(singleThreaded=true, timeOut = 500)
  public class TestOk {
      @DataProvider(name = "needed,classB1,classB2")
      public String[][] createData1() {
       return new String[][] {
 {"Outer0::C","{C:{method Void()}}","{C:{method Void()}##plus^##}##plus^##"
},{"Outer0::C","{C:{method Void()} D:{}}","{C:{method Void()}##plus^## D:{}##star ^##}##plus^##"
},{"Outer0::C",
  "{C:{type method Void foo() (Outer0.foo())} }",
  "{C:{type method Void foo() (Outer0.foo())}##star^## }##star^##"
},{"Outer0::C",
  "{C:{type method Void foo() (D.foo())} D:{method Void() type method Void foo() (void)}}",
  "{C:{type method Void foo() (D.foo())}##plus^## D:{method Void()type method Void foo() (void)}##plus ^##}##plus^##"
},{"Outer0::C",
  "{C:{E:{type method Void foo() (Outer1.foo())} type method Void foo() (D.foo())} D:{type method Void foo() (C::E.foo())}}",
  "{C:{E:{type method Void foo() (Outer1.foo())}##star^## type method Void foo() (D.foo())}##star^## D:{type method Void foo() (C::E.foo())}##star^##}##star^##"  
},{"Outer0::C",
  "{C:{E:{type method Void foo() (Outer1.foo())} type method Void foo() (D.foo())} D:{method Void() type method Void foo() (C::E.foo())}}",
  "{C:{E:{type method Void foo() (Outer1.foo())}##plus^## type method Void foo() (D.foo())}##plus^## D:{method Void()  type method Void foo() (C::E.foo())}##plus^##}##plus^##"
  
},{"Outer0::C",
  "{K:{E:{type method Void foo() (Outer2::C.foo())}} C:{type method Void foo() (D.foo())} D:{type method Void foo() (K::E.foo())}}",
  "{K:{E:{type method Void foo() (Outer2::C.foo())}##star^##}##star ^## C:{type method Void foo() (D.foo())}##star^## D:{type method Void foo() (K::E.foo())}##star^##}##star^##"  
},{"Outer0::C",
  "{K:{method Void() E:{type method C foo() (C.foo())}} C:{type method C foo() (D.foo())} D:{type method C foo() (K::E.foo())}}",
  "{K:{method Void() E:{type method C foo() (C.foo())}##star^##}##plus ^## C:{type method C foo() (D.foo())}##star^## D:{type method C foo() (K::E.foo())}##star^##}##plus^##"
  //norm//NO, Norm is executed only in the extracted method
//},{"Outer0::C",
//  "{K:{E:{type method C::foo() foo() (C.foo())}} C:{type method C foo() (D.foo())} D:{type method C foo() (K::E.foo())}}",
//  "{K:{E:{type method C foo() (C.foo())}##plus^##}##plus ^## C:{type method C foo() (D.foo())}##plus^## D:{type method C foo() (K::E.foo())}##plus^##}##plus^##"  
//},{"Outer0::C",
//  "{K:{E:{type method C::foo()::foo() foo() (C.foo())}} C:{type method C foo() (D.foo())} D:{type method C foo() (K::E.foo())}}",
//  "{K:{E:{type method C foo() (C.foo())}##plus^##}##plus^## C:{type method C foo() (D.foo())}##plus^## D:{type method C foo() (K::E.foo())}##plus^##}##plus^##"  
},{"Outer0::C",
  "{C:{ method Void foo() (Outer0 x= this void)} }",
  "{C:{ method Void foo() (Outer0 x= this void)}##star^## }##star^##"
},{"Outer0::C",
  "{C:{ method Void foo() (C x= this void)} }",
  "{C:{ method Void foo() (C x= this void)}##star^## }##star^##"


}};}

@Test(dataProvider="needed,classB1,classB2")//needed unused
public void testAllSteps(String sp,String scb1,String scb2) {
  ClassB cb1=runTypeSystem(scb1);
  ClassB cb2=(ClassB)Desugar.of(Parser.parse(null,scb2)).accept(new InjectionOnCore());
  TestHelper.assertEqualExp(cb1,cb2);
  }
}
@Test(singleThreaded=true, timeOut = 500)
    public class TestFail {
        @DataProvider(name = "needed,classB1")
        public String[][] createData1() {
         return new String[][] {
    {"Outer0::C",
    "{C:{type method Void foo() (D.foo())} D:{type method Void bar() (void)}}"
  },{"Outer0::C",
   "{C:{E:{type method Void foo() (Outer1.foo())} type method Library foo() (D.foo())} D:{type method Void foo() (C::E.foo())}}"  
  },{"Outer0::C",
    "{C:{E:{type method Void foo() (Outer1.foo())} type method Library foo() (D.foo())} D:{type method Void foo() (C::E.foo())}}"  
 },{"Outer0::C",
  "{K:{E:{type method Any  foo() (Outer1.foo())}} C:{type method Void foo() (D.foo())} D:{type method Library foo() (K::E.foo())}}"  
           
       }};}
   
      @Test(dataProvider="needed,classB1", expectedExceptions=ErrorMessage.TypeError.class)
      public void testAllSteps(String sp,String scb1) {//needed unused
        runTypeSystem(scb1);
        assert false;
      }
        
      }

  private static ClassB runTypeSystem(String scb1) {
        ClassB cb1=(ClassB)Desugar.of(Parser.parse(null,scb1)).accept(new InjectionOnCore());
        Program p=Program.empty();
        cb1=TypeExtraction.etFull(p,cb1);
        p=p.addAtTop(cb1);
        //assert p.checkComplete():cb1;//Not in this test?
        TypeSystemOK.checkAll(p);
        return cb1;
      } 
}

