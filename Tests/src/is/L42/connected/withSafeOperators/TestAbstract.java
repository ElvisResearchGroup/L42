package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.fail;
import helpers.TestHelper;

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
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

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
  },{"{ method Void m(Any x) void}","Outer0","m(x)","{ method Void m(Any x)}",false
  },{"{C:{B:{ method Void m(Any x) void}}}","C::B","m(x)","{C:{B:{ method Void m(Any x)}}}",false
  },{"{ method Void m()}","Outer0","m()","{ method Void m()}",false
  },{"{B:{ method Void m(Any x) void}}", "C", "m(x)",
    "{Kind:{'@stringU\n'MemberUnavailable\n}"+
    "Path:{'@::C\n}"+
    "Selector:{'@stringU\n'm(x)\n}"+
    "InvalidKind:{'@stringU\n'NonExistentPath\n}"+
    "IsPrivate:{'@stringU\n'false\n}}",
	  true
  },{"{B:{ method Void m(Any x) void}}", "B", "k()",
    "{Kind:{'@stringU\n'MemberUnavailable\n}"+
	  "Path:{'@::B\n}"+
	  "Selector:{'@stringU\n'k()\n}"+
    "InvalidKind:{'@stringU\n'NonExistentMethod\n}"+
    "IsPrivate:{'@stringU\n'false\n}}",
	  true
  },{"{B:{ method Void m(Any x) void}}", "B", "m()",
    "{Kind:{'@stringU\n'MemberUnavailable\n}"+
	  "Path:{'@::B\n}"+
	  "Selector:{'@stringU\n'm()\n}"+
    "InvalidKind:{'@stringU\n'NonExistentMethod\n}"+
    "IsPrivate:{'@stringU\n'false\n}}",
	  true
}});}
@Test  public void test() {
  ClassB cb1=getClassB(_cb1);
  Path path=Path.parse(_path);
  MethodSelector ms=MethodSelector.parse(_ms);
  assert ms!=null;
  ClassB expected=getClassB(_expected);
  if(!isError){
    //TODO: mettere tests per il caso con un selettore destinazione. In particolare testare interfacce
    ClassB res=Abstract.toAbstract(Program.empty(),cb1, path.getCBar(), ms,null);
    res=Functions.clearCache(res,Stage.None);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{Abstract.toAbstract(Program.empty(),cb1, path.getCBar(), ms,null);fail("error expected");}
    catch(Resources.Error err){
      ClassB res=(ClassB)err.unbox;
      TestHelper.assertEqualExp(expected,res);
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
      "{B:{ method Void m() void}}","B","m()","k()","{B:{ method Void m() method Void k() void}}",false
  },{lineNumber(),//
    "{B:{ method Void m(Any x) }}","B","m(x)","k(x)","{B:{ method Void m(Any x) method Void k(Any x)}}",false
  },{lineNumber(),//
    "{ method Void m(Any x) void}","Outer0","m(x)","k(x)","{ method Void m(Any x) method Void k(Any x) void}",false
  },{lineNumber(),//
    "{C:{B:{ method Void m(Any x) }}}","C::B","m(x)","k(x)","{C:{B:{ method Void m(Any x) method Void k(Any x)}}}",false
  },{
    lineNumber(),//
    "{ method Void m()}","Outer0","m()","k(x)",
    "    {Kind:{'@stringU\n"+
        "      'MethodClash\n"+
        "    }Path:{'@::\n"+
        "    }Left:{'@stringU\n"+
        "      'method Void m()\n"+
        "    }Right:{'@stringU\n"+
        "      'method Void k(Void x)\n"+
        "    }LeftKind:{'@stringU\n"+
        "      'AbstractMethod\n"+
        "    }RightKind:{'@stringU\n"+
        "      'AbstractMethod\n"+
        "    }DifferentParameters:{'@stringU\n"+
        "      '[0]\n"+
        "    }DifferentReturnType:{'@stringU\n"+
        "      'false\n"+
        "    }DifferentThisMdf:{'@stringU\n"+
        "      'false\n"+
        "    }IncompatibleException:{'@stringU\n"+
        "      'false\n"+
            "}}"
    ,true
}});}
@Test  public void test() {
  TestHelper.configureForTest();
  ClassB cb1=getClassB(_cb1);
  Path path=Path.parse(_path);
  MethodSelector ms1=MethodSelector.parse(_ms1);
  assert ms1!=null;
  MethodSelector ms2=MethodSelector.parse(_ms2);
  assert ms2!=null;
  ClassB expected=getClassB(_expected);
  if(!isError){
    //TODO: mettere tests per il caso con un selettore destinazione. In particolare testare interfacce
    ClassB res=Abstract.toAbstract(Program.empty(),cb1, path.getCBar(), ms1,ms2);
    res=Functions.clearCache(res,Stage.None);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{Abstract.toAbstract(Program.empty(),cb1, path.getCBar(), ms1,ms2);fail("error expected");}
    catch(Resources.Error err){
      ClassB res=(ClassB)err.unbox;
      TestHelper.assertEqualExp(expected,res);
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
    "{B:{ method '@private\n Void m() void}}","B","{B:{}}",false
  },{lineNumber(),//
    "{B:{ method Void m(Any x) void}}","B","{B:{ method Void m(Any x)}}",false
  },{lineNumber(),//
    "{ method Void m(Any x) void}","Outer0","{ method Void m(Any x)}",false
  },{lineNumber(),//
    "{B:{ method '@private\n Void m() void method Void n() void}}","B","{B:{ method Void n()}}",false
  },{lineNumber(),//
    "{ method '@private\n Void m(Any x) void}","Outer0","{}",false
  },{lineNumber(),//
    "{C:{B:{ method Void m(Any x) void}}}","C::B","{C:{B:{ method Void m(Any x)}}}",false
  },{lineNumber(),//
    "{C:{B:{ method '@private\n  Void m(Any x) void}}}","C::B","{C:{B:{}}}",false
  },{lineNumber(),//
    "{C:{B:{ method Void m(Any x) void  method '@private\nVoid foo() void }}}",
	  "C::B",
	  "{C:{B:{ method Void m(Any x)}}}",
	  false

  },{lineNumber(),//
    "{B:{interface method Void m()}}","B","{B:{interface method Void m()}}",false
  },{lineNumber(),//
    "{B:{interface method Void m() void}}","B","{B:{interface method Void m()}}",false
  },{lineNumber(),//
    "{C:{B:{ A:'@private\n{} }}}","C","{C:{B:{}}}",false
  },{lineNumber(),//
    "{C:{B:{ A:'@private\n{} }}}","C::B","{C:{B:{}}}",false
  },{lineNumber(),//
    "{C:{B:'@private\n{}}}","C","{C:{}}",false
  },{lineNumber(),//
    "{C:{B:'@private\n{} D:{}}}","C","{C:{D:{}}}",false
  },{lineNumber(),//
    "{C:{B:'@private\n{} D:{E:'@private\n{} }}}","C::D","{C:{B:'@private\n{} D:{}}}",false
  },{lineNumber(),//
    "{C:{ method '@private\n Void m() void D:{E:'@private\n{} }}}",
	  "C::D",
	  "{C:{ method '@private\n Void m() void D:{}}}",
	  false
  },{lineNumber(),//
    "{C:{ method '@private\n Void m() void D:{method '@private\n Void m() void}}}",
	  "C::D",
	  "{C:{ method '@private\n Void m() void D:{}}}",
	  false
  },{lineNumber(),//
    "{C:{ method Void m() D.m() D:{ type method Void m() void}}}",
    "C::D",
    "{C:{ method Void m() D.m() D:{ type method Void m() }}}",
    false
  },{lineNumber(),//
    "{C:{ method Void m() A::D.m() A:{D:{ type method Void m() void}}}}",
    "C::A::D",
    "{C:{ method Void m() A::D.m() A:{D:{ type method Void m() }}}}",
    false
  },{lineNumber(),//
    "{C:{ method Void m() A::D.m()} A:{D:{ type method Void m() void type method'@private\n Void k() void}}}",
    "A::D",
    "{C:{ method Void m() A::D.m()} A:{D:{ type method Void m() }}}",
    false
  },{lineNumber(),//
    "{C:{ B:'@private\n{}}}","C::B",
    "{Kind:{'@stringU\n'MemberUnavailable\n}"+
    "Path:{'@::C::B\n}"+
    "Selector:{'@stringU\n'\n}"+
    "InvalidKind:{'@stringU\n'PrivatePath\n}"+
    "IsPrivate:{'@stringU\n'true\n}}",
	  true

  },{lineNumber(),//
    "{C:{}}","C::B",
    "{Kind:{'@stringU\n'MemberUnavailable\n}"+
    "Path:{'@::C::B\n}"+
    "Selector:{'@stringU\n'\n}"+
    "InvalidKind:{'@stringU\n'NonExistentPath\n}"+
    "IsPrivate:{'@stringU\n'false\n}}",
	  true
  },{lineNumber(),//
    "{C:{}}","B",
    "{Kind:{'@stringU\n'MemberUnavailable\n}"+
    "Path:{'@::B\n}"+
    "Selector:{'@stringU\n'\n}"+
    "InvalidKind:{'@stringU\n'NonExistentPath\n}"+
    "IsPrivate:{'@stringU\n'false\n}}",
	  true

  },{lineNumber(),//
    "{C:{B:{ method Void m(Any x) void  method '@private\nVoid foo() void } D:{ method Void bar() B.foo() }}}",
	  "C::B",
	  "{Kind:{'@stringU\n'PrivacyCoupuled\n}"+
	  "CoupuledPath:{'@stringU\n'[]\n}"+
	  "CoupuledMethods:{'@stringU\n'"
	  //+ "[Outer2::C::B.foo()]\n}}",the user or the used??
	  +"[Outer0::C::D.bar()]\n}}",
	  true
  },{lineNumber(),//
    "{C:{B:'@private\n{} D:{ method B bar() void }}}",
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
    res=Functions.clearCache(res,Stage.None);
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





