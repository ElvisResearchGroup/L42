package testSlow;

import helpers.TestHelper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;






import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reduction.Executor;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.ErrorMessage;
import ast.ErrorMessage.FinalResult;
import ast.Expression;
import ast.ExpCore.ClassB;
import ast.Ast;
import ast.Ast.Stage;
import ast.Ast.Path;
import auxiliaryGrammar.Program;
import facade.L42;
import facade.Parser;

public class TestL42SuperSlow {

@Before public void config() {TestHelper.configureForTest();}
@Test
public void test7() throws IOException{
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"  C:{"
,"  t1=5N>2N"
,"  t2=5N>=2N"
,"  t3=2N<5N"
,"  t4=2N<=5N"
,"  t5=2N!=5N"
,"  t6=5N+3N==8N"
,"  t7=5N-3N==2N"
,"  t8=5N*3N==15N"
,"  t9=5N/3N==1N"
,"  if t1 & t2 & t3 & t4 & t5 &t6 &t7 &t8 &t9  (return ExitCode.normal())"
,"  return ExitCode.failure()"
,"  }}"
)).getErrCode(),0);}

@Test
public void testList1() throws IOException{
  L42.record=new StringBuilder();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"SList:Collections.list(S)"
,"C:{"
,"  l=SList[S\"a\";S\"b\";] "
,"  with s in l.vals() ("
,"    use Alu check stringDebug(s.that()) void"
,"    )"
,"  return ExitCode.normal()"
,"  }"
,"}"
)).getErrCode(),0);
  Assert.assertEquals(L42.record.toString(),"a\nb\n");
  L42.record=new StringBuilder();
  }

@Test
public void testList2() throws IOException{
  L42.record=new StringBuilder();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"SList:Collections.list(S)"
,"C:{"
,"  l=SList[S\"a\";S\"b\";] 'var?"
,"  l2=SList[with si in l.vals() (use[si])]"
,"  with s in l2.vals() ("
,"    use Alu check stringDebug(s.that()) void"
,"    )"
,"  return ExitCode.normal()"
,"  }"
,"}"
)).getErrCode(),0);
  Assert.assertEquals(L42.record.toString(),"a\nb\n");
  L42.record=new StringBuilder();
  }


@Test
public void testData() throws IOException{
  L42.record=new StringBuilder();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"Person:Data[]<{(S name, N age)}"
,"Main:{"
,"  mario=Person(name:S\"Mario\", age:33N)"
,"  mario2=Person(name:S\"Mario\", age:33N)"
,"  nicola=Person(name:S\"Nicola\", age:33N)"
,"  Bool eq1=mario==mario2"
,"  Bool eq2=mario==nicola"
,"  if eq1 & !eq2 (return ExitCode.normal())"
,"  return ExitCode.failure()"
,"  }"
,"}"
)).getErrCode(),0);
Assert.assertEquals(L42.record.toString(),"");
L42.record=new StringBuilder();
}


@Test
public void testMethods() throws IOException{
  L42.record=new StringBuilder();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"C:{"
," Library a={()"
,"   method Void aa() void"
,"   method Void bb(Outer0 a) void"
,"   }"
," i=Introspection(a) "
," ms=i.methods()"
," with m in ms.vals() ( Debug(m.name() ))"
," return ExitCode.normal()"
," }"
,"}"
)).getErrCode(),0);
  Assert.assertEquals(L42.record.toString(),"bb(a)\naa()\n#apply()\n");
  L42.record=new StringBuilder();
  }
@Test
public void testOpt() throws IOException{
  L42.record=new StringBuilder();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"C:{()}"
,"COpt:Opt(C)"
,"Main:{"
,"  C c=C()"
,"  var COpt cOpt=COpt()"
,"  cOpt:=COpt(c)"
,"  c2=!cOpt"
,"  return ExitCode.normal()"
,"  }"
,"}"
)).getErrCode(),0);
Assert.assertEquals(L42.record.toString(),"");
L42.record=new StringBuilder();
}

