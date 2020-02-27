package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.EndError.InvalidImplements;
import is.L42.common.EndError.PathNotExistent;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.tools.AtomicTest;
import is.L42.visitors.FullL42Visitor;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestProgram
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
//WellFormedness
   passWF("{method Void (Void that,Void foo)=foo}")
   ),new AtomicTest(()->
   failWF(EndError.NotWellFormed.class,"{method Void a::1(Void that,Void foo)=foo D={C::1={#norm{}} #norm{}}}",Err.nonUniqueNumber("[1]",hole))
//   ),new AtomicTest(()->//can not be tested now, since we can not have C::1 in a metaprogramming context
//   failWF(EndError.NotWellFormed.class,"{method Void a::1(Void that,Void foo)=foo} A= ={ A=void C::1={#norm{}}#norm{}}",Err.nonUniqueNumber("[1]",hole))
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
   minimize("{method Void (Void that,Void foo)=foo} A= ={A={}}","This1.B.C","This1.B.C")
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo} A= ={A={}}","This1.A.C","This0.C")
   ),new AtomicTest(()->
   minimize("{method Void (Void that,Void foo)=foo} A= ={A={}} B= ={B={}}","This2.B.A.C","This0.C")
   ),new AtomicTest(()->
   minimize("{} A= ={A={}} B= ={B={A={}}}"+emptyP,"This2.B.A","This")
   ),new AtomicTest(()->
   minimize("{} A= ={A={}} B= ={B={A={}}}"+emptyP,"This1.A","This")
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
  fromE("{} A= ={A={}} B= ={B={A={}}}"+emptyP,"(This2.B.A x1=void This1.A x2=void This2.B x3=void This3.B.A x4=void void)",
        "This1.C",                 "(This x1=void This x2=void This1 x3=void This3.B.A x4=void void)")
  ),new AtomicTest(()->
  toS("{[This1.I]}\nA= ={J={interface method This0 m()}I={interface[This1.J]}A={[This1.I]}}\n")
  ),new AtomicTest(()->
  toS("{}\nA= ={A={}}\n")
  ),new AtomicTest(()->
  toS("{}\nA= ={A={}}\nB= ={B={A={}}}\n")

  ));}
private static String emptyP="{#norm{}}{#norm{}}{#norm{}}{#norm{}}{#norm{}}";
public static String inCore(String s){
  return "{imm method imm Void a()="+s+" #norm{}}";
  }
public static void toS(String program){
  assertEquals(Program.parse(program).toString(),program);
  }

public static void minimize(String program,String pathIn,String pathOut){
  Constants.testWithNoUpdatePopChecks(()->
    assertEquals(Program.parse(program).minimize(P.parse(pathIn)),P.parse(pathOut)));
  }
public static void from(String program,String pathIn,String pathSource,String pathOut){
  Constants.testWithNoUpdatePopChecks(()->
    assertEquals(Program.parse(program).from(P.parse(pathIn),P.parse(pathSource).toNCs()),P.parse(pathOut)));
  }
public static void fromE(String program,String eIn,String pathSource,String eOut){
  Constants.testWithNoUpdatePopChecks(()->
    assertEquals(Program.parse(program).from(Core.E.parse(eIn),P.parse(pathSource).toNCs()),Core.E.parse(eOut)));
  }

public static void passWF(String input){
  Constants.testWithNoUpdatePopChecks(()->{
    var p=Program.parse(input);//internally calls wf
    assertTrue(p.wf());//to be more resilient to changes above
    });
  }
public static void failWF(Class<?> clazz,String input,String ...output){
  Constants.testWithNoUpdatePopChecks(()->{
    var r=Parse.program(Constants.dummy,input);
    assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
    TestHelpers.checkFail(()->Program.parse(input),output,clazz);
    });
  }
}