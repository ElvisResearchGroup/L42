package repl;

import static org.junit.Assert.*;

import org.junit.Test;

import repl.ReplGui.CodeInfo;

public class CachingTest {

  @Test
  public void test() {
    String content="reuse L42.is/AdamTowel02\n" +
        "\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";
    CodeInfo test= new CodeInfo(new File)
  }

}
