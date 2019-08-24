package adamsTowel02;

// TestB.java is a near-literal copy of Test.java, for the benefit of projects that wish
// to keep two configurations.  The expected use is that one will rebuild towels sequentially,
// while the other will run all other tests in parallel.

import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.runners.Parameterized.Parameters;

import helpers.TestRunnerPrePost;
import tools.TestRunner.Opt;

public class TestAdamTowel2Tests extends TestRunnerPrePost{
  @Parameters(name = "{index}:{1}")
  public static List<Object[]> go(){
    List<Object[]> tests = goInner(
      //// global options
      // Opt.NoAss,  // Run without assertions enabled (faster)
      // Opt.NoTrust, // Run without trusting plugins (much slower)
      // Opt.Parallel, // Double the number of threads of concurrent tests
                       // (starting from one; as many times as you like; will not wait for towels)
      // Opt.Parallel,
      Opt.ProfilerPrintOff,//disable profiler print and final profiling computation

      //// big individual deployment options
      //Opt.DeplAT1, // AdamsTowel01
      //Opt.DeplAT2, // AdamsTowel02

      // Opt.Project, // Run the local libProject as a folder, expecting it to deploy a project towel
      //// options for deploying small things
      //"_.L42", // Name of a file in libTests; edit to match your file
      /*"TestLambdaFullBeta.L42",
      //"TestExpProblemScalaWayCollatedNoCast.L42",//TODO: fails now
      "TestExpProblemScalaWay.L42",

      "TestPointAlgebra.L42",
      "TestPointFC.L42",
      "TestResource.L42",
      "TestHelloWorld.L42",
      //"TestRefactorFailToS.L42",//TODO: fails now
      "DeploySimpleLib.L42",
      "UseDeployedSimpleLib.L42",
      //--"TestAddEquals.L42",
      //--"TestAddInvariant.L42",
      "TestAlphanumeric.L42",
      "TestAssertPreHold.L42",
      //Binary flag still disabled "TestBinaryFlag32.L42",
      //--"TestDefaultParameter.L42",
      "TestEnumerationPostfix.L42",
      //////Not a valid test, more of a test template "TestIsConcrete.L42",
      "TestNum.L42",
      "TestPatch.L42",
      "TestPostOperation.L42",
      "TestRange.L42",
      "TestRedirectType.L42",
      //--"TestStringSplit.L42",
      //--"TestWither.L42",
      "UseLib.L42",
      "UseOperators1.L42",
      "TestData.L42",
      "Test42Data.L42",
      "TestDData.L42",
      "TestEiffelData.L42",
      "TestDataEncapsulated.L42",
      "TestToS.L42",
      "TestTrait.L42",
      "TestUnicode.L42",
      "TestOpt.L42",
      "HamsterInvariant.L42",
      "TestGraphInterfaces.L42",*/
      //"TestLogs.L42",
      "TestTraitCompostionGame.L42",
      //Opt.AllTests, // All files in libTests, as individual tests, in no defined order
      Opt.NOP  // Convenience option, so that all of the other options can end with a comma
    );
    return tests;
  }
}
