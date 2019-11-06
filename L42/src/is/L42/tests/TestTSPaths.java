package is.L42.tests;

import java.util.List;
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
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Full;
import is.L42.generated.LDom;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.AtomicTest;
import is.L42.top.Init;
import is.L42.top.Top;
import is.L42.translationToJava.Loader;
import is.L42.typeSystem.PathTypeSystem;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness.NotWellFormed;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;
import static is.L42.generated.LDom._elem;
import static org.junit.jupiter.api.Assertions.*;

public class TestTSPaths
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("{A={B={method Void main()=void}}}")   
   ),new AtomicTest(()->
   fail("{A={B={method Library main()=void}}}",Err.invalidExpectedTypeForVoidLiteral(hole))
   ),new AtomicTest(()->
   pass("{A={class method Void v()=void B={method class A main()=A<:class A}}}")   
   ),new AtomicTest(()->
   fail("{A={class method Void v()=void B={method class Void main()=A<:class A}}}",Err.subTypeExpected(hole,hole))   
   ),new AtomicTest(()->
   fail("{A={class method Void v()=void B={method class Void main()=A<:class Void}}}",Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->
   fail("{A={B={method class A main()=A<:class A}}}",Err.castOnPathOnlyValidIfDeclaredClassMethods(hole))   
   ),new AtomicTest(()->
   fail("{A={interface class method Void v() B={method class A main()=A<:class A}}}",Err.castOnPathOnlyValidIfNotInterface(hole))
   ),new AtomicTest(()->
   pass("{A={B={method class Any main()=A<:class Any}}}")
   ),new AtomicTest(()->
   pass("{A={B={method Any main()={#norm{}}}}}")
   ),new AtomicTest(()->
   fail("{A={B={method Void main()={#norm{}}}}}",Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->
   pass("{A={B={method This main()=this}}}")
   ),new AtomicTest(()->
   pass("{A={B={method Void main()=loop this.main()}}}")
   ),new AtomicTest(()->
   fail("{A={B={method Library main()=loop this.main()}}}",Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->
   pass("""
   {A={B={
     method Void main()=error B()
     class method B()
     }}}
     """)
   ),new AtomicTest(()->
   fail("""
   {A={B={
     method Void main()=exception B()
     class method B()
     }}}
     """,Err.leakedThrow(hole))
   ),new AtomicTest(()->
   fail("""
   {A={B={
     method Void main()[Void]=exception B()
     class method B()
     }}}
     """,Err.leakedThrow(hole))
   ),new AtomicTest(()->
   pass("""
   {A={B={
     method Void main()[B]=exception B()
     class method B()
     }}}
     """)
   ),new AtomicTest(()->
   pass("""
   {A={B={
     method Void main()= B().main()
     class method B()
     }}}
     """)
   ),new AtomicTest(()->
   fail("""
   {A={B={
     method Void main()= this.surprise()
     class method B()
     }}}
     """,Err.methodDoesNotExists(hole,hole))
     ),new AtomicTest(()->
   fail("""
   {A={B={
     method Void main()= this.leaking()
     class method B()
     method Void leaking()[B]
     }}}
     """,Err.leakedExceptionFromMethCall(hole))
  ));}

public static void pass(String program){
  Resources.clearRes();
  Init init=new Init(program){
    protected Top makeTop(FreshNames f){
      return new Top(f,0,new Loader()){
        protected Program flagTyped(Loader loader,Program p1) throws EndError{
          return p1;
        }};
    }};
  Core.L norm=(Core.L)init.top.top(new CTz(),init.p).top;
  var ab=List.of(new C("A",-1),new C("B",-1));
  MWT main=_elem(norm.cs(ab).mwts(),S.parse("main()"));
  Program p=Program.flat(norm).navigate(P.of(0,ab));
  ProgramTypeSystem.typeMWT(p,main);
  }
public static void fail(String program,String...out){
  Resources.clearRes();
  checkFail(()->{
    pass(program);
    }, out, TypeError.class);
  }
}