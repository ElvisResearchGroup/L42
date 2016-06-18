package testAux;


import static helpers.TestHelper.lineNumber;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.ErrorMessage;
import ast.ErrorMessage.PreParserError;
import ast.Expression;

public class TestParseAndDoAst {

  @RunWith(Parameterized.class)
  public static class Test1 {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String s;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
  {lineNumber(),"a"
},{lineNumber(),"a*b"
},{lineNumber(),"a*b+c==d&k+a+b*c>g|e"
},{lineNumber(),"a*b+c==d&k+a+b * c >g  | , e"
},{lineNumber(),"a *b"
},{lineNumber(),"a.m()"
},{lineNumber(),"a.m(a)"
},{lineNumber(),"a.m(a:c)"
},{lineNumber(),"a.m(a a:c)"
},{lineNumber(),"a.m(//blah\n a a:c)"
},{lineNumber(),"a.m(//blah\n)"
},{lineNumber(),"a.m(//bla\n a:c,,)"
},{lineNumber(),"a//bla\n"
},{lineNumber(),"a+b//bla\n"
},{lineNumber(),"a()"
},{lineNumber(),"a(a)"
},{lineNumber(),"a(a:c)"
},{lineNumber(),"a(a a:c)"
},{lineNumber(),"a(//blah\n a a:c)"
},{lineNumber(),"a(//blah\n)"
},{lineNumber(),"a(//bla\n a:c,,)"
},{lineNumber(),"a[]"
},{lineNumber(),"a[a;]"
},{lineNumber(),"a[a:c;]"
},{lineNumber(),"a[a a:c;]"
},{lineNumber(),"a[//blah\n a a:c;]"
},{lineNumber(),"a[a a:c;//bleh\n]"
},{lineNumber(),"a[//blah\n a a:c;//bleh\n]"
},{lineNumber(),"a[//blah\n]"
},{lineNumber(),"a[//bla\n a:c,;,]"
},{lineNumber()," (a)"
},{lineNumber()," (a a)"
},{lineNumber()," (a b c)"
},{lineNumber()," ( a = b c)"
},{lineNumber()," ( C a = b c)"
},{lineNumber()," ( C a = b, D b=c c)"
},{lineNumber()," ( C.D b=c c)"
},{lineNumber()," ( This2.D b=c c)"
},{lineNumber()," ( Any b=c c)"
},{lineNumber()," ( mut Any b=c c)"
},{lineNumber()," ( class Any b=c c)"
},{lineNumber()," ( fwd class Any b=c c)"
},{lineNumber()," if a (b)"
},{lineNumber()," if a (b) else e"
},{lineNumber()," while a (b)"
},{lineNumber()," error a"
},{lineNumber()," exception a"
},{lineNumber()," return a"
},{lineNumber()," ( a catch error Any x (foo) a)"
},{lineNumber()," ( a=e catch error Any x (foo) catch error Bar x fee a)"
},{lineNumber()," ( a catch error Any x (foo) b c d a)"
},{lineNumber()," ( a catch error Any x (foo) b c catch error Any x (foo) d a)"
},{lineNumber()," {a}"
},{lineNumber()," {a a}"
},{lineNumber()," {a b c}"
},{lineNumber()," { a = b c}"
},{lineNumber()," { a = b c=d}"
},{lineNumber()," { C a = b c}"
},{lineNumber()," { C a = b, D b=c c}"
},{lineNumber()," { a catch error Any x (foo) a}"
},{lineNumber()," { a catch error Any x (foo)}"
},{lineNumber()," { a=e catch error Any x (foo) catch error Bar x fee}"
},{lineNumber()," { a=e catch error Any x foo catch error Bar x (fee) a}"
},{lineNumber()," { a catch error Any x (foo) b c d a}"
},{lineNumber()," { a catch error Any x (foo) b c catch error Any x (foo)}"
},{lineNumber()," { var C.B a=d catch error Any x (foo) b c d a}"
},{lineNumber()," ( A: x+y  n)"
},{lineNumber()," { var C.B a=d A: x+y  n w catch error Any x (foo) b c d a}"
},{lineNumber()," {x( A f )}"
},{lineNumber()," {x( A f B f2)}"
},{lineNumber()," {( A f B f2)}"
},{lineNumber()," {}"
},{lineNumber()," { }"
},{lineNumber()," {()}"
},{lineNumber()," {interface}"
},{lineNumber()," { implements  A.B, C}"
},{lineNumber(),"with x (on T y)"
},{lineNumber(),"with x (on T y on T case e z)"
},{lineNumber(),"with x (on T y default e+e)"
},{lineNumber(),"with x y z (on T y)"
},{lineNumber(),"with x=a var y=b+c() z=t (on T y)"
},{lineNumber(),"with x in a var y in b+c() z=t (on T y)"
},{lineNumber(),"with a b c x in a var y in b+c() z=t (on T y)"
},{lineNumber(),"with x in a (a+b*c)"
},{lineNumber(),"a[with x in a (use[a+b*c])]"
},{lineNumber(),"{ method a(b c) {d e f}}"
},{lineNumber(),"{ class method T () (e)}"
},{lineNumber(),"{ class method T #a(T b) exception C (e)}"
},{lineNumber(),"{ class method T #a(T b) exception C ^## C.D (e)}"
},{lineNumber(),"{ class method T #a(T b) exception C}"
},{lineNumber(),"{ class method T #a(T b) }"
},{lineNumber(),"{ class method T #a(T b) ##field}"
},{lineNumber(),"{C: e}"
},{lineNumber(),"{C: ...}"
},{lineNumber(),"use Foo.BAr check time(limit:soFoo) b+a"
},{lineNumber(),"use Foo.BAr check time(//bla bla \n limit:soFoo) b+a"
},{lineNumber(),"Foo\"bla\""
  //},{"(a+b)//bla\n"
}});}

  @Test
  public void testOk() {
    //Object o=new ast.ExpCore.Signal();
    Parser.parse(null,s);
  }
  }
  @RunWith(Parameterized.class)
  public static class Test2 {
    @Parameter(0) public String s;
    @Parameter(1) public String expected;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
{"a","a"
},{"5N","Expression.Literal(receiver=N, inner=5, isNumber=true)"
},{"5-5N","Expression.Literal(receiver=N, inner=5-5, isNumber=true)"
},{"a-5N","(a-Expression.Literal(receiver=N, inner=5, isNumber=true))"
},{" (a (5N) e)","Expression.RoundBlock(doc=, inner=e, contents=[Expression.BlockContent(decs=[Ast.VarDecE(inner=a), Ast.VarDecE(inner=Expression.RoundBlock(doc=, inner=Expression.Literal(receiver=N, inner=5, isNumber=true), contents=[]))], _catch=[])])"
},{"a*b","(a*b)"
},{"a*b*c","((a*b)*c)"
},{"a*b*c*d","(((a*b)*c)*d)"
},{"a**b","(a**b)"
},{"a**b**c","(a**(b**c))"
},{"a**b**c**d","(a**(b**(c**d)))"
},{"a**b**c**d**e","(a**(b**(c**(d**e))))"
},{"a**b**c**d**e**f","(a**(b**(c**(d**(e**f)))))"
},{"a<<b<<c<<d<<e","(a<<(b<<(c<<(d<<e))))"
},{"a<<b<<c<<d<<e<<f","(a<<(b<<(c<<(d<<(e<<f)))))"
//},{"a+ b -c","(a)" is this not giving error since it is captured later? how?
},{"a*b < c+d","((a*b)<(c+d))"

},{" (a=b c)","Expression.RoundBlock(doc=, inner=c, contents=[Expression.BlockContent(decs=[Ast.VarDecXE(isVar=false, t=Optional.empty, x=a, inner=b)], _catch=[])])"
},{" (var a=b c)","Expression.RoundBlock(doc=, inner=c, contents=[Expression.BlockContent(decs=[Ast.VarDecXE(isVar=true, t=Optional.empty, x=a, inner=b)], _catch=[])])"
},{" (A::b() a=b c)","Expression.RoundBlock(doc=, inner=c, contents=[Expression.BlockContent(decs=[Ast.VarDecXE(isVar=false, t=Optional[Ast.HistoricType(path=A, selectors=[Ast.MethodSelectorX(ms=b(), x=)], forcePlaceholder=false)], x=a, inner=b)], _catch=[])])"
//15
},{"{a(A a)}","Expression.ClassB(doc1=, doc2=, h=Ast.ConcreteHeader(mdf=Immutable, name=a, fs=[Ast.FieldDec(isVar=false, t=Immutable[A], name=a, doc=)]), fields=[], supertypes=[], ms=[], stage=None)"
},{"{(A a)}","Expression.ClassB(doc1=, doc2=, h=Ast.ConcreteHeader(mdf=Immutable, name=, fs=[Ast.FieldDec(isVar=false, t=Immutable[A], name=a, doc=)]), fields=[], supertypes=[], ms=[], stage=None)"
},{"{ method a(b c) {d e f}}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), fields=[], supertypes=[], ms=[Expression.ClassB.MethodImplemented(doc=, s=a(b,c), inner=Expression.CurlyBlock(doc=, contents=[Expression.BlockContent(decs=[Ast.VarDecE(inner=d), Ast.VarDecE(inner=e), Ast.VarDecE(inner=f)], _catch=[])]))], stage=None)"
},{"if a (b) else c.h()","Expression.If(cond=a, then=Expression.RoundBlock(doc=, inner=b, contents=[]), _else=Optional[Expression.MCall(receiver=c, name=h, doc=, ps=Ast.Parameters(e=Optional.empty, xs=[], es=[]))])"
},{"Foo\"bla\"","Expression.Literal(receiver=Foo, inner=bla, isNumber=false)"
},{"Foo\"  bla  \"","Expression.Literal(receiver=Foo, inner=  bla  , isNumber=false)"

//22
},{"Foo\"\n  'bla\n  , \"","Expression.Literal(receiver=Foo, inner=bla\n, isNumber=false)"
//23
},{"Foo\"\n  'bla\n 'ble\n , \"","Expression.Literal(receiver=Foo, inner=bla\nble\n, isNumber=false)"

},{" { }","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), fields=[], supertypes=[], ms=[], stage=None)"
},{" {()}","Expression.ClassB(doc1=, doc2=, h=Ast.ConcreteHeader(mdf=Immutable, name=, fs=[]), fields=[], supertypes=[], ms=[], stage=None)"
},{" {mut ()}","Expression.ClassB(doc1=, doc2=, h=Ast.ConcreteHeader(mdf=Mutable, name=, fs=[]), fields=[], supertypes=[], ms=[], stage=None)"
},{" {//aa\n mut foo()//bb\n}","Expression.ClassB(doc1=aa\n, doc2=bb\n, h=Ast.ConcreteHeader(mdf=Mutable, name=foo, fs=[]), fields=[], supertypes=[], ms=[], stage=None)"
},{" {interface}","Expression.ClassB(doc1=, doc2=, h=Ast.InterfaceHeader(), fields=[], supertypes=[], ms=[], stage=None)"
},{" { implements  A.B, C}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), fields=[], supertypes=[A.B, C], ms=[], stage=None)"
},{" {//foo\n implements  A.B, C //bar\n}","Expression.ClassB(doc1=foo\n, doc2=bar\n, h=Ast.TraitHeader(), fields=[], supertypes=[A.B, C], ms=[], stage=None)"
},{" {//foo\n implements  A.B, C }","Expression.ClassB(doc1=foo\n, doc2=, h=Ast.TraitHeader(), fields=[], supertypes=[A.B, C], ms=[], stage=None)"
},{" { implements  A.B, C //bar\n}","Expression.ClassB(doc1=, doc2=bar\n, h=Ast.TraitHeader(), fields=[], supertypes=[A.B, C], ms=[], stage=None)"
},{"{ method Any a()}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), fields=[], supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=, ms=a(), mt=Ast.MethodType(docExceptions=, mdf=Immutable, ts=[], tDocs=[], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ method //a\nAny a()}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), fields=[], supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=a\n, ms=a(), mt=Ast.MethodType(docExceptions=, mdf=Immutable, ts=[], tDocs=[], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ method Any a()//b\n}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), fields=[], supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=, ms=a(), mt=Ast.MethodType(docExceptions=b\n, mdf=Immutable, ts=[], tDocs=[], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ method //a\nAny a()//b\n}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), fields=[], supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=a\n, ms=a(), mt=Ast.MethodType(docExceptions=b\n, mdf=Immutable, ts=[], tDocs=[], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ method Any a(T x, C y, B z)}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), fields=[], supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=, ms=a(x,y,z), mt=Ast.MethodType(docExceptions=, mdf=Immutable, ts=[Immutable[T], Immutable[C], Immutable[B]], tDocs=[, , ], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ method Any a(T x, C y//foo\n,,,,,,,,,,,,, B z)}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), fields=[], supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=, ms=a(x,y,z), mt=Ast.MethodType(docExceptions=, mdf=Immutable, ts=[Immutable[T], Immutable[C], Immutable[B]], tDocs=[, foo\n, ], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ (A a //foo\n)}","Expression.ClassB(doc1=, doc2=, h=Ast.ConcreteHeader(mdf=Immutable, name=, fs=[Ast.FieldDec(isVar=false, t=Immutable[A], name=a, doc=foo\n)]), fields=[], supertypes=[], ms=[], stage=None)"
},{" (a )(b,x:c)","Expression.FCall(receiver=Expression.RoundBlock(doc=, inner=a, contents=[]), doc=, ps=Ast.Parameters(e=Optional[b], xs=[x], es=[c]))"
},{" a //bar\n ","Expression.DocE(inner=a, doc=bar\n)"
},{"with x in a var y in b+c() z=t (on T y)","Expression.With(xs=[], is=[Ast.VarDecXE(isVar=false, t=Optional.empty, x=x, inner=a), Ast.VarDecXE(isVar=true, t=Optional.empty, x=y, inner=(b+Expression.FCall(receiver=c, doc=, ps=Ast.Parameters(e=Optional.empty, xs=[], es=[]))))], decs=[Ast.VarDecXE(isVar=false, t=Optional.empty, x=z, inner=t)], ons=[Expression.With.On(ts=[Immutable[T]], inner=y)], defaultE=Optional.empty)"
},{"S\"a//bla\"\n","Expression.Literal(receiver=S, inner=a//bla, isNumber=false)"
}});}
  @Test
  public void testOkToString() {
    Expression x = Parser.parse(null,s);
    //x.accept(new Desugar()).accept(new InjectionOnCore());
    Assert.assertEquals(x.toString(), expected);
    }
  }
  @RunWith(Parameterized.class)
  public static class Test3 {
    @Parameter(0) public String s;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
  {"a //@bla"
  },{"a \"ff"
  },{"a b"
  },{" ( )"
}});}
  @Test
  public void testNotOk() {
    try{
      Expression e1 = Parser.parse(null,s);
      String rep=e1.toString();
      Assert.assertEquals(rep+"\n"+s,"");
      }
    catch(ParseCancellationException e){}
    catch(IllegalArgumentException e){}
    catch(ErrorMessage.UnclosedParenthesis e){}
    }

  }

@RunWith(Parameterized.class)
  public static class TestBalancedPar {
    @Parameter(0) public String s;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
  {"a"
  },{"a \"aa\""
  },{"a(b)"
  },{" ( (a[]))"
  },{"a /*a*/ "
  },{"{ class method Library traitUnit() {  } } "
}});}
  @Test
  public void test() {
      Parser.checkForBalancedParenthesis(s);
      Expression e1 = Parser.parse(null,s);
      String rep=e1.toString();
    }
  }

  @RunWith(Parameterized.class)
  public static class TestUnBalancedPar {
    @Parameter(0) public String s;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
  {"a)"
  },{"a{{} \"aa\""
  },{"a(b"
  },{" ( a[]))"
  },{"a( /*a*/ "
  },{"{ class method Library traitUnit() {  } } }"
}});}
  @Test(expected=PreParserError.class)
  public void test() {
      Parser.checkForBalancedParenthesis(s);
    }
  }
}
