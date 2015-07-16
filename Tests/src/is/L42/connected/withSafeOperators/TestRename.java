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
  @RunWith(Parameterized.class) public static class TestRenameClass {//add more test for error cases
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
        }, {//        
        "{A:{}}","A","B","{B:{}}",false//
        }, {//        
          "{A:{ type method type A m() A}}","A","B", "{B:{type method type B m() B}}"    ,false//
        }, {//        
          "{A:{ type method type A m() {return A}}}","A","B", "{B:{type method type B m() {return B}}}"    ,false//
        }, {//        
          "{A:{ method A ()} B:{foo()}}","A","B",  "{B:{ type method Outer0 foo()  method B ()}}"   ,false//
        }, {//        
          "{C:{A:{ method A ()}} B:{foo()}}","C::A","C::B",  "{C:{} B:{ type method Outer0 foo() method B ()  }}"    ,false//
        }, {//        
          "{D:{C:{A:{ method A ()}}} B:{foo()}}","D::C::A","B",  "{D:{C:{}} B:{ type method Outer0 foo()  method B ()}}"   ,false//
        }, {//        
          "{A:{ method A ()} C:{B:{foo()}}}","A","C::B",  "{C:{B:{ type method Outer0 foo() method C::B () }}}"   ,false//
        }, {//        
          "{A:{ method A ()} D:{C:{B:{foo()}}}}","A","D::C::B",  "{D:{C:{B:{  type method Outer0 foo()  method D::C::B ()}}}}"   ,false//
        }, {//        ////
          "{ A:{ type method Outer1::B () } B:{ }}","A","Outer0", "{ B:{ } type method Outer0::B ()  }"    ,false//
        }, {//        
          "{ A:{ type method Outer1::B () } B:{ }}" ,"A","C", "{ B:{ }  C:{type method Outer1::B () }}"    ,false//
        }, {//        
          "{ A1:{ A2:{ type method B () }} B:{ }}","A1::A2","A1",   "{ A1:{ type method B () } B:{ }}"   ,false//
        }, {//        
          "{ A1:{ A2:{ type method B () }} B:{ }}" ,"A1::A2","Outer0",  "{ A1:{ } B:{ } type method B () }"   ,false//
        }, {//        
          "{ A1:{ A2:{ type method B () } B:{ } }}","A","B",   "{ A1:{B:{ } type method B () }}"  ,false//
        }, {//        
          "{ A1:{ A2:{ type method B () } B:{ } }}","A1::A2","A1",  "{ A1:{B:{ } type method B () }}"   ,false//
        }, {//        
          "{ A1:{ A2:{ type method Outer1::B () } B:{ } }}","A1::A2","Outer0",   "{ A1:{B:{ }} type method Outer0::A1::B () }"   ,false//
        }, {//        
          "{ A:{ D:{ method Outer0 d() Outer0.d() } type method Outer1::B foo ()Outer0.foo() } B:{ }}" ,
          "A","Outer0",
          "{ B:{ } D:{ method Outer0 d() Outer0.d() } type method Outer0::B foo ()Outer0.foo()  }"   ,false//
        }, {//        
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
        }, {//        
          helpers.TestHelper.multiLine(""
              ,"{ A:{mut(var mut Cell head)"
              ,"  Cell:{interface}"
              ,"  CellEnd:{<:Cell}"
              ,"  }}"),
              "A","Outer0",
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
              ,"}") ,false//
        }, {//        
          "{ A:{type method Outer1::B ()}  B:{ }}",
          "A","A::C",
          "{ B:{} A:{ C:{type method Outer2::B #apply() }}}"  ,false//
        }, {//    
          "{ A:{type method Outer0::B ()  B:{ }}}"
          ,"A","A::C",  
          "{ A:{ C:{type method Outer0::B #apply() B:{} }}}",false//
        }, {//  
          "{ type method Outer0::B ()  B:{ }}"
          ,"Outer0","C",  
          "{ C:{ type method Outer0::B #apply() B:{}}}",false//
     
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
