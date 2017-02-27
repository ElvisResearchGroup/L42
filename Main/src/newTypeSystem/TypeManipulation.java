package newTypeSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import newTypeSystem.TypeSystem.TIn;
import newTypeSystem.TypeSystem.TOk;
import ast.Ast;
import ast.ExpCore;

public class TypeManipulation {

public static NormType fwd(NormType t){
//  fwd T
//    fwd imm P=fwd fwd%imm P=fwdImm P
//    fwd mut P=fwd fwd%mut P=fwdMut P
//    otherwise fwd T=T
  Mdf m=t.getMdf();
  if(m==Mdf.Immutable || m==Mdf.ImmutablePFwd){
    return t.withMdf(Mdf.ImmutableFwd);
    }
  if(m==Mdf.Mutable ||m==Mdf.MutablePFwd){
    return t.withMdf(Mdf.MutableFwd);
    }
  return t;
  }
public static NormType fwdP(NormType t){
//  fwd% T
//    fwd% imm P=fwd%Imm P
//    fwd% mut P=fwd%Mut P
//    otherwise fwd% T=T
  Mdf m=t.getMdf();
  if(m==Mdf.Immutable){
    return t.withMdf(Mdf.ImmutableFwd);
    }
  if(m==Mdf.Mutable){
    return t.withMdf(Mdf.MutableFwd);
    }
  return t;
  }

public static boolean fwd_or_fwdP_in(Mdf m){
  return m==Mdf.ImmutableFwd 
      || m==Mdf.ImmutablePFwd
      || m==Mdf.MutableFwd
      || m==Mdf.MutablePFwd;
  }

public static boolean fwd_or_fwdP_in(Collection<NormType>ts){
//  fwd_or_fwd%_in Ts
//    exists T in Ts such that
//    T in {fwdImm _,fwdMut_,fwd%Imm _,fwd%Mut _}
  for(NormType ti:ts){
    if(fwd_or_fwdP_in(ti.getMdf())){return true;}
    }
  return false;
  }
public static boolean fwd_or_fwdP_inMdfs(Collection<Mdf>ms){
  for(Mdf mi:ms){
    if(fwd_or_fwdP_in(mi)){return true;}
    }
  return false;
  }

public static NormType noFwd(NormType t){
//  noFwd T
//    noFwd fwdImm P=noFwd fwd%Imm P=imm P
//    noFwd fwdMut P=noFwd fwd%Mut P=mut P
//    otherwise noFwd T=T
  Mdf m=t.getMdf();
  if(m==Mdf.ImmutableFwd || m==Mdf.ImmutablePFwd){
    return t.withMdf(Mdf.Immutable);
    }
  if(m==Mdf.MutableFwd ||m==Mdf.MutablePFwd){
    return t.withMdf(Mdf.Mutable);
    }
  return t;
  }
public static List<NormType>  noFwd(Collection<NormType>ts){
//  noFwd T1..Tn= noFwd T1 .. noFwd Tn
  List<NormType>res=new ArrayList<>();
  for(NormType ti:ts){res.add(noFwd(ti));}
  return res;
  }

public static NormType toImm(NormType t){//used only for fields in coherent
//  toImm(T)        
//    toImm(class P)=class P
//    otherwise, toImm(mdf P)=imm P
  if(t.getMdf()==Mdf.Class){return t;}
  return t.withMdf(Mdf.Immutable);
  }
public static NormType toImmOrCapsule(NormType t){
//  toImmOrCapsule(T)
//    toImmOrCapsule(mdf C)=capsule C with mdf in {lent,mut,fwdMut,fwd%Mut}
//    toImmOrCapsule(read C)=imm C
//    otherwise toImmOrCapsule(T)=T//mdf in {class,imm,fwdImm,fwd%Imm,capsule}
  Mdf m=t.getMdf();
  if (m==Mdf.Lent ||m==Mdf.Mutable ||m==Mdf.MutableFwd ||m==Mdf.MutablePFwd){
    return t.withMdf(Mdf.Capsule);
    }
  if(m==Mdf.Readable){return t.withMdf(Mdf.Immutable);}
  return t;
  }
public static NormType _toLent(NormType t){
//  toLent(T)
//    toLent(mut P)=lent P,
//    toLent(fwdMut P) and toLent(fwd%Mut P) undefined;
//    otherwise toLent(T)=T
  Mdf m=t.getMdf();
  if(m==Mdf.MutableFwd || m==Mdf.MutablePFwd){return null;}
  if(m==Mdf.Mutable){return t.withMdf(Mdf.Lent);}
  return t;
  }

public static NormType mutOnlyToLent(NormType t){
//  mutOnlyToLent(T)
//    mutOnlyToLent(mut P)=lent P,
//    otherwise mutOnlyToLent(T)=T 
  if(t.getMdf()==Mdf.Mutable){return t.withMdf(Mdf.Lent);}
  return t;
  }
public static NormType capsuleToLent(NormType t){
//  capsuleToLent(T)  
//    capsuleToLent(capsule P)=lent P
//    otherwise capsuleToLent(mdf P)=mdf P
  if(t.getMdf()==Mdf.Capsule){return t.withMdf(Mdf.Lent);}
  return t;
  }

public static NormType _toRead(NormType t){   
//  toRead(T)
//    toRead(fwdMut P)=toRead(fwd%Mut P)=undefined  
//    toRead(fwdImm P)=toRead(fwd%Imm P)=undefined
//    toRead(lent P)=toRead(mut P)=toRead(capsule P)=read P
//    toRead(lent P)=toRead(mut P)=toRead(capsule P)=read P
//    otherwise read(T)=T//mdf in imm,read,class  
  Mdf m=t.getMdf();
  if(m==Mdf.MutableFwd || m==Mdf.MutablePFwd){return null;}
  if(m==Mdf.ImmutableFwd || m==Mdf.ImmutablePFwd){return null;}
  if(m==Mdf.Lent ||m==Mdf.Mutable||m==Mdf.Capsule){
    return t.withMdf(Mdf.Readable);
    }
  return t;
  }
 

public static NormType lentToMut(NormType t){
//  lentToMut(T)
//    lentToMut(lent C)=mut C
//    otherwise lentToMut(T)=T  
  if(t.getMdf()==Mdf.Lent){return t.withMdf(Mdf.Mutable);}
  return t;
  }
public static NormType mutToCapsule(NormType t){
//  mutToCapsule(T)
//    mutToCapsule(fwdMut C) and mutToCapsule(fwd%Mut C) undefined
//    mutToCapsule(mut C)=capsule C
//    otherwise mutToCapsule(T)=T
  Mdf m=t.getMdf();
  assert m!=Mdf.MutableFwd && m!=Mdf.MutablePFwd;
  if(m==Mdf.Mutable){return t.withMdf(Mdf.Capsule);}
  return t;
  }

public static NormType mutToCapsuleAndFwdMutToFwdImm(NormType t){
//mutToCapsuleAndFwdMutToFwdImm(T) //called f in the implementation
//f(fwd%Mut P) undefined
//f(mut P)=capsule P
//f(fwdMut P)= fwdImm P 
//otherwise f(T)=T
 Mdf m=t.getMdf();
 assert m!=Mdf.MutablePFwd;
 if(m==Mdf.Mutable){return t.withMdf(Mdf.Capsule);}
 if(m==Mdf.MutableFwd){return t.withMdf(Mdf.ImmutableFwd);}
 return t;
 }
public static NormType mutToCapsuleAndFwdMutToRead(NormType t){
//mutToCapsuleAndFwdMutToRead(T) //called f in the implementation
//f(fwd%Mut P) undefined
//f(mut P)=capsule P
//f(fwdMut P)= read P 
//otherwise f(T)=T
  Mdf m=t.getMdf();
  assert m!=Mdf.MutablePFwd;
  if(m==Mdf.Mutable){return t.withMdf(Mdf.Capsule);}
  if(m==Mdf.MutableFwd){return t.withMdf(Mdf.Readable);}
  return t;
  }

public static Mdf mostGeneralMdf(Ast.SignalKind _throw,TOk out){
//mostGeneralMdf(throw,Tr)  
//  mostGeneralMdf(error,Tr)=imm
//  mostGeneralMdf(return,empty;Ps) undefined
//  mostGeneralMdf(return,T1..Tn;Ps)=mostGeneralMdf({T1.mdf .. Tn.mdf})
//  otherwise 
//  mostGeneralMdf(exception,_;Ps)=imm
  if(_throw!=SignalKind.Return){return Mdf.Immutable;}
  assert !out.returns.isEmpty();
  Stream<Ast.Mdf> s = out.returns.stream().map(t->t.getMdf());
  return mostGeneralMdf(s.collect(Collectors.toSet()));
  }
public static Mdf mostGeneralMdf(Set<Mdf> mdfs){
//mostGeneralMdf(mdfs)
//  mostGeneralMdf(mdfs)=mdf //assert fwd and fwd% not in mdfs
  assert !fwd_or_fwdP_inMdfs(mdfs);
//    if mdfs=mdf', then mdf=mdf' //that is the only way mdf=class
  if (mdfs.size()==1){return mdfs.iterator().next();}
//  otherwise if class in mdfs, then undefined
  assert !mdfs.contains(Mdf.Class);
//  otherwise if read in mdfs, mdf=read
  if(mdfs.contains(Mdf.Readable)){return Mdf.Readable;}
//  //ignoring capsule,
//  //from now on, we have at least two of those:lent, mut, imm //Note: fwd and fwd% are cutted out in the throw rule
//  otherwise if imm in mdfs, mdf=read
  if(mdfs.contains(Mdf.Immutable)){return Mdf.Readable;}
//  otherwise mdf=lent
  return Mdf.Lent;
  }


public static boolean catchRethrow(ExpCore.Block.On k){
  //liberal use of desugaring in the line under
  //catchRethrow(k) iff k=catch throw Any x ((e catch error Any z void void) throw x)
  if(!k.getT().equals(Path.Any().toImmNT())){return false;}
  if(!(k.getE() instanceof ExpCore.Block)){return false;}
  ExpCore.Block b=(ExpCore.Block)k.getE();
  if(!(b.getInner() instanceof ExpCore.Signal)){return false;}
  return false;
  }


}