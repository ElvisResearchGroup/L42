package is.L42.common;

import static is.L42.tools.General.L;
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
import is.L42.generated.Core;
import is.L42.generated.Core.T;
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
  public P minimize(P path){
    if(!(path instanceof P.NCs)){return path;}
    var p=path.toNCs();
    if(p.n()==0){return path;}
    
    if(p.n()==1){return baseMinimize(p);}
    P.NCs tmp=pop().minimize(p.withN(p.n()-1)).toNCs();
    tmp=tmp.withN(tmp.n()+1);
    if(tmp.n()==1){return baseMinimize(tmp);}
    return tmp;
    }
  private P.NCs baseMinimize(P.NCs p) {
    assert !pTails.isEmpty();
    if(!p.cs().isEmpty()){return p;}
    if(!pTails.hasC()){return p;}
    if(!pTails.c().equals(p.cs().get(0))){return p;}
    return P.of(p.n()-1,popL(p.cs()));
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
    .filter(p->subP(ts.stream().map(t->t.p()),p))
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
      this.of(pi).m
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
  public boolean subT(T subT,T superT){
    return true;
    }
  public boolean subP(P subP,P superP){
    return true;
    }
  public boolean subP(Stream<P> subPs,P superP){
    return true;
    }
  }