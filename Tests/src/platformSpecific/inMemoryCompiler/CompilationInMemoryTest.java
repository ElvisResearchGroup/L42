package platformSpecific.inMemoryCompiler;


import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import platformSpecific.inMemoryCompiler.RunningUtils;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CompilationInMemoryTest {

  @Test
  public void test1() throws Throwable {
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
    List<SourceFile> files = Arrays.asList(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(files);
    RunningUtils.runMain(classes,"math.Calculator");
    RunningUtils.runMainStrictSecurity(classes,"math.Calculator",1000);
  }
  @Test
  public void test2() throws Throwable {
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
    List<SourceFile> files = Arrays.asList(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(files);
    RunningUtils.runMain(classes,"math.Calculator");
    RunningUtils.runMainStrictSecurity(classes,"math.Calculator",1000);
  }
  @Test
  public void test2bis() throws Throwable {
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
    List<SourceFile> files = Arrays.asList(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(files);
    RunningUtils.runMain(classes,"math.Calculator");
    RunningUtils.runMainStrictSecurity(classes,"math.Calculator",1000);
  }


  public static class TestClass {
    @Test
    public void testMeth() { System.out.println(20+300);  }
    }
  @Test
  public void testExt() throws Throwable {
    SourceFile file = new SourceFile("math.Calculator",
        "package math;"
        + "public class Calculator { "
        + "  public static void main(String[] args) { "
        + TestClass.class.getCanonicalName() +" cal = new "+TestClass.class.getCanonicalName()+"(); "
        + "    cal.testMeth(); "
        + "  } "
        + "} "
        );
    List<SourceFile> files = Arrays.asList(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(files);
    RunningUtils.runMain(classes,"math.Calculator");
    RunningUtils.runMainStrictSecurity(classes,"math.Calculator",1000);
  }

  @Test
  public void test2Files() throws Throwable {
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
    List<SourceFile> files = Arrays.asList(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(files);
    RunningUtils.runMain(classes,"math.Calculator");
    RunningUtils.runMainStrictSecurity(classes,"math.Calculator",1000);
  }
  @Test
  public void testSecurity2File() throws Throwable {
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
    List<SourceFile> files = Arrays.asList(file);
    ClassLoader classes=InMemoryJavaCompiler.compile(files);
    RunningUtils.runMainStrictSecurity(classes,"math.Calculator",1000);
  }
}
