package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import helpers.TestHelper.ErrorCarry;
import static org.junit.Assert.fail;
import helpers.TestHelper;
import is.L42.connected.withSafeOperators.NormalizePrivates.CollectedPrivates;
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
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
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
    lineNumber(),"{method'@private\n Void foo()}","[]\n[Outer0::foo()foo__0_0()]\n[]\nfalse"
  },{
    lineNumber(),"{method'@private\n Void foo__42()}","[42]\n[Outer0::foo__42()null]\n[]\ntrue"
  },{
    lineNumber(),"{method'@private\n Void foo__42()method'@private\n Void foo__43()}",
    "[42, 43]\n[Outer0::foo__42()null, Outer0::foo__43()null]\n[]\ntrue"
  },{
    lineNumber(),
    "{method'@private\n Void foo__42()"
    + " this({ method '@private\n Void bla__33()  }, second:{})"
    + "}",
    "[33, 42]\n[Outer0::foo__42()null, Outer0::foo__42()[1]bla__33()null]\n[]\ntrue"
  },{
    lineNumber(),
    "{ make(Foo f)'@private\n method'@private\n Void foo()"
    + " this({ method '@private\n Void bla() void }, second:{mut kame(mut Hame ha)'@private\n})"
    + "}",
    "[]\n"
    + "[Outer0::make(f)make__0_0(f__0_0),"
    + " Outer0::#f()#f__0_0(),"
    + " Outer0::f()f__0_0(),"
    + " Outer0::foo()foo__1_0(),"
    + " Outer0::foo()[1]bla()bla__2_0(),"
    + " Outer0::foo()[2]kame(ha)kame__3_0(ha__3_0),"
    + " Outer0::foo()[2]#ha()#ha__3_0(),"
    + " Outer0::foo()[2]ha()ha__3_0()]\n[]\nfalse"
  },{
    lineNumber(),
    "{method'@private\n Void foo__42()"
    + " this({ method '@private\n Void bla()  }, second:{})"
    + "}",
    "[42]\n[Outer0::foo__42()foo_$%42__0_0(), Outer0::foo__42()[1]bla()bla__1_0()]\n[]\nfalse"
  
  }});}


@Test  public void test() {
  TestHelper.configureForTest();
  NormalizePrivates.reset();
  ClassB cb1=getClassB("cb1", _cb1);
  CollectedPrivates result = NormalizePrivates.collectPrivates(cb1);
  if (result.pedexes.size()==0 || !result.normalized){
    result.computeNewNames();
  }
  Assert.assertEquals(expected, result.toString());
  }
}
}