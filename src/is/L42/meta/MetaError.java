package is.L42.meta;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import is.L42.generated.Core.MWT;
import is.L42.generated.HasPos;
import is.L42.generated.S;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42Error;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.common.ErrMsg;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;

public class MetaError{
  Function<L42£LazyMsg,L42Any>wrap;
  public MetaError(Function<L42£LazyMsg,L42Any>wrap){this.wrap=wrap;}
  public String introName(Object fault){
    String elem="";
    if(fault instanceof P){
      var p=(P)fault;
      assert !p.hasUniqueNum();
      elem+=p;
      }
    if(fault instanceof List){
      @SuppressWarnings("unchecked") var cs=(List<C>)fault;
      assert cs.stream().noneMatch(c->c.hasUniqueNum());
      if(cs.isEmpty()){elem+="This";}
      else{elem+=cs.stream().map(c->c.toString()).collect(Collectors.joining("."));}
      }
    return "nested class "+elem+"=";
    }
    public String intro(List<C> cs,S s){
      String elem="";
      assert cs.stream().noneMatch(c->c.hasUniqueNum());
      if(cs.isEmpty()){elem+="method This."+s+"\n";}
      else{elem+="method "+cs.stream().map(c->c.toString()).collect(Collectors.joining("."))+"."+s+"\n";}
      return elem;
      }
    public String intro(Object fault,boolean top){
    String elem="";
    if(fault instanceof P){
      var p=(P)fault;
      assert !p.hasUniqueNum():
        "";
      elem+=p+"\n";
      }
    if(fault instanceof List){
      @SuppressWarnings("unchecked") var cs=(List<C>)fault;
      //assert cs.stream().noneMatch(c->c.hasUniqueNum());//reported for exposed numbers
      if(cs.isEmpty()){elem+="nested class This\n";}
      else{elem+="nested class "+cs.stream().map(c->c.toString()).collect(Collectors.joining("."))+"\n";}
      }
    if(fault instanceof MWT mwt){
      assert !mwt.key().hasUniqueNum();
      //System.err.print("Log: private name part of error:"+mwt.key());
      if(mwt._e()!=null && mwt.nativeUrl().isEmpty()){elem="=(..)";}
      if(mwt._e()!=null && !mwt.nativeUrl().isEmpty()){elem=" (..)";}
      elem=mwt.with_e(null).toString()+elem;
      elem+="\n";
      }
    if(fault instanceof CoreL){
      var l=(CoreL)fault;
      elem="{"+(l.isInterface()?"interface ":" ");
      var publicImpls=L(l.ts().stream().filter(
        t->!t.p().isNCs() ||!t.p().hasUniqueNum()));
      if(!publicImpls.isEmpty()){
        elem+="[ ";
        for(var t:publicImpls){elem+=t.p()+" ";}
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
    if(elem.startsWith("{")){return "nested class "+elem;}
    return elem; 
    }
  public String pos(Object fault){
    if(fault instanceof CoreL){
      var l=(CoreL)fault;
      return ErrMsg.posString(l.poss());
      }
    if(fault instanceof MWT mwt){ return ErrMsg.posString(mwt.poss()); }
    if(fault instanceof HasPos f){ return ErrMsg.posString(f.poss()); }
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