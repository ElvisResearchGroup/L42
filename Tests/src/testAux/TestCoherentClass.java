package testAux;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import facade.Parser;
import sugarVisitors.InjectionOnCore;

@RunWith(Parameterized.class)
public class TestCoherentClass {
  @Parameter(0) public Mdf mdf;
  @Parameter(1) public Path path;
  @Parameter(2) public String e;
  @Parameter(3) public boolean ok;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {           //a
         {Mdf.Immutable,Path.Any(),"{method Any foo()}",true
      },{Mdf.Immutable,Path.Any(),"{method Void foo()}",false
      },{Mdf.Class,Path.Void(),"{method class Void foo()}",true
      },{Mdf.Readable,Path.Void(),"{method read Void foo()}",true
        //b
      },{Mdf.Immutable,Path.Void(),"{mut method  Void foo( Void that)}",true
      },{Mdf.Immutable,Path.Void(),"{mut method  Void foo( Void that0)}",false
      },{Mdf.Immutable,Path.Void(),"{mut method  Void foo( Void that0, Any any)}",false
      },{Mdf.Immutable,Path.Void(),"{mut method  Void foo( read Void that)}",false
      },{Mdf.Immutable,Path.Void(),"{read method  Void foo( Void that)}",false
      },{Mdf.Immutable,Path.Void(),"{lent method  Void foo( Void that)}",true
      //c
      },{Mdf.Mutable,Path.Void(),"{lent method  mut Void foo()}",false
      },{Mdf.Mutable,Path.Void(),"{mut method  mut Void foo()}",true
      },{Mdf.Mutable,Path.Void(),"{lent method  lent Void foo()}",true
      },{Mdf.Lent,Path.Void(),"{lent method  mut Void foo()}",false
      //d
      },{Mdf.Mutable,Path.Void(),"{lent method  read Void foo()}",true
      },{Mdf.Mutable,Path.Void(),"{read method  read Void foo()}",true
      },{Mdf.Mutable,Path.Void(),"{read method  lent Void foo()}",false
      },{Mdf.Lent,Path.Void(),"{lent method  read Void foo()}",true
      },{Mdf.Lent,Path.Void(),"{read method  read Void foo()}",true
      },{Mdf.Lent,Path.Void(),"{read method  lent Void foo()}",false
       //e//pass
      },{Mdf.Mutable,Path.Void(),"{mut method  Void foo(mut Void that)}",true
      },{Mdf.Lent,Path.Void(),"{mut method  Void foo(mut Void that)}",true
      },{Mdf.Lent,Path.Void(),"{mut method  Void foo(lent Void that)}",true
      },{Mdf.Mutable,Path.Void(),"{mut method  Void foo(lent Void that)}",false
      },{Mdf.Mutable,Path.Void(),"{mut method  Void foo(read Void that)}",false
       //f
      },{Mdf.Mutable,Path.Void(),"{lent method  Void foo(capsule Void that)}",true
      },{Mdf.Lent,Path.Void(),"{lent method  Void foo(capsule Void that)}",true
      },{Mdf.Lent,Path.Void(),"{lent method  Void foo( Void that)}",false
        //g
      },{Mdf.Capsule,Path.Void(),"{read method  read Void foo()}",true
      },{Mdf.Capsule,Path.Void(),"{lent method  read Void foo()}",true
      },{Mdf.Capsule,Path.Void(),"{lent method  lent Void foo()}",true
      },{Mdf.Capsule,Path.Void(),"{capsule method  capsule Void foo()}",true
      },{Mdf.Capsule,Path.Void(),"{mut method  capsule Void foo()}",false
      },{Mdf.Capsule,Path.Void(),"{mut method  mut Void foo()}",true
        //h
      },{Mdf.Capsule,Path.Void(),"{mut method  Void foo(mut Void that)}",true
      },{Mdf.Capsule,Path.Void(),"{mut method  Void foo(capsule Void that)}",true
      },{Mdf.Capsule,Path.Void(),"{mut method  Void foo(lent Void that)}",false
       }});}

    @Test
    public void testCoherence() {
      ClassB cb1=(ClassB)(Parser.parse(null,e).accept(new InjectionOnCore()));
      Program p=Program.emptyLibraryProgram();
      MethodWithType mwt=(MethodWithType)cb1.getMs().get(0);
      //boolean res=Functions.coherent(p, mdf, path, mwt);
      //Assert.assertEquals(res,ok);
      //TODO: disabled for now
      }
    }