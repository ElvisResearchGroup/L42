package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.popLRight;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.unreachable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.Program;
import is.L42.flyweight.C;
import is.L42.flyweight.P;
import is.L42.generated.Core;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MCall;
import is.L42.generated.Core.PathSel;
import is.L42.generated.LDom;
import is.L42.top.Deps;
import is.L42.visitors.Accumulate;
import is.L42.visitors.CloneVisitor;

//Spec:
//this trashes some methods and classes with unique names.
//we do not touch libs in methods, either the lib is kept together with the 
//surrounding method, or not, 
//we remove all methods that can never be called starting from public ones.
//we remove all classes whose we remove all methods and nesteds
//and whose paths are not needed to preserve well typedness

//What about COMMENTS/DOCS?
//removing them may break pluggable tss?
//keeping a class becouse it has a doc looks silly.. 
//but is only a class, no methods
//so, do we care?

//what about interface methods? do we ever remove them?
//NOT REMOVE INTERFACE methods: interfaces may be implemented in metalibs that we want not to touch.

class CollectPublic extends Accumulate<Map<List<? extends LDom>,Object>>{
  public Map<List<? extends LDom>,Object> empty(){return new LinkedHashMap<>();}
  List<LDom> current=List.of();
  @Override public void visitNC(Core.L.NC nc){
    var aux=current;
    try{
      if(nc.key().hasUniqueNum()){return;}
      current=pushL(current,nc.key());
      result.put(current,nc);
      super.visitNC(nc);
      }
    finally{current=aux;}
  }
  @Override public void visitMWT(Core.L.MWT mwt){
    var aux=current;
    try{
      if(mwt.key().hasUniqueNum()){return;}
      current=pushL(current,mwt.key());
      result.put(current,mwt);
      //DO NOT PROPAGATE super.visitMWT(mwt);
      }
    finally{current=aux;}
    }
  }
class CollectFix{
  final Core.L topLib;
  Map<List<? extends LDom>,Object> all=new LinkedHashMap<>();
  Map<List<? extends LDom>,Object> novel=new LinkedHashMap<>();
  Map<List<? extends LDom>,Object> current;
  @Override public String toString(){
    return all.keySet().stream()
      .map(l->l.stream()
        .map(Object::toString)
        .collect(Collectors.joining(".")))
      .collect(Collectors.joining("\n"));
    }
  CollectFix(Core.L lib){
    topLib=lib;
    current=new CollectPublic().of(lib);
    all.putAll(current);
    Program p=Program.flat(topLib);
    var visitor=new AccumulateUsedNames(p,L());
    visitor.visitDocs(lib.docs());
    visitor.visitTs(lib.ts());
    while(true){
      for(var lso : current.entrySet()){
        accumulateUsedNames(lso.getKey(),lso.getValue());
        }
      if(novel.isEmpty()){return;}
      current=novel;
      novel=new LinkedHashMap<>();
      }
    }
  private void accumulateUsedNames(List<? extends LDom> key,Object value){
    List<C> cs=L(key.stream()
      .filter(e->e instanceof C)
      .map(e->(C)e));
    if(value instanceof MWT mwt){
      Program p=Program.flat(topLib).navigate(cs);
      assert cs.size()+1==key.size();
      mwt.accept(new AccumulateUsedNames(p,key));
      return;
      }
    assert cs.size()==key.size();
    var nc=(Core.L.NC)value;
    var cs0=popLRight(cs);
    Program p=Program.flat(topLib).navigate(cs);
    Program p0=Program.flat(topLib).navigate(cs0);
    var visitor=new AccumulateUsedNames(p,cs);
    var visitor0=new AccumulateUsedNames(p0,cs0);
    visitor0.visitDocs(nc.docs());
    visitor.visitDocs(nc.l().docs());
    visitor.visitTs(nc.l().ts());
    boolean all=nc.l().isInterface() ||!nc.l().info().nativeKind().isEmpty();
    var refined=nc.l().info().refined();
    for(var mwt:nc.l().mwts()){
      if(all ||refined.contains(mwt.key())){
        var key0=pushL(key,mwt.key());
        this.novel.put(key0,mwt);
        this.all.put(key0,mwt);
        }
      }
    }
  class AccumulateUsedNames extends Accumulate.WithG<Void>{
    AccumulateUsedNames(Program p,List<? extends LDom> whereFromTop){
      this.p=p;this.whereFromTop=whereFromTop;
      }
    Program p; List<? extends LDom> whereFromTop;
    List<C> _topCs(P.NCs p){
      var inC=whereFromTop.isEmpty() || whereFromTop.get(whereFromTop.size()-1) instanceof C;
      var inC2=whereFromTop.size()>1 && whereFromTop.get(whereFromTop.size()-2) instanceof C;
      var inTopMeth=!inC && whereFromTop.size()==1;
      var inMetaBody= (inTopMeth||inC2) && (
        !this.p.pTails.isEmpty() && !this.p.pTails.hasC()
        );
      var where= inMetaBody || inC?whereFromTop:popLRight(whereFromTop);
      return CloneRenameUsages._topCs(where,p);
      }
    private void processP(P p){
      if(!p.isNCs()){return;}
      List<C> ls=_topCs(p.toNCs());
      if(ls==null){return;}
      if(all.containsKey(ls)){return;}
      if(ls.isEmpty()){return;}
      addAllPrefixes(ls);
      }
    private void addAllPrefixes(List<C> ls){
      var ls0=popLRight(ls);
      var out=Program.flat(topLib)._ofCore(ls0);
      var nci=_elem(out.ncs(),ls.get(ls.size()-1));
      assert nci!=null:
        "";
      novel.put(ls,nci);
      all.put(ls, nci);
      if(!ls0.isEmpty()){addAllPrefixes(ls0);}
      }
    @Override public void visitL(Core.L l){
      visitLWithP(l,p.push(l));
      }
    public void visitLWithP(Core.L l,Program pp){
      var v=new AccumulateUsedNames(pp,whereFromTop);
      v.visitTs(l.ts());
      for(var mwt:l.mwts()){
        var wft=pushL(whereFromTop,mwt.key());
        new AccumulateUsedNames(pp,wft).visitMWT(mwt);
        }
      v.visitNCs(l.ncs());
      v.visitInfo(l.info());
      v.visitDocs(l.docs());
      }
    @Override public void visitNC(Core.L.NC nc){
      this.visitDocs(nc.docs());
      Program pp=p.push(nc.key());
      var v = new AccumulateUsedNames(
        pp,pushL(whereFromTop,nc.key()));
      v.visitLWithP(nc.l(),pp);
      }
    public void visitPathSel(PathSel p){
      processP(p.p());
      }
    public void visitP(P p){
      processP(p);}
    public void visitMCall(MCall mcall){
      super.visitMCall(mcall);
      var t=g(mcall.xP());
      if(t==null){return;}
      var path=t.p();
      if(!path.isNCs()){return;}
      var originPath=Deps._origin(p,path.toNCs(),mcall.s());
      if(originPath==null){originPath=path.toNCs();}
      List<C> currentP=_topCs(originPath);
      if(currentP==null){return;}
      List<LDom> full=pushL(currentP,mcall.s());
      if(all.containsKey(full)){return;}
      var mwts=Program.flat(topLib)._ofCore(currentP).mwts();
      var mwt0=_elem(mwts,mcall.s());
      novel.put(full, mwt0);
      all.put(full, mwt0);
      }  
    }
  }
