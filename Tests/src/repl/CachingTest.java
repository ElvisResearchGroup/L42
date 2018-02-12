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
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import caching.Cache;
import caching.Loader;
import facade.L42;
import helpers.TestHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import repl.ReplMain.CodeInfo;

public class CachingTest {

  @Before
  public void before() throws IOException {
    try{this.deleteDirectory(Paths.get("L42IDE").resolve("L42.is%2FAdamTowel02"));}
    catch(NoSuchFileException nsfe) {}

    Path projFolder=Paths.get("TestFolder");
    if(!Files.exists(projFolder)){
      Files.createDirectories(projFolder);
    }
    this.deleteFilesInDirectory(projFolder);

    TestHelper.configureForTest();
  }
  //@AfterClass public void exit(){Platform.exit();}

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
    @SuppressWarnings("unused")
    CodeInfo test= new CodeInfo(content);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCodeInfo_invalid_onlyFirstLine() {
    String content="reuse Hello\nMain: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    CodeInfo test= new CodeInfo(content);
  }

  @Test
  public void testCopyResetKVCthenRun_createFolderInL42IDE() throws IOException {
    String content="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    CodeInfo test= new CodeInfo(content);

    L42.setRootPath(Paths.get("TestFolder"));

    ReplMain.copyResetKVCthenRun(Loader::new, content);

    this.deleteDirectory(Paths.get("L42IDE").resolve(test.cacheLibName));
  }

  @Test
  public void testCopyResetKVCthenRun_cachedIsFaster() throws IOException { //also tests resuing cached code
    long start=System.currentTimeMillis();

    String content="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Main: {\n" +
        "  Debug(S\"Hello world\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";

    CodeInfo test= new CodeInfo(content);

    L42.setRootPath(Paths.get("TestFolder"));

    ReplMain.copyResetKVCthenRun(Loader::new, content);
    long first=System.currentTimeMillis();
    this.deleteDirectory(Paths.get("L42IDE").resolve(test.cacheLibName));

    ReplMain.copyResetKVCthenRun(Loader::new, content);
    long second=System.currentTimeMillis();
    assertTrue(second-first < first-start);

    this.deleteDirectory(Paths.get("L42IDE").resolve(test.cacheLibName));
  }

  private static class LoaderFactory{
    Function<Path, Loader> factory;
    int cached=0;
    int compiled=0;
    LoaderFactory(String searched){
      factory=p -> new Loader(p) {
        public void eventCache(Cache cache) {
          System.out.println("Test cache::: "+cache.contains(searched));
          //if(cache.contains(searched)){cached+=1;}
        }
        public void eventUsingCache(Set<String> byteCodeNames) {
          System.out.println("Using cache for "+byteCodeNames);
          if(byteCodeNames.stream().anyMatch(k->k.contains(searched))) {cached+=1;}
        }
        public void eventJavaC(HashMap<String, ClassFile> newClMap) {
          System.out.println("Javac for  "+newClMap.keySet());
          if(newClMap.keySet().stream().anyMatch(k->k.contains(searched))) {compiled+=1;}
        }
      };
    }
  }

  @Test
  public void testCopyResetKVCthenRun_testTwoOutputs_resetCache() throws IOException {
    LoaderFactory loadFactory1=new LoaderFactory("Foo");
    LoaderFactory loadFactory2=new LoaderFactory("Foo");
    {
      String content="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Foo:{class method S hi()S\"hi\"}\n"+
        "Main: {\n" +
        "  Debug(S\"Test one \"++Foo.hi())\n" +
        "  return ExitCode.normal()\n" +
        "  }";

      L42.setRootPath(Paths.get("TestFolder"));
      ReplMain.copyResetKVCthenRun(loadFactory1.factory, content);
    }
    String output= L42.record.toString();
    assertEquals(0, loadFactory1.cached);
    assertEquals(1, loadFactory1.compiled);

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
      ReplMain.copyResetKVCthenRun(loadFactory2.factory, content2);
    }
    String output2= L42.record.toString();

    assertEquals("Test one hi\n", output);
    assertEquals("Test two hi\n", output2);
    assertEquals(0, loadFactory2.cached);
    assertEquals(1, loadFactory2.compiled);

