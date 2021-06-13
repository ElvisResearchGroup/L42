package is.L42.nativeCode;

import static is.L42.tools.General.bug;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.translationToJava.J;

public class CacheEagerGenerator extends CacheLazyGenerator{
  @Override void readCache(J j,String name){throw bug();}
  @Override void typeCache(MWT mwt, J j){
    var p=mwt._e().poss();
    MH mh=mwt.mh();
    var url=mwt.nativeUrl();
    if(!mh.mdf().isImm()){
      throw new EndError.TypeError(p,ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),url,mh,
        "immutable methods",mh.mdf(),"immutable"));
      }
    super.typeCache(mwt,j);
    }
  }