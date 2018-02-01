package repl;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import repl.ReplGui.CodeInfo;

public class CachingTest {

  @Test
  public void testCodeInfo_valid() {
    String content="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    CodeInfo test= new CodeInfo(content);

    String first2Line="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()";
    String cacheLibName="L42.is/AdamTowel02";
    String restOfCode="Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    assertEquals(first2Line, test.first2Line);
    assertEquals(cacheLibName, test.cacheLibName);
    assertEquals(restOfCode, test.restOfCode);
  }

  @Test
  public void testCodeInfo_valid_manyNewSpaces() {
    String content="\n\n\n\nreuse L42.is/AdamTowel02\n" +
        "\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    CodeInfo test= new CodeInfo(content);

    String first2Line="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()";
    String cacheLibName="L42.is/AdamTowel02";
    String restOfCode="\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    assertEquals(first2Line, test.first2Line);
    assertEquals(cacheLibName, test.cacheLibName);
    assertEquals(restOfCode, test.restOfCode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCodeInfo_invalid_noTokenAfterReuse() {
    String content="reuse Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";
    CodeInfo test= new CodeInfo(content);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCodeInfo_invalid_onlyFirstLine() {
    String content="reuse Hello\nMain: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";
    CodeInfo test= new CodeInfo(content);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCodeInfo_invalid_invalidClassName() {
    String content="reuse Hello\n"+
        "999:Load.cacheTowel()\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";
    CodeInfo test= new CodeInfo(content);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCodeInfo_invalid_noSemicolonOn2ndLine() {
    String content="reuse Hello\n"+
        "999Load.cacheTowel()\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";
    CodeInfo test= new CodeInfo(content);
  }

}
