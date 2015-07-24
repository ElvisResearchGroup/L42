package testAux;


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

import facade.L42;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Expression;

public class TestParseAndDesugar {

  @RunWith(Parameterized.class)
  public static class Test1 {
    @Parameter(0) public String _p1;
    @Parameter(1) public String _p2;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
   {"a","a"
  },{" ( void catch exception x ( default x) void )",
 " ("
+"  Void unused=void"
//+"  catch exception x (on Any x)"//ok done in the method
+"  catch exception x ( default x)"
+"  void"
+"  )"
  },{" { method () ( void catch exception x ( default x) void ) }",
" {"
+"method #apply() ("
+"  Void unused=void"
//+"  catch exception x (default x)"
+"  catch exception x (on Any x)"
+"  void"
+"  )}"
  },{" { method Foo () ( void catch exception x ( on Void x  default x) void )}",
 " { method Outer0::Foo #apply() ("
+"  Void unused=void"
+"  catch exception x ("
+"    on Void x"
+"    on Any x"
+"    ) void )}"
  },{" { method Foo () { void catch return x ( default x) return void } }",
    " { method Outer0::Foo #apply() ( Void unused=( Void unused0=void"
+"     catch return x (  on Outer0::Foo x )  (  Void unused1=return void void ) )"
+"     catch return result ( on Outer0::Foo result ) error {'@stringU\n'CurlyBlock-Should be unreachable code\n } )}"
  },{" { method Foo () ( Bar b={void catch return x ( default x) void } b) }",
    " { method Outer0::Foo #apply() ( Outer0::Bar b=( Void unused=( Void unused0=void"
+"      catch return x ( on Outer0::Bar x ) ( Void unused1=void  void ) )"
+"      catch return result ( on Outer0::Bar result ) error {'@stringU\n'CurlyBlock-Should be unreachable code\n } ) b )}"
  },{"with var x in void ( on Void {a()})",
 " ( Void x=void  (  Void unused=(  Void unused0=(  Void unused1=loop ( Void unused2=x.#next()"
 + "  catch exception unused3 ( on Void (  Void unused4=( Void unused5=x.#checkEnd()"
 + "    catch exception unused6 ( on Void void )  void  ) exception void ) )"
 +"      ((  Void x0=( Void unused7=return x.#inner()  catch return casted ("
 +"         on Void casted                      "
 +"         on Any exception void ) error {'@stringU\n'CastT-Should be unreachable code\n })"
 +"      catch exception unused8 ( on Void void )"
 +"    ( Void unused9={  type method  Outer0 a()}  void ) )) )"
 +"  catch exception unused10 ( on Void void ) void )"
 +"  catch exception propagated (default ( Void unused11=x.#close() exception propagated))"
 +"      void) catch return propagated0 (default ( Void unused12=x.#close() return propagated0 ))"
 +"    x.#close() ) )"
},{" (var x=void x:=void)",
    " ( Varx:{mut (var Void inner)}  Void x=void"
   +"   mut Outer0::Varx varx=Outer0::Varx.#apply(inner:x)"
   +"   varx.inner(that:void) )"
//},{"with var x in void ( x:=void)",
//},{"with var x in {a()}, y in {b()} ( x+=y+y)",//why y++y do not work?
},{"with x=void (on Void void)",
   " ( Void x=void ("
   + " Void x0=("
   + "   Void unused=return x"
   + "   catch return casted ( on Void casted on Any exception void )"
   + "   error {'@stringU\n'CastT-Should be unreachable code\n } )"
   + " catch exception unused0 ( on Void void )"
   +" ( Void unused1=void void ) ) )"
},{"with x=void (on Library loop void)",
  " ( Void x=void ("
  + " Library x0=("
  + "   Void unused=return x"
  + "   catch return casted ( on Library casted on Any exception void )"
  + "   error {'@stringU\n'CastT-Should be unreachable code\n } )"
  + " catch exception unused0 ( on Void void )"
  +" ( Void unused1=loop void void ) ) )"
},{"with x=void y={'a\n} ("
 + "  on Library Library loop void"
 + "  on Void Void return Any"
 + "  on Any Any {'b\n}"
 + "  default {'c\n}"
 + "  )"," "
+"("
+"  Void x=void"
+"  Library y={ 'a\n}"
+"  ( Library x2=("
+"      Void unused=return x"
+"      catch return casted3 ( on Library casted3 on Any exception void )"
+"      error {'@stringU\n'CastT-Should be unreachable code\n }"
+"      )"
+"    Library y2=("
+"      Void unused0=return y"
+"      catch return casted4 ( on Library casted4 on Any exception void )"
+"      error {'@stringU\n'CastT-Should be unreachable code\n }"
+"      )"
+"    catch exception unused1 ( on Void ("
+"        Void x1=("
+"          Void unused2=return x"
+"          catch return casted1 ( on Void casted1 on Any exception void )"
+"          error {'@stringU\n'CastT-Should be unreachable code\n }"
+"          )"
+"        Void y1=("
+"          Void unused3=return y"
+"          catch return casted2 ( on Void casted2 on Any exception void )"
+"          error {'@stringU\n'CastT-Should be unreachable code\n }"
+"          )"
+"        catch exception unused4 ("
+"          on Void ("
+"            Any x0=("
+"              Void unused5=return x"
+"              catch return casted ( on Any casted on Any exception void )"
+"              error {'@stringU\n'CastT-Should be unreachable code\n }"
+"              )"
+"            Any y0=("
+"              Void unused6=return y"
+"              catch return casted0 ( on Any casted0 on Any exception void )"
+"              error {'@stringU\n'CastT-Should be unreachable code\n }"
+"              )"
+"            catch exception unused7 ("
+"              on Void { 'c\n}"
+"              )"
+"            ( Void unused8={ 'b\n} void )"
+"            )"
+"          )"
+"        ( Void unused9=return Any void ) )"
+"      )"
+"    ( Void unused10=loop void void ) )"
+"  )"

},{"with x=void y={'a\n} ("
    + "  on Library Library case x(y) loop void"
    + "  on Any Any case y(x) {'b\n}"//works also for just case y(x) {b()}, I may want to add another test?
    + "  )"," "
+"     ( Void x=void"
+"        Library y={ 'a\n}"
+"        ( Void unused=("
+"            Library x2=("
+"              Void unused0=return x"
+"              catch return casted3 ( on Library casted3 on Any exception void )"
+"              error {'@stringU\n'CastT-Should be unreachable code\n }"
+"              )"
+"            Library y2=("
+"              Void unused1=return y"
+"              catch return casted4 (on Library casted4 on Any exception void )"
+"              error {'@stringU\n'CastT-Should be unreachable code\n }"
+"              )"
+"            catch exception unused2 (on Void void )"
+"            ("
+"              Void unused3=("
+"                Library::#apply(that ) cond=x2.#apply(that:y2)"
+"                ( Void unused4=cond.#checkTrue()"
+"                  catch exception unused5 ( on Void exception void )"
+"                  void"
+"                  )"
+"                )"
+"              void"
+"              )"
+"            )"
+"          Library x1=("
+"            Void unused6=return x"
+"            catch return casted1 ( on Library casted1 on Any exception void )"
+"            error {'@stringU\n'CastT-Should be unreachable code\n }"
+"            )"
+"          Library y1=("
+"            Void unused7=return y"
+"            catch return casted2 ( on Library casted2 on Any exception void )"
+"            error {'@stringU\n'CastT-Should be unreachable code\n }"
+"            )"
+"          catch exception unused8 ("
+"            on Void ("
+"              Void unused9=("
+"                Any x3=("
+"                  Void unused10=return x"
+"                  catch return casted5 ( on Any casted5 on Any exception void )"
+"                  error {'@stringU\n'CastT-Should be unreachable code\n }"
+"                  )"
+"                Any y3=("
+"                  Void unused11=return y"
+"                  catch return casted6 ( on Any casted6 on Any exception void )"
+"                  error {'@stringU\n'CastT-Should be unreachable code\n }"
+"                  )"
+"                catch exception unused12 ( on Void void )"
+"                ( Void unused13=("
+"                    Library::#apply(that ) cond0=y.#apply(that:x)"
+"                    ( Void unused14=cond0.#checkTrue()"
+"                      catch exception unused15 ( on Void exception void )"
+"                      void"
+"                      )"
+"                    )"
+"                  void"
+"                  )"
+"                )"
+"              Any x0=("
+"                Void unused16=return x"
+"                catch return casted ( on Any casted on Any exception void )"
+"                error {'@stringU\n'CastT-Should be unreachable code\n }"
+"                )"
+"              Any y0=("
+"                Void unused17=return y"
+"                catch return casted0 ( on Any casted0 on Any exception void )"
+"                error {'@stringU\n'CastT-Should be unreachable code\n }"
+"                )"
+"              catch exception unused18 ( on Void void )"
+"              ( Void unused19={ 'b\n} void )"
+"              )"
+"            )"
+"          ( Void unused20=loop void void )"
+"          )"
+"        )"
},{"A*b","Outer0::A.#times(that:b)"
},{" ( T a=b T b=c catch error x (on Foo x) T a2=b2 T b2=c2 c )"," ( Outer0::T a=b Outer0::T b=c catch error x ( on Outer0::Foo x   )  (   Outer0::T a2=b2 Outer0::T b2=c2  c  ) )"
},{" (A*b a b c )"," ( Void unused=Outer0::A.#times(that:b) Void unused0=a Void unused1=b c )"
},{" (T a=b c=a c )"," (Outer0::T a=b Outer0::T c=a c )"
},{" (var Outer0::T a=a+c c=a a:=C(a) fuffa c )",//ok outer 0 can not be desugared since there is not outer nested class.
   " (Vara:{mut (var Outer1::T inner)}"
  +"  Outer0::T a=a.#plus(that:c)"
  +"  Outer0::T c=a"
  +"  mut Outer0::Vara vara=Outer0::Vara.#apply(inner:a)"
  +"  Void unused=vara.inner(that:Outer0::C.#apply(that:vara.#inner()))"
  +"  Void unused0=fuffa"
  +"  c)"
},{" (var Outer0::T a=a+c c=a Fuffa(a:=a(a)) c )",//ok outer 0 can not be desugared since there is not outer nested class.
  " ("
  +" Vara:{mut (var Outer1::T inner)}"
  +" Outer0::T a=a.#plus(that:c)"
  +" Outer0::T c=a"
  +" mut Outer0::Vara vara=Outer0::Vara.#apply(inner:a)"
  +" Void unused=Outer0::Fuffa.#apply(that:vara.inner(that:vara.#inner().#apply(that:vara.#inner())))"
  +"c"
  +")"
},{" (T a=b (c=a c ))"," (Outer0::T a=b (Outer0::T c=a c ))"
},{" (T a=b c=a.m() c )"," (Outer0::T a=b Outer0::T::m() c=a.m() c )"
},{" (T a=b (c=a.m() c ))"," (Outer0::T a=b (Outer0::T::m() c=a.m() c ))"
},{" (T a=void a*b a+b catch error x (on Foo x.bar()) a b c )",
   " (Outer0::T a=void  Void unused=a.#times(that:b)   Void unused0=a.#plus(that:b)  catch error x (    on Outer0::Foo x.bar()    )  (    Void unused1=a    Void unused2=b    c  ) )"
},{" (T a=void a+=a +a)",
   " (Outer0::T a=void a.inner(that:a.#inner().#plus(that:a.#plus(that:a))))"
//TOO UNSTABLE},{"{ reuse L42.is/base }","{Bool:{interface type method Void #checkTrue() exception Void} True:{ _private()<:Outer1::Bool method #checkTrue() ( void )} False:{ _private()<:Outer1::Bool method #checkTrue() (exception  void )}}"
},{"if X (Bla) else (Foo)",
   " (Void unused=Outer0::X.#checkTrue() catch exception unused0 ( on Void (Outer0::Foo) ) (Outer0::Bla) )"
},{"if X (Bla) ",
   " (Void unused=Outer0::X.#checkTrue() catch exception unused0 ( on Void  void ) (Outer0::Bla) )"
},{"if Foo+Bar (bla) ",
   " ( Outer0::Foo::#plus(that ) cond=Outer0::Foo.#plus(that:Outer0::Bar) ( Void unused=cond.#checkTrue() catch exception unused0 ( on Void  void ) (bla)))"
},{"{ <(T bar) }'bla\n"," ('bla\n{ type method Outer0 #left(Outer0::T^bar'@consistent\n)mut method '@consistent\nOuter0::T #bar()read method '@consistent\nOuter0::T bar()})"
},{"{ (T bar) }"," {type method Outer0 #apply(Outer0::T^bar'@consistent\n) mut method '@consistent\nOuter0::T #bar() read method '@consistent\nOuter0::T bar() }"
},{" (T x={ if A (return B) return C } x)",
   " (Outer0::T x=( Void unused=( Void unused0=( Void unused2=Outer0::A.#checkTrue() catch exception unused3 ( on Void  void ) (return Outer0::B)) Void unused1=return Outer0::C void ) catch return result ( on Outer0::T result ) error  {'@stringU\n'CurlyBlock-Should be unreachable code\n } )x )"
},{"{ Vara: {} method a() (var T a=a+c c=a Fuffa(a:=a(a)) c ) }",
  " {Vara0:{type method \n"
+"mut Outer0 #apply(Outer1::T^inner'@consistent\n"
+") \n"
+"mut method '@consistent\n"
+"Void inner(Outer1::T that) \n"
+"mut method '@consistent\n"
+"Outer1::T #inner() \n"
+"read method '@consistent\n"
+"Outer1::T inner() } Vara:{} method a() ( Outer0::T a=a.#plus(that:c) Outer0::T c=a mut Outer0::Vara0 vara=Outer0::Vara0.#apply(inner:a) Void unused=Outer0::Fuffa.#apply(that:vara.inner(that:vara.#inner().#apply(that:vara.#inner()))) c )}"
},{"{ method Void () Outer0[with b in Outer0 (b)]}","{"
+"Varaccumulator:{type method \n"
+"mut Outer0 #apply(Outer1::#begin() ^inner'@consistent\n"
+")\n"
+"mut method '@consistent\n"
+"Void inner(Outer1::#begin() that)\n"
+"mut method '@consistent\n"
+"Outer1::#begin() #inner()}"
+"method Void #apply() ("
+"  Outer0::#begin() accumulator=Outer0.#begin()"
+"  mut Outer0::Varaccumulator varaccumulator=Outer0::Varaccumulator.#apply(inner:accumulator)"
+"  Void unused=( type Outer0 b=Outer0"
+"    (  Void unused0=( Void unused1=( Void unused2=loop ("
+"       Void unused3=b.#next()"
+"       catch exception unused4 ( on Void ( Void unused5=("
+"         Void unused6=b.#checkEnd() catch exception unused7 ( on Void void  )"
+"         void ) exception void ) )"
+"       varaccumulator.inner(that:varaccumulator.#inner().#add(that:(b.#inner()))) )"
+"       catch exception unused8 ( on Void void ) void  )"
+"       catch exception propagated (  on Any ( Void unused9=b.#close() exception propagated"
+"       )) void ) catch return propagated0 ( on Any ("
+"       Void unused10=b.#close() return propagated0  )  )"
+"      b.#close() ) ) varaccumulator.#inner().#end())}"
},{
  "{reuse L42.is/nanoBasePrivates\n method Void seal() void }",
  "{\n"
+"  I:{interface\n"
+"    method '@private\n"
+"    Void seal0() }\n"
+"    Foo:{<:Outer1::I\n"
+"    type method\n"
+"    Outer0 #apply()\n"
+"    method seal0() this.seal0()\n"
+"    type method '@private\n"
+"    Void bar0() this.seal0()}\n"
+"    Bar:{\n"
+"    type method\n"
+"    Outer0 #apply()\n"
+"    Beer:{\n"
+"    type method\n"
+"    Outer0 #apply() \n"
+"    type method '@private\n"
+"    Void bar1() void}\n"
+"    Bit0:'@private\n"
+"    {\n"
+"    type method \n"
+"    Outer0 #apply() }}\n"//+"    Outer1::Bit0 #apply() }}\n"//Ok, there was a time this looked ok to me... :(
+"    method \n"
+"    Void seal() void}\n"
},{
  "{reuse L42.is/nanoBasePrivates2\n   method  Void gg() void}",
" {\n"
+"type method\n"
+"Outer0 #apply()\n"
+"Foo:{\n"
+"type method '@private\n"
+"Outer0 new0() }\n"
+"Bar:{\n"
+"mut method '@consistent\n"
+"Outer1::Foo #a()\n"
+"read method '@consistent\n"
+"Outer1::Foo a()\n"
+"mut method '@consistent\n"
+"Void b(Outer1::Foo that)\n"
+"mut method '@consistent\n"
+"Outer1::Foo #b() \n"
+"read method '@consistent\n"
+"Outer1::Foo b() \n"
+"method \n"
+"Void #apply() (\n"
+"  Void unused=this.b(that:this.a())\n"
+"  this.b(that:Outer1::Foo.new0())\n"
+"  )\n"
+"type method '@private\n"
+"mut Outer0 new1(Outer1::Foo^a0'@consistent\n"
+", Outer1::Foo^b0'@consistent\n"
+") }\n"
+"method\n"
+"Void gg() void}\n"
},{
    "{reuse L42.is/nanoBasePrivates3\n   C:{method  Void foo() void}}",
  "{"
+" type method "
+" Outer0::foo0() bar() Outer0.foo0()"
+" type method '@private\n"
+" Void foo0() void"
+" C:{"
+" method"
+" Void foo() void}}"

}});}

  @Test
  public void testOk() {
    TestHelper.configureForTest();
    L42.setRootPath(Paths.get("dummy"));
    L42.usedNames.clear();
    Expression es1=Parser.parse(null,_p1);
    Expression es2=Parser.parse(null,_p2);
    Expression result=Desugar.of(es1);
    TestHelper.assertEqualExp(result,es2);
  }
  }
}