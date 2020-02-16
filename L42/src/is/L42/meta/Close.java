package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import is.L42.platformSpecific.javaTranslation.L42Error;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.tools.General;
import is.L42.translationToJava.J;
import is.L42.typeSystem.Coherence;

public class Close {
  J j;
  HashMap<X,MH> capsMuts=new HashMap<>();
  ArrayList<MWT> c=new ArrayList<>();
  public L close(Program p){
    System.out.println(p.pTails.printCs());
    L l=p.topCore();
    j=new J(p,null,false,null,true);
    checkCoherence();
    noZero(l);
    if(l.info().close()){throw todo();}
    for(var m:l.mwts()){process(m);}
    List<MWT> newMWT=General.L(c.stream());
    l= l.withMwts(newMWT).withInfo(l.info().withClose(true));
    try{//TODO: need more? is this needed?
      new EagerCacheGenerator()
        .clearCacheGood(new J(p.update(l,false),null,false,null,true));
      }
    catch(EndError ee){
      var lazy=new L42£LazyMsg(ee.getMessage());
      ee.printStackTrace();
      throw new L42Error(lazy);
      }
    System.out.println(l);
    return l;
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
    if(match("cache",m)){processCache(m);return;}
    if(!match("property",m)){processBase(m);return;}
    if(!m.mh().mdf().isClass()){throw todo();}
    if(m.mh().pars().stream().anyMatch(t->t.mdf().isIn(Mdf.MutableFwd,Mdf.ImmutableFwd))){throw todo();}
    if(m.mh().pars().stream().anyMatch(t->t.mdf().isIn(Mdf.Mutable,Mdf.Lent))){processProperyMut(m);return;}
    processPropertyImm(m);
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
  public Core.E fAcc(Pos pos,X x,Mdf recMdf1,Mdf recMdf2){
    var mh=j.ch.mhs.stream().filter(m->
      m.mdf().isIn(recMdf1,recMdf2) 
      && Coherence.fieldName(m).equals(x)).reduce(toOneOr(()->{throw todo();}));
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
    if(!m.nativeUrl().isEmpty()){throw todo();}
    c.add(m.withNativeUrl("trusted:cachable"));
    }
  public void processProperyMut(MWT m){
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
  public void processPropertyImm(MWT m){
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
    c.add(m1.withNativeUrl("trusted:eagerCachable"));
    c.add(m);    
    }
  public void processAllowedAbs(MWT m){/*empty on purpose*/}
  public void processBase(MWT m){c.add(m);}
  public void noZero(L l){
    for(var m:l.mwts()){
      if(!m.key().hasUniqueNum() || m.key().uniqueNum()!=0){continue;}
      throw todo();
      }
    }
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
