package is.L42.top;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L;
import is.L42.generated.Full;
import is.L42.generated.P;

public class CircularityIssue {
  List<P.NCs> typePs;P.NCs path;L l0; Program p; E e;List<Full.L.NC>moreNCs;
  public CircularityIssue(List<P.NCs> typePs,P.NCs path,L l0, Program p, E e,List<Full.L.NC>moreNCs) {
    this.typePs=typePs;this.path=path;this.l0=l0;this.p=p;this.e=e;this.moreNCs=moreNCs;
    }
  public void reportError() {
    for(var pj:typePs){p.of(pj,e.poss());}//good error if pj does not exists
    var p0=p.navigate(path);
    for(var pi:l0.info().typeDep()){p0.of(pi,l0.poss());}
    throw new EndError.TypeError(l0.poss(),"better error for circularity issue. May be "+path+" used Cs contains some typos\n\n"+l0);
    }

}
