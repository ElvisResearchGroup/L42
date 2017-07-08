package programReduction;

import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.Path;
import helpers.TestHelper;

public class TestUsedPaths {
@RunWith(Parameterized.class)
public static class Test1 {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _p;
  @Parameter(2) public String _e;
  @Parameter(3) public String _expected1;
  @Parameter(4) public String _expected2;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{B:error void}","Void","<Empty Paths>","<Empty Paths>"
  },{lineNumber(),"{B:error void}","Void.foo()","<Empty Paths>","<Empty Paths>"
  },{lineNumber(),"{A:{}B:error void}","A.foo()","<Empty Paths>","This0.A "
  },{lineNumber(),"{C:{} A:{ method C m()} B:error void}","A.foo()","<Empty Paths>","This0.A This0.C "
  },{lineNumber(),"{ A:{ } B:error void}"," (A a=Any.foo() a)","<Empty Paths>","This0.A "
  },{lineNumber(),
  "{A:{A x class method mut This(fwd A x)}"
  +" Factory:{ class method A (fwd A a) A(x:a)}  B:error void} ",
  " (A myA=Factory(a:myA)  {})",
  "<Empty Paths>","This0.A This0.Factory "
  }});}
@Test  public void test() {
  Program p=TestProgram.p(_p);
  ExpCore e=TestHelper.getExpCore(TestUsedPaths.class.getSimpleName(),_e);
  PathsPaths res = UsedPaths.usedPathsE(p, e);
  assertEquals(this._expected1,res.left.toString());
  assertEquals(this._expected2,res.right.toString());
  }
}
}
