package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.popLRight;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.toOneOrBug;
import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import is.L42.common.From;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Core.MCall;
import is.L42.generated.Core.T;
import is.L42.generated.LDom;
import is.L42.generated.P;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.PathSel;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.General;
import is.L42.top.Top;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.Accumulate;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;

public class Rename {
  HashMap<List<C>,List<MWT>> addMapMWTs=new HashMap<>();//mutable, so should be ArrayList, but need List for the default emptyList
  HashMap<List<C>,List<NC>> addMapNCs=new HashMap<>();
  void addMapMWTs(MWT mwt){
    var val=addMapMWTs.get(cs);
    if(val!=null){val.add(mwt);return;}
    mayAdd(cs);
    val=new ArrayList<>();
    val.add(mwt);
    addMapMWTs.put(cs,val);
    }
  void addMapNCs(List<C>cs,NC nc){
    var val=addMapNCs.get(cs);
    if(val==null){
      mayAdd(cs);
      val=new ArrayList<>();
      val.add(nc);
      addMapNCs.put(cs,val);
      return;
      }
    C c=nc.key();
    for(int i:range(val)){
      var vi=val.get(i);
      if(!vi.key().equals(c)){continue;}
      assert vi.l()==Program.emptyL;
      val.set(i,nc);
      return;
      }
    val.add(nc);
    }
  void mayAdd(List<C> cs){
    int i=cs.size()-1;
    if(i>=0){mayAdd(cs.subList(0,i),cs.get(i));}
    }
  void mayAdd(List<C> cs,C c){
    var val=addMapNCs.get(cs);
    if(val!=null){
      if(_elem(val, c)!=null){return;}
      val.add(new L.NC(L(),L(), c,Program.emptyL));
      return;
      }
    mayAdd(cs);
    val=new ArrayList<>();
    val.add(new L.NC(L(),L(), c,Program.emptyL));
    addMapNCs.put(cs,val);
    }
  L addMapTop=Program.emptyL;
  LinkedHashMap<Arrow,Arrow> map;
  HashSet<Integer> existingNs=new HashSet<>();
  int allBusyUpTo=0;//TODO: both need to be recover from environment
  Program p;//includes the top level L
  List<C> cs;
  static final Program emptyP=Program.flat(Program.emptyL);
  MetaError errName;
  MetaError errFail;
  MetaError errC;
  MetaError errM;
  LinkedHashSet<List<C>> allWatched;
  LinkedHashSet<List<C>> allHiddenSupertypes;
  C cOut;
  
