package is.L42.cache;

import java.util.ArrayList;

import is.L42.nativeCode.Flags;

public abstract class AbstractListCache<L> extends AbstractStructuredCache<L>{
  protected abstract ArrayList<?> l(L t);
  @Override public Object f(L t, int i){return l(t).get(i+1);}
  @Override public void setF(L t,int i,Object o){
    var tt=l(t);
    if(i+1>=tt.size()){tt.add(null);tt.add(null);}//so that set is "also" an add
    @SuppressWarnings("unchecked")
    var tto=(ArrayList<Object>)tt;
    tto.set(i+1, o);    
    }      
  @Override protected void add(KeyNorm2D key, L t) {
    super.add(key,t);
    this.setMyNorm(t, t);
    }
  @Override public boolean isNorm(L t){return t==null ||l(t).get(0)!=null;}
  //above, if t==null, this cache is used for an Optional ArrayList, and the empty optional is
  //the normalized verion of itself
  @Override public int fn(L t){return l(t).size()-1;}
  @Override public L42Cache<?> rawFieldCache(Object o,int i){
      if(i%2!=0){return Flags.cache;}
      if(o!=null) {return L42CacheMap.getCacheObject(o);}
      return this;
      }
  @SuppressWarnings("unchecked")
  @Override public L getMyNorm(L me){
      if(me==null) {return null;}///the normalized version of iself
      return (L) ((Object[])l(me).get(0))[0];
      }
  @SuppressWarnings("unchecked") @Override 
  public void setMyNorm(L me, L norm){
    ((ArrayList<Object>)l(me)).set(0, new Object[]{norm});
    //Norm wrapped up as array, otherwise when placed as hashmap key it would loop 
    }
  }