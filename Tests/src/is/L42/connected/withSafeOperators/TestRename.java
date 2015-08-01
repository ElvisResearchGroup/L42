package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.fail;
import helpers.TestHelper;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Program;

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
          "{ method Void m() void}", "Outer0", "m()", "k()", "{ method Void k() void}", false //
          } });
    }

    @Test public void test() {
      TestHelper.configureForTest();
      ClassB cb1 = getClassB(_cb1);
      Path path = Path.parse(_path);
      MethodSelector ms1 = MethodSelector.parse(_ms1);
      MethodSelector ms2 = MethodSelector.parse(_ms2);
      ClassB expected = getClassB(_expected);
      if (!isError) {
        ClassB res = Rename.renameMethod(Program.empty(), cb1, path.getCBar(), ms1, ms2);
        TestHelper.assertEqualExp(expected, res);
      } else {
        try {
          Rename.renameMethod(Program.empty(), cb1, path.getCBar(), ms1, ms2);
          fail("error expected");
        } catch (Resources.Error err) {
          ClassB res = (ClassB) err.unbox;
          TestHelper.assertEqualExp(expected, res);
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
        "{B:{ method Void m() void}}", "B", "C::D", "{C:{ D:{method Void m() void}}}", false//
        } });
    }

    @Test public void test() {
      TestHelper.configureForTest();
      ClassB cb1 = getClassB(_cb1);
      Path path1 = Path.parse(_path1);
      Path path2 = Path.parse(_path2);
      ClassB expected = getClassB(_expected);
      if (!isError) {
        ClassB res = Rename.renameClassStrict(Program.empty(), cb1, path1.getCBar(), path2.getCBar());
        TestHelper.assertEqualExp(expected, res);
      } else {
        try {
          Rename.renameClassStrict(Program.empty(), cb1, path1.getCBar(), path2.getCBar());
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
    List<String> res = ClassOperations.toTop(Arrays.asList(),Path.parse("Outer0::A"));
    Assert.assertEquals(res,Arrays.asList("A"));
    res = ClassOperations.toTop(Arrays.asList(),Path.parse("Outer0::A::B"));
    Assert.assertEquals(res,Arrays.asList("A","B"));
    res = ClassOperations.toTop(Arrays.asList("C"),Path.parse("Outer1::A::B"));
    Assert.assertEquals(res,Arrays.asList("A","B"));
    res = ClassOperations.toTop(Arrays.asList("C"),Path.parse("Outer0::A::B"));
    Assert.assertEquals(res,Arrays.asList("C","A","B"));
  }
  @Test
  public void testNormalizePath() {
    Path res = ClassOperations.normalizePath(Arrays.asList(),0,Arrays.asList());
    Assert.assertEquals(res,Path.outer(0));
    res = ClassOperations.normalizePath(Arrays.asList("A"),1,Arrays.asList("B"));
    Assert.assertEquals(res,Path.parse("Outer1::B"));
    res = ClassOperations.normalizePath(Arrays.asList("B"),1,Arrays.asList("B"));
    Assert.assertEquals(res,Path.parse("Outer0"));

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
        "{B:{ method Void m() void}}", "B", "C::D", "{C:{ D:{method Void m() void}}}", false//
        }, {lineNumber(),//
        "{A:{}}","A","B","{B:{}}",false//
        }, {lineNumber(),//
          "{A:{ type method type A m() A}}","A","B", "{B:{type method type Outer0 m() Outer0}}"    ,false//
        }, {lineNumber(),//
          "{A:{ type method type A m() {return A}}}","A","B", "{B:{type method type Outer0 m() {return Outer0}}}"    ,false//
        }, {lineNumber(),//
          "{A:{ method A ()} B:{foo()}}","A","B",  "{B:{ type method Outer0 foo()  method Outer0 ()}}"   ,false//
        }, {lineNumber(),//
          "{C:{A:{ method A ()}} B:{foo()}}","C::A","B",  "{C:{} B:{ type method Outer0 foo() method Outer0 ()  }}"    ,false//
        }, {lineNumber(),//
          "{D:{C:{A:{ method A ()}}} B:{foo()}}","D::C::A","B",  "{D:{C:{}} B:{ type method Outer0 foo()  method Outer0 ()}}"   ,false//
        }, {lineNumber(),//
          "{A:{ method A ()} C:{B:{foo()}}}","A","C::B",  "{C:{B:{ type method Outer0 foo() method Outer0 () }}}"   ,false//
        }, {lineNumber(),//
          "{A:{ method A ()} D:{C:{B:{foo()}}}}","A","D::C::B",  "{D:{C:{B:{  type method Outer0 foo()  method Outer0 ()}}}}"   ,false//
        }, {lineNumber(),//        ////
          "{ A:{ type method Outer1::B () } B:{ }}","A","Outer0", "{ B:{ } type method Outer0::B ()  }"    ,false//
        }, {lineNumber(),//
          "{ A:{ type method Outer1::B () } B:{ }}" ,"A","C", "{ B:{ }  C:{type method Outer1::B () }}"    ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ type method B () }} B:{ }}","A1::A2","A1",   "{ A1:{ type method B () } B:{ }}"   ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ type method B () }} B:{ }}" ,"A1::A2","Outer0",  "{ A1:{ } B:{ } type method B () }"   ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ type method B () } B:{ } }}","A1::A2","A1",  "{ A1:{B:{ } type method B () }}"   ,false//
        }, {lineNumber(),//
          "{ A1:{ A2:{ type method Outer1::B () } B:{ } }}","A1::A2","Outer0",   "{ A1:{B:{ }} type method Outer0::A1::B () }"   ,false//
        }, {lineNumber(),//
          "{ A:{ D:{ method Outer0 d() Outer0.d() } type method Outer1::B foo ()Outer0.foo() } B:{ }}" ,
          "A","Outer0",
          "{ B:{ } D:{ method Outer0 d() Outer0.d() } type method Outer0::B foo ()Outer0.foo()  }"   ,false//
        }, {lineNumber(),//
          helpers.TestHelper.multiLine(""
              ,"{ A:{"
              ,"  OptMax:{"
              ,"    TOpt:{interface}"
              ,"    TEmpty:{<:Outer1::TOpt}"
              ,"    }"
              ,"  }}"),
              "A","Outer0",
              helpers.TestHelper.multiLine(""
              ,"{"
              ,"OptMax:{"
              ,"  TOpt:{interface }"
              ,"  TEmpty:{<:Outer1::TOpt}"
              ,"}}")    ,false//
        }, {lineNumber(),//        ////18
          helpers.TestHelper.multiLine(""
              ,"{ A:{mut(var mut Cell head)"
              ,"  Cell:{interface}"
              ,"  CellEnd:{<:Cell}"
              ,"  }}"),
              "A","Outer0",
              helpers.TestHelper.multiLine(""
              ,"{"
              ,"type method "
              ,"mut Outer0 #apply(mut Outer0::Cell^head'@private @consistent"
              ,") "
              ,"mut method '@private @consistent"
              ,"Void head(mut Outer0::Cell that)"
              ,"mut method '@private @consistent"
              ,"mut Outer0::Cell #head()"
              ,"read method '@private @consistent"
              ,"read Outer0::Cell head()"
              ,"Cell:{interface }"
              ,"CellEnd:{<:Outer1::Cell}"
              ,"}") ,false//
        }, {lineNumber(),//
          "{ A:{type method Outer1::B ()}  B:{ }}",
          "A","A::C",
          "{ B:{} A:{ C:{type method Outer2::B #apply() }}}"  ,false//
        }, {lineNumber(),//
          "{ A:{type method Outer0::B ()  B:{ }}}"
          ,"A","A::C",
          "{ A:{ C:{type method Outer0::B #apply() B:{} }}}",false//
        }, {lineNumber(),//
          "{ type method Outer0::B ()  B:{ }}"
          ,"Outer0","C",
          "{ C:{ type method Outer0::B #apply() B:{}}}",false//
        }, {lineNumber(),//
          "{ A:{B:{ method A m(B x)}} }"
          ,"A::B","C::B",
          "{ A:{ } C:{B:{ method A m(Outer0 x)}} }",false//
        }, {lineNumber(),//
          "{ A:{B:{ method A mab(B x)} method A ma(B x)} method A m(A::B x) }"
          ,"A::B","C::D",
          "{ A:{method Outer1::A ma(Outer1::C::D x)} method A m(C::D x) C:{D:{ method Outer2::A mab(Outer0 x)}}}",false//
        }, {lineNumber(),//
          "{ A:{B:{ method A mab(B x)} method A ma(B x)} method A m(A::B x) }"
          ,"A","C::D",
          "{ method C::D m(C::D::B x) C:{D:{B:{ method Outer1 mab(Outer0 x)} method Outer0 ma(B x)}} }",false//
        }, {lineNumber(),//
          "{ A:{method Library m() {method A k()}} }"
          ,"A","B",
          "{ B:{method Library m() {method Outer1 k()}} }",false//

        } });
    }

    @Test public void test() {
      TestHelper.configureForTest();
      ClassB cb1 = getClassB(_cb1);
      Path path1 = Path.parse(_path1);
      Path path2 = Path.parse(_path2);
      ClassB expected = getClassB(_expected);
      if (!isError) {
        ClassB res = Rename.renameClass(Program.empty(), cb1, path1.getCBar(), path2.getCBar());
        TestHelper.assertEqualExp(expected, res);
      } else {
        try {
          Rename.renameClass(Program.empty(), cb1, path1.getCBar(), path2.getCBar());
          fail("error expected");
        } catch (Resources.Error err) {
          ClassB res = (ClassB) err.unbox;
          TestHelper.assertEqualExp(expected, res);
        }
      }
    }
  }
}
