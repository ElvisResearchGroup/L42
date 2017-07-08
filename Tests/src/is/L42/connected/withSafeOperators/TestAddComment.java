package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static org.junit.Assert.fail;
import helpers.TestHelper;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import platformSpecific.javaTranslation.Resources;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;
import programReduction.Program;

public class TestAddComment {
  @RunWith(Parameterized.class)
  public static class TestAddCommentMeth {
    @Parameter(0) public String _cb1;
    @Parameter(1) public String _path;
    @Parameter(2) public String _ms;
    @Parameter(3) public String _doc;
    @Parameter(4) public String _expected;
    @Parameter(5) public boolean isError;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {"{B:{ method Void m() void}}","B","m()",
      "fuffa\n",
      "{B:{ method//fuffa\n Void m() void}}",false
    },{"{B:{ method//bar\n Void m() void}}",
    "B","m()",
    "@beer\n",
    "{B:{ method//bar\n//@beer\n Void m() void}}",false
 }});}
  @Test  public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(false,null,_cb1);
    List<Ast.C> path=TestHelper.cs(_path);
    MethodSelector ms=MethodSelector.parse(_ms);
    Doc doc=Doc.factory(true,_doc);
    assert ms!=null;
    ClassB expected=getClassB(false,null,_expected);
    if(!isError){
      ClassB res=AddDocumentation.addDocumentationOnMethod(Program.emptyLibraryProgram(),cb1, path, ms,doc);
      TestHelper.assertEqualExp(expected,res);
      }
    else{
      try{AddDocumentation.addDocumentationOnMethod(Program.emptyLibraryProgram(),cb1, path, ms,doc);fail("error expected");}
      catch(Resources.Error err){
        ClassB res=(ClassB)err.unbox;
        TestHelper.assertEqualExp(expected,res);
      }
    }
  }
  }

  @RunWith(Parameterized.class)
  public static class TestAddCommentClass {//add more test for error cases
    @Parameter(0) public String _cb1;
    @Parameter(1) public String _path;
    @Parameter(2) public String _doc;
    @Parameter(3) public String _expected;
    @Parameter(4) public boolean isError;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {"{B:{ method Void m() void}}","B","foo\n","{B://foo\n{ method Void m() void}}",false
    },{"{B://bar\n{ method Void m() void}}",
    "B","@beer\n",
    "{B://bar\n//@beer\n{ method Void m() void}}",false
  }});}
  @Test  public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(false,null,_cb1);
    List<Ast.C> path=TestHelper.cs(_path);
    Doc doc=Doc.factory(true,_doc);
    ClassB expected=getClassB(false,null,_expected);
    if(!isError){
      ClassB res=AddDocumentation.addDocumentationOnNestedClass(Program.emptyLibraryProgram(),cb1, path,doc);
      TestHelper.assertEqualExp(expected,res);
      }
    else{
      try{AddDocumentation.addDocumentationOnNestedClass(Program.emptyLibraryProgram(),cb1, path,doc);fail("error expected");}
      catch(Resources.Error err){
        ClassB res=(ClassB)err.unbox;
        TestHelper.assertEqualExp(expected,res);
      }
    }
  }
  }

}
