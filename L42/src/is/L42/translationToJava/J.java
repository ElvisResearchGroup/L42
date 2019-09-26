package is.L42.translationToJava;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;

import java.util.List;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core.D;
import is.L42.generated.Core.T;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.ST;
import is.L42.generated.X;
import is.L42.visitors.FV;
import is.L42.visitors.ToSTrait;
import lombok.NonNull;


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
    if(!nativeWrap(g(x.x()))){kw("£x"+x);return;}
    className(g(x.x()));
    c(".wrap(£x"+x+")");
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
  //static <K> K throwE(Error e){throw e;}
  //static<K> L42Void toVoid(K k){return L42Void.instance;}
  @Override public void visitLoop(Core.Loop loop){
    kw("switchKw(0){defaultKw->{if(false)yield Wrap.throwE(null);whileKw(trueKw)");
    var oldWrap=wrap;
    wrap(false);
    visitE(loop.e());
    wrap(oldWrap);
    c(";}}");
    }
  @Override public void visitThrow(Core.Throw thr){
    kw("Wrap.throwE(");
    var oldWrap=wrap;
    wrap(true);
    visitE(thr.e());
    wrap(oldWrap);
    c(")");
    }
  @Override public void visitOpUpdate(Core.OpUpdate o){
    kw("Wrap.toVoid(£x"+o.x()+"=");
    var oldWrap=wrap;
    wrap(g(o.x()).p()==P.pAny);
    visitE(o.e());
    wrap(oldWrap);
    c(")");
    }
  @Override public void visitBlock(Core.Block b){
    if(b.ds().isEmpty() && b.ks().isEmpty()){visitE(b.e());return;}
    indent();
    kw("switchKw(0){defaultKw->{");
    nl();
    var oldG=g;
    g=g.plusEq(b.ds());
    dec(b.ds());
    if(!b.ks().isEmpty()){kw("try{");}
    visitDs(b.ds());//init
    if(!b.ks().isEmpty()){
      c("}");
      visitKs(b.ks());
      }
    kw("yield");
    visitE(b.e());
    g=oldG;
    nl();
    deIndent();
    }
  private void dec(List<D> ds) {
    List<List<X>> fvs=L(ds,(c,di)->
      c.add(FV.of(di.e().visitable())));
    for(var di:ds){
      X xi=di.x();
      boolean found=false;
      for(var fv:fvs){if(fv.contains(xi)){found=true;}}
      fvs=popL(fvs);
      dec(found,di);
      }
    }
  private void dec(boolean fwd, D d) {
    typeName(d.t());
    kw("£x"+d.x()+"=");
    defaultFor(p.ofCore(d.t().p()).info().nativeKind());
    c(";");
    nl();
    if(!fwd){return;}
    typeName(d.t().withMdf(Mdf.ImmutableFwd));//so it can be 'Object'
    kw("£x"+d.x()+"£fwd=");
    className(d.t());
    c(".NewFwd();");
    nl();
    }
  private void defaultFor(String kind) {
    if(kind.isEmpty()){kw("null");return;}
    //TODO: more
    }
  @Override public void visitD(Core.D d){//init
    throw uc;
    }
  
  @Override public void visitK(Core.K k){throw uc;}
  @Override public void visitT(Core.T t){typeName(t);}
  @Override public void visitMWT(Core.L.MWT mwt){throw uc;}
  @Override public void visitMH(Core.MH mh){throw uc;}
  @Override public void visitNC(Core.L.NC nc){throw uc;}
  }
