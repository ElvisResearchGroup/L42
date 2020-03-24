package is.L42.top;

import java.util.HashMap;
import java.util.HashSet;

import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.S;
import is.L42.meta.Arrow;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.visitors.CloneVisitor;

public class UniqueNsRefresher extends CloneVisitor{
  HashSet<Integer> used=Resources.usedUniqueNs;
  int upTo=Resources.allBusyUpTo;
  HashSet<Integer> newUsed=new HashSet<Integer>();
  HashMap<Integer,Integer> refreshed=new HashMap<>();
  L refreshUniqueNs(L l){
    L res=l.accept(this);
    used.addAll(newUsed);
    Resources.allBusyUpTo=upTo;
    return res;
    }
  private int updatedFor(int n){
    if(!used.contains(n)){newUsed.add(n);return n;}
    Integer k=refreshed.get(n);
    if(k==null){
      k=this.firstFreshUnique();
      refreshed.put(n,k);
      }
    return k;
    }
  @Override public C visitC(C c){
    if(!c.hasUniqueNum() || c.uniqueNum()==0){return c;}
    return c.withUniqueNum(updatedFor(c.uniqueNum()));
    }
  @Override public S visitS(S s){
    if(!s.hasUniqueNum() || s.uniqueNum()==0){return s;}
    return s.withUniqueNum(updatedFor(s.uniqueNum()));
    }
  public int firstPrivateOf(L l){
    int count=0;
    int res=Integer.MAX_VALUE;
    for(var nci:l.ncs()){
      if(nci.key().hasUniqueNum()){
        count+=1;
        res=Math.min(nci.key().uniqueNum(),res);
        }
      }
    for(var mwti:l.mwts()){
      if(mwti.key().hasUniqueNum() &&mwti.key().uniqueNum()!=0){
        count+=1;
        res=Math.min(mwti.key().uniqueNum(),res);
        }
      }
    if(count!=0){return res;}
    res=firstFreshUnique();
    used.add(res);
    return res;
    }
  int firstFreshUnique(){
    upTo+=1;
    while(used.contains(upTo)){upTo+=1;}
    return upTo;
    }
  }