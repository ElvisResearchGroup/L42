package is.L42.common;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.todo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.generated.C;
import is.L42.generated.CT;
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
  public Program(LL top,PTails pTails){this.top=top;this.pTails=pTails;}
  public static Program flat(LL top){return new Program(top,PTails.empty);}
  public static final Core.L emptyL=new Core.L(L(),false,L(),L(),L(),Core.L.Info.empty,L());
  public static final Core.L emptyLInterface=emptyL.withInterface(true);
  public LL of(P path){
    if(path==P.pAny){return emptyLInterface;}
    if(path==P.pVoid){return emptyL;}
    if(path==P.pLibrary){return emptyL;}
    return this.pop(path.toNCs().n()).top.cs(path.toNCs().cs());
    }
  public Program pop(int n){
    assert n>=0;
    if(n==0){return this;}
    return this.pop().pop(n-1);
    }
  public Program pop(){
    if(!pTails.hasC()){return new Program(pTails.coreL(),pTails.tail());}
    var newTop=pTails.ll().withCs(L(pTails.c()),
      nc->nc.withE(pTails.ll()),
      nc->nc.withL((Core.L)pTails.ll())
      );
    return new Program(newTop,pTails.tail());
    }
  public Program push(C c,LL ll){return new Program(ll,pTails.pTailC(c, top));}
  public Program push(LL ll){return new Program(ll,pTails.pTailSingle((Core.L)top));}
  public Program push(C c){return push(c,top.c(c));}
  public Program update(LL ll){return new Program(ll,pTails);}
  public Program navigate(P.NCs p){
    Program res=this.pop(p.n());
    for(C c:p.cs()){res=res.push(c);}
    return res;
    }
  public P from(P p,P.NCs source){
    if(!p.isNCs()){return p;}
    return from(p.toNCs(),source);
    }
  public P.NCs from(P.NCs p,P.NCs source){
    assert minimize(source)==source;
    int k=source.cs().size();
    int m=source.n();
    int n=p.n();
    var cs=p.cs();
    if(n>=k){return minimize(P.of(m+n-k, cs));}
    if(n==0 && cs.isEmpty()){return source;}//optimization
    List<C> resCs=L(c->{
      c.addAll(source.cs().subList(0, k-n));
      c.addAll(cs);
      });
    var res=P.of(m,resCs);
    assert res==minimize(res):
      res+" "+minimize(res);
    return res;
    }
  public Core.E from(Core.E e,P.NCs source){
    assert minimize(source)==source;
    return fromVisitor(source).visitE(e);
    }
  private CloneVisitor fromVisitor(P.NCs source){
    return new CloneVisitor(){
      @Override public Half.T visitT(Half.T t){
        var t0=super.visitT(t);
        if(t0._mdf()==null){return t0;}
        if(t0.stz().size()!=1){return t0;}
        var st=t0.stz().get(0);
        if(!(st instanceof Core.T)){return t0;}
        return new Half.T(null,L(((Core.T)st).withMdf(t0._mdf())));
        }
      @Override public ST visitSTMeth(ST.STMeth stMeth){
        return minimize(super.visitSTMeth(stMeth));
        }
     @Override public ST visitSTOp(ST.STOp stOp){ 
        return minimize(super.visitSTOp(stOp));
        }
      @Override public Core.T visitT(Core.T  t){
        return t.withP(from(t.p(),source));
        }
      @Override public P visitP(P p){
        return from(p,source);
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

  public List<CT> fromCTz(List<CT>ctz,P.NCs source){
    return L(ctz,(c,ct1)->{
      CT ct=from(ct1,source);
      boolean cond=ct.st() instanceof T 
        && ct.t()._mdf()==null
        && ct.t().stz().size()==1
        && ct.t().stz().get(0) instanceof T;
      if(!cond){c.add(ct);}
      });
    }
  public CT from(CT ct,P.NCs source){
    assert minimize(source)==source;
    var v=fromVisitor(source);
    return ct.withSt(v.visitST(ct.st())).withT(v.visitT(ct.t()));
    }
  public List<T> collect(P.NCs p){
    LL l=of(p);
    if(!l.isFullL()){return from(((Core.L)l).ts(),p);}
    Full.L fl=(Full.L)l;
    if(!fl.reuseUrl().isEmpty()){
      assert false;
      return from(Constants.readURL.apply(fl.reuseUrl()).ts(),p);
      }
    if(!fl.isDots()){
      return collect(L(fl.ts(),(c,t)->c.add(from(toCore(t),p))));
      }
    assert false;
    Program p0=navigate(p);
    Full.L fl0=Constants.readFolder.apply(p0.pTails);
    return collect(L(fl0.ts(),(c,t)->c.add(from(toCore(t),p))));
    }   
  public List<T> collect(List<T> ts){//TODO: test collect
    if(ts.isEmpty()){return ts;}
    T t0=ts.get(0);
    ts=popL(ts);
    var recRes=collect(ts);
    var ll=of(t0.p());
    Core.L l=null;
    if(!ll.isFullL()){l=(Core.L)ll;}
    Full.L fl=(Full.L)ll;
    if(!fl.reuseUrl().isEmpty()){
      assert false;
      l=Constants.readURL.apply(fl.reuseUrl());
      }
    if(l!=null){return L(c->{
      if(!recRes.contains(t0)){c.add(t0);}
      for(var ti:((Core.L)ll).ts()){
        T tif=from(ti,t0.p().toNCs());
        if(!recRes.contains(tif)){c.add(tif);}        
        }
      c.addAll(recRes);
      });}
    if(fl.isDots()){
      assert false;
      var tail=navigate(t0.p().toNCs()).pTails;      
      fl=Constants.readFolder.apply(tail);
      }
    List<T> ts0=L(fl.ts(),(c,ti)->from(toCore(ti),t0.p().toNCs()));
    List<T> ts1=collect(ts0);
    return L(c->{//is not worth to remove this 6 lines dup
      if(!recRes.contains(t0)){c.add(t0);}
      for(var ti:ts1){
        if(!recRes.contains(ti)){c.add(ti);}        
        }
      c.addAll(recRes);
      });
    }
  public boolean isSubtype(Stream<P> subPs,P superP){
    return subPs.allMatch(p->isSubtype(p, superP));
    }
  public boolean isSubtype(Stream<T> subTs,T superT){
    return subTs.allMatch(t->isSubtype(t, superT));
    }
  public boolean isSubtype(T subT,T superT){
    if(!isSubtype(subT.mdf(),superT.mdf())){return false;}
    return isSubtype(subT.p(),superT.p());
    }
  public boolean isSubtype(P subP,P superP){
    assert minimize(subP)==subP;
    assert minimize(superP)==superP;
    if(superP==P.pAny){return true;}
    if(subP.equals(superP)){return true;}
    P.NCs subP0=subP.toNCs();
    if(!subP.isNCs()){return false;}
    if(!superP.isNCs()){return false;}
    for(T ti:collect(subP0)){
      P pi=from(ti.p(),subP0);
      assert minimize(pi)==pi;
      if(pi.equals(superP)){return true;}
      } 
    return false;
    }
  public boolean isSubtype(Mdf subM,Mdf superM){
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
  @SuppressWarnings("serial")
  public static class InvalidImplements extends EndError{
    public InvalidImplements(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public P origin(S s, P.NCs p) throws InvalidImplements{
    List<T> origins=L(collect(p),(c,t)->{
      if(!refine(s,t.p().toNCs())){c.add(t);}
      });
    if(origins.size()==1){return origins.get(0).p();}
    throw new InvalidImplements(of(p).poss(),
      Err.moreThenOneMethodOrigin(s,origins));
    }
  public boolean refine(S s, P.NCs p){
    for(T t:collect(p)){
      if(methods(t.p()).stream().anyMatch(mh->mh.s().equals(s))){return true;}
      }
    return false;
    }
  public Core.MH toCore(Full.MH mh){
    Mdf mdf=mh._mdf();
    if(mdf==null){mdf=Mdf.Immutable;}
    List<Doc> docs=L(mh.docs(),(c,d)->c.add(toCore(d)));
    T t=toCore(mh.t());
    S s=mh.key();
    List<T> pars=L(mh.pars(),(c,ti)->c.add(toCore(ti)));
    List<T> exceptions=L(mh.exceptions(),(c,ti)->c.add(toCore(ti)));
    return new Core.MH(mdf, docs, t, s, pars, exceptions);
    }
  public T toCore(Full.T t){
    Mdf mdf=t._mdf();
    if(mdf==null){mdf=Mdf.Immutable;}
    List<Doc> docs=L(t.docs(),(c,d)->c.add(toCore(d)));
    if (t._p()!=null){return new T(mdf,docs,minimize(t._p()));}
    int n=findScope(t.cs().get(0),0);
    return new T(mdf,docs,minimize(P.of(n, t.cs())));
    }
    private int findScope(C c, int acc){
      if(top.domNC().contains(c)){return acc;}
      if(pTails.isEmpty()){return acc;}
      assert top.isFullL();
      for(Full.Doc d:((Full.L)top).docs()){
        if(d.texts().size()!=1){continue;}
        if(d.texts().get(0).equals("__STOP_SCOPE__")){return acc;}
      }
      return pop().findScope(c, acc+1);
      }
  public Core.Doc toCore(Full.Doc doc){
    List<Doc> docs=L(doc.docs(),(c,d)->c.add(toCore(d)));
    return new Core.Doc(_toCore(doc._pathSel()),doc.texts(), docs);
    }
  public Core.PathSel _toCore(Full.PathSel _p){
    if(_p==null){return null;}
    if(_p._p()==null && _p.cs().isEmpty()){
      return new Core.PathSel(P.coreThis0.p(),_p._s(),_p._x());
      }
    assert _p._p()==null ||_p.cs().isEmpty();
    if(_p._p()!=null){
      return new Core.PathSel(_p._p(),_p._s(),_p._x());
      }
    int n=findScope(_p.cs().get(0),0);   
    return new Core.PathSel(P.of(n,_p.cs()),_p._s(),_p._x());
    }

  public List<Core.MH>extractMHs(List<Full.L.M> ms){
    return L(ms,(c,m)->{
      if(m instanceof Full.L.NC){return;}
      if(m instanceof Full.L.MI){return;}
      if(m instanceof Full.L.MWT){
        c.add(toCore(((Full.L.MWT)m).mh()));return;
        }
      Full.L.F f=(Full.L.F)m;
      Core.T t=toCore(f.t());
      Core.T tr=TypeManipulation._toRead(t);
      assert tr!=null;
      if(f.isVar()){
        c.add(new Core.MH(Mdf.Mutable,L(), P.coreVoid, f.key().withXs(X.thatXs), L(t),L()));        
        }
      if(t!=tr){
        assert !t.equals(tr);
        c.add(new Core.MH(Mdf.Mutable,L(), TypeManipulation.capsuleToLent(t), f.key().withM("#"+f.key().m()), L(),L()));
        }
      c.add(new Core.MH(Mdf.Readable,L(), tr, f.key(), L(),L()));
      });
    }
 
  List<Core.MH> methods(P p){
    if(!p.isNCs()){return L();}
    P.NCs p0=p.toNCs();
    LL ll=of(p0);
    if(!ll.isFullL()){
      return L(((Core.L)ll).mwts(),(c,m)->c.add(from(m.mh(),p0)));
      }
    Full.L l=(Full.L)ll;
    assert !l.isDots();
    assert l.reuseUrl().isEmpty();
    
    return methods(p0, l);
    }
  List<Core.MH> methods(P.NCs p0,Full.L l){
    List<Core.MH> mhs=this.navigate(p0).extractMHs(l.ms());
    List<T> ts=L(l.ts(),(c,t)->c.add(from(toCore(t),p0)));
    List<T> ps=collect(ts);
    List<List<MH>> methods=L(ps,(c,t)->c.add(methods(t.p())));
    List<S> ss=L(methods.stream().flatMap(ms->ms.stream().map(m->m.s())).distinct());
    for(S s:ss){origin(s,p0);}
    //throws InvalidImplements
    List<MH> res=L(ss,(c,s)->{
      var r1=mhs.stream().filter(mh->mh.s().equals(s)).reduce(toOneOr(()->bug()));
      if(r1.isPresent()){c.add(r1.get());return;}
      for(var ms:methods){
        var ri=ms.stream().filter(mh->mh.s().equals(s)).reduce(toOneOr(()->bug()));
        if(ri.isPresent()){c.add(ri.get());return;}
        }
      });
    return res;
    }
  public P minimize(P path){
    if(!path.isNCs()){return path;}
    return minimize(path.toNCs());
    }    
  public P.NCs minimize(P.NCs path){
    if(path.n()==0){return path;}
    if(path.n()==1){return baseMinimize(path);}
    var pathLess1=path.withN(path.n()-1);
    P.NCs tmp=pop().minimize(pathLess1).toNCs();
    if(pathLess1==tmp){tmp=path;}
    else{tmp=tmp.withN(tmp.n()+1);}
    if(tmp.n()==1){return baseMinimize(tmp);}
    return tmp;
    }
  private P.NCs baseMinimize(P.NCs p) {
    assert !pTails.isEmpty();
    if(p.cs().isEmpty()){return p;}
    if(!pTails.hasC()){return p;}
    if(!pTails.c().equals(p.cs().get(0))){return p;}
    return P.of(p.n()-1,popL(p.cs()));
    }
  public ST minimize(ST st){
    throw todo();//TODO:
    }
  public List<ST> minimize(List<ST>stz){
    throw todo();//TODO:
    }
  public T _chooseT(List<T> ts){
    Mdf _mdf=_mostGeneralMdf(ts.stream().map(t->t.mdf()).collect(Collectors.toSet()));
    if(_mdf==null){return null;}
    var ps=L(ts.stream().map(t->t.p()).filter(p->isSubtype(ts.stream().map(t->t.p()),p)));
    if(ps.size()!=1){return null;}
    return new T(_mdf,L(),ps.get(0));
    }
  private Mdf _mostGeneralMdf(Set<Mdf> mdfs){
    var g=generalEnoughMdf(mdfs);
    return g.stream().filter(mdf->g.stream()
      .allMatch(mdf1->isSubtype(mdf1, mdf1)))
      .reduce(toOneOr(()->bug())).orElse(null);
    }
  private List<Mdf> generalEnoughMdf(Set<Mdf> mdfs){
    return L(c->{
      for(Mdf mdf:Mdf.values()){
        if(mdfs.stream().allMatch(mdf1->isSubtype(mdf1,mdf))){
          c.add(mdf);
          }
        }
      });
    }

  /*public static class Psi{P p; S s; int i;}
  public List<Psi> opOptions(Op op, List<T>ts){
    return L(c->{for(int i:range(ts)){
      P pi=ts.get(i).p();
      //this.methods(pi)
      }});
    }*/

  public static Program parse(String s){
    var r=Parse.program("-dummy-",s);
    assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
    assert r.res.wf();
    return r.res;
    }
  }