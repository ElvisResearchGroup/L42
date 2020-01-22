package is.L42.tools.cacheTree;
import static is.L42.tools.General.L;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;

import java.util.List;
public class CacheTree {}

interface I{
  Boolean mayBeEq(I i);
  Fun complexEq(Fun start, I other, boolean[]eq);
  }
interface M{
  Boolean mayBeEq(M m);
  Fun complexEq(Fun start, M other, boolean[]eq);
  }
interface O{
  boolean eq(O f);
  }
interface R{Fun g(); O o(); Throwable haveErr();
  default boolean eq(R r){
    if(this.getClass()!=r.getClass()){return false;}
    return g().eq(r.g()) && o().eq(r.o());
    }
  }
interface RR{Fun g(); List<O> os();
  default boolean eq(RR r){
    if(this.getClass()!=r.getClass()){return false;}
    if(os().size()!=r.os().size()){return false;}
    for(int i:range(os())){
      if(!os().get(i).eq(r.os().get(i))){return false;}
      }
    return g().eq(r.g());
    }
  }
interface C{D d(); Fun gr(); List<C> cs(); R r();}

interface D{I i(); List<D> ds(); M m();
  default Boolean mayBeEq(D d){
    if(this.getClass()!=d.getClass()){return false;}
    if(ds().size()!=d.ds().size()){return false;}
    Boolean iTest=i().mayBeEq(d.i());
    boolean canTrue=iTest!=null;
    if(canTrue && !iTest){return false;}
    for(int i:range(ds())){
      Boolean dTest=ds().get(i).mayBeEq(d.ds().get(i));
      canTrue&=dTest!=null;
      if(dTest!=null&&!dTest){return false;}
      }
    Boolean mTest=m().mayBeEq(d.m());
    if(mTest!=null&&!mTest){return false;}
    canTrue&=mTest!=null;
    if(canTrue && mTest){return true;}
    return  null;
    }
  default boolean certainEqNoI(D d){
    if(this.getClass()!=d.getClass()){return false;}
    if(ds().size()!=d.ds().size()){return false;}
    for(int i:range(ds())){
      Boolean dTest=ds().get(i).mayBeEq(d.ds().get(i));
      if(dTest==null || !dTest){return false;}
      }
    Boolean mTest=m().mayBeEq(d.m());
    return mTest!=null && mTest;
    }
  }
interface Fun{
  boolean eq(Fun f);
  Fun apply(I i);
  R apply(M m, List<O> os);
  default R apply(D d){
    Fun g1=apply(d.i());
    RR rr=g1.apply(d.ds());
    return rr.g().apply(d.m(),rr.os());
    }
  RR RR(Fun g,List<O> os);
  default RR apply(List<D> ds){
    D d=ds.get(0);
    List<D> ds0=popL(ds);
    R r=apply(d);
    RR rr=r.g().apply(ds0);
    return RR(rr.g(),pushL(r.o(),rr.os()));
    }
  default C apply(C _c,D d){return null;}
/*


*/
  default List<C> apply(List<C> cs,List<D> ds){
    if(ds.isEmpty()){return  L();}
    C c=cs.isEmpty()?null:cs.get(0);
    List<C>cs0=cs.isEmpty()?L():popL(cs);
    D d=ds.get(0);
    List<D>ds0=popL(ds);
    Boolean dTest=c.d().mayBeEq(d);
    if(dTest!=null && dTest){
      if(c.r().haveErr()!=null){return L(c);}
      Fun g1=c.r().g();
      return pushL(c,g1.apply(cs0, ds0));
      }
    C cRes=this.apply(c,d);
    if(cRes.r().haveErr()!=null){return L(cRes);}
    Fun g1=cRes.r().g();
    if(dTest!=null && !dTest){
      return pushL(cRes,g1.apply(L(), ds0));
      }
    if(cRes.r().eq(c.r())){return pushL(cRes,g1.apply(cs0, ds0));}
    return pushL(cRes,g1.apply(L(), ds0));
    }
  }