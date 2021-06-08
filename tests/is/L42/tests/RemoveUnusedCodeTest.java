package is.L42.tests;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import is.L42.common.Err;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;

import is.L42.meta.RemoveUnusedCode;
import is.L42.meta.Rename;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.top.CachedTop;
import is.L42.top.Init;
import is.L42.top.UniqueNsRefresher;

class RemoveUnusedCodeTest {
  Core.L hide(Core.L l,String ... cs){
    try{
      List<C> csList=L(Stream.of(cs).map(s->new C(s,-1)));
      var renames=List.of(new is.L42.meta.Arrow(csList,null,true,true,null,null,null));
      Function<L42£LazyMsg,L42Any>wrap=lm->{throw new Error(lm.getMsg());};
      return new Rename(new UniqueNsRefresher())
        .apply(Program.flat(l),new C("TOP",-1),l,
        renames,wrap,wrap,wrap,wrap);
      }
    catch(Throwable t){return l;}
  }
  void checkDeps(String lib,String expected,String expectedRes){
    Resources.clearResKeepReuse();
    Core.L l1=Init.topCache(new CachedTop(L(),L()),"{"+lib+"}");
    l1=hide(l1,"B_");
    l1=hide(l1,"C_");
    l1=hide(l1,"A","B_");
    l1=hide(l1,"A","C_");
    String res=""+new RemoveUnusedCode().precompute(l1);
    String tot=""+new RemoveUnusedCode().of(l1);
    Err.strCmp(res,expected);
    expectedRes=expectedRes.replace(" ","");
    expectedRes=expectedRes.replace(",","");
    expectedRes=expectedRes.replace("\n","");
    tot=tot.replace(" ","");
    tot=tot.replace(",","");
    tot=tot.replace("\n","");
    Err.strCmp(tot,"{"+expectedRes+"}");
  }
  @Test void test() {checkDeps("""
    A = {method Void foo()=void}
    ""","""
    A
    A.foo()
    ""","""
    A={method Void foo()=void
      #typed{}}#norm{}
    """);}
  @Test void testNested() {checkDeps("""
    A = {method Void foo()=void B={method Void bar()}}
    ""","""
    A
    A.foo()
    A.B
    A.B.bar()
    ""","""
    A={method Void foo()=void
      B={method Void bar()#typed{}}
      #typed{}}#norm{}
    """);}
  @Test void testPrivOut() {checkDeps("""
    A = {method Void foo()=void B={method Void bar()}}
    B_={method Void barOut()=void}
    ""","""
    A
    A.foo()
    A.B
    A.B.bar()
    ""","""
    A={method Void foo()=void
      B={method Void bar()#typed{}}
      #typed{}}#norm{}
    """);}
  @Test void testPrivIn() {checkDeps("""
    A = {method Void foo()=void B_={method Void bar()=void}}
    B={method Void barOut()=void}
    ""","""
    A
    A.foo()
    B
    B.barOut()
    ""","""
    A={method Void foo()=void #typed{}}
    B={method Void barOut()=void#typed{}}
    #norm{}
    """);}
  @Test void testPrivInKeep() {checkDeps("""
    A = {method Void foo()=B_.bar() B_={class method Void bar()=void}}
    ""","""
    A
    A.foo()
    A.B_::1
    A.B_::1.bar::2()
    ""","""
    A={method Void foo()=This.B_::1<:class This.B_::1.bar::2()
      B_::1={class method Void bar::2()=void
        #typed{}}
      #typed{typeDep=This.B_::1 This coherentDep=This.B_::1 This}}
    #norm{}
    """);}
  @Test void testPrivInKeep2() {checkDeps("""
    A = {method Void foo()=B_.bar() 
      B_={
        class method Void bar()=void
        class method Void foo()=C_.bar()
        }
      C_={class method Void bar()=void}
      }
    ""","""
    A
    A.foo()
    A.B_::1
    A.B_::1.bar::2()
    ""","""
    A={method Void foo()=
      This.B_::1<:class This.B_::1.bar::2()
      B_::1={class method Void bar::2()=void
        #typed{typeDep=This1 coherentDep=This1 watched=This1}}
      #typed{typeDep=This.B_::1This coherentDep=This.B_::1 This}}
    #norm{}
    """);}
  @Test void testPrivInKeepByDocs() {checkDeps("""
    I = {interface}
    A = { [@B_ I]
      method Void foo()=void 
      B_={class method Void bar()=void}
      }
    ""","""
    I
    A
    A.foo()
    A.B_::1
    ""","""
    I={interface#typed{}}
    A={[@This.B_::1This1.I]
      method Void foo()=void
      B_::1={#typed{}}
      #typed{typeDep=This.B_::1 This This1.I}}
    #norm{}
    """);}
  @Test void testPrivInKeepByTopDocs() {checkDeps("""
    A = {
      method Void foo()=void 
      B_={class method Void bar()=void}
      }
    @{@A.B_}
    ""","""
    A
    A.foo()
    A.B_::1
    ""","""
    A={method Void foo()=void
      B_::1={#typed{}}#typed{}}
    #norm{typeDep=This.A.B_::1 This.A watched=This.A}
    @{@This.A.B_::1}
    """);}
  @Test void testPrivInKeepByMeta() {checkDeps("""
    A = {
      method Library foo()={
        class method Void bb()=void
        #norm{
          typeDep=This1.B_
          }
        }
      B_={class method Void bar()=void}
      }
    ""","""
    A
    A.foo()
    A.B_::1
    ""","""
    A={method Library foo()=
      {class method Void bb()=void
      #typed{typeDep=This1.B_::1 This1 watched=This1}}
      B_::1={#typed{}}
      #typed{typeDep=This.B_::1 This}}
    #norm{}
    """);}

