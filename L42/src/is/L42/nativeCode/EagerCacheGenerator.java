package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import is.L42.generated.Core.L.MWT;
import is.L42.translationToJava.J;

public class EagerCacheGenerator extends LazyCacheGenerator{
//TODO: must typecheck that the mdf is ONLY read
  //void immCache(J j,String name){throw bug();}
  void readCache(J j,String name){
    j.c("return £xthis."+name+";");j.nl();j.deIndent();    
    j.c("}");j.nl();
    }
  }
