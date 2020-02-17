package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.translationToJava.J;

public class LazyCacheGenerator implements Generator{
  void typeCache(MWT mwt, J j){
    var p=mwt._e().poss();
    MH mh=mwt.mh();
    var url=mwt.nativeUrl();
    if(mh.key().m().startsWith("#$")){
      throw new EndError.TypeError(p,Err.nativeKindInvalidSelector(url,mh));
      }
    if(!mh.mdf().isIn(Mdf.Immutable,Mdf.Readable,Mdf.Class)){
      throw new EndError.TypeError(p,Err.nativeParameterInvalidKind(url,mh,
        "immutable, readable or class methods",mh.mdf(),"immutable, readable or class"));
      }
    if(!mh.pars().isEmpty()){
      throw new EndError.TypeError(p,Err.nativeParameterCountInvalid(url,mh,0));
      }
    var t=mwt.mh().t();
    if(!t.mdf().isIn(Mdf.Immutable,Mdf.Class)){
      throw new EndError.TypeError(p,Err.nativeParameterInvalidKind(url,mh,
        "immutable or class return type",mh.mdf(),"immutable or class"));
      }
    }
  @Override public void of(boolean type, MWT mwt, J j) {
    if(type){typeCache(mwt,j); return;}
    assert mwt.key().xs().isEmpty();
    String retT=j.typeNameStr(mwt.mh().t().p());
    String thisT=j.typeNameStr(j.p());
    String name="£k"+mwt.key().m();//other used letters: x _ f c n h E
    if(mwt.key().hasUniqueNum()){name+="£k"+mwt.key().uniqueNum();}
    if(mwt.mh().mdf().isClass()){classCache(j,name);}
    if(mwt.mh().mdf().isImm()){immCache(j,name);}
    if(mwt.mh().mdf().isRead()){readCache(j,name);}
    fieldAndAuxMethod(j,name,retT,thisT,mwt._e());
    if(!mwt.mh().mdf().isIn(Mdf.Readable,Mdf.Immutable,Mdf.Class)){throw bug();}
    }
  void immCache(J j,String name){
    if(j.fields.xs.isEmpty()){readCache(j,name);return;}
    j.c("if(£xthis.norm==null){L42CacheMap.normalizeCachable(£xthis);}");j.nl();
    j.c("if(!£xthis.norm.is"+name+"){£xthis.norm."+name+"="+name+"(£xthis.norm); £xthis.norm.is"+name+"=true;}");j.nl();
    j.c("return £xthis.norm."+name+";");j.nl();j.deIndent();    
    j.c("}");j.nl();
    j.c("volatile boolean is"+name+";");j.nl();
    }
  void readCache(J j,String name){
    j.c("if(!£xthis.is"+name+"){£xthis."+name+"="+name+"(£xthis); £xthis.is"+name+"=true;}");j.nl();
    j.c("return £xthis."+name+";");j.nl();j.deIndent();    
    j.c("}");j.nl();
    j.c("volatile boolean is"+name+";");j.nl();
    }
  void classCache(J j,String name){
    j.c("if(!is"+name+"){"+name+"="+name+"(£xthis); is"+name+"=true;}");j.nl();
    j.c("return "+name+";");j.nl();j.deIndent();    
    j.c("}");j.nl();
    j.c("static volatile boolean is"+name+";");j.nl();
    j.c("static ");//evil but works, since fieldAndAuxMethod is called directly after
    }
  void fieldAndAuxMethod(J j,String name,String retT,String thisT,Core.E e){
    j.c("volatile "+retT+" "+name+";");j.nl();
    j.c("private static "+retT+" "+name+"("+thisT+" £xthis){");j.indent();j.nl();
    j.c("return ");
    j.visitE(e);
    j.c(";");    
    }
  }
