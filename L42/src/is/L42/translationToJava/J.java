package is.L42.translationToJava;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import is.L42.common.G;
import is.L42.common.PTails;
import is.L42.common.Program;
import is.L42.generated.Core.D;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L.MWT;
import is.L42.nativeCode.CacheNowGenerator;
import is.L42.nativeCode.CacheLazyGenerator;
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
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.typeSystem.Coherence;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.FV;
import is.L42.visitors.ToSTrait;

public class J extends is.L42.visitors.UndefinedCollectorVisitor implements ToSTrait{
  @Override public ToSTrait.ToSState state(){return state;}
  ToSTrait.ToSState state= new ToSTrait.ToSState();
  public Coherence newCoherence(Program p) {return new Coherence(p,false);} 
  public J(Program p, G g,ArrayList<L42£Library>libs,boolean forTs){
    this.p=p;
    this.ch=newCoherence(p);
    this.isCoherent=precomputeCoherent();
    if(this.isCoherent){this.fields=new Fields(forTs);}
    this.g=g;
    this.wrap=false;
    this.libs=libs;
    }
  public boolean precomputeCoherent(){return ch.isCoherent(true);}//Can be overridden for testing
  final Program p;
  public final boolean isCoherent;
  public final Coherence ch;
  public boolean cachedClearCacheGood=false;
  G g;
  boolean wrap;
  ArrayList<X>fwds=new ArrayList<>();
  int catchLev=0;
  int loopLev=0;
  final ArrayList<L42£Library>libs;
  public Fields fields;
  private String libToMap(Program p){
    libs.add(new L42£Library(p));
    return ""+(libs.size()-1);
    }
  public Program p(){return p;}  
  String catchVar(){return "catchVar"+catchLev;}
  String loopVar(){return "loopVar"+loopLev;}
  boolean nativeKind(T t){return !p._ofCore(t.p()).info().nativeKind().isEmpty();}
  boolean nativeKind(Program p){return !p.topCore().info().nativeKind().isEmpty();}
  boolean nativeWrap(T t){return wrap && nativeKind(t);}
  boolean isObjectT(T t){
    return t.p().isNCs() && t.mdf().isIn(Mdf.MutableFwd,Mdf.ImmutableFwd,Mdf.Class) && nativeKind(t);
    }
  void typeName(T t){kw(typeNameStr(t));}
  String typeNameStr(T t){
    if(!t.p().isNCs()){return typeNameStr(t.p());}
    if(t.mdf().isClass()){return classNameStr(p.navigate(t.p().toNCs()));}
    if (!t.mdf().isIn(Mdf.MutableFwd,Mdf.ImmutableFwd)){return typeNameStr(t.p());}
    if(!nativeKind(t)){return typeNameStr(t.p());}
    return "Object";
    }
  public String typeNameStr(P p){
    if(p.isNCs()){
      try{return typeNameStr(this.p.navigate(p.toNCs()));}
      catch(LL.NotInDom nid){
        throw new AssertionError("Not found "+p.toString());
        }
      }
    return primitivePToString(p);
    }
  public static String primitivePToString(P p){
    if(p==P.pAny){return "L42Any";}
    if(p==P.pVoid){return "L42£Void";}
    if(p==P.pLibrary){return "L42£Library";}
    throw bug(); 
    }

