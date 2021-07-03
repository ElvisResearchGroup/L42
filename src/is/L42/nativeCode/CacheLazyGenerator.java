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

public class CacheLazyGenerator implements Generator{
  @Override public void check(boolean allowAbs, MWT mwt, J j){//allowAbs correctly unused
    var p=mwt._e().poss();
    MH mh=mwt.mh();
    var url=mwt.nativeUrl();
    if(mh.key().m().startsWith("#$")){
      throw new EndError.TypeError(p,ErrMsg.nativeKindInvalidSelector(url,mh));
      }
    if(!mh.mdf().isIn(Mdf.Immutable,Mdf.Readable,Mdf.Class)){
      throw new EndError.TypeError(p,ErrMsg.nativeParameterInvalidKind(!url.isEmpty(),url,mh,
        "imm, class or read methods",mh.mdf(),"imm, class or read"));
      }
    if(!mh.pars().isEmpty()){
      throw new EndError.TypeError(p,ErrMsg.nativeParameterCountInvalid(url,mh,0));
      }
    var t=mwt.mh().t();
    if(!t.mdf().isIn(Mdf.Immutable,Mdf.Class,Mdf.Readable)){
      throw new EndError.TypeError(p,ErrMsg.nativeParameterInvalidKind(!url.isEmpty(),url,mh,
        "imm, class or read return type",t.mdf(),"imm, class or read"));
      }
    if(!mh.mdf().isRead()){ return; }
    var l=j.p().topCore();
    checkClose(l);
    if(!noMutFields(j, l)){checkOnlyThisCallFields(mwt,j);}
    }
  public static boolean noMutFields(J j, L l) {
    return l.mwts().stream()
        .filter(m->m.key().hasUniqueNum()
          && m._e()==null 
          && !m.mh().mdf().isClass()
          && m.key().xs().isEmpty())
        .allMatch(m->immOrCapsule(true,null,j,m.key()));
  }
  void checkOnlyThisCallFields(MWT mwt,J j){
    mwt._e().visitable().accept(new Accumulate.SkipL<Void>(){
      @Override public void visitMCall(Core.MCall m){
        if(isThisCall(m)){immOrCapsule(false,mwt,j,m.s());return;}
        super.visitMCall(m);//only pass if 0 args, thus no need of super
        }  
      @Override public void visitX(X x){//this in meth calls may be filtered above
        if(!x.inner().equals("this")){return;}
        throw new EndError.TypeError(mwt._e().poss(),
          ErrMsg.nativeBodyInvalidThis(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.mh()));
        }});
    clearCacheGood(j);
    }
  public static boolean immOrCapsule(boolean fwdImmOk,MWT _forErr,J j,S s){
    if(!s.xs().isEmpty()){
      if(_forErr==null){return false;}
      throw new EndError.TypeError(_forErr._e().poss(),
        ErrMsg.nativeBodyInvalidThis(!_forErr.nativeUrl().isEmpty(),_forErr.nativeUrl(),_forErr.mh()));}
    return immOrCapsule(fwdImmOk,_forErr,j,Coherence.fieldName(s));
    }
  static boolean immOrCapsule(boolean fwdImmOk,MWT _forErr,J j,X x){
    var f=j.ch.fieldTs(x, Mdf.Mutable);
    if(f.stream().allMatch(t->t.mdf().isClass())){return true;}
    if(!fwdImmOk && f.stream().allMatch(t->t.mdf().isImm())){return true;}
    if(fwdImmOk && f.stream().allMatch(t->t.mdf().isIn(Mdf.Immutable,Mdf.ImmutableFwd))){return true;}
    if(f.stream().allMatch(t->t.mdf().isCapsule())){return true;}
    if(_forErr==null){return false;}
    throw new EndError.TypeError(_forErr._e().poss(),
      ErrMsg.nativeBodyInvalidThis(!_forErr.nativeUrl().isEmpty(),_forErr.nativeUrl(),_forErr.mh()));
    }
  public void clearCacheGood(J j){
    if(j.cachedClearCacheGood){return;}
    Core.L l=j.p().topCore();
    checkClose(l);
    for(var mwt:l.mwts()){
      if(isCapsuleMutator(mwt,j)){validCapsuleMutator(mwt,j);}
        }
    j.cachedClearCacheGood=true;
    }
  private void checkClose(Core.L l) {
    if(!l.info().close()){
      throw new EndError.TypeError(l.poss(),ErrMsg.mustHaveCloseState());
      }
  }
  static public boolean isCapsuleMutator(MWT mwt,J j){
    var ch=j.ch;
    if(mwt._e()==null){return false;}
    if(!mwt.mh().mdf().isIn(Mdf.Mutable,Mdf.Lent)){return false;}
    var thises=new Accumulate.SkipL<ArrayList<Core.MCall>>(){
      @Override public ArrayList<Core.MCall> empty(){return new ArrayList<>();}
      @Override public void visitMCall(Core.MCall m){
        if(isThisCall(m)){acc().add(m);}
        super.visitMCall(m);
        }}.of(mwt._e().visitable());
    for(var mci:thises){
      if(!mci.s().hasUniqueNum() || mci.s().uniqueNum()!=0){continue;}
      X x=Coherence.fieldName(mci.s());
      assert j.fields!=null:
        "";
      if(!j.fields.xs.contains(x)){continue;}
      boolean caps=ch.fieldTs(x,Mdf.Mutable).stream().allMatch(t->t.mdf().isCapsule());
      if(!caps){continue;}
      boolean mut=ch.mhs.stream().anyMatch(m->
        m.s().equals(mci.s()) && m.t().mdf().isIn(Mdf.Mutable,Mdf.Lent));
      if(!mut){continue;}
      return true;
      }
    return false;
    }
  void  validCapsuleMutator(MWT mwt,J j){
    if(!mwt.mh().exceptions().isEmpty()){
      throw new EndError.TypeError(mwt._e().poss(),ErrMsg.nativeBodyInvalidExceptions(!mwt.nativeUrl().isEmpty(),"capsuleMutator",mwt.mh()));
      } 
    if(mwt.mh().t().mdf().isIn(Mdf.Mutable,Mdf.Lent,Mdf.MutableFwd)){
      throw new EndError.TypeError(mwt._e().poss(),
          ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.mh(),
            "not mutable or lent",mwt.mh().t(),"imm, capsule, class or read"));
      }
    for(var t:mwt.mh().pars()){
      if(t.mdf().isIn(Mdf.Mutable,Mdf.Lent,Mdf.Readable)){
        throw new EndError.TypeError(mwt._e().poss(),
            ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.mh(),
              "not mutable, lent or read",t,"not mutable, lent or read"));
        }
      }
    var thises=new Accumulate.SkipL<int[]>(){
      @Override public int[] empty(){return new int[]{0};}
      @Override public void visitX(X x){
        if(!x.inner().equals("this")){return;}
        acc()[0]++;
        }}.of(mwt._e().visitable())[0];
      if(thises!=1){
        throw new EndError.TypeError(mwt._e().poss(),ErrMsg.nativeBodyInvalidThisCount(
          !mwt.nativeUrl().isEmpty(),"capsuleMutator",mwt.mh()));
        }      
    }
  static boolean isThisCall(Core.MCall m){
    if(!(m.xP() instanceof Core.EX)){return false;}
    var ex=(Core.EX)m.xP();
    return ex.x().inner().equals("this");
    }
  public static String nameFromS(S s) {
    assert s.xs().isEmpty();
    String name="£k"+s.m().replace("#", "£h");
    //other used letters: x _ f c n h E
    if(s.hasUniqueNum()){name+="£n"+s.uniqueNum();}
    return name;
    }
  @Override public void generate(MWT mwt, J j) {
    assert mwt.key().xs().isEmpty();
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
    }
  }