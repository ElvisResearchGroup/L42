package is.L42.meta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import is.L42.common.Program;
import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.Core.MWT;
import is.L42.generated.Mdf;
import is.L42.typeSystem.Coherence;

public class GuessFields {
  MetaError err;
  ArrayList<MWT> abs=new ArrayList<>();
  LinkedHashMap<X,List<MWT>> getters=new LinkedHashMap<>();
  HashSet<X> fieldsUsedInReadCache=new HashSet<>();
  boolean autoNormed=false;
  boolean gettersAllImm=true;
  LinkedHashMap<X,List<MWT>> setters=new LinkedHashMap<>();

  public void addGettersSetters(Program p,List<X> _pars) {
    var l=p.topCore();
    for(var m:l.mwts()){
      if(m._e()==null){
        this.abs.add(m);
        var allowed=_pars==null?true:_pars.stream()
          .anyMatch(xi->xi.inner().contains(m.key().m()));
        if(allowed){_addGettersSetters(m);}
        }
      if(Utils.match(p, err, "readNowCache",m)){
        this.fieldsUsedInReadCache.addAll(m.key().xs());
        }
      if(Utils.match(p, err, "eagerCache",m)){this.autoNormed=true;}
      }
    }
  void _addGettersSetters(MWT m){
    if(m.key().xs().size()>1){return;}
    X x=Coherence.fieldName(m.mh());
    if(m.key().xs().size()==1){
      if(!m.key().xs().get(0).equals(X.thatX)){return;}
      if(!m.mh().t().equals(P.coreVoid)){return;}
      if(!m.mh().mdf().isIn(Mdf.Mutable, Mdf.Lent)){return;}
      var list=setters.getOrDefault(x,new ArrayList<>());
      list.add(m);
      setters.putIfAbsent(x, list);
      return;
      }
    if(!m.mh().mdf().isIn(Mdf.Mutable, Mdf.Lent,Mdf.Immutable,Mdf.Readable)){return;}
    var list=getters.getOrDefault(x,new ArrayList<>());
    list.add(m);
    if(!m.mh().t().mdf().isIn(Mdf.Immutable,Mdf.Class)){gettersAllImm=false;}
    getters.putIfAbsent(x, list);
    }
  }