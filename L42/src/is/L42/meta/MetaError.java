package is.L42.meta;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.HasPos;
import is.L42.generated.P;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42Error;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;

public class MetaError{
  Function<L42£LazyMsg,L42Any>wrap;
  public MetaError(Function<L42£LazyMsg,L42Any>wrap){this.wrap=wrap;}
  public String introName(Object fault){
    String elem="";
    if(fault instanceof P){
      var p=(P)fault;
      assert p.toNCs().cs().stream().noneMatch(c->c.hasUniqueNum());
      elem+=p;
      }
    if(fault instanceof List){
      @SuppressWarnings("unchecked") var cs=(List<C>)fault;
      assert cs.stream().noneMatch(c->c.hasUniqueNum());
      elem+=cs.stream().map(c->c.toString()).collect(Collectors.joining("."));
      }
    return "Invalid nested class "+elem+"=";
    }
  public String intro(Object fault,boolean top){
    String elem="";
    if(fault instanceof P){
      var p=(P)fault;
      assert p.toNCs().cs().stream().noneMatch(c->c.hasUniqueNum());
      elem+=p+"\n";
      }
    if(fault instanceof List){
      @SuppressWarnings("unchecked") var cs=(List<C>)fault;
      assert cs.stream().noneMatch(c->c.hasUniqueNum());
      elem+="nested class "+cs.stream().map(c->c.toString()).collect(Collectors.joining("."))+"\n";
      }
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
      elem="{"+(l.isInterface()?"interface ":" ");
      if(!l.ts().isEmpty()){
        elem+="[ ";
        for(var t:l.ts()){elem+=t.p()+" ";}
        elem+="] ";
        }
      for(var mwt:l.mwts()){
        if(mwt.key().hasUniqueNum()){continue;}
        elem+=mwt.key();
        if(mwt._e()!=null){elem+="=(..)";}
        elem+=" ";
        }
      for(var nc:l.ncs()){
        if(nc.key().hasUniqueNum()){continue;}
        elem+=nc.key()+"={..} ";
        }
      elem=elem+"}\n";
      }     
    if(!top){return elem;}  
    if(elem.startsWith("{")){return "Invalid nested class "+elem;}
    return "Invalid method "+elem; 
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
    var lazy=new L42£LazyMsg(intro(fault,true)+msg+"\n"+pos(fault));
    throw new L42Error(wrap.apply(lazy));
    }
  public RuntimeException throwErr(Object fault,Supplier<String> msg){
    Supplier<String> msg2=()->intro(fault,true)+msg.get()+"\n"+pos(fault);
    var lazy=new L42£LazyMsg(msg2);
    throw new L42Error(wrap.apply(lazy));
    }
  public RuntimeException throwErr(Object fault0,Object fault,String msg){
    Supplier<String> msg2=()->introName(fault0)+intro(fault,false)+msg+"\n"+pos(fault);
    var lazy=new L42£LazyMsg(msg2);
    throw new L42Error(wrap.apply(lazy));
    }
  public RuntimeException throwErr(Object fault0,Object fault,Supplier<String> msg){
    Supplier<String> msg2=()->introName(fault0)+intro(fault,false)+msg.get()+"\n"+pos(fault);
    var lazy=new L42£LazyMsg(msg2);
    throw new L42Error(wrap.apply(lazy));
    }
  public RuntimeException throwErr(Supplier<Object>fault,Supplier<String> msg){
    Supplier<String> msg2=()->intro(fault.get(),true)+msg.get()+"\n"+pos(fault);
    var lazy=new L42£LazyMsg(msg2);
    throw new L42Error(wrap.apply(lazy));
    }
  }