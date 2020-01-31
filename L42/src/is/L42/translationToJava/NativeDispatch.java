package is.L42.translationToJava;

import static is.L42.nativeCode.TrustedKind.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;

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
import is.L42.nativeCode.TrustedKind;
import is.L42.nativeCode.TrustedOp;
import is.L42.generated.P;
import is.L42.platformSpecific.javaTranslation.Resources;
import safeNativeCode.slave.host.ProcessSlave;

public class NativeDispatch {
  public static List<String>xs(MWT mwt){
    return L(Stream.concat(Stream.of("£xthis"), mwt.mh().s().xs().stream().map(x->"£x"+x.inner())));
    }
  public static void nativeCode(String nativeKind,Core.L.MWT mwt,J j){
    String nativeUrl=mwt.nativeUrl();
    if(!nativeUrl.startsWith("trusted:")){untrusted(nativeKind,nativeUrl,mwt,j);return;}
    String nativeOp=nativeUrl.substring("trusted:".length());
    var k=TrustedKind._fromString(nativeKind);
    var op=TrustedOp.fromString(nativeOp);
    assert op._of(k)!=null:k;//type checking should avoid this
    op._of(k).of(false, mwt,j);
    }
  public static String nativeFactory(J j,String nativeKind, Core.L.MWT mwt) {
    var k=TrustedKind._fromString(nativeKind);
    return k.factory(j,mwt);
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
  public static void untrusted(String nativeKind, String nativeUrl, MWT mwt,J j) {
    //anything in nativeUrl after first occurrence of the token "}\n" can be turned in a lambda
    List<String> xs=xs(mwt);
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
    j.c(java.lang.String.format("""
    try {
      Resources.slaves.get("%s").addClassLoader(new Object(){}.getClass().getEnclosingClass().getClassLoader());
      return Resources.slaves.get("%s").call(%s).get();
    } catch (java.rmi.RemoteException ex) {
        throw new RuntimeException(ex);
    }
    """, slaveName, slaveName, toLambda));   
  }
}