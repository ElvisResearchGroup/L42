package is.L42.tools.cacheTree;
import static is.L42.tools.General.L;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;

import java.util.List;

import is.L42.common.EndError;
public interface CacheTree {
  interface I{Boolean mayBeEq(I i);}
  interface M{Boolean mayBeEq(M m);}
  interface O{boolean eq(O f);}
  class R{Fun _g; O _o; EndError _err;
    //either only _err==null or only _g and _o ==null
    public R(Fun _g,O _o,EndError _err){
      this._g=_g;this._o=_o;this._err=_err;
      assert (_err==null && _o!=null && _g!=null)
        ||  (_err!=null && _o==null && _g==null);
      }
    public boolean eq(R r){
      if((this._g==null)!=(r._g==null)){return false;}
      if((this._o==null)!=(r._o==null)){return false;}
      if((this._err==null)!=(r._err==null)){return false;}
      if(_err==null){return _g.eq(r._g) && _o.eq(r._o);}
      return _err.getMessage().equals(r._err.getMessage());
      }
    }
  class C{D d; Fun _gr; List<C> cs; R r;
    public C(D d,Fun _gr,List<C> cs,R r){this.d=d;this._gr=_gr;this.cs=cs;this.r=r;}
    }
  interface D{I i(); List<? extends D> ds(); M m();
    default Boolean mayBeEqDs(D d){
      if(ds().size()!=d.ds().size()){return false;}
      boolean canTrue=true;
      for(int i:range(ds())){
        Boolean dTest=ds().get(i).mayBeEq(d.ds().get(i));
        canTrue&=dTest!=null;
        if(dTest!=null&&!dTest){return false;}
        }
      if(canTrue){return true;}
      return null;
      }
    default Boolean mayBeEq(D d){
      if(this.getClass()!=d.getClass()){return false;}
      if(ds().size()!=d.ds().size()){return false;}
      Boolean iTest=i().mayBeEq(d.i());
      boolean canTrue=iTest!=null;
      if(canTrue && !iTest){return false;}
      Boolean dsEq=mayBeEqDs(d);
      if(dsEq!=null && !dsEq){return false;}
      canTrue &=dsEq!=null;
      Boolean mTest=m().mayBeEq(d.m());
      if(mTest!=null&&!mTest){return false;}
      canTrue&=mTest!=null;
      if(canTrue && mTest){return true;}
      return null;
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
    Fun complexEq(C c, I i, boolean[]eq);
    Fun complexEq(C c, M m, boolean[]eq);
    boolean eq(Fun f);
    Fun apply(I i);
    R apply(M m, List<O> os);
    default EndError _errOf(List<C> cs){
      if(cs.isEmpty()){return null;}
      return cs.get(cs.size()-1).r._err;
      }
    default Fun lastGOf(Fun g1,List<C> cs){
      if(cs.isEmpty()){return g1;}
      Fun res=cs.get(cs.size()-1).r._g;
      assert res!=null;
      return res;
      }
    default List<O> osOf(List<C> cs){
      return L(cs,(c,ci)->{
        assert ci.r._o!=null;
        c.add(ci.r._o);
        });
      }
    default C emptyCache(D d){
      Fun g1;try{g1=apply(d.i());}
      catch(EndError e){return new C(d,null,L(),new R(null,null,e));} 
      List<C> cs=g1.apply(L(),d.ds());
      EndError err=_errOf(cs);
      if(err!=null){return new C(d,g1,cs,new R(null,null,err));}
      Fun g2=lastGOf(g1, cs);
      R r=g2.apply(d.m(), osOf(cs));
      return new C(d,g1,cs,r);
      }
    default C apply(C _c,D d){
      if(_c==null){return emptyCache(d);}
      D cd=_c.d;
      boolean[] testI={false};
      Fun g0=this.complexEq(_c,d.i(), testI);
      if(!testI[0]){
        Fun g1;try{g1=g0.apply(d.i());}
        catch(EndError e){return new C(d,null,L(),new R(null,null,e));}
        List<C> cs=g1.apply(L(),d.ds());
        EndError err=_errOf(cs);
        if(err!=null){return new C(d,g1,cs,new R(null,null,err));}
        Fun g2=lastGOf(g1, cs);
        return new C(d,g1,cs,g2.apply(d.m(), osOf(cs)));
        }
      assert testI[0];
      if(cd.certainEqNoI(d)){return _c;}// REUSE CACHE
      if(_c._gr==null){return new C(d,null,L(),_c.r);}
      Fun g1=_c._gr;
      Boolean testDs=cd.mayBeEqDs(d);
      if(testDs==null || !testDs){
        List<C> cs=g1.apply(_c.cs,d.ds());
        EndError err=_errOf(cs);
        if(err!=null){return new C(d,g1,cs,new R(null,null,err));}
        boolean eqRes=cs.size()==_c.cs.size();
        if(eqRes){
          for(int i:range(cs)){
            eqRes&=cs.get(i).r.eq(_c.cs.get(i).r);
            }
          }
        if(eqRes){return _c;}
        Fun g2=lastGOf(g1,cs);
        R r=g2.apply(d.m(),osOf(cs));
        return new C(d,g1,cs,r);
        }
      assert testDs;
      assert cd.m().mayBeEq(d.m())!=true;
      EndError err=_errOf(_c.cs);
      if(err!=null){return new C(d,g1,_c.cs,new R(null,null,err));}
      Fun g2=lastGOf(g1,_c.cs);
      boolean[]testM={false}; 
      Fun g3=g2.complexEq(_c, d.m(), testM);
      if(testM[0]){return _c;}// REUSE CACHE
      return new C(d,g1,_c.cs,g3.apply(d.m(),osOf(_c.cs)));
      }
    default List<C> apply(List<C> cs,List<? extends D> ds){
      if(ds.isEmpty()){return  L();}
      C _c=cs.isEmpty()?null:cs.get(0);
      List<C>cs0=cs.isEmpty()?L():popL(cs);
      D d=ds.get(0);
      List<? extends D>ds0=popL(ds);
      Boolean dTest=_c==null?false:_c.d.mayBeEq(d);
      if(dTest!=null && dTest){
        if(_c.r._err!=null){return L(_c);}
        Fun g1=_c.r._g;
        assert g1!=null;
        return pushL(_c,g1.apply(cs0, ds0));
        }
      C cRes=this.apply(_c,d);
      if(cRes.r._err!=null){return L(cRes);}
      Fun g1=cRes.r._g;
      assert g1!=null;
      if(dTest!=null && !dTest){
        return pushL(cRes,g1.apply(L(), ds0));
        } 
      if(_c!=null && cRes.r.eq(_c.r)){return pushL(cRes,g1.apply(cs0, ds0));}
      return pushL(cRes,g1.apply(L(), ds0));
      }
    }
  }