    this.deleteDirectory(Paths.get("L42IDE").resolve("L42.is%2FAdamTowel02"));
  }

  @Test
  public void testCopyResetKVCthenRun_testTwoOutputs_reuseCache() throws IOException {
    LoaderFactory loadFactory1=new LoaderFactory("Foo");
    LoaderFactory loadFactory2=new LoaderFactory("Foo");
    {
      String content="reuse L42.is/AdamTowel02\n" +
          "CacheAdamTowel02:Load.cacheTowel()\n" +
          "Foo:{class method S hi()S\"hi\"}\n"+
          "Main: {\n" +
          "  Debug(S\"Test one \"++Foo.hi())\n" +
          "  return ExitCode.normal()\n" +
          "  }";

      L42.setRootPath(Paths.get("TestFolder"));
      ReplMain.copyResetKVCthenRun(loadFactory1.factory, content);
    }
    String output= L42.record.toString();

    TestHelper.configureForTest();
    assertEquals(0, loadFactory1.cached);
    assertEquals(1, loadFactory1.compiled);

    {
      String content2="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Foo:{class method S hi()S\"hi\"}\n"+
        "Main: {\n" +
        "  Debug(S\"Test two \"++Foo.hi())\n" +
        "  return ExitCode.normal()\n" +
        "  }";

      L42.setRootPath(Paths.get("TestFolder"));
      ReplMain.copyResetKVCthenRun(loadFactory2.factory, content2, "This.C42");
    }
    String output2= L42.record.toString();

    assertEquals("Test one hi\n", output);
    assertEquals("Test two hi\n", output2);
    assertEquals(1, loadFactory2.cached);
    assertEquals(0, loadFactory2.compiled);

    this.deleteDirectory(Paths.get("L42IDE").resolve("L42.is%2FAdamTowel02"));
  }

  @Test
  public void testActualIDE_newProject() throws IOException {
    ReplGui.main=new ReplMain() {
      public void eventStart() {
        String content="reuse L42.is/AdamTowel02\n" +
            "CacheAdamTowel02:Load.cacheTowel()\n" +
            "Main: {\n" +
            "  Debug(S\"Hello New Test\")\n" +
            "  return ExitCode.normal()\n" +
            "  }";
        Path p=Paths.get("TestFolder");
        ReplGui.main.newProject(p, content);
        ReplGui.main.runCode(Loader::new, true);
        ReplMain.gui.stop();
      }
    };
    startApplication();
    String output= L42.record.toString();
    assertEquals("Hello New Test\n", output);
  }

  @Test
  public void testActualIDE_reuseCache() throws IOException {
    LoaderFactory loadFactory1=new LoaderFactory("Foo");
    LoaderFactory loadFactory2=new LoaderFactory("Foo");

    ReplGui.main=new ReplMain() {
      public void eventStart() {
        String content1="reuse L42.is/AdamTowel02\n" +
            "CacheAdamTowel02:Load.cacheTowel()\n" +
            "Foo:{class method S hi()S\"hi\"}\n"+
            "Main: {\n" +
            "  Debug(S\"Test one \"++Foo.hi())\n" +
            "  return ExitCode.normal()\n" +
            "  }";
        Path p=Paths.get("TestFolder");
        ReplGui.main.newProject(p, content1);
        ReplGui.main.runCode(loadFactory1.factory, true);

        String content2="reuse L42.is/AdamTowel02\n" +
            "CacheAdamTowel02:Load.cacheTowel()\n" +
            "Foo:{class method S hi()S\"hi\"}\n"+
            "Main: {\n" +
            "  Debug(S\"Test two \"++Foo.hi())\n" +
            "  return ExitCode.normal()\n" +
            "  }";

        try {Files.write(p.resolve("This.L42"), content2.getBytes());}
        catch (IOException e) {throw new Error(e);}
        ReplGui.main.runCode(loadFactory2.factory, false);
        ReplMain.gui.stop();
      }
    };
    startApplication();
    assertEquals(0, loadFactory1.cached);
    assertEquals(1, loadFactory1.compiled);
    assertEquals(1, loadFactory2.cached);
    assertEquals(0, loadFactory2.compiled);
  }

  @Test
  public void testActualIDE_loadProject() throws IOException {
    Path path=Paths.get("TestFolder");
    Files.createFile(path.resolve("This.L42"));//create an empty This.L42 file in the selected folder
    String content="reuse L42.is/AdamTowel02\n" +
        "CacheAdamTowel02:Load.cacheTowel()\n" +
        "Main: {\n" +
        "  Debug(S\"Hello loaded\")\n" +
        "  return ExitCode.normal()\n" +
        "  }";
    Files.write(path.resolve("This.L42"), content.getBytes());
    L42.setRootPath(Paths.get("TestFolder"));
    ReplMain.copyResetKVCthenRun(Loader::new, content);

    ReplGui.main=new ReplMain() {
      public void eventStart() {
        L42.setRootPath(Paths.get("TestFolder"));
        ReplGui.main.runCode(Loader::new, false);
        ReplMain.gui.stop();
      }
    };
    startApplication();
    String output= L42.record.toString();
    assertEquals("Hello loaded\nHello loaded\n", output);
  }

  @Test
  public void testActualIDE_newProjectRepeatWithDifferent() throws IOException {
    LoaderFactory loadFactory1=new LoaderFactory("Foo");
    LoaderFactory loadFactory2=new LoaderFactory("Foo");

    ReplGui.main=new ReplMain() {
      public void eventStart() {
        String content1="reuse L42.is/AdamTowel02\n" +
            "CacheAdamTowel02:Load.cacheTowel()\n" +
            "Foo:{class method S hi()S\"hi\"}\n"+
            "Main: {\n" +
            "  Debug(S\"Test one \"++Foo.hi())\n" +
            "  return ExitCode.normal()\n" +
            "  }";
        Path p=Paths.get("TestFolder");
        ReplGui.main.newProject(p, content1);
        ReplGui.main.runCode(loadFactory1.factory, true);

        String content2="reuse L42.is/AdamTowel02\n" +
            "CacheAdamTowel02:Load.cacheTowel()\n" +
            "Foo:{class method S hi()S\"bye\"}\n"+
            "Main: {\n" +
            "  Debug(S\"Test two \"++Foo.hi())\n" +
            "  return ExitCode.normal()\n" +
            "  }";

        try {Files.write(p.resolve("This.L42"), content2.getBytes());}
        catch (IOException e) {throw new Error(e);}
        ReplGui.main.runCode(loadFactory2.factory, false);
        ReplMain.gui.stop();
      }
    };
    startApplication();
    assertEquals(0, loadFactory1.cached);
    assertEquals(1, loadFactory1.compiled);

    assertEquals(0, loadFactory2.cached);
    assertEquals(1, loadFactory2.compiled);

    String output= L42.record.toString();
    assertEquals("Test one hi\nTest two bye\n", output);
  }

  private void startApplication() {
    new JFXPanel();
    ReplGui.runAndWait(1,l->{
      try {
        ReplGui gui=new ReplGui() {
          @Override
          public void stop(){
            l.countDown();
            }
        };
        gui.start(new Stage());
      } catch (Exception e) {throw new Error(e);}
      return null;
    });
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
