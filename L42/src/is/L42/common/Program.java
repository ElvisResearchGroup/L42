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
import is.L42.top.Init;
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
  public LL of(P path,List<Pos>errs){
    if(path==P.pAny){return emptyLInterface;}
    if(path==P.pVoid){return emptyL;}
    if(path==P.pLibrary){return emptyL;}
    try{return this.pop(path.toNCs().n()).top.cs(path.toNCs().cs());}
    catch(LL.NotInDom nid){
      throw new PathNotExistent(errs,Err.pathNotExistant(path));
      }
    }
  public Program pop(int n){
    assert n>=0;
    if(n==0){return this;}
    return this.pop().pop(n-1);
    }
  public Program pop(){
    if(!pTails.hasC()){return new Program(pTails.coreL(),pTails.tail());}
    if(!pTails.ll().domNC().contains(pTails.c())){
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
  public Program push(LL ll){return new Program(ll,pTails.pTailSingle((Core.L)top));}
  public Program push(C c){return push(c,top.c(c));}
  public Program update(LL ll){return new Program(ll,pTails);}
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
  public P.NCs from(P.NCs p,int m,List<C>sCs){
    int n=p.n();
    var cs=p.cs();
    int k=sCs.size();
    if(n>=k){return minimize(P.of(m+n-k, cs));}
    List<C> resCs=merge(sCs.subList(0, k-n),cs);
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
  
  public boolean isSubtype(Stream<P> subPs,P superP,List<Pos> poss){
    return subPs.allMatch(p->isSubtype(p, superP,poss));
    }
  public boolean isSubtype(Stream<T> subTs,T superT,List<Pos> poss){
    return subTs.allMatch(t->isSubtype(t, superT,poss));
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
    P.NCs subP0=subP.toNCs();
    if(!subP.isNCs()){return false;}
    if(!superP.isNCs()){return false;}
    assert minimize(subP0)==subP0;
    for(T ti:((Core.L)of(subP0,poss)).ts()){
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
  @SuppressWarnings("serial")
  public static class PathNotExistent extends EndError{
    public PathNotExistent(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public P resolve(List<C> cs,List<Pos>poss){
    int n=findScope(cs.get(0),0,poss);
    return P.of(n, cs);
    }
  private int findScope(C c, int acc,List<Pos>poss){
    String url="";
    if(top.isFullL()){url=((Full.L)top).reuseUrl();}
    if(!url.isEmpty()){
      throw bug();//will give error if the url is not #$
      //and the retrived code do not define C c.
      }
    if(top.domNC().contains(c)){return acc;}
    if(pTails.isEmpty()){
      throw new PathNotExistent(poss, Err.pathNotExistant(c));
      }
    return pop().findScope(c, acc+1,poss);
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
  public List<ST> minimizeSTz(List<ST>stz){
    throw todo();//TODO:
    }
  public CT minimize(CT ct){
    throw todo();//TODO:
    }
  public List<CT> minimizeCTz(List<CT> ctz){
    return ctz;//TODO:
    }

  public T _chooseT(List<T> ts,List<Pos> poss){
    Mdf _mdf=_mostGeneralMdf(ts.stream().map(t->t.mdf()).collect(Collectors.toSet()));
    if(_mdf==null){return null;}
    var ps=L(ts.stream().map(t->t.p()).filter(p->isSubtype(ts.stream().map(t->t.p()),p,poss)));
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
    var res=Init.init(r.res);
    assert res.wf();
    return res;
    }
  }