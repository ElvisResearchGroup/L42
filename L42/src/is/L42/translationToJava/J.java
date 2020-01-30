package is.L42.translationToJava;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core.D;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L.MWT;
import is.L42.nativeCode.TrustedKind;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.C;
import is.L42.generated.LL;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;
import is.L42.platformSpecific.javaTranslation.L42Library;
import is.L42.typeSystem.Coherence;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.FV;
import is.L42.visitors.ToSTrait;

public class J extends is.L42.visitors.UndefinedCollectorVisitor implements ToSTrait{
  @Override public ToSTrait.ToSState state(){return state;}
  ToSTrait.ToSState state= new ToSTrait.ToSState();

  public J(Program p, G g, boolean wrap,ArrayList<L42Library>libs) {
    this.p=p;
    this.isCoherent=precomputeCoherent();
    this.g=g;
    this.wrap=wrap;
    this.libs=libs;
    }
  public boolean precomputeCoherent(){return new Coherence(p,false).isCoherent(true);}
  final Program p;
  final boolean isCoherent;
  G g;
  boolean wrap;
  ArrayList<X>fwds=new ArrayList<>();
  int catchLev=0;
  int loopLev=0;
  final ArrayList<L42Library>libs;
  Fields fields;
  private String libToMap(Program p){
    libs.add(new L42Library(p));
    return ""+(libs.size()-1);
    }
  public Program p(){return p;}
  T g(Core.XP xP){
    if(xP instanceof Core.EX){return g(((Core.EX)xP).x());}
    var p=(Core.PCastT)xP;
    return p.t();    
    }
  T g(X x){return g.of(x);}
  
  String catchVar(){return "catchVar"+catchLev;}
  String loopVar(){return "loopVar"+loopLev;}
  boolean nativeKind(T t){return !p._ofCore(t.p()).info().nativeKind().isEmpty();}
  boolean nativeKind(Program p){return !p.topCore().info().nativeKind().isEmpty();}
  boolean nativeWrap(T t){return wrap && nativeKind(t);}
  boolean isObjectT(T t){
    return t.p().isNCs() && t.mdf().isIn(Mdf.MutableFwd,Mdf.ImmutableFwd,Mdf.Class) && nativeKind(t);
    }
  void typeName(T t){
    if(!t.p().isNCs()){typeName(t.p());return;}
    if(t.mdf().isClass()){className(p.navigate(t.p().toNCs()));return;}
    if (!t.mdf().isIn(Mdf.MutableFwd,Mdf.ImmutableFwd)){typeName(t.p());return;}
    if(!nativeKind(t)){typeName(t.p());}
    else{kw("Object");}
  };
  public String typeNameStr(P p){
    if(p.isNCs()){
      try{return typeNameStr(this.p.navigate(p.toNCs()));}
      catch(LL.NotInDom nid){
        throw new AssertionError("Not found "+p.toString());
        }
      }
    return "L42"+p;
    }

