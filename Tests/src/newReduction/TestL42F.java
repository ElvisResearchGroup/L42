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
import org.junit.runners.Parameterized.Parameters;

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
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{method Void v()void}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    void\n" +
    "  }\n"
    },{lineNumber(),
    "{ A:{method Void va()void} B:{method Void vb()void}}",
    "Class Foo£Id7 implements {\n"+
    "  }\n"+
    "Class Foo£CA£Id8 implements {\n"+
    "  mehtod Immutable Void va()(Immutable Foo£CA£Id8 this)\n"+
    "    void\n"+
    "  }\n"+
    "Class Foo£CB£Id9 implements {\n"+
    "  mehtod Immutable Void vb()(Immutable Foo£CB£Id9 this)\n"+
    "    void\n"+
    "  }\n"
    },{lineNumber(),"{method Void v()void}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    void\n" +
    "  }\n"
    },{lineNumber(),"{method Void v()this.v()}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    Foo£Id5.v()(this)\n" +
    "  }\n"
    },{lineNumber(),"{method Void v() (this.v())}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    (Foo£Id5.v()(this))\n" +
    "  }\n"
    },{lineNumber(),"{method Void v() (x=this.v() x)}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    (\n" +
    "      Immutable Void x=Foo£Id5.v()(this)\n" +
    "      x\n" +
    "    )\n" +
    "  }\n"
    },{lineNumber(),"{method Void v() (x=this.v() catch error Void r r x)}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    (\n" +
    "      Immutable Void x=Foo£Id5.v()(this)\n" +
    "      catch Error Immutable Void r r\n" +
    "      x\n" +
    "    )\n" +
    "  }\n"
    },{lineNumber(),"{method Void v() loop this.v()}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    loop Foo£Id5.v()(this)\n" +
    "  }\n"

    },{lineNumber(),"{method Void v() (var Void x=void x:=void x)}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    (\n" +
    "      Immutable Void x=void\n" +
    "      Immutable Void unused=(\n" +
    "        Immutable Void updateX=void\n" +
    "        x:=updateX\n" +
    "      )\n" +
    "      x\n" +
    "    )\n" +
    "  }\n"

    },{lineNumber(),"{class method This k(fwd This that) method This v() (x=This.k(x) x)}",
    "Class Foo£Id5 implements {\n" +
    "  mehtod Immutable Foo£Id5 k(that)(ImmutableFwd Foo£Id5 that)\n" +
    "    NewFwd\n"+
    "  mehtod Immutable Foo£Id5 New_k(that)(Immutable Foo£Id5 that)\n" +
    "    New\n"+
    "  mehtod Immutable Foo£Id5 v()(Immutable Foo£Id5 this)\n" +
    "    (\n" +
    "      Immutable Foo£Id5 x1=Foo£Id5.NewFwd()()\n" +
    "      Immutable Foo£Id5 x=(\n" +
    "        Class Foo£Id5 receiverX=Foo£Id5\n" +//ok for this to happen, it may be simplified later since not used
    "        Foo£Id5.k(that)(x1)\n" +
    "      )\n" +
    "      Immutable Void unused1=Resource.Fix()(x1,x)\n" +
    "      x\n" +
    "    )\n" +
    "  }\n"


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
