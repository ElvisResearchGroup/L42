package testAux;


import static helpers.TestHelper.lineNumber;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import helpers.TestHelper;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import facade.L42;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.DesugarVars;
import sugarVisitors.InjectionOnCore;
import sugarVisitors.ToFormattedText;
import ast.ExpCore;
import ast.Expression;
import auxiliaryGrammar.Functions;
import coreVisitors.InjectionOnSugar;

public class TestParseAndDesugar {
/*
  @RunWith(Parameterized.class)
  public static class TestDesugarVars {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String _p1;
    @Parameter(2) public String _p2;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
   {lineNumber(), "a","a"
 },{lineNumber(), " ( a=void a)"," ( a=void a)"
 },{lineNumber(), " ( var Void a=void a)"," ("
  +" Vara_$_1:\n {mut (var Void inner)} "
  +" Void a=void "
  +" mut This0.Vara_$_1 vara_$_=This0.Vara_$_1.#apply(inner:a) "
  +" vara_$_.#inner() "
  +" )"
 },{lineNumber(), "  (   var Library lib={} (x={} lib:=x)  (x={ } lib:=x  ) lib   )",
   " (  Varlib_$_1:\n  {mut (var Library inner)}"
  +" Library lib={}"
  +" mut This0.Varlib_$_1 varlib_$_=This0.Varlib_$_1.#apply(inner:lib) "
  +" (  x={}   varlib_$_:=x   ) "
  +" (  x={}   varlib_$_:=x  ) "
  +" varlib_$_.#inner() "
  +" )"
   }});}
      @Test
      public void testDesugarVars() {
        TestHelper.configureForTest();
        L42.resetFreshPrivate();
        L42.setRootPath(Paths.get("dummy"));
        L42.usedNames.clear();
        Expression es1=Parser.parse(null,_p1);
        Expression es2=Parser.parse(null,_p2);
        Expression result=DesugarVars.of(L42.usedNames,es1);
        TestHelper.assertEqualExp(result,es2);
        }

}
  */
  
