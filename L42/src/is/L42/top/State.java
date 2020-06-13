package is.L42.top;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.GX;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.constraints.InferToCore;
import is.L42.constraints.ToHalf;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
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
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.translationToJava.Loader;
import is.L42.typeSystem.Coherence;
import is.L42.typeSystem.FlagTyped;
import is.L42.typeSystem.MdfTypeSystem;
import is.L42.typeSystem.PathTypeSystem;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.WellFormedness;

public class State{
  public static Loader loader=new Loader();
  public State(FreshNames freshNames,ArrayList<HashSet<List<C>>>alreadyCoherent, int uniqueId,
      ArrayList<SClassFile> allByteCode,ArrayList<L42£Library> allLibs,Path initialPath){
    this.freshNames=freshNames;this.alreadyCoherent=alreadyCoherent;this.uniqueId=uniqueId;
    this.allByteCode=allByteCode;this.allLibs=allLibs;this.initialPath=initialPath;
    }
  final FreshNames freshNames;
  final ArrayList<HashSet<List<C>>> alreadyCoherent;//=new ArrayList<>();
  int uniqueId;
  final ArrayList<SClassFile> allByteCode;
  final ArrayList<L42£Library> allLibs;

  final Path initialPath;
  private ArrayList<HashSet<List<C>>> copyAlreadyCoherent(){
    var alreadyCoherent2=new ArrayList<HashSet<List<C>>>(alreadyCoherent.size());
    for(var hs:alreadyCoherent){alreadyCoherent2.add(new HashSet<List<C>>(hs));}
    return alreadyCoherent2;
    }
  public State copy(){
    return new State(
      freshNames.copy(),copyAlreadyCoherent(),uniqueId,
      new ArrayList<>(allByteCode),new ArrayList<>(allLibs),
      initialPath);
    }

