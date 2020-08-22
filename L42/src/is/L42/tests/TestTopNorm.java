package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.common.Constants;
import is.L42.constraints.FreshNames;
import is.L42.common.EndError.InvalidImplements;
import is.L42.common.EndError.NotWellFormed;
import is.L42.common.EndError.PathNotExistent;
import is.L42.common.EndError.TypeError;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.AtomicTest;
import is.L42.top.CachedTop;
import is.L42.top.Init;
import is.L42.translationToJava.Loader;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestTopNorm{
  @Test public void t1(){top(
    "{}",
    "{#norm{}}"
  );}@Test public void notWellFormed0(){top("""
    {A={
       class method Library foo(Library a,Library b)=b
       }
     B=A.foo(a={},b={})
     }""","""
     {A={
        class method Library foo(Library a, Library b)=b
        #typed{}
        }
      B={#typed{}}#norm{}}
     """
  );}@Test public void notWellFormed1(){topFail(EndError.NotWellFormed.class,"""
    {Z={}
     A={
       class method Library foo(Library a,Library b)=b
       #norm{watched=This1.Z}}
       B=A.foo(a={},b={})
       }""",Err.infoPathNotInTyped("[###]","[###]")
  );}@Test public void notWellFormed2(){topFail(EndError.NotWellFormed.class,"""
    {Z={}
     A={
       class method Library foo(Library a,Library b)=b
       }
       B=A.foo(a={#norm{watched=This1.Z}},b={})
       }""",Err.infoPathNotInTyped("[###]","[###]")
  );}@Test public void notWellFormed3OkIfTrashed(){top("""
    {Z={}
     A={
       class method Library foo(Library a,Library b)=b
       }
     B=A.foo(a={#norm{typeDep=This1.Z, coherentDep=This1.Z, watched=This1.Z,nativeKind=Opt, nativePar=This1.Z,This1.Z}},b={})
     }""","""
     {Z={#typed{}}
      A={class method Library foo(Library a, Library b)=b
         #typed{}}
      B={#typed{}}#norm{}}
     """
  /*Suppressed: now the invalid natives will simply perform the body
  );}@Test public void notWellFormed3ButNotWellTyped(){topFail(EndError.TypeError.class,"""
    {Z={}
     A={
       class method Library foo(Library a,Library b)=a
       }
       B=A.foo(a={#norm{typeDep=This1.Z, coherentDep=This1.Z, watched=This1.Z,nativeKind=Opt, nativePar=This1.Z,This1.Z}},b={})
      }""",Err.nativeExceptionNotLazyMessage("[###]","[###]")
      */
  );}@Test public void notWellTypedMap(){topFail(EndError.TypeError.class,"""
    {Z={#norm{nativeKind=LazyMessage}}
     A={
       #norm{typeDep=This1.Z, coherentDep=This1.Z, watched=This1.Z,nativeKind=HIMap, nativePar=This1.Z,This1.Z,This1.Z,This1.Z}
       }
    }""",Err.nativeParameterViolatedConstraint("[###]","[###]","[###]")
  );}@Test public void t2(){top(
   "{A={} B=(void)}","{A={#typed{}}B={#typed{}}#norm{}}"
  );}@Test public void t3(){top(
    "{A={} B=({})}","{A={#typed{}}B={#typed{}}#norm{}}"
   );}@Test public void t4(){topFail(EndError.TypeError.class,"""
    {A={interface[This1.B] #norm{typeDep=This1.B}}
     B={interface[This1.C] #norm{typeDep=This1.C}}
     C={interface #norm{}} 
     D=({})
     }""",Err.missingImplementedInterface(hole)
   );}@Test public void t5() {topFail(EndError.NotWellFormed.class,"""
    {A={interface[This] #norm{typeDep=This}}
     D=({})
     }""",Err.interfaceImplementsItself(hole)
   );}@Test public void t6(){top(
     "{A={class method This() method Library #toLibrary()={#norm{}}} B=A()}","""
     {
     A={
       class method imm This0 #apply()
       imm method imm Library #toLibrary()={#typed{}}
       #typed{typeDep=This0}}
     B={#typed{}}#norm{}}
     """
   //private implements
   );}@Test public void t7(){top(
     "{I={interface} A={class method Library foo()={[This2.I] #norm{typeDep=This2.I}}} B=void}","""
   {I={interface #typed{}}
   A={class method Library foo()={[This2.I]#typed{typeDep=This2.I}}
   #typed{typeDep=This1.I hiddenSupertypes=This1.I}}
   B={#typed{}}#norm{}}"""
   //privates
   );}@Test public void t8(){top(
   "{A={class method This foo::0()} B=void}",
   "{A={class method imm This0 foo::0() #typed{typeDep=This0 close}} B={#typed{}}#norm{}}"
   );}@Test public void t8a(){topFail(NotWellFormed.class,
     "{A={class method This foo::1()=error void} B={class method This foo::1()=error void} C=void}",
     Err.nonUniqueNumber(hole,hole)
   );}@Test public void t9(){top(
     "{A={class method This foo::0()} B={class method This foo::0()} C=void}",
     "{A={class method imm This0 foo::0()#typed{typeDep=This0 close}}B={class method imm This0 foo::0()#typed{typeDep=This0 close}}C={#typed{}}#norm{}}"
   //also check refined methods are not included
   //top level init well formedness
   );}@Test public void t10(){topFail(NotWellFormed.class,
     "{B={} A={ method This1.B b() #norm{}} C=void}",
     Err.missedTypeDep(hole)
   );}@Test public void t11(){topFail(NotWellFormed.class,
     "{B={} A={ method Void b()=This1.B<:class Any #norm{}} C=void}",
     Err.missedTypeDep(hole)
   );}@Test public void t12(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      method Void b()=This1.B<:class This1.B
      #norm{typeDep=This1.B}
      } C=void}
   """,
   Err.missedCoheDep(hole)
   );}@Test public void t13(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      method Void b()=(Void x=void catch return class This1.B y y x)
      #norm{typeDep=This1.B}
      } C=void}
   """,
   Err.missedCoheDep(hole)
   );}@Test public void t14(){topFail(NotWellFormed.class,"""
   {A={#norm{typeDep=This0, watched=This0}}
    C=void}
    """,
   Err.noSelfWatch()
   );}@Test public void t15(){topFail(NotWellFormed.class,"""
   {B={D::1={#norm{}}#norm{}}
    A={
      method This1.B.D::1 b() 
      #norm{typeDep=This1.B,This1.B.D::1}}
    C=void}
    """,
   Err.missedWatched("[This1.B]")
   );}@Test public void t16(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      method Void b()=This1.B<:class This1.B.foo::1() 
      #norm{typeDep=This1.B coherentDep=This1.B}}
    C=void}
    """,
   Err.missedWatched("[This1.B]")
   );}@Test public void t17(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      method Void b()=(class This1.B bb=This1.B<:class This1.B   bb.foo::1())
      #norm{typeDep=This1.B coherentDep=This1.B}}
    C=void}
    """,
   Err.missedWatched("[This1.B]")
   );}@Test public void t18(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      method Void b()={#norm{typeDep=This2.B watched=This2.B}}
      #norm{typeDep=This1.B}}
    C=void}
    """,
   Err.missedWatched("[This1.B]")
   );}@Test public void t19(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      DD::5={#norm{typeDep=This2.B watched=This2.B}}
      #norm{typeDep=This1.B}}
    C=void}
    """,
   Err.missedWatched("[This1.B]")
   );}@Test public void t20(){top("""
   {B={}
    A={
      DD={#norm{typeDep=This2.B watched=This2.B}}
      #norm{typeDep=This1.B}}
    C=void}
    ""","""
   {B={#typed{}}
    A={
      DD={#typed{typeDep=This2.B watched=This2.B}}
      #typed{typeDep=This1.B}}
    C={#typed{}}#norm{}}
    """
//computing watched
   );}@Test public void t21(){top("""
   {B={D::1={#norm{}} #norm{}}
    A={
      method This1.B.D::1 b() 
      }
    C=void}
    ""","""
    {B={D::1={#typed{}}#typed{}}
     A={imm method imm This1.B.D::1 b()
       #typed{typeDep=This1.B.D::1, This1.B watched=This1.B}}
     C={#typed{}}#norm{}}
    """
   );}@Test public void t22(){top("""
   {B={class method Void foo::1()=void}
    A={
      method Void b()=This1.B<:class This1.B.foo::1() 
      }
    C=void}
    ""","""
    {B={class method imm Void foo::1()=void #typed{}}
     A={imm method imm Void b()=This1.B<:class This1.B.foo::1()
       #typed{typeDep=This1.B coherentDep=This1.B watched=This1.B}}
     C={#typed{}}#norm{}}
    """
   );}@Test public void t23(){top("""
   {B={class method Void foo::1()=void}
    A={
      method Void b()=(class This1.B bb=This1.B<:class This1.B   bb.foo::1())
      }
    C=void}
    ""","""
    {B={class method imm Void foo::1()=void #typed{}}
     A={imm method imm Void b()=(
         class This1.B bb=This1.B<:class This1.B
         bb.foo::1())
       #typed{typeDep=This1.B coherentDep=This1.B watched=This1.B}}
       C={#typed{}}#norm{}}
    """
   );}@Test public void t24(){top("""
   {B={}
    A={
      method Library b()={#norm{typeDep=This2.B watched=This2.B}}
      }
    C=void}
    ""","""
    {B={#typed{}}
     A={
       imm method imm Library b()={
         #typed{typeDep=This2.B watched=This2.B}}
       #typed{typeDep=This1.B watched=This1.B}}
     C={#typed{}}#norm{}}
     """
   );}@Test public void t25(){top("""
   {B={}
    A={
      DD={#norm{typeDep=This2.B watched=This2.B}}
      }
    C=void}
    ""","""
    {B={#typed{}}
     A={DD={#typed{typeDep=This2.B watched=This2.B}}
     #typed{}}
     C={#typed{}}#norm{}}
     """
   );}@Test public void t26(){top("""
   {B={}
    A={
      DD::2={#norm{typeDep=This2.B watched=This2.B}}
      }
    C=void}
    ""","""
    {B={#typed{}}
     A={DD::2={#typed{typeDep=This2.B watched=This2.B}}
     #typed{typeDep=This1.B watched=This1.B}}
     C={#typed{}}#norm{}}
     """
   );}@Test public void t27(){top("""
   {B={D::1={#norm{}} #norm{}}
    A={@B.D::1}
    C=void}
    ""","""
    {B={D::1={#typed{}} #typed{}}
     A={
       #typed{typeDep=This1.B.D::1,This1.B watched=This1.B}
       @This1.B.D::1}
     C={#typed{}}#norm{}}
     """
//hidden supertypes
   );}@Test public void t28(){topFail(NotWellFormed.class,"""
   {I={interface}
    A={B::1={[This2.I]#norm{typeDep=This2.I}} #norm{typeDep=This1.I}}
    C=void}
    """,Err.missedHiddenSupertypes("[This1.I]")
   );}@Test public void t29(){topFail(NotWellFormed.class,"""
   {I={interface}
    A={ class method Library foo()={[This2.I]#norm{typeDep=This2.I}} #norm{typeDep=This1.I}}
    C=void}
    """,Err.missedHiddenSupertypes("[This1.I]")
   );}@Test public void t30(){top("""
   {I={interface}
    A={B::1={[This2.I]#norm{typeDep=This2.I}}}
    C=void}
    ""","""
    {I={interface #typed{}}
     A={B::1={[This2.I]#typed{typeDep=This2.I}}
       #typed{typeDep=This1.I hiddenSupertypes=This1.I}}
     C={#typed{}}#norm{}}
    """
   );}@Test public void t30a(){top("""
   {I={interface}
    A={ class method Library foo()={[This2.I]#norm{typeDep=This2.I}}}
    C=void}
    ""","""
    {I={interface #typed{}}
     A={class method imm Library foo()=
         {[This2.I]#typed{typeDep=This2.I}}
       #typed{typeDep=This1.I hiddenSupertypes=This1.I}}
     C={#typed{}}#norm{}}
    """
 //collect
   );}@Test public void t31(){top(
     "{} A= ={A={}} B= ={B={A={}}}","{#norm{}}"
   );}@Test public void t32(){topFail(InvalidImplements.class,
     "{[This.B] B={interface}}",Err.nestedClassesImplemented(hole)
   );}@Test public void t33(){topFail(InvalidImplements.class,
     "{B={} A={[This1.B]}}",Err.notInterfaceImplemented()
   );}@Test public void t34(){top(
     "{B={interface} A={[B]}}","{B={interface #typed{}} A={[This1.B]#typed{typeDep=This1.B}}#norm{}}"
   );}@Test public void t35(){top(
     "{C={B={method This m() #norm{typeDep= This0 This1.B }}} A={}}",
     "{C={B={imm method imm This0 m()#typed{typeDep=This0}}#typed{}}A={#typed{}}#norm{}}"
   );}@Test public void t36(){top(
     "{C={B={method This m() #norm{typeDep= This1.B }}} A={}}",
     "{C={B={imm method imm This0 m()#typed{typeDep=This0}}#typed{}}A={#typed{}}#norm{}}"
//   );}@Test public void t1(){top(//Note: can not support starting from a non flat program any more
//   topFail(PathNotExistent.class,"{[This1.A]}B= ={}",Err.pathNotExistant("This1.A"))
   );}@Test public void t37(){top(
     "{A={interface}B={interface[This1.A]}C={[This1.B, This1.A]}}","""
     {A={interface #typed{}}
      B={interface[This1.A]#typed{typeDep=This1.A}}
      C={[This1.B, This1.A]#typed{typeDep=This1.B, This1.A}}
      #norm{}     
      }"""
   );}@Test public void t38(){top(
     "{A={interface}B={interface[This1.A]}C={[This1.A, This1.B]}}","""
     {A={interface #typed{}}
      B={interface[This1.A]#typed{typeDep=This1.A}}
      C={[This1.A, This1.B]#typed{typeDep=This1.A, This1.B}}
      #norm{}
      }"""
   );}@Test public void t39(){top(
     "{A={interface}B={interface[This1.A]}C={[This1.B]}}","""
     {A={interface #typed{}}
      B={interface[This1.A]#typed{typeDep=This1.A}}
      C={[This1.B, This1.A]#typed{typeDep=This1.B, This1.A}}
      #norm{}
      }"""
    );}@Test public void t40(){topFail(InvalidImplements.class,
      "{A={interface [B]} B={interface[A]}}",
      Err.nestedClassesImplemented(hole)
    );}@Test public void t41(){top(
      "{ method Void v()}",
      "{ method Void v() #norm{}}"
    );}@Test public void t42(){top(
      "{ method Void v() method Any g(Any that)[Library]}",
      "{ method Void v() method Any g(Any that)[Library] #norm{}}"
    );}@Test public void t43(){top(
      "{A={interface method A a()} C={interface [A] method Void v()}}","""
      {A={interface method This a() #typed{typeDep=This0}} 
      C={interface [This1.A] method Void v()
          imm method imm This1.A a() #typed{typeDep=This1.A refined=a()}
          } #norm{}}
      """
    );}@Test public void t44(){top(
      "{C={interface method A a()} A={interface [C]} B={interface [C]} D={interface[A,B]}}","""
      {C={interface method This1.A a() #typed{typeDep=This1.A}}
      A={interface [This1.C] method This a() #typed{typeDep=This, This1.C refined=a()}}
      B={interface [This1.C] method This1.A a() #typed{typeDep=This1.A, This1.C refined=a()}}
      D={interface[This1.A,This1.B,This1.C] method This1.A a() #typed{typeDep=This1.A, This1.B, This1.C refined=a()}}
      #norm{}}"""
    );}@Test public void t45(){top(
      "{C={interface method Any a()} A={interface [C] method Void a()} B={interface [C] method Any a()} D={interface[A,B]}}","""
      {C={interface method Any a() #typed{}}
      A={interface [This1.C] method Void a() #typed{typeDep=This1.C refined=a()}}
      B={interface [This1.C] method Any a() #typed{typeDep=This1.C refined=a()}}
      D={interface[This1.A,This1.B,This1.C]imm method imm Void a() #typed{typeDep=This1.A, This1.B,This1.C refined=a()}}
      #norm{}}"""
   );}@Test public void t46(){topFail(TypeError.class,
      "{C={interface method Any a()} A={interface [C] method Void a()} B={interface [C] method Any a()} D={interface[B,A]}}",
      Err.methSubTypeExpectedRet(hole, hole, hole)
    );}@Test public void t47(){topFail(InvalidImplements.class,
      "{C={interface } A={interface [C] method Void a()} B={interface [C] method Any a()} D={[B,A]}}",
      Err.moreThenOneMethodOrigin("a()", hole)
    );}@Test public void t48(){topFail(InvalidImplements.class,
      "{A={interface method Any a()} B={interface method Any a()} C={[B,A]}}",
      Err.moreThenOneMethodOrigin("a()", hole)
    );}@Test public void t49(){topFail(InvalidImplements.class,
      "{C={interface } A={[C] method a()=void}}",
      Err.noMethodOrigin(hole,hole)
    );}@Test public void t50(){top(
      "{I={interface method Any m()} A={interface[I]}}","""
      {I={interface method Any m()#typed{}}
      A={interface[This1.I] method Any m()#typed{typeDep=This1.I refined=m()}}
      #norm{}}"""
    );}@Test public void t51(){top(
      "{I2={interface method Any m2()} I1={interface method Any m1()} A={interface[I1,I2]}}","""
      {I2={interface method Any m2()#typed{}}
      I1={interface method Any m1()#typed{}}
      A={interface[This1.I1,This1.I2] method Any m1() method Any m2()#typed{typeDep=This1.I1,This1.I2 refined=m1(),m2()}}
      #norm{}}"""
    );}@Test public void t52(){top(
      "{I0={interface method Any m0()} I2={interface [I0] method Any m2()} I1={interface [I0] method Any m1()} A={interface[I1,I2]}}","""
      {I0={interface method Any m0()#typed{}}
      I2={interface [This1.I0] method Any m2()method Any m0()#typed{typeDep=This1.I0 refined=m0()}}
      I1={interface [This1.I0] method Any m1()method Any m0()#typed{typeDep=This1.I0 refined=m0()}}
      A={interface[This1.I1,This1.I2,This1.I0] method Any m1() method Any m0()method Any m2()#typed{typeDep=This1.I1,This1.I2,This1.I0 refined=m1(),m0(),m2()}}
      #norm{}}"""
    );}@Test public void t53(){topFail(TypeError.class,
      "{I0={interface method Any m0()} I2={interface [I0] method Any m2() method Void m0()} I1={interface [I0] method Any m1()} A={interface[I1,I2]}}",
      Err.methSubTypeExpectedRet(hole,hole,hole)
    );}@Test public void t54(){top(
      "{I0={interface method Any m0()} I2={interface [I0] method Any m2() method Void m0()} I1={interface [I0] method Any m1()} A={interface[I2,I1]}}","""
      {I0={interface method Any m0()#typed{}}
      I2={interface [This1.I0] method Any m2()method Void m0()#typed{typeDep=This1.I0 refined=m0()}}
      I1={interface [This1.I0] method Any m1()method Any m0()#typed{typeDep=This1.I0 refined=m0()}}
      A={interface[This1.I2,This1.I1,This1.I0] method Any m2() method Void m0() method Any m1()#typed{typeDep=This1.I2,This1.I1,This1.I0 refined=m2(),m0(),m1()}}
      #norm{}}"""
    //WellFormedness
    );}@Test public void t55(){top(
      "{C={}}",
      "{C={#typed{}}#norm{}}"
    );}@Test public void t56(){top(
      "{method Any foo()=void}",
      "{method Any foo()=void #norm{}}"
    );}@Test public void t57(){topFail(InvalidImplements.class,
      "{[I], I={interface }}",
      Err.nestedClassesImplemented(hole)
   );}@Test public void t58(){topFail(InvalidImplements.class,
      "{A={[I]} J={interface method This m()} I={interface [J] method This m()}}",
      Err.nestedClassesImplemented(hole)
   );}@Test public void t59(){top(
   "{J={interface method This m()} I={interface [J] method This m()} A={interface[I]} }","""
   {J={interface imm method imm This0 m()#typed{typeDep=This0}}
   I={interface[This1.J]imm method imm This0 m()#typed{typeDep=This0,This1.J refined=m()}}
   A={interface[This1.I, This1.J]imm method imm This1.I m()#typed{typeDep=This1.I, This1.J refined=m()}}
   #norm{}}"""
   );}@Test public void t60(){top("""
     {J={interface method This m()}
     I1={interface [J] method A m()}
     I2={interface [J] method This m()}
     A={interface[I1,I2]} }
     ""","""
     {J={interface imm method imm This0 m()#typed{typeDep=This0}}
     I1={interface[This1.J]imm method imm This1.A m()#typed{typeDep=This1.A, This1.J refined=m()}}
     I2={interface[This1.J]imm method imm This0 m()#typed{typeDep=This0,This1.J refined=m()}}
     A={interface[This1.I1,This1.I2 This1.J]imm method imm This0 m()#typed{typeDep=This0,This1.I1,This1.I2,This1.J refined=m()}}#norm{}}
     """
   );}@Test public void t61(){top("""
   {J={interface method This m()}
   I1={interface [J] method A m()}
   I2={interface [J] method This m()}
   A={[I1,I2] method m()=this} }
   ""","""
   {J={interface imm method imm This0 m()#typed{typeDep=This0}}
   I1={interface[This1.J]imm method imm This1.A m()#typed{typeDep=This1.A, This1.J refined=m()}}
   I2={interface[This1.J]imm method imm This0 m()#typed{typeDep=This0,This1.J refined=m()}}
   A={[This1.I1,This1.I2 This1.J]imm method imm This0 m()=this #typed{typeDep=This0,This1.I1,This1.I2,This1.J refined=m()}}#norm{}}
   """
   );}@Test public void t62(){top("""
   {J={interface method This m()}
   I1={interface [J] method B.C.A m()}
   I2={interface [J] method This m()}
   B={C={A={[I1,I2] method m()=this}}} }
   ""","""
   {J={interface imm method imm This0 m()#typed{typeDep=This0}}
    I1={interface[This1.J]imm method imm This1.B.C.A m()#typed{typeDep=This1.B.C.A,This1.J refined=m()}}
    I2={interface[This1.J]imm method imm This0 m()#typed{typeDep=This0,This1.J refined=m()}}
    B={C={A={[This3.I1, This3.I2, This3.J]imm method imm This0 m()=this #typed{typeDep=This0,This3.I1,This3.I2,This3.J refined=m()}}#typed{}}#typed{}}#norm{}}
   """
   );}@Test public void t63(){top("""
   {A=({#norm{}})}
   ""","""
   {A={#typed{}}#norm{}}
   """
   );}@Test public void t64(){topFail(EndError.NotWellFormed.class,"""
   {I={interface[This]}}""",
   Err.interfaceImplementsItself(hole)
   );}@Test public void t65(){top("""
   {I={interface[This.A] A={interface #norm{}}#norm{typeDep=This.A}} B=void}
   ""","""
   {I={interface[This.A] A={interface #typed{}}#typed{typeDep=This.A}} B={#typed{}} #norm{}}
   """
   );}@Test public void t66(){
//For catch class P to be sound we need to be careful: we do not "cast up" a non coherent class P
//We had various options here, for now, we implemented (2)
//(1)"return e: class Any not good" or
//(2)catch return class Foo only good if Foo not interface or Foo==Any
//   and "catch return class Foo" requires Foo to be coherent (so it can not become interface with sum)
//(3) When catches are collected, in the Ts, if class Any is in the set,
// then for any other class P, P must be p(P).interface?=empty.
// When collecting coherent, catch class P cause P to be required coherent only if p(P).interface?=empty
//(3a) we give error if the condition of (3) do not hold
//(3b) we remove from Ts the non interfaces class P if there is class Any  ** //actually unsound!
//(3c) we remove class Any from the Ts if some class P is interface
//(3d) we look to ks: if ks captures class Any we remove the interface class Ps from the Ts.
// otherwise we remove the class Any from the Ts.
//(4) when building a Ts, if we add class Any then we remove from the former accumulated Ts all the class Ps (then 3c)

//If class Any in Ts, do we care what other class P are in Ts??

//-check desugar for cast/if: if we avoid Any for the failed cast state, it would be more expressive
//-think what is the effect in nested {}s
  topFail(EndError.TypeError.class,"""
    {
    I={interface class method Void ()}
    A={[I]}//not coherent
    Task=(
      class Any a=A<:class Any
      class I bang=(
        return a //class Any
        catch return class I i i
        catch return class Any _ error void
        )
      bang()
      ) 
    }
    """,Err.castOnPathOnlyValidIfNotInterface(hole)
  //strings printing ba
  );}@Test public void t67(){top("""
    {
      S={
        class method This0 of()
        method This0 sum(This0 that)=native{trusted:OP+} error void
        #norm{nativeKind=String nativePar=This1.PE typeDep=This0,This1.PE coherentDep=This1.PE,This, watched=This1.PE}
        }
      PE={class method This0 of() #norm{nativeKind=LazyMessage, typeDep=This}}
      Debug={
        class method Void #apply(This1.S that)=(This d=This<:class This.of() d.strDebug(that=that))
        class method This0 of()        
        method Void strDebug(This1.S that)=native{trusted:strDebug} error void
        method Void deployLibrary(This1.S that,Library lib)=native{trusted:deployLibrary} error void
        #norm{nativeKind=TrustedIO typeDep=This0 This1.S coherentDep=This0, watched=This1.S}        
        }
      SB={
        class method mut This0 of()
        mut method Void #a()=native{trusted:'a'} error void
        mut method Void #b()=native{trusted:'b'} error void
        read method This1.S toS()=native{trusted:toS} error void
        #norm{nativeKind=StringBuilder typeDep=This0 This1.S coherentDep=This0,This1.S, watched=This1.S}
        }
      C=(
        mut SB sb=SB.of()
        sb.#b()
        sb.#a()
        Debug(sb.toS())
        Debug.of().deployLibrary(sb.toS(), lib={
        A={
          class method Library foo()={method Void retrived() #norm{}}
          #norm{}}
        #norm{}})
        {#norm{}}
        )
      }
    ""","""
    {S={
      class method imm This0 of()
      imm method imm This0 sum(imm This0 that)=native{trusted:OP+}error void
      #typed{nativeKind=String nativePar=This1.PE typeDep=This0,This1.PE coherentDep=This1.PE,This,watched=This1.PE}
      }
     PE={class method This0 of() #typed{nativeKind=LazyMessage typeDep=This}}
     Debug={
       class method imm Void #apply(imm This1.S that)=(imm This0 d=This0<:class This0.of()d.strDebug(that=that))
       class method imm This0 of()
       imm method imm Void strDebug(imm This1.S that)=native{trusted:strDebug}error void
       imm method imm Void deployLibrary(imm This1.S that, imm Library lib)=native{trusted:deployLibrary}error void
       #typed{nativeKind=TrustedIO typeDep=This0 This1.S coherentDep=This0,watched=This1.S}
       }
     SB={
       class method mut This0 of()
       mut method Void #a()=native{trusted:'a'} error void
       mut method Void #b()=native{trusted:'b'} error void
       read method This1.S toS()=native{trusted:toS} error void
       #typed{nativeKind=StringBuilder typeDep=This0 This1.S coherentDep=This0,This1.S,watched=This1.S}
       }
     C={#typed{}}
     #norm{}}
     """    
  );}@Test public void t68(){top("""
    {reuse[ba]
     C=A.foo() 
    }
    ""","""
    {
      A={
        class method imm Library foo()={imm method imm Void retrived()#typed{}}
        #typed{}}
      C={imm method imm Void retrived()#typed{}}
      #norm{}}
    """
  );}@Test public void t68a(){top("""
    {
      B={A={
        class method Library of()={#norm{}}
        }}
      C=B.A.of()
      }
    ""","""
    {B={A={class method imm Library of()={#typed{}}
       #typed{}}#typed{}}
    C={#typed{}}#norm{}}
    """
  );}@Test public void t69(){top("""
    {
      B={A={
        class method Library of()={method This3.B m() #norm{typeDep=This3.B}}
        }}
      C=B.A.of()
      }
    ""","""
    {B={A={
      class method imm Library of()={imm method imm This2 m()#typed{typeDep=This2}}
      #typed{typeDep=This1}}#typed{}}
    C={imm method imm This1.B m()#typed{typeDep=This1.B}}#norm{}}
    """
 //try-catch
  );}@Test public void t70(){top(tryCatchTest("""
    Void v=void
    catch error Void x (A.b())
    A.a()
    """),tryCatchRes("a")
  );}@Test public void t71(){top(tryCatchTest("""
    Void v=A.throwErr()
    catch error Void x (A.b())
    A.a()
    """),tryCatchRes("b")
  );}@Test public void t72(){top(tryCatchTest("""
    Void v=void
    catch error Void x (A.a())
    A.b()
    """),tryCatchRes("b")
  );}@Test public void t73(){top(tryCatchTest("""
    Void v=A.throwErr()
    catch error Void x (A.a())
    A.b()
    """),tryCatchRes("a")
  //a running "if", finally, after 4 months of work no stop...
  );}@Test public void t74(){top("""
    {
      B={
        class method This0 false()
        class method This0 true()=(This false=this.false() false.not())
        method This0 #if()=this
        method Void #checkTrue()[Void]=native{trusted:checkTrue} error void
        method This0 not()=native{trusted:OP!} error void
        method This0 and(This0 that)=native{trusted:OP&} error void
        method This0 or(This0 that)=native{trusted:OP|} error void
        #norm{nativeKind=Bool typeDep=This coherentDep=This}
        }
      C=if B.true().and(B.true()) {imm method imm Void a()#typed{}}
        else {imm method imm Void b()#typed{}}
      }
    ""","""
    {B={
      class method imm This0 false()
      class method imm This0 true()=(imm This0 false=this.false()false.not())
      imm method imm This0 #if()=this
      imm method imm Void #checkTrue()[Void]=native{trusted:checkTrue}error void
      imm method imm This0 not()=native{trusted:OP!}error void
      imm method imm This0 and(imm This0 that)=native{trusted:OP&}error void
      imm method imm This0 or(imm This0 that)=native{trusted:OP|}error void
      #typed{typeDep=This0 nativeKind=Bool coherentDep=This}
      }
    C={imm method imm Void a()#typed{}}#norm{}}
    """
  );}@Test public void t75(){topFail(InvalidImplements.class,
    "{A={...}}",
    Err.dotDotDotSouceNotExistant(hole)
  );}@Test public void t76(){top("""
    {TestingDotDotDot={...}}
    ""","""
    {TestingDotDotDot={
    imm method imm@{hei!} Void v() = void
    AA={#typed{}@{it is there}}#typed{}}
    #norm{}}
    """
  );}@Test public void t77(){topFail(EndError.TypeError.class,"""
    {A={class method This() mut method Void foo(Void v)}
    Test=(
      A a=A()
      a.foo(v=void)
      )
    }
    """,
    Err.methCallNoCompatibleMdfParametersSignature(hole,hole)
  );}@Test public void t78(){topFail(EndError.TypeError.class,"""
    {A={imm method This foo()=native{trusted:lazyCache} void}
    Test=void
    }
    """,
    Err.invalidExpectedTypeForVoidLiteral(hole)
  );}@Test public void t79(){top("""
    {A={imm method This foo()=native{trusted:lazyCache} error void}
    Test=void
    }
    ""","""
    {A={imm method This foo()=native{trusted:lazyCache} error void #typed{typeDep=This0}}
    Test={#typed{}}
    #norm{}}
    """
  );}@Test public void t80(){top("""
    {A={}
     B1={class method Void v(class Any that)=void}
     C1={class method Void vv()=B1.v(A)}
     B2={class method Void =>(class Any that)=void}
     C2={class method Void vv(class B2 b)=b=>A}
     Test=void
    }
    ""","""
    {A={#typed{}}
     B1={class method Void v(class Any that)=void #typed{}}
     C1={class method Void vv()=
       This1.B1<:class This1.B1.v(that=This1.A<:class Any)
       #typed{typeDep=This1.B1, This1.A coherentDep=This1.B1
       usedMethods=This1.B1.v(that)}}
     B2={class method Void #equalgt0(class Any that)=void #typed{}}
     C2={class method Void vv(class This1.B2 b)=
       b.#equalgt0(that=This1.A<:class Any)
       #typed{typeDep=This1.B2, This1.A
       usedMethods=This1.B2.#equalgt0(that)}}
     Test={#typed{}}#norm{}}
    """
  /*Suppressed: now the invalid natives will simply perform the body
  );}@Test public void t81(){topFail(EndError.TypeError.class,"""
    {A={imm method mut This foo()=native{trusted:lazyCache} error void}
    Test=void
    }
    """,
    Err.nativeParameterInvalidKind(hole,hole,hole,hole,hole)
  );}@Test public void t82(){topFail(EndError.TypeError.class,"""
    {A={capsule method This foo()=native{trusted:lazyCache} error void}
    Test=void
    }
    """,
    Err.nativeParameterInvalidKind(hole,hole,hole,hole,hole)
  );}@Test public void t83(){topFail(EndError.TypeError.class,"""
    {A={method This foo(Any that)=native{trusted:lazyCache} error void}
    Test=void
    }
    """,
    Err.nativeParameterCountInvalid(hole,hole,hole)
    */
  );}@Test public void t84(){topFail(EndError.CoherentError.class,"""
    {A={
      class method This of::0()
      class method This of()=This.of::0()
      method Void v()
      }
    Test=A.of().v()
    }
    """,
    Err.nonCoherentPrivateStateAndPublicAbstractMethods(hole)
  );}@Test public void t85(){top("""
    {A={class method Void a(Any that)=(
      if This x=that error void
      void
      )}}
    ""","""
    {A={class method Void a(Any that)=(
      Void fresh0_underscore=(
        This x=(
          Void fresh2_underscore=return that
          catch return This fresh1_cast fresh1_cast
          error void)
        catch return Any fresh1_cast void
        error void)
      void)
    #typed{typeDep=This}}#norm{}}
    """
  );}@Test public void t86(){top("""
    {A={method This a(Any that)={
      if This x=that return x
      return this
      }}}
    ""","""
    {A={method This a(Any that)=(
      Void fresh0_curlyX=(
        Void fresh2_underscore=(
          This x=(
            Void fresh4_underscore=return that
            catch return This fresh3_cast fresh3_cast
            error void)
          catch return Any fresh3_cast void
          return x)
        Void fresh5_underscore=return this 
        void)
      catch return This fresh1_curlyX1 fresh1_curlyX1
      error void)
    #typed{typeDep=This}}#norm{}}          
    """
    );}

  private static String tryCatchTest(String s){
    return """
      { A={
          class method Void throwErr()=error void
          class method Library a()={method Void a() #norm{}}
          class method Library b()={method Void b() #norm{}}
          }
        C=("""+s+"""
          )
        }
        """; 
    };
  private static String tryCatchRes(String s){
    return """
      {A={
        class method Void throwErr()=error void
        class method imm Library a()={imm method imm Void a()#typed{}}
        class method imm Library b()={imm method imm Void b()#typed{}}
        #typed{}
        }
      C={imm method imm Void """//in Java multiline strings autotrims :-(
      +" "+s+"() #typed{}}#norm{}}";
   };

public static void top(String program,String out){
  Resources.clearResKeepReuse();
  Constants.testWithNoUpdatePopChecks(()->{
    var res=Init.topCache(new CachedTop(L(),L()),program);
    assertEquals(res,Core.L.parse(out));
    });
  }
public static void topFail(Class<?> kind,String program,String ...output){
  Resources.clearResKeepReuse();
  checkFail(()->Constants.testWithNoUpdatePopChecks(()->{
    Init.topCache(new CachedTop(L(),L()),program);
    }), output, kind);
  }
}