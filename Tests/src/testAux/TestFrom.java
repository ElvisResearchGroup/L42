package testAux;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import coreVisitors.From;
import coreVisitors.FromInClass;
import facade.Parser;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.ExpCore;

public class TestFrom {
  @RunWith(Parameterized.class)
  public static class Test1 {
    @Parameter(0) public String p1;
    @Parameter(1) public String p2;
    @Parameter(2) public String pr;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {"This1.C", "This0.C","This0.C"
    },{"This1.Foo.Bar", "This3.Beer.Baz","This3.Beer.Foo.Bar"
    },{"This1.A.B", "This40.C.D","This40.C.A.B"
    },{"This10.A.B", "This3.C.D","This11.A.B"
    },{"This2.A.B", "This2.C.D","This2.A.B"
    },{"This0.A.B.C.D.E", "This0.A1.B1.C2.D1","This0.A1.B1.C2.D1.A.B.C.D.E"
    },{"This0.A.B.C.D.E", "This1.A1.B1.C2.D1","This1.A1.B1.C2.D1.A.B.C.D.E"
    },{"This0.A.B.C.D.E", "This2.A1.B1.C2.D1","This2.A1.B1.C2.D1.A.B.C.D.E"
    },{"This1.A.B.C.D.E", "This0.A1.B1.C2.D1","This0.A1.B1.C2.A.B.C.D.E"
    },{"This2.A.B.C.D.E", "This0.A1.B1.C2.D1","This0.A1.B1.A.B.C.D.E"
    },{"This3.A.B.C.D.E", "This0.A1.B1.C2.D1","This0.A1.A.B.C.D.E"
    },{"This3.A.B.C.D.E", "This1.A1.B1.C2.D1","This1.A1.A.B.C.D.E"
    },{"This3.A.B.C.D.E", "This2.A1.B1.C2.D1","This2.A1.A.B.C.D.E"
    },{"This0", "This1","This1"
    },{"This2.B","This1.C.A","This1.B"
  }});}

    @Test
    public void testFromP() {
      Path pp1=Path.parse(p1);
      Path pp2=Path.parse(p2);
      Path ppr=Path.parse(pr);
      Assert.assertEquals(From.fromP(pp1, pp2),ppr);
    }

  }
  @RunWith(Parameterized.class)
  public static class Test2 {
    @Parameter(0) public String e1;
    @Parameter(1) public String path;
    @Parameter(2) public String er;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {

  {"{ method () (This3.A.m())}", "This2.B","{ method () (This4.A.m())}"
  },{"{ C:{ method () (This3.A.m())}}",
      "This2.B",
      "{ C:{ method () (This4.A.m())}}"
  },{"{ C:{ method () (This1.A.m())}}", "This2.B","{ C:{ method () (This1.A.m())}}"
  },{"{ C:{ method () (This1.A.m())}}", "This20.B","{ C:{ method () (This1.A.m())}}"
  },{"{ C:{ method () (This1.A.m())}}", "This0.B","{ C:{ method () (This1.A.m())}}"
  },{"{interface method Void m()^## This10}","This1","{interface method Void m()^## This11}"
  },{"{interface method Void m()^## This0}","This1","{interface method Void m()^## This0}"
    //and would be different as members out of a class
  },{"{method This2.B #apply() }",
     "This1.C.A",
     "{method This2.C.B #apply() }"
}});}

  @Test
  public void testFrom() {
    ExpCore ee1=Parser.parse(null,e1).accept(new InjectionOnCore());
    ExpCore eer=Parser.parse(null,er).accept(new InjectionOnCore());
    Path pp2=Path.parse(path);
    Assert.assertEquals(FromInClass.of((ClassB) ee1,pp2),eer);
  }
  }








  @RunWith(Parameterized.class)
  public static class Test3 {
    @Parameter(0) public String e1;
    @Parameter(1) public String path;
    @Parameter(2) public String er;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {{"{ method Void m() exception Void void}", "This2.B","{ method Void m() exception Void void}"
},{"{method This2.B #apply() }", "This1.C.A","{method This1.B #apply() }"
},{"{ method Void m() This0<{ <: This1 } }",
   "This0.C",
   "{ method Void m() This0.C <{ <: This1.C }  }"
},{"{ method Void m() This0.B<This1.B<This2.B<This0<{ <: This1, This0.A, This1.A,This2.A } }",
   "This0.C",//as in D:C.m()  C:{ m {<:This1.A,This2.A }}
   "{ method Void m() This0.C.B<This0.B<This1.B<This0.C <{ <: This1.C, This0.A, This1.C.A,This1.A }  }"
},{"{ method Void m() This0.B<This1.B<This2.B<This0<{ D:{<: This2, This1.A, This2.A,This3.A } }}",
    "This0.C",//as in D:C.m()  C:{ m {<:This1.A,This2.A }}
    "{ method Void m() This0.C.B<This0.B<This1.B<This0.C <{ D:{<: This2.C, This1.A, This2.C.A,This2.A } } }"


},{"{ method Void m() This0.B<This1.B<This2.B<This0<{ <: This1, This0.A, This1.A,This2.A } }",
    "This1.C",//as in D:This1.C.m()  C:{ m {<:This1.A,This2.A }}
    "{ method Void m() This1.C.B<This1.B<This2.B<This1.C <{ <: This2.C, This0.A, This2.C.A,This2.A }  }"
 },{"{ method Void m() This0.B<This1.B<This2.B<This0<{ D:{<: This2, This1.A, This2.A,This3.A } }}",
     "This1.C",//as in D:This1.C.m()  C:{ m {<:This1.A,This2.A }}
     "{ method Void m() This1.C.B<This1.B<This2.B<This1.C <{ D:{<: This3.C, This1.A, This3.C.A,This3.A } } }"



}});}

@Test
public void testFromMethod() {
  ClassB cb1=(ClassB)Desugar.of(Parser.parse(null,e1)).accept(new InjectionOnCore());
  ClassB cb2=(ClassB)Desugar.of(Parser.parse(null,er)).accept(new InjectionOnCore());
  Path p=Path.parse(path);
  Assert.assertEquals(
          cb1.withMs(Collections.singletonList(From.from(cb1.getMs().get(0),p))),
          cb2);
}

}




}


