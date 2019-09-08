package is.L42.common;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.generated.C;
import is.L42.generated.CT;
import is.L42.generated.Core;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.LL;
import is.L42.generated.Mdf;
import is.L42.generated.Op;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.ST;
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
    return e.visitable().accept(new CloneVisitor(){
      @Override public P visitP(P p){return from(p,source);}
      @Override public Full.L visitL(Full.L l){throw bug();}
      @Override public Core.L visitL(Core.L l){
        return new From(Program.this,source,0).visitL(l);
      }});
    }
  public T from(T t,P.NCs source){return t.withP(from(t.p(),source));}
  public List<T> from(List<T> ts,P.NCs source){return L(ts,t->from(t,source));}

  public List<CT> fromCTz(List<CT>ctz,P.NCs source){return L(ctz,ct->from(ct,source));}
  public CT from(CT ct,P.NCs source){
    assert minimize(source)==source;
    var fromST=new CloneVisitor(){//no need to override visitT(Half.T)
      @Override public ST visitSTMeth(ST.STMeth stMeth){
        return minimize(super.visitSTMeth(stMeth));}
      @Override public ST visitSTOp(ST.STOp stOp){
        return minimize(super.visitSTOp(stOp));}
      @Override public Core.T visitT(Core.T  t){return from(t,source);}
      };
    return ct.withSt(fromST.visitST(ct.st())).withT(fromST.visitT(ct.t()));
    }
  public List<T> collect(P.NCs p){
    LL l=of(p);
    if(!l.isFullL()){return from(((Core.L)l).ts(),p);}
    Full.L fl=(Full.L)l;
    return collect(L(fl.ts(),(c,t)->c.add(from(toCore(t),p))));
    }   
  public List<T> collect(List<T> ts){
    if(ts.isEmpty()){return ts;}
    T t0=ts.get(0);
    ts=popL(ts);
    var recRes=collect(ts);
    var ll=of(t0.p());
    if(!ll.isFullL()){return L(c->{
      if(!recRes.contains(t0)){c.add(t0);}
      for(var ti:((Core.L)ll).ts()){
        T tif=from(ti,t0.p().toNCs());
        if(!recRes.contains(tif)){c.add(tif);}        
        }
      c.addAll(recRes);
      });}
    Full.L l=(Full.L)ll;
    List<T> ts0=L(l.ts(),(c,ti)->from(toCore(ti),t0.p().toNCs()));
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
/*
  
    _______
#define origin(p;s;P) = P'   refine(p;s;P)
* origin(p;s; P) = P'
    {P'} = {P'| P' in collect(p,P) and !refine(p;s;P')}
    
* refine(p;s;P) iff exists P' in collect(p,P) such that s in dom(p(P'))
    
    */
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
    //requires
    //opOptions
    //chooseT
    //toCore
    //subtype
    }
  public T _chooseT(List<T> ts){
    Mdf _mdf=_mostGeneralMdf(ts.stream().map(t->t.mdf()).collect(Collectors.toSet()));
    if(_mdf==null){return null;}
    var ps=ts.stream().map(t->t.p())
    .filter(p->isSubtype(ts.stream().map(t->t.p()),p))
    .collect(Collectors.toList());
    if(ps.size()!=1){return null;}
    return new T(_mdf,L(),ps.get(0));
    }
  private Mdf _mostGeneralMdf(Set<Mdf> mdfs){
    if (mdfs.size()==1){return mdfs.iterator().next();}
    if (mdfs.contains(Mdf.Class)){return null;}
    if (mdfs.contains(Mdf.Capsule) && mdfs.size()==2){
      var i = mdfs.iterator();
      Mdf m=i.next();
      if(m==Mdf.Capsule){m=i.next();}
      assert m!=Mdf.Capsule;
      return m;
      }
    if(TypeManipulation.fwd_or_fwdP_inMdfs(mdfs.stream())){
      if (mdfs.contains(Mdf.Readable)){return null;}
      if (mdfs.contains(Mdf.Lent)){return null;}
      boolean mutIn=false;
      boolean immIn=false;
      for(Mdf m:mdfs){
        if(Mdf.muts.contains(m)){mutIn=true;}
        if(Mdf.imms.contains(m)){immIn=true;}
        }
      if (mutIn && immIn){return null;}
      //we know: more then one, no read/lent, either all imm side or mut side
      if(mdfs.contains(Mdf.ImmutableFwd)){return Mdf.ImmutableFwd;}
      if(mdfs.contains(Mdf.ImmutablePFwd)){return Mdf.ImmutablePFwd;}
      if(mdfs.contains(Mdf.MutableFwd)){return Mdf.MutableFwd;}
      assert mdfs.contains(Mdf.MutablePFwd): mdfs;
      return Mdf.MutablePFwd;
      }
    //if read in mdfs, mdf=read
    if(mdfs.contains(Mdf.Readable)){return Mdf.Readable;}
    if(mdfs.contains(Mdf.Immutable)){return Mdf.Readable;}
    return Mdf.Lent;
    }
  public static class Psi{P p; S s; int i;}
  public List<Psi> opOptions(Op op, List<T>ts){
    return L(c->{for(int i:range(ts)){
      P pi=ts.get(i).p();
      //this.methods(pi)
      }});
    }
    /*
#define P.s.i in p.opOptions(OP, CORE.Ts) //note: now the special case for Path is 
* P.s.i in p.opOptions(OP, T0..Tn)//handled with a 'non op dispatch' desugar
    i in 0..n,
    s = methName(OP_i)(x1..xn)
    P1 ... Pn = p(Ti.P)(s).pars.Ps[from Ti.P;p]
    P'1 ... P'n = (T0..Tn\i).Ps
    P=Ti.P
    p|-P'1<=P1 .. p|-P'1<=P1
    
    */
  public T toCore(Full.T t){return null;}//TODO:
  public Core.MH toCore(Full.MH mh){return null;}//TODO:
  public static Program parse(String s){
    var r=Parse.program("-dummy-",s);
    assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
    assert r.res.wf();
    return r.res;
    }
  }