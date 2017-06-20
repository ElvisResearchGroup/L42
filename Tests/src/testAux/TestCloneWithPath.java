package testAux;

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
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import programReduction.Program;
import coreVisitors.PathAnnotateClass;

@RunWith(Parameterized.class)
public class TestCloneWithPath {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String _expected;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {return Arrays.asList(new Object[][] {{
    lineNumber(),"{}","{/*This0.*/\n}"
  },{
    lineNumber(),"{method Void foo()}","{/*This0.*/\nmethod /*This0.*/\nVoid foo()}"
  },{
    lineNumber(),"{ A:{method Void foo()  method Void bar() }}",
    "{/*This0.*/\n A:/*This0.*/\n{/*This0.A[0]*/\nmethod /*This0.A[1]*/\nVoid foo()  method /*This0.A[1]*/\nVoid bar()   }}"
  },{
    lineNumber(),"{ A:{B:{method Void foo()  method Void bar() }}}",
    "{/*This0.*/\n"
    + "A:/*This0.*/\n"
    + "{/*This0.A[0]*/\n"
    + "B:/*This0.A[1]*/\n"
    + "{/*This0.A[1]B[0]*/\n"
    + "method /*This0.A[1]B[1]*/\n"
    + "Void foo() \n"
    + "method /*This0.A[1]B[1]*/\n"
    + "Void bar() }}}"
  },{
    lineNumber(),"{ A:{method Void foo() this.foo({ implements A},x:{ implements B}) }}",
    "{/*This0.*/\n"
    + "A:/*This0.*/\n"
    + "{/*This0.A[0]*/\n"
    + "method /*This0.A[1]*/\n"
    + "Void foo() this.foo(that:{/*This0.A[1]foo()[0]*/\n"
    + " implements This2.A}, x:{/*This0.A[1]foo()[1]*/\n"
    + " implements This2.B})}}"
  }});}


@Test  public void test() {
  TestHelper.configureForTest();
  ClassB cb1=getClassB(true,"cb1", _cb1);
  ClassB expected=getClassB(true,"expected", _expected);
  ClassB result = (ClassB) cb1.accept(new PathAnnotateClass());
  TestHelper.assertEqualExp(expected, result);
  }
}