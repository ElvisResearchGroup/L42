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
import programReduction.Paths;
import programReduction.Program;
import tools.Assertions;

@RunWith(Parameterized.class)
public class TestL42F {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb;
  @Parameter(2) public String expected;
  @Parameter(3) public String expectedJ;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{method Void v()void}",
    "Class Foo£Id5 implements {\n" +
    "  method null Foo£Id5 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    void\n" +
    "  }\n",
    //java
    "public class Foo£Id5{\n" +
    "  public Foo£Id5 NewFwd(){return new _Fwd();}\n" +
    "private static class _Fwd extends Foo£Id5CN implements Fwd{\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +

    "  public platformSpecific.javaTranslation.Resources.Void v(Foo£Id5 this){return platformSpecific.javaTranslation.Resources$Void.Instance();}\n" +
    "  }\n"
    },{lineNumber(),
    "{ A:{method Void va()void} B:{method Void vb()void}}",
    "Class Foo£Id7 implements {\n"+
    "  method null Foo£Id7 NewFwd()()\n"+
    "    NewFwd\n"+
    "  }\n"+
    "Class Foo£CA£Id8 implements {\n"+
    "  method null Foo£CA£Id8 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void va()(Immutable Foo£CA£Id8 this)\n"+
    "    void\n"+
    "  }\n"+
    "Class Foo£CB£Id9 implements {\n"+
    "  method null Foo£CB£Id9 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void vb()(Immutable Foo£CB£Id9 this)\n"+
    "    void\n"+
    "  }\n",
    //java
    "public class Foo£Id7{\n"+
    "  public Foo£Id7 NewFwd(){return new _Fwd();}\n"+
  "private static class _Fwd extends Foo£Id7CN implements Fwd{\n"+
  "private java.util.List<Object> os=new java.util.ArrayList<>();\n"+
  "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n"+
  "public java.util.List<Object> os(){return os;}\n"+
  "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n"+
  "  }\n"+
  "public class Foo£CA£Id8{\n"+
  "  public Foo£CA£Id8 NewFwd(){return new _Fwd();}\n"+
  "private static class _Fwd extends Foo£CA£Id8CN implements Fwd{\n"+
  "private java.util.List<Object> os=new java.util.ArrayList<>();\n"+
  "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n"+
  "public java.util.List<Object> os(){return os;}\n"+
  "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n"+
  "  public platformSpecific.javaTranslation.Resources.Void va(Foo£CA£Id8 this){return platformSpecific.javaTranslation.Resources$Void.Instance();}\n"+
  "  }\n"+
  "public class Foo£CB£Id9{\n"+
  "  public Foo£CB£Id9 NewFwd(){return new _Fwd();}\n"+
  "private static class _Fwd extends Foo£CB£Id9CN implements Fwd{\n"+
  "private java.util.List<Object> os=new java.util.ArrayList<>();\n"+
  "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n"+
  "public java.util.List<Object> os(){return os;}\n"+
  "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n"+
  "  public platformSpecific.javaTranslation.Resources.Void vb(Foo£CB£Id9 this){return platformSpecific.javaTranslation.Resources$Void.Instance();}\n"+
  "  }\n"

    },{lineNumber(),"{method Void v()void}",
    "Class Foo£Id5 implements {\n" +
    "  method null Foo£Id5 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    void\n" +
    "  }\n",
    //java
    "public class Foo£Id5{\n"+
    "  public Foo£Id5 NewFwd(){return new _Fwd();}\n"+
  "private static class _Fwd extends Foo£Id5CN implements Fwd{\n"+
  "private java.util.List<Object> os=new java.util.ArrayList<>();\n"+
  "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n"+
  "public java.util.List<Object> os(){return os;}\n"+
  "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n"+
  "  public platformSpecific.javaTranslation.Resources.Void v(Foo£Id5 this){return platformSpecific.javaTranslation.Resources$Void.Instance();}\n"+
  "  }\n"

    },{lineNumber(),"{method Void v()this.v()}",
    "Class Foo£Id5 implements {\n" +
    "  method null Foo£Id5 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    Foo£Id5.v()(this)\n" +
    "  }\n",
    "public class Foo£Id5{\n" +
    "  public Foo£Id5 NewFwd(){return new _Fwd();}\n" +
    "private static class _Fwd extends Foo£Id5CN implements Fwd{\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "  public platformSpecific.javaTranslation.Resources.Void v(Foo£Id5 this){return Foo£Id5.v(this);}\n" +
    "  }\n"
    },{lineNumber(),"{method Void v() (this.v())}",
    "Class Foo£Id5 implements {\n" +
    "  method null Foo£Id5 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    (Foo£Id5.v()(this))\n" +
    "  }\n",
    "public class Foo£Id5{\n" +
    "  public Foo£Id5 NewFwd(){return new _Fwd();}\n" +
    "private static class _Fwd extends Foo£Id5CN implements Fwd{\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "  public platformSpecific.javaTranslation.Resources.Void v(Foo£Id5 this){return Foo£Id5.v(this);}\n" +
    "  }\n"
    },{lineNumber(),"{method Void v() (x=this.v() x)}",
    "Class Foo£Id5 implements {\n" +
    "  method null Foo£Id5 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    (\n" +
    "      Immutable Void x=Foo£Id5.v()(this)\n" +
    "      x\n" +
    "    )\n" +
    "  }\n",
    "public class Foo£Id5{\n" +
    "  public Foo£Id5 NewFwd(){return new _Fwd();}\n" +
    "private static class _Fwd extends Foo£Id5CN implements Fwd{\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "  public platformSpecific.javaTranslation.Resources.Void v(Foo£Id5 this){"
    + "platformSpecific.javaTranslation.Resources.Void x;"
    + "x=Foo£Id5.v(this);"
    + "return x;}\n" +
    "  }\n"
    },{lineNumber(),"{method Void v() (x=this.v() catch error Void r r x)}",
    "Class Foo£Id5 implements {\n" +
    "  method null Foo£Id5 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    (\n" +
    "      Immutable Void x=Foo£Id5.v()(this)\n" +
    "      catch Error Immutable Void r r\n" +
    "      x\n" +
    "    )\n" +
    "  }\n",
    //java
    "public class Foo£Id5{\n" +
    "  public Foo£Id5 NewFwd(){return new _Fwd();}\n" +
    "private static class _Fwd extends Foo£Id5CN implements Fwd{\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "  public platformSpecific.javaTranslation.Resources.Void v(Foo£Id5 this){"
    + "platformSpecific.javaTranslation.Resources.Void x;"
    + "try{x=Foo£Id5.v(this);}catch (Error catchX){"
    + "if(catchX.inner() instanceof platformSpecific.javaTranslation.Resources.Void){"
    + "platformSpecific.javaTranslation.Resources.Void r"
    + "=(platformSpecific.javaTranslation.Resources.Void)catchX.inner();"
    + " return r;}"
    + " else throw catchX;}"
    + "return x;}\n" +
    "  }\n"
    },{lineNumber(),"{method Void v() loop this.v()}",
    "Class Foo£Id5 implements {\n" +
    "  method null Foo£Id5 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    loop Foo£Id5.v()(this)\n" +
    "  }\n",
    //java
"public class Foo£Id5{\n" +
"  public Foo£Id5 NewFwd(){return new _Fwd();}\n" +
"private static class _Fwd extends Foo£Id5CN implements Fwd{\n" +
"private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
"private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
"public java.util.List<Object> os(){return os;}\n" +
"public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
"  public platformSpecific.javaTranslation.Resources.Void v(Foo£Id5 this){"
+ "label0:while(true)Foo£Id5.v(this);}\n" +
"  }\n"

    },{lineNumber(),"{method Void v() (var Void x=void x:=void x)}",
    "Class Foo£Id5 implements {\n" +
    "  method null Foo£Id5 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void v()(Immutable Foo£Id5 this)\n" +
    "    (\n" +
    "      Immutable Void x=void\n" +
    "      Immutable Void unused=(\n" +
    "        Immutable Void updateX=void\n" +
    "        x:=updateX\n" +
    "      )\n" +
    "      x\n" +
    "    )\n" +
    "  }\n",
    "public class Foo£Id5{\n" +
    "  public Foo£Id5 NewFwd(){return new _Fwd();}\n" +
    "private static class _Fwd extends Foo£Id5CN implements Fwd{\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "  public platformSpecific.javaTranslation.Resources.Void v(Foo£Id5 this){"
    + "platformSpecific.javaTranslation.Resources.Void x;"
    + "platformSpecific.javaTranslation.Resources.Void unused;"
    + "x=platformSpecific.javaTranslation.Resources$Void.Instance();"
    + "{platformSpecific.javaTranslation.Resources.Void updateX;"
    + "updateX=platformSpecific.javaTranslation.Resources$Void.Instance();"
    + "x=updateX;"
    + "}return x;}\n" +
    "  }\n"

    },{lineNumber(),"{class method This k(fwd This that) method This v() (x=This.k(x) x)}",
    "Class Foo£Id5 implements {\n" +
    "  method null Foo£Id5 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Foo£Id5 k(that)(ImmutableFwd Foo£Id5 that)\n" +
    "    NewFwd\n"+
    "  method Immutable Foo£Id5 New_k(that)(Immutable Foo£Id5 that)\n" +
    "    New\n"+
    "  method Immutable Foo£Id5 v()(Immutable Foo£Id5 this)\n" +
    "    (\n" +
    "      Immutable Foo£Id5 x1=Foo£Id5.NewFwd()()\n" +
    "      Immutable Foo£Id5 x=(\n" +
    "        Class Foo£Id5 receiverX=Foo£Id5\n" +//ok for this to happen, it may be simplified later since not used
    "        Foo£Id5.k(that)(x1)\n" +
    "      )\n" +
    "      Immutable Void unused1=Resource.Fix()(x1,x)\n" +
    "      x\n" +
    "    )\n" +
    "  }\n",
    "public class Foo£Id5{\n" +
    "  public Foo£Id5 NewFwd(){return new _Fwd();}\n" +
    "private static class _Fwd extends Foo£Id5CN implements Fwd{\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "  public Foo£Id5 k£Xthat(Foo£Id5 that){return new _Fwd();}\n" +
    "private static class _Fwd extends Foo£Id5CN implements Fwd{\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "  public Foo£Id5 New_k£Xthat(Foo£Id5 that){Foo£Id5 res=new Foo£Id5();res.that=that;return res;}\n" +
    "  public Foo£Id5 v(Foo£Id5 this){"
    + "Foo£Id5 x1;"
    + "Foo£Id5 x;"
    + "platformSpecific.javaTranslation.Resources.Void unused1;"
    + "x1=Foo£Id5.NewFwd();"
    + "{Foo£Id5 receiverX;"
    + "receiverX=Foo£Id5.Instance();"
    + "x=Foo£Id5.k£Xthat(x1);"
    + "}unused1=Resource.Fix(x1, x);"
    + "return x;}\n" +
    "  }\n"


    }});}

  @Test  public void test() {
    ExpCore.ClassB l=(ExpCore.ClassB)TestHelper.getExpCore(TestL42F.class.getSimpleName(),_cb);
    Program p=Program.emptyLibraryProgram();
    p=p.updateTop(l);
    l=TypeSystem.instance().topTypeLib(Phase.Typed,p);
    p=p.updateTop(l);
    Paths ps=Paths.empty().push(Collections.singletonList(Collections.emptyList()));
    ClassTable ct=ClassTable.empty.growWith(Collections.singletonList("Foo"),p,ps);
    assertEquals(expected,ct.toString());
    assertEquals(expectedJ,ct.toJString());

    }
  }
