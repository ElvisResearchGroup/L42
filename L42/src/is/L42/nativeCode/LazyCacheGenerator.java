package is.L42.nativeCode;

import is.L42.common.Program;
import is.L42.generated.Core.L.MWT;
import is.L42.translationToJava.J;

public class LazyCacheGenerator implements TrustedOp.Generator{
  @Override public void of(boolean type, MWT mwt, J j) {
    if(type){return;}
    j.c("return ");
    j.visitE(mwt._e());
    j.c(";");
    }

}
