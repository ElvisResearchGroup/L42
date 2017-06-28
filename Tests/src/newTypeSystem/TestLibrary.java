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
import ast.ErrorMessage.NotOkToStar;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Phase;
import facade.Parser;
import helpers.TestHelper;
import programReduction.Program;
import programReduction.TestProgram;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
@RunWith(Parameterized.class)
public class TestLibrary {
static Object sameAsFormer=new Object();

@Parameter(0) public int _lineNumber;
@Parameter(1) public String sProg;
@Parameter(2) public String s1;
@Parameter(3) public Object s2;
@Parameters(name = "{index}: line {0}")
public static List<Object[]> createData() {
  return Arrays.asList(new Object[][] {
//----- basic attempts
//ok
{  lineNumber(),"{}","{C:{method Void()void}}",sameAsFormer
//err
},{lineNumber(),"{}","{C:{method Void()}}",ErrorKind.LibraryNotCoherent

//ok
},{lineNumber(),"{}","{C:{method Void()void } D:{}}",sameAsFormer
//err
},{lineNumber(),"{}","{C:{method Void()this }}",ErrorKind.NotSubtypeClass

//ok
},{lineNumber(),"{}","{C:{method This()this }}",sameAsFormer
},{lineNumber(),"{}","{C:{method This()this() }}",sameAsFormer
//err
},{lineNumber(),"{}","{C:{method class This()this }}",ErrorKind.NotSubtypeMdf
},{lineNumber(),"{}","{C:{method This()this.foo() method Any foo()this }}",ErrorKind.NotSubtypeClass

//ok
},{lineNumber(),"{}","{C:{method D() this() } D:{class method This ()}}",sameAsFormer
},{lineNumber(),"{}","{C:{method Library() (x=void catch error Library y y {} ) }}","{C:{method Library() (Void x=void catch error Library y y {} ) }}"
},{lineNumber(),"{}","{C:{class method Void foo() (This0.foo())} }",sameAsFormer
//err
},{lineNumber(),"{}","{C:{method D() this() } D:{class method This ( mut This that )}}",ErrorKind.LibraryNotCoherent


//fromming and calling method from other classes
//ok
},{lineNumber(),"{}",
"{C:{class method Void foo() (D.foo())} D:{class method Void foo() (void)}}",sameAsFormer
},{lineNumber(),"{}",
"{C:{E:{class method Void foo() (This1.foo())} class method Void foo() (D.foo())} D:{class method Void foo() (C.E.foo())}}",sameAsFormer
},{lineNumber(),"{}",
"{K:{E:{class method Void foo() (This2.C.foo())}} C:{class method Void foo() (D.foo())} D:{class method Void foo() (K.E.foo())}}",sameAsFormer
},{lineNumber(),"{}",
"{K:{ E:{class method C foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",sameAsFormer
},{lineNumber(),"{}",
"{C:{ method Void foo() (This0 x= this void)} }",sameAsFormer
},{lineNumber(),"{}",
"{C:{ method Void foo() (C x= this void)} }",sameAsFormer
},{lineNumber(),"{}",
"{K:{ E:{class method C foo1() (C.foo2())}} C:{class method C foo2() (D.foo3())} D:{class method C foo3() (K.E.foo1())}}",sameAsFormer

//method chains
//ok
},{lineNumber(),"{}",
"{K:{ E:{class method C foo1() (D.foo3().foo2().foo2())}} C:{method C foo2() (D.foo3())} D:{class method C foo3() (K.E.foo1())}}",sameAsFormer
//err
},{lineNumber(),"{}",
"{K:{ E:{class method C foo1() (D.foo3().foo2().foo2())}} C:{class method C foo2() (D.foo3())} D:{class method C foo3() (K.E.foo1())}}",ErrorKind.ClassMethCalledOnNonClass

//method promotions
},{lineNumber(),"{}",
"{ method This m() this.readM(this)   method read This readM(read This that)that}",sameAsFormer
},{lineNumber(),"{}",
"{ method This m() this.readM()   read method read This readM()this}",sameAsFormer
},{lineNumber(),"{}",
"{ class method mut This() method This m() This() }",sameAsFormer

//block promotion
},{lineNumber(),"{}",
"{ class method mut This() method This m() ( mut This x=This() x) }",sameAsFormer

}});}

@Test()
public void test() {
Program p=TestProgram.p(sProg);
ClassB cb1Pre=(ClassB)Desugar.of(Parser.parse(null,s1)).accept(new InjectionOnCore());
cb1Pre=new programReduction.Norm().norm(p.evilPush(cb1Pre));
TOut out;try{out=TypeSystem.instance().type(TIn.top(Phase.Coherent,p,cb1Pre));}
catch(NotOkToStar coh){
  assert s2==ErrorKind.LibraryNotCoherent;
  return;
  }
if(s2 instanceof ErrorKind){
  assert !out.isOk();
  ErrorKind kind= out.toError().kind;
  assert kind==s2;
  }
else{
  assert out.isOk():
  "";
  ClassB cb1=(ClassB)out.toOk().annotated;
  ClassB cbExpected=(s2==sameAsFormer)?cb1Pre:(ClassB)Desugar.of(Parser.parse(null,(String)s2)).accept(new InjectionOnCore());
  TestHelper.assertEqualExp(cb1,cbExpected);
  }
}
}

