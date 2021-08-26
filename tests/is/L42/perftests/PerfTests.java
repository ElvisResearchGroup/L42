package is.L42.perftests;

import static is.L42.tools.General.L;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import is.L42.common.Parse;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tests.DeployAdamsTowel;
import is.L42.top.CachedTop;
import is.L42.top.Init;

public class PerfTests {
  
  public static final PrintStream out, err;
  
  private static final boolean saveResults = true;
  
  private static final File programOutput;
  
  static {
    PerfCounters.setEnabled(false);
    try {
      programOutput = File.createTempFile("l42testoutput", ".log");
      PrintStream stream = new PrintStream(new FileOutputStream(programOutput));
      System.setOut(stream);
      System.setErr(stream);
      out = new PrintStream(new FileOutputStream(FileDescriptor.out));
      err = new PrintStream(new FileOutputStream(FileDescriptor.err));
      } 
    catch (Exception e) {
      throw new Error(e);
      }
    }
  
  @AfterAll
  public static void showLog() {
    System.setOut(out);
    System.setErr(err);
    System.out.println("L42 Program output logged to: " + programOutput.getAbsolutePath());
    }
  
  @Test 
  public void testAdamsTowel() {
    testRun("L42Source/AdamsTowel", "adamstowel");
    }

  @Test 
  public void testUnit() {
    testRun("L42Source/TestUnit", "unit");
    }

  @Test 
  public void testLoad() {
    testRun("L42Source/TestLoad", "load");
    }

  @Test 
  public void testHelloWorld() {
    testRun("L42Source/HelloWorld2", "helloworld");
    }
  
  @Test 
  public void testCacheSaving() {
    try {
      PerfStat prevres = PerfStat.getResult("cachesaveload");
      PerfStat prevres2 = PerfStat.getResult("unit.memcache");
      PerfStat prevres3 = PerfStat.getResult("unit.filecache");
      File tmp = new File("tmp/tmp" + Math.random()); tmp.mkdirs();
      var url=Paths.get(DeployAdamsTowel.class.getResource("L42Source/TestUnit").toURI()).resolve("This.L42");
      var code=Parse.codeFromPath(url);
      CachedTop memcache = new CachedTop(L(),L());
      Resources.clearRes();
      Init.topCache(memcache, url, code);
      Resources.clearResKeepReuse();
      final CachedTop finalvar = memcache;
      PerfMonitor.PerfResult res = PerfMonitor.test(() -> {
        finalvar.toNextCache().saveCache(tmp.toPath());
        return CachedTop.loadCache(tmp.toPath());
        }, 5, true);
      CachedTop filecache = res.cache();
      PerfStat res2 = PerfMonitor.test(() -> {
        doExecCache(finalvar.toNextCache(), url, code);
        return null;
        }, 5, true).stat();
      PerfStat res3 = PerfMonitor.test(() -> {
        doExecCache(filecache, url, code);
        return null;
        }, 1, true).stat();
      System.out.println("Cache saving and loading: " + res.stat().toString());
      System.out.println("Unit with in-memory cache: " + res2.toString());
      System.out.println("Unit with file cache: " + res3.toString());
      if(saveResults) {
        PerfStat.saveResult("cachesaveload", res.stat());
        PerfStat.saveResult("unit.memcache", res2);
        PerfStat.saveResult("unit.filecache", res3);
        return;
        }  
      if(prevres != null) {
        assertTrue(prevres.compareTo(res.stat()) <= 0);
        }
      if(prevres2 != null) {
        assertTrue(prevres2.compareTo(res2) <= 0);
        }
      if(prevres3 != null) {
        assertTrue(prevres3.compareTo(res3) <= 0);
        }
      } 
    catch(Exception e) {
      throw new Error(e);
      }
    }
  
  public void testRun(String path, String name) {
    try {
      PerfStat prevres = PerfStat.getResult(name + ".nocache");
      PerfStat prevres2 = PerfStat.getResult(name + ".cache");
      out.println("Testing " + name);
      out.println("Previous results:");
      out.println(prevres);
      out.println(prevres2);
      out.println("New results:");
      var url=Paths.get(DeployAdamsTowel.class.getResource(path).toURI()).resolve("This.L42");
      var code=Parse.codeFromPath(url);
      PerfMonitor.PerfResult res = PerfMonitor.test(() -> {
        return doExecNoCache(url, code);
        }, 5, true);
      PerfStat res2 = PerfMonitor.test(() -> {
        doExecCache(res.cache().toNextCache(), url, code);
        return null;
        }, 5, true).stat();
      out.println(name + ", no cache: " + res.stat().toString());
      out.println(name + ", cache: " + res2.toString());
      if(saveResults) {
        PerfStat.saveResult(name + ".nocache", res.stat());
        PerfStat.saveResult(name + ".cache", res2);
        return;
        } 
      if(prevres != null) {
        assertTrue(prevres.compareTo(res.stat()) <= 0);
        }
      if(prevres2 != null) {
        assertTrue(prevres.compareTo(res2) <= 0);
        } 
      }
    catch(Exception e) {
      throw new Error(e);
      }
    }
  private static final CachedTop doExecNoCache(Path url, String code) {
    System.out.println("--------------");
    CachedTop top = new CachedTop(L(),L());
    Resources.clearRes();
    Init.topCache(top, url, code);
    Resources.clearResKeepReuse();
    System.out.println("--------------");
    return top;
    }
  private static final void doExecCache(CachedTop cache, Path url, String code) {
    Resources.clearResKeepReuse();
    Init.topCache(cache, url, code);
    Resources.clearResKeepReuse();
    }
  
}
