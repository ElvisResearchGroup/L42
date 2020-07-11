package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import java.util.ArrayList;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.translationToJava.J;
import is.L42.typeSystem.Coherence;
import is.L42.visitors.Accumulate;

public class CacheEagerGenerator extends CacheLazyGenerator{
  @Override void readCache(J j,String name){throw bug();}
  @Override void typeCache(MWT mwt, J j){
    var p=mwt._e().poss();
    MH mh=mwt.mh();
    var url=mwt.nativeUrl();
    if(!mh.mdf().isImm()){
      throw new EndError.TypeError(p,Err.nativeParameterInvalidKind(url,mh,
        "immutable methods",mh.mdf(),"immutable"));
      }
    super.typeCache(mwt,j);
    }
  }