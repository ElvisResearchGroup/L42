package is.L42.top;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.todo;
import static is.L42.tools.General.typeFilter;
import static is.L42.tools.General.unique;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Program;
import is.L42.common.EndError.InvalidImplements;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.LL;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Full.L.M;
import is.L42.generated.Full.L.NC;
import is.L42.generated.LDom;
import is.L42.generated.P.NCs;
import is.L42.typeSystem.TypeManipulation;

class SortHeader{
  public static Core.L coreTopReuse(Program p,int uniqueId,Full.L l,List<Pos> poss)throws EndError{
    Core.L coreL=Constants.readURL.apply(l.reuseUrl());
    List<LDom>dups=L(l.ms().stream().map(m->m.key()).filter(k->
      coreL.mwts().stream().anyMatch(m->k.equals(m.key()))
      ||coreL.ncs().stream().anyMatch(m->k.equals(m.key()))
      ));
    if(!dups.isEmpty()){
      throw new EndError.InvalidHeader(poss,Err.reuseShadowsMember(l.reuseUrl(), dups));
      }
    List<Core.L.MWT> mwts=L(l.ms(),(c,mi)->{
      if(!(mi instanceof Full.L.MWT)){return;}
      var mwti=(Full.L.MWT)mi;
      var docsi=TypeManipulation.toCoreDocs(mwti.docs());
      var mhi=TypeManipulation.toCore(mwti.mh());
      c.add(new Core.L.MWT(mwti.poss(),docsi,mhi,"",null));
      });
    Program p0=p.update(coreL);
    List<P.NCs> typeDep=L(c->{
      Top.collectDeps(p0,mwts,c,null,false);
      });
    List<S> ss=L(mwts,(c,mi)->{
      if(refine(p0,mi.key(),P.pThis0,poss)){c.add(mi.key());}
      });
    boolean declaresClassMethods=mwts.stream().anyMatch(mi->mi.mh().mdf().isClass());
    var newInfo=Core.L.Info.empty
      .withTypeDep(typeDep)
      .withRefined(ss)
      .withDeclaresClassMethods(declaresClassMethods)
      .with_uniqueId(uniqueId);
    newInfo=Top.sumInfo(coreL.info(),newInfo);
    return coreL.withMwts(merge(coreL.mwts(),mwts)).withInfo(newInfo);
    }
  public static Core.L coreTop(Program p,int uniqueId) throws EndError{
    Full.L l=(Full.L)p.top;
    List<Pos> poss=l.poss();
    if(!l.reuseUrl().isEmpty()){return coreTopReuse(p,uniqueId,l,poss);}
    var notNC=L(l.ms().stream().filter(m->!(m instanceof Full.L.NC)));
    if(notNC.size()==l.ms().size()){notNC=l.ms();}
    Program p0=p.update(l.withMs(notNC));
    var cts=TypeManipulation.toCoreTs(l.ts());
    List<Core.T> ts1=L(c->{
      for(var ti:cts){if(!c.contains(ti)){c.add(ti);}}
      var all=collect(p0,cts,poss);
      for(var ti:all){if(!cts.contains(ti)){c.add(ti);}}
      });
    for(var ti:ts1){
      if(cts.contains(ti)){continue;}
      List<C> cs=ti.p().toNCs().cs();
      for(C ci:cs){
        if(!ci.hasUniqueNum()){continue;}
        throw new InvalidImplements(poss,Err.sealedInterface(ti,ts1));
        }
      }
    var infoP1=Core.L.Info.empty.withTypeDep(L(ts1.stream().map(t->t.p().toNCs())));
    Program p1 = p.update(new Core.L(poss,l.isInterface(), ts1, L(), L(), infoP1,L()));
    var mh1n=methods(p1,ts1,l.ms(),poss);//propagate err
    List<Core.L.MWT> mwts=L(mh1n,(c,mh)->{
      List<Core.Doc> docs=L();
      List<Pos> possi=poss;
      var mi=_elem(l.ms(),mh.key());
      if(mi!=null){
        docs=TypeManipulation.toCoreDocs(mi.docs());
        possi=mi.poss();
        }
      var res=new Core.L.MWT(possi, docs, mh,"",null);
      c.add(res);
      });
    ArrayList<P.NCs> typePs=new ArrayList<>();
    ArrayList<P.NCs> cohePs=new ArrayList<>();
    Top.collectDeps(p1,mwts,typePs,cohePs,false);
    var docs=TypeManipulation.toCoreDocs(l.docs());
    boolean classMeth=mh1n.stream().anyMatch(m->m.mdf().isClass());
    List<P.NCs> typeDeps=unique(L(c->{
      for(var ti:ts1){c.add(ti.p().toNCs());}
      c.addAll(typePs);
      Top.collectDepDocs(docs, c);
      }));
    List<S> refined=L(mwts,(c,mi)->{
      S s=mi.key();
      if(refine(p1,s,P.pThis0,poss)){c.add(s);}
      });
    Info info=new Info(false,typeDeps,unique(L(cohePs.stream())),
      L(),L(),L(),refined,classMeth,"",L(),uniqueId); 
  return new Core.L(poss,l.isInterface(), ts1, mwts, L(), info, docs);
  }
  private static List<T> collect(Program p,List<T> ts,List<Pos> poss)throws InvalidImplements{
    if(ts.isEmpty()){return ts;}
    T t0=ts.get(0);
    ts=popL(ts);
    var recRes=collect(p,ts,poss);
    var ll=p.of(t0.p(),poss);
    Core.L l=(Core.L)ll;
    if(!l.isInterface()){throw new InvalidImplements(poss,Err.notInterfaceImplemented());}
    return L(c->{
      if(!recRes.contains(t0)){c.add(t0);}
      for(var ti:((Core.L)ll).ts()){
        T tif=p.from(ti,t0.p().toNCs());
        if(!recRes.contains(tif)){c.add(tif);}        
        }
      c.addAll(recRes);
      });
    }
  private static List<Core.MH> methods(Program p,List<Core.T>ps,List<M> ms,List<Pos> poss){
    List<Core.MH> mhs=p.extractMHs(ms);
    List<List<MH>> methods=L(c->{
      c.add(mhs);
      for(var t: ps){
        P.NCs tp=t.p().toNCs();
        var li=(Core.L)p.of(tp,p.top.poss());
        c.add(L(li.mwts().stream().map(m->p.from(m.mh(),tp))));
        }
      });
    List<S> ss=L(methods.stream().flatMap(msi->msi.stream().map(m->m.s())).distinct());
    for(S s:ss){origin(p,s,p.top.poss());}// it throws InvalidImplements
    List<MH> res=L(ss,(c,s)->{
      for(var msi:methods){
        var ri=_elem(msi,s);
        if(ri!=null){c.add(ri);return;}
        }
      });
    return res;
    }
  private static P origin(Program p,S s,List<Pos> poss) throws InvalidImplements{
    List<P> origins=L(c->{
      if(!refine(p,s,P.coreThis0.p().toNCs(),poss)){c.add(P.coreThis0.p());}
      for(var t:((Core.L)p.top).ts()){
        var tp=t.p().toNCs();
        var l=(Core.L)p.of(tp,poss);
        if(l.mwts().stream().noneMatch(m->m.key().equals(s))){continue;}
        if(!refine(p,s,tp,poss)){c.add(tp);}
        }
      });
    if(origins.size()==1){return origins.get(0);}
    throw new InvalidImplements(poss,
      Err.moreThenOneMethodOrigin(s,origins));
    }
/*
* refine(p;s;P) iff exists P' in p(P).Ts.Ps[from P;p] such that s in dom(p(P').mwts)
  //assert P' never This0
*/
  private static boolean refine(Program p,S s, P.NCs path,List<Pos> poss){
    for(T t:((Core.L)p.of(path,poss)).ts()){
      var p1=p.from(t.p(), path);
      assert !p1.equals(P.coreThis0.p());
      var l=(Core.L)p.of(p1,poss);
      if(l.mwts().stream().anyMatch(m->m.mh().s().equals(s))){return true;}
      }
    return false;
    }
  }