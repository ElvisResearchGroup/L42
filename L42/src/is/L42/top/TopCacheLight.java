package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.pushL;
import java.util.List;
import java.util.function.Function;
import is.L42.common.EndError;
import is.L42.common.Program;

public class TopCacheLight {}
class R{
  final EndError _err;final G _g;
  R(G g){assert g!=null;_g=g;_err=null;}
  R(EndError err){assert err!=null;_err=err;_g=null;}
  boolean isErr(){return _err!=null;}
  }
class Rs{
  final EndError _err;final List<G> _g;
  Rs(List<G> g){assert g!=null;_g=g;_err=null;}
  Rs(EndError err){assert err!=null;_err=err;_g=null;}
  boolean isErr(){return _err!=null;}
  }
interface G{
  G _open();
  G _close();
  boolean middleAndCloseCached(Cache c);
    //holds if we can guarantee that g is correctly cached by C
  boolean needOpen();
  Program out();
  default R open(){try{return new R(_open());}catch(EndError err){return new R(err);}}
  default R close(){try{return new R(_close());}catch(EndError err){return new R(err);}}
  }
class DirectTop{
  Program top(G g){
    R r=openClose(g);
    if(r.isErr()){throw r._err;}
    return r._g.out();
    }
  R openClose(G g0){
    R r0=g0.open();
    if(r0.isErr()){return r0;}
    R r1=openCloseNested(r0._g);
    if(r1.isErr()){return r1;}
    return r1._g.close();
    }
  R openCloseNested(G g){
    while(true){
      if(!g.needOpen()){return new R(g);}
      R r=openClose(g);
      if(r.isErr()){return r;}
      g=r._g;
      }
    }
  }
abstract class Cache{G g0;
  abstract boolean isErr();
  abstract EndError err();
  abstract <T> T op(Function<CacheEmpty0,T>f0,Function<CacheErr1,T>f1,Function<CacheErr2,T>f2,Function<Cache3,T>f3);
  List<Cache>cs(){return op(c->{throw bug();},c->{throw bug();},c->c.cs,c->c.cs);}
  private static Cache empty=new CacheEmpty0();
  static Cache of(){return empty;}
  static Cache of(G g0,EndError err){return new CacheErr1(g0,err);}
  static Cache of(G g0,G g1,List<Cache> gs){return new CacheErr2(g0,g1,gs);}
  static Cache of(G g0,G g1,List<Cache> gs,R res){return new Cache3(g0,g1,gs,res);}
  static class CacheEmpty0 extends Cache{
    boolean isErr(){return false;}
    EndError err(){throw bug();}
    <T> T op(Function<CacheEmpty0,T>f0,Function<CacheErr1,T>f1,Function<CacheErr2,T>f2,Function<Cache3,T>f3){return f0.apply(this);}
    }
  static class CacheErr1 extends Cache{
    EndError err;
    CacheErr1(G g0,EndError err){this.g0=g0;this.err=err;}
    boolean isErr(){return true;}
    EndError err(){return err;}
    <T> T op(Function<CacheEmpty0,T>f0,Function<CacheErr1,T>f1,Function<CacheErr2,T>f2,Function<Cache3,T>f3){return f1.apply(this);}
    }
  static class CacheErr2 extends Cache{
    G g1;
    List<Cache> cs;
    CacheErr2(G g0,G g1,List<Cache> cs){this.g0=g0;this.g1=g1;this.cs=cs;} 
    boolean isErr(){return true;}
    EndError err(){return cs.get(cs.size()-1).err();}
    <T> T op(Function<CacheEmpty0,T>f0,Function<CacheErr1,T>f1,Function<CacheErr2,T>f2,Function<Cache3,T>f3){return f2.apply(this);}
    }
  static class Cache3 extends Cache{
    G g1;
    List<Cache> cs;
    R res;
    Cache3(G g0,G g1,List<Cache> cs,R res){this.g0=g0;this.g1=g1;this.cs=cs;this.res=res;}
    boolean isErr(){return res.isErr();}
    EndError err(){return res._err;}
    <T> T op(Function<CacheEmpty0,T>f0,Function<CacheErr1,T>f1,Function<CacheErr2,T>f2,Function<Cache3,T>f3){return f3.apply(this);}
    }
  }
class CachedTop{
  G _lastOrErr(G g0,List<Cache>cs){
    if(cs.isEmpty()){return g0;}
    return _lastOrErr(cs.get(cs.size()-1));
    }
  G _lastOrErr(Cache c){
    return c.op(
      empty->null,
      gErr->null,
      gG1Cs0->null,
      full->full.res.isErr()?null:full.res._g
      );
    }

  R open(boolean[] ok,Cache c,G g){
    R r=g.open();
    ok[0]=c.op(
      /*empty*/c0->false,
      /*start*/c0->r.isErr(),//Note: we do not check equality of errors. Should be ok since errors are fatal and we return r and not the cached one
      /*middle*/c0->!r.isErr() && r._g.equals(c0.g1),
      /*full*/c0->!r.isErr() && r._g.equals(c0.g1)
      );
    return r;
    }
  Cache middleAndClose(G g0,List<Cache> cs0,G g1){
    List<Cache> cs=openCloseNested(cs0,g1);
    G lastG=_lastOrErr(g1,cs);
    if(lastG==null){return Cache.of(g0,g1,cs);}
    return Cache.of(g0,g1,cs,lastG.close());
    }
  Cache openClose(Cache c,G g){
    if(c==Cache.of()){
      R r=g.open();
      if(r.isErr()){return Cache.of(g,r._err);}
      return middleAndClose(g,L(),r._g);
      }
    boolean[]ok={false};
    R r1=open(ok, c, g);
    if(r1.isErr()){return Cache.of(g,r1._err);}
    if(!ok[0]){return middleAndClose(g,L(),r1._g);}
    var cs=c.cs();
    if(r1._g.middleAndCloseCached(c)){return c;}
    return middleAndClose(g,cs,r1._g);
    }
  List<Cache> openCloseNested(List<Cache> cs,G g0){
    if(!g0.needOpen()){return L();}
    if(cs.isEmpty()){
      Cache c1=openClose(Cache.of(),g0);
      G g1=_lastOrErr(c1);
      if(g1==null){return L(c1);}
      return pushL(c1,openCloseNested(cs,g1));
      }
    Cache c0=cs.get(0);
    Cache c1=openClose(c0,g0);
    G g1=_lastOrErr(c1);
    if(g1==null){return L(c1);}
    if(cs.size()==1){return pushL(c1,openCloseNested(L(),g1));}
    Cache c=cs.get(1);
    if(!c.g0.equals(g1)){return pushL(c1,openCloseNested(L(),g1));}
    return pushL(c1,openCloseNested(cs.subList(1, cs.size()),g1));
    }
  }