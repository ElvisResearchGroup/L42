package testAux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import coreVisitors.FreeVariables;
import coreVisitors.From;
import facade.Parser;
import ast.Ast.Path;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.ExpCore;

public class TestFreeVariables {
  @RunWith(Parameterized.class)
  public static class Test1 {
    @Parameter(0) public String _e;
    @Parameter(1) public String[] ss;
    @Parameterized.Parameters
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
      {"x",new String[]{"x"}
    },{" (y)",new String[]{"y"}
    },{" (Outer0::T x=y x)",new String[]{"y"}
    },{" (Outer0::T y=y x)",new String[]{"x"}
    },{" (Outer0::T y=void catch error z( on Outer0::Foo z.foo(that:bar) ) x)",new String[]{"x","bar"}
    },{" (Outer0::List x=this.factoryAux(that:that, top:x) x ) ",new String[]{"this","that"}
    },{" (Outer0::N that=Outer0::N.k() (Outer0::List x=this.factoryAux(that:that, top:x) x )  )",new String[]{"this"}

    }});}

    @Test
    public void testFreeVar() {
      ExpCore e=Parser.parse(null,_e).accept(new InjectionOnCore());
      Set<String> fve = FreeVariables.of(e);
      Set<String>in=new HashSet<String>(Arrays.asList(ss));
      Assert.assertEquals(fve.toString(),in.toString());
      Assert.assertEquals(fve,in);
    }
  }
 }
