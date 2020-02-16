package is.L42.meta;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;
import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.mergeU;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
  private final Map<List<C>,P> redirects;
  private final String toString;
  public L42£Meta(){this(Map.of());}
  public L42£Meta(Map<List<C>,P> redirects){
    this.redirects=redirects;
    this.toString=redirects.toString();
    }
  public L42£Library close(L42£Library input,Function<L42£LazyMsg,L42Any>wrap){
    L l=input.unwrap;
    //? TODO: should we first add constructors and after make @Property stuff private?
    return wrapL(new Close().close(Resources.currentP.push(Resources.currentC,l),wrap));    
    } 
  public boolean eq(L42£Meta meta){return toString.equals(meta.toString);}
  public L42£Meta addMapP(String name,L42Any target){
    List<C> cs=unwrapCs(name);
    P p=unwrapPath(target);
    var res=new HashMap<>(redirects);
    res.merge(cs,p,(old,val)->{throw todo();});
    return new L42£Meta(Collections.unmodifiableMap(res));
    }
  public L42£Meta mergeMap(L42£Meta meta){
    var res=new HashMap<>(redirects);
    for(var e:meta.redirects.entrySet()){
      res.merge(e.getKey(),e.getValue(),(old,val)->{throw todo();});
      }
    return new L42£Meta(Collections.unmodifiableMap(res));
    }
  public L42£Library applyMap(L42£Library input){
    assert redirects.size()==1;
    var cs=redirects.keySet().iterator().next();
    var t=redirects.values().iterator().next();
    L l=input.unwrap;
    return wrapL(simpleRedirect(l,cs, t));
    }
  private static P unwrapPath(L42Any classAny){
    L42ClassAny cn;
    if(classAny instanceof L42ClassAny){cn=(L42ClassAny)classAny;}
    else{cn=((L42Fwd)classAny).asPath();}
    return cn.unwrap;
    }
  private static List<C> unwrapCs(String s){
    var csP = Parse.csP(Constants.dummy, s);
    assert !csP.hasErr();
    var res=csP.res;
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
    L l=that.unwrap;
    L body=addThis1().visitL(l);
    var mh=new Core.MH(Mdf.Class,L(),P.coreLibrary,applyS,L(),L());
    var meth=new Core.L.MWT(body.poss(),L(),mh,"",body);
    ArrayList<P.NCs> typePs=new ArrayList<>();
    ArrayList<P.NCs> cohePs=new ArrayList<>();
    Top.collectDepsE(Resources.currentP,body,typePs,cohePs);
    var i=l.info();
    List<P.NCs> watched=L(c->TypeManipulation.skipThis0(i.watched(),l,
      pi->pi,(p1,p2)->c.add(p2)));
    List<PathSel> usedMethods=L(c->TypeManipulation.skipThis0(i.usedMethods(),l,
      ps->ps.p().toNCs(),(ps,p)->c.add(ps.withP(p))));
    List<P.NCs> hidden=L(c->TypeManipulation.skipThis0(i.hiddenSupertypes(),l,
      pi->pi,(p1,p2)->c.add(p2)));      
    var info=new Info(
      i.isTyped(),L(typePs.stream()),L(cohePs.stream()),
      watched,usedMethods,hidden,L(),false,"",L(),-1);
    var res=new Core.L(body.poss(), false, L(), L(meth), L(), info,L());
    return wrapL(res);
    }
  public L42£Library simpleSum(L42£Library a, L42£Library b){
    L res=directSum(a.unwrap,b.unwrap);
    return wrapL(res);
    }
  public L directSum(L a, L b){
    List<MWT> mwts=sumMWTs(a.mwts(),b.mwts());
    List<NC> ncs=sumNCs(a.ncs(),b.ncs());
    Info info=Top.sumInfo(a.info(),b.info());
    boolean interf=a.isInterface() || b.isInterface();
    List<T> ts=mergeU(a.ts(),b.ts());
    List<Pos> pos=mergeU(a.poss(),b.poss());
    List<Doc> docs=mergeU(a.docs(),b.docs());
    return new Core.L(pos, interf, ts, mwts, ncs, info, docs);
    }

  public List<NC> sumNCs(List<NC> a,List<NC> b){
    return L(c->{
      for(var mi:a){
        var other=_elem(b,mi.key());
        if(other==null){c.add(mi);}
        else{c.add(sumNC(mi,other));}
        }
      for(var mi:b){
        var other=_elem(a,mi.key());
        if(other==null){c.add(mi);}
        }
      });    
    }
  public NC sumNC(NC a,NC b){
    return a.withL(directSum(a.l(), b.l()));
    //TODO: sum the docs and the pos
    }
  public List<MWT> sumMWTs(List<MWT> a,List<MWT> b){
    return L(c->{
      for(var mi:a){
        var other=_elem(b,mi.key());
        if(other==null){c.add(mi);}
        else{c.add(sumMWT(mi,other));}
        }
      for(var mi:b){
        var other=_elem(a,mi.key());
        if(other==null){c.add(mi);}
        }
      });
    }
  public MWT sumMWT(MWT a,MWT b){
    if (a._e()!=null && b._e()!=null){
      assert false:a+" "+b;
      throw todo();}
    if(!a.mh().equals(b.mh())){
      System.out.println(a.mh());
      System.out.println(b.mh());
      throw todo();
      }//also check proper subtype
    var body=a._e();
    var nativeUrl=a.nativeUrl();
    if(body==null){body=b._e();nativeUrl=b.nativeUrl();}
    return a.with_e(body).withNativeUrl(nativeUrl);
    //TODO: sum the mh docs and the pos
    }
  private L simpleRedirect(L input, List<C> cs, P target){
    Program p=Resources.currentP;
    //TODO: check if source and dest are compatible with p._ofCore(path);
    var res=replaceP(cs,target,p.push(Resources.currentC,input)).visitL(input);
    res=res.withCs(cs, nc->{throw unreachable();},nc->null);
    //System.out.println(res);
    return res;
    }
  public L42£Library simpleRedirect(String innerPath, L42£Library l42Lib, L42Any classAny){
    L l=l42Lib.unwrap;
    List<C> cs=unwrapCs(innerPath);
    P path=unwrapPath(classAny);
    return wrapL(simpleRedirect(l,cs,path));
    }
  private static CloneVisitorWithProgram replaceP(List<C>cs,P dest,Program pStart){
    return new CloneVisitorWithProgram(pStart){
      @Override public P visitP(P path){
        int nesting=whereFromTop().size();
        if(!path.isNCs()){return path;}
        var src=this.p().minimize(P.of(nesting,cs));
        if(!src.equals(path)){
          //if(path.toString().endsWith(".Elem"))System.out.println(this.whereFromTop()+"encountered "+path+", looking from "+src+" was not ==, origin"+cs+";"+nesting+this.p());
          return path;}
        if(!dest.isNCs()){return dest;}
        var res=dest.toNCs();
        res=res.withN(res.n()+nesting+1);//because destination is relative to outside pStart.top
        return this.p().minimize(res);
        }
      };
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
      return "hi";
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