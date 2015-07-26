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
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;

public class TestAbstract{
@RunWith(Parameterized.class)
public static class TestAbstractMeth {//add more test for error cases
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
  },{"{ method Void m(Any x) void}","Outer0","m(x)","{ method Void m(Any x)}",false
  },{"{C:{B:{ method Void m(Any x) void}}}","C::B","m(x)","{C:{B:{ method Void m(Any x)}}}",false
  },{"{ method Void m()}","Outer0","m()","{ method Void m()}",false
  },{"{B:{ method Void m(Any x) void}}", "C", "m(x)",
    "{Kind:{'@stringU\n'TargetUnavailable\n}"+
    "Path:{'@stringU\n'Outer0::C\n}"+
    "Selector:{'@stringU\n'm(x)\n}"+
    "InvalidKind:{'@stringU\n'InexistentPath\n}}",
	  true
  },{"{B:{ method Void m(Any x) void}}", "B", "k()",
    "{Kind:{'@stringU\n'TargetUnavailable\n}"+
	  "Path:{'@stringU\n'Outer0::B\n}"+
	  "Selector:{'@stringU\n'k()\n}"+
    "InvalidKind:{'@stringU\n'InexistentMethod\n}}",
	  true
  },{"{B:{ method Void m(Any x) void}}", "B", "m()",
    "{Kind:{'@stringU\n'TargetUnavailable\n}"+
	  "Path:{'@stringU\n'Outer0::B\n}"+
	  "Selector:{'@stringU\n'm()\n}"+
    "InvalidKind:{'@stringU\n'InexistentMethod\n}}",
	  true
}});}
@Test  public void test() {
  ClassB cb1=getClassB(_cb1);
  Path path=Path.parse(_path);
  MethodSelector ms=MethodSelector.parse(_ms);
  assert ms!=null;
  ClassB expected=getClassB(_expected);
  if(!isError){
    ClassB res=Abstract.toAbstract(cb1, path.getCBar(), ms);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{Abstract.toAbstract(cb1, path.getCBar(), ms);fail("error expected");}
    catch(Resources.Error err){
      ClassB res=(ClassB)err.unbox;
      TestHelper.assertEqualExp(expected,res);
    }
  }
}
}

@RunWith(Parameterized.class)
public static class TestAbstractClass {//add more test for error cases
  @Parameter(0) public String _cb1;
  @Parameter(1) public String _path;
  @Parameter(2) public String _expected;
  @Parameter(3) public boolean isError;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {"{B:{ method Void m() void}}","B","{B:{ method Void m()}}",false
  },{"{B:{ method '@private\n Void m() void}}","B","{B:{}}",false
  },{"{B:{ method Void m(Any x) void}}","B","{B:{ method Void m(Any x)}}",false
  },{"{ method Void m(Any x) void}","Outer0","{ method Void m(Any x)}",false
  },{"{B:{ method '@private\n Void m() void method Void n() void}}","B","{B:{ method Void n()}}",false
  },{"{ method '@private\n Void m(Any x) void}","Outer0","{}",false
  },{"{C:{B:{ method Void m(Any x) void}}}","C::B","{C:{B:{ method Void m(Any x)}}}",false
  },{"{C:{B:{ method '@private\n  Void m(Any x) void}}}","C::B","{C:{B:{}}}",false
  },{"{C:{B:{ method Void m(Any x) void  method '@private\nVoid foo() void }}}",
	  "C::B",
	  "{C:{B:{ method Void m(Any x)}}}",
	  false

  },{"{B:{interface method Void m()}}","B","{B:{interface method Void m()}}",false
  },{"{B:{interface method Void m() void}}","B","{B:{interface method Void m()}}",false
  },{"{C:{B:{ A:'@private\n{} }}}","C","{C:{B:{}}}",false
  },{"{C:{B:{ A:'@private\n{} }}}","C::B","{C:{B:{}}}",false
  },{"{C:{B:'@private\n{}}}","C","{C:{}}",false
  },{"{C:{B:'@private\n{} D:{}}}","C","{C:{D:{}}}",false
  },{"{C:{B:'@private\n{} D:{E:'@private\n{} }}}","C::D","{C:{B:'@private\n{} D:{}}}",false
  },{"{C:{ method '@private\n Void m() void D:{E:'@private\n{} }}}",
	  "C::D",
	  "{C:{ method '@private\n Void m() void D:{}}}",
	  false
  },{"{C:{ method '@private\n Void m() void D:{method '@private\n Void m() void}}}",
	  "C::D",
	  "{C:{ method '@private\n Void m() void D:{}}}",
	  false

  },{"{C:{ B:'@private\n{}}}","C::B",
    "{Kind:{'@stringU\n'TargetUnavailable\n}"+
    "Path:{'@stringU\n'Outer0::C::B\n}"+
    "Selector:{'@stringU\n'\n}"+
    "InvalidKind:{'@stringU\n'PrivatePath\n}}",
	  true

  },{"{C:{}}","C::B",
    "{Kind:{'@stringU\n'TargetUnavailable\n}"+
    "Path:{'@stringU\n'Outer0::C::B\n}"+
    "Selector:{'@stringU\n'\n}"+
    "InvalidKind:{'@stringU\n'InexistentPath\n}}",
	  true
  },{"{C:{}}","B",
    "{Kind:{'@stringU\n'TargetUnavailable\n}"+
    "Path:{'@stringU\n'Outer0::B\n}"+
    "Selector:{'@stringU\n'\n}"+
    "InvalidKind:{'@stringU\n'InexistentPath\n}}",
	  true

  },{"{C:{B:{ method Void m(Any x) void  method '@private\nVoid foo() void } D:{ method Void bar() B.foo() }}}",
	  "C::B",
	  "{Kind:{'@stringU\n'PrivacyCoupuled\n}"+
	  "CoupuledPath:{'@stringU\n'[]\n}"+
	  "CoupuledMethods:{'@stringU\n'[Outer2::C::B.foo()]\n}}",
	  true
  },{"{C:{B:'@private\n{} D:{ method B bar() void }}}",
	  "C",
	  "{Kind:{'@stringU\n'PrivacyCoupuled\n}"+
	   "CoupuledPath:{'@stringU\n'[Outer0::C::B]\n}"+
	   "CoupuledMethods:{'@stringU\n'[]\n}}",
	   true
}});}
@Test  public void test() {
  TestHelper.configureForTest();
  ClassB cb1=getClassB(_cb1);
  Path path=Path.parse(_path);
  ClassB expected=getClassB(_expected);
  if(!isError){
    ClassB res=Abstract.toAbstract(cb1, path.getCBar());
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{Abstract.toAbstract(cb1, path.getCBar());fail("error expected");}
    catch(Resources.Error err){
      ClassB res=(ClassB)err.unbox;
      TestHelper.assertEqualExp(expected,res);
    }
  }
}
}


}





