package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import helpers.TestHelper.ErrorCarry;
import static org.junit.Assert.fail;
import helpers.TestHelper;
import is.L42.connected.withSafeOperators.NormalizePrivates.CollectedPrivates;
import static helpers.TestHelper.lineNumber;

import java.util.Arrays;
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
    lineNumber(),"{method'@private\n Void foo()}","[]\n[Outer0.foo()]\n[]\nfalse"
  },{
    lineNumber(),"{method'@private\n Void foo__42()}","[42]\n[Outer0.foo__42()]\n[]\ntrue"
  },{
    lineNumber(),"{method'@private\n Void foo__42()method'@private\n Void foo__43()}",
    "[42, 43]\n[Outer0.foo__42(), Outer0.foo__43()]\n[]\ntrue"
  }});}


@Test  public void test() {
  TestHelper.configureForTest();
  ClassB cb1=getClassB("cb1", _cb1);
  CollectedPrivates result = NormalizePrivates.collectPrivates(cb1);
  Assert.assertEquals(expected, result.toString());
  }
}


}