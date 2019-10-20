package is.L42.translationToJava;

import static is.L42.tools.General.L;
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
import java.util.stream.Stream;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.P;
import is.L42.platformSpecific.javaTranslation.Resources;
import safeNativeCode.slave.host.ProcessSlave;

public class NativeDispatch {
  public static String nativeCode(Program p,String nativeKind,Core.L.MWT mwt){
    String nativeUrl=mwt.nativeUrl();
    if(!nativeUrl.startsWith("trusted:")){return untrusted(nativeKind,nativeUrl,mwt);}
    String nativeOp=nativeUrl.substring("trusted:".length());
    var k=TrustedKind.fromString(nativeKind);
    var op=TrustedOp.fromString(nativeOp);
    assert op.code.get(k)!=null:k;//type checking should avoid this
    return op.code.get(k).of(p, mwt);
    }
  public static String nativeFactory(Program p,String nativeKind, Core.L.MWT mwt) {
    var k=TrustedKind.fromString(nativeKind);
    return k.factory(p,mwt);
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
  public static String untrusted(String nativeKind, String nativeUrl, MWT mwt) {
    //anything in nativeUrl after first occurrence of the token "}\n" can be turned in a lambda
    List<String> xs=OpUtils.xs(mwt);
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