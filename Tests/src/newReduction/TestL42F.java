package newReduction;

import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.Phase;
import auxiliaryGrammar.TestWellFormednessCore;
import auxiliaryGrammar.WellFormednessCore;
import helpers.TestHelper;
import newTypeSystem.FormattedError;
import newTypeSystem.TIn;
import newTypeSystem.TOut;
import newTypeSystem.TypeSystem;
import programReduction.Program;
import tools.Assertions;

@RunWith(Parameterized.class)
public class TestL42F {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb;
  @Parameter(2) public String expected;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{method Void v()void}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "  void\n" +
    "}\n"
    },{lineNumber(),
    "{ A:{method Void va()void} B:{method Void vb()void}}",
    "Class Foo£Id7 implements {\n"+
    "  \n"+
    "}\n"+
    "Class Foo£CA£Id8 implements {\n"+
    "  mehtod Immutable Void va()(Immutable Foo£CA£Id8 this)\n"+
    "  void\n"+
    "}\n"+
    "Class Foo£CB£Id9 implements {\n"+
    "  mehtod Immutable Void vb()(Immutable Foo£CB£Id9 this)\n"+
    "  void\n"+
    "}\n"
    },{lineNumber(),"{method Void v()void}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "  void\n" +
    "}\n"
    }});}

  @Test  public void test() {
    ExpCore.ClassB l=(ExpCore.ClassB)TestHelper.getExpCore(TestL42F.class.getSimpleName(),_cb);
    Program p=Program.emptyLibraryProgram();
    p=p.updateTop(l);
    l=TypeSystem.instance().topTypeLib(Phase.Typed,p);
    p=p.updateTop(l);
    ClassTable ct=ClassTable.empty.plus(Collections.singletonList("Foo"),p);
    assertEquals(expected,ct.toString());
    }
  }
