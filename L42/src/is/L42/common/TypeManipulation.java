package is.L42.common;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Mdf;

public class TypeManipulation {

  public static boolean fwd_or_fwdP_inMdfs(Stream<Mdf> mdfs){
    return mdfs.anyMatch(m->m.isIn(Mdf.ImmutableFwd,Mdf.ImmutablePFwd,Mdf.MutableFwd,Mdf.MutablePFwd));
    }
  public static Core.T capsuleToLent(Core.T t){
    if(t.mdf()==Mdf.Capsule){return t.withMdf(Mdf.Lent);}
    return t;
    }
  public static Core.T _toRead(Core.T t){
  if(t.mdf().isIn(Mdf.MutableFwd,Mdf.MutablePFwd)){return null;}
  if(t.mdf().isIn(Mdf.ImmutableFwd,Mdf.ImmutablePFwd)){return null;}
  if(t.mdf().isIn(Mdf.Lent,Mdf.Mutable,Mdf.Capsule)){
    return t.withMdf(Mdf.Readable);
    }
  return t;//imm, read, class
  }  
}
