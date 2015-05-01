package testAux;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import coreVisitors.From;
import facade.Parser;
import ast.Ast.Path;
import sugarVisitors.InjectionOnCore;
import ast.ExpCore;
import auxiliaryGrammar.Functions;

public class TestGarbage {
  @Test(singleThreaded=true, timeOut = 500)
  public class Test1 {
      @DataProvider(name = "int,e,e")
      public Object[][] createData1() {
       return new Object[][] {
      {0,"(void)","(void)"
    },{4,"(Void x=void Any y=void Any z=void Any zz=void void)","(void)"
    },{3,"(Void x=void Any y=void Any z=void void)","(void)"
    },{2,"(Void x=void Any y=void x.foo(p:x))","(Void x=void x.foo(p:x))"
    },{2,"(Void x=void Any y=void x.foo())","(Void x=void  x.foo())"
    },{3,"(Void x=void Any y=void Any zorro=void (zorro.foo()))","(Any zorro=void (zorro.foo()))"
    },{3,"( Void x=void  Void y=void Void zeppa=void (zeppa.foo(w:x)))","( Void x=void Void zeppa=void (zeppa.foo(w:x)))"
    },{3,"(Void x=void Void y=void Void z=void (z.foo(w:x)))","(Void x=void Void z=void (z.foo(w:x)))"
    },{3,"(Void x=void Void y=void Void z=void z.foo(w:x))","(Void x=void Void z=void z.foo(w:x))"
    },{4,"(Void x=z Any y=zz Any z=y Any zz=void x)","(Void x=z Any y=zz Any z=y Any zz=void x)"
    },{4,"(Void x=z Any y=void Any z=y Any zz=void x)","(Void x=z Any y=void Any z=y x)"
    },{2,"(Void x=z Any y=void Any z=y Any zz=void x)","(Void x=z Any y=void Any z=y Any zz=void x)"
    },{3,"(Any babba=void Void x=z Any y=void Any z=y Any zz=void x)","(Void x=z Any y=void Any z=y Any zz=void x)"
  }};}

    @Test(dataProvider="int,e,e")
  public void testFrom(int n,String e1,String er) {
    ExpCore ee1=Parser.parse(null," "+e1).accept(new InjectionOnCore());
    ExpCore eer=Parser.parse(null," "+er).accept(new InjectionOnCore());
    Assert.assertEquals(Functions.garbage((ExpCore.Block)ee1, n),eer);
  }
      
    }

}
