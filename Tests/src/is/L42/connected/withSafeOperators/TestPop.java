package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static org.junit.Assert.fail;
import helpers.TestHelper;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import platformSpecific.javaTranslation.Resources;
import ast.ExpCore.ClassB;

@RunWith(Parameterized.class)
public class TestPop {//add more test for error cases
  @Parameter(0) public String _cb1;
  @Parameter(1) public String _expected;
  @Parameter(2) public boolean isError;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {"{B:{}}","{}",false
  },{"{B:{()}}","{()}",false
  },{"{B:{(C a) C:{}}}","{(C a) C:{}}",false
  },{"{B:{method This1.B b()}}",
    "{method This0 b()}",false
  },{"{B:{(This0.C a,This1.B.C b,This2.C c ) C:{}}}",
    "{(This0.C a,This0.C b,This1.C c ) C:{}}",false
  },{"{B:{(This0.C a,This1 b,This2.C c ) C:{}}}",
    "{Kind:{'@stringU\n'NotBox\n}"
    +"UsedBy:{'@stringU\n'[This0.B]\n}"
    +"Supertypes:{'@stringU\n'[]\n}"
    +"ContainsMethods:{'@stringU\n'[]\n}"
    +"ActualKind:{'@stringU\n'Template\n}}",true
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



