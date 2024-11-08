package is.L42.tests;

import static is.L42.tools.General.L;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.stream.Stream;

import is.L42.common.Program;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.JavaCodeStore;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.AtomicTest;
import is.L42.top.CachedTop;
import is.L42.top.Init;
import is.L42.translationToJava.Loader;

public class TestCompileAndRunJ
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
  load("")
  ),new AtomicTest(()->
  load("C={method This m(This x)=this.m(x=void) #norm{typeDep=This}}")//ill typed, but is norm so is skipped
  ),new AtomicTest(()->
  load("C={method This m(This x)=this.m(x=this) #typed{typeDep=This}}")
  ),new AtomicTest(()->
  load("""
  C={
    class method Void m()=(
      mut This1.SB sb=This1.SB<:class This1.SB.of()
      Void u1=sb.#a()
      Void u2=sb.#b()
      This1.S s0=sb.toS()
      s0.strDebug()
      )
    #typed{typeDep=This1.SB This1.S This coherentDep=This1.SB This1.S This
      usedMethods=This1.SB.of(), This1.SB.#a(), This1.SB.#b(), This1.SB.toS(), This1.S.strDebug()
      }}
  """)
  ),new AtomicTest(()->
  loadRun("""
  C={
    class method Void m()=(
      mut This1.SB sb=This1.SB<:class This1.SB.of()
      Void u1=sb.#a()
      Void u2=sb.#b()
      This1.S s0=sb.toS()
      s0.strDebug()
      )
    #typed{typeDep= This1.SB This1.S This coherentDep=This1.SB This1.S This
      usedMethods=This1.SB.of(), This1.SB.#a(), This1.SB.#b(), This1.SB.toS(), This1.S.strDebug()
      }}
  ""","(Void x=This0.C<:class This0.C.m() {#norm{}})","ab\n")
  ),new AtomicTest(()->
  loadRun("""
  C={
    class method Void m()=(
      This1.S s0=(error void)
      catch error Void x x
      void
      )
    #typed{typeDep=This1.S }}
  ""","(Void x=This0.C<:class This0.C.m() {#norm{}})","")
  ),new AtomicTest(()->
  loadRun("""
    I={interface method Void i() #typed{}}
    D={[This1.I] class method This of() method Void i()=void #typed{typeDep=This,This1.I,watched=This1.I, refined=i() nativeKind=Bool}}
    ""","(This.D x=This.D<:class This.D.of() Void v=x.i() {#norm{}})","")
//slaves
  ),new AtomicTest(()->
  loadRun("""
  SafeReadFile={
    class method This1.S #$read(This1.S fileName)=native{
      ioSlave{}{
      try( 
      java.util.stream.Stream<String>lines=java.nio.file.Files.lines(
        java.nio.file.Paths.get(#1+".txt")))
        {return lines.collect(java.util.stream.Collectors.joining("\\n"));}
      catch (java.io.IOException ioe) {return "";}
      }} error void
    #typed{typeDep=This1.S coherentDep=This1.S}
    }
  C={
    class method Void #$m()=(
      mut This1.SB sb=This1.SB<:class This1.SB.of()
      Void u1=sb.#a()
      Void u2=sb.#b()
      This1.S s3=This1.SafeReadFile<:class This1.SafeReadFile.#$read(fileName=sb.toS())
      s3.strDebug()
      )
    #typed{typeDep=This1.SB This1.S This1.SafeReadFile coherentDep=This1.SB This1.S This1.SafeReadFile
      usedMethods=This1.SB.of(), This1.SB.#a(), This1.SB.#b(), This1.SB.toS(), This1.SafeReadFile.#$read(fileName), This1.S.strDebug()
      }}
  ""","(Void x=This0.C<:class This0.C.#$m() {#norm{}})","Hello\nWorld\n")
  ),new AtomicTest(()-> 
  loadRun("""
  Safe2={
    class method This1.S #$go()=native{
      nativeSlave{}{
      try {
            java.net.URLClassLoader cl = new java.net.URLClassLoader(new java.net.URL[] {java.nio.file.Paths.get("testLibs/test.jar").toUri().toURL()});
            return (String)cl.loadClass("Test").getDeclaredMethod("sayHello").invoke(null);
        } catch (java.net.MalformedURLException | java.lang.reflect.InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
      }} error void
    #typed{typeDep=This1.S coherentDep=This1.S}
    }
  C={
    class method Void #$m()=(
      This1.S s3=This1.Safe2<:class This1.Safe2.#$go()
      s3.strDebug()
      )
    #typed{typeDep=This1.S This1.Safe2 coherentDep=This1.Safe2
      usedMethods=This1.Safe2.#$go(), This1.S.strDebug()
      }}
  ""","(Void x=This0.C<:class This0.C.#$m() {#norm{}})","Hello World!\n")
  ),new AtomicTest(()-> 
  loadRun("""
  Safe2={
    class method This1.S #$go()=native{
      nativeSlave{}{
      try {
            java.net.URLClassLoader cl = new java.net.URLClassLoader(new java.net.URL[] {java.nio.file.Paths.get("testLibs/native-test.jar").toUri().toURL()});
            Class<?> c = cl.loadClass("HelloWorld");
            return (String)c.getDeclaredMethod("print").invoke(c.newInstance());
        } catch (java.net.MalformedURLException | java.lang.reflect.InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
      }} error void
    #typed{typeDep=This1.SB This1.S coherentDep=This1.S}
    }
  C={
    class method Void #$m()=(
      This1.S s3=This1.Safe2<:class This1.Safe2.#$go()
      s3.strDebug()
      )
    #typed{typeDep=This1.SB This1.S This1.Safe2 coherentDep=This1.SB This1.S This1.Safe2
      usedMethods=This1.Safe2.#$go(), This1.S.strDebug()
      }}
  ""","(Void x=This0.C<:class This0.C.#$m() {#norm{}})","Hello Native World\n")
  ),new AtomicTest(()-> 
  loadRunErr("""
  Safe3={
    class method This1.S #$go()=native{
      nativeSlave{timeLimit:2}{
      while(true){
        if(false){break;}
        System.out.println("looping");
        }
      return "looped";
      }} error void
    #typed{typeDep=This1.SB This1.S coherentDep=This1.SB This1.S}
    }
  C={
    class method Void #$m()=(
      This1.S s3=This1.Safe3<:class This1.Safe3.#$go()
      s3.strDebug()
      )
    #typed{typeDep=This1.SB This1.S This1.Safe3 coherentDep=This1.SB This1.S This1.Safe3
      usedMethods=This1.Safe3.#$go(), This1.S.strDebug()
      }}
  ""","(Void x=This0.C<:class This0.C.#$m() {#norm{}})")


  ));}
