package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.popLRight;
import static is.L42.tools.General.unreachable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.top.Deps;
import is.L42.top.Top;
import is.L42.generated.C;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Core.MCall;
import is.L42.generated.Core.PathSel;
import is.L42.generated.Core.T;
import is.L42.generated.LDom;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;

class CloneRenameUsages extends CloneVisitorWithProgram.WithG{
  CloneRenameUsages(Rename r){
    super(r.p.navigate(r.cs),G.empty());
    this.r=r;
    this.whereFromTop().addAll(r.cs);
    }
  Rename r;
  P renamedPath(P path){
    int nesting=whereFromTop().size();
    if(!path.isNCs()){return path;}
    List<C> currentP=_topCs(whereFromTop(),path.toNCs());
    if(currentP==null){return path;}
    Arrow a=r.map.get(new Arrow(currentP,null));
    if(a==null || !a.full){return path;}
    if(a.isCs()){return p().minimize(P.of(nesting,a._cs));}
    assert a.isP();
    if(!a._path.isNCs()){return a._path;}
    var res=a._path.toNCs();
    res=res.withN(nesting+res.n()+1);//because destination is relative to outside pStart.top
    assert p().minimize(res)==res;
    return res;
    }
  S renamedS(P.NCs path,S s){
    List<C> currentP=_topCs(whereFromTop(),path);
    if(currentP==null){return s;}
    Arrow a=r.map.get(new Arrow(currentP,s));
    if(a==null || !a.full){return s;}
    assert a._sOut!=null;
    return a._sOut;
    }
  static List<C> _topCs(List<? extends LDom> ldoms,P.NCs p){//the result is not wrapped in an unmodifiable list
    if(p.n()>ldoms.size()){return null;}
    if(p.n()==ldoms.size()){return p.cs();}
    ArrayList<C> res=new ArrayList<>();
    for(LDom li:ldoms.subList(0,ldoms.size()-p.n())){
      if(!(li instanceof C)){return null;}
      res.add((C)li);
      }
    res.addAll(p.cs());
    return res;
    }
    
