package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.fail;

import facade.Configuration;
import facade.L42;
import helpers.TestHelper;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import ast.Ast;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

  @RunWith(Parameterized.class)
  public class TestSumMethod {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String _cb;
    @Parameter(2) public String _path;
    @Parameter(3) public String _ms1;
    @Parameter(4) public String _ms2;
    @Parameter(5) public String _expected;
    @Parameter(6) public boolean isError;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {    lineNumber(),"{method Void m1() method Void m2(Void that)}",
                                   "Outer0","m1()", "m2(that)",
                                   "{method Void m1() method Void m2(Void that)"
                                   + " method Void m1m2() this.m2(that:this.m1())}",false
     },{    lineNumber(),"{method Void m1() method Void (Void that)}",
                                     "Outer0","m1()", "#apply(that)",
                                     "{method Void m1() this.#apply(that:this.m1()) "
                                     + "method Void #apply(Void that) }",false
     },{    lineNumber(),"{method Void () method Void m2(Void that)}",
       "Outer0","#apply()", "m2(that)",
       "{method Void #apply() method Void m2(Void that) "
       + "method Void m2() this.m2(that:this.#apply())}",false
  },{    lineNumber(),"{method Void +() method Void m2(Void that)}",
    "Outer0","#plus()", "m2(that)",
    "{method Void +() method Void m2(Void that) "
    + "method Void m2() this.m2(that:this.#plus())}",false
  },{    lineNumber(),"{A:{} B:{} C:{} method Void m1(A a, B b) method Void m2(Void that, C c)}",
    "Outer0","m1(a,b)", "m2(that,c)",
    "{A:{} B:{} C:{}"
    + "method Void m1(Outer0::A a, Outer0::B b) "
    + "method Void m2(Void that, Outer0::C c) "
    + "method Void m1m2(Outer0::A a, Outer0::B b, Outer0::C c)"
    + "   this.m2(that:this.m1(a:a, b:b), c:c)}",false
  },{    lineNumber(),"{A:{} B:{} C:{} type method Void m1(A a) method Void m2(Void that, B b,C c)}",
    "Outer0","m1(a)", "m2(that,b,c)",
    "{A:{} B:{} C:{}"
    + "type method Void m1(Outer0::A a) "
    + "method Void m2(Void that, Outer0::B b, Outer0::C c) "
    + "method Void m1m2(Outer0::A a, Outer0::B b, Outer0::C c)"
    + "   this.m2(that:Outer0.m1(a:a), b:b, c:c)}",false

   }});}
  @Test  public void test() {
    TestHelper.configureForTest();
    ClassB cb=getClassB(_cb);
    Configuration.typeSystem.computeStage(Program.empty(), cb);
    Path path=Path.parse(_path);
    MethodSelector ms1=MethodSelector.parse(_ms1);
    MethodSelector ms2=MethodSelector.parse(_ms2);
    ClassB expected=getClassB(_expected);
    if(!isError){
      ClassB res=SumMethods.sumMethods(cb,path.getCBar(),ms1,ms2);
      res=Functions.clearCache(res,Ast.Stage.None);
      TestHelper.assertEqualExp(expected,res);
      }
    else{
      try{ClassB res=SumMethods.sumMethods(cb,path.getCBar(),ms1,ms2);fail("error expected");}
      catch(Resources.Error err){
        ClassB res=(ClassB)err.unbox;
        TestHelper.assertEqualExp(expected,res);
      }
    }
  }
  }
