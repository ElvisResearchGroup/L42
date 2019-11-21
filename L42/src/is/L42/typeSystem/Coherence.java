package is.L42.typeSystem;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.generated.Mdf.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.E;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.X;
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
    if(classMhs.isEmpty()){return true;}
    //TODO: add isCloseState in info?
    var uniqueNums=mhs.stream()
      .map(m->m.key().uniqueNum()).distinct().count();
    if(uniqueNums>1){
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
  public boolean coherentClass(MH mh, Set<X> xz){
    assert mh.key().xs().containsAll(xz) && xz.containsAll(mh.key().xs());
    Mdf mdf=mh.t().mdf();
    if(!p.isSubtype(P.pThis0,mh.t().p(),p.topCore().poss())){return false;}
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
    return true;
    }
  public boolean coherentSetter(MH mh, Set<X> xz){
    if(!p.isSubtype(P.coreVoid,mh.t(),p.topCore().poss())){return false;}
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
      if(!p.isSubtype(ti.p(), p1,p.topCore().poss())){return false;}
      }
    return coherentGetMdf(mdf1,mh.mdf(),fieldTs,fieldAccessMdf(x,mh.mdf()));
    }
  private List<T> fieldTs(X x,Mdf mdf){
    return L(mhs,(c,mh)->{
      if(mh.mdf().isClass()){//factory
        if(!canAlsoBe(mh.t().mdf(),mdf)){return;}
        int index=mh.key().xs().indexOf(x);
        assert index!=-1;
        c.add(mh.pars().get(index));
        return;
        }//else is setter
      if(mh.key().xs().size()!=1){return;}//else is setter
      if(mh.mdf().isCapsule()){return;}
      if(!canAlsoBe(mh.mdf(),mdf)){return;}
      X xi=fieldName(mh);
      if(!xi.equals(x)){return;}      
      if(allowedAbstract(mh)){return;}//TODO: we could cached allowedAbstracts
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
      if(mh.mdf().isCapsule()){return;}
      assert !mh.t().mdf().isClass();// TODO: is the check 'or mdf" = class' needed or not?, when we know, remove todo for file 6
      if(!canAlsoBe(mh.mdf(),mdf)){return;}
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
  private X fieldName(MH mh){
    String x=mh.key().m();
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
  public boolean allowedAbstract(MH mh){
    return !mh.mdf().isClass() &&
      classMhs.stream().allMatch(k->!canAlsoBe(k.t().mdf(),mh.mdf()));
    }
  static public boolean canAlsoBe(Mdf mdf0, Mdf mdf){
    return switch(mdf0){
      case Capsule,Mutable-> !mdf.isClass();
      case Lent-> mdf.isIn(Mdf.Mutable,Mdf.Lent,Mdf.Readable,Mdf.MutableFwd);
      case Readable,Immutable->mdf.isIn(Mdf.Readable,Mdf.Immutable,Mdf.ImmutableFwd);
      default->false;
      };
    }
  static public void coherentE(Program p,E e,ArrayList<P.NCs> cohePs){
    for(var pi:cohePs){
      var p0=p.navigate(pi);
      new Coherence(p0,false).isCoherent(false);
      }
    }
  }