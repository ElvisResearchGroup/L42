package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Core.L.MWT;
import is.L42.translationToJava.J;

public class ClearCacheGenerator implements Generator{
  @Override public void of(boolean type, MWT mwt, J j) {
    if(type){return;}
    if(j.fields.xs.isEmpty()){
      j.c("return "); 
      j.visitE(mwt._e());
      j.c(";");
      return; 
      }
    j.c("var Res=");
    j.visitE(mwt._e());
    j.c(";");    
    j.c("£xthis.clearCache();");
    j.c("return Res;");
    }
  }
