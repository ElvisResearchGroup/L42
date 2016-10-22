package platformSpecific.inMemoryCompiler;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import coreVisitors.ExtractThrow;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;

public class TestCacheOnDisk {


@Test
public void test1() throws Throwable {
  SourceFile fileA = new SourceFile("ab.A",
      "package ab;"
      + "public class A { "
      + "  public int a() {return 1;} "
      + "  public int a_b() { "
      + "    return a()+new B().b();"
      + "  }"
      + "} ");
  SourceFile fileB = new SourceFile("ab.B",
          "package ab;"
          + "public class B { "
          + "  public int b() {return 2;} "
          + "  public int a_b() { "
          + "    return b()+new A().a();"
          + "  }"
          + "} ");
  SourceFile fileM = new SourceFile("ab.Main",
          "package ab;"
          + "public class Main { "
          + "  public static int execute() { "
          + "    return new A().a_b()+new B().a_b();"
          + "  }"
          + "} ");

  List<SourceFile> files = Arrays.asList(fileA,fileB);
  MapClassLoader classes1=InMemoryJavaCompiler.compile(ClassLoader.getSystemClassLoader(),files);
  HashMap<String, ClassFile> map = classes1.map();
  InMemoryJavaCompiler.compile(classes1,Arrays.asList(fileM));
  int res=(int) RunningUtils.runExecute(classes1,"ab.Main");
  Assert.assertEquals(6,res);
  classes1.saveOnFile(Paths.get("src."+this.getClass().getPackage().getName()+".findMe.tt"));

}

}
