package newTypeSystem;

import static helpers.TestHelper.lineNumber;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Phase;
import facade.Parser;
import helpers.TestHelper;
import programReduction.Program;
import programReduction.TestProgram;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
@RunWith(Parameterized.class)
public class TestLibraryError {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String sProg;
  @Parameter(2) public String s1;
  @Parameter(3) public newTypeSystem.ErrorKind s2;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
{lineNumber(),"{}","{C:{method Void()}}",ErrorKind.LibraryNotCoherent
},{lineNumber(),"{}","{C:{method Void()this }}",ErrorKind.NotSubtypeClass
},{lineNumber(),"{}","{C:{method class This()this }}",ErrorKind.NotSubtypeMdf
},{lineNumber(),"{}","{C:{method This()this.foo() method Any foo()this }}",ErrorKind.NotSubtypeClass

}});}

@Test()
public void test() {
Program p=TestProgram.p(sProg);
ClassB cb1Pre=(ClassB)Desugar.of(Parser.parse(null,s1)).accept(new InjectionOnCore());
cb1Pre=new programReduction.Norm().norm(p.evilPush(cb1Pre));
TOut out=TypeSystem.instance().type(TIn.top(p,cb1Pre));
assert !out.isOk();
ErrorKind kind= out.toError().kind;
assert kind==s2;
}
}
