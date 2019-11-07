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

public class TestTypeStstem
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
   ),new AtomicTest(()->
   pass("""
   {A={B={
     method Void main()[B]= B().leaking()//testing block by desugaring
     class method B()
     method Void leaking()[B]
     }}}
     """)
   ),new AtomicTest(()->
   fail("""
   {A={B={
     method Void main()= B().leaking()//testing block by desugaring
     class method B()
     method Void leaking()[B]
     }}}
    """,Err.leakedExceptionFromMethCall(hole))
   ),new AtomicTest(()->
   pass("""
   {A={B={
     method Void main()[B]=(
       B b1=B()
       B b2=B()
       this.many(b1,other=b2)
       )
     class method B()
     method Void many(B that,B other)=void
     }}}
     """)
   ),new AtomicTest(()->
   pass("""
   {A={B={
     method Void main()=(
       B b1=B()
       B b2=B()
       this.many(b1,other=b2)
       )
     class method B()
     method Void many(B that,B other)=void
     }}}
     """)
   ),new AtomicTest(()->
   pass("""
   {A={B={
     method Void main()=(
       B b1=B()
       B b2=B()
       catch Void v (v)
       this.many(b1,other=b2)
       )
     class method B()
     method Void many(B that,B other)=void
     }}}
     """)
   ),new AtomicTest(()->
   fail("""
   {A={B={
     method Void main()=(
       Void v0=void
       catch Void v (v<:Any)
       void
       )
     }}}
     """,Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->
   fail("""
   {A={B={
     method Void main()=(
       Void b1=void
       B b2=B()
       this.many(b1,other=b2)
       )
     class method B()
     method Void many(B that,B other)=void
     }}}
     """,Err.subTypeExpected(hole,hole))
   ),new AtomicTest(()->pass("""
   {A={B={method read Any main()=(exception void catch exception Void  x void)}}}
   """)
   ),new AtomicTest(()->pass("""
   {A={B={method Any main()[Any]=(
     (exception void catch exception Any x exception x)
     catch exception Void xOut (void) void)
   }}}
   """)
   ),new AtomicTest(()->pass("""
   {A={B={method class Any main()=Any}}}
   """)
   ),new AtomicTest(()->pass("""
   {A={B={method class Any main()=Library}}}
   """)
   ),new AtomicTest(()->pass("""
   {A={B={method class Any main()=Void}}}
   """)
   ),new AtomicTest(()->fail("""
   {A={B={method class Void main()=Void}}}
   ""","nope")

     /*
       },{lineNumber(),"Library",
         new Type(Mdf.Class,Path.Any(),Doc.empty()),
         new Type(Mdf.Class,Path.Any(), Doc.empty()),
         new String[]{"{ C:{class method This k()}}"}
       },{lineNumber(),"C.k()",
         new Type(Mdf.Immutable,Path.parse("This0.C"), Doc.empty()),
         new Type(Mdf.Immutable,Path.parse("This0.C"), Doc.empty()),
         new String[]{"{ C:{class method This k()}}"}
       },{lineNumber(),"C.k()",
         new Type(Mdf.Mutable,Path.parse("This0.C"), Doc.empty()),
         new Type(Mdf.Capsule,Path.parse("This0.C"), Doc.empty()),
         new String[]{"{ C:{class method mut This k()}}"}
       },{lineNumber(),"error D.k()",
         new Type(Mdf.Readable,Path.Any(),Doc.empty()),
         new Type(Mdf.Readable,Path.Any(),Doc.empty()),
         new String[]{"{ D:{class method This k()}}"}

       },{lineNumber(),"( D x=D.k(f:x), x)",
         new Type(Mdf.Readable,Path.Any(),Doc.empty()),
         new Type(Mdf.Immutable,Path.Any(), Doc.empty()),
         new String[]{"{ D:{var D f, class method This k(fwd D f)} }"}

       },{lineNumber(),"List.factory(N.k())",
         new Type(Mdf.Readable,Path.Any(),Doc.empty()),
         new Type(Mdf.Immutable,Path.parse("This0.List"), Doc.empty()),
         new String[]{listExample}
       },{lineNumber(),"(class List this=List, N that=N.k(), List x=this.factoryAux(that,top:x)  x)",
         new Type(Mdf.Readable,Path.Any(),Doc.empty()),
         new Type(Mdf.Immutable,Path.Any(), Doc.empty()),
         new String[]{listExample}
       },{lineNumber(),Functions.multiLine(""
,"(class List this=List,"
," N that=N.k(),"
," fwd List top=List.factory(N.k())"
," ( Void z=that.checkZero(),"
,"   catch error "
,"     Void  x List.k("
,"       next:List.factoryAux(that.lessOne(),top:top),"
,"       elem:that)"
,"   List.k(next:top,elem:N.k()))"
," )"),
       new Type(Mdf.ImmutablePFwd,Path.parse("This0.List"),Doc.empty()),
         new Type(Mdf.ImmutablePFwd,Path.parse("This0.List"),Doc.empty()),
         new String[]{listExample}

         },{lineNumber(),"( fwd D x=D.k(f:void), x)",
         new Type(Mdf.ImmutableFwd,Path.parse("This0.D"),Doc.empty()),
           new Type(Mdf.ImmutableFwd,Path.parse("This0.D"),Doc.empty()),
           new String[]{"{ D:{var Any f class method This k(fwd Any f)} }"}

         //
         },{lineNumber(),"( fwd D x=D.k(f:void), D.k(f:x))",
         new Type(Mdf.MutablePFwd,Path.parse("This0.D"),Doc.empty()),
           new Type(Mdf.MutablePFwd,Path.parse("This0.D"),Doc.empty()),
           new String[]{"{ D:{var Any f class method mut This k(fwd Any f)} }"}
         },{lineNumber(),"( fwd D x=D.k(f:void), D.k(f:x))",
         new Type(Mdf.ImmutablePFwd,Path.parse("This0.D"),Doc.empty()),
           new Type(Mdf.ImmutablePFwd,Path.parse("This0.D"),Doc.empty()),
           new String[]{"{ D:{Any f, class method mut This k( fwd Any f)} }"}
         },{lineNumber(),"( fwd D x=D.k(f:void), D.k(f:x))",
         new Type(Mdf.ImmutablePFwd,Path.parse("This0.D"),Doc.empty()),
           new Type(Mdf.ImmutablePFwd,Path.parse("This0.D"),Doc.empty()),
           new String[]{"{ D:{Any f, class method This k( fwd Any f)} }"}//here no mut This
         },{lineNumber(),"( D x=D.k(f:void), D.k(f:x))",
           new Type(Mdf.Immutable,Path.parse("This0.D"), Doc.empty()),
           new Type(Mdf.Capsule,Path.parse("This0.D"), Doc.empty()),
           new String[]{"{ D:{var fwd Any f class method mut This k( fwd Any f)} }"}
        //
         },{lineNumber(),Functions.multiLine(""
,"("
,"  Void unused1=("
,"    Void unused00=exception C.k()"
,"    catch exception "
,"      D x return { }"
,"    catch exception C x error void"
,"      "
,"    void"
,"    )"
,"  catch return  Library result0 ("
,"    result0"
,"    )"
,"  error void"
,"  )"),
         new Type(Mdf.Immutable,Path.Library(), Doc.empty()),
new Type(Mdf.Immutable,Path.Library(), Doc.empty()),
new String[]{"{ C:{ class method This k()}, D:{class method This k()}}"}

}});}
      //TODO: before ts after desugaring, do well formedness check!

static String listExample=Functions.multiLine(
    "{N:{class method This k() method Void checkZero() (void) method N lessOne() (this)}"
    ,"List:{class method This k(fwd List next, N elem)"
    ,"  class method List factory(N that) ("
    ,"    List x=this.factoryAux(that,top:x)"
    ,"    x"
    ,"    )"
    ,"class method List factoryAux(N that,fwd List top)"
    ,"  (Void z=that.checkZero()"
    ,"   catch error Void x"
    ,"     List.k("
    ,"       next:List.factoryAux(that.lessOne(),top:top),"
    ,"       elem:that)"
    ,"   List.k(next:top,elem:N.k())) "
    ,"}}");

    @Test
    public void testType() {
      TestHelper.configureForTest();
      ExpCore e=Desugar.of(Parser.parse(null," "+_e)).accept(new InjectionOnCore());
      Program p=TestHelper.getProgram( program);
      TOut out=TypeSystem.instance().type(TIn.top(Phase.Typed, p, e,true,this.typeSugg));
      assert out.isOk():new FormattedError(out.toError());
      Assert.assertEquals(typeExpected,out.toOk().computed);
    }

    }

@RunWith(Parameterized.class)
public static class TestStage2 {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _e;
  @Parameter(2) public Type typeSugg;
  @Parameter(3) public Type typeExpected;
  @Parameter(4) public String[] program;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
      {lineNumber(), "void",
        Type.immVoid,
        Type.immVoid,
      new String[]{"{ C:{}}"}
    },{lineNumber(), "C.#mutK()",
      new Type(Mdf.Capsule,Path.parse("This0.C"), Doc.empty()),
      new Type(Mdf.Capsule,Path.parse("This0.C"), Doc.empty()),
      new String[]{"{ C:{class method mut This #mutK()}}"}
    },{lineNumber(), "C.#mutK(f:D.#mutK()).f()",
      new Type(Mdf.Readable,Path.parse("This0.D"), Doc.empty()),
      new Type(Mdf.Immutable,Path.parse("This0.D"), Doc.empty()),
      new String[]{"{ C:{var mut D f class method mut This #mutK(mut D f)} D:{class method mut This #mutK()}}"}
    },{lineNumber(), "C.#mutK(f:D.#mutK()).#f()",
      new Type(Mdf.Mutable,Path.parse("This0.D"), Doc.empty()),
      new Type(Mdf.Capsule,Path.parse("This0.D"), Doc.empty()),
      new String[]{"{ C:{var mut D f class method mut This #mutK(mut D f)} D:{class method mut This #mutK()}}"}
    },{lineNumber(), "C.#mutK(f:D.#mutK()).f()",
      new Type(Mdf.Immutable,Path.parse("This0.D"), Doc.empty()),
      new Type(Mdf.Immutable,Path.parse("This0.D"), Doc.empty()),
      new String[]{"{ C:{var mut D f class method mut This #mutK(mut D f)} D:{class method mut This #mutK()}}"}

    },{lineNumber(), "(lent Vector v=Vector.#mutK(), mut B b=B.#mutK(N.#mutK()),v.add(b.clone()))",
      Type.immVoid,
      Type.immVoid,
      new String[]{cloneExample}
    },{lineNumber(), "(A a=A.k(f:B.k(N.k()).clone()) a)",
      new Type(Mdf.Immutable,Path.parse("This0.A"), Doc.empty()),
      new Type(Mdf.Immutable,Path.parse("This0.A"), Doc.empty()),
      new String[]{cloneExample}
  //is ok, clone vuole readable
    },{lineNumber(), "(mut B b=B.k(N.k()), A a=A.k(f:b.clone()) a)",
      new Type(Mdf.Immutable,Path.parse("This0.A"), Doc.empty()),
      new Type(Mdf.Immutable,Path.parse("This0.A"), Doc.empty()),
      new String[]{cloneExample}

    },{lineNumber(), "(mut B b=B.k(N.k()), A a=A.k(f:b.clone()) a)",
      new Type(Mdf.Immutable,Path.parse("This0.A"), Doc.empty()),
      new Type(Mdf.Immutable,Path.parse("This0.A"), Doc.empty()),
      new String[]{cloneExample}

    },{lineNumber(), " ( B bi=A.k(f:(read B b=B.k(N.k()) b).clone()).f() bi)",
      new Type(Mdf.Immutable,Path.parse("This0.B"), Doc.empty()),
      new Type(Mdf.Immutable,Path.parse("This0.B"), Doc.empty()),
      new String[]{cloneExample}

    },{lineNumber(), " (read B b=B.k(N.k()),  B bi=A.k(f:b.clone()).f() bi)",
      new Type(Mdf.Immutable,Path.parse("This0.B"), Doc.empty()),
      new Type(Mdf.Immutable,Path.parse("This0.B"), Doc.empty()),
      new String[]{cloneExample}
    },{lineNumber(), " (AI any=(mut AI aI=AI.#mutK() aI) any)",
    new Type(Mdf.Immutable,Path.parse("This0.AI"), Doc.empty()),
    new Type(Mdf.Immutable,Path.parse("This0.AI"), Doc.empty()),
    new String[]{"{ AI:{class method mut This #mutK()} }"}

    },{lineNumber(), "error D.k()",//get capsule promoted
      new Type(Mdf.Readable,Path.Any(),Doc.empty()),
      new Type(Mdf.Readable,Path.Any(),Doc.empty()),
      new String[]{"{ C:{var mut D f class method mut This #mutK(mut D f)} D:{class method mut This k()}}"}
    },{lineNumber(), "( D().m(D()) )",
      new Type(Mdf.Readable,Path.Void(),Doc.empty()),
      Type.immVoid,
      new String[]{"{class method This() D:{ class method mut This() mut method Void m(mut D that) error void }}"}
    },{lineNumber(), "( mut D x=D() mut D y=D() x.m(y)  )",
      new Type(Mdf.Readable,Path.Void(),Doc.empty()),
      Type.immVoid,
      new String[]{"{class method This() D:{ class method mut This() mut method Void m(mut D that) error void }}"}
 //now not well typed any more... it seams like a good thing?
  //   },{lineNumber(), "( lent D x=D.#mutK() mut D y=D.#mutK() x.m(y)  )",//Deceiving, but it should pass! as for ( lent D x=D() ( mut D y=D() x.m(y) ) )
  //    new NormType(Mdf.Readable,Path.Void(),Doc.empty()),
  //    NormType.immVoid,
  //    new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}
  /// },{lineNumber(),
      "Reader.readCustomer()",
      new Type(Mdf.Readable,Path.Any(),Doc.empty()),
      new Type(Mdf.Capsule,Path.parse("This0.Customer"), Doc.empty()),
      new String[]{"{ class method This() \n Customer:{ class method mut This() }\n Reader :{class method This()\n"
      +" class method capsule Customer readCustomer() (\n"
      +"   mut Customer c=Customer()\n"
      +"   c //ok, capsule promotion here\n"
      +" )}}"}
    },{lineNumber(),
        "Reader.readCustomer()",
        new Type(Mdf.Readable,Path.Any(),Doc.empty()),
        new Type(Mdf.Capsule,Path.parse("This0.Customer"), Doc.empty()),
        new String[]{"{class method This() \n Customer:{ class method mut This() }\n Reader :{class method This()\n"
        +" class method capsule Customer readCustomer() {\n"
        +" return Customer()"
        +" }}}"}
    },{lineNumber(),
      "Reader.readCustomer()",
      new Type(Mdf.Readable,Path.Any(),Doc.empty()),
      new Type(Mdf.Capsule,Path.parse("This0.Customer"), Doc.empty()),
      new String[]{"{class method This() \n Customer:{ class method mut This() }\n Reader :{class method This()\n"
      +" class method capsule Customer readCustomer() (\n"
      +"   mut Customer c=Customer()\n"
      +"   return c //ok, capsule promotion here\n"
      +"   catch return mut Customer x x"
      +"   error void)}}"}
    },{lineNumber(),
      "Reader.readCustomer()",
      new Type(Mdf.Readable,Path.Any(),Doc.empty()),
      new Type(Mdf.Capsule,Path.parse("This0.Customer"), Doc.empty()),
      new String[]{"{class method This() \n Customer:{ class method mut This() }\n Reader :{class method This()\n"
      +" class method capsule Customer readCustomer() {\n"
      +"   mut Customer c=Customer()\n"
      +"   return c //ok, capsule promotion here\n"
      +" }}}"}
    }});}



      @Test
      public void testType() {
        TestHelper.configureForTest();
        ExpCore e=Desugar.of(Parser.parse(null," "+_e)).accept(new InjectionOnCore());
        Program p=TestHelper.getProgram(program);
        p=p.updateTop(TypeSystem.instance().topTypeLib(Phase.Coherent, p));
        TOut out=TypeSystem.instance().type(TIn.top(Phase.Typed, p, e,true, this.typeSugg));
        assert out.isOk():FormattedError.format(out.toError());
        Assert.assertEquals(typeExpected,out.toOk().computed);
   }
}
@RunWith(Parameterized.class)
public static class TestStage3_notOk {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _e;
  @Parameter(2) public Type typeSugg;
  @Parameter(3) public Type typeExpected;//TODO: what is the role of this for test not ok?
  @Parameter(4) public String[] program;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
        {lineNumber()," (mut C x=C(x) x)",
          Type.immVoid,
        new Type(Mdf.Capsule,Path.Void(), Doc.empty()),
        new String[]{"{ C:{mut C that class method mut This(mut C that)}}"}
        },{lineNumber()," (mut C x=C(x) (capsule C y=x y))",
          new Type(Mdf.Capsule,Path.parse("This0.C"), Doc.empty()),
          new Type(Mdf.Capsule,Path.parse("This0.C"), Doc.empty()),
          new String[]{"{ C:{mut C that class method mut This(mut C that)}}"}

      },{lineNumber()," (mut C x=C(x) (capsule C y=x.#that() y))",
        new Type(Mdf.Capsule,Path.parse("This0.C"), Doc.empty()),
        new Type(Mdf.Capsule,Path.parse("This0.C"), Doc.empty()),
        new String[]{"{ C:{mut C that class method mut This(mut C that)}}"}
      },{lineNumber(),"(mut B b=B.k(N.k()), A a=A.k(f:b) a)",
        new Type(Mdf.Immutable,Path.parse("This0.A"), Doc.empty()),
        new Type(Mdf.Immutable,Path.parse("This0.A"), Doc.empty()),
        new String[]{cloneExample}

      },{lineNumber(),"use A check sumInt32(n1:void n2:{}) error void",
          new Type(Mdf.Readable,Path.Any(),Doc.empty()),
          new Type(Mdf.Readable,Path.Any(),Doc.empty()),
          new String[]{"{ A:{//@plugin\n//L42.is/connected/withAlu\n}}"}
      //test to check that exception Any can not be captured//TODO: is now ok to capture any with the new TS?
      //},{lineNumber(),"( exception D() catch exception Any x void void)",
      //  Type.immVoid,
      //  Type.immVoid,
      //  new String[]{"{ D:{class method This() method Void m() (void)}}"}
      },{lineNumber(),"( mut D x=D() lent D y=D() x.m(y)  )",//must fail//ok
              new Type(Mdf.Readable,Path.Any(),Doc.empty()),
              Type.immVoid,
              new String[]{"{ D:{class method mut This() mut method Void m(mut D that) error void }}"}
      },{lineNumber(),"( mut D y=D() lent D x=D() x.m(y)  )",//must fail//ok
              new Type(Mdf.Readable,Path.Any(),Doc.empty()),
              Type.immVoid,
              new String[]{"{ D:{ class method mut This() mut method Void m(mut D that) error void }}"}
      },{lineNumber(),"D.foo()",//must fail//ok
        new Type(Mdf.Readable,Path.Any(),Doc.empty()),
        Type.immVoid,
        new String[]{"{ D:{class method This() class method Void foo() (exception D())}}"}

      },{lineNumber(),"D.foo()",//must fail//ok
      new Type(Mdf.Readable,Path.Any(),Doc.empty()),
      Type.immVoid,
      new String[]{"{ D:{class method This() "
      + "class method Void foo() (this.bar())"
      + "class method Void bar() exception D (void)"
      + "}}"}

      }});}
      @Test(expected=RuntimeException.class)
      public void testFail() {
        try{
          TestHelper.configureForTest();
          ExpCore e=Desugar.of(Parser.parse(null," "+_e)).accept(new InjectionOnCore());
          Program p=TestHelper.getProgram(program);
          p=p.updateTop(TypeSystem.instance().topTypeLib(Phase.Coherent, p));
          TOut out=TypeSystem.instance().type(TIn.top(Phase.Typed, p, e,true, this.typeSugg));

          assert !out.isOk();
          throw new FormattedError(out.toError());//assert out.toOk().computed.equals(typeExpected);
          }
        catch(ParseCancellationException err){fail();}
        }

      }



static String cloneExample=Functions.multiLine("{"
    ,"  N:{class method mut This #mutK() class method mut This k() this.#mutK()}"
    ,"  B:{var N that "
    ,"   class method mut This #mutK(N that)"
    ,"   class method mut This k(N that) this.#mutK(that)"
    ,"    read method"
    ,"    capsule B clone() (B.k(this.that()))"
    ,"    }"
    ,"  A:{mut B f class method mut This #mutK(mut B f)"
    ,"   class method mut This k(mut B f) this.#mutK(f:f)}"
    ,"  Vector:{class method mut This #mutK()"
    ,"    mut method Void add(mut B that) (void)}"
    ,"}");


}

     
     
     
     //--------------
       {lineNumber(),
  "{C:{class method Void foo() (This0.foo())} }",
  "{C:{class method Void foo() (This0.foo())}##star^## }##star^##"
},{lineNumber(),
  "{C:{E:{class method Void foo() (This1.foo())} class method Void foo() (D.foo())} D:{class method Void foo() (C.E.foo())}}",
  "{C:{class method Void foo() (D.foo()) E:{class method Void foo() (This1.foo())}} D:{class method Void foo() (C.E.foo())}}"
},{lineNumber(),
  "{K:{E:{class method Void foo() (This2.C.foo())}} C:{class method Void foo() (D.foo())} D:{class method Void foo() (K.E.foo())}}",
  "{K:{E:{class method Void foo() (This2.C.foo())}##star^##}##star ^## C:{class method Void foo() (D.foo())}##star^## D:{class method Void foo() (K.E.foo())}##star^##}##star^##"
},{lineNumber(),
  "{K:{ E:{class method C foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",
  "{K:{ E:{class method C foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}"
  //norm//NO, Norm is executed only in the extracted method
//},{"This0.C",
//  "{K:{E:{class method C.foo() foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",
//  "{K:{E:{class method C foo() (C.foo())}##plus^##}##plus ^## C:{class method C foo() (D.foo())}##plus^## D:{class method C foo() (K.E.foo())}##plus^##}##plus^##"
//},{"This0.C",
//  "{K:{E:{class method C.foo().foo() foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",
//  "{K:{E:{class method C foo() (C.foo())}##plus^##}##plus^## C:{class method C foo() (D.foo())}##plus^## D:{class method C foo() (K.E.foo())}##plus^##}##plus^##"
},{lineNumber(),
  "{C:{ method Void foo() (This0 x= this void)} }",
  "{C:{ method Void foo() (This0 x= this void)}##star^## }##star^##"
},{lineNumber(),
  "{C:{ method Void foo() (C x= this void)} }",
  "{C:{ method Void foo() (C x= this void)}##star^## }##star^##"

},{lineNumber(),
"{"
+ "method Void (class A a, class B b)#?bin(a:a,b:b)"
+ "A:{class method Void #bin#0a(class B b)void} B:{class method Void #bin#0b(class A a)void}\n"
+ "}",
"{"
+ "method Void #apply(class A a, class B b) a.#bin#0a(b:b)"
+ "A:{class method Void #bin#0a(class B b)void} B:{class method Void #bin#0b(class A a)void}\n"
+ "}",

},{lineNumber(),
"{"
+ "method Void (class A a, class B b)#?bin(a:a,b:b)"
+ "A:{class method Void #bin#1a(class B b)void} B:{class method Void #bin#0b(class A a)void}\n"
+ "}",
"{"
+ "method Void #apply(class A a, class B b) b.#bin#0b(a:a)"
+ "A:{class method Void #bin#1a(class B b)void} B:{class method Void #bin#0b(class A a)void}\n"
+ "}",

},{lineNumber(),
"{"
+ "method Void (class A a, class B b)#?bin(a:a,b:b)"
+ "A:{class method Void #bin#1a(class B b)void} B:{class method Void #bin#0b(read A a)void}\n"
+ "}",
"{"
+ "method Void #apply(class A a, class B b) a.#bin#1a(b:b)"
+ "A:{class method Void #bin#1a(class B b)void} B:{class method Void #bin#0b(read A a)void}\n"
+ "}",

},{lineNumber(),
"{"
+ "method Void (class A a, class B b)#?bin(a:a,b:b)"
+ "A:{class method Void #bin#1a(read B b)void} B:{class method Void #bin#0b(read A a)void class method Void #bin#10b(class A a)void}\n"
+ "}",
"{"
+ "method Void #apply(class A a, class B b) b.#bin#10b(a:a)"
+ "A:{class method Void #bin#1a(read B b)void} B:{class method Void #bin#0b(read A a)void class method Void #bin#10b(class A a)void}\n"
+ "}",

},{lineNumber(),
"{"
+ "method Void (class A a, class B b,class C c)#?m3(a:a,b:b,c:c)"
+ "A:{class method Void #m3#0a(class B b,class C c)void}"
+ "B:{class method Void #m3#0b(class A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}",
"{"
+ "method Void (class A a, class B b,class C c) a.#m3#0a(b:b,c:c)"
+ "A:{class method Void #m3#0a(class B b,class C c)void}"
+ "B:{class method Void #m3#0b(class A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}"

},{lineNumber(),
"{"
+ "method Void (class A a, class B b,class C c)#?m3(a:a,b:b,c:c)"
+ "A:{class method Void #m3#0a(read B b,class C c)void}"
+ "B:{class method Void #m3#0b(class A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}",
"{"
+ "method Void (class A a, class B b,class C c) b.#m3#0b(a:a,c:c)"
+ "A:{class method Void #m3#0a(read B b,class C c)void}"
+ "B:{class method Void #m3#0b(class A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}"

},{lineNumber(),
"{"
+ "method Void (class A a, class B b,class C c)#?m3(a:a,b:b,c:c)"
+ "A:{class method Void #m3#0a(read B b,class C c)void}"
+ "B:{class method Void #m3#0b(read A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}",
"{"
+ "method Void (class A a, class B b,class C c) c.#m3#0c(a:a,b:b)"
+ "A:{class method Void #m3#0a(read B b,class C c)void}"
+ "B:{class method Void #m3#0b(read A a,class C c)void}\n"
+ "C:{class method Void #m3#0c(class A a,class B b)void}\n"
+ "}"

}});}

    
    
@Test()
public void testAllSteps() {
  ClassB cb1=runTypeSystem(original);
  ClassB cb2=(ClassB)Desugar.of(Parser.parse(null,annotated)).accept(new InjectionOnCore());
  TestHelper.assertEqualExp(cb1,cb2);
  }
}


public static class TesFail {
@Test(expected=MethodNotPresent.class)
public void test2() {runTypeSystem(
    "{C:{class method Void foo() (D.foo())} "
    +"D:{class method Void bar() (void)}}"
    );}
@Test(expected=FormattedError.class)
public void test3() {runTypeSystem(
   "{C:{E:{class method Void foo() (This1.foo())} class method Library foo() (D.foo())} D:{class method Void foo() (C.E.foo())}}"
    );}
@Test(expected=FormattedError.class)
public void test4() {runTypeSystem(
    "{C:{E:{class method Void foo() (This1.foo())} class method Library foo() (D.foo())} D:{class method Void foo() (C.E.foo())}}"
    );}
@Test(expected=MethodNotPresent.class)
public void test5() {runTypeSystem(
  "{K:{E:{class method Any  foo() (This1.foo())}} C:{class method Void foo() (D.foo())} D:{class method Library foo() (K.E.foo())}}"
   );}

@Test()
public void test6() {try{runTypeSystem(
  "{class method Any  foo() (exception void)}"
   );fail();}catch(FormattedError fe){assertEquals(
  ErrorKind.MethodLeaksExceptions,
  fe.kind);};}

@Test()
public void test7() {try{runTypeSystem(
  "{class method Any  foo()exception This (exception void)}"
   );fail();}catch(FormattedError fe){assertEquals(
  ErrorKind.MethodLeaksExceptions,
  fe.kind);};}

@Test()
public void test8() {try{runTypeSystem(
  "{class method Any  foo()exception This "
  + "{return (exception void {})}"
  + "}"
   );fail();}catch(FormattedError fe){assertEquals(
  ErrorKind.MethodLeaksExceptions,
  fe.kind);};}

   //--------------------------------
    
    //----- basic attempts
//ok
{  lineNumber(),"{}","{C:{method Void()void}}",sameAsFormer
//ok
},{lineNumber(),"{}","{C:{method Void()}}",sameAsFormer

//ok
},{lineNumber(),"{}","{C:{method Void()void } D:{}}",sameAsFormer
//err
},{lineNumber(),"{}","{C:{method Void()this }}",ErrorKind.NotSubtypeClass

//ok
},{lineNumber(),"{}","{C:{method This()this }}",sameAsFormer
},{lineNumber(),"{}","{C:{method This()this() }}",sameAsFormer
//err
},{lineNumber(),"{}","{C:{method class This()this }}",ErrorKind.NotSubtypeMdf
},{lineNumber(),"{}","{C:{method This()this.foo() method Any foo()this }}",ErrorKind.NotSubtypeClass

//ok
},{lineNumber(),"{}","{C:{method D() this() } D:{class method This ()}}",sameAsFormer
},{lineNumber(),"{}","{C:{method Library() (x=void catch error Library y y {} ) }}","{C:{method Library() (Void x=void catch error Library y y {} ) }}"
},{lineNumber(),"{}","{C:{class method Void foo() (This0.foo())} }",sameAsFormer
//err
},{lineNumber(),"{}","{C:{method D() this() } D:{class method This ( mut This that )}}",ErrorKind.LibraryNotCoherent


},{lineNumber(),"{}",
"{A:{class method This ()\n"
+ " class method Void #next()void\n"
+ " class method Void #checkEnd()void"
+ " }\n"+
"B:{class method This ()}\n"+
"Test:{\n"+
"  class method Void foo()\n"+
"  exception B (\n"+
"    with class A x in A (exception B())\n"+
"  )\n"+
"  }}",sameAsFormer


},{lineNumber(),"{}",
"{A:{class method This ()}\n"+
"B:{class method This ()}\n"+
"Test:{\n"+
"  class method Void foo()\n"+
"  exception A (\n"+
"    exception B()\n"+
"  )\n"+
"  }}",ErrorKind.MethodLeaksExceptions

},{lineNumber(),"{}",
"{Top:{class method Library (){A:{class method This ()}\n"+
"B:{class method This ()}\n"+
"Test:{\n"+
"  class method Void foo()\n"+
"  exception A (\n"+
"    exception B()\n"+
"  )\n"+
"  }}}}",ErrorKind.MethodLeaksExceptions

},{lineNumber(),"{}",
"{A:{class method This ()}\n"+
"B:{class method This (A that)}\n"+
"Test:{\n"+
"  class method Void foo()\n"+
"  exception A {(\n"+
"    a=void   exception on A (B)   void\n"+
"  ) return void}\n"+
"  }}",ErrorKind.MethodLeaksExceptions


//fromming and calling method from other classes
//ok
},{lineNumber(),"{}",
"{C:{class method Void foo() (D.foo())} D:{class method Void foo() (void)}}",sameAsFormer
},{lineNumber(),"{}",
"{C:{E:{class method Void foo() (This1.foo())} class method Void foo() (D.foo())} D:{class method Void foo() (C.E.foo())}}",sameAsFormer
},{lineNumber(),"{}",
"{K:{E:{class method Void foo() (This2.C.foo())}} C:{class method Void foo() (D.foo())} D:{class method Void foo() (K.E.foo())}}",sameAsFormer
},{lineNumber(),"{}",
"{K:{ E:{class method C foo() (C.foo())}} C:{class method C foo() (D.foo())} D:{class method C foo() (K.E.foo())}}",sameAsFormer
},{lineNumber(),"{}",
"{C:{ method Void foo() (This0 x= this void)} }",sameAsFormer
},{lineNumber(),"{}",
"{C:{ method Void foo() (C x= this void)} }",sameAsFormer
},{lineNumber(),"{}",
"{K:{ E:{class method C foo1() (C.foo2())}} C:{class method C foo2() (D.foo3())} D:{class method C foo3() (K.E.foo1())}}",sameAsFormer

//method chains
//ok
},{lineNumber(),"{}",
"{K:{ E:{class method C foo1() (D.foo3().foo2().foo2())}} C:{method C foo2() (D.foo3())} D:{class method C foo3() (K.E.foo1())}}",sameAsFormer
//err
},{lineNumber(),"{}",
"{K:{ E:{class method C foo1() (D.foo3().foo2().foo2())}} C:{class method C foo2() (D.foo3())} D:{class method C foo3() (K.E.foo1())}}",ErrorKind.ClassMethCalledOnNonClass

//method promotions
},{lineNumber(),"{}",
"{ method This m() this.readM(this)   method read This readM(read This that)that}",sameAsFormer
},{lineNumber(),"{}",
"{ method This m() this.readM()   read method read This readM()this}",sameAsFormer
},{lineNumber(),"{}",
"{ class method mut This() method This m() This() }",sameAsFormer

//block promotion
},{lineNumber(),"{}",
"{ class method mut This() method This m() ( mut This x=This() x) }",sameAsFormer


          
   
     
     
     */
     
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