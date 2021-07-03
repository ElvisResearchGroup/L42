package is.L42.nativeCode;

import static is.L42.tools.General.bug;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.generated.Core;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.translationToJava.J;

public class CacheNowGenerator extends CacheLazyGenerator{
  @Override void immCache(J j,String name){throw bug();}
  @Override void readCache(J j,String name){
    j.c("return £xthis."+name+";");j.nl();j.deIndent();    
    j.c("}");j.nl();
    }
  @Override public void check(boolean allowAbs, MWT mwt, J j){//allowAbs correctly unused
    MH mh=mwt.mh();
    if(!mh.mdf().isRead()){
      throw new EndError.TypeError(mwt._e().poss(),
        ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.mh(),"readable methods",mh.mdf(),"readable methods"));
      }
    super.check(allowAbs,mwt, j);
    }
  @Override void fieldAndAuxMethod(boolean isStatic,J j,String name,String retT,String thisT,Core.E e){
    if(isStatic) {j.c("static ");}
    j.c(retT+" "+name+";");
    j.c("private static "+retT+" "+name+"("+thisT+" £xthis){");j.indent();j.nl();
    j.c("return ");
    j.visitE(e);
    j.c(";");
    }
  }