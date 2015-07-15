package is.L42.connected.withSafeOperators;

import static org.junit.Assert.*;
import facade.L42;
import helpers.TestHelper;
import introspection.IntrospectionSum;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import platformSpecific.javaTranslation.Resources;
import static helpers.TestHelper.getClassB;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Program;

public class PluginTest {

  @RunWith(Parameterized.class)
  public static class TestPush {
    @Parameter(0) public String e1;
    @Parameter(1) public String e2;
    @Parameter(2) public String e3;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {"{}","{B:{}}","B"
    },{"{()}","{B:{()}}","B"
    },{"{A:{}}","{B:{A:{}}}","B"
    },{"{(Outer2 foo)}","{B:{(Outer3 foo)}}","B"
    },{"{(Outer2 a,Outer1 b,Outer0 c)}","{B:{(Outer3 a,Outer2 b,Outer0 c)}}","B"
    },{"{(Outer0::H a) H:{}}","{B:{(Outer0::H a) H:{}}}","B"
    //test also the pushMany
  }});}
  @Test  public void test() {
    ClassB cb1=getClassB(e1);
    ClassB cb2=getClassB(e2);
    ClassB res=Push.pushOne(cb1,e3);
    TestHelper.assertEqualExp(res,cb2);
    }
  }


  @RunWith(Parameterized.class)
  public static class TestPop {//add more test for error cases
    @Parameter(0) public String _cb1;
    @Parameter(1) public String _expected;
    @Parameter(2) public boolean isError;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {"{B:{}}","{}",false
    },{"{B:{()}}","{()}",false
    },{"{B:{(C a) C:{}}}","{(C a) C:{}}",false
    },{"{B:{method Outer1::B b()}}",
      "{method Outer0 b()}",false
    },{"{B:{(Outer0::C a,Outer1::B::C b,Outer2::C c ) C:{}}}",
      "{(Outer0::C a,Outer0::C b,Outer1::C c ) C:{}}",false
    },{"{B:{(Outer0::C a,Outer1 b,Outer2::C c ) C:{}}}",
      "{Kind:{'@stringU\n'NotBox\n}"
      +"UsedBy:{'@stringU\n'[Outer0::B]\n}"
      +"ContainsMethods:{'@stringU\n'[]\n}"
      +"IsInterface:{'@stringU\n'false\n}}",true
  }});}
  @Test  public void test() {
    ClassB cb1=getClassB(_cb1);
    ClassB expected=getClassB(_expected);
    if(!isError){
      ClassB res=Pop.pop(cb1);
      TestHelper.assertEqualExp(expected,res);
      }
    else{
      try{Pop.pop(cb1);fail("error expected");}
      catch(Resources.Error err){
        ClassB res=(ClassB)err.unbox;
        TestHelper.assertEqualExp(expected,res);
      }
    }
  }
}



  @RunWith(Parameterized.class)
  public static class TestSum {
    @Parameter(0) public String _cb1;
    @Parameter(1) public String _cb2;
    @Parameter(2) public String _expected;
    @Parameter(3) public boolean isError;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {"{B:{}}","{C:{}}","{B:{} C:{}}",false
    },{"{()}","{B:{()}}","{() B:{()} }",false
    },{"{B:'@private\n{}}","{}","{B:'@private\n{}}",false
    },{"{B:'@private\n{}}","{}","{B:'@private\n{}}",false//twice the same, to test that we clear the used names
    },{"{B:'@private\n{}}","{B:'@private\n{}}","{B:'@private\n{} B0:'@private\n{} }",false
    },{"{B:{method Void m()}}","{B:{method B m()}}",
      "{Kind:{'@stringU\n'MethodClash\n}"
      +"Path:{'@stringU\n'Outer0::B\n}"
      +"Left:{'@stringU\n'\\u000amethod \\u000aVoid m() \n}"
      +"Right:{'@stringU\n'\\u000amethod \\u000aOuter0 m() \n}"
      +"LeftKind:{'@stringU\n'AbstractMethod\n}"
      +"RightKind:{'@stringU\n'AbstractMethod\n}"
      +"DifferentParameters:{'@stringU\n'[]\n}"
      +"DifferentReturnType:{'@stringU\n'true\n}"
      +"DifferentThisMdf:{'@stringU\n'false\n}"
      +"IncompatibleException:{'@stringU\n'false\n}}",
      true
    },{"{B:{method Outer0 m()}}","{B:{method B m()}}","{B:{method Outer0 m()} }",false
    //test the following:
    //sum of Box with Box =Box
    //sum of interface with interface =interface
    //sum of Box with interface=interface
    //sum of Box with free interface=free interface
    //sum of Class with free interface=class
    //sum of Box with class=class
    //sum of class with non free interface is error
  }});}
  @Test  public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(_cb1);
    ClassB cb2=getClassB(_cb2);
    ClassB expected=getClassB(_expected);
    L42.usedNames.clear();
    if(!isError){
      ClassB res=Sum.sum(Program.empty(),cb1,cb2);
      TestHelper.assertEqualExp(expected,res);
      }
    else{
      try{Sum.sum(Program.empty(),cb1,cb2);fail("error expected");}
      catch(Resources.Error err){
        ClassB res=(ClassB)err.unbox;
        TestHelper.assertEqualExp(expected,res);
      }
    }
  }
  }
}
