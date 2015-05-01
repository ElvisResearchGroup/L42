package testAux;


import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Expression;

@Test(singleThreaded=true, timeOut = 500)
public class TestParseAndDoAst {
    @DataProvider(name = "termsOk")
    public Object[][] createData1() {
     return new Object[][] {
  {"a"
},{"a*b"
},{"a*b+c==d&k+a+b*c>g|e"
},{"a*b+c==d&k+a+b * c >g  | , e"
},{"a *b"
},{"a.m()"
},{"a.m(a)"
},{"a.m(a:c)"
},{"a.m(a a:c)"
},{"a.m('blah\n a a:c)"
},{"a.m('blah\n)"
},{"a.m('bla\n a:c,,)"
},{"a'bla\n"
},{"a+b'bla\n"
},{"a()"
},{"a(a)"
},{"a(a:c)"
},{"a(a a:c)"
},{"a('blah\n a a:c)"
},{"a('blah\n)"
},{"a('bla\n a:c,,)"
},{"a[]"
},{"a[a;]"
},{"a[a:c;]"
},{"a[a a:c;]"
},{"a['blah\n a a:c;]"
},{"a[a a:c;'bleh\n]"
},{"a['blah\n a a:c;'bleh\n]"
},{"a['blah\n]"
},{"a['bla\n a:c,;,]"
},{" (a)"
},{" (a a)"
},{" (a b c)"
},{" ( a = b c)"
},{" ( C a = b c)"
},{" ( C a = b, D b=c c)"
},{" ( C::D b=c c)"
},{" ( Outer2::D b=c c)"
},{" ( Any b=c c)"
},{" ( shared Any b=c c)"
},{" ( type Any b=c c)"
},{" ( type Any ^ b=c c)"
},{" if a (b)"
},{" if a (b) else e"
},{" while a (b)"
},{" error a"
},{" exception a"
},{" return a"
},{" ( a catch error x (on Any foo) a)"
},{" ( a catch error x (on Any case e foo) a)"
},{" ( a=e catch error x (on Any foo on Bar fee) a)"
},{" ( a catch error x (on Any foo) b c d a)"
},{" ( a catch error x (on Any foo) b c catch error x (on Any foo) d a)"
},{" ( void catch exception x ( default x) void )"
},{" ( void catch exception x ( on Void x  default x) void )"
},{" {a}"
},{" {a a}"
},{" {a b c}"
},{" { a = b c}"
},{" { a = b c=d}"
},{" { C a = b c}"
},{" { C a = b, D b=c c}"
},{" { a catch error x (on Any foo) a}"
},{" { a catch error x (on Any foo)}"
},{" { a=e catch error x (on Any foo on Bar fee)}"
},{" { a=e catch error x (on Any foo on Bar fee) a}"
},{" { a catch error x (on Any foo) b c d a}"
},{" { a catch error x (on Any foo) b c catch error x (on Any foo)}"
},{" { var C::B a=d catch error x (on Any foo) b c d a}"
},{" ( A: x+y  n)"
},{" { var C::B a=d A: x+y  n w catch error x (on Any foo) b c d a}"
},{" {x( A f )}"
},{" {x( A f B f2)}"
},{" {( A f B f2)}"
},{" {}"
},{" { }"
},{" {()}"
},{" {interface}"
},{" {<: A::B, C}"
},{"with x (on T y)"
},{"with x (on T y on T case e z)"
},{"with x (on T y default e+e)"
},{"with x y z (on T y)"
},{"with x=a var y=b+c() z=t (on T y)"
},{"with x in a var y in b+c() z=t (on T y)"
},{"with a b c x in a var y in b+c() z=t (on T y)"
},{"with x in a (a+b*c)"
},{"a[with x in a (a+b*c)]"
},{"{ method a(b c) {d e f}}"
},{"{ type method T () (e)}"
},{"{ type method T #a (T b) exception C (e)}"
},{"{ type method T #a (T b) exception C ^## C::D (e)}"
},{"{ type method T #a (T b) exception C}"
},{"{ type method T #a (T b) }"
},{"{ type method T #a (T b) ##field}"
},{"{C: e}"
},{"{C: ...}"
},{"using Foo::BAr check time(limit:soFoo) b+a"
},{"using Foo::BAr check time('bla bla \n limit:soFoo) b+a"
},{"Foo\"bla\""
  //},{"(a+b)'bla\n"
}};}

  @Test(dataProvider="termsOk")
  public void testOk(String s) {
    //Object o=new ast.ExpCore.Signal();
    Parser.parse(null,s);
  }

