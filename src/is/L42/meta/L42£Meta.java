package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import is.L42.cache.L42Cache;
import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Mdf;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.maps.L42£ImmMap;
import is.L42.nativeCode.TrustedKind;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.ToJar;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42ClassAny;
import is.L42.platformSpecific.javaTranslation.L42Fwd;
import is.L42.platformSpecific.javaTranslation.L42NoFields;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.javaTranslation.L42£Void;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.sifo.Lattice42;
import is.L42.sifo.SifoTopTS;
import is.L42.top.Deps;
import is.L42.top.UniqueNsRefresher;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;

public class L42£Meta extends L42NoFields.Eq<L42£Meta>{
  private final List<Arrow> renames;
  private final String toString;
  public L42£Meta(){this(L());}
  public L42£Meta(List<Arrow> renames){
    this.renames=renames;
    this.toString=renames.toString();
    }
  public L42£Library nativeSlaveRename(L42£Library input,String oldName,String newName,Function<L42£LazyMsg,L42Any>wrap){
    CoreL l=input.unwrap;
    return wrapL(new NativeSlaveNames().apply(l,oldName,newName,wrap));
    }
  public L42£Library resetDocs(L42£Library input,L42£ImmMap<?,?> map,Function<L42£LazyMsg,L42Any>wrap){
    CoreL l=input.unwrap;
    var pIn=Resources.currentP.push(Resources.currentC,l);
    return wrapL(new ResetDocs().apply(pIn,map,wrap));
    }
  public L42£Library removeUnusedCode(L42£Library input){
    CoreL l=input.unwrap;
    CoreL res=new RemoveUnusedCode().of(l);
    return wrapL(res);
    }
  public L42£Library wither(L42£Library input,String cs,Function<L42£LazyMsg,L42Any>wrap,String immK){
    CoreL l=input.unwrap;
    var pIn=Resources.currentP.push(Resources.currentC,l);
    return wrapL(new Wither().wither(pIn,unwrapCs(cs),wrap,immK));
    }
  public L42£Library defaults(L42£Library input,String cs,Function<L42£LazyMsg,L42Any>wrap){
    CoreL l=input.unwrap;
    var pIn=Resources.currentP.push(Resources.currentC,l);
    return wrapL(new Defaults().of(pIn,unwrapCs(cs),wrap));
    }
  public L42£Library addConstructors(L42£Library input,String cs,boolean autoNorm,Function<L42£LazyMsg,L42Any>wrap,String mutK,String immK){
    CoreL l=input.unwrap;
    var pIn=Resources.currentP.push(Resources.currentC,l);
    return wrapL(new K().k(pIn,unwrapCs(cs),autoNorm,wrap,mutK,immK));
    }
  public L42£Library cacheCall(L42£Library input,Function<L42£LazyMsg,L42Any>wrap){
    CoreL l=input.unwrap;
    var pIn=Resources.currentP.push(Resources.currentC,l);
    return wrapL(CacheCall.of(pIn,wrap));
    }
  public L42£Library close(L42£Library input,String cs,boolean autoNorm,Function<L42£LazyMsg,L42Any>wrap){
    CoreL l=input.unwrap;
    var pIn=Resources.currentP.push(Resources.currentC,l);
    return wrapL(new Close().close(pIn,unwrapCs(cs),autoNorm,wrap));    
    } 
  @Override public String toString(){return toString;}
  @Override public boolean eq(L42£Meta meta){return toString.equals(meta.toString);}
  
