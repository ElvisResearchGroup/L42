package is.L42.meta;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unique;
import static is.L42.tools.General.unreachable;
import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.pushL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import is.L42.cache.L42CacheMap;
import is.L42.cache.L42SingletonCache;
import is.L42.cache.nativecache.ValueCache;
import is.L42.common.Constants;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Core.PathSel;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.Mdf;
import is.L42.generated.Core.Doc;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42ClassAny;
import is.L42.platformSpecific.javaTranslation.L42Fwd;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.javaTranslation.L42NoFields;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.General;
import is.L42.top.Top;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;

public class L42£Meta extends L42NoFields<L42£Meta>{
  private final List<Arrow> renames;
  private final String toString;
  public L42£Meta(){this(L());}
  public L42£Meta(List<Arrow> renames){
    this.renames=renames;
    this.toString=renames.toString();
    }
  public L42£Library wither(L42£Library input,String cs,Function<L42£LazyMsg,L42Any>wrap,String immK){
    L l=input.unwrap;
    var pIn=Resources.currentP.push(Resources.currentC,l);
    return wrapL(new Wither().wither(pIn,unwrapCs(cs),wrap,immK));
    }
  public L42£Library addConstructors(L42£Library input,String cs,Function<L42£LazyMsg,L42Any>wrap,String mutK,String immK){
    L l=input.unwrap;
    var pIn=Resources.currentP.push(Resources.currentC,l);
    return wrapL(new K().k(pIn,unwrapCs(cs),wrap,mutK,immK));
    }
  public L42£Library close(L42£Library input,String cs,Function<L42£LazyMsg,L42Any>wrap){
    L l=input.unwrap;
    var pIn=Resources.currentP.push(Resources.currentC,l);
    return wrapL(new Close().close(pIn,unwrapCs(cs),wrap));    
    } 
  public boolean eq(L42£Meta meta){return toString.equals(meta.toString);}
  
  public L42£Meta addMapP(String name,L42Any target){
    Full.PathSel pathSel=unwrapPathSel(name);
    P p=unwrapPath(target);
    var a=new Arrow(pathSel.cs(),pathSel._s(),true,p,null,null);
    return new L42£Meta(pushL(renames,a));
    }
  public L42£Meta addMapDoubleArrow(String name1,String name2){
    Full.PathSel p1=unwrapPathSel(name1);
    Full.PathSel p2=unwrapPathSel(name2);
    var a=new Arrow(p1.cs(),p1._s(),true,null,p2.cs(),p2._s());
    return new L42£Meta(pushL(renames,a));
    }
  public L42£Meta addMapSingleArrow(String name1,String name2){
    Full.PathSel p1=unwrapPathSel(name1);
    Full.PathSel p2=unwrapPathSel(name2);
    var a=new Arrow(p1.cs(),p1._s(),false,null,p2.cs(),p2._s());
    return new L42£Meta(pushL(renames,a));
    }
  public L42£Meta mergeMap(L42£Meta meta){
    return new L42£Meta(mergeU(renames,meta.renames));
    }
  public L42£Library applyMap(L42£Library input,Function<L42£LazyMsg,L42Any>wrapName,Function<L42£LazyMsg,L42Any>wrapFail,Function<L42£LazyMsg,L42Any>wrapC,Function<L42£LazyMsg,L42Any>wrapM){
    L l=input.unwrap;
    L res=new Rename().apply(Resources.currentP,Resources.currentC,l,
      renames, wrapName, wrapFail, wrapC, wrapM);
    return wrapL(res);
    }
  private static P unwrapPath(L42Any classAny){
    L42ClassAny cn;
    if(classAny instanceof L42ClassAny){cn=(L42ClassAny)classAny;}
    else{cn=((L42Fwd)classAny).asPath();}
    return cn.unwrap;
    }
  private static Full.PathSel unwrapPathSel(String s){
    return Parse.pathSel(Constants.dummy, s);
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
  private static L42£Library wrapL(L res){
    var res0=new L42£Library(Resources.currentP.push(Resources.currentC,res));
    assert res0.unwrap==null;
    res0.currentProgram(Resources.currentP);
    assert res0.unwrap!=null;
    return res0;
    }
  private static S applyS=S.parse("#apply()");
  public L42£Library resource(L42£Library that){
    L l=addThis1().visitL(that.unwrap);
    var mh=new Core.MH(Mdf.Class,L(),P.coreLibrary,applyS,L(),L());
    var meth=new Core.L.MWT(l.poss(),L(),mh,"",l);
    ArrayList<P.NCs> typePs=new ArrayList<>();
    ArrayList<P.NCs> cohePs=new ArrayList<>();
    ArrayList<P.NCs> metaCohePs=new ArrayList<>();
    Top.collectDepsE(Resources.currentP,l,typePs,cohePs,metaCohePs);
    var i=l.info();
    List<P.NCs> watched=L(c->TypeManipulation.skipThis0(i.watched(),l,
      pi->pi,(p1,p2)->c.add(p2)));
    List<PathSel> usedMethods=L(c->TypeManipulation.skipThis0(i.usedMethods(),l,
      ps->ps.p().toNCs(),(ps,p)->c.add(ps.withP(p))));
    List<P.NCs> hidden=L(c->{
      TypeManipulation.skipThis0(i.hiddenSupertypes(),l,pi->pi,(p1,p2)->c.add(p2));
      TypeManipulation.skipThis0(l.ts(),l,ti->ti.p().toNCs(),(p1,p2)->{if(!c.contains(p2)){c.add(p2);}});
      });      
    var info=new Info(
      i.isTyped(),L(typePs.stream()),L(),mergeU(cohePs,metaCohePs),
      watched,usedMethods,hidden,L(),false,"",L(),-1);
    var res=new Core.L(l.poss(), false, L(), L(meth), L(), info,L());
    return wrapL(res);
    }
  public L42£Library simpleSum(L42£Library a, L42£Library b,Function<L42£LazyMsg,L42Any>wrapC,Function<L42£LazyMsg,L42Any>wrapM){
    L res=new Sum().compose(Resources.currentP,Resources.currentC,a.unwrap, b.unwrap, wrapC, wrapM);
    return wrapL(res);
    }
  private static CloneVisitor addThis1(){
    return new CloneVisitor(){
      int dept=0;
      @Override public Core.L visitL(Core.L l){
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
      throw todo();//or bug???
      }
  public static final Class<L42£Meta> _class=L42£Meta.class;
  public static final MetaCache myCache=new MetaCache();
  static{L42CacheMap.addCachableType_synchronized(L42£Meta.class,myCache);}
  @Override public MetaCache myCache(){return myCache;}
  @Override 
  public L42£Meta newInstance() { 
    throw General.unreachable(); 
    }
  }
class MetaCache extends ValueCache<L42£Meta>{
  @Override public String typename() {return "Meta";}
  @Override protected boolean valueCompare(L42£Meta t1, L42£Meta t2) {return t1.eq(t2);}
  }