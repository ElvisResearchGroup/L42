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