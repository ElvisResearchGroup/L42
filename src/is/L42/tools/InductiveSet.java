package is.L42.tools;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public abstract class InductiveSet<K,R>{
  public abstract void rule(K k, Consumer<R> set);
  public boolean isCached(K k){return memoizedMap.containsKey(k);}
  public Set<R> compute(K k){
    Set<R> mRes=memoizedMap.get(k);
    if(mRes!=null){return mRes;}
    assert !isComputing;//prevents lambdas capturing the InductiveSet object and trying to use it.
    isComputing=true;
    assert progressMap.isEmpty();
    var resRec=new Record<K,R>();
    progressMap.put(k,resRec);
    rule(k,resRec);
    var res=progressMap.get(k);
    assert res!=null;
    for(var novel:progressMap.entrySet()){
      assert !memoizedMap.containsKey(novel.getKey());
      memoizedMap.put(novel.getKey(),novel.getValue().res=Collections.unmodifiableSet(novel.getValue().res));
      }
    isComputing=false;
    progressMap.clear();
    return res.res;//the novel.getValue().res= above is needed so that we return the unmodifiable wrapper
    } 
  private static class Record<K,R> implements Consumer<R>{
    Set<R> res=new HashSet<>();
    List<Consumer<R>> rules=new ArrayList<>();
    @Override public void accept(R r) {
      boolean wasAdded=res.add(r);
      if(!wasAdded){return;}
      for(int i=0;i<rules.size();i+=1){
        var rule=rules.get(i);//size can grow during this iteration
        rule.accept(r);
        }
      }
    public void register(Consumer<R>iRule){
      rules.add(iRule);
      for(var r:new ArrayList<>(res)){iRule.accept(r);}
      }    
    }
  private boolean isComputing=false;
  private final Map<K,Record<K,R>> progressMap=new HashMap<>();
  private final Map<K,Set<R>> memoizedMap=new HashMap<>();
  public void install(K ki, Consumer<R> iR) {
    assert isComputing;
    var cachedRes=memoizedMap.get(ki);
    if(cachedRes!=null){
      for(R cri:cachedRes){iR.accept(cri);}
      return;
      }
    var res0=new Record<K,R>();
    var resi=progressMap.putIfAbsent(ki,res0);
    if(resi==null){
      resi=res0;
      rule(ki,resi);
      }
    resi.register(iR);
    }
  }