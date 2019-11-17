package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.todo;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.Core.L;

public class L42ClassAny implements L42Any{
  final public P unwrap;  
  public L42ClassAny(P p) {
    assert p!=null;
    this.unwrap=p;
    }
  }