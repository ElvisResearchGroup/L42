package adamsTowel01;

import java.nio.file.Path;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

public class SeeFile extends helpers.SeeFile{
  @Parameters(name = "{index}:{0}")
  public static List<Object[]> go(){return goInner();}
}
