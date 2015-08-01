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
  },{"{()}","{()}"
  },{"{C:{}}","{C:{}}"
  },{"{ C:'@private\n{}}","{}"
  },{"{ A:{ method Void foo() using C check bar() void } C:'@private\n{}}","{ A:{ method Void foo() using C check bar() void } C:'@private\n{}}"
  },{"{(C x) C:'@private\n{}}","{(C x) C:'@private\n{}}"
  },{"{(C x) C:'@private\n{(D y)} D:'@private\n{(D y)}}","{(C x) C:'@private\n{(D y)} D:'@private\n{(D y)}}"
  },{"{(C x) C:'@private\n{(D y)} D:'@private\n{(D y)} E:{} F:'@private\n{}}","{(C x) E:{} C:'@private\n{(D y)} D:'@private\n{(D y)} }"
  },{"{method C::m() foo() C:'@private\n{method Void m()}}","{method C::m() foo() C:'@private\n{method Void m()}}"
  },{"{D:{method C::m() foo()} C:'@private\n{method Void m()}}","{D:{method C::m() foo()} C:'@private\n{method Void m()}}"
  }});}
  @Test public void test() {
    TestHelper.configureForTest();
    ClassB e1=getClassB(_e1);
    ClassB expected=getClassB(_expected);
    ClassB res=RemoveCode.removeUnreachableCode(e1);
    TestHelper.assertEqualExp(res,expected);}}


}
