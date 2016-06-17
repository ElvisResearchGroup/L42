package testAdamTowel01;

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
public class TestBase02 {

  @Before
  public void initialize() throws Throwable {
    //TestHelper.configureForTest();
    //System.out.println("AssertionsDisabled");
    //ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(false);
    L42.trustPluginsAndFinalProgram=true;
    //_02_00DeployAdamTowel02();
    }
  //not run when single test executed?

  //@Test
  public  void _00_00AJustToWarmUpJVM() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseAdamTowel01.L42"});
    }

  @Test
  public  void _02_00DeployAdamTowel02() throws Throwable{
    TestHelper.configureForTest();
    Paths.get("localhost","AdamTowel02.L42").toFile().delete();
    L42.main(new String[]{"examples/DeployAdamTowel02"});
    Assert.assertTrue(Paths.get("localhost","AdamTowel02.L42").toFile().exists());
  }

  @Test
  public  void _02_01UseLib() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel02/UseLib.L42"});
    Assert.assertEquals(L42.record.toString(),"Hello World 42\n");
    }

  @Test
  public  void _02_02DeploySimpleLib() throws Throwable{
    TestHelper.configureForTest();
    //new TestBase01()._01_00DeployAdamTowel01();
    //this._02_00DeployAdamTowel02();
    Paths.get("localhost","DeployedSimpleLib.L42").toFile().delete();
    L42.main(new String[]{"examples/testsForAdamTowel02/DeploySimpleLib.L42"});
    Assert.assertTrue(Paths.get("localhost","DeployedSimpleLib.L42").toFile().exists());
    }
  @Test
  public  void _02_03LoadDeployedSimpleLib() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel02/UseDeployedSimpleLib.L42"});
    Assert.assertEquals(L42.record.toString(),"Hello World Deployed\n");
    }
   @Test
  public  void _02_04UseOperators1() throws Throwable{
    TestHelper.configureForTest();
    //new TestBase01()._01_00DeployAdamTowel01();
    //this._02_00DeployAdamTowel02();
    L42.main(new String[]{"examples/testsForAdamTowel02/UseOperators1.L42"});
    Assert.assertEquals(L42.record.toString(),
        "c1c2c3\nc1c2c3\nic1ic2ic3\nc1c2c3\nc1c2c3\nc1c2c3c1c2c3c1c2c3\n");
    }
   @Test
   public  void _02_05UseAssertPreHold() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestAssertPreHold.L42"});
     TestHelper.check42Fails(L42.record.toString());
     }

   //OLD unused @Test
   public  void _02_06IsConcrete() throws Throwable{
     TestHelper.configureForTest();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestIsConcrete.L42"});
     Assert.assertEquals(L42.record.toString(),
         "DanielWriteHere\n");
     }
   //@Test//Discover why binary flag code take some much to compile? or will not be a problem in optimized code?
   public  void _02_101BinaryFlag32() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestBinaryFlag32.L42"});
     Assert.assertEquals(L42.record.toString(),
         "B00000000000000000000000000000101\n5\nB00000000000000000000000010100101\n"+
         "true\nThis.Mdf.Mutable\nThis.Mdf.Set[Mutable;Readable]\n");
     }
   //B00000000000000000000000000000000



   @Test
   public  void _02_07addInvariant() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestAddInvariant.L42"});
     Assert.assertEquals(L42.record.toString(),
         "OK\n");
     }

   @Test
   public  void _02_08PostOperation() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestPostOperation.L42"});
     Assert.assertEquals(L42.record.toString(),
         "Hello\nWorld\nHello\n");
     }
   @Test
   public  void _02_09Patch() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestPatch.L42"});
     Assert.assertEquals(L42.record.toString(),
         "Hello\nWorld\nHello-\nWorld\nOK\n");
     }

   @Test
   public  void _02_10DefaultParameter() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestDefaultParameter.L42"});
     Assert.assertEquals(L42.record.toString(),
         "Hello\nWorld -- 42\n");
     }

   @Test
   public  void _02_11Wither() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestWither.L42"});
     Assert.assertEquals(L42.record.toString(),
         "20\n20\n20\n10\n10\n20\n10\n10\n");
     }

   @Test
   public  void _02_12TestRedirectType() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestRedirectType.L42"});
     Assert.assertEquals(L42.record.toString(),
         "{\nmethod \nThis0.C c() \nC:{}}\n"+
         "{\nmethod \nThis1.A c() }\n"+
         "{\nmethod \nThis0.B c() \nB:{}}\n"+
         "{\nmethod \nVoid c() }\n");
     }

@Test
   public  void _02_13AddToS() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestToS.L42"});
     Assert.assertEquals(L42.record.toString(),
     "[name:\"Bob\", age:\"10\"]\n"+
     "[name:\"Bob\", age:?]\n"+
     "[name:This.S\"Bob\", age:??]\n"+
     "[p:[name:\"Bob\", age:\"10\"]]\n"+
     "[p:This.Person[name:\"Bob\", age:\"10\"]]\n"
     ); }


@Test
   public  void _02_14AddEquals() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestAddEquals.L42"});
     TestHelper.check42Fails(L42.record.toString());
     }

@Test
   public  void _02_15Data() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestData.L42"});
     TestHelper.check42Fails(L42.record.toString());
     }
@Test
   public  void _02_16Num() throws Throwable{
     TestHelper.configureForTest();
     //new TestBase01()._01_00DeployAdamTowel01();
     //this._02_00DeployAdamTowel02();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestNum.L42"});
     TestHelper.check42Fails(L42.record.toString());
     }

  @Test
   public  void _02_101HelloWorld() throws Throwable{
     TestHelper.configureForTest();
     L42.main(new String[]{"examples/testsForAdamTowel02/TestHelloWorld.L42"});
     Assert.assertEquals(L42.record.toString(),
         "the max for [1;2;3] is 3!\n");
     }

  @Test
  public  void _02_17Range() throws Throwable{
    TestHelper.configureForTest();
    //new TestBase01()._01_00DeployAdamTowel01();
    //this._02_00DeployAdamTowel02();
    L42.main(new String[]{"examples/testsForAdamTowel02/TestRange.L42"});
    TestHelper.check42Fails(L42.record.toString());
    }

  @Test
  public  void _02_18Alphanumeric() throws Throwable{
    TestHelper.configureForTest();
    //new TestBase01()._01_00DeployAdamTowel01();
    //this._02_00DeployAdamTowel02();
    L42.main(new String[]{"examples/testsForAdamTowel02/TestAlphanumeric.L42"});
    TestHelper.check42Fails(L42.record.toString());
    }

  @Test
  public  void _02_19StringSplit() throws Throwable{
    TestHelper.configureForTest();
    //new TestBase01()._01_00DeployAdamTowel01();
    //this._02_00DeployAdamTowel02();
    L42.main(new String[]{"examples/testsForAdamTowel02/TestStringSplit.L42"});
    TestHelper.check42Fails(L42.record.toString());
    }
  
  @Test
  public  void _02_20EnumerationPostFix() throws Throwable{
    TestHelper.configureForTest();
    //new TestBase01()._01_00DeployAdamTowel01();
    //this._02_00DeployAdamTowel02();
    L42.main(new String[]{"examples/testsForAdamTowel02/TestEnumerationPostfix.L42"});
    TestHelper.check42Fails(L42.record.toString());
    }
    
  }
