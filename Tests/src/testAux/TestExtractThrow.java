package testAux;

import helpers.TestHelper;

import java.util.ArrayList;
import java.util.Arrays;


import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import coreVisitors.ExtractThrow;
import coreVisitors.From;
import facade.Parser;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import sugarVisitors.InjectionOnCore;
import ast.ExpCore;
import ast.Expression;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

public class TestExtractThrow {
  @Test(singleThreaded=true, timeOut = 500)
  public class Test1 {
      @DataProvider(name = "e,e")
      public Object[][] createData1() {
       return new Object[][] {
      {"(void)","##walkBy"
    },{"error void","error void"
    },{"error exception void","exception void"
    },{"(Outer0::C x=void error void)","##walkBy"//OK, since x=void is NOT a valid dv
    },{"(Outer0::C x=Outer0::C.new() error void)","error (Outer0::C x=Outer0::C.new() void)"
    },{"(Outer0::C x=Outer0::C.new() Any y=error void void)","error (void)"
    },{"void.m(that:(Outer0::C x=Outer0::C.new() Any y=error void void))","error (void)"
  }};}

    @Test(dataProvider="e,e")
  public void test(String e1,String er) {
    TestHelper.configureForTest();
    ExpCore ee1=Parser.parse(null," "+e1).accept(new InjectionOnCore());
    ExpCore eer=Parser.parse(null," "+er).accept(new InjectionOnCore());
    Program p=TestHelper.getProgramCD();
    Assert.assertEquals(ExtractThrow.of(p,ee1),eer);
  }
      
    }

}