@Test
public void testFields() throws IOException{
  L42.record=new StringBuilder();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"C:{"
," Library a={(C c1 C c2)"
,"   method Void aa() void"
,"   method Void bb(Outer0 a) void"
,"   method Void cc(Outer0 b Outer0 c) void"
,"   }"
," i=Introspection(a)"
," with f in i.fields().vals() ("
,"   Debug(f.name())"
,"   )"
," return ExitCode.normal()"
," }"
,"}"
)).getErrCode(),0);
Assert.assertEquals(L42.record.toString(),"c2\nc1\n");
L42.record=new StringBuilder();
}


@Test
public void testParameters() throws IOException{
  L42.record=new StringBuilder();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"C:{"
," Library a={()"
,"   method Void aa() void"
,"   method Void bb(Outer0 a) void"
,"   method Void cc(Outer0 b Outer0 c) void"
,"   }"
," i=Introspection(a)"
," ms=i.methods()"
," with m in ms.vals() ("
,"   Debug(m.name())"
,"   with p in m.parameters().vals() ( Debug(p.name()) )"
,"   )"
,"return ExitCode.normal()"
," }"
,"}"
)).getErrCode(),0);
Assert.assertEquals(L42.record.toString(),"cc(b,c)\nc\nb\nbb(a)\na\naa()\n#apply()\n");
L42.record=new StringBuilder();
}

@Test
public void testResolver() throws IOException{
  L42.record=new StringBuilder();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"C:{"
,"  a={ method N res() 10N }"
,"  b={ method N res() 2N }"
,"  c={ method N res() 30N }"
,"  d={"
,"    method N res() this.left()+this.right()"
,"    method N left()"
,"    method N right()"
,"    }"
,"  return Use[a;b;c;resolver:d;]<{()}"
,"  }"
,"Main:{"
,"  Debug(C().res().toS())"
,"  return ExitCode.normal()"
,"  }"
,"}"
)).getErrCode(),0);
Assert.assertEquals(L42.record.toString(),"42\n");
L42.record=new StringBuilder();
}


@Test
public void testAdapt() throws IOException{
  L42.record=new StringBuilder();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"C:{"
,"  Library a=Adapt[Name\"aa\" into:Name\"aa1\";Name\"bb(a)\" into:Name\"bb1(a)\";Name\"D\" into :Name\"%d\"]<{()"
,"    method Void aa() void"
,"    method Void bb(Outer0 a) void"
,"    D:{()}"
,"    }"
,"  i=Introspection(a)"
,"  ms=i.methods()"
,"  with m in ms.vals() ("
,"    Debug(m.name())"
,"    )"
,"  i2=Introspection(a,node:S\"%d\")"
,"  ms2=i2.methods()"
,"  with m2 in ms2.vals() ("
,"    Debug(m2.name())"
,"    )"
,"  return ExitCode.normal()"
,"  }"
,"}"
)).getErrCode(),0);
Assert.assertEquals(L42.record.toString(),"bb1(a)\naa1\n#apply\n#apply\n");
L42.record=new StringBuilder();
}


@Test
public void test14() throws IOException{
  String s=TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"SList:Collections.list(S)"
,"C:{"
,"  l1=SList[S\"a\";S\"b\";]"//
,"  l2=SList[S\"c\";]"
,"  with"
    + " s1 in l1.valsCut()"//
    + " s2 in l2.valsCut() ("
,"    use Alu check stringDebug(s1.that()) void"//
,"    use Alu check stringDebug(s2.that()) void"
,"    )"
,"  return ExitCode.normal()"
,"  }"
,"}"
);
  try{
    FinalResult res = L42.runSlow(null,s);
    System.out.println(L42.record.toString());
    if(res.getResult()!=0){
      System.out.println(res);
      TestHelper.reportError(null);
      }
    }
  catch(ErrorMessage msg){
    System.out.println(L42.record.toString());
    msg.printStackTrace();
    TestHelper.reportError(msg);
    }
  }




