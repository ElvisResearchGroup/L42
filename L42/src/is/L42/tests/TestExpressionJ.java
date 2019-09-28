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
  j("Any<:class Any","Wrap.ofPath(\"L42Any\")")
  ),new AtomicTest(()->
  j("Library<:class Any","Wrap.ofPath(\"L42Library\")")
  ),new AtomicTest(()->
  j("Void<:class Any","Wrap.ofPath(\"L42Void\")")
  ),new AtomicTest(()->
  j("This0.A<:class This0.A","£cA1.instance")
  ),new AtomicTest(()->
  j("This0.B<:class This0.B","£cB1.instance")
  ),new AtomicTest(()->
  j("This0.A<:class Any","Wrap.ofPath(\"£cA1\")")
  ),new AtomicTest(()->
  j("This0.B<:class Any","Wrap.ofPath(\"£cB1\")")
  ),new AtomicTest(()->
  j("void","L42Void.instance")
  ),new AtomicTest(()->
  j("{#norm{}}","Wrap.ofLib(\"1\")")
    ),new AtomicTest(()->
  j("{#typed{}}","Wrap.ofLib(\"1\")")
    ),new AtomicTest(()->
  j("This0.A<:class This0.A.ma(a=This0.A<:class This0.A.of())","£cA1.£mma£xa(£cA1.instance,£cA1.£mof(£cA1.instance))")
    ),new AtomicTest(()->
  j("This0.B<:class This0.B.mb(b=This0.B<:class This0.B.of())","£cB1.£mmb£xb(£cB1.instance,£cB1.£mof(£cB1.instance))")
    ),new AtomicTest(()->
  j("loop void","switchKw(0){defaultKw->{if(false)yield Wrap.throwE(null);whileKw(trueKw)L42Void.instance;}}")
    ),new AtomicTest(()->
  j("return void","Wrap.throwE(new L42Return(L42Void.instance))")
  //op update needs blocks
  ),new AtomicTest(()->
  j("(void)","L42Void.instance")
  ),new AtomicTest(()->
  j("(This0.A a=This0.A<:class This0.A.of() a)","""
    switchKw(0){defaultKw->{
      £cA1 £xa=null;
      £xa=£cA1.£mof(£cA1.instance);
      yield £xa
      }}
    """)
  ),new AtomicTest(()->
  j("(var This0.A a=This0.A<:class This0.A.of() a:=a)","""
    switchKw(0){defaultKw->{
      £cA1 £xa=null;
      £xa=£cA1.£mof(£cA1.instance);
      yield Wrap.toVoid(£xa=£xa)
      }}
    """)
  ),new AtomicTest(()->//template, next tests will be similar
  j("""
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
  j("""
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
  j("""
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
  j("""
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
  j("""
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
  j("""
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
  j("""
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


  ));}
public static void j(String e,String out){
  String l="{ method Void m()="+e+" A={class method This0 of() method Void ma(This0 a) #norm{}} B={class method This0 of() method Void mb(This0 b) #norm{nativeKind=Foo}} #norm{uniqueId=id1}}";
  var p=Program.parse(l);
  J j=new J(p,G.empty(),false);
  j.visitE(p.topCore().mwts().get(0)._e());
  String res=j.result().toString();
  Err.strCmp(res,out);
  }
}