package is.L42.top;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.GX;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.constraints.InferToCore;
import is.L42.constraints.ToHalf;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.Block;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.Full.L.NC;
import is.L42.generated.Half;
import is.L42.generated.I;
import is.L42.generated.LL;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.generated.X;
import is.L42.generated.Y;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.translationToJava.J;
import is.L42.translationToJava.Loader;
import is.L42.typeSystem.Coherence;
import is.L42.typeSystem.FlagTyped;
import is.L42.typeSystem.MdfTypeSystem;
import is.L42.typeSystem.PathTypeSystem;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.Accumulate;
import is.L42.visitors.WellFormedness;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.*;

public class Top {
  public Program top(CTz ctz, final Program p)throws EndError {
    alreadyCoherent.add(new HashSet<>());
    assert p.dept()+1>=alreadyCoherent.size(): p.dept()+"!="+alreadyCoherent.size();
    Core.L coreL=SortHeader.coreTop(p, uniqueId++);//propagates the right header errors
    List<Full.L.M> ms=((Full.L)p.top).ms();
    List<Full.L.NC> ncs=typeFilter(ms.stream(),Full.L.NC.class);
    Program p0=p.update(coreL,false);
    List<Half.E> e1n=L(coreL.mwts(),(c,mwti)->{
      var memi=_elem(ms,mwti.key());
      Full.E _ei=null;
      if(memi!=null){_ei=memi._e();}
      ctzAdd(ctz,p0,mwti.mh(),_ei,c);
      });
    assert p.top.isFullL();
    String reuse=((Full.L)p.top).reuseUrl();
    boolean isReuse=!reuse.isEmpty();
    if(isReuse){topReuse(ctz,p0,reuse.contains("#$"));}
    Program p1=topNC(ctz,p0,ncs);//propagate exceptions
    assert p1.top instanceof Core.L;
    WellFormedness.of(p1.topCore());//Check the new core top is well formed
    List<Core.E> coreE1n=L(coreL.mwts(),e1n,(c,mwti,_ei)->{
      if(_ei==null){c.add(null);return;}
      Core.E eri=infer(new I(null,p1,G.of(mwti.mh())),ctz,null,_ei);
      c.add(eri);
      });//and propagate errors out
    List<MWT> coreMWTs=L(coreL.mwts(),coreE1n,(c,mwti,_ei)->{//mwt'1..mwt'n
      var memi=_elem(ms,mwti.key());
      String nat="";
      if(memi instanceof Full.L.MWT){nat=((Full.L.MWT)memi).nativeUrl();}
      c.add(mwti.withNativeUrl(nat).with_e(_ei));
      });
    Core.L l=updateInfo(p1,coreMWTs);//mwt'1..mwt'n
    assert l.info()._uniqueId()!=-1;
    Program p2=p1.update(l,false);//propagate illTyped
    l=p2.topCore();
    l=l.withInfo(l.info().with_uniqueId(-1));
    alreadyCoherent.remove(alreadyCoherent.size()-1);
    return p2.update(l,false);
    }
  private Core.L updateInfo(Program p, List<Core.L.MWT>mwts) {
    Core.L l=(Core.L)p.top;
    List<Core.L.MWT> mwts0=L(l.mwts(),(c,m)->{
      var newM=_elem(mwts,m.key());
      if(newM==null){c.add(m);return;}
      assert newM.with_e(null).withNativeUrl("").equals(m);
      });
    assert mwts0.size()+mwts.size()==l.mwts().size();
    ArrayList<P.NCs> typePs=new ArrayList<>();
    ArrayList<P.NCs> cohePs=new ArrayList<>();
    collectDeps(p,mwts,typePs,cohePs,true);
    Info info=Info.empty.withTypeDep(L(typePs.stream())).withCoherentDep(L(cohePs.stream()));
    var allMwts=merge(mwts0,mwts);
    var bridges=WellFormedness.bridge(allMwts);
    var closeState=!WellFormedness.hasOpenState(l.isInterface(),allMwts,bridges);
    Info info1=sumInfo(l.info(),info).withClose(closeState);
    return l.withMwts(allMwts).withInfo(info1);
    }
  public static Info sumInfo(Info info1, Info info2) {
    assert info1._uniqueId()==-1 || info2._uniqueId()==-1;
    assert info1.nativeKind().equals("") || info2.nativeKind().equals("");
    assert info1.nativePar().isEmpty() || info2.nativePar().isEmpty();
    Info res=new Info(info1.isTyped() && info2.isTyped(),
      mergeU(info1.typeDep(),info2.typeDep()),
      mergeU(info1.coherentDep(),info2.coherentDep()),
      mergeU(info1.watched(),info2.watched()),
      mergeU(info1.usedMethods(),info2.usedMethods()),
      mergeU(info1.hiddenSupertypes(),info2.hiddenSupertypes()),
      mergeU(info1.refined(),info2.refined()),
      info1.declaresClassMethods() || info2.declaresClassMethods(),
      info1.close() || info2.close(),
      info1.nativeKind()+info2.nativeKind(),
      info1.nativePar().isEmpty()?info2.nativePar():info1.nativePar(),
      Math.max(info1._uniqueId(),info2._uniqueId())
      );
    if(res.equals(info1)){return info1;}
    if(res.equals(info2)){return info2;}
    return res;
    }
  private Core.L updateInfo(Program p1, Core.L.NC nc) {
    //if nc.key().hasUniqueNum() this can cause a type error in the outer (is ok)
    List<P.NCs>dep=new ArrayList<>();
    collectDepDocs(nc.docs(),dep);
    Core.L l=(Core.L)p1.top;
    var info=l.info();
    info=info.withTypeDep(mergeU(info.typeDep(),dep));
    if(nc.key().hasUniqueNum()){
      var typePs=new ArrayList<P.NCs>();
      var cohePs=new ArrayList<P.NCs>();
      collectDepsE(p1,nc.l(),typePs,cohePs);
      info=info.withTypeDep(mergeU(info.typeDep(),typePs));
      info=info.withCoherentDep(mergeU(info.coherentDep(),cohePs));
      //TODO: add dependencies for watched and others (also need to be done in formalism)
      }
    l=l.withNcs(pushL(l.ncs(),nc)).withInfo(info);
    return l;
    }
  public static void collectDepDocs(List<Doc> docs, List<P.NCs> acc) {
    for(Doc d:docs){
      collectDepDocs(d.docs(),acc);
      if(d._pathSel()==null){continue;}
      if(!d._pathSel().p().isNCs()){continue;}
      acc.add(d._pathSel().p().toNCs());
      }
    }
  private Core.E infer(I i,CTz ctz,CTz frommed,Half.E e) throws EndError{
    return new InferToCore(i,ctz,this).compute(e);
    }
  private final FreshNames freshNames;
  private final ArrayList<HashSet<List<C>>> alreadyCoherent=new ArrayList<>();
  private int uniqueId=0;
  private final Loader loader;
  private int cacheIndex=-1;
  private final Path initialPath;
  private final ArrayList<CacheEntry>cache;
  private boolean validCache=true;
  public Top(FreshNames freshNames, int uniqueId, Loader loader,Path initialPath) {
    this.freshNames=freshNames;
    this.uniqueId=uniqueId;
    this.loader=loader;
    this.initialPath=initialPath;
    if(initialPath!=null){this.cache=CacheEntry.loadCache(initialPath);}
    else{this.cache=new ArrayList<>();}
  }
  private void flushBadCache(){
    assert !validCache;
    for(int i=cache.size()-1;i>=cacheIndex;i-=1){cache.remove(i);}
    }
  private HashMap<String,ClassFile> loadByteCode(Program p){
    var res=new HashMap<String,ClassFile>();
    try{this.loader.loadNow(p,res);}
    catch(CompilationError e){throw new Error(e);}
    return res;
    }
  private void topReuse(CTz ctz,Program p,boolean hasHashDollar){
    cacheIndex+=1;
    CacheEntry c=null;
    if(cacheIndex<cache.size()){c=cache.get(cacheIndex);}
    else{validCache=false;}
    boolean hopeToUseCache=validCache && (!hasHashDollar || !p.topCore().ncs().equals(c.p.topCore().ncs()));
    if(hopeToUseCache){
      loader.loadByteCodeFromCache(c.cByteCode);
      return;
      }
    validCache=false;
    flushBadCache();
    var cByteCode=loadByteCode(p);
    cache.add(new CacheEntry(null,ctz,p,null,cByteCode));
    }
  private Program topNC(CTz ctz, Program p, List<NC> ncs)throws EndError{
    if(ncs.isEmpty()){return p;}
    NC current=ncs.get(0);
    CTz frommedCTz=p.push(current.key(),Program.emptyL).from(ctz,P.pThis1);
    Program p2=topNC1(ctz,p,frommedCTz,current,ncs);
    ncs=popL(ncs);
    Program res=topNC(p2.from(frommedCTz,P.of(0,L(current.key()))),p2,ncs);
    return res; 
    }
  private boolean noHashDollar(NC nc){
    boolean[]res={false};
    return !new Accumulate.SkipL<boolean[]>(){
        @Override public boolean[] empty() {return res;}
        @Override public void visitS(S s){
          if(s.m().startsWith("#$")){acc()[0]=true;}
          }
      }.of(nc.e().visitable())[0];
    }
  private Program topNC1(CTz ctz, Program p,CTz frommedCTz,NC current,List<NC>allNCs){
    C c0=current.key();
    System.out.println("Now considering main "+c0);
    Full.E fe=current.e();
    List<Full.Doc> docs=current.docs();
    List<Pos> poss=current.poss();
    //caching part
    cacheIndex+=1;
    CacheEntry c=null;
    if(cacheIndex<cache.size()){c=cache.get(cacheIndex);}
    else{validCache=false;}
    Core.L l=null;
    assert c==null || c._key!=null;
    boolean hopeToUseCache=validCache
      && c.p.topCore().info()._uniqueId()==p.topCore().info()._uniqueId()
      && c._key.equals(current);
    if(hopeToUseCache){
      if(noHashDollar(current)){
        loader.loadByteCodeFromCache(c.cByteCode);
        return c.p;//TODO:CTZ/frommedCTZ are pointless until cache runs
        }
      assert c._mByteCode!=null;
      loader.loadByteCodeFromCache(c._mByteCode);
      l=this.reduceByName(p,c0);
      Core.L cachedL=_elem(c.p.topCore().ncs(),c0).l();
      if(l.equals(cachedL)){
        loader.loadByteCodeFromCache(c.cByteCode);
        return c.p;//TODO:CTZ/frommedCTZ are pointless until cache runs
        }
      }
    validCache=false;
    HashMap<String,ClassFile> mByteCode=null;
    if(c!=null){mByteCode=c._mByteCode;}
    assert c==null || mByteCode!=null;
    //recover CTz??
    flushBadCache();
    if(l==null){
      Core.E ce0=toCoreTypeCohereht(ctz,frommedCTz,poss,p,c0,fe,allNCs);
      System.out.println("Now reducing main "+c0);
      mByteCode=new HashMap<>();
      l=reduce(p,c0,ce0,mByteCode);//propagate errors
      System.out.println(c0+ " reduced");
      assert l!=null:c0+" "+ce0;
      }
    Core.L.NC nc=new Core.L.NC(poss, TypeManipulation.toCoreDocs(docs), c0, l);
    Program p1=p.update(updateInfo(p,nc),false);
    //TODO: try to understand if we can avoid any bytecode generation here (is this bytecode ever usable?)
    //that is, the last nested class would not generate usable bytecode
    var cByteCode=new HashMap<String,ClassFile>();
    p1=flagTyped(p1,cByteCode);//propagate errors
    //TODO: if error, save current cache on disk
    cache.add(new CacheEntry(current,ctz,p1,mByteCode,cByteCode));
    return p1;  
    }
  private Core.E toCoreTypeCohereht(CTz ctz,CTz frommedCTz,List<Pos>poss,Program p,C c0,Full.E fe,List<NC> allNCs){
      Y y=new Y(p,GX.empty(),L(),null,L());
      var  hq=toHalf(ctz,y,freshNames,fe);
      Half.E he=hq.e;
      I i=new I(c0,p,G.empty());
      Core.E ce=infer(i,ctz,frommedCTz,he); //propagates errors
      assert ce!=null;
      WellFormedness.of(ce.visitable());
      var cohePs=new ArrayList<P.NCs>();
      P pRes=wellTyped(p,ce,cohePs,allNCs);//propagate errors //ncs is passed just to provide better errors
      Core.E ce0=adapt(ce,pRes);
      coherentAllPs(p,cohePs); //propagate errors
      return ce0;    
    }
  protected Program flagTyped(Program p1,HashMap<String,ClassFile> cBytecode) throws EndError{
    return FlagTyped.flagTyped(this.loader,p1,cBytecode);//but can be overridden as a testing handler
    }
  protected Core.L reduce(Program p,C c,Core.E e,HashMap<String,ClassFile> outMapNewBytecode)throws EndError {
    try{return loader.runNow(p, c, e,outMapNewBytecode);}
    catch(InvocationTargetException e1){
      if(e1.getCause()instanceof RuntimeException){throw (RuntimeException) e1.getCause();}
            if(e1.getCause()instanceof Error){throw (Error) e1.getCause();} 
      throw new Error(e1.getCause());
      }
    catch(CompilationError e1){throw new Error(e1);}
    }
  protected Core.L reduceByName(Program p, C c)throws EndError {
    String name="£c"+c;
    if(!p.pTails.isEmpty()){name=J.classNameStr(p)+name;}
    try{return loader.runMainName(name);}
    catch(InvocationTargetException e1){
      if(e1.getCause()instanceof RuntimeException){throw (RuntimeException) e1.getCause();}
            if(e1.getCause()instanceof Error){throw (Error) e1.getCause();} 
      throw new Error(e1.getCause());
      }
    }

