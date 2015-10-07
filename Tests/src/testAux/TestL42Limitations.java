package testAux;

import helpers.TestHelper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;




import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Expression;
import ast.ExpCore.ClassB;
import ast.Ast;
import ast.Ast.Stage;
import ast.Ast.Path;
import auxiliaryGrammar.Program;
import facade.L42;
import facade.Parser;

public class TestL42Limitations {
//TODO: in loop on big step on11/04/2015

//@Test
public void test1() throws IOException{
    TestHelper.configureForTest();
    //L42.setRootPath(Paths.get("dummy"));
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase1"
,"  Time:{'@plugin"
,"    'L42.is/connected/withLimitations"
,"    ()}"
,"  Main:{"
,"    using Time check executionTime(lessThan:200N.binaryRepr()) ("
,"      while Bool.true() ("
,"        using Alu check stringDebug(S\"Foo\".binaryRepr()) void ))"
,"    return ExitCode.normal()"
,"    }"
,"  }"
  )).getErrCode(),0);
  Assert.assertNotEquals(L42.record.toString(),"");
  L42.record=new StringBuilder();
  }
}