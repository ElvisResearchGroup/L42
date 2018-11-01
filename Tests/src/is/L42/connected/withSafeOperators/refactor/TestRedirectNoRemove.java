package is.L42.connected.withSafeOperators.refactor;

import static helpers.TestHelper.getClassB;

import ast.Ast;
import helpers.TestHelper.ErrorCarry;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.IncoherentMapping;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.refactor.RedirectObj;
import helpers.TestHelper;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.Util.CsPath;
import programReduction.Program;

@RunWith(Parameterized.class)
public class TestRedirectNoRemove {

  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String[] _p;
  @Parameter(2) public String _cb1;
  @Parameter(3) public String _path1;
  @Parameter(4) public String _path2;
  @Parameter(5) public String _expected;
  @Parameter(6) public boolean isError;

  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(), new String[]{"{A:{}}"},
      "{InnerA:{} B:{ method InnerA m(InnerA a) a}}","InnerA","This0.A",
      "{InnerA: {} B:{ method This2.A m(This2.A a) a}}",false
    }, {lineNumber(),//
      new String[]{"{D:{method Library m(This1.D a)}}"},
      "{ A:{method Library m(This1.A a) } method Void b(This0.A m)}"
      ,"A","This0.D",
      " { method \n" +
      "Void b(This1.D m) \n" +
      "A: {\n" +
      "method \n" +
      "Library m(This2.D a) }}",false//
    }});
}

@Test  public void test() throws ClassUnfit, IncoherentMapping, MethodClash, PathUnfit {

  TestHelper.configureForTest();
  Program p=TestHelper.getProgram(_p);
  ClassB cb1=getClassB(true,p,"cb1", _cb1);
  List<Ast.C> path1=_path1.isEmpty() ? List.of() : Path.parse("This0." + _path1).getCBar();
  Path path2=Path.parse(_path2);
  if(!isError){
    ClassB expected=getClassB(true,p,"expected", _expected);
    ClassB res=new RedirectObj(cb1){
      protected ClassB removeRedirected(ClassB cb, List<CsPath> mapPath) {return cb;}
    }.redirect(p,path1,path2);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{new RedirectObj(cb1).redirect(p,path1,path2);fail("error expected");}
    catch(ClassUnfit err){
      String txt=err.getClass().getSimpleName()+"::"+err.getMessage();
      assertEquals(_expected,txt);
      }
    catch(IncoherentMapping err){
      String txt=err.getClass().getSimpleName()+"::"+err.getMessage();
      assertEquals(_expected,txt);
      }
    catch(MethodClash err){
      String txt=err.getClass().getSimpleName()+"::"+err.getMessage();
      assertEquals(_expected,txt);
      }
    catch(PathUnfit err){
      String txt=err.getClass().getSimpleName()+"::"+err.getMessage();
      assertEquals(_expected,txt);
      }
    }
  }
}