  public L42£Meta addMapP(L42£Name name,L42Any target){
    P p=unwrapPath(target);
    var a=new Arrow(name.cs,name._s,true,false,p,null,null);
    return new L42£Meta(pushL(renames,a));
    }
  public Arrow unwrapArrow(L42£Name name1,L42£Name name2,boolean full,boolean star){
    List<C> _cs2=null;
    S _s2=null;
    if(name2!=L42£Name.instance){_cs2=name2.cs;_s2=name2._s;}
    return new Arrow(name1.cs,name1._s,full,star,null,_cs2,_s2);
    }
  public L42£Meta addMapDoubleArrow(L42£Name name1,L42£Name name2){
    var a=unwrapArrow(name1, name2,true,false);
    return new L42£Meta(pushL(renames,a));
    }
  public L42£Meta addMapSingleArrow(L42£Name name1,L42£Name name2){
    var a=unwrapArrow(name1, name2,false,false);
    return new L42£Meta(pushL(renames,a));
    }
  public L42£Meta mergeMap(L42£Meta meta){
    return new L42£Meta(mergeU(renames,meta.renames));
    }
  public L42£Meta mergeMapDeep(L42£Meta meta){
    var tmp=meta.renames.stream().map(a->a.withStar());
    return new L42£Meta(mergeU(renames,L(tmp)));
    }
  private static Pos noPos=new Pos(URI.create("InternalHidden"),0,0);
  public static CoreL libraryCloseAndTyped(String fauxFileName, L42£Library l42Lib, MetaError err){
      CoreL l=l42Lib.unwrap;
      assert l.wf();
      Program p=Program.flat(l);
      l.accept(new CloneVisitorWithProgram(p){//could be an accumulator visitor to be more efficient
        @Override public P visitP(P p){
          if(!p.isNCs()) {return p;}
          boolean out=p.toNCs().n()>p().dept();
          if(out){err.throwErr(p,"Path "+p+" not defined inside of deployed code");}
          boolean missed=this.p()._ofCore(p)==null;
          if(!missed){return p;}
          var cs=popLRight(p.toNCs().cs());
          var p0=P.NCs.of(p.toNCs().n(),cs);
          var loc=this.p()._ofCore(p0);
          if(loc==null){err.throwErr(p,"Path "+p+" adn path "+p0+" not defined inside of deployed code.");}
          throw err.throwErr(p,"Path "+p+" adn path "+p0+" not defined in "+loc.poss());
          }
        });
      try {ProgramTypeSystem.type(true, p);}
      catch(EndError e){
        throw err.throwErr(l42Lib,e.toString());
        }
      l=l.accept(new CloneVisitor(){//info->typed; pos->anonimized
        List<Pos>poss(List<Pos>poss){
          return poss.stream()
            .<Pos>map(p->p.withFileName(fileName(p.fileName())))
            .toList();
          }
        URI fileName(URI uri){
          String p=uri.getPath();
          int i=p.lastIndexOf("/");
          if(i!=-1) {
            p=p.substring(i+1);}
          try{return new URI(fauxFileName+"/"+p);}
          catch (URISyntaxException e){throw new Error(e);}
          }
        public Core.EX visitEX(Core.EX x){return super.visitEX(x.withPos(noPos));}
        public Core.PCastT visitPCastT(Core.PCastT pCastT){
          return super.visitPCastT(pCastT.withPos(noPos));
          }          
        public Core.EVoid visitEVoid(Core.EVoid eVoid){return eVoid.withPos(noPos);}

        public CoreL visitL(CoreL l){return super.visitL(l.withPoss(poss(l.poss())));}

        public Core.MWT visitMWT(Core.MWT mwt){
          return super.visitMWT(mwt.withPoss(poss(mwt.poss())));
          }
        public Core.NC visitNC(Core.NC nc){
          return super.visitNC(nc.withPoss(poss(nc.poss())));
          }
        public Core.MCall visitMCall(Core.MCall mCall){
          return super.visitMCall(mCall.withPos(noPos));
          }          
        public Core.Block visitBlock(Core.Block block){
          return super.visitBlock(block.withPos(noPos));
          }
        public Core.Loop visitLoop(Core.Loop loop){
          return super.visitLoop(loop.withPos(noPos));
          }          
        public Core.Throw visitThrow(Core.Throw thr){
          return super.visitThrow(thr.withPos(noPos));
          }          
        public Core.OpUpdate visitOpUpdate(Core.OpUpdate opUpdate){
          return super.visitOpUpdate(opUpdate.withPos(noPos));
          }
        @Override public Core.Info visitInfo(Core.Info info){
          return info.withTyped(true);
          }});
      return l;
      }
  public String deployLibraryToBase64(String fauxFileName, L42£Library l42Lib,Function<L42£LazyMsg,L42Any>wrap){
    var err=new MetaError(wrap);
    CoreL l=libraryCloseAndTyped(fauxFileName, l42Lib, err);
    var auxOut = new ByteArrayOutputStream();
    try(var out = new ObjectOutputStream(auxOut)){
      out.writeObject(l);
      out.flush();
      byte[] arr = auxOut.toByteArray();
      return Resources.encoder.encodeToString(arr).replace("\r\n","");
      }
    catch(IOException e) { throw unreachable(); }//unreachable
    }
  public L42£Void deployLibrary(String s, String fauxFileName, L42£Library l42Lib,Function<L42£LazyMsg,L42Any>wrap){
    var err=new MetaError(wrap);
    CoreL l=libraryCloseAndTyped(fauxFileName, l42Lib, err);
    Path fullPath=Constants.localhost.resolve(s+".L42");
    try(
      var file=new FileOutputStream(fullPath.toFile()); 
      var out=new ObjectOutputStream(file);
      ){ out.writeObject(l); }
    //catch (FileNotFoundException e) {throw unreachable();}
    catch (IOException e) {
      throw err.throwErr(fullPath,"Failed to read file:\n"+e.getMessage());
      }
    return L42£Void.instance;
    }
  public String deployJarToBase64(String fauxFileName, L42£Library l42Lib,Function<L42£LazyMsg,L42Any>wrap){
    var err=new MetaError(wrap);
    CoreL l=libraryCloseAndTyped(fauxFileName, l42Lib, err);
    var mainS=S.parse("#$main()");
    var main=_elem(l.mwts(),mainS);
    if(main==null){ err.throwErr(mainS,"Method "+mainS+" not defined inside of the deployed code"); }
    try{ return ToJar.ofBase64(l); }
    catch (FileNotFoundException e) {throw unreachable();}
    catch (IOException e) {throw unreachable();}
    catch (CompilationError e) {
      throw new EndError.InlinedNativeInvalid(l.poss(),
        ErrMsg.nativeInlinedInvalid(e.getMessage()));
      }
    }
  public L42£Library sifo(L42£Library input,L42Any top,Function<L42£LazyMsg,L42Any>wrap){
    CoreL l=input.unwrap;
    var pathTop=unwrapPath(top).toNCs();
    pathTop=pathTop.withN(pathTop.n());
    var pIn=Resources.currentP.push(Resources.currentC,l);
    var lattice=new Lattice42(Resources.currentP,pathTop);
    new SifoTopTS(pIn,lattice,1).visitL(l);
    return input;    
    }
  