  void allWatchedAbstractErr(List<C> cs0){
    if(!allWatched.contains(cs0)){return;}
    err(errFail,()->"The "+errFail.intro(cs0,false)
      +"can not be made abstract since is watched by "+errFail.intro(watchedBy(p.topCore(),cs0),false));
    }
  public Core.L apply(Program pOut,C cOut,Core.L l,List<Arrow>list,Function<L42£LazyMsg,L42Any>wrapName,Function<L42£LazyMsg,L42Any>wrapFail,Function<L42£LazyMsg,L42Any>wrapC,Function<L42£LazyMsg,L42Any>wrapM){
    this.p=pOut.push(cOut,l);
    this.cOut=cOut;
    this.errName=new MetaError(wrapName);
    this.errFail=new MetaError(wrapFail);
    this.errC=new MetaError(wrapC);
    this.errM=new MetaError(wrapM);
    this.map=new LinkedHashMap<Arrow,Arrow>();
    for(var a:list){
      var key=new Arrow(a.cs,a._s);
      if(map.containsKey(key)){err(errFail,"Rename map contains two entries for "+key.toStringErr());}
      map.put(new Arrow(a.cs,a._s),a);
      }    
    allWatched=Sum.allFromInfo(l,(c,li,csi)->{
      if(isDeleted(csi)){return;}
      for(var w:li.info().watched()){Sum.addPublicCsOfP(w,csi,c);}
      });
    allHiddenSupertypes=Sum.allFromInfo(l,(c,li,csi)->{
      if(isDeleted(csi)){return;}
      for(var w:li.info().hiddenSupertypes()){Sum.addPublicCsOfP(w,csi,c);}
      });
    cs=L();
    L res=applyMap();
    return res;
    }
  L applyMap(){
    earlyCheck();
    replaceEmpty();
    L l1=renameTop();
    L l2=lOfAddMap();
    return new Sum().compose(p.pop(),cOut,l1,l2,errC,errM);
    }
  void replaceEmpty(){
    //TODO; we really need to compute the used ns globally and to normalize them over library reuse and end of topNC
    for(Arrow a:map.values()){
      if(!a.full || !a.isEmpty()){continue;}
      if(a._s!=null){
        int n=firstPrivateOf(p._ofCore(a.cs));
        a._cs=a.cs;
        a._sOut=a._s.withUniqueNum(n);
        continue;
        }
      var popped=a.cs.subList(0,a.cs.size()-1);
      int n=firstPrivateOf(p._ofCore(popped));
      C top=a.cs.get(a.cs.size()-1);
      a._cs=pushL(popped,top.withUniqueNum(n));
      }
    }
  int firstPrivateOf(L l){
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
    allBusyUpTo+=1;
    while(existingNs.contains(allBusyUpTo)){allBusyUpTo+=1;}
    existingNs.add(allBusyUpTo);
    return allBusyUpTo;
    }
  List<C> culpritOf(L l,List<C> cs,Function<L,List<P.NCs>>f){//uses the map
    return Sum.culpritFromInfo(l,(li,csi)->{
      if(isDeleted(csi)){return null;}
      for(var w:f.apply(li)){
        if(cs.equals(Sum._publicCsOfP(w, csi))){return csi;}        
        }
      return null;
      });
    }
  List<C> watchedBy(L l,List<C> cs){return culpritOf(l,cs,li->li.info().watched());}
  List<C> hiddenBy(L l,List<C> cs){return culpritOf(l,cs,li->li.info().hiddenSupertypes());}
  boolean isDeleted(List<C>csi){
    var a=map.get(new Arrow(csi,null));
    return a!=null && (a.isEmpty() || a.isP());
    }
  String mapToS(){
    return map.values().stream().map(e->e.toStringErr()).collect(Collectors.joining(";"));
    }
  Error err(MetaError err,String s){throw err(err, ()->s);}
  Error err(MetaError err,Supplier<String> ss){
    throw err.throwErr(p.topCore(),()->{
      String s=ss.get();
      if(!s.endsWith("\n")){s=s+"\n";}
      s+="Full mapping:"+mapToS();
      return s;
      });
    }
  void earlyCheck(){
    HashSet<List<C>> domCodom=new HashSet<>();
    HashSet<List<C>> codCs=new HashSet<>();
    HashSet<Arrow> codMeth=new HashSet<>();
    for(var a:map.values()){
      if(a._s==null){domCodom.add(a.cs);}
      if(a.isMeth()){
        var k=new Arrow(a._cs,a._sOut);
        if(codMeth.contains(k)){err(errFail,"Rename can not map two methods on the same method: "+errFail.intro(k.cs,k._s));}
        codMeth.add(k);
        }
      if(a.isCs()){
        domCodom.add(a._cs);
        if(codCs.contains(a._cs)){
          err(errFail,"Two different nested class are renamed into "+errFail.intro(a._cs,false));
          }
        codCs.add(a._cs);
        }
      earlyCheck(a);
      }
    assert !domCodom.contains(null);
    for(var a:map.values()){
      if(a.isMeth()){
        assert a.cs.equals(a._cs);
        if(domCodom.contains(a.cs)){
          err(errFail,errFail.intro(a.cs,false)+"is already involved in the rename; thus "+errFail.intro(a.cs,a._s)+"can not be renamed");
          }
        }
      }
    }
  void earlyCheck(Arrow that){
    L l=p._ofCore(that.cs);
    if(l==null){err(errName,errName.intro(that.cs,false)+"does not exists");}
    MWT mwt=null;
    if(that._s!=null){
      mwt=_elem(l.mwts(),that._s);
      if(mwt==null){err(errName,errName.intro(that.cs,that._s)+"does not exists");}
      }
    earlyCheckNoUniqueNum(that);
    if(that.isP()){earlyCheckP(that, l);}
    if(that._s!=null){
      if(!that.isEmpty() &&!that.isMeth()){
        err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a method into a nested class");
        }
      if(that.isMeth() && that._sOut.equals(that._s)){
        err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a method on itself");
        }
      }
    else{
      if(that.isMeth()){err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a nested class into a method");}
      if(that.isCs() && that._cs.equals(that.cs)){
        err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a nested class on itself");
        }
      }
    if(!that.full && that._s==null){allWatchedAbstractErr(that.cs);}
    if(that._s!=null && !that.isEmpty()){
      assert that.isMeth();
      if(!that._cs.equals(that.cs)){
        err(errFail,"methods can only be renamed from inside the same nested class, but the following mapping is present: "+that.toStringErr()+"\n");
        }
      if(that._s.xs().size()!=that._sOut.xs().size()){
        err(errFail,"methods renames need to keep the same number of parameters, but the following mapping is present: "+that.toStringErr()+"\n");
        }
      }
    }
  private void earlyCheckNoUniqueNum(Arrow that) {
    boolean priv=that.cs.stream().anyMatch(c->c.hasUniqueNum());
    if(that._s!=null){priv&=that._s.hasUniqueNum();}
    if(that._sOut!=null){priv&=that._sOut.hasUniqueNum();}
    if(that._cs!=null){priv&=that._cs.stream().anyMatch(c->c.hasUniqueNum());}
    if(priv){err(errFail,"A mapping using unique numbers was attempted");}//Should it be prevented before
    }
  private void earlyCheckP(Arrow that, L l){
    if(allWatched.contains(that.cs)){
      err(errFail,"Redirected classes need to be fully abstract and not watched, but is watched by "+errFail.intro(watchedBy(p.topCore(), that.cs),false));
      }
    for(var mwt:l.mwts()){
      if(mwt.key().hasUniqueNum()){return;}
      if(mwt._e()!=null){
        err(errFail,"Redirected classes need to be fully abstract and not watched, but the following mapping is present: "+that.toStringErr()+"\nand "+errFail.intro(mwt,false)+"is implemented");
        }
      var ts=new ArrayList<T>();
      ts.add(mwt.mh().t());
      ts.addAll(mwt.mh().pars());
      ts.addAll(mwt.mh().exceptions());
      for(T t:ts){
        if(!t.p().isNCs()){continue;}
        var pi=emptyP.from(t.p().toNCs(),that.cs);
        if(pi.n()!=0){continue;}
        var arrow=new Arrow(pi.cs(),null);
        var a=map.get(arrow);
        if(a==null || !a.isP()){
          err(errFail,"Also "+errFail.intro(pi.cs(),false)+"need to be redirected to an outer path");
          }          
        }
      }
    }
  private List<C> addC(C c) {
    var old=this.cs;
    this.cs=pushL(this.cs,c);
    return old;
    }
  L lOfAddMap(){return lOfAddMapAux(addMapTop,L());}
  L lOfAddMapAux(L l,List<C> cs){//if A.B.C in the map, then A.B is also in the map, and a nested class for C=_ is present. It may point just to emptyL
    List<MWT> mwts=L(c->{
      c.addAll(l.mwts());
      c.addAll(addMapMWTs.getOrDefault(cs,L()));
      });
    List<NC> ncs=L(c->{
      c.addAll(l.ncs());
      for(NC nci:addMapNCs.getOrDefault(cs,L())){
        L li=lOfAddMapAux(nci.l(),pushL(cs,nci.key()));
        c.add(nci.withL(li));
        }
      });
    return l.withMwts(mwts).withNcs(ncs);
    }
  MWT mwtOf(MWT mwt,S s1){
    assert mwt.key().xs().size()==s1.xs().size();
    MH mh=mwt.mh().withS(s1);
    if(mwt._e()==null || mwt.key().xs().equals(s1.xs())){return mwt.withMh(mh);}
    HashMap<X,X> varMap=new HashMap<>();
    for(int i:range(s1.xs())){varMap.put(mwt.key().xs().get(i),s1.xs().get(i));}
    Core.E e=mwt._e().visitable().accept(new CloneVisitor(){
      public X visitX(X x){
        X xx=varMap.get(x);
        if(xx==null){return x;}
        return xx;
        }
      });
    return mwt.withMh(mh).with_e(e);
    }
  MWT renameUsages(MWT mwt){return mwt.accept(new CloneRenameUsages(this));}
  private final CloneVisitor simpleRename=new CloneVisitor(){
      @Override public P visitP(P path){
        return renamedPath(map,cs,p.navigate(cs),path);
        }
      @Override public S visitS(S s){
        Program p0=p.navigate(cs);
        for(var t:p0.topCore().ts()){
          L l=p0._ofCore(t.p());
          if(l.info().refined().contains(s)){continue;}
          if(_elem(l.mwts(),s)==null){continue;}
          return renamedS(map, cs, p0, t.p().toNCs(), s);
          }
        return s;
        }
      @Override public PathSel visitPathSel(PathSel s){
        Program p0=p.navigate(cs);
        var path=s.p().toNCs();
        P p2=renamedPath(map,cs,p0,path);
        if(s._s()==null){return s.withP(p2);}
        S s2=renamedS(map, cs, p0, path, s._s());
        return s.withP(p2).with_s(s2);
        }
      };
    List<T> renameUsagesTs(List<T> ts){return simpleRename.visitTs(ts);}
  List<Doc> renameUsagesDocs(List<Doc> docs){return simpleRename.visitDocs(docs);}
  Info renameUsagesInfo(boolean isInterface,List<MWT>mwts,Info info){
    Info res=info;
    if(isInterface && !res.close()){
      boolean priv=mwts.stream().anyMatch(m->m.key().hasUniqueNum());
      if(priv){res=res.withClose(true);}
      }
    ArrayList<P.NCs>newWatched=new ArrayList<>();
    for(var ps:res.usedMethods()){
      List<C> csi=Sum._publicCsOfP(ps.p(),cs);
      assert ps._s()!=null;
      if(csi==null){continue;}
      Arrow a=map.get(new Arrow(csi,ps._s()));
      if(a==null || !a.full){continue;}
      if(a._sOut.hasUniqueNum()){
        newWatched.add(ps.p().toNCs());
        }
      }
    for(var p:res.typeDep()){
      List<C> csi=Sum._publicCsOfP(p,cs);
      if(csi==null){continue;}
      Arrow a=map.get(new Arrow(csi,null));
      if(a==null || !a.full || a.isP()){continue;}
      int i=a._cs.size();
      if(i==0){continue;}
      if(!a._cs.get(i-1).hasUniqueNum()){continue;}
      if(p.cs().isEmpty()){newWatched.add(p.withN(p.n()+1));continue;}
      if(p.cs().size()==1 && p.n()==0){continue;}//do not watch This0
      //what happens if we was typeDep of This2 and This2 becomes private? we watch This3!
      newWatched.add(p.withCs(popLRight(p.cs())));
      }
    assert newWatched.stream().noneMatch(p->p.equals(P.pThis0) || p.hasUniqueNum()):newWatched;
    res=res.withWatched(mergeU(res.watched(),newWatched));
    var ums=res.usedMethods();
    ums=L(ums.stream().filter(ps->!newWatched.contains(ps.p())));
    res=res.withUsedMethods(ums);
    return res.accept(simpleRename);
    }
  L renameTop(){
    L l1=renameL(p.topCore());
    Arrow a=map.get(new Arrow(L(),null));
    if(a==null){return l1;}
    if(a.isMeth()){return l1;}
    if(!a.full){
      allWatchedAbstractErr(L());
      noExposeUniqueN(l1);
      }
    if(!a.full && a.isCs()){
      noCircular(l1,L());
      int n=a._cs.size();
      P.NCs src=P.of(n,L());
      C c1=freshC(p.pop().topCore().ncs(),0);
      L l=noNesteds(l1);
      l=fromAndPushThis0Out(forcedNavigate(p,a._cs),l,src,c1,true);
      int last=a._cs.size()-1;
      addMapNCs(a._cs.subList(0,last),new NC(L(),L(),a._cs.get(last),l));
      return toAbstract(l1);
      }
    if(!a.full && a.isEmpty()){return toAbstract(l1);}
    return l1;
    }
  static L fromAndPushThis0Out(Program prg,L l,P.NCs src,C c1,boolean removeC1){
    return (L)new From(prg,src,-1){
      @Override public P visitP(P p){
        if(!p.isNCs()){return super.visitP(p);}
        var pp=p.toNCs();
        if(pp.n()!=j()){return super.visitP(p);}
        pp=P.of(pp.n()+1,pushL(c1,pp.cs()));
        pp=(P.NCs)super.visitP(pp);
        if(!removeC1){return pp;}
        return program().minimize(P.of(pp.n()-1,popL(pp.cs())));
        }
      }.visitL(l);
    }
  C freshC(List<NC> ncs,int num){
    C res=new C("Fresh"+num,-1);
    if(ncs.stream().noneMatch(e->e.key().equals(res))){return res;}
    return freshC(ncs,num+1);
    }
  L renameL(L l){
    assert p._ofCore(cs)==l;
    var mwts1=renameMWTs(l.mwts());
    var ncs1=renameNCs(l.ncs());
    List<T> ts1=renameUsagesTs(l.ts());
    Info info1=renameUsagesInfo(l.isInterface(),mwts1,l.info());
    List<Doc> docs1=renameUsagesDocs(l.docs());
    return new L(l.poss(),l.isInterface(),ts1,mwts1,ncs1,info1,docs1);    
    }
  List<NC> renameNCs(List<NC> ncs){return L(ncs,(c,nci)->{
    var oldCs=this.addC(nci.key());
    L li=this.renameL(nci.l());
    this.cs=oldCs;
    List<Doc> docs=renameUsagesDocs(nci.docs());
    c.addAll(renameNC(nci.withL(li).withDocs(docs)));    
    });}
  List<MWT> renameMWTs(List<MWT> mwts){return L(mwts,(c,mwti)->{
    var mwt=renameUsages(mwti);
    c.addAll(renameMWT(mwt));
    });}
  List<NC> renameNC(NC nc){
    Arrow a=map.get(new Arrow(pushL(cs,nc.key()),null));
    if(a==null){return L(nc);}
    List<C> csc=pushL(cs,nc.key());
    if(!a.full){//either 7 or 8
      if(allWatched.contains(csc)){err(errFail,errFail.intro(csc,false)
        +"The implementation can not be removed since the class is watched by "
        +errFail.intro(watchedBy(p.topCore(),csc),false));}
      if(a.isEmpty()){return L(rename8restrictNC(nc));}
      return L(rename7superNC(nc,csc,a));
      }
    if(a.isP()){
      NC res=_rename11reidrectNested(nc,a);
      if(res==null){return L();}
      return L(res);
      }
    assert a.isCs();
    int size=a._cs.size();
    var hide=size!=0 && a._cs.get(size-1).hasUniqueNum();
    if(hide){return rename10hideNested(nc,a);}
    NC res=_rename9nested(nc,a);
    if(res==null){return L();}
    return L(res);
    }
  List<MWT> renameMWT(MWT mwt){
    Arrow e=map.get(new Arrow(cs,mwt.key()));
    if(e==null){return notDirectlyRenamed(mwt);}
    if(!e.full){
      if(mwt._e()==null){err(errFail,errFail.intro(cs,mwt.key())+"is already abstract");}
      if(e.isEmpty()){return L(rename3restrictMeth(mwt));}
      assert e.isMeth();
      return L(rename4superMeth(mwt,e._sOut));
      }
    assert e.isMeth();
    L l=p._ofCore(e.cs);
    assert l!=null;
    if(l.info().refined().contains(mwt.key())){
      err(errFail,"refined method "+errFail.intro(cs,mwt.key())+"can not be directly renamed");
      }
    if(l.isInterface()){
      if(e._sOut.hasUniqueNum()){return L(rename1hideInterfaceMeth(l,mwt,e._sOut));}
      return rename2interfaceMeth(mwt,e._sOut);
      }
    if(e._sOut.hasUniqueNum()){return L(rename6hideMeth(mwt,e._sOut));}
    return rename5meth(mwt,e._sOut);
    }
  List<MWT> notDirectlyRenamed(MWT mwt){
    L l=p._ofCore(cs);
    assert l!=null;
    for(T t:l.ts()){
      List<C> cs0=Sum._publicCsOfP(t.p(), cs);
      if(cs0==null){continue;}
      var a=map.get(new Arrow(cs0,mwt.key()));
      if(a==null){continue;}
      if(a._sOut.hasUniqueNum()){return L(rename1hideInterfaceMeth(l,mwt, a._sOut));}
      return rename2interfaceMeth(mwt,a._sOut);
      }
    return L(mwt);
    }
  MWT rename1hideInterfaceMeth(L l,MWT mwt,S s1){
    if(allHiddenSupertypes.contains(cs)){
      err(errFail,()->"The "+errFail.intro(cs,mwt.key())
          +"can not be made private since is implemented by private parts of "
          +errFail.intro(hiddenBy(p.topCore(),cs),false));
        }
    if(!l.isInterface() && mwt._e()==null){
      err(errFail,errFail.intro(cs,mwt.key())+"is abstract, thus it can not be hidden");
      }
    return mwtOf(mwt,s1);
    }
  List<MWT> rename2interfaceMeth(MWT mwt,S s1){
    if(allHiddenSupertypes.contains(cs)){
      err(errFail,()->"The "+errFail.intro(cs,mwt.key())
          +"can not be renamed since is implemented by private parts of "
          +errFail.intro(hiddenBy(p.topCore(),cs),false));
        }
    addMapMWTs(mwtOf(mwt,s1));
    return L();
    }
  MWT rename3restrictMeth(MWT mwt){return mwt.with_e(null);}
  MWT rename4superMeth(MWT mwt,S s1){
    addMapMWTs(mwtOf(mwt,s1));
    return mwt.with_e(null);
    }
  List<MWT> rename5meth(MWT mwt,S s1){
    addMapMWTs(mwtOf(mwt,s1));
    return L();
    }
  MWT rename6hideMeth(MWT mwt,S s1){
    if(mwt._e()==null){err(errFail,errFail.intro(cs,mwt.key())+"is abstract, thus it can not be hidden");}
    return mwtOf(mwt,s1);    
    }
  NC rename7superNC(NC nc,List<C> csc,Arrow a){
    noCircular(nc.l(),csc);
    noExposeUniqueN(nc.l());
    if(a._cs.isEmpty()){
      L l1=nc.l();
      l1=noNesteds(l1);
      l1=fromAndPushThis0Out(emptyP,l1,P.of(0,a.cs),nc.key(),false);
      //l1=pushThis0Out(l1,nc.key());
      //l1=(L)new From(emptyP,P.of(0,a.cs),-1).visitL(l1);
      addMapTop=l1;
      }
    else{
      var cs1=popL(a._cs);
      int n=cs1.size();
      Program p=forcedNavigate(this.p,cs1);
      L l1=nc.l();
      l1=noNesteds(l1);
      l1=fromAndPushThis0Out(p,l1,p.minimize(P.of(n,cs)),nc.key(),false);
      //l1=pushThis0Out(l1,nc.key());
      //l1=(L)p.from(l1,p.minimize(P.of(n,cs)));//Note: now is instead doing the new From(_ _ -1)
      NC newNC=new NC(nc.poss(),L(),a._cs.get(n),l1);
      addMapNCs(cs1,newNC);
      }
    return nc.withL(toAbstract(nc.l()));
    }
  NC rename8restrictNC(NC nc){
    noExposeUniqueN(nc.l());
    return nc.withL(toAbstract(nc.l()));
    }
  NC _rename9nested(NC nc,Arrow a){
    int n=cs.size();
    int last=a._cs.size()-1;
    var cs1=a._cs.subList(0, last);
    Program p=forcedNavigate(this.p,cs1);
    P.NCs src=p.minimize(P.of(n,cs));
    L l=noNesteds(nc.l());
    l=fromAndPushThis0Out(p,l,src,nc.key(),false);
    var docs=p.fromDocs(nc.docs(),src);
    addMapNCs(cs1, new NC(nc.poss(),docs,a._cs.get(last),l));
    return _onlyNested(nc);
    }
  List<NC> rename10hideNested(NC nc,Arrow a){
    var prv=nc.l().mwts().stream().anyMatch(m->!m.key().hasUniqueNum());
    if(prv){err(errFail,()->errFail.intro(pushL(cs,nc.key()),false)
      +"can not be hidden since some methods are still public:\n"
      +nc.l().mwts().stream().filter(m->!m.key().hasUniqueNum())
      .map(m->errFail.intro(pushL(cs,nc.key()),m.key())).collect(Collectors.joining()));}
    var nc1=_onlyNested(nc);
    var l2=noNesteds(nc.l());
    l2=pushThis0Out(l2, nc.key());
    int last=a._cs.size()-1;
    var nc2=new NC(nc.poss(),nc.docs(),a._cs.get(last),l2);
    if(nc1==null){return L(nc2);}
    return List.of(nc1,nc2);
    }

   NC _rename11reidrectNested(NC nc,Arrow a){
    assert !allWatched.contains(a.cs);
    if(nc.l().mwts().isEmpty()){return _onlyNested(nc);}
    if(!a._path.isNCs()){errRedirect(a.cs, a._path,nc.l().mwts().get(0),"the method does not exists");}
    P.NCs path=a._path.toNCs();
    path=path.withN(path.n()+1);
    Program p=this.p.navigate(a.cs);
    L l=this.p._ofCore(path);
    path=P.of(path.n()+a.cs.size(),path.cs());
    for(MWT mwt:nc.l().mwts()){
      MWT mwtOut=_elem(l.mwts(),mwt.key());
      if(mwtOut==null){errRedirect(a.cs,a._path,mwt,"the method does not exists");}
      var mhOut=p.from(mwtOut.mh(),path);
      String msg=ProgramTypeSystem._typeMHSubtypeErrMsg(p,mwt.mh(),mhOut);
      if(msg!=null){errRedirect(a.cs,a._path,mwt,msg);} 
      }
    return _onlyNested(nc);
    }
  void errRedirect(List<C> cs,P path,MWT mwt,String extraMsg){
    err(errFail,errFail.intro(cs,false)+"can not be redirected, the target "+errFail.intro(path, false)
      +"does not expose a compatible method "+errFail.intro(cs,mwt.key())
      +extraMsg);
    }

  L toAbstract(L l0){
    List<MWT> mwts=L(l0.mwts(),(c,m)->{
      if(m.key().hasUniqueNum()){return;}
      c.add(m.with_e(null));
      });
    List<NC> ncs=L(l0.ncs(),(c,n)->{
      if(n.key().hasUniqueNum()){return;}
      c.add(n);
      });
    L l=new L(l0.poss(),l0.isInterface(),l0.ts(),mwts,L(),Info.empty,l0.docs());
    List<P.NCs> typeDep=L(c->l.accept(new Accumulate<Void>(){
      @Override public void visitP(P p){
        if(p.isNCs() && !c.contains(p)){c.add(p.toNCs());}
        }
      }));
    Info i=l0.info();    
    i=new Info(i.isTyped(),typeDep,L(),L(),L(),L(),L(),i.refined(),false, "",L(), -1);
    return l.withNcs(ncs).withInfo(i);
    } 
  Program forcedNavigate(Program p,List<C> cs){
    for(C c:cs){p=p.push(c,Program.emptyL);}
    return p;
    }
  void noCircular(L l,List<C>cs0){
    for(var pi:l.info().typeDep()){
      var csi=Sum._publicCsOfP(pi, cs0);
      if(csi==null){continue;}
      L li=p._ofCore(csi);
      if(csi.equals(cs0)){continue;}
      if(li==null){continue;}
      for(var pj:li.info().typeDep()){
        var csj=Sum._publicCsOfP(pj, csi);
        if(!cs0.equals(csj)){continue;}//covers also the csj==null case
        err(errFail,errFail.intro(cs0,false)
          +"Code can not be extracted since is circularly depended from "
          +errFail.intro(csi,false));
        }
      }
    }
  void noExposeUniqueN(L l){
    l.accept(new Accumulate<Void>(){
      @Override public void visitMWT(MWT mwt){
        super.visitMWT(mwt.with_e(null));
        }
      @Override public void visitNC(NC nc){}
      @Override public void visitP(P path){
        if(!path.isNCs()){return;}
        var p=path.toNCs();
        if(p.n()!=0 || p.cs().isEmpty()){return;}
        if(!p.cs().get(0).hasUniqueNum()){return;}
        err(errFail,errFail.intro(cs,false)
          +"Code can not be extracted since it exposes uniquely numbered path "
          +errFail.intro(p.cs(),false));
        }
      });
    }
  NC _onlyNested(NC nc){
    List<NC> ncs=L(nc.l().ncs(),(c,nci)->{
      if(!nci.key().hasUniqueNum()){c.add(nci);}
      });
    if(ncs.isEmpty()){return null;}
    L l=Program.emptyL.withNcs(ncs).withPoss(nc.poss());//purposely trashing the pos of the L inside the nc
    return nc.withL(l).withDocs(L());
    }
  L noNesteds(L l){
    List<NC> ncs=L(l.ncs(),(c,nci)->{
      if(nci.key().hasUniqueNum()){c.add(nci);}
      });
    return l.withNcs(ncs);
    }
  
  abstract class TweakPs extends CloneVisitor{
    int level=-1;
    @Override public L visitL(L l){
      level+=1;
      L res=super.visitL(l);
      level-=1;
      return res;
      }
    @Override public P visitP(P p){
      if(!p.isNCs()){return p;}
      var pp=p.toNCs();
      if(pp.cs().isEmpty()){return p;}
      C c0=pp.cs().get(0);
      if(c0.hasUniqueNum()){return p;}
      return tweakP(pp);
      }
    abstract P tweakP(P.NCs p);
    }  
  L pushThis0Out(L l,C c){return l.accept(new TweakPs(){    
    P tweakP(P.NCs p){
      if(p.n()!=level){return p;}
      return P.of(level+1,pushL(c,p.cs()));
      }});
    }
  static P renamedPath(LinkedHashMap<Arrow,Arrow> map,List<? extends LDom> whereFromTop,Program p,P path){
    int nesting=whereFromTop.size();
    if(!path.isNCs()){return path;}
    List<C> currentP=_topCs(whereFromTop,path.toNCs());
    if(currentP==null){return path;}
    Arrow a=map.get(new Arrow(currentP,null));
    if(a==null || !a.full){return path;}
    if(a.isCs()){return p.minimize(P.of(nesting,a._cs));}
    assert a.isP();
    if(!a._path.isNCs()){return a._path;}
    var res=a._path.toNCs();
    res=res.withN(nesting+res.n()+1);//because destination is relative to outside pStart.top
    assert p.minimize(res)==res;
    return res;
    }
  static S renamedS(LinkedHashMap<Arrow,Arrow> map,List<? extends LDom> whereFromTop,Program p,P.NCs path,S s){
    List<C> currentP=_topCs(whereFromTop,path);
    if(currentP==null){return s;}
    Arrow a=map.get(new Arrow(currentP,s));
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
  static class CloneRenameUsages extends CloneVisitorWithProgram.WithG{
    CloneRenameUsages(Rename r){
      super(r.p.navigate(r.cs),G.empty());
      this.r=r;
      this.whereFromTop().addAll(r.cs);
      }
    Rename r;
    @Override public P visitP(P path){return renamedPath(r.map,whereFromTop(),this.p(),path);}
    @Override public MCall visitMCall(MCall mcall){
      mcall=super.visitMCall(mcall);
      var t=g._of(mcall.xP());
      if(t==null){return mcall;}
      var path=t.p();
      if(!path.isNCs()){return mcall;}
      var s2=renamedS(r.map,whereFromTop(),this.p(),path.toNCs(),mcall.s());
      return mcall.withS(s2);    
      }
    }
  }
  /*
   * 
   *   class NoCircular{
    void onErr(L l,List<C>cs0){
      err(errFail,()->{
        NoCircularErr e=new NoCircularErr();
        e.noCircular(l,cs0);
        return errFail.intro(cs0,false)+"Code can not be extracted since is circularly depended from\n"+e.error;
        });
      }
    void noCircular(L l,List<C>cs0){
      var visited=new HashSet<List<C>>();
      for(var pi:l.info().typeDep()){
        var cs2=Sum._publicCsOfP(pi, cs0);
        if(cs2.equals(cs0) || !circular(cs2,visited,cs0)){continue;}
        onErr(l,cs0);
        return; 
        }
      }
    boolean circular(List<C> cs1,HashSet<List<C>>visited,List<C> cs0){
      if(cs1.equals(cs0)){return true;}
      if(visited.contains(cs1)){return false;}
      visited.add(cs1);
      L l=p._ofCore(cs1);
      if(l==null){return false;}
      for(var pi:l.info().typeDep()){
        var cs2=Sum._publicCsOfP(pi, cs1);
        if(circular(cs2,visited,cs0)){return true;}
        }
      return false;
      }
    }
  class NoCircularErr extends NoCircular{
    StringBuilder error=new StringBuilder();
    List<List<C>> visitOrder=new ArrayList<>();
    @Override void onErr(L l,List<C>cs0){}
    @Override boolean circular(List<C> cs1,HashSet<List<C>>visited,List<C> cs0){
      visitOrder.add(cs1);
      var res=super.circular(cs1,visited,cs0);
      visitOrder.remove(visitOrder.size()-1);
      if(error.length()!=0 || !res){return res;}
      for(var csi:visitOrder){
        String si=csi.stream().map(c->c.toString()).collect(Collectors.joining("."));
        error.append(si+"\n");
        }
      return true;
      }
    } 

  
  
  */