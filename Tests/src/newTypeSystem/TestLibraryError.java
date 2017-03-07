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
},{lineNumber(),"{}","{C:{method Void()this }}",ErrorKind.NotSybtype
/*},{lineNumber(),"This0.C",
"{C:{class method Void foo() (This0.foo())} }",
"{C:{class method Void foo() (This0.foo())}##star^## }##star^##"
},{lineNumber(),"This0.C",
"{C:{class method Void foo() (D.foo())} D:{method Void() class method Void foo() (void)}}",
"{C:{class method Void foo() (D.foo())}##plus^## D:{method Void()class method Void foo() (void)}##plus ^##}##plus^##"
},{lineNumber(),"This0.C",
"{C:{E:{class method Void foo() (This1.foo())} class method Void foo() (D.foo())} D:{class method Void foo() (C.E.foo())}}",
"{C:{E:{class method Void foo() (This1.foo())}##star^## class method Void foo() (D.foo())}##star^## D:{class method Void foo() (C.E.foo())}##star^##}##star^##"
},{lineNumber(),"This0.C",
"{C:{E:{class method Void foo() (This1.foo())} class method Void foo() (D.foo())} D:{method Void() class method Void foo() (C.E.foo())}}",
"{C:{E:{class method Void foo() (This1.foo())}##plus^## class method Void foo() (D.foo())}##plus^## D:{method Void()  class method Void foo() (C.E.foo())}##plus^##}##plus^##"

},{lineNumber(),"This0.C",
"{K:{E:{class method Void foo() (This2.C.foo())}} C:{class method Void foo() (D.foo())} D:{class method Void foo() (K.E.foo())}}",
"{K:{E:{class method Void foo() (This2.C.foo())}##star^##}##star ^## C:{class method Void foo() (D.foo())}##star^## D:{class method Void foo() (K.E.foo())}##star^##}##star^##"
},{lineNumber(),"This0.C",
"{K:{method Void() E:{class method C foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",
"{K:{method Void() E:{class method C foo() (C.foo())}##star^##}##plus ^## C:{class method C foo() (D.foo())}##star^## D:{class method C foo() (K.E.foo())}##star^##}##plus^##"
//norm//NO, Norm is executed only in the extracted method
//},{"This0.C",
//"{K:{E:{class method C.foo() foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",
//"{K:{E:{class method C foo() (C.foo())}##plus^##}##plus ^## C:{class method C foo() (D.foo())}##plus^## D:{class method C foo() (K.E.foo())}##plus^##}##plus^##"
//},{"This0.C",
//"{K:{E:{class method C.foo().foo() foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",
//"{K:{E:{class method C foo() (C.foo())}##plus^##}##plus^## C:{class method C foo() (D.foo())}##plus^## D:{class method C foo() (K.E.foo())}##plus^##}##plus^##"
},{lineNumber(),"This0.C",
"{C:{ method Void foo() (This0 x= this void)} }",
"{C:{ method Void foo() (This0 x= this void)}##star^## }##star^##"
},{lineNumber(),"This0.C",
"{C:{ method Void foo() (C x= this void)} }",
"{C:{ method Void foo() (C x= this void)}##star^## }##star^##"
*/

}});}

@Test()
public void test() {
Program p=TestProgram.p(sProg);
ClassB cb1Pre=(ClassB)Desugar.of(Parser.parse(null,s1)).accept(new InjectionOnCore());
cb1Pre=new programReduction.Norm().norm(p.evilPush(cb1Pre));
TOut out=TypeSystem.instance().type(new TIn(Phase.Coherent,p,cb1Pre,Path.Library().toImmNT()));
assert !out.isOk();
ErrorKind kind= out.toError().kind;
assert kind==s2;
}
}
