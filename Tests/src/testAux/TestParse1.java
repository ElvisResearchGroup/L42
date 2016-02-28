package testAux;

import static helpers.TestHelper.lineNumber;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import helpers.TestHelper;


public class TestParse1 {

  @RunWith(Parameterized.class)
  public static class Test1 {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String s;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
  {lineNumber(), "a"
  },{lineNumber()," (This0.that().#next() x=this.that().#next() x)"
//},{lineNumber(),"{'@ExpectedClass :( \n}"//TODO: the ( is turned in \t, and \t is not accepted in comments.
//    more important: should ' be accepted in single line strings?
},{lineNumber(),TestHelper.multiLine("{"
        ,"C:{ k() class method Library ok() ({'@OK\n})"
        ,"        class method Library ko() ({'@KO\n})"
        ,"  }"
        ,"I:{interface }"
        ,"AI:{ k()<:This1.I}"
        ,"D:("
        ,"  Any z=error This0.AI.k()"
        ,"  catch error This0.AI x This0.C.ok()"
        ,"  This0.C.ko()"
        ,"  )"
        ,"}"),
  },{lineNumber(),"{ }"
  },{lineNumber(),"{ reuse L42.is/nanoBase }"
  },{lineNumber(),"{ reuse L42.is/base(2) }"//I test that that is a well formed url
  },{lineNumber(),"{ reuse L42.is/nanoBase \n method foo() (void)}"
},{lineNumber(),"{ C:{k() class method Library m() ({'@OK\n} )} D:C.m() }"
},{lineNumber(),"{outer() C: {new() class method Library m() ({inner()})} D: C.m()}"
},{lineNumber(),"{outer() C: {new() class method Library m() ({inner()})} D: ({inner()})}"

},{lineNumber()," (This0.C).foo(bar:This0.C)"
},{lineNumber()," (class This0.C c=(This0.C) c.foo(bar:This0.C))"
},{lineNumber()," (This0.C c=This0.C.new() This0.D r=This0.D.new(x:c) (r).x())"
},{lineNumber()," (This0.C c=This0.C.new() This0.D r=This0.D.new(x:c) (This0.D d=(r) d.x()))"
},{lineNumber(),"void"
},{lineNumber()," (void)"
},{lineNumber(),"5N"
},{lineNumber(),"-5N"
},{lineNumber(),"+5N"
},{lineNumber(),"~N"
},{lineNumber()," (~N)"
},{lineNumber()," (-5N)"
},{lineNumber(),"a- 5N"
},{lineNumber(),"a-5N"
},{lineNumber()," (a (-5N) e)"
},{lineNumber(),"a+b"
},{lineNumber(),"a*c"
},{lineNumber(),"a:=c"
},{lineNumber(),"a+=c"
},{lineNumber(),"a+=c +a"
},{lineNumber(),"a+b<c-d|h"
},{lineNumber(),"a + b < c - d | h"
},{lineNumber(),"a + b < c - d |, h"
},{lineNumber(),"a + (b < c) - d |, h"
},{lineNumber(),"a + ( (b < (c))) - d |, h"
},{lineNumber(),"a + ((b < (c))) - d |, h"
},{lineNumber(),"a.#foo()"
},{lineNumber(),"a.foo()"
},{lineNumber(),"a .foo()"
},{lineNumber(),"a .foo( )"
},{lineNumber(),"a .foo(a:b )"
},{lineNumber(),"a .foo(a:b b:c)"
},{lineNumber(),"a .foo(a:!b, b:c+d)"
},{lineNumber(),"a .foo( a: !b b: c7d )"
},{lineNumber(),"a .foo(a:b ).bar()"
},{lineNumber(),"a .foo(a:b ) .bar( )"
},{lineNumber(),"a '.foo(a$$:b ) .bar( )\n"
},{lineNumber(),"a 'b\n 'c\n "
},{lineNumber(),"a[]"
},{lineNumber(),"a []"
},{lineNumber(),"a [ ]"
},{lineNumber(),"a ['foo\n ]"
},{lineNumber(),"a [x:a;]"
},{lineNumber(),"a [a;]"
},{lineNumber(),"a [a; ]"
},{lineNumber(),"a [ a;]"
},{lineNumber(),"a [ a; ]"
},{lineNumber(),"a [ a; b; ]"

},{lineNumber(),"a[;]"
},{lineNumber(),"a [;;]"
},{lineNumber(),"a [;;a]"
},{lineNumber(),"a [;'foo\n ]"
},{lineNumber(),"a [x:a;a]"
},{lineNumber(),"a [x:a]"
},{lineNumber(),"a [a]"
},{lineNumber(),"a [ a; b ]"
},{lineNumber(),"a [ ;;a;;; b ]"
},{lineNumber(),"a [ ;;a;;; b;;; ]"

},{lineNumber(),"a(a, b:c)"
},{lineNumber(),"C"
},{lineNumber()," ( C a = b a)"
},{lineNumber()," ( a = b a)"
},{lineNumber()," ( a )"
},{lineNumber()," ( a = b b a)"
},{lineNumber()," ( a catch error Any x foo a)"
},{lineNumber()," ( a catch return Any x foo a)"
},{lineNumber()," ( a catch return Any foo a)"
},{lineNumber()," (mut C x=C(x) (capsule C y=x.#that() y))"
},{lineNumber()," {()}"
},{lineNumber()," {foo()}"
},{lineNumber()," {mut ()}"
},{lineNumber()," {mut foo()}"
},{lineNumber()," {'bla bla \n mut foo()}"
},{lineNumber()," (a )(b,x:c)"
},{lineNumber()," (a-a)()"
},{lineNumber(),  "{ method () (b)}"
},{lineNumber(),  "{a( This0.A a)"
    +" class method This0 a( This0.A a) ##field"
    +" mut method This0.A a() ##field"
    +" read method This0.A #a() ##field"
    +" mut method Void a(This0.A that) ##field"
    +" }"
},{lineNumber(),"a'bla\n"//test that is not ok without newline
},{lineNumber()," S\"aaa\""
},{lineNumber()," S\"a'aa\""
},{lineNumber()," S\"+\""
},{lineNumber()," S\"(\""
},{lineNumber()," S\")\""
},{lineNumber()," S\"+(\""
},{lineNumber()," S\"+ (\"" //& ?
},{lineNumber()," S\" (\""
},{lineNumber()," S\"((\""
},{lineNumber()," S\"))\""
},{lineNumber()," S\"+foo(\""
},{lineNumber(),"Gui.executeJs(S\"alert'Hello\")"
},{lineNumber()," { method Foo foo()(this)}"
},{lineNumber()," { method Foo foo()(this)'bla\n}"
},{lineNumber()," { method Foo foo()(this)[x:this();]}"
},{lineNumber()," { method Foo foo()(this)[]()()'bla\n}"
//No more ok},{lineNumber()," {'bla bla \n shared 'blue blue \n foo()}"
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
