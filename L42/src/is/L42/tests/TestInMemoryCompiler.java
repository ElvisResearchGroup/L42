package is.L42.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;

public class TestInMemoryCompiler {

  @Test public void test1() throws Throwable {
    SourceFile file = new SourceFile("math.Calculator",
        "package math;"
        + "public class Calculator { "
        + "  public void testAdd() { "
        + "    System.out.println(200+300); "
        + "  }"
        + "  public static void main(String[] args) { "
        + "    Calculator cal = new Calculator(); "
        + "    cal.testAdd(); "
        + "  } "
        + "} ");
    List<SourceFile> files = List.of(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(ClassLoader.getSystemClassLoader(),files);
    RunningUtils.runMain(classes,"math.Calculator");
    }
  @Test public void test2() throws Throwable {
    SourceFile file = new SourceFile("math.Calculator",
        "package math;"
        + "class Calculator2 { "
        + "  public void testAdd() { "
        + "    System.out.println(200+300); "
        + "  }} "
        + "public class Calculator { "
        + "  public static void main(String[] args) { "
        + "    Calculator2 cal = new Calculator2(); "
        + "    cal.testAdd(); "
        + "  } "
        + "} ");
    List<SourceFile> files = List.of(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(ClassLoader.getSystemClassLoader(),files);
    RunningUtils.runMain(classes,"math.Calculator");
  }
  @Test public void test2bis() throws Throwable {
    SourceFile file = new SourceFile("math.Calculator",
        "package math;"
        + "public class Calculator { "
        + "  public static void main(String[] args) { "
        + "    Calculator2 cal = new Calculator2(); "
        + "    cal.testAdd(); "
        + "  } "
        + "} "
        + "class Calculator2 { "
        + "  public void testAdd() { "
        + "    System.out.println(200+300); "
        + "  }} "
        );
    List<SourceFile> files = List.of(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(ClassLoader.getSystemClassLoader(),files);
    RunningUtils.runMain(classes,"math.Calculator");
    }
  public static class TestClass {
    @Test public void testMeth() { System.out.println(20+300);  }
    }
  @Test public void testExt() throws Throwable {
    SourceFile file = new SourceFile("math.Calculator",
        "package math;"
        + "public class Calculator { "
        + "  public static void main(String[] args) { "
        + TestClass.class.getCanonicalName() +" cal = new "+TestClass.class.getCanonicalName()+"(); "
        + "    cal.testMeth(); "
        + "  } "
        + "} "
        );
    List<SourceFile> files = List.of(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(ClassLoader.getSystemClassLoader(),files);
    RunningUtils.runMain(classes,"math.Calculator");
    }
  @Test public void test2Files() throws Throwable {
    SourceFile[] file = {
        new SourceFile("math.Calculator",
        "package math;"
        + "class Calculator2 { "
        + "  public void testAdd() { "
        + "    System.out.println(200+300); "
        + "  }} "
        ),
        new SourceFile("math.Calculator",
        "package math;"
        + "public class Calculator { "
        + "  public static void main(String[] args) { "
        + "    Calculator2 cal = new Calculator2(); "
        + "    cal.testAdd(); "
        + "  } "
        + "} "
        )};
    List<SourceFile> files = List.of(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(ClassLoader.getSystemClassLoader(),files);
    RunningUtils.runMain(classes,"math.Calculator");
    }
  }