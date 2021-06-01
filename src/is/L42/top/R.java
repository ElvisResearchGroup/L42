package is.L42.top;

import java.io.Serializable;

import is.L42.common.EndError;

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