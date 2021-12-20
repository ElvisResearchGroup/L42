package is.L42.cache;

import java.util.ArrayList;

import is.L42.nativeCode.TrustedKind;

public class ArrayListCache extends AbstractListCache<ArrayList<?>>{
  @Override protected ArrayList<?> l(ArrayList<?> t){return t;}
  @Override protected ArrayList<?> newInstance(ArrayList<?> t){
    var res=new ArrayList<>(t.size());
    res.add(null);
    return res;
    }
  @Override public Object typename(){return TrustedKind.List;}
  }