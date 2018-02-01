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
  public void testCodeInfo() {
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

//  @Test(expected = IllegalArgumentException.class)
//  public void testCodeInfo2() {
//    String content="Main: {\n" +
//        "  Debug(S\"Hello world\")\n" +
//        "  return ExitCode.normal()\n" +
//        "  }";
//    CodeInfo test= new CodeInfo(new File(content));
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void testCodeInfo3() {
//    String content="Main: {\n" +
//        "  Debug(S\"Hello world\")\n" +
//        "  return ExitCode.normal()\n" +
//        "  }";
//    CodeInfo test= new CodeInfo(new File(content));
//  }

}
