package is.L42.tests;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.common.EndError.InvalidImplements;
import is.L42.common.EndError.PathNotExistent;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.AtomicTest;
import is.L42.top.Top;
import is.L42.translationToJava.J;
import is.L42.translationToJava.Loader;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness.NotWellFormed;
import safeNativeCode.exceptions.SlaveException;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;


import static org.junit.jupiter.api.Assertions.*;

public class TestCompileAndRunJ
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
  load("")
  ),new AtomicTest(()->
  load("C={method This m(This x)=this.m(x=void) #norm{}}")//ill typed, but is norm so is skipped
  ),new AtomicTest(()->
  loadFail("C={method This m(This x)=this.m(x=void) #typed{}}")
  ),new AtomicTest(()->
  load("C={method This m(This x)=this.m(x=this) #typed{}}")
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
    #typed{}}
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
    #typed{}}
  ""","(Void x=This0.C<:class This0.C.m() {#norm{}})","ab")
  ),new AtomicTest(()->
  loadRun("""
  C={
    class method Void m()=(
      This1.S s0=(error void)
      catch error Void x x
      void
      )
    #typed{}}
  ""","(Void x=This0.C<:class This0.C.m() {#norm{}})","")

  ),new AtomicTest(()->
  loadRun("""
  SafeReadFile={
    class method This1.S read(This1.S fileName)=native{
      ioSlave{}
      {try(//I may have messed up some of the code, 
      java.util.stream.Stream<String>lines=java.nio.file.Files.lines(
        java.nio.file.Paths.get(#1+".txt")))
        {return lines.collect(java.util.stream.Collectors.joining("\\n"));}
      catch (java.io.IOException ioe) {return "";}
      }} error void
    #typed{}
    }
  C={
    class method Void m()=(
      This1.SB sb=This1.SB<:class This1.SB.of()
      Void u1=sb.#a()
      Void u2=sb.#b()
      This1.S s3=This1.SafeReadFile<:class This1.SafeReadFile.read(fileName=sb.toS())
      s3.strDebug()
      )
    #typed{}}
  ""","(Void x=This0.C<:class This0.C.m() {#norm{}})","Hello\nWorld")
  ),new AtomicTest(()-> 
  loadRun("""
  Safe2={
    class method This1.S go()=native{
      nativeSlave{}
      {
      try {
            java.net.URLClassLoader cl = new java.net.URLClassLoader(new java.net.URL[] {java.nio.file.Paths.get("testLibs/test.jar").toUri().toURL()});
            return (String)cl.loadClass("Test").getDeclaredMethod("sayHello").invoke(null);
        } catch (java.net.MalformedURLException | java.lang.reflect.InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
      }} error void
    #typed{}
    }
  C={
    class method Void m()=(
      This1.S s3=This1.Safe2<:class This1.Safe2.go()
      s3.strDebug()
      )
    #typed{}}
  ""","(Void x=This0.C<:class This0.C.m() {#norm{}})","Hello World!")
  ),new AtomicTest(()-> 
  loadRun("""
  Safe2={
    class method This1.S go()=native{
      nativeSlave{}
      {
      try {
            java.net.URLClassLoader cl = new java.net.URLClassLoader(new java.net.URL[] {java.nio.file.Paths.get("testLibs/native-test.jar").toUri().toURL()});
            Class<?> c = cl.loadClass("HelloWorld");
            return (String)c.getDeclaredMethod("print").invoke(c.newInstance());
        } catch (java.net.MalformedURLException | java.lang.reflect.InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
      }} error void
    #typed{}
    }
  C={
    class method Void m()=(
      This1.S s3=This1.Safe2<:class This1.Safe2.go()
      s3.strDebug()
      )
    #typed{}}
  ""","(Void x=This0.C<:class This0.C.m() {#norm{}})","Hello Native World")
  ),new AtomicTest(()-> 
  loadRunErr("""
  Safe3={
    class method This1.S go()=native{
      nativeSlave{
        timeLimit:2
        }
      {
      while(true){
        if(false){break;}
        System.out.println("looping");
        }
      return "looped";
      }} error void
    #typed{}
    }
  C={
    class method Void m()=(
      This1.S s3=This1.Safe3<:class This1.Safe3.go()
      s3.strDebug()
      )
    #typed{}}
  ""","(Void x=This0.C<:class This0.C.m() {#norm{}})")


  ));}
public static void loadFail(String s){
  try{loadBase(base(s),false);fail();}
  catch(CompilationError ce){}
  }
public static void load(String s){
  try{loadBase(base(s),true);}
  catch(CompilationError ce){fail(ce);}
  }

public static void loadRunErr(String s,String e){
  Resources.clearRes();
  try{
    Program p=base(s);
    Loader l;try{l=loadBase(p,true);}
    catch(CompilationError ce){fail(ce);throw bug();}
    String code="{ method Library m()="+e+" #norm{uniqueId=id1}}";
    var p2=Program.parse(code);
    try {l.runNow(p, new C("Task",-1),p2.topCore().mwts().get(0)._e());}
    catch (InvocationTargetException e1) {
      if(!(e1.getCause() instanceof java.util.concurrent.CancellationException)){fail(e1.getCause());}
//      assertEquals("loopinglooping",Resources.out());
      return;
      }
    catch (CompilationError e1) {fail(e1);}
    fail("java.util.concurrent.CancellationException expected for timeout");
    //TODO: is there a better unchecked exception in java to use to this aim?
    }
  finally{Resources.clearRes();}
  }  

public static void loadRun(String s,String e,String output){
  Resources.clearRes();
  try{
    Program p=base(s);
    //somehow using a switch expression makes junit fail
    Loader l;try{l=loadBase(p,true);}
    catch(CompilationError ce){fail(ce);throw bug();}
    String code="{ method Library m()="+e+" #norm{uniqueId=id1}}";
    var p2=Program.parse(code);
    try {l.runNow(p, new C("Task",-1),p2.topCore().mwts().get(0)._e());}
    catch (InvocationTargetException e1) {fail(e1.getCause());}
    catch (CompilationError e1) {fail(e1);}
    assertEquals(output,Resources.out());
    }
  finally{Resources.clearRes();}
  }
public static Program base(String s){
  String l="{ "+s+
  """
  S={
    class method This0 of()
    method This0 sum(This0 that)=native{trusted:OP+} error void
    method Void strDebug()=native{trusted:strDebug} error void
    #typed{nativeKind=String}
    }
  SB={
    class method mut This0 of()
    mut method Void #a()=native{trusted:'a'} error void
    mut method Void #b()=native{trusted:'b'} error void
    read method This1.S toS()=native{trusted:toS} error void
    #typed{nativeKind=StringBuilder}
    }
  A={
    class method mut This0 of(This1.S n)
    method This1.S n()
    mut method Void n(This1.S that)
    #typed{}} 
  #norm{uniqueId=id1}}
  """;
  return Program.parse(l);
  }
public static Loader loadBase(Program p,boolean print) throws CompilationError{
  Loader loader=new Loader();
  try{loader.loadNow(p);}
  catch(CompilationError ce){
    if(print){System.err.println(loader);}
    throw ce;
    }
  return loader;
  }
}