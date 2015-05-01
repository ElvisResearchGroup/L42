package testAux;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import facade.Parser;
import sugarVisitors.InjectionOnCore;
import sugarVisitors.ToFormattedText;
import ast.ExpCore;
import ast.Expression;

public class TestParseBackAndForth {
  @DataProvider(name = "termsToString")
  public Object[][] createData1() {
   return new Object[][] {
{"a","a"
},{"a*b","a * b"
},{" (a=b c)","(\n  a=b\n  c\n  )"
},{" (var a=b c)","(\n  var a=b\n  c\n  )"
},{"{a(A a)}","{ a(A a)}"
},{"{'qq\n a(A a)}","{'qq\n a(A a)}"
},{"{(A a)}","{ (A a)}"
},{"{(A a'foo\n)}","{ (A a'foo\n)}"
},{"{(A a)'ee\n}","{ (A a)'ee\n}"
},{"{(A a)<:A}","{ (A a)<:A}"
},{"{ method a(b c) {d e f}}","{\nmethod a(b c ) {\n  d\n  e\n  f\n  }}"
},{"{ method a(b c) (d e f)}","{\nmethod a(b c ) (\n  d\n  e\n  f\n  )}"
},{"if a (b) else c.h()","if a (b)else c.h()"
},{"while a (b.h(a+a))","while a (b.h(a + a))"
},{"Foo\"bla\"","Foo\"bla\""
},{"Foo\"  bla  \"","Foo\"  bla  \""
//},{"Foo\"\n  'bla\n  , \"",""
//},{"Foo\"\n  'bla\n 'ble\n , \"",""
},{" { }","{}"
},{" {()}","{ ()}"
},{" {interface}","{interface }"
},{" {<: A::B, C}","{<:A::B, C}"
  
},{" a 'bar\n ","a'bar\n"
},{"!a","!a"
},{"~(a+b)","~(a + b)"
},{"a(b,x:c)","a(b, x:c)"
},{"!a(b,x:c)","!a(b, x:c)"
},{" (a )(b,x:c)","(a)(b, x:c)"
},{" using A::B::C check m('bar\nfoo) a+b","using A::B::C check m('bar\nfoo) a + b"

},{"with x in a  var y in b + c() z= t var T w=t (on T y)",
   "with x in a, var y in b + c() z= t, var T w= t (\non T y\n)"
},{"with x in a (on T y on T T z+y on A case e() b )",
   "with x in a (\non T y\non T, T z + y\non A case e() b\n)"
}};}
  @Test(dataProvider="termsToString")
  public void testOkToString(String s,String expected) {
     Expression x = Parser.parse(null,s);
     //InjectionOnCore c=new InjectionOnCore();
     //ExpCore e2 = x.accept(c);
     Assert.assertEquals(ToFormattedText.of(x), expected);
  }

}
