package is.L42.top;

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
import is.L42.generated.Cache;
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
import is.L42.generated.P.NCs;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.generated.X;
import is.L42.generated.Y;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import is.L42.platformSpecific.javaTranslation.L42Library;
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
import is.L42.visitors.WellFormedness;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.*;

public class Top {
/*
top:
  sortHeader
  flagTyped
  topNCs
  inferMeth and Info
topNC1:
  full->half->core
  type
  coherent
  reduce
  flagTyped

-Presence/absence of #$ is cached, thus is about the old version

top:
              allNo#$                  reuseNo#$           bodyNo#$           bothHave#$
allEq         useCache                 reuseHeader         checkHeaderEq      stepByStep
eqReuse+Meth  reuseHeader*             reuseHeader*        stepByStep*        stepByStep*
eqNCs         dropCache                dropCache           dropCache          dropCache 
allDiff       dropCache                dropCache           dropCache          dropCache

* == assert !validCache after topNCs
useCache:
  just return the cached result
reuseHeader: 
  import header from cache, call topNCs, if validCache, just return the cached result
  else recompute inferMeth and Info and return new cache
checkHeaderEq:
  l=sortHeader; if l==cache.sortHeader, just return the cached result
  else cacheIsInvalid, flagTyped;  topNCs;  inferMeth and Info
stepByStep:
  l=sortHeader; if l==cache.sortHeader, reuseHeader
  else cacheIsInvalid, flagTyped;  topNCs;  inferMeth and Info
dropCache:
  cacheIsInvalid, sortHeader; flagTyped;  topNCs;  inferMeth and Info

topNC1:
                  no#$               e#$               Ls#$            both#$
  allEq           useCache           rerunE            rerunI          rerunEI
  ExprEq,LDiff    rerunI*            rerunEI*          rerunI*         rerunEI*
  allDiff         dropCache          dropCache         dropCache       dropCache

useCache:
  just return the cached result
rerunE:
  l=run cache.core_e, if l==cache.l just return the cached result
  else cacheIsInvalid, flagTyped
rerunI:
  core_e=full->half->core, if cache still valid (assert core_e==cache.core_e)  just return the cached result
  else update libs, l=run core_e (old bytecode, new libs), flagTyped  
rerunEI:
  core_e=full->half->core, if cache still valid (assert core_e==cache.core_e) rerunE
  else update libs, l=run core_e (old bytecode, new libs), flagTyped

dropCache:
  cacheIsInvalid, full->half->core;  type;  coherent;  reduce;  flagTyped
*/

