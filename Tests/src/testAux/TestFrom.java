package testAux;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import coreVisitors.From;
import facade.Parser;
import ast.Ast.Path;
import sugarVisitors.InjectionOnCore;
import ast.ExpCore;

public class TestFrom {
  @Test(singleThreaded=true, timeOut = 500)
  public class Test1 {
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
  },{"Outer3::A", "Outer2::B","Outer4::A"
  },{"Outer3::A.m()", "Outer2::B","Outer4::A.m()"
  },{"Outer3::A.m(x:Outer3::A)", "Outer2::B","Outer4::A.m(x:Outer4::A)"
  },{"{ method () (Outer3::A.m())}", "Outer2::B","{ method() (Outer4::A.m())}"
  },{"{ C:{ method () (Outer3::A.m())}}", "Outer2::B","{ C:{ method() (Outer4::A.m())}}"
  },{"{ C:{ method () (Outer1::A.m())}}", "Outer2::B","{ C:{ method() (Outer1::A.m())}}"
  },{"{ C:{ method () (Outer1::A.m())}}", "Outer20::B","{ C:{ method() (Outer1::A.m())}}"
  },{"{ C:{ method () (Outer1::A.m())}}", "Outer0::B","{ C:{ method() (Outer1::A.m())}}"
  },{"{interface method Void m()^## Outer10}","Outer1","{interface method Void m()^## Outer11}"
  },{"{interface method Void m()^## Outer0}","Outer1","{interface method Void m()^## Outer0}"
    //and would be different as members out of a class
}};}

  @Test(dataProvider="expCore,path,expCore")
  public void testFrom(String e1,String path,String er) {
    ExpCore ee1=Parser.parse(null,e1).accept(new InjectionOnCore());
    ExpCore eer=Parser.parse(null,er).accept(new InjectionOnCore());
    Path pp2=Path.parse(path);
    Assert.assertEquals(From.from(ee1,pp2),eer);
  }
      
    }

}
