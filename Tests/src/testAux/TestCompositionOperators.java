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
	    @Parameter(0) public String e1;
	    @Parameter(1) public String e2;
	    @Parameter(2) public String e3;
	    @Parameterized.Parameters
      public static List<String[]> createData() {
        return Arrays.asList(new String[][] {
//fails correctly?      {"{}","{ A:{(B that)} B:{}}","{}"
      {"{A:{}}","{ A:{'@B\n} B:{}}","{B:{}}"
    },{"{A:{ type method type A m() A}}","{ A:{'@B\n} B:{}}","{B:{type method type B m() B}}"
    },{"{A:{ type method type A m() {return A}}}","{ A:{'@B\n} B:{}}","{B:{type method type B m() {return B}}}"
    },{"{A:{ method A ()} B:{foo()}}",
      "{ A:{'@B\n} B:{}}",
      "{B:{ type method Outer0 foo()  method B ()}}"//TODO: is it the expected outcome ordering?, same for the next 3
    },{"{C:{A:{ method A ()}} B:{foo()}}",
      "{ C:{A:{'@B\n}} B:{}}",
      "{C:{} B:{ type method Outer0 foo() method B ()  }}"
    },{"{D:{C:{A:{ method A ()}}} B:{foo()}}",
      "{ D:{C:{A:{'@B\n}}} B:{}}",
      "{D:{C:{}} B:{ type method Outer0 foo()  method B ()}}"
    },{"{A:{ method A ()} C:{B:{foo()}}}",
      "{ A:{'@C::B\n} C:{B:{}}}",
      "{C:{B:{ type method Outer0 foo() method C::B () }}}"
    },{"{A:{ method A ()} D:{C:{B:{foo()}}}}",
      "{ A:{'@D::C::B\n} D:{C:{B:{}}}}",
      "{D:{C:{B:{  type method Outer0 foo()  method D::C::B ()}}}}"

    },{"{A:{}}","{ A:{'@Outer2::Ext\n} }","{}"
    },{"{A:{} method A(A a) a}","{ A:{'@Outer2::Ext\n} }","{ method Outer1::Ext(Outer1::Ext a) a}"
    },{"{A:{} B:{method A(A a) a}}","{ A:{'@Outer2::Ext\n} }","{B:{method Outer2::Ext(Outer2::Ext a) a}}"
    },{"{A:{} C:{B:{method A(A a) a}}}","{ A:{'@Outer2::Ext\n} }","{C:{B:{method Outer3::Ext(Outer3::Ext a) a}}}"
    },{"{B:{A:{}} method B::A(B::A a) a}","{ B:{A:{'@Outer3::Ext\n}} }","{B:{} method Outer1::Ext(Outer1::Ext a) a}"
    //umm...},{"{B:{A:{}} method B::A(B::A a) a}","{ B:{(Outer2::Ext that)} }","{ method Outer1::Ext::A(Outer1::Ext::A a) a}"
    },{"{A:{ method A(A a) a } }", "{ A:{ method Void (Void a) this.foo(b:a) method Void foo(Void b)} }",   "{A:{ method A foo(A b) b } }"
    },{"{A:{ method A(A a) a method A foo(A b) A()} }",
      "{ A:{ method Void (Void a) this.foo(b:a) method Void foo(Void b) this(a:b)} }",
      "{A:{ method A foo(A b) b  method A(A a) A()} }"
    },{"{A:{() method A(A a) a method A foo(A b) A()} }",
      "{ A:{ method Void (Void a) this.foo(b:a) method Void foo(Void b) this(a:b)} }",
      "{A:{() method A foo(A b) b method A (A a) A()} }"
    },{//
     "{ A:{ type method Outer1::B () } B:{ }}",
     "{ A:{'@Outer1\n}}",
     "{ B:{ } type method Outer0::B ()  }"
     },{
     "{ A:{ type method Outer1::B () } B:{ }}",
     "{ A:{'@Outer1::C\n}}",
     "{ B:{ }  C:{type method Outer1::B () }}"
     },{
     "{ A1:{ A2:{ type method B () }} B:{ }}",
     "{ A1:{A2:{'@Outer1\n}}}",
     "{ A1:{ type method B () } B:{ }}",
     },{
     "{ A1:{ A2:{ type method B () }} B:{ }}",
     "{ A1:{A2:{'@Outer2\n}}}",
     "{ A1:{ } B:{ } type method B () }",
     //Outer1::A1::B
     },{
     "{ A1:{ A2:{ type method B () } B:{ } }}",
     "{ A1:{A2:{'@Outer1\n}}}",
     "{ A1:{B:{ } type method B () }}",
     //Outer1::A1::B
     },{
    "{ A:{ D:{ method Outer0 d() Outer0.d() } type method Outer1::B foo ()Outer0.foo() } B:{ }}",
    "{ A:{'@Outer1\n}}",
    "{ B:{ } D:{ method Outer0 d() Outer0.d() } type method Outer0::B foo ()Outer0.foo()  }"
},{helpers.TestHelper.multiLine(""
,"{ A:{"
,"  OptMax:{"
,"    TOpt:{interface}"
,"    TEmpty:{<:Outer1::TOpt}"
,"    }"
,"  }}"),
"{ A:{'@Outer1\n}}",
helpers.TestHelper.multiLine(""
,"{"
,"OptMax:{"
,"  TOpt:{interface }"
,"  TEmpty:{<:Outer1::TOpt}"
,"}}")
},{helpers.TestHelper.multiLine(""
,"{ A:{mut(var mut Cell head)"
,"  Cell:{interface}"
,"  CellEnd:{<:Cell}"
//,"  read method lent Iterator vals() Iterator(this)"
//,"  Iterator:{lent (read Outer1 that)}"
,"  }}"),
"{ A:{'@Outer1\n }}",//"{ A:{'@Outer1\n Cell:{'@Outer2::PCell\n}}}",//invalid for now//TODO:
helpers.TestHelper.multiLine(""
,"{"
,"type method "
,"mut Outer0 #apply(mut Outer0::Cell^head'@consistent"
,") "
,"mut method '@consistent"
,"Void head(mut Outer0::Cell that)"
,"mut method '@consistent"
,"mut Outer0::Cell #head()"
,"read method '@consistent"
,"read Outer0::Cell head()"
,"Cell:{interface }"
,"CellEnd:{<:Outer1::Cell}"
,"}")


},{
 "{ A:{type method Outer1::B ()}  B:{ }}",
 "{ A:{'@Outer0::C\n C:{} }}",
 "{ B:{} A:{ C:{type method Outer2::B #apply() }}}"

},{
  "{ A:{type method Outer0::B ()  B:{ }}}",
  "{ A:{'@Outer0::C\n C:{} }}",
  "{ A:{ C:{type method Outer0::B #apply() B:{} }}}"

},{
  "{ type method Outer0::B ()  B:{ }}",
  "{ '@Outer0::C\n C:{} }",
  "{ C:{ type method Outer0::B #apply() B:{}}}"

  
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
      "m"
  },{"{ A:{} method A m(A a)exception A  }",0,"Outer0",
      "m(a)"
  },{"{ A:{} method A m(A a,A b)exception A  }",0,"Outer0",
    "m(a,b)"
  },{"{ A:{} method A (A a,A b)exception A  }",0,"Outer0",
    "#apply(a,b)"
  },{"{ A:{} method A ()exception A  }",0,"Outer0",
    "#apply"
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
  public static ClassB getClassB(String e1) {
    return (ClassB)Desugar.of(Parser.parse(null," "+e1)).accept(new InjectionOnCore());
  }
  public static Program getProgram() {
    ClassB empty=(ClassB)Parser.parse(null," {Ext:{}}").accept(new InjectionOnCore());
    Program p=Program.empty().addAtTop(empty);
    return p;
  }
}
