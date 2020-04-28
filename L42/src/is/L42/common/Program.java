package is.L42.common;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.toOneOrBug;
import static is.L42.tools.General.todo;
import static is.L42.tools.General.typeFilter;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import is.L42.constraints.FreshNames;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.LDom;
import is.L42.generated.LL;
import is.L42.generated.Mdf;
import is.L42.generated.Op;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.Psi;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.generated.X;
import is.L42.top.Init;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;

public class Program implements Visitable<Program>{
  @Override public Program accept(CloneVisitor v) {return v.visitProgram(this); }
  @Override public void accept(CollectorVisitor v) {v.visitProgram(this);}
  @Override public boolean wf() {return Constants.wf.test(this);}
  @Override public String toString() {return Constants.toS.apply(this);}
  public final LL top;
  public final PTails pTails;
  public Program(LL top,PTails pTails){
    this.top=top;
    this.pTails=pTails;
    //assert Constants.newFwProgram(this);
    }
  public static Program flat(LL top){return new Program(top,PTails.empty);}
  public static final Core.L emptyL=new Core.L(L(),false,L(),L(),L(),Core.L.Info.empty.withTyped(true),L());
  public static final Core.L emptyLInterface=emptyL.withInterface(true);
  public static final Program emptyP=Program.flat(Program.emptyL);

