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
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.tools.AtomicTest;
import is.L42.top.Top;
import is.L42.translationToJava.J;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness.NotWellFormed;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestExpressionJ
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
  je("Any<:class Any","Wrap.ofPath(\"L42Any\")")
  ),new AtomicTest(()->
  je("Library<:class Any","Wrap.ofPath(\"L42Library\")")
  ),new AtomicTest(()->
  je("Void<:class Any","Wrap.ofPath(\"L42Void\")")
  ),new AtomicTest(()->
  je("This0.A<:class This0.A","£cA1.instance")
  ),new AtomicTest(()->
  je("This0.B<:class This0.B","£cB1.instance")
  ),new AtomicTest(()->
  je("This0.A<:class Any","Wrap.ofPath(\"£cA1\")")
  ),new AtomicTest(()->
  je("This0.B<:class Any","Wrap.ofPath(\"£cB1\")")
  ),new AtomicTest(()->
  je("void","L42Void.instance")
  ),new AtomicTest(()->
  je("{#norm{}}","Wrap.ofLib(\"1\")")
    ),new AtomicTest(()->
  je("{#typed{}}","Wrap.ofLib(\"1\")")
    ),new AtomicTest(()->
  je("This0.A<:class This0.A.ma(a=This0.A<:class This0.A.of())","£cA1.£mma£xa(£cA1.instance,£cA1.£mof(£cA1.instance))")
    ),new AtomicTest(()->
  je("This0.B<:class This0.B.mb(b=This0.B<:class This0.B.of())","£cB1.£mmb£xb(£cB1.instance,£cB1.£mof(£cB1.instance))")
    ),new AtomicTest(()->
  je("loop void","switchKw(0){defaultKw->{if(false)yield Wrap.throwE(null);whileKw(trueKw)L42Void.instance;}}")
    ),new AtomicTest(()->
  je("return void","Wrap.throwE(new L42Return(L42Void.instance))")
  //op update needs blocks
  ),new AtomicTest(()->
  je("(void)","L42Void.instance")
  ),new AtomicTest(()->
  je("(This0.A a=This0.A<:class This0.A.of() a)","""
    switchKw(0){defaultKw->{
      £cA1 £xa=null;
      £xa=£cA1.£mof(£cA1.instance);
      yield £xa
      }}
    """)
  ),new AtomicTest(()->
  je("(var This0.A a=This0.A<:class This0.A.of() a:=a)","""
    switchKw(0){defaultKw->{
      £cA1 £xa=null;
      £xa=£cA1.£mof(£cA1.instance);
      yield Wrap.toVoid(£xa=£xa)
      }}
    """)
  ),new AtomicTest(()->//template, next tests will be similar
  je("""
   (var This0.A a=This0.A<:class This0.A.of()
    This0.A a0=a
    a.ma(a=a)
    )""","""
    switchKw(0){defaultKw->{
      £cA1 £xa=null;
      £cA1 £xa0=null;
      £xa=£cA1.£mof(£cA1.instance);
      £xa0=£xa;
      yield £cA1.£mma£xa(£xa,£xa)
      }}
    """)
  ),new AtomicTest(()->//using Any for a0
  je("""
   (var This0.A a=This0.A<:class This0.A.of()
    Any a0=a
    a.ma(a=a)
    )""","""
    switchKw(0){defaultKw->{
      £cA1 £xa=null;
      L42Any £xa0=null;
      £xa=£cA1.£mof(£cA1.instance);
      £xa0=£xa;
      yield £cA1.£mma£xa(£xa,£xa)
      }}
    """)
  ),new AtomicTest(()->//using B instead of A
  je("""
   (var This0.B b=This0.B<:class This0.B.of()
    This0.B b0=b
    b.mb(b=b)
    )""","""
    switchKw(0){defaultKw->{
      Foo £xb=null;
      Foo £xb0=null;
      £xb=£cB1.£mof(£cB1.instance);
      £xb0=£xb;
      yield £cB1.£mmb£xb(£xb,£xb)
      }}
    """)
  ),new AtomicTest(()->//B instead of A, Any for b0
  je("""
   (var This0.B b=This0.B<:class This0.B.of()
    Any b0=b
    b.mb(b=b)
    )""","""
    switchKw(0){defaultKw->{
      Foo £xb=null;
      L42Any £xb0=null;
      £xb=£cB1.£mof(£cB1.instance);
      £xb0=£cB1.wrap(£xb);
      yield £cB1.£mmb£xb(£xb,£xb)
      }}
    """)
  ),new AtomicTest(()->//throwing a B and an A
  je("""
   (This0.A a=This0.A<:class This0.A.of()
    This0.B b=This0.B<:class This0.B.of()
    Void v0=error a
    Void v1=error b
    void
    )""","""
    switchKw(0){defaultKw->{
      £cA1 £xa=null;
      Foo £xb=null;
      L42Void £xv0=null;
      L42Void £xv1=null;
      £xa=£cA1.£mof(£cA1.instance);
      £xb=£cB1.£mof(£cB1.instance);
      £xv0=Wrap.throwE(new L42Error(£xa));
      £xv1=Wrap.throwE(new L42Error(£cB1.wrap(£xb)));
      yield L42Void.instance
      }}
    """)

  ),new AtomicTest(()->//using fwds
  je("""
   (This0.A a=This0.A<:class This0.A.of()
    This0.A a0=a.ma(a=a0)
    a0
    )""","""
    switchKw(0){defaultKw->{
      £cA1 £xa=null;
      £cA1 £xa0=null;
      £cA1 £xa0£fwd=£cA1.NewFwd();
      £xa=£cA1.£mof(£cA1.instance);
      £xa0=£cA1.£mma£xa(£xa,£xa0£fwd);
      ((Fwd)£xa0£fwd).fix(£xa0);
      yield £xa0
      }}
    """)
  ),new AtomicTest(()->//using fwds
  je("""
   (This0.B b=This0.B<:class This0.B.of()
    This0.B b0=b.mb(b=b0)
    b0
    )""","""
    switchKw(0){defaultKw->{
      Foo £xb=null;
      Foo £xb0=null;
      Object £xb0£fwd=£cB1.NewFwd();
      £xb=£cB1.£mof(£cB1.instance);
      £xb0=£cB1.£mmb£xb(£xb,£xb0£fwd);
      ((Fwd)£xb0£fwd).fix(£xb0);
      yield £xb0
      }}
    """)
  ),new AtomicTest(()->//using fwds
  jc("""
     """,
     """
     class £cN1 implements L42Any{
       public static int £mof(£cN1 £xthis){
         return 0;}
       public static int £msum£xthat(int £xthis, int £xthat){
         return £xthis + £xthat;}
       public static £cN1 NewFwd(){return new _Fwd();}
       public static class _Fwd extends £cN1 implements Fwd{
         private List<Object> os=new ArrayList<>();
         private List<BiConsumer<Object,Object>> fs=new ArrayList<>();
         public List<Object> os(){return os;}
         public List<BiConsumer<Object,Object>> fs(){return fs;}
         }
       public static final £cN1 Instance=new _Fwd();
       public intunwrap;
       public static £cN1 wrap(int that){£cN1 res=new £cN1();res.unwrap=that;return res;}
       }
     """,""
)

  ));}
public static void je(String e,String out){
  String l="{ method Void m()="+e+" A={class method This0 of() method Void ma(This0 a) #norm{}} B={class method This0 of() method Void mb(This0 b) #norm{nativeKind=Foo}} #norm{uniqueId=id1}}";
  var p=Program.parse(l);
  J j=new J(p,G.empty(),false);
  j.visitE(p.topCore().mwts().get(0)._e());
  String res=j.result().toString();
  Err.strCmp(res,out);
  }
public static void jc(String e,String ...out){
  String l="{ "+e+
  """
  N={
    class method This0 of()
    method This0 sum(This0 that)=native{trusted:OP+} error void
    #norm{nativeKind=int}}
  A={
    class method This0 of(This1.N n)
    method This1.N n()
    #norm{}} 
  #norm{uniqueId=id1}}
  """;
  var p=Program.parse(l);
  List<String> res=L(p.topCore().ncs(),(c,nc)->{
    var pi=p.push(nc.key());
    J j=new J(pi,G.empty(),false);
    j.mkClass();
    c.add(j.result().toString());
    });
  L(res,List.of(out),(c,r,o)->c.add(o.isEmpty() || Err.strCmp(r,o)));
  }
}