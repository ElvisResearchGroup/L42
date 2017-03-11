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
public class TestLibraryOk {
//static constants to make it more readable
@Parameter(0) public int _lineNumber;
@Parameter(1) public String sProg;
@Parameter(2) public String s1;
@Parameter(3) public String s2;
@Parameters(name = "{index}: line {0}")
public static List<Object[]> createData() {
  return Arrays.asList(new Object[][] {
//----- basic attempts
{lineNumber(),"{}","{C:{method Void()void}}",null
},{lineNumber(),"{}","{C:{method Void()void } D:{}}",null
},{lineNumber(),"{}","{C:{method This()this }}",null
},{lineNumber(),"{}","{C:{method This()this() }}",null
},{lineNumber(),"{}","{C:{method D() this() } D:{class method This ()}}",null
},{lineNumber(),"{}","{C:{method Library() (x=void catch error Library y y {} ) }}",null
},{lineNumber(),"{}","{C:{class method Void foo() (This0.foo())} }",null

//fromming and calling method from other classes
},{lineNumber(),"{}",
"{C:{class method Void foo() (D.foo())} D:{class method Void foo() (void)}}",null
},{lineNumber(),"{}",
"{C:{E:{class method Void foo() (This1.foo())} class method Void foo() (D.foo())} D:{class method Void foo() (C.E.foo())}}",null
},{lineNumber(),"{}",
"{K:{E:{class method Void foo() (This2.C.foo())}} C:{class method Void foo() (D.foo())} D:{class method Void foo() (K.E.foo())}}",null
},{lineNumber(),"{}",
"{K:{ E:{class method C foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",null
},{lineNumber(),"{}",
"{C:{ method Void foo() (This0 x= this void)} }",null
},{lineNumber(),"{}",
"{C:{ method Void foo() (C x= this void)} }",null
},{lineNumber(),"{}",
"{K:{ E:{class method C foo1() (C.foo2())}} C:{class method C foo2() (D.foo3())} D:{class method C foo3() (K.E.foo1())}}",null

//method chains
},{lineNumber(),"{}",
"{K:{ E:{class method C foo1() (D.foo3().foo2().foo2())}} C:{method C foo2() (D.foo3())} D:{class method C foo3() (K.E.foo1())}}",null

//method promotions
},{lineNumber(),"{}",
"{ method This m() this.readM(this)   method read This readM(read This that)that}",null
},{lineNumber(),"{}",
"{ method This m() this.readM()   read method read This readM()this}",null
},{lineNumber(),"{}",
"{ class method mut This() method This m() This() }",null

//block promotion
},{lineNumber(),"{}",
"{ class method mut This() method This m() ( mut This x=This() x) }",null

}});}

@Test()
public void test() {
Program p=TestProgram.p(sProg);
ClassB cb1Pre=(ClassB)Desugar.of(Parser.parse(null,s1)).accept(new InjectionOnCore());
cb1Pre=new programReduction.Norm().norm(p.evilPush(cb1Pre));
TOut out=TypeSystem.instance().type(TIn.top(p,cb1Pre));
assert out.isOk():
"";
ClassB cb1=(ClassB)out.toOk().annotated;
ClassB cbExpected=s2==null?cb1Pre:(ClassB)Desugar.of(Parser.parse(null,s2)).accept(new InjectionOnCore());
TestHelper.assertEqualExp(cb1,cbExpected);
}
}
