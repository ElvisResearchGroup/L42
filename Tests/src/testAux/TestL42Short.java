package testAux;

import helpers.TestHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.ErrorMessage;
import facade.L42;


public class TestL42Short {
  @Test
  public void test1() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{ reuse L42.is/nanoBase0"
,"C:{'@exitStatus"
," '0"
,"}"
,"}"
        )).getErrCode(),0);
  }

  @Test
  public void test2() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
,"    C:{"
//,"    'if True() (return ExitCore.normal())"
//,"    'return ExitCode.failure()"
,"    return ExitCode.normal()"
,"    }"
,"  }"
)).getErrCode(),0);
}

  @Test
  public void test3() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
,"    C:{"
,"    if True() (return ExitCode.normal())"
,"    return ExitCode.failure()"
,"    }"
,"  }"
)).getErrCode(),0);}

  @Test
  public void test3b() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
,"    C:{"
,"    if False() (return ExitCode.failure())"
,"    return ExitCode.normal()"
,"    }"
,"  }"
)).getErrCode(),0);}


  @Test
  public void test4() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"  {reuse L42.is/nanoBase0"
,"    C:{if True() & False() (return ExitCode.failure())"
,"      return ExitCode.normal()}}"
)).getErrCode(),0);}

  @Test
  public void test5() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"  {reuse L42.is/nanoBase0"
,"    C:{if True() & True() (return ExitCode.failure())"
,"      return ExitCode.normal()}}"
)).getErrCode(),42000);}

}
