package is.L42.tests;

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
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestRename
extends AtomicTest.Tester{
public static LinkedHashMap<Arrow,Arrow> map(String s){
  LinkedHashMap<Arrow,Arrow>map=new LinkedHashMap<>();
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
    if(b.equals("<empty>")){map.put(key,arrow);return;}
    if(b.startsWith("#")){
      arrow._path=P.parse(b.substring(1));
      assert !map.containsKey(key);
      map.put(key,arrow);return;
      }
    var rest=fromS(b);
    arrow._cs=rest.cs;
    arrow._sOut=rest._s;
    assert !map.containsKey(key);
    map.put(key,arrow);
    });  
  System.out.println(map.values());
  return map;
  }
static Arrow fromS(String s){
  int i=s.lastIndexOf(".");
  assert i!=-1;
  var cs=P.parse("This0."+s.substring(0,i)).toNCs().cs();
  S _s=null;
  s=s.substring(i+1);
  if(!s.isEmpty()){_s=S.parse(s);}
  return new Arrow(cs,_s);
  }
static class FailErr extends Error{}
public static void fail(String sl1,String s2,String err){
  Resources.clearRes();
  String[]msg={null};
  Init init1=new Init("{A={"+sl1+"#norm{}}");
  Core.L l1=init1.p._ofCore(P.of(0,List.of(new C("A",-1))));
  Function<L42£LazyMsg,L42Any>wrap=lm->{msg[0]=lm.getMsg();throw new FailErr();};
  try{new Rename().apply(init1.p,new C("A",-1),l1,map(s2),wrap,wrap,wrap,wrap);Assertions.fail();}
  catch(FailErr fe){}
  Err.strCmp(msg[0],err);
  }
public static void pass(String sl1,String s2,String sl3){
  Resources.clearRes();//TODO:
  Init init1=new Init("{A={"+sl1+"#norm{}}");
  Core.L l1=init1.p._ofCore(P.of(0,List.of(new C("A",-1))));
  Core.L l3Actual=new Rename().apply(init1.p,new C("A",-1),l1,map(s2),null,null,null,null);
  Init init3=new Init("{A={"+sl3+"#norm{}}");
  Core.L l3Expected=init3.p._ofCore(P.of(0,List.of(new C("A",-1))));
  assertEquals(l3Expected, l3Actual);
  }
public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   //pass("#norm{}}","#norm{}}","#norm{}}")
   //),new AtomicTest(()->
   pass("A={#norm{}}#norm{}}","A.=>B.","B={#norm{}}#norm{}}")
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
   Rename can not map two methods on the same method: method A.s(y)
   Full mapping:A.s(x)=>A.s(y);A.s(z)=>A.s(y)
   [file:[###]"""/*next test after this line*/)
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
   The nested class nested class A
   can not be made abstract since is watched by nested class C
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
   Redirected classes need to be fully abstract and not watched, but the following mapping is present: A=>This1.K
   and imm method imm This1.B s(imm This1.B x)=(..)
   is implemented
   Full mapping:A=>This1.K
   [file:[###]"""/*next test after this line*/)
   ),new AtomicTest(()->fail("""
     A={#norm{}} B={#norm{typeDep=This1.A watched=This1.A}}
   #norm{}} K={#typed{}}""",/*rename map after this line*/"""
     A.=>#This1.K
   """,/*expected after this line*/"""
   nested class { A={..} B={..} }
   Redirected classes need to be fully abstract and not watched, but is watched by nested class B
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
   ));}
}