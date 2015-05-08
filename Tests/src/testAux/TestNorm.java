package testAux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

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
  @RunWith(Parameterized.class)
  public static class Test1 {
    @Parameter(0) public String _p1;
    @Parameter(1) public String _p2;
    @Parameter(2) public String[] prog;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
         {"Outer0::A","Outer0::A",new String[]{}
       },{"Outer0::A","Outer0::A",new String[]{"{C:{}}"}
       },{"Outer0::A","Outer0::A",new String[]{"{C:##walkBy}"}
       },{"Outer1::A","Outer1::A",new String[]{"{A:{}}","{C:{}}"}
       },{"Outer1::C","Outer1::C",new String[]{"{A:{}}","{C:{}}"}
       },{"Outer1::C","Outer0",new String[]{"{A:{}}","{C:##walkBy}"}
       },{"Outer2::D::C","Outer0",new String[]{"{A:{}}","{C:##walkBy}","{D:##walkBy}"}
       },{"Outer2::D::C","Outer1::C",new String[]{"{A:{}}","{C:{}}","{D:##walkBy}"}
       }});}

    @Test
    public void testOk() {
      Path pp1=Path.parse(_p1);
      Path pp2=Path.parse(_p2);
      Program p=Program.empty();
      List<String>inp=Arrays.asList(prog);
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
