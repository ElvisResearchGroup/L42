package is.L42.connected.withSafeOperators.refactor;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import helpers.TestHelper;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodUnfit;
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
public class TestWither{
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String _path;
  @Parameter(3) public String _ms;
  @Parameter(4) public String _expected;
  @Parameter(5) public boolean isError;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(), "{}","This","k(a)",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$SelectorUnfit"
    ,true
  },{lineNumber(), "{B:{}}","C","k(a)",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$PathUnfit",true
  },{lineNumber(), "{Any a Any b class method This k(Any a Any b)}","This","k(a)",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$SelectorUnfit",true
  },{lineNumber(), "{Any a class method This k(Any a)}","This","k(a)",
  "{"
  + "Any a "
  + "class method This k(Any a)"
  + "method This0 with(Any a) This0.k(a:a)"
  + "}",
  false
  },{lineNumber(), "{Any a class method This k(Any a) exception This}","This","k(a)",
  "{"
  + "Any a "
  + "class method This k(Any a) exception This "
  + "method This0 with(Any a)  exception This This0.k(a:a)"
  + "}",
  false

  },{lineNumber(), "{Any a Any b class method This k(Any a Any b)}","This","k(a,b)",
  "{"
  + "Any a Any b "
  + "class method This k(Any a Any b) "
  + "method This0 with(Any a) This0.k(a:a b:this.b()) "
  + "method This0 with(Any b) This0.k(a:this.a() b:b) "
  + "}",
  false
  
  },{lineNumber(), "{Any a Any b class method This k(Any a Any b) method Void with(Any b)}","This","k(a,b)",
  "{"
  + "Any a Any b "
  + "class method This k(Any a Any b) "
  + "method Void with(Any b) "
  + "method This0 with(Any a) This0.k(a:a b:this.b()) "
  + "}",
  false
}});}
@Test  public void test() throws PathUnfit, SelectorUnfit, MethodUnfit, ClassUnfit {
  TestHelper.configureForTest();
  ClassB cb1=getClassB(false,null,_cb1);
  List<Ast.C> path=TestHelper.cs(_path);
  MethodSelector ms=MethodSelector.parse(_ms);
  if(!isError){
    ClassB res=Wither.wither(Program.emptyLibraryProgram(), path, cb1,ms);
    TestHelper.configureForTest();
    ClassB expected=getClassB(false,null,_expected);    
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{
      Wither.wither(Program.emptyLibraryProgram(), path, cb1,ms);
      fail("error expected");
      }
    catch(ClassUnfit|PathUnfit | SelectorUnfit | MethodUnfit err){
      assertEquals(_expected,((Object)err).getClass().getName());
    }
  }
}
}




