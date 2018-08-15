package platformSpecific.inMemoryCompiler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

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
  SourceFile fileA2 = new SourceFile("ab.A2",
          "package ab;"
          + "public class A2 { "
          + "  public int a() {return 10;} "
          + "  public int a_b() { "
          + "    return a()+new B2().b()+new A().a_b();"
          + "  }"
          + "} ");
  SourceFile fileB2 = new SourceFile("ab.B2",
          "package ab;"
          + "public class B2 { "
          + "  public int b() {return 20;} "
          + "  public int a_b() { "
          + "    return b()+new A2().a()+new B().a_b();"
          + "  }"
          + "} ");
  SourceFile fileM2 = new SourceFile("ab.Main2",
          "package ab;"
          + "public class Main2 { "
          + "  public static int execute() { "
          + "    return new A2().a_b()+new B2().a_b();"
          + "  }"
          + "} ");

  List<SourceFile> files = Arrays.asList(fileA,fileB);
  MapClassLoader classes1=InMemoryJavaCompiler.compile(ClassLoader.getSystemClassLoader(),files);
  HashMap<String, ClassFile> map = classes1.map();
  InMemoryJavaCompiler.compile(classes1,Arrays.asList(fileM));
  int res=(int) RunningUtils.runExecute(classes1,"ab.Main");
  Assert.assertEquals(6,res);
  Path path=Paths.get("src/"
          +this.getClass().getPackage().getName().replace(".","/")
          +"/data1.bytes");
  classes1.saveOnFile(path);
  MapClassLoader classes2=MapClassLoader.readFromFile(path,ClassLoader.getSystemClassLoader());
  InMemoryJavaCompiler.compile(classes2,Arrays.asList(fileA2,fileB2));
  InMemoryJavaCompiler.compile(classes2,Arrays.asList(fileM2));
  int res2=(int) RunningUtils.runExecute(classes2,"ab.Main2");
  Assert.assertEquals(66,res2);
  try{
    RunningUtils.runExecute(classes1,"ab.Main2");
    Assert.fail();
    }
  catch(Error t){
    Assert.assertTrue(t.getCause() instanceof ClassNotFoundException);
  }
  }
}
