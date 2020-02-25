package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unique;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Full;
import is.L42.generated.Full.CsP;
import is.L42.generated.LDom;
import is.L42.generated.LL;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.nativeCode.TrustedKind;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.CloneVisitorWithProgram;

class InitVisitor extends CloneVisitorWithProgram{
  public InitVisitor(FreshNames f,Program p) {super(p);this.f=f;this.pStart=p;}
  FreshNames f;
  Program pStart;
  HashMap<Integer,List<Pos>> uniqueNs=new HashMap<>();
  LL addDots(Full.L s){
    LDom cms=getLastCMs();
    if(cms==null ||!(cms instanceof C)){
      throw new EndError.InvalidImplements(s.poss(),Err.invalidDotDotDotLocation());} 
    C lastC=(C)cms;
    Path outerPath=Paths.get(s.pos().fileName()).getParent();
    Path path=outerPath.resolve(lastC.inner());
    if(Files.exists(path) && Files.isDirectory(path)){
      path=path.resolve("This.L42");
      }
    else if(!Files.exists(path)){path=outerPath.resolve(lastC.inner()+".L42");}
    LL dots;try{dots=Parse.fromPath(path);}
    catch(IOException ioe){
      throw new EndError.InvalidImplements(s.poss(),Err.dotDotDotSouceNotExistant(path));
      }
    if(dots.isFullL()){
      List<Full.L.M> newMs=merge(((Full.L)dots).ms(),s.ms());
      return ((Full.L)dots).withMs(newMs);
      }
    if(s.ms().isEmpty()){return dots;}
    throw new EndError.InvalidImplements(s.poss(),Err.dotDotDotCoreSouceWithMs());
    }
  boolean isDefined(S s,List<P>ps,List<Pos>pos){
    for(P p:ps){if(isDefined(s,p,pos)){return true;}}
       return false;
       }
  boolean isDefined(S s,P p,List<Pos>pos){
    if(!p.isNCs()){return false;}
    LL l=p().of(p,pos);
    if(l.isFullL()){return isDefined(s,p.toNCs(),(Full.L)l);}
    return isDefined(s,p.toNCs(),(Core.L)l);
    }
  boolean isDefined(S s,P.NCs p,Full.L l){
    boolean found=l.ms().stream().anyMatch(m->m.key().equals(s));
    if(found){return true;}//ignoring reuse[URL] on purpose
    for(var ti:l.ts()){
      var pi=p().from(ti._p(),p);
      if(isDefined(s,pi,l.poss())){return true;}
      }
    return false;
    }
    boolean isDefined(S s,P.NCs p,Core.L l){
      boolean found=l.mwts().stream().anyMatch(m->m.key().equals(s));
      if(found){return true;}
      for(var ti:l.ts()){
        var pi=p().from(ti.p(),p);
        if(isDefined(s,pi,l.poss())){return true;}
        }//searching transitivelly instead of using Info.refine on purpose
      return false;
      }
    void checkUniqueNs(Full.L l){
      var pos=l.poss();
      List<P> ps=L(l.ts().stream().map(t->t._p()));
      List<Integer> ns=L(l.ms(),(c,mi)->{
        if(!mi.key().hasUniqueNum()){return;}
        LDom key=mi.key();
        if(key instanceof C){c.add(key.uniqueNum());return;}
        //filter the refined methods
        boolean refined=isDefined((S)key,ps,pos);
        if(!refined){c.add(key.uniqueNum());}
        });
      //check they are not seen already
      if(!Collections.disjoint(uniqueNs.keySet(),ns)){
        List<Pos> morePos=L(ns,(c,ni)->{
          var posi=uniqueNs.get(ni);
          if(posi!=null){c.addAll(posi);}
          });
        throw new EndError.NotWellFormed(pos,Err.nonUniqueNumber(ns,morePos));
        }
      for(var n:ns){uniqueNs.put(n,pos);uniqueNs.remove(0);}
      }
  void checkUniqueNs(Core.L l){
    var pos=l.poss();
    List<P> ps=L(l.ts().stream().map(t->t.p()));
    List<Integer> ns=L(c->{
      for(var nci:l.ncs()){
        if(nci.key().hasUniqueNum()){c.add(nci.key().uniqueNum());}
        }
      for(var mi:l.mwts()){
        if(!mi.key().hasUniqueNum()){return;}
        var key=mi.key();
        var refined=isDefined(key, ps,pos);
        if(!refined){c.add(key.uniqueNum());}
        }
      });
    //check they are not seen already
    if(!Collections.disjoint(uniqueNs.keySet(),ns)){
      List<Pos> morePos=L(ns,(c,ni)->{
        var posi=uniqueNs.get(ni);
        if(posi!=null){c.addAll(posi);}
        });
      throw new EndError.NotWellFormed(pos,Err.nonUniqueNumber(ns,morePos));
      }
    for(var n:ns){uniqueNs.put(n,pos);}
    }
  @Override public LL visitL(Full.L s){
    if(!s.isDots()){return super.visitL(s);}
    LL res=addDots(s);
    if(res.isFullL()){return super.visitL((Full.L)res);}
    return super.visitL((Core.L)res);
    }
  @Override public LL fullLHandler(Full.L s){
    if(s.isDots()){throw bug();}
    s=(Full.L)super.fullLHandler(s);//the only case it changes type is for the '...'
    List<Full.T> this0s=L(s.ts().stream().filter(this::invalidAfter));
    if(!this0s.isEmpty()){
      throw new EndError.InvalidImplements(s.poss(),Err.nestedClassesImplemented(this0s));
      }
    checkUniqueNs(s);
    return s;        
    }
  <T>void checkMissing(List<T> setAll,List<T> setSome,List<Pos> pos,Function<Object,String>err){
    var allGood=setSome.containsAll(setAll);
    if(allGood){return;}
    setAll.removeAll(setSome);
    throw new EndError.NotWellFormed(pos,err.apply(unique(setAll)));
    }
  void checkInfo(Core.L l){
    if(l.info().watched().contains(P.pThis0)){
      throw new EndError.NotWellFormed(l.poss(),Err.noSelfWatch());
      }
    ArrayList<P.NCs>typePs=new ArrayList<>(); 
    ArrayList<P.NCs>cohePs=new ArrayList<>();
    ArrayList<P.NCs>metaCohePs=new ArrayList<>();
    Top.collectDeps(p(), l.mwts(), typePs, cohePs, metaCohePs,false);
    Top.collectDepDocs(l.docs(),typePs);
    for(var t:l.ts()){
      if(t.p().isNCs()){typePs.add(t.p().toNCs());}
      }
    ArrayList<P.NCs> watchedPs=new ArrayList<>();
    Top.collectWatched(l, watchedPs);
    ArrayList<P.NCs> hiddenPs=new ArrayList<>();
    Top.collectHidden(l, hiddenPs);
    ArrayList<S> refined=new ArrayList<>();
    Top.collectRefined(p(),refined);
    //l can be different from p().top because all nested stuff has been inited in l and not in p().top
    checkMissing(typePs,l.info().typeDep(),l.poss(),Err::missedTypeDep);
    checkMissing(cohePs,l.info().coherentDep(),l.poss(),Err::missedCoheDep);
    checkMissing(metaCohePs,l.info().metaCoherentDep(),l.poss(),Err::missedMetaCoheDep);
    checkMissing(watchedPs,l.info().watched(),l.poss(),Err::missedWatched);
    checkMissing(hiddenPs,l.info().hiddenSupertypes(),l.poss(),Err::missedHiddenSupertypes);
    checkMissing(refined,L(l.mwts().stream().map(m->m.key())),l.poss(),Err::missedRefined);
    boolean refinedExact=l.info().refined().containsAll(refined) && refined.containsAll(l.info().refined());
    if(!refinedExact){
      throw new EndError.NotWellFormed(l.poss(),Err.mismatchRefine(
        L(refined.stream().distinct()),l.info().refined()));
      }
    if(l.info().isTyped()){
      for(var p:l.info().typeDep()){
        var li=p().of(p,l.poss());//throw the right error if path not exists
        if(li.isFullL()){
          throw new EndError.NotWellFormed(l.poss(),Err.typeDependencyNotCore(p));
          }
        }
      ProgramTypeSystem.type(true,p().update(l,false));
      }
        
    //TODO: ??? usedMethods=(P.s)s,
    }
  @Override public Core.L coreLHandler(Core.L s){
    s=super.coreLHandler(s);
    checkUniqueNs(s);
    checkInfo(s);
    var this0s=L(s.ts().stream().filter(this::invalidAfter));
    if(this0s.isEmpty()){return s;}
    throw new EndError.InvalidImplements(s.poss(),Err.nestedClassesImplemented(this0s));
    }
  @Override public X visitX(X x){
    f.addToUsed(x);
    return x;
    }
  private boolean invalidAfter(Full.T t){
    assert t._p()!=null;
    return invalidAfter(t._p());
    }
  private boolean invalidAfter(Core.T t){
    return invalidAfter(t.p());
    }
  private boolean invalidAfter(P p0){
    if(!pStart.pTails.isEmpty()){return false;}
    if(!p0.isNCs()){return false;}
    P.NCs p=p0.toNCs();
    if(p.n()==0){return true;}
    if(p.cs().isEmpty()){return false;}
    C c=p.cs().get(0);
    LL l=this.p().pop(p.n()).top;
    LDom d=this.whereFromTop().get(this.whereFromTop().size()-p.n());
    int dn=-1;
    int cn=-1;
    if(!l.isFullL()){return false;}
    var fl=(Full.L)l;
    for(int i:range(fl.ms())){
      var ki=fl.ms().get(i).key();
      if(ki.equals(d)){dn=i;}
      if(ki.equals(c)){
        var nci=(Full.L.NC)fl.ms().get(i);
        if(nci._e() instanceof Core.L){cn=-2;}
        else{cn=i;}
        }
      }
    /*else{
      var cl=(Core.L)l;
      if(d instanceof S){dn=0;}
      for(int i:range(cl.ncs())){
        var ki=cl.ncs().get(i).key();
        if(ki.equals(d)){dn=i+1;}
        if(ki.equals(c)){cn=i+1;}
        }      
      }*/
    assert dn!=-1;
    assert cn!=-1;
    assert dn!=cn;
    return dn<cn;
    }
  @Override public Info visitInfo(Info info) {
    info=super.visitInfo(info);
    info=info.withTypeDep(unique(info.typeDep()));
    info=info.withCoherentDep(unique(info.coherentDep()));
    info=info.withWatched(unique(info.watched()));
    info=info.withHiddenSupertypes(unique(info.hiddenSupertypes()));
    info=info.withRefined(unique(info.refined()));
    info=info.withUsedMethods(unique(info.usedMethods()));
    return info;
    }
  @Override public P visitP(P p) {return min(p);}
  
  @Override public CsP visitCsP(CsP s) {
    if(s._p()!=null){return s.with_p(min(s._p()));}
    return new CsP(s.pos(),L(),min(p().resolve(s.cs(),s.poss())));
    }
  @Override public Full.T visitT(Full.T s) {
    s=super.visitT(s);
    if(s._p()!=null){return s.with_p(min(s._p()));}
    return new Full.T(s._mdf(),s.docs(),L(),min(p().resolve(s.cs(),poss)));
    }    
  @Override public Full.PathSel visitPathSel(Full.PathSel s) {
    if(s._p()!=null){return s.with_p(min(s._p()));}
    return new Full.PathSel(L(),min(p().resolve(s.cs(),poss)),s._s(),s._x());
    }
  private P min(P p){
    if(!p.isNCs()){return p;}
    var p0=p.toNCs();
    if(p0.n()>p().dept()){
      throw new EndError.PathNotExistent(poss,Err.thisNumberOutOfScope(p));
      }
    return p().minimize(p0);
    }
  }
