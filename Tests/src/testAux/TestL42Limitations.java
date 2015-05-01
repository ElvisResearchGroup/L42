package testAux;

import helpers.TestHelper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;



import org.testng.Assert;
import org.testng.annotations.Test;

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
  
//@Test(singleThreaded=false)
public void test1() throws IOException{
    TestHelper.configureForTest();
    //L42.setRootPath(Paths.get("dummy"));
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"  Time:{'@plugin"
,"    'L42.is/connected/withLimitations"
,"    ()}"
,"  Main:{"
,"    using Time check executionTime(lessThan:200N.that()) ("
//err1: operator precedence over 2000N.that()
//err2: 2000(N.that()) cause err in typechecking since library is not core
//!ms.pop().pop().isEmpty()
//==!(ms.pop().pop().isEmpty())
//200N.that()==(200N).that()
,"      while True() ("
,"        using Alu check stringDebug(S\"Foo\".that()) void ))"
,"    return ExitCode.normal()"
,"    }"
,"  }"
  )).getErrCode(),0);
  Assert.assertNotEquals(L42.record.toString(),"");
  L42.record=new StringBuilder();
  }
}