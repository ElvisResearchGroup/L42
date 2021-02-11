package is.L42.tests;

import static is.L42.tools.General.L;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import is.L42.common.Err;
import is.L42.generated.Core;
import is.L42.meta.Defaults;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.top.Init;

public class TestDefault{
@Test public void mini00(){pass("""
  class method Void foo(Any x)
  ""","""
  class method Void foo(Any x)
  """);}
@Test public void mini01(){pass("""
  class method Void foo(Any x)
  class method Any #default#foo#x()
  ""","""
  class method Void foo(Any x)
  class method Any #default#foo#x()
  class method Void foo()=(Any x=this.#default#foo#x() this.foo(x=x))
  """);}
@Test public void mini02(){pass("""
  class method Void foo(Any x, Any y)
  class method Any #default#foo#x()
  ""","""
  class method Void foo(Any x, Any y)
  class method Any #default#foo#x()
  class method Void foo(Any y)=(Any x=this.#default#foo#x() this.foo(x=x,y=y))
  """);}
@Test public void mini03(){pass("""
  class method Void foo(Any x, Any y)
  class method Any #default#foo#x()
  class method Any #default#foo#y(Any x)
  ""","""
  class method Void foo(Any x, Any y)
  class method Any #default#foo#x()
  class method Any #default#foo#y(Any x)
  class method Void foo()=(Any x=this.#default#foo#x() Any y=this.#default#foo#y(x=x) this.foo(x=x,y=y))
  """);}
@Test public void mini03Order(){pass("""
  class method Void foo(Any x, Any y)
  class method Void foo()
  class method Any #default#foo#x()
  class method Any #default#foo#y(Any x)
  ""","""
  class method Void foo(Any x, Any y)
  class method Void foo()=(Any x=this.#default#foo#x() Any y=this.#default#foo#y(x=x) this.foo(x=x,y=y))
  class method Any #default#foo#x()
  class method Any #default#foo#y(Any x)
  """);}
@Test public void wrongParType00(){fail("""
  class method Void foo(Any x, Any y)
  class method Any #default#foo#x()
  class method Any #default#foo#y(Void x)
  ""","""
  class method Any #default#foo#y(Void x)
  Default method #default#foo#y(x) uses invalid type for parameter x; it should be Any
  [###]""");}
@Test public void wrongParType01(){fail("""
  class method Void foo(Any x, Any y)
  class method Any #default#foo#x()
  class method Any #default#foo#y(capsule Void x)
  ""","""
  class method Any #default#foo#y(capsule Void x)
  Default method #default#foo#y(x) uses invalid modifier Capsule for parameter x
  [###]""");}
@Test public void wrongParType02(){fail("""
    class method Void foo(fwd imm Any x, fwd imm Any y)
    class method Any #default#foo#x()
    class method fwd imm Any #default#foo#y(fwd imm Void x)
    ""","""
    class method fwd imm Any #default#foo#y(fwd imm Void x)
    Default method #default#foo#y(x) uses invalid modifier ImmutableFwd for parameter x
    [###]""");}
@Test public void okFwd(){pass("""
    class method Void foo(fwd imm Any x, fwd imm Any y)
    class method Any #default#foo#x()
    class method Any #default#foo#y(Any x)
    ""","""
    class method Void foo(fwd imm Any x, fwd imm Any y)
    class method Any #default#foo#x()
    class method Any #default#foo#y(Any x)
    class method Void foo()=(imm Any x=this.#default#foo#x() imm Any y=this.#default#foo#y(x=x)this.foo(x=x, y=y))
    """);}
@Test public void apply01(){pass("""
  class method Void #apply(Any x, Any y)
  class method Any #default#x()
  class method Any #default#y()
  ""","""
  class method Void #apply(Any x, Any y)
  class method Any #default#x()
  class method Any #default#y()
  class method Void #apply()=(Any x=this.#default#x() Any y=this.#default#y() this.#apply(x=x,y=y))
  """);}
@Test public void apply02(){pass("""
  class method Void #apply(Any x, Any y, Any z)
  class method Any #default#x()
  class method Any #default#y()
  class method Any #default#z(Any x, Any y)
  ""","""
  class method Void #apply(Any x, Any y, Any z)
  class method Any #default#x()
  class method Any #default#y()
  class method Any #default#z(Any x, Any y)
  class method Void #apply()=(Any x=this.#default#x() Any y=this.#default#y() Any z=this.#default#z(x=x,y=y) this.#apply(x=x,y=y,z=z))
  """);}
@Test public void apply02Swap(){pass("""
  class method Void #apply(Any x, Any y, Any z)
  class method Any #default#x()
  class method Any #default#y()
  class method Any #default#z(Any y, Any x)
  ""","""
  class method Void #apply(Any x, Any y, Any z)
  class method Any #default#x()
  class method Any #default#y()
  class method Any #default#z(Any y, Any x)
  class method Void #apply()=(Any x=this.#default#x() Any y=this.#default#y() Any z=this.#default#z(y=y,x=x) this.#apply(x=x,y=y,z=z))
  """);}
@Test public void apply03(){pass("""
  class method Void #apply(Any x, Any y, Any z)
  class method Any #default#z(Any x, Any y)
  ""","""
  class method Void #apply(Any x, Any y, Any z)
  class method Any #default#z(Any x, Any y)
  class method Void #apply(Any x,Any y)=(Any z=this.#default#z(x=x,y=y) this.#apply(x=x,y=y,z=z))
  """);}
@Test public void apply03Swap(){pass("""
  class method Void #apply(Any x, Any y, Any z)
  class method Any #default#z(Any y, Any x)
  ""","""
  class method Void #apply(Any x, Any y, Any z)
  class method Any #default#z(Any y, Any x)
  class method Void #apply(Any x,Any y)=(Any z=this.#default#z(y=y,x=x) this.#apply(x=x,y=y,z=z))
  """);}

public static void pass(String sl1,String sl2){pass(sl1,sl2,new String[]{null});}
public static void pass(String sl1,String sl2,String[]msg){
  Resources.clearResKeepReuse();
  Init init1=new Init("{"+sl1+"#norm{}}");
  Init init2=new Init("{"+sl2+"#norm{}}");
  Function<L42£LazyMsg,L42Any>wrap=lm->{msg[0]=lm.getMsg();throw new FailErr();};
  Core.L lRes=new Defaults().of(init1.p,L(),wrap);
  assertEquals(lRes, init2.p.topCore());
  }
static class FailErr extends Error{}
public static void fail(String sl1,String err){
  String[]msg={null};
  try{pass(sl1,"",msg); Assertions.fail();}
  catch(FailErr fe){}
  Err.strCmp(msg[0],err);
  }
}