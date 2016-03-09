package testAux;

import helpers.TestHelper;

import static helpers.TestHelper.lineNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;




import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import coreVisitors.CollectPaths0;
import facade.Configuration;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import typeSystem.SealEnv;
import typeSystem.ThrowEnv;
import typeSystem.TypeSystem;
import ast.Ast;
import ast.Ast.NormType;
import ast.Ast.Type;
import ast.Ast.Ph;
import ast.Ast.Stage;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Expression;
import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Program;

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
         {lineNumber(),"void",
           NormType.immVoid,
           new NormType(Mdf.Capsule,Path.Void(),Ph.None),
           new String[]{"{ C:{k()}}"}

         },{lineNumber(),"( exception void catch exception  Void  x void  void)",
           new Ast.FreeType(),
           new NormType(Mdf.Capsule,Path.Void(),Ph.None),
           new String[]{"{ D:{k()}}"}
         },{lineNumber(),"( (exception void "
          + "   catch exception Any x exception x"
          + "   void)"
          + " catch exception Void xOut void void)",
           new Ast.FreeType(),
           new NormType(Mdf.Capsule,Path.Void(),Ph.None),
           new String[]{"{ D:{k()}}"}
       },{lineNumber(),"Any",
         new NormType(Mdf.Class,Path.Any(),Ph.None),
         new NormType(Mdf.Class,Path.Any(),Ph.None),
         new String[]{"{ C:{k()}}"}
       },{lineNumber(),"Library",
         new Ast.FreeType(),
         new NormType(Mdf.Class,Path.Library(),Ph.None),
         new String[]{"{ C:{k()}}"}
       },{lineNumber(),"C.k()",
         new NormType(Mdf.Immutable,Path.parse("This0.C"),Ph.None),
         new NormType(Mdf.Immutable,Path.parse("This0.C"),Ph.None),
         new String[]{"{ C:{k()}}"}
       },{lineNumber(),"C.#mutK()",
         new NormType(Mdf.Mutable,Path.parse("This0.C"),Ph.None),
         new NormType(Mdf.Mutable,Path.parse("This0.C"),Ph.None),
         new String[]{"{ C:{mut k()}}"}
       },{lineNumber(),"C.k(f:D.k(),ft:D)",
         new NormType(Mdf.Mutable,Path.parse("This0.C"),Ph.None),
         new NormType(Mdf.Mutable,Path.parse("This0.C"),Ph.None),
         new String[]{"{ C:{mut k(var D f, class D ft)}, D:{k()}}"}
       },{lineNumber(),"( D x=D.k(), C.k(f:x,ft:D))",
         new NormType(Mdf.Mutable,Path.parse("This0.C"),Ph.None),
         new NormType(Mdf.Mutable,Path.parse("This0.C"),Ph.None),
         new String[]{"{ C:{mut k(var D f, class D ft)}, D:{k()}}"}

       },{lineNumber(),"error D.k()",
         new Ast.FreeType(),
         new Ast.FreeType(),
         new String[]{"{ D:{k()}}"}

       },{lineNumber(),"( D x=D.k(f:x), x)",
         new Ast.FreeType(),
         new NormType(Mdf.Immutable,Path.parse("This0.D"),Ph.None),
         new String[]{"{ D:{k(var fwd D f)} }"}

       },{lineNumber(),"List.factory(N.k())",
         new Ast.FreeType(),
         new NormType(Mdf.Immutable,Path.parse("This0.List"),Ph.None),
         new String[]{listExample}
       },{lineNumber(),"(class List this=List, N that=N.k(), List x=this.factoryAux(that,top:x)  x)",
         new Ast.FreeType(),
         new NormType(Mdf.Immutable,Path.parse("This0.List"),Ph.None),
         new String[]{listExample}
       },{lineNumber(),TestHelper.multiLine(""
,"(class List this=List,"
," N that=N.k(),"
," fwd List top=List.factory(N.k())"
," ( Void z=that.checkZero(),"
,"   catch error "
, "    Void  x List.k("
,"       next:List.factoryAux(that.lessOne(),top:top),"
,"       elem:that)"
,"   List.k(next:top,elem:N.k()))"
," )"),
         new Ast.FreeType(),
         new NormType(Mdf.Immutable,Path.parse("This0.List"),Ph.Partial),
         new String[]{listExample}

         },{lineNumber(),"( fwd D x=D.k(f:void), x)",
           new Ast.FreeType(),
           new NormType(Mdf.Immutable,Path.parse("This0.D"),Ph.Ph),
           new String[]{"{ D:{k(var Any f)} }"}

         //
         },{lineNumber(),"( fwd D x=D.k(f:void), D.k(f:x))",
           new Ast.FreeType(),
           new NormType(Mdf.Mutable,Path.parse("This0.D"),Ph.Partial),
           new String[]{"{ D:{k(var fwd Any f)} }"}
         },{lineNumber(),"( fwd D x=D.k(f:void), D.k(f:x))",
           new Ast.FreeType(),
           new NormType(Mdf.Immutable,Path.parse("This0.D"),Ph.Partial),
           new String[]{"{ D:{k( fwd Any f)} }"}
         },{lineNumber(),"( D x=D.k(f:void), D.k(f:x))",
           new NormType(Mdf.Immutable,Path.parse("This0.D"),Ph.None),
           new NormType(Mdf.Immutable,Path.parse("This0.D"),Ph.None),
           new String[]{"{ D:{k(var fwd Any f)} }"}
        //
         },{lineNumber(),TestHelper.multiLine(""
,"("
,"  Void unused1=("
,"    Void unused00=exception C.k()"
,"    catch exception "
,"      D x return { a()}"
,"    catch exception C x error void"
,"      "
,"    void"
,"    )"
,"  catch return  Library result0 ("
,"    result0"
,"    )"
,"  error void"
,"  )"),
new Ast.FreeType(),
new NormType(Mdf.Immutable,Path.Library(),Ph.None),
new String[]{"{ C:{ k()}, D:{k()}}"}

}});}
      //TODO: before ts after desugaring, do well formedness check!

