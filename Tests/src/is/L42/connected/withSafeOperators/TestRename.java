package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import helpers.TestHelper;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import is.L42.connected.withSafeOperators.refactor.AbstractClass;
import is.L42.connected.withSafeOperators.refactor.RenameMethods;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import ast.Ast;
import ast.Ast.C;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore;
import ast.PathAux;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Phase;
import ast.Util.PathMx;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import facade.Configuration;

public class TestRename {
  @RunWith(Parameterized.class) public static class TestRenameMethod {//add more test for error cases
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String _cb1;
    @Parameter(2) public String _path;
    @Parameter(3) public String _ms1;
    @Parameter(4) public String _ms2;
    @Parameter(5) public String _expected;
    @Parameter(6) public boolean isError;

    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] { { //
          lineNumber(),
          "{B:{ method Void m() void}}", "B", "m()", "k()", "{B:{ method Void k() void}}", false //
          }, {lineNumber(), //
          "{ method Void m() void}", "This0", "m()", "k()", "{ method Void k() void}", false //
          }, {lineNumber(), //
          "{ method Void m() void}", "This0", "m()", "k(x)",
          "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$SelectorUnfit",
          true //
          }, {lineNumber(), //
          "{interface  method Any m() B:{implements This1 refine method Void m()}}",
          "This0", "m()", "k()",
          "{interface  method Any k() B:{implements This1 refine method Void k()}}",
          false //
          }, {lineNumber(), //
          "{interface  method Any m() B:{implements This1 refine method Void m()}}",
          "B", "m()", "k()",
          "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$SelectorUnfit",
          true //

          } });
    }

    @Test public void test() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit {
      TestHelper.configureForTest();
      ClassB cb1 = getClassB(true,null,_cb1);
      List<Ast.C> path=TestHelper.cs(_path);
      MethodSelector ms1 = MethodSelector.parse(_ms1);
      MethodSelector ms2 = MethodSelector.parse(_ms2);
      if (!isError) {
        ClassB expected = getClassB(true,null,_expected);
        ClassB res = new RenameMethods().addRename(path, ms1, ms2).actP(Program.emptyLibraryProgram(), cb1);
        TestHelper.assertEqualExp(expected, res);
      } else {
        try {
          new RenameMethods().addRename(path, ms1, ms2).actP(Program.emptyLibraryProgram(), cb1);
          fail("error expected");
        } catch (PathUnfit|SelectorUnfit|MethodClash|ClassUnfit err) {
          assertEquals(_expected, err.getClass().getName());
        }
      }
    }
  }

 
  //-----
  @Test
  public void testToTop() {
    List<Ast.C> res = ClassOperations.toTop(Arrays.asList(),Path.parse("This0.A"));
    Assert.assertEquals(res,PathAux.sToC(Arrays.asList("A")));
    res = ClassOperations.toTop(Arrays.asList(),Path.parse("This0.A.B"));
    Assert.assertEquals(res,PathAux.sToC(Arrays.asList("A","B")));
    res = ClassOperations.toTop(PathAux.sToC(Arrays.asList("C")),Path.parse("This1.A.B"));
    Assert.assertEquals(res,PathAux.sToC(Arrays.asList("A","B")));
    res = ClassOperations.toTop(PathAux.sToC(Arrays.asList("C")),Path.parse("This0.A.B"));
    Assert.assertEquals(res,PathAux.sToC(Arrays.asList("C","A","B")));
  }
  @Test
  public void testNormalizePath() {
    Path res = ClassOperations.normalizePath(Arrays.asList(),0,Arrays.asList());
    Assert.assertEquals(res,Path.outer(0));
    res = ClassOperations.normalizePath(Arrays.asList(C.of("A")),1,Arrays.asList(C.of("B")));
    Assert.assertEquals(res,Path.parse("This1.B"));
    res = ClassOperations.normalizePath(Arrays.asList(C.of("B")),1,Arrays.asList(C.of("B")));
    Assert.assertEquals(res,Path.parse("This0"));

  }
}
