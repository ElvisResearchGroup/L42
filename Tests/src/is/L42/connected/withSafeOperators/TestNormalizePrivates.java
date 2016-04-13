package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import helpers.TestHelper.ErrorCarry;
import static org.junit.Assert.fail;
import helpers.TestHelper;

import static helpers.TestHelper.lineNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
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

public class TestNormalizePrivates{
@RunWith(Parameterized.class)
public static class TestCollectPrivates {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String expected;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {return Arrays.asList(new Object[][] {{
    lineNumber(),"{}","[]\n[]\n[]\ntrue"
  },{
    lineNumber(),"{method Void foo()}","[]\n[]\n[]\ntrue"
  },{
    lineNumber(),"{method//@private\n Void foo()}","[]\n[This0.foo()[0]foo__0_0()]\n[]\nfalse"
  },{
    lineNumber(),"{method//@private\n Void foo__42()}","[42]\n[This0.foo__42()[0]null]\n[]\nfalse"
  },{
    lineNumber(),"{method//@private\n Void foo__42()method//@private\n Void foo__43()}",
    "[42, 43]\n[This0.foo__42()[0]null, This0.foo__43()[0]null]\n[]\nfalse"
  },{
    lineNumber(),
    "{method//@private\n Void foo__42_0()"
    + " this({ method //@private\n Void bla__33_1()  }, second:{})"
    + "}",
    "[33_1, 42_0]\n[This0.foo__42_0()[0]null, This0.foo__42_0()[1]bla__33_1()[0]null]\n[]\ntrue"
  },{

    lineNumber(),
    "{method//@private\n Void foo__42()"
    + " this({ method //@private\n Void bla()  }, second:{})"
    + "}",
    "[42]\n[This0.foo__42()[0]null, This0.foo__42()[1]bla()[0]null]\n[]\nfalse"

  },{
    lineNumber(),
    "{method//@private\n Void foo( This0::foo() x)}",
    "[]\n[This0.foo(x)[0]foo__0_0(x)]\n[]\nfalse"

  }});}


@Test  public void test() {
  TestHelper.configureForTest();
  NormalizePrivates.reset();
  ClassB cb1=getClassB("cb1", _cb1);
  CollectedLocatorsMap result = NormalizePrivates.collectPrivates(cb1);
  if (result.pedexes.size()==0){
    result.computeNewNames();
  }
  Assert.assertEquals(expected, result.toString());
  }
}
@RunWith(Parameterized.class)
public static class TestNormalizePrivates1 {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String _expected;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {return Arrays.asList(new Object[][] {{
    lineNumber(),"{}","{}"
  },{
    lineNumber(),"{method Void foo()}","{method Void foo()}"
  },{
    lineNumber(),"{method Void fo__o()}","{method Void fo_$%o()}"
  },{
    lineNumber(),"{method Void foo___a()}","{method Void foo_$%_a()}"
  },{
    lineNumber(),"{method Void foo____a()}","{method Void foo_$%_$%a()}"
  },{
    lineNumber(),"{method Void foo_____a()}","{method Void foo_$%_$%_a()}"
  },{
    lineNumber(),"{method Void fo$%$%o__a()}","{method Void fo$%$%o_$%$%$%a()}"
  },{
    lineNumber(),"{ C__A:{interface  implements C__A }}","{ C_$%A:{interface  implements This1.C_$%A }}"
  },{
    lineNumber(),"{ C://@private\n{interface  implements C }}","{ C__0_0://@private\n{interface  implements This1.C__0_0}}"
  },{
    lineNumber(),"{ C://@private\n{interface D:{ implements C} }}","{ C__0_0://@private\n{interface D:{ implements This2.C__0_0} }}"
  },{
    lineNumber(),"{ C://@private\n{interface A:{B:{ implements C }}}}","{ C__0_0://@private\n{interface A:{B:{ implements This3.C__0_0}}}}"
  },{
    lineNumber(),"{ D:{ implements C} C://@private\n{interface  implements C }}","{ D:{ implements C__0_0 } C__0_0://@private\n{interface  implements This1.C__0_0}}"
  },{
    lineNumber(),"{ D:{ implements A.C} A:{C://@private\n{interface  implements C }}}","{ D:{ implements A.C__0_0 } A:{C__0_0://@private\n{interface  implements This1.C__0_0}}}"
  },{
    lineNumber(),"{ D:{ implements  A.C.D} A:{C://@private\n{ D:{interface}}}}","{ D:{ implements A.C__0_0.D } A:{C__0_0://@private\n{ D:{interface}}}}"
  },{
    lineNumber(),"{ D:{ implements A.C, A.C.D} A:{C://@private\n{interface  implements C D:{interface}}}}","{ D:{ implements A.C__0_0,A.C__0_0.D } A:{C__0_0://@private\n{interface  implements C__0_0 D:{interface}}}}"

  },{
    lineNumber(),"{ A:{ method //@private\n Void foo() void}}","{ A:{ method //@private\n Void foo__0_0() void}}"
  },{
    lineNumber(),"{  method //@private\n Void foo() void}","{  method //@private\n Void foo__0_0() void}"
  },{
    lineNumber(),"{ method Library bar() {  method //@private\n Void foo() void}}","{ method Library bar() {  method //@private\n Void foo__0_0() void}}"
  },{
    lineNumber(),
    "{method//@private\n Void foo( This0::foo(x) x)}",
    "{\nmethod //@private\nVoid foo__0_0(This0::foo__0_0(x ) x) }"

  },{
    lineNumber(),
    "{ A://@private\n{method A a()} method A foo( A::a() x)}",
    "{ A__0_0://@private\n{method A__0_0 a()} method  A__0_0 foo( A__0_0::a() x)}"

  },{
    lineNumber(),
    "{ A__1_12://@private\n{method A__1_12 a()} method A__1_12 foo()}",
    "{ A__1_12://@private\n{method A__1_12 a()} method  A__1_12 foo()}"
  },{
    lineNumber(),
    "{ A:{method//@private\n A a__1_12()}}",
    "{ A:{method//@private\n A a__1_12()}}"
  /*},{
    lineNumber(),
    "{ A:{(A a)//@private\n}  B:{method A::a() fuffa()} }",
    "{ A:{#apply__0_0(A a__0_0)//@private\n}  B:{method A::a__0_0() fuffa()} }"
  },{
    lineNumber(),
    "{ A:{(A a)//@private\n}  B:{method A fuffa(A a) a.a()} }",
    "{ A:{#apply__0_0(A a__0_0)//@private\n}  B:{method A fuffa(A a) a.a__0_0()} }"
  },{
    lineNumber(),
    "{ A:{(A a)//@private\n}  B:{(A a) method A fuffa(B a) a.a().a()} }",
    "{ A:{#apply__0_0(A a__0_0)//@private\n}  B:{(A a) method A fuffa(B a) a.a().a__0_0()} }"
  },{
    lineNumber(),
    "{ A:{(A a)//@private\n}  B:{(A a)//@private\n method A fuffa(B a) a.a().a()} }",
    "{ A:{#apply__0_0(A a__0_0)//@private\n}  B:{#apply__1_0(A a__1_0)//@private\n method A fuffa(B a) a.a__1_0().a__0_0()} }"
  },{
    lineNumber(),
    "{ A:{(A a)//@private\n}  B:{(A _a)//@private\n method A a()this._a() method A fuffa(B a) a.a().a()} }",
    "{ A:{#apply__0_0(A a__0_0)//@private\n}  B:{#apply__1_0(A _a__1_0)//@private\n method A a()this._a__1_0() method A fuffa(B a) a.a().a__0_0()} }"
  */
  },{
    lineNumber(),
    "{reuse L42.is/NanoBasePrivates6\n  B2:{ } }",
    "{C:{method //@private\nVoid foo__1_1() void\nmethod Void bar() this.foo__1_1()}B2:{}}"

  }});}


@Test  public void test() {
  TestHelper.configureForTest();
  NormalizePrivates.reset();
  ClassB cb1=getClassB("cb1", _cb1);
  ClassB expected=getClassB("expected", _expected);
  cb1=NormalizePrivates.normalize(Program.empty(),cb1);
  cb1=Functions.clearCache(cb1,Ast.Stage.None);
  TestHelper.assertEqualExp(expected,cb1);
  }
}
}