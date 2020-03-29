package is.L42.meta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import is.L42.common.Program;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.X;
import is.L42.typeSystem.Coherence;

public class GuessFields {
  MetaError err;
  ArrayList<MWT> abs=new ArrayList<>();
  LinkedHashMap<X,List<MWT>> getters=new LinkedHashMap<>();
  HashSet<X> fieldsUsedInReadCache=new HashSet<>();
  boolean gettersNoMut=true;
  LinkedHashMap<X,List<MWT>> setters=new LinkedHashMap<>();

  public void addGettersSetters(Program p) {
    var l=p.topCore();
    for(var m:l.mwts()){
      if(m._e()==null){
        this.abs.add(m);
        _addGettersSetters(m);
        }
      if(Utils.match(p, err, "readEagerCache",m)){//||Utils.match(p, err, "readLazyCache",m)//TODO: add it when we make the readLazyCache too...
        this.fieldsUsedInReadCache.addAll(m.key().xs());
        }
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
    if(!m.mh().t().mdf().isIn(Mdf.Immutable,Mdf.Readable,Mdf.Class)){gettersNoMut=false;}
    getters.putIfAbsent(x, list);
    }
  }