  public boolean isSelfRename(){
    if(this.renames.size()!=1) {return false;}
    var r=this.renames.get(0);
    if(r.isP()){return false;}
    if((r._s==null)!=(r._sOut==null)){return false;}
    boolean csOk=r._cs==null || r.cs.equals(r._cs);
    boolean sOk=r._s==null || r._s.equals(r._sOut);
    return csOk && sOk;
    }
  public L42£Library applyMap(L42£Library input,Function<L42£LazyMsg,L42Any>wrapName,Function<L42£LazyMsg,L42Any>wrapFail,Function<L42£LazyMsg,L42Any>wrapC,Function<L42£LazyMsg,L42Any>wrapM){
    try{
      CoreL l=input.unwrap;
      CoreL res=new Rename(new UniqueNsRefresher()).apply(Resources.currentP,Resources.currentC,l,
        L(renames.stream().map(a->a.copy())),
        wrapName, wrapFail, wrapC, wrapM);
      return wrapL(res);
      }
    catch(RuntimeException rte){
      throw rte;}//To support breakpoints here
    }
  public static P unwrapPath(L42Any classAny){
    L42ClassAny cn;
    if(classAny instanceof L42ClassAny){cn=(L42ClassAny)classAny;}
    else{cn=((L42Fwd)classAny).asPath();}
    return cn.unwrap;
    }
  public static Full.PathSel unwrapPathSel(String s){
    var res=Parse.pathSel(Constants.dummy, s);
    if(res._p()!=null){
      assert res._p().toNCs().n()==0;
      res=res.with_p(null).withCs(res._p().toNCs().cs());
      }
    return res;
    }
  private static List<C> unwrapCs(String s){
    var csP = Parse.csP(Constants.dummy, s);
    assert !csP.hasErr();
    Full.CsP res=csP.res;
    if(!res.cs().isEmpty()){return res.cs();}
    var path0=res._p();
    assert path0.isNCs() && path0.toNCs().n()==0;
    return path0.toNCs().cs();
    }
  private static L42£Library wrapL(CoreL res){
    var res0=new L42£Library(Resources.currentP.push(Resources.currentC,res));
    assert res0.unwrap==null;
    res0.currentProgram(Resources.currentP);
    assert res0.unwrap!=null;
    return res0;
    }
  private static S applyS=S.parse("#apply()");
  public L42£Library resource(L42£Library that,L42£Name name){
    CoreL l=addThis1().visitL(that.unwrap);
    S sName=applyS;
    if(name._s!=null || name._s.xs().isEmpty()){sName=name._s;}
    var mh=new Core.MH(Mdf.Class,L(),P.coreLibrary,sName,L(),L());
    var meth=new Core.MWT(l.poss(),L(),mh,"",l);
    Deps deps=new Deps().collectDepsE(Resources.currentP,l);
    var res=new CoreL(l.poss(), false, L(), L(meth), L(), deps.toInfo(l.info().isTyped()),L());
    return wrapL(res);
    }
  public L42£Library simpleSum(L42£Library a, L42£Library b,Function<L42£LazyMsg,L42Any>wrapC,Function<L42£LazyMsg,L42Any>wrapM){
    CoreL res=new Sum().compose(Resources.currentP,Resources.currentC,a.unwrap, b.unwrap, wrapC, wrapM);
    return wrapL(res);
    }
  private static CloneVisitor addThis1(){
    return new CloneVisitor(){
      int dept=0;
      @Override public CoreL visitL(CoreL l){
        dept+=1;
        var res=super.visitL(l);
        dept-=1;
        return res;
        }
      @Override public P visitP(P path){
        if(!path.isNCs()){return path;}
        var p=path.toNCs();
        if(p.n()<dept){return p;}
        return p.withN(p.n()+1);
        }
      };
    }
    public String pathName(L42Any target){
      P p=unwrapPath(target);
      return p.toString();
      }

  public static final Class<L42£Meta> _class=L42£Meta.class;
  public static final L42Cache<L42£Meta> myCache=new EqCache<>(TrustedKind.Meta);
  @Override public L42Cache<L42£Meta> myCache(){return myCache;}
  }