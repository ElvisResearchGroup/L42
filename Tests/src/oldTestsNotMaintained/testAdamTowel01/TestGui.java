package oldTestsNotMaintained.testAdamTowel01;

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
public class TestGui {

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
  public  void _00_00DeployGui() throws Throwable{
    TestHelper.configureForTest();
    //new TestBase01()._01_00DeployAdamTowel01();
    //new TestBase02()._02_00DeployAdamTowel02();
    Paths.get("localhost","GuiLib.L42").toFile().delete();
    L42.main(new String[]{"examples/DeployGui.L42"});
    Assert.assertTrue(Paths.get("localhost","GuiLib.L42").toFile().exists());
    }
  @Test
  public  void _01_01UseGui() throws Throwable{
    TestHelper.configureForTest();
    //L42.setRootPath(Paths.get("dummy"));
    L42.main(new String[]{"examples/UseGui.L42"});
    Assert.assertTrue(L42.record.toString().contains("#@Success@#"));
    }




  }
