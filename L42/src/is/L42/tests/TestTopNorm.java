package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.common.Program.InvalidImplements;
import is.L42.common.Program.PathNotExistent;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.tools.AtomicTest;
import is.L42.top.Top;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness.NotWellFormed;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestTopNorm
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   top("{}","{#norm{}}")
   
 //collect
   ),new AtomicTest(()->
   top("{} A={A={}} B={B={A={}}}"+emptyP,"{#norm{}}")
   ),new AtomicTest(()->
   topFail(InvalidImplements.class,"{[This.B] B={interface}}",Err.nestedClassesImplemented(hole))
   ),new AtomicTest(()->
   topFail(InvalidImplements.class,"{B={} A={[This1.B]}}"+emptyP,Err.notInterfaceImplemented())
   ),new AtomicTest(()->
   top("{B={interface} A={[B, B]}}"+emptyP,"{B={interface #norm{}} A={[This1.B]#norm{typeDep=This1.B}}#norm{}}")
   ),new AtomicTest(()->
   topFail(PathNotExistent.class,"{[This1.A]}"+emptyP,Err.pathNotExistant("This1.A"))
   ),new AtomicTest(()->
   top("{A={interface}B={interface[This1.A]}C={[This1.B, This1.A]}}"+emptyP,"""
     {A={interface #norm{}}
      B={interface[This1.A]#norm{typeDep=This1.A}}
      C={[This1.B, This1.A]#norm{typeDep=This1.B, This1.A}}
      #norm{}
      }""")
   ),new AtomicTest(()->
   top("{A={interface}B={interface[This1.A]}C={[This1.A, This1.B]}}"+emptyP,"""
     {A={interface #norm{}}
      B={interface[This1.A]#norm{typeDep=This1.A}}
      C={[This1.A, This1.B]#norm{typeDep=This1.A, This1.B}}
      #norm{}
      }""")
   ),new AtomicTest(()->
   top("{A={interface}B={interface[This1.A]}C={[This1.B]}}"+emptyP,"""
     {A={interface #norm{}}
      B={interface[This1.A]#norm{typeDep=This1.A}}
      C={[This1.B, This1.A]#norm{typeDep=This1.B, This1.A}}
      #norm{}
      }""")
    ),new AtomicTest(()->
    topFail(InvalidImplements.class,"{A={interface [B]} B={interface[A]}}",Err.nestedClassesImplemented(hole))
  
    ),new AtomicTest(()->
    top("{ method Void v()}"+emptyP,"{ method Void v() #norm{}}")
    ),new AtomicTest(()->
    top("{ method Void v() method Any g(Any that)[Library]}"+emptyP,
    "{ method Void v() method Any g(Any that)[Library] #norm{}}")
    ),new AtomicTest(()->
    top("{A={interface method A a()} C={[A] method Void v()}}"+emptyP,"""
     {A={interface method This a() #norm{typeDep=This0}} 
      C={[This1.A] method Void v()
          imm method imm This1.A a() #norm{typeDep=This1.A}
          } #norm{}}
     """)
    ),new AtomicTest(()->
    top("{C={interface method A a()} A={interface [C]} B={interface [C]} D={[A,B]}}"+emptyP,"""
    {C={interface method This1.A a() #norm{typeDep=This1.A}}
     A={interface [This1.C] method This a() #norm{typeDep=This1.C, This}}
     B={interface [This1.C] method This1.A a() #norm{typeDep=This1.C, This1.A}}
     D={[This1.A,This1.B,This1.C]imm method imm This1.A a() #norm{typeDep=This1.A, This1.B,This1.C}}
    #norm{}}""")
    ),new AtomicTest(()->
    top("{C={interface method Any a()} A={interface [C] method Void a()} B={interface [C] method Any a()} D={[A,B]}}"+emptyP,"""
    {C={interface method Any a() #norm{}}
     A={interface [This1.C] method Void a() #norm{typeDep=This1.C}}
     B={interface [This1.C] method Any a() #norm{typeDep=This1.C}}
     D={[This1.A,This1.B,This1.C]imm method imm Void a() #norm{typeDep=This1.A, This1.B,This1.C}}
    #norm{}}""")
   ),new AtomicTest(()->
    top("{C={interface method Any a()} A={interface [C] method Void a()} B={interface [C] method Any a()} D={[B,A]}}"+emptyP,"""
    {C={interface method Any a() #norm{}}
     A={interface [This1.C] method Void a() #norm{typeDep=This1.C}}
     B={interface [This1.C] method Any a() #norm{typeDep=This1.C}}
     D={[This1.B,This1.A,This1.C]imm method imm Any a() #norm{typeDep=This1.B, This1.A,This1.C}}
    #norm{}}""")
    ),new AtomicTest(()->
    topFail(InvalidImplements.class,
    "{C={interface } A={interface [C] method Void a()} B={interface [C] method Any a()} D={[B,A]}}",
    Err.moreThenOneMethodOrigin("a()", hole))
    ),new AtomicTest(()->
    topFail(InvalidImplements.class,"{A={interface method Any a()} B={interface method Any a()} C={[B,A]}}"+emptyP,Err.moreThenOneMethodOrigin("a()", hole))
    ),new AtomicTest(()->
    top("{I={interface method Any m()} A={[I]}}","""
    {I={interface method Any m()#norm{}}
     A={[This1.I] method Any m()#norm{typeDep=This1.I}}
    #norm{}}""")
    ),new AtomicTest(()->
    top("{I2={interface method Any m2()} I1={interface method Any m1()} A={[I1,I2]}}","""
    {I2={interface method Any m2()#norm{}}
     I1={interface method Any m1()#norm{}}
     A={[This1.I1,This1.I2] method Any m1() method Any m2()#norm{typeDep=This1.I1,This1.I2}}
    #norm{}}""")
    ),new AtomicTest(()->
    top("{I0={interface method Any m0()} I2={interface [I0] method Any m2()} I1={interface [I0] method Any m1()} A={[I1,I2]}}","""
    {I0={interface method Any m0()#norm{}}
     I2={interface [This1.I0] method Any m2()method Any m0()#norm{typeDep=This1.I0}}
     I1={interface [This1.I0] method Any m1()method Any m0()#norm{typeDep=This1.I0}}
     A={[This1.I1,This1.I2,This1.I0] method Any m1() method Any m0()method Any m2()#norm{typeDep=This1.I1,This1.I2,This1.I0}}
    #norm{}}""")//TODO: is this really the method order we want? see also next test

    ),new AtomicTest(()->
    top("{I0={interface method Any m0()} I2={interface [I0] method Any m2() method Void m0()} I1={interface [I0] method Any m1()} A={[I1,I2]}}","""
    {I0={interface method Any m0()#norm{}}
     I2={interface [This1.I0] method Any m2()method Void m0()#norm{typeDep=This1.I0}}
     I1={interface [This1.I0] method Any m1()method Any m0()#norm{typeDep=This1.I0}}
     A={[This1.I1,This1.I2,This1.I0] method Any m1() method Any m0() method Any m2()#norm{typeDep=This1.I1,This1.I2,This1.I0}}
    #norm{}}""")
    ),new AtomicTest(()->
    top("{I0={interface method Any m0()} I2={interface [I0] method Any m2() method Void m0()} I1={interface [I0] method Any m1()} A={[I2,I1]}}","""
    {I0={interface method Any m0()#norm{}}
     I2={interface [This1.I0] method Any m2()method Void m0()#norm{typeDep=This1.I0}}
     I1={interface [This1.I0] method Any m1()method Any m0()#norm{typeDep=This1.I0}}
     A={[This1.I2,This1.I1,This1.I0] method Any m2() method Void m0() method Any m1()#norm{typeDep=This1.I2,This1.I1,This1.I0}}
    #norm{}}""")

 //WellFormedness
   ),new AtomicTest(()->
   top("{C={}}","{C={#norm{}}#norm{}}")
   ),new AtomicTest(()->
   top("{method Any foo()=void}","{method Any foo()=void #norm{}}")

   ),new AtomicTest(()->
   topFail(InvalidImplements.class,"{[I], I={interface }}",Err.nestedClassesImplemented(hole))
   ),new AtomicTest(()->
   topFail(InvalidImplements.class,"{A={[I]} J={interface method This m()} I={interface [J] method This m()}}",Err.nestedClassesImplemented(hole))

   ),new AtomicTest(()->
   top("{[I]}A={I={interface #norm{}} #norm{}}","{[This1.I] #norm{typeDep=This1.I}}")
   ),new AtomicTest(()->
   top("{[I]}A={J={interface #norm{}} I={interface [This1.J]#norm{}} #norm{}}","{[This1.I,This1.J] #norm{typeDep=This1.I,This1.J}}")
   ),new AtomicTest(()->
   top("{[I]}A={J={interface method This m()#norm{}} I={interface [This1.J]#norm{}}#norm{}}","{[This1.I,This1.J] method This1.J m() #norm{typeDep=This1.I,This1.J}}")
   ),new AtomicTest(()->
   top("{[I]}A={J={interface method This m()#norm{}} I={interface [This1.J] method This m()#norm{}}#norm{}}","{[This1.I,This1.J] method This1.I m() #norm{typeDep=This1.I,This1.J}}")

   ),new AtomicTest(()->
   top(
   "{J={interface method This m()} I={interface [J] method This m()} A={[I]} }",
   """
   {J={interface imm method imm This0 m()#norm{typeDep=This0}}
   I={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   A={[This1.I, This1.J]imm method imm This1.I m()#norm{typeDep=This1.I, This1.J}}
   #norm{}}
   """)
   ),new AtomicTest(()->top("""
   {J={interface method This m()}
   I1={interface [J] method This m()}
   I2={interface [J] method This m()}
   A={[I1,I2]} }
   ""","""
   {J={interface imm method imm This0 m()#norm{typeDep=This0}}
   I1={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   I2={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   A={[This1.I1,This1.I2 This1.J]imm method imm This1.I1 m()#norm{typeDep=This1.I1,This1.I2,This1.J}}#norm{}}
   """)

   ),new AtomicTest(()->top("""
   {J={interface method This m()}
   I1={interface [J] method This m()}
   I2={interface [J] method This m()}
   A={[I1,I2] method m()=this} }
   ""","""
   {J={interface imm method imm This0 m()#norm{typeDep=This0}}
   I1={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   I2={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   A={[This1.I1,This1.I2 This1.J]imm method imm This1.I1 m()=this #norm{typeDep=This1.I1,This1.I2,This1.J}}#norm{}}
   """)
   ),new AtomicTest(()->top("""
   {J={interface method This m()}
   I1={interface [J] method This m()}
   I2={interface [J] method This m()}
   B={C={A={[I1,I2] method m()=this}}} }
   ""","""
   {J={interface imm method imm This0 m()#norm{typeDep=This0}}
   I1={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   I2={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   B={C={A={[This3.I1,This3.I2 This3.J]imm method imm This3.I1 m()=this #norm{typeDep=This3.I1,This3.I2,This3.J}}#norm{}}#norm{}}#norm{}}
   """)


  ));}
