package testRunnerDemo;

import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.runners.Parameterized.Parameters;

public class Test extends helpers.TestRunner{
  @Parameters(name = "{index}:{1}")
  public static List<Object[]> go(){return goInner(
   //"testNameToRunOnlyOne",
   //Opt.NoDeplCurrent
   //,Opt.NoAss,Opt.NoTrust,
   //,Opt.DeplAT1,Opt.DeplAT2
      Opt.AllTests,
      Opt.NOP
    );}
}
