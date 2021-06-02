package is.L42.translationToJava;

import static is.L42.nativeCode.TrustedKind.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.L42AuxParser.NativeKindContext;
import is.L42.meta.MetaError;
import is.L42.nativeCode.TrustedKind;
import is.L42.nativeCode.TrustedOp;
import is.L42.generated.P;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.platformSpecific.javaTranslation.Resources;
import safeNativeCode.slave.Functions;
import safeNativeCode.slave.Slave;
import safeNativeCode.slave.host.ProcessSlave;

public class NativeDispatch {
  public static List<String>xs(MWT mwt){
    return L(Stream.concat(Stream.of("£xthis"), mwt.mh().s().xs().stream().map(x->"£x"+x.inner())));
    }
  public static void nativeCode(String nativeKind,Core.L.MWT mwt,J j){
    assert j!=null;
    String nativeUrl=mwt.nativeUrl();
    if(!nativeUrl.startsWith("trusted:")){untrusted(nativeKind,nativeUrl,mwt,j);return;}
    String nativeOp=nativeUrl.substring("trusted:".length());
    var k=TrustedKind._fromString(nativeKind,j.p());
    assert k!=null :nativeKind;
    var op=TrustedOp._fromString(nativeOp);
    assert op!=null :nativeOp;
    assert op._of(k)!=null:k+" "+op;//type checking should avoid this
    //op._of(k).of(false, mwt,j);//before type checking guranteed there was no need of "true", now type checking just make a warning
    try{
      op._of(k).of(true, mwt,j);
      k.checkNativePars(j.p(),true);
      }
    catch(EndError.TypeError te){
      j.c("return ");
      j.visitE(mwt._e());
      j.c(";");
      return;
      }
    op._of(k).of(false, mwt,j);
    }
  public static String nativeFactory(J j,String nativeKind, Core.L.MWT mwt) {
    var k=TrustedKind._fromString(nativeKind,j.p());
    return k.factory(j,mwt);
    }
  public static class NativeUrlInfo{
    public String errorMsg="";
    public final String slaveName;
    public final int endLine;
    public final int timeLimit;
    public final int memoryLimit;
    public NativeUrlInfo(String s){
      assert s.length()==s.trim().length();
      assert !s.isEmpty();
      assert !s.startsWith("trusted:");
      int endName=s.indexOf("{");
      int endNamePar=s.indexOf("}");
      endLine=s.indexOf("\n");
      int preEndLine=s.indexOf("{\n");
      if(endName==-1 || endNamePar==-1){errorMsg+="Slave parameters missing\n";}
      else{assert endName<endNamePar:endName+" "+endNamePar+" "+s;}
      if(endNamePar>endLine){errorMsg+="Slave name and parameters need to sit on one line\n";}
      if(preEndLine+1!=endLine){errorMsg+="Slave name parameters must end with '}{\\n' and be followed by native code\n";}
      slaveName=s.substring(0,endName).trim();
      if(!p.matcher(slaveName).matches()){errorMsg+="Invalid Slave name: ["+slaveName+"] \n";}
      String nativeData = s.substring(endName+1,endNamePar).trim()+"\n";
      timeLimit = Integer.parseInt(readSection(nativeData, "timeLimit:", "0"));
      memoryLimit = Integer.parseInt(readSection(nativeData, "memoryLimit:", "0"));      
      }
    private static final Pattern p = Pattern.compile("^([a-zA-Z_$][a-zA-Z\\d_$]*)$");
    }
  private static String readSection(String nativeUrl, String part, String def) {
    if(!nativeUrl.contains(part)){return def;}
    nativeUrl = nativeUrl.substring(nativeUrl.indexOf(part)+part.length());
    int nl = nativeUrl.indexOf("\n");
    if(nl == -1){throw bug();}
    return nativeUrl.substring(0, nl).trim();
  }
  static class WhiteListed{
    String resT;
    boolean isOpt;
    List<String> invalid=new ArrayList<>();
    static List<String>list=List.of("String","Boolean","Integer","Double");
    String type(J j,Core.T t){
      String res=j.typeNameStr(t);
      res=J.boxed(res);
      if(!list.contains(res)){invalid.add(t.p().toString());}
      return res;
      }
    WhiteListed(J j,MWT mwt){
      this.resT=type(j,mwt.mh().t());
      var l=j.p()._ofCore(mwt.mh().t().p());
      this.isOpt=l.info().nativeKind().equals(TrustedKind.Opt.inner);
      //Note: the return type must be WhiteListed, but the parameter types can be free.
      //This is useful for a range of tasks, including pluggable type systems
      }    
    }
  public static void untrusted(String nativeKind, String nativeUrl, MWT mwt,J j) {
    nativeUrl=nativeUrl.trim();
    var info=new NativeUrlInfo(nativeUrl);
    //anything in nativeUrl after first occurrence of the token "}\n" can be turned in a lambda
    List<String> xs=xs(mwt);
    //String toLambda="()->{"+nativeUrl.substring(info.endLine);
    var wl=new WhiteListed(j, mwt);
    String toLambda="new safeNativeCode.slave.Functions.Supplier<"
      +wl.resT+">(){public "+wl.resT+" get()throws Exception {"
      +nativeUrl.substring(info.endLine)+"}";
    if(!wl.invalid.isEmpty()){
      toLambda="new safeNativeCode.slave.Functions.Supplier<"
          +wl.resT+">(){public "+wl.resT+" get()throws Exception {"
          +"throw new Error(\"The type "+wl.invalid+" can not be safelly returned from Java\");}}";      
      }
    String toLambdaOnNull="";
    if(!wl.isOpt){
      toLambdaOnNull="if(res==null){return slave.call(new safeNativeCode.slave.Functions.Supplier<"
        +wl.resT+">(){public "+wl.resT+" get()throws Exception {"
        +"throw new Error(\"Java code was returning null, but the expected result is not optional\");}}).get();}";
      }
    for(int i:range(xs)){toLambda=toLambda.replaceAll("#"+i, xs.get(i));}
    j.c(java.lang.String.format("""
    try{
      var slave=Resources.loadSlave(%s,%s,"%s",new Object(){});
      var res=slave.call(%s).get();
      %s
      return res;
      }
    catch(safeNativeCode.exceptions.SlaveException ex){%s}
    catch(java.util.concurrent.CancellationException|java.rmi.RemoteException ex){%s}
    """,info.memoryLimit,info.timeLimit,info.slaveName,toLambda,toLambdaOnNull,onErr(mwt, j),onJavaErr(mwt, j)));   
  }
  private static String onErr(MWT mwt, J j) {
    if(mwt.mh().exceptions().size()!=1){
      //TODO: var sw=new StringWriter();//Should we run this instead of getMessage?
      //e.printStackTrace(new PrintWriter(sw));
      //return e.getClass().getName()+"\n"+e.getMessage()
      //  +"\n"+sw;
      return """
        String msg;try{msg=ex.getChild().call(Throwable::getMessage).get();}
        catch (java.rmi.RemoteException ex1){msg=ex1.getClass().getName()+"\\n"+ex1.getMessage();}
        throw new is.L42.common.EndError.LeakedSlaveError(List.of(), msg);
        """;
      }
    var t=mwt.mh().exceptions().get(0);
    Program pErr=j.p()._navigate(t.p().toNCs());
    if(!pErr.topCore().info().nativeKind().equals(TrustedKind.LazyMessage.name())){return "throw ex;";}
    return java.lang.String.format("""
      String msg;try{msg=ex.getChild().call(Throwable::getMessage).get();}
      catch (java.rmi.RemoteException ex1){msg=ex1.getClass().getName()+"\\n"+ex1.getMessage();}
      throw new L42Exception(%s.wrap(new L42£LazyMsg(msg)));
      """,J.classNameStr(pErr));
    }
  private static String onJavaErr(MWT mwt, J j) {
    if(mwt.mh().exceptions().size()!=1){return "throw new Error(ex);";}
    var t=mwt.mh().exceptions().get(0);
    Program pErr=j.p()._navigate(t.p().toNCs());
    var notLazy = !pErr.topCore().info().nativeKind().equals(TrustedKind.LazyMessage.name());
    if(notLazy){return "throw new Error(ex);";}
    return java.lang.String.format("""
      String msg=ex.getClass().getName()+"\\n"+ex.getMessage();
      throw new L42Exception(%s.wrap(new L42£LazyMsg(msg)));
      """,J.classNameStr(pErr));
    }
  }