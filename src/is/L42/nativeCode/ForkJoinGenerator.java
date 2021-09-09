package is.L42.nativeCode;

import static is.L42.tools.General.bug;

import java.util.ArrayList;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Mdf;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.translationToJava.J;
import is.L42.typeSystem.Coherence;
import is.L42.visitors.Accumulate;

public class ForkJoinGenerator implements Generator{
  @Override public void check(boolean allowAbs, MWT mwt, J j){//allowAbs correctly unused
    var pos=mwt._e().poss();
    MH mh=mwt.mh();
    var url=mwt.nativeUrl();
    Core.L l=j.p().topCore();
    //  throw new EndError.TypeError(p,ErrMsg.nativeKindInvalidSelector(url,mh));
    }
  @Override public void generate(MWT mwt, J j) {
    /*assert mwt.key().xs().isEmpty();
    String retT=j.typeNameStr(mwt.mh().t().p());
    String thisT;
    if(mwt.mh().mdf().isClass()){thisT=J.classNameStr(j.p());}
    else{thisT=j.typeNameStr(j.p());}
    String name=nameFromS(mwt.key());
    if(mwt.mh().mdf().isClass()){classCache(j,name);}
    if(mwt.mh().mdf().isImm()){immCache(j,name);}
    if(mwt.mh().mdf().isRead()){readCache(j,name);}
    fieldAndAuxMethod(mwt.mh().mdf().isClass(),j,name,retT,thisT,mwt._e());
    if(!mwt.mh().mdf().isIn(Mdf.Readable,Mdf.Immutable,Mdf.Class)){throw bug();}
    }
  void immCache(J j,String name){
    if(j.fields.xs.isEmpty()){readCache(j,name);return;}
    j.c("if(£xthis.norm==null){L42CacheMap.normalizeCachable(£xthis);}");j.nl();
    j.c("return £xthis.norm."+name+".join();");j.nl();j.deIndent();    
    j.c("}");j.nl();
    }
  void readCache(J j,String name){
    j.c("return £xthis."+name+".join();");j.nl();j.deIndent();    
    j.c("}");j.nl();
    }
  void classCache(J j,String name){
    j.c("return "+name+".join();");j.nl();j.deIndent();    
    j.c("}");j.nl();
    }
  void fieldAndAuxMethod(boolean isStatic,J j,String name,String retT,String thisT,Core.E e){
    String theThis="pathInstance";
    if(isStatic) {j.c("static ");}
    else {theThis=thisT+".this";}
    j.c("CachedRes<"+J.boxed(retT)+"> "+name+"=new CachedRes<"+J.boxed(retT)+">(){public "
      +J.boxed(retT)+" op(){return "+name+"("+theThis+");"+"}};");j.nl();
      //TODO: not for the imm only on the normalized one
    j.c("private static "+retT+" "+name+"("+thisT+" £xthis){");j.indent();j.nl();
    j.c("return ");
    j.visitE(e);
    j.c(";");    
    }*/
    }
  }