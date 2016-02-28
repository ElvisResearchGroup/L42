package testAux;

import helpers.TestHelper;

import static helpers.TestHelper.lineNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import typeSystem.FillCache;
import ast.Ast.Mdf;
import ast.Ast.Stage;
import ast.ExpCore;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Expression;
import ast.Util.CachedStage;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class TestTypeCaching {
  @RunWith(Parameterized.class)
  public static class Test1 {
	@Parameter(0) public int _lineNumber;
    @Parameter(1) public String e1;
    @Parameter(2) public String _path;
    @Parameter(3) public String expected;
    @Parameter(4) public boolean expectedCoh;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
          {lineNumber(),"{a( This0.A a) A:{}}","This0",
         "[]",true
        },{lineNumber(),"{a( This0.A a) A:{}}","A",
         "[]" ,true

        },{lineNumber(),"{method Void abstractMeth() }","This0",
         "[]"  ,false

       },{lineNumber(),"{a( This0.A a, var This0.B b) A:{}  B:{}}","This0",
        "[]",true
             //interface inside
       },{lineNumber(),"{interface method Void m() A:{interface <:This1}}","This0.A",
         "[This1::method Void m()]"
          ,true
       //check normal from
       },{lineNumber(),"{interface method This0.A m() A:{interface <:This1}}","This0.A",
      "[This1::method This1.A m()]"
             ,true
           //interface outside
       },{lineNumber(),"{interface <: This0.A A:{interface method This0 m() }}","This0",
      "[This0.A::method This0.A m()]"
         ,true
       //two interfaces implemented
       },{lineNumber(),"{interface <: This0.A, This0.B A:{interface method This0 ma() }##less ^## B:{interface method This0 mb() }}","This0",
          "[This0.A::method This0.A ma(), This0.B::method This0.B mb()]"
           ,true
           //two interfaces implemented nested simple
         },{lineNumber(),"{interface <: This0.A, This0.A.B A:{interface method This0 ma()  B:{interface method This0 mb() }}}","This0",
             "[This0.A::method This0.A ma(), This0.A.B::method This0.A.B mb()]"
             ,true
             //two interfaces implemented nested transitive
           },{lineNumber(),"{interface <: This0.A A:{interface<: This0.B method This0 ma()  B:{interface method This0 mb() }}}","This0",
               "[This0.A::method This0.A ma(), This0.A.B::method This0.A.B mb()]"
               ,true
               //good self diamond//TODO: may become not well formed.
       },{lineNumber(),"{interface <: This0.B, This0.B B:{interface method This0 mb()}}","This0",
         "[This0.B::method This0.B mb()]"
          ,true
          //good standard diamond
       },{lineNumber(),"{interface <: This0.A, This0.B A:{interface <:This1.C } B:{interface <:This1.C } C:{interface method This0 mc()}}","This0",
          "[This0.C::method This0.C mc()]"
          ,true
          //check transitive plusness
       },{lineNumber(),"{A:{ T:{method Void ()} Cell:{interface  method Void #inner(This1.T a, This1.Cell b)  } } }","This0.A.Cell",
       "[]"
          ,true

       }});}
    @Test
    public void testAllSteps() {
      ClassB cb1=(ClassB)(Desugar.of(Parser.parse(null,e1)).accept(new InjectionOnCore()));
      Program p=Program.empty();
      Path path=Path.parse(_path);
      FillCache.computeInheritedDeep(p, cb1,new ArrayList<>());
      FillCache.computeStage(p, cb1);
      CachedStage stg = Program.extractCBar(path.getCBar(), cb1).getStage();
      Assert.assertEquals(expectedCoh, stg.getCoherent().isEmpty());
      Assert.assertEquals(expected,stg.getInherited().toString());
    }
    }
}