@Test
public void test15() throws IOException{
  String s=TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"A:{() method Bool m(Void v) True() }"
//,"NList:Collections.list(N)"
,"AList:Collections.list(A)"
,"Util:{()"
,"  type method A max(AList that){"
//,"    if that.isEmpty() (error S\"Empty lists have no max\")"
//,"    var N candidate=that.top()"
,"    with ei in that.vals() ("
,"        void"
//,"      if ei>candidate (candidate:=ei)"
,"      )"
//,"    return candidate"
,"    return A()"
,"    }"
,"  }"
,"C:{"
,"  list=AList[A();]"
,"  if Util.max(list).m(v:void) (return ExitCode.normal())"
,"  return ExitCode.failure()"
,"  }"
,"}"
);
  try{
    FinalResult res = L42.runSlow(null,s);
    System.out.println(L42.record.toString());
    if(res.getResult()!=0){
      System.out.println(res);
      TestHelper.reportError(null);
      }
    }
  catch(ErrorMessage msg){
    System.out.println(L42.record.toString());
    msg.printStackTrace();
    TestHelper.reportError(msg);
    }
  }

@Test
public void test16() throws IOException{
  String s=TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"NList:Collections.list(N)"
,"Util:{()"
,"  type method N max(NList that){"
,"    if that.isEmpty() (error S\"Empty lists have no max\")"
,"    var N candidate=that.top()"
,"    with ei in that.vals() ("
,"      if ei>candidate (candidate:=ei)"
,"      )"
,"    return candidate"
,"    }"
,"  }"
,"C:{"
,"  list=NList[3N;]"
,"  if Util.max(list)==3N (return ExitCode.normal())"
,"  return ExitCode.failure()"
,"  }"
,"}"
);
  try{
    FinalResult res = L42.runSlow(null,s);
    System.out.println(L42.record.toString());
    if(res.getResult()!=0){
      System.out.println(res);
      TestHelper.reportError(null);
      }
    }
  catch(ErrorMessage msg){
    System.out.println(L42.record.toString());
    msg.printStackTrace();
    TestHelper.reportError(msg);
    }
  }


@Test
public void test17() throws IOException{
  String s=TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"NList:Collections.list(N)"
,"Util:{()"
,"  type method N max(NList that){"
,"    if that.isEmpty() (error S\"Empty lists have no max\")"
,"    var N candidate=that.top()"
,"    with ei in that.vals() ("
,"      if ei>candidate (candidate:=ei)"
,"      )"
,"    return candidate"
,"    }"
,"  }"
,"C:{"
,"  list=NList[2N;0N;2N;0N;1N;3N;2N;0N;2N;0N;2N;0N;2N;0N;]"
,"  if Util.max(list)==3N (return ExitCode.normal())"
,"  return ExitCode.failure()"
,"  }"
,"}"
);
  try{
    FinalResult res = L42.runSlow(null,s);
    System.out.println(L42.record.toString());
    if(res.getResult()!=0){
      System.out.println(res);
      TestHelper.reportError(null);
      }
    }
  catch(ErrorMessage msg){
    System.out.println(L42.record.toString());
    msg.printStackTrace();
    TestHelper.reportError(msg);
    }
  }



@Test
public void test18() throws IOException{
  String s=TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"SList:Collections.list(S)"
,"Util:{()"
,"  type method S max(SList that){"
,"    if that.isEmpty() (error S\"Empty lists have no max\")"
,"    var S candidate=S\"\""
,"    with ei in that.vals() ("
,"      if ei.size()>candidate.size() (candidate:=ei)"
,"      )"
,"    return candidate"
,"    }"
,"  }"
,"C:{"
,"  list=SList[S\"a\";S\"bb\";S\"ccc\";S\"dd\";]"
,"  if Util.max(list)!=S\"ccc\" (return ExitCode.failure())"
,"  top=S\"ccc\"(0N)"
,"  if top!=S\"c\" (return ExitCode.failure())"
,"  return ExitCode.normal()"
,"  }"
,"}"
);
try{
  FinalResult res = L42.runSlow(null,s);
  System.out.println(L42.record.toString());
  if(res.getResult()!=0){
    System.out.println(res);
    TestHelper.reportError(null);
    }
  }
catch(ErrorMessage msg){
  System.out.println(L42.record.toString());
  msg.printStackTrace();
  TestHelper.reportError(msg);
  }
}


}