package is.L42.tests;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
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
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.meta.Sum;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.AtomicTest;
import is.L42.top.Init;
import is.L42.top.Top;
import is.L42.translationToJava.Loader;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

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
   Invalid method @{foo}imm method imm Void m()=(..)
   Conflicting implementation: the method is implemented on both side of the sum
   [file:[###]
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
     Invalid nested class B={ [ This1.I This1.J ] m() }
     No unique source for m(); it originates from both This1.J and This1.I
     [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface method Void m() #norm{}}
     B={[This1.I] method Void m() #norm{typeDep=This1.I refined=m() hiddenSupertypes=This1.I}}
   #norm{}}""",/*second lib after this line*/"""
     I={method Void k() #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     Invalid nested class I={interface m() }
     This interface is privately implemented  but the summed version is larger: { k() }
     [file:[###]"""/*next test after this line*/)    
   ),new AtomicTest(()->fail("""
     I={interface method Void m() #norm{}}
     B={[This1.I] method Void m() #norm{typeDep=This1.I refined=m()}}
   #norm{}}""",/*second lib after this line*/"""
     I={interface method Any m() #norm{}}
     C={[This1.I] method Any m() #norm{typeDep=This1.I refined=m()}}
   #norm{}}""",/*expected lib after this line*/"""
     Invalid method imm method imm Void m()
     Both versions of this method are implemented, but the other have a different header:
     imm method imm Any m()
     [file:[###]"""/*next test after this line*/)    
   ),new AtomicTest(()->fail("""
     I={interface method Void m() #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={interface method Library m() #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     Invalid method imm method imm Void m()
     The other method have a different signature:
     imm method imm Library m()
     But there is no local refinement between those two signatures
     [file:[###]"""/*next test after this line*/)
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
     Invalid method imm method imm This1.E m()
     The other method have a different signature:
     imm method imm This1.R m()
     But there is ambiguous refinement between those two signatures
     [file:[###]"""/*next test after this line*/)
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
     Invalid method imm method imm This1.R m()
     The other method have a different signature:
     imm method imm This1.E m()
     But there is no local refinement between those two signatures
     [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     E::1={interface #norm{}}
     I1={interface [This1.E::1] method Any m() #norm{close typeDep=This1.E::1 This1, watched=This1}}
   #norm{}}""",/*second lib after this line*/"""
     R::1={interface #norm{}}
     I1={interface [This1.R::1] method Any m() #norm{close typeDep=This1.R::1 This1, watched=This1}}
   #norm{}}""",/*expected lib after this line*/"""
     Invalid nested class I1={interface [ This1.E::1 ] m() }
     The two nested classes are both closed, thus can not be composed.
     [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     E::1={interface #norm{}}
     I1={interface [This1.E::1] method Any m() #norm{close typeDep=This1.E::1 This1, watched=This1}}
   #norm{}}""",/*second lib after this line*/"""
     R={interface #norm{}}
     I1={interface [This1.R] method Any m() #norm{typeDep=This1.R}}
   #norm{}}""",/*expected lib after this line*/"""
     E::1={interface #norm{}}
     I1={interface[This1.E::1, This1.R] imm method imm Any m()#norm{typeDep=This1.E::1, This1, This1.R watched=This1 close}}
     R={interface #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={ #norm{}}
     C={ #norm{typeDep=This1.I watched=This1.I}}
   #norm{}}""",/*expected lib after this line*/"""
     Invalid nested class I={ }
     The nested class can not be turned into an interface; since its privates are used by other code (is watched)
     [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={ #norm{}}
     C={ #norm{typeDep=This1.I coherentDep=This1.I}}
   #norm{}}""",/*expected lib after this line*/"""
     Invalid nested class I={ }
     The nested class can not be turned into an interface, since it is used with 'class' modifier (is required coherent)
     [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface #norm{}}
   #norm{}}""",/*second lib after this line*/"""
     I={ method Void v()=void #norm{}}
   #norm{}}""",/*expected lib after this line*/"""
     Invalid nested class I={ v()=(..) }
     The nested class can not be turned into an interface; some public methods are implemented
     [file:[###]"""/*next test after this line*/)
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

//TODO: normalize privates in sum right!
   ));}
public static void pass(String sl1,String sl2,String sl3){
  Resources.clearRes();
  Init init1=new Init("{A={"+sl1+"}");
  Core.L l1=init1.p._ofCore(P.of(0,List.of(new C("A",-1))));
  Init init2=new Init("{A={"+sl2+"}");
  Core.L l2=init2.p._ofCore(P.of(0,List.of(new C("A",-1))));
  Core.L l3Actual=new Sum().compose(l1, l2,null,null);
  Init init3=new Init("{A={"+sl3+"}");
  Core.L l3Expected=init3.p._ofCore(P.of(0,List.of(new C("A",-1))));
  assertEquals(l3Expected, l3Actual);
  }
static class FailErr extends Error{}
public static void fail(String sl1,String sl2,String err){
  Resources.clearRes();
  String[]msg={null};
  Init init1=new Init("{A={"+sl1+"}");
  Core.L l1=init1.p._ofCore(P.of(0,List.of(new C("A",-1))));
  Init init2=new Init("{A={"+sl2+"}");
  Core.L l2=init2.p._ofCore(P.of(0,List.of(new C("A",-1))));
  Function<L42£LazyMsg,L42Any>wrap=lm->{msg[0]=lm.getMsg();throw new FailErr();};
  try{new Sum().compose(l1, l2,wrap,wrap);Assertions.fail();}
  catch(FailErr fe){}
  Err.strCmp(msg[0],err);
  }
}