  public Core.L topCore(){return (Core.L)top;}
  public Core.L _ofCore(P path){
    if(path==P.pAny){return emptyLInterface;}
    if(path==P.pVoid){return emptyL;}
    if(path==P.pLibrary){return emptyL;}
    return _ofCore(path.toNCs());    
    }
  public Core.L _ofCore(P.NCs path){return this.pop(path.n())._ofCore(path.cs());}
  public Core.L _ofCore(List<C> path){return (Core.L)top._cs(path);}
  public LL of(P path,List<Pos>errs){
    if(path==P.pAny){return emptyLInterface;}
    if(path==P.pVoid){return emptyL;}
    if(path==P.pLibrary){return emptyL;}
    return of(path.toNCs(),errs);
    }
  public LL of(P.NCs path,List<Pos>errs){
    try{return this.pop(path.n()).top.cs(path.cs());}
    catch(LL.NotInDom nid){
      throw new EndError.PathNotExistent(errs,Err.pathNotExistant(path));
      }
    }
  public Program pop(int n){
    assert n>=0;
    if(n==0){return this;}
    return this.pop().pop(n-1);
    }
  public Program pop(){
    assert !pTails.isEmpty();
    if(!pTails.hasC()){return new Program(pTails.ll(),pTails.tail());}
    if(!pTails.ll().inDom(pTails.c())){
      return new Program(pTails.ll(),pTails.tail());
      }
    var newTop=pTails.ll().withCs(L(pTails.c()),
      nc->nc.withE(top),
      nc->nc.withL((Core.L)top)
      );
    return new Program(newTop,pTails.tail());
    }
  public Program push(C c,LL ll){
    assert c!=null;
    assert ll!=null;
    return new Program(ll,pTails.pTailC(c, top));
    }
  public Program push(LL ll){return new Program(ll,pTails.pTailSingle(top));}
  public Program push(C c){return push(c,top.c(c));}
  public Program _push(C c){
    var res=LDom._elem(topCore().ncs(), c);
    if(res==null){return null;}
    return push(c,res.l());
    }
  public Program update(LL ll){return update(ll,true);}
  public Program update(LL ll,boolean cleanPushed){
    assert !is.L42.common.Constants.updatePopChecks() || !cleanPushed || cleanPushed(pTails):pTails.printCs();
    if(ll==this.top){return this;}
    return new Program(ll,pTails);
    }
  public Program navigate(P.NCs p){
    return this.pop(p.n()).navigate(p.cs());
    }
  public Program navigate(List<C> cs){
    Program res=this;
    for(C c:cs){res=res.push(c);}
    return res;
    }
  public Program _navigate(P.NCs p){//Still exception if n>dept
    return this.pop(p.n())._navigate(p.cs());
    }
  public Program _navigate(List<C> cs){
    Program res=this;
    for(C c:cs){
      res=res._push(c);
      if(res==null){return null;}
      }
    return res;
    }
  public int dept(){
    int count=0;
    for(PTails p=pTails;!p.isEmpty();p=p.tail()){count+=1;}
    return count;
    }
  public ArrayList<C> path(){
    var res=new ArrayList<C>();
    for(PTails p=pTails;!p.isEmpty();p=p.tail()){
      if(!p.hasC()){res.add(0,null);}
      else{res.add(0,p.c());}
      }
    return res;
    }
  public P from(P p,P.NCs source){
    if(!p.isNCs()){return p;}
    return from(p.toNCs(),source);
    }
  public P.NCs from(P.NCs p,P.NCs source){
    assert minimize(source)==source:
      source +" "+minimize(source);
    P.NCs res=from(p,source.n(),source.cs());
    if(res.equals(p)){return p;}
    if(res.equals(source)){return source;}
    return res;
    }
  public P.NCs from(P.NCs p,List<C>sCs){return from(p,0,sCs);}
  public P.NCs from(P.NCs p,int m,List<C>sCs){
    int n=p.n();
    var cs=p.cs();
    int k=sCs.size();
    if(n>=k){
      var res=P.of(m+n-k, cs);
      if(m==0){return res;}
      return minimize(res);
      }
    List<C> resCs=merge(sCs,k-n,cs);//not using General.merge; performance+non serializable sublists
    var res=P.of(m,resCs);
    assert res==minimize(res):
      res+" "+minimize(res);
    return res;
    }
  private List<C> merge(List<C> cs1,int limit,List<C>cs2){
    ArrayList<C> res=new ArrayList<>();
    for(int i=0;i<limit;i+=1){res.add(cs1.get(i));}
    res.addAll(cs2);
    return Collections.unmodifiableList(res);
    }    
  public Core.E from(Core.E e,P.NCs source){
    assert minimize(source)==source;
    return fromVisitor(source).visitE(e);
    }
  private CloneVisitor fromVisitor(P.NCs source){
    return new CloneVisitor(){
      @Override public Core.T visitT(Core.T t){
        return t.withP(from(t.p(),source));
        }
      @Override public P visitP(P p){
        return from(p,source);
        }
      @Override public ST visitSTMeth(ST.STMeth st){
        return solve(super.visitSTMeth(st));
        }
      @Override public ST visitSTOp(ST.STOp st){
        return solve(super.visitSTOp(st));
        }
      @Override public Full.L visitL(Full.L l){throw bug();}
      @Override public Core.L visitL(Core.L l){
        return new From(Program.this,source,0).visitL(l);
        }
      };
    }
  public T from(T t,P.NCs source){return fromVisitor(source).visitT(t);}
  public Core.MH from(Core.MH mh,P.NCs source){return fromVisitor(source).visitMH(mh);}
  public List<T> from(List<T> ts,P.NCs source){return fromVisitor(source).visitTs(ts);}
  public List<Doc> fromDocs(List<Doc> docs,P.NCs source){return fromVisitor(source).visitDocs(docs);}
  public ST from(ST st,P.NCs source){return fromVisitor(source).visitST(st);}
  public List<ST> fromSTz(List<ST> stz,P.NCs source){return fromVisitor(source).visitSTz(stz);}
  public CTz from(CTz ctz,P.NCs source){
    var res=new CTz();
    for(var e:ctz.entries()){
      ST st=from(e.getKey(),source);
      List<ST> stz=fromSTz(e.getValue(),source);
      res.plusAcc(this, st, stz);
      }
    return res;
    }
  