  void typeName(P p){
    kw(typeNameStr(p));
    }
  public String typeNameStr(Program p){
    var info=p.topCore().info();
    String nk=info.nativeKind();
    if(nk.isEmpty()){return classNameStr(p);}
    return TrustedKind._fromString(nk).typeNameStr(p,this);
    }
  void typeName(Program p){
    kw(typeNameStr(p));
    }
  void className(T t){className(t.p());}
  void className(P p){
    if(p.isNCs()){className(this.p.navigate(p.toNCs()));return;}
    kw("L42"+p.toString());
    }
  void className(Program p){
    kw(J.classNameStr(p));
    }
  public static String classNameStr(Program p){
    var pt=p.pTails;
    String name="";
    while(!pt.isEmpty()){
      String uId="";
      if(pt.coreL().info()._uniqueId()!=-1){uId="£n"+pt.coreL().info()._uniqueId();}
      name="£c"+pt.c().toString()+uId+"£_"+name;
      pt=pt.tail();
      }
    return name.substring(0,name.length()-2);
    }
  public static List<C> classNamePath(Program p){
    return L(c->{
      var pt=p.pTails;
      while(!pt.isEmpty()){
        c.add(0,pt.c());
        pt=pt.tail();
        }
      });
    }    
  void wrap(Boolean b){this.wrap=b;}
  void wrap(P p){
    var l=this.p._ofCore(p);
    assert l!=null:p+" "+this.p;
    wrap(l.isInterface());
    }  
  @Override public void visitEX(Core.EX ex){
    X x=ex.x();
    String tail="";
    if(fwds.contains(x)){tail="£fwd";}
    if(!nativeWrap(g(x))){kw("£x"+x.inner()+tail);return;}
    className(g(x));
    c(".wrap(£x"+x.inner()+tail+")");
    }
  @Override public void visitX(X x){
    kw("£x"+x.inner());
    }
  @Override public void visitPCastT(Core.PCastT pCastT){
    //if(pCastT.t().p()!=P.pAny){
      className(pCastT.p());
      c(".pathInstance");
    /*  return;
      }
    //kw("Resources.ofPath(\"");className(pCastT.p()); c("\")");
    P path=pCastT.p();
    if(path.isNCs()){
      Program p0=p.navigate(path.toNCs());
      kw("Resources.ofPath("+libToMap(p0)+")");
      return;
      }
    if(path==P.pAny){kw("Resources.ofPath(-1)");return;}
    if(path==P.pVoid){kw("Resources.ofPath(-2)");return;}
    assert path==P.pLibrary;
    kw("Resources.ofPath(-3)");*/
    }
  @Override public void visitEVoid(Core.EVoid eVoid){
    kw("L42Void.instance");
    }
  @Override public void visitL(Core.L l){
    kw("Resources.ofLib("+libToMap(p.push(l))+")");
    }

