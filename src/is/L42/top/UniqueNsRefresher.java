package is.L42.top;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import is.L42.flyweight.C;
import is.L42.flyweight.P;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.LDom;
import is.L42.generated.S;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.General;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.PropagatorCollectorVisitor;

public class UniqueNsRefresher extends CloneVisitor{
  HashSet<Integer> used=Resources.usedUniqueNs;
  int upTo=Resources.allBusyUpTo;
  HashSet<Integer> newUsed=new HashSet<Integer>();
  HashMap<Integer,Integer> refreshed=new HashMap<>();
  boolean scope;public UniqueNsRefresher(boolean scope){this.scope=scope;}
  public UniqueNsRefresher(){this(false);}
  L refreshUniqueNs(L l){
    if(scope){l.accept(new PropagatorCollectorVisitor(){
      @Override public void visitNC(Core.L.NC nc){
        if(!nc.key().hasUniqueNum()){return;}
        refreshed.put(nc.key().uniqueNum(),UniqueNsRefresher.this.firstFreshUnique());
        }
      @Override public void visitMWT(Core.L.MWT mwt){
        if(!mwt.key().hasUniqueNum()){return;}
        refreshed.put(mwt.key().uniqueNum(),UniqueNsRefresher.this.firstFreshUnique());
        }
      });}
    L res=l.accept(this);
    used.addAll(newUsed);
    Resources.allBusyUpTo=upTo;
    return res;
    }
  private int updatedFor(int n){
    if(scope){
      Integer k=refreshed.get(n);
      if(k==null){return n;}
      return k;
      }
    if(!used.contains(n)){newUsed.add(n);return n;}
    Integer k=refreshed.get(n);
    if(k==null){
      k=this.firstFreshUnique();
      refreshed.put(n,k);
      }
    return k;
    }
  @Override public P visitP(P p){
    if(!p.isNCs()){return p;}
    var pp=p.toNCs();
    List<C> cs=General.L(pp.cs(),this::visitC);
    return pp.withCs(cs);
    }
  @Override public C visitC(C c){
    if(!c.hasUniqueNum() || c.uniqueNum()==0){return c;}
    return c.withUniqueNum(updatedFor(c.uniqueNum()));
    }
  @Override public S visitS(S s){
    if(!s.hasUniqueNum() || s.uniqueNum()==0){return s;}
    return s.withUniqueNum(updatedFor(s.uniqueNum()));
    }
  public int firstPrivateOf(L l,LDom name){
    int count=0;
    int res=Integer.MAX_VALUE;
    for(var nci:l.ncs()){
      var k=nci.key();
      if(k.hasUniqueNum()){
        if(k.withUniqueNum(-1).equals(name)){continue;}
        count+=1;
        res=Math.min(nci.key().uniqueNum(),res);
        }
      }
    for(var mwti:l.mwts()){
      var k=mwti.key();
      if(k.hasUniqueNum() && k.uniqueNum()!=0){
        if(k.withUniqueNum(-1).equals(name)){continue;}
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