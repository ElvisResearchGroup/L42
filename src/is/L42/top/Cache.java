package is.L42.top;

import java.util.List;
import java.util.function.Function;
import is.L42.common.EndError;
import is.L42.common.Program;

class Rs{
  final EndError _err;final List<G> _g;
  Rs(List<G> g){assert g!=null;_g=g;_err=null;}
  Rs(EndError err){assert err!=null;_err=err;_g=null;}
  boolean isErr(){return _err!=null;}
  }