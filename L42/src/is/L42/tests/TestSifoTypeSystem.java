package is.L42.tests;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.common.EndError.InvalidImplements;
import is.L42.common.EndError.PathNotExistent;
import is.L42.common.EndError.TypeError;
import is.L42.common.EndError.CoherentError;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.E;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Full;
import is.L42.generated.LDom;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import is.L42.tools.AtomicTest;
import is.L42.top.CachedTop;
import is.L42.top.Init;
import is.L42.top.State;
import is.L42.translationToJava.Loader;
import is.L42.typeSystem.PathTypeSystem;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.typeSystem.TypeManipulation;
import is.L42.typeSystem.Coherence;
import is.L42.typeSystem.MdfTypeSystem;
import is.L42.visitors.FullL42Visitor;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;
import static is.L42.generated.LDom._elem;
import static org.junit.jupiter.api.Assertions.*;

public class TestSifoTypeSystem
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("A={B={method Void main()=void}}")   
   ),new AtomicTest(()->
   pass("""
     A={
       Left={interface @{securityLevel}}
       Right={interface@{securityLevel}}
       Top={interface [Left,Right] @{securityLevel}}
       A={class method This()}
       B={class method @Left A main(@Left A a)=a}//should pass       
       }""")   
   ),new AtomicTest(()->
   fail("""
       A={
         Left={interface @{securityLevel}}
         Right={interface@{securityLevel}}
         Top={interface [Left,Right] @{securityLevel}}
         A={class method This()}
         B={class method @Left A main(@Right A a)=a}//should pass       
         }""",
       "MySifoError in line[###]: in method ...")   
     ),new AtomicTest(()->
   fail("A={B={method Library main()=void}}",Err.invalidExpectedTypeForVoidLiteral(hole))
   ),new AtomicTest(()->
   pass("A={class method Void v()=void B={method class A main()=A<:class A}}")   
   ),new AtomicTest(()->
   fail("A={class method Void v()=void B={method class Void main()=A<:class A}}",Err.subTypeExpected(hole,hole))   
   ),new AtomicTest(()->
   fail("A={class method Void v()=void B={method class Void main()=A<:class Void}}",Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->
   pass("A={B={method class A main()=A<:class A}}")//we now allow A<:class A even if no class methods on A   
   ),new AtomicTest(()->
   fail("A={interface class method Void v() B={method class A main()=A<:class A}}",Err.castOnPathOnlyValidIfNotInterface(hole))
   ),new AtomicTest(()->
   pass("A={B={method class Any main()=A<:class Any}}")
   ),new AtomicTest(()->
   pass("A={B={method Any main()={#norm{}}}}")
   ),new AtomicTest(()->
   fail("A={B={method Void main()={#norm{}}}}",Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->
   pass("A={B={method This main()=this}}")
   ),new AtomicTest(()->
   pass("A={B={method Void main()=loop this.main()}}")
   ),new AtomicTest(()->
   fail("A={B={method Library main()=loop this.main()}}",Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->
   pass("""
   A={B={
     method Void main()=error B()
     class method B()
     }}
     """)
   ),new AtomicTest(()->
   fail("""
   A={B={
     method Void main()=exception B()
     class method B()
     }}
     """,Err.leakedThrow(hole))
   ),new AtomicTest(()->
   fail("""
   A={B={
     method Void main()[Void]=exception B()
     class method B()
     }}
     """,Err.leakedThrow(hole))
   ),new AtomicTest(()->
   pass("""
   A={B={
     method Void main()[B]=exception B()
     class method B()
     }}
     """)
   ),new AtomicTest(()->
   pass("""
   A={B={
     method Void main()= B().main()
     class method B()
     }}
     """)
   ),new AtomicTest(()->
   fail("""
   A={B={
     method Void main()= this.surprise()
     class method B()
     }}
     """,Err.methodDoesNotExists(hole,hole))
   ),new AtomicTest(()->
   fail("""
   A={B={
     method Void main()= this.leaking()
     class method B()
     method Void leaking()[B]=void
     }}
     """,Err.leakedExceptionFromMethCall(hole))
   ),new AtomicTest(()->
   pass("""
   A={B={
     method Void main()[B]= B().leaking()//testing block by desugaring
     class method B()
     method Void leaking()[B]=void
     }}
     """)
   ),new AtomicTest(()->
   fail("""
   A={B={
     method Void main()= B().leaking()//testing block by desugaring
     class method B()
     method Void leaking()[B]=void
     }}
    """,Err.leakedExceptionFromMethCall(hole))
   ),new AtomicTest(()->
   pass("""
   A={B={
     method Void main()[B]=(
       B b1=B()
       B b2=B()
       this.many(b1,other=b2)
       )
     class method B()
     method Void many(B that,B other)=void
     }}
     """)
   ),new AtomicTest(()->
   pass("""
   A={B={
     method Void main()=(
       B b1=B()
       B b2=B()
       this.many(b1,other=b2)
       )
     class method B()
     method Void many(B that,B other)=void
     }}
     """)
   ),new AtomicTest(()->
   pass("""
   A={B={
     method Void main()=(
       B b1=B()
       B b2=B()
       catch Void v (v)
       this.many(b1,other=b2)
       )
     class method B()
     method Void many(B that,B other)=void
     }}
     """)
   ),new AtomicTest(()->
   fail("""
   A={B={
     method Void main()=(
       Void v0=void
       catch Void v (v<:Any)
       void
       )
     }}
     """,Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->
   fail("""
   A={B={
     method Void main()=(
       Void b1=void
       B b2=B()
       this.many(b1,other=b2)
       )
     class method B()
     method Void many(B that,B other)=void
     }}
     """,Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->pass("""
   A={B={method read Any main()=(exception void catch exception Void  x void)}}
   """)
   ),new AtomicTest(()->pass("""
   A={B={method Any main()[Any]=(
     (exception void catch exception Any x exception x)
     catch exception Void xOut (void) void)
   }}
   """)
   ),new AtomicTest(()->pass("""
   A={B={method class Any main()=Any}}
   """)
   ),new AtomicTest(()->pass("""
   A={B={method class Any main()=Library}}
   """)
   ),new AtomicTest(()->pass("""
   A={B={method class Any main()=Void}}
   """)
   ),new AtomicTest(()->pass("A={B={method class Void main()=Void}}")
   //now it is allowed to return class Void, before was prevented since Void has no class methods
   ),new AtomicTest(()->pass("""
   C={class method This k()}A={B={method C main()=C.k()}}
   """)
   ),new AtomicTest(()->pass("""
   C={class method mut This k()}A={B={method C main()=C.k()}}
   """)
   ),new AtomicTest(()->pass("""
   C={class method mut This k()}A={B={method capsule C main()=C.k()}}
   """)
   ),new AtomicTest(()->pass("""
   C={class method mut This k()}A={B={method read C main()=C.k()}}
   """)
   ),new AtomicTest(()->pass("""
   C={class method mut This k()}A={B={method lent C main()=C.k()}}
   """)
      ),new AtomicTest(()->pass("""
   C={class method mut This k()}A={B={method mut C main()=C.k()}}
   """)
   ),new AtomicTest(()->fail("""
   C={class method mut This k()}A={B={method class C main()=C.k()}}
   """,Err.methCallResultIncompatibleWithExpected(hole,hole))
   ),new AtomicTest(()->pass("""
   D={class method This k()}A={B={method read Any main()=error D.k()}}
   """)
   ),new AtomicTest(()->pass("""
   D={var D f class method This k(fwd imm D f)}
    A={B={method read Any main()=( D x=D.k(f=x), x)}}
   """)
   ),new AtomicTest(()->pass(listExample+"""
   A={B={method List main()=List.factory(N.k())}}
   """)
   ),new AtomicTest(()->pass(listExample+"""
   A={B={method List main()=(
     class List wthis=List,
     N that=N.k(),
     List x=wthis.factoryAux(that,top=x)
     x
     )}}
   """)
   ),new AtomicTest(()->pass(listExample+"""
   A={B={method List main()=(
    class List wthis=List
    N that=N.k()
    fwd imm List top=List.factory(N.k())
    ( Void z=that.checkZero()
      catch error Void  x (List.k(
        next=List.factoryAux(that.lessOne(),top=top)
        elem=that))
      List.k(next=top,elem=N.k()))
     )}}
   """)
   ),new AtomicTest(()->pass("""
   D={var Any f, class method This k(fwd imm Any f)}
   A={B={method D main()=D.k(f=( fwd imm D x=D.k(f=void), x))}}
   """)
   ),new AtomicTest(()->pass("""
   D={var Any f, class method mut This k(fwd imm Any f)}
   A={B={method D main()=D.k(f=( fwd imm D x=D.k(f=void), D.k(f=x)))}}
   """)
   ),new AtomicTest(()->pass("""
   D={var Any f, class method mut This k(fwd imm Any f)}
   A={B={method mut D main()=( fwd imm D x=D.k(f=void), D.k(f=x))}}
   """)
   ),new AtomicTest(()->pass("""
   D={Any f, class method This k(fwd imm Any f)}
   A={B={method D main()=( fwd imm D x=D.k(f=void), D.k(f=x))}}
   """)
   ),new AtomicTest(()->pass("""
   D={class method mut This k(fwd imm Any f)}
   A={B={method D main()=( D x=D.k(f=void), D.k(f=x))}}
   """)
   ),new AtomicTest(()->pass("""
   C={class method This k()}
   D={class method This k()}
   A={B={method Library main()=(
     Void unused1=(
       exception C.k()
       catch exception D x return {#norm{}}
       catch exception C x error void
       )
     catch return  Library result0 (result0)
     error void
   )}}
   """)
   ),new AtomicTest(()->pass(
   "A={B={method Void main()=void}}")
   ),new AtomicTest(()->pass("""
   C={class method mut This #mutK()}
   A={B={method C main()=C.#mutK()}}
   """)
   ),new AtomicTest(()->pass("""
   C={var mut D f class method mut This #mutK(mut D f)}
   D={class method mut This #mutK()}
   A={B={method D main()=C.#mutK(f=D.#mutK())<:C.f()}}
   """)//TODO: Discuss: this is easy to misread as <:(C.f()). Would [<C] be better? any other alternative?
   ),new AtomicTest(()->pass("""
   C={var mut D f class method mut This #mutK(mut D f)}
   D={class method mut This #mutK()}
   A={B={method mut D main()=C.#mutK(f=D.#mutK()).#f()}}
   """)
   ),new AtomicTest(()->pass("""
   C={var mut D f class method mut This #mutK(mut D f)}
   D={class method mut This #mutK()}
   A={B={method read D main()=C.#mutK(f=D.#mutK()).f()}}
   """)
   ),new AtomicTest(()->pass(cloneExample+"""
   A={B={method Void main()=(
     lent Vector v=Vector.#mutK(),
     mut BB b=BB.#mutK(N.#mutK()),
     v.add(b.clone())
     )}}
   """)
   ),new AtomicTest(()->pass(cloneExample+"""
   A={B={method AA main()=(
     AA a=AA.k(f=BB.k(N.k()).clone()) a
     )}}
   """)
   ),new AtomicTest(()->pass(cloneExample+"""
   A={B={method AA main()=(
     mut BB b=BB.k(N.k())
     AA a=AA.k(f=b.clone())
     a
     )}}
   """)
   ),new AtomicTest(()->pass(cloneExample+"""
   A={B={method BB main()=(
     BB bi=AA.k(f=(read BB b=BB.k(N.k()) b).clone())<:AA.f()
     bi
     )}}
   """)
   ),new AtomicTest(()->pass(cloneExample+"""
   A={B={method BB main()=(
     read BB b=BB.k(N.k())
     BB bi=AA.k(f=b.clone())<:AA.f() 
     bi
     )}}
   """)
   ),new AtomicTest(()->pass("""
   AI={class method mut This #mutK()}
   A={B={method AI main()=(
     AI any=(mut AI aI=AI.#mutK() aI)//block capsule promotion 
     any
     )}}
   """)
   ),new AtomicTest(()->pass("""
   D={class method mut This k()}
   A={B={method read Any main()=error D.k()}}
   """)
   ),new AtomicTest(()->pass("""
   D={
     class method mut This ()
     mut method Void m(mut D that)=error void 
     }
   A={B={method read Void main()=D().m(D())}}
   """)
   ),new AtomicTest(()->pass("""
   D={
     class method mut This ()
     mut method Void m(mut D that)=error void 
     }
   A={B={method read Void main()=(
     mut D x=D()
     mut D y=D()
     x.m(y)
     )}}
   """)
   ),new AtomicTest(()->pass("""
   D={
     class method mut This ()
     mut method mut D m(mut D that)=error void 
     }
   A={B={
     method lent D main()=(
       lent D x=D()
       This.doXM(x=x)
       )
     class method mut D doXM(mut D x)=(mut D y=D() x.m(y))
     }}
   """)
   ),new AtomicTest(()->fail("""
   D={
     class method mut This ()
     mut method Void m(mut D that)=error void 
     }
   A={B={
     method mut D main()=(
       lent D x=D()
       This.doXM(x=x)
       )
     class method mut D doXM(mut D x)=(mut D y=D() x.m(y))
     }}
   """,Err.methCallNoCompatibleMdfParametersSignature(hole,hole))
   ),new AtomicTest(()->pass("""
   Customer={ class method mut This() }
   Reader={class method This()
     class method capsule Customer readCustomer()=(
       mut Customer c=Customer()
       c)}//ok, capsule promotion here
   A={B={method capsule Customer main()=Reader.readCustomer()}}
   """)
   ),new AtomicTest(()->pass("""
   Customer={ class method mut This() }
   Reader={class method This()
     class method capsule Customer readCustomer()={
       return Customer()}}
   A={B={method capsule Customer main()=Reader.readCustomer()}}
   """)
   ),new AtomicTest(()->pass("""
   Customer={ class method mut This() }
   Reader={class method This()
     class method capsule Customer readCustomer()=(
      mut Customer c=Customer()
      return c //ok, capsule promotion here
      catch return mut Customer x x
      )}
   A={B={method capsule Customer main()=Reader.readCustomer()}}
   """)
   ),new AtomicTest(()->pass("""
   Customer={ class method mut This() }
   Reader={class method This()
     class method capsule Customer readCustomer()={
      mut Customer c=Customer()
      return c //ok, capsule promotion here
      }}
   A={B={method capsule Customer main()=Reader.readCustomer()}}
   """)
   ),new AtomicTest(()->pass("""
   C={mut C that class method mut This(fwd mut C that)}
   A={B={method C main()=(mut C x=C(x) x)}}
   """)
   ),new AtomicTest(()->fail("""
   C={mut C that class method mut This(fwd mut C that)}
   A={B={method capsule C main()=(mut C x=C(x)\n(capsule C y=x y))}}
   """,Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->fail("""
   C={mut C that class method mut This(fwd mut C that)}
   A={B={method capsule C main()=(mut C x=C(x)\n(capsule C y=x.#that() y))}}
   """,Err.methCallNoCompatibleMdfParametersSignature(hole,hole))
   ),new AtomicTest(()->fail(cloneExample+"""
   C={mut C that class method mut This(fwd mut C that)}
   A={B={method capsule AA main()=(mut BB b=BB.k(N.k()), AA a=AA.k(f=b) a)}}
   """,Err.methCallNoCompatibleMdfParametersSignature(hole,hole))
   ),new AtomicTest(()->pass("""
   A={B={
   class method This of()
   method This0 main(This0 that)=native{trusted:OP+} error void
   #norm{typeDep=This0 coherentDep=This nativeKind=Int}
   }}
   """)
   ),new AtomicTest(()->fail("""
   A={B={
   class method This of()
   method This0 main(This1 that)=native{trusted:OP+} error void
   #norm{typeDep=This0 This1 watched=This1 coherentDep=This nativeKind=Int}
   }}
   """,Err.nativeParameterInvalidKind(hole,"method This main(This1 that)",hole,hole,"Int"))
   ),new AtomicTest(()->pass("""
   A={B={ class method This()
   method Void main()=(
     exception B()
     catch exception Any x (void)     
     )
   }}
   """)
   ),new AtomicTest(()->fail("""
   A={B={ class method mut This()  mut method Void m(mut This that)=error void
   method read Any main()=(
     mut B x=B() lent B y=B() x.m(y)     
     )
   }}
   """,Err.methCallNoCompatibleMdfParametersSignature(hole,hole))
   ),new AtomicTest(()->fail("""
   D={class method This() class method Void foo()=exception D()}
   A={B={method read Any main()=D.foo()}}
   """,Err.leakedThrow(hole))
   ),new AtomicTest(()->fail("""
   D={class method This()
     class method Void foo()=this.bar()
     class method Void bar()[D]=void
     }
   A={B={method read Any main()=D.foo()}}
   """,Err.leakedExceptionFromMethCall(hole))

   ),new AtomicTest(()->fail("""
   C={
     E={class method Void foo()=This1.foo()} 
     class method Library foo()=D.foo()
     }
   D={class method Void foo()=C.E.foo()}
   """,Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->fail("""
   C={
     E={class method Void foo()=This1.foo()}
     class method Library foo()=D.foo()
     }
   D={class method Void foo()=C.E.foo()}
   """,Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->fail("""
  K={
    E={class method Any foo()=This1.foo()}
    }
  C={class method Void foo()=D.foo()}
  D={class method Library foo()=K.E.foo()}
  """,Err.methodDoesNotExists(hole,hole))
  ),new AtomicTest(()->fail("""
  class method Any  foo()=exception void
  """,Err.leakedThrow(hole))
  ),new AtomicTest(()->fail("""
  class method Any foo()[This]=exception void
  """,Err.leakedThrow(hole))
  ),new AtomicTest(()->fail("""
  class method Void mayFail()=void
  class method Any foo()[This]={return (
    This.mayFail()
    exception void
    catch error Void x x
    )}
  """,Err.leakedThrow(hole))
  ),new AtomicTest(()->pass("""
  C={method Void()=void}
  """)
  ),new AtomicTest(()->pass("""
  C={method Void()}
  """)
  ),new AtomicTest(()->fail("""
  C={method Void()=this}
  """,Err.subTypeExpected(hole,hole))
  ),new AtomicTest(()->pass("""
  C={method This()=this}
  """)
  ),new AtomicTest(()->pass("""
  C={method This()=this()}
  """)
  ),new AtomicTest(()->fail("""
  C={method class This()=this}
  """,Err.subTypeExpected(hole,hole))
  ),new AtomicTest(()->fail("""
  C={method This()=this.foo() method Any foo()=this} 
  """,Err.subTypeExpected(hole,hole))
  ),new AtomicTest(()->pass("""
  C={method D()=this() }
  D={class method This()}
  """)
  ),new AtomicTest(()->pass("""
  C={method Library()=(x=void catch error Library y (y) {#norm{}})} 
  """)
  ),new AtomicTest(()->pass("""
  C={class method Void foo()=This.foo()}
  """)
  ),new AtomicTest(()->pass("""
  A={
    class method This()
    method A #iterator()=this
    method Void #close(N that)=void
    method B #incomplete(N that)=B()
    method N #startIndex()=N()
    method B #hasElem(N that)=B()
    method E #elem#default(N that)=E()
    }
  N={
    class method This ()
    method This #succ()=this
    }
  E={
    class method This ()
    }
  B={
    class method This ()
    method This #shortProcess#andand(This that,This other)=this
    method This #shortResult#andand(This that)=this
    method This #shortCircut#andand()=this
    method This #if()=this
    method Void #checkTrue()[Void]=void
    }
  Test={
    class method Void foo()[B]=(
      A list=A()
      for E x in list (exception B())
      )
    }
  """)
  ),new AtomicTest(()->fail("""
  A={class method This ()}
  B={class method This ()}
  Test={ class method Void foo()[A]=( exception B() ) }
  """,Err.leakedThrow(hole))
  ),new AtomicTest(()->fail("""
  Top={class method Library ()={
    A={class method This k() #norm{typeDep=This}}
    B={class method This k() #norm{typeDep=This}}
    Test={
      class method Void foo()[This1.A]=
        exception This1.B<:class This1.B.k() 
      #norm{typeDep=This1.A This1.B coherentDep=This1.B usedMethods=This1.B.k()}}
    #norm{}}}
  """,Err.leakedThrow(hole))
  ),new AtomicTest(()->pass("""
  C={class method Void foo()=D.foo()}
  D={class method Void foo()=(void)}
  """)
  ),new AtomicTest(()->pass("""
  C={
    E={class method Void foo()=This1.foo()}
    class method Void foo()=D.foo()
    }
  D={class method Void foo()=C.E.foo()}
  """)
  ),new AtomicTest(()->pass("""
  K={ E={class method Void foo()=This2.C.foo()} }
  C={class method Void foo()=D.foo()}
  D={class method Void foo()=K.E.foo()}
  """)
  ),new AtomicTest(()->pass("""
  C={ method Void foo()=(This0 x=this void) }
  """)
  ),new AtomicTest(()->pass("""
  C={ method Void foo()=(C x=this void) } 
  """)
  ),new AtomicTest(()->pass("""
  K={ E={class method C foo1()=C.foo2()} }
  C={ class method C foo2()=D.foo3() }
  D={ class method C foo3()=K.E.foo1() } 
  """)
  ),new AtomicTest(()->pass("""
  K={ E={ class method C foo1()=(D.foo3()<:C.foo2()<:C.foo2()) } }
  C={ method C foo2()=D.foo3() }
  D={ class method C foo3()=K.E.foo1() } 
  """)
  ),new AtomicTest(()->fail("""
  K={ E={ class method C foo1()=D.foo3()<:C.foo2()<:C.foo2() } }
  C={ class method C foo2()=D.foo3() }
  D={ class method C foo3()=K.E.foo1() }   
  """,Err.methCallNoCompatibleMdfParametersSignature(hole,hole))
  //TODO: consider a more specific error as ErrorKind.ClassMethCalledOnNonClass
  //in general this above is a poor error
  ),new AtomicTest(()->pass("""
  method This m()=this.readM(this)
  method read This readM(read This that)=that
  """)
  ),new AtomicTest(()->pass("""
  method This m()=this.readM()
  read method read This readM()=this
  """)
  ),new AtomicTest(()->pass("""
  class method mut This()
  method This m()=This()
  """)
  ),new AtomicTest(()->pass("""
  class method mut This()
  method This m()=( mut This x=This() x) 
  """)
//cohence tests from here on
  ),new AtomicTest(()->pass("""
  """)
  ),new AtomicTest(()->pass("""
  class method mut This()
  """)
  ),new AtomicTest(()->pass("""
  class method This()
  """)
  ),new AtomicTest(()->pass("""
  class method read This()
  """)
  ),new AtomicTest(()->failC("""
  class method class This()
  """,Err.nonCoherentMethod(hole))
  ),new AtomicTest(()->failC("""
  A={}    A a
  class method fwd imm This(fwd imm A a)
  """,Err.nonCoherentMethod(hole))
  ),new AtomicTest(()->failC("""
  A={}    A a
  class method fwd mut This(fwd imm A a)
  """,Err.nonCoherentMethod(hole))
  ),new AtomicTest(()->pass("""
  A={}    A a
  class method This(fwd imm A a)
  """)
  ),new AtomicTest(()->pass("""
  mut method mut This unusable()[This]
  """)
  ),new AtomicTest(()->pass("""
  A={}    A a
  class method This(fwd imm A a)
  mut method mut This unusable()[This]
  """)
  ),new AtomicTest(()->failC("""
  A={}    A a
  class method capsule This(A a)
  mut method mut This unusable()[This]
  """,Err.nonCoherentMethod("unusable()"))
  ),new AtomicTest(()->pass("""
  A={} B={}   A a mut B b
  class method mut This k1(fwd imm A a, mut B b)
  class method mut This k2(mut B b, fwd imm A a)
  mut method mut B ###b()
  """)
  ),new AtomicTest(()->failC("""
  B={}   mut B b
  class method This k1(mut B b)
  """,Err.nonCoherentMethod("k1(b)"))//fails since mut B can not be used for imm result
  ),new AtomicTest(()->failC("""
  A={} B={}   A a mut B b
  class method mut This k1(fwd imm A a, mut B b)
  class method mut This k2(mut B b, fwd imm A a)
  mut method mut A ###a()
  """,Err.nonCoherentMethod("a()"))//fails for a() since ###a() can not be mut... sad error?  
  ),new AtomicTest(()->pass("""
  X={}
  class method lent This foo(lent X x, lent X y)
  mut method lent X x()
  mut method lent X y()
  """)
  ),new AtomicTest(()->pass("""
  S={} N={}
  class method mut This a(S x)
  class method imm This b(N x)
  mut method S #x()
  read method Any x()
  //mut method Void x(N that) //enabling this method makes #x not valid for coherence
  """)
  ),new AtomicTest(()->failC("""
  S={} N={}
  class method mut This a(S x)
  class method imm This b(N x)
  mut method S #x()
  read method Any x()
  mut method Void x(N that) //enabling this method makes #x not valid for coherence
  """,Err.nonCoherentMethod("#x()"))
  ),new AtomicTest(()->pass("""
  S={} N={}//let see what are all the accepted state methods
  class method mut This a(S x)
  class method imm This b(N x)
  mut method S #x()
  mut method Void ##x(S that)
  mut method Any ##x()
  read method Any x()
  imm method Any ###x()//N would be invalid
  """)
  ),new AtomicTest(()->failC("""
  S={} N={}//let see what are all the accepted state methods
  class method mut This a(S x)
  class method imm This b(N x)
  mut method S #x()
  mut method Void ##x(S that)
  mut method Any ##x()
  read method Any x()
  imm method N ###x()//N would be invalid
  """,Err.nonCoherentMethod("###x()"))
  ),new AtomicTest(()->failC("""
  NotCoh={This field, class method This ()}
  """,Err.nonCoherentMethod("field()"))
  ));}

public static void pass(String program){
  Resources.clearResKeepReuse();
  class StateForTest extends State{
    public StateForTest(FreshNames f,ArrayList<HashSet<List<C>>>c, int u,ArrayList<SClassFile> b,ArrayList<L42£Library> l){
      super(f,c,u,b,l);
      }
    @Override protected Program flagTyped(Program p) throws EndError{return p;}
    @Override public State copy(){
      return new StateForTest(freshNames.copy(),copyAlreadyCoherent(),
        uniqueId,new ArrayList<>(allByteCode),new ArrayList<>(allLibs));
      }
    }
  Init init=new Init("{"+program+"}"){
    @Override protected State makeState(){
      return new StateForTest(f,new ArrayList<>(),0,new ArrayList<>(),new ArrayList<>());
      }
    };
  Program p=Program.flat(init.topCache(new CachedTop(L(),L())));
  ProgramTypeSystem.type(true, p);
  TestTypeSystem.allCoherent(p);
  new TypeAllMeth(p).visitL(p.topCore());
  }
static class TypeAllMeth extends is.L42.visitors.PropagatorCollectorVisitor{
  Program p;TypeAllMeth(Program p){this.p=p;}
  @Override public void visitNC(Core.L.NC nc){
     Program oldP=p;
     p=p.push(nc.key(),nc.l());
     try{visitL(nc.l());}
     finally{p=oldP;}
    }
  @Override public void visitMWT(Core.L.MWT m){
    if(m._e()!=null){typeMethESifo(p,m.mh(),m._e());}
    }
  }
private static void typeMethESifo(Program p,MH mh, E e){
  var g=G.of(mh);
  var mdf=TypeManipulation.fwdPOf(mh.t().mdf());
  e.visitable().accept(new MdfTypeSystem(p,g,Collections.emptySet(),mdf));
  }
public static void failC(String program,String...out){
  checkFail(()->{
    pass(program);
    }, out, CoherentError.class);  }

public static void fail(String program,String...out){
  checkFail(()->{
    pass(program);
    }, out, TypeError.class);
  }
public static String listExample="""
  N={
    class method This k()
    method Void checkZero()=void 
    method N lessOne()=this
    }
  List={
    class method This k(fwd imm List next, N elem)
    class method List factory(N that)=(
      List x=this.factoryAux(that,top=x)
      x
      )
    class method List factoryAux(N that,fwd imm List top)=(
      Void z=that.checkZero()
      catch error Void x (
        List.k(
          next=List.factoryAux(that.lessOne(),top=top)
          elem=that
          ))
      List.k(next=top,elem=N.k())
      )
    }
  """;
public static String cloneExample="""
  N={class method mut This #mutK() class method mut This k()=this.#mutK()}
  BB={var N that
    class method mut This #mutK(N that)
    class method mut This k(N that)=this.#mutK(that)
    read method capsule BB clone()=BB.k(this.that())
    }
  AA={mut BB f
    class method mut This #mutK(mut BB f)
    class method mut This k(mut BB f)=this.#mutK(f=f)
    }
  Vector={
    class method mut This #mutK()
    mut method Void add(mut BB that)=void
    }
  """;


}