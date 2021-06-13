package is.L42.meta;

import java.util.function.Function;

import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.translationToJava.NativeDispatch;
import is.L42.visitors.CloneVisitor;

public class NativeSlaveNames extends CloneVisitor{
  MetaError err;
  String oldName;
  String newName;
  public L apply(L l, String oldName, String newName, Function<L42£LazyMsg, L42Any> wrap) {
    err=new MetaError(wrap);
    this.oldName=oldName=oldName.trim();
    this.newName=newName=newName.trim();
    var info1=new NativeDispatch.NativeUrlInfo(oldName+"{}{\n_");
    var info2=new NativeDispatch.NativeUrlInfo(newName+"{\n_");
    if(!info1.errorMsg.isEmpty()){err.throwErr(l,"Attempting replacing native slave id ["+oldName+"], but it is not a valid native slave id: "+info1.errorMsg);}
    if(!info2.errorMsg.isEmpty()){err.throwErr(l,"Attempting replacing native slave id ["+oldName+"] with ["+newName+"] but it is not a valid native slave id: "+info2.errorMsg);}
    return l.accept(this);
    }
  @Override public Core.L.MWT visitMWT(Core.L.MWT mwt){
    mwt=super.visitMWT(mwt);
    var nu=mwt.nativeUrl();
    if(nu.isEmpty()||nu.startsWith("trusted:")){return mwt;}
    var info=new NativeDispatch.NativeUrlInfo(nu.trim());
    if(!info.slaveName.equals(oldName)){return mwt;}
    nu=nu.substring(info.endLine);
    return mwt.withNativeUrl(newName+"{"+nu);
    }
  }
