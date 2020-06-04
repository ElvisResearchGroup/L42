package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.pushL;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.Program;

public class CachedTop{
  final List<G> cached;
  final List<R>cachedR;
  public CachedTop(List<G> cached,List<R>cachedR){
    this.cached=cached;
    this.cachedR=cachedR;
    }
  public CachedTop toNextCache(){return new CachedTop(performed,performedR);}
  final ArrayList<G> performed=new ArrayList<>();
  final ArrayList<R> performedR=new ArrayList<>();
  boolean ok=true;
  G _getCached(){
    assert consistent();
    if(!ok){return null;}
    if(performed.size()>=cached.size()){return null;}
    return cached.get(performed.size());
    }
  R _getCachedR(){
    if(!ok){return null;}
    if(performed.size()>=cached.size()){return null;}
    return cachedR.get(performed.size());
    }
  void addPerformed(G g,R r){
    assert consistent();
    performed.add(g);
    performedR.add(r);
    assert consistent();
    }  
  boolean consistent(){
    for(var e:cached){
      assert cached.stream().noneMatch(e0->e0!=e && e0.state==e.state);
      assert performed.stream().noneMatch(e0->e0.state==e.state);
      }
    for(var e:performed){
      assert cached.stream().noneMatch(e0->e0.state==e.state);
      assert performed.stream().noneMatch(e0->e0!=e && e0.state==e.state);
      }
    return true;
    }  
  Program top(G g){
    R r=openClose(g);
    if(r.isErr()){throw r._err;}
    return (Program)r._obj;
    }
  R openClose(G g0){
    G cg0=_getCached();
    R cr0=_getCachedR();
    R r0=g0.open(cg0,cr0);
    addPerformed(g0,r0);
    if(r0.isErr()){return r0;}
    R r1=openCloseNested(r0);
    if(r1.isErr()){return r1;}
    G cg1=_getCached();
    R cr1=_getCachedR();
    R res=r1._g.close(cg1,cr1);
    addPerformed(r1._g,res);
    return res;
    }
  R openCloseNested(R r0){
    assert !r0.isErr();
    while(true){
      if(!r0._g.needOpen()){return r0;}
      R r1=openClose(r0._g);
      if(r1.isErr()){return r1;}
      r0=r1;
      }
    }
  }

//public class CachedTop{
//G _lastOrErr(G g0,List<Cache>cs){
//  if(cs.isEmpty()){return g0;}
//  return _lastOrErr(cs.get(cs.size()-1));
//  }
//G _lastOrErr(Cache c){
//  return c.op(
//    empty->null,
//    gErr->null,
//    gG1Cs0->null,
//    full->full.res.isErr()?null:full.res._g
//    );
//  }
//
//R open(boolean[] ok,Cache c,G g){
//  R r=g.open();
//  ok[0]=c.op(
//    /*empty*/c0->false,
//    /*start*/c0->r.isErr(),//Note: we do not check equality of errors. Should be ok since errors are fatal and we return r and not the cached one
//    /*middle*/c0->!r.isErr() && g.openOk(r._g,c0.g0,c0.g1),
//    /*full*/c0->!r.isErr() && g.openOk(r._g,c0.g0,c0.g1)
//    );
//  return r;
//  }
//Cache middleAndClose(G g0,List<Cache> cs0,G g1){
//  List<Cache> cs=openCloseNested(cs0,g1);
//  G lastG=_lastOrErr(g1,cs);
//  if(lastG==null){return Cache.of(g0,g1,cs);}
//  return Cache.of(g0,g1,cs,lastG.close());
//  }
//public Program top(Cache c,G g){
//  Cache r=openClose(c,g);
//  if(r.isErr()){throw r.err();}
//  return r.op(
//    f->{throw bug();},
//    f->{throw bug();},
//    f->{throw bug();},
//    f->f.res._g.out()
//    );
//  }
//
//Cache openClose(Cache c,G g){
//  if(c==Cache.of()){
//    R r=g.open();
//    if(r.isErr()){return Cache.of(g,r._err);}
//    return middleAndClose(g,L(),r._g);
//    }
//  boolean[]ok={false};
//  R r1=open(ok, c, g);
//  if(r1.isErr()){return Cache.of(g,r1._err);}
//  if(!ok[0]){return middleAndClose(g,L(),r1._g);}
//  var cs=c.cs();
//  if(r1._g.middleAndCloseCached(c)){return c;}
//  return middleAndClose(g,cs,r1._g);
//  }
//List<Cache> openCloseNested(List<Cache> cs,G g0){
//  if(!g0.needOpen()){return L();}
//  if(cs.isEmpty()){
//    Cache c1=openClose(Cache.of(),g0);
//    G g1=_lastOrErr(c1);
//    if(g1==null){return L(c1);}
//    return pushL(c1,openCloseNested(cs,g1));
//    }
//  Cache c0=cs.get(0);
//  Cache c1=openClose(c0,g0);
//  G g1=_lastOrErr(c1);
//  if(g1==null){return L(c1);}
//  if(cs.size()==1){return pushL(c1,openCloseNested(L(),g1));}
//  Cache c=cs.get(1);
//  if(c.g0!=g1){return pushL(c1,openCloseNested(L(),g1));}//TODO: is pointer equality right? note: is can not be an error cache
//  return pushL(c1,openCloseNested(cs.subList(1, cs.size()),g1));
//  }
//}