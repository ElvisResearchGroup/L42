package testAux;

import helpers.TestHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import facade.L42;
import facade.Parser;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import platformSpecific.inMemoryCompiler.RunningUtils;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Translator;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import typeSystem.TypeExtraction;
import typeSystem.TypeSystemOK;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class TranslationTest {
  @Test public void t1(){tester(
    "{  }"," void","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t2a(){tester(
      "{ C:{ type method Void foo() void } }"," C.foo()","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t2b(){tester(
      "{ C:{k() type method Void foo() void } }"," C.foo()","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t3(){tester(
      "{ C:{k() method Void foo() void } }"," C.k().foo()","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t4(){tester(
      "{ C:{() method Void foo() void  method C foo(C x) x} }"," C().foo(x:C()).foo()","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t5(){tester(
      "{ C:{() method Void foo() void  method C (C x) x} }"," C()(x:C()).foo()","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t6(){tester(
      "{  }"," { return void }","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t7(){tester(
      "{ A:{(B b, Void v)} B:{(A a)} }"," (aa=A(b:bb,v:void)  bb=B(a:aa) bb.a().b().a().v()  )","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t8(){tester(
      "{ A:{(A a, Void v)} }"," (aa=A(a:aa,v:void) aa.a().a().a().v()  )","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t9(){tester(
      "{ A:{()<:IA method foo() void} IA:{interface method Void foo() } }"," A().foo()","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t10(){tester(
      TestHelper.multiLine("{"
      ,"Alu:{'@plugin"
      ,"  'L42.is/connected/withAlu"
      ,"  ()}"
      ,"N:{(Library that)"
      ,"  type method N #numberParser(Library that) ("
      ,"    N(using Alu"
      ,"      check stringToInt32(that)"
      ,"      error void))"
      ,"  method N +(N that) ("
      ,"    N(using Alu"
      ,"      check sumInt32(n1:this.that() n2:that.that())"
      ,"      error void))"
      ,"}}"),
      " 5N+3N","generated.Program42$Outer0£_N");}

  @Test public void t10b(){tester(
      TestHelper.multiLine("{"
      ,"Alu:{'@plugin"
      ,"  'L42.is/connected/withAlu"
      ,"  ()}"
      ,"N:{(Library that)"
      ,"  type method N #numberParser(Library that) ("
      ,"    N(using Alu"
      ,"      check stringToInt32(that)"
      ,"      error void))"
      ,"  method N +(N that) ("
      ,"    N(using Alu"
      ,"      check sumInt32(n1:this.that() n2:that.that())"
      ,"      error void))"
      ,"}}"),
      " 5N+3N+80N","generated.Program42$Outer0£_N");}


  @Test public void t11(){tester(
      TestHelper.multiLine("{"
      ,"Alu:{'@plugin"
      ,"  'L42.is/connected/withAlu"
      ,"  ()}"
      ,"N:{(Library that)"
      ,"  type method N #numberParser(Library that) ("
      ,"    N(using Alu"
      ,"      check stringToInt32(that)"
      ,"      error void))"
      ,"  method N +(N that) ("
      ,"    N(using Alu"
      ,"      check sumInt32(n1:this.that() n2:that.that())"
      ,"      error void))"
      ,"}"
      ,"S:{#stringParser(Library that)"
      ,"  method S ++ (S that) ("
      ,"    S.#stringParser(using Alu"
      ,"      check stringConcat(s1:this.that(),s2:that.that())"
      ,"      error void) )"
      ,"  }"
      ,"  }"),
      TestHelper.multiLine(""
      ," ( S s1=S\"foo\""
      ,"   S s2=s1++S\"bar\""
      ,"   using Alu check stringDebug(s2.that()) void "
      ,"  )")
       ,"platformSpecific.javaTranslation.Resources$Void");
  Assert.assertEquals(L42.record.toString(),"foobar\n");
  L42.record=new StringBuilder();
  }

  public void tester(String cbStr,String eStr,String nameRes) {
    TestHelper.configureForTest();
    Program p=runTypeSystem(cbStr);
    ExpCore e=Desugar.of(Parser.parse(null,eStr)).accept(new InjectionOnCore());
    ExpCore e2=Norm.of(p, e);
    String code=Resources.withPDo(p,()->Translator.translateProgram(p, e2));
    System.out.println(code);
    Object o=Resources.withPDo(p,()->Translator.runString(code));
    Assert.assertEquals(o.getClass().getName(), nameRes);
    }

  private static Program runTypeSystem(String scb1) {
    ClassB cb1=(ClassB)Desugar.of(Parser.parse(null,scb1)).accept(new InjectionOnCore());
    Program p=Program.empty();
    ClassB cb1t=TypeExtraction.etFull(p,cb1);
    p=p.addAtTop(cb1,cb1t);
    TypeSystemOK.checkAll(p);
    return p;
  }
}
