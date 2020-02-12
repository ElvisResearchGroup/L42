package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Mdf;
import is.L42.translationToJava.J;

public class EagerCacheGenerator extends LazyCacheGenerator{
  @Override void immCache(J j,String name){throw bug();}
  @Override void readCache(J j,String name){
    j.c("return £xthis."+name+";");j.nl();j.deIndent();    
    j.c("}");j.nl();
    }
  @Override void typeCache(MWT mwt, J j){
    MH mh=mwt.mh();
    if(!mh.mdf().isIn(Mdf.Immutable,Mdf.Readable)){
    //TODO: need to be incoherent if some constructor return not imm
    //NOPE, if the method is imm, then the normalization process will make "this" reachable globally, thus
    //we could observe a null eagerCache on this!!
      throw new EndError.TypeError(mwt._e().poss(),
          Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.mh(),"immutable or readable methods",mh.mdf(),"immutable or readable methods"));
      }
    super.typeCache(mwt, j);
    }
  }
