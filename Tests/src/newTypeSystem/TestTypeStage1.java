package newTypeSystem;

import helpers.TestHelper;

import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.FormatterClosedException;
import java.util.HashMap;




import java.util.List;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import coreVisitors.CollectPaths0;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Ast;
import ast.Ast.Type;
import ast.Ast.Stage;
import ast.ErrorMessage;
import ast.ErrorMessage.TypeError;
import ast.ExpCore;
import ast.Expression;
import auxiliaryGrammar.Functions;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Phase;
import programReduction.Program;

public class TestTypeStage1 {
  @Before public void config() {TestHelper.configureForTest();}

    @RunWith(Parameterized.class)
    public static class TestStage1 {
      @Parameter(0) public int _lineNumber;
      @Parameter(1) public String _e;
      @Parameter(2) public Type typeSugg;
      @Parameter(3) public Type typeExpected;
      @Parameter(4) public String[] program;
      @Parameters(name = "{index}: line {0}")
      public static List<Object[]> createData() {
        return Arrays.asList(new Object[][] {
           {lineNumber(),"( exception void catch exception  Void  x void  void)",
           new Type(Mdf.Readable,Path.Any(),Doc.empty()),
           new Type(Mdf.Immutable,Path.Any(), Doc.empty()),
           new String[]{"{ D:{class method This k()}}"}
         },{lineNumber(),"( (exception void "
          + "   catch exception Any x exception x"
          + "   void)"
          + " catch exception Void xOut void void)",
           new Type(Mdf.Readable,Path.Any(),Doc.empty()),
           new Type(Mdf.Immutable,Path.Any(), Doc.empty()),
           new String[]{"{ D:{class method This k()}}"}
       },{lineNumber(),"Any",
         new Type(Mdf.Class,Path.Any(), Doc.empty()),
         new Type(Mdf.Class,Path.Any(), Doc.empty()),
         new String[]{"{ C:{class method This k()}}"}
       },{lineNumber(),"Library",
         new Type(Mdf.Class,Path.Any(),Doc.empty()),
         new Type(Mdf.Class,Path.Library(), Doc.empty()),
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
      TOut out=TypeSystem.instance().type(TIn.top(Phase.Typed, p, e).withE(e, this.typeSugg));
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
  /*   },{lineNumber(), "( lent D x=D.#mutK() mut D y=D.#mutK() x.m(y)  )",//Deceiving, but it should pass! as for ( lent D x=D() ( mut D y=D() x.m(y) ) )
      new NormType(Mdf.Readable,Path.Void(),Doc.empty()),
      NormType.immVoid,
      new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}
   */ },{lineNumber(),
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
        TOut out=TypeSystem.instance().type(TIn.top(Phase.Typed, p, e).withE(e, this.typeSugg));
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
          TOut out=TypeSystem.instance().type(TIn.top(Phase.Typed, p, e).withE(e, this.typeSugg));

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

