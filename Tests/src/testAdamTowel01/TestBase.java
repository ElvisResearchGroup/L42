package testAdamTowel01;

import org.junit.Assert;
import org.junit.Test;

import facade.L42;
import helpers.TestHelper;

public class TestBase {
  @Test
  public  void testBase() throws Throwable{
    TestHelper.configureForTest();
    //L42.main(new String[]{"examples/DeployAdamTowel01"});
    L42.main(new String[]{"examples/testsForAdamTowel01/UseAdamTowel01.L42"});
    Assert.assertEquals(L42.record.toString(),"FreeTemplate\nFreeTemplate\n");
    }
  @Test
  public  void testLoadSimpleLib() throws Throwable{
    TestHelper.configureForTest();
    //L42.main(new String[]{"examples/DeployAdamTowel01"});
    L42.main(new String[]{"examples/testsForAdamTowel01/UseSimpleLib.L42"});
    Assert.assertEquals(L42.record.toString(),"Hello World 42\n");
    }
  @Test
  public  void testDeploySimpleLib() throws Throwable{
    TestHelper.configureForTest();
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/DeploySimpleLib.L42"});
    //how to check success?
    }
}

//  Kind:MethodClash
//  Path:Outer0::N
//  Left: method Library binaryRepr()
//  Right read method 'consistent
//   Library binaryRepr()
//   DifferentThisMdf:true