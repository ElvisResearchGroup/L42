package is.L42.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.main.Main;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L;
import is.L42.generated.Core.PathSel;
import is.L42.maps.L42£ImmMap;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.tools.General;
import is.L42.visitors.CloneVisitor;

import static is.L42.tools.General.*;

public class ResetDocs extends CloneVisitor{
  MetaError err;
  Program p;
  HashMap<PathSel,String> reDocs;
  List<C>csIn=L();
  final ArrayList<PathSel>processed=new ArrayList<>();
  public L apply(Program pIn, L42£ImmMap<?,?> map, Function<L42£LazyMsg, L42Any> wrap) {
    err=new MetaError(wrap);
    p=pIn;
    setReDocs(map);
    L res=processTop();
    checkAllProcessed();
    return res;
    }
  private void checkAllProcessed(){
    if(processed.size()==reDocs.size()){return;}
    for(var ps:processed){
      if(reDocs.containsKey(ps)){continue;}
      err.throwErr(p.topCore(),
        err.intro(ps.p().toNCs().cs(), ps._s())+"does not exists");
      }
    }
  private L processTop(){
    Core.L top=p.topCore();
    top=top.accept(this);
    var topKey=new PathSel(P.of(0,L()),null,null);
    var doc=reDocs.get(topKey);
    if(doc==null){return top;}
    return top.withDocs(newDocs(doc));
    }
  private void checkUniqueN(PathSel current){
    if(!current.p().hasUniqueNum()){return;}
    if(current._s()!=null && current._s().hasUniqueNum()){return;}
    err.throwErr(p.topCore(),err.intro(current.p().toNCs().cs(), current._s())+"contains unique numbers");
    }
  @Override public Core.L.NC visitNC(Core.L.NC nc){
    var oldCsIn=csIn;
    csIn=pushL(csIn,nc.key());
    var current=new PathSel(P.of(0,csIn),null,null);
    var doc=reDocs.get(current);
    if(doc!=null){
      checkUniqueN(current);
      nc=nc.withDocs(newDocs(doc));
      }
    csIn=oldCsIn;
    return nc;
    }
  @Override public Core.L.MWT visitMWT(Core.L.MWT mwt){
    var current=new PathSel(P.of(0,csIn),mwt.key(),null);
    var doc=reDocs.get(current);
    if(doc==null){return mwt;}
    checkUniqueN(current);
    return mwt.withDocs(newDocs(doc));
    }
  private List<Doc> newDocs(String s){
    if(s.isEmpty()){return L();}
    return L(new Doc(null,L(s),L()));
    }
  private void setReDocs(L42£ImmMap<?,?> map){
    //Object o=Main._unwrap(oo);
    //if(o==null || !(o instanceof L42£ImmMap<?, ?>)){err.throwErr(p.topCore(),"Invalid input for reset doc: map is not a native HIMap");}
    //var map=(L42£ImmMap<?, ?>)o;
    reDocs=new HashMap<>();
    for(int i=0;i<map.size();i+=1){
      Object k=map.keyIndex(i);
      Object v=map.valIndex(i);
      if(!(k instanceof L42£Name)){err.throwErr(p.topCore(),"Invalid input for reset doc: key is not a native Name");}
      if(!(v instanceof String)){err.throwErr(p.topCore(),"Invalid input for reset doc: val is not a native String");}
      var kk=(L42£Name)k;
      var ps=new PathSel(P.of(0,kk.cs),kk._s,null);
      reDocs.put(ps,(String)v);
      }
    }
  }