  public boolean isSubtype(Stream<P> subPs,P superP){
    return subPs.allMatch(p->_isSubtype(p, superP));
    }
  public boolean isSubtype(P subP,Stream<P> superPs){
    return superPs.allMatch(p->_isSubtype(subP,p));
    }
  public boolean isSubtype(Stream<T> subTs,T superT){
    return subTs.allMatch(t->_isSubtype(t, superT));
    }
  public boolean isSubtype(T subT,Stream<T> superTs){
    return superTs.allMatch(t->_isSubtype(subT,t));
    }
  public Boolean _isSubtype(T subT,T superT){
    if(!isSubtype(subT.mdf(),superT.mdf())){return false;}
    return _isSubtype(subT.p(),superT.p());
    }
  public Boolean _isSubtype(P subP,P superP){//return null if path do not exists as core
    assert minimize(subP)==subP;
    assert minimize(superP)==superP;
    if(superP==P.pAny){return true;}
    if(subP.equals(superP)){return true;}
    if(!subP.isNCs()){return false;}
    if(!superP.isNCs()){return false;}
    P.NCs subP0=subP.toNCs();
    assert minimize(subP0)==subP0;
    var l=_ofCore(subP0);
    if(l==null){return null;}
    for(T ti:l.ts()){
      P pi=from(ti.p(),subP0);
      assert minimize(pi)==pi;
      if(pi.equals(superP)){return true;}
      } 
    return false;
    }
  public static boolean isSubtype(Mdf subM,Mdf superM){
    if(subM==superM){return true;}
    switch(subM){
      case Class:        return false;
      case Capsule:      return superM!=Mdf.Class;
      case Immutable:    return superM.isIn(Mdf.Readable,Mdf.ImmutablePFwd,Mdf.ImmutableFwd);//imm<=read,fwd%Imm //,fwdImm
      case Mutable:      return superM.isIn(Mdf.Lent,Mdf.MutablePFwd,Mdf.Readable,Mdf.MutableFwd);//mut<=lent,fwd%Mut //,read,fwdMut
      case Lent:         return superM==Mdf.Readable;//lent<=read
      case MutablePFwd:  return superM==Mdf.MutableFwd;//fwd%Mut<=fwdMut
      case ImmutablePFwd:return superM==Mdf.ImmutableFwd;//fwd%Imm<=fwdImm
      default: return false;
      }
    }
  public P resolve(List<C> cs,List<Pos>poss){
    int n=findScope(cs.get(0),0,poss);
    return P.of(n, cs);
    }
  private int findScopeFull(C c, int acc,List<Pos>poss){
    String url=((Full.L)top).reuseUrl();
    Full.L fTop=(Full.L)top;
    if(_elem(fTop.ms(),c)!=null){return acc;}
    if(!url.isEmpty()){
      if(url.startsWith("#$")){return acc;}
      Core.L cTop = Constants.readURL.apply(url);
      if(cTop.domNC().contains(c)){return acc;}
      throw new EndError.PathNotExistent(poss,Err.pathNotExistant(c));
      }
    assert !fTop.isDots();
    if(pTails.isEmpty()){
      throw new EndError.PathNotExistent(poss, Err.pathNotExistant(c));
      }
    return pop().findScope(c, acc+1,poss);
    }
  private int findScopeCore(C c, int acc,List<Pos>poss){
    if(top.domNC().contains(c)){return acc;}
    return pop().findScope(c, acc+1,poss);
    }
  private int findScope(C c, int acc,List<Pos>poss){
    if(top.isFullL()){return findScopeFull(c,acc,poss);}
    return findScopeCore(c,acc,poss);
    }
  public List<S> fieldMs(Full.L.F f){
    return L(c->{
      if(f.isVar()){c.add(f.key().withXs(X.thatXs));}
      Core.T t=TypeManipulation.toCore(f.t());
      Core.T tr=TypeManipulation.toRead(t);
      if(t!=tr){
        assert !t.equals(tr);
        if(!t.mdf().isCapsule()){c.add(f.key().withM("#"+f.key().m()));}
        }
      });
    }
  public List<Core.MH>extractMHs(List<Full.L.M> ms){
    return L(ms,(c,m)->{
      if(m instanceof Full.L.NC){return;}
      if(m instanceof Full.L.MI){return;}
      if(m instanceof Full.L.MWT){c.add(TypeManipulation.toCore(((Full.L.MWT)m).mh()));return;}
      Full.L.F f=(Full.L.F)m;
      Core.T t=TypeManipulation.toCore(f.t());
      Core.T tr=TypeManipulation.toRead(t);
      assert tr!=null;
      if(f.isVar()){c.add(new Core.MH(Mdf.Mutable,L(), P.coreVoid, f.key().withXs(X.thatXs), L(t),L()));}
      if(t!=tr){
        assert !t.equals(tr);
        if(!t.mdf().isCapsule()){
          c.add(new Core.MH(Mdf.Mutable,L(),t, f.key().withM("#"+f.key().m()), L(),L()));
          }
        }
      c.add(new Core.MH(Mdf.Readable,L(), tr, f.key(), L(),L()));
      });
    }
  
