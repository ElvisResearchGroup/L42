package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import java.util.ArrayList;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.translationToJava.J;
import is.L42.typeSystem.Coherence;
import is.L42.visitors.Accumulate;

public class EagerCacheGenerator extends LazyCacheGenerator{
  @Override void immCache(J j,String name){throw bug();}
  @Override void readCache(J j,String name){
    j.c("return £xthis."+name+";");j.nl();j.deIndent();    
    j.c("}");j.nl();
    }
  @Override void typeCache(MWT mwt, J j){
    MH mh=mwt.mh();
    if(!mh.mdf().isRead()){
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.mh(),"readable methods",mh.mdf(),"readable methods"));
      }
    super.typeCache(mwt, j);
    mwt._e().visitable().accept(new Accumulate.SkipL<Void>(){
      @Override public Void empty(){return null;}
      @Override public void visitMCall(Core.MCall m){
        if(isThisCall(m)){immOrCapsule(mwt,j,m.s());return;}
        super.visitMCall(m);//only pass if 0 args, thus no need of super
        }  
      @Override public void visitX(X x){//this in meth calls may be filtered above
        if(!x.inner().equals("this")){return;}
        throw new EndError.TypeError(mwt._e().poss(),
          Err.nativeBodyInvalidThis(mwt.nativeUrl(),mwt.mh()));
        }});
    clearCacheGood(j);
    }
  void immOrCapsule(MWT forErr,J j,S s){
    if(!s.xs().isEmpty()){
      throw new EndError.TypeError(forErr._e().poss(),
        Err.nativeBodyInvalidThis(forErr.nativeUrl(),forErr.mh()));}
    X x=Coherence.fieldName(s);
    var f=j.ch.fieldTs(x, Mdf.Mutable);
    if(f.stream().allMatch(t->t.mdf().isImm())){return;}
    if(f.stream().allMatch(t->t.mdf().isCapsule())){return;}
    throw new EndError.TypeError(forErr._e().poss(),
      Err.nativeBodyInvalidThis(forErr.nativeUrl(),forErr.mh()));
    }
  public void clearCacheGood(J j){
    if(j.cachedClearCacheGood){return;}
    Core.L l=j.p().topCore();
    if(!l.info().close()){
      throw new EndError.TypeError(l.poss(),Err.mustHaveCloseState());
      }
    for(var mwt:l.mwts()){
      if(isCapsuleMutator(mwt,j)){validCapsuleMutator(mwt,j);}
        }
    j.cachedClearCacheGood=true;
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
      throw new EndError.TypeError(mwt._e().poss(),Err.nativeBodyInvalidExceptions("capsuleMutator",mwt.mh()));
      } 
    if(mwt.mh().t().mdf().isIn(Mdf.Mutable,Mdf.Lent,Mdf.MutableFwd)){
      throw new EndError.TypeError(mwt._e().poss(),
          Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.mh(),
            "not mutable or lent",mwt.mh().t(),"not mutable or lent"));
      }
    for(var t:mwt.mh().pars()){
      if(t.mdf().isIn(Mdf.Mutable,Mdf.Lent,Mdf.Readable)){
        throw new EndError.TypeError(mwt._e().poss(),
            Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.mh(),
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
        throw new EndError.TypeError(mwt._e().poss(),Err.nativeBodyInvalidThisCount("capsuleMutator",mwt.mh()));
        }      
    }
  static boolean isThisCall(Core.MCall m){
    if(!(m.xP() instanceof Core.EX)){return false;}
    var ex=(Core.EX)m.xP();
    return ex.x().inner().equals("this");
    }
  }