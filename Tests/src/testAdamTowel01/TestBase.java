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
public class TestBase {

  //@Before public void initialize() {  TestHelper.configureForTest();}
  //not run when single test executed?

  @Test
  public  void _00DeployAdamTowel01() throws Throwable{
    TestHelper.configureForTest();
    Paths.get("localhost","AdamTowel01.L42").toFile().delete();
    L42.main(new String[]{"examples/DeployAdamTowel01"});
    Assert.assertTrue(Paths.get("localhost","AdamTowel01.L42").toFile().exists());
  }
  //@Test
  public  void _01basicTest() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseAdamTowel01.L42"});
    Assert.assertEquals(L42.record.toString(),"FreeTemplate\nFreeTemplate\nHello Adam 0\nazz\nbzz\nczz\n");
    }
  //@Test
  public  void _02LoadSimpleLib() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseSimpleLib.L42"});
    Assert.assertEquals(L42.record.toString(),"Hello World 42\n");
    }
  //@Test
  public  void _03DeploySimpleLib() throws Throwable{
    TestHelper.configureForTest();
    //TODO: remove file localhost/DeployedSimpleLib.L42
    L42.main(new String[]{"examples/testsForAdamTowel01/DeploySimpleLib.L42"});
    //check now is there
    }
  //@Test
  public  void _04LoadDeployedSimpleLib() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseDeployedSimpleLib.L42"});
    Assert.assertEquals(L42.record.toString(),"Hello World Deployed\n");
    }
  //@Test
  public  void _05DeployCollections() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/DeployCollections"});
    }
  //@Test
  public  void _06UseCollections() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseCollections.L42"});
    Assert.assertEquals(L42.record.toString(),"size is 2 hello world\nhello\nworld\n");
    }
  @Test
  public  void _07introspection() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseIntrospectionAdamTowel.L42"});
    Assert.assertEquals(L42.record.toString(),TestHelper.multiLine(
        "classkind of Bar is FreeTemplate"
       ,"Bas as string: {"
       ,"method "
       ,"Void foo() }"
       ,"--------------------------"
       ,"classkind of Outer0 is FreeTemplate"
       ,"Outer0 as string: {"
       ,"Bar:{"
       ,"method "
       ,"Void foo() }}"
       ,"--------------------------"
       ,"for all the methods of Bar:"
       ,"selector is: foo()"
       ,"return type is:"
       //,""
        ));
    }
}

//  Kind:MethodClash
//  Path:Outer0::N
//  Left: method Library binaryRepr()
//  Right read method 'consistent
//   Library binaryRepr()
//   DifferentThisMdf:true