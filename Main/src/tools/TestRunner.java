package tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;

import facade.L42;

@RunWith(ParallelizedParameterized.class)
//@RunWith(Parameterized.class)

public abstract class TestRunner {

  @Parameter(0) public Path p;
  @Parameter(1) public String shortName;

  private static int numThreads = 1;

  public static List<Object[]> goInner(Object ...opts){
    // Parameters are String or Opt.
    // The strings request a specific test.
    // The Opts either change global parameters or add a defined set of tests.
    Path root = findClassRoot();
    List<Object[]> result = new LinkedList<Object[]>();

    L42.trustPluginsAndFinalProgram=true;  // If desired, one of the options will set this false

    for(Object opt : opts) {
      if (opt instanceof String) {
        String s = (String)opt;
        addFilesFromRoot(root, "libTests/"+s, result);
      }else if (opt instanceof Opt) {
        Opt oopt = (Opt)opt;
        oopt.act(root, result);
      }else{
        assert false: "Parameters to goInner must be String or Opt";
      }
    }
    ParallelizedParameterized.setNumThreads(numThreads);
    return result;
    //catch (Throwable e) { throw handleThrowable(e);}
  }

  private static String findCaller() {
    StackTraceElement[] stes = new Error().getStackTrace();
    for(StackTraceElement ste:stes){
      if(!ste.getClassName().equals(TestRunner.class.getName())){return ste.getClassName();}
      }
      throw new Error("caller?");
  }

  private static Path findClassRoot() {
    try {
      Class <?> rootObj = Class.forName(findCaller());
      return Paths.get(rootObj.getResource(".").toURI());
    }catch (Throwable e) { throw handleThrowable(e);}
  }

  private static Path findTests(Path root){
    try {
      // The root path is probably the directory containing Test.java.
      // Remove trailing elements until we get to the directory Test.
      Path result = root;
      while (!result.endsWith("Tests")) {
//        System.out.println("findTest: removing a leaf from: "+result.toString());
        result = result.getParent();
        if (null == result)
          assert false : "Test driver must be in Test subtree and isn't.";
      }
      return result;
    }catch (Throwable e) { throw handleThrowable(e);}
  }

  public static void addFileFromPath(Path p, List<Object[]> files) {
    Path normP = p.normalize();
    files.add(new Object[]{normP, shortName(normP)});
  }
  static String shortName(java.nio.file.Path p){
  // Remove the uninteresting bits from a path, in order to make a short name for a test
  final String preamble = "/Tests/bin/";
  final String middle1 = "libTests";
//  final String middle2 = "libProject";
  String ret = p.toString();
  int preStart = ret.indexOf(preamble);
  ret = ret.substring(preStart + preamble.length());
  ret = ret.replace(middle1, "");
//  ret = ret.replace(middle2,  "");
  return ret;
}

  public static void addFilesFromRoot(Path root, String subPath, List<Object[]> files){
  try {
    assert root!=null;

    Path p = root.resolve(subPath);

    if (Files.isRegularFile(p, java.nio.file.LinkOption.NOFOLLOW_LINKS)) {
      addFileFromPath(p, files);
      return;
    }

    if (Files.isDirectory(p, java.nio.file.LinkOption.NOFOLLOW_LINKS)) {
      StreamSupport.stream (Files.newDirectoryStream(p).spliterator(), false)
      .forEach(test -> addFileFromPath(test, files));
      return;
    }

    throw new Error("File "+p.toString()+" unavalable ");
  }
  catch (Throwable t){throw handleThrowable(t);}
  }

public static enum Opt{
  NoTrust{public void act(Path root, List<Object[]> tests){L42.trustPluginsAndFinalProgram=false;}},
  NoAss{public void act(Path root, List<Object[]> tests){
    System.out.println("AssertionsDisabled");
    ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(false);
    }},
  Parallel{public void act(Path root, List<Object[]> tests){
    numThreads *= 2;
    }},
  ProfilerPrintOff{public void act(Path root, List<Object[]> tests){
    L42.profilerPrintOn=false;
    }},
  DeplAT1{public void act(Path root, List<Object[]> tests){
    addFileFromPath(findTests(root).resolve("bin/adamsTowel01/libProject/"), tests);}},
  DeplAT2{public void act(Path root, List<Object[]> tests){
    addFileFromPath(findTests(root).resolve("bin/adamsTowel02/libProject/"), tests);}},
  Project{public void act(Path root, List<Object[]> tests){
    addFileFromPath(root.resolve("libProject"), tests);}},
  AllTests{public void act(Path root, List<Object[]> tests){
    addFilesFromRoot(root, "libTests/", tests);}},
  NOP{public void act(Path root, List<Object[]> tests){}};
  public abstract void act(Path root, List<Object[]> tests);
  };

public abstract void pre();
public abstract void post();
@Test
public void test() throws Throwable{
  System.out.println("start: "+this.p);
  pre();//TestHelper.configureForTest();
  L42.main(new String[]{this.p.toString()});
  post();//TestHelper.check42Fails(L42.record.toString());
  }

  public static Error handleThrowable(Throwable t){
    if (t instanceof RuntimeException) throw (RuntimeException)t;
    if (t instanceof Error) throw (Error)t;
    throw new Error(t);
    }
}

