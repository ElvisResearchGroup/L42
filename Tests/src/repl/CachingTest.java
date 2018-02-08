package repl;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.junit.Test;

import facade.L42;
import helpers.TestHelper;
import repl.ReplMain.CodeInfo;

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
    String cacheLibUrl="L42.is/AdamTowel02";
    String restOfCode="Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    assertEquals(first2Line, test.first2Line);
    assertEquals(cacheLibUrl, test.cacheLibUrl);
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
    String cacheLibUrl="L42.is/AdamTowel02";
    String restOfCode="\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    assertEquals(first2Line, test.first2Line);
    assertEquals(cacheLibUrl, test.cacheLibUrl);
    assertEquals(restOfCode, test.restOfCode);
  }

  @Test
  public void testCodeInfo_valid_manyNewSpaces2() {
    String content="reuse          L42.is/AdamTowel02\n" +
        ",,,,,,,,,,,,,,,,,,,,,,,,,," +
        ",,,,,,,,,,,,,CacheAdamTowel02:Load.cacheTowel()\n" +
        "\n,,,,,,,,,,,,,,,,,,,,,," +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    CodeInfo test= new CodeInfo(content);

    String first2Line="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()";
    String cacheLibUrl="L42.is/AdamTowel02";
    String restOfCode="\n,,,,,,,,,,,,,,,,,,,,,," +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    assertEquals(first2Line, test.first2Line);
    assertEquals(cacheLibUrl, test.cacheLibUrl);
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
        "HelloLoad.cacheTowel()\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";
    CodeInfo test= new CodeInfo(content);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCodeInfo_invalid_lotsOfSemicolons() {
    String content="reuse Hello\n"+
        "H:el:lo:Load.cacheTowel()\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";
    CodeInfo test= new CodeInfo(content);
  }

  @Test
  public void testCopyResetKVCthenRun_createFolderInL42IDE() throws IOException {
    TestHelper.configureForTest();

    String content="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    CodeInfo test= new CodeInfo(content);

    L42.setRootPath(Paths.get("TestFolder"));

    ReplMain.copyResetKVCthenRun(content);

    this.deleteDirectory(Paths.get("L42IDE").resolve(test.cacheLibName));
  }

  @Test
  public void testCopyResetKVCthenRun_cachedIsFaster() throws IOException { //also tests resuing cached code
    TestHelper.configureForTest();

    long start=System.currentTimeMillis();

    String content="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    CodeInfo test= new CodeInfo(content);

    L42.setRootPath(Paths.get("TestFolder"));

    ReplMain.copyResetKVCthenRun(content);
    long first=System.currentTimeMillis();
    this.deleteDirectory(Paths.get("L42IDE").resolve(test.cacheLibName));

    ReplMain.copyResetKVCthenRun(content);
    long second=System.currentTimeMillis();
    assertTrue(second-first < first-start);

    this.deleteDirectory(Paths.get("L42IDE").resolve(test.cacheLibName));
  }

  @Test
  public void testCopyResetKVCthenRun_testTwoOutputs_resetCache() throws IOException {
    this.deleteFilesInDirectory(Paths.get("TestFolder"));

    TestHelper.configureForTest();
    {
      String content="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Foo:{class method S hi()S\"hi\"}\n"+
        "Main: {\n" +
        "  Debug(S\"Test one \"++Foo.hi())\n" +
        "  return ExitCode.normal()\n" +
        "  }";

      L42.setRootPath(Paths.get("TestFolder"));
      ReplMain.copyResetKVCthenRun(content);
    }
    String output= L42.record.toString();

    TestHelper.configureForTest();
    {
      String content2="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Foo:{class method S hi()S\"hi\"}\n"+
        "Main: {\n" +
        "  Debug(S\"Test two \"++Foo.hi())\n" +
        "  return ExitCode.normal()\n" +
        "  }";

      L42.setRootPath(Paths.get("TestFolder"));
      ReplMain.copyResetKVCthenRun(content2);
    }
    String output2= L42.record.toString();

    assertEquals("Test one hi\n", output);
    assertEquals("Test two hi\n", output2);

    this.deleteDirectory(Paths.get("L42IDE").resolve("L42.is%2FAdamTowel02"));
  }

  @Test
  public void testCopyResetKVCthenRun_testTwoOutputs_reuseCache() throws IOException {
    try{this.deleteDirectory(Paths.get("L42IDE").resolve("L42.is%2FAdamTowel02"));}
    catch(NoSuchFileException nsfe) {}

    this.deleteFilesInDirectory(Paths.get("TestFolder"));

    TestHelper.configureForTest();
    {
      String content="reuse L42.is/AdamTowel02\n" +
          "CacheAdamTowel02:Load.cacheTowel()\n" +
          "Foo:{class method S hi()S\"hi\"}\n"+
          "Main: {\n" +
          "  Debug(S\"Test one \"++Foo.hi())\n" +
          "  return ExitCode.normal()\n" +
          "  }";

      L42.setRootPath(Paths.get("TestFolder"));
      ReplMain.copyResetKVCthenRun(content);
    }
    String output= L42.record.toString();

    TestHelper.configureForTest();
    //System.gc();
    {
      String content2="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Foo:{class method S hi()S\"hi\"}\n"+
        "Main: {\n" +
        "  Debug(S\"Test two \"++Foo.hi())\n" +
        "  return ExitCode.normal()\n" +
        "  }";

      L42.setRootPath(Paths.get("TestFolder"));
      ReplMain.copyResetKVCthenRun(content2, "This.C42");
    }
    String output2= L42.record.toString();

    assertEquals("Test one hi\n", output);
    assertEquals("Test two hi\n", output2);

    this.deleteDirectory(Paths.get("L42IDE").resolve("L42.is%2FAdamTowel02"));
    this.deleteFilesInDirectory(Paths.get("TestFolder"));
  }

  private void deleteDirectory(Path path) throws IOException {
    Files.walk(path)
      .sorted(Comparator.reverseOrder())
      .map(Path::toFile)
      .forEach(File::delete);
  }

  private void deleteFilesInDirectory(Path path) throws IOException {
    Files.list(path)
      .sorted(Comparator.reverseOrder())
      .map(Path::toFile)
      .forEach(File::delete);
  }

}
