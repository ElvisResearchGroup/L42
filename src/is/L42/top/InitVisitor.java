package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unique;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.*;
import is.L42.generated.Core.Info;
import is.L42.generated.Full.CsP;
import is.L42.visitors.CloneVisitorWithProgram;
import is.L42.visitors.WellFormedness;

class InitVisitor extends CloneVisitorWithProgram{
  public InitVisitor(FreshNames f,Program p) {super(p);this.f=f;this.pStart=p;}
  FreshNames f;
  Program pStart;
  HashMap<Integer,List<Pos>> uniqueNs=new HashMap<>();
  
  private Path resolveSystemIndependent(Path path,String inner,List<Pos>poss){
    Path res=path.resolve(inner);
    File f=res.toFile();
    try{res=f.getCanonicalFile().toPath();}
    catch(IOException e) {}
    String lastName=res.getName(res.getNameCount()-1).toString();
    if(!lastName.equals(inner)) {
      throw new EndError.InvalidImplements(poss,ErrMsg.dotDotDotSouceRepeated(inner,res));
      }
    if(f.isHidden()) {
      throw new EndError.InvalidImplements(poss,ErrMsg.dotDotDotSouceHidden(inner));
      }
    return res;
    }
  private Path systemIndependentPath(Path outerPath,String inner,List<Pos>poss){
    Path path=resolveSystemIndependent(outerPath,inner,poss);
    if(Files.exists(path) && Files.isDirectory(path)){
      return path.resolve("This.L42");
      }
    if(!Files.exists(path)){return resolveSystemIndependent(outerPath,inner+".L42",poss);}
    return path;
    }
  LL addDots(Full.L s){
    LDom cms=getLastCMs();
    if(cms==null ||!(cms instanceof C)){
      throw new EndError.InvalidImplements(s.poss(),ErrMsg.invalidDotDotDotLocation());} 
    C lastC=(C)cms;
    Path outerPath=Paths.get(s.pos().fileName()).getParent();
    Path path=systemIndependentPath(outerPath,lastC.inner(),s.poss());
    LL dots;try {dots=Parse.sureProgram(path,Parse.codeFromPath(path)).top;}
    catch(IOException ioe){
      throw new EndError.InvalidImplements(s.poss(),ErrMsg.dotDotDotSouceNotExistant(path));
      }
    dots.wf();
    if(dots.isFullL()){
      List<Full.L.M> newMs=merge(((Full.L)dots).ms(),s.ms());
      newMs=Init.exapandMs(newMs);
      return ((Full.L)dots).withMs(newMs);
      }
    if(s.ms().isEmpty()){return dots;}
    throw new EndError.InvalidImplements(s.poss(),ErrMsg.dotDotDotCoreSouceWithMs());
    }
  boolean isDefined(S s,List<P>ps,List<Pos>pos){
    for(P p:ps){if(isDefined(s,p,pos)){return true;}}
       return false;
       }
  boolean isDefined(S s,P p,List<Pos>pos){
    if(!p.isNCs()){return false;}
    LL l=p().of(p,pos);
    if(l.isFullL()){return isDefined(s,p.toNCs(),(Full.L)l);}
    return isDefined(s,p.toNCs(),(CoreL)l);
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
    boolean isDefined(S s,P.NCs p,CoreL l){
      boolean found=l.mwts().stream().anyMatch(m->m.key().equals(s));
      if(found){return true;}
      for(var ti:l.ts()){
        var pi=p().from(ti.p(),p);
        if(isDefined(s,pi,l.poss())){return true;}
        }//searching transitively instead of using Info.refine on purpose
      return false;
      }
    void checkUniqueNs(Full.L l){
      var pos=l.poss();
      List<P> ps=L(l.ts().stream().map(t->t._p()));
      List<Integer> ns=L(l.ms(),(c,mi)->{
        if(!mi.key().hasUniqueNum()){return;}
        if(mi.key().uniqueNum()==0){return;}
        LDom key=mi.key();
        if(key instanceof C){c.add(key.uniqueNum());return;}
        //filter the refined methods
        boolean refined=isDefined((S)key,ps,pos);
        if(!refined){c.add(key.uniqueNum());}
        });
      checkUniqueNsCommon(pos, ns);
      }
  void checkUniqueNs(CoreL l){
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
    checkUniqueNsCommon(pos, ns);
    }
  private void checkUniqueNsCommon(List<Pos> pos, List<Integer> ns) { //check they are not seen already
    assert !uniqueNs.containsKey(0);
    if(!Collections.disjoint(uniqueNs.keySet(),ns)){
      List<Pos> morePos=L(ns,(c,ni)->{
        var posi=uniqueNs.get(ni);
        if(posi!=null){c.addAll(posi);}
        });
      throw new EndError.NotWellFormed(pos,ErrMsg.nonUniqueNumber(ns,morePos));
      }
    for(var n:ns){if(n!=0){uniqueNs.put(n,pos);}} 
    }
  @Override public LL visitL(Full.L s){
    if(!s.isDots()){
      var newMs=Init.exapandMs(s.ms());
      return super.visitL(s.withMs(newMs));
      }
    LL res=addDots(s);
    if(res.isFullL()){return super.visitL((Full.L)res);}
    return super.visitL((CoreL)res);
    }
  @Override public LL fullLHandler(Full.L s){
    if(s.isDots()){throw bug();}
    s=(Full.L)super.fullLHandler(s);//the only case it changes type is for the '...'
    List<Full.T> this0s=L(s.ts().stream().filter(this::invalidAfter));
    if(!this0s.isEmpty()){
      throw new EndError.InvalidImplements(s.poss(),ErrMsg.nestedClassesImplemented(this0s));
      }
    checkUniqueNs(s);
    //TODO: the above test is dangerous, it may encounter ... or reuse, and then we do not know if it is in the domain (good AssertionError)
    return s;        
    }
  void checkInfo(CoreL l){WellFormedness.checkInfo(p(),l);}
  @Override public CoreL coreLHandler(CoreL s){
    s=super.coreLHandler(s);
    checkUniqueNs(s);
    checkInfo(s);
    var this0s=L(s.ts().stream().filter(this::invalidAfter));
    if(this0s.isEmpty()){return s;}
    throw new EndError.InvalidImplements(s.poss(),ErrMsg.nestedClassesImplemented(this0s));
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
    if(p.n()==0){
      LL ll=this.p().top;
      if(!ll.isFullL()){return false;}
      try{return ll.cs(p.cs()).isFullL();}
      catch(ClassCastException cce){return true;}
      }
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
        if(nci._e() instanceof CoreL){cn=-2;}
        else{cn=i;}
        }
      }
    assert dn!=-1:p0+"\n"+l;
    //assert cn!=-1:p0+"\n"+l;//No, it can be hidden behind a reuse url
    if(cn==-1){return false;}
    assert dn!=cn:p0+"\n"+l;
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
    return new CsP(s.pos(),L(),Init.resolveCsP(p(),s));
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
  private P min(P p){return min(p,p(),poss);}
  static P min(P p,Program prog,List<Pos>poss){
    if(!p.isNCs()){return p;}
    var p0=p.toNCs();
    if(p0.n()>prog.dept()){
      throw new EndError.PathNotExistent(poss,ErrMsg.thisNumberOutOfScope(p));
      }
    return prog.minimize(p0);
    }
  }
