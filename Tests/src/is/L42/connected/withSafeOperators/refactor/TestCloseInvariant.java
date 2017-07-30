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
  "{ read method This0.A a() This0.a_$_2() "
  + "read method This0.A a_$_2() "
  + "read method Void #invariant() void "
  + "class method mut This0 mutK(This0.A a) ( "
  + "r=This0.k_$_2(a:a) this.#invariant() r) "
  + "class method mut This0 k_$_2(fwd This0.A a) "
  + "class method mut This0 immK(This0.A a) ( "
  + "r=This0.k_$_2(a:a)this.#invariant() r) "
  + "A: {}} "
  ,false
  },{lineNumber(), 
  "{var A a read method Void #invariant() void A:{}}","This",
  "{ mut method Void a(This0.A that) ("
  + "r=This0.a_$_2(that:that) this.#invariant() r ) "
  + "mut method Void a_$_2(This0.A that) "  
  + " read method This0.A a() This0.a_$_2() "
  + "read method This0.A a_$_2() "
  + "read method Void #invariant() void "
  + "class method mut This0 mutK(This0.A a) ( "
  + "r=This0.k_$_2(a:a) this.#invariant() r) "
  + "class method mut This0 k_$_2(fwd This0.A a) "
  + "class method mut This0 immK(This0.A a) ( "
  + "r=This0.k_$_2(a:a)this.#invariant() r) "
  + "A: {}} "
  ,false
  
  },{lineNumber(), 
  "{capsule A a read method Void #invariant() void "
  + " mut method Void doStuff(A that)that(this.#a()) A:{}}","This",
  "{ read method read This0.A a() This0.a_$_2()"
  + "read method read This0.A a_$_2() "
  + "read method Void #invariant() void "
  + "mut method Void doStuff(This0.A that) ("
  + "  r=that(this.#a()) this.#invariant() r ) "
  + "class method mut This0 mutK(capsule This0.A a) ("
  + "  r=This0.k_$_2(a:a) this.#invariant() r ) "
  + "class method mut This0 k_$_2(fwd mut This0.A a) "
  + "class method mut This0 immK(This0.A a) ("
  + "r=This0.k_$_2(a:a) this.#invariant() r ) "
  + "A: {}} "
  ,false

  
  
  },{lineNumber(), "{B:{}}","C",
  "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$PathUnfit",true
}});}
@Test  public void test() throws PathUnfit, ClassUnfit, ParseFail {
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




