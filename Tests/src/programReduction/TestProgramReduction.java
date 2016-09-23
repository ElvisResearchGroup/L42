package programReduction;

import static helpers.TestHelper.lineNumber;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.ExpCore;
import ast.Ast.NormType;
import ast.Ast.Type;
import helpers.TestHelper;

public class TestProgramReduction {
@RunWith(Parameterized.class)
public static class TestResolve {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _p;
  @Parameter(2) public String _expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
//    {lineNumber(),"{B:error void}","{B:error void}" 
    {lineNumber(),"{B:{C:error void}}","{B:error void}" 

//  },{lineNumber(),"{I:{method Any m()} B:error void}","This0.I::m()","Any"  
//  },{lineNumber(),"{I:{method I m() method Any m2()} B:error void}","This0.I::m()::m()::m2()","Any" 
//  },{lineNumber(),"{I:{method I m(I x) method Any m2()} B:error void}","This0.I::m(x)::m(x)::x::m2()","Any" 

    }});}
@Test  public void test() {
  ExpCore.ClassB l1=(ExpCore.ClassB)TestHelper.getExpCore(TestProgramReduction.class.getSimpleName(),_p);
  ExpCore.ClassB l2=(ExpCore.ClassB)TestHelper.getExpCore(TestProgramReduction.class.getSimpleName(),_expected);
  TestHelper.assertEqualExp(l2,ProgramReduction.allSteps(l1));
  }
}

}
