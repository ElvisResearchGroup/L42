package is.L42.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import is.L42.common.Err;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.meta.Sum;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.AtomicTest;
import is.L42.top.Init;

public class TestSum
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("#norm{}}","#norm{}}","#norm{}}")
   ),new AtomicTest(()->
   pass("A={#norm{}}#norm{}}","B={#norm{}}#norm{}}","A={#norm{}}B={#norm{}}#norm{}}")
   ),new AtomicTest(()->pass("""
     A={#norm{}}
   #norm{}}""",/*second lib after this line*/"""
     A={#norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     A={#norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     @{foo}method Void m()
   #norm{}}""",/*second lib after this line*/"""
     @{bar}method Void m()
   #norm{}}""",/*expected lib after this line*/"""
     @{foo}@{bar}method Void m()
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     method Void m1()
   #norm{}}""",/*second lib after this line*/"""
     method Void m2()
   #norm{}}""",/*expected lib after this line*/"""
     method Void m1() method Void m2()
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     @{foo}method Void m()
   #norm{}}""",/*second lib after this line*/"""
     @{bar}method Void m()=void
   #norm{}}""",/*expected lib after this line*/"""
     @{foo}@{bar}method Void m()=void
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     @{foo}method Void m()=void
   #norm{}}""",/*second lib after this line*/"""
     @{bar}method Void m()
   #norm{}}""",/*expected lib after this line*/"""
     @{foo}@{bar}method Void m()=void
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     @{foo}method Void m()=void
   #norm{}}""",/*second lib after this line*/"""
     @{bar}method Void m()=void
   #norm{}}""",/*expected lib after this line*/"""
   @{foo}method Void m()=(..)
   Conflicting implementation: the method is implemented on both sides of the sum
   file:[###]
   """/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     I={interface method This2.D.B m() #norm{typeDep=This2.D.B}}
     C={[This1.I] method This2.D.C m() #norm{typeDep=This1.I,This2.D.C refined=m()}}
   #norm{}}""",/*second lib after this line*/"""
     I={interface method This2.D.B m() #norm{typeDep=This2.D.B}}
     C={[This1.I] method This2.D.B m() #norm{typeDep=This1.I,This2.D.B refined=m()}}
   #norm{}}""",/*expected lib after this line*/"""
     I={interface method This2.D.B m() #norm{typeDep=This2.D.B}}
     C={[This1.I] method This2.D.C m() #norm{typeDep=This1.I,This2.D.C, This2.D.B refined=m()}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     I={interface method This2.D.B m() #norm{typeDep=This2.D.B}}
   #norm{}}""",/*second lib after this line*/"""
     I={#norm{typeDep=This2.D.K coherentDep=This2.D.K watched=This2.D.K}}
   #norm{}}""",/*expected lib after this line*/"""
     I={interface method This2.D.B m() #norm{typeDep=This2.D.B,This2.D.K}}
   #norm{}}"""/*next test after this line*/)   
   ),new AtomicTest(()->pass("""
     I={interface method Void m() #norm{}}
     B={[This1.I] method Void m() #norm{typeDep=This1.I refined=m()}}
   #norm{}}""",/*second lib after this line*/"""
     C={interface #norm{}}
     I={interface[This1.C] method Void m() #norm{typeDep=This1.C}}
   #norm{}}""",/*expected lib after this line*/"""
     I={interface[This1.C] method Void m() #norm{typeDep=This1.C}}
     B={[This1.I,This1.C] method Void m() #norm{typeDep=This1.I This1.C refined=m()}}
     C={interface #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     I={interface method Void m() #norm{}}
     B={[This1.I] method Void m() #norm{typeDep=This1.I refined=m()}}
   #norm{}}""",/*second lib after this line*/"""
     C={interface #norm{}}
     I={interface[This1.C] method Void m() #norm{typeDep=This1.C}}
     B={method Void m() #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     I={interface[This1.C] method Void m() #norm{typeDep=This1.C}}
     B={[This1.I,This1.C] method Void m() #norm{typeDep=This1.I This1.C refined=m()}}
     C={interface #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface method Void m() #norm{}}
     B={[This1.I] method Void m() #norm{typeDep=This1.I refined=m()}}
   #norm{}}""",/*second lib after this line*/"""
     J={interface method Void m() #norm{}}
     B={[This1.J] method Void m() #norm{typeDep=This1.J refined=m()}}
   #norm{}}""",/*expected lib after this line*/"""
     nested class B={ [ This1.I This1.J ] m() }
     No unique source for m(); it originates from both This1.J and This1.I
     file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface method Void m() #norm{}}
     B={[This1.I] method Void m() #norm{typeDep=This1.I refined=m() hiddenSupertypes=This1.I}}
   #norm{}}""",/*second lib after this line*/"""
     I={method Void k() #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     nested class I={interface m() }
     nested class I={ k() }
     This interface is privately implemented but the summed version is larger: { k() }
     file:[###]"""/*next test after this line*/)    
   ),new AtomicTest(()->fail("""
     I={interface method Void m() #norm{}}
     B={[This1.I] method Void m() #norm{typeDep=This1.I refined=m()}}
   #norm{}}""",/*second lib after this line*/"""
     I={interface method Any m() #norm{}}
     C={[This1.I] method Any m() #norm{typeDep=This1.I refined=m()}}
   #norm{}}""",/*expected lib after this line*/"""
     method Void m()
     The two headers are incompatible:
     method Any m()
     file:[###]"""/*next test after this line*/)    
   ),new AtomicTest(()->fail("""
     I={interface method Void m() #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={interface method Library m() #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     method Void m()
     The methods have different signatures:
     method Library m()
     But there is no local refinement between those two signatures
     file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     E={interface #norm{}} R={interface #norm{}}
     I3={[This1.I2] method This1.E m() #norm{typeDep=This1.I2 This1.E refined=m()}}
     I1={interface method Any m() #norm{}}
     I2={interface [This1.I1] method This1.R m() #norm{typeDep=This1.I1 This1.R refined=m()}}      
   #norm{}}""",/*second lib after this line*/"""
     E={interface #norm{}} R={interface #norm{}}
     I3={[This1.I0] method This1.R m() #norm{typeDep=This1.I0 This1.R refined=m()}}
     I0={interface method This1.E m() #norm{typeDep=This1.E}}
     I2={interface method This1.E m() #norm{typeDep=This1.E}}
   #norm{}}""",/*expected lib after this line*/"""
     method This1.E m()
     The methods have different signatures:
     method This1.R m()
     But there is ambiguous refinement between those two signatures
     file:[###]"""/*next test after this line*/)
//TODO: the case above trigger a difficoult to trigger error.
//But only because of smart line orderings. Can we find a test that trigger it for any line order?
//See below for different line ordering and different result 
   ),new AtomicTest(()->fail("""
     E={interface #norm{}} R={interface #norm{}}
     I1={interface method Any m() #norm{}}
     I2={interface [This1.I1] method This1.R m() #norm{typeDep=This1.I1 This1.R refined=m()}}
     I3={[This1.I2] method This1.E m() #norm{typeDep=This1.I2 This1.E refined=m()}}      
   #norm{}}""",/*second lib after this line*/"""
     E={interface #norm{}} R={interface #norm{}}
     I0={interface method This1.E m() #norm{typeDep=This1.E}}
     I2={interface method This1.E m() #norm{typeDep=This1.E}}
     I3={[This1.I0] method This1.R m() #norm{typeDep=This1.I0 This1.R refined=m()}}
   #norm{}}""",/*expected lib after this line*/"""
     method This1.R m()
     The methods have different signatures:
     method This1.E m()
     But there is no local refinement between those two signatures
     file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     E::1={interface #norm{}}
     I1={interface [This1.E::1] method Any m() #norm{close typeDep=This1.E::1 This1, watched=This1}}
   #norm{}}""",/*second lib after this line*/"""
     R::1={interface #norm{}}
     I1={interface [This1.R::1] method Any m() #norm{close typeDep=This1.R::1 This1, watched=This1}}
   #norm{}}""",/*expected lib after this line*/"""
   nested class I1={interface m() }
   nested class I1={interface m() }
   One of the two interfaces in nested class I1
   is close (have private methods or implements private interfaces). Only open interfaces can be composed
   file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     E::1={interface #norm{}}
     I1={interface [This1.E::1] method Any k() #norm{close typeDep=This1.E::1 This1, watched=This1}}
   #norm{}}""",/*second lib after this line*/"""
     R={interface #norm{}}
     I1={[This1.R] method Any m() #norm{typeDep=This1.R}}
   #norm{}}""",/*expected lib after this line*/"""
     E::1={interface #norm{}}
     I1={interface[This1.E::1, This1.R] method Any k() method Any m() #norm{typeDep=This1.E::1, This1, This1.R watched=This1 close}}
     R={interface #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={ #norm{}}
     C={ #norm{typeDep=This1.I watched=This1.I}}
   #norm{}}""",/*expected lib after this line*/"""
     nested class I={ }
     The nested class can not be turned into an interface; since its privates are used by other code (is watched)
     file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={ #norm{}}
     C={ #norm{typeDep=This1.I coherentDep=This1.I}}
   #norm{}}""",/*expected lib after this line*/"""
     nested class I={ }
     The nested class can not be turned into an interface; since it is used with 'class' modifier (is required coherent)
     file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={ method Void v()=void #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     nested class I={ v()=(..) }
     The nested class can not be turned into an interface; some public methods are implemented
     file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={#typed{}}
     B={#typed{}}
     C={#norm{}}
     D={#norm{}}
   #norm{}}""",/*second lib after this line*/"""
     A={#typed{}}
     B={#norm{}}
     C={#typed{}}
     D={#norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     A={#typed{}}
     B={#norm{}}
     C={#norm{}}
     D={#norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     B={ method Void foo::1()=void #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     B={ method Void foo::1()=error void #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     B={
       method Void foo::1()=void
       method Void foo::2()=error void
       #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     B={[This2.E] method This self() #norm{typeDep=This,This2.E refined=self()}}
   #norm{}} E={interface method This self() #norm{typeDep=This}}
   """,/*second lib after this line*/"""
     B={[This2.E] method This2.E self() #norm{typeDep=This,This2.E refined=self()}}
   #norm{}} E={interface method This self() #norm{typeDep=This}}
   """,/*expected lib after this line*/"""
     B={[This2.E] method This self() #norm{typeDep=This,This2.E refined=self()}}
   #norm{}} E={interface method This self() #norm{typeDep=This}}
   """/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     B={#norm{}}
   #norm{}}""",/*second lib after this line*/"""
     C={#norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     B={#norm{}} C={#norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
   #norm{}}""",/*second lib after this line*/"""
     B={#norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     B={#norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     B::1={#norm{}}
   #norm{}}""",/*second lib after this line*/"""
   #norm{}}""",/*expected lib after this line*/"""
     B::1={#norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     B::1={#norm{}}
   #norm{}}""",/*second lib after this line*/"""
     B::1={#norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     B::1={#norm{}} B::2={#norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     B::1={#norm{}}
   #norm{}}""",/*second lib after this line*/"""
     B::1={#norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     B::1={#norm{}} B::2={#norm{}}
   #norm{}}"""/*next test after this line*/)//twice the same test to check that is a determinitic rename and no counter is statically shared
   ),new AtomicTest(()->fail("""
     B={method Void m() #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     B={method This m() #norm{typeDep=This}}
   #norm{}}""",/*expected lib after this line*/"""
   method Void m()
   The methods have different signatures:
   method This m()
   But there is no local refinement between those two signatures
   file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     B={method Void m() #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={interface method Void m()#norm{}} B={[This1.I] method Void m() #norm{typeDep=This,This1.I refined=m()}}
   #norm{}}""",/*expected lib after this line*/"""
     B={[This1.I] method Void m() #norm{typeDep=This,This1.I refined=m()}}
     I={interface method Void m()#norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     J={interface method Void m()#norm{}} B={[This1.J] method Void m() #norm{typeDep=This,This1.J refined=m()}}
   #norm{}}""",/*second lib after this line*/"""
     I={interface method Void m()#norm{}} B={[This1.I] method Void m() #norm{typeDep=This,This1.I refined=m()}}
   #norm{}}""",/*expected lib after this line*/"""
   nested class B={ [ This1.J This1.I ] m() }
   No unique source for m(); it originates from both This1.I and This1.J
   file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I1={interface[This1.I2] #norm{typeDep=This1.I2}} I2={interface #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I2={interface[This1.I1] #norm{typeDep=This1.I1}} I1={interface #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
   nested class I1={interface [ This1.I2 This ] }
   The sum would induce a circular interface implementation for [This1.I2, This]
   file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={interface #norm{}}
     B={interface[This1.A] #norm{typeDep=This1.A}}
     C={interface #norm{}}
     D={interface[This1.C] #norm{typeDep=This1.C}}
     E={interface #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     A={interface #norm{}}
     B={interface #norm{}}
     C={interface[This1.B] #norm{typeDep=This1.B}}
     D={interface #norm{}}
     E={interface[This1.D] #norm{typeDep=This1.D}}     
   #norm{}}""",/*expected lib after this line*/"""
     A={interface #norm{}}
     B={interface[This1.A] #norm{typeDep=This1.A}}
     C={interface[This1.B,This1.A] #norm{typeDep=This1.B,This1.A}}
     D={interface[This1.C,This1.B,This1.A] #norm{typeDep=This1.C,This1.B,This1.A}}
     E={interface[This1.D,This1.C,This1.B,This1.A] #norm{typeDep=This1.D,This1.C,This1.B,This1.A}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={interface method @{bar} Void foo() #norm{}}
     B={interface #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     A={interface #norm{}}
     B={interface[This1.A] #norm{typeDep=This1.A}}     
   #norm{}}""",/*expected lib after this line*/"""
     A={interface method @{bar} Void foo() #norm{}}
     B={interface[This1.A] method Void foo() #norm{typeDep=This1.A refined=foo()}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={interface method Void m::1()  method Void k() #norm{close}}
     B={[This1.A] method Void m::1()=void  method Void k()=void 
       #norm{typeDep=This1.A refined=m::1() k()}}
   #norm{}}""",/*second lib after this line*/"""
     A={interface #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
   nested class A={interface k() }
   nested class A={interface }
   One of the two interfaces in nested class A
   is close (have private methods or implements private interfaces). Only open interfaces can be composed
   file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={interface  method Void k() #norm{}}
     B={[This1.A]  method Void k()=void 
       #norm{typeDep=This1.A refined= k()}}
   #norm{}}""",/*second lib after this line*/"""
     A={interface method Void m::1() #norm{close}}
   #norm{}}""",/*expected lib after this line*/"""
   nested class A={interface k() }
   nested class A={interface }
   One of the two interfaces in nested class A
   is close (have private methods or implements private interfaces). Only open interfaces can be composed
   file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={interface  method Void k() #norm{}}
     B={[This1.A]  method Void k()=void 
       #norm{typeDep=This1.A refined= k()}}
   #norm{}}""",/*second lib after this line*/"""
     A={interface method Void m::1() method Void k() #norm{close}}
   #norm{}}""",/*expected lib after this line*/"""
   nested class A={interface k() }
   nested class A={interface k() }
   One of the two interfaces in nested class A
   is close (have private methods or implements private interfaces). Only open interfaces can be composed 
   file:[###]"""/*next test after this line*/)   
   ),new AtomicTest(()->fail("""
     method Library lib()=
       {[This1.A]  method Void foo()= void #norm{typeDep=This1.A refined=foo()}}
     A={interface method Void foo() #norm{}}
     B={[This1.A]  method Void foo()= void #norm{typeDep=This1.A refined=foo()}}
   #norm{typeDep=This.A hiddenSupertypes=This.A}}""",/*second lib after this line*/"""
     A={interface method Void bar() #norm{}}     
   #norm{}}""",/*expected lib after this line*/"""
   nested class A={interface foo() }
   nested class A={interface bar() }
   This interface is privately implemented but the summed version is larger: {interface bar() }
   file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface method Void foo::1() #norm{close}}
     A={[This1.I] method Void foo::1()=void #norm{typeDep=This1.I refined=foo::1()}}
   #norm{}}""",/*second lib after this line*/"""
     A={interface #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
   nested class A={ [ This1.I ] }
   The nested class can not be turned into an interface; a close interface is implemented
   file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={ K::1={#norm{}} #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     A={method Void a() #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     A={ method Void a() K::1={#norm{}} #norm{}}
   #norm{}}"""/*next test after this line*/)

   ),new AtomicTest(()->fail("""
     A={method Void m()#norm{}} I={interface method Any m() #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     A={[This1.I] #norm{typeDep=This1.I}}
     I={interface #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
   method Void m()
   The methods have different signatures:
   method Any m()
   But there is no local refinement between those two signatures
   [###]"""/*next test after this line*/)
   //NOTE: we chose not to make a special case for Any!
   //the test below checks that is ok with both sides to Any
   ),new AtomicTest(()->pass("""
     A={method Any m()#norm{}} I={interface method Any m() #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     A={[This1.I] #norm{typeDep=This1.I}}
     I={interface #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     A={[This1.I] method Any m()#norm{typeDep=This1.I refined=m()}} I={interface method Any m() #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface method Any m() #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={ method Any m() #norm{}}
     C={ #norm{typeDep=This1.I, coherentDep=This1.I}}
   #norm{}}""",/*expected lib after this line*/"""
   nested class I={ m() }
   The nested class can not be turned into an interface; since it is used with 'class' modifier (is required coherent)
   [###]"""/*next test after this line*/)
   //Note: exploring minimal conditions to turn interface in class 
   ),new AtomicTest(()->pass("""
     I={interface #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={ #norm{close}}
   #norm{}}""",/*expected lib after this line*/"""
     I={interface #norm{close}}
   #norm{}}"""/*next test after this line*/)
   ));}
//TODO: test add interface with class with a class method
   
   @Test public void test2(){
     miniFrom("A.B","A.B.C","This.C");
     miniFrom("A.B.D.E","A.B.C","This2.C");
     miniFrom("A.B.C.D.E","A.B","This3");
     }
   void miniFrom(String into,String that,String res){
     P pThat=P.parse("This."+that);
     P pInto=P.parse("This."+into);
     P pRes=P.parse(res);
     assertEquals(pRes,Sum.miniFrom(pInto.toNCs().cs(),pThat.toNCs().cs()));
     }
public static void pass(String sl1,String sl2,String sl3){
  Resources.clearResKeepReuse();
  Init init1=new Init("{A={"+sl1+"#norm{}}");
  CoreL l1=init1.p._ofCore(P.of(0,List.of(C.of("A",-1))));
  Init init2=new Init("{A={"+sl2+"#norm{}}");
  CoreL l2=init2.p._ofCore(P.of(0,List.of(C.of("A",-1))));
  CoreL l3Actual=new Sum().compose(init1.p,C.of("A",-1),l1, l2,(Function<L42£LazyMsg,L42Any>)null,null);
  Init init3=new Init("{A={"+sl3+"#norm{}}");
  CoreL l3Expected=init3.p._ofCore(P.of(0,List.of(C.of("A",-1))));
  assertEquals(l3Expected, l3Actual);
  }
static class FailErr extends Error{}
public static void fail(String sl1,String sl2,String err){
  Resources.clearResKeepReuse();
  String[]msg={null};
  Init init1=new Init("{A={"+sl1+"#norm{}}");
  CoreL l1=init1.p._ofCore(P.of(0,List.of(C.of("A",-1))));
  Init init2=new Init("{A={"+sl2+"#norm{}}");
  CoreL l2=init2.p._ofCore(P.of(0,List.of(C.of("A",-1))));
  Function<L42£LazyMsg,L42Any>wrap=lm->{msg[0]=lm.getMsg();throw new FailErr();};
  try{new Sum().compose(init1.p,C.of("A",-1),l1,l2,wrap,wrap);Assertions.fail();}
  catch(FailErr fe){}
  Err.strCmp(msg[0],err);
  }
}