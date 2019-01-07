package oldTestsNotMaintained;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import facade.L42;
import helpers.TestHelper;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCollections {

  @Before
  public void initialize() {
    //TestHelper.configureForTest();
    System.out.println("AssertionsDisabled");
    ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(false);
    L42.trustPluginsAndFinalProgram=true;
    }
  //not run when single test executed?

  //@Test
  public  void _00_00AJustToWarmUpJVM() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseAdamTowel01.L42"});
    }
    @Test
  public  void _00_00DeployCollections() throws Throwable{
    TestHelper.configureForTest();
    Paths.get("localhost","Collections.L42").toFile().delete();
    L42.main(new String[]{"examples/DeployCollections"});
    Assert.assertTrue(Paths.get("localhost","Collections.L42").toFile().exists());
    }
  @Test
  public  void _01_01UseCollections() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForCollections/UseCollections.L42"});
    Assert.assertEquals(L42.record.toString(),"size is 2 hello world\nhello\nworld\n");
    }
  
   
 
  
  }
