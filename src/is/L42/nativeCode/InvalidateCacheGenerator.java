package is.L42.nativeCode;

import java.util.ArrayList;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.generated.Core;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Mdf;
import is.L42.generated.X;
import is.L42.translationToJava.J;
import is.L42.typeSystem.Coherence;
import is.L42.visitors.Accumulate;

public class InvalidateCacheGenerator implements Generator{
  static public void isAnnotatedAsCapsuleMutator(MWT mwt){
    if(!mwt.nativeUrl().equals("trusted:invalidateCache")){
      throw new EndError.TypeError(mwt._e().poss(),ErrMsg.nativeKindInvalidNativeKind(mwt.mh(),"trusted:invalidateCache",mwt.nativeUrl()));
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
  static void validCapsuleMutator(MWT mwt,J j){
    if(!mwt.nativeUrl().equals("trusted:invalidateCache")){
      throw new EndError.TypeError(mwt._e().poss(),ErrMsg.nativeKindInvalidNativeKind(mwt.mh(),"trusted:invalidateCache",mwt.nativeUrl()));
      }
    if(!mwt.mh().exceptions().isEmpty()){
      throw new EndError.TypeError(mwt._e().poss(),ErrMsg.nativeBodyInvalidExceptions(mwt));
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
  
  @Override public void check(boolean allowAbs, MWT mwt, J j){ validCapsuleMutator(mwt, j); }

  @Override public void generate(MWT mwt, J j) {
    if(!j.needClearCache()){
      j.c("return ");
      j.visitE(mwt._e());
      j.c(";");j.nl();
      return;
      }
    j.c("var Res=");
    j.visitE(mwt._e());
    j.c(";");    
    j.c("£xthis.clearCache();");
    j.c("return Res;");
    }
  }