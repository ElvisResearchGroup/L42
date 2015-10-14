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

  @Before
  public void initialize() {
    //TestHelper.configureForTest();
    System.out.println("AssertionsDisabled");
    ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(false);
    L42.trustPluginsAndFinalProgram=true;
    }
  //not run when single test executed?

  @Test
  public  void _000AJustToWarmUpJVM01() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseAdamTowel01.L42"});
    }
  @Test
  public  void _00DeployAdamTowel01() throws Throwable{
    TestHelper.configureForTest();
    Paths.get("localhost","AdamTowel01.L42").toFile().delete();
    L42.main(new String[]{"examples/DeployAdamTowel01"});
    Assert.assertTrue(Paths.get("localhost","AdamTowel01.L42").toFile().exists());
  }
  @Test
  public  void _01UseAdamTowel01() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseAdamTowel01.L42"});
    Assert.assertEquals(L42.record.toString(),"FreeTemplate\nFreeTemplate\nHello Adam 0\nazz\nbzz\nczz\nHello Adam n1:0 n2:false endOfString\n");
    }
  @Test
  public  void _02UseSimpleLib() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseSimpleLib.L42"});
    Assert.assertEquals(L42.record.toString(),"Hello World 42\n");
    }
  @Test
  public  void _03DeploySimpleLib() throws Throwable{
    TestHelper.configureForTest();
    Paths.get("localhost","DeployedSimpleLib.L42").toFile().delete();
    L42.main(new String[]{"examples/testsForAdamTowel01/DeploySimpleLib.L42"});
    Assert.assertTrue(Paths.get("localhost","DeployedSimpleLib.L42").toFile().exists());
    }
  @Test
  public  void _04LoadDeployedSimpleLib() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseDeployedSimpleLib.L42"});
    Assert.assertEquals(L42.record.toString(),"Hello World Deployed\n");
    }
  @Test
  public  void _05DeployCollections() throws Throwable{
    TestHelper.configureForTest();
    Paths.get("localhost","Collections.L42").toFile().delete();
    L42.main(new String[]{"examples/DeployCollections"});
    Assert.assertTrue(Paths.get("localhost","Collections.L42").toFile().exists());
    }
  @Test
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
       ,"Void foo() }"
        ,"Beer:{}}"
       ,"--------------------------"
       ,"for all the methods of Bar:"
       ,"selector is: foo()"
       ,"return type is:Type[Void]"
       ,"--------------------------"
       ,"for all the nested classes of Bar:"
       ,"Nested class path is:Beer"
       ,"NestedClass[{}]"
       ,"Nested class path is:Bar"
       ,"NestedClass[{"
       ,"method "
       ,"Void foo() }]"
       //,""
        ));
    }
  
  @Test
  public  void _08introspection2() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseIntrospectionAdamTowel2.L42"});
    Assert.assertEquals(L42.record.toString(),TestHelper.multiLine(
        "fuffa @a beer @Outer1::External bar @::Internal fuzz"
       ,""
       ,"a"
       ,"Report plgFailure as PluginFailure[SafeOperators.introspectLibraryDocPath]"
       ,"Iteration complete"
       ,"Outer1::External"
       ,"External found"
       ,"Iteration complete"
       ,"::Internal"
       ,"Report plgFailure as PluginFailure[SafeOperators.introspectLibraryDocPath]"
       ,"Iteration complete"
        ));
    }
  @Test
  public  void _09introspection3() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseIntrospectionAdamTowel3.L42"});
    Assert.assertEquals(L42.record.toString(),TestHelper.multiLine(
   "Foo!"
  ,"Outer1::External"
  ,"Outer1::Generated"
  ,"Outer1::Generated::Foo"
  ,"Outer1::Debug"
));
    }
  @Test
  public  void _10introspection4() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseIntrospectionAdamTowel4.L42"});
    Assert.assertEquals(L42.record.toString(),TestHelper.multiLine(
 "false"
,"true"
,"m6(), methRootExternal:false, typeExternal:false, typeRefExternal:false"
,"m6(), methRootExternal:true, typeExternal:true, typeRefExternal:true"
,"Outer1::Generated::Foo"
,"m5(), methRootExternal:false, typeExternal:false, typeRefExternal:true"
,"Outer1::External"
,"m5(), methRootExternal:true, typeExternal:true, typeRefExternal:true"
,"Outer1::External"
,"m4(), methRootExternal:false, typeExternal:false, typeRefExternal:false"
,"m4(), methRootExternal:true, typeExternal:true, typeRefExternal:true"
,"Outer1::Generated"
));
    }
  
  @Test
  public  void _11UseOperators1() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseOperators1.L42"});
    Assert.assertEquals(L42.record.toString(),"c1c2c3\n");
    }
  
  
  
  
  }

//  Kind:MethodClash
//  Path:Outer0::N
//  Left: method Library binaryRepr()
//  Right read method 'consistent
//   Library binaryRepr()
//   DifferentThisMdf:true