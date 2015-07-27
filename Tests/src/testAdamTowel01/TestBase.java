package testAdamTowel01;

import org.junit.Assert;
import org.junit.Test;

import facade.L42;

public class TestBase {
  @Test
  public  void testBase() throws Throwable{
    //L42.main(new String[]{"examples/DeployAdamTowel01"});
    L42.main(new String[]{"examples/testsForAdamTowel01/UseAdamTowel01.L42"});
    Assert.assertEquals(L42.record.toString(),"Template\nBox\n");
    }
  @Test
  public  void testLoadSimpleLib() throws Throwable{
    //L42.main(new String[]{"examples/DeployAdamTowel01"});
    L42.main(new String[]{"examples/testsForAdamTowel01/UseSimpleLib.L42"});
    Assert.assertEquals(L42.record.toString(),"Template\nBox\n------------------------------\nEND (zero for success): 0");
    }
}