package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Core.L.MWT;
import is.L42.translationToJava.J;

public class LazyCacheGenerator implements Generator{
  void typeCache(MWT mwt, J j){
    /*assert sig.parMdfs.size()==sig.parTs.size();
      if(sig.parTs.size()!=mwt.mh().pars().size()){
        throw new EndError.TypeError(mwt._e().poss(),
          Err.nativeParameterCountInvalid(mwt.nativeUrl(),mwt.key(),sig.parMdfs.size()));
        }
      checkMdf(mwt,sig,mwt.mh().mdf(),sig.methMdf);
      var t=mwt.mh().t();
      checkSingle(p,mwt,sig,t.p(),t.mdf(),sig.retT,sig.retMdf);
      for(int i:range(mwt.mh().pars().size())){
        var pi=mwt.mh().pars().get(i).p();
        var mdfi=mwt.mh().pars().get(i).mdf();
        var tti=sig.parTs.get(i);
        var tmdfi=sig.parMdfs.get(i);
        checkSingle(p,mwt,sig,pi,mdfi,tti,tmdfi);
        }
      //TODO: check exceptions
      return true;*/
    }
  @Override public void of(boolean type, MWT mwt, J j) {
    if(type){typeCache(mwt,j); return;
      }
    //if(!mwt.mh().mdf().isImm()){throw todo();}
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
    j.c("if(£xthis.norm==null){£xthis.norm=£xthis.myCache.normalize(£xthis);}");j.nl();
    j.c("if(!£xthis.norm.is"+name+"){£xthis.norm."+name+"="+name+"(£xthis.norm); £xthis.norm.is"+name+"=true;}");j.nl();
    j.c("return £xthis.norm."+name+";");j.nl();j.deIndent();    
    j.c("}");j.nl();
    j.c("boolean is"+name+";");j.nl();
    }
  void readCache(J j,String name){
    j.c("if(!£xthis.is"+name+"){£xthis."+name+"="+name+"(£xthis); £xthis.is"+name+"=true;}");j.nl();
    j.c("return £xthis."+name+";");j.nl();j.deIndent();    
    j.c("}");j.nl();
    j.c("boolean is"+name+";");j.nl();
    }
  void classCache(J j,String name){
    j.c("if(!is"+name+"){"+name+"="+name+"(£xthis); is"+name+"=true;}");j.nl();
    j.c("return "+name+";");j.nl();j.deIndent();    
    j.c("}");j.nl();
    j.c("static boolean is"+name+";");j.nl();
    j.c("static ");//evil but works, since fieldAndAuxMethod is called directly after
    }
  void fieldAndAuxMethod(J j,String name,String retT,String thisT,Core.E e){
    j.c(retT+" "+name+";");j.nl();
    j.c("private static "+retT+" "+name+"("+thisT+" £xthis){");j.indent();j.nl();
    j.c("return ");
    j.visitE(e);
    j.c(";");    
    }
  }
