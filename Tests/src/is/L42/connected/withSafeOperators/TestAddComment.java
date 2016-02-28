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
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

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
      {"{B:{ method Void m() void}}","B","m()","fuffa\n","{B:{ method'fuffa\n Void m() void}}",false
    },{"{B:{ method Void m() void}}","B","m()","@private\n","{B:{ method'@private\n Void m__0_0() void}}",false
    },{"{B:{ method Void m() void}}","B","m()","@private\n","{B:{ method'@private\n Void m__0_0() void}}",false
    },{"{B:{k(B b)}}",
      "B","k(b)","@private\n","{B:{class method '@private\n"
          + "This0 k__0_0(This1.B b__0_0)mut method '@private\n"
          + "This1.B #b__0_0() read method '@private\n"
          + "This1.B b__0_0() }}",false
     
      //TODO: make test that check that making private allows for name replacement if sum is used
  }});}
  @Test  public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(_cb1);
    Path path=Path.parse(_path);
    MethodSelector ms=MethodSelector.parse(_ms);
    Doc doc=Doc.factory(_doc);
    assert ms!=null;
    ClassB expected=getClassB(_expected);
    if(!isError){
      ClassB res=AddDocumentation.addDocumentationOnMethod(Program.empty(),cb1, path.getCBar(), ms,doc);
      res=Functions.clearCache(res,Stage.None);
      TestHelper.assertEqualExp(expected,res);
      }
    else{
      try{AddDocumentation.addDocumentationOnMethod(Program.empty(),cb1, path.getCBar(), ms,doc);fail("error expected");}
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
      {"{B:{ method Void m() void}}","B","foo\n","{B:'foo\n{ method Void m() void}}",false
    },{"{B:{ method Void m() void}}","B","@private\n","{B__0_0:'@private\n{ method Void m() void}}",false
  }});}
  @Test  public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(_cb1);
    Path path=Path.parse(_path);
    Doc doc=Doc.factory(_doc);
    ClassB expected=getClassB(_expected);
    if(!isError){
      ClassB res=AddDocumentation.addDocumentationOnNestedClass(Program.empty(),cb1, path.getCBar(),doc);
      TestHelper.assertEqualExp(expected,res);
      }
    else{
      try{AddDocumentation.addDocumentationOnNestedClass(Program.empty(),cb1, path.getCBar(),doc);fail("error expected");}
      catch(Resources.Error err){
        ClassB res=(ClassB)err.unbox;
        TestHelper.assertEqualExp(expected,res);
      }
    }
  }
  }

}
