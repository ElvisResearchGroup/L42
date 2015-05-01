package testAux;

import helpers.TestHelper;
import java.util.ArrayList;
import java.util.HashMap;




import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
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
  @BeforeSuite public void config() {TestHelper.configureForTest();}
  @Test(singleThreaded=true, timeOut = 500)
  public class TestStage1 {
      @DataProvider(name = "e,typeSugg,typeExpected,stage,program")
      public Object[][] createData1() {
       return new Object[][] {
         {"void",
           new NormType(Mdf.Immutable,Path.Void(),Ph.None),
           new NormType(Mdf.Capsule,Path.Void(),Ph.None),
           new String[]{"{ C:{k()}}"}
         
         },{"( exception void catch exception x (on Void void) void)",
           new Ast.FreeType(),
           new NormType(Mdf.Capsule,Path.Void(),Ph.None),
           new String[]{"{ D:{k()}}"}
         },{"( (exception void "
          + "   catch exception x (on Any exception x)"
          + "   void)"
          + " catch exception xOut (on Void void) void)",
           new Ast.FreeType(),
           new NormType(Mdf.Capsule,Path.Void(),Ph.None),
           new String[]{"{ D:{k()}}"}
       },{"Any",
         new NormType(Mdf.Type,Path.Any(),Ph.None),
         new NormType(Mdf.Type,Path.Any(),Ph.None),
         new String[]{"{ C:{k()}}"}
       },{"Library",
         new Ast.FreeType(),
         new NormType(Mdf.Type,Path.Library(),Ph.None),
         new String[]{"{ C:{k()}}"}
       },{"C.k()",
         new NormType(Mdf.Immutable,Path.parse("Outer0::C"),Ph.None),
         new NormType(Mdf.Immutable,Path.parse("Outer0::C"),Ph.None),
         new String[]{"{ C:{k()}}"}
       },{"C.k()",
         new NormType(Mdf.Mutable,Path.parse("Outer0::C"),Ph.None),
         new NormType(Mdf.Mutable,Path.parse("Outer0::C"),Ph.None),
         new String[]{"{ C:{mut k()}}"}
       },{"C.k(f:D.k(),ft:D)",
         new NormType(Mdf.Mutable,Path.parse("Outer0::C"),Ph.None),
         new NormType(Mdf.Mutable,Path.parse("Outer0::C"),Ph.None),
         new String[]{"{ C:{mut k(var D f, type D ft)}, D:{k()}}"}
       },{"( D x=D.k(), C.k(f:x,ft:D))",
         new NormType(Mdf.Mutable,Path.parse("Outer0::C"),Ph.None),
         new NormType(Mdf.Mutable,Path.parse("Outer0::C"),Ph.None),
         new String[]{"{ C:{mut k(var D f, type D ft)}, D:{k()}}"}
       
       },{"error D.k()",
         new Ast.FreeType(),
         new Ast.FreeType(),
         new String[]{"{ D:{k()}}"}
       
       },{"( D x=D.k(f:x), x)",
         new Ast.FreeType(),
         new NormType(Mdf.Immutable,Path.parse("Outer0::D"),Ph.None),
         new String[]{"{ D:{k(var D f)} }"}

       },{"List.factory(N.k())",
         new Ast.FreeType(),
         new NormType(Mdf.Immutable,Path.parse("Outer0::List"),Ph.None),
         new String[]{listExample}
       },{"(type List this=List, N that=N.k(), List x=this.factoryAux(that,top:x)  x)",
         new Ast.FreeType(),
         new NormType(Mdf.Immutable,Path.parse("Outer0::List"),Ph.None),
         new String[]{listExample}
       },{TestHelper.multiLine(""
,"(type List this=List,"
," N that=N.k(),"
," List^ top=List.factory(N.k())"
," ( Void z=that.checkZero(),"
,"   catch error x ("
, "    on Void  List.k("
,"       next:List.factoryAux(that.lessOne(),top:top),"
,"       elem:that))"
,"   List.k(next:top,elem:N.k())"
," ))"),
         new Ast.FreeType(),
         new NormType(Mdf.Immutable,Path.parse("Outer0::List"),Ph.Partial),
         new String[]{listExample}

         },{"( D^ x=D.k(f:void), x)",
           new Ast.FreeType(),
           new NormType(Mdf.Immutable,Path.parse("Outer0::D"),Ph.Ph),
           new String[]{"{ D:{k(var Any f)} }"}
           
         },{"( D^ x=D.k(f:void), D.k(f:x))",
           new Ast.FreeType(),
           new NormType(Mdf.Immutable,Path.parse("Outer0::D"),Ph.Partial),
           new String[]{"{ D:{k(var Any f)} }"}

         },{TestHelper.multiLine(""
,"("
,"  Void unused1=("
,"    Void unused00=exception C.k()"
,"    catch exception x ("
,"      on D return { a()}"
,"      on C error void"
,"      )"
,"    void"
,"    )"
,"  catch return result0 ("
,"    on Library result0"
,"    )"
,"  error void"
,"  )"),
new Ast.FreeType(),
new NormType(Mdf.Immutable,Path.Library(),Ph.None),
new String[]{"{ C:{ k()}, D:{k()}}"}
        
}};}
      //TODO: before ts after desugaring, do well formedness check!
      
String listExample=TestHelper.multiLine(
    "{N:{k() method Void checkZero() (void) method N lessOne() (this)}"
    ,"List:{k(List next, N elem)"
    ,"  type method List factory(N that) ("
    ,"    List x=this.factoryAux(that,top:x)"
    ,"    x"
    ,"    )"
    ,"type method List factoryAux(N that, List^ top)"
    ,"  (Void z=that.checkZero()"
    ,"   catch error x (on Void "
    ,"     List.k("
    ,"       next:List.factoryAux(that.lessOne(),top:top),"
    ,"       elem:that))"
    ,"   List.k(next:top,elem:N.k()) )"
    ,"}}");     
      
    @Test(dataProvider="e,typeSugg,typeExpected,stage,program")
    public void testType(String es,Type sugg,Type expected,String []program) {
      ExpCore e=Desugar.of(Parser.parse(null," "+es)).accept(new InjectionOnCore());
      List<Path> paths = CollectPaths0.of(e);//TODO: is it usefull?
      Program p=TestHelper.getProgram(paths, program);
      Type t2=TypeSystem.typecheckSure(false,p, new HashMap<>(),SealEnv.empty(),new ThrowEnv(), sugg, e);
      Assert.assertEquals(t2,expected);
      //TestHelper.assertEqualExp(eRed,ee2);
    }
      
    }

  @Test(singleThreaded=true, timeOut = 500)
  public class TestStage2 {
    @DataProvider(name = "e,typeSugg,typeExpected,stage,program")
    public Object[][] createData1() {
      return new Object[][] {
      {"void",
      new NormType(Mdf.Immutable,Path.Void(),Ph.None),
      new NormType(Mdf.Capsule,Path.Void(),Ph.None),
      new String[]{"{ C:{k()}}"}
    },{"C.k()",
      new NormType(Mdf.Capsule,Path.parse("Outer0::C"),Ph.None),
      new NormType(Mdf.Capsule,Path.parse("Outer0::C"),Ph.None),
      new String[]{"{ C:{mut k()}}"}
    },{"C.k(f:D.k()).f()",
      new NormType(Mdf.Readable,Path.parse("Outer0::D"),Ph.None),
      new NormType(Mdf.Readable,Path.parse("Outer0::D"),Ph.None),
      new String[]{"{ C:{mut k(var mut D f)} D:{mut k()}}"}
    },{"C.k(f:D.k()).#f()",
      new NormType(Mdf.Mutable,Path.parse("Outer0::D"),Ph.None),
      new NormType(Mdf.Mutable,Path.parse("Outer0::D"),Ph.None),
      new String[]{"{ C:{mut k(var mut D f)} D:{mut k()}}"}
    },{"C.k(f:D.k()).f()",
      new NormType(Mdf.Immutable,Path.parse("Outer0::D"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("Outer0::D"),Ph.None),
      new String[]{"{ C:{mut k(var mut D f)} D:{mut k()}}"}

    },{"(lent Vector v=Vector.k(), mut B b=B.k(N.k()),v.add(b.clone()))",
      new NormType(Mdf.Immutable,Path.Void(),Ph.None),
      new NormType(Mdf.Immutable,Path.Void(),Ph.None),
      new String[]{cloneExample}
    },{"(A a=A.k(f:B.k(N.k()).clone()) a)",
      new NormType(Mdf.Immutable,Path.parse("Outer0::A"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("Outer0::A"),Ph.None),
      new String[]{cloneExample}
  //is ok, clone vuole readable
    },{"(mut B b=B.k(N.k()), A a=A.k(f:b.clone()) a)",
      new NormType(Mdf.Immutable,Path.parse("Outer0::A"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("Outer0::A"),Ph.None),
      new String[]{cloneExample}

    },{"(mut B b=B.k(N.k()), A a=A.k(f:b.clone()) a)",
      new NormType(Mdf.Immutable,Path.parse("Outer0::A"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("Outer0::A"),Ph.None),
      new String[]{cloneExample}
   
    },{" ( B bi=A.k(f:(read B b=B.k(N.k()) b).clone()).f() bi)",
      new NormType(Mdf.Immutable,Path.parse("Outer0::B"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("Outer0::B"),Ph.None),
      new String[]{cloneExample}

    },{" (read B b=B.k(N.k()),  B bi=A.k(f:b.clone()).f() bi)",
      new NormType(Mdf.Immutable,Path.parse("Outer0::B"),Ph.None),
      new NormType(Mdf.Immutable,Path.parse("Outer0::B"),Ph.None),
      new String[]{cloneExample}
    },{" (AI any=(mut AI aI=AI.k() aI) any)",
    new NormType(Mdf.Immutable,Path.parse("Outer0::AI"),Ph.None),
    new NormType(Mdf.Immutable,Path.parse("Outer0::AI"),Ph.None),
    new String[]{"{ AI:{mut k()} }"}
    
    },{"error D.k()",//get capsule promoted
      new Ast.FreeType(),
      new Ast.FreeType(),
      new String[]{"{ D:{ mut k()}}"}
    },{"( D().m(D()) )",
      new Ast.FreeType(),
      new NormType(Mdf.Immutable,Path.Void(),Ph.None),
      new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}
    },{"( mut D x=D() mut D y=D() x.m(y)  )",
      new Ast.FreeType(),
      new NormType(Mdf.Immutable,Path.Void(),Ph.None),
      new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}
    },{"( lent D x=D() mut D y=D() x.m(y)  )",//Deceiving, but it should pass! as for ( lent D x=D() ( mut D y=D() x.m(y) ) ) 
      new Ast.FreeType(),
      new NormType(Mdf.Immutable,Path.Void(),Ph.None),
      new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}
    },{
      "Reader.readCustomer()",
      new Ast.FreeType(),
      new NormType(Mdf.Capsule,Path.parse("Outer0::Customer"),Ph.None),
      new String[]{"{ () \n Customer:{ mut() }\n Reader :{()\n"
      +" type method capsule Customer readCustomer() (\n"
      +"   mut Customer c=Customer()\n"
      +"   c 'ok, capsule promotion here\n"
      +" )}}"}
    },{
        "Reader.readCustomer()",
        new Ast.FreeType(),
        new NormType(Mdf.Capsule,Path.parse("Outer0::Customer"),Ph.None),
        new String[]{"{() \n Customer:{ mut() }\n Reader :{()\n"
        +" type method capsule Customer readCustomer() {\n"
        +" return Customer()"
        +" }}}"}
    },{
      "Reader.readCustomer()",
      new Ast.FreeType(),
      new NormType(Mdf.Capsule,Path.parse("Outer0::Customer"),Ph.None),
      new String[]{"{() \n Customer:{ mut() }\n Reader :{()\n"
      +" type method capsule Customer readCustomer() (\n"
      +"   mut Customer c=Customer()\n"
      +"   return c 'ok, capsule promotion here\n"
      +"   catch return x (on mut Customer x)"
      +"   error void)}}"}
    },{
      "Reader.readCustomer()",
      new Ast.FreeType(),
      new NormType(Mdf.Capsule,Path.parse("Outer0::Customer"),Ph.None),
      new String[]{"{() \n Customer:{ mut() }\n Reader :{()\n"
      +" type method capsule Customer readCustomer() {\n"
      +"   mut Customer c=Customer()\n"
      +"   return c 'ok, capsule promotion here\n"
      +" }}}"}
    }};}
    
    String cloneExample=TestHelper.multiLine("{"
        ,"  N:{k()}"
        ,"  B:{mut k(var N that)"
        ,"    read method"
        ,"    capsule B clone() (B.k(this.that()))"
        ,"    }"
        ,"  A:{mut k(mut B f)}"
        ,"  Vector:{mut k() mut method Void add(mut B that) (void)}"
        ,"}");

      @Test(dataProvider="e,typeSugg,typeExpected,stage,program")
      public void testType(String es,Type sugg,Type expected,String []program) {
        TestHelper.configureForTest();
        ExpCore e=Desugar.of(Parser.parse(null," "+es)).accept(new InjectionOnCore());
        List<Path> paths = CollectPaths0.of(e);
        Program p=TestHelper.getProgram(paths,program);
        Configuration.typeSystem.checkAll(p);
        Type t2=TypeSystem.typecheckSure(false,p, new HashMap<>(),SealEnv.empty(),new ThrowEnv(), sugg, e);
        Assert.assertEquals(t2,expected);
        //TestHelper.assertEqualExp(eRed,ee2);
      }
      @DataProvider(name = "!e,typeSugg,typeExpected,stage,program")
      public Object[][] createData2() {
        return new Object[][] {
        {" (mut C x=C(x) x)",
        new NormType(Mdf.Immutable,Path.Void(),Ph.None),
        new NormType(Mdf.Capsule,Path.Void(),Ph.None),
        new String[]{"{ C:{mut (mut C that)}}"}
        },{" (mut C x=C(x) (capsule C y=x y))",
          new NormType(Mdf.Capsule,Path.parse("Outer0::C"),Ph.None),
          new NormType(Mdf.Capsule,Path.parse("Outer0::C"),Ph.None),
          new String[]{"{ C:{mut (mut C that)}}"}
      
      },{" (mut C x=C(x) (capsule C y=x.#that() y))",
        new NormType(Mdf.Capsule,Path.parse("Outer0::C"),Ph.None),
        new NormType(Mdf.Capsule,Path.parse("Outer0::C"),Ph.None),
        new String[]{"{ C:{mut (mut C that)}}"}
      },{"(mut B b=B.k(N.k()), A a=A.k(f:b) a)",
        new NormType(Mdf.Immutable,Path.parse("Outer0::A"),Ph.None),
        new NormType(Mdf.Immutable,Path.parse("Outer0::A"),Ph.None),
        new String[]{cloneExample}
      
      },{"error D.k()",
        new Ast.FreeType(),
        new Ast.FreeType(),
        new String[]{"{ D:{ lent k()}}"}
      },{" ( mut D x= D.k() error x)",
        new Ast.FreeType(),
        new Ast.FreeType(),
        new String[]{"{ D:{ mut k()}}"}
      
      },{"using A check sumInt32(n1:void n2:{}) error void",
          new Ast.FreeType(),
          new Ast.FreeType(),
          new String[]{"{ A:{'@plugin\n'L42.is/connected/withAlu\n}}"}
      //test to check that exception Any can not be captured
      },{"( exception D() catch exception x (on Any void) void)",
        new NormType(Mdf.Immutable,Path.Void(),Ph.None),
        new NormType(Mdf.Immutable,Path.Void(),Ph.None),
        new String[]{"{ D:{k() method Void m() (void)}}"}
      },{"( loop ( exception void"
      +"      catch exception ( on Void ( exception void ) ) void)"
      +"    catch exception p ( on Any ( exception p ) ) void )",
      new NormType(Mdf.Immutable,Path.Void(),Ph.None),
      new NormType(Mdf.Immutable,Path.Void(),Ph.None),
      new String[]{"{ D:{k() method Void m() (void)}}"}
      },{
        "(exception void catch exception p ( on Any ( exception p ) ) void )",
        new NormType(Mdf.Immutable,Path.Void(),Ph.None),
        new NormType(Mdf.Capsule,Path.Void(),Ph.None),
        new String[]{"{ D:{k() method Void m() (void)}}"}

            },{ "( ( exception void"
                +"      catch exception ( on Void ( exception void ) ) void)"
                +"    catch exception p ( on Any ( exception p ) ) void )",
                new NormType(Mdf.Immutable,Path.Void(),Ph.None),
                new NormType(Mdf.Capsule,Path.Void(),Ph.None),
                new String[]{"{ D:{k() method Void m() (void)}}"}
            },{"( exception D.k() catch exception x (on Any void) void)",//should fail
              new NormType(Mdf.Immutable,Path.Void(),Ph.None),
              new NormType(Mdf.Capsule,Path.Void(),Ph.None),
              new String[]{"{ D:{k() method Void m() (void)}}"}
            },{"( mut D x=D() lent D y=D() x.m(y)  )",//must fail//ok 
              new Ast.FreeType(),
              new NormType(Mdf.Immutable,Path.Void(),Ph.None),
              new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}
            },{"( mut D y=D() lent D x=D() x.m(y)  )",//must fail//ok 
              new Ast.FreeType(),
              new NormType(Mdf.Immutable,Path.Void(),Ph.None),
              new String[]{"{() D:{ mut () mut method Void m(mut D that) error void }}"}



      }};}
      @Test(dataProvider="!e,typeSugg,typeExpected,stage,program",expectedExceptions=ErrorMessage.TypeError.class)
      public void testFail(String es,Type sugg,Type expected,String []program) {
        ExpCore e=Desugar.of(Parser.parse(null," "+es)).accept(new InjectionOnCore());
        List<Path> paths = CollectPaths0.of(e);
        Program p=TestHelper.getProgram(paths,program);
        Type t2=TypeSystem.typecheckSure(false,p, new HashMap<>(),SealEnv.empty(),new ThrowEnv(), sugg, e);
        //Assert.assertEquals(t2,expected);
        //TestHelper.assertEqualExp(eRed,ee2);
      }
        
      } 

 
}



