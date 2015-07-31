package testAux;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import introspection.IntrospectionAdapt;
import introspection.IntrospectionSum;
import is.L42.connected.withItself.Plugin;
import helpers.TestHelper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Resources.Revertable;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.*;
import auxiliaryGrammar.Program;
import coreVisitors.ExtractThrow;
import facade.Configuration;
import facade.Parser;
import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
public class TestCompositionOperators {
	@RunWith(Parameterized.class)
  public static class TestSum {
    @Parameter(0) public String e1;
    @Parameter(1) public String e2;
    @Parameter(2) public String e3;
    @Parameterized.Parameters
    public static List<String[]> createData() {
      return Arrays.asList(new String[][] {
      {"{}","{}","{}"
    },{"{()}","{}","{()}"
    },{"{}","{()}","{()}"
    },{"{()A:{()}}","{B:{()}}","{() A:{()} B:{()}}"
    },{"{() method Void a() void }","{ method Void a() }","{() method Void a() void }"
    },{"{ method Void a() }","{ method Void b() }","{ method Void a()  method Void b() }"


  }});}

  @Test
  public void test() {
    ClassB cb1=getClassB(e1);
    ClassB cb2=getClassB(e2);
    ClassB cb3=getClassB(e3);
    ClassB res=(ClassB)IntrospectionSum.sum(cb1,cb2,Path.outer(0));
    TestHelper.assertEqualExp(res,cb3);
    }
  }
	@RunWith(Parameterized.class)
  public static class TestAdapt {
	    @Parameter(0) public int _lineNumber;
	    @Parameter(1) public String e1;
	    @Parameter(2) public String e2;
	    @Parameter(3) public String e3;
	    @Parameters(name = "{index}: line {0}")
      public static List<Object[]> createData() {
        return Arrays.asList(new Object[][] {
//fails correctly?      {"{}","{ A:{(B that)} B:{}}","{}"
      {lineNumber(),"{A:{}}","{ A:{'@B\n} B:{}}","{B:{}}"
    //A->B
    },{lineNumber(),"{A:{ type method type A m() A}}","{ A:{'@B\n} B:{}}","{B:{type method type B m() B}}"
    },{lineNumber(),"{A:{ type method type A m() {return A}}}","{ A:{'@B\n} B:{}}","{B:{type method type B m() {return B}}}"
    },{lineNumber(),"{A:{ method A ()} B:{foo()}}",
      "{ A:{'@B\n} B:{}}",
      "{B:{ type method Outer0 foo()  method B ()}}"//TODO: is it the expected outcome ordering?, same for the next 3
    },{lineNumber(),"{C:{A:{ method A ()}} B:{foo()}}",
      "{ C:{A:{'@B\n}} B:{}}",
      "{C:{} B:{ type method Outer0 foo() method B ()  }}"
    },{lineNumber(),"{D:{C:{A:{ method A ()}}} B:{foo()}}",
      "{ D:{C:{A:{'@B\n}}} B:{}}",
      "{D:{C:{}} B:{ type method Outer0 foo()  method B ()}}"
    },{lineNumber(),"{A:{ method A ()} C:{B:{foo()}}}",
      "{ A:{'@C::B\n} C:{B:{}}}",
      "{C:{B:{ type method Outer0 foo() method C::B () }}}"
    },{lineNumber(),"{A:{ method A ()} D:{C:{B:{foo()}}}}",
      "{ A:{'@D::C::B\n} D:{C:{B:{}}}}",
      "{D:{C:{B:{  type method Outer0 foo()  method D::C::B ()}}}}"

    },{lineNumber(),"{A:{}}","{ A:{'@Outer2::Ext\n} }","{}"
    },{lineNumber(),"{A:{} method A(A a) a}","{ A:{'@Outer2::Ext\n} }","{ method Outer1::Ext(Outer1::Ext a) a}"
    },{lineNumber(),"{A:{} B:{method A(A a) a}}","{ A:{'@Outer2::Ext\n} }","{B:{method Outer2::Ext(Outer2::Ext a) a}}"
    },{lineNumber(),"{A:{} C:{B:{method A(A a) a}}}","{ A:{'@Outer2::Ext\n} }","{C:{B:{method Outer3::Ext(Outer3::Ext a) a}}}"
    },{lineNumber(),"{B:{A:{}} method B::A(B::A a) a}","{ B:{A:{'@Outer3::Ext\n}} }","{B:{} method Outer1::Ext(Outer1::Ext a) a}"
    //umm...},{"{B:{A:{}} method B::A(B::A a) a}","{ B:{(Outer2::Ext that)} }","{ method Outer1::Ext::A(Outer1::Ext::A a) a}"
    },{lineNumber(),"{A:{ method A(A a) a } }", "{ A:{ method Void (Void a) this.foo(b:a) method Void foo(Void b)} }",   "{A:{ method A foo(A b) b } }"
    },{lineNumber(),"{A:{ method A(A a) a method A foo(A b) A()} }",
      "{ A:{ method Void (Void a) this.foo(b:a) method Void foo(Void b) this(a:b)} }",
      "{A:{ method A foo(A b) b  method A(A a) A()} }"
    },{lineNumber(),"{A:{() method A(A a) a method A foo(A b) A()} }",
      "{ A:{ method Void (Void a) this.foo(b:a) method Void foo(Void b) this(a:b)} }",
      "{A:{() method A foo(A b) b method A (A a) A()} }"
     },{lineNumber(),
     "{ A:{ type method Outer1::B () } B:{ }}",
     "{ A:{'@Outer1::C\n}}",
     "{ B:{ }  C:{type method Outer1::B () }}"

}});}
  @Test
  public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(e1);
    ClassB cb2=getClassB(e2);
    ClassB cb3=getClassB(e3);
    ClassB res=(ClassB)IntrospectionAdapt.adapt(getProgram(),cb1,cb2);
    //ClassB ct= Configuration.typeSystem.typeExtraction(Program.empty(),res);
    TestHelper.assertEqualExp(res,cb3);
    }
  }

  //----------------------------------------------------------
  @RunWith(Parameterized.class)
  public static class Test_MsumComment£xthat£xcomment£xadapter {
    @Parameter(0) public String that;
    @Parameter(1) public String comment;
    @Parameter(2) public String adapter;
    @Parameter(3) public String expected;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
    {"{ A:{}}","{'fuffa\n}","{A:{} %o_0%:{'@Outer1::A\n}}","{ A:'fuffa\n{}}"
  },{"{ A:{B:{}}}","{'@private\n}","{A:{B:{}} %o_0%:{'@Outer1::A::B\n}}","{ A:{B:'@private\n{}}}"
  },{"{ A:{B:{}}}","{'@private\n}","{A:{B:{}} %o_0%:{'@Outer1::A::B\n}}","{ A:{B:'@private\n{}}}"
//boh, I think it can work only on comments with zero paths?
//},{"{ A:{B:{}}}","{'@Outer1::N\n}","{A:{B:{}} %o_0%:{'@Outer1::A::B\n}}","{ A:{B:'@private\n{}}}"
  },{"{ A:{method Void foo(Any a)}}","{'fuffa\n}",
      "{A:{method Void #o_0#(Void _0) this.foo(a:_0)  method Void foo(Void a)}}",
      "{ A:{method'fuffa\n Void foo(Any a)}}"
  },{"{ method Void foo(Any a) void}","{'fuffa\n}",
      "{method Void #o_0#(Void _0) this.foo(a:_0)  method Void foo(Void a)}",
      "{ method'fuffa\n Void foo(Any a)void }"

    }});}
  @Test public void test() {
    TestHelper.configureForTest();
    ClassB _that=getClassB(that);
    ClassB _comment=getClassB(comment);
    ClassB _adapter=getClassB(adapter);
    ClassB _expected=getClassB(expected);
    ClassB cb=(ClassB)getWI(wi->wi.MsumComment£xthat£xcomment£xadapter(_that, _comment, _adapter));
    TestHelper.assertEqualExp(_expected,cb);
    }
  }

  //----------------------------------------------------------
	@RunWith(Parameterized.class)
  public static class Test_Mget£that£node {
    @Parameter(0) public String e1;
    @Parameter(1) public String e2;
    @Parameter(2) public String e3;
    @Parameterized.Parameters
    public static List<String[]> createData() {
      return Arrays.asList(new String[][] {
    {"{A:{}}","Outer0","{\nA:{}}"
  },{"{A:{}}","A","{}"
    }});}

  @Test
  public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(e1);
    String res=(String)getWI(wi->wi.Mget£xthat£xnode(cb1,e2));
    Assert.assertEquals(res,e3);}}
  //----------------------------------------------------------
	@RunWith(Parameterized.class)
	public static class Test_MgetOrElse£that£interfaceNum£node {
	  @Parameter(0) public String e1;
    @Parameter(1) public int e2;
    @Parameter(2) public String e3;
    @Parameter(3) public String e4;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
    {"{<:Foo A:{} Foo:{interface}}",0,"Outer0","{ Foo:{} %o_0%:{'@Outer1::Foo\n}}"
  },{"{A:{<:Bar} Bar:{interface}}",0,"A","{Bar:{} %o_0%:{'@Outer1::Bar\n}}"
    }});}
  @Test
  public void test() {
    ClassB cb1=getClassB(e1);
    ClassB cb4=getClassB(e4);
    ClassB res=(ClassB)getWI(wi->wi.MgetOrElse£xthat£xinterfaceNum£xnode(cb1,e2,e3));
    TestHelper.assertEqualExp(res,cb4);}}
  //----------------------------------------------------------
	@RunWith(Parameterized.class)
	public static class Test_MgetOrElse£that£methodNum£exceptionNum£node {
	  @Parameter(0) public String e1;
    @Parameter(1) public int e2;
    @Parameter(2) public int e3;
    @Parameter(3) public String e4;
    @Parameter(4) public String _expected;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
    {"{ A:{} method A m()exception A }",0,0,"Outer0",
      "{A:{}%o_0%:{ '@Outer1::A\n}}"
  },{"{ A:{} B:{} method A m()exception A method A ()exception A A B}",1,2,"Outer0",
    "{B:{}%o_0%:{ '@Outer1::B\n}}"
  //ok under: one extra method generated in ct as constructor
  },{"{ A:{} B:{} C:{foo() method A m()exception A method A ()exception A A B}}",2,2,"C",
    "{B:{}%o_0%:{ '@Outer1::B\n}}"
    }});}
  @Test public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(e1);
    ClassB expected=getClassB(_expected);
    ClassB res=(ClassB)getWI(wi->wi.MgetOrElse£xthat£xmethodNum£xexceptionNum£xnode(cb1,e2,e3,e4));
    TestHelper.assertEqualExp(res,expected);}}

  //----------------------------------------------------------
	@RunWith(Parameterized.class)
	public static class Test_MnameToAdapter£that {
      @Parameter(0) public String e1;
	    @Parameter(1) public String _expected;
	    @Parameterized.Parameters
	    public static List<Object[]> createData() {
	      return Arrays.asList(new Object[][] {

    {"foo(a,b)",
     "{%o_0%:{method Void #o_0#(Void _0, Void _1) this.foo(a:_0, b:_1)\n"
   + "        method Void foo(Void a, Void b)}}"
  },{"()","{%o_0%:{method Void #o_0#() this()  method Void #apply() }}"
  },{"A","{A:{} %o_0%:{'@Outer1::A\n}}"
  },{"A::B","{A:{B:{}} %o_0%:{'@Outer1::A::B\n}}"
  },{"Outer0","{ %o_0%:{'@Outer1\n}}"
  //ok it is invalid},{"Outer0::A","{ %o_0%:{ #apply(Outer1 that)}}"
    }});}
  @Test public void test() {
    ClassB expected=getClassB(_expected);
    ClassB res=(ClassB)getWI(wi->wi.MnameToAdapter£xthat(e1));
    TestHelper.assertEqualExp(res,expected);}}




  //----------------------------------------------------------
	@RunWith(Parameterized.class)
	public static class Test_MtypeNameToAdapter£xthat {
    @Parameter(0) public Ast.Path e1;
    @Parameter(1) public String _expected;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {

    {Path.parse("Outer0::Ext"),"{%o_0%:{'@Outer2::Ext\n}}"
//  },{Path.parse("Outer2"),"{%o_0%:{ #apply(Outer4 that)}}"
    }});}
	@Test public void test() {
    ClassB expected=getClassB(_expected);
    ClassB res=(ClassB)getWI(wi->wi.MtypeNameToAdapter£xthat(e1));
    TestHelper.assertEqualExp(res,expected);}}
  //----------------------------------------------------------
	@RunWith(Parameterized.class)
	public static class Test_MgetOrElse£xthat£xmethodNum£xnode {
	  @Parameter(0) public String e1;
    @Parameter(1) public int e2;
    @Parameter(2) public String e3;
    @Parameter(3) public String _expected;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
    {"{ A:{} method A m()exception A this.m() }",0,"Outer0",
      "method \nOuter0::A m() exception Outer0::A this.m()"
    }});}
  @Test public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(e1);
    String res=(String)getWI(wi->wi.MgetOrElse£xthat£xmethodNum£xnode(cb1,e2,e3));
    Assert.assertEquals(res.trim(),_expected);}}
  //----------------------------------------------------------
	@RunWith(Parameterized.class)
	public static class Test_MgetNameOrElse£xthat£xmethodNum£xnode {
	  @Parameter(0) public String e1;
    @Parameter(1) public int e2;
    @Parameter(2) public String e3;
    @Parameter(3) public String _expected;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
    {"{ A:{} method A m()exception A this.m() }",0,"Outer0",
      "m()"
  },{"{ A:{} method A m(A a)exception A  }",0,"Outer0",
      "m(a)"
  },{"{ A:{} method A m(A a,A b)exception A  }",0,"Outer0",
    "m(a,b)"
  },{"{ A:{} method A (A a,A b)exception A  }",0,"Outer0",
    "#apply(a,b)"
  },{"{ A:{} method A ()exception A  }",0,"Outer0",
    "#apply()"
    }});}
  @Test public void test() {
    ClassB cb1=getClassB(e1);
    String res=(String)getWI(wi->wi.MgetNameOrElse£xthat£xmethodNum£xnode(cb1,e2,e3));
    Assert.assertEquals(res,_expected);}}

    //----------------------------------------------------------
    @RunWith(Parameterized.class)
    public static class Test_MgetInternalAdapterPathOrElse£xthat {
    @Parameter(0) public String e1;
    @Parameter(1) public String expected;//can be null
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
    {"{A:{} %o_0%:{'@Outer1::A\n}}","A"
  },{"{A:{B:{}} %o_0%:{'@Outer1::A::B\n}}","A::B"
  },{"{ %o_0%:{'@Outer2::A::B\n}}",null
  },{"{ %o_0%:{'@Outer2\n}}",null
  },{"{ %o_0%:{'@Outer1\n}}","Outer0"
  },{"{ %o_0%:{'@Outer0\n}}",null

    }});}
  @Test public void test() {
    TestHelper.configureForTest();
    ClassB cb1=getClassB(e1);
    try{
      String s=(String)getWI(wi->wi.MgetInternalAdapterPathOrElse£xthat(cb1));
      Assert.assertNotNull(this.expected);
      Assert.assertEquals(this.expected,s);
      }
    catch(ErrorMessage.PluginActionUndefined isExternal){
      Assert.assertNull(this.expected);
      }
    }
    }

	//----------------------------------------------------------
	@RunWith(Parameterized.class)
	public static class Test_MgetTypePathOrElse£xthat£xmethodNum£xnode {
	  @Parameter(0) public String _lib;
    @Parameter(1) public int num;
    @Parameter(2) public String path;
    @Parameter(3) public String _expected;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {"{ A:{} method A m()exception A this.m() }",0,"Outer0",
        "{A:{}%o_0%:{'@Outer1::A\n}}"
    },{"{ A:{ method A m(A a)exception A  }}",0,"A",
      "{A:{}%o_0%:{'@Outer1::A\n}}"
    },{"{ B:{A:{ method B m()  }}}",0,"B::A",
      "{B:{}%o_0%:{'@Outer1::B\n}}"
    },{"{ B:{A:{ method A m(A a)exception A  }}}",0,"B::A",
      "{B:{A:{}}%o_0%:{ '@Outer1::B::A\n}}"
      }});}
  @Test public void test() {
    TestHelper.configureForTest();
    ClassB lib=getClassB(_lib);
    ClassB expected=getClassB(_expected);
    ClassB res=(ClassB)getWI(wi->wi.MgetTypePathOrElse£xthat£xmethodNum£xnode(lib,num,path));
    TestHelper.assertEqualExp(res,expected);}}
  //----------------------------------------------------------
	@RunWith(Parameterized.class)
	public static class Test_MgetOrElse£xthat£xmethodNum£xparameterNum£xnode {
	  @Parameter(0) public String _lib;
    @Parameter(1) public int num;
    @Parameter(2) public int pNum;
    @Parameter(3) public String path;
    @Parameter(4) public String _expected;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {"{ A:{} method A m( A a )this.m() }",0,0,"Outer0",
        "Outer0::A a"
      },{"{ A:{} method A m( A a A b'foo\n  'bar\n)this.m() }",0,1,"Outer0",
        "Outer0::A b'foo\n'bar\n"

      }});}
  @Test public void test() {
    TestHelper.configureForTest();
    ClassB lib=getClassB(_lib);
    String res=(String)getWI(wi->wi.MgetOrElse£xthat£xmethodNum£xparameterNum£xnode(lib,num,pNum,path));
    Assert.assertEquals(res,_expected);
    }}

  public static <T> T getWI(Function<Plugin,T> action) {
    Plugin w=new Plugin();
    //w.setProgram(getProgram());
    T res= Resources.withPDo(getProgram(), ()->action.apply(w));
    assert Resources.isValid(getProgram(), res, new Object[0]);
    return res;
  }
  public static Program getProgram() {
    ClassB empty=(ClassB)Parser.parse(null," {Ext:{}}").accept(new InjectionOnCore());
    Program p=Program.empty().addAtTop(empty,empty);
    return p;
  }
}
