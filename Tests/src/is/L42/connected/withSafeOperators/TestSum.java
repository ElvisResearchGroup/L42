package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static org.junit.Assert.fail;
import facade.L42;
import helpers.TestHelper;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import platformSpecific.javaTranslation.Resources;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Program;

  @RunWith(Parameterized.class)
  public class TestSum {
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