private static String emptyP="{#norm{}}{#norm{}}{#norm{}}{#norm{}}{#norm{}}";

public static void top(String program,String out){
  assertEquals(new Top().top(L(),Program.parse(program)).p().top,Core.L.parse(out));
  }
public static void topFail(Class<?> kind,String program,String ...output){
  checkFail(()->new Top().top(L(),Program.parse(program)), output, kind);
  }
/*
public static void collect(String program,String pathIn,String out){
  assertEquals(Program.parse(program).collect(P.parse(pathIn).toNCs(),null).toString(),out);
  }
public static void methods(String program,String pathIn,String out){
  assertEquals(Program.parse(program).methods(P.parse(pathIn).toNCs(),null).toString(),out);
  }
public static void collectFail(Class<?> clazz,String program,String pathIn,String... output){
  Program p=Program.parse(program);
  var pos=p.of(P.parse(pathIn).toNCs(),null).poss();
  TestHelpers.checkFail(()->p.collect(P.parse(pathIn).toNCs(),pos),output,clazz);
  }
public static void methodsFail(Class<?> clazz,String program,String pathIn,String... output){
  Program p=Program.parse(program);
  var pos=p.of(P.parse(pathIn).toNCs(),null).poss();
  TestHelpers.checkFail(()->p.methods(P.parse(pathIn).toNCs(),pos),output,clazz);
  }
*/
}