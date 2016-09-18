package programReduction;

import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Ast.Doc;
import ast.Ast.Path;
import helpers.TestHelper;
import tools.Map;

public class TestMethods {

@RunWith(Parameterized.class)
public static class TestMethodsCollect {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _p;
  @Parameter(2) public String _path1;
  @Parameter(3) public String _expectedCb;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{B:error void}","This0","{}" 
  },{lineNumber(),"{method Any m() B:error void}","This0","{method Any m()}"  
  },{lineNumber(),"{I:{interface method Any m()} A:{implements I} B:error void}","This0.I","{method Any m()}"  
  },{lineNumber(),"{I:{interface method Any m()} A:{implements I} B:error void}","This0.A","{refine method Any m()}"  
  },{lineNumber(),"{I2:{interface method Any m2()} I1:{interface method Any m1()} A:{implements I1, I2} B:error void}","This0.A","{refine method Any m1() refine method Any m2()}"  //hope the order is stable
  },{lineNumber(),"{I0:{interface method Any m0()} I2:{interface  implements I0  method Any m2()} I1:{interface  implements I0  method Any m1()} A:{implements I1, I2} B:error void}","This0.A","{refine method Any m1() refine method Any m0() refine method Any m2()}"  //hope the order is stable
  },{lineNumber(),"{I0:{interface method Any m0()} I2:{interface  implements I0  method Any m2() refine method Void m0()} I1:{interface  implements I0  method Any m1()} A:{implements I1, I2} B:error void}","This0.A","{refine method Any m1() refine method Any m0() refine method Any m2()}"  //hope the order is stable
  //TODO: @James, look to test over and under, is this what we wanted?
  },{lineNumber(),"{I0:{interface method Any m0()} I2:{interface  implements I0  method Any m2() refine method Void m0()} I1:{interface  implements I0  method Any m1()} A:{implements I2, I1} B:error void}","This0.A","{refine method Any m2() refine method Void m0() refine method Any m1()}"  //hope the order is stable

  
    }});}
@Test  public void test() {
  Program p=TestProgram.p(_p);
  Ast.Path path1=Path.parse(_path1);
  ExpCore.ClassB l=(ExpCore.ClassB)TestHelper.getExpCore(TestProgram.class.getSimpleName(),_expectedCb);
  List<MethodWithType> ms = Methods.methods(p, path1);
  ExpCore.ClassB cb=l.withMs(Map.of(m->m,ms));//inference magic
  TestHelper.assertEqualExp(l, cb);
  }
}
}