  @RunWith(Parameterized.class)
  public static class Test1 {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String _p1;
    @Parameter(2) public String _p2;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
   {lineNumber(), "a","a"
   },{lineNumber(), " This","This0"
   /*},{lineNumber(), " (var Void x=void x:=void)",
    " ( Varx_$_2:\n{mut (var Void inner)}  Void x=void"
   +"   mut This0.Varx_$_2 varx_$_=This0.Varx_$_2.#apply(inner:x)"
   +"   varx_$_.inner(that:void) )"
*/
},{lineNumber(), " (A +B)*C"," (This0.A.#plus(that:This0.B)).#times(that:This0.C)"
  },{lineNumber(), " (6Meter +A)*B"," (This0.Meter.#from(builder:(  b=This0.Meter.#builder() "
  +" Void unused=b.#6()  b  )).#plus(that:This0.A)).#times(that:This0.B)"

},{lineNumber(), " (A + 1B)*C",
  " (This0.A.#plus(that:This0.B.#from(builder:(  b=This0.B.#builder()  Void unused=b.#1()  b  )))).#times(that:This0.C)"

},{lineNumber(), " S\"foo\"",
  " This0.S.#from(builder:("+
  " b=This0.S.#builder()"+
  "Void unused=b.#f()"+
  "Void unused0=b.#o()"+
  "Void unused1=b.#o()"+
  "b"+
  "))"
},{lineNumber(), " S\"fo,o (\"",
  " This0.S.#from(builder:("+
  " b=This0.S.#builder()"+
  "Void unused=b.#f()"+
  "Void unused0=b.#o()"+
  "Void unused1=b.#comma()"+
  "Void unused2=b.#o()"+
  "Void unused3=b.#space()"+
  "Void unused4=b.#oRound()"+
  "b"+
  "))"
},{lineNumber(), "with x=void (on Void void)",
   " (  x=void ("
   + " Void x0=("
   + "   Void unused=return x"
   + "   catch return Void casted casted catch return Any casted exception void "
   + "   error {/*@stringU\nCastT-Should be unreachable code\n*/ } )"
   + " catch exception Void catched1 void "
   +" ( Void unused0=void void ) ) )"
},{lineNumber(), "with x=void (on Library loop void)",
  " ( x=void ("
  + " Library x0=("
  + "   Void unused=return x"
  + "   catch return Library casted casted catch return Any casted exception void "
  + "   error {/*@stringU\nCastT-Should be unreachable code\n*/ } )"
  + " catch exception Void catched1 void "
  +" ( Void unused0=loop void void ) ) )"

},{lineNumber(), "A.m(that: \\foo)","This0.A.m(that:This0.A.foo())"
},{lineNumber(), "B.m(A.m(that: \\foo))","This0.B.m(that:This0.A.m(that:This0.A.foo()))"
},{lineNumber(), "A.m(that: \\foo+void)","This0.A.m(that:This0.A.foo().#plus(that:void))"
},{lineNumber(), "A.m(that: \\+void)","This0.A.m(that:This0.A.#default#m(that:void).#plus(that:void))"
},{lineNumber(), "A(\\+void)","This0.A.#apply(that:This0.A.#default##apply(that:void).#plus(that:void))"
},{lineNumber(), "A[\\a;\\a]",
  //"This0.A.#begin().#add(that:This0.A.a()).#add(that:This0.A.a()).#end()"
  "This0.A.#from(seqBuilder:(  b=This0.A.#seqBuilder()   Void unused=b.#add(that:This0.A.a())   Void unused0=b.#add(that:This0.A.a())     b   ))"
},{lineNumber(), "with var a in b ( a:=c)",
" (a=b ( Void unused=loop ( Void unused0=a.#next()"
+ " catch exception Void catched0 ( Void unused1=("
+ " Void unused2=a.#checkEnd()"
+ " catch exception Void catched1 void void )"
+ "exception void ) "
+ "(a.inner(that:c)) )"
+ " catch exception Void catched3 void void ))"
},{lineNumber(), "with var a in b ( a+=c)",
" (a=b ( Void unused=loop ( Void unused0=a.#next()"
+ " catch exception Void catched0 ( Void unused1=("
+ " Void unused2=a.#checkEnd()"
+ " catch exception Void catched1 void void )"
+ "exception void ) "
+ "(a.inner(that:a.#plus(that:c))) )"
+ " catch exception Void catched3 void void ))"},{lineNumber(), "{method Library(){ method This bar() method This foo(This that) this.foo(\\bar) } }","{method Library #apply(){ method This0 bar() method This0 foo(This0 that) this.foo(that:this.bar()) } }"
},{lineNumber(), "{method Library(){ method This bar() method This foo(This that) this.foo(\\bar+void) } }","{method Library #apply(){ method This0 bar() method This0 foo(This0 that) this.foo(that:this.bar().#plus(that:void)) } }"
  //in this test setting, we can not generate This0.A for method local nested classes introduced desugaring vars, and other stuff :(
  //thus we use a classB literal however
  //},{lineNumber(), "{method foo() A[with x in \\a ( void )]}","would be super long"

},{lineNumber(), "{/*hello*/implements Foo/*hi*/}",
                 "{/*hello*/implements This0.Foo/*hi*/}"
},{lineNumber(), "{method Void foo() exception Bar/*hi*/ Beer/*hello*/ void}",
                 "{method Void foo() exception This0.Bar/*hi*/ This0.Beer/*hello*/ void}"
},{lineNumber(), "{method Void foo() exception Bar/*hi*/ void}",
                 "{method Void foo() exception This0.Bar/*hi*/ void}"
},{lineNumber(), "{method Void foo() exception Bar (void)}",
                 "{method Void foo() exception This0.Bar (void)}"

},{lineNumber(), "A*b","This0.A.#times(that:b)"
},{lineNumber(), "A(b)","This0.A.#apply(that:b)"
},{lineNumber(), " ( Void a=void a(b))"," (Void a=void a.#apply(that:b))"
},{lineNumber(), " ( Void a=void (a)(b) )"," ( Void a=void ( rcv=(a)  rcv.#apply(that:b)))"
},{lineNumber(), " ( T a=b T b=c catch error  Foo x x T a2=b2 T b2=c2 c )"," ( This0.T a=b This0.T b=c catch error This0.Foo x x     (   This0.T a2=b2 This0.T b2=c2  c  ) )"
},{lineNumber(), " (A*b a b c )"," ( Void unused=This0.A.#times(that:b) Void unused0=a Void unused1=b c )"
},{lineNumber(), " (T a=b c=a c )"," (This0.T a=b c=a c )"
/*},{lineNumber(), " (var This0.T a=a+c c=a a:=C(a) fuffa c )",//ok outer 0 can not be desugared since there is not outer nested class.
   " (Vara_$_2:\n{mut (var This1.T inner)}"
  +"  This0.T a=a.#plus(that:c)"
  +"  This0.T c=a"
  +"  mut This0.Vara_$_2 vara_$_=This0.Vara_$_2.#apply(inner:a)"
  +"  Void unused=vara_$_.inner(that:This0.C.#apply(that:vara_$_.#inner()))"
  +"  Void unused0=fuffa"
  +"  c)"*/
/*},{lineNumber(), " (var This0.T a=a+c c=a Fuffa(a:=a.foo(a)) c )",//ok outer 0 can not be desugared since there is not outer nested class.
  " ("
  +" Vara_$_2:\n{mut (var This1.T inner)}"
  +" This0.T a=a.#plus(that:c)"
  +" This0.T c=a"
  +" mut This0.Vara_$_2 vara_$_=This0.Vara_$_2.#apply(inner:a)"
  +" Void unused=This0.Fuffa.#apply(that:vara_$_.inner(that:vara_$_.#inner().foo(that:vara_$_.#inner())))"
  +"c"
  +")"*/
},{lineNumber(), " (T a=b (c=a c ))"," (This0.T a=b ( c=a c ))"
},{lineNumber(), " (T a=b c=a.m() c )"," (This0.T a=b  c=a.m() c )"
},{lineNumber(), " (T a=b (c=a.m() c ))"," (This0.T a=b ( c=a.m() c ))"
},{lineNumber(), " (T a=void a*b a+b catch error Foo x (x.bar()) a b c )",
   " (This0.T a=void  Void unused=a.#times(that:b)   Void unused0=a.#plus(that:b)  catch error This0.Foo x (x.bar()    )  (    Void unused1=a    Void unused2=b    c  ) )"
/*},{lineNumber(), " (T a=void a+=a +a)",
   " (This0.T a=void a.inner(that:a.#inner().#plus(that:a.#plus(that:a))))"*/
//TOO UNSTABLE},{"{ reuse L42.is/base }","{Bool:{interface class method Void #checkTrue() exception Void} True:{ _private() implements This1.Bool method #checkTrue() ( void )} False:{ _private() implements This1.Bool method #checkTrue() (exception  void )}}"
},{lineNumber(), "if X (Bla) else (Foo)",
   " (Void unused=This0.X.#checkTrue() catch exception Void catched (This0.Foo ) (This0.Bla) )"
},{lineNumber(), "if X (Bla) ",
   " (Void unused=This0.X.#checkTrue() catch exception Void catched  void  (This0.Bla) )"
},{lineNumber(), "if Foo+Bar (bla) ",
   " (  cond=This0.Foo.#plus(that:This0.Bar) ( Void unused=cond.#checkTrue() catch exception Void catched0  void  (bla)))"
//},{lineNumber(), "{ <(T bar) }//bla\n"," (//bla\n{ class method This0 #left(This0.T bar)mut method This0.T #bar()read method This0.T bar()})"
//},{lineNumber(), "{ (T bar) }"," {class method This0 #apply(This0.T bar) mut method This0.T #bar() read method This0.T bar() }"
},{lineNumber(), " (T x={ if A (return B) return C } x)",
   " (This0.T x=( Void unused=( Void unused0=( Void unused2=This0.A.#checkTrue() catch exception  Void catched void  (return This0.B)) Void unused1=return This0.C void ) catch return This0.T result result error  { /*@stringU\nCurlyBlock-Should be unreachable code\n*/ } )x )"
  },{lineNumber(),"{ method Any(Any a,Any b) a+ b}",
  "{ method Any #apply(Any a, Any b) a.#plus(that:b)}"
},{lineNumber(),"{ method Any() this!=this &   this}",
  "{ method Any #apply() this.#equalequal(that:this).#bang().#and(that:this)}"
},{lineNumber(),"{ method Any() this==this &   this}",
  "{ method Any #apply() this.#equalequal(that:this).#and(that:this)}"
},{lineNumber(),"{ method Any() this<=this &   this}",
  "{ method Any #apply() ( opNorm=this this.#rightequal(that:opNorm)).#and(that:this)}"
},{lineNumber(),"{ method Any() this+this &   this}",
  "{ method Any #apply() this.#plus(that:this).#and(that:this)}"


},{lineNumber(),"{ method Any m(Any a, Any b) a<><b}",
"{method Any m(Any a, Any b) a.#leftrightleft(that:b)}"
/*},{lineNumber(),"{ method Any m(Any a, Any b) a+=b}",
 "{method Any m(Any a, Any b) "
+"a.inner(that:a.#inner().#plus(that:b))}"*/
/*},{lineNumber(),"{ method Any m(Any a, Any b) a<><=b}",
 "{method Any m(Any a, Any b)"
+"a.inner(that:a.#inner().#leftrightleft(that:b))}"*/
/*},{lineNumber(),"{ method Any m(Any a, Any b) a><>=b}",
 " { method Any m(Any a, Any b)"
+"a.inner(that:("
+ "  opNorm=a.#inner()"
+ "  b.#leftrightleft(that:opNorm)"
+ "  ))}"
*/

}});}

  @Test
  public void testOk() {
    TestHelper.configureForTest();
    L42.resetFreshPrivate();
    L42.setRootPath(Paths.get("dummy"));
    L42.usedNames.clear();
    Expression es1=Parser.parse(null,_p1);
    Expression es2=Parser.parse(null,_p2);
    Expression result=Desugar.of(es1);
    //ExpCore res = result.accept(new InjectionOnCore());
    //result=res.accept(new InjectionOnSugar());
    TestHelper.assertEqualExp(es2,result);
  }
  }


  //TODO: @RunWith(Parameterized.class)
  public static class TestPrivateNormalization {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String _p1;
    @Parameter(2) public String _p2;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
  {lineNumber(),
  "{reuse L42.is/nanoBasePrivates\n  BB:{reuse L42.is/nanoBasePrivates}}",
  "__0_19"
},{lineNumber(),
  "{reuse L42.is/nanoBasePrivates2\n   method  Void gg() void}",  "__0_1"
},{lineNumber(),
    "{reuse L42.is/nanoBasePrivates3\n   C:{method  Void foo() void}}",  "__0_1"
  },{lineNumber(), //whe we fix this test, go in FlatFirstLevel and put back the @private add.
  "{reuse L42.is/nanoBasePrivates4\n   C:{method  Void f1() void C1:{}}}",  "__0_1"

  },{lineNumber(), //whe we fix this test, go in FlatFirstLevel and put back the @private add.
    "{reuse L42.is/NanoBasePrivates5\n AA:{(A a)}}",  "__0_1"

}});}

//----------
//TODO: @Test
  public void testOk() {
    TestHelper.configureForTest();
    L42.resetFreshPrivate();
    L42.setRootPath(Paths.get("dummy"));
    L42.usedNames.clear();
    Expression es1=Parser.parse(null,_p1);
    String res2=(String)_p2;
    Expression result=Desugar.of(es1);
    ExpCore res = result.accept(new InjectionOnCore());
    result=res.accept(new InjectionOnSugar());
    String r1=ToFormattedText.of(res);
    boolean check=r1.contains(res2);
    if(!check){Assert.assertEquals(r1,"We expected it to contain  "+res2);}
    Assert.assertTrue(check);
  }
  }


}