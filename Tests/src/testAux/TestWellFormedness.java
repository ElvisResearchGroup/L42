package testAux;

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

  @RunWith(Parameterized.class)
  public static class TestFail {
    @Parameter(0) public String s;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
    {"{C:{} C:{}}"
  },{"{f(C a C a)}"
  },{"{ method() ( D d=D.k() catch exception x (on Void d.m()) void)}"
  },{"{ method() ( this+this*this )}"
  },{"{ method() ( this++this**this )}"
       }});}

      @Test(expected=ErrorMessage.NotWellFormed.class)
      public void testAll() {
        //Expression.ClassB sugared =(Expression.ClassB)Desugar.of(Parser.parse(null,scb1));
        Expression.ClassB sugared =(Expression.ClassB)Parser.parse(null,s);
        auxiliaryGrammar.WellFormedness.checkAll(sugared);
        assert false;
      }

      }

@RunWith(Parameterized.class)
public static class TestPass {
  @Parameter(0) public String s;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
  {"{()}"
},{"{ method()  this+this+this }"
},{"{ method()  this<=this<=this }"
},{"{ method()  this+this<=this <=this & this }"
},{"{ method()  this<=this<= this+this<=this <=this & this *this *this }"
 }});}

@Test
public void testAll() {
  Expression.ClassB sugared =(ast.Expression.ClassB) Parser.parse(null,s);
  auxiliaryGrammar.WellFormedness.checkAll(sugared);
}

}

}