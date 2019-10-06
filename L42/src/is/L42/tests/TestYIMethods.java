package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.common.TypeManipulation;
import is.L42.constraints.InferToCore;
import is.L42.common.Program.InvalidImplements;
import is.L42.common.Program.PathNotExistent;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.I;
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

public class TestYIMethods
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   chooseT("Any a, Any b","imm Any")
   ),new AtomicTest(()->
   chooseT("Any a, Void b","imm Any")
   ),new AtomicTest(()->
   chooseT("Library a, Void b","null")
   ),new AtomicTest(()->
   chooseT("read Any a, imm Any b","read Any")
   ),new AtomicTest(()->
   chooseT("lent Any a, mut Any b","lent Any")
   ),new AtomicTest(()->
   chooseT("lent Any a, imm Any b","read Any")
   ),new AtomicTest(()->
   chooseT("capsule Void v,lent Any a, imm Any b","read Any")

   ),new AtomicTest(()->
   pass("","")
   ),new AtomicTest(()->
   pass("method Void v()=void","method Void v()=void")
   ),new AtomicTest(()->
   pass("method Void v()=void, method Any a(Any a)=void",
   "method Void v()=void, method Any a(Any a)=void")
   ),new AtomicTest(()->pass("""
     method Any m1(Any a)=(x=this.m2(b=void) x)
     method Any m2(Any b)=(x=this.m1(a=void) x)
     ""","""
     method Any m1(Any a)=(Any x=this.m2(b=void) x)
     method Any m2(Any b)=(Any x=this.m1(a=void) x)
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(Any a)=(Void x=this.nope() void)
     method Any m2(Any b)=(x=this.nope() void)
     ""","""
     method Any m1(Any a)=(Void x=this.nope() void)
     method Any m2(Any b)=(Void x=this.nope() void)
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(Any b)=(x=this.nope() void)
     method Any m2(Any a)=(Void x=this.nope() void)
     ""","""
     method Any m1(Any b)=(Void x=this.nope() void)
     method Any m2(Any a)=(Void x=this.nope() void)
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(Any b)=(x=this.m1(b=this.nope()) void)
     method Any m2(Any a)=(x=this.nope() void)
     ""","""
     method Any m1(Any b)=(Any x=this.m1(b=this.nope()) void)
     method Any m2(Any a)=(Any x=this.nope() void)
     """)
   ),new AtomicTest(()->pass("""
     method Void m0(Any b)=this.nope()
     method Any m1(Any b)=this.nope()
     method Any m2(Any a)=(x=this.nope() void)
     ""","""
     method Void m0(Any b)=this.nope()
     method Any m1(Any b)=this.nope()
     method Any m2(Any a)=(Void x=this.nope() void)
     """)
   ),new AtomicTest(()->pass("""
     method Any m2(Any a)=(x=(
       Any y=void catch error Void v v y)
       void)
     ""","""
     method Any m2(Any a)=(Any x=(
       Any y=void catch error Void v v y)
       void)
     """)



   ),new AtomicTest(()->pass("""
     method Any m1(Any b)=(x=this.m1(b=this.nope()) void)
     method Void m0(Any b)=this.nope()
     method Any m2(Any a)=(x=this.nope() void)
     ""","""
     method Any m1(Any b)=(Any x=this.m1(b=this.nope()) void)
     method Void m0(Any b)=this.nope()
     method Any m2(Any a)=(Void x=this.nope() void)
     """)
  ));}
//private static String emptyP="{#norm{}}{#norm{}}{#norm{}}{#norm{}}{#norm{}}";
static Program p0=Program.parse("""
  {
   method Void m()
   method This k(Any x)
   method Void #lt0(This a)
   method Void #lt0(This a,Void b)
   method Void #ltequal0(This a,Any b)
   #norm{}}
  """);
public static void chooseT(String in,String out){
  Full.L fl=(Full.L)Program.parse("{method Void m("+in+")}").top;
  var ts=((Full.L.MWT)fl.ms().get(0)).mh().pars();
  var res=p0._chooseT(L(ts.stream().map(TypeManipulation::toCore)),L());
  assertEquals(out,""+res);
  }
  
public static void pass(String l,String out){
  Full.L fl=(Full.L)Program.parse("{"+l+"}").top;
  Core.L cl=Program.parse("{"+out+" #norm{}}").topCore();
  List<Full.L.MWT> mwts=L(fl.ms(),(c,m)->{if(m instanceof Full.L.MWT){c.add((Full.L.MWT)m);}});
  CTz ctz=new CTz();
  List<Core.MH> mhs=L(mwts,(c,mi)->c.add(TypeManipulation.toCore(mi.mh())));
  Program p=p0.update(p0.topCore().withMwts(L(c->{
    c.addAll(p0.topCore().mwts());
    for(var mh:mhs){c.add(new Core.L.MWT(L(),L(),mh,"",null));}
    })));
  List<Half.E> hes=L(mhs,mwts,(c,mhi,mi)->{
    c.add(ctz._add(p, mhi, mi._e()));
    });
  List<Core.L.MWT> ces=L(mhs,hes,(c,mhi,ei)->{
    if(ei==null){c.add(new Core.L.MWT(L(),L(),mhi,"",null));return;} 
    I i=new I(new C("C",-1),p,G.empty());
    var cei=new InferToCore(i,ctz,null).compute(ei);
    c.add(new Core.L.MWT(L(),L(),mhi,"",cei));
    });
  assertEquals(ces,cl.mwts()); 
  }
//public static void topFail(Class<?> kind,String program,String ...output){
//  checkFail(()->new Top().top(new CTz(),Program.parse(program)), output, kind);
//  }
}