  @DataProvider(name = "termsToString")
  public Object[][] createData2() {
   return new Object[][] {
{"a","a"
},{"5N","Expression.Literal(receiver=Ast.Path(rowData=[N]), inner=5, isNumber=true)"
},{"+5-5N","Expression.Literal(receiver=Ast.Path(rowData=[N]), inner=+5-5, isNumber=true)"
},{"a-5N","(a-Expression.Literal(receiver=Ast.Path(rowData=[N]), inner=5, isNumber=true))"
},{" (a (-5N) e)","Expression.RoundBlock(doc=, inner=e, contents=[Ast.BlockContent(decs=[Ast.VarDecE(inner=a), Ast.VarDecE(inner=Expression.RoundBlock(doc=, inner=Expression.Literal(receiver=Ast.Path(rowData=[N]), inner=-5, isNumber=true), contents=[]))], _catch=Optional.empty)])"  
},{"a*b","(a*b)"
},{" (a=b c)","Expression.RoundBlock(doc=, inner=c, contents=[Ast.BlockContent(decs=[Ast.VarDecXE(isVar=false, t=Optional.empty, x=a, inner=b)], _catch=Optional.empty)])"
},{" (var a=b c)","Expression.RoundBlock(doc=, inner=c, contents=[Ast.BlockContent(decs=[Ast.VarDecXE(isVar=true, t=Optional.empty, x=a, inner=b)], _catch=Optional.empty)])"
},{" (A::b() a=b c)","Expression.RoundBlock(doc=, inner=c, contents=[Ast.BlockContent(decs=[Ast.VarDecXE(isVar=false, t=Optional[Ast.HistoricType(path=Ast.Path(rowData=[A]), selectors=[Ast.MethodSelectorX(ms=b, x=)], forcePlaceholder=false)], x=a, inner=b)], _catch=Optional.empty)])"
},{"{a(A a)}","Expression.ClassB(doc1=, doc2=, h=Ast.ConcreteHeader(mdf=Immutable, name=a, fs=[Ast.FieldDec(isVar=false, t=Immutable[A], name=a, doc=)]), supertypes=[], ms=[], stage=None)"
},{"{(A a)}","Expression.ClassB(doc1=, doc2=, h=Ast.ConcreteHeader(mdf=Immutable, name=, fs=[Ast.FieldDec(isVar=false, t=Immutable[A], name=a, doc=)]), supertypes=[], ms=[], stage=None)"
},{"{ method a(b c) {d e f}}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), supertypes=[], ms=[Expression.ClassB.MethodImplemented(doc=, s=a(b,c), inner=Expression.CurlyBlock(doc=, contents=[Ast.BlockContent(decs=[Ast.VarDecE(inner=d), Ast.VarDecE(inner=e), Ast.VarDecE(inner=f)], _catch=Optional.empty)]))], stage=None)"
},{"if a (b) else c.h()","Expression.If(cond=a, then=Expression.RoundBlock(doc=, inner=b, contents=[]), _else=Optional[Expression.MCall(receiver=c, name=h, doc=, ps=Ast.Parameters(e=Optional.empty, xs=[], es=[]))])"
},{"Foo\"bla\"","Expression.Literal(receiver=Ast.Path(rowData=[Foo]), inner=bla, isNumber=false)"
},{"Foo\"  bla  \"","Expression.Literal(receiver=Ast.Path(rowData=[Foo]), inner=  bla  , isNumber=false)"
},{"Foo\"\n  'bla\n  , \"","Expression.Literal(receiver=Ast.Path(rowData=[Foo]), inner=bla\n, isNumber=false)"
},{"Foo\"\n  'bla\n 'ble\n , \"","Expression.Literal(receiver=Ast.Path(rowData=[Foo]), inner=bla\nble\n, isNumber=false)"
},{" { }","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), supertypes=[], ms=[], stage=None)"
},{" {()}","Expression.ClassB(doc1=, doc2=, h=Ast.ConcreteHeader(mdf=Immutable, name=, fs=[]), supertypes=[], ms=[], stage=None)"
},{" {mut()}","Expression.ClassB(doc1=, doc2=, h=Ast.ConcreteHeader(mdf=Mutable, name=, fs=[]), supertypes=[], ms=[], stage=None)"
},{" {'aa\n mut foo()'bb\n}","Expression.ClassB(doc1=aa\n, doc2=bb\n, h=Ast.ConcreteHeader(mdf=Mutable, name=foo, fs=[]), supertypes=[], ms=[], stage=None)"
},{" {interface}","Expression.ClassB(doc1=, doc2=, h=Ast.InterfaceHeader(), supertypes=[], ms=[], stage=None)"
},{" {<: A::B, C}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), supertypes=[Ast.Path(rowData=[A, B]), Ast.Path(rowData=[C])], ms=[], stage=None)"
},{" {'foo\n<: A::B, C 'bar\n}","Expression.ClassB(doc1=foo\n, doc2=bar\n, h=Ast.TraitHeader(), supertypes=[Ast.Path(rowData=[A, B]), Ast.Path(rowData=[C])], ms=[], stage=None)"
},{" {'foo\n<: A::B, C }","Expression.ClassB(doc1=foo\n, doc2=, h=Ast.TraitHeader(), supertypes=[Ast.Path(rowData=[A, B]), Ast.Path(rowData=[C])], ms=[], stage=None)"
},{" {<: A::B, C 'bar\n}","Expression.ClassB(doc1=, doc2=bar\n, h=Ast.TraitHeader(), supertypes=[Ast.Path(rowData=[A, B]), Ast.Path(rowData=[C])], ms=[], stage=None)"
},{"{ method Any a()}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=, ms=a, mt=Ast.MethodType(docExceptions=, mdf=Immutable, ts=[], tDocs=[], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ method 'a\nAny a()}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=a\n, ms=a, mt=Ast.MethodType(docExceptions=, mdf=Immutable, ts=[], tDocs=[], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ method Any a()'b\n}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=, ms=a, mt=Ast.MethodType(docExceptions=b\n, mdf=Immutable, ts=[], tDocs=[], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ method 'a\nAny a()'b\n}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=a\n, ms=a, mt=Ast.MethodType(docExceptions=b\n, mdf=Immutable, ts=[], tDocs=[], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ method Any a(T x, C y, B z)}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=, ms=a(x,y,z), mt=Ast.MethodType(docExceptions=, mdf=Immutable, ts=[Immutable[T], Immutable[C], Immutable[B]], tDocs=[, , ], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ method Any a(T x, C y'foo\n,,,,,,,,,,,,, B z)}","Expression.ClassB(doc1=, doc2=, h=Ast.TraitHeader(), supertypes=[], ms=[Expression.ClassB.MethodWithType(doc=, ms=a(x,y,z), mt=Ast.MethodType(docExceptions=, mdf=Immutable, ts=[Immutable[T], Immutable[C], Immutable[B]], tDocs=[, foo\n, ], returnType=Immutable[Any], exceptions=[]), inner=Optional.empty)], stage=None)"
},{"{ (A a 'foo\n)}","Expression.ClassB(doc1=, doc2=, h=Ast.ConcreteHeader(mdf=Immutable, name=, fs=[Ast.FieldDec(isVar=false, t=Immutable[A], name=a, doc=foo\n)]), supertypes=[], ms=[], stage=None)"
},{" (a )(b,x:c)","Expression.FCall(receiver=Expression.RoundBlock(doc=, inner=a, contents=[]), doc=, ps=Ast.Parameters(e=Optional[b], xs=[x], es=[c]))"
},{" a 'bar\n ","Expression.DocE(inner=a, doc=bar\n)"
},{"with x in a var y in b+c() z=t (on T y)","Expression.With(xs=[], is=[Ast.VarDecXE(isVar=false, t=Optional.empty, x=x, inner=a), Ast.VarDecXE(isVar=true, t=Optional.empty, x=y, inner=(b+Expression.FCall(receiver=c, doc=, ps=Ast.Parameters(e=Optional.empty, xs=[], es=[]))))], decs=[Ast.VarDecXE(isVar=false, t=Optional.empty, x=z, inner=t)], ons=[Ast.On(ts=[Immutable[T]], _if=Optional.empty, inner=y)], defaultE=Optional.empty)"
},{"S\"a'bla\"\n","Expression.Literal(receiver=Ast.Path(rowData=[S]), inner=a'bla, isNumber=false)"
}};}
  @Test(dataProvider="termsToString")
  public void testOkToString(String s,String expected) {
    Expression x = Parser.parse(null,s);
    //x.accept(new Desugar()).accept(new InjectionOnCore());
    Assert.assertEquals(x.toString(), expected);
    }
  @DataProvider(name = "termsNotOk")
  public Object[][] createData3() {
   return new Object[][] {
  {"a '@bla"
  },{"a \"ff"
  },{"a b"
  },{" ( )"
}};}
  @Test(dataProvider="termsNotOk")
  public void testNotOk(String s) {
    try{
      Expression e1 = Parser.parse(null,s);
      String rep=e1.toString();
      Assert.assertEquals(rep+"\n"+s,"");
      }
    catch(ParseCancellationException e){}
    catch(IllegalArgumentException e){}
    }
  
  @DataProvider(name = "termsToCore")
  public Object[][] createData4() {
   return new Object[][] {
  {"a"
},{"a.m(x:a)"
  
//},{"a.m(a)"ok to be wrong
}};}
  @Test(dataProvider="termsToCore")
  public void testOkToCore(String s) {
     Expression x = Parser.parse(null,s);
    ast.ExpCore y=x.accept(new InjectionOnCore());
    Assert.assertEquals(y.toString(),y.toString());
  }

  
  @AfterSuite public void endTest(){
    //assert false;
  }
  
}
