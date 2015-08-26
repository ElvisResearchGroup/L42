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

public class TestTypeExtraction {
  @RunWith(Parameterized.class)
  public static class Test1 {
	@Parameter(0) public int _lineNumber;
    @Parameter(1) public String e1;
    @Parameter(2) public String e2;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
          {lineNumber(),"{a( Outer0::A a)}",
         "{a( Outer0::A a)}##star ^##"
       },{lineNumber(),"{a( Outer0::A a, var Outer0::B b)}",
         "{"
        +" type method Outer0 a( Outer0::A^ a, Outer0::B^ b) "
        +" mut method Outer0::A #a() "
        +" read method Outer0::A a()"
        +" mut method Void b(Outer0::B that)"
        +" mut method Outer0::B #b()"
        +" read method Outer0::B b()"
        +" }##star ^##"    //mostly testing desugar now...
       },{lineNumber(),"{a( Outer0::A a, var Outer0::B b)'@private\n}",
           "{"
          +" type method'@private\n Outer0 a( Outer0::A^ a, Outer0::B^ b) "
          +" mut method'@private\n Outer0::A #a() "
          +" read method'@private\n Outer0::A a()"
          +" mut method'@private\n Void b(Outer0::B that)"
          +" mut method'@private\n Outer0::B #b()"
          +" read method'@private\n Outer0::B b()"
          +" }##star ^##"    //mostly testing desugar now...

       },{lineNumber(),"{interface method Void m() A:{interface <:Outer1}}",
          "{interface method Void m() A:{interface <:Outer1 }}##star ^##"

       //interface inside
       },{lineNumber(),"{interface method Void m() A:{interface <:Outer1}}##star ^##",
         "{interface method Void m() A:{interface  <:Outer1 method Void m()}##star ^## }##star ^##"
       //check normal from
       },{lineNumber(),"{interface method Outer0::A m() A:{interface <:Outer1}}##less ^##",
         "{interface method Outer0::A m() A:{interface  <:Outer1 method Outer1::A m()}##star ^## }##less ^##"
       //interface outside
       },{lineNumber(),"{interface <: Outer0::A A:{interface method Outer0 m() }##plus ^##}",
          "{interface  <: Outer0::A method Outer0::A m()  A:{interface method Outer0 m() }##plus ^##}##star ^##",

       },{lineNumber(),"{'foo\ninterface <: Outer0::A A:{interface method Outer0 m() }}",
         "{'foo\ninterface <: Outer0::A A:{interface method Outer0 m() }##star ^##}"

         //propagation of plus
         },{lineNumber(),"{  B:{ }##plus ^##}##star ^##",
           "{  B:{ }##plus ^##}##plus ^##"
           //propagation of less
           },{lineNumber(),"{  B:{ }##less ^##}##star ^##",
             "{  B:{ }##less ^##}##less ^##"
             //propagation of less better than plus
             },{lineNumber(),"{  B:{ }##less ^## C:{}##plus ^##}##star ^##",
               "{  B:{ }##less ^## C:{}##plus ^##}##less ^##"
               //propagation of plus by use
             },{lineNumber(),"{  B:{ }##plus ^## C:{ method B() Outer0()}##star ^##}##plus ^##",
               "{  B:{ }##plus ^## C:{ method B() Outer0()}##plus ^##}##plus ^##"
               //propagation of less by use
             },{lineNumber(),"{  B:{ }##less ^## C:{ method B() Outer0()}##star ^##}##less ^##",
               "{  B:{ }##less ^## C:{ method B() Outer0()}##less ^##}##less ^##"
               //propagation of less by undefinition
             },{lineNumber(),"{  B:{A:void }##star ^## C:{ method B::A() Outer0()}##star ^##}##less ^##",
               "{  B:{ A:void}##star ^## C:{ method B::A() Outer0()}##less ^##}##less ^##"
         }});}

    @Test
    public void testStep() {
      ClassB cb1=(ClassB)(Desugar.of(Parser.parse(null,e1)).accept(new InjectionOnCore()));
      ClassB cb2=(ClassB)(Desugar.of(Parser.parse(null,e2)).accept(new InjectionOnCore()));
      Program p=Program.empty();
//      List<String>inp=Arrays.asList(in).subList(2,in.length);
//      inp=new ArrayList<String>(inp);
//      Collections.reverse(inp);
//      for(String s: inp){
//        Expression e=Parser.parse(null,s);
//        ExpCore ec=e.accept(new InjectionOnCore());
//        assert ec instanceof ClassB;
//        p=p.addAtTop((ClassB)ec);
//      }
      //Assert.assertEquals(ExtractTypeStep.etDispatch(Stage.Star,new ArrayList<Path>(), p, cb1),cb2);
      cb1=TypeExtraction.etDispatch(p, cb1);
      //cb1.equals(cb2);
      TestHelper.assertEqualExp(cb1,cb2);
    }

    }

