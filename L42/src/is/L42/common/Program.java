package is.L42.common;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.todo;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.constraints.FreshNames;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.LL;
import is.L42.generated.Mdf;
import is.L42.generated.Op;
import is.L42.generated.P;
import is.L42.generated.Pos;
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
  public static final Core.L emptyL=new Core.L(L(),false,L(),L(),L(),Core.L.Info.empty,L());
  public static final Core.L emptyLInterface=emptyL.withInterface(true);

  public Core.L topCore(){return (Core.L)top;}
  public Core.L _ofCore(P path){
    if(path==P.pAny){return emptyLInterface;}
    if(path==P.pVoid){return emptyL;}
    if(path==P.pLibrary){return emptyL;}
    return _ofCore(path.toNCs());    
    }
  public Core.L _ofCore(P.NCs path){
    try{return (Core.L)this.pop(path.n()).top.cs(path.cs());}
    catch(LL.NotInDom nid){return null;}
    }
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
  public Program update(LL ll){return update(ll,true);}
  public Program update(LL ll,boolean cleanPushed){
    assert !is.L42.common.Constants.updatePopChecks() || !cleanPushed || cleanPushed(pTails):pTails.printCs();
    if(ll==this.top){return this;}
    return new Program(ll,pTails);
    }
  public Program navigate(P.NCs p){
    Program res=this.pop(p.n());
    for(C c:p.cs()){res=res.push(c);}
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
        var res=super.visitSTMeth(st);
        res=CTz.solve(Program.this, res);
        return res;
        }
      @Override public ST visitSTOp(ST.STOp st){
        var res=super.visitSTOp(st);
        res=CTz.solve(Program.this, res);
        return res;
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
  
  public boolean isSubtype(Stream<P> subPs,P superP,List<Pos> poss){
    return subPs.allMatch(p->isSubtype(p, superP,poss));
    }
  public boolean isSubtype(P subP,Stream<P> superPs,List<Pos> poss){
    return superPs.allMatch(p->isSubtype(subP,p,poss));
    }
  public boolean isSubtype(Stream<T> subTs,T superT,List<Pos> poss){
    return subTs.allMatch(t->isSubtype(t, superT,poss));
    }
  public boolean isSubtype(T subT,Stream<T> superTs,List<Pos> poss){
    return superTs.allMatch(t->isSubtype(subT,t,poss));
    }
  public boolean isSubtype(T subT,T superT,List<Pos> poss){
    if(!isSubtype(subT.mdf(),superT.mdf())){return false;}
    return isSubtype(subT.p(),superT.p(),poss);
    }
  public boolean isSubtype(P subP,P superP,List<Pos> poss){
    assert minimize(subP)==subP;
    assert minimize(superP)==superP;
    if(superP==P.pAny){return true;}
    if(subP.equals(superP)){return true;}
    if(!subP.isNCs()){return false;}
    P.NCs subP0=subP.toNCs();
    if(!subP.isNCs()){return false;}
    if(!superP.isNCs()){return false;}
    assert minimize(subP0)==subP0;
    var l=(Core.L)of(subP0,top.poss());//may throw a PathNotExistant that is captured by solve STOp
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
  public List<Core.MH>extractMHs(List<Full.L.M> ms){
    return L(ms,(c,m)->{
      if(m instanceof Full.L.NC){return;}
      if(m instanceof Full.L.MI){return;}
      if(m instanceof Full.L.MWT){
        c.add(TypeManipulation.toCore(((Full.L.MWT)m).mh()));return;
        }
      Full.L.F f=(Full.L.F)m;
      Core.T t=TypeManipulation.toCore(f.t());
      Core.T tr=TypeManipulation.toRead(t);
      assert tr!=null;
      if(f.isVar()){
        c.add(new Core.MH(Mdf.Mutable,L(), P.coreVoid, f.key().withXs(X.thatXs), L(t),L()));        
        }
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
    assert !pTails.isEmpty(): path;
    P.NCs tmp=pop().minimize(pathLess1).toNCs();
    if(pathLess1==tmp){tmp=path;}
    else{tmp=tmp.withN(tmp.n()+1);}
    if(tmp.n()==1){return baseMinimize(tmp);}
    return tmp;
    }
  private P.NCs baseMinimize(P.NCs p) {
    assert !pTails.isEmpty():p;
    if(p.cs().isEmpty()){return p;}
    if(!pTails.hasC()){return p;}
    if(!pTails.c().equals(p.cs().get(0))){return p;}
    return P.of(p.n()-1,popL(p.cs()));
    }
  public T _chooseGeneralT(List<T> ts,List<Pos> poss){
    Mdf _mdf=TypeManipulation._mostGeneralMdf(ts.stream().map(t->t.mdf()).collect(Collectors.toSet()));
    if(_mdf==null){return null;}
    var ps=L(ts.stream()
      .map(ti->ti.p())
      .filter(pi->isSubtype(ts.stream().map(ti->ti.p()),pi,poss))
      .distinct());
    if(ps.size()!=1){return null;}
    return new T(_mdf,L(),ps.get(0));
    }
  //-----------
  public T _chooseSpecificT(List<T> ts,List<Pos> poss){
    Mdf _mdf=_mostSpecificMdf(ts.stream().map(t->t.mdf()).collect(Collectors.toSet()));
    if(_mdf==null){return null;}
    var ps=L(ts.stream()
      .map(ti->ti.p())
      .filter(pi->isSubtype(pi,ts.stream().map(ti->ti.p()),poss))
      .distinct());
    if(ps.size()!=1){return null;}
    return new T(_mdf,L(),ps.get(0));
    }
  private Mdf _mostSpecificMdf(Set<Mdf> mdfs){
    var g=specificEnoughMdf(mdfs);
    return g.stream().filter(mdf->g.stream()
      .allMatch(mdf1->isSubtype(mdf1, mdf)))
      .reduce(toOneOr(()->bug())).orElse(null);
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
  //-----------
  public static Program parse(String s){
    var r=Parse.program(Constants.dummy,s);
    assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
    var res=Init.init(r.res,new FreshNames());
    assert res.wf();
    return res;
    }
  }