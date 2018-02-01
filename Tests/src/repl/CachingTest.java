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
    String content="reuse L42.is/AdamTowel02\n" +
        "\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";
    File file=createFileFromString(content);
    System.out.println(file.exists()+" "+file.getAbsolutePath());

    CodeInfo test= new CodeInfo(file);

    String first2Line="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()";
    String cacheLibName="L42.is/AdamTowel02";
    String restOfCode="\n\n" +
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

  private static File createFileFromString(String s) {

    Charset charset = Charset.forName("US-ASCII");
    Path path = Paths.get("testfile.L42");

    try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
      writer.write(s, 0, s.length());
    } catch (IOException x) {
        System.err.format("IOException: %s%n", x);
    }

    return path.toFile();
  }


}
