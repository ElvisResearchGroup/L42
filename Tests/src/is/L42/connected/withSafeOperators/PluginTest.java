package is.L42.connected.withSafeOperators;

import static org.junit.Assert.*;
import helpers.TestHelper;
import introspection.IntrospectionSum;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import static helpers.TestHelper.getClassB;
import ast.Ast.Path;
import ast.ExpCore.ClassB;

public class PluginTest {
  
  @RunWith(Parameterized.class)
  public static class TestSum {
    @Parameter(0) public String e1;
    @Parameter(1) public String e2;
    @Parameter(2) public String e3;
    @Parameterized.Parameters
    public static List<String[]> createData() {
      return Arrays.asList(new String[][] {
      {"{}","{B:{}}","B"
    },{"{()}","{B:{()}}","B"
    },{"{A:{}}","{B:{A:{}}}","B"
    },{"{(Outer2 foo)}","{B:{(Outer3 foo)}}","B"
    },{"{(Outer2 a,Outer1 b,Outer0 c)}","{B:{(Outer3 a,Outer2 b,Outer0 c)}}","B"
    },{"{(Outer0::H a) H:{}}","{B:{(Outer0::H a) H:{}}}","B"
    //test also the pushMany

  }});}

  @Test
  public void test() {
    ClassB cb1=getClassB(e1);
    ClassB cb2=getClassB(e2);
    ClassB res=Push.pushOne(cb1,e3);
    TestHelper.assertEqualExp(res,cb2);
    }
  }
}
