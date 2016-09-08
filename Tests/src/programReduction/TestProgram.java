package programReduction;

import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.Path;
import helpers.TestHelper;

public class TestProgram {
public static Program p(String s){
  ExpCore.ClassB l=(ExpCore.ClassB)TestHelper.getExpCore(s,TestProgram.class.getSimpleName());
  CtxL first=CtxL.split(l);
  
  return null;
  }
@RunWith(Parameterized.class)
public static class TestIsEquivPaths {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _p;
  @Parameter(2) public String _path1;
  @Parameter(3) public String _path2;
  @Parameter(4) public boolean equiv;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"Any.m({B:error void})","void","Any.m(void)"
  },{lineNumber(),"{B:error void}.m({B:error void})","void","void.m({B:error void})"
}});}
@Test  public void test() {
  Program p=p(_p);
  Ast.Path path1=Path.parse(_path1);
  Ast.Path path2=Path.parse(_path2);
  assertEquals(p.equiv(path1, path2),this.equiv); }
  }

}
