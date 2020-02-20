package is.L42.typeSystem;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.toOneOrBug;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.EX;
import is.L42.generated.Core.PCastT;
import is.L42.generated.Core.T;
import is.L42.generated.Core.XP;
import is.L42.generated.Full;
import is.L42.generated.Mdf;
import static is.L42.generated.Mdf.*;
import is.L42.generated.P;
import is.L42.generated.S;
public class TypeManipulation {

  public static boolean fwd_or_fwdP_in(Mdf m){
    return m.isIn(ImmutableFwd,ImmutablePFwd,MutableFwd,MutablePFwd);
    }
  public static boolean fwd_or_fwdP_inMdfs(Collection<Mdf> mdfs){
    return mdfs.stream().anyMatch(m->fwd_or_fwdP_in(m));
    }
  public static boolean fwd_or_fwdP_inTs(Stream<T> ts){
    return ts.anyMatch(t->fwd_or_fwdP_in(t.mdf()));
    }
  public static Mdf noFwd(Mdf mdf){
    if(mdf.isIn(ImmutableFwd,ImmutablePFwd)){return Immutable;}
    if(mdf.isIn(MutableFwd,MutablePFwd)){return Mutable;}
    return mdf;
    }
  public static Mdf mutToCapsule(Mdf m){
    assert !m.isIn(Mdf.MutableFwd,Mdf.MutablePFwd);
    if(m==Mdf.Mutable){return Mdf.Capsule;}
    return m;
    }
  public static T toImmOrCapsule(T t){
    return t.withMdf(toImmOrCapsule(t.mdf()));
    }
  public static Mdf toImmOrCapsule(Mdf m){
    if (m.isIn(Mdf.Lent,Mdf.Mutable,Mdf.MutableFwd,Mdf.MutablePFwd)){
      return Mdf.Capsule;
      }
    if(m==Mdf.Readable){return Mdf.Immutable;}
    return m;
    }
  public static Mdf _toLent(Mdf m){
    if(m.isIn(MutableFwd,MutablePFwd)){return null;}
    if(m.isMut()){return Lent;}
    return m;
    }
  public static T mutToCapsuleAndFwdMutToFwdImm(T t){
    return t.withMdf(mutToCapsuleAndFwdMutToFwdImm(t.mdf()));
    }
  public static Mdf mutToCapsuleAndFwdMutToFwdImm(Mdf m){
    assert m!=Mdf.MutablePFwd;
    if(m==Mdf.Mutable){return Mdf.Capsule;}
    if(m==Mdf.MutableFwd){return Mdf.ImmutableFwd;}
    return m;
    }
  public static T mutToCapsuleAndFwdToRead(T t){
    return t.withMdf(mutToCapsuleAndFwdToRead(t.mdf()));
    }
  public static Mdf mutToCapsuleAndFwdToRead(Mdf m){
    assert m!=Mdf.MutablePFwd;
    if(m==Mdf.Mutable){return Mdf.Capsule;}
    if(m==Mdf.MutableFwd){return Mdf.Readable;}
    if(m==Mdf.ImmutableFwd){return Mdf.Immutable;}
    return m;
    }

  

  public static Core.T capsuleToLent(Core.T t){
    if(t.mdf()==Mdf.Capsule){return t.withMdf(Mdf.Lent);}
    return t;
    }
  public static Core.T toRead(Core.T t){
    if(!t.mdf().isIn(Lent,Mutable,Capsule)){return t;}//imm, read, class, fwd, fwd%
    return t.withMdf(Readable);
    }  
  public static Core.MH toCore(Full.MH mh){
    Mdf mdf=mh._mdf();
    if(mdf==null){mdf=Mdf.Immutable;}
    T t=toCore(mh.t());
    S s=mh.key();
    List<T> pars=toCoreTs(mh.pars());
    List<T> exceptions=toCoreTs(mh.exceptions());
    return new Core.MH(mdf, toCoreDocs(mh.docs()), t, s, pars, exceptions);
    }
  public static List<Core.Doc> toCoreDocs(List<Full.Doc> docs){
    return L(docs,(c,d)->c.add(toCore(d)));
    }
  public static List<Core.T> toCoreTs(List<Full.T> ts){
    return L(ts,(c,t)->c.add(toCore(t)));
    }
    
  public static T toCore(Full.T t){
    assert t.cs().isEmpty() && t._p()!=null;
    Mdf mdf=t._mdf();
    if(mdf==null){mdf=Mdf.Immutable;}
    return new T(mdf,toCoreDocs(t.docs()),t._p());
    }
  public static Core.Doc toCore(Full.Doc doc){
    return new Core.Doc(_toCore(doc._pathSel()),doc.texts(), toCoreDocs(doc.docs()));
    }
  public static Core.PathSel _toCore(Full.PathSel _p){
    if(_p==null){return null;}
    if(_p._p()==null && _p.cs().isEmpty()){
      return new Core.PathSel(P.coreThis0.p(),_p._s(),_p._x());
      }
    assert _p._p()!=null;
    return new Core.PathSel(_p._p(),_p._s(),_p._x());
    }
  public static P.NCs _skipThis0(P.NCs pi,Core.L l){
    if(pi.n()==0){
      Program.flat(l).of(pi,l.poss());//propagate errors is path is not existent
      return null;
      }
    return pi.toNCs().withN(pi.n()-1);
    }
  public static<E> void skipThis0(List<E> es ,Core.L l,Function<E,P.NCs> in,BiConsumer<E,P.NCs>c){
    for(var e:es){
      var res=_skipThis0(in.apply(e),l);
      if(res!=null){c.accept(e,res);}
      };
    }
  public static List<Mdf> generalEnoughMdf(Set<Mdf> mdfs){
    return L(c->{
      for(Mdf mdf:Mdf.values()){
        if(mdf.isIn(Mdf.ImmutablePFwd,Mdf.MutablePFwd)){continue;}
        if(mdfs.stream().allMatch(mdf1->Program.isSubtype(mdf1,mdf))){
          c.add(mdf);
          }
        }
      });
    }
  public static Mdf _mostGeneralMdf(Set<Mdf> mdfs){
    var g=generalEnoughMdf(mdfs);
    return g.stream().filter(mdf->g.stream()
      .allMatch(mdf1->Program.isSubtype(mdf, mdf1)))
      .reduce(toOneOrBug()).orElse(null);
    }
  public static Mdf fwdOf(Mdf m){
    if(m.isIn(Immutable,ImmutablePFwd)){return ImmutableFwd;}
    if(m.isIn(Mutable,MutablePFwd)){return MutableFwd;}
    return m;
    }
  public static Mdf fwdPOf(Mdf m){
    if(m.isImm()){return ImmutablePFwd;}
    if(m.isMut()){return MutablePFwd;}
    return m;
    }
  public static P guess(G g, XP xp){
  if(xp instanceof EX){return g.of(((EX)xp).x()).p();}
  return ((PCastT)xp).t().p();
  }
    
  }