static String listExample=TestHelper.multiLine(
    "{N:{k() method Void checkZero() (void) method N lessOne() (this)}"
    ,"List:{k(fwd List next, N elem)"
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
      Type t2=TypeSystem.typecheckSure(false,p, new HashMap<>(),SealEnv.empty(),new ThrowEnv(), this.typeSugg, e);
      Assert.assertEquals(t2,typeExpected);
      //TestHelper.assertEqualExp(eRed,ee2);
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
        NormType.immVoid,
      new NormType(Mdf.Capsule,Path.Void(),Ph.None),
      new String[]{"{ C:{k()}}"}
    },{lineNumber(), "C.#mutK()",
      new NormType(Mdf.Capsule,Path.parse("This0.C"),Ph.None),
      new NormType(Mdf.Capsule,Path.parse("This0.C"),Ph.None),
      new String[]{"{ C:{mut k()}}"}
    },{lineNumber(), "C.#mutK(f:D.#mutK()).f()",
      new NormType(Mdf.Readable,Path.parse("This0.D"),Ph.None),
      new NormType(Mdf.Readable,Path.parse("This0.D"),Ph.None),
      new String[]{"{ C:{mut k(var mut D f)} D:{mut k()}}"}
    },{lineNumber(), "C.#mutK(f:D.#mutK()).#f()",
      new NormType(Mdf.Mutable,Path.parse("This0.D"),Ph.None),
      new NormType(Mdf.Mutable,Path.parse("This0.D"),Ph.None),
      new String[]{"{ C:{mut k(var mut D f)} D:{mut k()}}"}
    },{lineNumber(), "C.#mutK(f:D.#mutK()).f()",
      new NormType(Mdf.Immutable,Path.parse("This0.D"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("This0.D"),Ph.None),
      new String[]{"{ C:{mut k(var mut D f)} D:{mut k()}}"}

    },{lineNumber(), "(lent Vector v=Vector.#mutK(), mut B b=B.#mutK(N.#mutK()),v.add(b.clone()))",
      NormType.immVoid,
      NormType.immVoid,
      new String[]{cloneExample}
    },{lineNumber(), "(A a=A.k(f:B.k(N.k()).clone()) a)",
      new NormType(Mdf.Immutable,Path.parse("This0.A"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("This0.A"),Ph.None),
      new String[]{cloneExample}
  //is ok, clone vuole readable
    },{lineNumber(), "(mut B b=B.k(N.k()), A a=A.k(f:b.clone()) a)",
      new NormType(Mdf.Immutable,Path.parse("This0.A"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("This0.A"),Ph.None),
      new String[]{cloneExample}

    },{lineNumber(), "(mut B b=B.k(N.k()), A a=A.k(f:b.clone()) a)",
      new NormType(Mdf.Immutable,Path.parse("This0.A"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("This0.A"),Ph.None),
      new String[]{cloneExample}

    },{lineNumber(), " ( B bi=A.k(f:(read B b=B.k(N.k()) b).clone()).f() bi)",
      new NormType(Mdf.Immutable,Path.parse("This0.B"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("This0.B"),Ph.None),
      new String[]{cloneExample}

    },{lineNumber(), " (read B b=B.k(N.k()),  B bi=A.k(f:b.clone()).f() bi)",
      new NormType(Mdf.Immutable,Path.parse("This0.B"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("This0.B"),Ph.None),
      new String[]{cloneExample}
    },{lineNumber(), " (AI any=(mut AI aI=AI.#mutK() aI) any)",
    new NormType(Mdf.Immutable,Path.parse("This0.AI"),Ph.None),
    new NormType(Mdf.Immutable,Path.parse("This0.AI"),Ph.None),
    new String[]{"{ AI:{mut k()} }"}

    },{lineNumber(), "error D.k()",//get capsule promoted
      new Ast.FreeType(),
      new Ast.FreeType(),
      new String[]{"{ D:{ mut k()}}"}
    },{lineNumber(), "( D.#mutK().m(D.#mutK()) )",
      new Ast.FreeType(),
      NormType.immVoid,
      new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}
    },{lineNumber(), "( mut D x=D.#mutK() mut D y=D.#mutK() x.m(y)  )",
      new Ast.FreeType(),
      NormType.immVoid,
      new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}
    },{lineNumber(), "( lent D x=D.#mutK() mut D y=D.#mutK() x.m(y)  )",//Deceiving, but it should pass! as for ( lent D x=D() ( mut D y=D() x.m(y) ) )
      new Ast.FreeType(),
      NormType.immVoid,
      new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}
    },{lineNumber(),
      "Reader.readCustomer()",
      new Ast.FreeType(),
      new NormType(Mdf.Capsule,Path.parse("This0.Customer"),Ph.None),
      new String[]{"{ () \n Customer:{ mut () }\n Reader :{()\n"
      +" class method capsule Customer readCustomer() (\n"
      +"   mut Customer c=Customer.#mutK()\n"
      +"   c //ok, capsule promotion here\n"
      +" )}}"}
    },{lineNumber(),
        "Reader.readCustomer()",
        new Ast.FreeType(),
        new NormType(Mdf.Capsule,Path.parse("This0.Customer"),Ph.None),
        new String[]{"{() \n Customer:{ mut () }\n Reader :{()\n"
        +" class method capsule Customer readCustomer() {\n"
        +" return Customer.#mutK()"
        +" }}}"}
    },{lineNumber(),
      "Reader.readCustomer()",
      new Ast.FreeType(),
      new NormType(Mdf.Capsule,Path.parse("This0.Customer"),Ph.None),
      new String[]{"{() \n Customer:{ mut () }\n Reader :{()\n"
      +" class method capsule Customer readCustomer() (\n"
      +"   mut Customer c=Customer.#mutK()\n"
      +"   return c //ok, capsule promotion here\n"
      +"   catch return mut Customer x x"
      +"   error void)}}"}
    },{lineNumber(),
      "Reader.readCustomer()",
      new Ast.FreeType(),
      new NormType(Mdf.Capsule,Path.parse("This0.Customer"),Ph.None),
      new String[]{"{() \n Customer:{ mut () }\n Reader :{()\n"
      +" class method capsule Customer readCustomer() {\n"
      +"   mut Customer c=Customer.#mutK()\n"
      +"   return c //ok, capsule promotion here\n"
      +" }}}"}
    }});}



      @Test
      public void testType() {
        TestHelper.configureForTest();
        ExpCore e=Desugar.of(Parser.parse(null," "+_e)).accept(new InjectionOnCore());
        Program p=TestHelper.getProgram(program);
        Configuration.typeSystem.checkAll(p);
        Type t2=TypeSystem.typecheckSure(false,p, new HashMap<>(),SealEnv.empty(),new ThrowEnv(), typeSugg, e);
        Assert.assertEquals(t2,typeExpected);
        //TestHelper.assertEqualExp(eRed,ee2);
      }
}
@RunWith(Parameterized.class)
public static class TestStage3_notOk {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _e;
  @Parameter(2) public Type typeSugg;
  @Parameter(3) public Type typeExpected;
  @Parameter(4) public String[] program;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
        {lineNumber()," (mut C x=C(x) x)",
          NormType.immVoid,
        new NormType(Mdf.Capsule,Path.Void(),Ph.None),
        new String[]{"{ C:{mut (mut C that)}}"}
        },{lineNumber()," (mut C x=C(x) (capsule C y=x y))",
          new NormType(Mdf.Capsule,Path.parse("This0.C"),Ph.None),
          new NormType(Mdf.Capsule,Path.parse("This0.C"),Ph.None),
          new String[]{"{ C:{mut (mut C that)}}"}

      },{lineNumber()," (mut C x=C(x) (capsule C y=x.#that() y))",
        new NormType(Mdf.Capsule,Path.parse("This0.C"),Ph.None),
        new NormType(Mdf.Capsule,Path.parse("This0.C"),Ph.None),
        new String[]{"{ C:{mut (mut C that)}}"}
      },{lineNumber(),"(mut B b=B.k(N.k()), A a=A.k(f:b) a)",
        new NormType(Mdf.Immutable,Path.parse("This0.A"),Ph.None),
        new NormType(Mdf.Immutable,Path.parse("This0.A"),Ph.None),
        new String[]{cloneExample}

  /*now k mdf is ignored    },{lineNumber(),"error D.k()",
        new Ast.FreeType(),
        new Ast.FreeType(),
        new String[]{"{ D:{ lent k()}}"}
      },{lineNumber()," ( mut D x= D.k() error x)",
        new Ast.FreeType(),
        new Ast.FreeType(),
        new String[]{"{ D:{ mut k()}}"}
*/
      },{lineNumber(),"use A check sumInt32(n1:void n2:{}) error void",
          new Ast.FreeType(),
          new Ast.FreeType(),
          new String[]{"{ A:{//@plugin\n//L42.is/connected/withAlu\n}}"}
      //test to check that exception Any can not be captured
      },{lineNumber(),"( exception D() catch exception Any x void void)",
        NormType.immVoid,
        NormType.immVoid,
        new String[]{"{ D:{k() method Void m() (void)}}"}
      },{lineNumber(),"( loop ( exception void"
      +"      catch exception Void ( exception void )  void)"
      +"    catch exception Any p ( exception p  ) void )",
      NormType.immVoid,
      NormType.immVoid,
      new String[]{"{ D:{k() method Void m() (void)}}"}
      },{lineNumber(),
        "(exception void catch exception Any p ( exception p  ) void )",
        NormType.immVoid,
        new NormType(Mdf.Capsule,Path.Void(),Ph.None),
        new String[]{"{ D:{k() method Void m() (void)}}"}

            },{lineNumber(), "( ( exception void"
                +"      catch exception Void  exception void  void)"
                +"    catch exception  Any p  exception p  void )",
                NormType.immVoid,
                new NormType(Mdf.Capsule,Path.Void(),Ph.None),
                new String[]{"{ D:{k() method Void m() (void)}}"}
            },{lineNumber(),"( exception D.k() catch exception Any x void void)",//should fail
              NormType.immVoid,
              new NormType(Mdf.Capsule,Path.Void(),Ph.None),
              new String[]{"{ D:{k() method Void m() (void)}}"}
            },{lineNumber(),"( mut D x=D() lent D y=D() x.m(y)  )",//must fail//ok
              new Ast.FreeType(),
              NormType.immVoid,
              new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}
            },{lineNumber(),"( mut D y=D() lent D x=D() x.m(y)  )",//must fail//ok
              new Ast.FreeType(),
              NormType.immVoid,
              new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}



      }});}
      @Test(expected=ErrorMessage.TypeError.class)
      public void testFail() {
        TestHelper.configureForTest();
        ExpCore e=Desugar.of(Parser.parse(null," "+_e)).accept(new InjectionOnCore());
        Program p=TestHelper.getProgram(program);
        TypeSystem.typecheckSure(false,p, new HashMap<>(),SealEnv.empty(),new ThrowEnv(), typeSugg, e);
      }

      }



static String cloneExample=TestHelper.multiLine("{"
    ,"  N:{k()}"
    ,"  B:{mut k(var N that)"
    ,"    read method"
    ,"    capsule B clone() (B.k(this.that()))"
    ,"    }"
    ,"  A:{mut k(mut B f)}"
    ,"  Vector:{mut k() mut method Void add(mut B that) (void)}"
    ,"}");


}

