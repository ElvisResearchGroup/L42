package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

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
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness.NotWellFormed;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestProgram
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
//WellFormedness
   passWF("{method Void (Void that,Void foo)=foo}")
   ),new AtomicTest(()->
   failWF(NotWellFormed.class,"{method Void a::1(Void that,Void foo)=foo D={C::1={}}}",Err.nonUniqueNumber("1",hole))
   ),new AtomicTest(()->
   failWF(NotWellFormed.class,"{method Void a::1(Void that,Void foo)=foo} A={ A=void C::1={}}",Err.nonUniqueNumber("1",hole))
   ),new AtomicTest(()->

   passWF("{method Void a(This3.Foo f)}"+emptyP)
   ),new AtomicTest(()->
   passWF("{C={method Void a(This3.Foo f)}}"+emptyP)
   ),new AtomicTest(()->
   passWF("{C={[This3.Foo]}}"+emptyP)
   ),new AtomicTest(()->

   failWF(PathNotExistent.class,"{method Void a(This3.Foo f)}",Err.thisNumberOutOfScope("This3.Foo"))
   ),new AtomicTest(()->
   failWF(PathNotExistent.class,"{C={method Void a(This3.Foo f)}}",Err.thisNumberOutOfScope("This3.Foo"))
   ),new AtomicTest(()->
   failWF(PathNotExistent.class,"{C={[This3.Foo]}}",Err.thisNumberOutOfScope("This3.Foo"))
   ),new AtomicTest(()->

//---test minimize path
   minimize("{method Void (Void that,Void foo)=foo}","This0.Foo","This0.Foo")
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo}","Library","Library")
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo} A={A={}}","This1.B.C","This1.B.C")
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo} A={A={}}","This1.A.C","This0.C")
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo} A={A={}} B={B={}}","This2.B.A.C","This0.C")
   ),new AtomicTest(()->
   minimize("{} A={A={}} B={B={A={}}}"+emptyP,"This2.B.A","This")
   ),new AtomicTest(()->
   minimize("{} A={A={}} B={B={A={}}}"+emptyP,"This1.A","This")
   ),new AtomicTest(()->   
//----testing from
   from(emptyP,"This2.A","This.B.C","This.A")
   ),new AtomicTest(()->
   from(emptyP,"This2.A","This.B.C","This.A")
   ),new AtomicTest(()->
   from(emptyP,"This1.C", "This0.C","This0.C")
   ),new AtomicTest(()->
   from(emptyP,"This1.Foo.Bar", "This3.Beer.Baz","This3.Beer.Foo.Bar")
   ),new AtomicTest(()->
   from(emptyP+emptyP+emptyP, "This1.A.B", "This10.C.D","This10.C.A.B")
   ),new AtomicTest(()->
   from(emptyP+emptyP+emptyP,"This10.A.B", "This3.C.D","This11.A.B")
   ),new AtomicTest(()->
   from(emptyP,"This2.A.B", "This2.C.D","This2.A.B")
   ),new AtomicTest(()->
   from(emptyP,"This0.A.B.C.D.E", "This0.A1.B1.C2.D1","This0.A1.B1.C2.D1.A.B.C.D.E")
   ),new AtomicTest(()->
   from(emptyP,"This0.A.B.C.D.E", "This1.A1.B1.C2.D1","This1.A1.B1.C2.D1.A.B.C.D.E")
   ),new AtomicTest(()->
   from(emptyP,"This0.A.B.C.D.E", "This2.A1.B1.C2.D1","This2.A1.B1.C2.D1.A.B.C.D.E")
   ),new AtomicTest(()->
   from(emptyP,"This1.A.B.C.D.E", "This0.A1.B1.C2.D1","This0.A1.B1.C2.A.B.C.D.E")
   ),new AtomicTest(()->
   from(emptyP,"This2.A.B.C.D.E", "This0.A1.B1.C2.D1","This0.A1.B1.A.B.C.D.E")
   ),new AtomicTest(()->
   from(emptyP,"This3.A.B.C.D.E", "This0.A1.B1.C2.D1","This0.A1.A.B.C.D.E")
   ),new AtomicTest(()->
   from(emptyP,"This3.A.B.C.D.E", "This1.A1.B1.C2.D1","This1.A1.A.B.C.D.E")
   ),new AtomicTest(()->
   from(emptyP,"This3.A.B.C.D.E", "This2.A1.B1.C2.D1","This2.A1.A.B.C.D.E")
   ),new AtomicTest(()->
   from(emptyP,"This0", "This1","This1")
   ),new AtomicTest(()->
   from(emptyP,"This2.B","This1.C.A","This1.B")
   ),new AtomicTest(()->
//------testing fromE
  fromE(emptyP,"{ method Any m()=This3.A<:This.m() #norm{}}",
    "This2.B", "{ method Any m()=This4.A<:This.m() #norm{}}")
  ),new AtomicTest(()->
  fromE(emptyP,"{ C={ method Any m()=This3.A<:This.m() #norm{}} #norm{}}",
    "This2.B", "{ C={ method Any m()=This4.A<:This.m() #norm{}} #norm{}}")
  ),new AtomicTest(()->
  fromE(emptyP,"{ C={ method Any m()=This1.A<:This.m() #norm{}} #norm{}}",
    "This2.B", "{ C={ method Any m()=This1.A<:This.m() #norm{}} #norm{}}")
  ),new AtomicTest(()->
  fromE(emptyP+emptyP+emptyP,"{ C={ method Any m()=This1.A<:This.m() #norm{}} #norm{}}",
    "This10.B","{ C={ method Any m()=This1.A<:This.m() #norm{}} #norm{}}")
  ),new AtomicTest(()->
  fromE(emptyP,"{ C={ method Any m()=This1.A<:This.m() #norm{}} #norm{}}",
    "This0.B","{ C={ method Any m()=This1.A<:This.m() #norm{}} #norm{}}")
  ),new AtomicTest(()->
  fromE(emptyP,"{method This2.B #apply() #norm{}}",
    "This1.C.A","{method This2.C.B #apply() #norm{}}")
  ),new AtomicTest(()->
  fromE(emptyP,"{ method Void m()[Void]= void #norm{}}", "This2.B",
  "{ method Void m()[Void]= void  #norm{}}")
  ),new AtomicTest(()->
  fromE(emptyP,"This0<:Any.foo(bar={[This1] #norm{}})","This0.C",
               "This0.C<:Any.foo(bar={[This1.C] #norm{}})")
  ),new AtomicTest(()->
  fromE(emptyP,"This2.B<:This2.B", "This1.C.A","This1.B<:This1.B")
  ),new AtomicTest(()->
  fromE(emptyP,"(This0.B x1=void,This1.B x2=void,This2.B x3=void,This0 x4=void, {[This1, This0.A, This1.A,This2.A]#norm{}})",
   "This0.C",//as in D:C.m()  C={ m { implements This1.A,This2.A }}
   "(This0.C.B x1=void,This0.B x2=void,This1.B x3=void,This0.C x4=void, {[This1.C, This0.A, This1.C.A,This1.A]#norm{}})")
  ),new AtomicTest(()->
  fromE(emptyP,"(This0.B x1=void This1.B x2=void This2.B x3=void This0 x4=void { D={[This2, This1.A, This2.A,This3.A] #norm{}} #norm{}})",
    "This0.C",//as in D:C.m()  C={ m { implements This1.A,This2.A }}
    "(This0.C.B x1=void This0.B x2=void This1.B x3=void This0.C x4=void { D={[This2.C, This1.A, This2.C.A,This2.A] #norm{}} #norm{}})")
  ),new AtomicTest(()->
  fromE(emptyP,"(This0.B x1=void This1.B x2=void This2.B x3=void This0 x4=void {[This1, This0.A, This1.A,This2.A]#norm{}})",
    "This1.C",//as in D:This1.C.m()  C={ m { implements This1.A,This2.A }}
    "(This1.C.B x1=void This1.B x2=void This2.B x3=void This1.C x4=void {[This2.C, This0.A, This2.C.A,This2.A]#norm{}})")
  ),new AtomicTest(()->
  fromE(emptyP,"(This0.B x1=void This1.B x2=void This2.B x3=void This0 x4=void { D={[This2, This1.A, This2.A,This3.A]#norm{}} #norm{}})",
     "This1.C",//as in D:This1.C.m()  C={ m { implements This1.A,This2.A }}
     "(This1.C.B x1=void This1.B x2=void This2.B x3=void This1.C x4=void { D={[This3.C, This1.A, This3.C.A,This3.A]#norm{}} #norm{}})")

  ),new AtomicTest(()->
  fromE("{} A={A={}} B={B={A={}}}"+emptyP,"(This2.B.A x1=void This1.A x2=void This2.B x3=void This3.B.A x4=void void)",
        "This1.C",                 "(This x1=void This x2=void This1 x3=void This3.B.A x4=void void)")
//collect
  ),new AtomicTest(()->
  collect("{} A={A={}} B={B={A={}}}"+emptyP,"This","[]")
  ),new AtomicTest(()->
  collect("{[This.B] B={interface}}"+emptyP,"This","[imm This0.B]")
  ),new AtomicTest(()->
  collectFail(InvalidImplements.class,"{[This.B] B={}}"+emptyP,"This",Err.notInterfaceImplemented())
  ),new AtomicTest(()->
  collect("{[B, B] B={interface}}"+emptyP,"This","[imm This0.B]")
  ),new AtomicTest(()->
  collectFail(PathNotExistent.class,"{[This.B, This.A] B={interface}}"+emptyP,"This",Err.pathNotExistant("This0.A"))
  ),new AtomicTest(()->
  collect("{[This.B, This.A] A={interface}B={interface[This1.A]}}"+emptyP,"This","[imm This0.B, imm This0.A]")
  ),new AtomicTest(()->
  collect("{[This.A, This.B] A={interface}B={interface[This1.A]}}"+emptyP,"This","[imm This0.B, imm This0.A]")//order tweaked in top
  ),new AtomicTest(()->
  collect("{[This.B] A={interface}B={interface[This1.A]}}"+emptyP,"This","[imm This0.B, imm This0.A]")
  ),new AtomicTest(()->
  collect("{[This.B] A={interface} B={interface[This2.A]}} C={C={} A={interface [C.A]}}"+emptyP,"This","[imm This0.B, imm This1.A, imm This0.A]")
  ),new AtomicTest(()->
  collect("{[This.A This.B] A={interface}B={interface[This2.A]}} C={C={} A={interface [C.A]}}"+emptyP,"This","[imm This0.B, imm This1.A, imm This0.A]")
  ),new AtomicTest(()->
  collect("{[This.B] A={interface}B={interface[This2.A, A]}} C={C={} A={interface [C.A]}}"+emptyP,"This","[imm This0.B, imm This1.A, imm This0.A]")
  ),new AtomicTest(()->
  collectFail(InvalidImplements.class,"{[This.A] A={interface [B]} B={interface[A]}}"+emptyP,"This",Err.circularImplements(hole))

  ),new AtomicTest(()->
  methods("{ method Void v()}"+emptyP,"This","[imm method imm Void v()]")
  ),new AtomicTest(()->
  methods("{ method Void v() method Any g(Any that)[Library]}"+emptyP,"This","[imm method imm Void v(), imm method imm Any g(imm Any that)[Library]]")
  ),new AtomicTest(()->
  methods("{[A] method Void v() A={interface method A a()}}"+emptyP,"This","[imm method imm Void v(), imm method imm This0.A a()]")
  ),new AtomicTest(()->
  methods("{[A,B] A={interface [C]} B={interface [C]} C={interface method A a()}}"+emptyP,"This","[imm method imm This0.A a()]")
  ),new AtomicTest(()->
  methods("{[A,B] A={interface [C] method Void a()} B={interface [C] method Any a()} C={interface method Any a()}}"+emptyP,"This","[imm method imm Void a()]")
  ),new AtomicTest(()->
  methods("{[B,A] A={interface [C] method Void a()} B={interface [C] method Any a()} C={interface method Any a()}}"+emptyP,"This","[imm method imm Any a()]")
  ),new AtomicTest(()->
  methodsFail(InvalidImplements.class,"{[B,A] A={interface [C] method Void a()} B={interface [C] method Any a()} C={interface}}"+emptyP,"This",Err.moreThenOneMethodOrigin("a()", hole))
  ),new AtomicTest(()->
  methodsFail(InvalidImplements.class,"{[B,A] A={interface method Any a()} B={interface method Any a()}}"+emptyP,"This",Err.moreThenOneMethodOrigin("a()", hole))
  ),new AtomicTest(()->
  methods("{}"+emptyP,"This","[]")
  ),new AtomicTest(()->
  methods("{I={interface method Any m()} A={[I]}}","This0.I","[imm method imm Any m()]")
  ),new AtomicTest(()->
  methods("{I={interface method Any m()} A={[I]}}","This0.A","[imm method imm Any m()]")
  ),new AtomicTest(()->
  methods("{I2={interface method Any m2()} I1={interface method Any m1()} A={[I1,I2]}}","This0.A","[imm method imm Any m1(), imm method imm Any m2()]")
  ),new AtomicTest(()->
  methods("{I0={interface method Any m0()} I2={interface [I0] method Any m2()} I1={interface [I0] method Any m1()} A={[I1,I2]}}","This0.A","[imm method imm Any m1(), imm method imm Any m0(), imm method imm Any m2()]")
  ),new AtomicTest(()->
  methods("{I0={interface method Any m0()} I2={interface [I0] method Any m2() method Void m0()} I1={interface [I0] method Any m1()} A={[I1,I2]}}","This0.A","[imm method imm Any m1(), imm method imm Any m0(), imm method imm Any m2()]")
  ),new AtomicTest(()->
  methods("{I0={interface method Any m0()} I2={interface [I0] method Any m2() method Void m0()} I1={interface [I0] method Any m1()} A={[I2,I1]}}","This0.A","[imm method imm Any m2(), imm method imm Void m0(), imm method imm Any m1()]")

  ),new AtomicTest(()->
  toS("{[This1.I]}\nA={J={interface method This0 m()}I={interface[This1.J]}A={[This1.I]}}\n")
  ),new AtomicTest(()->
  toS("{}\nA={A={}}\n")
  ),new AtomicTest(()->
  toS("{}\nA={A={}}\nB={B={A={}}}\n")

  ));}
