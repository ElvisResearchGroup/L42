package testAux;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import coreVisitors.From;
import facade.Parser;
import ast.Ast.Path;
import sugarVisitors.InjectionOnCore;
import ast.ExpCore;
import auxiliaryGrammar.Functions;

public class TestGarbage {
  @RunWith(Parameterized.class)
  public static class Test1 {
    @Test public void deleteMe(){Assert.assertEquals(1,1);}
    @Parameter(0) public int n;
    @Parameter(1) public String e1;
    @Parameter(2) public String er;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
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
  }});}

  @Test
  public void testFrom() {
    ExpCore ee1=Parser.parse(null," "+e1).accept(new InjectionOnCore());
    ExpCore eer=Parser.parse(null," "+er).accept(new InjectionOnCore());
    Assert.assertEquals(Functions.garbage((ExpCore.Block)ee1, n),eer);
  }

    }

}
