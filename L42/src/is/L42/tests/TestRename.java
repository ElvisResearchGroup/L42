package is.L42.tests;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
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
import is.L42.generated.S;
import is.L42.meta.Arrow;
import is.L42.meta.Rename;
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

import static org.junit.jupiter.api.Assertions.*;

public class TestRename
extends AtomicTest.Tester{
public static List<Arrow> map(String s){
  List<Arrow>map=new ArrayList<>();
  Stream.of(s.strip().split(Pattern.quote("|"))).forEach(arr->{
    int fullNo=arr.indexOf("->");
    int fullYes=arr.indexOf("=>");
    assert fullNo!=-1 || fullYes!=-1;
    assert !(fullNo!=-1 && fullYes!=-1);
    int pos=Math.max(fullNo, fullYes);
    String a=arr.substring(0,pos);
    String b=arr.substring(pos+2,arr.length());
    var key=fromS(a);
    var arrow=new Arrow(key.cs,key._s,fullYes!=-1,null,null,null);
    if(b.equals("<empty>")){map.add(arrow);return;}
    if(b.startsWith("#")){
      arrow._path=P.parse(b.substring(1));
      map.add(arrow);return;
      }
    var rest=fromS(b);
    arrow._cs=rest.cs;
    arrow._sOut=rest._s;
    map.add(arrow);
    });  
  System.out.println(map);
  return map;
  }
static Arrow fromS(String s){
  int i=s.lastIndexOf(".");
  List<C> cs=L();
  if(i!=-1){cs=P.parse("This0."+s.substring(0,i).trim()).toNCs().cs();}
  S _s=null;
  s=s.substring(i+1).trim();//also works for -1; pure coincidence
  if(!s.isEmpty()){_s=S.parse(s);}
  return new Arrow(cs,_s);
  }
static class FailErr extends Error{}
public static void fail(String sl1,String s2,String err){
  Resources.clearRes();
  String[]msg={null};
  Init init1=new Init("{Outer={"+sl1+"#norm{}}");
  Core.L l1=init1.p._ofCore(P.of(0,List.of(new C("Outer",-1))));
  Function<L42£LazyMsg,L42Any>wrap=lm->{msg[0]=lm.getMsg();throw new FailErr();};
  try{new Rename().apply(init1.p,new C("Outer",-1),l1,map(s2),wrap,wrap,wrap,wrap);Assertions.fail();}
  catch(FailErr fe){}
  Err.strCmp(msg[0],err);
  }
public static void pass(String sl1,String s2,String sl3){
  Resources.clearRes();
  Init init1=new Init("{Outer={"+sl1+"#norm{}}");
  Core.L l1=init1.p._ofCore(P.of(0,List.of(new C("Outer",-1))));
  Core.L l3Actual=new Rename().apply(init1.p,new C("Outer",-1),l1,map(s2),null,null,null,null);
  Init init3=new Init("{Outer={"+sl3+"#norm{}}");
  Core.L l3Expected=init3.p._ofCore(P.of(0,List.of(new C("Outer",-1))));
  assertEquals(l3Expected, l3Actual);
  }
public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("A={#norm{}}#norm{}}","A.=>B.","B={#norm{}}#norm{}}")
   ),new AtomicTest(()->
   pass("A={ method Void foo(Void x)=x #norm{}}#norm{}}",
     "A.foo(x)=>A.bar(y)",
     "A={ method Void bar(Void y)=y #norm{}}#norm{}}")
   ),new AtomicTest(()->
   pass("A={ method Void foo(Void x)=x #norm{}}#norm{}}",
     "A.foo(x)-><empty>",
     "A={ method Void foo(Void x) #norm{}}#norm{}}")
   ),new AtomicTest(()->fail("""
     A={ method Void foo(Void x) #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)-><empty>
   """,/*expected after this line*/"""
   nested class { A={..} }
   method A.foo(x)
   is already abstract
   Full mapping:A.foo(x)-><empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={ method Void foo(Void x)=x #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)->A.bar(y)
   """,/*expected after this line*/"""
     A={ method Void foo(Void x) method Void bar(Void y)=y #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={ method Void foo(Void x)=x method Void bar(Void y) #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)->A.bar(y)
   """,/*expected after this line*/"""
     A={ method Void foo(Void x) method Void bar(Void y)=y #norm{}}
   #norm{}}"""/*next test after this line*/)
      
   ),new AtomicTest(()->fail("""
     A={ method Void foo(Void x)=x method Void bar(Void y)=y #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)->A.bar(y)
   """,/*expected after this line*/"""
   method imm method imm Void bar(imm Void y)=(..)
   Conflicting implementation: the method is implemented on both side of the sum
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={ method Void foo(Void x)=x method Void user(Void z)=this.foo(x=z) #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=>A.bar(y)
   """,/*expected after this line*/"""
     A={ method Void user(Void z)=this.bar(y=z) method Void bar(Void y)=y #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface method Void foo(Void x) #norm{}}
     A={[This1.I] method Void foo(Void x)=x method Void user(Void z)=this.foo(x=z)
         #norm{typeDep=This1.I, refined=foo(x) usedMethods=This1.I.foo(x)}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=>A.bar(y)
   """,/*expected after this line*/"""
   nested class { I={..} A={..} }
   refined method method A.foo(x)
   can not be directly renamed
   Full mapping:A.foo(x)=>A.bar(y)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={ method Void foo(Void x)=x method Void user(Void z)=this.foo(x=z) #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=><empty>
   """,/*expected after this line*/"""
     A={
       method Void foo::1(Void x)=x
       method Void user(Void z)=this.foo::1(x=z) #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={ method Void foo(Void x) method Void user(Void z)=this.foo(x=z) #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=><empty>
   """,/*expected after this line*/"""
   nested class { A={..} }
   method A.foo(x)
   is abstract, thus it can not be hidden
   Full mapping:A.foo(x)=><empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface method Void foo(Void x) #norm{}}
     A={[This1.I] method Void foo(Void x)=x method Void user(Void z)=this.foo(x=z)
         #norm{typeDep=This1.I, refined=foo(x), usedMethods=This1.I.foo(x)}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=><empty>
   """,/*expected after this line*/"""
   nested class { I={..} A={..} }
   refined method method A.foo(x)
   can not be directly renamed
   Full mapping:A.foo(x)=><empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={interface method Void bar() #norm{}}
     D={interface [This1.A] method Void bar() #norm{typeDep=This1.A refined=bar()}}
     B={
       method Void user(This1.D d)=d.bar()
       #norm{typeDep=This1.D usedMethods=This1.D.bar()}
       }
   #norm{}}""",/*rename map after this line*/"""
     A.bar()=><empty>
   """,/*expected after this line*/"""
   A={interface imm method imm Void bar::1()#norm{close}}
   D={interface[This1.A]imm method imm Void bar::1()
     #norm{typeDep=This1.A refined=bar::1()close}}
   B={imm method imm Void user(imm This1.D d)=d.bar::1()
     #norm{typeDep=This1.D watched=This1.D}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
   A={interface imm method imm Void bar::1()#norm{close}}
   D={interface[This1.A]imm method imm Void bar::1()
     #norm{typeDep=This1.A refined=bar::1()close}}
   B={imm method imm Void user(imm This1.D d)=d.bar::1()
     #norm{typeDep=This1.D watched=This1.D}}
   #norm{}}""",/*rename map after this line*/"""
     D.-><empty>
   """,/*expected after this line*/"""
   nested class { A={..} D={..} B={..} }
   nested class D
   The implementation can not be removed since the class is watched by nested class B
   Full mapping:D-><empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
   A={interface imm method imm Void bar::1()#norm{close}}
   D={[This1.A]imm method imm Void bar::1()=void
     #norm{typeDep=This1.A refined=bar::1()}}
   #norm{}}""",/*rename map after this line*/"""
     D.-><empty>
   """,/*expected after this line*/"""
   nested class { A={..} D={..} }
   nested class D
   The implementation can not be removed; a close interface is implemented
   Full mapping:D-><empty>
   [file:[###]"""/*next test after this line*/)
  ),new AtomicTest(()->pass("""
     A={interface method Void foo(Void x) method Void bar() #norm{}}
     D={interface [This1.A] method Void foo(Void x) method Void bar() #norm{typeDep=This1.A refined=foo(x) bar()}}
     DC={[This1.D,This1.A] method Void foo(Void x)=x method Void bar() #norm{typeDep=This1.D,This1.A refined=foo(x) bar()}}
     B={#norm{typeDep=This1.A This1.C usedMethods=This1.A.foo(x) This1.A.bar() This1.C.c()}}
     C={method Void c() #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=><empty>
   """,/*expected after this line*/"""
     A={interface
       method Void foo::1(Void x) 
       method Void bar()
       #norm{close}}
     D={interface [This1.A] method Void foo::1(Void x) method Void bar() #norm{typeDep=This1.A refined=foo::1(x) bar() close}}
     DC={[This1.D,This1.A] method Void foo::1(Void x)=x method Void bar() #norm{typeDep=This1.D,This1.A refined=foo::1(x) bar()}}
     B={#norm{typeDep=This1.A This1.C 
       watched=This1.A
       usedMethods=This1.C.c()
       }}
     C={method Void c() #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={interface method Void foo(Void x) method Void bar() #norm{}}
     D={interface [This1.A] method Void foo(Void x) method Void bar() #norm{typeDep=This1.A refined=foo(x) bar()}}
     DC={[This1.D,This1.A] method Void foo(Void x) method Void bar() #norm{typeDep=This1.D,This1.A refined=foo(x) bar()}}
     B={#norm{typeDep=This1.A This1.C usedMethods=This1.A.foo(x) This1.A.bar() This1.C.c()}}
     C={method Void c() #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=><empty>
   """,/*expected after this line*/"""
   nested class { A={..} D={..} DC={..} B={..} C={..} }
   method DC.foo(x)
   is abstract, thus it can not be hidden
   Full mapping:A.foo(x)=><empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={interface method Void foo(Void x) #norm{}}
     B={#norm{typeDep=This1.A hiddenSupertypes=This1.A}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=><empty>
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   The method A.foo(x)
   can not be made private since is implemented by private parts of nested class B
   Full mapping:A.foo(x)=><empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface method Void foo(Void x) #norm{}}
     A={interface [This1.I] method Void foo(Void x)
         #norm{typeDep=This1.I, refined=foo(x)}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=><empty>
   """,/*expected after this line*/"""
   nested class { I={..} A={..} }
   refined method method A.foo(x)
   can not be directly renamed
   Full mapping:A.foo(x)=><empty>
   [file:[###]"""/*next test after this line*/)
   //now again but rename instead of hide
   ),new AtomicTest(()->pass("""
     A={interface method Void foo(Void x) method Void bar() #norm{}}
     D={interface [This1.A] method Void foo(Void x) method Void bar() #norm{typeDep=This1.A refined=foo(x) bar()}}
     DC={[This1.D,This1.A] method Void foo(Void x)=x method Void bar() #norm{typeDep=This1.D,This1.A refined=foo(x) bar()}}
     B={#norm{typeDep=This1.A This1.C usedMethods=This1.A.foo(x) This1.A.bar() This1.C.c()}}
     C={method Void c() #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=>A.beer(y)
   """,/*expected after this line*/"""
     A={interface
       method Void bar()
       method Void beer(Void y)
       #norm{}}
     D={interface [This1.A] method Void bar() method Void beer(Void y) #norm{typeDep=This1.A refined=beer(y) bar()}}
     DC={[This1.D,This1.A] method Void bar() method Void beer(Void y)=y #norm{typeDep=This1.D,This1.A refined=beer(y) bar()}}
     B={#norm{typeDep=This1.A This1.C 
       usedMethods=This1.A.beer(y) This1.A.bar() This1.C.c()
       }}
     C={method Void c() #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={interface method Void foo(Void x) method Void bar() #norm{}}
     D={interface [This1.A] method Void foo(Void x) method Void bar() #norm{typeDep=This1.A refined=foo(x) bar()}}
     DC={[This1.D,This1.A] method Void foo(Void x) method Void bar() #norm{typeDep=This1.D,This1.A refined=foo(x) bar()}}
     B={#norm{typeDep=This1.A This1.C usedMethods=This1.A.foo(x) This1.A.bar() This1.C.c()}}
     C={method Void c() #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=>A.beer(y)
   """,/*expected after this line*/"""
     A={interface
       method Void bar()
       method Void beer(Void y)
       #norm{}}
     D={interface [This1.A] method Void bar() method Void beer(Void y) #norm{typeDep=This1.A refined=beer(y) bar()}}
     DC={[This1.D,This1.A] method Void bar() method Void beer(Void y) #norm{typeDep=This1.D,This1.A refined=beer(y) bar()}}
     B={#norm{typeDep=This1.A This1.C 
       usedMethods=This1.A.beer(y) This1.A.bar() This1.C.c()
       }}
     C={method Void c() #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={interface method Void foo(Void x) #norm{}}
     B={#norm{typeDep=This1.A hiddenSupertypes=This1.A}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=>A.beer(y)
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   The method A.foo(x)
   can not be renamed since is implemented by private parts of nested class B
   Full mapping:A.foo(x)=>A.beer(y)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface method Void foo(Void x) #norm{}}
     A={interface [This1.I] method Void foo(Void x)
         #norm{typeDep=This1.I, refined=foo(x)}}
   #norm{}}""",/*rename map after this line*/"""
     A.foo(x)=>A.beer(y)
   """,/*expected after this line*/"""
   nested class { I={..} A={..} }
   refined method method A.foo(x)
   can not be directly renamed
   Full mapping:A.foo(x)=>A.beer(y)
   [file:[###]"""/*next test after this line*/)
//---
   ),new AtomicTest(()->fail("""
     A={#norm{}}
   #norm{}}""",/*rename map after this line*/"""
     C.=>B.
   """,/*expected after this line*/"""
   nested class { A={..} }
   nested class C
   does not exists
   Full mapping:C=>B
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={#norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.=>B.s(x)
   """,/*expected after this line*/"""
   nested class { A={..} }
   mapping: A=>B.s(x)
   Can not rename a nested class into a method
   Full mapping:A=>B.s(x)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x  #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)=>B.
   """,/*expected after this line*/"""
   nested class { A={..} }
   mapping: A.s(x)=>B
   Can not rename a method into a nested class
   Full mapping:A.s(x)=>B
   [file:[###]"""/*next test after this line*/)   
   ),new AtomicTest(()->fail("""
     A={#norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)=>A.s(x)
   """,/*expected after this line*/"""
   nested class { A={..} }
   method A.s(x)
   does not exists
   Full mapping:A.s(x)=>A.s(x)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)=>A.s(x)
   """,/*expected after this line*/"""
   nested class { A={..} }
   mapping: A.s(x)=>A.s(x)
   Can not rename a method on itself
   Full mapping:A.s(x)=>A.s(x)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x method Void s(Void z)=z #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)=>A.s(y)|A.s(z)=>A.s(y)
   """,/*expected after this line*/"""
   nested class { A={..} }
   Two different methods are renamed into method A.s(y)
   Full mapping:A.s(x)=>A.s(y);A.s(z)=>A.s(y)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x method Void s(Void z)=z #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)->A.s(y)|A.s(z)->A.s(y)
   """,/*expected after this line*/"""
   nested class { A={..} }
   Two different methods are renamed into method A.s(y)
   Full mapping:A.s(x)->A.s(y);A.s(z)->A.s(y)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)=>A.s(y)|A.s(x)=>A.s(k)
   """,/*expected after this line*/"""
   nested class { A={..} }
   Rename map contains two entries for A.s(x)
   Full mapping:A.s(x)=>A.s(y);A.s(x)=>A.s(k)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)->A.s(y)|A.s(x)=>A.s(k)
   """,/*expected after this line*/"""
   nested class { A={..} }
   Rename map contains two entries for A.s(x)
   Full mapping:A.s(x)->A.s(y);A.s(x)=>A.s(k)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)=>A.s(y)|A.s(x)->A.s(k)
   """,/*expected after this line*/"""
   nested class { A={..} }
   Rename map contains two entries for A.s(x)
   Full mapping:A.s(x)=>A.s(y);A.s(x)->A.s(k)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={method Void s(Void x)=x method Void s(Void y) 
      method Void userY()=this.s(y=void) 
      method Void userX()=this.s(x=void)
      #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)=>A.s(y)|A.s(y)=>A.s(x)
   """,/*expected after this line*/"""
     A={
       method Void userY()=this.s(x=void)
       method Void userX()=this.s(y=void)
       method Void s(Void y)=y
       method Void s(Void x) 
       #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={method Void a() #norm{}}
     B={method Void b() #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.=>B.|B.=>A.
   """,/*expected after this line*/"""
     B={imm method imm Void a()#norm{}}
     A={imm method imm Void b()#norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={method Void s(Void x)=x method Void s(Void y)=y method Void s(Void z)=z 
      method Void userY()=this.s(y=void) 
      method Void userX()=this.s(x=void)
      method Void userZ()=this.s(z=void)
      #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)=>A.s(y)|A.s(y)=>A.s(z)|A.s(z)=>A.s(x)
   """,/*expected after this line*/"""
     A={
       method Void userY()=this.s(z=void)
       method Void userX()=this.s(y=void)
       method Void userZ()=this.s(x=void)
       method Void s(Void y)=y
       method Void s(Void z)=z
       method Void s(Void x)=x
       #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={
      method Void s(Void x)=(@{1}Void v=void x)
      method Void s(Void y)=(@{2}Void v=void y)
      method Void s(Void z)=(@{3}Void v=void z)
      method Void userY()=this.s(y=void) 
      method Void userX()=this.s(x=void)
      method Void userZ()=this.s(z=void)
      #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)->A.s(y)|A.s(y)->A.s(z)|A.s(z)->A.s(x)
   """,/*expected after this line*/"""
     A={
       method Void s(Void x)=(@{3}Void v=void x)
       method Void s(Void y)=(@{1}Void v=void y)
       method Void s(Void z)=(@{2}Void v=void z)
       method Void userY()=this.s(y=void)
       method Void userX()=this.s(x=void)
       method Void userZ()=this.s(z=void)
       #norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={method Void a() #norm{}}
     B={method Void b() #norm{}}
     C={method Void c() #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.=>B.|B.=>C.|C.=>A. 
   """,/*expected after this line*/"""
     B={imm method imm Void a()#norm{}}
     C={imm method imm Void b()#norm{}}
     A={imm method imm Void c()#norm{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={#norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.=>A.
   """,/*expected after this line*/"""
   nested class { A={..} }
   mapping: A=>A
   Can not rename a nested class on itself
   Full mapping:A=>A
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={#norm{}} C={#norm{typeDep=This1.A watched=This1.A}}
   #norm{}}""",/*rename map after this line*/"""
     A.->B.
   """,/*expected after this line*/"""
   nested class { A={..} C={..} }
   nested class A
   The implementation can not be removed since the class is watched by nested class C
   Full mapping:A->B
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)=>B.s(y)
   """,/*expected after this line*/"""
   nested class { A={..} }
   methods can only be renamed from inside the same nested class, but the following mapping is present: A.s(x)=>B.s(y)
   Full mapping:A.s(x)=>B.s(y)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.s(x)=>A.s(y,z)
   """,/*expected after this line*/"""
   nested class { A={..} }
   methods renames need to keep the same number of parameters, but the following mapping is present: A.s(x)=>A.s(y,z)
   Full mapping:A.s(x)=>A.s(y,z)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={#norm{}} B={#norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.=>C.|B.=>C.
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   Two different nested class are renamed into nested class C
   Full mapping:A=>C;B=>C
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x #norm{}}
   #norm{}}""",/*rename map after this line*/"""
     A.=>C.|A.s(x)=>A.s(y)
   """,/*expected after this line*/"""
   nested class { A={..} }
   nested class A
   is already involved in the rename; thus method A.s(x)
   can not be renamed
   Full mapping:A=>C;A.s(x)=>A.s(y)
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void s(Void x)=x #norm{}} B={#norm{}}
   #norm{}}""",/*rename map after this line*/"""
     B.=>A.|A.s(x)=>A.s(y)
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   nested class A
   is already involved in the rename; thus method A.s(x)
   can not be renamed
   Full mapping:B=>A;A.s(x)=>A.s(y)
   [file:[###]"""/*next test after this line*/)
//redirect from now on
   ),new AtomicTest(()->fail("""
     A={method This1.B s(This1.B x)=x #norm{typeDep=This1.B}} B={#norm{}}
   #norm{}} K={#typed{}}""",/*rename map after this line*/"""
     A.=>#This1.K
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   nested class A
   Redirected classes need to be fully abstract and imm method imm This1.B s(imm This1.B x)=(..)
   is implemented
   Full mapping:A=>This1.K
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={#norm{}} B={#norm{typeDep=This1.A watched=This1.A}}
   #norm{}} K={#typed{}}""",/*rename map after this line*/"""
     A.=>#This1.K
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   nested class A
   The implementation can not be removed since the class is watched by nested class B
   Full mapping:A=>This1.K
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method This1.B s(This1.B x) #norm{typeDep=This1.B}} B={#norm{}}
   #norm{}} K={#typed{}}""",/*rename map after this line*/"""
     A.=>#This1.K
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   Also nested class B
   need to be redirected to an outer path
   Full mapping:A=>This1.K
   [file:[###]"""/*next test after this line*/)
   //nested classes
    ),new AtomicTest(()->pass("""
     A={
       method This1.B s(This1.B x)=x
       method This1.B s::1(This1.B x)=this.s(x=x)
       C={#norm{}}
       D::2={#norm{}}
       #norm{typeDep=This1.B This.C}}
     B={#norm{}}
   #norm{}} K={#typed{}}""",/*rename map after this line*/"""
     A.-><empty>
   """,/*expected after this line*/"""
     A={
       method This1.B s(This1.B x)
       C={#norm{}}
       #norm{typeDep=This1.B}}
     B={#norm{}}
     #norm{}}"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     A={
       method This1.B s(This1.B x)=x
       method This1.B s::1(This1.B x)=this.s(x=x)
       C={#norm{typeDep=This1 watched=This1}}
       D::2={#norm{}}
       #norm{typeDep=This1.B}}
     B={#norm{}}
   #norm{}} K={#typed{}}""",/*rename map after this line*/"""
     A.-><empty>
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   nested class A
   The implementation can not be removed since the class is watched by nested class A.C
   Full mapping:A-><empty>
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->pass("""
     AA={
       method This1.B s(This1.B x, This.C c)=x
       method This1.B s::1(This1.B x This.C c)=this.s(x=x,c=c)
       C={#norm{}}
       D::2={#norm{}}
       #norm{typeDep=This1.B This.C}}
     B={#norm{}}
   #norm{}} K={#typed{}}""",/*rename map after this line*/"""
     AA.->C.
   """,/*expected after this line*/"""
   AA={
     method This1.B s(This1.B x,This0.C c)
     C={#norm{}}
     #norm{typeDep=This1.B, This0.C}}
   B={#norm{}}
   C={
     method This1.B s(This1.B x,This1.AA.C c)=x
     method This1.B s::1(This1.B x,This1.AA.C c)=this.s(x=x, c=c)
     D::2={#norm{}}
     #norm{typeDep=This1.B, This1.AA.C}}
     #norm{}}"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     A={
       method This1.B s(This1.B x)=x
       method This1.B s::1(This1.B x)=this.s(x=x)
       C={#norm{typeDep=This1 watched=This1}}
       D::2={#norm{}}
       #norm{typeDep=This1.B}}
     B={#norm{typeDep=This1.K}}
     K={#norm{typeDep=This1.A}}
   #norm{}} K={#typed{}}""",/*rename map after this line*/"""
     A.->C.
   """,/*expected after this line*/"""
   nested class { A={..} B={..} K={..} }
   nested class A
   The implementation can not be removed since the class is watched by nested class A.C
   Full mapping:A->C
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     A={
       method Void s(This1.B x)=x.a(a=this)
       #norm{typeDep=This1.B usedMethods=This1.B.a(a)}}
     B={method Void a(This1.A a)=void #norm{typeDep=This1.K,This1.A}}
   #norm{}}""",/*rename map after this line*/"""
     A.->C.
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   nested class A
   Code can not be extracted since is circularly depended from nested class B
   Full mapping:A->C
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     A={
       method This.D::2 s()=this.s()
       D::2={#norm{}}
       #norm{typeDep=This,This.D::2}}
     #norm{}}""",/*rename map after this line*/"""
     A.->C.
   """,/*expected after this line*/"""
   nested class { A={..} }
   nested class This0
   Code can not be extracted since it exposes uniquely numbered path nested class D::2
   Full mapping:A->C
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->pass("""
     B={
       method This1.C s(This1.C c)=c
       method This1.C s::1(This1.C c)=this.s(c=c)
       #norm{typeDep=This1.C}
       }
     C={#norm{}}
     #norm{}
     }""",/*rename map after this line*/"""
     B.->
   """,/*expected after this line*/"""
     method This.C s(This.C c)=c
     method This.C s::1(This.C c)=this.s(c=c)
     B={
       method This1.C s(This1.C c)
       #norm{typeDep=This1.C}
       }
     C={#norm{}}
     #norm{typeDep=This.C}
     }"""/*next test after this line*/)
    ),new AtomicTest(()->pass("""
     B={
       method This1.C s(This.KK c)=this.s(c=c)
       method This1.C s::1(This.KK c)=this.s(c=c)
       KK={#norm{}}
       #norm{typeDep=This1.C, This.KK}
       }
     C={#norm{}}
     #norm{}
     }""",/*rename map after this line*/"""
     B.->
   """,/*expected after this line*/"""
   method This.C s(This.B.KK c)=this.s(c=c)
   method This.C s::1(This.B.KK c)=this.s(c=c)
   B={
     method This1.C s(This.KK c)
     KK={#norm{}}
     #norm{typeDep=This1.C, This0.KK}}
   C={#norm{}}
   #norm{typeDep=This0.C, This0.B.KK}
   }"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     B={
       method This1.C s(This1.C c)=c
       #norm{typeDep=This1.C}
       }
     C={#norm{ typeDep=This1.B}}
     #norm{}
     }""",/*rename map after this line*/"""
     B.->
   """,/*expected after this line*/"""
   nested class { B={..} C={..} }
   nested class B
   Code can not be extracted since is circularly depended from nested class C
   Full mapping:B->This0
   [file:[###]"""/*next test after this line*/)

    ),new AtomicTest(()->pass("""
     method This.C s(This.C c)=c
     method This.C s::1(This.C c)=this.s(c=c)
     C={#norm{}}
     #norm{typeDep=This.C}
     }""",/*rename map after this line*/"""
     ->C.
   """,/*expected after this line*/"""
    method This.C s(This.C c)
    C={
      method This1.C s(This1.C c)=c
      method This1.C s::1(This1.C c)=this.s(c=c)
      #norm{typeDep=This1.C}
      }
    #norm{typeDep=This.C}
    }"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     method This.D::2 s(This.D::2 x, This.C c)=x
     method This.D::2 s::1(This.D::2 x This.C c)=this.s(x=x,c=c)
     C={#norm{}}//Should fail: exposes private names, can not be made abstract
     D::2={#norm{}}
     #norm{typeDep=This,This.C, This.D::2}
     }""",/*rename map after this line*/"""
     ->C.
   """,/*expected after this line*/"""
   nested class { s(x,c)=(..) C={..} }
   nested class This0
   Code can not be extracted since it exposes uniquely numbered path nested class D::2
   Full mapping:This0->C
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     method This.C s(This.C x)=x
     C={#norm{typeDep=This1 watched=This1}}
     #norm{typeDep=This.C}}""",/*rename map after this line*/"""
     ->C.
   """,/*expected after this line*/"""
   nested class { s(x)=(..) C={..} }
   nested class This0
   The implementation can not be removed since the class is watched by nested class C
   Full mapping:This0->C
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     method Void v()
     B={#norm{typeDep=This1}}
     #norm{typeDep=This.B}}""",/*rename map after this line*/"""
     ->C.
   """,/*expected after this line*/"""
   nested class { v() B={..} }
   nested class This0
   Code can not be extracted since is circularly depended from nested class B
   Full mapping:This0->C
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->pass("""
     method Void v()=void
     method Void v::2()=void
     B={method Void b()=void #norm{}}
     C::2={#norm{}}
     #norm{}}""",/*rename map after this line*/"""
     -><empty>
   """,/*expected after this line*/"""
     method Void v()
     B={method Void b()=void #norm{}}
     #norm{}}"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     method Void v()=void
     method Void v::2()=void
     B={method Void b()=void #norm{typeDep=This1 watched=This1}}
     C::2={#norm{}}
     #norm{}}""",/*rename map after this line*/"""
     -><empty>
   """,/*expected after this line*/"""
   nested class { v()=(..) B={..} }
   nested class This0
   The implementation can not be removed since the class is watched by nested class B
   Full mapping:This0-><empty>
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->pass("""
     @This.A A={
       method Void v()=void
       B={#norm{}}
       D::2={#norm{}}
       #norm{}}
     C={method Void b()=void
       #norm{}}
     #norm{typeDep=This.A}}""",/*rename map after this line*/"""
     A.=>C.D.E.
   """,/*expected after this line*/"""
     A={
       B={#norm{}}
       #typed{}}
     C={
       method Void b()=void 
       D={
         @This.E E={
           method Void v()=void
           D::2={#norm{}}
           #norm{}}
         #typed{typeDep=This.E}}
       #norm{}}
     #norm{typeDep=This.C.D.E}}
     """/*next test after this line*/)
    ),new AtomicTest(()->pass("""
     @This.A A={
       method Void v()=void
       B={#norm{}}
       D::2={#norm{}}
       #norm{}}
     C={method Void b()=void
       #norm{}}
     #norm{typeDep=This.A}}""",/*rename map after this line*/"""
     A.=>C.D.E. | A.B.=>C.D.E.B.
   """,/*expected after this line*/"""
   C={method Void b()=void
     D={
       @This.E E={
         method Void v()=void
         D::2={#norm{}}
         B={#norm{}}
       #norm{}}
     #typed{typeDep=This.E}}
   #norm{}}
 #norm{typeDep=This.C.D.E}}"""/*next test after this line*/)    
    ),new AtomicTest(()->pass("""
     A={
       method Void v::0()=void
       B={#norm{}}
       D::2={#norm{}}
       #norm{}}
     C={method Void b()=void
       #norm{}}
     #norm{typeDep=This.A}}""",/*rename map after this line*/"""
     A.=><empty>
   """,/*expected after this line*/"""
   A={
     B={#norm{}}
     #typed{}}
   A::1={
     method Void v::0()=void
     D::2={#norm{}}
     #norm{}}
   C={method Void b()=void #norm{}}
   #norm{typeDep=This,This.A::1}}
   """/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     A={
       method Void v1()=void
       method Void v2()=void
       B={#norm{}}
       D::2={#norm{}}
       #norm{}}
     C={method Void b()=void
       #norm{}}
     #norm{typeDep=This.A}}""",/*rename map after this line*/"""
     A.=><empty>
   """,/*expected after this line*/"""
   nested class { A={..} C={..} }
   nested class A
   can not be hidden since some methods are still public:
   method A.v1()
   method A.v2()
   Full mapping:A=><empty>
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->pass("""
     method This.B aa(This.A a)=a.a()
     A={
       method This1.B a()
       #norm{typeDep=This1.B}}
     B={method This1.A b()
       #norm{typeDep=This1.A}}
     C={ method This1.A bb(This1.B b)=b.b() 
       #norm{typeDep=This1.A,This1.B usedMethods=This1.B.b()}}
     #norm{typeDep=This.A,This.B usedMethods=This.A.a()}}
   EA={
     method This1.EB a()
     #norm{typeDep=This1.EB}}
   EB={method This1.EA b()
     #norm{typeDep=This1.EA}}
   """,/*rename map after this line*/"""
   A.=>#This.EA | B.=>#This.EB
   """,/*expected after this line*/"""
   method This1.EB aa(This1.EA a)=a.a()
   C={
     method This2.EA bb(This2.EB b)=b.b()
     #norm{typeDep=This2.EA,This2.EB usedMethods=This2.EB.b()}}
   #norm{typeDep=This1.EA,This1.EB usedMethods=This1.EA.a()}}
   EA={
     method This1.EB a()
     #norm{typeDep=This1.EB}}
   EB={method This1.EA b()
     #norm{typeDep=This1.EA}}
   """/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     method This.B aa(This.A a)=(
       This.A a2=this.aa(a=( 
         This.A a3=a
         a3
         ))
       catch error This.A ea (ea.a())
       a2.a())     
     A={
       method This1.B a()
       #norm{typeDep=This1.B}}
     B={method This1.A b()
       #norm{typeDep=This1.A}}
     C={ method This1.A bb(This1.B b)=b.b() 
       #norm{typeDep=This1.A,This1.B usedMethods=This1.B.b()}}
     #norm{typeDep=This.A,This.B usedMethods=This.A.a()}}
   EA={
     method This1.EB a()
     #norm{typeDep=This1.EB}}
   EB={method This1.EA b()
     #norm{typeDep=This1.EA}}
   """,/*rename map after this line*/"""
   A.=>#This.EA | B.=>#This.EB
   """,/*expected after this line*/"""
   method This1.EB aa(This1.EA a)=(
     This1.EA a2=this.aa(a=(This1.EA a3=a a3))
     catch error This1.EA ea (ea.a())
     a2.a())
   C={
     method This2.EA bb(This2.EB b)=b.b()
     #norm{typeDep=This2.EA,This2.EB usedMethods=This2.EB.b()}}
   #norm{typeDep=This1.EA,This1.EB usedMethods=This1.EA.a()}}
   EA={
     method This1.EB a()
     #norm{typeDep=This1.EB}}
   EB={method This1.EA b()
     #norm{typeDep=This1.EA}}
   """/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     method This.B aa(This.A a)=a.a()     
     A={
       method This1.B a()
       #norm{typeDep=This1.B}}
     B={method This1.A b()
       #norm{typeDep=This1.A}}
     C={ method This1.A bb(This1.B b)=b.b() 
       #norm{typeDep=This1.A,This1.B usedMethods=This1.B.b()}}
     #norm{typeDep=This.A,This.B usedMethods=This.A.a()}}
   EA={
     method This1.EB a()
     #norm{typeDep=This1.EB}}
   EB={method Void b()
     #norm{typeDep=This1.EA}}
   """,/*rename map after this line*/"""
   A.=>#This.EA | B.=>#This.EB
   """,/*expected after this line*/"""
   nested class { aa(a)=(..) A={..} B={..} C={..} }
   nested class B
   can not be redirected, the target This0.EB
   does not expose a compatible method method B.b()
   Invalid method inheritance for b():
   the return type imm Void is not a subtype of the inherited type imm This2.EA
   Full mapping:A=>This0.EA;B=>This0.EB
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->pass("""
    method Void a(This.A a)=void
    A={method Void foo()=void #norm{}}
    B={method Any foo()=void #norm{}}
    C={method Void a(This1.A a)=a.foo() #norm{typeDep=This1.A usedMethods=This1.A.foo()}}
    #norm{typeDep=This.A}}
    """,/*rename map after this line*/"""
   A.=>
   """,/*expected after this line*/"""
   method Void a(This a)=void
   method Void foo()=void
   B={method Any foo()=void #norm{}}
   C={method Void a(imm This1 a)=a.foo()#norm{typeDep=This1 usedMethods=This1.foo()}}
   #norm{typeDep=This}}"""/*next test after this line*/)
    ),new AtomicTest(()->pass("""
    method Void a(This0.A a)=void
    A={method Void foo::1()=void #typed{}}
    B={method Any foo()=void #typed{}}
    C={method Void a(imm This1.A a)=a.foo::1()
      #typed{typeDep=This1.A watched=This1.A}}
    #typed{typeDep=This0.A}}
    """,/*rename map after this line*/"""
   A.=><empty>
   """,/*expected after this line*/"""
   method Void a(imm This.A::2 a)=void
   A::2={method Void foo::1()=void #typed{}}
   B={method Any foo()=void #typed{}}
   C={method Void a(This1.A::2 a)=a.foo::1()
     #typed{typeDep=This1,This1.A::2 watched=This1.A::2, This1}}
   #typed{typeDep=This,This.A::2}}
   """/*next test after this line*/)
       ),new AtomicTest(()->fail("""
    method Void a(This0.A a)=void
    A={method Void foo::1()=void #typed{}}
    B={method Any foo()=void #typed{}}
    C={method Void a(imm This1.A a)=a.foo::1()
      #typed{typeDep=This1.A watched=This1.A}}
    #typed{typeDep=This0.A}}
    """,/*rename map after this line*/"""
   A.-><empty>
   """,/*expected after this line*/"""
   nested class { a(a)=(..) A={..} B={..} C={..} }
   nested class A
   The implementation can not be removed since the class is watched by nested class C
   Full mapping:A-><empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
    A={interface method Void foo() #typed{}}
    B={[This1.A] method Void foo()=void #typed{typeDep=This1.A refined=foo()}}
    #typed{}}
    """,/*rename map after this line*/"""
   A.foo()=>A.bar() | B.=>C.
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   nested class B
   is already involved in the rename; thus method A.foo()
   can not be renamed: is an interface method refined by such nested class
   Full mapping:A.foo()=>A.bar();B=>C
   [file:[###]"""/*next test after this line*/)
   
  ),new AtomicTest(()->fail("""
    A={method Void foo() #typed{}}
    #typed{}}
    """,/*rename map after this line*/"""
   =><empty>
   """,/*expected after this line*/"""
   nested class { A={..} }
   'This' can not be hidden
   Full mapping:This0=><empty>
   [file:[###]"""/*next test after this line*/)
  ),new AtomicTest(()->fail("""
    A={method Void foo() #typed{}}
    #typed{}}
    """,/*rename map after this line*/"""
   =>#Any
   """,/*expected after this line*/"""
   nested class { A={..} }
   'This' can not be redirected away
   Full mapping:This0=>Any
   [file:[###]"""/*next test after this line*/)
   ));}
}