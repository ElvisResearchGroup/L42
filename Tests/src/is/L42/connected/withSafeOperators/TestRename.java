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
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Program;

public class TestRename{
@RunWith(Parameterized.class)
public static class TestRenameMethod {//add more test for error cases
  @Parameter(0) public String _cb1;
  @Parameter(1) public String _path;
  @Parameter(2) public String _ms1;
  @Parameter(3) public String _ms2;
  @Parameter(4) public String _expected;
  @Parameter(5) public boolean isError;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {"{B:{ method Void m() void}}","B","m()","k()","{B:{ method Void k() void}}",false
  },{"{ method Void m() void}","Outer0","m()","k()","{ method Void k() void}",false
}});}
@Test  public void test() {
  TestHelper.configureForTest();
  ClassB cb1=getClassB(_cb1);
  Path path=Path.parse(_path);
  MethodSelector ms1=MethodSelector.parse(_ms1);
  MethodSelector ms2=MethodSelector.parse(_ms2);
  ClassB expected=getClassB(_expected);
  if(!isError){
    ClassB res=Rename.renameMethod(Program.empty(),cb1, path.getCBar(), ms1,ms2);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{Rename.renameMethod(Program.empty(),cb1, path.getCBar(), ms1,ms2);fail("error expected");}
    catch(Resources.Error err){
      ClassB res=(ClassB)err.unbox;
      TestHelper.assertEqualExp(expected,res);
    }
  }
}
}

@RunWith(Parameterized.class)
public static class TestRenameClass {//add more test for error cases
  @Parameter(0) public String _cb1;
  @Parameter(1) public String _path1;
  @Parameter(2) public String _path2;
  @Parameter(3) public String _expected;
  @Parameter(4) public boolean isError;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {"{B:{ method Void m() void}}","B","C","{C:{ method Void m() void}}",false
  },{"{B:{ method Void m() void}}","B","C::D","{C:{ D:{method Void m() void}}}",false
}});}
@Test  public void test() {
  TestHelper.configureForTest();
  ClassB cb1=getClassB(_cb1);
  Path path1=Path.parse(_path1);
  Path path2=Path.parse(_path2);
  ClassB expected=getClassB(_expected);
  if(!isError){
    ClassB res=Rename.renameClass(Program.empty(),cb1, path1.getCBar(),path2.getCBar());
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{Rename.renameClass(Program.empty(),cb1, path1.getCBar(),path2.getCBar());fail("error expected");}
    catch(Resources.Error err){
      ClassB res=(ClassB)err.unbox;
      TestHelper.assertEqualExp(expected,res);
    }
  }
}
}


}