@RunWith(Parameterized.class)
public static class Test2 {
  @Parameter(0) public String e1;
  @Parameter(1) public String e2;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
         {"{}","{}##star^##"
       },{"{()}","{type method Outer0 ()}##star ^##"
       },{"{()}","{() }##star ^##"//,"{C:{}}"
       },{"{D:{method Void foo()}}","{D:{method Void foo()}##plus^##}##plus^##"//,"{C:{}}"
       },{"{a( Outer0::A a) A:{}}",
          "{"
         +" type method Outer0 a( Outer0::A^ a '@private\n) "
         +" mut method  '@private\nOuter0::A #a()"
         +" read method  '@private\nOuter0::A a()"
         //+" mut method Void a(Outer0::A that) ##field"
         +" A:{}##star ^##}##star^##"
       },{"{a(var  Outer0::A a)  A:{}}",
         "{"
        +" type method Outer0 a( Outer0::A^ a '@private\n)"
        +" mut method '@private\nVoid a(Outer0::A that)"
        +" mut method '@private\nOuter0::A #a() "
        +" read method '@private\nOuter0::A a() "
        +" A:{}##star ^##}##star^##"
       },{"{a( Outer0::A a, var Outer0::B b) A:{}  B:{}}",
         "{"
        +" type method Outer0 a( Outer0::A^ a'@private\n, Outer0::B^ b'@private\n)"
        +" mut method '@private\nOuter0::A #a()"
        +" read method '@private\nOuter0::A a() "
        +" mut method '@private\nVoid b(Outer0::B that)"
        +" mut method '@private\nOuter0::B #b()"
        +" read method '@private\nOuter0::B b() "
        +" A:{}##star ^## B:{}##star ^##}##star^##"
       },{"{interface method Void m() A:{interface <:Outer1}}",
          "{interface method Void m() A:{interface<:Outer1 method Void m()  }##star^## }##star^##"
       //interface inside
       },{"{interface method Void m() A:{interface <:Outer1}}",
          "{interface method Void m() A:{interface <:Outer1 method Void m()} ##star^## }##star^##"
       //check normal from
       },{"{interface method Outer0::A m() A:{interface <:Outer1}}",
         "{interface method Outer0::A m() A:{interface <:Outer1 method Outer1::A m()} ##star^## }##star^##"
       //interface outside
       },{"{interface <: Outer0::A A:{interface method Outer0 m() }}",
          "{interface <:Outer0::A method Outer0::A m() A:{interface method Outer0 m()} ##star^##}##star^##"

       //two interfaces implemented
       },{"{interface <: Outer0::A, Outer0::B A:{interface method Outer0 ma() }##less ^## B:{interface method Outer0 mb() }}",
           "{interface <: Outer0::A, Outer0::B method Outer0::A ma() method Outer0::B mb() A:{interface method Outer0 ma()}##less ^## B:{interface method Outer0 mb() }##star^##} ##less^##"
         //two interfaces implemented nested simple
         },{"{interface <: Outer0::A, Outer0::A::B A:{interface method Outer0 ma()  B:{interface method Outer0 mb() }}}",
             "{interface <: Outer0::A, Outer0::A::B method Outer0::A ma() method Outer0::A::B mb() A:{interface method Outer0 ma() B:{interface method Outer0 mb() }##star^##}##star^##} ##star^##"
           //two interfaces implemented nested transitive
           },{"{interface <: Outer0::A A:{interface<: Outer0::B method Outer0 ma()  B:{interface method Outer0 mb() }}}",
               "{interface <: Outer0::A, Outer0::A::B method Outer0::A::B mb() method Outer0::A ma() A:{interface <: Outer0::B method Outer0::B mb() method Outer0 ma() B:{interface method Outer0 mb() }##star^##} ##star^##}##star^##"
         //good self diamond
       },{"{interface <: Outer0::B, Outer0::B B:{interface method Outer0 mb()}}",
          "{interface <: Outer0::B, Outer0::B method Outer0::B mb() B:{interface method Outer0 mb()}##star^##}##star^## "
         //good standard diamond
       },{"{interface <: Outer0::A, Outer0::B A:{interface <:Outer1::C } B:{interface <:Outer1::C } C:{interface method Outer0 mc()}}",
          "{interface  <: Outer0::A, Outer0::B, Outer0::C, Outer0::C method Outer0::C mc() A:{interface <:Outer1::C method Outer1::C mc() }##star^##  B:{interface <:Outer1::C method Outer1::C mc() }##star^##  C:{interface method Outer0 mc()}##star^## }##star^##  "
       },{"{A:{ T:{method Void ()} Cell:{interface  method Void #inner(Outer1::T a, Outer1::Cell b)  } } }",
          "{A:{ "
        + " T:{method Void()}##plus ^##"
        + " Cell:{interface method Void #inner(Outer1::T a, Outer1::Cell b)"
        + "  }##plus ^##}##plus ^##"
        + "}##plus ^##"
       }});}
    @Test
    public void testAllSteps() {
      ClassB cb1=(ClassB)(Desugar.of(Parser.parse(null,e1)).accept(new InjectionOnCore()));
      ClassB cb2=(ClassB)(Desugar.of(Parser.parse(null,e2)).accept(new InjectionOnCore()));
      Program p=Program.empty();
      cb1=TypeExtraction.etFull(p, cb1);
      //cb1.equals(cb2);
      TestHelper.assertEqualExp(cb1,cb2);
    }
    }

