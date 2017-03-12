package testAux;

import helpers.TestHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import facade.Configuration;
import facade.L42;
import facade.PData;
import facade.Parser;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import platformSpecific.inMemoryCompiler.RunningUtils;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Translator;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Phase;
import auxiliaryGrammar.Functions;
import programReduction.Program;

public class TranslationTest {
  @Test public void t1(){tester(
    "{  }"," void","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t2a(){tester(
      "{ C:{ class method Void foo() void } }"," C.foo()","platformSpecific.javaTranslation.Resources$Void");}

  @Test public void t2b(){tester(
      "{ C:{k() class method Void foo() void } }"," C.foo()","platformSpecific.javaTranslation.Resources$Void");}

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
      "{ A:{() implements IA method foo() void} IA:{interface method Void foo() } }"," A().foo()","platformSpecific.javaTranslation.Resources$Void");}



  public void tester(String cbStr,String eStr,String nameRes) {
    TestHelper.configureForTest();
    Program p=runTypeSystem(cbStr);
    ExpCore e=Desugar.of(Parser.parse(null,eStr)).accept(new InjectionOnCore());
    Translator code=Resources.withPDo(new PData(p),()->Translator.translateProgram(p, e));
    System.out.println(code);
    Object o=Resources.withPDo(new PData(p),()->code.runMap());
    Assert.assertEquals(o.getClass().getName(), nameRes);
    }

  private static Program runTypeSystem(String scb1) {
    ClassB cb1=(ClassB)Desugar.of(Parser.parse(null,scb1)).accept(new InjectionOnCore());
    Program p=Program.emptyLibraryProgram();
    ClassB cb=newTypeSystem.TypeSystem.instance().topTypeLib(Phase.Coherent, p.evilPush(cb1));
    return p.evilPush(cb);
  }
}
