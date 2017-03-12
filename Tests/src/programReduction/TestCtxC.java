package programReduction;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import ast.ExpCore;
import ast.Expression;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import facade.Parser;
import helpers.TestHelper;
import is.L42.connected.withSafeOperators.Abstract;
import platformSpecific.javaTranslation.Resources;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;

public class TestCtxC {
@RunWith(Parameterized.class)
public static class TestFillHole {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _e1;
  @Parameter(2) public String _eHole;
  @Parameter(3) public String _expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"Any.m({B:error void})","void","Any.m(void)"
  },{lineNumber(),"{B:error void}.m({B:error void})","void","void.m({B:error void})"
}});}
@Test  public void test() {
  ExpCore e1=TestHelper.getExpCore(this.getClass().getCanonicalName(),_e1);
  ExpCore eHole=TestHelper.getExpCore(this.getClass().getCanonicalName(),_eHole);
  ExpCore expected=TestHelper.getExpCore(this.getClass().getCanonicalName(),_expected);
  CtxC ctxC1=CtxC.split(e1);
  ExpCore res=ctxC1.fillHole(eHole);
  TestHelper.assertEqualExp(expected,res);
  }
  }

@RunWith(Parameterized.class)
public static class TestDivide {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _e1;
  @Parameter(2) public String _e2;
  @Parameter(3) public String _expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"Any.m({B:error void})","Any.m(Any)","Any"
  },{lineNumber(),"{B:error void}.m({B:error void})","Any.m(Any)","Any"
}});}
@Test  public void test() {
  ExpCore e1=TestHelper.getExpCore(this.getClass().getCanonicalName(),_e1);
  ExpCore e2=TestHelper.getExpCore(this.getClass().getCanonicalName(),_e2);
  ExpCore expected=TestHelper.getExpCore(this.getClass().getCanonicalName(),_expected);
  CtxC ctxC1=CtxC.split(e1);
  CtxC res=ctxC1.divide(e2);
  TestHelper.assertEqualExp(expected,res.originalHole());
  TestHelper.assertEqualExp(e2,res.fillHole(res.originalHole()));
  }
  }
}