package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.toOneOrBug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.nativeCode.CacheNowGenerator;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.tools.General;
import is.L42.translationToJava.J;
import is.L42.typeSystem.Coherence;
import is.L42.visitors.WellFormedness;

public class Close extends GuessFields{
  Program p;
  HashMap<X,MH> capsMuts=new HashMap<>();
  ArrayList<MH> ks=new ArrayList<>();
  HashSet<X> fieldNames=new HashSet<>();
  ArrayList<MWT> newMWTs=new ArrayList<>();
  ArrayList<MWT> oldMWTs=new ArrayList<>();
  public Core.L close(Program p,List<C> cs,boolean autoNorm,Function<L42£LazyMsg,L42Any>wrap){
    this.autoNormed|=autoNorm;
    if(cs.isEmpty()){
      var res=close(p,wrap);
      assert res.wf();
      return res;
      }
    var pIn=p.navigate(cs);
    var l=close(pIn,wrap);
    pIn=pIn.update(l,false);
    assert l.wf();
    var res=pIn._ofCore(P.of(cs.size(),General.L()));
    assert res.wf();
    return res;
    }
  private Core.L close(Program p,Function<L42£LazyMsg,L42Any>wrap){
    this.p=p;
    this.err=new MetaError(wrap);
    var l=p.topCore();
    if(l.info().close()){err.throwErr(l,"Class is already close");}
    addGettersSetters(p);
    for(var m:this.abs){
      if(!Coherence.validConstructorSignature(p, m.mh())){continue;}
      if(!m.key().xs().containsAll(getters.keySet())){continue;}
      //constructors can initialize more fields then the getters
      fieldNames.addAll(m.key().xs());
      ks.add(m.mh());
      }
    for(MH k:ks){
      if(k.key().xs().size()!=fieldNames.size()){errorAmbiguousK(l,k);}
      }
    for(var m:l.mwts()){process(m);}
    Info i=l.info();
    i=i.withClose(true);
    if(!i.typeDep().contains(P.pThis0)){i=i.withTypeDep(pushL(i.typeDep(),P.pThis0));}
    if(mustAddThis0Coherence && !i.coherentDep().contains(P.pThis0)){
      i=i.withCoherentDep(pushL(i.coherentDep(),P.pThis0));
      }
    //check that there is no dup in new MWTs
    long countNew=newMWTs.stream().map(m->m.key()).distinct().count();
    if(countNew<newMWTs.size()) {
      var all=L(newMWTs.stream().map(m->m.key()));
      var dups=WellFormedness.dups(all);
      var mErr=_elem(newMWTs,dups.get(0));
      err.throwErr(mErr, "Close is attempting to create multiple versions of methods "+dups);
      }
    var mwts=new SumMethods(err).sum(oldMWTs, newMWTs);
    l= l.withMwts(mwts).withInfo(i);
    J newJ=new J(p.update(l,false),null,null,true){//so that it ignore public abstract methods
      public Coherence newCoherence(Program p){return new Coherence(p,true);}
      };
    assert newJ.isCoherent:
      "";
    assert newJ.fields!=null:"";
    //TODO: need more? is this needed?//that is, can this code be triggered?
    if (this.countMutCache!=0){
      try{new CacheNowGenerator().clearCacheGood(newJ);}
      catch(EndError ee){err.throwErr(l,ee.getMessage());}
      }
    return l;
    }
  private void errorAmbiguousK(Core.L l,MH k){
    Supplier<Object> t=()->l.mwts().stream().filter(m->m.mh()==k).reduce(toOneOrBug()).get();
    Supplier<String> msg=()->"abstract class method "+k.key()
      +" is an ambiguos constructor; all those fields should be initialized: "+
     L(fieldNames.stream().sorted((x1,x2)->x1.inner().compareTo(x2.inner())));
    err.throwErr(t,msg);
    }
  private int countMutCache=0;
  public void process(MWT m){
    if(m._e()==null){
      if(ks.contains(m.mh())){processK(m);return;}
      if(Coherence.allowedAbstract(m.mh(),ks)){processAllowedAbs(m);return;}
      if(getters.containsKey(Coherence.fieldName(m.mh()))){processGetter(m);return;}
      if(setters.containsKey(Coherence.fieldName(m.mh()))){processSetter(m);return;}
      processAllowedAbs(m);//we just let it alone
      return;
      }
    if(match("lazyCache",m)){processLazyCache(m);return;}
    if(match("eagerCache",m)){processEagerCache(m);return;}
    if(match("lazyReadCache",m)){countMutCache++;processLazyReadCache(m);return;}
    var invalidate=match("invalidateCache",m);
    var now=match("readNowCache",m);
    if(!invalidate && !now){processBase(m);return;}
    if(invalidate){countMutCache++;processInvalidate(m);return;}
    if(now){countMutCache++;processNow(m);return;}
    assert false;
    }
  public Core.E fCapsExposer(MWT mErr,Pos pos,X x,T t){
    MH mh=this.capsMuts.get(x);
    if(mh!=null){
      if(!mh.t().equals(t)){
        err.throwErr(mErr,"first parameter does not correspond to a capsule field; ambiguous field type: "+mh.t()+" or "+t);
        }
      return Utils.thisCall(pos,mh.key(),General.L());
      }    
    if(!fieldNames.contains(x)){
      err.throwErr(mErr,"first parameter does not correspond to a capsule field: the parameter name is not a field");
      }
    for(var ki:ks){
      int i=ki.key().xs().indexOf(x);
      assert i!=-1;
      T ti=ki.pars().get(i);
      if(!ti.p().equals(t.p())){
        err.throwErr(mErr,"first parameter does not correspond to a capsule field; ambiguous field type: "+ti+" or "+t);
        }
      if(!ti.mdf().isCapsule() && !ki.t().mdf().isImm()){
        err.throwErr(mErr,"first parameter does not correspond to a capsule field; constructor "+ki+" initializes it as "+ti);
        }
      }
    var setter=setters.get(x);
    if(setter!=null){for(MWT mi:setter){
      T ti=mi.mh().pars().get(0);
      if(!ti.p().equals(t.p())){
        err.throwErr(mErr,"first parameter does not correspond to a capsule field; ambiguous field type: "+ti+" or "+t);
        }
      if(!ti.mdf().isCapsule()){
        err.throwErr(mErr,"first parameter does not correspond to a capsule field; setter "+mi+" update it as "+ti);
        }      
      }}    
    int countHs=0;
    for(MWT m:abs){
      X xi=Coherence.fieldName(m.mh());
      if(!xi.equals(x)){continue;}
      assert !m.key().hasUniqueNum();
      countHs=Math.max(countHs,m.key().m().length()-xi.inner().length());
      if(!m.mh().mdf().isIn(Mdf.Mutable,Mdf.Lent)){continue;}
      if(!m.mh().t().mdf().isIn(Mdf.Mutable,Mdf.Lent)){continue;}
      err.throwErr(mErr,"first parameter does not correspond to a capsule field: the field is exposed by "+mh);
      }
    String hs="#".repeat(countHs+1);
    S s=new S(hs+x.inner(),General.L(),0);
    mh=new MH(Mdf.Mutable,General.L(),t,s,General.L(),General.L());
    this.capsMuts.put(x,mh);
    this.newMWTs.add(new MWT(General.L(pos),General.L(),mh,"",null));
    return Utils.thisCall(pos,mh.key(),General.L());
    }
  String errMsgMoreThenOne(X x,Mdf recMdf1,Mdf recMdf2){
    return "More then one candidate getter/exposer with method modifier"
      +recMdf1+" or "+recMdf2+" in:"+
      General.L(abs.stream().filter(m->
        m.mh().mdf().isIn(recMdf1,recMdf2) 
        && Coherence.fieldName(m.mh()).equals(x)));
    }
  private static final List<Mdf>immCapsClass=List.of(Mdf.Immutable, Mdf.Capsule,Mdf.Class);
  private static final List<Mdf>immCapsClassImmFwd=List.of(Mdf.Immutable, Mdf.Capsule,Mdf.Class,Mdf.ImmutableFwd);
  public Core.E fAcc(boolean noFwdImm,MWT mErr,Pos pos,X x,Mdf recMdf1,Mdf recMdf2){
    var mh=abs.stream().filter(m->
      m.mh().mdf().isIn(recMdf1,recMdf2) 
      && Coherence.fieldName(m.mh()).equals(x)
      ).reduce(toOneOr(()->err.throwErr(mErr,()->errMsgMoreThenOne(x,recMdf1,recMdf2))));
    if(!mh.isPresent()){
      err.throwErr(mErr, "No candidate getter/exposer with name "+x
        +" and method modifier "  +recMdf1.inner+" or "+recMdf2.inner);
      }
    for(var ki:ks){
      int i=ki.key().xs().indexOf(x);
      assert i!=-1;
      T ti=ki.pars().get(i);
      var allowed=noFwdImm?immCapsClass:immCapsClassImmFwd;
      if(!allowed.contains(ti.mdf())){
        if(ti.mdf().isFwdImm()){
          err.throwErr(mErr,"parameter "+x+" is initialized with fwd in the constructor "+ki);  
          }
        err.throwErr(mErr,"parameter "+x+" does not correspond to an imm/capsule/class field; constructor "+ki+" initializes it as "+ti);
        }
      }
    var setter=setters.get(x);
    if(setter!=null){for(MWT mi:setter){
      T ti=mi.mh().pars().get(0);
      if(!ti.mdf().isIn(Mdf.Immutable, Mdf.Capsule,Mdf.Class)){
        err.throwErr(mErr,"parameter "+x+" does not correspond to an imm/capsule/class field; setter "+mi+" updates it as "+ti);
        }      
      }}    
    return Utils.thisCall(pos,mh.get().key().withUniqueNum(0),General.L());
    }
  private static final S normS=S.parse("norm()");
  private Core.E addNorm(Core.E e){
    String varName="x";
    for(X xi:fieldNames) {varName+=xi.inner();}
    X x=new X(varName);
    var d=new Core.D(false,P.coreThis0,x, e);
    var body=new Core.MCall(e.pos(),new Core.EX(e.pos(), x),normS,L());
    return new Core.Block(e.pos(),L(d),L(),body);
    }
  private void processState(MWT m,boolean norm){
    var s=m.key();
    var s2=s.withUniqueNum(0);
    assert !s.hasUniqueNum();
    var m2=m.withMh(m.mh().withS(s2));
    Pos pos=m.poss().get(0);
    List<Core.E> exs=General.L(s.xs(),(ci,xi)->ci.add(new Core.EX(pos,xi)));
    Core.E newE=Utils.thisCall(pos,s2,exs);
    if(norm){newE=addNorm(newE);} 
    m=m.with_e(newE);
    oldMWTs.add(m);
    newMWTs.add(m2);
    }
  private boolean mNormOk(){
    var mNorm=_elem(p.topCore().mwts(),normS);
    if(mNorm==null){return false;}
    var mh=mNorm.mh();
    if(!mh.mdf().isImm() || !mh.t().mdf().isImm()){return false;}
    if(!mh.t().p().equals(P.pThis0)){return false;}
    if(!mh.exceptions().isEmpty()) {return false;}
    return true;    
    }
  public void processK(MWT m){
    if(!this.autoNormed) {processState(m,false);return;}
    if(!mNormOk()){err.throwErr(m,"class can not use eager cache with invalid norm method");}
    var hasFwd=m.mh().pars().stream().anyMatch(t->t.mdf().isIn(Mdf.MutableFwd,Mdf.ImmutableFwd));
    if(hasFwd){err.throwErr(m,"abstract state operation with fwd parameters can not be prenormalized");}
    if(!m.mh().t().mdf().isImm()){err.throwErr(m,"only imm objects can be prenormalized");}
    processState(m,true);
    }
  public void processGetter(MWT m){processState(m,false);}
  public void processSetter(MWT m){processState(m,false);}

