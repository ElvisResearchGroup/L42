package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.fail;

import facade.Configuration;
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
import programReduction.Program;

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
    },{    lineNumber(),"{B://@private\n{}}","{}","{B__0_0://@private\n{}}",false
    },{    lineNumber(),"{B://@private\n{}}","{}","{B__0_0://@private\n{}}",false//twice the same, to test that we clear the used names
    },{    lineNumber(),"{B://@private\n{}}","{B://@private\n{}}","{B__0_0://@private\n{} B__1_0://@private\n{} }",false
    },{    lineNumber(),"{B:{method Void m()}}","{B:{method B m()}}",
      "{Kind:{//@stringU\n//MethodClash\n}"
      +"Path:{//@.B\n}"
      +"Left:{//@stringU\n//method Void m()\n}"
      +"Right:{//@stringU\n//method This0 m()\n}"
      +"LeftKind:{//@stringU\n//AbstractMethod\n}"
      +"RightKind:{//@stringU\n//AbstractMethod\n}"
      +"DifferentParameters:{//@stringU\n//[]\n}"
      +"DifferentReturnType:{//@stringU\n//true\n}"
      +"DifferentThisMdf:{//@stringU\n//false\n}"
      +"IncompatibleException:{//@stringU\n//false\n}}",
      true
    },{    lineNumber(),"{B:{method Void m()}}","{I:{interface method Void m()}B:{ implements I}}",
      "{Kind:{//@stringU\n//MethodClash\n}"
      +"Path:{//@.B\n}"
      +"Left:{//@stringU\n//method Void m()\n}"
      +"Right:{//@stringU\n//method Void m()\n}"
      +"LeftKind:{//@stringU\n//AbstractMethod\n}"
      +"RightKind:{//@stringU\n//InterfaceAbstractMethod\n}"
      +"DifferentParameters:{//@stringU\n//[]\n}"
      +"DifferentReturnType:{//@stringU\n//false\n}"
      +"DifferentThisMdf:{//@stringU\n//false\n}"
      +"IncompatibleException:{//@stringU\n//false\n}}",
      true
    },{    lineNumber(),"{J:{interface method Void m()} B:{ implements J}}","{I:{interface method Void m()}B:{ implements I}}",
      "{Kind:{//@stringU\n//ClassClash\n}"
     +"Path:{//@.B\n}"
     +"ConflictingImplementedInterfaces:{//[@.J, @.I]\n}}",
      true

    },{    lineNumber(),"{B:{method This0 m()}}","{B:{method B m()}}","{B:{method This0 m()} }",false

    },{    lineNumber(),"{B__0fred://@private\n{}}","{}",
      "{B_$%0fred__0_0://@private\n{}}",false

    },{    lineNumber(),"{B__0_0://@private\n{}}","{B__0_1://@private\n{}}",
      "{B__0_0://@private\n{} B__0_1://@private\n{}}",false


    },{    lineNumber(),"{B__0_0://@private\n{}}","{B__1_0://@private\n{}}",
      "{B__0_0://@private\n{} B__0_1://@private\n{}}",false

    //TODO: test sum private state plus public state is ok
    },{    lineNumber(),
      "{I1:{interface  implements  I2} I2:{interface} }",
      "{I1:{interface } I2:{interface implements  I1} }",
      "{I1:{interface  implements I2} I2:{interface implements I1} }",
      false
  }});}
  @Test  public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(_cb1);
    ClassB cb2=getClassB(_cb2);
    ClassB expected=getClassB(_expected);
    if(!isError){
      ClassB res=_Sum.sum(Program.emptyLibraryProgram(),cb1,cb2);
      TestHelper.assertEqualExp(expected,res);
      }
    else{
      try{_Sum.sum(Program.emptyLibraryProgram(),cb1,cb2);fail("error expected");}
      catch(Resources.Error err){
        ClassB res=(ClassB)err.unbox;
        TestHelper.assertEqualExp(expected,res);
      }
    }
  }
  }