  @Test void testPrivInKeepByMetaDeep() {checkDeps("""
      A = {
        method Library foo()={
          class method Void bb()=This1.B_<:class This1.B_.bar()
          #norm{
            typeDep=This1.B_
            coherentDep=This1.B_
            usedMethods=This1.B_.bar()
            }
          }
        B_={class method Void bar()=void}
        }
      ""","""
      A
      A.foo()
      A.B_::1
      A.B_::1.bar::2()
      ""","""
      A={method Library foo()=
        {class method Void bb()=This1.B_::1<:class This1.B_::1.bar::2()
        #typed{typeDep=This1.B_::1This1
               coherentDep=This1.B_::1This1
               watched=This1}}
        B_::1={class method Void bar::2()=void #typed{}}
        #typed{typeDep=This.B_::1 This}}
      #norm{}
      """);}

  @Test void testPrivInKeepByMetaNested() {checkDeps("""
      A = {
        method Library foo()={
          Nested={
            class method Void bb()=This2.B_<:class This2.B_.bar()
            #norm{
              typeDep=This2.B_
              coherentDep=This2.B_
              usedMethods=This2.B_.bar()
              }
            }
          #norm{}
          }
        B_={class method Void bar()=void}
        }
      ""","""
      A
      A.foo()
      A.B_::1
      A.B_::1.bar::2()
      ""","""
      A={
        method Library foo()={
          Nested={class method Void bb()=This2.B_::1<:classThis2.B_::1.bar::2()
            #norm{typeDep=This2.B_::1 This2 coherentDep=This2.B_::1 This2 watched=This2}}
          #typed{}}
        B_::1={class method Void bar::2()=void
          #typed{}}
        #typed{typeDep=This.B_::1 This}}
      #norm{}
      """);}
  
