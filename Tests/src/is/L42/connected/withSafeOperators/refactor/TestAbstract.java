package is.L42.connected.withSafeOperators.refactor;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import helpers.TestHelper;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
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

public class TestAbstract{
@RunWith(Parameterized.class)
public static class TestAbstractMeth {//add more test for error cases
  //@Parameter(0) public int _lineNumber;
  @Parameter(0) public String _cb1;
  @Parameter(1) public String _path;
  @Parameter(2) public String _ms;
  @Parameter(3) public String _expected;
  @Parameter(4) public boolean isError;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {"{B:{ method Void m() void}}","B","m()","{B:{ method Void m()}}",false
  },{"{B:{ method Void m(Any x) void}}","B","m(x)","{B:{ method Void m(Any x)}}",false
  },{"{ method Void m(Any x) void}","This0","m(x)","{ method Void m(Any x)}",false
  },{"{C:{B:{ method Void m(Any x) void}}}","C.B","m(x)","{C:{B:{ method Void m(Any x)}}}",false
  },{"{ method Void m()}","This0","m()","{ method Void m()}",false
  },{"{B:{ method Void m(Any x) void}}", "C", "m(x)",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$PathUnfit",
	  true
  },{"{B:{ method Void m(Any x) void}}", "B", "k()",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$SelectorUnfit",
	  true
  },{"{B:{ method Void m(Any x) void}}", "B", "m()",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$SelectorUnfit",
	  true
}});}
@Test  public void test() throws SelectorUnfit, PathUnfit, MethodClash {
  ClassB cb1=getClassB(false,null,_cb1);
  List<Ast.C> path=TestHelper.cs(_path);
  MethodSelector ms=MethodSelector.parse(_ms);
  assert ms!=null;
  if(!isError){
    ClassB expected=getClassB(false,null,_expected);
    //TODO: mettere tests per il caso con un selettore destinazione. In particolare testare interfacce
    ClassB res=ToAbstract.toAbstractAux(Program.emptyLibraryProgram(),cb1, path, ms,null);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{ToAbstract.toAbstractAux(Program.emptyLibraryProgram(),cb1, path, ms,null);fail("error expected");}
    catch(SelectorUnfit| PathUnfit| MethodClash err){
      assertEquals(_expected,err.getClass().getName());
    }
  }
}
}

@RunWith(Parameterized.class)
public static class TestMoveMeth {//add more test for error cases
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String _path;
  @Parameter(3) public String _ms1;
  @Parameter(4) public String _ms2;
  @Parameter(5) public String _expected;
  @Parameter(6) public boolean isError;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),//
      "{B:{ method Void m() void}}","B","m()","k()","{B:{ method Void k() void method Void m()}}",false
  },{lineNumber(),//
    "{B:{ method Void m(Any x) }}","B","m(x)","k(x)","{B:{ method Void m(Any x)}}",false
  },{lineNumber(),//
    "{ method Void m(Any x) void}","This0","m(x)","k(x)","{ method Void k(Any x) void  method Void m(Any x)}",false
  },{lineNumber(),//
    "{C:{B:{ method Void m(Any x) }}}","C.B","m(x)","k(x)","{C:{B:{ method Void m(Any x)}}}",false
  },{
    lineNumber(),//
    "{ method Void m()}","This0","m()","k(x)",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$SelectorUnfit"
    ,true
}});}
@Test  public void test() throws SelectorUnfit, PathUnfit, MethodClash {
  TestHelper.configureForTest();
  ClassB cb1=getClassB(false,null,_cb1);
  List<Ast.C> path=TestHelper.cs(_path);
  MethodSelector ms1=MethodSelector.parse(_ms1);
  assert ms1!=null;
  MethodSelector ms2=MethodSelector.parse(_ms2);
  assert ms2!=null;
  if(!isError){
  ClassB expected=getClassB(false,null,_expected);
    ClassB res=ToAbstract.toAbstractAux(Program.emptyLibraryProgram(),cb1, path, ms1,ms2);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{ToAbstract.toAbstractAux(Program.emptyLibraryProgram(),cb1, path, ms1,ms2);fail("error expected");}
    catch(SelectorUnfit| PathUnfit| MethodClash err){
      assertEquals(_expected,err.getClass().getName());
    }
  }
}
}

