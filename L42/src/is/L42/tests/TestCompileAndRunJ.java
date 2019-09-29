package is.L42.tests;

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
import is.L42.common.Program.InvalidImplements;
import is.L42.common.Program.PathNotExistent;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.javaTranslation.Loader;
import is.L42.tools.AtomicTest;
import is.L42.top.Top;
import is.L42.translationToJava.J;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness.NotWellFormed;

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
      This1.S s0=This1.S<:class This1.S.of()
      This1.S s1=s0._a()
      This1.S s2=s1._b()
      s2.strDebug()
      )
    #typed{}}
  """)
  ));}
public static void loadFail(String s){
  try{loadBase(base(s),false);fail();}
  catch(CompilationError ce){}
  }
public static void load(String s){
  try{loadBase(base(s),true);}
  catch(CompilationError ce){fail(ce);}
  }
public static void loadRun(String s,String e){
  Program p=base(s);
  Loader l=switch(0){default->{
    try{yield loadBase(p,true);}
    catch(CompilationError ce){fail(ce);throw bug();}
    }};
  l.runNow(p, new C("Task"), p.)
  }

public static Program base(String s){
  String l="{ "+s+
  """
  S={
    class method This0 of()
    method This0 sum(This0 that)=native{trusted:OP+} error void
    method This0 _a()=native{trusted:_a} error void
    method This0 _b()=native{trusted:_b} error void
    method Void strDebug()=native{trusted:strDebug} error void
    #typed{nativeKind=String}
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