package programReduction;

import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Ast.Doc;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Type;
import helpers.TestHelper;
import tools.Map;

public class TestNorm {

@RunWith(Parameterized.class)
public static class TestResolve {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _p;
  @Parameter(2) public String _type;
  @Parameter(3) public String _expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{B:error void}","Void","Void" 
  },{lineNumber(),"{I:{method Any m()} B:error void}","This0.I::m()","Any"  
  },{lineNumber(),"{I:{method I m() method Any m2()} B:error void}","This0.I::m()::m()::m2()","Any" 
  },{lineNumber(),"{I:{method I m(I x) method Any m2()} B:error void}","This0.I::m(x)::m(x)::x::m2()","Any" 

    }});}
@Test  public void test() {
  Program p=TestProgram.p(_p);
  ExpCore.Block wType=(ExpCore.Block)TestHelper.getExpCore(TestProgram.class.getSimpleName()," ("+_type+" x=Any x)");
  ExpCore.Block wTypeExp=(ExpCore.Block)TestHelper.getExpCore(TestProgram.class.getSimpleName()," ("+_expected+" x=Any x)");
  Type type = wType.getDecs().get(0).getT().get();
  NormType nt = Norm.resolve(p, type);
  TestHelper.assertEqualExp(wTypeExp,wType.withDeci(0,wType.getDecs().get(0).withT(Optional.of(nt))));
  }
}

@RunWith(Parameterized.class)
public static class TestNorm1 {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _p;
  @Parameter(2) public String _path;
  @Parameter(3) public String _expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{A:{} B:error void}","This0.A","{}" 
  },{lineNumber(),"{A:{implements I}I:{interface method Any m()} B:error void}","This0.A","{implements This1.I refine method Any m()}"  

    }});}
@Test  public void test() {
  Program p=TestProgram.p(_p);
  Ast.Path path=Path.parse(_path);
  ExpCore.ClassB expected=(ExpCore.ClassB)TestHelper.getExpCore(TestProgram.class.getSimpleName(),_expected);
  ExpCore.ClassB l=new Norm().norm(p.navigate(path));
  
  TestHelper.assertEqualExp(expected,l);
  }
}
}
