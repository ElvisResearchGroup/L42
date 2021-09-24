package is.L42.tests;

import static is.L42.common.Err.hole;
import static is.L42.tests.TestHelpers.checkFail;
import static is.L42.tools.General.L;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.constraints.InferToCore;
import is.L42.flyweight.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.I;
import is.L42.tools.AtomicTest;
import is.L42.typeSystem.TypeManipulation;


public class TestYIMethods
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   chooseGeneralT("Any a, Any b","Any")
   ),new AtomicTest(()->
   chooseGeneralT("Any a, Void b","Any")
   ),new AtomicTest(()->
   chooseGeneralT("Library a, Void b","null")
   ),new AtomicTest(()->
   chooseGeneralT("read Any a, Any b","read Any")
   ),new AtomicTest(()->
   chooseGeneralT("lent Any a, mut Any b","lent Any")
   ),new AtomicTest(()->
   chooseGeneralT("lent Any a, Any b","read Any")
   ),new AtomicTest(()->
   chooseGeneralT("capsule Void v,lent Any a, Any b","read Any")
   
   ),new AtomicTest(()->
   chooseSpecificT("Any a, Any b","Any")
   ),new AtomicTest(()->
   chooseSpecificT("Any a, Void b","Void")
   ),new AtomicTest(()->
   chooseSpecificT("Library a, Void b","null")
   ),new AtomicTest(()->
   chooseSpecificT("read Any a, Any b","Any")
   ),new AtomicTest(()->
   chooseSpecificT("lent Any a, mut Any b","mut Any")
   ),new AtomicTest(()->
   chooseSpecificT("lent Any a, Any b","capsule Any")
   ),new AtomicTest(()->
   chooseSpecificT("capsule Void v,lent Any a, Any b","capsule Void")

   ),new AtomicTest(()->
   pass("","")
   ),new AtomicTest(()->
   pass("method Void v()=void","method Void v()=void")
   ),new AtomicTest(()->
   pass("method Void v()=void, method Any a(Any a)=void",
   "method Void v()=void, method Any a(Any a)=void")
   ),new AtomicTest(()->
   pass("method read Void v()=(var read Void v=void v)",
   "method read Void v()=(var read Void v=void v)")   
   ),new AtomicTest(()->pass(
   "method Any x()=this method This m1()={if (This x)=this return this return this}",
   """
   method Any x()=this
   method This m1()=(
     Void fresh0_curlyX=(
       Void fresh2_underscore=(
         This fresh3_DecMatch=this
         Any x=fresh3_DecMatch.x()
         (
           This fresh4_typeCase=(
             Void fresh6_underscore=return x
             catch return This fresh5_cast fresh5_cast
             error void
             )
           catch return Any fresh5_cast void
           return this
           )
         )
       Void fresh7_underscore=return this
       void
       )
     catch return This fresh1_curlyX1 fresh1_curlyX1
     error void
     )
   """)),new AtomicTest(()->pass("""
     method This x()
     method Any y()
     method Any m1(Any a)={if a<:This return a.m1(a=a) return this.m1(a=a)}
     method Any m2(Any a)={if This b=a return b.m1(a=a) return this.m1(a=a)}
     method Any m3(Any a)={if This (x,y2)=a return x.m1(a=y2) return this.m1(a=a)}
     method Any m4(Any a)={if (x,This y)=this return y.m1(a=y) return this.m1(a=a)}
     method Any m5(Any a)={if This (x,This y)=a return y.m1(a=y) return this.m1(a=a)}
     method Any m6(Any a)={if This (This x,This y)=a return y.m1(a=y) return this.m1(a=a)}
     ""","""
     method This x()
     method Any y()
     method Any m1(Any a)=(
       Void fresh0_curlyX=(
         Void fresh2_underscore=(
           This fresh3_typeCase=(Void fresh5_underscore=return a
             catch return This fresh4_cast fresh4_cast
             error void)
           catch return Any fresh4_cast void
           return fresh3_typeCase.m1(a=fresh3_typeCase)
           )
         Void fresh6_underscore=return this.m1(a=a)
         void)
       catch return Any fresh1_curlyX1 fresh1_curlyX1
       error void)
     method Any m2(Any a)=(
       Void fresh7_curlyX=(
         Void fresh9_underscore=(
           This b=(
             Void fresh11_underscore=return a
             catch return This fresh10_cast fresh10_cast
             error void)
           catch return Any fresh10_cast void
           return b.m1(a=a))
         Void fresh12_underscore=return this.m1(a=a)
         void)
       catch return Any fresh8_curlyX1 fresh8_curlyX1
       error void)
     method Any m3(Any a)=(
       Void fresh13_curlyX=(
         Void fresh15_underscore=(
           This fresh16_typeMatch=(
             Void fresh18_underscore=return a
             catch return This fresh17_cast fresh17_cast
             error void
             )
           catch return Any fresh17_cast void
           (This fresh19_DecMatch=fresh16_typeMatch
             This x=fresh19_DecMatch.x()
             Any y2=fresh19_DecMatch.y()
             return x.m1(a=y2)
             )
           )
         Void fresh20_underscore=return this.m1(a=a)
         void
         )
       catch return Any fresh14_curlyX1 fresh14_curlyX1
       error void
       )
     method Any m4(Any a)=(
       Void fresh21_curlyX=(
         Void fresh23_underscore=(
           This fresh24_DecMatch=this
           This x=fresh24_DecMatch.x()
           Any y=fresh24_DecMatch.y()
           (
             This fresh25_typeCase=(
               Void fresh27_underscore=return y
               catch return This fresh26_cast fresh26_cast
               error void
               )
             catch return Any fresh26_cast void
             return fresh25_typeCase.m1(a=fresh25_typeCase)
             )
           )
         Void fresh28_underscore=return this.m1(a=a)
         void
         )
       catch return Any fresh22_curlyX1 fresh22_curlyX1
       error void
       )
     method Any m5(Any a)=(
       Void fresh29_curlyX=(
         Void fresh31_underscore=(
           This fresh32_typeMatch=(
             Void fresh34_underscore=return a
             catch return This fresh33_cast fresh33_cast
             error void
             )
           catch return Any fresh33_cast
           void
           (
             This fresh35_DecMatch=fresh32_typeMatch
             This x=fresh35_DecMatch.x()
             Any y=fresh35_DecMatch.y()
             (
               This fresh36_typeCase=(
                 Void fresh38_underscore=return y
                 catch return This fresh37_cast fresh37_cast
                 error void
                 )
               catch return Any fresh37_cast void
               return fresh36_typeCase.m1(a=fresh36_typeCase)
               )
             )
           )
         Void fresh39_underscore=return this.m1(a=a)
         void
         )
       catch return Any fresh30_curlyX1 fresh30_curlyX1
       error void
       )
     method Any m6(Any a)=(
       Void fresh40_curlyX=(
         Void fresh42_underscore=(
           This fresh43_typeMatch=(
             Void fresh45_underscore=return a
             catch return This fresh44_cast fresh44_cast
             error void
             )
           catch return Any fresh44_cast void
           (
             This fresh46_DecMatch=fresh43_typeMatch
             This x=fresh46_DecMatch.x()
             Any y=fresh46_DecMatch.y()
             (
               This fresh47_typeCase=(
                 Void fresh49_underscore=return x
                 catch return This fresh48_cast fresh48_cast
                 error void
                 )
               catch return Any fresh48_cast void
               (
                 This fresh50_typeCase=(
                   Void fresh52_underscore=return y
                   catch return This fresh51_cast fresh51_cast
                   error void
                   )
                 catch return Any fresh51_cast void
                 return fresh50_typeCase.m1(a=fresh50_typeCase)
                 )
               )
             )
           )
         Void fresh53_underscore=return this.m1(a=a)
         void
         )
       catch return Any fresh41_curlyX1 fresh41_curlyX1
       error void
       )
     """)
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
       Any y=void catch error Void v (v) y)
       void)
     ""","""
     method Any m2(Any a)=(Any x=(
       Any y=void catch error Void v (v) y)
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
//slashX       
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method Any m2()=this.m1(a=\\foo)
     ""","""
     method Any m1(This a)
     method Any m2()=this.m1(a=this.foo())
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method This foo()
     method Any m2()=this.m1(a=\\foo.m1(a=this))
     ""","""
     method Any m1(This a)
     method This foo()
     method Any m2()=this.m1(a=(
       This fresh0_receiver=this.foo()
       fresh0_receiver.m1(a=this))
       )
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method Any m2()=this.m1(a=\\#f#oo)
     ""","""
     method Any m1(This a)
     method Any m2()=this.m1(a=this.#f#oo())
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method This #foo()
     method Any m2()=this.m1(a=\\#foo.m1(a=this))
     ""","""
     method Any m1(This a)
     method This #foo()
     method Any m2()=this.m1(a=(
       This fresh0_receiver=this.#foo()
       fresh0_receiver.m1(a=this))
       )
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method This foo()
     method This bar(This squareBuilder)
     method This #squareBuilder#bar()
     method This #shortCircutSquare()
     method This #if()
     method Any m2()=this.m1(a=\\foo.bar[])
     ""","""
     method Any m1(This a)
     method This foo()
     method This bar(This squareBuilder)
     method This #squareBuilder#bar()
     method This #shortCircutSquare()
     method This #if()
     method Any m2()=this.m1(a=(
       This fresh1_receiver=this.foo()
       fresh1_receiver.bar(squareBuilder=(
         This fresh0_builder=This<:class This.#squareBuilder#bar()
         Void fresh2_underscore=(
           This fresh3_cond=This<:class This.#shortCircutSquare()
           (Void fresh4_underscore=(This fresh5_receiver=fresh3_cond.#if()fresh5_receiver.#checkTrue())
         catch exception Void fresh6_underscore void(void)))
       fresh0_builder))))
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method Any m2()=this.m1(a=\\foo())
     ""","""
     method Any m1(This a)
     method Any m2()=this.m1(a=this.foo())
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method Any m2()=this.m1(a=\\foo(this,x=void))
     ""","""
     method Any m1(This a)
     method Any m2()=this.m1(a=this.foo(that=this,x=void))
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method This foo(This squareBuilder)
     method This #shortCircutSquare()
     method This #if()
     method Any m2()=this.m1(a=\\foo[this])
     ""","""
     method Any m1(This a)
     method This foo(This squareBuilder)
     method This #shortCircutSquare()
     method This #if()
     method Any m2()=this.m1(a=this.foo(
       squareBuilder=(
         This fresh0_builder=This<:class This.#squareBuilder#foo()
         Void fresh1_underscore=(
           This fresh2_cond=This<:class This.#shortCircutSquare()
             (
             Void fresh3_underscore=(
               This fresh4_receiver=fresh2_cond.#if()
               fresh4_receiver.#checkTrue()
               )
             catch exception Void fresh5_underscore
             void
             ( Void fresh6_underscore=fresh0_builder.#squareAdd(that=this) void)
           ))
         fresh0_builder)))
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method Any foo()
     method Any m2()=this.m1(a=\\foo()())
     ""","""
     method Any m1(This a)
     method Any foo()
     method Any m2()=this.m1(a=(
       Any fresh0_receiver=this.foo()
       fresh0_receiver.#apply()))
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method Any foo()
     method Any m2()=this.m1(a=(\\foo)())
     ""","""
     method Any m1(This a)
     method Any foo()
     method Any m2()=this.m1(a=(
       Any fresh0_receiver=(this.foo())
       fresh0_receiver.#apply()))
     """)
   ),new AtomicTest(()->pass("""
     method Any m1(This a)
     method Any m2()=this.m1(a=\\(this))
     ""","""
     method Any m1(This a)
     method Any m2()=this.m1(a=This<:class This.#apply(that=this))
     """)
  //operators       
  ),new AtomicTest(()->pass("""
     method Void +(This that)=void 
     method Void m()=this+this
     ""","""
     method Void #plus0(This that)=void
     method Void m()=this.#plus0(that=this)
     """)
   ),new AtomicTest(()->fail(EndError.InferenceFailure.class,"""
     method Void *(This that)=void     
     method Void m()=this+this
     """,ErrMsg.operatorNotFound(hole,L()))
   ),new AtomicTest(()->fail(EndError.InferenceFailure.class,"""
     method Void +(This that)=void
     method Void +(Any dope)=void     
     method Void m()=this+this
     """,ErrMsg.operatorNotFound(hole,List.of("This.#plus0(that)","This.#plus0(dope)")))
   ),new AtomicTest(()->pass("""
     class method class This a(This that)=\\
     class method This b()=\\.b()
     class method This c()=This.a(that=\\.b())
     ""","""
     class method class This a(This that)=This<:class This
     class method This b()=This<:class This.b()
     class method This c()=This<:class This.a(that=This<:class This.b())
     """)
   ),new AtomicTest(()->pass("""
     class method class Any a(This that)=that<:Any
     class method class Any b(This that)=that<:Void
     class method class Any c(This that)=void<:Void
     ""","""
     class method class Any a(This that)=(Any fresh0_casted=that fresh0_casted)
     class method class Any b(This that)=(Void fresh1_casted=that fresh1_casted)
     class method class Any c(This that)=(Void fresh2_casted=void fresh2_casted)
     """)          
   ),new AtomicTest(()->pass("""
     class method This !()=!this
     class method This ~()=~this
     class method This aa()=~~!~!this
     ""","""
     class method This #bang0()=this.#bang0()
     class method This #tilde0()=this.#tilde0()
     class method This aa()=(
       This fresh0_receiver=(
         This fresh1_receiver=(
           This fresh2_receiver=(
             This fresh3_receiver=this.#bang0()
               fresh3_receiver.#tilde0())
             fresh2_receiver.#bang0())
           fresh1_receiver.#tilde0())
         fresh0_receiver.#tilde0()
       )
     """)
   ),new AtomicTest(()->pass("""
     class method Void foo1()=(var This x=this  x:=void)
     class method Void foo2()=(var This x=this  x+=void)
     method Void +(Void v)
     ""","""
     class method Void foo1()=(var This x=this x:=void)
     class method Void foo2()=(var This x=this x:=
       (Void fresh0_op=void x.#plus0(v=fresh0_op))
       )
     method Void #plus0(Void v)
     """)
   ),new AtomicTest(()->pass("""
     class method Void ()=This()
     ""","""
     class method Void #apply()=This<:class This.#apply()
     """)
   ),new AtomicTest(()->pass("""
     class method Void (This that)=This(this)
     ""","""
     class method Void #apply(This that)=
       This<:class This.#apply(that=this)
     """)
   ),new AtomicTest(()->fail(EndError.InferenceFailure.class,"""
     method Void a()=(x=this.nope() void)
     """,ErrMsg.inferenceFailNoInfoAbout(hole,hole))
   ),new AtomicTest(()->fail(EndError.InferenceFailure.class,"""
     method Void a()=(Void i1=this.nope() Library i2=this.nope() x=this.nope() void)
     """,ErrMsg.contraddictoryInfoAbout(hole,hole))
   ),new AtomicTest(()->fail(EndError.InferenceFailure.class,"""
     method Void a(Void i1, Library i2)=( x=(Void i0=this.nope() catch error Void z (i1) i2) void)
     """,ErrMsg.noCommonSupertypeAmong(hole,hole))
   /*),new AtomicTest(()->pass("""
     ClassOperators={class method class This1 ()=This1}
     class method Void #plus0(This that)=void
     class method Void (This that)=This+that
     ""","""
     ??//can not work in this setting environment
     """)*/
   ),new AtomicTest(()->pass("""
     class method Void a()=(This.a() catch error Void z z)
     ""","""
     class method Void a()=(
       Void fresh0_underscore=This<:class This.a()
       catch error Void z z void)
     """)
   ),new AtomicTest(()->pass("""
     class method Void a()=(Void v0=void catch error Void z z Void v1=void void)
     ""","""
     class method Void a()=(
       Void v0=void catch error Void z z
       (Void v1=void void))
     """)
   ),new AtomicTest(()->pass("""
     class method Void a()=(Void v0=void
       catch error Void z z
       whoops Library
       void)
     ""","""
     class method Void a()=(Void v0=void
       catch error Void z z
       catch exception Library fresh0_whoops
         error fresh0_whoops.#whoopsed(atPos={#typed{}})
       void)
     """)
   ),new AtomicTest(()->pass("""
     class method Void a()=(Void v0=void
       whoops Library
       void)
     ""","""
     class method Void a()=(Void v0=void
       catch exception Library fresh0_whoops
         error fresh0_whoops.#whoopsed(atPos={#typed{}})
       void)     """)
   ),new AtomicTest(()->pass("""
     class method Void a()=(Void v0=void
       catch error Void z z
       whoops Library, Void
       void)
     ""","""
     class method Void a()=(Void v0=void
       catch error Void z z
       catch exception Library fresh0_whoops
         error fresh0_whoops.#whoopsed(atPos={#typed{}})
       catch exception Void fresh1_whoops
         error fresh1_whoops.#whoopsed(atPos={#typed{}})
       void)
       """)     
   ),new AtomicTest(()->pass("""
     method This foo()
     method This x() method This y()
     method Void a()=((x12,y2)=this.foo() x12)
     ""","""
     method This foo()
     method This x()
     method This y()
     method Void a()=(
       This fresh0_DecMatch=this.foo()
       This x12=fresh0_DecMatch.x()
       This y2=fresh0_DecMatch.y()
       x12)
     """) 
   ),new AtomicTest(()->pass("""
     method This foo()
     method This x() method This y()
     method Void a()=(This(var This x12,y2)=this.foo() x12.foo().foo())
     ""","""
     method This foo()
     method This x()
     method This y()
     method Void a()=(
       This fresh0_DecMatch=this.foo()
       var This x12=fresh0_DecMatch.x()
       This y2=fresh0_DecMatch.y()
       (
         This fresh1_receiver=x12.foo()
         fresh1_receiver.foo()
         )
       )
     """)
   ),new AtomicTest(()->pass("""
     method Void a()=(Void v=void catch Library _ void void)
     ""","""
     method Void a()=(
       Void v=void
       catch exception Library fresh0_underscore void
       void)
     """)
   ),new AtomicTest(()->pass("""
     method Void a()=(this.a() catch Library _ void)
     ""","""
     method Void a()=(
       Void fresh0_underscore=this.a()
       catch exception Library fresh1_underscore void
       void)
     """)
   ),new AtomicTest(()->pass("""
     method Void a()={ return void }
     ""","""
     method Void a()=(
       Void fresh0_curlyX=(
         Void fresh2_underscore=return void
         void)
       catch return Void fresh1_curlyX1 fresh1_curlyX1
       error void)
     """)
   ),new AtomicTest(()->pass("""
     method Void a()={ loop (void) catch Library _ return void }
     ""","""
     method Void a()=(
       Void fresh0_curlyX=(
         Void fresh2_underscore=loop(void)
         catch exception Library fresh3_underscore return void
         void)
       catch return Void fresh1_curlyX1 fresh1_curlyX1
       error void)
     """)
   ),new AtomicTest(()->pass("""
     class method This #from(This stringLiteral)
     class method This #stringLiteralBuilder()
     method Void a()=This"a+c"
     ""","""
     class method This #from(This stringLiteral)
     class method This #stringLiteralBuilder()
     method Void a()=This<:class This.#from(stringLiteral=(
       This fresh0_builder=This<:class This.#stringLiteralBuilder()
       Void fresh1_underscore=fresh0_builder.#la()
       Void fresh2_underscore=fresh0_builder.#splus()
       Void fresh3_underscore=fresh0_builder.#lc()
       fresh0_builder
       ))
     """)
     ),new AtomicTest(()->pass("""
     class method This #from(This stringLiteral)
     class method This #stringLiteralBuilder()
     class method Void #stringAddAll(This that)
     method Void a()=This"a+%this c"
     ""","""
     class method This #from(This stringLiteral)
     class method This #stringLiteralBuilder()
     class method Void #stringAddAll(This that)
     method Void a()=This<:class This.#from(stringLiteral=(
       This fresh0_builder=This<:class This.#stringLiteralBuilder()
       Void fresh3_underscore=fresh0_builder.#stringAddAll(that=(
         This fresh1_builder=This<:class This.#stringLiteralBuilder()
         Void fresh4_underscore=fresh1_builder.#la()
         Void fresh5_underscore=fresh1_builder.#splus()
         fresh1_builder))
       Void fresh6_underscore=fresh0_builder.#stringAddExpr(that=this)
       Void fresh7_underscore=fresh0_builder.#stringAddAll(that=(
         This fresh2_builder=This<:class
         This.#stringLiteralBuilder()
         Void fresh8_underscore=fresh2_builder.#sspace()
         Void fresh9_underscore=fresh2_builder.#lc()
         fresh2_builder))
       fresh0_builder))
       """)
   ),new AtomicTest(()->pass("""
     method This #if()
     method This #checkTrue()
     method This a(This b,This c)=if this b else c
     ""","""
     method This #if()
     method This #checkTrue()
     method This a(This b,This c)=(
       Void fresh0_underscore=(
         This fresh1_receiver=this.#if()
         fresh1_receiver.#checkTrue())
     catch exception Void fresh2_underscore c
     b)
     """)      
   ),new AtomicTest(()->pass("""
     method This #if()
     method This #checkTrue()
     method This a(This b,This c)=if this b
     ""","""
     method This #if()
     method This #checkTrue()
     method This a(This b,This c)=(
       Void fresh0_underscore=(
         This fresh1_receiver=this.#if()
         fresh1_receiver.#checkTrue())
     catch exception Void fresh2_underscore void
     b)
     """)
   ),new AtomicTest(()->pass("""
     method This #if()
     method This #checkTrue()
     method This a(This b,This c)=if (this) b
     ""","""
     method This #if()
     method This #checkTrue()
     method This a(This b,This c)=(
       This fresh0_cond=(this)
       ( Void fresh1_underscore=(
           This fresh2_receiver=fresh0_cond.#if()
           fresh2_receiver.#checkTrue())
         catch exception Void fresh3_underscore void 
         b))
     """)
   ),new AtomicTest(()->pass("""
     method This #if()
     method This #checkTrue()
     method This #shortCircutSquare()
     method This foo(This squareBuilder)
     method This a(This b,This c)=this.foo[c;b; key=c, val=b]
     ""","""
     method This #if()
     method This #checkTrue()
     method This #shortCircutSquare()
     method This foo(This squareBuilder)
     method This a(This b, This c)=this.foo(squareBuilder=(
       This fresh0_builder=This<:class This.#squareBuilder#foo()
       Void fresh1_underscore=(
         This fresh2_cond=This<:class This.#shortCircutSquare()
         ( Void fresh3_underscore=(
             This fresh4_receiver=fresh2_cond.#if()
             fresh4_receiver.#checkTrue())
           catch exception Void fresh5_underscore void
           ( Void fresh6_underscore=fresh0_builder.#squareAdd(that=c)
             Void fresh7_underscore=fresh0_builder.#squareAdd(that=b)
             Void fresh8_underscore=fresh0_builder.#squareAdd(key=c, val=b)
             void)
           )
         )
       fresh0_builder))
     """)
     ),new AtomicTest(()->pass("""
     method Bool v(This a,This b)=(x= a!=b x)
     method Bool #bangequal0(This that)=Bool.k()
     Bool={}
     ""","""
     method This.Bool v(This a, This b)=(
       This.Bool x=a.#bangequal0(that=b)
       x
       )
     method This.Bool #bangequal0(This that)=
       This.Bool<:class This.Bool.k()     
     Bool={#norm{}}
     """)
     ),new AtomicTest(()->pass("""
     method Void v(This a,This b)=while a!=b void
     method Bool #bangequal0(This that)=Bool.k()
     Bool={}
     ""","""
     method Void v(This a, This b)=(
       Void fresh0_underscore=loop(
         Void fresh1_underscore=(
           This.Bool fresh2_receiver=a.#bangequal0(that=b)
           fresh2_receiver.#checkTrue()
           )
         void
         )
       catch exception Void fresh3_underscore void error
       void
       )
     method This.Bool #bangequal0(This that)=
       This.Bool<:class This.Bool.k()
     Bool={#norm{}}
     """)
          ),new AtomicTest(()->pass("""
     method Void v(This as)=for var imm a in as (a:=a)
     method This #varIterator()
     method This #startIndex()
     method This #hasElem(This that)
     method This #elem#imm(This that)
     mut method Void #update#imm(This that, This val)
     method This #if()
     method This #shortCircut#andand()
     method This #shortResult#andand()
     method This #shortProcess#andand(This that,This other)
     ""","""
     method Void v(This as)=(
       This fresh0_xIt=as.#varIterator()
       var This fresh1_xIndex=as.#startIndex(),,,,(
         Void fresh2_underscore=loop(
           This fresh3_cond=(
             This fresh4_op3=fresh0_xIt.#hasElem(that=fresh1_xIndex)
             (
               This fresh5_op3=fresh4_op3.#shortCircut#andand()
               (
                 Void fresh6_underscore=(
                   This fresh7_receiver=fresh5_op3.#if()
                   fresh7_receiver.#checkTrue()
                   )
                 catch exception Void fresh8_underscore
                   fresh4_op3.#shortProcess#andand(that=fresh5_op3,
                     other=fresh0_xIt.#incomplete(that=fresh1_xIndex)
                     )
                 fresh4_op3.#shortResult#andand(that=fresh5_op3)
                 )
               )
             ),,,,(
               Void fresh9_underscore=(
                 This fresh10_receiver=fresh3_cond.#if()
                 fresh10_receiver.#checkTrue()
                 )
               catch exception Void fresh11_underscore(
                 Void fresh12_underscore=fresh0_xIt.#close(that=fresh1_xIndex)
                 exception void
                 )
               (
                 var This a=fresh0_xIt.#elem#imm(that=fresh1_xIndex)
                 Void fresh13_underscore=(
                   a:=fresh0_xIt.#update#imm(that=fresh1_xIndex, val=a)
                   )
                 Void fresh14_underscore=fresh1_xIndex:=fresh1_xIndex.#succ()
                 void
                 )
               )
             )
           catch exception Void fresh15_underscore void
           error void
           )
         )
     method This #varIterator()
     method This #startIndex()
     method This #hasElem(This that)
     method This #elem#imm(This that)
     mut method Void #update#imm(This that, This val)
     method This #if()
     method This #shortCircut#andand()
     method This #shortResult#andand()
     method This #shortProcess#andand(This that, This other)
     """)
),new AtomicTest(()->pass("""
  method class Any v()={ imm Any a={ return void } return Void}
  ""","""
  method class Any v()=(
    Void fresh0_curlyX=(
      Any a=(
        Void fresh2_curlyX=(
          Void fresh4_underscore=return void
          void)
      catch return Any fresh3_curlyX1 fresh3_curlyX1
      error void)
    Void fresh5_underscore=return Void<:class Void
    void)
  catch return class Any fresh1_curlyX1 fresh1_curlyX1
  error void)
  """     
  )));}
static Program p0=Program.parse("""
  {
   method Void m()
   method This k(Any x)
   method Void #lt0(This a)
   method Void #lt0(This a,Void b)
   method Void #ltequal0(This a,Any b)
   #norm{typeDep=This}}
  """);
public static void chooseGeneralT(String in,String out){
  Full.L fl=(Full.L)Program.parse("{method Void m("+in+")}").top;
  var ts=((Full.L.MWT)fl.ms().get(0)).mh().pars();
  var res=p0._chooseGeneralT(L(ts.stream().map(TypeManipulation::toCore)));
  assertEquals(out,""+res);
  }
public static void chooseSpecificT(String in,String out){
  Full.L fl=(Full.L)Program.parse("{method Void m("+in+")}").top;
  var ts=((Full.L.MWT)fl.ms().get(0)).mh().pars();
  var res=TypeManipulation._chooseSpecificT(p0,L(ts.stream().map(TypeManipulation::toCore)),L());
  assertEquals(out,""+res);
  }  
public static void pass(String l,String out){
  Core.L cl=Program.parse("{"+out+" #norm{typeDep=This This.$plus0 This.Bool coherentDep=This This.$plus0 This.Bool usedMethods=This.Bool.k(),This.Bool.#checkTrue()}}").topCore();
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
    I i=new I(C.of("C",-1),p,G.of(mhi));
    var cei=new InferToCore(i,ctz).compute(ei);
    var mwt=new Core.L.MWT(L(),L(),mhi,"",cei);
    c.add(mwt);
    }); 
  }
}