package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.popLRight;

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
  Info renameUsagesInfo(boolean isInterface,List<MWT>mwts,Info info){
    Info res=info.accept(infoRename);
    if(isInterface && !res.close()){
      boolean priv=mwts.stream().anyMatch(m->m.key().hasUniqueNum());
      if(priv){res=res.withClose(true);}
      }
    ArrayList<P.NCs>newWatched=new ArrayList<>();
    ArrayList<P.NCs>newTypeDep=new ArrayList<>();
    for(var p:res.typeDep()){//again; and also do the new public roots for typeDep
      if(newTypeDep.contains(p)){continue;}
      var pp=Deps._publicRoot(p);
      if(pp!=null && !newTypeDep.contains(p)){
        newTypeDep.add(pp);
        if(!pp.equals(P.pThis0)){newWatched.add(pp);}
        }
      newTypeDep.add(p);
      }
    for(var ps:res.usedMethods()){
      var pi=ps.p().toNCs();
      if(pi.equals(P.pThis0)){continue;}
      if(pi.hasUniqueNum()){
        assert newWatched.contains(pi);
        continue;
        }
      if(ps._s().hasUniqueNum()){newWatched.add(pi);continue;}
      }
    ArrayList<PathSel>newUsedMethods=new ArrayList<>();
    for(var ps:res.usedMethods()){
      if(ps.p().equals(P.pThis0)){continue;}
      if(newWatched.contains(ps.p())){continue;}
      if(!newUsedMethods.contains(ps)){newUsedMethods.add(ps);}
      }
    assert newWatched.stream().noneMatch(p->p.equals(P.pThis0) || p.hasUniqueNum()):newWatched;
    return res
      .withTypeDep(L(newTypeDep.stream()))
      .withWatched(mergeU(res.watched(),newWatched))
      .withUsedMethods(L(newUsedMethods.stream()));
    }
  @Override public L visitL(L l){
    assert r.p._ofCore(r.cs)==l;
    List<MWT> mwts1=L(l.mwts(),(c,mwti)->
      c.addAll(r.renameMWT(visitMWT(mwti))));
    var ncs1=r.renameNCs(this,l.ncs());
    List<T> ts1=visitTs(l.ts());
    Info info1=renameUsagesInfo(l.isInterface(),mwts1,l.info());
    List<Doc> docs1=visitDocs(l.docs());
    return new L(l.poss(),l.isInterface(),ts1,mwts1,ncs1,info1,docs1);    
    }
  @Override public P visitP(P path){return renamedPath(path);}
  @Override public MCall visitMCall(MCall mcall){
    mcall=super.visitMCall(mcall);
    var t=g._of(mcall.xP());
    if(t==null){return mcall;}
    var path=t.p();
    if(!path.isNCs()){return mcall;}
    var s2=renamedS(path.toNCs(),mcall.s());
    return mcall.withS(s2);    
    }
  @Override public Info visitInfo(Info i){return infoRename.visitInfo(i);}
  private final CloneVisitor infoRename=new CloneVisitor(){
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
      if(s._s()==null){return s.withP(p2);}
      S s2=renamedS(path,s._s());
      X x2=null;
      if(s._x()!=null){
        int i=s._s().xs().indexOf(s._x());
        if(i!=-1){x2=s2.xs().get(i);}
        }
      return new PathSel(p2,s2,x2);
      }
    };
  }