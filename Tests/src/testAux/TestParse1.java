package testAux;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import helpers.TestHelper;


public class TestParse1 {

  @RunWith(Parameterized.class)
  public static class Test1 {
    @Parameter(0) public String s;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
  {"a"
  },{" (Outer0::that() ::#next() x=this.that().#next() x)"
//},{"{'@ExpectedClass :( \n}"//TODO: the ( is turned in \t, and \t is not accepted in comments.
//    more important: should ' be accepted in single line strings?
},{TestHelper.multiLine("{"
        ,"C:{ k() type method Library ok() ({'@OK\n})"
        ,"        type method Library ko() ({'@KO\n})"
        ,"  }"
        ,"I:{interface }"
        ,"AI:{ k()<:Outer1::I}"
        ,"D:("
        ,"  Any z=error Outer0::AI.k()"
        ,"  catch error x (on Outer0::AI Outer0::C.ok()"
        ,"    )"
        ,"  Outer0::C.ko()"
        ,"  )"
        ,"}"),
  },{" ( void catch exception x ( default x) void )"
  },{" ( void catch exception x ( on Void x  default x) void )"
  },{"{ }"
  },{"{ reuse L42.is/nanoBase }"
  },{"{ reuse L42.is/base(2) }"//I test that that is a well formed url
  },{"{ reuse L42.is/nanoBase \n method foo() (void)}"
},{"{ C:{k() type method Library m() ({'@OK\n} )} D:C.m() }"
},{"{outer() C: {new() type method Library m() ({inner()})} D: C.m()}"
},{"{outer() C: {new() type method Library m() ({inner()})} D: ({inner()})}"

},{" (Outer0::C).foo(bar:Outer0::C)"
},{" (type Outer0::C c=(Outer0::C) c.foo(bar:Outer0::C))"
},{" (Outer0::C c=Outer0::C.new() Outer0::D r=Outer0::D.new(x:c) (r).x())"
},{" (Outer0::C c=Outer0::C.new() Outer0::D r=Outer0::D.new(x:c) (Outer0::D d=(r) d.x()))"
},{"void"
},{" (void)"
},{"5N"
},{"-5N"
},{"+5N"
},{"~N"
},{" (~N)"
},{" (-5N)"
},{"a- 5N"
},{"a-5N"
},{" (a (-5N) e)"
},{"a+b"
},{"a*c"
},{"a:=c"
},{"a+=c"
},{"a+=c +a"
},{"a+b<c-d|h"
},{"a + b < c - d | h"
},{"a + b < c - d |, h"
},{"a + (b < c) - d |, h"
},{"a + ( (b < (c))) - d |, h"
},{"a + ((b < (c))) - d |, h"
},{"a.#foo()"
},{"a.foo()"
},{"a .foo()"
},{"a .foo( )"
},{"a .foo(a:b )"
},{"a .foo(a:b b:c)"
},{"a .foo(a:!b, b:c+d)"
},{"a .foo( a: !b b: c7d )"
},{"a .foo(a:b ).bar()"
},{"a .foo(a:b ) .bar( )"
},{"a '.foo(a$$:b ) .bar( )\n"
},{"a 'b\n 'c\n "
},{"a[]"
},{"a []"
},{"a [ ]"
},{"a ['foo\n ]"
},{"a [x:a;]"
},{"a [a;]"
},{"a [a; ]"
},{"a [ a;]"
},{"a [ a; ]"
},{"a [ a; b; ]"

},{"a[;]"
},{"a [;;]"
},{"a [;;a]"
},{"a [;'foo\n ]"
},{"a [x:a;a]"
},{"a [x:a]"
},{"a [a]"
},{"a [ a; b ]"
},{"a [ ;;a;;; b ]"
},{"a [ ;;a;;; b;;; ]"

},{"a(a, b:c)"
},{"C"
},{" ( C a = b a)"
},{" ( a = b a)"
},{" ( a )"
},{" ( a = b b a)"
},{" ( a catch error x (on Any foo) a)"
},{" ( a catch return (on Any foo) a)"
},{" (mut C x=C(x) (capsule C y=x.#that() y))"
},{" {()}"
},{" {foo()}"
},{" {shared ()}"
},{" {shared foo()}"
},{" {'bla bla \n shared foo()}"
},{" (a )(b,x:c)"
},{" (a-a)()"
},{  "{ method () (b)}"
},{  "{a( Outer0::A a)"
    +" type method Outer0 a( Outer0::A a) ##field"
    +" mut method Outer0::A a() ##field"
    +" read method Outer0::A #a() ##field"
    +" mut method Void a(Outer0::A that) ##field"
    +" }"
},{"a'bla\n"//test that is not ok without newline
},{" S\"aaa\""
},{" S\"a'aa\""
},{" S\"+\""
},{" S\"(\""
},{" S\")\""
},{" S\"+(\""
},{" S\"+ (\""
},{" S\"((\""
},{" S\"))\""
},{" S\"+foo(\""
},{"Gui.executeJs(S\"alert'Hello\")"
},{" { method Foo foo()(this)}"
},{" { method Foo foo()(this)'bla\n}"
},{" { method Foo foo()(this)[x:this();]}"
},{" { method Foo foo()(this)[]()()'bla\n}"
//No more ok},{" {'bla bla \n shared 'blue blue \n foo()}"
}});}
  @Test public void termsOk(){TestHelper.isETop(s);}
  }

  @RunWith(Parameterized.class)
  public static class Test2 {
    @Parameter(0) public String s;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
  {"a ['foo ]"
},{"a .foo (a:b ) .bar( )"
//??notETop("12 a+b<c-d|h");
},{"a+b ))c"
},{"a`fuffa+++"
},{"a \t)"
},{"a ()"
},{"a (a, b:c)"
},{"a(a, c)"
},{"a.m(a, c)"
},{"(a+ (c)"
},{"(a+(c)"
},{"(a))+c)"
},{"a'bla"
//},{"a+b*c" is ok, is captured as a well formedness
  //now is fine instead//"a + ((b < (c))) - d |, h");}//space is required before parenthesis
}});}
  @Test public void termsNotOk() {TestHelper.notETop(s);}

}
}
