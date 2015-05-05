package testAux;

import java.util.Collections;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import coreVisitors.From;
import coreVisitors.FromInClass;
import facade.Parser;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.ExpCore;

public class TestFrom {
      @DataProvider(name = "paths3")
      public Object[][] createData1() {
       return new Object[][] {
      {"Outer1::C", "Outer0::C","Outer0::C"
    },{"Outer1::Foo::Bar", "Outer3::Beer::Baz","Outer3::Beer::Foo::Bar"
    },{"Outer1::A::B", "Outer40::C::D","Outer40::C::A::B"
    },{"Outer10::A::B", "Outer3::C::D","Outer11::A::B"
    },{"Outer2::A::B", "Outer2::C::D","Outer2::A::B"
    },{"Outer0::A::B::C::D::E", "Outer0::A1::B1::C2::D1","Outer0::A1::B1::C2::D1::A::B::C::D::E"
    },{"Outer0::A::B::C::D::E", "Outer1::A1::B1::C2::D1","Outer1::A1::B1::C2::D1::A::B::C::D::E"
    },{"Outer0::A::B::C::D::E", "Outer2::A1::B1::C2::D1","Outer2::A1::B1::C2::D1::A::B::C::D::E"
    },{"Outer1::A::B::C::D::E", "Outer0::A1::B1::C2::D1","Outer0::A1::B1::C2::A::B::C::D::E"
    },{"Outer2::A::B::C::D::E", "Outer0::A1::B1::C2::D1","Outer0::A1::B1::A::B::C::D::E"
    },{"Outer3::A::B::C::D::E", "Outer0::A1::B1::C2::D1","Outer0::A1::A::B::C::D::E"
    },{"Outer3::A::B::C::D::E", "Outer1::A1::B1::C2::D1","Outer1::A1::A::B::C::D::E"
    },{"Outer3::A::B::C::D::E", "Outer2::A1::B1::C2::D1","Outer2::A1::A::B::C::D::E"
    },{"Outer0", "Outer1","Outer1"
    },{"Outer2::B","Outer1::C::A","Outer1::B"
  }};}

    @Test(dataProvider="paths3")
    public void testFromP(String p1,String p2,String pr) {
      Path pp1=Path.parse(p1);
      Path pp2=Path.parse(p2);
      Path ppr=Path.parse(pr);
      Assert.assertEquals(From.fromP(pp1, pp2),ppr);
    }
    @DataProvider(name = "expCore,path,expCore")
    public Object[][] createData2() {
     return new Object[][] {

  {"{ method () (Outer3::A.m())}", "Outer2::B","{ method() (Outer4::A.m())}"
  },{"{ C:{ method () (Outer3::A.m())}}",
      "Outer2::B",
      "{ C:{ method() (Outer4::A.m())}}"
  },{"{ C:{ method () (Outer1::A.m())}}", "Outer2::B","{ C:{ method() (Outer1::A.m())}}"
  },{"{ C:{ method () (Outer1::A.m())}}", "Outer20::B","{ C:{ method() (Outer1::A.m())}}"
  },{"{ C:{ method () (Outer1::A.m())}}", "Outer0::B","{ C:{ method() (Outer1::A.m())}}"
  },{"{interface method Void m()^## Outer10}","Outer1","{interface method Void m()^## Outer11}"
  },{"{interface method Void m()^## Outer0}","Outer1","{interface method Void m()^## Outer0}"
    //and would be different as members out of a class
  },{"{method Outer2::B #apply() }",
     "Outer1::C::A",
     "{method Outer2::C::B #apply() }"
}};}

  @Test(dataProvider="expCore,path,expCore")
  public void testFrom(String e1,String path,String er) {
    ExpCore ee1=Parser.parse(null,e1).accept(new InjectionOnCore());
    ExpCore eer=Parser.parse(null,er).accept(new InjectionOnCore());
    Path pp2=Path.parse(path);
    Assert.assertEquals(FromInClass.of((ClassB) ee1,pp2),eer);
  }



  
  
  
  
  
  
  @DataProvider(name = "meth,path,meth")
  public Object[][] createData3() {
   return new Object[][] {
{"{ method Void m() exception Void void}", "Outer2::B","{ method Void m() exception Void void}"
},{"{method Outer2::B #apply() }", "Outer1::C::A","{method Outer1::B #apply() }"
},{"{ method Void m() Outer0<{ <: Outer1 } }",
   "Outer0::C",
   "{ method Void m() Outer0::C <{ <: Outer1::C }  }"
},{"{ method Void m() Outer0::B<Outer1::B<Outer2::B<Outer0<{ <: Outer1, Outer0::A, Outer1::A,Outer2::A } }",
   "Outer0::C",//as in D:C.m()  C:{ m {<:Outer1::A,Outer2::A }}
   "{ method Void m() Outer0::C::B<Outer0::B<Outer1::B<Outer0::C <{ <: Outer1::C, Outer0::A, Outer1::C::A,Outer1::A }  }"
},{"{ method Void m() Outer0::B<Outer1::B<Outer2::B<Outer0<{ D:{<: Outer2, Outer1::A, Outer2::A,Outer3::A } }}",
    "Outer0::C",//as in D:C.m()  C:{ m {<:Outer1::A,Outer2::A }}
    "{ method Void m() Outer0::C::B<Outer0::B<Outer1::B<Outer0::C <{ D:{<: Outer2::C, Outer1::A, Outer2::C::A,Outer2::A } } }"


},{"{ method Void m() Outer0::B<Outer1::B<Outer2::B<Outer0<{ <: Outer1, Outer0::A, Outer1::A,Outer2::A } }",
    "Outer1::C",//as in D:Outer1::C.m()  C:{ m {<:Outer1::A,Outer2::A }}
    "{ method Void m() Outer1::C::B<Outer1::B<Outer2::B<Outer1::C <{ <: Outer2::C, Outer0::A, Outer2::C::A,Outer2::A }  }"
 },{"{ method Void m() Outer0::B<Outer1::B<Outer2::B<Outer0<{ D:{<: Outer2, Outer1::A, Outer2::A,Outer3::A } }}",
     "Outer1::C",//as in D:Outer1::C.m()  C:{ m {<:Outer1::A,Outer2::A }}
     "{ method Void m() Outer1::C::B<Outer1::B<Outer2::B<Outer1::C <{ D:{<: Outer3::C, Outer1::A, Outer3::C::A,Outer3::A } } }"

    
    
}};}

@Test(dataProvider="meth,path,meth")
public void testFromMethod(String e1,String path,String er) {
  ClassB cb1=(ClassB)Desugar.of(Parser.parse(null,e1)).accept(new InjectionOnCore());
  ClassB cb2=(ClassB)Desugar.of(Parser.parse(null,er)).accept(new InjectionOnCore());
  Path p=Path.parse(path);
  Assert.assertEquals(
          cb1.withMs(Collections.singletonList(From.from(cb1.getMs().get(0),p))),
          cb2);
}

  
  
  
  
  
}