public static void loadFail(String s){
  try{loadBase(base(s),false);fail();}
  catch(CompilationError ce){}
  }
public static void load(String s){
  try{loadBase(base(s),true);}
  catch(CompilationError ce){fail(ce.toString());}
  }

public static void loadRunErr(String s,String e){
  Resources.clearResKeepReuse();
  try{
    String p="{"+baseStr+s+"Task="+e+"}";
    Init init=new Init(p);
    init.topCache(new CachedTop(L(),L()));
    }
  catch(Error e1) {
    assertTrue(e1.getCause()!=null &&e1.getCause() instanceof java.util.concurrent.CancellationException);
    return;
    }
  fail("java.util.concurrent.CancellationException expected for timeout");
  }  

public static void loadRun(String s,String e,String output){
  Resources.clearResKeepReuse();
  String p="{"+baseStr+s+"Task="+e+"}";
  Init init=new Init(p);
  init.topCache(new CachedTop(L(),L()));
  assertEquals(output,Resources.out());
  }
public static String baseStr="""
  S={
    class method This0 of()
    method This0 sum(This0 that)=native{trusted:OP+} error void
    method Void strDebug()=native{trusted:strDebug} error void
    #typed{nativeKind=String,nativePar=This1.ParseErr
      typeDep=This This1.ParseErr
      watched=This1.ParseErr
      coherentDep=This,This1.ParseErr
      }
    }
  ParseErr={class method This of() #norm{nativeKind=LazyMessage typeDep=This}}
  SB={
    class method mut This0 of()
    mut method Void #a()=native{trusted:'a'} error void
    mut method Void #b()=native{trusted:'b'} error void
    read method This1.S toS()=native{trusted:toS} error void
    #typed{typeDep=This This1.S nativeKind=StringBuilder coherentDep=This1.S watched=This1.S}
    }
  A={
    class method mut This0 of(This1.S n)
    method This1.S n()
    mut method Void n(This1.S that)
    #typed{typeDep=This This1.S}} 
  """;
public static Program base(String s){
  String l="{ "+s+baseStr+"#norm{uniqueId=id1}}";
  return Program.parse(l);
  }
public static Loader loadBase(Program p,boolean print) throws CompilationError{
  Loader loader=new Loader();
  try{loader.loadNow(p,new JavaCodeStore(),new ArrayList<>());}
  catch(CompilationError ce){
    if(print){System.err.println(loader);}
    throw ce;
    }
  return loader;
  }
}