  private void checkClassRec(MWT m){
    if(!m.mh().mdf().isClass()){err.throwErr(m,"can not be made into a cached read method; the receiver modifier must be class but it is "+m.mh().mdf().inner);}    
    }
  private void checkZeroPars(MWT m){
    if(!m.key().xs().isEmpty()){err.throwErr(m,"can not be made cached; it must have zero parameters");}
    }
  private void checkRetAndBody(MWT m){
    if(!m.nativeUrl().isEmpty()){err.throwErr(m,"can not be made cached, since it is already native");}
    if(!m.mh().t().mdf().isIn(Mdf.Immutable, Mdf.Class, Mdf.Readable)){err.throwErr(m,"can not be made cached; the return type modifier must be imm, class or read; but it is "+m.mh().t().mdf().inner);}
    }
  public void processLazyCache(MWT m){
    if(!m.mh().mdf().isIn(Mdf.Immutable, Mdf.Class)){err.throwErr(m,"can not be made cached; the receiver modifier must be imm or class but it is "+m.mh().mdf().inner);}
    checkZeroPars(m);
    checkRetAndBody(m);
    //TODO: edit here to add multi parameter lazy cached imm/class
    oldMWTs.add(m.withNativeUrl("trusted:lazyCache"));
    }
  public void processEagerCache(MWT m){
    if(!m.mh().mdf().isImm()){err.throwErr(m,"can not be made cached; the receiver modifier must be imm but it is "+m.mh().mdf().inner);}
    checkZeroPars(m);
    checkRetAndBody(m);
    oldMWTs.add(m.withNativeUrl("trusted:eagerCache"));
    }
  public void processLazyReadCache(MWT m){
    processClassToRead(ClassToRead.LazyRead,m);
    }
  public void processInvalidate(MWT m){
    mustAddThis0Coherence=true;//will use This0
    if(m.key().xs().isEmpty()){
      err.throwErr(m,"first parameter must refer to a capsule field as mut or lent");
      }
    Mdf first=m.mh().pars().get(0).mdf();
    if(!first.isIn(Mdf.Mutable,Mdf.Lent)){
      err.throwErr(m,"first parameter must refer to a capsule field as mut or lent");
      }
    if(!m.mh().pars().stream().skip(1).allMatch(t->t.mdf().isIn(Mdf.Immutable,Mdf.Class,Mdf.Capsule))){
      err.throwErr(m,"non first parameters can not be mut, lent or read");
      }
    if(!m.mh().t().mdf().isIn(Mdf.Immutable,Mdf.Class,Mdf.Capsule)){
      if(!(first.isLent() && m.mh().t().mdf().isMut())){
        err.throwErr(m,"return type must be imm, capsule or class. (Can also be mut if the capsule is seen as lent.)");
        }
      }
    var pos=m._e().pos();
    S s=m.key();
    assert !s.xs().isEmpty();
    var xs1=popL(s.xs());
    var ts1=popL(m.mh().pars());
    S s1=s.withXs(xs1);
    List<Core.E> exs1=General.L(ci->{
      X x=s.xs().get(0);
      T t=m.mh().pars().get(0);
      ci.add(fCapsExposer(m,pos,x,t));
      for(var xi:xs1){ci.add(new Core.EX(pos,xi));}
      });
    var mh1=new MH(Mdf.Mutable,m.mh().docs(),m.mh().t(),s1,ts1,m.mh().exceptions());
    var m1=m.withMh(mh1);
    m1=m1.with_e(Utils.ThisCall(pos,s,exs1));
    newMWTs.add(m1);
    oldMWTs.add(m);
    }
  private boolean mustAddThis0Coherence=false;
  public void processNow(MWT m){
    processClassToRead(ClassToRead.Now,m);
    }
  static enum ClassToRead{
    Now(true,"trusted:readNowCache","readNowCache"),
    LazyRead(false,"trusted:lazyCache","lazyReadCache");
    boolean noFwdImm;
    String trusted;
    String annotation;
    ClassToRead(boolean noFwdImm,String trusted,String annotation){
      this.noFwdImm=noFwdImm;this.trusted=trusted;this.annotation=annotation;
      }
    }
  public void processClassToRead(ClassToRead ctr,MWT m){
    checkRetAndBody(m);
    checkClassRec(m);
    mustAddThis0Coherence=true;//will use This0
    var ok=m.mh().pars().stream().allMatch(t->t.mdf().isIn(Mdf.Immutable,Mdf.Readable,Mdf.Class));
    if(!ok){err.throwErr(m,"all parameters must be imm, readable or class");}
    var s=m.key();
    var s1=s.withXs(General.L());
    assert !s.hasUniqueNum();
    var mh1=new MH(Mdf.Readable,m.mh().docs(),m.mh().t(),s1,General.L(),General.L());
    var old=_elem(p.topCore().mwts(),s1);
    if(old!=null){
      if(old._e()!=null){return;}//Do nothing if the method s1 is already present and implemented
      boolean eq=Utils.equalMH(old.mh(),mh1);
      if(!eq){err.throwErr(old,"Generated method "+mh1+" would conflict with existent abstract method "+old.mh());}
      oldMWTs.add(old);
      }
    var m1=m.withMh(mh1);//m1: the no arg meth calling the static method s
    List<Core.E> exs1=General.L(s.xs(),(ci,xi)->ci.add(fAcc(ctr.noFwdImm,m,m._e().pos(),xi,Mdf.Immutable,Mdf.Readable)));
    m1=m1.with_e(Utils.ThisCall(m.poss().get(0),s,exs1));
    newMWTs.add(m1.withNativeUrl(ctr.trusted));
    oldMWTs.add(m.withDocs(removeClassToReadDoc(ctr.annotation,m.docs())));
    }
  private List<Doc> removeClassToReadDoc(String target,List<Doc>ds){
    return L(c->{
      boolean removed=false;
      for(Doc d:ds){
        if(!removed && Utils.match(p,err,target,null,d)){removed=true;}
        else{c.add(d);}
        }
      });
    }
  public void processAllowedAbs(MWT m){oldMWTs.add(m);}
  public void processBase(MWT m){oldMWTs.add(m);}
  public boolean match(String target,MWT m){return Utils.match(p,err,target,m);}
  }
  /*
   To add multi arg imm/class methods:
    @Cache.Lazy method S name(S par1,Size par2)=e
 //becomes
 @Cache.Lazy method S name(S par1,Size par2)=e
 method S name(S par1,Size par2)=
   FreshCachedName(fresh=this,par1=par1,par2=par2)()
 FreshCachedName={
   read method This1 fresh()
   read method S par1()
   read method S par2()
   class method This (This1 fresh,S par1,S par2)
   S ()=
     native{trusted:immLazyCache}
     this.fresh().name(par1=this.par1(),par2=this.par2())
   }
 //and
 @Cache.Lazy class method S name(S par1,Size par2)=e
 //becomes
 @Cache.Lazy class method S name(S par1,Size par2)=e
 class method S name(S par1,Size par2)=
   FreshCachedName(par1=par1,par2=par2)()
 FreshCachedName={
   read method S par1()
   read method S par2()
   class method This (S par1,S par2)
   S ()=
     native{trusted:immLazyCache}
     This1.name(par1=this.par1(),par2=this.par2())
   }
  
  */