@RunWith(Parameterized.class)
public static class TestAbstractClass {//add more test for error cases
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String _path;
  @Parameter(3) public String _expected;
  @Parameter(4) public boolean isError;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),//
      "{B:{ method Void m() void}}","B","{B:{ method Void m()}}",false
  },{lineNumber(),//
    "{B:{ method Void m_$_1() void}}","B","{B:{}}",false
  },{lineNumber(),//
    "{B:{ method Void m(Any x) void}}","B","{B:{ method Void m(Any x)}}",false
  },{lineNumber(),//
    "{ method Void m(Any x) void}","This0","{ method Void m(Any x)}",false
  },{lineNumber(),//
    "{B:{ method Void m_$_2() void method Void n() void}}","B","{B:{ method Void n()}}",false
  },{lineNumber(),//
    "{ method Void m_$_1(Any x) void}","This0","{}",false
  },{lineNumber(),//
    "{C:{B:{ method Void m(Any x) void}}}","C.B","{C:{B:{ method Void m(Any x)}}}",false
  },{lineNumber(),//
    "{C:{B:{ method  Void m_$_1(Any x) void}}}","C.B","{C:{B:{}}}",false
  },{lineNumber(),//
    "{C:{B:{ method Void m(Any x) void  method Void foo_$_1() void }}}",
	  "C.B",
	  "{C:{B:{ method Void m(Any x)}}}",
	  false

  },{lineNumber(),//
    "{B:{interface method Void m()}}","B","{B:{interface method Void m()}}",false
  },{lineNumber(),//
    "{C:{B:{ A_$_1:{} }}}","C","{C:{B:{}}}",false
  },{lineNumber(),//
    "{C:{B:{ A_$_1:{} }}}","C.B","{C:{B:{}}}",false
  },{lineNumber(),//
    "{C:{B_$_1:{}}}","C","{C:{}}",false
  },{lineNumber(),//
    "{C:{B_$_1:{} D:{}}}","C","{C:{D:{}}}",false
  },{lineNumber(),//
    "{C:{B_$_1:{} D:{E_$_2:{} }}}","C.D","{C:{B_$_1:{} D:{}}}",false
  },{lineNumber(),//
    "{C:{ method  Void m_$_1() void D:{E_$_2:{} }}}",
	  "C.D",
	  "{C:{ method Void m_$_1() void D:{}}}",
	  false
  },{lineNumber(),//
    "{C:{ method Void m_$_1() void D:{method Void m_$_2() void}}}",
	  "C.D",
	  "{C:{ method Void m_$_1() void D:{}}}",
	  false
  },{lineNumber(),//
    "{C:{ method Void m() D.m() D:{ class method Void m() void}}}",
    "C.D",
    "{C:{ method Void m() D.m() D:{ class method Void m() }}}",
    false
  },{lineNumber(),//
    "{C:{ method Void m() A.D.m() A:{D:{ class method Void m() void}}}}",
    "C.A.D",
    "{C:{ method Void m() A.D.m() A:{D:{ class method Void m() }}}}",
    false
  },{lineNumber(),//
    "{C:{ method Void m() A.D.m()} A:{D:{ class method Void m() void class method Void k_$_1() void}}}",
    "A.D",
    "{C:{ method Void m() A.D.m()} A:{D:{ class method Void m() }}}",
    false
  },{lineNumber(),//
    "{C:{ B_$_1:{}}}","C.B",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$PathUnfit",
	  true

  },{lineNumber(),//
    "{C:{}}","C.B",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$PathUnfit",
	  true
  },{lineNumber(),//
    "{C:{}}","B",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$PathUnfit",
	  true

  },{lineNumber(),//
    "{C:{B:{ method Void m(Any x) void  method Void foo_$_1() void } D:{ method Void bar() B.foo_$_1() }}}",
	  "C.B",
	  "PrivacyCoupuled:\n"+
      "coupuled paths:[]\n"+
      "coupuled selectors:[C.B::foo_$_1()]"
	  //+ "[This2.C.B.foo()]\n}}",the user or the used??
	  //+"[This0.C.D::bar()]\n}}",
	 , true
  },{lineNumber(),//
    "{C:{B_$_1:{} D:{ method B_$_1 bar() void }}}",
	  "C",
	  "PrivacyCoupuled:\n"+
      "coupuled paths:[[C, B_$_1]]\n"+
      "coupuled selectors:[]",
	   true
  },{lineNumber(),// Should this pass, or the previous one?
    "{C:{B_$_1:{}} D:{ method B_$_1 bar() void }}",
    "C",
    "{C: {} D: {method This1.B_$_1 bar() void}}",
     false // SHOULD FAIL

}});}
@Test  public void test() throws PathUnfit, PrivacyCoupuled {
  TestHelper.configureForTest();
  ClassB cb1=getClassB(false,null,_cb1);
  List<Ast.C> path=TestHelper.cs(_path);
  if(!isError){
    ClassB expected=getClassB(false,null,_expected);
    ClassB res=AbstractClass.toAbstract(Program.emptyLibraryProgram(),cb1, path);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{
      AbstractClass.toAbstract(Program.emptyLibraryProgram(),cb1, path);
      fail("error expected");}
    catch(PathUnfit | PrivacyCoupuled err){
      assertEquals(err.toString(),_expected);
      }
  }
}
}


}





