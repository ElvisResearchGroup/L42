package programReduction;

import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Ast.Doc;
import ast.Ast.NormType;
import ast.Ast.Path;
import helpers.TestHelper;
import tools.Map;

public class TestNorm {



@RunWith(Parameterized.class)
public static class TestNorm1 {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _p;
  @Parameter(2) public String _path;
  @Parameter(3) public String _expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{A:{} B:error void}","This0.A","{}" 
  },{lineNumber(),"{A:{implements I}I:{interface method Any m()} B:error void}","This0.A","{implements This1.I refine method Any m()}"  

    }});}
@Test  public void test() {
  Program p=TestProgram.p(_p);
  Ast.Path path=Path.parse(_path);
  ExpCore.ClassB expected=(ExpCore.ClassB)TestHelper.getExpCore(TestProgram.class.getSimpleName(),_expected);
  ExpCore.ClassB l=new Norm().norm(p.navigate(path));
  
  TestHelper.assertEqualExp(expected,l);
  }
}
}
