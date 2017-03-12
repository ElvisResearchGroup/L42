package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import helpers.TestHelper.ErrorCarry;
import static org.junit.Assert.fail;
import helpers.TestHelper;
import static helpers.TestHelper.lineNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import programReduction.Program;

@RunWith(Parameterized.class)
public class TestAddKs{

  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String[] _fields;
  @Parameter(3) public String _mutK;
  @Parameter(4) public String _lentK;
  @Parameter(5) public String _readK;
  @Parameter(6) public String _immK;
  @Parameter(7) public String _expected;
  @Parameter(8) public boolean isError;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    List<Object[]> tests= Arrays.asList(new Object[][] {
    {lineNumber(),
    "{ }",new String[]{},"aMut","aLent","aRead","aImm",
    "{  class method mut   This0 aMut()"+
    "   class method lent   This0 aLent() "+
    "   class method read  This0 aRead() "+
    "   class method          This0 aImm()         }",
    false
    },{lineNumber(),
    "{ method Any foo() }",new String[]{"foo"},"aMut","aLent","aRead","aImm",
    "{  method Any foo() "+
    "   class method mut   This0 aMut(fwd Any foo)"+
    "   class method lent   This0 aLent(fwd Any foo) "+
    "   class method read  This0 aRead(fwd Any foo) "+
    "   class method          This0 aImm(fwd Any foo)         }",
    false
    },{lineNumber(),
    "{ mut method mut Any foo() }",new String[]{"foo"},"aMut","aLent","aRead","aImm",
    "{  mut method mut Any foo() "+
    "   class method mut   This0 aMut(fwd mut Any foo)"+
    "   class method lent   This0 aLent(fwd lent Any foo) "+
    "   class method read  This0 aRead(fwd read Any foo) "+
    "   class method          This0 aImm(fwd Any foo)         }",
    false
    },{lineNumber(),
    "{ mut method lent Any foo() }",new String[]{"foo"},"aMut","aLent","aRead","aImm",
    "{  mut method lent Any foo() "+
    "   class method mut   This0 aMut(fwd capsule Any foo)"+
    "   class method lent   This0 aLent(fwd capsule Any foo) "+
    "   class method read  This0 aRead(fwd capsule Any foo) "+
    "   class method          This0 aImm(fwd Any foo)         }",
    false

        }});
    return tests;
}


@Test  public void test() {

  TestHelper.configureForTest();
  ClassB cb1=getClassB("cb1", _cb1);
  ClassB expected=getClassB("expected", _expected);
  if(!isError){
    ClassB res=MakeKs.makeKs(cb1, Collections.emptyList(),Arrays.asList(_fields), _mutK, _lentK, _readK, _immK, true);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{
      MakeKs.makeKs(cb1, Collections.emptyList(),Arrays.asList(_fields), _mutK, _lentK, _readK, _immK, true);
      fail("error expected");
      }
    catch(Resources.Error err){
      ClassB res=(ClassB)err.unbox;
      TestHelper.assertEqualExp(expected,res);
    }
  }
}
}

