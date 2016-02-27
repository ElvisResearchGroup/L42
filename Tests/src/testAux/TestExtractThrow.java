package testAux;

import helpers.TestHelper;

import java.util.ArrayList;
import java.util.Arrays;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

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

    @RunWith(Parameterized.class)
    public static class Test1 {
      @Parameter(0) public String e1;
      @Parameter(1) public String er;
      @Parameterized.Parameters
      public static List<Object[]> createData() {
        return Arrays.asList(new Object[][] {
      {"(void)","##walkBy"
    },{"error void","error void"
    },{"error exception void","exception void"
    },{"(This0.C x=void error void)","##walkBy"//OK, since x=void is NOT a valid dv
    },{"(This0.C x=This0.C.new() error void)","error (This0.C x=This0.C.new() void)"
    },{"(This0.C x=This0.C.new() Any y=error void void)","error (void)"
    },{"void.m(that:(This0.C x=This0.C.new() Any y=error void void))","error (void)"
  }});}

  @Test
  public void test() {
    TestHelper.configureForTest();
    ExpCore ee1=Parser.parse(null," "+e1).accept(new InjectionOnCore());
    ExpCore eer=Parser.parse(null," "+er).accept(new InjectionOnCore());
    Program p=TestHelper.getProgramCD();
    Assert.assertEquals(ExtractThrow.of(p,ee1),eer);
  }

    }

}