private static String emptyP="{#norm{}}{#norm{}}{#norm{}}{#norm{}}{#norm{}}";
public static String inCore(String s){
  return "{imm method imm Void a()="+s+" #norm{}}";
  }
public static void toS(String program){
  assertEquals(Program.parse(program).toString(),program);
  }

public static void minimize(String program,String pathIn,String pathOut){
  assertEquals(Program.parse(program).minimize(P.parse(pathIn)),P.parse(pathOut));
  }
public static void collect(String program,String pathIn,String out){
  assertEquals(Program.parse(program).collect(P.parse(pathIn).toNCs(),null).toString(),out);
  }
public static void methods(String program,String pathIn,String out){
  assertEquals(Program.parse(program).methods(P.parse(pathIn).toNCs(),null).toString(),out);
  }

public static void from(String program,String pathIn,String pathSource,String pathOut){
  assertEquals(Program.parse(program).from(P.parse(pathIn),P.parse(pathSource).toNCs()),P.parse(pathOut));
  }
public static void fromE(String program,String eIn,String pathSource,String eOut){
  assertEquals(Program.parse(program).from(Core.E.parse(eIn),P.parse(pathSource).toNCs()),Core.E.parse(eOut));
  }

public static void passWF(String input){
  var p=Program.parse(input);//internally calls wf
  assertTrue(p.wf());//to be more resilient to changes above
  }
public static void failWF(Class<?> clazz,String input,String ...output){
  var r=Parse.program("-dummy-",input);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  TestHelpers.checkFail(()->Program.parse(input),output,clazz);
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
}

interface I{static int a(){return 0;}}
class A implements I{static int a(){return 1;}
  static void m(){
    A.a();
    I.a();
    }
  }