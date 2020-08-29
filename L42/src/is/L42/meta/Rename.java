package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.checkNoException;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import is.L42.common.From;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
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
import is.L42.top.Deps;
import is.L42.top.UniqueNsRefresher;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.Accumulate;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;
import is.L42.visitors.WellFormedness;

public class Rename {
  HashMap<List<C>,List<MWT>> addMapMWTs=new HashMap<>();//mutable, so should be ArrayList, but need List for the default emptyList
  HashMap<List<C>,List<NC>> addMapNCs=new HashMap<>();
  public Rename(UniqueNsRefresher fresh){this.fresh=fresh;}
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
  final UniqueNsRefresher fresh;
  Program p;//includes the top level L
  List<Arrow>list;
  List<C> cs;
  MetaError errName;
  MetaError errFail;
  MetaError errC;
  MetaError errM;
  LinkedHashSet<List<C>> allWatched;
  LinkedHashSet<List<C>> allHiddenSupertypes;
  C cOut;
  
  void makeAbstractOk(L _l,List<C> cs){
    if(allWatched.contains(cs)){err(errFail,errFail.intro(cs,false)
      +"The implementation can not be removed since the class is watched by "
      +errFail.intro(watchedBy(p.topCore(),cs),false));}
    if(_l!=null && _l.isInterface()){err(errFail,errFail.intro(cs,false)
      +"The implementation can not be removed since the class is an interface");}
    Sum.openImplements(p.navigate(cs),
      s->err(errFail,errFail.intro(cs,false)+"The implementation can not be removed; "+s));
    }
  private boolean deepCheckInfo(Program p,Core.L l){
    l.visitInnerL((li,csi)->{
      assert checkNoException(()->WellFormedness.checkInfo(p.navigate(csi),li)): ""+li;
      });
    return true;
    }
  public Core.L apply(Program pOut,C cOut,Core.L l,List<Arrow>list,Function<L42£LazyMsg,L42Any>wrapName,Function<L42£LazyMsg,L42Any>wrapFail,Function<L42£LazyMsg,L42Any>wrapC,Function<L42£LazyMsg,L42Any>wrapM){
    this.list=list;
    this.p=pOut.push(cOut,l);
    this.cOut=cOut;
    this.errName=new MetaError(wrapName);
    this.errFail=new MetaError(wrapFail);
    this.errC=new MetaError(wrapC);
    this.errM=new MetaError(wrapM);
    this.map=new LinkedHashMap<>();
    assert l.wf();
    assert deepCheckInfo(p,l);
    assert WellFormedness.allMinimized(pOut,cOut,l);
    for(var a:list){miniAddMap(new Arrow(a.cs,a._s),a);}    
    cs=L();
    L res=applyMap();
    assert res.wf();
    assert WellFormedness.allMinimized(pOut,cOut,res);
    assert deepCheckInfo(pOut.push(cOut,res),res);
    return res;
    }
  L applyMap(){
    extendMap();
    allWatched=Sum.allFromInfo(p.topCore(),(c,li,csi)->{
      if(isDeleted(csi)){return;}
      for(var w:li.info().watched()){Sum.addPublicCsOfP(w,csi,c);}
      });
    allHiddenSupertypes=Sum.allFromInfo(p.topCore(),(c,li,csi)->{
      if(isDeleted(csi)){return;}
      for(var w:li.info().hiddenSupertypes()){Sum.addPublicCsOfP(w,csi,c);}
      });
    earlyCheck();
    completeMap();
    L l1=renameTop();
    L l2=lOfAddMap();
    return new Sum().compose(true,p.pop(),cOut,l1,l2,errC,errM);
    }
  void miniAddMap(Arrow key,Arrow val){
    if(map.containsKey(key)){
      err(errFail,"Rename map contains two entries for "+key.toStringKey());
      }
    map.put(key,val);
    }
  void addedArrow(Arrow a,Arrow src,List<C>csi,L li,Map<Arrow,Arrow> map){
    if(!a.full && li.isInterface() && a._cs!=null){assert a._path==null;return;}
    assert a._s==null && a._sOut==null;
    if(csi.isEmpty() && !a.isEmpty()){//either isCs or isP, if csi is empty, is simple
      miniAddMap(src,new Arrow(src.cs,null,a.full,false,a._path,a._cs,null));
      return;
      }
    if(a.isCs()){
      miniAddMap(src,new Arrow(src.cs,null,a.full,false,null,merge(a._cs,csi),null));
      return;
      }
    if(a._path!=null){
      assert a.full && a._cs==null:a;
      if(!a._path.isNCs()){err(errFail,"mapping: "+a.toStringErr()+"\n"
        +errFail.intro(merge(a.cs,csi),false)
        +"can not be redirected on a nested of "+a._path);}
      var p=a._path.toNCs();
      p=p.withCs(merge(p.cs(),csi));
      miniAddMap(src,new Arrow(src.cs,null,a.full,false,p,null,null));
      return;
      }
    assert a.isEmpty();
    miniAddMap(src,new Arrow(src.cs,null,a.full,false,null,null,null));
    if(!a.full){return;}
    for(var m:li.mwts()){
      if(m.key().hasUniqueNum()){continue;}
      if(li.info().refined().contains(m.key())){continue;}
      miniAddMap(new Arrow(src.cs,m.key()),new Arrow(src.cs,m.key(),a.full,false,null,null,null));
      }
    }
  void extendMap(){
    var old=map;
    map=new LinkedHashMap<>();
    for(var e:old.entrySet()){
      Arrow a=e.getValue();
      if(a.star && a._s!=null){err(errFail,"transitive rename only applicable on nested classes");}
      if(!a.star){miniAddMap(e.getKey(),a);continue;}
      var lcs=p._ofCore(a.cs);
      if(lcs==null){err(errName,errName.intro(a.cs,false)+"does not exists");}
      lcs.visitInnerLNoPrivate((li,csi)->{
        var src=new Arrow(merge(a.cs,csi),null);
        addedArrow(a,src,csi,li,map);
        });
      }
    }
  int firstPrivateOf(IdentityHashMap<Core.L,Integer>map,Core.L l,LDom lDom){
    return map.computeIfAbsent(l,l0->fresh.firstPrivateOf(l0,lDom));
    }
  void completeMap(){
    var assignedPrivates=new IdentityHashMap<Core.L,Integer>();
    for(Arrow a:map.values()){
      if(!a.full || !a.isEmpty()){continue;}
      if(a._s!=null){
        int n=firstPrivateOf(assignedPrivates,p._ofCore(a.cs),a._s);
        a._cs=a.cs;
        a._sOut=a._s.withUniqueNum(n);
        continue;
        }
      var popped=a.cs.subList(0,a.cs.size()-1);
      C top=a.cs.get(a.cs.size()-1);
      int n=firstPrivateOf(assignedPrivates,p._ofCore(popped),top);
      a._cs=pushL(popped,top.withUniqueNum(n));
      }
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
    return a!=null && ((!a.full && a.isEmpty() )|| a.isP());
    }
  String mapToS(){return list.stream().map(e->e.toStringErr()).collect(Collectors.joining(";"));}
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
        if(codMeth.contains(k)){err(errFail,"Two different methods are renamed into "+errFail.intro(k.cs,k._s));}
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
      L l=p._ofCore(a.cs);
      if(!a.isMeth()){continue;}
      assert a.cs.equals(a._cs);
      if(domCodom.contains(a.cs)){errAlreadyInvolved(a, a.cs,errFail.intro(a.cs,a._s));}
      assert l!=null;
      if(!l.isInterface()){continue;}
      for(var cs:domCodom){
        L li=p._ofCore(cs);
        if(li==null){continue;}
        for(T t:li.ts()){
          List<C> cs0=Sum._publicCsOfP(t.p(), cs);
          if(!cs0.equals(a.cs)){continue;}
          err(errFail,errFail.intro(cs,false)+"is already involved in the rename; thus "
            +errFail.intro(a.cs,a._s)+"can not be renamed: is an interface method refined by such nested class");
          }
        }
      }
    }
  void errAlreadyInvolved(Arrow a,List<C>involved,String elem){
    err(errFail,"mapping: "+a.toStringErr()+"\n"+errFail.intro(involved,false)
      +"is already involved in the rename; thus "+elem+"can not be renamed");
    }
  void earlyCheck(Arrow that){
    earlyCheckNoUniqueNum(that);
    L l=p._ofCore(that.cs);
    if(l==null){err(errName,errName.intro(that.cs,false)+"does not exists");}
    if(that._s!=null){earlyCheckHasS(that,l);return;}
    if(that.isMeth()){err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a nested class into a method");}
    if(that.isCs() && that._cs.equals(that.cs)){
      err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a nested class on itself");
      }
    if(that.full && that.cs.isEmpty()){
      if(that.isP()){err(errFail,"'This' can not be redirected away");}
      if(that.isEmpty()){err(errFail,"'This' can not be hidden");}
      }
    if(that.isP()){earlyCheckPOne(that.cs, l);return;}
    if(!that.full){
      if(that.isEmpty()){makeAbstractOk(null,that.cs);return;}//allow to abstractify interface
      makeAbstractOk(l,that.cs);//but not to move implementation out
      assert that._cs!=null;
      var l_cs=p._ofCore(that._cs);
      if(l_cs!=null && l_cs.isInterface()){err(errFail,"Implementation can not be moved onto an interface");}
      return;
      }
    if(that.isEmpty()){return;}
    var l_cs=p._ofCore(that._cs);
    if(l_cs==null){return;}
    if(l_cs.isInterface()!=l.isInterface()){
      var info=errFail.intro(l_cs.isInterface()?that.cs:that._cs,false);
      err(errFail,info+"can not be turned into an interface inside of a rename operation");}
    if(!l_cs.isInterface()){return;}
    var lBig=Sum.moreThen(l, l_cs);
    var l_csBig=Sum.moreThen(l_cs,l);
    if(!lBig && !l_csBig){return;}
    var info=errFail.intro(lBig?that._cs:that.cs,false);
    err(errFail,info+"can not grow inside of a rename operation");
    }
  private void earlyCheckHasS(Arrow that, L l) {
    MWT mwt=_elem(l.mwts(),that._s);
    if(mwt==null){err(errName,errName.intro(that.cs,that._s)+"does not exists");}
    if(!that.isEmpty() &&!that.isMeth()){
      err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a method into a nested class");
      }
    if(that.isMeth() && that._sOut.equals(that._s)){
      err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a method on itself");
      }
    if(!that.isEmpty()){
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
  private void earlyCheckPOne(List<C> cs, L l){
    makeAbstractOk(null,cs);
    for(var mwt:l.mwts()){
      if(mwt.key().hasUniqueNum()){return;}
      if(mwt._e()!=null){
        err(errFail,errFail.intro(cs,false)+
          "Redirected classes need to be fully abstract and "+errFail.intro(mwt,false)+"is implemented");
        }
      earlyCheckPType(cs,mwt.mh().t());
      for(T t:mwt.mh().pars()){earlyCheckPType(cs,t);}
      for(T t:mwt.mh().exceptions()){earlyCheckPType(cs,t);}
      }
    }
  private void earlyCheckPType(List<C> cs,T t){
    if(!t.p().isNCs()){return;}
    var pi=Program.emptyP.from(t.p().toNCs(),cs);
    if(pi.n()!=0){return;}
    var arrow=new Arrow(pi.cs(),null);
    var a=map.get(arrow);
    if(a!=null && a.isP()){return;}
    err(errFail,"Also "+errFail.intro(pi.cs(),false)+"need to be redirected to an outer path");
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
    Deps deps=new Deps();
    for(var nc:ncs){deps.collectDocs(forcedNavigate(p, cs),nc.docs());}
    Info i=l.info().sumInfo(deps.toInfo(true));
    return l.withMwts(mwts).withNcs(ncs).withInfo(i);
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
    assert e!=null;
    return mwt.withMh(mh).with_e(e);
    }
  L renameTop(){
    L l1=renameL(p.topCore());
    Arrow a=map.get(new Arrow(L(),null));
    if(a==null){return l1;}
    if(a.isMeth()){return l1;}
    if(!a.full){noExposeUniqueN(L(),l1);}
    if(!a.full && a.isEmpty()){return toAbstract(l1);}
    assert a.isCs();
    if(!a.full){noCircular(l1,L());}
    P.NCs src=P.of(a._cs.size(),L());
    L l=noNesteds(l1);
    l=fromAndPushThis0Out(forcedNavigate(p,a._cs),l,src);
    int last=a._cs.size()-1;
    addMapNCs(a._cs.subList(0,last),new NC(L(),L(),a._cs.get(last),l));
    if(!a.full){return toAbstract(l1);}
    return onlyNesteds(l1);
    //TODO: discuss, should the label by #typed, #norm or something else?
    }
  static L fromAndPushThis0Out(Program prg,L l,P.NCs src){
    return new From(prg,src,0){//0+start since p is placed in the new l position
      L start(L l){return superVisitL(l);}
      Program forAssert(){
        Program res=forcedNavigate(program().pop(j()+src.n()),src.cs());
        List<C> path=program().path();
        int i=path.size();
        path=path.subList(i-j(),i);
        return forcedNavigate(res,path);
        }
      @Override public Info visitInfo(Info i){
        assert program().dept()-j()==prg.dept();
        assert WellFormedness.coherentInfo(forAssert(), i);
        Info i0=super.visitInfo(i);
        assert WellFormedness.coherentInfo(program(), i0);
        return i0;        
        }
      @Override public P visitP(P p){
        if(!p.isNCs()){return p;}
        var pp=p.toNCs();
        int n=pp.n();
        if(n<j()){return p;}
        if(n==j()){
          if(pp.cs().isEmpty() || pp.cs().get(0).hasUniqueNum()){return p;}
          pp=prg.from(pp.withN(0),src);
          return program().minimize(pp.withN(pp.n()+j()));
          }
        pp=prg.from(pp.withN(n-j()),src);//note, correctly prg~=~ progam().pop(j())
        return program().minimize(pp.withN(pp.n()+j()));
        }
      @Override public List<P.NCs> visitInfoWatched(List<P.NCs> ps){
        return L(ps,(c,p)->{
          var pp=this.visitP(p);
          if(!pp.isNCs()||c.contains(pp)||pp.equals(P.pThis0)){return;}
          c.add(pp.toNCs());
          });
        }
      }.start(l);
    }
  L renameL(L l){return new CloneRenameUsages(this).visitL(l);}
  List<NC> renameNCs(CloneRenameUsages r,List<NC> ncs){return L(ncs,(c,nci)->{
    var oldCs=this.addC(nci.key());
    L li=this.renameL(nci.l());
    this.cs=oldCs;
    List<Doc> docs=r.visitDocs(nci.docs());
    c.addAll(renameNC(nci.withL(li).withDocs(docs)));    
    });}
  List<NC> renameNC(NC nc){
    Arrow a=map.get(new Arrow(pushL(cs,nc.key()),null));
    if(a==null){return L(nc);}
    List<C> csc=pushL(cs,nc.key());
    if(!a.full){//either 7 or 8
      if(a.isEmpty()){return L(rename8restrictNC(csc,nc));}
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
      if(mwt._e()==null){
        err(errFail,errFail.intro(cs,mwt.key())+"is already abstract");}
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
      assert a.full && a._sOut!=null: a;//if there is a rename, is a rename =>
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
  MWT rename3restrictMeth(MWT mwt){return mwt.with_e(null).withNativeUrl("");}
  MWT rename4superMeth(MWT mwt,S s1){
    addMapMWTs(mwtOf(mwt,s1));
    return mwt.with_e(null).withNativeUrl("");
    }
  List<MWT> rename5meth(MWT mwt,S s1){
    addMapMWTs(mwtOf(mwt,s1));
    return L();
    }
  MWT rename6hideMeth(MWT mwt,S s1){
    if(mwt._e()==null){err(errFail,errFail.intro(cs,mwt.key())+"is abstract, thus it can not be hidden");}
    return mwtOf(mwt,s1);    
    }
  void nestedInNewPosition(NC nc,Arrow a,boolean moveDocs){
    if(a._cs.isEmpty()){
      L l1=noNesteds(nc.l());
      addMapTop=fromAndPushThis0Out(this.p,l1,P.of(0,a.cs));
      return;
      }
    var cs1=popLRight(a._cs);
    Program p=forcedNavigate(this.p,a._cs);
    L l1=nc.l();
    l1=noNesteds(l1);
    var src=p.minimize(P.of(a._cs.size(),a.cs));
    l1=fromAndPushThis0Out(p,l1,src);
    List<Doc> docs=L();
    if(moveDocs){
      var p0=p.pop();
      var src0=p0.minimize(P.of(cs1.size(),cs));
      docs=p0.fromDocs(nc.docs(),src0);
      }
    NC newNC=new NC(nc.poss(),docs,a._cs.get(cs1.size()),l1);
    addMapNCs(cs1,newNC);
    }
  NC rename7superNC(NC nc,List<C> csc,Arrow a){
    noCircular(nc.l(),csc);
    noExposeUniqueN(csc,nc.l());
    nestedInNewPosition(nc,a,false);
    return nc.withL(toAbstract(nc.l()));
    }
  NC rename8restrictNC(List<C> csc,NC nc){
    noExposeUniqueN(csc,nc.l());
    return nc.withL(toAbstract(nc.l()));
    }
  NC _rename9nested(NC nc,Arrow a){
    nestedInNewPosition(nc,a,true);
    return _onlyNested(nc);
    }
  List<NC> rename10hideNested(NC nc,Arrow a){
    var r=nc.l().info().refined();
    for(var m:nc.l().mwts()){
      if(r.contains(m.key())){continue;}
      if(m.key().hasUniqueNum()){continue;}
      err(errFail,()->errFail.intro(pushL(cs,nc.key()),false)
        +"can not be hidden since some methods are still public:\n"
        +nc.l().mwts().stream()
          .map(mi->mi.key())
          .filter(k->!k.hasUniqueNum() && !r.contains(k))
          .map(k->errFail.intro(pushL(cs,nc.key()),k)).collect(Collectors.joining()));
      }
    var nc1=_onlyNested(nc);
    var l2=noNesteds(nc.l());
    Program p=forcedNavigate(this.p,a._cs);
    var src=p.minimize(P.of(a._cs.size(),a.cs));
    l2=fromAndPushThis0Out(p,l2,src);
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
      if(!m.key().hasUniqueNum()){c.add(m.with_e(null).withNativeUrl(""));}
      });
    List<NC> ncs=L(l0.ncs(),(c,n)->{
      if(!n.key().hasUniqueNum()){c.add(n);}
      });
    L l=new L(l0.poss(),l0.isInterface(),l0.ts(),mwts,L(),Info.empty,l0.docs());
    List<P.NCs> typeDep=L(c->{
      var acc=new Accumulate<Void>(){
        @Override public
        void visitP(P p){if(p.isNCs() && !c.contains(p)){c.add(p.toNCs());}}
        };
      l.accept(acc);
      for(var nc:ncs){acc.visitDocs(nc.docs());}
      });
    Info i=l0.info();    
    i=new Info(i.isTyped(),typeDep,L(),L(),L(),L(),L(),i.refined(),false, "",L(), -1);
    return l.withNcs(ncs).withInfo(i);
    }
  static Program forcedNavigate(Program p,List<C> cs){
    for(C c:cs){
      if(c!=null){p=p.push(c,Program.emptyL);}
      else{p=p.push(Program.emptyL);}
      }
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
  void noSingleExposeUniqueN(List<C> cs,P.NCs p){
    P.NCs pi=Program.emptyP.from(p,cs);
    if(pi.n()!=0){return;}
    if(!pi.hasUniqueNum()){return;}
    var csCut=L(pi.cs().stream().takeWhile(c->!c.hasUniqueNum()));
    var a=map.get(new Arrow(csCut,null));
    if(a==null || (a.full && !a.isP())){return;}
    err(errFail,errFail.intro(cs,false)
      +"Code can not be extracted since it exposes uniquely numbered path "
      +errFail.intro(p.cs(),false));
    }
  void noExposeUniqueN(List<C> cs,L l){
    l.accept(new Accumulate<Void>(){
      @Override public void visitMWT(MWT mwt){
        if(mwt.key().hasUniqueNum()){return;}
        super.visitMWT(mwt.with_e(null).withNativeUrl(""));
        }
      @Override public void visitInfo(Info info){}
      @Override public void visitNC(NC nc){}
      @Override public void visitP(P path){
        if(!path.isNCs()){return;}
        noSingleExposeUniqueN(cs,path.toNCs());
        }
      });
    }
  L onlyNesteds(L l){
    List<NC> ncs=L(l.ncs().stream().filter(nci->!nci.key().hasUniqueNum()));
    Deps deps=new Deps();
    for(var nc:ncs){deps.collectDocs(forcedNavigate(p, cs),nc.docs());}
    return Program.emptyL.withNcs(ncs).withInfo(deps.toInfo(true)).withPoss(L());
    //purposely trashing the pos of the L inside the nc
    }
  NC _onlyNested(NC nc){
    var l=onlyNesteds(nc.l());
    if(l.ncs().isEmpty()){return null;}
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
  }