@RunWith(Parameterized.class)
public static class Test3 {
  @Parameter(0) public Mdf mdf;
  @Parameter(1) public Path path;
  @Parameter(2) public String e;
  @Parameter(3) public boolean ok;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {           //a
         {Mdf.Immutable,Path.Any(),"{method Any foo()}",true
      },{Mdf.Immutable,Path.Any(),"{method Void foo()}",false
      },{Mdf.Type,Path.Void(),"{method type Void foo()}",true
      },{Mdf.Readable,Path.Void(),"{method read Void foo()}",true
        //b
      },{Mdf.Immutable,Path.Void(),"{mut method  Void foo( Void that)}",true
      },{Mdf.Immutable,Path.Void(),"{mut method  Void foo( Void that0)}",false
      },{Mdf.Immutable,Path.Void(),"{mut method  Void foo( Void that0, Any any)}",false
      },{Mdf.Immutable,Path.Void(),"{mut method  Void foo( read Void that)}",false
      },{Mdf.Immutable,Path.Void(),"{read method  Void foo( Void that)}",false
      },{Mdf.Immutable,Path.Void(),"{lent method  Void foo( Void that)}",true
      //c
      },{Mdf.Mutable,Path.Void(),"{lent method  mut Void foo()}",false
      },{Mdf.Mutable,Path.Void(),"{mut method  mut Void foo()}",true
      },{Mdf.Mutable,Path.Void(),"{lent method  lent Void foo()}",true
      },{Mdf.Lent,Path.Void(),"{lent method  mut Void foo()}",false
      //d
      },{Mdf.Mutable,Path.Void(),"{lent method  read Void foo()}",true
      },{Mdf.Mutable,Path.Void(),"{read method  read Void foo()}",true
      },{Mdf.Mutable,Path.Void(),"{read method  lent Void foo()}",false
      },{Mdf.Lent,Path.Void(),"{lent method  read Void foo()}",true
      },{Mdf.Lent,Path.Void(),"{read method  read Void foo()}",true
      },{Mdf.Lent,Path.Void(),"{read method  lent Void foo()}",false
       //e//pass
      },{Mdf.Mutable,Path.Void(),"{mut method  Void foo(mut Void that)}",true
      },{Mdf.Lent,Path.Void(),"{mut method  Void foo(mut Void that)}",true
      },{Mdf.Lent,Path.Void(),"{mut method  Void foo(lent Void that)}",true
      },{Mdf.Mutable,Path.Void(),"{mut method  Void foo(lent Void that)}",false
      },{Mdf.Mutable,Path.Void(),"{mut method  Void foo(read Void that)}",false
       //f
      },{Mdf.Mutable,Path.Void(),"{lent method  Void foo(capsule Void that)}",true
      },{Mdf.Lent,Path.Void(),"{lent method  Void foo(capsule Void that)}",true
      },{Mdf.Lent,Path.Void(),"{lent method  Void foo( Void that)}",false
        //g
      },{Mdf.Capsule,Path.Void(),"{read method  read Void foo()}",true
      },{Mdf.Capsule,Path.Void(),"{lent method  read Void foo()}",true
      },{Mdf.Capsule,Path.Void(),"{lent method  lent Void foo()}",true
      },{Mdf.Capsule,Path.Void(),"{capsule method  capsule Void foo()}",true
      },{Mdf.Capsule,Path.Void(),"{mut method  capsule Void foo()}",false
      },{Mdf.Capsule,Path.Void(),"{mut method  mut Void foo()}",true
        //h
      },{Mdf.Capsule,Path.Void(),"{mut method  Void foo(mut Void that)}",true
      },{Mdf.Capsule,Path.Void(),"{mut method  Void foo(capsule Void that)}",true
      },{Mdf.Capsule,Path.Void(),"{mut method  Void foo(lent Void that)}",false
       }});}

    @Test
    public void testCoherence() {
      ClassB cb1=(ClassB)(Parser.parse(null,e).accept(new InjectionOnCore()));
      Program p=Program.empty();
      MethodWithType mwt=(MethodWithType)cb1.getMs().get(0);
      boolean res=Functions.coherent(p, mdf, path, mwt);
      Assert.assertEquals(res,ok);
      }
    }
}
