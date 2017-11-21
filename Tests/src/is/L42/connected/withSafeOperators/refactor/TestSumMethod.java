package is.L42.connected.withSafeOperators.refactor;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import facade.L42;
import helpers.TestHelper;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import is.L42.connected.withSafeOperators.refactor.SumMethods;

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
import programReduction.Program;

  @RunWith(Parameterized.class)
  public class TestSumMethod {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String _cb;
    @Parameter(2) public String _path;
    @Parameter(3) public String _ms1;
    @Parameter(4) public String _ms2;
    @Parameter(5) public String _ms3;
    @Parameter(6) public String name;
    @Parameter(7) public String _expected;
    @Parameter(8) public boolean isError;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {    lineNumber(),"{method Void m1() method Void m2(Void that)}",
                                   "This0","m1()", "m2(that)","m1m2()","that",
                                   "{"
                                   + " method Void m1m2() this.m2(that:this.m1())"
                                   + "method Void m1() method Void m2(Void that)"
                                   + "}",false
     },{    lineNumber(),"{method Void m1() method Void (Void that)}",
                                     "This0","m1()", "#apply(that)","m1()","that",
                                     "{method Void m1() this.#apply(that:this.m1()) "
                                     + "method Void #apply(Void that) }",false
     },{    lineNumber(),"{method Void () method Void m2(Void that)}",
       "This0","#apply()", "m2(that)","m2()","that",
       "{"
       + "method Void m2() this.m2(that:this.#apply())"
       + "method Void #apply() method Void m2(Void that) "
       + "}",false
  },{    lineNumber(),"{method Void +() method Void m2(Void that)}",
    "This0","#plus()", "m2(that)","m2()","that",
    "{"
    + "method Void m2() this.m2(that:this.#plus())"
    + "method Void +() method Void m2(Void that) "
    + "}",false
  },{    lineNumber(),"{A:{} B:{} C:{} method Void m1(A a, B b) method Void m2(Void that, C c)}",
    "This0","m1(a,b)", "m2(that,c)","m1m2(a,b,c)","that",
    "{A:{} B:{} C:{}"
    + "method Void m1m2(This0.A a, This0.B b, This0.C c)"
    + "   this.m2(that:this.m1(a:a, b:b), c:c)"
    + "method Void m1(This0.A a, This0.B b) "
    + "method Void m2(Void that, This0.C c) "
    + "}",false
  },{    lineNumber(),"{A:{} B:{} C:{} class method Void m1(A a) method Void m2(Void that, B b,C c)}",
    "This0","m1(a)", "m2(that,b,c)","m1m2(a,b,c)","that",
    "{A:{} B:{} C:{}"
    + "method Void m1m2(This0.A a, This0.B b, This0.C c)"
    + "   this.m2(that:This0.m1(a:a), b:b, c:c)"
    + "class method Void m1(This0.A a) "
    + "method Void m2(Void that, This0.B b, This0.C c) "
    + "}",false
  },{    lineNumber(),"{method Void noArg() method Void m(Void a,Void b)}",
    "This0","noArg()", "m(a,b)","m(c)","a",
    "{"
    + "method Void m(Void c) this.m(a:this.noArg(),b:c)"
    + "method Void noArg() method Void m(Void a,Void b) "
    + "}",false
  },{    lineNumber(),"{method Void noArg() method Void m(Void a,Void b)}",
    "This0","noArg()", "m(a,b)","m(c)","b",
    "{"
    + "method Void m(Void c) this.m(a:c,b:this.noArg())"
    + "method Void noArg() method Void m(Void a,Void b) "
    + "}",false

  },{    lineNumber(),"{A:{} B:{} C:{} class method Void m1(A a) method Void m2(B b,Void that, C c)}",
    "This0","m1(a)", "m2(b,that,c)","m1m2(a,b,c)","that",
    "{A:{} B:{} C:{}"
    + "method Void m1m2(This0.A a, This0.B b, This0.C c)"
    + "   this.m2(b:b,that:This0.m1(a:a), c:c)"
    + "class method Void m1(This0.A a) "
    + "method Void m2(This0.B b,Void that, This0.C c) "
    + "}",false

  },{    lineNumber(),"{A:{} B:{} C:{} class method Void m1(A a) method Void m2(B b, C c,Void that)}",
    "This0","m1(a)", "m2(b,c,that)","m1m2(a,b,c)","that",
    "{A:{} B:{} C:{}"
    + "method Void m1m2(This0.A a, This0.B b, This0.C c)"
    + "   this.m2(b:b,c:c,that:This0.m1(a:a))"
    + "class method Void m1(This0.A a) "
    + "method Void m2(This0.B b, This0.C c,Void that) "
    + "}",false

   }});}
  @Test  public void test() throws MethodClash, PathUnfit, SelectorUnfit {
    TestHelper.configureForTest();
    ClassB cb=getClassB(true,null,_cb);
    Path path=Path.parse(_path);
    MethodSelector ms1=MethodSelector.parse(_ms1);
    MethodSelector ms2=MethodSelector.parse(_ms2);
    MethodSelector ms3=MethodSelector.parse(_ms3);
    ClassB expected=getClassB(true,null,_expected);
    if(!isError){
      ClassB res=SumMethods.sumMethodsP(Program.emptyLibraryProgram(),cb,path.getCBar(),ms1,ms2,ms3,name);
      TestHelper.assertEqualExp(expected,res);
      }
    else{
      try{SumMethods.sumMethodsP(Program.emptyLibraryProgram(),cb,path.getCBar(),ms1,ms2,ms3,name);fail("error expected");}
      catch(Exception err){
        assertEquals(err.getClass().getName(),_expected);
        }
      }
    }
  }