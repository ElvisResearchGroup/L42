package is.L42.meta;

import java.util.function.Function;
import java.util.function.Supplier;

import is.L42.generated.Core;
import is.L42.generated.HasPos;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42Error;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;

public class MetaError{
  Function<L42£LazyMsg,L42Any>wrap;
  public MetaError(Function<L42£LazyMsg,L42Any>wrap){this.wrap=wrap;}
  public String intro(Object fault){
    String elem="";
    if(fault instanceof Core.L.MWT){
      var mwt=(Core.L.MWT)fault;
      assert !mwt.key().hasUniqueNum();
      if(mwt._e()!=null && mwt.nativeUrl().isEmpty()){elem="=(..)";}
      if(mwt._e()!=null && !mwt.nativeUrl().isEmpty()){elem=" (..)";}
      mwt=mwt.with_e(null);
      elem=mwt.toString()+elem;
      elem+="\n";
      }
    if(fault instanceof Core.L){
      var l=(Core.L)fault;
      elem="{"+(l.isInterface()?"interface":"");
      for(var mwt:l.mwts()){
        if(mwt.key().hasUniqueNum()){continue;}
        elem+=mwt.with_e(null);
        if(mwt._e()!=null && mwt.nativeUrl().isEmpty()){elem+="=(..)";}
        if(mwt._e()!=null && !mwt.nativeUrl().isEmpty()){elem+=" (..)";}
        elem+="\n";
        }
      for(var nc:l.ncs()){
        if(nc.key().hasUniqueNum()){continue;}
        elem+=nc.key()+"={..}\n";
        }
      elem="class literal "+elem+"}\n";
      }      
    return "Invalid "+elem;
    }
  public String pos(Object fault){
    if(fault instanceof Core.L){
      var l=(Core.L)fault;
      return l.poss().toString();
      }
    if(fault instanceof Core.L.MWT){
      var mwt=(Core.L.MWT)fault;
      return mwt.poss().toString();
      }
    if(fault instanceof HasPos){
      var f=(HasPos)fault;
      return f.poss().toString();
      }
    return "[Position unknown]";
    }
  public RuntimeException throwErr(Object fault,String msg){
    var lazy=new L42£LazyMsg(intro(fault)+msg+"\n"+pos(fault));
    throw new L42Error(wrap.apply(lazy));
    }
  public RuntimeException throwErr(Object fault,Supplier<String> msg){
    Supplier<String> msg2=()->intro(fault)+msg.get()+"\n"+pos(fault);
    var lazy=new L42£LazyMsg(msg2);
    throw new L42Error(wrap.apply(lazy));
    }
  public RuntimeException throwErr(Supplier<Object>fault,Supplier<String> msg){
    Supplier<String> msg2=()->intro(fault.get())+msg.get()+"\n"+pos(fault);
    var lazy=new L42£LazyMsg(msg2);
    throw new L42Error(wrap.apply(lazy));
    }
  }