  public P minimize(P path){
    if(!path.isNCs()){return path;}
    return minimize(path.toNCs());
    }    
  public P.NCs minimize(P.NCs path){
    if(path.n()==0){return path;}
    if(path.n()==1){return baseMinimize(path);}
    var pathLess1=path.withN(path.n()-1);
    assert !pTails.isEmpty(): 
    path;
    P.NCs tmp=pop().minimize(pathLess1).toNCs();
    if(pathLess1==tmp){tmp=path;}
    else{tmp=tmp.withN(tmp.n()+1);}
    if(tmp.n()==1){return baseMinimize(tmp);}
    return tmp;
    }
  private P.NCs baseMinimize(P.NCs p) {
    assert !pTails.isEmpty():
      p;
    if(p.cs().isEmpty()){return p;}
    if(!pTails.hasC()){return p;}
    if(!pTails.c().equals(p.cs().get(0))){return p;}
    return P.of(p.n()-1,popL(p.cs()));
    }
  public T _chooseGeneralT(List<T> ts){
    Mdf _mdf=TypeManipulation._mostGeneralMdf(ts.stream().map(t->t.mdf()).collect(Collectors.toSet()));
    if(_mdf==null){return null;}
    var ps=L(ts.stream()
      .map(ti->ti.p())
      .filter(pi->ts.stream().allMatch(//ti.p() may not exist as code, and _isSubtype may return null
        ti->Boolean.FALSE!=_isSubtype(ti.p(), pi)))//checking for !=false does not discard those cases 
      .distinct());//so we can return null at the line after
    if(ps.size()!=1){return null;}
    return new T(_mdf,L(),ps.get(0));
    }
  //-----------
  public T _chooseSpecificT(List<T> ts,List<Pos> poss){
    Mdf _mdf=_mostSpecificMdf(ts.stream().map(t->t.mdf()).collect(Collectors.toSet()));
    if(_mdf==null){return null;}
    var ps=L(ts.stream()
      .map(ti->ti.p())
      .filter(pi->isSubtype(pi,ts.stream().map(ti->ti.p())))
      .distinct());
    if(ps.size()!=1){return null;}
    return new T(_mdf,L(),ps.get(0));
    }
  private Mdf _mostSpecificMdf(Set<Mdf> mdfs){
    var g=specificEnoughMdf(mdfs);
    return g.stream().filter(mdf->g.stream()
      .allMatch(mdf1->isSubtype(mdf1, mdf)))
      .reduce(toOneOrBug()).orElse(null);
    }
  private List<Mdf> specificEnoughMdf(Set<Mdf> mdfs){
    return L(c->{
      for(Mdf mdf:Mdf.values()){
        if(mdf.isIn(Mdf.ImmutablePFwd,Mdf.MutablePFwd)){continue;}
        if(mdfs.stream().allMatch(mdf1->isSubtype(mdf,mdf1))){
          c.add(mdf);
          }
        }
      });
    }
  private static boolean cleanPushed(PTails pTails){
    if(pTails.isEmpty()){return true;}
    if(!pTails.hasC()){return false;}
    C c=pTails.c();
    return pTails.ll().inDom(c) && cleanPushed(pTails.tail());
    }
  public List<ST> solve(List<ST> stz){return L(stz,sti->solve(sti));}
  public ST solve(ST st){
    if(st instanceof T){return st;}
    if(st instanceof ST.STMeth){return solve((ST.STMeth)st);}
    if(st instanceof ST.STOp){return solve((ST.STOp)st);}
    throw bug();
    }
  public ST solve(ST.STMeth stsi){
    assert stsi.toString().length()<500:
    "";
    ST st=solve(stsi.st());
    if(!(st instanceof T) ||!((T)st).p().isNCs()){return stsi.withSt(st);}
    P.NCs p0=((T)st).p().toNCs();
    var pOfP0=_ofCore(p0);
    if(pOfP0==null){return stsi.withSt(st);}
    var mwt= _elem(pOfP0.mwts(),stsi.s());
    if(mwt==null){return stsi.withSt(st);}
    if(stsi.i()==-1){return from(mwt.mh().t(),p0);}
    assert stsi.i()<=mwt.mh().s().xs().size();
    if(stsi.i()==0){return new Core.T(mwt.mh().mdf(),mwt.mh().docs(),p0);}
    return from(mwt.mh().pars().get(stsi.i()-1),p0);
    }
  public ST solve(ST.STOp st){
    List<List<ST>> minStzi=L(st.stzs(),stzi->solve(stzi));
    List<List<T>> tzs=L(minStzi,(c,stzi)->c.add(typeFilter(stzi.stream(),T.class)));
    List<List<T>> tsz=tzsToTsz(tzs);
    Set<Psi> options=new HashSet<>();
    for(var ts:tsz){
      var res=_opOptions(st.op(),ts);
      if(res!=null){options.addAll(res);continue;}
      return st.withStzs(minStzi);
      }
    if(options.size()!=1){return st.withStzs(minStzi);}
    assert options.size()==1;
    Psi psi=options.iterator().next();
    return from(_elem(_ofCore(psi.p()).mwts(),psi.s()).mh().t(),psi.p());
    }
  static List<List<T>> tzsToTsz(List<List<T>> tzs){
    assert !tzs.isEmpty();
    if(tzs.size()==1){
      return L(tzs.get(0),(c,tz)->c.add(L(tz)));
      }
    var inductive=tzsToTsz(popL(tzs));
    var tz0=tzs.get(0);
    return L(tz0,(c,ti)->{for(var tz:inductive){c.add(pushL(ti,tz));}});
    }
  private boolean _opOptionsAcc(Op op, List<T>ts, int i,ArrayList<Psi>acc){
    List<T> t11n=L(range(ts),(cj,j)->{
      if(j!=i){cj.add(ts.get(j));}
      });
    String sName = NameMangling.methName(op,i);
    P tmp=ts.get(i).p();
    if(!tmp.isNCs()){return true;}
    P.NCs tip=tmp.toNCs();
    var l=_ofCore(tip);
    if(l==null){return false;}
    List<MWT> mwts=l.mwts();
    List<MH> mhs=L(mwts.stream()
      .map(m->m.mh())
      .filter(m->
        m.s().m().equals(sName) && !m.s().hasUniqueNum() && m.s().xs().size()==ts.size()-1
        ));
    for(MH mh:mhs){
      List<T>t1n=L(mh.pars(),(ci,ti)->ci.add(from(ti,tip)));
      assert t1n.size()==t11n.size(): t1n+" "+t11n;
      boolean acceptablePaths=true;
      for(int j:range(t1n)){
        P p1j=t11n.get(j).p();
        P pj=t1n.get(j).p();
        Boolean res=_isSubtype(p1j,pj);
        if(res==null){return false;}
        res&=(t11n.get(j).mdf()==Mdf.Class)==(t1n.get(j).mdf()==Mdf.Class);
        if(!res){acceptablePaths=false;}//Do not break, so the return false above can be triggered
        }
      if(acceptablePaths){acc.add(new Psi(tip,mh.s(),i));}
      }
    return true;  
    }
  public List<Psi> _opOptions(Op op, List<T>ts){
    assert ts.stream().noneMatch(t->t==null):ts;
    boolean[]flag={true};
    List<Psi> res=L(c->{
      for(int i:range(ts)){
        flag[0]&=_opOptionsAcc(op,ts,i,c);
        if(!flag[0]){return;}
        }
      });
    if(flag[0]){return res;}
    return null;
    }    
  //-----------
  public static Program parse(String s){
    var r=Parse.program(Constants.dummy,s);
    assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
    var res=Init.init(r.res,new FreshNames());
    assert res.wf();
    return res;
    }
  public boolean inPrivate(){
    if(pTails.isEmpty()){return false;}
    return pTails.hasC() && pTails.c().hasUniqueNum();
    }
  public boolean inPublicUpTo(int j){
    if(j==0){return false;}
    if(pTails.isEmpty()){return false;}
    if(pTails.hasC() && !pTails.c().hasUniqueNum()){return true;}
    return pop().inPublicUpTo(j-1);
    }
  }