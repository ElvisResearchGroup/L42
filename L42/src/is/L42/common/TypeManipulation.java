package is.L42.common;

import java.util.Set;
import java.util.stream.Stream;

import is.L42.generated.Mdf;

public class TypeManipulation {

  public static boolean fwd_or_fwdP_inMdfs(Stream<Mdf> mdfs){
    return mdfs.anyMatch(m->m.isIn(Mdf.ImmutableFwd,Mdf.ImmutablePFwd,Mdf.MutableFwd,Mdf.MutablePFwd));
    }
}
