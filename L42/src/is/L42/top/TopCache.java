package is.L42.top;
/*
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import is.L42.generated.Core.L;
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
import is.L42.generated.P.NCs;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.generated.X;
import is.L42.generated.Y;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.translationToJava.J;
import is.L42.translationToJava.Loader;
import is.L42.typeSystem.Coherence;
import is.L42.typeSystem.FlagTyped;
import is.L42.typeSystem.MdfTypeSystem;
import is.L42.typeSystem.PathTypeSystem;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.Accumulate;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.PropagatorCollectorVisitor;
import is.L42.visitors.WellFormedness;
import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.*;
import is.L42.tools.cacheTree.CacheTree;
import is.L42.tools.cacheTree.CacheTree.O;
//import static is.L42.tools.cacheTree.CacheTree.*;

class Static{//to import static
  static LI li(CacheTree.I i){return (LI)i;}
  static EI ei(CacheTree.I i){return (EI)i;}
  static LL pointedLInHalf(CacheTree.I i){
    LI li=(LI)i;
    LL res=_get(li.halfE,li.index);
    assert res!=null;
    return res;
    }
  static LL _get(Half.E e,int index){
    return new Accumulate<LL>(){
      int i=index;
      void setDecrement(LL ll){
        if(i==0){this.result=ll;}
        i-=1;
        }
      @Override public void visitL(Full.L l){setDecrement(l);}
      @Override public void visitL(Core.L l){setDecrement(l);}
      }.of(e.visitable());
    }
  static Half.E set(Half.E e,int index,Core.L coreL){
    return new CloneVisitor(){
      int i=index;
      LL setDecrement(LL ll){
        if(i!=0){i-=1;return ll;}
        i-=1;
        assert ll instanceof Full.L;
        return coreL;
        }
      @Override public LL visitL(Full.L l){return setDecrement(l);}
      @Override public Core.L visitL(Core.L l){return (Core.L)setDecrement(l);}
      }.visitE(e);
    }
  }

class LI implements CacheTree.I{
  Half.E halfE;
  int index;
  @Override public Boolean mayBeEq(CacheTree.I i) {return null;}
  }
class EI implements CacheTree.I{
  Full.L fullL;
  int index;
  @Override public Boolean mayBeEq(CacheTree.I i) {return null;}
  }
class LM implements CacheTree.M{
  @Override public Boolean mayBeEq(CacheTree.M m) {return null;}
  }
class EM implements CacheTree.M{
  @Override public Boolean mayBeEq(CacheTree.M m) {return null;}
  }

class DL implements CacheTree.D{
  @Override public LI i(){return null;}
  @Override public List<DE> ds(){return null;}
  @Override public LM m(){return null;}
  }
class DE implements CacheTree.D{
  @Override public EI i(){return null;}
  @Override public List<DL> ds(){return null;}
  @Override public EM m(){return null;}
  }
interface FunBase extends CacheTree.Fun{
  State state();
  default FunBase complexEq(CacheTree.C c, CacheTree.I i,boolean[] eq) {return null;}
  default FunBase complexEq(CacheTree.C c, CacheTree.M m, boolean[] eq) {return null;}
  default boolean eq(CacheTree.Fun f) {throw bug();}
  default FunBase apply(CacheTree.I i) {throw bug();}
  default CacheTree.R apply(CacheTree.M m, List<CacheTree.O> os) {throw bug();}
  }
interface FunL1 extends FunBase{//  L1:getReuse(LI) -> L3
  default FunL3 apply(CacheTree.I i) {
    LL ll=Static.pointedLInHalf(i);
    State state;
    if(ll instanceof Full.L){
      state=state().copy();
      state.topOpen((Full.L)ll);
      }
    else{state=state();}
    return new FunL3(){@Override public State state(){return state;}};
    }
  }
interface FunL3 extends FunE1,FunL2{}//  L3:dispatch(EI/LM) -> E1/L2
interface FunL2 extends FunBase{//  L2:getMethBodies(LM) -> E3
  default CacheTree.R apply(CacheTree.M m, List<CacheTree.O> os) {
    var state=state().copy();
    try{//TODO: is try catch needed (only) here or can be lifted down in the cacheTree?
      state.topClose();
      var res=new FunE3(){@Override public State state(){return state;}};
      return new CacheTree.R(res,f->true,null);//TODO:
      }
    catch(EndError e){return new CacheTree.R(null,null,e);}
    }
  }
interface FunE1 extends FunBase{//  E1:getFullE(EI) -> E3
  default FunE3 apply(CacheTree.I i) {
    var state=state().copy();
    Half.E he=state.topNCiOpen(Static.ei(i).index,Static.ei(i).fullL);
    return new FunE3(){
      @Override public Half.E he(){return he;}
      @Override public State state(){return state;}
      };
    }
  }
interface FunE3 extends FunL1,FunE2{}//  E3:dispatch(LI/EM) -> L1/E2

interface FunE2 extends FunBase{//  E2:executeE(EM) -> L3

  }
class State{
  public State(FreshNames freshNames,ArrayList<HashSet<List<C>>>alreadyCoherent, int uniqueId,
      ArrayList<SClassFile> allByteCode,ArrayList<L42£Library> allLibs,Path initialPath,CTz ctz,Program p){
    this.freshNames=freshNames;this.alreadyCoherent=alreadyCoherent;this.uniqueId=uniqueId;
    this.allByteCode=allByteCode;this.allLibs=allLibs;this.initialPath=initialPath;this.ctz=ctz;this.p=p;
    }
  final FreshNames freshNames;
  final ArrayList<HashSet<List<C>>> alreadyCoherent;//=new ArrayList<>();
  int uniqueId;
  final ArrayList<SClassFile> allByteCode;
  final ArrayList<L42£Library> allLibs;
  final Path initialPath;
  final CTz ctz;
  Program p;
  private ArrayList<HashSet<List<C>>> copyAlreadyCoherent(){
    var alreadyCoherent2=new ArrayList<HashSet<List<C>>>(alreadyCoherent.size());
    for(var hs:alreadyCoherent){alreadyCoherent2.add(new HashSet<List<C>>(hs));}
    return alreadyCoherent2;
    }
  public State copy(){
    return new State(
      freshNames.copy(),copyAlreadyCoherent(),uniqueId,
      new ArrayList<>(allByteCode),new ArrayList<>(allLibs),
      initialPath,ctz.copy(),p
      );
    }
  void topOpen(Full.L original){
    p=p.push(original);
    alreadyCoherent.add(new HashSet<>());
    assert p.dept()+1>=alreadyCoherent.size(): p.dept()+"!="+alreadyCoherent.size();
    Core.L coreL=SortHeader.coreTop(p,uniqueId++);//propagates the right header errors
    Full.L topL=(Full.L)p.top;
    List<Full.L.M> ms=topL.ms();
    //var info=new HashDollarInfo(topL);//TODO: what to do with this?
    Program p0=p.update(coreL,false);
    //next line, is mhs to be closer to the formalism
    List<MWT> mhs=L(coreL.mwts().stream().filter(mi->_elem(ms, mi.key())!=null));
    List<Half.E> e1n=L(mhs,(c,mhi)->{
      var memi=_elem(ms,mhi.key());
      Full.E _ei=null;
      if(memi!=null){_ei=memi._e();}
      ctzAdd(ctz,p0,mhi.mh(),_ei,c);
      });
    assert p.top.isFullL();
    this.p=p0;
    }
  void topClose(){}
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
  private Core.E infer(I i,CTz ctz,CTz frommed,Half.E e) throws EndError{
    return new InferToCore(i,ctz,this).compute(e);
    }
  public Half.E topNCiOpen(int i,Full.L original){
    List<Full.L.NC> allNCs=typeFilter(original.ms().stream(),Full.L.NC.class);
    NC current=allNCs.get(i);//implicit assert is in range 
    CTz frommedCTz=p.push(current.key(),Program.emptyL).from(ctz,P.pThis1);
    Full.E fe=current.e();
    C c0=current.key();
    System.out.println("Now considering main "+c0+" "+p.topCore().info()._uniqueId());
    List<Full.Doc> docs=current.docs();
    List<Pos> poss=current.poss();
    //var info=new HashDollarInfo(current);//TODO: more stuff to remove/handle?
    Y y=new Y(p,GX.empty(),L(),null,L());
    var  hq=toHalf(ctz,y,freshNames,fe);
    return hq.e;
    }
  public Program topNCiClose(int index,Full.L original,Half.E he){//now we can assume he have no full   
    List<Full.L.NC> allNCs=typeFilter(original.ms().stream(),Full.L.NC.class);
    NC current=allNCs.get(index);//implicit assert is in range 
    CTz frommedCTz=p.push(current.key(),Program.emptyL).from(ctz,P.pThis1);
    C c0=current.key();
    I i=new I(c0,p,G.empty());
    Core.E ce=infer(i,ctz,frommedCTz,he); //propagates errors
    assert ce!=null;
    WellFormedness.of(ce.visitable());
    Deps deps=new Deps();
    P pRes=wellTyped(p,ce,deps,allNCs);//propagate errors //ncs is passed just to provide better errors
    Core.E ce0=adapt(ce,pRes);
    coherentAllPs(p,deps.cohePs); //propagate errors
    //return ce0;}
    Core.E e=ce0;//toCoreTypeCoherent(ctz,frommedCTz,poss,p,c0,fe,allNCs);
    Core.L l=null;
    ArrayList<SClassFile> allByteCode=new ArrayList<>();//TODO: those two will need to be saved for caching
    ArrayList<L42£Library> allLibs=new ArrayList<>();
    if(e instanceof Core.L){l=(Core.L)e;}
    else{l=reduce(p,c0,e,allByteCode,allLibs);}//propagate errors
    //assert loader.bytecodeSize()==allByteCode.size():loader.bytecodeSize()+" "+allByteCode.size();
    System.out.println(c0+ " reduced");
    assert l!=null:c0+" "+e;
    //now, generating the new nc and adding it to the top of the program
    Core.L.NC nc=new Core.L.NC(poss, TypeManipulation.toCoreDocs(docs), c0, l);
    Program p1=p.update(updateInfo(p,e,nc),false);
    //note: we generate also the last round of bytecode to be cache friendly (if a new nested is added afterwards)
    //assert loader.bytecodeSize()==allByteCode.size():loader.bytecodeSize()+" "+allByteCode.size();
    p1=flagTyped(p1,allByteCode,allLibs);//propagate errors
    CTz newCTz=p1.from(frommedCTz,P.of(0,L(c0)));
    //assert loader.bytecodeSize()==allByteCode.size():loader.bytecodeSize()+" "+allByteCode.size();
    return topNC(newCTz,p1,popL(allNCs)); 
    }
  private Core.L reduce(Program p,C c,Core.E e,ArrayList<? super SClassFile> outNewBytecode,ArrayList<? super L42£Library> newLibs)throws EndError {
    try{return loader.runNow(p, c, e,outNewBytecode,newLibs);}
    catch(InvocationTargetException e1){
      if(e1.getCause()instanceof RuntimeException){throw (RuntimeException) e1.getCause();}
            if(e1.getCause()instanceof Error){throw (Error) e1.getCause();} 
      throw new Error(e1.getCause());
      }
    catch(CompilationError e1){throw new Error(e1);}
    }
  private Program flagTyped(Loader loader,Program p1,ArrayList<SClassFile> cBytecode,ArrayList<L42£Library> newLibs) throws EndError{
    Program p=FlagTyped.flagTyped(loader,p1);//but can be overridden as a testing handler
    try {loader.loadNow(p,cBytecode,newLibs);}
    catch (CompilationError e) {throw new Error(e);}
    return p;
    }
  private Core.L updateInfo(Program p1,Core.E source, Core.L.NC nc) {
    //if nc.key().hasUniqueNum() this can cause a type error in the outer (is ok)
    Core.L l=(Core.L)p1.top;
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
  }*/