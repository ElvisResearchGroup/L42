package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.fail;
import facade.L42;
import helpers.TestHelper;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import ast.Ast;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

  @RunWith(Parameterized.class)
  public class TestSum {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String _cb1;
    @Parameter(2) public String _cb2;
    @Parameter(3) public String _expected;
    @Parameter(4) public boolean isError;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {    lineNumber(),"{B:{}}","{C:{}}","{B:{} C:{}}",false
    },{    lineNumber(),"{()}","{B:{()}}","{() B:{()} }",false
    },{    lineNumber(),"{B:'@private\n{}}","{}","{B__0_0:'@private\n{}}",false
    },{    lineNumber(),"{B:'@private\n{}}","{}","{B__0_0:'@private\n{}}",false//twice the same, to test that we clear the used names
    },{    lineNumber(),"{B:'@private\n{}}","{B:'@private\n{}}","{B__0_0:'@private\n{} B__1_0:'@private\n{} }",false
    },{    lineNumber(),"{B:{method Void m()}}","{B:{method B m()}}",
      "{Kind:{'@stringU\n'MethodClash\n}"
      +"Path:{'@::B\n}"
      +"Left:{'@stringU\n'method Void m()\n}"
      +"Right:{'@stringU\n'method Outer0 m()\n}"
      +"LeftKind:{'@stringU\n'AbstractMethod\n}"
      +"RightKind:{'@stringU\n'AbstractMethod\n}"
      +"DifferentParameters:{'@stringU\n'[]\n}"
      +"DifferentReturnType:{'@stringU\n'true\n}"
      +"DifferentThisMdf:{'@stringU\n'false\n}"
      +"IncompatibleException:{'@stringU\n'false\n}}",
      true
    },{    lineNumber(),"{B:{method Outer0 m()}}","{B:{method B m()}}","{B:{method Outer0 m()} }",false

    },{    lineNumber(),"{B__0fred:'@private\n{}}","{}",
      "{B_$%0fred__0_0:'@private\n{}}",false
      
    },{    lineNumber(),"{B__0_0:'@private\n{}}","{B__0_1:'@private\n{}}",
      "{B__0_0:'@private\n{} B__0_1:'@private\n{}}",false
      

    },{    lineNumber(),"{B__0_0:'@private\n{}}","{B__1_0:'@private\n{}}",
      "{B__0_0:'@private\n{} B__0_1:'@private\n{}}",false
  }});}
  @Test  public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(_cb1);
    ClassB cb2=getClassB(_cb2);
    ClassB expected=getClassB(_expected);
    if(!isError){
      ClassB res=Sum.sum(Program.empty(),cb1,cb2);
      res=Functions.clearCache(res,Ast.Stage.None);
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
