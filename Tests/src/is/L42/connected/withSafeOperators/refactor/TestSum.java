package is.L42.connected.withSafeOperators.refactor;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import facade.L42;
import helpers.TestHelper;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SubtleSubtypeViolation;
import is.L42.connected.withSafeOperators.refactor.Compose;

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
    },{    lineNumber(),"{}","{B:{}}","{ B:{} }",false
    },{    lineNumber(),"{B_$_1:{}}","{}","{B_$_1:{}}",false
    },{    lineNumber(),"{B_$_1:{}}","{B_$_1:{}}","{B_$_1:{} B_$_4:{}}",false//twice the same, to test that we clear the used names
    },{    lineNumber(),"{B_$_1:{}}","{B_$_1:{}}","{B_$_1:{} B_$_4:{}}",false//twice the same, to test that we clear the used names
    },{    lineNumber(),"{B:{method Void m()}}","{B:{method B m()}}",
      "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$MethodClash",
      true
    },{    lineNumber(),
    "{                             B:{method Void m()}}",
    "{I:{interface method Void m()}B:{implements I}}",
    "{I:{interface method Void m()}B:{implements I refine method Void m()}}",
    //"is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$MethodClash",//we relaxed the sum to allow this
      false
    },{    lineNumber(),
    "{J:{interface method Void m()} B:{ implements J}}",
    "{I:{interface method Void m()} B:{ implements I}}",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$SubtleSubtypeViolation",
    true

    },{    lineNumber(),"{B:{method This0 m()}}","{B:{method B m()}}","{B:{method This0 m()} }",false

    },{    lineNumber(),
      "{I1:{interface  implements  I2} I2:{interface} }",
      "{I1:{interface } I2:{interface implements  I1} }",
      "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$SubtleSubtypeViolation",
      true
  }});}
  @Test  public void test() throws MethodClash, SubtleSubtypeViolation, ClassClash {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(true,null,_cb1);
    ClassB cb2=getClassB(true,null,_cb2);
    if(!isError){
      ClassB res=new Compose(cb1,cb2).compose(Program.emptyLibraryProgram(),cb1,cb2);
      ClassB expected=getClassB(true,null,_expected);
      TestHelper.assertEqualExp(expected,res);
      }
    else{
      try{new Compose(cb1,cb2).compose(Program.emptyLibraryProgram(),cb1,cb2);fail("error expected");}
      catch(Exception err){
        assertEquals(err.getClass().getName(),_expected);
        }
    }
  }
  }
