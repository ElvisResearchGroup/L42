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
   Invalid @{foo}imm method imm Void m()=(..)
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
   //Need to not have any coherentDep or watched!
   //TODO: test that grown interface can induce new implements
   //test that refined relation is not broken by sum
   

   

   //topFail(EndError."{A={} B=(void)}","{A={#typed{}}B={#typed{}}#norm{}}")
   //),new AtomicTest(()->
   //fail(EndError."{A={} B=(void)}","{A={#typed{}}B={#typed{}}#norm{}}")
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