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
  @Parameter(4) public String expectedD;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{method Void v()void}",
    "Class £Id10 implements {\n" +
    "  method null £Id10 NewFwd()()\n"+
    "    NewFwd\n"+
    "  method Immutable Void v()(Immutable £Id10 this)\n" +
    "    void\n" +
    "  }\n",
    //java
    "public class £Id10{\n" +
    "  static public £Id10 £CNewFwd(){return new _Fwd();}\n" +
    "public static class _Fwd extends £Id10 implements newReduction.Fwd{\n" +
    "public ast.ExpCore revert(){return generated.Resource.£CPathOf(10);}\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "public static final £Id10 Instance=new _Fwd();\n" +
    "public static £Id10 Instance(){return Instance; }\n" +
    "  static public platformSpecific.javaTranslation.Resources.Void £Cv(£Id10 £Xthis){return platformSpecific.javaTranslation.Resources.Void.Instance();}\n" +
    "  }\n",
    "10->[10]\n"
    },{lineNumber(),
    "{ A:{method Void va()void} B:{method Void vb()void}}",
    "Class A£Id10 implements {\n" +
    "  method null A£Id10 NewFwd()()\n" +
    "    NewFwd\n" +
    "  method Immutable Void va()(Immutable A£Id10 this)\n" +
    "    void\n" +
    "  }\n" +
    "Class B£Id11 implements {\n" +
    "  method null B£Id11 NewFwd()()\n" +
    "    NewFwd\n" +
    "  method Immutable Void vb()(Immutable B£Id11 this)\n" +
    "    void\n" +
    "  }\n" +
    "Class £Id12 implements {\n" +
    "  method null £Id12 NewFwd()()\n" +
    "    NewFwd\n" +
    "  }\n",
    //java
  null,
  "10->[10]\n" +
  "11->[11]\n" +
  "12->[12]\n"
    },{lineNumber(),"{method Void v()void}",
    null,null,
   "10->[10]\n"
    },{lineNumber(),"{method Void v()this.v()}",
    "Class £Id10 implements {\n" +
    "  method null £Id10 NewFwd()()\n" +
    "    NewFwd\n" +
    "  method Immutable Void v()(Immutable £Id10 this)\n" +
    "    £Id10.v()(this)\n" +
    "  }\n",
    null,
    null
    },{lineNumber(),"{method Void v() (this.v())}",
    "Class £Id10 implements {\n" +
    "  method null £Id10 NewFwd()()\n" +
    "    NewFwd\n" +
    "  method Immutable Void v()(Immutable £Id10 this)\n" +
    "    (£Id10.v()(this))\n" +
    "  }\n",
    null,
    null
    },{lineNumber(),"{method Void v() (x=this.v() x)}",
    null,
    "public class £Id10{\n" +
    "  static public £Id10 £CNewFwd(){return new _Fwd();}\n" +
    "public static class _Fwd extends £Id10 implements newReduction.Fwd{\n" +
    "public ast.ExpCore revert(){return generated.Resource.£CPathOf(10);}\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "public static final £Id10 Instance=new _Fwd();\n" +
    "public static £Id10 Instance(){return Instance; }\n" +
    "  static public platformSpecific.javaTranslation.Resources.Void £Cv(£Id10 £Xthis){platformSpecific.javaTranslation.Resources.Void £Xx;£Xx=£Id10.£Cv(£Xthis);return £Xx;}\n" +
    "  }\n",
    null
    },{lineNumber(),"{method Void v() (x=this.v() catch error Void r r x)}",
    "Class £Id10 implements {\n" +
    "  method null £Id10 NewFwd()()\n" +
    "    NewFwd\n" +
    "  method Immutable Void v()(Immutable £Id10 this)\n" +
    "    (\n" +
    "      Immutable Void x=£Id10.v()(this)\n" +
    "      catch Error Immutable Void r r\n" +
    "      x\n" +
    "    )\n" +
    "  }\n",
    //java
    "public class £Id10{\n" +
    "  static public £Id10 £CNewFwd(){return new _Fwd();}\n" +
    "public static class _Fwd extends £Id10 implements newReduction.Fwd{\n" +
    "public ast.ExpCore revert(){return generated.Resource.£CPathOf(10);}\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "public static final £Id10 Instance=new _Fwd();\n" +
    "public static £Id10 Instance(){return Instance; }\n" +
    "  static public platformSpecific.javaTranslation.Resources.Void £Cv(£Id10 £Xthis){label:{platformSpecific.javaTranslation.Resources.Void £Xx;try{£Xx=£Id10.£Cv(£Xthis);}catch (platformSpecific.javaTranslation.Resources.Error catchX){if(catchX.inner() instanceof platformSpecific.javaTranslation.Resources.Void){platformSpecific.javaTranslation.Resources.Void £Xr=(platformSpecific.javaTranslation.Resources.Void)catchX.inner(); return £Xr;} else throw catchX;}return £Xx;}}\n" +
    "  }\n",
    null
    },{lineNumber(),"{method Void v() loop this.v()}",
    null,
    //java
    "public class £Id10{\n" +
    "  static public £Id10 £CNewFwd(){return new _Fwd();}\n" +
    "public static class _Fwd extends £Id10 implements newReduction.Fwd{\n" +
    "public ast.ExpCore revert(){return generated.Resource.£CPathOf(10);}\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "public static final £Id10 Instance=new _Fwd();\n" +
    "public static £Id10 Instance(){return Instance; }\n" +
    "  static public platformSpecific.javaTranslation.Resources.Void £Cv(£Id10 £Xthis){label:while(true)£Id10.£Cv(£Xthis);}\n" +
    "  }\n",
    null
    },{lineNumber(),"{method Void v() (var Void x=void x:=void x)}",
    null,null,null
    },{lineNumber(),"{class method This k(fwd This that) method This v() (x=This.k(x) x)}",
    "Class £Id10 implements {\n" +
    "  method null £Id10 NewFwd()()\n" +
    "    NewFwd\n" +
    "  method Immutable £Id10 k(that)(ImmutableFwd £Id10 that)\n" +
    "    NewWithFwd\n" +
    "  method Immutable £Id10 New_k(that)(Immutable £Id10 that)\n" +
    "    New\n" +
    "  method Immutable £Id10 v()(Immutable £Id10 this)\n" +
    "    (\n" +
    "      Immutable £Id10 x0=£Id10.NewFwd()()\n" +
    "      Immutable £Id10 x=(\n" +
    "        Class £Id10 receiverX=£Id10\n" +
    "        £Id10.k(that)(x0)\n" +
    "      )\n" +
    "      Immutable Void unused=newReduction.Fwd.Fix()(x0,x)\n" +
    "      x\n" +
    "    )\n" +
    "  }\n",
    "public class £Id10 implements platformSpecific.javaTranslation.Resources.Revertable{\n" +
    "  static public £Id10 £CNewFwd(){return new _Fwd();}\n" +
    "public static class _Fwd extends £Id10 implements newReduction.Fwd{\n" +
    "public ast.ExpCore revert(){return generated.Resource.£CPathOf(10);}\n" +
    "private java.util.List<Object> os=new java.util.ArrayList<>();\n" +
    "private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n" +
    "public java.util.List<Object> os(){return os;}\n" +
    "public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n" +
    "public static final £Id10 Instance=new _Fwd();\n" +
    "public static £Id10 Instance(){return Instance; }\n" +
    "  static public £Id10 £Ck£Xthat(£Id10 £Xthat){£Id10 Res=new £Id10();Res.£Xthat=£Xthat;newReduction.Fwd.£CAddIfFwd(£Xthat,Res,£Id10.FieldAssFor£X£Xthat);return Res;}£Id10 £Xthat;public static java.util.function.BiConsumer<Object,Object> FieldAssFor£X£Xthat=(f,o)->{((£Id10)o).£Xthat=(£Id10)f;};public ast.ExpCore revert(){\n" +
    "return new ast.ExpCore.MCall(Instance().revert(),ast.Ast.MethodSelector.of(\"k\",java.util.Arrays.asList(\"that\")),ast.Ast.Doc.empty(),java.util.Arrays.asList(platformSpecific.javaTranslation.Resources.Revertable.doRevert(this.£Xthat)),null,null,null);\n" +
    "}\n" +
    "\n" +
    "  static public £Id10 £CNew_k£Xthat(£Id10 £Xthat){£Id10 Res=new £Id10();Res.£Xthat=£Xthat;return Res;}\n" +
    "  static public £Id10 £Cv(£Id10 £Xthis){£Id10 £Xx0;£Id10 £Xx;platformSpecific.javaTranslation.Resources.Void £Xunused;£Xx0=£Id10.£CNewFwd();{£Id10 £XreceiverX;£XreceiverX=£Id10.Instance();£Xx=£Id10.£Ck£Xthat(£Xx0);}£Xunused=newReduction.Fwd.£CFix(£Xx0, £Xx);return £Xx;}\n" +
    "  }\n" +
    "",
    null
    },{lineNumber(),"{method Library l() (x={A:{}} x)}",
    "Stub for cn: 11\n" +
    "Class £Id12 implements {\n" +
    "  method null £Id12 NewFwd()()\n" +
    "    NewFwd\n" +
    "  method Immutable Library l()(Immutable £Id12 this)\n" +
    "    (\n" +
    "      Immutable Library x=generated.Resource.LoadLib_11()()\n" +
    "      x\n" +
    "    )\n" +
    "  }\n",
    null,
    "12->[12]\n"
    },{lineNumber(),"{class method Void m() A.ma() A:{class method Void ma() void }}",
    null,null,
    "10->[10]\n" +
    "11->[10, 11]\n"
    },{lineNumber(),"{class method Void m() A.ma() A:{class method Void ma() This1.m() }}",
    null,null,
    "10->[10, 11]\n" +
    "11->[10, 11]\n"
    },{lineNumber(),"{class method Void m() void A:{class method Void ma() This1.m() }}",
    null,null,
    "10->[10, 11]\n" +
    "11->[11]\n"
    },{lineNumber(),"{class method Void m() A.ma()"
        + " A:{class method Void ma() This1.m() }"
        + " B:{class method Void mb() This1.m() }"
        + "}",
    null,null,
    "10->[10, 12]\n" +
    "11->[10, 11, 12]\n" +
    "12->[10, 12]\n"
    },{lineNumber(),"{class method Void m() void"
        + " A:{class method Void ma() This1.m() }"
        + " B:{class method Void mb() This1.m() }"
        + "}",
    null,null,
    "10->[10, 12]\n" +
    "11->[11, 12]\n" +
    "12->[12]\n"
    },{lineNumber(),"{class method Void m() void"
        + " A:{class method Void ma() B.mb() }"
        + " B:{class method Void mb() C.mc() }"
        + " C:{class method Void mc() This1.m() }"
        + "}",
    null,null,
    "10->[10, 11, 12, 13]\n" +
    "11->[11, 12, 13]\n" +
    "12->[12, 13]\n" +
    "13->[13]\n"

    }});}

  @Test  public void test() {
    TestHelper.configureForTest();
    ExpCore.ClassB l=(ExpCore.ClassB)TestHelper.getExpCore(TestL42F.class.getSimpleName(),_cb);
    Program p=Program.emptyLibraryProgram();
    p=p.updateTop(l);
    l=TypeSystem.instance().topTypeLib(Phase.Coherent,p);
    p=p.updateTop(l);
    Paths ps=Paths.empty().push(Collections.singletonList(Collections.emptyList()));
    ClassTable ct=ClassTable.empty.growWith(p,ps);
    ct=ct.computeDeps();
    if(expected!=null) {assertEquals(expected,ct.toString());}
    if(expectedJ!=null) {assertEquals(expectedJ,ct.toJString());}
    if(expectedD!=null) {assertEquals(expectedD,ct.toDepJString());}

    }
  }
