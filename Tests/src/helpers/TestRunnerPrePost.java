package helpers;

import org.junit.Test;

import facade.L42;

public abstract class TestRunnerPrePost extends tools.TestRunner{
  public void pre(){TestHelper.configureForTest();}
  public void post(){TestHelper.check42Fails(L42.record.toString());}
}
