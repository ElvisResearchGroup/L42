package platformSpecific.inMemoryCompiler;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;

public class TestTwoClass {
@Test
  public void test1() throws Throwable {
    SourceFile file1 = new SourceFile("twoC.A",
        "package twoC;"
        + "public class A { "
        + "  public void testA() { "
        + "    System.out.println(200+300); "
        + "  }"
        + "  public static void main(String[] args) { "
        + "    B b=new B(); A a=new A();"
        + "    b.testB();  a.testA();"
        + "  } "
        + "} ");
       SourceFile file2 = new SourceFile("twoC.B",
        "package twoC;"
        + "public class B { "
        + "  public void testB() { "
        + "    System.out.println(20+30); "
        + "  }"
        + "  public static void main(String[] args) { "
        + "    B b=new B(); A a=new A();"
        + "    b.testB();  a.testA();"
        + "  } "
        + "} ");
       SourceFile file3 = new SourceFile("twoC.C",
        "package twoC;"
        + "public class C { "
        + "  public void testC() { "
        + "    System.out.println(2+3); "
        + "  }"
        + "  public static void main(String[] args) { "
        + "    B b=new B(); A a=new A(); C c=new C();"
        + "    b.testB();  a.testA(); c.testC();"
        + "  } "
        + "} ");
    List<SourceFile> files = Arrays.asList(file1,file2);
    List<SourceFile> files3 = Arrays.asList(file3);
    ClassLoader classes=InMemoryJavaCompiler.compile(ClassLoader.getSystemClassLoader(),files);
    RunningUtils.runMain(classes,"twoC.A");
    RunningUtils.runMain(classes,"twoC.B");
    RunningUtils.runMainStrictSecurity(classes,"twoC.A",1000);
    RunningUtils.runMainStrictSecurity(classes,"twoC.B",1000);
    ClassLoader classes3=InMemoryJavaCompiler.compile(classes,files3);
    RunningUtils.runMain(classes3,"twoC.C");
    RunningUtils.runMainStrictSecurity(classes3,"twoC.C",1000);
  }
}
