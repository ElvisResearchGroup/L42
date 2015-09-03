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
import typeSystem.TypeExtraction;
import ast.Ast.Mdf;
import ast.Ast.Stage;
import ast.ExpCore;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Expression;
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
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
          {lineNumber(),"{a( Outer0::A a) A:{}}","Outer0",
         "Util.CachedStage(stage=Star, dependencies=[], inherited=[], verified=false,"
      + " coherent=true, givenName=, privateNormalized=false, families=[])"
        },{lineNumber(),"{a( Outer0::A a) A:{}}","A",
          "Util.CachedStage(stage=Star, dependencies=[], inherited=[], verified=false,"
       + " coherent=true, givenName=, privateNormalized=false, families=[])"

        },{lineNumber(),"{method Void abstractMeth() }","Outer0",
          "Util.CachedStage(stage=Plus, dependencies=[], inherited=[], verified=false,"
       + " coherent=false, givenName=, privateNormalized=false, families=[])"
     
       },{lineNumber(),"{a( Outer0::A a, var Outer0::B b) A:{}  B:{}}","Outer0",
         "Util.CachedStage(stage=Star, dependencies=[], inherited=[], verified=false,"
             + " coherent=true, givenName=, privateNormalized=false, families=[])"
           
       },{lineNumber(),"{interface method Void m() A:{interface <:Outer1}}","Outer0::A",
          "Util.CachedStage(stage=Star, dependencies=[], inherited="
          + "[Outer1::method Void m()], verified=false,"
          + " coherent=true, givenName=, privateNormalized=false, families=[])"
     /*  //interface inside
       },{lineNumber(),"{interface method Void m() A:{interface <:Outer1}}","Outer0",
          "{interface method Void m() A:{interface <:Outer1 method Void m()} ##star^## }##star^##"
       //check normal from
       },{lineNumber(),"{interface method Outer0::A m() A:{interface <:Outer1}}","Outer0",
         "{interface method Outer0::A m() A:{interface <:Outer1 method Outer1::A m()} ##star^## }##star^##"
       //interface outside
       },{lineNumber(),"{interface <: Outer0::A A:{interface method Outer0 m() }}","Outer0",
          "{interface <:Outer0::A method Outer0::A m() A:{interface method Outer0 m()} ##star^##}##star^##"

       //two interfaces implemented
       },{lineNumber(),"{interface <: Outer0::A, Outer0::B A:{interface method Outer0 ma() }##less ^## B:{interface method Outer0 mb() }}","Outer0",
           "{interface <: Outer0::A, Outer0::B method Outer0::A ma() method Outer0::B mb() A:{interface method Outer0 ma()}##less ^## B:{interface method Outer0 mb() }##star^##} ##less^##"
         //two interfaces implemented nested simple
         },{lineNumber(),"{interface <: Outer0::A, Outer0::A::B A:{interface method Outer0 ma()  B:{interface method Outer0 mb() }}}","Outer0",
             "{interface <: Outer0::A, Outer0::A::B method Outer0::A ma() method Outer0::A::B mb() A:{interface method Outer0 ma() B:{interface method Outer0 mb() }##star^##}##star^##} ##star^##"
           //two interfaces implemented nested transitive
           },{lineNumber(),"{interface <: Outer0::A A:{interface<: Outer0::B method Outer0 ma()  B:{interface method Outer0 mb() }}}","Outer0",
               "{interface <: Outer0::A, Outer0::A::B method Outer0::A::B mb() method Outer0::A ma() A:{interface <: Outer0::B method Outer0::B mb() method Outer0 ma() B:{interface method Outer0 mb() }##star^##} ##star^##}##star^##"
         //good self diamond
       },{lineNumber(),"{interface <: Outer0::B, Outer0::B B:{interface method Outer0 mb()}}","Outer0",
          "{interface <: Outer0::B, Outer0::B method Outer0::B mb() B:{interface method Outer0 mb()}##star^##}##star^## ""Outer0",
         //good standard diamond
       },{lineNumber(),"{interface <: Outer0::A, Outer0::B A:{interface <:Outer1::C } B:{interface <:Outer1::C } C:{interface method Outer0 mc()}}","Outer0",
          "{interface  <: Outer0::A, Outer0::B, Outer0::C, Outer0::C method Outer0::C mc() A:{interface <:Outer1::C method Outer1::C mc() }##star^##  B:{interface <:Outer1::C method Outer1::C mc() }##star^##  C:{interface method Outer0 mc()}##star^## }##star^##  "
       },{lineNumber(),"{A:{ T:{method Void ()} Cell:{interface  method Void #inner(Outer1::T a, Outer1::Cell b)  } } }","Outer0",
          "{A:{ "
        + " T:{method Void()}##plus ^##"
        + " Cell:{interface method Void #inner(Outer1::T a, Outer1::Cell b)"
        + "  }##plus ^##}##plus ^##"
        + "}##plus ^##"*/
       }});}
    @Test
    public void testAllSteps() {
      ClassB cb1=(ClassB)(Desugar.of(Parser.parse(null,e1)).accept(new InjectionOnCore()));
      Program p=Program.empty();
      Path path=Path.parse(_path);
      FillCache.computeInheritedDeep(p, cb1);
      FillCache.computeStage(p, cb1);
      Assert.assertEquals(expected,Program.extractCBar(path.getCBar(), cb1).getStage().toString());
    }
    }
}
