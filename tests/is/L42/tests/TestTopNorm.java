package is.L42.tests;

import org.junit.jupiter.api.Test;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.Constants;
import is.L42.common.EndError.InvalidImplements;
import is.L42.common.EndError.NotWellFormed;
import is.L42.common.EndError.TypeError;
import is.L42.generated.Core;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.top.CachedTop;
import is.L42.top.Init;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static is.L42.common.Err.hole;


public class TestTopNorm{
  @Test public void t1(){top(
    "{}",
    "{#norm{}}"
  //Take 93 sec to fail, much more than normally serialized ones [towelToS]
  /*);}@Test public void sourceUrls(){top("""
    {reuse [towelToS]
    Main=(
      Debug(S"Hello world")
      )
    }""",
    "{Trash={class method Void #apply(Library that)=void #typed{}}Task={#typed{}}#norm{}}"*/    
  //Take 26/19 sec
  );}@Test public void onlineUrls(){top("""
    {
    Trash={class method Void (Library that)=void}
    Task=Trash({
      reuse [L42.is/AdamsTowel]
      Main=Debug(S"ok")
      })
    }
    """,
    "{Trash={class method Void #apply(Library that)=void #typed{}}Task={#typed{}}#norm{}}"
  );}@Test public void traitTopUnique0(){top("""
    {
    Trait = {
      PrePriv::3 = {#norm{}}
      class method Library foo::2() = {
        #norm{typeDep=This1,This1.PrePriv::3 watched=This1} 
        }
      class method Library () = This.foo::2()
      }
    Main = Trait()
    }
    """,
    """
    {
    Trait = {
      class method Library foo::2() = { #typed{typeDep=This1, This1.PrePriv::3 watched=This1} }
      class method Library #apply() = This<:class This.foo::2()
      PrePriv::3={#typed{}}
      #typed{typeDep=This,This.PrePriv::3 coherentDep=This}}
    Main= { #typed{typeDep=This1.Trait, This1.Trait.PrePriv::3 watched=This1.Trait} }
    #norm{}}
    """
  );}@Test public void traitTopUniqueTypeDep(){top("""
    {
    Trait = {
      Priv::1 = {
        class method Library foo::2() = {
          method This1 m()
          #norm{typeDep=This1}}
        #norm{typeDep=This}}
      class method Library () = Priv::1.foo::2()
      }
    Main = Trait()
    }
    """,
    """
    {
    Trait = {
      class method Library #apply() = 
        This.Priv::1<:class This.Priv::1.foo::2()
      Priv::1 = {
        class method Library foo::2() = {method This1 m() #typed{typeDep=This1}}
        #typed{typeDep=This}}
      #typed{typeDep=This.Priv::1 coherentDep=This.Priv::1}}
    Main={method This1.Trait.Priv::1 m() #typed{typeDep=This1.Trait.Priv::1, This1.Trait, watched=This1.Trait}}
    #norm{}}
    """
  );}@Test public void traitTopUnique1(){top("""
    {
    Trait = {
      PrePriv::3 = {#norm{}}
      Priv::1 = {
        class method Library foo::2() = {
          class method class Any bar()=This2.PrePriv::3<:class Any
          #norm{typeDep=This1,This2,This2.PrePriv::3 watched=This2} 
          }
        #norm{typeDep=This,This1,This1.PrePriv::3}}
      class method Library () = Priv::1.foo::2()
      }
    Main = Trait()
    }
    """,
    """
    {
    Trait={
      class method Library #apply() = 
        This.Priv::1<:class This.Priv::1.foo::2()
      PrePriv::3 = {#typed{}}
      Priv::1 = {
        class method Library foo::2() = {
          class method class Any bar() = 
            This2.PrePriv::3<:class Any
          #typed{typeDep=This1, This2, This2.PrePriv::3
            watched=This2}}
        #typed{typeDep=This, This1, This1.PrePriv::3}}
      #typed{typeDep=This, This.PrePriv::3, This.Priv::1
             coherentDep=This.Priv::1}}
    Main={
      class method class Any bar() = 
        This1.Trait.PrePriv::3<:class Any
      #typed{typeDep=This1.Trait.Priv::1, This1.Trait, This1.Trait.PrePriv::3
             watched=This1.Trait}}
    #norm{}}
    """
  );}@Test public void traitTopUnique(){top("""
    {
    Trait = {
      Priv::1 = {
        class method Library foo::2() = { #norm{typeDep=This1, watched=This1} }
        #norm{typeDep=This}}
      class method Library () = Priv::1.foo::2()
      }
    Main = Trait()
    }
    """,
    """
    {
    Trait={
      class method Library #apply()=This.Priv::1<:class This.Priv::1.foo::2()
      Priv::1={
        class method Library foo::2() =
          {#typed{typeDep=This1 watched=This1}}
        #typed{typeDep=This}}
      #typed{typeDep=This.Priv::1 coherentDep=This.Priv::1}}
    Main={#typed{typeDep=This1.Trait.Priv::1, This1.Trait watched=This1.Trait}}
    #norm{}}
    """
  );}@Test public void interpolation() {topFail(EndError.NotWellFormed.class,"""
    {A={class method Void v()=Void\"\"\"%
       |a%"b
       \"\"\"
     }} 
     """,
     """
     [###]Error: ill formed string interpolation: [<empty>]
     The interpolated expression is empty
     [###]
     """
  );}@Test public void mispelledNative() {topFail(EndError.NotWellFormed.class,"""
    {A={#norm{nativeKind=baalean}}}
    """,
    "native kind baalean is not recognized"
   );}@Test public void tNestedInterface(){
   //TODO: If This1.A is omitted as implemented interface, the error is not very understandable
   top("""
    {A={interface method Void v() B={interface [This1]}}
    C={[This1.A.B,This1.A] method Void v()=void
      #norm{typeDep=This1.A.B,This1.A refined=v()}
      }} 
    """,
    """
    {A={
      interface method Void v()
      B={interface[This1]
       method Void v()
       #typed{typeDep=This1 refined=v()}
       }
    #typed{}}
    C={[This1.A.B, This1.A]
      method Void v()=void
      #typed{typeDep=This1.A.B, This1.A refined=v()}
      }
    #norm{}}
    """
  );}@Test public void tIfInterface(){top("""
    {A={interface }
    C={method Void v(A that)={if Void x=that void return void}
    }} 
    """,
    """
    {A={interface #typed{}}
    C={
      method Void v(This1.A that)=(
        Void fresh0_curlyX=(
          Void fresh2_underscore=(
            Void x=(
              Void fresh4_underscore=return that
              catch return Void fresh3_cast fresh3_cast
              error void)
            catch return Any fresh3_cast void
            void)
          Void fresh5_underscore=return void
          void)
        catch return Void fresh1_curlyX1 fresh1_curlyX1
        error void)
      #typed{typeDep=This1.A}}
     #norm{}}      
     """
  );}@Test public void tInfer(){top("""
      {
      A={interface method Void foo()[Void,A]}
      C={
        method Void a(A that)[_]=(this.b(that) catch A a void void)
        method Void b(A that)[_]=(this.c(that) catch Void v v void)
        method Void c(A that)[_]=this.d(that)
        method Void d(A that)[_]=that.foo()
        }} 
    ""","""
    {A={interface method Void foo()[Void,This]#typed{typeDep=This}}
    C={
      method Void a(This1.A that)=(Void fresh0_underscore=this.b(that=that)catch exception This1.A a void void)
      method Void b(This1.A that)[This1.A]=(Void fresh1_underscore=this.c(that=that)catch exception Void v v void)
      method Void c(This1.A that)[Void,This1.A]=this.d(that=that)
      method Void d(This1.A that)[Void,This1.A]=that.foo()
    #typed{typeDep=This1.A usedMethods=This1.A.foo()}}
  #norm{}}                                          
  """      
  );}@Test public void tInferBase(){top("""
    {Res=(lib={ A={} B={}
      class method Void m(A that)=(
        A a=that.of()
        B b=a.foo()
      void )} void)}
    ""","""
    {Res={#typed{}}#norm{}}
    """
  );}@Test public void tInferAdvanced(){top("""
     {Res=(lib={ A={} B={}
      class method Void m(A that)=(
        a=that.of()//we do not know the type of 'a'
        A a0=a.foo()//but a.foo()=A
        A a1=a0.of()//and a0.of()=A
        //This is NOT requiring to use the type A.of().foo()
        //TODO: can we find an example where such type is required?
        //for now we disable those types to avoid some type loops
      void )} void)}
    ""","""
    {Res={#typed{}}#norm{}}                                      
    """
  );}@Test public void fwdInitDelegate(){top("""
    {A={
      class method This foo(fwd imm Any a)=this.foo(a=a)
      class method This bar(fwd imm Any a)=this.foo(a=a)
      }
     }""","""
     {A={
     class method This foo(fwd imm Any a)=this.foo(a=a)
     class method This bar(fwd imm Any a)=this.foo(a=a)
     #typed{typeDep=This}
     }#norm{}}
     """
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
       }""",ErrMsg.infoPathNotInTyped("[###]","[###]")
  );}@Test public void notWellFormed2(){topFail(EndError.NotWellFormed.class,"""
    {Z={}
     A={
       class method Library foo(Library a,Library b)=b
       }
       B=A.foo(a={#norm{watched=This1.Z}},b={})
       }""",ErrMsg.infoPathNotInTyped("[###]","[###]")
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
      }""",ErrMsg.nativeExceptionNotLazyMessage("[###]","[###]")
  );}@Test public void notWellTypedMap(){topFail(EndError.TypeError.class,"""
    {Z={#norm{nativeKind=LazyMessage}}
     A={
       #norm{typeDep=This1.Z, coherentDep=This1.Z,
       watched=This1.Z,
       nativeKind=HIMap,
       nativePar=This1.Z,This1.Z,This1.Z,This1.Z}
       }
    }""",ErrMsg.nativeParameterViolatedConstraint("[###]","[###]","[###]")
  */
  );}@Test public void t2(){top(
   "{A={} B=(void)}","{A={#typed{}}B={#typed{}}#norm{}}"
  );}@Test public void t3(){top(
    "{A={} B=({})}","{A={#typed{}}B={#typed{}}#norm{}}"
   );}@Test public void t4(){topFail(EndError.TypeError.class,"""
    {A={interface[This1.B] #norm{typeDep=This1.B}}
     B={interface[This1.C] #norm{typeDep=This1.C}}
     C={interface #norm{}} 
     D=({})
     }""",ErrMsg.missingImplementedInterface(hole)
   );}@Test public void t5() {topFail(EndError.NotWellFormed.class,"""
    {A={interface[This] #norm{typeDep=This}}
     D=({})
     }""",ErrMsg.interfaceImplementsItself(hole)
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
     ErrMsg.nonUniqueNumber(hole,hole)
   );}@Test public void t9(){top(
     "{A={class method This foo::0()} B={class method This foo::0()} C=void}",
     "{A={class method imm This0 foo::0()#typed{typeDep=This0 close}}B={class method imm This0 foo::0()#typed{typeDep=This0 close}}C={#typed{}}#norm{}}"
   //also check refined methods are not included
   //top level init well formedness
   );}@Test public void t10(){topFail(NotWellFormed.class,
     "{B={} A={ method This1.B b() #norm{}} C=void}",
     ErrMsg.missedTypeDep(hole)
   );}@Test public void t11(){topFail(NotWellFormed.class,
     "{B={} A={ method Void b()=This1.B<:class Any #norm{}} C=void}",
     ErrMsg.missedTypeDep(hole)
   );}@Test public void t12(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      method Void b()=This1.B<:class This1.B
      #norm{typeDep=This1.B}
      } C=void}
   """,
   ErrMsg.missedCoheDep(hole)
   );}@Test public void t13(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      method Void b()=(Void x=void catch return class This1.B y y x)
      #norm{typeDep=This1.B}
      } C=void}
   """,
   ErrMsg.missedCoheDep(hole)
   );}@Test public void t14(){topFail(NotWellFormed.class,"""
   {A={#norm{typeDep=This0, watched=This0}}
    C=void}
    """,
   ErrMsg.noSelfWatch()
   );}@Test public void t15(){topFail(NotWellFormed.class,"""
   {B={D::1={#norm{}}#norm{}}
    A={
      method This1.B.D::1 b() 
      #norm{typeDep=This1.B,This1.B.D::1}}
    C=void}
    """,
   ErrMsg.missedWatched("[This1.B]")
   );}@Test public void t16(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      method Void b()=This1.B<:class This1.B.foo::1() 
      #norm{typeDep=This1.B coherentDep=This1.B}}
    C=void}
    """,
   ErrMsg.missedWatched("[This1.B]")
   );}@Test public void t17(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      method Void b()=(class This1.B bb=This1.B<:class This1.B   bb.foo::1())
      #norm{typeDep=This1.B coherentDep=This1.B}}
    C=void}
    """,
   ErrMsg.missedWatched("[This1.B]")
   );}@Test public void t18(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      method Void b()={#norm{typeDep=This2.B watched=This2.B}}
      #norm{typeDep=This1.B}}
    C=void}
    """,
   ErrMsg.missedWatched("[This1.B]")
   );}@Test public void t19(){topFail(NotWellFormed.class,"""
   {B={}
    A={
      DD::5={#norm{typeDep=This2.B watched=This2.B}}
      #norm{typeDep=This1.B}}
    C=void}
    """,
   ErrMsg.missedWatched("[This1.B]")
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
    """,ErrMsg.missedHiddenSupertypes("[This1.I]")
   );}@Test public void t29(){topFail(NotWellFormed.class,"""
   {I={interface}
    A={ class method Library foo()={[This2.I]#norm{typeDep=This2.I}} #norm{typeDep=This1.I}}
    C=void}
    """,ErrMsg.missedHiddenSupertypes("[This1.I]")
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
     "{[This.B] B={interface}}",ErrMsg.nestedClassesImplemented(hole)
   );}@Test public void t33(){topFail(InvalidImplements.class,
     "{B={} A={[This1.B]}}",ErrMsg.notInterfaceImplemented()
   );}@Test public void t34(){top(
     "{B={interface} A={[B]}}","{B={interface #typed{}} A={[This1.B]#typed{typeDep=This1.B}}#norm{}}"
   );}@Test public void t35(){top(
     "{C={B={method This m() #norm{typeDep= This0 This1.B }}} A={}}",
     "{C={B={imm method imm This0 m()#typed{typeDep=This0}}#typed{}}A={#typed{}}#norm{}}"
   );}@Test public void t36(){top(
     "{C={B={method This m() #norm{typeDep= This1.B }}} A={}}",
     "{C={B={imm method imm This0 m()#typed{typeDep=This0}}#typed{}}A={#typed{}}#norm{}}"
//   );}@Test public void t1(){top(//Note: can not support starting from a non flat program any more
//   topFail(PathNotExistent.class,"{[This1.A]}B= ={}",ErrMsg.pathNotExistant("This1.A"))
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
      ErrMsg.nestedClassesImplemented(hole)
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
      ErrMsg.methSubTypeExpectedRet(hole, hole, hole)
    );}@Test public void t47(){topFail(InvalidImplements.class,
      "{C={interface } A={interface [C] method Void a()} B={interface [C] method Any a()} D={[B,A]}}",
      ErrMsg.moreThenOneMethodOrigin("a()", hole)
    );}@Test public void t48(){topFail(InvalidImplements.class,
      "{A={interface method Any a()} B={interface method Any a()} C={[B,A]}}",
      ErrMsg.moreThenOneMethodOrigin("a()", hole)
    );}@Test public void t49(){topFail(InvalidImplements.class,
      "{C={interface } A={[C] method a()=void}}",
      ErrMsg.noMethodOrigin(hole,hole)
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
      ErrMsg.methSubTypeExpectedRet(hole,hole,hole)
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
      ErrMsg.nestedClassesImplemented(hole)
   );}@Test public void t58(){topFail(InvalidImplements.class,
      "{A={[I]} J={interface method This m()} I={interface [J] method This m()}}",
      ErrMsg.nestedClassesImplemented(hole)
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
   ErrMsg.interfaceImplementsItself(hole)
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
    """,ErrMsg.castOnPathOnlyValidIfNotInterface(hole)
  );}@Test public void t68_0(){topFail(EndError.TypeError.class,"""
    {reuse[aaa]
    Main=(
      Deploy.DeployMeta.of().#$deployLibrary(S"ba",fauxName=S"ba",lib={
        A={
         class method imm Library foo()={imm method imm Void retrived()#typed{}}
         #typed{}}
         }) 
      )
    FailHere=Void.foo()
    }
    """,hole
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
    ErrMsg.dotDotDotSouceNotExistant(hole)
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
    ErrMsg.methCallNoCompatibleMdfParametersSignature(hole,hole)
  );}@Test public void t78(){topFail(EndError.TypeError.class,"""
    {A={imm method This foo()=native{trusted:lazyCache} void}
    Test=void
    }
    """,
    ErrMsg.invalidExpectedTypeForVoidLiteral(hole)
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
  );}@Test public void zeroMakeItClose(){topFail(EndError.CoherentError.class,"""
    {A={
      class method This ()
      class method Void foo::0()=void
      }
    B=(_=A() {})
    }
    """,ErrMsg.nonCoherentPrivateStateAndPublicAbstractMethods("[#apply()]",hole)
  /*Suppressed: now the invalid natives will simply perform the body
  );}@Test public void t81(){topFail(EndError.TypeError.class,"""
    {A={imm method mut This foo()=native{trusted:lazyCache} error void}
    Test=void
    }
    """,
    ErrMsg.nativeParameterInvalidKind(hole,hole,hole,hole,hole)
  );}@Test public void t82(){topFail(EndError.TypeError.class,"""
    {A={capsule method This foo()=native{trusted:lazyCache} error void}
    Test=void
    }
    """,
    ErrMsg.nativeParameterInvalidKind(hole,hole,hole,hole,hole)
  );}@Test public void t83(){topFail(EndError.TypeError.class,"""
    {A={method This foo(Any that)=native{trusted:lazyCache} error void}
    Test=void
    }
    """,
    ErrMsg.nativeParameterCountInvalid(hole,hole,hole)
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
    ErrMsg.nonCoherentPrivateStateAndPublicAbstractMethods(hole,hole)
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
  @Test public void sumTypeStaysOk(){topFail(EndError.TypeError.class,"""
    {reuse [AdamsTowel]
     A=Trait:{method B b(B b)=b.foo()}
     C=A:{}
     B={method This b()=this}
     Main=Debug(S"ok")}
    """,hole+ErrMsg.uncompiledDependency(hole,"This1.B",hole)
    );}
  @Test public void passCacheCore(){top("""
    {
    C = {class method mut This()   read method Any doIt() = void}
    A = {
      class method Void of() = void
      mut method mut C inner::0()
      class method mut This of::0(mut C inner)
      class method mut This of(mut C inner) = this.of::0(inner=inner)
      imm method Void ft() = native{trusted:lazyCache} void
      }
    B = A.of()
    }
  """);}
  @Test public void failCacheCore1(){topFail(EndError.TypeError.class,"""
    {
    C = {class method mut This()   read method Any doIt() = void}
    A = {
      class method Void of() = void
      read method read C inner::0()
      mut method mut C #inner::0()
      class method mut This of::0(mut C inner)
      class method mut This of(mut C inner) = this.of::0(inner=inner)
      read method Any ft() = native{trusted:lazyCache}
        this.inner::0().doIt()
      }
    B = A.of()
    }
  """,hole+ErrMsg.nativeBodyInvalidThis(true,hole,hole)
        );}
  @Test public void passCacheCore1(){top("""
    {//no mut fields this time
    C = {class method mut This()   read method Any doIt() = void}
    A = {
      class method Void of() = void
      read method read C inner::0()
      mut method C #inner::0()
      class method mut This of::0(C inner)
      class method mut This of(C inner) = this.of::0(inner=inner)
      read method Any ft() = native{trusted:lazyCache}
        this.inner::0().doIt()
      }
    B = A.of()
    }
  """
  );}
@Test public void nonDetErrorRecognizedError(){topFail(EndError.TypeError.class,"""
  {reuse [AdamsTowel]
  Looping={
    class method Void loop()=this.loop()
    class method Void loopStop()=(//would need #$ name to compile
      Debug(S"Start")
      Looping.loop()
      catch error System.NonDeterministicError e ( void )
      void
      )
    }
  }
  """,ErrMsg.nonDetermisticErrorOnlyHD(hole, hole)
  );}
@Test public void nonDetErrorRecognizedException(){topFail(EndError.TypeError.class,"""
  {reuse [AdamsTowel]
  Looping={//could pass, but we fail for consistency?
    class method Void loop()=this.loop()
    class method Void loopStop()=(//would need #$ name to compile
      Debug(S"Start")
      Looping.loop()
      catch exception System.NonDeterministicError e ( void )
      void
      )
    }
  }
  """,ErrMsg.nonDetermisticErrorOnlyHD(hole, hole)
  );}
@Test public void nonDetErrorRecognizedReturn(){topFail(EndError.TypeError.class,"""
    {reuse [AdamsTowel]
    Looping={//could pass, but we fail for consistency?
      class method Void loop()=this.loop()
      class method Void loopStop()=(//would need #$ name to compile
        Debug(S"Start")
        Looping.loop()
        catch return System.NonDeterministicError e ( void )
        void
        )
      }
    }
    """,ErrMsg.nonDetermisticErrorOnlyHD(hole, hole)
    );}

@Test public void forkJoinErrShapeInvalid(){topFail(EndError.TypeError.class,"""
    {A={
      class method mut This ()
      read method read This meth() = this
      method A foo(A a)=native{trusted:forkJoin} {
        A tmp1=this.meth()
        A tmp2=a.meth()
        return this
        }
      }    }
    """,ErrMsg.nativeBodyShapeInvalid(hole, hole)
    );}
@Test public void forkJoin1Mut(){top("""
    {A={
      class method mut This ()
      read method read This meth() = this
      method A foo(mut A a)=native{trusted:forkJoin} (
        read A tmp1=this.meth()
        read A tmp2=a.meth()
        this
        )
      }    }
    """);}
@Test public void forkJoinErr2Mut(){topFail(EndError.TypeError.class,"""
    {A={    
      class method mut This ()
      read method read This meth() = this
      method A foo(mut A a,mut A b)=native{trusted:forkJoin} (
        read A tmp1=a.meth()
        read A tmp2=b.meth()
        this
        )
      }    }
    """,ErrMsg.nativeBodyShapeInvalid(hole, hole)
    );}

@Test public void forkJoin2CapsMut(){top("""
    { B={ class method mut This() }
      A={
      mut method mut B #b::0()      
      class method mut This of ()=this.of::0(b=B())    
      class method mut This of::0(capsule B b)
      mut method Void meth() = native{trusted:invalidateCache} ( _=this.#b::0() void)
      mut method Void foo()=native{trusted:forkJoin} (
        Void tmp1=this.meth()
        Void tmp2=void
        void
        )
      }    }
    """
    );}

@Test public void forkJoin1CapsMut(){top("""
    { B={ class method mut This() }
      A={
      mut method mut B #b::0()      
      class method mut This of ()=this.of::0(b=B())    
      class method mut This of::0(capsule B b)
      mut method Void meth() = native{trusted:invalidateCache} ( _=this.#b::0() void)
      mut method Void foo()=native{trusted:forkJoin} (
        Void tmp1=this.meth()
        Void tmp2=void
        void
        )
      }    }
    """
    );}

@Test public void forkJoin2capsMut(){top("""
    { B={ class method mut This() }
      A={
      mut method mut B #b1::0()
      mut method mut B #b2::0()      
      class method mut This of ()=this.of::0(b1=B(),b2=B())    
      class method mut This of::0(capsule B b1,capsule B b2)
      mut method Void meth11() = native{trusted:invalidateCache} ( _=this.#b1::0() void)
      mut method Void meth21() = native{trusted:invalidateCache} ( _=this.#b2::0() void)
      mut method Void meth12() = native{trusted:invalidateCache} ( _=this.#b1::0() void)
      mut method Void meth22() = native{trusted:invalidateCache} ( _=this.#b2::0() void)      
      mut method Void foo()=native{trusted:forkJoin} (
        Void tmp1=this.meth11()
        Void tmp2=this.meth21()
        void
        )
      }    }
    """
    );}
@Test public void forkJoin2capsMut2(){top("""
    { B={ class method mut This() }
      A={
      mut method mut B #b1::0()
      mut method mut B #b2::0()      
      class method mut This of ()=this.of::0(b1=B(),b2=B())    
      class method mut This of::0(capsule B b1,capsule B b2)
      mut method Void meth11() = native{trusted:invalidateCache} ( _=this.#b1::0() void)
      mut method Void meth21() = native{trusted:invalidateCache} ( _=this.#b2::0() void)
      mut method Void meth12() = native{trusted:invalidateCache} ( _=this.#b1::0() void)
      mut method Void meth22() = native{trusted:invalidateCache} ( _=this.#b2::0() void)      
      mut method Void foo()=native{trusted:forkJoin} (
        Void tmp1=( this.meth11() this.meth12() )
        Void tmp2=( this.meth21() this.meth22() )
        void
        )
      }    }
    """
    );}

@Test public void forkJoinErr2capsMutOverlap(){topFail(EndError.TypeError.class,"""
    { B={ class method mut This() }
      A={
      mut method mut B #b1::0()
      mut method mut B #b2::0()      
      class method mut This of ()=this.of::0(b1=B(),b2=B())    
      class method mut This of::0(capsule B b1,capsule B b2)
      mut method Void meth11() = native{trusted:invalidateCache} ( _=this.#b1::0() void)
      mut method Void meth21() = native{trusted:invalidateCache} ( _=this.#b2::0() void)
      mut method Void meth12() = native{trusted:invalidateCache} ( _=this.#b1::0() void)
      mut method Void meth22() = native{trusted:invalidateCache} ( _=this.#b2::0() void)      
      mut method Void foo()=native{trusted:forkJoin} (
        Void tmp1=this.meth11()
        Void tmp2=this.meth12()
        void
        )
      }    }
    """,hole
    );}
@Test public void forkJoinErr2capsMut2Overlap(){topFail(EndError.TypeError.class,"""
    { B={ class method mut This() }
      A={
      mut method mut B #b1::0()
      mut method mut B #b2::0()      
      class method mut This of ()=this.of::0(b1=B(),b2=B())    
      class method mut This of::0(capsule B b1,capsule B b2)
      mut method Void meth11() = native{trusted:invalidateCache} ( _=this.#b1::0() void)
      mut method Void meth21() = native{trusted:invalidateCache} ( _=this.#b2::0() void)
      mut method Void meth12() = native{trusted:invalidateCache} ( _=this.#b1::0() void)
      mut method Void meth22() = native{trusted:invalidateCache} ( _=this.#b2::0() void)      
      mut method Void foo()=native{trusted:forkJoin} (
        Void tmp1=( this.meth11() this.meth21() )
        Void tmp2=( this.meth21() this.meth22() )
        void
        )
      }    }
    """,hole
    );}
@Test public void wasAssertErrorOnBadPathDept(){top("""
  {AA={}
  WasAssertError = (_={method AA foo() = this.bar()} {})}
  """);}
@Test public void variantOfAboveThatWasNotBuggy(){topFail(EndError.TypeError.class,"""
  {AA={}
  WasAssertError = (_={} {method AA foo() = this.bar()})}
  """,hole);}
@Test public void variantOfAboveThatWasStillBuggy(){topFail(EndError.TypeError.class,"""
  {AA={}
  WasAssertError = (_={method AA foo() = this.bar()} {method AA foo() = this.bar()})}
  """,hole);}
@Test public void variant2OfAboveThatWasNotBuggy(){top("""
  {AA={}
  WasAssertError = (_={method AA foo() = this.bar()} void)}
  """);}
@Test public void variantOfAboveRequiringCTz(){top("""
  {AA={method AA bb()}
  WasAssertError = (
    _={method AA foo() = this.bar()}
    _={method AA foo() = (aa=this.bar().bb() aa)}//requires to infer the type of .bar()
    {})}
  """);}

//Disabled: send the Java compiler in loop
  /*@Test*/ public void t_manyAnds(){top("""
      {reuse [AdamsTowel]
      Foo={
        class method Bool isK(Bool a)=
          a && a && a && a && a && a && a &&
          a && a && a && a && a
        }
      Main=Debug(S"Hello world")}
      ""","""
      {#typed{typeDep=This}}#norm{}}          
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
public static void top(String program){
  Resources.clearResKeepReuse();
  Constants.testWithNoUpdatePopChecks(()->
    Init.topCache(new CachedTop(L(),L()),program)
    );
  }
public static void topFail(Class<?> kind,String program,String ...output){
  Resources.clearResKeepReuse();
  checkFail(()->Constants.testWithNoUpdatePopChecks(()->{
    Init.topCache(new CachedTop(L(),L()),program);
    }), output, kind);
  }
}