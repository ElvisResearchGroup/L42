package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.common.Constants;
import is.L42.constraints.FreshNames;
import is.L42.common.EndError.InvalidImplements;
import is.L42.common.EndError.PathNotExistent;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.AtomicTest;
import is.L42.top.Init;
import is.L42.top.Top;
import is.L42.translationToJava.Loader;
import is.L42.visitors.FullL42Visitor;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestTopNorm
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   top("{}","{#norm{}}")
   
 //collect
   ),new AtomicTest(()->
   top("{} A= ={A={}} B= ={B={A={}}}","{#norm{}}")
   ),new AtomicTest(()->
   topFail(InvalidImplements.class,"{[This.B] B={interface}}",Err.nestedClassesImplemented(hole))
   ),new AtomicTest(()->
   topFail(InvalidImplements.class,"{B={} A={[This1.B]}}",Err.notInterfaceImplemented())
   ),new AtomicTest(()->
   top("{B={interface} A={[B]}}","{B={interface #typed{}} A={[This1.B]#typed{typeDep=This1.B}}#norm{}}")
   ),new AtomicTest(()->
   top("{C={B={method This m() #norm{typeDep= This0 This1.B }}} A={}}",
   "{C={B={imm method imm This0 m()#typed{typeDep=This0}}#typed{}}A={#typed{}}#norm{}}")
   ),new AtomicTest(()->
   top("{C={B={method This m() #norm{typeDep= This1.B }}} A={}}",
   "{C={B={imm method imm This0 m()#typed{typeDep=This0}}#typed{}}A={#typed{}}#norm{}}")

   ),new AtomicTest(()->
   topFail(PathNotExistent.class,"{[This1.A]}B= ={}",Err.pathNotExistant("This1.A"))
   ),new AtomicTest(()->
   top("{A={interface}B={interface[This1.A]}C={[This1.B, This1.A]}}","""
     {A={interface #typed{}}
      B={interface[This1.A]#typed{typeDep=This1.A}}
      C={[This1.B, This1.A]#typed{typeDep=This1.B, This1.A}}
      #norm{}     
      }""")
   ),new AtomicTest(()->
   top("{A={interface}B={interface[This1.A]}C={[This1.A, This1.B]}}","""
     {A={interface #typed{}}
      B={interface[This1.A]#typed{typeDep=This1.A}}
      C={[This1.A, This1.B]#typed{typeDep=This1.A, This1.B}}
      #norm{}
      }""")
   ),new AtomicTest(()->
   top("{A={interface}B={interface[This1.A]}C={[This1.B]}}","""
     {A={interface #typed{}}
      B={interface[This1.A]#typed{typeDep=This1.A}}
      C={[This1.B, This1.A]#typed{typeDep=This1.B, This1.A}}
      #norm{}
      }""")
    ),new AtomicTest(()->
    topFail(InvalidImplements.class,"{A={interface [B]} B={interface[A]}}",Err.nestedClassesImplemented(hole))
  
    ),new AtomicTest(()->
    top("{ method Void v()}","{ method Void v() #norm{}}")
    ),new AtomicTest(()->
    top("{ method Void v() method Any g(Any that)[Library]}",
    "{ method Void v() method Any g(Any that)[Library] #norm{}}")
    ),new AtomicTest(()->
    top("{A={interface method A a()} C={interface [A] method Void v()}}","""
     {A={interface method This a() #typed{typeDep=This0}} 
      C={interface [This1.A] method Void v()
          imm method imm This1.A a() #typed{typeDep=This1.A refined=a()}
          } #norm{}}
     """)
    ),new AtomicTest(()->
    top("{C={interface method A a()} A={interface [C]} B={interface [C]} D={interface[A,B]}}","""
    {C={interface method This1.A a() #typed{typeDep=This1.A}}
     A={interface [This1.C] method This a() #typed{typeDep=This1.C, This refined=a()}}
     B={interface [This1.C] method This1.A a() #typed{typeDep=This1.C, This1.A refined=a()}}
     D={interface[This1.A,This1.B,This1.C]imm method imm This1.A a() #typed{typeDep=This1.A, This1.B,This1.C refined=a()}}
    #norm{}}""")
    ),new AtomicTest(()->
    top("{C={interface method Any a()} A={interface [C] method Void a()} B={interface [C] method Any a()} D={interface[A,B]}}","""
    {C={interface method Any a() #typed{}}
     A={interface [This1.C] method Void a() #typed{typeDep=This1.C refined=a()}}
     B={interface [This1.C] method Any a() #typed{typeDep=This1.C refined=a()}}
     D={interface[This1.A,This1.B,This1.C]imm method imm Void a() #typed{typeDep=This1.A, This1.B,This1.C refined=a()}}
    #norm{}}""")
/*   ),new AtomicTest(()-> //TODO: when type system is available, need to test that the result is ill typed for Any<=Void
    top("{C={interface method Any a()} A={interface [C] method Void a()} B={interface [C] method Any a()} D={interface[B,A]}}","""
    {C={interface method Any a() #typed{}}
     A={interface [This1.C] method Void a() #typed{typeDep=This1.C}}
     B={interface [This1.C] method Any a() #typed{typeDep=This1.C}}
     D={interface[This1.B,This1.A,This1.C]imm method imm Any a() #typed{typeDep=This1.B, This1.A,This1.C}}
    #norm{}}""")*/
    ),new AtomicTest(()->
    topFail(InvalidImplements.class,
    "{C={interface } A={interface [C] method Void a()} B={interface [C] method Any a()} D={[B,A]}}",
    Err.moreThenOneMethodOrigin("a()", hole))
    ),new AtomicTest(()->
    topFail(InvalidImplements.class,"{A={interface method Any a()} B={interface method Any a()} C={[B,A]}}",Err.moreThenOneMethodOrigin("a()", hole))
    ),new AtomicTest(()->
    top("{I={interface method Any m()} A={interface[I]}}","""
    {I={interface method Any m()#typed{}}
     A={interface[This1.I] method Any m()#typed{typeDep=This1.I refined=m()}}
    #norm{}}""")
    ),new AtomicTest(()->
    top("{I2={interface method Any m2()} I1={interface method Any m1()} A={interface[I1,I2]}}","""
    {I2={interface method Any m2()#typed{}}
     I1={interface method Any m1()#typed{}}
     A={interface[This1.I1,This1.I2] method Any m1() method Any m2()#typed{typeDep=This1.I1,This1.I2 refined=m1(),m2()}}
    #norm{}}""")
    ),new AtomicTest(()->
    top("{I0={interface method Any m0()} I2={interface [I0] method Any m2()} I1={interface [I0] method Any m1()} A={interface[I1,I2]}}","""
    {I0={interface method Any m0()#typed{}}
     I2={interface [This1.I0] method Any m2()method Any m0()#typed{typeDep=This1.I0 refined=m0()}}
     I1={interface [This1.I0] method Any m1()method Any m0()#typed{typeDep=This1.I0 refined=m0()}}
     A={interface[This1.I1,This1.I2,This1.I0] method Any m1() method Any m0()method Any m2()#typed{typeDep=This1.I1,This1.I2,This1.I0 refined=m1(),m0(),m2()}}
    #norm{}}""")//TODO: is this really the method order we want? see also next test

    /*),new AtomicTest(()->TODO:when type system is available, need to test that the result is ill typed for Any<=Void
    top("{I0={interface method Any m0()} I2={interface [I0] method Any m2() method Void m0()} I1={interface [I0] method Any m1()} A={interface[I1,I2]}}","""
    {I0={interface method Any m0()#typed{}}
     I2={interface [This1.I0] method Any m2()method Void m0()#typed{typeDep=This1.I0}}
     I1={interface [This1.I0] method Any m1()method Any m0()#typed{typeDep=This1.I0}}
     A={interface[This1.I1,This1.I2,This1.I0] method Any m1() method Any m0() method Any m2()#typed{typeDep=This1.I1,This1.I2,This1.I0}}
    #norm{}}""")*/
    ),new AtomicTest(()->
    top("{I0={interface method Any m0()} I2={interface [I0] method Any m2() method Void m0()} I1={interface [I0] method Any m1()} A={interface[I2,I1]}}","""
    {I0={interface method Any m0()#typed{}}
     I2={interface [This1.I0] method Any m2()method Void m0()#typed{typeDep=This1.I0 refined=m0()}}
     I1={interface [This1.I0] method Any m1()method Any m0()#typed{typeDep=This1.I0 refined=m0()}}
     A={interface[This1.I2,This1.I1,This1.I0] method Any m2() method Void m0() method Any m1()#typed{typeDep=This1.I2,This1.I1,This1.I0 refined=m2(),m0(),m1()}}
    #norm{}}""")

 //WellFormedness
   ),new AtomicTest(()->
   top("{C={}}","{C={#typed{}}#norm{}}")
   ),new AtomicTest(()->
   top("{method Any foo()=void}","{method Any foo()=void #norm{}}")

   ),new AtomicTest(()->
   topFail(InvalidImplements.class,"{[I], I={interface }}",Err.nestedClassesImplemented(hole))
   ),new AtomicTest(()->
   topFail(InvalidImplements.class,"{A={[I]} J={interface method This m()} I={interface [J] method This m()}}",Err.nestedClassesImplemented(hole))

   ),new AtomicTest(()->
   top("{[I]}A= ={I={interface #norm{}} #norm{}}","{[This1.I] #norm{typeDep=This1.I}}")
   ),new AtomicTest(()->
   top("{[I]}A= ={J={interface #norm{}} I={interface [This1.J]#norm{typeDep=This1.J}} #norm{}}","{[This1.I,This1.J] #norm{typeDep=This1.I,This1.J}}")
   ),new AtomicTest(()->
   top("{[I]}A= ={J={interface method This m()#norm{typeDep=This}} I={interface [This1.J]#norm{typeDep=This1.J}}#norm{}}","{[This1.I,This1.J] method This1.J m() #norm{typeDep=This1.I,This1.J refined=m()}}")
   ),new AtomicTest(()->
   top("{[I]}A= ={J={interface method This m()#norm{typeDep=This}} I={interface [This1.J] method This m()#norm{typeDep=This1.J This}}#norm{}}","{[This1.I,This1.J] method This1.I m() #norm{typeDep=This1.I,This1.J refined=m()}}")

   ),new AtomicTest(()->
   top(
   "{J={interface method This m()} I={interface [J] method This m()} A={interface[I]} }",
   """
   {J={interface imm method imm This0 m()#typed{typeDep=This0}}
   I={interface[This1.J]imm method imm This0 m()#typed{typeDep=This1.J, This0 refined=m()}}
   A={interface[This1.I, This1.J]imm method imm This1.I m()#typed{typeDep=This1.I, This1.J refined=m()}}
   #norm{}}
   """)
   ),new AtomicTest(()->top("""
   {J={interface method This m()}
   I1={interface [J] method A m()}
   I2={interface [J] method This m()}
   A={interface[I1,I2]} }
   ""","""
   {J={interface imm method imm This0 m()#typed{typeDep=This0}}
   I1={interface[This1.J]imm method imm This1.A m()#typed{typeDep=This1.J, This1.A refined=m()}}
   I2={interface[This1.J]imm method imm This0 m()#typed{typeDep=This1.J, This0 refined=m()}}
   A={interface[This1.I1,This1.I2 This1.J]imm method imm This0 m()#typed{typeDep=This1.I1,This1.I2,This1.J,This0 refined=m()}}#norm{}}
   """)

   ),new AtomicTest(()->top("""
   {J={interface method This m()}
   I1={interface [J] method A m()}
   I2={interface [J] method This m()}
   A={[I1,I2] method m()=this} }
   ""","""
   {J={interface imm method imm This0 m()#typed{typeDep=This0}}
   I1={interface[This1.J]imm method imm This1.A m()#typed{typeDep=This1.J, This1.A refined=m()}}
   I2={interface[This1.J]imm method imm This0 m()#typed{typeDep=This1.J, This0 refined=m()}}
   A={[This1.I1,This1.I2 This1.J]imm method imm This0 m()=this #typed{typeDep=This1.I1,This1.I2,This1.J,This0 refined=m()}}#norm{}}
   """)
   ),new AtomicTest(()->top("""
   {J={interface method This m()}
   I1={interface [J] method B.C.A m()}
   I2={interface [J] method This m()}
   B={C={A={[I1,I2] method m()=this}}} }
   ""","""
   {J={interface imm method imm This0 m()#typed{typeDep=This0}}
    I1={interface[This1.J]imm method imm This1.B.C.A m()#typed{typeDep=This1.J, This1.B.C.A refined=m()}}
    I2={interface[This1.J]imm method imm This0 m()#typed{typeDep=This1.J, This0 refined=m()}}
    B={C={A={[This3.I1, This3.I2, This3.J]imm method imm This0 m()=this #typed{typeDep=This3.I1, This3.I2, This3.J,This0 refined=m()}}#typed{}}#typed{}}#norm{}}
   """)

   ),new AtomicTest(()->top("""
   {A=({#norm{}})}
   ""","""
   {A={#typed{}}#norm{}}
   """)
   
   ),new AtomicTest(()->topFail(EndError.InvalidImplements.class,"""
   {I={interface[This]}}
   """,Err.nestedClassesImplemented(hole))

  //strings printing ba
  ),new AtomicTest(()->
  top("""
    {
      S={
        class method This0 of()
        method This0 sum(This0 that)=native{trusted:OP+} error void
        #norm{nativeKind=String nativePar=This1.PE declaresClassMethods typeDep=This0,This1.PE}
        }
      PE={#norm{nativeKind=LazyMessage}}
      Debug={
        class method Void #apply(This1.S that)=(This d=This<:class This.of() d.strDebug(that=that))
        class method This0 of()        
        method Void strDebug(This1.S that)=native{trusted:strDebug} error void
        method Void deployLibrary(This1.S that,Library lib)=native{trusted:deployLibrary} error void
        #norm{declaresClassMethods nativeKind=TrustedIO typeDep=This0 This1.S coherentDep=This0}        
        }
      SB={
        class method mut This0 of()
        mut method Void #a()=native{trusted:'a'} error void
        mut method Void #b()=native{trusted:'b'} error void
        read method This1.S toS()=native{trusted:toS} error void
        #norm{nativeKind=StringBuilder,declaresClassMethods typeDep=This0 This1.S coherentDep=This0}
        }
      C=(
        mut SB sb=SB.of()
        sb.#b()
        sb.#a()
        Debug(sb.toS())
        Debug.of().deployLibrary(sb.toS(), lib={
          A={
            class method Library foo()={method Void retrived() #norm{}}
            #norm{declaresClassMethods}}
          #norm{}})
        {#norm{}}
        )
      }
    ""","""
      {S={
        class method imm This0 of()
        imm method imm This0 sum(imm This0 that)=native{trusted:OP+}error void
        #typed{nativeKind=String nativePar=This1.PE declaresClassMethods typeDep=This0,This1.PE}
        }
      PE={#typed{nativeKind=LazyMessage}}
      Debug={
         class method imm Void #apply(imm This1.S that)=(imm This0 d=This0<:class This0.of()d.strDebug(that=that))
         class method imm This0 of()
         imm method imm Void strDebug(imm This1.S that)=native{trusted:strDebug}error void
         imm method imm Void deployLibrary(imm This1.S that, imm Library lib)=native{trusted:deployLibrary}error void
         #typed{declaresClassMethods nativeKind=TrustedIO typeDep=This0 This1.S coherentDep=This0}
         }
      SB={
        class method mut This0 of()
        mut method Void #a()=native{trusted:'a'} error void
        mut method Void #b()=native{trusted:'b'} error void
        read method This1.S toS()=native{trusted:toS} error void
        #typed{nativeKind=StringBuilder,declaresClassMethods typeDep=This0 This1.S coherentDep=This0}
        }
      C={#typed{}}
      #norm{}}
    """)

  ),new AtomicTest(()->
  top("""
    {reuse[ba]
     C=A.foo() 
    }
    ""","""
    {
      A={
        class method imm Library foo()={imm method imm Void retrived()#typed{}}
        #typed{declaresClassMethods}}
      C={imm method imm Void retrived()#typed{}}
      #norm{}}
    """)

  ),new AtomicTest(()->
  top("""
    {
      B={A={
        class method Library of()={#norm{}}
        }}
      C=B.A.of()
      }
    ""","""
    {B={A={class method imm Library of()={#typed{}}
       #typed{declaresClassMethods}}#typed{}}
    C={#typed{}}#norm{}}
    """)
  ),new AtomicTest(()->
  top("""
    {
      B={A={
        class method Library of()={method This3.B m() #norm{typeDep=This3.B}}
        }}
      C=B.A.of()
      }
    ""","""
    {B={A={
      class method imm Library of()={imm method imm This2 m()#typed{typeDep=This2}}
      #typed{typeDep=This1 declaresClassMethods}}#typed{}}
    C={imm method imm This1.B m()#typed{typeDep=This1.B}}#norm{}}
    """)
 //try-catch
  ),new AtomicTest(()->
  top(tryCatchTest("""
        Void v=void
        catch error Void x (A.b())
        A.a()
        """),tryCatchRes("a"))
  ),new AtomicTest(()->
  top(tryCatchTest("""
        Void v=A.throwErr()
        catch error Void x (A.b())
        A.a()
        """),tryCatchRes("b"))
  ),new AtomicTest(()->
  top(tryCatchTest("""
        Void v=void
        catch error Void x (A.a())
        A.b()
        """),tryCatchRes("b"))
  ),new AtomicTest(()->
  top(tryCatchTest("""
        Void v=A.throwErr()
        catch error Void x (A.a())
        A.b()
        """),tryCatchRes("a"))

  //a running "if", finally, after 4 months of work no stop...
  ),new AtomicTest(()->
  top("""
    {
      B={
        class method This0 false()
        class method This0 true()=(This false=this.false() false.not())
        method This0 #if()=this
        method Void #checkTrue()[Void]=native{trusted:checkTrue} error void
        method This0 not()=native{trusted:OP!} error void
        method This0 and(This0 that)=native{trusted:OP&} error void
        method This0 or(This0 that)=native{trusted:OP|} error void
        #norm{nativeKind=Bool,declaresClassMethods typeDep=This}
        }
      C=if B.true().and(B.true()) {imm method imm Void a()#typed{}}
        else {imm method imm Void b()#typed{}}
      }
    ""","""
    {B={
      class method imm This0 false()
      class method imm This0 true()=(imm This0 false=this.false()false.not())
      imm method imm This0 #if()=this
      imm method imm Void #checkTrue()[Void]=native{trusted:checkTrue}error void
      imm method imm This0 not()=native{trusted:OP!}error void
      imm method imm This0 and(imm This0 that)=native{trusted:OP&}error void
      imm method imm This0 or(imm This0 that)=native{trusted:OP|}error void
      #typed{typeDep=This0 nativeKind=Bool,declaresClassMethods}
      }
    C={imm method imm Void a()#typed{}}#norm{}}
    """)
  ),new AtomicTest(()->
  topFail(InvalidImplements.class,"{A={...}}",Err.dotDotDotSouceNotExistant(hole))
  ),new AtomicTest(()->
  top("""
  {TestingDotDotDot={...}}
  ""","""
  {TestingDotDotDot={
    imm method imm@{hei!} Void v() = void
    AA={#typed{}@{it is there}}#typed{}}
  #norm{}}
  """)
  ),new AtomicTest(()->
  topFail(EndError.TypeError.class,"""
  {A={class method This() mut method Void foo(Void v)}
  Test=(
    A a=A()
    a.foo(v=void)
    )
  }
  """,
  Err.methCallNoCompatibleMdfParametersSignature(hole,hole)
  )));}

  private static String tryCatchTest(String s){
    return """
      { A={
          class method Void throwErr()=error void
          class method Library a()={method Void a() #norm{}}
          class method Library b()={method Void b() #norm{}}
          }
        C=("""+s+"""
          )
        }
        """; 
    };
  private static String tryCatchRes(String s){
    return """
      {A={
        class method Void throwErr()=error void
        class method imm Library a()={imm method imm Void a()#typed{}}
        class method imm Library b()={imm method imm Void b()#typed{}}
        #typed{declaresClassMethods}
        }
      C={imm method imm Void """//in Java multiline strings autotrims :-(
      +" "+s+"() #typed{}}#norm{}}";
   };

public static void top(String program,String out){
  Resources.clearRes();
  Constants.testWithNoUpdatePopChecks(()->{
    Init init=new Init(program);
    assertEquals(init.top.top(init.p).top,Core.L.parse(out));
    });
  }
public static void topFail(Class<?> kind,String program,String ...output){
  Resources.clearRes();
  checkFail(()->Constants.testWithNoUpdatePopChecks(()->{
    Init init=new Init(program);
    init.top.top(init.p);
    }), output, kind);
  }
}