package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.fail;
import helpers.TestHelper;
import is.L42.connected.withSafeOperators.Rename.UserForMethodResult;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import ast.Ast;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.Util.PathMx;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;
import facade.Configuration;

public class TestRename {
  @RunWith(Parameterized.class) public static class TestRenameMethod {//add more test for error cases
    @Parameter(0) public String _cb1;
    @Parameter(1) public String _path;
    @Parameter(2) public String _ms1;
    @Parameter(3) public String _ms2;
    @Parameter(4) public String _expected;
    @Parameter(5) public boolean isError;

    @Parameterized.Parameters public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] { { //
          "{B:{ method Void m() void}}", "B", "m()", "k()", "{B:{ method Void k() void}}", false //
          }, { //
          "{ method Void m() void}", "This0", "m()", "k()", "{ method Void k() void}", false //
          }, { //
          "{ method Void m() void method This0::m() mm() void}", "This0", "m()", "k()", "{ method Void k() void method This0::k() mm() void}", false //
          }, { //
            "{ method Void m() void method//@private\n Void mm() void}",
            "This0", "m()", "mm()",
            "{method Void mm() void"
            + " method //@private\n"
            + "Void mm__0_0() void}", false //
          }, { //
         "{ class method //@private\n"
            +" This0 a(Any n) \n"
            +"   mut method //@private\n"
            +"  Any #n() \n"
            +"  read method //@private\n"
            +"   Any n() \n"
            +"  class method\n"
            +"  This0 _1(This0 new)  new\n"
            +"  class method \n"
            +"  This0 a_1(Any n) This0._1(new:This0.a(n:n))}\n",
            "This0", "a_1(n)", "a(n)",
            "{class method //@private\n"
            +"This0 a__0_0(Any n__0_0)\n"
            +"mut method //@private\n"
            +"Any #n__0_0()\n"
            +"read method //@private\n"
            +"Any n__0_0()\n"
            +"class method\n"
            +"This0 _1(This0 new) new\n"
            +"class method\n"
            +"This0 a(Any n) This0._1(new:This0.a__0_0(n__0_0:n))}",false

          }, { //
          "{ method Void m() void}", "This0", "m()", "k(x)",
"{ Kind:{//@stringU\n"+
"//MethodClash\n"+
"}\n"+
"Path:{//@.\n"+
"}\n"+
"Left:{//@stringU\n"+
"//method Void m() void\n"+
"}\n"+
"Right:{//@stringU\n"+
"//method Void k(Void x) void\n"+
"}\n"+
"LeftKind:{//@stringU\n"+
"//ImplementedMethod\n"+
"}\n"+
"RightKind:{//@stringU\n"+
"//ImplementedMethod\n"+
"}\n"+
"DifferentParameters:{//@stringU\n"+
"//[0]\n"+
"}\n"+
"DifferentReturnType:{//@stringU\n"+
"//false\n"+
"}\n"+
"DifferentThisMdf:{//@stringU\n"+
"//false\n"+
"}\n"+
"IncompatibleException:{//@stringU\n"+
"//false\n"+
"}}",
          true //




          } });
    }

    @Test public void test() {
      TestHelper.configureForTest();
      ClassB cb1 = getClassB(_cb1);
      List<String> path=TestHelper.cs(_path);
      MethodSelector ms1 = MethodSelector.parse(_ms1);
      MethodSelector ms2 = MethodSelector.parse(_ms2);
      ClassB expected = getClassB(_expected);
      if (!isError) {
        ClassB res = Rename.renameMethod(Program.empty(), cb1, path, ms1, ms2);
        res=Functions.clearCache(res,Ast.Stage.None);
        TestHelper.assertEqualExp(expected, res);
      } else {
        try {
          Rename.renameMethod(Program.empty(), cb1, path, ms1, ms2);
          fail("error expected");
        } catch (Resources.Error err) {
          ClassB res = (ClassB) err.unbox;
          TestHelper.assertEqualExp(expected, res);
        }
      }
    }
  }
  @RunWith(Parameterized.class) public static class TestUserForMethod{//add more test for error cases
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String _cb1;
    @Parameter(2) public String _path;
    @Parameter(3) public String _ms1;
    @Parameter(4) public String expected1;
    @Parameter(5) public String expected2;
    @Parameter(6) public boolean isError;

    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] { { //
          lineNumber(),"{B:{ method Void m() void}}", "B", "m()", "[]","[]", false //
          }, { //
            lineNumber(),"{ method Void m()  this.m()}", "This0", "m()",
            "[]","[m()]", false //
          }, { //
            lineNumber(),"{ B:{method Void m()  this.m()} method Void mm(B b) b.m()}", "B", "m()",
            "[This0::mm(b)]","[m()]", false //
          }, { //
            lineNumber(),"{ B:{method Void m()  this.m() method B::m() k() void} method Void mm(B b) b.m()}", "B", "m()",
            "[This0::mm(b)]","[m()]", false //
          } });
    }

    @Test public void test() {
      TestHelper.configureForTest();
      ClassB cb1 = getClassB(_cb1);
      List<String> path=TestHelper.cs(_path);
      MethodSelector ms1 = MethodSelector.parse(_ms1);
      if (!isError) {
        UserForMethodResult res = Rename.userForMethod(Program.empty(), cb1, path, ms1,true);
        Assert.assertEquals(expected1, res.asClient.toString());
        Assert.assertEquals(expected2, res.asThis.toString());
      } else {
        try {
          Rename.userForMethod(Program.empty(), cb1, path, ms1,true);
          fail("error expected");
        } catch (Resources.Error err) {
          ClassB res = (ClassB) err.unbox;
          TestHelper.assertEqualExp(new ExpCore._void(), res);
        }
      }
    }
  }

  @RunWith(Parameterized.class) public static class TestRenameClassStrict {//add more test for error cases
    @Parameter(0) public String _cb1;
    @Parameter(1) public String _path1;
    @Parameter(2) public String _path2;
    @Parameter(3) public String _expected;
    @Parameter(4) public boolean isError;

    @Parameterized.Parameters public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] { {//
        "{B:{ method Void m() void}}", "B", "C", "{C:{ method Void m() void}}", false//
        }, {//
        "{B:{ method Void m() void}}", "B", "C.D", "{C:{ D:{method Void m() void}}}", false//
        },{//
        "{A:{interface method This0 m()  } B:{ implements A method m() this.m().m()}   User:{ method A mm(B b) b.m().m()}}","B","C",
        "{A:{interface method This0 m()  } User:{ method A mm(C b) b.m().m()}  C:{ implements A method m() this.m().m()} }",false
        },{//
          "{A:{interface method This0 m()  } B:{ implements A method m() this.m().m()}   User:{ method A mm(B b) b.m().m()} }","A","C",
          "{B:{ implements C method m() this.m().m()}  User:{ method C mm(B b) b.m().m()} C:{interface method This0 m()  }    }",false
        } });
    }

    @Test public void test() {
      TestHelper.configureForTest();
      ClassB cb1 = getClassB(_cb1);
      Configuration.typeSystem.computeStage(Program.empty(), cb1);
      List<String> path1=TestHelper.cs(_path1);
      List<String> path2=TestHelper.cs(_path2);
      ClassB expected = getClassB(_expected);
      if (!isError) {
        ClassB res = Rename.renameClassStrict(Program.empty(), cb1, path1,  path2);
        Configuration.typeSystem.checkCt(Program.empty(),res);
        res=Functions.clearCache(res,Ast.Stage.None);
        TestHelper.assertEqualExp(expected, res);
      } else {
        try {
          Rename.renameClassStrict(Program.empty(), cb1, path1, path2);
          fail("error expected");
        } catch (Resources.Error err) {
          ClassB res = (ClassB) err.unbox;
          TestHelper.assertEqualExp(expected, res);
        }
      }
    }
  }

  //-----
  @Test
  public void testToTop() {
    List<String> res = ClassOperations.toTop(Arrays.asList(),Path.parse("This0.A"));
    Assert.assertEquals(res,Arrays.asList("A"));
    res = ClassOperations.toTop(Arrays.asList(),Path.parse("This0.A.B"));
    Assert.assertEquals(res,Arrays.asList("A","B"));
    res = ClassOperations.toTop(Arrays.asList("C"),Path.parse("This1.A.B"));
    Assert.assertEquals(res,Arrays.asList("A","B"));
    res = ClassOperations.toTop(Arrays.asList("C"),Path.parse("This0.A.B"));
    Assert.assertEquals(res,Arrays.asList("C","A","B"));
  }
  @Test
  public void testNormalizePath() {
    Path res = ClassOperations.normalizePath(Arrays.asList(),0,Arrays.asList());
    Assert.assertEquals(res,Path.outer(0));
    res = ClassOperations.normalizePath(Arrays.asList("A"),1,Arrays.asList("B"));
    Assert.assertEquals(res,Path.parse("This1.B"));
    res = ClassOperations.normalizePath(Arrays.asList("B"),1,Arrays.asList("B"));
    Assert.assertEquals(res,Path.parse("This0"));

  }
  @RunWith(Parameterized.class) public static class TestRenameClass {//add more test for error cases
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String _cb1;
    @Parameter(2) public String _path1;
    @Parameter(3) public String _path2;
    @Parameter(4) public String _expected;
    @Parameter(5) public boolean isError;

    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] { {//
        lineNumber(),"{B:{ method Void m() void}}", "B", "C", "{C:{ method Void m() void}}", false//
        }, {lineNumber(),//
        "{B:{ method Void m() void}}", "B", "C.D", "{C:{ D:{method Void m() void}}}", false//
        }, {lineNumber(),//
        "{A:{}}","A","B","{B:{}}",false//
        }, {lineNumber(),//
          "{A:{ class method class A m() A}}","A","B", "{B:{class method class This0 m() This0}}"    ,false//
        }, {lineNumber(),//
          "{A:{ class method class A m() {return A}}}","A","B", "{B:{class method class This0 m() {return This0}}}"    ,false//
        }, {lineNumber(),//
          "{A:{ method A ()} B:{method Void foo()}}","A","B",  "{B:{ method Void  foo()  method This0 ()}}"   ,false//
        }, {lineNumber(),//
          "{C:{A:{ method A ()}} B:{method Void  foo()}}","C.A","B",  "{C:{} B:{ method Void  foo() method This0 ()  }}"    ,false//
        }, {lineNumber(),//
          "{D:{C:{A:{ method A ()}}} B:{method Void  foo()}}","D.C.A","B",  "{D:{C:{}} B:{ method Void  foo()  method This0 ()}}"   ,false//
        }, {lineNumber(),//
          "{A:{ method A ()} C:{B:{method Void  foo()}}}","A","C.B",  "{C:{B:{ method Void  foo() method This0 () }}}"   ,false//
        }, {lineNumber(),//
          "{A:{ method A ()} D:{C:{B:{method Void foo()}}}}","A","D.C.B",  "{D:{C:{B:{  method Void  foo()  method This0 ()}}}}"   ,false//
        }, {lineNumber(),//        ////
          "{ A:{ class method This1.B () } B:{ }}","A","This0", "{ B:{ } class method This0.B ()  }"    ,false//
        }, {lineNumber(),//
          "{ A:{ class method This1.B () } B:{ }}" ,"A","C", "{ B:{ }  C:{class method This1.B () }}"    ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ class method B () }} B:{ }}","A1.A2","A1",   "{ A1:{ class method B () } B:{ }}"   ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ class method B () }} B:{ }}" ,"A1.A2","This0",  "{ A1:{ } B:{ } class method B () }"   ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ class method B () } B:{ } }}","A1.A2","A1",  "{ A1:{B:{ } class method B () }}"   ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ class method This1.B () } B:{ } }}","A1.A2","This0",   "{ A1:{B:{ }} class method This0.A1.B () }"   ,false//
        }, {lineNumber(),//
          "{ A:{ D:{ class method This0 d() This0.d() } class method This1.B foo()This0.foo() } B:{ }}" ,
          "A","This0",
          "{ B:{ } D:{ class method This0 d() This0.d() } class method This0.B foo()This0.foo()  }"   ,false//
        }, {lineNumber(),//
          helpers.TestHelper.multiLine(""
              ,"{ A:{"
              ,"  OptMax:{"
              ,"    TOpt:{interface}"
              ,"    TEmpty:{ implements This1.TOpt}"
              ,"    }"
              ,"  }}"),
              "A","This0",
              helpers.TestHelper.multiLine(""
              ,"{"
              ,"OptMax:{"
              ,"  TOpt:{interface }"
              ,"  TEmpty:{ implements This1.TOpt}"
              ,"}}")    ,false//
      }, {lineNumber(),//
          "{ A:{class method This1.B ()}  B:{ }}",
          "A","A.C",
          "{ B:{} A:{ C:{class method This2.B #apply() }}}"  ,false//
        }, {lineNumber(),//
          "{ A:{class method This0.B ()  B:{ }}}"
          ,"A","A.C",
          "{ A:{ C:{class method This0.B #apply() B:{} }}}",false//

       },{lineNumber(),////the sub parts of test above
        "{ Result:{ A:{ class method This0.B #apply() B:{}}}}",
       "Result.A","Tmp",
       "{Result:{} Tmp:{ class method This0.B #apply() B:{}}}",false
/*
          "{  Result:{}
            Tmp:{
            class method
            This2.Tmp #apply()
            B:{}}}
        "Tmp","Result.A.C",false*/
        }, {lineNumber(),//
          "{ class method This0.B ()  B:{ }}"
          ,"This0","C",
          "{ C:{ class method This0.B #apply() B:{}}}",false//
        }, {lineNumber(),//
          "{ A:{B:{ method A m(B x)}} }"
          ,"A.B","C.B",
          "{ A:{ } C:{B:{ method A m(This0 x)}} }",false//
        }, {lineNumber(),//
          "{ A:{B:{ } method Void ma(B x)}  }"
          ,"A.B","C.D",
          "{ A:{method Void ma(This1.C.D x)}  C:{D:{ }}}",false//
        }, {lineNumber(),//
          "{ A:{B:{ method A mab(B x)} method A ma(B x)} method A m(A.B x) }"
          ,"A.B","C.D",
          "{ A:{method This0 ma(This1.C.D x)} method A m(C.D x) C:{D:{ method This2.A mab(This0 x)}}}",false//

        }, {lineNumber(),//
          "{ A:{B:{ method A mab(B x)} method A ma(B x)} method A m(A.B x) }"
          ,"A","C.D",
          "{ method C.D m(C.D.B x) C:{D:{B:{ method This1 mab(This0 x)} method This0 ma(B x)}} }",false//
        }, {lineNumber(),//simplified method from before
          "{ A:{B:{}} method Void m(A.B x) }"
          ,"A","C.D",
          "{ method Void m(C.D.B x) C:{D:{B:{}}} }",false//
        }, {lineNumber(),//simplified method from before2
          "{ A:{B:{}} method A m() }"
          ,"A","C.D",
          "{ method C.D m() C:{D:{B:{}}} }",false//
        }, {lineNumber(),//nested method from before
          "{ A:{B:{method A m()}}  }"
          ,"A","C.D",
          "{  C:{D:{B:{method This1 m() }}} }",false//
        }, {lineNumber(),//
          "{ A:{method Library m() {method A k()}} }"
          ,"A","B",
          "{ B:{method Library m() {method This1 k()}} }",false//

        } });
    }

    @Test public void test() {
      TestHelper.configureForTest();
      ClassB cb1 = getClassB(_cb1);
      Configuration.typeSystem.computeStage(Program.empty(), cb1);
      List<String> path1=TestHelper.cs(_path1);
      List<String> path2=TestHelper.cs(_path2);
      ClassB expected = getClassB(_expected);
      if (!isError) {
        ClassB res = Rename.renameClass(Program.empty(), cb1, path1, path2);
        res=Functions.clearCache(res,Ast.Stage.None);
        TestHelper.assertEqualExp(expected, res);
      } else {
        try {
          Rename.renameClass(Program.empty(), cb1, path1, path2);
          fail("error expected");
        } catch (Resources.Error err) {
          ClassB res = (ClassB) err.unbox;
          TestHelper.assertEqualExp(expected, res);
        }
      }
    }
  }
}