  @Override public L pushedOp(L l) {return infoRename.renameUsageInfo(l);}
  @Override public L visitL(L l){
    var key=getLastCMs();
    var inner=key instanceof S || this.whereFromTop().stream().anyMatch(k->k instanceof S);
    if(inner){
      assert r.p._ofCore(r.cs)!=l;
      return doPushedOp(super.visitL(l));
      }
    assert r.p._ofCore(r.cs)==l:
    "";
    List<MWT> mwts1=L(l.mwts(),(c,mwti)->
      c.addAll(r.renameMWT(visitMWT(mwti))));
    var ncs1=r.renameNCs(this,l.ncs());
    Info i=infoForNewPrivateNesteds(l.ncs(),ncs1,l.info());
    List<T> ts1=visitTs(l.ts());
    List<Doc> docs1=visitDocs(l.docs());
    return doPushedOp(new L(l.poss(),l.isInterface(),ts1,mwts1,ncs1,i,docs1));    
    }
  Info infoForNewPrivateNesteds(List<NC> ncs0,List<NC> ncs1,Info former){
    Deps d=new Deps();
    for(NC nc:ncs1){
      if(!nc.key().hasUniqueNum()){continue;}
      if(_elem(ncs0, nc.key())!=null){continue;}
      d.collectDepsE(p(),nc.l());
      }
    if (d.isEmpty()){return former;}
    return Top.sumInfo(former,d.toInfo(true));
    }
  @Override public P visitP(P path){return renamedPath(path);}
  @Override public Doc visitDoc(Doc doc){return infoRename.visitDoc(doc);}
  @Override public MCall visitMCall(MCall mcall){
    mcall=super.visitMCall(mcall);
    var t=g._of(mcall.xP());
    if(t==null){return mcall;}
    var path=t.p();
    if(!path.isNCs()){return mcall;}
    var originPath=Deps._origin(p(),path.toNCs(),mcall.s());
    if(originPath==null){originPath=path.toNCs();}
    var s2=renamedS(originPath,mcall.s());
    return mcall.withS(s2);    
    }
  @Override public Info visitInfo(Info i){return i;}//it is handled later by infoRename.renameUsageInfo 
  private final InfoCloneVisitor infoRename=new InfoCloneVisitor();
  private class InfoCloneVisitor extends CloneVisitor{
    public L renameUsageInfo(L l){
      Info res=l.info().accept(this);
      boolean close=res.close();
      if(l.isInterface() && !close){
        close=l.mwts().stream().anyMatch(m->m.key().hasUniqueNum());
        if(!close){
        close=l.ts().stream().anyMatch(t->t.p().hasUniqueNum());}
        }
      ArrayList<P.NCs>newWatched=new ArrayList<>();
      ArrayList<P.NCs>newTypeDep=new ArrayList<>();
      for(var p:res.typeDep()){
        if(newTypeDep.contains(p)){continue;}//even if already without duplicated, we may add it as a public root below
        newTypeDep.add(p);
        var pp=Deps._publicRoot(p);
        if(pp!=null && !newTypeDep.contains(pp)){
          newTypeDep.add(pp);
          if(!pp.equals(P.pThis0)){newWatched.add(pp);}
          }
        }
      for(var ps:res.usedMethods()){//two iterations to first collect all the newWatched
        var pi=ps.p().toNCs();
        if(pi.equals(P.pThis0)){continue;}
        if(pi.hasUniqueNum()){
          assert newWatched.contains(pi);
          continue;
          }
        if(ps._s().hasUniqueNum() && !newWatched.contains(pi)){newWatched.add(pi);}
        }
      ArrayList<PathSel>newUsedMethods=new ArrayList<>();
      for(var ps:res.usedMethods()){
        if(ps.p().equals(P.pThis0) || ps.p().hasUniqueNum()){continue;}
        if(newWatched.contains(ps.p())){continue;}
        assert !newUsedMethods.contains(ps);//dups already removed by CloneVisitor
        newUsedMethods.add(ps);
        }
      assert newWatched.stream().noneMatch(p->p.equals(P.pThis0) || p.hasUniqueNum()):newWatched;
      res=new Info(res.isTyped(),L(newTypeDep.stream()),
        res.coherentDep(),res.metaCoherentDep(),
        mergeU(res.watched(),newWatched),L(newUsedMethods.stream()),
        L(res.hiddenSupertypes().stream().filter(p->!p.hasUniqueNum())),
        res.refined(),
        close,
        res.nativeKind(),res.nativePar(),res._uniqueId()
        );
      return l.withInfo(res);
      }
    @Override public L visitL(L l){
      throw unreachable();
      //return renameUsageInfo(super.visitL(l));
      }
    @Override public P visitP(P path){return CloneRenameUsages.this.visitP(path);}
    @Override public S visitS(S s){//for how we use this, we can assume it is inside an Info.refined
      var p0=CloneRenameUsages.this.p();
      for(var t:p0.topCore().ts()){
        L l=p0._ofCore(t.p());
        if(l.info().refined().contains(s)){continue;}
        if(_elem(l.mwts(),s)==null){continue;}
        return renamedS(t.p().toNCs(),s);
        }
      return s;
      }
    @Override public PathSel visitPathSel(PathSel s){//for how we use this, we can assume it is inside an Info.usedMethods or Docs
      var path=s.p().toNCs();
      P p2=renamedPath(path);
      if(s._s()==null ||!p2.isNCs()){return s.withP(p2);}
      var pathOrigin=Deps._origin(p(), path, s._s());
      if(pathOrigin==null){return s;}
      S s2=renamedS(pathOrigin,s._s());
      X x2=null;
      if(s._x()!=null){
        int i=s._s().xs().indexOf(s._x());
        if(i!=-1){x2=s2.xs().get(i);}
        }
      return new PathSel(p2,s2,x2);//we do not need to "watch the origin ever" so we can keep the simpler usedMethods p2
      }
    };
  }