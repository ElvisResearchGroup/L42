package is.L42.typeSystem;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.generated.Mdf.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.E;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.nativeCode.TrustedKind;
import is.L42.tools.InductiveSet;
import is.L42.visitors.Accumulate;

public class Coherence {
  public final Program p;
  public final List<MH> mhs;
  public final List<MH> classMhs;
  public Coherence(Program p, boolean onlyPrivate){
    this.p=p;
    mhs=L(p.topCore().mwts(),(c,mi)->{
      if(mi._e()==null && (!onlyPrivate || mi.key().hasUniqueNum())){c.add(mi.mh());}
      });
    classMhs=L(mhs.stream().filter(mh->mh.mdf().isClass()));
    }
    
  public boolean isCoherent(boolean justResult){
    if(p.topCore().isInterface()){return true;}
    if(classMhs.isEmpty()){
      boolean emptyNative=p.topCore().info().nativeKind().isEmpty();
      if(emptyNative || justResult){return emptyNative;}
      throw new EndError.CoherentError(p.topCore().poss(),
        Err.nativeFactoryAbsent(p.topCore().info().nativeKind())
        );
      }
    boolean closeClass=p.topCore().info().close() && !p.topCore().isInterface();
    var uniqueNums=mhs.stream()
      .map(m->m.key().uniqueNum()).distinct().count();
    boolean err=uniqueNums>1;//where absence of uniqueNum is also a uniqueNum value
    if(!err && closeClass){err=mhs.stream().anyMatch(m->!m.key().hasUniqueNum());}
    if(err){//TODO: this error does not discuss all the cases properly, but
      //it can also come up with introspection, so should not disclose informations
      //on uniqueNumbers... is this already the best error to report then?
      if(justResult){return false;}
      throw new EndError.CoherentError(p.topCore().poss(),
        Err.nonCoherentPrivateStateAndPublicAbstractMethods(
        L(mhs.stream().filter(m->!m.key().hasUniqueNum()).map(m->m.key()))
        ));}   
    var xzs=L(classMhs.stream().map(m->new HashSet<>(m.key().xs())).distinct());
    if(xzs.size()>1){
      if(justResult){return false;}
      throw new EndError.CoherentError(p.topCore().poss(),
        Err.nonCoherentNoSetOfFields(xzs));}
    var xz=xzs.get(0);
    for(MH mh:mhs){
      if(!coherent(mh,xz)){
        if(justResult){return false;}
        throw new EndError.CoherentError(p.topCore().poss(),
          Err.nonCoherentMethod(mh.key()));}
      }
    return true;
    }
  public static boolean validConstructorSignature(Program p,MH mh){
    Mdf mdf=mh.t().mdf();
    if(!p._isSubtype(P.pThis0,mh.t().p())){return false;}
    if(mdf.isIn(Class,MutableFwd,ImmutableFwd)){return false;}
    if(mdf.isIn(Immutable,Capsule)){
      for(T t:mh.pars()){
        if(!t.mdf().isIn(Immutable,ImmutableFwd,Capsule,Class)){return false;}
        }
      }
    else if(!mdf.isIn(Readable,Lent)){
      for(T t:mh.pars()){
        if(t.mdf().isIn(Readable,Lent)){return false;}
        }
      }
    var nat=p.topCore().info().nativeKind();
    TrustedKind tk=null;
    if(!nat.isEmpty()){tk=TrustedKind._fromString(nat);}
    return tk==null || tk.typePluginK(p,mh);
    }
  public boolean coherentClass(MH mh, Set<X> xz){
    assert mh.key().xs().containsAll(xz) && xz.containsAll(mh.key().xs());
    return validConstructorSignature(p,mh);
    }
  public boolean coherentSetter(MH mh, Set<X> xz){
    if(!p._isSubtype(P.coreVoid,mh.t())){return false;}
    Mdf mdf=mh.mdf();
    T parT=mh.pars().get(0);
    Mdf mdf1=parT.mdf();
    if(!mdf1.isIn(Immutable,Mutable,Capsule,Class)){return false;}
    if(mdf.isLent() && mdf1.isMut()){return false;}
    X x=fieldName(mh);
    if(!xz.contains(x)){return false;}
    return mdf.isIn(Lent,Mutable);
    }
  public boolean coherentGetter(MH mh, Set<X> xz){
    assert !mh.mdf().isClass();
    X x=fieldName(mh);
    P p1=mh.t().p();
    Mdf mdf1=mh.t().mdf();
    if(!xz.contains(x)){return false;}
    List<T> fieldTs=fieldTs(x,mh.mdf());
    for(T ti:fieldTs){
      if(!p._isSubtype(ti.p(), p1)){return false;}
      }
    return coherentGetMdf(mdf1,mh.mdf(),fieldTs,fieldAccessMdf(x,mh.mdf()));
    }
  public List<T> fieldTs(X x,Mdf mdf){
    return L(mhs,(c,mh)->{
      if(mh.mdf().isClass()){//factory
        if(!canAlsoBe(mh.t().mdf(),mdf)){return;}
        int index=mh.key().xs().indexOf(x);
        assert index!=-1: x+" "+mh;
        c.add(mh.pars().get(index));
        return;
        }//else is setter
      if(mh.key().xs().size()!=1){return;}//else is setter
      if(mh.mdf().isCapsule()){return;}
      if(!canAlsoBe(mh.mdf(),mdf)){return;}
      X xi=fieldName(mh);
      if(!xi.equals(x)){return;}      
      if(allowedAbstract(mh)){return;}//note: caching allowedAbstracts may only have a minor impact
      c.add(mh.pars().get(0));
      });
    }
  private List<Mdf> fieldAccessMdf(X x,Mdf mdf){
    return L(mhs,(c,mh)->{
      if(mh.mdf().isClass()){return;}
      if(!mh.key().xs().isEmpty()){return;}
      X xi=fieldName(mh);
      if(!xi.equals(x)){return;}
      if(allowedAbstract(mh)){return;}
      if(!canAlsoBe(mh.mdf(),mdf)){return;}
      if(mh.mdf().isCapsule() && !mh.t().mdf().isClass()){return;}
      c.add(mh.t().mdf());
      });
    }
  private boolean coherentGetMdf(Mdf valueMdf,Mdf getterMdf,List<T> inMdfs,List<Mdf> outMdfs){
    switch(valueMdf){
      case Class: return inMdfs.stream().allMatch(t->t.mdf().isClass());
      case Readable: return inMdfs.stream().noneMatch(t->t.mdf().isClass());
      case Immutable:
        if(getterMdf.isImm()){return inMdfs.stream().noneMatch(t->t.mdf().isClass());}
        return 
          inMdfs.stream().allMatch(t->t.mdf().isIn(Immutable,ImmutableFwd,Capsule))
          && outMdfs.stream().noneMatch(m->m.isIn(Mutable,Lent));
      case Capsule: return getterMdf.isCapsule() &&
        inMdfs.stream().allMatch(t->t.mdf().isIn(Mutable,MutableFwd,Capsule))
        && outMdfs.stream().noneMatch(m->m.isImm());
      case Lent: return getterMdf.isIn(Lent,Mutable,Capsule) &&
        inMdfs.stream().allMatch(t->t.mdf().isIn(Mutable,MutableFwd,Capsule,Lent))
        && outMdfs.stream().noneMatch(m->m.isImm());
      case Mutable: return getterMdf.isIn(Mutable,Capsule) &&
        inMdfs.stream().allMatch(t->t.mdf().isIn(Mutable,MutableFwd,Capsule))
        && outMdfs.stream().noneMatch(m->m.isImm());
      default: return false;
      }
    }  
  public static X fieldName(MH mh){return fieldName(mh.key());}
  public static X fieldName(S s){
    String x=s.m();
    while(x.startsWith("#")){x=x.substring(1);}
    return new X(x);
    }
  public boolean coherent(MH mh, Set<X> xz){
    if(mh.mdf().isClass()){return coherentClass(mh,xz);}
    if(allowedAbstract(mh)){return true;}
    if(mh.key().xs().size()==1){return coherentSetter(mh,xz);}
    if(mh.key().xs().isEmpty()){return coherentGetter(mh,xz);}
    return false;
    }
  public static boolean allowedAbstract(MH mh,List<MH> classMhs){
    return !mh.mdf().isClass() &&
      classMhs.stream().allMatch(k->!canAlsoBe(k.t().mdf(),mh.mdf()));
      //it is not relevant to use this or the more specific constructor signature    
    }
  public boolean allowedAbstract(MH mh){
    return allowedAbstract(mh,classMhs);
    }
  static public boolean canAlsoBe(Mdf mdf0, Mdf mdf){
    if(mdf0==null) {mdf0=Mdf.Immutable;}
    if(mdf==null) {mdf=Mdf.Immutable;}
    return switch(mdf0){
      case Capsule,Mutable-> !mdf.isClass();
      case Lent-> mdf.isIn(Mdf.Mutable,Mdf.Lent,Mdf.Readable,Mdf.MutableFwd);
      case Readable,Immutable->mdf.isIn(Mdf.Readable,Mdf.Immutable,Mdf.ImmutableFwd);
      default->false;
      };
    }
  static public void coherentAllPs(Program p,List<P.NCs> cohePs, ArrayList<HashSet<List<C>>> alreadyCoherent){
    for(var pi:cohePs){coherentE(p,pi,alreadyCoherent);}
    }
  static private void coherentE(Program p,P.NCs coheP,ArrayList<HashSet<List<C>>> alreadyCoherent){
    var setCs=alreadyCoherent.get((alreadyCoherent.size()-1)-coheP.n());
    if(setCs.contains(coheP.cs())){return;}
    var p0=p.navigate(coheP);
    new Coherence(p0,false).isCoherent(false);
    setCs.add(coheP.cs());
    var cohePs=p0.topCore().info().coherentDep();
    for(P.NCs pi:cohePs){coherentE(p,p.from(pi,coheP),alreadyCoherent);}
    }

  }