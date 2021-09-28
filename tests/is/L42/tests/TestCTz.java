package is.L42.tests;

import static is.L42.tools.General.L;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import is.L42.common.CTz;
import is.L42.common.Program;
import is.L42.flyweight.P;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Op;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.tools.AtomicTest;

public class TestCTz
extends AtomicTest.Tester{
  static ST This=st("This");
  static ST Void=st("Void");
  static ST Library=st("Library");
  static ST Any=st("Any");
  static List<ST> lThis=l(This);
  static List<ST> lVoid=l(Void);
  static List<ST> lLibrary=l(Library);
  static List<ST> lAny=l(Any);
  
  static List<ST> l(ST...stz){return List.of(stz);}
  public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   plusAcc(l(),l(This),"")
   ),new AtomicTest(()->
   plusAcc(l(st(This,"k()")),l(This,st(This,"k()")),
     "This.k()=[This, This.k()]")
   ),new AtomicTest(()->
   plusAcc(l(st(This,"k()")),lThis,
   "This.k()=[This]")

   ),new AtomicTest(()->
   plusAcc(l(st(This,"k(a,b,c)",3)),lThis,
   "This.k(a,b,c).3=[This]")

   ),new AtomicTest(()->
   plusAcc(l(st(This,"k(a,b,c)",3)),l(st(This,"m()")),
   "This.k(a,b,c).3=[Void]")

   ),new AtomicTest(()->
   plusAcc(l(st(This,"m()")),lThis,
   "")
   ),new AtomicTest(()->
   plusAcc(l(st(This,"k(x)")),lThis,
   "")
   ),new AtomicTest(()->
   plusAcc(l(st(st(This,"k(x)"),"m()")),lThis,
   "")
   ),new AtomicTest(()->
   plusAcc(l(st(st(st(This,"k(x)"),"k(x)"),"m()")),lThis,
   "")
   ),new AtomicTest(()->
   plusAcc(l(st(st(This,"k(x)",1),"m()")),lThis,
   "Any.m()=[This]")
   ),new AtomicTest(()->
   plusAcc(
     l(st(Op.GT,l(This),l(This))), lThis,
   ">[This][This]=[This]")
   ),new AtomicTest(()->
   plusAcc(
     l(st(Op.LT,lThis,lThis)), lThis,
   "")

   ),new AtomicTest(()->
   plusAcc(
     l(st(Op.GT,lThis,lThis,lVoid)), lThis,
   ">[This][This][Void]=[This]")

   ),new AtomicTest(()->
   plusAcc(
     l(st(Op.LT,lThis,lThis,lAny)), lThis,
   "<[This][This][Any]=[This]")

   ),new AtomicTest(()->
   plusAcc(
     l(st(Op.LTEqual,lThis,lThis,lAny)), lThis,
   "")

   ),new AtomicTest(()->
   plusAcc(
     l(st(Op.LTEqual,lThis,lThis, lVoid)),  lThis,
   "")
   ),new AtomicTest(()->inferAll(ctz()
   .a(st(This,"nope()"),Void)
   ,"""
   This.nope() = [This.nope(), Void]
   """)
   ),new AtomicTest(()->inferAll(ctz()
   .a(st(This,"nope()"),This).a(st(st(This,"nope()"),"dope()"),Void)
   ,"""
   This.nope() = [This, This.nope()]
   This.nope().dope() = [This.dope(), This.nope().dope(), Void]
   """)
   ),new AtomicTest(()->inferAll(ctz()
   .a(st(This,"nope()"),This)
   .a(st(st(This,"nope()"),"dope()"),Void)
   .a(st(This,"dope()"),Library)
   ,"""
   This.nope() = [This, This.nope()]
   This.nope().dope() = [Library, This.dope(), This.nope().dope(), Void]
   This.dope() = [Library, This.dope()]
   """)
   ),new AtomicTest(()->inferAll(ctz()
   .a(st(This,"nope()"),This)
   .a(st(This,"dope()"),st(st(This,"nope()"),"m()"))
   ,"""
   This.nope() = [This, This.nope()]
   This.dope() = [This.dope(), This.nope().m(), Void]
   """)
//   ),new AtomicTest(()->inferAll(ctz() //types of form This.nope().m() are disabled for now
//   .a(st(This,"nope()"),This)
//   .a(st(This,"dope()"),st(st(st(This,"nope()"),"nope()"),"m()"))
//   ,"""
//   This.nope() = [This, This.nope()]
//   This.dope() = [This.dope(), This.nope().m(), This.nope().nope().m(), Void]
//   """)

//operators         
   ),new AtomicTest(()->inferAll(ctz()
   .a(st(Op.Plus,lThis,lVoid,lVoid),Void)
   .a(st(Op.Plus,lThis,lThis,lThis),Library)
   .a(st(This,"dope()"),st(st(Op.Plus,lThis,lThis,lThis),"nope()"))
   ,"""
   +[This][Void][Void] = [+[This][Void][Void], Void]
   +[This][This][This] = [+[This][This][This], Library]
   This.dope() = [+[This][This][This].nope(), Library.nope(), This.dope()]
   """)
   ),new AtomicTest(()->inferAll(ctz()
   .a(st(This,"cope()"),st(Op.LT,l(st(This,"nope()")),l(st(This,"dope()"))))
   .a(st(This,"nope()"),This)
   .a(st(This,"dope()"),This)
   ,"""
   This.cope() = [<[This.nope()][This.dope()], <[This.nope()][This], <[This][This.dope()], This.cope(), Void]
   This.nope() = [This, This.nope()]
   This.dope() = [This, This.dope()]
   """)

  ));}
static Program p=Program.parse("""
  {
   method Void m()
   method This k(Any x)
   method Void #lt0(This a)
   method Void #lt0(This a,Void b)
   method Void #ltequal0(This a,Any b)
   #norm{typeDep=This}}
  """);
public static ST st(String t){
  P p=P.parse(t);
  return new Core.T(Mdf.Immutable,L(),p);
  }
public static ST st(ST st,String s){return new ST.STMeth(st,S.parse(s),-1);}
public static ST st(String st,String s){return st(st(st),s);}
public static ST st(ST st,String s,int i){return new ST.STMeth(st,S.parse(s),i);}
public static ST st(String st,String s,int i){return st(st(st),s,i);}

@SafeVarargs public static ST st(Op op, List<ST>... stzs){return new ST.STOp(op,List.of(stzs));}

public static class CTzBuilder{
  CTz ctz=new CTz();
  Set<ST> dom=new LinkedHashSet<>();
  CTzBuilder a(ST st, ST... stz){
    ctz.plusAcc(p,L(st),List.of(stz));
    dom.add(st);
    return this;
    }
  }
public static CTzBuilder ctz(){return new CTzBuilder();}
public static void inferAll(CTzBuilder ctzb,String out){
  CTz ctz=ctzb.ctz;
  var is=ctz.allSTz(p);
  String res="";
  for(var st:ctzb.dom){
    res+=st+" = ";
    List<String> sorted=L(is.compute(st).stream().map(e->e.toString()).sorted());
    res+=sorted+"\n";
    }
  res=res.replaceAll("imm ","");
  assertEquals(out,res);
  }
public static void plusAcc(List<ST> stz,List<ST> stz1,String out){
  CTz ctz=new CTz();
  ctz.plusAcc(p, new ArrayList<>(stz),new ArrayList<>(stz1));
  assertEquals(out,ctz.toString());
  }

public static void topFail(Class<?> kind,String program,String ...output){
  //checkFail(()->new Top().top(L(),Program.parse(program)), output, kind);
  }
}