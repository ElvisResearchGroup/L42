package is.L42.connected.withSafeOperators.refactor;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import helpers.TestHelper;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ParseFail;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PrivacyCoupuled;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import is.L42.connected.withSafeOperators.refactor.ToAbstract;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import ast.Ast;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;
import programReduction.Program;
@RunWith(Parameterized.class)
public class TestCloseInvariant{
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String _path;
  @Parameter(3) public String _expected;
  @Parameter(4) public boolean isError;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(), "{}","This",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$ClassUnfit"
    ,true
  },{lineNumber(), 
  "{A a read method Void #invariant() void A:{}}","This",
  " {\n" +
      "read method \n" +
      "This0.A a() this.a_$_2()\n" +
      "read method \n" +
      "This0.A a_$_2() \n" +
      "read method \n" +
      "Void #invariant() this.#invariant_$_2()\n" +
      "read method \n" +
      "Void #invariant_$_2() void\n" +
      "class method \n" +
      "mut This0 mutK(This0.A a) (\n" +
      "  r1=this.k_$_2(a:a)\n" +
      "  Void unusedInv1=r1.#invariant_$_2()\n" +
      "  r1\n" +
      "  )\n" +
      "class method \n" +
      "This0 immK(This0.A a) (\n" +
      "  This0 r2=this.k_$_2(a:a)\n" +
      "  Void unusedInv2=r2.#invariant_$_2()\n" +
      "  r2\n" +
      "  )\n" +
      "class method \n" +
      "mut This0 k_$_2(fwd This0.A a) \n" +
      "A: {}}"
  ,false
  },{lineNumber(), 
  "{var A a read method Void #invariant() void A:{}}","This",
  " {\n" +
      "mut method \n" +
      "Void a(This0.A that) (\n" +
      "  r1=this.a_$_2(that:that)\n" +
      "  Void unusedInv1=this.#invariant_$_2()\n" +
      "  r1\n" +
      "  )\n" +
      "mut method \n" +
      "Void a_$_2(This0.A that) \n" +
      "read method \n" +
      "This0.A a() this.a_$_2()\n" +
      "read method \n" +
      "This0.A a_$_2() \n" +
      "read method \n" +
      "Void #invariant() this.#invariant_$_2()\n" +
      "read method \n" +
      "Void #invariant_$_2() void\n" +
      "class method \n" +
      "mut This0 mutK(This0.A a) (\n" +
      "  r2=this.k_$_2(a:a)\n" +
      "  Void unusedInv2=r2.#invariant_$_2()\n" +
      "  r2\n" +
      "  )\n" +
      "class method \n" +
      "This0 immK(This0.A a) (\n" +
      "  This0 r3=this.k_$_2(a:a)\n" +
      "  Void unusedInv3=r3.#invariant_$_2()\n" +
      "  r3\n" +
      "  )\n" +
      "class method \n" +
      "mut This0 k_$_2(fwd This0.A a) \n" +
      "A: {}}"
  ,false
  
  },{lineNumber(), 
  "{capsule A a read method Void #invariant() void "
  + " mut method Void doStuff(A that)that(this.#a()) A:{}}","This",
  " {\n" +
      "read method \n" +
      "read This0.A a() this.a_$_2()\n" +
      "read method \n" +
      "read This0.A a_$_2() \n" +
      "mut method \n" +
      "lent This0.A #a_$_2() \n" +
      "read method \n" +
      "Void #invariant() this.#invariant_$_2()\n" +
      "read method \n" +
      "Void #invariant_$_2() void\n" +
      "mut method \n" +
      "Void doStuff(This0.A that) (\n" +
      "  Void r1=that.#apply(that:this.#a_$_2())\n" +
      "  Void unusedInv1=this.#invariant_$_2()\n" +
      "  r1\n" +
      "  )\n" +
      "class method \n" +
      "mut This0 mutK(capsule This0.A a) (\n" +
      "  r2=this.k_$_2(a:a)\n" +
      "  Void unusedInv2=r2.#invariant_$_2()\n" +
      "  r2\n" +
      "  )\n" +
      "class method \n" +
      "This0 immK(This0.A a) (\n" +
      "  This0 r3=this.k_$_2(a:a)\n" +
      "  Void unusedInv3=r3.#invariant_$_2()\n" +
      "  r3\n" +
      "  )\n" +
      "class method \n" +
      "mut This0 k_$_2(fwd mut This0.A a) \n" +
      "A: {}}"
  ,false

  },{lineNumber(), 
  "{capsule A a read method Void #invariant() void mut method Void mutA()( lent A a=this.#a() void) A:{}}","This",
  " {\n" +
      "read method \n" +
      "read This0.A a() this.a_$_2()\n" +
      "read method \n" +
      "read This0.A a_$_2() \n" +
      "mut method \n" +
      "lent This0.A #a_$_2() \n" +
      "read method \n" +
      "Void #invariant() this.#invariant_$_2()\n" +
      "read method \n" +
      "Void #invariant_$_2() void\n" +
      "mut method \n" +
      "Void mutA() (\n" +
      "  Void r1=(\n" +
      "    lent This0.A a=this.#a_$_2()\n" +
      "    void\n" +
      "    )\n" +
      "  Void unusedInv1=this.#invariant_$_2()\n" +
      "  r1\n" +
      "  )\n" +
      "class method \n" +
      "mut This0 mutK(capsule This0.A a) (\n" +
      "  r2=this.k_$_2(a:a)\n" +
      "  Void unusedInv2=r2.#invariant_$_2()\n" +
      "  r2\n" +
      "  )\n" +
      "class method \n" +
      "This0 immK(This0.A a) (\n" +
      "  This0 r3=this.k_$_2(a:a)\n" +
      "  Void unusedInv3=r3.#invariant_$_2()\n" +
      "  r3\n" +
      "  )\n" +
      "class method \n" +
      "mut This0 k_$_2(fwd mut This0.A a) \n" +
      "A: {}}"
  ,false
  
  },{lineNumber(), "{B:{}}","C",
  "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$PathUnfit",true
}});}
@Test  public void test() throws PathUnfit, ClassUnfit, ParseFail {
  /**
   real issue here:
   some metaprogramming classes have some parsing
   at class loading time. This uses L42.usedNames.
   Caching currently does not work well with those.
   
     Algorithm? Load all the parsing first, then do caching 
     Or normal behaviour? not clean usedNames but make it as
     after parsing meta class loaders?
   */

  TestHelper.configureForTest();
  ClassB cb1=getClassB(false,null,_cb1);
  List<Ast.C> path=TestHelper.cs(_path);
  if(!isError){
    ClassB res=InvariantClose.close(Program.emptyLibraryProgram(), path, cb1, "mutK", "immK");
    TestHelper.configureForTest();
    ClassB expected=getClassB(false,null,_expected);    
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{
      InvariantClose.close(Program.emptyLibraryProgram(), path, cb1, "mutK", "immK");
      fail("error expected");
      }
    catch(ClassUnfit|PathUnfit err){
      assertEquals(_expected,err.getClass().getName());
    }
  }
}
}




