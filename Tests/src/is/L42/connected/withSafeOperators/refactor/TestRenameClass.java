package is.L42.connected.withSafeOperators.refactor;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import ast.Ast;
import ast.Ast.C;
import ast.ExpCore.ClassB;
import helpers.TestHelper;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SubtleSubtypeViolation;
import is.L42.connected.withSafeOperators.refactor.Rename;
import platformSpecific.javaTranslation.Resources;
import programReduction.Program;

@RunWith(Parameterized.class) public class TestRenameClass {//add more test for error cases
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
          "{ A:{ class method This1.B () } B:{ }}",
          "A","This0",
          "{ class method This0.B () B:{ } }"
          ,false//
        }, {lineNumber(),//
          "{ A:{ class method This1.B () } B:{ }}" ,"A","C", "{ B:{ }  C:{class method This1.B () }}"    ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ class method B () }} B:{ }}",
          "A1.A2","A1",
          "{ B:{ } A1:{ class method B () } }"   ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ class method B () }} B:{ }}" ,
          "A1.A2","This0",
          "{ class method B () A1:{ } B:{ }}"   ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ class method B () } B:{ } }}",
          "A1.A2","A1",
          "{ A1:{ class method B () B:{ } }}"   ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ class method This1.B () } B:{ } }}",
          "A1.A2","This0",
          "{ class method This0.A1.B () A1:{B:{ }} }"   ,false//
        }, {lineNumber(),//
          "{ A:{ D:{ class method This0 d() This0.d() } class method This1.B foo()This0.foo() } B:{ }}" ,
          "A","This0",
          "{ B:{ } D:{ class method This0 d() This0.d() } class method This0.B foo()This0.foo()  }"   ,false//
        }, {lineNumber(),//
          auxiliaryGrammar.Functions.multiLine(""
              ,"{ A:{"
              ,"  OptMax:{"
              ,"    TOpt:{interface}"
              ,"    TEmpty:{ implements This1.TOpt}"
              ,"    }"
              ,"  }}"),
              "A","This0",
              auxiliaryGrammar.Functions.multiLine(""
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
        }, {lineNumber(),//
          "{ A:{class method Void ()void}"+
          " Fix:{A:{class method Void ()void}}}"
          ,"Fix","This",
          "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$MethodClash",true
        }, {lineNumber(),//
          "{ A:{B:{C:{} method Library m(This2.A a, This1.B b, This0.C c) }}}"
          ,"A","D",
          "{ D:{B:{C:{} method Library m(This1 a, This0 b, This0.C c) }}}",false//
        }, {lineNumber(),//
           "{ A: {interface method Library() {implements This1}}}"
           ,"A","D",
           "{ D: {interface method Library() {implements This1}}}",false//TODO: Causes a StackOverflow
        }, {lineNumber(),//
           "{ A: {method Library() {method This0()}}}"
           ,"A","D",
           "{ D: {method Library() {method This0()}}}",false//
        }});
    }
    @Test public void test() throws MethodClash, SubtleSubtypeViolation, ClassClash, PathUnfit {
      TestHelper.configureForTest();
      ClassB cb1 = getClassB(true,null,_cb1);
      List<Ast.C> path1=TestHelper.cs(_path1);
      List<Ast.C> path2=TestHelper.cs(_path2);
      if (!isError) {
        TestHelper.configureForTest();
        ClassB expected = getClassB(true,null,_expected);
        ClassB res = Rename.renameClassAux(Program.emptyLibraryProgram(), cb1, path1, path2);
        TestHelper.assertEqualExp(expected, res);
      } else {
        try {
          Rename.renameClassAux(Program.emptyLibraryProgram(), cb1, path1, path2);
          fail("error expected");
        }
        catch(Exception err){
        assertEquals(err.getClass().getName(),_expected);
        }
      }
    }
  }