package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.popLRight;
import static is.L42.tools.General.pushL;
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
import is.L42.visitors.WellFormedness;

class CloneRenameUsages extends CloneVisitorWithProgram.WithG{
  CloneRenameUsages(Rename r){
    super(r.p.navigate(r.cs),G.empty());
    this.r=r;
    this.whereFromTop().addAll(r.cs);
    }
  Rename r;
  P renamedPath(P path){
    assert this.p().minimize(path)==path: 
      path +" "+this.p().minimize(path);
    var res=renamedPathAux(path);
    assert this.p().minimize(res)==res: res +" "+this.p().minimize(res);
    return res;
    }
  private P.NCs renamedPathPrivate(int nesting,P.NCs path,C last){//there was a mapping Cs.C->Cs.C::n
    var p0=path.withCs(popLRight(path.cs()));
    List<C> currentP=_topCs(whereFromTop(),p0);
    if(currentP==null){return path;}
    Arrow a=r.map.get(new Arrow(currentP,null)); //if Cs was also renamed
    if(a==null || !a.full){return path;}
    if(!a.isCs() || a._cs.isEmpty()){return path;}
    C last0=a._cs.get(a._cs.size()-1);
    if(!last0.hasUniqueNum()){return path;}
    P.NCs newP=p().minimize(P.of(nesting,a._cs));
    int size=newP.cs().size();
    if(size<=1){return newP.withCs(pushL(newP.cs(),last));}//0 or 1; so removed the last there is nothing to rename
    C lastC=newP.cs().get(size-1);
    if(!lastC.hasUniqueNum()){return newP.withCs(pushL(newP.cs(),last));}
    newP=renamedPathPrivate(nesting,newP,lastC);
    return newP.withCs(pushL(newP.cs(),last));
    }
  private P addPrivateTail(P.NCs path,List<C> tail){return path.withCs(merge(path.cs(),tail));}
  private P renamedPathAux(P path){
    int nesting=whereFromTop().size();
    if(!path.isNCs()){return path;}
    List<C> currentP=_topCs(whereFromTop(),path.toNCs());
    if(currentP==null){return path;}
    var csCut=L(currentP.stream().takeWhile(c->!c.hasUniqueNum()));
    Arrow a=r.map.get(new Arrow(csCut,null));
    if(a==null || !a.full){return path;}
    if(a.isCs()){
      List<C> csMore=L();//the private tail
      if(csCut.size()!=currentP.size()){csMore=currentP.subList(csCut.size(),currentP.size());}
      P.NCs newP=p().minimize(P.of(nesting,a._cs));
      int size=newP.cs().size();
      if(size<=1){return addPrivateTail(newP,csMore);}//0 or 1; so removed the last there is nothing to rename      
      C lastC=newP.cs().get(size-1);
      if(!lastC.hasUniqueNum()){return addPrivateTail(newP,csMore);}
      assert !csMore.isEmpty() || popLRight(newP.cs()).equals(popLRight(path.toNCs().cs())): newP+" "+path;
      assert newP.n()==path.toNCs().n(): newP+" "+path;
      return addPrivateTail(renamedPathPrivate(nesting,newP,lastC),csMore);
      }
    assert a.isP();
    assert csCut.size()==currentP.size();
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
    
  @Override public L pushedOp(L l) {return infoRename.renameUsageInfo(this.p(),l);}
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
    assert l.info().usedMethods().stream().noneMatch(u->l.info().watched().contains(u.p()));
    Info i=infoForNewPrivateNesteds(l.ncs(),ncs1,l.info());
    assert i.usedMethods().stream().noneMatch(u->i.watched().contains(u.p()));
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
    return former.sumInfo(d.toInfo(true));
    }
  @Override public P visitP(P path){return renamedPath(path);}
  @Override public Doc visitDoc(Doc doc){return infoRename.visitDoc(doc);}
  @Override public MCall visitMCall(MCall mcall0){
    MCall mcall1=super.visitMCall(mcall0);
    var t=g._of(mcall0.xP());
    if(t==null){t=g._of(mcall1.xP());}
    if(t==null){return mcall1;}
    var path=t.p();
    if(!path.isNCs()){return mcall1;}
    var originPath=Deps._origin(p(),path.toNCs(),mcall1.s());
    if(originPath==null){originPath=path.toNCs();}
    var s2=renamedS(originPath,mcall1.s());
    return mcall1.withS(s2);    
    }
  @Override public Info visitInfo(Info i){return i;}//it is handled later by infoRename.renameUsageInfo 
  private final InfoCloneVisitor infoRename=new InfoCloneVisitor();
  private class InfoCloneVisitor extends CloneVisitor{
    boolean assertWatchedOk(Program p0,P.NCs pi, List<P.NCs>newWatched){
      var pr=Deps._publicRoot(p0,pi);
      if(newWatched.contains(pr)){return true;}
      if(pr.equals(P.pThis0)){return true;}
      if(pr.cs().size()!=0){return false;}
      return p0.pop(pr.n()).inPrivate();
      }
    boolean watchable(Program p0,ArrayList<P.NCs>newWatched,P.NCs w){
      if(newWatched.contains(w)){return false;}
      if(w.hasUniqueNum()){return false;}
      if(w.cs().size()!=0){return true;}
      if(w.n()==0){return false;}
      return !p0.pop(w.n()).inPrivate();
      }
    public L renameUsageInfo(Program p0,L l){
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
        var pp=Deps._publicRoot(p(),p);
        if(pp!=null){
          if(!newTypeDep.contains(pp)){newTypeDep.add(pp);}
          if(watchable(p0, newWatched,pp)){newWatched.add(pp);}
          }
        }
      for(var ps:res.usedMethods()){//two iterations to first collect all the newWatched
        var pi=ps.p().toNCs();
        if(pi.equals(P.pThis0)){continue;}
        if(pi.hasUniqueNum()){
          assert assertWatchedOk(p(),pi,newWatched);
          continue;
          }
        if(ps._s().hasUniqueNum() && watchable(p0, newWatched, pi)){newWatched.add(pi);}
        }
      for(var w:res.watched()){if(watchable(p0, newWatched, w)){newWatched.add(w);}}
      ArrayList<PathSel>newUsedMethods=new ArrayList<>();
      for(var ps:res.usedMethods()){
        if(ps.p().equals(P.pThis0) || ps.p().hasUniqueNum()){continue;}
        if(!watchable(p0, newWatched,ps.p().toNCs())){continue;}
        assert !newUsedMethods.contains(ps);//dups already removed by CloneVisitor
        newUsedMethods.add(ps);
        }
      assert newWatched.stream().noneMatch(p->p.equals(P.pThis0) || p.hasUniqueNum()):newWatched;
      res=new Info(res.isTyped(),L(newTypeDep.stream()),
        res.coherentDep(),res.metaCoherentDep(),
        L(newWatched.stream()),L(newUsedMethods.stream()),
        L(res.hiddenSupertypes().stream().filter(p->!p.hasUniqueNum())),
        res.refined(),
        close,
        res.nativeKind(),res.nativePar(),res._uniqueId()
        );
      return l.withInfo(res);
      }
    @Override public L visitL(L l){throw unreachable();}
    @Override public P visitP(P path){return CloneRenameUsages.this.visitP(path);}
    @Override public List<S> visitInfoSs(List<S> ss){
      return L(ss,(c,s)->{
        var s0=this.visitS(s);
        if(s0==null){return;}
        if(c.contains(s0)){return;}
        c.add(s0);
        });
      }
    @Override public S visitS(S s){//for how we use this, we can assume it is inside an Info.refined
      var p0=CloneRenameUsages.this.p();
      for(var t:p0.topCore().ts()){
        L l=p0._ofCore(t.p());
        //if l is null, it was made private.
        //The only case where s is renamed and also P is renamed, is if s is made private
        if(l==null){continue;} 
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