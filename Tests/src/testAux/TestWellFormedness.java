package testAux;

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
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Ph;
import ast.Ast.Stage;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.Expression;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

//TODO: add way more tests

public class TestWellFormedness {
@Test(singleThreaded=true, timeOut = 500)
    public class TestFail {
        @DataProvider(name = "classB1")
        public String[][] createData1() {
         return new String[][] {
    {"{C:{} C:{}}"
  },{"{f(C a C a)}"
  },{"{ method() ( D d=D.k() catch exception x (on Void d.m()) void)}"
  },{"{ method() ( this+this*this )}"
  },{"{ method() ( this++this**this )}"
       }};}
   
      @Test(dataProvider="classB1", expectedExceptions=ErrorMessage.NotWellFormed.class)
      public void testAll(String scb1) {
        //Expression.ClassB sugared =(Expression.ClassB)Desugar.of(Parser.parse(null,scb1));
        Expression.ClassB sugared =(Expression.ClassB)Parser.parse(null,scb1);
        auxiliaryGrammar.WellFormedness.checkAll(sugared);
        assert false;
      }
        
      }


  public class TestPass {
    @DataProvider(name = "classB2")
    public String[][] createData1() {
    return new String[][] {
  {"{()}"
},{"{ method()  this+this+this }"
},{"{ method()  this<=this<=this }"
},{"{ method()  this+this<=this <=this & this }"
},{"{ method()  this<=this<= this+this<=this <=this & this *this *this }"
 }};}

@Test(dataProvider="classB2")
public void testAll(String scb1) {
  Expression.ClassB sugared =(ast.Expression.ClassB) Parser.parse(null,scb1);
  auxiliaryGrammar.WellFormedness.checkAll(sugared);
}
  
}

}
