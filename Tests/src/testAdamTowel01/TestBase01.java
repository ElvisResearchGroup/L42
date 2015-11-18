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
public class TestBase01 {

  @Before
  public void initialize() {
    //TestHelper.configureForTest();
    System.out.println("AssertionsDisabled");
    ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(false);
    L42.trustPluginsAndFinalProgram=true;
    }
  //not run when single test executed?

  @Test
  public  void _00_00AJustToWarmUpJVM() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseAdamTowel01.L42"});
    }
  @Test
  public  void _01_00DeployAdamTowel01() throws Throwable{
    TestHelper.configureForTest();
    Paths.get("localhost","DeployTowel.L42").toFile().delete();
    Paths.get("localhost","AdamTowel01.L42").toFile().delete();
    L42.main(new String[]{"examples/DeployAdamTowel01"});
    Assert.assertTrue(Paths.get("localhost","DeployTowel.L42").toFile().exists());
    Assert.assertTrue(Paths.get("localhost","AdamTowel01.L42").toFile().exists());
  }
 @Test
  public  void _01_01UseAdamTowel01() throws Throwable{
    TestHelper.configureForTest();
    L42.main(new String[]{"examples/testsForAdamTowel01/UseAdamTowel01.L42"});
    Assert.assertEquals(L42.record.toString(),"FreeTemplate\nFreeTemplate\nHello Adam 0\nazz\nbzz\nczz\nHello Adam n1:0 n2:false endOfString\n");
    }
 
 @Test
 public  void _01_02introspection() throws Throwable{
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
      ,"return type is:--NameStillToFix--[Void]"
      ,"--------------------------"
      ,"for all the nested classes of Bar:"
      ,"Nested class path is:Beer"
      ,"--NameStillToFix--[{}]"
      ,"Nested class path is:Bar"
      ,"--NameStillToFix--[{"
      ,"method "
      ,"Void foo() }]"
      //,""
       ));
   }
 
 @Test
 public  void _01_03introspection2() throws Throwable{
   TestHelper.configureForTest();
   L42.main(new String[]{"examples/testsForAdamTowel01/UseIntrospectionAdamTowel2.L42"});
   Assert.assertEquals(L42.record.toString(),TestHelper.multiLine(
       "fuffa @a beer @Outer1::External bar @::Internal fuzz"
      ,""
      ,"a"
      ,"Report plgFailure as --NameStillToFix--\"SafeOperators.introspectLibraryDocPath\""
      ,"Iteration complete"
      ,"Outer1::External"
      ,"External found"
      ,"Iteration complete"
      ,"::Internal"
      ,"Report plgFailure as --NameStillToFix--\"SafeOperators.introspectLibraryDocPath\""
      ,"Iteration complete"
       ));
   }
 @Test
 public  void _01_04introspection3() throws Throwable{
   TestHelper.configureForTest();
   L42.main(new String[]{"examples/testsForAdamTowel01/UseIntrospectionAdamTowel3.L42"});
   Assert.assertEquals(L42.record.toString(),TestHelper.multiLine(
  "Foo!"
 ,"Outer0::External"
 ,"Outer0::Generated"
 ,"Outer0::Generated::Foo"
 ,"Outer0::Debug"
));
   }
 @Test
 public  void _01_05introspection4() throws Throwable{
   TestHelper.configureForTest();
   L42.main(new String[]{"examples/testsForAdamTowel01/UseIntrospectionAdamTowel4.L42"});
   Assert.assertEquals(L42.record.toString(),TestHelper.multiLine(
"false"
,"true"
,"m6(), methRootExternal:false, typeExternal:false, typeRefExternal:false"
,"m6(), methRootExternal:true, typeExternal:true, typeRefExternal:true"
,"Outer0::Generated::Foo"
,"m5(), methRootExternal:false, typeExternal:false, typeRefExternal:true"
,"Outer0::External"
,"m5(), methRootExternal:true, typeExternal:true, typeRefExternal:true"
,"Outer0::External"
,"m4(), methRootExternal:false, typeExternal:false, typeRefExternal:false"
,"m4(), methRootExternal:true, typeExternal:true, typeRefExternal:true"
,"Outer0::Generated"
));
   }
 
 @Test
 public  void _01_06introspection5() throws Throwable{
   TestHelper.configureForTest();
   L42.main(new String[]{"examples/testsForAdamTowel01/UseIntrospectionAdamTowel5.L42"});
   Assert.assertEquals(L42.record.toString(),"OK\n");}
  }