  @Test void testPrivFixP() {checkDeps("""
    A = {
      method Void foo()=B_.bar0()
      B_={class method Void bar0()=C_.bar1()}
      }
    C_={class method Void bar1()=void}
    ""","""
    A
    A.foo()
    A.B_::3
    A.B_::3.bar0::4()
    C_::1
    C_::1.bar1::2()
    ""","""
    A={
      method Void foo()=This.B_::3<:class This.B_::3.bar0::4()
      B_::3={class method Void bar0::4()=This2.C_::1<:class This2.C_::1.bar1::2()
        #typed{typeDep=This2.C_::1 This2 coherentDep=This2.C_::1 This2 watched=This2}}
      #typed{typeDep=This.B_::3 This This1.C_::1 This1 coherentDep=This.B_::3 This watched=This1}}
    C_::1={class method Void bar1::2()=void
      #typed{}}
    #norm{}
    """);}
  @Test void testPrivCirc() {checkDeps("""
    A = {
      method Void foo()=B_.bar0()
      B_={class method Void bar0()=C_.bar1()}
      }
    C_={class method Void bar1()=A.B_.bar0()}
    ""","""
    A
    A.foo()
    A.B_::3
    A.B_::3.bar0::4()
    C_::1
    C_::1.bar1::2()
    ""","""
    A={method Void foo()=
      This.B_::3<:class This.B_::3.bar0::4()
      B_::3={class method Void bar0::4()=
        This2.C_::1<:classThis2.C_::1.bar1::2()
        #typed{[###]}}
        #typed{[###]}}
      C_::1={class method Void bar1::2()=
        This1.A.B_::3<:classThis1.A.B_::3.bar0::4()
        #typed{[###]}}
      #norm{[###]}
    """);}
  @Test void testPrivInKeepUnusedOuter() {checkDeps("""
    A = {method Void foo()=C_.B.bar()}
    C_= {
      B={class method Void bar()=void}
      }
    ""","""
    A
    A.foo()
    C_::1.B::2
    C_::1.B::2.bar::3()
    ""","""
    A={method Void foo()=
      This1.C_::1.B::2<:class This1.C_::1.B::2.bar::3()
      #typed{typeDep=This1.C_::1.B::2 This1 coherentDep=This1.C_::1.B::2 This1 watched=This1}}
    C_::1={B::2={class method Void bar::3()=void #typed{}}#typed{}}
    #norm{}
    """);}

  @Test void testPrivInKeepInterfaceMeth() {checkDeps("""
      A = {method Void foo()=(
        class C_.I i=C_.B
        i.bar()
        )
        }
      C_= {
        I={interface
         class method Void bar()
         class method Void beer()
         }
        B={[I]
          method bar()=void
          method beer()=void
          }
        }
      ""","""
      A
      A.foo()
      C_::1.I::2
      C_::1.B::2
      C_::1.I::2.bar::3()
      C_::1.I::2.beer::3()
      C_::1.B::2.bar::3()
      C_::1.B::2.beer::3()
      ""","""
      A={method Void foo()=(
        class This1.C_::1.I::2 i=This1.C_::1.B::2<:class This1.C_::1.B::2
        i.bar::3())
        #typed{typeDep=This1.C_::1.I::2 This1 This1.C_::1.B::2 coherentDep=This1.C_::1.B::2 This1 watched=This1}}
      C_::1={
        I::2={interface 
          class method Void bar::3()
          class method Void beer::3()
          #typed{close}}
        B::2={[This1.I::2]
          class method Void bar::3()=void
          class method Void beer::3()=void
          #typed{typeDep=This1.I::2 This1 watched=This1 refined=bar::3()beer::3()}}
        #typed{typeDep=This.I::2 This This1 watched=This1}}
      #norm{typeDep=This}
      """);}
  @Test void testPrivInKeepInterfaceMeth2() {checkDeps("""
      A = {method Void foo()=C_.B.bar()}
      C_= {
        I={interface
         class method Void bar()
         class method Void beer()
         }
        B={[I]
          method bar()=void
          method beer()=void
          }
        }
      ""","""
      A
      A.foo()
      C_::1.B::2
      C_::1.I::2.bar::3()
      C_::1.I::2
      C_::1.B::2.bar::3()
      C_::1.B::2.beer::3()
      C_::1.I::2.beer::3()
      ""","""
      A={method Void foo()=
        This1.C_::1.B::2<:class This1.C_::1.B::2.bar::3()
        #typed{typeDep=This1.C_::1.B::2 This1 coherentDep=This1.C_::1.B::2 This1 watched=This1}}
      C_::1={
        I::2={interface 
          class method Void bar::3()
          class method Void beer::3()
          #typed{close}}
        B::2={[This1.I::2]
          class method Void bar::3()=void
          class method Void beer::3()=void
          #typed{typeDep=This1.I::2 This1 watched=This1 refined=bar::3()beer::3()}}
        #typed{typeDep=This.I::2 This This1 watched=This1}}
      #norm{typeDep=This}
      """);}
  
  //se un metodo e' refined/native non e' rimosso.
  //ma se non e' rimosso, allora potrebbe usare nel corpo o nell'header degli altri tipi.
  //quindi devo aggiungerlo alla visita in qualche modo.
}
//what if an nc is kept as a box?
//TODO: should boxes be kept EMPTY??? it would be more effective too
