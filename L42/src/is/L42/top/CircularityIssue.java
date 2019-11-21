package is.L42.top;

import java.util.List;

import is.L42.common.Program;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L;
import is.L42.generated.Full;
import is.L42.generated.P;

public class CircularityIssue {
  P path;L l; Program p; E e;List<Full.L.NC>moreNCs;
  public CircularityIssue(P path,L l, Program p, E e,List<Full.L.NC>moreNCs) {
    this.path=path;this.l=l;this.p=p;this.e=e;this.moreNCs=moreNCs;
    }
  public void reportError() {
    throw new Error("better error for circularity issue. May be "+path+" used Cs contains some typos\n\n"+l);
    }

}
