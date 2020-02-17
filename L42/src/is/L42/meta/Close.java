package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.Core.MCall;
import is.L42.generated.Core.T;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.nativeCode.EagerCacheGenerator;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42Error;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.tools.General;
import is.L42.translationToJava.J;
import is.L42.typeSystem.Coherence;

public class Close {
  J j;
  HashMap<X,MH> capsMuts=new HashMap<>();
  ArrayList<MWT> c=new ArrayList<>();
  Function<L42£LazyMsg,L42Any>wrap;
  public L close(Program p,Function<L42£LazyMsg,L42Any>wrap){
    System.out.println(p.pTails.printCs());
    L l=p.topCore();
    j=new J(p,null,false,null,true);
    this.wrap=wrap;
    checkCoherence();
    if(l.info().close()){throwErr("Class is already close");}
    for(var m:l.mwts()){process(m);}
    List<MWT> newMWT=General.L(c.stream());
    l= l.withMwts(newMWT).withInfo(l.info().withClose(true));
    try{//TODO: need more? is this needed?
      new EagerCacheGenerator()
        .clearCacheGood(new J(p.update(l,false),null,false,null,true));
      }
    catch(EndError ee){
      ee.printStackTrace();
      throwErr(ee.getMessage());
      }
    System.out.println(l);
    return l;
    }
  public RuntimeException throwErr(String msg){
    var lazy=new L42£LazyMsg(msg);
    throw new L42Error(wrap.apply(lazy));
    }
  public RuntimeException throwErr(Supplier<String> msg){
    var lazy=new L42£LazyMsg(msg);
    throw new L42Error(wrap.apply(lazy));
    }
  public void process(MWT m){
    if(m._e()==null){
      if(m.mh().mdf().isClass()){processK(m);return;}
      X x=Coherence.fieldName(m.mh());
      if(!j.fields.xs.contains(x)){processAllowedAbs(m);return;}
      if(m.key().xs().isEmpty()){processGetter(m);return;}
      if(m.key().xs().size()==1){processSetter(m);return;}
      throw bug();
      }
    if(match("lazyCache",m)){processCache(m);return;}
    var stage=match("invalidateCache",m);
    var eager=match("readEagerCache",m);
    if(!stage &&!eager){processBase(m);return;}
    if(!m.mh().mdf().isClass()){
      throwErr("@Eager annotation must go on class methods, but is placed on "+m.mh()+"\n"+m.poss());
      }
    if(m.mh().pars().stream().anyMatch(t->t.mdf().isIn(Mdf.MutableFwd,Mdf.ImmutableFwd))){throw todo();}
    if(stage){
      if(m.key().xs().isEmpty()){throw todo();}
      Mdf first=m.mh().pars().get(0).mdf();
      if(!first.isIn(Mdf.Mutable,Mdf.Lent)){throw todo();}
      if(!m.mh().pars().stream().skip(1).allMatch(t->t.mdf().isIn(Mdf.Immutable,Mdf.Readable,Mdf.Class))){throw todo();}
      processStage(m);return;
      }
    if(eager){
      if(!m.mh().pars().stream().allMatch(t->t.mdf().isIn(Mdf.Immutable,Mdf.Readable,Mdf.Class))){throw todo();}
      processEager(m);return;      
      }
    assert false;
    }
  public static Core.PCastT This0=new Core.PCastT(null,P.pThis0,P.coreThis0.withMdf(Mdf.Class));
  public static Core.EX this0=new Core.EX(null,X.thisX);
  public Core.E fCapsExposer(Pos pos,X x,T t){
    MH mh=this.capsMuts.get(x);
    System.out.println(x+" "+t+" "+j.fields.xs);
    if(mh==null){
      if(!j.fields.xs.contains(x)){
        throw todo();//mispelled capsule field?
        }
      int[]countHs={0};
      var mh1=General.L(j.ch.mhs.stream().filter(m->{
        X xi=Coherence.fieldName(m);
        if(!xi.equals(x)){return false;}
        assert !m.key().hasUniqueNum();
        countHs[0]=Math.max(countHs[0],m.key().m().length()-xi.inner().length());
        return m.mdf().isIn(Mdf.Mutable,Mdf.Lent);
        }));
      if(!mh1.isEmpty()){assert false: mh;throw todo();}//somehow there is an exposer for capsule
      String hs="#".repeat(countHs[0]+1);
      S s=new S(hs+x.inner(),General.L(),0);
      mh=new MH(Mdf.Mutable,General.L(),t,s,General.L(),General.L());
      this.capsMuts.put(x,mh);
      this.c.add(new MWT(General.L(pos),General.L(),mh,"",null));
      }
    if(!mh.t().equals(t)){
      throw todo();//different capsule exposers requires different types
      }
    var rec=this0.withPos(pos);
    return new Core.MCall(pos,rec,mh.key(),General.L());
    }
  String errMsgMoreThenOne(X x,Mdf recMdf1,Mdf recMdf2){
    return "More then one candidate getter/exposer with"
      +recMdf1+" "+recMdf2+" in:"+
      General.L(j.ch.mhs.stream().filter(m->
        m.mdf().isIn(recMdf1,recMdf2) 
        && Coherence.fieldName(m).equals(x)));
    }  
  public Core.E fAcc(Pos pos,X x,Mdf recMdf1,Mdf recMdf2){
    var mh=j.ch.mhs.stream().filter(m->
      m.mdf().isIn(recMdf1,recMdf2) 
      && Coherence.fieldName(m).equals(x)
      ).reduce(toOneOr(()->throwErr(()->errMsgMoreThenOne(x,recMdf1,recMdf2))));
    assert mh.isPresent();
    var rec=this0.withPos(pos);
    return new Core.MCall(pos,rec,mh.get().key().withUniqueNum(0),General.L());
    }
  public void processState(MWT m){
    var s=m.key();
    var s2=s.withUniqueNum(0);
    assert !s.hasUniqueNum();
    var m2=m.withMh(m.mh().withS(s2));
    var rec=this0.withPos(m.poss().get(0));
    List<Core.E> exs=General.L(s.xs(),(ci,xi)->ci.add(new Core.EX(rec.pos(),xi)));
    m=m.with_e(new Core.MCall(rec.pos(),rec,s2,exs));
    c.add(m);
    c.add(m2);
    }
  public void processK(MWT m){processState(m);}
  public void processGetter(MWT m){processState(m);}
  public void processSetter(MWT m){processState(m);}
  public void processCache(MWT m){
    if(!m.nativeUrl().isEmpty()){throwErr("Method can not be annotated as Cache, since it is already native: "+m.nativeUrl());}
    c.add(m.withNativeUrl("trusted:lazyCache"));
    }
  public void processStage(MWT m){
    var pos=m._e().pos();
    S s=m.key();
    assert !s.xs().isEmpty();
    var xs1=popL(s.xs());
    var ts1=popL(m.mh().pars());
    S s1=s.withXs(xs1);
    List<Core.E> exs1=General.L(ci->{
      X x=s.xs().get(0);
      T t=m.mh().pars().get(0);
      ci.add(fCapsExposer(pos,x,t));
      for(var xi:xs1){ci.add(new Core.EX(pos,xi));}
      });
    var mh1=m.mh().withMdf(Mdf.Mutable).withS(s1).withPars(ts1);
    var m1=m.withMh(mh1);
    m1=m1.with_e(new Core.MCall(pos,This0.withPos(pos),s,exs1));
    c.add(m1);
    c.add(m);
    }
  public void processEager(MWT m){
    var s=m.key();
    var s1=s.withXs(General.L());
    assert !s.hasUniqueNum();
    var rec=This0.withPos(m.poss().get(0));
    var old=_elem(j.p().topCore().mwts(),s1);
    if(old!=null){
      assert false: s1+" "+s+" "+old;
      throw todo();/*make sum*/}
    var mh1=m.mh().withMdf(Mdf.Readable).withS(s1).withPars(General.L());
    var m1=m.withMh(mh1);//m1: the no arg meth calling the static method s
    List<Core.E> exs1=General.L(s.xs(),(ci,xi)->ci.add(fAcc(m._e().pos(),xi,Mdf.Immutable,Mdf.Readable)));
    m1=m1.with_e(new Core.MCall(rec.pos(),rec,s,exs1));
    c.add(m1.withNativeUrl("trusted:readEagerCache"));
    c.add(m);    
    }
  public void processAllowedAbs(MWT m){/*empty on purpose*/}
  public void processBase(MWT m){c.add(m);}
  public void checkCoherence(){
    boolean coh=j.ch.isCoherent(true);
    if(coh){return;}
    var lazy=new L42£LazyMsg(()->{
      try{j.ch.isCoherent(false);}
      catch(EndError er){return er.getMessage();}
      throw bug();
      });
    throw new L42Error(lazy);
    }
  public boolean match(String target,MWT m){
    for( var d:m.docs()){
      if(d._pathSel()==null){continue;}
      L ld=j.p()._ofCore(d._pathSel().p());
      if(ld==null){ throw todo();/*annotation not existent, possible typo: d._pathSel().p(), position from m*/}
      for(var di:ld.docs()){
        System.out.println(di.texts());
        if(di.texts().size()!=1){continue;}
        if(di.texts().get(0).equals(target)){return true;}
        }
      }
    return false;
    }
}
