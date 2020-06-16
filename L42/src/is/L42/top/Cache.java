package is.L42.top;

import static is.L42.tools.General.bug;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import is.L42.common.EndError;
import is.L42.common.Program;

class R implements Serializable{
  final EndError _err;final G _g;final Object _obj;
  R(G g,Object obj){
    assert g!=null;
    _g=g;
    _obj=obj;
    _err=null;
    }
  R(EndError err){assert err!=null;_err=err;_g=null;_obj=null;}
  boolean isErr(){return _err!=null;}
  }
class Rs{
  final EndError _err;final List<G> _g;
  Rs(List<G> g){assert g!=null;_g=g;_err=null;}
  Rs(EndError err){assert err!=null;_err=err;_g=null;}
  boolean isErr(){return _err!=null;}
  }
abstract class G implements Serializable{
  State state;
  public abstract Object layer();
  public abstract R _open(G cg,R cr);
  public abstract R _close(G cg,R cr);
  public abstract boolean needOpen();
  public R open(G cg,R cr){try{return _open(cg,cr);}catch(EndError err){return new R(err);}}
  public R close(G cg,R cr){try{return _close(cg,cr);}catch(EndError err){return new R(err);}}
  @Override public int hashCode(){throw bug();}
  @Override public boolean equals(Object o){throw bug();}
  }