  @Override public void visitMCall(Core.MCall m){
    T t=g(m.xP());
    var lib=p._ofCore(t.p());
    assert lib!=null:t;
    var mwts=lib.mwts();
    var elem=_elem(mwts,m.s());
    assert elem!=null:m.s();
    var mh=elem.mh();
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
      wrap(p.from(mh.pars().get(i).p(),t.p().toNCs()));
      c(",");
      visitE(m.es().get(i));
      }
    wrap(oldWrap);
    c(")");
    if(nw){c(")");}
    }
  @Override public void visitLoop(Core.Loop loop){
    this.loopLev+=1;
    kw("switch(0){default->{if(false)yield Resources.throwE(null);while(true){");
    c("Object "+loopVar()+"=");
    var oldWrap=wrap;
    wrap(false);
    visitE(loop.e());
    wrap(oldWrap);
    c(";}}}");
    this.loopLev-=1;
    }
  @Override public void visitThrow(Core.Throw thr){
    kw("Resources.throwE(");
    var oldWrap=wrap;
    wrap(true);
    c("new L42"+thr.thr()+"(");
    visitE(thr.e());
    wrap(oldWrap);
    c("))");
    }
  @Override public void visitOpUpdate(Core.OpUpdate o){
    kw("Resources.toVoid(£x"+o.x()+"=");
    var oldWrap=wrap;
    wrap(g(o.x()).p());
    visitE(o.e());
    wrap(oldWrap);
    c(")");
    }
  @Override public void visitBlock(Core.Block b){
    if(b.ds().isEmpty() && b.ks().isEmpty()){visitE(b.e());return;}
    indent();
    kw("switch(0){default->{");
    nl();
    var oldG=g;
    g=g.plusEq(b.ds());
    addFwds(b.ds());    
    dec(b.ds());
    if(!b.ks().isEmpty()){kw("try{");indent();nl();}
    visitDs(b.ds());//init
    if(!b.ks().isEmpty()){
      nl();
      c("}");
      deIndent();
      nl();
      catchKs(b.ks());
      }
    kw("yield");
    visitE(b.e());
    c(";");
    g=oldG;
    nl();
    c("}}");
    nl();
    deIndent();
    }
  private void addFwds(List<Core.D>ds) {
    ArrayList<List<X>> fvs=new ArrayList<>();
    for(var di:ds){fvs.add(FV.of(di.e().visitable()));}
    for(int i=ds.size()-1;i>=0;i-=1){
      X xi=ds.get(i).x();
      boolean found=false;
      for(var fv:fvs){if(fv.contains(xi)){found=true;}}
      fvs.remove(i);
      if(found){fwds.add(xi);}
      }
    }
  private void dec(List<D> ds) {
    for(var di:ds){dec(di);}
    }
  private void dec(D d) {
    typeName(d.t());
    kw("£x"+d.x()+"=");
    String nativeKind=p._ofCore(d.t().p()).info().nativeKind();
    if(nativeKind.isEmpty()){c("null");}
    else{c(TrustedKind._fromString(nativeKind).defaultVal());}
    c(";");
    nl();
    if(!fwds.contains(d.x())){return;}
    typeName(d.t().withMdf(Mdf.ImmutableFwd));//so it can be 'Object'
    kw("£x"+d.x()+"£fwd=");
    className(d.t());
    c(".NewFwd();");
    nl();
    }

  @Override public void visitD(Core.D d){//init
    kw("£x"+d.x()+"=");
    var oldWrap=wrap;
    wrap(g(d.x()).p());
    visitE(d.e());
    c(";");
    nl();
    if(fwds.contains(d.x())){
      c("((L42Fwd)£x"+d.x()+"£fwd).fix(£x"+d.x()+");");
      nl();
      }
    wrap(oldWrap);
    fwds.remove(d.x());
    }  
  private void catchKs(List<Core.K> ks){
    catchGroup(ThrowKind.Error,L(ks.stream().filter(k->k.thr()==ThrowKind.Error)));
    catchGroup(ThrowKind.Exception,L(ks.stream().filter(k->k.thr()==ThrowKind.Exception)));
    catchGroup(ThrowKind.Return,L(ks.stream().filter(k->k.thr()==ThrowKind.Return)));
    }
  private void catchGroup(ThrowKind tk,List<Core.K> ks){
    if(ks.isEmpty()){return;}
    kw("catch(L42"+tk+" "+catchVar()+"){");
    indent();nl();
    ks.forEach(this::catchIf);
    c("throw "+catchVar()+";");
    nl();c("}");deIndent();nl();

    }
  private void catchIf(Core.K k){
    kw("if("+catchVar()+".obj42() instanceof ");
    className(k.t());
    c("){");
    indent();nl();
    typeName(k.t());
    kw("£x"+k.x()+"=(");
    boolean eq=!nativeKind(k.t()); 
    if(eq){typeName(k.t()); c(")"+catchVar()+".obj42();");nl();}
    else{c("(");className(k.t()); c(")"+catchVar()+".obj42()).unwrap;");nl();}
    kw("yield");
    G oldG=this.g;
    this.g=oldG.plusEq(k.x(), k.t());
    this.catchLev+=1;
    visitE(k.e());
    this.g=oldG;
    this.catchLev-=1;
    c(";");
    nl();
    c("}");
    deIndent();
    nl();
     }
  @Override public void visitS(S s){
    kw("£m"+(s.m().replace("#", "£h")));
    if(s.hasUniqueNum()){c("£u"+s.uniqueNum());}
    for(var x:s.xs()){c("£x"+x);}
    }
  @Override public void visitT(Core.T t){typeName(t);}
  
  //EXPRESSION PART FINISH, CLASS PART START

  public void mkClass(){
    boolean interf=p.topCore().isInterface();
    String jC = J.classNameStr(p);
    if(interf){kw("interface "+jC+ " extends L42Any");}
    else{kw("class "+jC+ " implements L42Any");}
    for(T ti:p.topCore().ts()){c(", "); visitT(ti);}
    c("{");indent();nl();
    if(this.isCoherent){
      this.fields=new Fields(p);
      for(int i:range(fields.xs)){
        X xi=fields.xs.get(i);
        P pi=fields.ps.get(i);
        typeName(pi);
        kw("£x"+xi.inner());
        c(";");
        nl();
        assert !xi.inner().startsWith(" ");
        c("public static BiConsumer<Object,Object> FieldAssFor_"
          +xi+"=(f,o)->{(("+jC+")o).£x"+xi+"=(");
        typeName(pi);
        c(")f;};");
        nl();
        }
      }      
    visitMWTs(p.topCore().mwts());
    c("public static "+jC+" NewFwd(){return new _Fwd();}");
    nl();
    if(interf){c("public static class _Fwd implements "+jC+", L42Fwd{");}
    else{c("public static class _Fwd extends "+jC+" implements L42Fwd{");}
    indent();nl();
    c("private List<Object> os=new ArrayList<>();");nl();
    c("private List<BiConsumer<Object,Object>> fs=new ArrayList<>();");nl();
    c("public List<Object> os(){return os;}");nl();
    c("public List<BiConsumer<Object,Object>> fs(){return fs;}");nl();
    String myNumber=libToMap(p);
    c("public L42ClassAny asPath(){return Resources.ofPath("+myNumber+");}");nl();
    if(interf){
      for(var mwt:p.topCore().mwts()){
        refineMethHeader(mwt.mh());
        cThrowError();        
        }
      }
    c("}");deIndent();nl();
    c("public static final "+jC+" pathInstance=new _Fwd();");nl();
    if(nativeKind(p)){
      c("public ");
      typeName(p);
      kw("unwrap;");nl();
      c("public static "+jC+" wrap(");
      typeName(p);
      c(" that){"+jC+" res=new "+jC+"();res.unwrap=that;return res;}");nl();
      }
    c("}");deIndent();nl();
    }  
  static class Fields{
    final List<X> xs;
    final List<P> ps;
    final Coherence ch;
    Fields(Program p){
      ch=new Coherence(p,false);
      if(ch.classMhs.isEmpty()){ xs=L(); ps=L();return;}
      xs=ch.classMhs.get(0).s().xs();
      assert p.topCore().isInterface() ||ch.classMhs.stream().allMatch(m->m.s().xs().equals(xs)):xs;
      ps=L(range(xs),(c,i)->{
        List<P> pis=L(ch.classMhs.stream().map(m->m.pars().get(i).p()).distinct());
        if(pis.size()==1){c.add(pis.get(0));}
        else{c.add(P.pAny);}
        });
      assert xs.size()==ps.size();
      }
    }
  @Override public void visitMWT(MWT mwt){//J.meth
    refined(mwt);
    if(p.topCore().isInterface()){c(";");nl();return;}
    g=G.of(mwt.mh());
    wrap(mwt.mh().t().p());
    staticMethHeader(mwt.mh());
    c("{");indent();nl();
    methBody(mwt);
    nl();c("}");deIndent();nl();
    }
  private void methBody(MWT mwt){
    if(!isCoherent){cThrowError();return;}
    if(!mwt.nativeUrl().isEmpty()){
      assert mwt._e()!=null;
      String k=p.topCore().info().nativeKind();
      NativeDispatch.nativeCode(k,mwt,this);
      return;
      }
    if(mwt._e()!=null){
      c("return ");
      visitE(mwt._e());
      c(";");nl();
      return;
      }
    //allowed abstract
    assert this.fields!=null;
    assert this.fields.ch!=null;
    if(this.fields.ch.allowedAbstract(mwt.mh())){
      cThrowError();
      return;
      }
    if(mwt.mh().mdf().isClass()){factoryBody(mwt);return;}
    if(mwt.mh().s().xs().isEmpty()){getterBody(mwt.mh());return;}
    assert mwt.mh().s().xs().size()==1;
    setterBody(mwt.mh()); 
    }
  private void getterBody(MH mh){
    assert this.fields!=null;
    String m=mh.s().m();
    int i=m.lastIndexOf('#');
    m=m.substring(i+1,m.length()); //works also for -1;
    X x=new X(m);
    T t=mh.t();
    int j=fields.xs.indexOf(x);
    assert j!=-1:
      x+" "+fields.xs+" "+mh;
    boolean toCast=!fields.ps.get(j).equals(t.p());
    kw("return ");
    kw("£xthis.");
    if(toCast){c("(");typeName(t);c(")");}
    visitX(x);
    c(";");
    }
  private void setterBody(MH mh){
    String m=mh.s().m();
    int i=m.lastIndexOf('#');
    m=m.substring(i+1,m.length()); //works also for -1;
    kw("£xthis.£x"+m+"=£xthat;return L42Void.instance;");
    }
  private void factoryBody(MWT mwt){
    assert this.fields!=null;
    String kind=p.topCore().info().nativeKind();
    if(!kind.isEmpty()){
      c(NativeDispatch.nativeFactory(this,kind,mwt));
      return;  
      }
    //TODO: here we could add optimization for 0 arg constructors
    //if a (non native) class has no fields, it has a static .instance that is used
    //instead of any constructor, and instead of any placeholder variable
    //thus, variables of those types are never in fwds
    typeName(p);
    kw("Res=new ");
    typeName(p);
    c("();");nl();
    for(int i:range(fields.xs)){
      X xi=fields.xs.get(i);
      T ti=mwt.mh().pars().get(i);
      boolean isFwd=ti.mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd);
      if(isFwd){
        kw("if(");
        visitX(xi);
        kw("instanceof L42Fwd){((L42Fwd)");
        visitX(xi);
        c(").rememberAssign(Res,");
        typeName(p);
        c(".FieldAssFor_"+xi.inner());
        c(");}else{");
        }
      c("Res.");
      visitX(xi);
      c("=");
      if(isObjectT(ti)){c("(");typeName(ti.withMdf(Mdf.Immutable));c(")");}
      visitX(xi);
      c(";");
      if(isFwd){c("}");}
      nl();
      }
      c("return Res;");
    }   
  private void refined(MWT mwt){
    var l=p.topCore();
    MH mh=mwt.mh();
    if(l.isInterface()){
      refineMethHeader(mh);
      c(";");nl();
      staticMethHeader(mh);
      staticMethBody(mh);
      return;
      }
    if(!p.topCore().info().refined().contains(mwt.mh().s())){return;}
    kw("@Override");
    refineMethHeader(mh);
    refineMethBody(mh);
    }
  private void staticMethHeader(MH mh) {
    kw("public static ");
    typeName(mh.t());
    visitS(mh.s());
    c("(");
    if(mh.mdf().isClass()){className(p);}
    else{typeName(p);}
    kw("£xthis");
    for(var i:range(mh.s().xs())){
      c(", ");
      typeName(mh.pars().get(i));
      kw("£x"+mh.s().xs().get(i).inner());
      }
    c(")");
    }
  private void refineMethHeader(MH mh) {
    kw("public ");
    typeName(mh.t());
    visitS(mh.s());
    c("(");
    seq(i->typeName(mh.pars().get(i)),mh.s().xs(),", ");
    c(")");
    }
  private void cThrowError(){
    c("{throw new Error(\"unreachable method body\");}");
    }
  private void staticMethBody(MH mh) {
    if(!isCoherent){cThrowError();return;}
    c("{");indent();nl();
    kw("return");
    kw("£xthis");
    c(".");
    visitS(mh.s());
    c("(");
    seq(empty(),mh.s().xs(),", ");
    c(");");nl();
    c("}");deIndent();nl();
    }
private void refineMethBody(MH mh) {
    if(!isCoherent){cThrowError();return;}
    c("{");indent();nl();
    kw("return");
    className(p);
    c(".");
    visitS(mh.s());
    c("(");
    kw("this");
    if(!p.topCore().info().nativeKind().isEmpty()){c(".unwrap");}
    for(X x:mh.s().xs()){
      c(", ");
      kw("£x"+x.inner());
      }
    c(");");nl();
    c("}");deIndent();nl();
    }
  }
