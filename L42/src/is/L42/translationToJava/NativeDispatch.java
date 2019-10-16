package is.L42.translationToJava;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;
import static is.L42.translationToJava.TrustedKind.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import is.L42.generated.Core.E;
import is.L42.platformSpecific.javaTranslation.Resources;
import safeNativeCode.slave.host.ProcessSlave;

public class NativeDispatch {
  public static String nativeCode(String nativeKind, String nativeUrl, List<String> xs, E e) {
    if(!nativeUrl.startsWith("trusted:")){return untrusted(nativeKind,nativeUrl,xs,e);}
    String nativeOp=nativeUrl.substring("trusted:".length());
    var k=TrustedKind.fromString(nativeKind);
    var op=TrustedOp.fromString(nativeOp);
    assert op.code.get(k)!=null:k;//type checking should avoid this
    return op.code.get(k).apply(xs,e);
    }
  public static String nativeFactory(String nativeKind, List<String> xs) {
    var k=TrustedKind.fromString(nativeKind);
    return k.factory(xs);
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
  public static String untrusted(String nativeKind, String nativeUrl, List<String> xs, E e) {
    //anything in nativeUrl after first occurrence of the token "}\n" can be turned in a lambda
    String toLambda="()->"+nativeUrl.substring(nativeUrl.indexOf("}\n")+2); 
    for(int i:range(xs)){toLambda=toLambda.replaceAll("#"+i, xs.get(i));}
    String slaveName=nativeUrl.substring(0,nativeUrl.indexOf("{")).trim();
    Resources.slaves.computeIfAbsent(slaveName, sn->{
      String nativeData = nativeUrl.substring(nativeUrl.indexOf("{")+1, nativeUrl.indexOf("}")).trim()+"\n";
      int timeLimit = Integer.parseInt(readSection(nativeData, "timeLimit:", "0"));
      int memoryLimit = Integer.parseInt(readSection(nativeData, "memoryLimit:", "0"));
      String[] args = new String[]{"--enable-preview"};
      if (memoryLimit > 0) {
        args = new String[]{"-Xmx"+memoryLimit+"M","--enable-preview"};
      }
      return new ProcessSlave(timeLimit, args, ClassLoader.getPlatformClassLoader());
    });
    return java.lang.String.format("""
    try {
      Resources.slaves.get("%s").addClassLoader(new Object(){}.getClass().getEnclosingClass().getClassLoader());
      return Resources.slaves.get("%s").call(%s).get();
    } catch (java.rmi.RemoteException ex) {
        throw new RuntimeException(ex);
    }
    """, slaveName, slaveName, toLambda);   
  }
}