//1-collect all List<LDom>
//1a-all=[], new=[], swap=collect all public List<LDom>
//1b-forall ds in swap collect all used names in new
//  all the prefixes of the used are used
//  all methods of used native and interfaces are marked as used, even if unused
//  all refined methods of used classes are marked as used.
//1c all+=swap, swap=new, new=[]
//1d back to b, until change
//2-clone visitor keep only the stuff in visitedLDom
class RestVisitor extends CloneVisitor{
  Set<List<?extends LDom>> rest;
  List<?extends LDom> where=L();
  RestVisitor(Set<List<?extends LDom>> rest){this.rest=rest;}
  boolean keepMWT(MWT mwt,Core.L l){
    var fullKey=pushL(where,mwt.key());
    var res=rest.contains(fullKey);
    assert !l.isInterface() || res;
    assert l.info().nativeKind().isEmpty() || res;
    assert !l.info().refined().contains(mwt.key()) || res;
    return res;    
    }
  @Override public Core.L visitL(Core.L l){
    var ts0=l.ts();
    var mwts0=l.mwts();
    var ncs0=l.ncs();
    var info0=l.info();
    var docs0=l.docs();
    var mwts=L(mwts0.stream().filter(mwt->keepMWT(mwt,l)));
    var ncs=L(ncs0.stream().flatMap(this::_visitNC));
    var info=visitInfo(info0);
    if(mwts==mwts0 && ncs==ncs0 && info==info0){return l;}
    return new Core.L(l.poss(),l.isInterface(),ts0,mwts,ncs,info,docs0);
    }
  @Override public Core.L.MWT visitMWT(Core.L.MWT mwt){throw unreachable();}
  @Override public Core.L.NC visitNC(Core.L.NC nc){throw unreachable();}
  public Stream<Core.L.NC> _visitNC(Core.L.NC nc){
    var where0=pushL(where,nc.key());
    if(!rest.contains(where0)) {return Stream.of();}
    var aux=where;
    try{where=where0;return Stream.of(super.visitNC(nc));}
    finally{where=aux;}
    }
  public Core.L.Info visitInfo(Core.L.Info info){
    var typeDep0=info.typeDep();
    var coherentDep0=info.coherentDep();
    var metaCoherentDep0=info.metaCoherentDep();
    var watched0=info.watched();
    var usedMethods0=info.usedMethods();
    var hiddenSupertypes0=info.hiddenSupertypes();
    var refined0=info.refined();
    var nativePar0=info.nativePar();

    var typeDep=omitUnused(typeDep0);
    var coherentDep=omitUnused(coherentDep0);
    var metaCoherentDep=omitUnused(metaCoherentDep0);
    var res=new Core.L.Info(info.isTyped(),typeDep,coherentDep,metaCoherentDep,watched0,
      usedMethods0,hiddenSupertypes0,refined0,
      info.close(),info.nativeKind(),nativePar0,info._uniqueId());
    if(res.equals(info)){return info;}
    return res;
    }
  List<P.NCs> omitUnused(List<P.NCs> l){
    return L(l.stream().filter(this::usedP));
    }
  boolean usedP(P.NCs p){
    if(!p.hasUniqueNum()) {return true;}
    var key=CloneRenameUsages._topCs(where,p);
    if(key==null){return true;} 
    return rest.contains(key);
    }
  }
public class RemoveUnusedCode {
  public Core.L of(Core.L l){
    var rest=precompute(l);
    return l.accept(new RestVisitor(rest.all.keySet()));
    }
  public CollectFix precompute(Core.L l){
    return new CollectFix(l);
    }
  }