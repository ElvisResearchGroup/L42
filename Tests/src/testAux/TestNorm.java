package testAux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import coreVisitors.From;
import facade.Parser;
import ast.Ast.Path;
import ast.Expression;
import sugarVisitors.InjectionOnCore;
import ast.ExpCore;
import ast.ExpCore.*;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class TestNorm {
  @Test(singleThreaded=true, timeOut = 500)
  public class Test1 {
      @DataProvider(name = "p,paths2")
      public String[][] createData1() {
       return new String[][] {
       {"Outer0::A","Outer0::A"
       },{"Outer0::A","Outer0::A","{C:{}}"
       },{"Outer0::A","Outer0::A","{C:##walkBy}"
       },{"Outer1::A","Outer1::A","{A:{}}","{C:{}}"
       },{"Outer1::C","Outer1::C","{A:{}}","{C:{}}"
       },{"Outer1::C","Outer0","{A:{}}","{C:##walkBy}"
       },{"Outer2::D::C","Outer0","{A:{}}","{C:##walkBy}","{D:##walkBy}"
       },{"Outer2::D::C","Outer1::C","{A:{}}","{C:{}}","{D:##walkBy}"  }};}

    @Test(dataProvider="p,paths2")
    public void testOk(String... in) {
      Path pp1=Path.parse(in[0]);
      Path pp2=Path.parse(in[1]);
      Program p=Program.empty();
      List<String>inp=Arrays.asList(in).subList(2,in.length);
      inp=new ArrayList<String>(inp);
      Collections.reverse(inp);
      for(String s: inp){
        Expression e=Parser.parse(null,s);
        ExpCore ec=e.accept(new InjectionOnCore());
        assert ec instanceof ClassB;
        p=p.addAtTop((ClassB)ec);
      }
      
      Assert.assertEquals(Norm.of(p,pp1),pp2);
    }
      
    }

}