  Program topOpen(Program p,ArrayList<Half.E>e1n,Map<ST,List<ST>> ctzMap,AtomicReference<Map<ST,List<ST>>> releasedMap){
    assert e1n.isEmpty();
    Resources.notifyCompiledNC("topO:"+p.dept()+",");
    alreadyCoherent.add(new HashSet<>());
    assert p.dept()+1>=alreadyCoherent.size(): p.dept()+"!="+alreadyCoherent.size();
    Core.L coreL=SortHeader.coreTop(p,uniqueId++);//propagates the right header errors
    Full.L topL=(Full.L)p.top;
    List<Full.L.M> ms=topL.ms();
    //var info=new HashDollarInfo(topL);//TODO: what to do with this?
    Program p0=p.update(coreL,false);
    CTz ctz=p0.from(ctzMap,P.pThis1);
    assert ctz.coherent(p0);
    //next line, is mhs to be closer to the formalism
    List<MWT> mhs=L(coreL.mwts().stream().filter(mi->_elem(ms, mi.key())!=null));
    for(var mhi:mhs){
      var memi=_elem(ms,mhi.key());
      Full.E _ei=null;
      if(memi!=null){_ei=memi._e();}
      ctzAdd(ctz,p0,mhi.mh(),_ei,e1n);
      };
    assert p.top.isFullL();
    try{loader.loadNow(p0,allByteCode,allLibs);}
    catch(CompilationError e){throw new Error(e);}
    releasedMap.set(ctz.releaseMap());
    return p0;
    }
  Core.L topClose(Program p1,List<Full.L.M> ms,List<Half.E> e1n,CTz ctz){
    Resources.notifyCompiledNC("topC:"+p1.dept()+",");
    List<MWT> mhs=L(p1.topCore().mwts().stream().filter(mi->_elem(ms, mi.key())!=null));
    assert p1.top instanceof Core.L;
    assert p1.topCore().wf();//Check the new core top is well formed
    List<Core.E> coreE1n=L(mhs,e1n,(c,mhi,_ei)->{
      if(_ei==null){c.add(null);return;}
      Core.E eri=infer(new I(null,p1,G.of(mhi.mh())),_ei,ctz);
      c.add(eri);
      });//and propagate errors out
    List<MWT> coreMWTs=L(mhs,coreE1n,(c,mhi,_ei)->{//mwt'1..mwt'n
      var memi=_elem(ms,mhi.key());
      String nat="";
      if(memi instanceof Full.L.MWT){nat=((Full.L.MWT)memi).nativeUrl();}
      c.add(mhi.withNativeUrl(nat).with_e(_ei));
      });
    Core.L l=updateInfo(p1,coreMWTs);//mwt'1..mwt'n
    assert l.info()._uniqueId()!=-1;
    Program p2=p1.update(l,false);//propagate illTyped
    l=p2.topCore();
    l=l.withInfo(l.info().with_uniqueId(-1));
    alreadyCoherent.remove(alreadyCoherent.size()-1);
    return l;
    }
  private void ctzAdd(CTz ctz0, Program p, MH mh, Full.E _e, ArrayList<Half.E> es) {
    if(_e==null){es.add(null);return;}
    Y y=new Y(p,GX.of(mh),L(mh.t()),null,L(mh.t()));
    var hq=toHalf(ctz0,y,freshNames,_e);
    ctz0.plusAcc(p,hq.resSTz, L(mh.t()));
    es.add(hq.e);
    }
  private static ToHalf.Res<Half.E> toHalf(CTz ctz,Y y, FreshNames fresh,Full.E fe){
    return new ToHalf(y, ctz, fresh).compute(fe);
    }
  private Core.E infer(I i,Half.E e,CTz ctz) throws EndError{
    return new InferToCore(i,ctz,null).compute(e);
    //TODO: may update InferToCore to not do the Full.L any more?
    }
  public Half.E topNCiOpen(Program p,int i,List<Full.L.NC> allNCs,CTz ctz){
    NC current=allNCs.get(i);//implicit assert is in range 
    Full.E fe=current.e();
    C c0=current.key();
    Resources.notifyCompiledNC("NCiO:"+c0+",");
    System.out.println("Now considering main "+c0+" "+p.topCore().info()._uniqueId());
    //var info=new HashDollarInfo(current);//TODO: more stuff to remove/handle?
    Y y=new Y(p,GX.empty(),L(),null,L());
    var  hq=toHalf(ctz,y,freshNames,fe);
    return hq.e;
    }
  public Program topNCiClose(Program p,int index,List<Full.L.NC> allNCs,Half.E he,CTz ctz){//now we can assume he have no full
    NC current=allNCs.get(index);//implicit assert is in range
    List<Pos> poss=current.poss();
    List<Full.Doc> docs=current.docs();
    C c0=current.key();
    Resources.notifyCompiledNC("NCiC:"+c0+",");
    I i=new I(c0,p,G.empty());
    Core.E ce=infer(i,he,ctz); //propagates errors
    assert ce!=null;
    WellFormedness.of(ce.visitable());
    Deps deps=new Deps();
    P pRes=wellTyped(p,ce,deps,allNCs);//propagate errors //ncs is passed just to provide better errors
    Core.E ce0=adapt(ce,pRes);
    coherentAllPs(p,deps.cohePs); //propagate errors
    //return ce0;}
    Core.E e=ce0;//toCoreTypeCoherent(ctz,frommedCTz,poss,p,c0,fe,allNCs);
    Core.L l=null;
    if(e instanceof Core.L){l=(Core.L)e;}
    else{l=reduce(p,c0,e,allByteCode,allLibs);}//propagate errors
    //assert loader.bytecodeSize()==allByteCode.size():loader.bytecodeSize()+" "+allByteCode.size();
    System.out.println(c0+ " reduced");
    assert l!=null:c0+" "+e;
    //now, generating the new nc and adding it to the top of the program
    Core.L.NC nc=new Core.L.NC(poss, TypeManipulation.toCoreDocs(docs), c0, l);
    Program p1=p.update(updateInfo(p,e,nc),false);
    //note: we generate also the last round of bytecode to be cache friendly (if a new nested is added afterwards)
    assert loader.bytecodeSize()==allByteCode.size():loader.bytecodeSize()+" "+allByteCode.size();
    p1=flagTyped(p1);//propagate errors
    assert loader.bytecodeSize()==allByteCode.size():loader.bytecodeSize()+" "+allByteCode.size();
    return p1; 
    }
  public void coherentAllPs(Program p, List<P.NCs> cohePs)throws EndError{
    Coherence.coherentAllPs(p,cohePs,alreadyCoherent);
    }
  protected Program flagTyped(Program p1) throws EndError{
    Program p=FlagTyped.flagTyped(loader,p1);//but can be overridden as a testing handler
    try {loader.loadNow(p,allByteCode,allLibs);}
    catch (CompilationError e) {throw new Error(e);}
    return p;
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
  private P wellTyped(Program p, Core.E ce,Deps deps,List<Full.L.NC>moreNCs)  throws EndError{
    var depsV=deps.new DepsV(p){@Override public void visitL(Core.L l){return;}};
    depsV.of(ce.visitable());
    for(var pi:deps.typePs){
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
  private Core.L reduce(Program p,C c,Core.E e,ArrayList<? super SClassFile> outNewBytecode,ArrayList<? super L42£Library> newLibs)throws EndError {
    try{return loader.runNow(p, c, e,outNewBytecode,newLibs);}
    catch(InvocationTargetException e1){
      if(e1.getCause()instanceof RuntimeException){throw (RuntimeException) e1.getCause();}
            if(e1.getCause()instanceof Error){throw (Error) e1.getCause();} 
      throw new Error(e1.getCause());
      }
    catch(CompilationError e1){throw new Error(e1);}
    }
  private Core.L updateInfo(Program p1,Core.E source, Core.L.NC nc) {
    //if nc.key().hasUniqueNum() this can cause a type error in the outer (is ok)
    Core.L l=p1.topCore();
    if(!(source instanceof LL)){
      nc=nc.withL(new UniqueNsRefresher(true).refreshUniqueNs(nc.l()));
      }
    var info=l.info();
    Deps deps=new Deps().collectDocs(p1,nc.docs());
    if(nc.key().hasUniqueNum()){deps.collectDepsE(p1, nc.l());}
    info=Top.sumInfo(info,deps.toInfo(true));
    l=l.withNcs(pushL(l.ncs(),nc)).withInfo(info);
    return l;
    }
  private Core.L updateInfo(Program p, List<Core.L.MWT>mwts) {
    Core.L l=(Core.L)p.top;
    List<Core.L.MWT> mwts0=L(l.mwts(),(c,m)->{
      var newM=_elem(mwts,m.key());
      if(newM==null){c.add(m);return;}
      });
    assert mwts0.size()+mwts.size()==l.mwts().size();
    Deps collected=new Deps().collectDeps(p,mwts);
    Info info=collected.toInfo(true);
    var allMwts=merge(mwts,mwts0);
    var bridges=WellFormedness.bridge(allMwts);
    var closeState=!WellFormedness.hasOpenState(l.isInterface(),allMwts,bridges);
    Info info1=Top.sumInfo(l.info(),info).withClose(closeState);
    return l.withMwts(allMwts).withInfo(info1);
    }
  @Override public int hashCode(){
    final int prime = 31;
    int result = 1;
    result = prime * result + allByteCode.hashCode();
    result = prime * result + allLibs.hashCode();
    result = prime * result + alreadyCoherent.hashCode();
    result = prime * result + freshNames.hashCode();
    result = prime * result + uniqueId;
    return result;
    }
  @Override public boolean equals(Object obj){//initial path is not considered for equality
    if(this == obj){return true;}
    if(obj == null){return false;}
    if(getClass() != obj.getClass()){return false;}
    State other = (State) obj;
    if(allByteCode.size()!=other.allByteCode.size()){return false;}
    //seams like there is some compiler freedom :( if(!allByteCode.equals(other.allByteCode)){return false;}
    if(allLibs.size()!=other.allLibs.size()){return false;}
    //libs define a complex equality, that eventually check the whole surrunding program, and is ok for those to be different in this context.
    //if(!allLibs.equals(other.allLibs)){return false;}
    if(!alreadyCoherent.equals(other.alreadyCoherent)){return false;}
    if(!freshNames.equals(other.freshNames)){return false;}
    if(uniqueId != other.uniqueId){return false;}
    return true;
    }
  }