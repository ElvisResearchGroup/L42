package adamsTowel01;

// TestB.java is a near-literal copy of Test.java, for the benefit of projects that wish
// to keep two configurations.  The expected use is that one will rebuild towels sequentially,
// while the other will run all other tests in parallel.

import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.runners.Parameterized.Parameters;

import helpers.TestRunner.Opt;

public class TestAdamTowel1Tests extends helpers.TestRunner{
  @Parameters(name = "{index}:{1}")
  public static List<Object[]> go(){
    List<Object[]> tests = goInner(
      //// global options
      // Opt.NoAss,  // Run without assertions enabled (faster)
      //Opt.NoTrust, // Run without trusting plugins (much slower)
      //Opt.Parallel, // Double the number of threads of concurrent tests
                       // (starting from one; as many times as you like; will not wait for towels)
      //Opt.Parallel,
      //Opt.Parallel,
      Opt.ProfilerPrintOff,//disable profiler print and final profiling computation
      //// big individual deployment options
      //Opt.DeplAT1, // AdamsTowel01
      // Opt.DeplAT2, // AdamsTowel02
      // Opt.Project, // Run the local libProject as a folder, expecting it to deploy a project towel
      //// options for deploying small things
      "TestLocation.L42",
      "TestDebug.L42", // Name of a file in libTests; edit to match your file
      "TestHelloWorld.L42",
      "TestK.L42",
      "TestParseAnnotations.L42",
      ////was a parsing error, now added to testParseFail"TestParseBadDecoration.L42",
      //// performance drop if remove /* */ ??"TestParseNoBinding.L42",   // adding this in causes quick failure on the seven things with which it shares execution time
      //// It is surprising but correct that is syntax error to have both // and /**/ in that point.
      "TestVector.L42",
      "TestVector02.L42",
      "UseAdamTowel01.L42",
      "UseAdamTowel_SBuilder.L42",
      "UseIntrospectionAdamTowel.L42",
      ////Problem with old cache, will be fixed with new TS soon "UseIntrospectionAdamTowel2.L42",
      "UseIntrospectionAdamTowel3.L42",
      "UseIntrospectionAdamTowel4.L42",
      "TestPlgWrapper.L42",
      ////Problem with old cache, will be fixed with new TS soon "UseIntrospectionAdamTowel5.L42",
      //Opt.AllTests, // All files in libTests, as indivisrcdual tests, in no defined order
      Opt.NOP  // Convenience option, so that all of the other options can end with a comma
    );
    return tests;
  }
}
