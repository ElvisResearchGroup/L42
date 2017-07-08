package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import helpers.TestHelper;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.ExpCore.ClassB;

public class TestRemoveCode {
  //----------------------------------------------------------
  @RunWith(Parameterized.class)
  public static class Test_removeUnreachableCode {
      @Parameter(0) public String _e1;
      @Parameter(1) public String _expected;
      @Parameterized.Parameters
      public static List<Object[]> createData() {
        return Arrays.asList(new Object[][] {

    {"{}","{}"
  },{"{C:{}}","{C:{}}"
  },{"{ C_$_1:{}}","{}"
  },{"{ A:{ method Void foo() use C_$_1 check bar() void }"
    + " C_$_1:{}}",
    "{ A:{ method Void foo() use C_$_1 check bar() void }"
    + " C_$_1:{}}"
  },{"{ A:{ method class Any foo() C_$_1 } C_$_1:{}}",
  "{ A:{ method class Any foo() C_$_1 } C_$_1:{}}"
  },{"{C_$_1 x C_$_1:{}}","{C_$_1 x C_$_1:{}}"
  },{"{C_$_1 x C_$_1:{D_$_1 y} D_$_1:{D_$_1 y}}",
  "{C_$_1 x C_$_1:{D_$_1 y} D_$_1:{D_$_1 y}}"
  },{"{C_$_1 x C_$_1:{D_$_1 y} D_$_1:{D_$_1 y} E:{} F_$_1:{}}",
  "{C_$_1 x E:{} C_$_1:{D_$_1 y} D_$_1:{D_$_1 y} }"
  },{"{D:{method Any foo() A_$_1()} A_$_1:{implements I_$_1 class method This() } I_$_1:{interface}}",
  "{D:{method Any foo() A_$_1()} A_$_1:{implements I_$_1 class method This()} I_$_1:{interface}}"
  }});}
  @Test public void test() {
    TestHelper.configureForTest();
    ClassB e1=getClassB(true,null,_e1);
    ClassB expected=getClassB(true,null,_expected);
    ClassB res=RemoveCode.removeUnreachableCode(e1);
    TestHelper.assertEqualExp(res,expected);}}


}
