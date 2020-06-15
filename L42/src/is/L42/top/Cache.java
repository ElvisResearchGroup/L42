package is.L42.top;

import static is.L42.tools.General.bug;

import java.util.List;
import java.util.function.Function;
import is.L42.common.EndError;
import is.L42.common.Program;

class R{
  final EndError _err;final G _g;final Object _obj;
  R(G g,Object obj){
    assert g!=null;
    _g=g;
    _obj=obj;
    _err=null;
    }
  R(EndError err){assert err!=null;_err=err;_g=null;_obj=null;}
  boolean isErr(){return _err!=null;}
  }
class Rs{
  final EndError _err;final List<G> _g;
  Rs(List<G> g){assert g!=null;_g=g;_err=null;}
  Rs(EndError err){assert err!=null;_err=err;_g=null;}
  boolean isErr(){return _err!=null;}
  }
abstract class G{
  State state;
  public abstract Object layer();
  public abstract R _open(G cg,R cr);
  public abstract R _close(G cg,R cr);
  public abstract boolean needOpen();
  public R open(G cg,R cr){try{return _open(cg,cr);}catch(EndError err){return new R(err);}}
  public R close(G cg,R cr){try{return _close(cg,cr);}catch(EndError err){return new R(err);}}
  @Override public int hashCode(){throw bug();}
  @Override public boolean equals(Object o){throw bug();}
  }
  /*@Override public int hashCode(){return layer().hashCode()*31+state.hashCode();}
  @Override public boolean equals(Object o){
    if(this==o){return true;}
    if(o==null){return false;}
    if(this.getClass()!=o.getClass()){return false;}
    var g=(G)o;
    assert layer()!=null;
    assert state!=null;
    return state.equals(g.state) && layer().equals(g.layer());
    }
  }*/
/*
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
  }*/
//public abstract class Cache{G g0;
//  abstract G g1();
//  abstract boolean isErr();
//  abstract EndError err();
//  abstract <T> T op(Function<CacheEmpty0,T>f0,Function<CacheErr1,T>f1,Function<CacheErr2,T>f2,Function<Cache3,T>f3);
//  List<Cache>cs(){return op(c->{throw bug();},c->{throw bug();},c->c.cs,c->c.cs);}
//  private static Cache empty=new CacheEmpty0();
//  public static Cache of(){return empty;}
//  public static Cache of(G g0,EndError err){return new CacheErr1(g0,err);}
//  public static Cache of(G g0,G g1,List<Cache> gs){return new CacheErr2(g0,g1,gs);}
//  public static Cache of(G g0,G g1,List<Cache> gs,R res){return new Cache3(g0,g1,gs,res);}
//  static class CacheEmpty0 extends Cache{
//    boolean isErr(){return false;}
//    G g1(){throw bug();}
//    EndError err(){throw bug();}
//    <T> T op(Function<CacheEmpty0,T>f0,Function<CacheErr1,T>f1,Function<CacheErr2,T>f2,Function<Cache3,T>f3){return f0.apply(this);}
//    }
//  static class CacheErr1 extends Cache{
//    EndError err;
//    CacheErr1(G g0,EndError err){this.g0=g0;this.err=err;}
//    G g1(){throw bug();}
//    boolean isErr(){return true;}
//    EndError err(){return err;}
//    <T> T op(Function<CacheEmpty0,T>f0,Function<CacheErr1,T>f1,Function<CacheErr2,T>f2,Function<Cache3,T>f3){return f1.apply(this);}
//    }
//  static class CacheErr2 extends Cache{
//    G g1;
//    List<Cache> cs;
//    CacheErr2(G g0,G g1,List<Cache> cs){this.g0=g0;this.g1=g1;this.cs=cs;}
//    G g1(){return g1;} 
//    boolean isErr(){return true;}
//    EndError err(){return cs.get(cs.size()-1).err();}
//    <T> T op(Function<CacheEmpty0,T>f0,Function<CacheErr1,T>f1,Function<CacheErr2,T>f2,Function<Cache3,T>f3){return f2.apply(this);}
//    }
//  static class Cache3 extends Cache{
//    G g1;
//    List<Cache> cs;
//    R res;
//    Cache3(G g0,G g1,List<Cache> cs,R res){this.g0=g0;this.g1=g1;this.cs=cs;this.res=res;}
//    G g1(){return g1;}
//    boolean isErr(){return res.isErr();}
//    EndError err(){return res._err;}
//    <T> T op(Function<CacheEmpty0,T>f0,Function<CacheErr1,T>f1,Function<CacheErr2,T>f2,Function<Cache3,T>f3){return f3.apply(this);}
//    }
//  }