  void typeName(P p){
    kw(typeNameStr(p));
    }
  public String typeNameStr(Program p){
    var info=p.topCore().info();
    String nk=info.nativeKind();
    if(nk.isEmpty()){return classNameStr(p);}
    return TrustedKind._fromString(nk,p).typeNameStr(p,this);
    }
  void typeName(Program p){
    kw(typeNameStr(p));
    }
  void className(T t){className(t.p());}
  void className(P p){
    if(p.isNCs()){className(this.p.navigate(p.toNCs()));return;}
    kw(primitivePToString(p));
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
      String cName=pt.c().inner();
      if(pt.c().hasUniqueNum()){cName+="£n"+pt.c().uniqueNum();}
      name="£c"+cName+uId+"£_"+name;
      pt=pt.tail();
      }
    if(name.isEmpty()){return name;}
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
    if(!nativeWrap(g.of(x))){kw("£x"+x.inner()+tail);return;}
    className(g.of(x));
    c(".wrap(£x"+x.inner()+tail+")");
    }
  @Override public void visitX(X x){
    kw("£x"+x.inner());
    }
  @Override public void visitPCastT(Core.PCastT pCastT){
      className(pCastT.p());
      c(".pathInstance");
    }
  @Override public void visitEVoid(Core.EVoid eVoid){
    kw("L42£Void.instance");
    }
  @Override public void visitL(Core.L l){
    kw("Resources.ofLib("+libToMap(p.push(l))+")");
    }

  @Override public void visitMCall(Core.MCall m){
    T t=g.of(m.xP());
    var lib=p._ofCore(t.p());
    assert lib!=null:t;
    var mwts=lib.mwts();
    var elem=_elem(mwts,m.s());
    assert elem!=null:
      m.s()+" "+mwts+" "+lib.poss();
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
    wrap(g.of(o.x()).p());
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
    Optional<Core.K> k=locateNonDetermisticError(b.ks());
    if(!b.ks().isEmpty()){kw("try{");indent();nl();}
    if(k.isPresent()){kw("try{");indent();nl();}
    visitDs(b.ds());//init
    if(k.isPresent()) {rethrowNonDeterministicError(k.get());}
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
    else{c(TrustedKind._rawFromString(nativeKind).defaultVal());}
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
    wrap(g.of(d.x()).p());
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
  Optional<Core.K> locateNonDetermisticError(List<Core.K> ks){
    return ks.stream().filter(ki->
      ki.thr()==ThrowKind.Error && isNonDeterministicError(ki.t().p())).findFirst();  
    }
  private boolean isNonDeterministicError(P path){
    String nk=this.p._ofCore(path).info().nativeKind();
    return !nk.isEmpty() && TrustedKind._fromString(nk,p.navigate(path.toNCs())).equals(TrustedKind.NonDeterministicError);
    }
  private void rethrowNonDeterministicError(Core.K k){
    var v=catchVar();
    kw("}catch(Throwable "+v+"){");
    indent();nl();
    c("if("+v+" instanceof L42Throwable){throw (L42Throwable)"+v+";}");
    kw("Resources.throwE(new L42Error(");
    className(k.t().p());
    c(".wrap(new L42£NonDeterministicError("+v+"))");
    c("));");
    nl();c("}");deIndent();nl();
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
  public static String methName(S s){
    var res="£m"+(s.m().replace("#", "£h"));
    if(s.hasUniqueNum()){res+="£n"+s.uniqueNum();}
    for(var x:s.xs()){res+="£x"+x;}
    return res;
    }
  @Override public void visitS(S s){kw(methName(s));}
  @Override public void visitT(Core.T t){typeName(t);}
  
  //EXPRESSION PART FINISH, CLASS PART START
  public String addDotClass(String f){
    if(f.endsWith(">")){f=f.substring(0,f.indexOf("<"));}
    if(f.contains("£")){return f+"._class";}
    return f+".class";
    }
  private void addCachableMethods(String jC,String jCName){
    List<Boolean> is0=L(fields.ps,(c,pi)->c.add(p._ofCore(pi).isInterface()));
    List<String> ps0=fields.psJ;
    List<String> xs=L(fields.xs,(c,xi)->c.add("£x"+xi.inner()));
    if(nativeKind(p)){
      is0=pushL(is0,false);
      ps0=pushL(ps0,typeNameStr(p));
      xs=pushL(xs,"unwrap");
      }
    List<Boolean>is=is0;
    List<String>ps=ps0;
    c("static final Class<"+jC+"> _class="+jC+".class;");nl();
    c("public static final L42StandardCache<"+jC+"> myCache=L42CacheMap.newStandardCache("+jCName+","+jC+"._class);");nl();
    c("static{L42CacheMap.lateInitialize(myCache,new Class<?>[]{");
    seq(range(ps),i->is.get(i)?"null":addDotClass(ps.get(i)),",");
    c("});}");nl();
    c("@Override public L42Cache<"+jC+",?> myCache(){return myCache;}");nl();
    c("private volatile "+jC+" norm;");nl();
    c("@Override public "+jC+" newInstance(){ return new " + jC + "();}");nl();
    c("@Override public void setNorm("+jC+" t){assert this.norm==null;this.norm=t;");
    if(!fields.cacheEager.isEmpty()){
      c("if(t==this){");
      for(var mi:fields.cacheEager){
        String name=CacheLazyGenerator.nameFromS(mi.key());
        c("this."+name+".startEager();");nl();

        }
      c("}");nl();
      }
    c("}");nl();
    c("@Override public "+jC+" myNorm(){return this.norm;}");nl();
    c("@Override public int numFields(){return "+xs.size()+";}");nl();
    c("@Override public Object[] allFields() {return new Object[]{");
    seq(xs,x->x,",");
    c("};}");nl();
    c("@Override public void setField(int i, Object o){switch(i){");indent();nl();
    for(int i:range(xs)){
      c("case "+i+":"+xs.get(i)+"=("+ps.get(i)+")o;return;");nl();
      }
    c("default:throw new ArrayIndexOutOfBoundsException();");nl();deIndent();
    c("}}");nl();
    c("@Override public Object getField(int i) {switch(i){");indent();nl();
    for(int i:range(xs)){
      c("case "+i+":return "+xs.get(i)+";");nl();
      }
    c("default:throw new ArrayIndexOutOfBoundsException();");nl();deIndent();
    c("}}");nl();
    if(needClearCache()){generateClearCache();}
    }
  private boolean needClearCache(){return !fields.xs.isEmpty() && fields.cacheReadLazy.size()+fields.cacheNow.size()!=0;}
  private void generateClearCache() { 
    c("public void clearCache(){");indent();nl();
    for(MWT mwti:fields.cacheReadLazy){
      String name=CacheLazyGenerator.nameFromS(mwti.key());
      c("this."+name+".clear();");nl();
      }
    for(MWT mwti:fields.cacheNow){
      String name=CacheLazyGenerator.nameFromS(mwti.key());
      c("this."+name+"="+name+"(this);");nl();
      }
    c("}");deIndent();nl();

   }
  private void addCachableMethodsNoFields(String jC,String jCName){
    c("static final Class<"+jC+"> _class="+jC+".class;");nl();
    c("public static final L42Cache<"+jC+",?> myCache=L42CacheMap.newSingletonCache("+jCName+","+jC+"._class);");nl();
    c("@Override public L42Cache<"+jC+",?> myCache(){return myCache;}");nl();
    }
  public void header(boolean interf,String jC){
    if(interf){
      kw("public interface "+jC+ " extends L42Any");
      return;
      }
    if(this.isCoherent && (!this.fields.xs.isEmpty() || nativeKind(p))){
      kw("public class "+jC+ " implements L42Any,L42Cachable<"+jC+">");
      return;
      }
    kw("public class "+jC+ " extends L42NoFields<"+jC+"> implements L42Any");
    }
  public String jCName(PTails p){
    if(p.isEmpty()){return "";}
    return jCName(p.tail())+", \""+p.c()+"\"";
    }
  public void mkClass(){
    boolean interf=p.topCore().isInterface();
    String jC = J.classNameStr(p);
    String jCName="new String[]{"+jCName(p.pTails).substring(1)+"}";
    
    header(interf,jC);
    for(T ti:p.topCore().ts()){c(", "); visitT(ti);}
    c("{");indent();nl();
    if(interf){c("static final Class<"+jC+"> _class="+jC+".class;");nl();}
    else{
      boolean useFields=this.isCoherent && (!this.fields.xs.isEmpty() || nativeKind(p));
      if(useFields){addCachableMethods(jC,jCName);}
      else{addCachableMethodsNoFields(jC,jCName);}
      }
    if(this.isCoherent){
      for(int i:range(fields.xs)){
        X xi=fields.xs.get(i);
        kw(fields.psJ.get(i));
        kw("£x"+xi.inner());
        c(";");
        nl();
        assert !xi.inner().startsWith(" ");
        c("public static BiConsumer<Object,Object> FieldAssFor_"
          +xi+"=(f,o)->{(("+jC+")o).£x"+xi+"=(");
        c(fields.psJ.get(i));
        c(")f;};");
        nl();
        }
      }
    visitMWTs(p.topCore().mwts());
    c("public static "+jC+" NewFwd(){return new _Fwd();}");
    nl();
    if(interf){c("public static class _Fwd extends L42NoFields<"+jC+"> implements "+jC+", L42Fwd{");}
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
    c("@Override public L42Cache<"+jC+",?> myCache() {return mySCache;}");
    c("}");deIndent();nl();
    c("public static final "+jC+" pathInstance=new _Fwd();");nl();
    c("static final L42Cache<"+jC+",?> mySCache=L42CacheMap.newSingletonCache("+jCName+", pathInstance.getClass());");nl();
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
  public class Fields{
    final public List<X> xs;
    final public List<P> ps;
    final public List<String> psJ;
    final public ArrayList<MWT> cacheReadLazy=new ArrayList<>();
    final public ArrayList<MWT> cacheEager=new ArrayList<>();
    final public ArrayList<MWT> cacheNow=new ArrayList<>();
    public Fields(boolean forTs){
      if(ch.classMhs.isEmpty()|| p.topCore().isInterface()){ xs=L(); ps=L();psJ=L();return;}
      xs=ch.classMhs.get(0).s().xs();
      assert forTs || ch.classMhs.stream().allMatch(m->m.s().xs().equals(xs)) : xs +" "+ch.classMhs;
      ps=L(range(xs),(c,i)->{
        List<P> pis=L(ch.classMhs.stream().map(m->m.pars().get(i).p()).distinct());
        if(pis.size()==1){c.add(pis.get(0));}
        else{c.add(P.pAny);}
        });
      if(!forTs){psJ=L(ps,(c,pi)->c.add(typeNameStr(pi)));}
      else{psJ=null;}
      assert xs.size()==ps.size();
      for(MWT mwti:p.topCore().mwts()){
        if(mwti.nativeUrl().isEmpty()){continue;}
        if(mwti.nativeUrl().equals("trusted:lazyCache") && mwti.mh().mdf().isRead()){cacheReadLazy.add(mwti);}
        if(mwti.nativeUrl().equals("trusted:readNowCache")){cacheNow.add(mwti); assert forTs||mwti.mh().mdf().isRead();}
        if(mwti.nativeUrl().equals("trusted:eagerCache")){cacheEager.add(mwti);}
        }
      }
    }
  @Override public void visitMWT(MWT mwt){//J.meth
    refined(mwt);
    if(p.topCore().isInterface()){c(";");nl();return;}
    g=G.of(mwt.mh());
    wrap(mwt.mh().t().p());
    var mh=staticMethHeaderStr(mwt.mh());
    c(mh.get(0));
    c("{");indent();nl();
    methBody(mwt);
    nl();c("}");deIndent();nl();
    if(mh.size()==1){return;}
    c(mh.get(1));
    c("{");indent();nl();
    c("return ("+typeNameStr(mwt.mh().t().p())+")");
    visitS(mwt.key());
    c("(£xthis");//the first parameter was never a fwd
    for(var i:range(mwt.key().xs())){
      var xi="£x"+mwt.key().xs().get(i).inner();
      var ci="";
      if(typeNameStr(mwt.mh().pars().get(i)).equals("Object")){ci="(Object)";}
      c(", "+ci+xi);
      }
    c(");");
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
    if(CacheNowGenerator.isCapsuleMutator(mwt,this) && needClearCache()){
      c("var Res=");
      visitE(mwt._e());
      c(";");    
      c("£xthis.clearCache();");
      c("return Res;");
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
    assert this.ch!=null;
    if(this.ch.allowedAbstract(mwt.mh())){
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
    if(needClearCache()){
      kw("£xthis.£x"+m+"=£xthat;£xthis.clearCache();return L42£Void.instance;");
      }
    else{kw("£xthis.£x"+m+"=£xthat;return L42£Void.instance;");}    
    }
  private void factoryBody(MWT mwt){
    assert this.fields!=null;
    String kind=p.topCore().info().nativeKind();
    if(!kind.isEmpty()){
      c(NativeDispatch.nativeFactory(this,kind,mwt));
      return;  
      }
    if(fields.xs.isEmpty()){c("assert pathInstance!=null; return pathInstance;");}
    else{newAndFwd(mwt);}
    }
  private void newAndFwd(MWT mwt){
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
        kw("instanceof L42Fwd && !(((Object)");
        visitX(xi);
        kw(") instanceof L42NoFields<?>)){((L42Fwd)");
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
    for(MWT mwti:fields.cacheNow){
      String name=CacheLazyGenerator.nameFromS(mwti.key());
      c("Res."+name+"="+name+"(Res);");nl();
      }     
    c("return Res;"); 
    }   
  private void refined(MWT mwt){
    var l=p.topCore();
    MH mh=mwt.mh();
    if(l.isInterface()){
      refineMethHeader(mh);
      c(";");nl();
      var mhS=staticMethHeaderStr(mh);
      c(mhS.get(0));
      staticMethBody(mh);
      if(mhS.size()==1){return;}
      c(mhS.get(1));
      staticMethBody(mh);
      return;
      }
    if(!p.topCore().info().refined().contains(mwt.mh().s())){return;}
    kw("@Override");
    refineMethHeader(mh);
    refineMethBody(mh);
    }
  private void staticMethHeader(MH mh) {
    var res=staticMethHeaderStr(mh);
    assert res.size()==1;
    c(res.get(0));
    }
  private List<String> staticMethHeaderStr(MH mh) {
    var retT=typeNameStr(mh.t());
    var ts=L(mh.pars().stream().map(ti->typeNameStr(ti)));
    var one=!retT.equals("Object") || !ts.contains("Object");
    if(one){return L(staticMethHeader(mh, retT,ts,false));}
    return List.of(staticMethHeader(mh, retT,ts,false),staticMethHeader(mh, retT,ts,true));
    }
  private String staticMethHeader(MH mh,String retT,List<String>ts,boolean noFwd){
    if(noFwd){retT=typeNameStr(mh.t().p());}
    var res="public static "+retT+" "+methName(mh.s())+"(";
    if(mh.mdf().isClass()){res+=classNameStr(p);}
    else{res+=typeNameStr(p);}
    res+=" £xthis";
    for(var i:range(mh.s().xs())){
      res+=", "
        +(noFwd?typeNameStr(mh.pars().get(i).p()):ts.get(i))
        +" £x"+mh.s().xs().get(i).inner();
      }
    return res+=")";
    }
  private void refineMethHeader(MH mh) {
    kw("public ");
    if(refineNative(mh.t(),mh.s())){className(mh.t());}
    else{typeName(mh.t());}
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
    boolean unwrap=!p.topCore().info().nativeKind().isEmpty() && !mh.mdf().isClass();
    boolean wrap=refineNative(mh.t(),mh.s());
    if(!isCoherent){cThrowError();return;}
    c("{");indent();nl();
    kw("return");
    if(wrap){className(p._navigate(mh.t().p().toNCs()));c(".wrap(");}
    className(p);
    c(".");
    visitS(mh.s());
    c("(");
    kw("this");if(unwrap){c(".unwrap");}
    for(X x:mh.s().xs()){
      c(", ");
      kw("£x"+x.inner());
      }
    if(wrap){c(")");}
    c(");");nl();
    c("}");deIndent();nl();
    }
  private boolean refineNative(T t,S s) {
    if(p._ofCore(t.p()).info().nativeKind().isEmpty()) {return false;}
    for(T ti:p.topCore().ts()){
      Program pi=p.navigate(ti.p().toNCs());
      var m=_elem(pi.topCore().mwts(),s);
      if(m==null){continue;}
      if(pi._ofCore(m.mh().t().p()).isInterface()) {return true;}
      }
    return false;
    }
  
  public static String boxed(String name) {
    switch(name){
      case "float":return "Float";
      case "double":return "Double";
      case "boolean":return "Boolean";
      case "char":return "Char";
      case "byte":return "Byte";
      case "short":return "Short";
      case "int":return "Integer";
      case "long":return "Long";
      }
    return name;
    }
  }
