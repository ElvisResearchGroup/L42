package is.L42.translationToJava;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;

import java.util.List;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core.T;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.ST;
import is.L42.generated.X;
import is.L42.visitors.ToSTrait;


public class J extends is.L42.visitors.UndefinedCollectorVisitor implements ToSTrait{
  @Override public ToSTrait.ToSState state(){return state;}
  ToSTrait.ToSState state= new ToSTrait.ToSState();

  public J(Program p, G g, boolean wrap) {this.p=p; this.g=g; this.wrap=wrap;}
  Program p;
  G g;
  boolean wrap;
  T g(Core.XP xP){
    if(xP instanceof Core.EX){return g(((Core.EX)xP).x());}
    var p=(Core.PCastT)xP;
    return p.t();    }
  T g(X x){return g.of(x);}
  boolean nativeKind(T t){return !p.ofCore(t.p()).info().nativeKind().isEmpty();}
  boolean nativeWrap(T t){return wrap && nativeKind(t);}
  void typeName(T t){
    if(!t.p().isNCs()){typeName(t.p());return;}
    if(t.mdf().isClass()){className(p.navigate(t.p().toNCs()));return;}
    if (!t.mdf().isIn(Mdf.MutableFwd,Mdf.ImmutableFwd)){typeName(t.p());return;}
    if(!nativeKind(t)){typeName(t.p());}
    else{kw("Object");}
  };
  void typeName(P p){
    if(p.isNCs()){typeName(this.p.navigate(p.toNCs()));return;}
    kw("L42"+p.toString());
    }
  void typeName(Program p){
    var info=p.topCore().info();
    String nk=info.nativeKind();
    if(nk.isEmpty()){className(p);return;}
    kw(nk+"<");
    seq(empty(),info.nativePar(),", ");
    c(">");
    };
  void className(T t){className(t.p());}
  void className(P pi){className(p.navigate(pi.toNCs()));}
  void className(Program p){
    var pt=p.pTails;
    String name="";
    while(!pt.isEmpty()){
      assert pt.coreL().info()._uniqueId()!=-1;
      name=pt.c().toString()+pt.coreL().info()._uniqueId()+"£_"+name;
      }
    kw(name);
    }
  void wrap(Boolean b){this.wrap=b;}
  @Override public void visitEX(Core.EX x){
    if(!nativeWrap(g(x.x()))){kw("£_"+x);return;}
    className(g(x.x()));
    c(".wrap(£_"+x+")");
    }
  @Override public void visitPCastT(Core.PCastT pCastT){
    if(pCastT.t().p()!=P.pAny){
      className(pCastT.p());
      c(".instance");
      return;
      }
    kw("Wrap.ofPath(\"");
    className(pCastT.p());
    c("\")");
    }
  @Override public void visitEVoid(Core.EVoid eVoid){
    kw("L42Void.instance");
    }
  @Override public void visitL(Core.L l){
    kw("Wrap.ofLib(\""+Wrap.libName(l));
    c("\")");}

  @Override public void visitMCall(Core.MCall m){
    T t=g(m.xP());
    var mwts=p.ofCore(t.p()).mwts();
    var mh=mwts.stream().filter(mi->mi.key().equals(m.s()))
      .reduce(toOneOr(()->bug())).get().mh();
    T ret=p.from(mh.t(),t.p().toNCs());
    boolean nw=nativeWrap(ret);
    if(nw){
      className(ret);
      c(".wrap(");
      }
    className(t);
    c(".");
    visitS(m.s());
    c("(");
    boolean oldWrap=this.wrap;
    wrap(false);
    visitE(m.xP());
    for(int i:range(m.es())){
      wrap(mh.pars().get(i).p()==P.pAny);
      c(",");
      visitE(m.es().get(i));
      }
    wrap(oldWrap);
    c(")");
    if(nw){c(")");}
    }
  @Override public void visitLoop(Core.Loop loop){throw uc;}
  @Override public void visitThrow(Core.Throw thr){throw uc;}
  @Override public void visitOpUpdate(Core.OpUpdate opUpdate){throw uc;}
  @Override public void visitBlock(Core.Block block){throw uc;}
  @Override public void visitD(Core.D d){throw uc;}
  @Override public void visitK(Core.K k){throw uc;}
  @Override public void visitT(Core.T t){throw uc;}
  @Override public void visitMWT(Core.L.MWT mwt){throw uc;}
  @Override public void visitMH(Core.MH mh){throw uc;}
  @Override public void visitNC(Core.L.NC nc){throw uc;}

  }
