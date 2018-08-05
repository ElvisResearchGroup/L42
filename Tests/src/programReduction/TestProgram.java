package programReduction;

import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.Path;
import helpers.TestHelper;

public class TestProgram {
public static Program p(String s){
  ExpCore.ClassB l=(ExpCore.ClassB)TestHelper.getExpCore(TestProgram.class.getSimpleName(),s);
  FlatProgram fp=new FlatProgram(l);
  fp.freshIds=new int[]{10};
  Program p=fp;
  ExpCore.ClassB currentTop=l;
  while(true){
    CtxL first=CtxL._split(currentTop);
    if (first==null){return p;}
    ExpCore hole=first.originalHole();
    if (!(hole instanceof ExpCore.ClassB)){return p;}
    currentTop=(ExpCore.ClassB)hole;
    p= new PushedProgram(currentTop,first,p);
    }
  }

@RunWith(Parameterized.class)
public static class TestIsEquivPaths {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _p;
  @Parameter(2) public String _path1;
  @Parameter(3) public String _path2;
  @Parameter(4) public boolean equiv;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{B:error void}","Void","Void",true
  },{lineNumber(),"{B:error void}","Void","Library",false
  },{lineNumber(),"{B:{C:error void}}","Void","Void",true
  },{lineNumber(),"{B:{C:error void}}","This0.C","This1.B.C",false
  },{lineNumber(),"{B:{C:error void}}","This0.C","This1.B",false
  },{lineNumber(),"{B:{C:{method m() this.foo({D:error void})}}}","This2.C","This3.B.C",false
  },{lineNumber(),"{B:{C:{method m() this.foo({D:error void})}}}","This0.D","This3.B.C.D",false

  //  },{lineNumber(),"{B:error void}.m({B:error void})","void","void.m({B:error void})"
}});}
@Test  public void test() {
  Program p=p(_p);
  Ast.Path path1=Path.parse(_path1);
  Ast.Path path2=Path.parse(_path2);
  assertEquals(this.equiv,p.equiv(path1, path2)); }
  }

@RunWith(Parameterized.class)
public static class TestUpdate {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _p1;
  @Parameter(2) public String _l;
  @Parameter(3) public String _expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{B:error void}","{}","{}",//yes, here the whole is replaced; It is a flat program indeed
  },{lineNumber(),"{A:{B:error void}}","{}","{A:{}}",
  },{lineNumber(),"{A:{B:{C:{error void}}}}","{}","{A:{B:{}}}",
 
}});}
@Test  public void test() {
  Program p=p(_p1);
  ExpCore.ClassB l=(ExpCore.ClassB)TestHelper.getExpCore(TestProgram.class.getSimpleName(),_l);
  ExpCore.ClassB expected=(ExpCore.ClassB)TestHelper.getExpCore(TestProgram.class.getSimpleName(),_expected);
  p=p.updateTop(l);
  try{while(true){ p=p.pop(); }}
  catch(Program.EmptyProgram ep){}
  TestHelper.assertEqualExp(expected,p.top()); 
  }
}

}
