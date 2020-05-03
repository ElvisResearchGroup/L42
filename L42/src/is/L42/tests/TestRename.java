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
import is.L42.top.UniqueNsRefresher;
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
    boolean starYes=arr.contains(">*");
    assert fullNo!=-1 || fullYes!=-1;
    assert !(fullNo!=-1 && fullYes!=-1);
    int pos=Math.max(fullNo, fullYes);
    String a=arr.substring(0,pos);
    int size=starYes?3:2;
    String b=arr.substring(pos+size,arr.length());
    b=b.trim();
    var key=fromS(a);
    var arrow=new Arrow(key.cs,key._s,fullYes!=-1,starYes,null,null,null);
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
  if(i!=-1){cs=P.parse("This."+s.substring(0,i).trim()).toNCs().cs();}
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
  try{new Rename(new UniqueNsRefresher()).apply(init1.p,new C("Outer",-1),l1,map(s2),wrap,wrap,wrap,wrap);Assertions.fail();}
  catch(FailErr fe){}
  Err.strCmp(msg[0],err);
  }
public static void pass(String sl1,String s2,String sl3){
  Resources.clearRes();
  Init init1=new Init("{Outer={"+sl1+"#norm{}}");
  Core.L l1=init1.p._ofCore(P.of(0,List.of(new C("Outer",-1))));
  Core.L l3Actual=new Rename(new UniqueNsRefresher()).apply(init1.p,new C("Outer",-1),l1,map(s2),null,null,null,null);
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
   method Void bar(Void y)=(..)
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
   A={interface method Void bar::1()#norm{close}}
   D={interface[This1.A] method  Void bar::1()
     #norm{typeDep=This1.A refined=bar::1()close}}
   B={method Void user(This1.D d)=d.bar::1()
     #norm{typeDep=This1.D watched=This1.D}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
   A={interface method Void bar::1()#norm{close}}
   D={interface[This1.A] method Void bar::1()
     #norm{typeDep=This1.A refined=bar::1()close}}
   B={method Void user(This1.D d)=d.bar::1()
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
   A={interface method Void bar::1()#norm{close}}
   D={[This1.A]method Void bar::1()=void
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
     B={method Void a()#norm{}}
     A={method Void b()#norm{}}
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
     B={method Void a()#norm{}}
     C={method Void b()#norm{}}
     A={method Void c()#norm{}}
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
   mapping: A.s(x)=>A.s(y)
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
   mapping: A.s(x)=>A.s(y)
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
   Redirected classes need to be fully abstract and method This1.B s(This1.B x)=(..)
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
     method This1.B s(This1.B x,This.C c)
     C={#norm{}}
     #norm{typeDep=This1.B, This.C}}
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
   nested class This
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
     #norm{typeDep=This1.C, This.KK}}
   C={#norm{}}
   #norm{typeDep=This.C, This.B.KK}
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
   Full mapping:B->This
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
   nested class This
   Code can not be extracted since it exposes uniquely numbered path nested class D::2
   Full mapping:This->C
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     method This.C s(This.C x)=x
     C={#norm{typeDep=This1 watched=This1}}
     #norm{typeDep=This.C}}""",/*rename map after this line*/"""
     ->C.
   """,/*expected after this line*/"""
   nested class { s(x)=(..) C={..} }
   nested class This
   The implementation can not be removed since the class is watched by nested class C
   Full mapping:This->C
   [file:[###]"""/*next test after this line*/)
    ),new AtomicTest(()->fail("""
     method Void v()
     B={#norm{typeDep=This1}}
     #norm{typeDep=This.B}}""",/*rename map after this line*/"""
     ->C.
   """,/*expected after this line*/"""
   nested class { v() B={..} }
   nested class This
   Code can not be extracted since is circularly depended from nested class B
   Full mapping:This->C
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
   nested class This
   The implementation can not be removed since the class is watched by nested class B
   Full mapping:This-><empty>
   [file:[###]"""/*next test after this line*/)
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
   #norm{typeDep=This.A::1,This}}
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
   can not be redirected, the target This.EB
   does not expose a compatible method method B.b()
   Invalid method inheritance for b():
   the return type Void is not a subtype of the inherited type This2.EA
   Full mapping:A=>This.EA;B=>This.EB
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
   C={method Void a(This1 a)=a.foo()#norm{typeDep=This1 usedMethods=This1.foo()}}
   #norm{typeDep=This}}"""/*next test after this line*/)
    ),new AtomicTest(()->pass("""
    method Void a(This.A a)=void
    A={method Void foo::1()=void #typed{}}
    B={method Any foo()=void #typed{}}
    C={method Void a(This1.A a)=a.foo::1()
      #typed{typeDep=This1.A watched=This1.A}}
    #typed{typeDep=This.A}}
    """,/*rename map after this line*/"""
   A.=><empty>
   """,/*expected after this line*/"""
   method Void a(This.A::2 a)=void
   A::2={method Void foo::1()=void #typed{}}
   B={method Any foo()=void #typed{}}
   C={method Void a(This1.A::2 a)=a.foo::1()
     #typed{typeDep=This1.A::2,This1 watched=This1}}
   #typed{typeDep=This.A::2,This}}
   """/*next test after this line*/)
       ),new AtomicTest(()->fail("""
    method Void a(This.A a)=void
    A={method Void foo::1()=void #typed{}}
    B={method Any foo()=void #typed{}}
    C={method Void a(This1.A a)=a.foo::1()
      #typed{typeDep=This1.A watched=This1.A}}
    #typed{typeDep=This.A}}
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
   Full mapping:This=><empty>
   [file:[###]"""/*next test after this line*/)
  ),new AtomicTest(()->fail("""
    A={method Void foo() #typed{}}
    #typed{}}
    """,/*rename map after this line*/"""
   =>#Any
   """,/*expected after this line*/"""
   nested class { A={..} }
   'This' can not be redirected away
   Full mapping:This=>Any
   [file:[###]"""/*next test after this line*/)
  ),new AtomicTest(()->pass("""
     A={method This foo() #typed{typeDep=This}}
     B={method This foo() #typed{typeDep=This}}
     #typed{}}""",/*rename map after this line*/"""
   A.=>B.
   """,/*expected after this line*/"""
     B={method This foo() #typed{typeDep=This}}
     #typed{}}"""/*next test after this line*/)
  ),new AtomicTest(()->pass("""
     method This foo()
     B={method This foo() #typed{typeDep=This}}
     #typed{typeDep=This}}""",/*rename map after this line*/"""
   =>B.
   """,/*expected after this line*/"""
     B={method This foo() #typed{typeDep=This}}
     #typed{}}"""/*next test after this line*/)
  ),new AtomicTest(()->pass("""
     method This foo()
     B={method This foo() #typed{typeDep=This}}
     #typed{typeDep=This}}""",/*rename map after this line*/"""
   B.=>
   """,/*expected after this line*/"""
     method This foo()
     #typed{typeDep=This}}"""/*next test after this line*/)
  ),/*test78*/new AtomicTest(()->pass("""
     method This foo()
     B={C={method This foo() #typed{typeDep=This}}#typed{}}
     #typed{typeDep=This}}""",/*rename map after this line*/"""
   =>B.C.
   """,/*expected after this line*/"""
     B={C={method This foo() #typed{typeDep=This}}#typed{}}
     #typed{}}"""/*next test after this line*/)
  ),new AtomicTest(()->pass("""
     method This foo()
     B={C={method This foo() #typed{typeDep=This}}#typed{}}
     #typed{typeDep=This}}""",/*rename map after this line*/"""
   B.C.=>
   """,/*expected after this line*/"""
     method This foo()
     B={#typed{}}
     #typed{typeDep=This}}"""/*next test after this line*/)
  ),new AtomicTest(()->pass("""
     A={C={method This foo() #typed{typeDep=This}}#typed{}}
     B={method This foo() #typed{typeDep=This}}
     #typed{}}""",/*rename map after this line*/"""
   A.C.=>B.
   """,/*expected after this line*/"""
     A={#typed{}}
     B={method This foo() #typed{typeDep=This}}
     #typed{}}"""/*next test after this line*/)
  ),new AtomicTest(()->pass("""
     A={method This foo() #typed{typeDep=This}}
     B={C={method This foo() #typed{typeDep=This}}#typed{}}
     #typed{}}""",/*rename map after this line*/"""
   A.=>B.C.
   """,/*expected after this line*/"""
     B={C={method This foo() #typed{typeDep=This}}#typed{}}
     #typed{}}"""/*next test after this line*/)
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
       method Void vb()=This.B::2<:class This.B::2.v::3()
       method Void vc()=This.C<:class This.C.vc()
       B::2={ class method Void v::3()=void #norm{}}
       C={ class method Void vc()=void #norm{}}       
       #norm{
         typeDep=This,This.B::2,This.C
         coherentDep=This,This.B::2,This.C
         usedMethods=This.C.vc()
         }}
     #norm{}}""",/*rename map after this line*/"""
     A.=>D.
   """,/*expected after this line*/"""
   A={
     C={class method Void vc()=void #norm{}}
     #typed{}}
   D={
     method Void vb()=This.B::2<:class This.B::2.v::3()
     method Void vc()=This1.A.C<:class This1.A.C.vc()
     B::2={class method Void v::3()=void #norm{}}
     #norm{
       typeDep=This,This.B::2,This1.A.C
       coherentDep=This,This.B::2,This1.A.C
       usedMethods=This1.A.C.vc()}
     }
   #norm{}}"""/*next test after this line*/)
   ),/*test 85*/new AtomicTest(()->fail("""
     A={B={#norm{}}#norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*D. | A.B.=>K.
   """,/*expected after this line*/"""
   nested class { A={..} }
   Rename map contains two entries for A.B
   Full mapping:A=>*D;A.B=>K
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={B={#norm{}}#norm{}}
     K={#norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*D. | K.=>D.B.
   """,/*expected after this line*/"""
   nested class { A={..} K={..} }
   Two different nested class are renamed into nested class D.B
   Full mapping:A=>*D;K=>D.B
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void foo()=void B={#norm{}}#norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.foo()=>*D.bar()
   """,/*expected after this line*/"""
   nested class { A={..} }
   transitive rename only applicable on nested classes
   Full mapping:A.foo()=>*D.bar()
   [file:[###]"""/*next test after this line*/)   
   ),new AtomicTest(()->fail("""
     A={method Void foo() B={#norm{}}#norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*#Void
   """,/*expected after this line*/"""
   nested class { A={..} }
   mapping: A=>*Void
   nested class A.B
   can not be redirected on a nested of Void
   Full mapping:A=>*Void
   [file:[###]"""/*next test after this line*/)   
   ),new AtomicTest(()->pass("""
     A={B={#norm{}}#norm{}}
     K={#norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*D. | K.=>D.G.
   """,/*expected after this line*/"""
     D={B={#norm{}}G={#norm{}} #norm{} }
     #norm{}}"""/*next test after this line*/)
   ),/*test 90*/new AtomicTest(()->pass("""
     A={ method Void foo()=void #norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*<empty>
   """,/*expected after this line*/"""
     A::1={method Void foo::2()=void #norm{}}
     #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={ method Void a()=void 
       B={ method Void b()=void #norm{}}
       #norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>A1.|A.B.=>B1.
   """,/*expected after this line*/"""
     B1={method Void b()=void #norm{}}
     A1={method Void a()=void #norm{}}
     #norm{}}"""/*next test after this line*/)
//now with coreL inside methods
   ),new AtomicTest(()->pass("""
     A={
       method Library a()={#norm{}}
       #norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>B.
   """,/*expected after this line*/"""
     B={
       method Library a()={#norm{}}
       #norm{}}
     #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={
       method Library a(This that)=(This e=that {
          method Library i(This1 that)=that.a()
          #norm{typeDep=This1 usedMethods=This1.a()}})
       #norm{typeDep=This}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>B.
   """,/*expected after this line*/"""
     B={
       method Library a(This that)=(This e=that {
         method Library i(This1 that)=that.a()
         #norm{typeDep=This1 usedMethods=This1.a()}})
       #norm{typeDep=This}}
     #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     @This.A.a() K={#norm{}}
     A={
       method Library a()={
          method Library i(This1 that)=that.a()
          #norm{typeDep=This1 usedMethods=This1.a()}}
       #norm{typeDep=This}}
     #norm{typeDep=This.A}}""",/*rename map after this line*/"""
     A.a()=>A.foo()
   """,/*expected after this line*/"""
     @This.A.foo() K={#norm{}}
     A={
       method Library foo()={
         method Library i(This1 that)=that.foo()
         #norm{typeDep=This1 usedMethods=This1.foo()}}
       #norm{typeDep=This}}
     #norm{typeDep=This.A}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={method Void foo()=void #norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.foo()=>A.bar() | A.=><empty>
   """,/*expected after this line*/"""
   nested class { A={..} }
   mapping: A.foo()=>A.bar()
   nested class A
   is already involved in the rename; thus method A.foo()
   can not be renamed
   Full mapping:A.foo()=>A.bar();A=><empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={method Void foo()=void #norm{}}
     C={method Void a(This1.A a)=a.foo() 
       #norm{typeDep=This1.A, usedMethods=This1.A.foo()}}
     #norm{}}""",/*rename map after this line*/"""
     A.foo()=><empty> | A.=>B.
   """,/*expected after this line*/"""
     C={method Void a(This1.B a)=a.foo::1()
       #norm{typeDep=This1.B watched=This1.B}}
     B={method Void foo::1()=void #norm{}}
     #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={method Void foo()=void #norm{}}
     C={method Void a(This1.A a)=a.foo() 
       #norm{typeDep=This1.A, usedMethods=This1.A.foo()}}
     #norm{}}""",/*rename map after this line*/"""
     A.foo()=><empty>
   """,/*expected after this line*/"""
     A={method Void foo::1()=void #norm{}}
     C={method Void a(This1.A a)=a.foo::1()
       #norm{typeDep=This1.A watched=This1.A}}
     #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     O={#norm{}}
     A={method Void foo()=void #norm{typeDep=This1.O watched=This1.O}}
     #norm{}}""",/*rename map after this line*/"""
     A.foo()=><empty> | A.=><empty>
   """,/*expected after this line*/"""
     O={#norm{}}
     A::2={method Void foo::1()=void #norm{typeDep=This1.O watched=This1.O}}
     #norm{typeDep=This.O watched=This.O}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     D={#norm{}}
     A={ 
       B={
         C={#norm{typeDep=This3.D watched=This3.D}}
         #norm{}}
       #norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.B.C.=><empty>|A.B.=>K.
   """,/*expected after this line*/"""
   D={#norm{}}
   A={#norm{}}
   K={
     C::1={#norm{typeDep=This2.D watched=This2.D}}
     #norm{typeDep=This1.D watched=This1.D}}
   #norm{}}"""/*next test after this line*/)
      ),new AtomicTest(()->pass("""
     D={#norm{}}
     A={ 
       B={
         C={#norm{typeDep=This3.D watched=This3.D}}
         #norm{}}
       #norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.B.C.=><empty>
   """,/*expected after this line*/"""
   D={#norm{}}
   A={
     B={
       C::1={#norm{typeDep=This3.D watched=This3.D}}
       #norm{typeDep=This2.D watched=This2.D}}
     #norm{}}
   #norm{}}"""/*next test after this line*/)
      ),new AtomicTest(()->pass("""
     D={#norm{}}
     A={ 
       B={
         C={#norm{typeDep=This3.D watched=This3.D}}
         #norm{}}
       #norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.B.C.->A.K.
   """,/*expected after this line*/"""
     D={#norm{}}
     A={
       B={ C={#norm{}} #norm{}}
       K={#norm{typeDep=This2.D watched=This2.D}}
       #norm{}}
     #norm{}}"""/*next test after this line*/)
      ),new AtomicTest(()->pass("""
     D={#norm{}}
     A={ 
       B={
         C={method Void a::2()=void #norm{}}
         C::1={method Void b::3()=void #norm{}}
         #norm{}}
       #norm{}}
     #norm{}}""",/*rename map after this line*/"""
     A.B.C.=><empty>
   """,/*expected after this line*/"""
   D={#norm{}}
   A={
     B={
       C::4={method Void a::2()=void #norm{}}
       C::1={method Void b::3()=void #norm{}}
       #norm{}}
     #norm{}}   
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     J1={interface #norm{}}
     J2={interface method Void a() #norm{}}
     A={
       method Library a()={
         I1={interface [This3.J1] #norm{typeDep=This3.J1}}
         I2={interface [This3.J2] method Void a() #norm{typeDep=This3.J2 refined=a()}}
         C={ method Void c(This3.J2 j)=j.a() #norm{typeDep=This3.J2 usedMethods=This3.J2.a()}}
         #norm{}}
       #norm{typeDep=This1.J1,This1.J2 hiddenSupertypes=This1.J1,This1.J2 usedMethods=This1.J2.a()}}
     #norm{}}""",/*rename map after this line*/"""
     J1.=><empty>|J2.a()=><empty>
   """,/*expected after this line*/"""
   nested class { J1={..} J2={..} A={..} }
   The method J2.a()
   can not be made private since is implemented by private parts of nested class A
   Full mapping:J1=><empty>;J2.a()=><empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     J1={interface #norm{}}
     J2={interface method Void a() #norm{}}
     A={
       method Library a()={
         I1={interface [This3.J1] #norm{typeDep=This3.J1}}
         C={ method Void c(This3.J2 j)=j.a() #norm{typeDep=This3.J2 usedMethods=This3.J2.a()}}
         #norm{}}
       #norm{typeDep=This1.J1,This1.J2 hiddenSupertypes=This1.J1 usedMethods=This1.J2.a()}}
     #norm{}}""",/*rename map after this line*/"""
     J1.=><empty>|J2.a()=><empty>
   """,/*expected after this line*/"""
   J1::1={interface #norm{}}
   J2={interface method Void a::2()#norm{close}}
   A={method Library a()={
       I1={interface[This3.J1::1]
         #norm{typeDep=This3.J1::1, This3 watched=This3 close}}
       C={
         method Void c(This3.J2 j)=j.a::2()
         #norm{typeDep=This3.J2 watched=This3.J2}}
       #norm{}}
     #norm{typeDep=This1.J1::1, This1, This1.J2 watched=This1, This1.J2}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     J1={interface method Void a() #norm{}}
     J2={interface[This1.J1] method Void a() #norm{typeDep=This1.J1 refined=a()}}
     A={[This1.J2,This1.J1]method Void a()=void #norm{typeDep=This1.J2,This1.J1 refined=a()}}
     #norm{}}""",/*rename map after this line*/"""
     J1.=>J0.|J1.a()=><empty>
   """,/*expected after this line*/"""
   J2={interface[This1.J0]method Void a::1()
     #norm{typeDep=This1.J0 refined=a::1()close}}
   A={[This1.J2, This1.J0]method Void a::1()=void
     #norm{typeDep=This1.J2, This1.J0 refined=a::1()}}
   J0={interface method Void a::1()#norm{close}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     J1={interface method Void a() #norm{}}
     J2={interface[This1.J1] method Void a() #norm{typeDep=This1.J1 refined=a()}}
     A={[This1.J2,This1.J1]method Void a()=void #norm{typeDep=This1.J2,This1.J1 refined=a()}}
     #norm{}}""",/*rename map after this line*/"""
     J1.=><empty>|J1.a()=><empty>
   """,/*expected after this line*/"""
   J1::1={interface method Void a::2()#norm{close}}
   J2={interface[This1.J1::1]method Void a::2()
     #norm{typeDep=This1.J1::1, This1 watched=This1 refined=a::2()close}}
   A={[This1.J2, This1.J1::1]method Void a::2()=void
     #norm{typeDep=This1.J2, This1.J1::1, This1 watched=This1 refined=a::2()}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     J1={interface method Void a() #norm{}}
     J2={interface[This1.J1] method Void a() #norm{typeDep=This1.J1 refined=a()}}
     A={[This1.J2,This1.J1]method Void b()=void method Void a()=void #norm{typeDep=This1.J2,This1.J1 refined=a()}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*<empty>
   """,/*expected after this line*/"""
   J1={interface method Void a()#norm{}}
   J2={interface[This1.J1]method Void a()
     #norm{typeDep=This1.J1 refined=a()}}
   A::1={[This1.J2, This1.J1]
     method Void b::2()=void
     method Void a()=void
     #norm{typeDep=This1.J2, This1.J1 refined=a()}
     }
   #norm{typeDep=This.J2, This.J1 hiddenSupertypes=This.J2, This.J1}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={method This a()=this #norm{typeDep=This}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*<empty>
   """,/*expected after this line*/"""
   A::1={method This a::2()=this
     #norm{typeDep=This, This1 watched=This1}}
   #norm{typeDep=This}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass(/*this show the former was failing only for privates*/"""
     A={method This a()=this #norm{typeDep=This}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*B.
   """,/*expected after this line*/"""
   B={method This a()=this #norm{typeDep=This}}
   #norm{}}"""/*next test after this line*/)
   ),//TODO: Ideally, I would like a::2 below
   new AtomicTest(()->pass("""
     A={
       method Void v()=void
       method Library a()={ method Void m(This1 a)=a.v() #norm{typeDep=This1 usedMethods=This1.v()}}
       #norm{typeDep=This}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*<empty>
   """,/*expected after this line*/"""
   A::1={
     method Void v::2()=void
     method Library a::2()={
       method Void m(This1 a)=a.v::2()
       #norm{typeDep=This1, This2 watched=This2}}
     #norm{typeDep=This, This1 watched=This1}}
   #norm{typeDep=This}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={
       method Void v(This.B b)=b.b()       
       B={ method Void b()=void #norm{}} 
       #norm{typeDep=This.B usedMethods=This.B.b()}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*<empty>
   """,/*expected after this line*/"""
   A::1={
     method Void v::2(This.B::3 b)=b.b::4()
     B::3={method Void b::4()=void #norm{}}
     #norm{typeDep=This.B::3,This}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={ B={ #norm{}} #norm{}}
     C={ method This1.A.B foo() #norm{typeDep=This1.A.B}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*<empty>
   """,/*expected after this line*/"""
   A::1={ B::2={#norm{}}#norm{}}
   C={method This1.A::1.B::2 foo()
   #norm{typeDep=This1.A::1.B::2,This1 watched=This1}}
   #norm{}}"""/*next test after this line*/)   
   ),new AtomicTest(()->pass("""
     A={ B={ #norm{}} #norm{}}
     C={ method This1.A.B foo() #norm{typeDep=This1.A.B}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*K.
   """,/*expected after this line*/"""
   C={method This1.K.B foo()
     #norm{typeDep=This1.K.B}}
   K={B={#norm{}}#norm{}}
   #norm{}}"""/*next test after this line*/)
   //8 permutation tests
   ),/*test 114*/new AtomicTest(()->pass(nested4,/*rename map after this line*/"""
     A.=>*<empty>
   """,/*expected after this line*/"""
   A::1={method Void foo::2(This a, This.B::3 b, This.B::3.C::5 c, This.B::3.C::5.D::7 d)=void
     B::3={method Void foo::4(This1 a, This b, This.C::5 c, This.C::5.D::7 d)=void
       C::5={method Void foo::6(This2 a, This1 b, This c, This.D::7 d)=void
          D::7={method Void foo::8(This3 a, This2 b, This1 c, This d)=void
            #norm{typeDep=This3, This4, This2, This1, This watched=This4, This3, This2, This1}}
          #norm{typeDep=This2, This3, This1, This, This.D::7 watched=This3, This2, This1}}
        #norm{typeDep=This1, This2, This, This.C::5, This.C::5.D::7 watched=This2, This1}}
      #norm{typeDep=This, This1, This.B::3, This.B::3.C::5, This.B::3.C::5.D::7 watched=This1}}
    #norm{typeDep=This}}
    """/*next test after this line*/)
   ),new AtomicTest(()->pass(nested4,/*rename map after this line*/"""
     A.B.=>*<empty>
   """,/*expected after this line*/"""
   A={method Void foo(This a, This.B::1 b, This.B::1.C::3 c, This.B::1.C::3.D::5 d)=void
     B::1={method Void foo::2(This1 a, This b, This.C::3 c, This.C::3.D::5 d)=void
       C::3={method Void foo::4(This2 a, This1 b, This c, This.D::5 d)=void
         D::5={method Void foo::6(This3 a, This2 b, This1 c, This d)=void
           #norm{typeDep=This3, This2, This1, This watched=This3, This2, This1}}
         #norm{typeDep=This2, This1, This, This.D::5 watched=This2, This1}}
       #norm{typeDep=This1, This, This.C::3, This.C::3.D::5 watched=This1}}
     #norm{typeDep=This, This.B::1, This.B::1.C::3, This.B::1.C::3.D::5}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass(nested4,/*rename map after this line*/"""
     A.B.C.=>*<empty>
   """,/*expected after this line*/"""
   A={method Void foo(This a, This.B b, This.B.C::1 c, This.B.C::1.D::3 d)=void
     B={method Void foo(This1 a, This b, This.C::1 c, This.C::1.D::3 d)=void
       C::1={method Void foo::2(This2 a, This1 b, This c, This.D::3 d)=void
         D::3={method Void foo::4(This3 a, This2 b, This1 c, This d)=void
           #norm{typeDep=This3, This2, This1, This watched=This2, This1}}
         #norm{typeDep=This2, This1, This, This.D::3 watched=This1}}
       #norm{typeDep=This1, This, This.C::1, This.C::1.D::3}}
     #norm{typeDep=This, This.B, This.B.C::1, This.B.C::1.D::3 watched=This.B}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass(nested4,/*rename map after this line*/"""
     A.B.C.D.=>*<empty>
   """,/*expected after this line*/"""
   A={method Void foo(This a, This.B b, This.B.C c, This.B.C.D::1 d)=void
     B={method Void foo(This1 a, This b, This.C c, This.C.D::1 d)=void
       C={method Void foo(This2 a, This1 b, This c, This.D::1 d)=void
         D::1={method Void foo::2(This3 a, This2 b, This1 c, This d)=void
           #norm{typeDep=This3, This2, This1, This watched=This1}}
         #norm{typeDep=This2, This1, This, This.D::1}}
       #norm{typeDep=This1, This, This.C, This.C.D::1 watched=This.C}}
     #norm{typeDep=This, This.B, This.B.C, This.B.C.D::1 watched=This.B.C}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass(nested4,/*rename map after this line*/"""
     A.=>*K.
   """,/*expected after this line*/"""
   K={method Void foo(This a, This.B b, This.B.C c, This.B.C.D d)=void
     B={method Void foo(This1 a, This b, This.C c, This.C.D d)=void
       C={method Void foo(This2 a, This1 b, This c, This.D d)=void
         D={method Void foo(This3 a, This2 b, This1 c, This d)=void
           #norm{typeDep=This3, This2, This1, This}}
         #norm{typeDep=This2, This1, This, This.D}}
       #norm{typeDep=This1, This, This.C, This.C.D}}
     #norm{typeDep=This, This.B, This.B.C, This.B.C.D}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass(nested4,/*rename map after this line*/"""
     A.B.=>*K.
   """,/*expected after this line*/"""
   A={method Void foo(This a, This1.K b, This1.K.C c, This1.K.C.D d)=void
     #norm{typeDep=This, This1.K, This1.K.C, This1.K.C.D}}
   K={method Void foo(This1.A a, This b, This.C c, This.C.D d)=void
     C={method Void foo(This2.A a, This1 b, This c, This.D d)=void
       D={method Void foo(This3.A a, This2 b, This1 c, This d)=void
         #norm{typeDep=This3.A, This2, This1, This}}
       #norm{typeDep=This2.A, This1, This, This.D}}
     #norm{typeDep=This1.A, This, This.C, This.C.D}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass(nested4,/*rename map after this line*/"""
     A.B.C.=>*K.
   """,/*expected after this line*/"""
   A={method Void foo(This a, This.B b, This1.K c, This1.K.D d)=void
     B={method Void foo(This1 a, This b, This2.K c, This2.K.D d)=void
       #norm{typeDep=This1, This, This2.K, This2.K.D}}
     #norm{typeDep=This, This.B, This1.K, This1.K.D}}
   K={method Void foo(This1.A a, This1.A.B b, This c, This.D d)=void
     D={method Void foo(This2.A a, This2.A.B b, This1 c, This d)=void
       #norm{typeDep=This2.A, This2.A.B, This1, This}}
     #norm{typeDep=This1.A, This1.A.B, This, This.D}}
   #norm{}}"""/*next test after this line*/)
   ),/*test 121*/new AtomicTest(()->pass(nested4,/*rename map after this line*/"""
     A.B.C.D.=>*K.
   """,/*expected after this line*/"""
   A={method Void foo(This a, This.B b, This.B.C c, This1.K d)=void
     B={method Void foo(This1 a, This b, This.C c, This2.K d)=void
       C={method Void foo(This2 a, This1 b, This c, This3.K d)=void
         #norm{typeDep=This2, This1, This, This3.K}}
       #norm{typeDep=This1, This, This.C, This2.K}}
     #norm{typeDep=This, This.B, This.B.C, This1.K}}
   K={method Void foo(This1.A a, This1.A.B b, This1.A.B.C c, This d)=void
     #norm{typeDep=This1.A This1.A.B This1.A.B.C This}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={
       method Void foo(This a,This.B b)=void
       B={
         method Void foo(This1 a,This b)=void
         #norm{typeDep=This1,This}}
       #norm{typeDep=This,This.B}}
     #norm{}}""",/*rename map after this line*/"""
     A.B.=>*K.
   """,/*expected after this line*/"""
   A={method Void foo(This a, This1.K b)=void
     #norm{typeDep=This, This1.K}}
   K={method Void foo(This1.A a, This b)=void
     #norm{typeDep=This1.A,This}}
   #norm{}}"""/*next test after this line*/)

   ),new AtomicTest(()->pass("""
     A={
       method Void foo(This a,This.B b)=void
       B={
         method Void foo(This1 a,This b)=void
         #norm{typeDep=This1,This}}
       #norm{typeDep=This,This.B}}
     #norm{}}""",/*rename map after this line*/"""
     A.B.=>*K.J.
   """,/*expected after this line*/"""
   A={method Void foo(This a, This1.K.J b)=void
     #norm{typeDep=This, This1.K.J}}
   K={
     J={method Void foo(This2.A a, This b)=void
       #norm{typeDep=This2.A, This}}
     #typed{}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={
       method Void foo(This a,This.B b)=void
       B={
         method Void foo(This1 a,This b)=void
         #norm{typeDep=This1,This}}
       #norm{typeDep=This,This.B}}
     #norm{}}""",/*rename map after this line*/"""
     A.B.=>*<empty>
   """,/*expected after this line*/"""
   A={method Void foo(This a, This.B::1 b)=void
     B::1={method Void foo::2(This1 a, This b)=void
       #norm{typeDep=This1, This watched=This1}}
     #norm{typeDep=This, This.B::1}}
   #norm{}}"""/*next test after this line*/)//watched=This1 is added by CloneRename before the private content is moved in place
   ),new AtomicTest(()->pass("""
     A={class method Void foo()=void #norm{}}
     B={method Void bar()=This1.A<:class This1.A.foo() #norm{typeDep=This1.A coherentDep=This1.A usedMethods=This1.A.foo()}}
     #norm{}}""",/*rename map after this line*/"""
     A.=>*<empty>
   """,/*expected after this line*/"""
   A::1={class method Void foo::2()=void #norm{}}
   B={method Void bar()=This1.A::1<:class This1.A::1.foo::2()
     #norm{typeDep=This1.A::1, This1 coherentDep=This1.A::1 watched=This1}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface class method Void foo() #norm{}}
     C={
       class method Library lib()={[This2.I]
         class method Void foo()=void
         #norm{typeDep=This2.I refined=foo()}}
       #norm{typeDep=This1.I hiddenSupertypes=This1.I}}
     #norm{}}""",/*rename map after this line*/"""
     I.=>*<empty> | C.=>*<empty>
   """,/*expected after this line*/"""
   nested class { I={..} C={..} }
   The method I.foo()
   can not be made private since is implemented by private parts of nested class C
   Full mapping:I=>*<empty>;C=>*<empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     method This.A bar(This.A a)
     K::1={#norm{}}
     A={interface class method Void foo() #norm{}}
     B={method Void bar()=void #norm{ typeDep=This1 watched=This1}}
     #norm{typeDep=This.A This.B watched=This.A This.B}}""",/*rename map after this line*/"""
     =>A.
   """,/*expected after this line*/"""
   nested class { bar(a) A={..} B={..} }
   nested class This
   can not be turned into an interface inside of a rename operation
   Full mapping:This=>A
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     method This.A bar(This.A a)
     K::1={#norm{}}
     A={class method Void foo() #norm{}}
     B={method Void bar()=void #norm{ typeDep=This1 watched=This1}}
     #norm{typeDep=This.A This.B watched=This.A This.B}}""",/*rename map after this line*/"""
     =>A.
   """,/*expected after this line*/"""
   A={
     class method Void foo()
     method This bar(This a)
     K::1={#norm{}}
     #norm{typeDep=This, This1.B watched=This1.B}
     }
   B={method Void bar()=void
     #norm{typeDep=This1.A watched=This1.A}}
   #typed{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     method This.B bar(This.B a)
     K::1={#norm{}}
     B={method Void bar()=void #norm{ typeDep=This1 watched=This1}}
     #norm{typeDep=This.B watched=This.B}}""",/*rename map after this line*/"""
     =>A.
   """,/*expected after this line*/"""
   B={method Void bar()=void #norm{typeDep=This1.A watched=This1.A}}
   A={method This1.B bar(This1.B a)
     K::1={#norm{}}
     #norm{typeDep=This1.B watched=This1.B}}
   #typed{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     method Void v(This.A a)
     @This.A A={#norm{}}
     #norm{typeDep=This.A}}""",/*rename map after this line*/"""
     =>B.
   """,/*expected after this line*/"""
   @This.A A={#norm{}}
   B={method Void v(This1.A a)#norm{typeDep=This1.A}}
   #typed{typeDep=This.A}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     C={
       method Void v(This.A a)
       @This.A A={#norm{}}
       #norm{typeDep=This.A}}
     #norm{}}""",/*rename map after this line*/"""
     C.=>B.
   """,/*expected after this line*/"""
   C={
     @This.A A={#norm{}}
     #typed{typeDep=This.A}//why this is not triggering the "not well formedness" as above?
     }
   B={method Void v(This1.C.A a)
     #norm{typeDep=This1.C.A}}
   #norm{}}"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     I={interface method Void foo() #norm{}}
     C={
       D={[This2.I] method Void foo()=void
         K={method Void user(This1 that)=that.foo()
           #norm{typeDep=This1 usedMethods=This1.foo()}}
         #norm{typeDep=This2.I refined=foo()}}
       #norm{}}
     #norm{}}""",/*rename map after this line*/"""
     C.=>*<empty>
   """,/*expected after this line*/"""
   I={interface method Void foo()#norm{}}
   C::1={
     D::2={[This2.I]method Void foo()=void
       K::3={method Void user::4(This1 that)=that.foo()
         #norm{typeDep=This1, This2 watched=This2}}
       #norm{typeDep=This2.I, This, This2, This1 watched=This2 refined=foo()}}
     #norm{typeDep=This1.I,This.D::2, This1, This watched=This1 hiddenSupertypes=This1.I}}
   #norm{typeDep=This.I, This hiddenSupertypes=This.I}}
   """/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     C::1={method Void foo::2()=void #norm{}}
     D={method Void foo(This1.C::1 c)=c.foo::2()
       #norm{typeDep=This1 This1.C::1 watched=This1}}
     #norm{}}""",/*rename map after this line*/"""
     =>K.
   """,/*expected after this line*/"""
   D={method Void foo(This1.K.C::1 c)=c.foo::2()
     #norm{typeDep=This1.K, This1.K.C::1, watched=This1.K}}
   K={C::1={method Void foo::2()=void #norm{}}
     #norm{}}
   #typed{}}"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     I={interface method Void foo() #norm{}}
     D={[This1.I] method Void foo()=void
       #norm{typeDep=This1.I refined=foo()}}
     #norm{}}""",/*rename map after this line*/"""
     A.->*<empty>
   """,/*expected after this line*/"""
   nested class { I={..} D={..} }
   nested class A
   does not exists
   Full mapping:A->*<empty>
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->pass("""
     A={I={interface method Void foo() #norm{}} #norm{}}
     D={[This1.A.I] method Void foo()=void
       #norm{typeDep=This1.A.I refined=foo()}}
     #norm{}}""",/*rename map after this line*/"""
     A.->*<empty>
   """,/*expected after this line*/"""
   A={I={interface method Void foo()#norm{}}#norm{}}
   D={[This1.A.I]method Void foo()=void
     #norm{typeDep=This1.A.I refined=foo()}}
   #norm{}}"""/*next test after this line*/)
   ));}
private static String nested4="""
     A={
       method Void foo(This a,This.B b,This.B.C c,This.B.C.D d)=void
       B={
         method Void foo(This1 a,This b,This.C c,This.C.D d)=void
         C={
           method Void foo(This2 a,This1 b,This c,This.D d)=void
           D={method Void foo(This3 a,This2 b,This1 c,This d)=void
             #norm{typeDep=This3,This2,This1,This}}
           #norm{typeDep=This2,This1,This,This.D}} 
         #norm{typeDep=This1,This,This.C,This.C.D}}
       #norm{typeDep=This,This.B,This.B.C,This.B.C.D}}
     #norm{}}""";

   //TODO: it seams like it may refresh the novel private numbers for every rename in L42. Test it in a testRenameL42? how?
      /*
        sumInRename ignore errors for growHiddenError and differentInterfaces
        early checks? 
        rename Cs=>Cs' checks:
          Cs' empty or Cs.interface?=Cs'.interface? and if both interface, check growHiddenError
        rename Cs->Cs' checks:
          Cs' empty or Cs'.interface?=empty         

    issue with interfaces and private nesteds:
    with a sum, we would prevent top to become an interface since is watched,
    however when the sum is invoked by rename, l2 is incomplete, thus l2.A does not
    result as watched, and the nested classes are removed.
    We now block all the "advanced" sum features during rename
    
    
    
    TODO: WELL FORMEDNESS OF INFOS:
    now does: ProgramTypeSystem.type(true,p.update(l,false));
    but:
      1-this type public nested classes even if only the outer is "typed" labelled"
      2-this type private nested classes if the outer is "typed". Good. But...
        we now just put "typed" on any box we introduce. Is this ok? Yes because the created boxes never have private nesteds?
    */
}