  public void coherentAllPs(Program p, List<P.NCs> cohePs)throws EndError{
    Coherence.coherentAllPs(p,cohePs,alreadyCoherent);
    }
  private Core.E adapt(Core.E ce, P path) {
    if(path==P.pLibrary){return ce;}
    X x=new X(freshNames.fresh("main"));
    if(path==P.pVoid){
      Core.D d=new Core.D(false,P.coreVoid,x,ce);
      return new Core.Block(ce.pos(),L(d),L(),Program.emptyL);
      }
    var mCall=new Core.MCall(ce.pos(), new Core.EX(ce.pos(),x),toLibraryS,L());
    Core.D d=new Core.D(false,P.coreAny.withP(path),x,ce);
    return new Core.Block(ce.pos(),L(d),L(),mCall);
    }
  private P wellTyped(Program p, is.L42.generated.Core.E ce,ArrayList<P.NCs> cohePs,List<Full.L.NC>moreNCs)  throws EndError{
    ArrayList<P.NCs> typePs=new ArrayList<>();
    var deps=new Deps(p,typePs,cohePs){@Override public void visitL(Core.L l){return;}};
    deps.of(ce.visitable());
    for(var pi:typePs){
      LL ll=p.of(pi,ce.poss());//propagate errors for path not existent
      Core.L l=(Core.L)ll;
      if(!l.info().isTyped()){
        new CircularityIssue(pi,l,p,ce,moreNCs).reportError();
        }
      }
    List<P> ps=L(P.pAny);
    var g=G.empty();
    var pts=new PathTypeSystem(false,p,g,L(),ps,P.pAny);
    ce.visitable().accept(pts);
    var cmp=pts._computed();
    if(cmp==null){cmp=P.pVoid;}
    ce.visitable().accept(new MdfTypeSystem(p,g,Collections.emptySet(),Mdf.Immutable));
    if(cmp==P.pLibrary || cmp==P.pVoid){return cmp;}
    var l=p._ofCore(cmp);
    var mwt=_elem(l.mwts(),toLibraryS);
    if(mwt==null){
      throw new EndError.TypeError(ce.poss(),
        Err.methodDoesNotExists(toLibraryS,L(l.mwts().stream().map(m->m.key()))));
      }
    if(!mwt.mh().mdf().isIn(Mdf.Immutable,Mdf.Readable)){
      throw new EndError.TypeError(ce.poss(),
        Err.methCallNoCompatibleMdfParametersSignature(toLibraryS,"reciever not in imm,read"));
      }
    return cmp;
    }
  private static final S toLibraryS=S.parse("#toLibrary()");
  private static ToHalf.Res<Half.E> toHalf(CTz ctz,Y y, FreshNames fresh,Full.E fe){
    return new ToHalf(y, ctz, fresh).compute(fe);
    }
  private void ctzAdd(CTz ctz0, Program p, MH mh, Full.E _e, ArrayList<Half.E> es) {
    if(_e==null){es.add(null);return;}
    Y y=new Y(p,GX.of(mh),L(mh.t()),null,L(mh.t()));
    var hq=toHalf(ctz0,y,freshNames,_e);
    ctz0.plusAcc(p,hq.resSTz, L(mh.t()));
    es.add(hq.e);
    }
  static void collectDeps(Program p0, List<MWT> mwts, ArrayList<P.NCs> typePs, ArrayList<P.NCs> cohePs,boolean justBodies) {
    var deps=new Deps(p0,typePs,cohePs);
    if(!justBodies){for(var m:mwts){deps.of(m);}return;}
    for(var m:mwts){if(m._e()!=null){deps.of(m._e().visitable());}}
    }
  static public void collectDepsE(Program p0,Core.E e, ArrayList<P.NCs> typePs, ArrayList<P.NCs> cohePs) {
    var deps=new Deps(p0,typePs,cohePs);
    deps.of(e.visitable());
    }
  }