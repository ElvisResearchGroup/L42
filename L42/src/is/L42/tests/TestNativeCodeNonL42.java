package is.L42.tests;

import static is.L42.tools.General.bug;

import java.lang.reflect.InvocationTargetException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.General;
import safeNativeCode.slave.host.ProcessSlave;

public class TestNativeCodeNonL42 {
  
  @Test
  public void doThing() throws InvocationTargetException, CompilationError
  {
    String nativeKind = "";
    String nativeURL = """
    ioSlave{}
    {try(
    java.util.stream.Stream<String>lines=java.nio.file.Files.lines(
      java.nio.file.Paths.get(#1+".txt")))
      {return lines.collect(java.util.stream.Collectors.joining("\\n"));}
    catch (java.io.IOException ioe) {return "";}
    }
    """;
    String toLambda = """
    ()->    {try(
    java.util.stream.Stream<String>lines=java.nio.file.Files.lines(
      java.nio.file.Paths.get(£xfileName+".txt")))
      {return lines.collect(java.util.stream.Collectors.joining("\\n"));}
    catch (java.io.IOException ioe) {return "";}
    }""";
    String s = untrusted(nativeKind, nativeURL, toLambda);
    subDoThing(s);
  }
  
  public static void subDoThing(String code) throws CompilationError, InvocationTargetException
  {
    String bigcode =  "package is.L42.metaGenerated;\n\n"
        + "public class SlaveDoThing2 {\n"
        + "  public String myUntrustedMethod(String £xfileName) {\n "
   //     + "     System.out.println(\"Dothething2\");\n"
        + code + "\n"
//        + "     try {\n"
//        + "       safeNativeCode.slave.Slave sl = is.L42.platformSpecific.javaTranslation.Resources.slaves.get(\"ioSlave\");\n"
//        + "       //sl.addClassLoader(java.lang.ClassLoader.getPlatformClassLoader());\n"
//        + "       //sl.addClassLoader(java.lang.ClassLoader.getSystemClassLoader());\n"
//        + "       sl.addClassLoader(SlaveDoThing2.class.getClassLoader());\n"
//        + "       System.out.println(sl);\n"
//        + "       return sl.call(() -> { return \"\"; }).get();\n"
//        + "     } catch (java.rmi.RemoteException ex) {\n" 
//        + "       throw new RuntimeException(ex);\n" 
//        + "     }\n" 
//        + "     //return null;\n"
        + "  }\n" 
        + "}";
    SourceFile file = new SourceFile("is.L42.metaGenerated.SlaveDoThing2", bigcode);
    SourceFile file2 = new SourceFile("is.L42.metaGenerated.SlaveDoThing",
        "package is.L42.metaGenerated;"
        + "public class SlaveDoThing { "
        + "  public static void main(String[] args) { "
        + "    new SlaveDoThing2().myUntrustedMethod(\"ab\");"
        + "  } "
        + "} ");
    List<SourceFile> files = List.of(file, file2);
    ClassLoader classes = InMemoryJavaCompiler.compile(Resources.class.getClassLoader(), files,new ArrayList<>());
      RunningUtils.runMain(classes, "is.L42.metaGenerated.SlaveDoThing");
  }
  
  public static String myMethod(String £xfileName)
  {
    try {
      Resources.slaves.get("ioSlave").addClassLoader(new Object(){}.getClass().getEnclosingClass().getClassLoader());
      return Resources.slaves.get("ioSlave").call(()->    {try(
    java.util.stream.Stream<String>lines=java.nio.file.Files.lines(
      java.nio.file.Paths.get(£xfileName+".txt")))
      {return lines.collect(java.util.stream.Collectors.joining("\n"));}
    catch (java.io.IOException ioe) {return "";}
    }).get();
    } catch (java.rmi.RemoteException ex) {
        throw new RuntimeException(ex);
    }
  }
  
  public static String untrusted(String nativeKind, String nativeUrl, String toLambda) {
    String slaveName=nativeUrl.substring(0,nativeUrl.indexOf("{")).trim();
    Resources.slaves.computeIfAbsent(slaveName, sn->{
      String nativeData = nativeUrl.substring(nativeUrl.indexOf("{")+1, nativeUrl.indexOf("}")).trim()+"\n";
      int timeLimit = Integer.parseInt(readSection(nativeData, "timeLimit:", "0"));
      int memoryLimit = Integer.parseInt(readSection(nativeData, "memoryLimit:", "0"));
      String[] args = new String[]{"--enable-preview"};
      if (memoryLimit > 0) {
        args = new String[]{"-Xmx"+memoryLimit+"M","--enable-preview"};
      }
      return new ProcessSlave(timeLimit, args, Resources.class.getClassLoader());
    });
    
    return java.lang.String.format("""
    try {
      is.L42.platformSpecific.javaTranslation.Resources.slaves.get("%s").addClassLoader(new Object(){}.getClass().getEnclosingClass().getClassLoader());
      return is.L42.platformSpecific.javaTranslation.Resources.slaves.get("%s").call(%s).get();
    } catch (java.rmi.RemoteException ex) {
        throw new RuntimeException(ex);
    }
    """, slaveName, slaveName, toLambda);   
  }
  
  private static String readSection(String nativeUrl, String part, String def) {
    if(!nativeUrl.contains(part)){return def;}
    nativeUrl = nativeUrl.substring(nativeUrl.indexOf(part)+part.length());
    int nl = nativeUrl.indexOf("\n");
    if(nl == -1){
      throw bug();
      }
    return nativeUrl.substring(0, nl).trim();
  }

}
