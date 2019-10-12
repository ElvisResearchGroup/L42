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
import is.L42.constraints.FreshNames;
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
   chooseGeneralT("Any a, Any b","imm Any")
   ),new AtomicTest(()->
   chooseGeneralT("Any a, Void b","imm Any")
   ),new AtomicTest(()->
   chooseGeneralT("Library a, Void b","null")
   ),new AtomicTest(()->
   chooseGeneralT("read Any a, imm Any b","read Any")
   ),new AtomicTest(()->
   chooseGeneralT("lent Any a, mut Any b","lent Any")
   ),new AtomicTest(()->
   chooseGeneralT("lent Any a, imm Any b","read Any")
   ),new AtomicTest(()->
   chooseGeneralT("capsule Void v,lent Any a, imm Any b","read Any")
   
   ),new AtomicTest(()->
   chooseSpecificT("Any a, Any b","imm Any")
   ),new AtomicTest(()->
   chooseSpecificT("Any a, Void b","imm Void")
   ),new AtomicTest(()->
   chooseSpecificT("Library a, Void b","null")
   ),new AtomicTest(()->
   chooseSpecificT("read Any a, imm Any b","imm Any")
   ),new AtomicTest(()->
   chooseSpecificT("lent Any a, mut Any b","mut Any")
   ),new AtomicTest(()->
   chooseSpecificT("lent Any a, imm Any b","capsule Any")
   ),new AtomicTest(()->
   chooseSpecificT("capsule Void v,lent Any a, imm Any b","capsule Void")

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
   ),new AtomicTest(()->pass("""
     method Any m1(Any b)=(x=this.nope() y=x.m2(a=void) void)
     method This m0(Any b)=this.nope()
     method Void m2(Any a)=(x=this.nope() void)
     ""","""
     method Any m1(Any b)=(This x=this.nope() Void y=x.m2(a=void) void)
     method This m0(Any b)=this.nope()
     method Void m2(Any a)=(This x=this.nope() void)
     """)


   ),new AtomicTest(()->pass("""
     method Void +(This that)=void     
     method Void m()=this+this
     ""","""
     imm method imm Void #plus0(imm This0 that)=void
     imm method imm Void m()=this.#plus0(that=this)
     """)
   ),new AtomicTest(()->fail(EndError.InferenceFailure.class,"""
     method Void *(This that)=void     
     method Void m()=this+this
     """,Err.operatorNotFound(hole,L()))
   ),new AtomicTest(()->fail(EndError.InferenceFailure.class,"""
     method Void +(This that)=void
     method Void +(Any dope)=void     
     method Void m()=this+this
     """,Err.operatorNotFound(hole,List.of("This0.#plus0(that)","This0.#plus0(dope)")))
   ),new AtomicTest(()->pass("""
     class method class This a(This that)=\\
     class method This b()=\\.b()
     class method This c()=This.a(that=\\.b())
     ""","""
     class method class This0 a(imm This0 that)=This0<:class This0
     class method imm This0 b()=This0<:class This0.b()
     class method imm This0 c()=This0<:class This0.a(that=This0<:class This0.b())
     """)
   ),new AtomicTest(()->pass("""
     class method class Any a(This that)=that<:Any
     class method class Any b(This that)=that<:Void
     class method class Any c(This that)=void<:Void
     ""","""
     class method class Any a(imm This0 that)=(imm Any fresh0_casted=that fresh0_casted)
     class method class Any b(imm This0 that)=(imm Void fresh1_casted=that fresh1_casted)
     class method class Any c(imm This0 that)=(imm Void fresh2_casted=void fresh2_casted)
     """)          
   ),new AtomicTest(()->pass("""
     class method This !()=!this
     class method This ~()=~this
     ""","""
     class method imm This0 #bang0()=this.#bang0()
     class method imm This0 #tilde0()=this.#tilde0()
     """)
   ),new AtomicTest(()->pass("""
     class method Void foo1()=(var This x=this  x:=void)
     class method Void foo2()=(var This x=this  x+=void)
     ""","""
     class method imm This0 #bang0()=this.#bang0()
     class method imm This0 #tilde0()=this.#tilde0()
     """)
   ),new AtomicTest(()->pass("""
     class method Void ()=This()
     ""","""
     class method imm Void #apply()=This0<:class This0.#apply()
     """)
     //TODO: assert in the Core.MH that the selector name is not empty
     
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
public static void chooseGeneralT(String in,String out){
  Full.L fl=(Full.L)Program.parse("{method Void m("+in+")}").top;
  var ts=((Full.L.MWT)fl.ms().get(0)).mh().pars();
  var res=p0._chooseGeneralT(L(ts.stream().map(TypeManipulation::toCore)),L());
  assertEquals(out,""+res);
  }
public static void chooseSpecificT(String in,String out){
  Full.L fl=(Full.L)Program.parse("{method Void m("+in+")}").top;
  var ts=((Full.L.MWT)fl.ms().get(0)).mh().pars();
  var res=p0._chooseSpecificT(L(ts.stream().map(TypeManipulation::toCore)),L());
  assertEquals(out,""+res);
  }  
public static void pass(String l,String out){
  Core.L cl=Program.parse("{"+out+" #norm{}}").topCore();
  var ces=processIn(l);
  assertEquals(ces,cl.mwts());
  }
public static void fail(Class<?> kind,String l,String ...err){
  checkFail(()->processIn(l),err, kind);
  }

public static List<Core.L.MWT> processIn(String l){
  FreshNames fresh=new FreshNames();
  Full.L fl=(Full.L)Program.parse("{"+l+"}").top;
  List<Full.L.MWT> mwts=L(fl.ms(),(c,m)->{if(m instanceof Full.L.MWT){c.add((Full.L.MWT)m);}});
  CTz ctz=new CTz();
  List<Core.MH> mhs=L(mwts,(c,mi)->c.add(TypeManipulation.toCore(mi.mh())));
  Program p=p0.update(p0.topCore().withMwts(L(c->{
    c.addAll(p0.topCore().mwts());
    for(var mh:mhs){c.add(new Core.L.MWT(L(),L(),mh,"",null));}
    })));
  List<Half.E> hes=L(mhs,mwts,(c,mhi,mi)->{
    c.add(ctz._add(fresh,p, mhi, mi._e()));
    });
  return L(mhs,hes,(c,mhi,ei)->{
    if(ei==null){c.add(new Core.L.MWT(L(),L(),mhi,"",null));return;} 
    I i=new I(new C("C",-1),p,G.of(mhi));
    var cei=new InferToCore(i,ctz,null).compute(ei);
    c.add(new Core.L.MWT(L(),L(),mhi,"",cei));
    }); 
  }
}