  public Cache.CTop topCache(Program p, Cache.CTop ctop){
    assert p.dept()==ctop.in().p().dept();
    assert ctop.in().p().top.isFullL();
    assert validCache;
    var cachedTop=(Full.L)ctop.in().p().top;
    var cachedTopReuseMeth=cachedTop.withMs(L(cachedTop.ms().stream().filter(Full.L.NC.class::isInstance)));
    var newTop=(Full.L)p.top;
    var newTopReuseMeth=newTop.withMs(L(newTop.ms().stream().filter(Full.L.NC.class::isInstance)));
    boolean eqReuseMeth=newTopReuseMeth.equals(cachedTopReuseMeth);
    boolean allEq=eqReuseMeth && newTop.equals(cachedTop);
    if(!eqReuseMeth){return dropCache(p,ctop);}
    boolean reuseHD=cachedTop.reuseUrl().contains("#$");
    if(allEq){
      if(!reuseHD && !ctop.hasHDDeep()){return ctop;}
      if(!reuseHD){return reuseHeader(true,p,ctop.hasHDDeep(),ctop);}
      if(!ctop.hasHDDeep()){return checkHeaderEq(p,ctop);}
      return stepByStep(true,p,ctop);
      }
    if(!reuseHD){return reuseHeader(false,p,null,ctop);}
    return stepByStep(false,p,ctop);
    }   
  public Cache.CTop reuseHeader(boolean hope,Program p,Boolean oldInfo,Cache.CTop ctop){ 
    Core.L coreL=ctop.sortedHeader();
    Full.L topL=(Full.L)p.top;
    List<Full.L.M> ms=topL.ms();
    List<Full.L.NC> ncs=typeFilter(ms.stream(),Full.L.NC.class);
    boolean deepHD;if(oldInfo!=null){deepHD=oldInfo;}
    else{
      var info=new HashDollarInfo(topL);
      assert !info.hashDollarTop;
      deepHD=info.hashDollarInside;
      } 
    var currentState=new Cache.InOut(loader.bytecodeSize(),loader.libsCachedSize(),ctop.in().ctz(),ctop.in().coherentList(),p);
    var newCache=new Cache.CTop(currentState, null, null, deepHD, coreL, -1, -1);
    Program p0=p.update(coreL,false);
    assert p.top.isFullL();
    var subB=cache.allByteCode().subList(loader.bytecodeSize(),ctop.nHByteCode());
    var subL=cache.allLibs().subList(loader.libsCachedSize(),ctop.nHlibs());
    this.loader.loadByteCodeFromCache(subB,subL);
    ArrayList<Cache.CTopNC1> cachedNCs=topNCCache(ctop.ncs(),p0,ncs);//propagate exceptions
    newCache.ncs(cachedNCs);
    Program p1=cachedNCs.isEmpty()?p0:cachedNCs.get(cachedNCs.size()-1).out().p();
    assert p1.top instanceof Core.L;
    if(validCache){return ctop;}
    assert !cachedNCs.isEmpty();
    CTz ctz=cachedNCs.get(cachedNCs.size()-1).out().ctz().copy();
    List<Half.E> e1n=L(coreL.mwts(),(c,mwti)->{//duplicated
      var memi=_elem(ms,mwti.key());
      Full.E _ei=null;
      if(memi!=null){_ei=memi._e();}
      ctzAdd(ctz,p0,mwti.mh(),_ei,c);
      });
    //identical to topNoCache from now on
    WellFormedness.of(p1.topCore());//Check the new core top is well formed
    List<Core.E> coreE1n=L(coreL.mwts(),e1n,(c,mwti,_ei)->{
      if(_ei==null){c.add(null);return;}
      Core.E eri=infer(new I(null,p1,G.of(mwti.mh())),ctz,null,_ei,null);
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
    Program p3=p2.update(l,false);
    var outState=new Cache.InOut(loader.bytecodeSize(),loader.libsCachedSize(),ctz.copy(),cachableAlreadyCoherent(),p3);
    newCache.out(outState);
    return newCache;
    //cacheIsInvalid, sortHeader; flagTyped;  topNCs;  inferMeth and Info

    //import header from cache, call topNCs, if validCache, just return the cached result
    //else recompute inferMeth and Info and return new cache
    }
  public Cache.CTop checkHeaderEq(Program p,Cache.CTop ctop){throw todo();
    //l=sortHeader; if l==cache.sortHeader, just return the cached result
    //else cacheIsInvalid, flagTyped;  topNCs;  inferMeth and Info
    }
  public Cache.CTop stepByStep(boolean hope,Program p,Cache.CTop ctop){throw todo();
    //l=sortHeader; if l==cache.sortHeader, reuseHeader
    //else cacheIsInvalid, flagTyped;  topNCs;  inferMeth and Info
    }
  public Cache.CTop dropCache(Program p,Cache.CTop toDrop){
    assert toDrop==null||p.dept()==toDrop.in().p().dept();
    CTz ctz;
    alreadyCoherent.clear();
    if(toDrop==null){ctz=new CTz();}
    else{
      ctz=setInvalid(toDrop.in());
      for(var e:toDrop.in().coherentList()){alreadyCoherent.add(new HashSet<>(e));}
      }    
    return topNoCache(ctz,p);
    }
  public Cache.CTop topNoCache(CTz ctz,Program p){
    alreadyCoherent.add(new HashSet<>());
    assert !validCache;
    assert p.dept()+1>=alreadyCoherent.size():
     p.dept()+"!="+alreadyCoherent.size();
    Core.L coreL=SortHeader.coreTop(p,uniqueId++);//propagates the right header errors
    Full.L topL=(Full.L)p.top;
    List<Full.L.M> ms=topL.ms();
    List<Full.L.NC> ncs=typeFilter(ms.stream(),Full.L.NC.class);
    var info=new HashDollarInfo(topL);
    System.out.println(topL.pos());
    System.out.println(info.hashDollarTop);
    System.out.println(info.hashDollarInside);
    var currentState=new Cache.InOut(loader.bytecodeSize(),loader.libsCachedSize(),ctz.copy(),cachableAlreadyCoherent(),p);
    var newCache=new Cache.CTop(currentState, null, null, info.hashDollarInside, coreL, -1, -1);
    Program p0=p.update(coreL,false);
    List<Half.E> e1n=L(coreL.mwts(),(c,mwti)->{
      var memi=_elem(ms,mwti.key());
      Full.E _ei=null;
      if(memi!=null){_ei=memi._e();}
      ctzAdd(ctz,p0,mwti.mh(),_ei,c);
      });
    assert p.top.isFullL();
    assert loader.bytecodeSize()==cache.allByteCode().size():loader.bytecodeSize()+" "+cache.allByteCode().size();
    assert loader.libsCachedSize()==cache.allLibs().size();
    try{this.loader.loadNow(p0,cache.allByteCode(),cache.allLibs());}
    catch(CompilationError e){throw new Error(e);}
    assert loader.bytecodeSize()==cache.allByteCode().size();
    assert loader.libsCachedSize()==cache.allLibs().size();
    newCache.nHByteCode(loader.bytecodeSize());
    newCache.nHlibs(loader.libsCachedSize());
    ArrayList<Cache.CTopNC1> cachedNCs=topNCNoCache(ctz,p0,ncs);//propagate exceptions
    assert loader.bytecodeSize()==cache.allByteCode().size():loader.bytecodeSize()+" "+cache.allByteCode().size();
    newCache.ncs(cachedNCs);
    Program p1=cachedNCs.isEmpty()?p0:cachedNCs.get(cachedNCs.size()-1).out().p();
    assert p1.top instanceof Core.L;
    WellFormedness.of(p1.topCore());//Check the new core top is well formed
    List<Core.E> coreE1n=L(coreL.mwts(),e1n,(c,mwti,_ei)->{
      if(_ei==null){c.add(null);return;}
      Core.E eri=infer(new I(null,p1,G.of(mwti.mh())),ctz,null,_ei,null);
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
    Program p3=p2.update(l,false);
    var outState=new Cache.InOut(loader.bytecodeSize(),loader.libsCachedSize(),ctz.copy(),cachableAlreadyCoherent(),p3);
    newCache.out(outState);
    return newCache;
    //cacheIsInvalid, sortHeader; flagTyped;  topNCs;  inferMeth and Info
    }
//--------------
  private CloneVisitor noL(){return new CloneVisitor(){
      @Override public LL visitL(Full.L l){return Program.emptyL;}
      @Override public Core.L visitL(Core.L l){return Program.emptyL;}
    };} 
  public ArrayList<Cache.CTopNC1> topNCCache(ArrayList<Cache.CTopNC1> cnc,Program p,List<Full.L.NC> ncs){ 
    var res=new ArrayList<Cache.CTopNC1>();
    int cacheUsedUpTo=cnc.size();
    for(var i:range(cnc)){
      res.add(topNC1Cache(cnc.get(i),p,ncs.get(i)));
      if(!validCache){cacheUsedUpTo=i;break;}
      }
    if(ncs.size()>cacheUsedUpTo){
      var last=cnc.get(cacheUsedUpTo);
      CTz ctz=setInvalid(last.out());
      res.addAll(topNCNoCache(ctz,last.out().p(),ncs.subList(cacheUsedUpTo, ncs.size())));
      }
    return res;
    }   
  public Cache.CTopNC1 topNC1Cache(Cache.CTopNC1 cnc,Program p,Full.L.NC nc){
    var newNoLibs=noL().visitNC(nc);
    var cachedNoLibs=noL().visitNC(cnc.ncIn());
    boolean eqExpr=newNoLibs.equals(cachedNoLibs);
    boolean allEq=eqExpr && nc.equals(cnc.ncIn());
    if(!eqExpr){return dropCacheNC1(p,nc);}
    if(allEq){
      if(!cnc.hasHDL() && !cnc.hasHDE()){return cnc;}
      if(!cnc.hasHDL()){return rerunE(p,nc,cnc);}
      if(!cnc.hasHDE()){return rerunI(true,p,nc,cnc);}
      return rerunEI(true,p,nc,cnc);
      }
    if(cnc.hasHDE()){return rerunEI(false,p,nc,cnc);}
    return rerunI(false,p,nc,cnc);
    }
  public Cache.CTopNC1 rerunE(Program p,Full.L.NC nc, Cache.CTopNC1 cnc){
    throw todo();
    /*var subB=cache.allByteCode().subList(loader.bytecodeSize(),cnc.in().nByteCode());
    var subL=cache.allLibs().subList(loader.libsCachedSize(),cnc.in().nLibs());
    this.loader.loadByteCodeFromCache(subB,subL);
    return reduceNoCache(p,nc.key(),cnc.coreE(),res,nc.docs(),nc.poss(),frommedCTz);
    *///l=run cache.core_e, if l==cache.l just return the cached result
    //else cacheIsInvalid, flagTyped
    }
  public Cache.CTopNC1 rerunI(boolean hope,Program p,Full.L.NC current, Cache.CTopNC1 cnc){
    CTz ctz=cnc.in().ctz().copy();
    CTz frommedCTz=p.push(current.key(),Program.emptyL).from(ctz,P.pThis1);
    Full.E fe=current.e();
    C c0=current.key();
    System.out.println("RerunI for "+c0+" "+p.topCore().info()._uniqueId());
    List<Full.Doc> docs=current.docs();
    List<Pos> poss=current.poss();
    var in=cnc.in();
    var res=new Cache.CTopNC1(new ArrayList<>(cnc.tops()),in,null,cnc.hasHDE(),cnc.hasHDL(),current,null,null);
    Core.E e=toCoreTypeCohereht(ctz,frommedCTz,poss,p,c0,fe,/*allNCs*/null,res.tops());
    if(this.validCache){
      assert e.equals(cnc.coreE());
      return cnc; 
      }
    assert hope;
    var subB=cache.allByteCode().subList(loader.bytecodeSize(),cnc.in().nByteCode());
    var subL=cache.allLibs().subList(loader.libsCachedSize(),cnc.in().nLibs());
    this.loader.loadByteCodeFromCache(subB,subL);
    return reduceNoCache(p,c0,e,res,docs,poss,frommedCTz);
    //core_e=full->half->core, if cache still valid (assert core_e==cache.core_e)  just return the cached result
    //else update libs, l=run core_e (old bytecode, new libs), flagTyped
    }  
  public Cache.CTopNC1 rerunEI(boolean hope,Program p,Full.L.NC nc, Cache.CTopNC1 cnc){throw todo();
    //core_e=full->half->core, if cache still valid (assert core_e==cache.core_e) rerunE
    //else update libs, l=run core_e (old bytecode, new libs), flagTyped
    }
  public Cache.CTopNC1 dropCacheNC1(Program p,Full.L.NC nc){throw todo();
    //cacheIsInvalid, full->half->core;  type;  coherent;  reduce;  flagTyped
    }
  public ArrayList<Cache.CTopNC1> topNCNoCache(CTz ctz, Program p, List<Full.L.NC> ncs){
    if(ncs.isEmpty()){return new ArrayList<>();}
    NC current=ncs.get(0);
    Cache.CTopNC1 c=topNC1NoCache(ctz,p,current,ncs);
    ncs=popL(ncs);
    CTz newCTz=c.out().ctz().copy();
    var res=topNCNoCache(newCTz,c.out().p(),ncs);
    res.add(0,c);
    return res; 
    }
  public Cache.CTopNC1 topNC1NoCache(CTz ctz, Program p,Full.L.NC current,List<Full.L.NC> allNCs){
    assert loader.bytecodeSize()==cache.allByteCode().size():loader.bytecodeSize()+" "+cache.allByteCode().size();
    CTz frommedCTz=p.push(current.key(),Program.emptyL).from(ctz,P.pThis1);
    Full.E fe=current.e();
    C c0=current.key();
    System.out.println("Now considering main "+c0+" "+p.topCore().info()._uniqueId());
    List<Full.Doc> docs=current.docs();
    List<Pos> poss=current.poss();
    var in=new Cache.InOut(loader.bytecodeSize(),loader.libsCachedSize(), ctz.copy(), cachableAlreadyCoherent(), p);
    var info=new HashDollarInfo(current);
    var res=new Cache.CTopNC1(new ArrayList<>(),in,null,info.hashDollarTop,info.hashDollarInside,current,null,null);
    Core.E e=toCoreTypeCohereht(ctz,frommedCTz,poss,p,c0,fe,allNCs,res.tops());
    return reduceNoCache(p,c0,e,res,docs,poss,frommedCTz);
    }
  Cache.CTopNC1 reduceNoCache(Program p, C c0, Core.E e, Cache.CTopNC1 res, List<Full.Doc> docs, List<Pos> poss,CTz frommedCTz){
    assert loader.bytecodeSize()==cache.allByteCode().size():loader.bytecodeSize()+" "+cache.allByteCode().size();
    Core.L l=reduce(p,c0,e,cache.allByteCode(),cache.allLibs());//propagate errors
    assert loader.bytecodeSize()==cache.allByteCode().size():loader.bytecodeSize()+" "+cache.allByteCode().size();
    res.coreE(e);
    res.lOut(l);    
    System.out.println(c0+ " reduced");
    assert l!=null:c0+" "+e;
    Core.L.NC nc=new Core.L.NC(poss, TypeManipulation.toCoreDocs(docs), c0, l);
    Program p1=p.update(updateInfo(p,nc),false);
    //note: we generate also the last round of bytecode to be cache friendly (if a new nested is added afterwards)
    assert loader.bytecodeSize()==cache.allByteCode().size():loader.bytecodeSize()+" "+cache.allByteCode().size();
    p1=flagTyped(p1,cache.allByteCode(),cache.allLibs());//propagate errors
    CTz newCTz=p1.from(frommedCTz,P.of(0,L(c0)));
    var out=new Cache.InOut(loader.bytecodeSize(),loader.libsCachedSize(), newCTz, cachableAlreadyCoherent(), p1);
    res.out(out);
    assert loader.bytecodeSize()==cache.allByteCode().size():loader.bytecodeSize()+" "+cache.allByteCode().size();
    return res;
    }
//-----------------
  boolean cacheOk(){
    if(this.cache._top()==null){
      assert this.cache.allLibs().isEmpty():this.cache.allLibs();
      assert this.cache.allByteCode().isEmpty();
      return true;
      }
    assert this.initialPath.endsWith("localhost")|| this.cache._top().in().p().dept()==0:this.initialPath +" "+this.cache._top().in().p().dept();
    assert this.cache._top().in().coherentList().size()==1;
    assert this.cache._top().in().ctz().coherent();
    assert this.cache._top().in().ctz().entries().isEmpty();
    assert this.cache._top().in().nByteCode()==0;
    assert this.cache._top().in().nLibs()==0;
    assert this.cache._top().in().p().dept()==this.cache._top().out().p().dept();
    return true;
    }
  public Program top(Program p)throws EndError {
    assert cacheOk();
    try{
      Cache.CTop res;
      if(this.validCache){
        assert this.cache._top()!=null;
        res=topCache(p, this.cache._top());
        }
      else{res=dropCache(p,null);}
      assert res!=null;
      cache._top(res);
      return res.out().p();
      }
    finally{
      /*if(cache._top()!=null){//may be false if 
      Hard problem: we want to save the cache also in case of errors/exceptions, but how we
      update the cache? now we use return values of top/topNC methods, but.. when they throw an
      exception, they will not get the result!
        cacheOk();f*/
        //Cache.saveCache(initialPath, cache);//TODO: decomment to activate cache
      }
    }
  public CTz setInvalid(Cache.InOut in){
    assert validCache;
    validCache=false;
    assert cache.allByteCode().size()>=in.nByteCode();
    assert cache.allLibs().size()>=in.nLibs(); 
    for(int i=cache.allByteCode().size()-1;i>=in.nByteCode();i-=1){
      cache.allByteCode().remove(i);
      }
    for(int i=cache.allLibs().size()-1;i>=in.nLibs();i-=1){
      cache.allLibs().remove(i);
      }
    return in.ctz();
    }
    /*
  private CTz setInvalid(Cache.InOut in,CTz ctz){
    if(!validCache){return ctz;}
    validCache=false;
    assert cache.allByteCode().size()>=in.nCByteCode();
    assert cache.allByteCode().size()>=in.nMByteCode();
    assert cache.allLibs().size()>=in.nLibs(); 
    for(int i=cache.allByteCode().size()-1;i>=in.nMByteCode();i-=1){
      cache.allByteCode().remove(i);
      }
    for(int i=cache.allLibs().size()-1;i>=in.nLibs();i-=1){
      cache.allLibs().remove(i);
      }
    assert ctz==null;
    return in.ctz();
    }
  public Cache.CTop top(CTz ctz,Program p, Cache.CTop ctop)throws EndError {
    var currentState=new Cache.InOut(loader.bytecodeSize(),-1,loader.libsCachedSize(),ctz,alreadyCoherent,p);
    var currentEq=ctop.in().equals(currentState);
    if(!ctop.hasHD() && currentEq){return ctop;}
    alreadyCoherent.add(new HashSet<>());
    assert p.dept()+1>=alreadyCoherent.size(): p.dept()+"!="+alreadyCoherent.size();
    if(validCache && ctop!=null){uniqueId=ctop.in().p().topCore().info()._uniqueId();}
    Core.L coreL=SortHeader.coreTop(p, uniqueId++);//propagates the right header errors
    List<Full.L.M> ms=((Full.L)p.top).ms();
    List<Full.L.NC> ncs=typeFilter(ms.stream(),Full.L.NC.class);
    if(validCache && coreL.equals(ctop.sortedHeader())){
      //-load bytecode about the header
      List<Cache.CTopNC1> p1Cache=topNC(ctznull,ctop.ncs(),ncs);//propagate exceptions
      if(validCache && currentEq){return ctop;}//currentEq can be different if meth body is different
      ctz=setInvalid(ctop.in(),ctz);
      //-may load e1n from cache?
      //-make new cache, return new cache
      }
    //different header (either after reuse #$ or just updated reuse/methods)
    ctz=setInvalid(ctop.in(),ctz);
    var newCache=new Cache.CTop(currentState, null, new ArrayList<>(), hasHDfalse, coreL, -1, -1);
    //-compute hasHD;
    Program p0=p.update(coreL,false);
    List<Half.E> e1n=L(coreL.mwts(),(c,mwti)->{
      var memi=_elem(ms,mwti.key());
      Full.E _ei=null;
      if(memi!=null){_ei=memi._e();}
      ctzAdd(ctz,p0,mwti.mh(),_ei,c);
      });
    assert p.top.isFullL();
    //-update newCache by saving the new bytecode/libs
    try{this.loader.loadNow(p0,cache, newCache);}
    catch(CompilationError e){throw new Error(e);}
    int size1=loader.libsCachedSize();
    assert size0+newCLibs.size()==size1;
    return newCache(new CacheEntry(null,null,p,null,newCLibs,size0,size1,null,cByteCode));
    //----
    Program p1=topNC(ctz,cache.p,ncs);//propagate exceptions
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
    }*/
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
  private Core.E infer(I i,CTz ctz,CTz frommed,Half.E e,ArrayList<Cache.CTop> ctops) throws EndError{
    return new InferToCore(i,ctz,this,ctops).compute(e);
    }
  private final FreshNames freshNames;
  private final ArrayList<HashSet<List<C>>> alreadyCoherent=new ArrayList<>();
  private int uniqueId=0;
  public final Loader loader;
  private final Path initialPath;
  public final Cache cache;
  public boolean validCache=false;
  public Top(FreshNames freshNames, int uniqueId, Loader loader,Path initialPath) {
    this.freshNames=freshNames;
    this.uniqueId=uniqueId;
    this.loader=loader;
    this.initialPath=initialPath;
    if(initialPath!=null){
      this.cache=Cache.loadCache(initialPath);
      assert cacheOk();
      if(cache._top()!=null){this.validCache=true;}
      }
    else{this.cache=new Cache(new ArrayList<>(),new ArrayList<>(),null);}
    }
  private List<Set<List<C>>> cachableAlreadyCoherent(){  //immutable data, to be converted back when out of cache
    return L(alreadyCoherent,(c,e)->c.add(Collections.unmodifiableSet(new HashSet<>(e))));
    }
  
  /*private CacheEntry topNC1(CTz ctz, Program p,CTz frommedCTz,NC current,List<NC>allNCs){
    //assert !validCache || ( ctz==null && frommedCTz==null);
    //assert  validCache || ( ctz!=null && frommedCTz!=null); 
    C c0=current.key();
    System.out.println("Now considering main "+c0);
    Full.E fe=current.e();
    List<Full.Doc> docs=current.docs();
    List<Pos> poss=current.poss();
    //caching part
    var info=new HashDollarInfo(current);
    CacheEntry c=null;
    if(validCache && !info.hashDollar && cacheIndex+info.steps<cache.size()){
      c=cache.get(cacheIndex+info.steps);
      assert c._key!=null: "";
      if(c._key.equals(current)){//all good! skip steps
        var cachesToLoad=cache.subList(cacheIndex,cacheIndex+info.steps+1);
        for(var ci:cachesToLoad){oldCache(ci,true);}
        cacheIndex+=info.steps+1;
        return c;//since c contains p,CTz and frommedCTz
        }
      }
    Core.E e=toCoreTypeCohereht(ctz,frommedCTz,poss,p,c0,fe,allNCs);
    if(!validCache || cacheIndex+info.steps>=cache.size()){
      var mByteCode=new HashMap<String,SClassFile>();
      int size0=loader.libsCachedSize();
      var newMLibs=new ArrayList<L42Library>();
      Core.L l=reduce(p,c0,e,mByteCode,newMLibs);//propagate errors
      int size1=loader.libsCachedSize();
      assert size0+newMLibs.size()==size1;
      System.out.println(c0+ " reduced");
      assert l!=null:c0+" "+e;
      Core.L.NC nc=new Core.L.NC(poss, TypeManipulation.toCoreDocs(docs), c0, l);
      Program p1=p.update(updateInfo(p,nc),false);
      //TODO: try to understand if we can avoid any bytecode generation here (is this bytecode ever usable?)
      //that is, the last nested class would not generate usable bytecode
      var cByteCode=new HashMap<String,SClassFile>();
      var newCLibs=new ArrayList<L42Library>();
      int size2=loader.libsCachedSize();
      p1=flagTyped(p1,cByteCode,newCLibs);//propagate errors
      int size3=loader.libsCachedSize();
      assert size2+newCLibs.size()==size3;
      return newCache(new CacheEntry(current,frommedCTz,p1,newMLibs,newCLibs,size1,size3,mByteCode,cByteCode));
      }
    c=cache.get(cacheIndex);//that is, the 'current' cache, not the next one
    assert c._key!=null;
    //cacheIndex+=info.steps+1;
    //assert info.steps==0;//info.steps is irrelevent from now on?
    cacheIndex+=1;
    if(!info.hashDollarTop){return oldCache(c,true);}
    assert c._mByteCode!=null;
    int size0=loader.libsCachedSize();
    assert size0==c.sizeWithMLibs-c._mLibs.size();
    loader.loadByteCodeFromCache(c._mByteCode,c._mLibs);
    int size1=loader.libsCachedSize();
    assert size0+c._mLibs.size()==size1;
    assert size1==c.sizeWithMLibs;
    Core.L l=this.reduceByName(p,c0);
    var cByteCode=new HashMap<String,SClassFile>();
    var newLibs=new ArrayList<L42Library>();    
    Core.L.NC nc=new Core.L.NC(poss, TypeManipulation.toCoreDocs(docs), c0, l);
    Program p1=p.update(updateInfo(p,nc),false);
    int size2=loader.libsCachedSize();
    p1=flagTyped(p1,cByteCode,newLibs);//propagate errors
    int size3=loader.libsCachedSize();
    assert c.sizeWithCLibs==size3:
      c.sizeWithCLibs+" "+size3+" "+size2+" "+newLibs.size();//TODO:remove
    assert size2+newLibs.size()==size3;
    Core.L newL=_elem(p1.topCore().ncs(),c0).l();//This may be typed, while l may not
    Core.L cachedL=_elem(c.p.topCore().ncs(),c0).l();
    if(newL.equals(cachedL)){return oldCache(c,false);}
    return newCache(new CacheEntry(current,frommedCTz,p1,c._mLibs,newLibs,size1,size3,c._mByteCode,cByteCode));    
    }*/
  private Core.E toCoreTypeCohereht(CTz ctz,CTz frommedCTz,List<Pos>poss,Program p,C c0,Full.E fe,List<NC> allNCs,ArrayList<Cache.CTop> ctops){
      Y y=new Y(p,GX.empty(),L(),null,L());
      var  hq=toHalf(ctz,y,freshNames,fe);
      Half.E he=hq.e;
      I i=new I(c0,p,G.empty());
      Core.E ce=infer(i,ctz,frommedCTz,he,ctops); //propagates errors
      assert ce!=null;
      WellFormedness.of(ce.visitable());
      var cohePs=new ArrayList<P.NCs>();
      P pRes=wellTyped(p,ce,cohePs,allNCs);//propagate errors //ncs is passed just to provide better errors
      Core.E ce0=adapt(ce,pRes);
      coherentAllPs(p,cohePs); //propagate errors
      return ce0;    
    }
  protected Program flagTyped(Program p1,ArrayList<SClassFile> cBytecode,ArrayList<L42Library> newLibs) throws EndError{
    Program p=FlagTyped.flagTyped(this.loader,p1);//but can be overridden as a testing handler
    try {this.loader.loadNow(p,cBytecode,newLibs);}
    catch (CompilationError e) {throw new Error(e);}
    return p;
    }
  protected Core.L reduce(Program p,C c,Core.E e,List<SClassFile> outNewBytecode,ArrayList<L42Library> newLibs)throws EndError {
    try{return loader.runNow(p, c, e,outNewBytecode,newLibs);}
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
    try{return loader.runMainName(p,c,name);}
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
  static public void collectWatched(Core.L l,ArrayList<P.NCs> watched){
    var acc= new Accumulate.WithCoreG<ArrayList<P.NCs>>(){
      @Override public ArrayList<P.NCs> empty() {return watched;}
      @Override public void visitMCall(Core.MCall mc){
        if(!mc.s().hasUniqueNum()){return;}
        var t=g(mc.xP());
        if(!t.p().isNCs()){return;}
        watched.add(t.p().toNCs());
        }
      @Override public void visitL(Core.L l){
        TypeManipulation.skipThis0(l.info().watched(),l,p->p,(p0,p1)->watched.add(p1));
        }
      @Override public void visitP(P p){
        if(!p.isNCs()){return;}
        P.NCs pi=p.toNCs();
        var cs=pi.cs();
        var csCut=L(cs.stream().takeWhile(c->!c.hasUniqueNum()));
        if(cs.size()==csCut.size()){return;}
        watched.add(pi.withCs(csCut));
        }
      };
    for(var ti:l.ts()){acc.of(ti);}
    for(var di: l.docs()){acc.of(di);}
    for(var mwti:l.mwts()){acc.of(mwti);}
    for(var nci:l.ncs()){
      Info info=nci.l().info();
      TypeManipulation.skipThis0(info.watched(),nci.l(),p->p,(p0,p1)->watched.add(p1));
      for(var di: nci.docs()){acc.of(di);}
      }
    watched.removeAll(L(P.pThis0));
    }
  }
  /*private void flushBadCache(){
  if(!validCache){return;}
  assert cache.size()>=cacheIndex: cache.size()+" "+cacheIndex; 
  for(int i=cache.size()-1;i>=cacheIndex;i-=1){cache.remove(i);}
  }
CacheEntry newCache(CacheEntry c){
  System.out.println("New cache for "+c._key);
  flushBadCache();
  validCache=false;
  cache.add(c);
  return c;
  }
CacheEntry oldCache(CacheEntry c,boolean loadLibs){
  System.out.println("Old cache for "+c._key);
  if(loadLibs && c._mByteCode!=null){
      int size0=loader.libsCachedSize();
      assert size0==c.sizeWithMLibs-c._mLibs.size():size0+" "+c.sizeWithMLibs+" "+c._mLibs.size();
      loader.loadByteCodeFromCache(c._mByteCode,c._mLibs);
      int size1=loader.libsCachedSize();
      assert size0+c._mLibs.size()==size1;
    }
  int size0=loader.libsCachedSize();
  assert size0==c.sizeWithCLibs-c.cLibs.size(): size0+" "+c.sizeWithCLibs+" "+c.cLibs.size();
  loader.loadByteCodeFromCache(c.cByteCode,c.cLibs);
  int size1=loader.libsCachedSize();
  assert size0+c.cLibs.size()==size1;
  return c;
  }
private Program topNC(CTz ctz, Program p, List<NC> ncs)throws EndError{
  if(ncs.isEmpty()){return p;}
  NC current=ncs.get(0);
  CTz frommedCTz=p.push(current.key(),Program.emptyL).from(ctz,P.pThis1);
  CacheEntry c=topNC1(ctz,p,frommedCTz,current,ncs);
  ncs=popL(ncs);
  CTz newCTz=c.p.from(c.frommedCTz,P.of(0,L(current.key())));
  Program res=topNC(newCTz,c.p,ncs);
  return res; 
  }*/
