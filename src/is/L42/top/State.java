package is.L42.top;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.pushL;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.G;
import is.L42.common.GX;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.constraints.InferExceptions;
import is.L42.constraints.InferToCore;
import is.L42.constraints.ToHalf;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.*;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.MWT;
import is.L42.generated.Full.L.NC;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.JavaCodeStore;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.typeSystem.Coherence;
import is.L42.typeSystem.FlagTyped;
import is.L42.typeSystem.MdfTypeSystem;
import is.L42.typeSystem.PathTypeSystem;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.WellFormedness;

public class State implements Serializable{
  public State(FreshNames freshNames,ArrayList<HashSet<List<C>>>alreadyCoherent, int uniqueId,
      JavaCodeStore allByteCode,ArrayList<L42£Library> allLibs){
    this.freshNames=freshNames;this.alreadyCoherent=alreadyCoherent;this.uniqueId=uniqueId;
    this.allByteCode=allByteCode;this.allLibs=allLibs;
    }
  protected final FreshNames freshNames;
  protected final ArrayList<HashSet<List<C>>> alreadyCoherent;
  protected int uniqueId;
  protected final JavaCodeStore allByteCode;
  protected final ArrayList<L42£Library> allLibs;
  
  protected ArrayList<HashSet<List<C>>> copyAlreadyCoherent(){
    var alreadyCoherent2=new ArrayList<HashSet<List<C>>>(alreadyCoherent.size());
    for(var hs:alreadyCoherent){alreadyCoherent2.add(new HashSet<List<C>>(hs));}
    return alreadyCoherent2;
    }
  public State copy(){
    return new State(freshNames.copy(),copyAlreadyCoherent(),
      uniqueId,this.allByteCode.next(),new ArrayList<>(allLibs));
    }
  TopOpenOut topOpen(Program p,Map<ST,List<ST>> ctzMap){return new TopOpenOut(p,ctzMap);}
  public class TopOpenOut{
    public final ArrayList<Half.E>e1n=new ArrayList<>();
    public final Map<ST,List<ST>> releasedMap;
    public final Program p;
    public TopOpenOut(Program p,Map<ST,List<ST>> ctzMap){
      Resources.notifyCompiledNC("topO:"+p.dept()+",");
      alreadyCoherent.add(new HashSet<>());
      assert p.dept()+1>=alreadyCoherent.size(): p.dept()+"!="+alreadyCoherent.size();
      Full.L topL=(Full.L)p.top;
      CoreL coreL=SortHeader.coreTop(p,uniqueId++);
      Program p0=p.update(coreL,false);
      //CTz ctz=new CTz(ctzMap);//was p0.from(ctzMap,P.pThis1);
      CTz ctz=p0.from(ctzMap,P.pThis0,true);//we are in a new context, where we could do more 'solve' 
      assert ctz.coherent(p0);
      //next line, is called mhs to be closer to the formalism
      List<MWT> mhs=L(coreL.mwts().stream().filter(mi->_elem(topL.ms(), mi.key())!=null));
      for(var mhi:mhs){
        var memi=_elem(topL.ms(),mhi.key());
        Full.E _ei=(memi==null)?null:memi._e();
        ctzAdd(ctz,p0,mhi.mh(),_ei);//adds to e1n
        }
      try{Resources.loader.loadNow(p0,allByteCode,allLibs);}
      catch(CompilationError e){throw new Error(e);}
      this.releasedMap=ctz.releaseMap();
      this.p=p0;
      }
    private void ctzAdd(CTz ctz0, Program p, MH mh, Full.E _e) {
      if(_e==null){e1n.add(null);return;}
      Half.XP self=new Core.EX(_e.pos(),X.thisX);
      Y y=new Y(p,GX.of(mh),L(mh.t()),self,L(mh.t()));
      var hq=new ToHalf(y, ctz0, freshNames).compute(_e);
      ctz0.plusAcc(p,hq.resSTz, L(mh.t()));
      e1n.add(hq.e);
      }
    }
  CoreL topClose(Program p1,List<Full.L.M> ms,List<Half.E> e1n,CTz ctz){
    Resources.notifyCompiledNC("topC:"+p1.dept()+",");
    List<MWT> mhs=L(p1.topCore().mwts().stream().filter(mi->_elem(ms, mi.key())!=null));
    assert p1.top instanceof CoreL;
    assert p1.topCore().wf();
    List<Core.E> coreE1n=L(mhs,e1n,(c,mhi,_ei)->{
      if(_ei==null){c.add(null);return;}
      I i=new I(null,p1,G.of(mhi.mh()));
      c.add(new InferToCore(i,ctz).compute(_ei));
      });//propagate errors out
    List<MWT> coreMWTs=L(mhs,coreE1n,(c,mhi,_ei)->{//mwt'1..mwt'n
      var memi=_elem(ms,mhi.key());
      assert memi!=null;
      String nat=(memi instanceof Full.L.MWT)?((Full.L.MWT)memi).nativeUrl():"";
      c.add(mhi.withNativeUrl(nat).with_e(_ei));
      });
    CoreL l=updateInfo(p1,coreMWTs);//mwt'1..mwt'n
    assert l.info()._uniqueId()!=-1;
    List<S> sz=L(coreMWTs,(c,m)->{
      var _m=_elem(ms,m.key());
      if(_m==null || !(_m instanceof Full.L.MWT)){return;}
      var mwt=(Full.L.MWT)_m;
      if(mwt.mh().infer()){c.add(m.key());}
      });
    var ie=new InferExceptions(p1.update(l,false));
    Program p2=ie.inferExceptions(sz);//propagate illTyped
    alreadyCoherent.remove(alreadyCoherent.size()-1);
    for(var mwti:coreMWTs) {
      Resources.inferenceHandler().mwt(mwti, p2);//TODO: not perfect, we can have methods inside methods :/
      }
    l=p2.topCore();
    l=l.withInfo(l.info().with_uniqueId(-1));
    return l;
    }
  public Half.E topNCiOpen(Program p,int i,List<Full.L.NC> allNCs,CTz ctz){
    NC current=allNCs.get(i);//implicit assert is in range 
    Full.E fe=current.e();
    C c0=current.key();
    Resources.notifyCompiledNC("NCiO:"+c0+",");
    System.err.println("Now considering main "+c0+" "+p.topCore().info()._uniqueId());
    Y y=new Y(p,GX.empty(),L(),null,L());
    var hq=new ToHalf(y, ctz, freshNames).compute(fe);
    return hq.e;
    }
  public Program topNCiClose(Program p,int index,List<Full.L.NC> allNCs,Half.E he,CTz ctz){
    NC current=allNCs.get(index);//implicit assert is in range
    List<Pos> poss=current.poss();
    List<Full.Doc> docs=current.docs();
    C c0=current.key();
    Resources.notifyCompiledNC("NCiC:"+c0+",");
    I i=new I(c0,p,G.empty());
    Core.E ce=new InferToCore(i,ctz).compute(he);//propagates errors
    WellFormedness.of(ce.visitable());
    Deps deps=new Deps();
    P pRes=wellTyped(p,ce,deps,allNCs);//propagate errors //ncs is passed just to provide better errors
    Core.E e=adapt(ce,pRes);
    Coherence.coherentAllPs(p,deps.cohePs,alreadyCoherent);
    Resources.inferenceHandler().nc(e, p);
    CoreL l;
    if(e instanceof CoreL){l=(CoreL)e;}
    else{l=reduce(p,c0,e,allByteCode,allLibs);}//propagate errors
    System.err.println(c0+ " reduced");
    assert l!=null:c0+" "+e;
    //now, generating the new nc and adding it to the top of the program
    Core.NC nc=new Core.NC(poss, TypeManipulation.toCoreDocs(docs), c0, l);
    Program p1=p.update(updateInfo(p,e,nc),false);
    //note: we generate also the last round of bytecode to be cache friendly (if a new nested is added afterwards)
    //assert Resources.loader.bytecodeSize()==allByteCode.size():Resources.loader.bytecodeSize()+" "+allByteCode.size();
    p1=flagTyped(p1);//propagate errors
    //assert Resources.loader.bytecodeSize()==allByteCode.size():Resources.loader.bytecodeSize()+" "+allByteCode.size();
    return p1; 
    }
  protected Program flagTyped(Program p1) throws EndError{
    Program p=FlagTyped.flagTyped(Resources.loader,p1);//but can be overridden as a testing handler
    try {Resources.loader.loadNow(p,allByteCode,allLibs);}
    catch (CompilationError e) {
      var err=ErrMsg.nativeInlinedInvalid(e);
      throw new EndError.InlinedNativeInvalid(List.of(),err);
      }
    return p;
    }
  private Core.E adapt(Core.E ce, P path) {
    if(path==P.pLibrary){return ce;}
    X x=X.of(freshNames.fresh("main"));
    if(path==P.pVoid){
      Core.D d=new Core.D(false,P.coreVoid,x,ce);
      return new Core.Block(ce.pos(),L(d),L(),Program.emptyL);
      }
    var mCall=new Core.MCall(ce.pos(), new Core.EX(ce.pos(),x),toLibraryS,L());
    Core.D d=new Core.D(false,P.coreAny.withP(path),x,ce);
    return new Core.Block(ce.pos(),L(d),L(),mCall);
    }
  private P wellTyped(Program p, Core.E ce,Deps deps,List<Full.L.NC>moreNCs)  throws EndError{
    var depsV=deps.new DepsV(p){@Override public void visitL(CoreL l){return;}};
    depsV.of(ce.visitable());
    new CircularityIssue(deps.typePs,p,ce,moreNCs).checkNotIllTyped();
    var pts=new PathTypeSystem(false,p,G.empty(),L(),L(P.pAny),P.pAny);
    ce.visitable().accept(pts);
    var cmp=pts._computed();
    if(cmp==null){cmp=P.pVoid;}
    ce.visitable().accept(new MdfTypeSystem(p,G.empty(),Collections.emptySet(),Mdf.Immutable));
    if(cmp==P.pLibrary || cmp==P.pVoid){return cmp;}
    var l=p._ofCore(cmp);
    var mwt=_elem(l.mwts(),toLibraryS);
    if(mwt==null){
      throw new EndError.TypeError(ce.poss(),
        ErrMsg.methodDoesNotExists(toLibraryS,l.mwts()));
      }
    if(!mwt.mh().mdf().isIn(Mdf.Immutable,Mdf.Readable)){
      throw new EndError.TypeError(ce.poss(),
        ErrMsg.methCallNoCompatibleMdfParametersSignature(toLibraryS,"reciever not in imm,read"));
      }
    return cmp;
    }
  private static final S toLibraryS=S.parse("#toLibrary()");
  private CoreL reduce(Program p,C c,Core.E e,JavaCodeStore outNewBytecode,ArrayList<? super L42£Library> newLibs)throws EndError {
    try{return Resources.loader.runNow(p, c, e,outNewBytecode,newLibs);}
    catch(InvocationTargetException e1){
      if(e1.getCause()instanceof RuntimeException){throw (RuntimeException) e1.getCause();}
      if(e1.getCause()instanceof Error){throw (Error) e1.getCause();} 
      throw new Error(e1.getCause());
      }
    catch(CompilationError e1){throw new Error(e1);}
    }
  private CoreL updateInfo(Program p1,Core.E source, Core.NC nc) {
    //if nc.key().hasUniqueNum() this can cause a type error in the outer (is ok)
    CoreL l=p1.topCore();
    if(!(source instanceof LL)){
      nc=nc.withL(new UniqueNsRefresher(true).refreshUniqueNs(nc.l()));
      }
    var info=l.info();
    Deps deps=new Deps().collectDocs(p1,nc.docs());
    if(nc.key().hasUniqueNum()){deps.collectDepsE(p1, nc.l());}
    info=info.sumInfo(deps.toInfo(true));
    l=l.withNcs(pushL(l.ncs(),nc)).withInfo(info);
    return l;
    }
  private CoreL updateInfo(Program p, List<Core.MWT>mwts) {
    CoreL l=(CoreL)p.top;
    List<Core.MWT> mwts0=L(l.mwts(),(c,m)->{
      var newM=_elem(mwts,m.key());
      if(newM==null){c.add(m);return;}
      });
    assert mwts0.size()+mwts.size()==l.mwts().size();
    Deps collected=new Deps().collectDeps(p,mwts);
    Core.Info info=collected.toInfo(true);
    var allMwts=merge(mwts,mwts0);
    var bridges=WellFormedness.bridge(allMwts);
    var closeState=!WellFormedness.hasOpenState(l.isInterface(),allMwts,l.ncs(),bridges);
    Core.Info info1=l.info().sumInfo(info).withClose(closeState);
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
    //if(allByteCode.size()!=other.allByteCode.size()){return false;}
    //seams like there is some compiler freedom :( if(!allByteCode.equals(other.allByteCode)){return false;}
    //if(allLibs.size()!=other.allLibs.size()){return false;}
    //libs define a complex equality, that eventually check the whole surrunding program, and is ok for those to be different in this context.
    //if(!allLibs.equals(other.allLibs)){return false;}
    if(!alreadyCoherent.equals(other.alreadyCoherent)){return false;}
    if(!freshNames.equals(other.freshNames)){return false;}
    if(uniqueId != other.uniqueId){return false;}
    return true;
    }
  }