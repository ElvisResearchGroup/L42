package typeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import coreVisitors.FreeVariables;
import coreVisitors.GuessTypeCore;
import coreVisitors.InjectionOnSugar;
import coreVisitors.IsCompiled;
import coreVisitors.Visitor;
import facade.Configuration;
import facade.ErrorFormatter;
import facade.ErrorFormatter.Reporter;
import sugarVisitors.CollapsePositions;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.FreeType;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Type;
import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.SignalKind;
import ast.Ast.Stage;
import ast.ErrorMessage;
import ast.ErrorMessage.MethodNotPresent;
import ast.ErrorMessage.PathsNotSubtype;
import ast.ErrorMessage.TypesNotSubtype;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import ast.Util;
import ast.Util.CachedStage;
import ast.Ast.NormType;
import auxiliaryGrammar.*;

public class TypeSystem implements Visitor<Type>, Reporter, ast.Ast.HasPos{
  private TypeSystem(Program p, HashMap<String, NormType> varEnv, SealEnv sealEnv, ThrowEnv throwEnv, Type suggested,ExpCore e) {
    this.p = p;
    this.varEnv = varEnv;
    this.sealEnv = sealEnv;
    this.throwEnv = throwEnv;
    this.suggested = suggested;
    this.submittedExp=e;
  }
  public String toString(){
    return "\n["+ToFormattedText.of(submittedExp)+"]";
  }
  public Program p;
  public HashMap<String,NormType> varEnv;
  public SealEnv sealEnv;
  public ThrowEnv throwEnv;
  public Type suggested;
  public ExpCore submittedExp;

  //public static NormType of(ExpCore e)
  @Override
  public Type visit(WalkBy s) {
    throw Assertions.codeNotReachable();
  }

  @Override
  public Type visit(_void s) {
    return collectEnvs(null,()->{
      return new NormType(Mdf.Capsule,Path.Void(),Doc.empty());
    });
  }

  @Override
  public Type visit(X s) {
    return collectEnvs(null,()->{
      assert varEnv.containsKey(s.getInner()):s.getInner()+" "+varEnv;
      if(sealEnv.xInXs(s.getInner())){
        throw new ErrorMessage.VariableSealed(s);
      }
      if(sealEnv.xInXss(s.getInner())){
        NormType nt=varEnv.get(s.getInner());
        assert nt.getMdf()==Mdf.Mutable;
        if(this.suggested instanceof FreeType){
          return nt.withMdf(Mdf.Lent);}
        if(((NormType)suggested).getMdf()!=Mdf.Mutable){
          return nt.withMdf(Mdf.Lent);}
        throw new ErrorMessage.LentShuldBeMutable(s);
        }
      return varEnv.get(s.getInner());
      });
    }

  @Override
  public Type visit(ClassB s) {
    //Program pOld=this.p;
    //try{if (this.p.isExecutableStar()){this.p=p.removeExecutableStar();}
      return collectEnvs(null,()->{
        assert IsCompiled.of(s);
        Configuration.typeSystem.computeStage(p,s);
       /* if(p.executablePlus()){
          if(s.getStage().getStage()==Stage.Less){
            throw new ErrorMessage.LibraryRefersToIncompleteClasses(p.getInnerData(), s);
            }
          }*/
        if (p.isExecutableStar()){
          TypeSystemOK.checkCt(p, s);
          //if(s.getStage().getStage()==Stage.Less){
          //  throw new ErrorMessage.LibraryRefersToIncompleteClasses(p.getInnerData(), s);
          //  }
          }
        return NormType.immLibrary;
      });
    //}
   // finally{this.p=pOld;}
  }
  @Override
  public Type visit(Path s) {
    return collectEnvs(null,()->{
      if( s.isPrimitive()){return new NormType(Mdf.Class,s,Doc.empty());}
      ClassB ct=p.extractCb(s);
      if(ct.isInterface()){
        return NormType.classAny;
        }
      if(isPathPath(p,ct)){
        return new NormType(Mdf.Class,s,Doc.empty());
        }
      return NormType.classAny;
      });
    }
 private boolean isPathPath(Program p,ClassB cb) {
   //if(p.isExecutableStar()){return cb.getStage().getStage()==Stage.Star;}
   // if(p.executablePlus()){
   //   return cb.getStage().getStage()==Stage.Plus || cb.getStage().getStage()==Stage.Star;
   //}
   //TODO:24/09/2016, commented to allow new normalization.txt
   return true;
  }

  @Override
  public Type visit(Signal s) {
    return collectEnvs(null,()->{
      Mdf suggestedMdf=Mdf.Immutable;
      if(s.getKind()==SignalKind.Return){
        suggestedMdf=throwEnv.mdfOfRes();
      }
      NormType suggestedNested=Functions.toComplete(new NormType(suggestedMdf,Path.Any(),Doc.empty()));
      //TODO:was tollerant
      Type preciseTOpt=typecheckSure(false,p,varEnv,sealEnv,throwEnv,suggestedNested,s.getInner());
      if (preciseTOpt instanceof Ast.FreeType){return preciseTOpt;}
      NormType preciseT = Functions.forceNormType(this.p,s.getInner(), preciseTOpt);
      if(!Functions.isComplete(preciseT)){
        throw new ErrorMessage.InvalidTypeForThrowe(s, preciseT,null);
      }
      if(!Functions.isSubtype(preciseT.getMdf(),suggestedMdf)){
        throw new ErrorMessage.InvalidTypeForThrowe(s, preciseT,null);
      }
      if(s.getKind()==SignalKind.Error){return new Ast.FreeType();}
      if(s.getKind()==SignalKind.Return){
        for(NormType t:this.throwEnv.res()){
          if(Functions.isSubtype(p, preciseT,t)){
            return new Ast.FreeType();
          }}}
      if(s.getKind()==SignalKind.Exception){
        for(Path path:this.throwEnv.exceptions){
          if(Functions.isSubtype(p, preciseT.getPath(),path)){
            return new Ast.FreeType();
          }}}
      throw new ErrorMessage.InvalidTypeForThrowe(s, preciseT,null);
      });
    }




  private static Type suggestedAllowPromotions(Type suggested) {
    if(suggested instanceof FreeType){return suggested;}
    NormType nt=(NormType)suggested;
    Mdf m=nt.getMdf();
    if(m==Mdf.Capsule ||m==Mdf.Immutable/*NO?||m==Mdf.ImmutableFwd||m==Mdf.ImmutablePFwd*/){return nt.withMdf(Mdf.Readable);}
    return suggested;
  }
  static int typecheckSureInner_loopingcount=0;
  public static Type typecheckSureInner(boolean unlocking,Program p,HashMap<String, NormType> varEnv, SealEnv sealEnv, ThrowEnv throwEnv, Type suggested,Type suggestedAllowPromotions,ExpCore inner) {
    //se suggested imm o capsula, fake sugg as readonly, and
    //then consider for promotions.
    //promotion on block puo' promuovere tutto o ricorsivamente l'espressione+ons
    //per questo secondo caso, basta suggerire capsula+imm?

    Type result;
    try{
      result=_typecheckTollerant(p, varEnv, sealEnv, throwEnv, suggestedAllowPromotions, inner);}
    catch(TypesNotSubtype re){
      if(typecheckSureInner_loopingcount>1){throw re;}
      if(!(suggested instanceof Ast.NormType)){throw re;}
      if (suggested.getNT().getMdf()==Mdf.ImmutablePFwd){
        try{typecheckSureInner_loopingcount+=1;
        result=_typecheckTollerant(p, varEnv, sealEnv, throwEnv, suggested.getNT().withMdf(Mdf.MutablePFwd), inner);
        }finally{typecheckSureInner_loopingcount-=1;}
        }
      else throw re;
     }
    if(result instanceof Ast.FreeType){return result;}
    if(suggested instanceof Ast.FreeType){return result;}
    NormType nts=Functions.forceNormType( p,inner,suggested);
    NormType nt=Functions.forceNormType(p, inner,result);
    if(!Functions.isSubtype(p, nt.getPath(),nts.getPath())){
      CachedStage cs = new CachedStage();
      Path iPath=null;
      if(inner instanceof Path){iPath=(Path)inner;}
      if(iPath!=null &&!iPath.isPrimitive()){
    	  cs=p.extractCb(iPath).getStage();
    	  }
	  throw new ErrorMessage.PathsNotSubtype(nt,nts,inner,p.getInnerData(),cs,null);
      }
    if(Functions.isSubtype(p, nt,nts)){return result;}

    Mdf m=nts.getMdf();
    if(nt.getMdf()==Mdf.Mutable && (m==Mdf.Capsule ||m==Mdf.Immutable ||m==Mdf.ImmutableFwd||m==Mdf.ImmutablePFwd)){
      SealEnv newSealEnv=new SealEnv(sealEnv);
      newSealEnv.addLayer(varEnv);
      try{return typecheckPromotion(unlocking,p, varEnv, throwEnv, inner, nts.withMdf(Mdf.Mutable), nt,newSealEnv,sealEnv).withMdf(m);}
      catch(ErrorMessage.TypeError e){
        if(inner instanceof Block){
          try{return typecheckSureInner(unlocking,p,varEnv,sealEnv,throwEnv,suggested,suggested,inner);}
          catch(ErrorMessage.TypeError ignore){}
          }
        throw new ErrorMessage.TypesNotSubtype(nt,nts,inner,e,e.getPos());
        }
      }
    if(nt.getMdf()==Mdf.Readable && (m==Mdf.Immutable || m==Mdf.ImmutableFwd|| m==Mdf.ImmutablePFwd)){
      SealEnv newSealEnv=new SealEnv(sealEnv);
      newSealEnv.addStrongLock(varEnv);
      newSealEnv.addLayer(varEnv);
      try{return typecheckPromotion(unlocking,p, varEnv, throwEnv, inner, nts.withMdf(Mdf.Readable), nt,newSealEnv,sealEnv).withMdf(m);}
      catch(ErrorMessage.TypeError e){
        if(inner instanceof Block){
          try{return typecheckSureInner(unlocking,p,varEnv,sealEnv,throwEnv,suggested,suggested,inner);}
        catch(ErrorMessage.TypeError ignore){}}
        throw new ErrorMessage.TypesNotSubtype(nt,nts,inner,e,e.getPos());
        }
      }
    throw new ErrorMessage.TypesNotSubtype(nt,nts,inner, null, null);
    }

  public static Type typecheckSure(boolean unlocking,Program p,HashMap<String, NormType> varEnv, SealEnv sealEnv, ThrowEnv throwEnv, Type suggested,ExpCore inner) {
    sealEnv=SealEnv.addLentSingletons(varEnv,sealEnv);
    varEnv=removeLents(varEnv);
    try{
      return typecheckSureInner(unlocking,p, varEnv, sealEnv, throwEnv, suggested, suggestedAllowPromotions(suggested), inner);
      }
    catch(ErrorMessage.VariableSealed v){
      if(unlocking){throw v;}
        SealEnv newSealEnv=new SealEnv(sealEnv);
        newSealEnv.xs.clear();
        return concludeVewPointAdaptation(p, varEnv,newSealEnv, throwEnv,suggested, inner, v);
        }
      catch(ErrorMessage.LentShuldBeMutable v){
        if(unlocking){throw v;}
        String x=v.getVar().getInner();
        if(!varEnv.containsKey(x)){
          throw new ErrorMessage.UnlockImpossible(v,null);
          //TODO: is need also below?
        }
        assert varEnv.get(x).getMdf()!=Mdf.Lent;
        if(!sealEnv.xInXss(x)){
          assert false:sealEnv.xss;
          //we are out of the scope of capsule promotion that made x lent.
          //No swap is possible, give a good error
          throw new ErrorMessage.UnlockImpossible(v,null);
        }
        assert sealEnv.xInXss(x): x+" "+sealEnv.xss;
        SealEnv newSealEnv=new SealEnv(sealEnv);
        newSealEnv.swapLayer(x,varEnv);
        return concludeVewPointAdaptation(p, varEnv,newSealEnv, throwEnv,suggested, inner, v);
        }
    }
  public static Type concludeVewPointAdaptation(Program p,
      HashMap<String, NormType> varEnv, SealEnv newSealEnv, ThrowEnv throwEnv,
      Type suggested, ExpCore inner, ErrorMessage v) {
    if(suggested instanceof NormType && ((NormType)suggested).getMdf()==Mdf.Mutable){
      suggested=((NormType)suggested).withMdf(Mdf.Capsule);
      }
   // try{
      Type tProm=null;
      try{tProm=typecheckSure(true,p,varEnv, newSealEnv,throwEnv,suggested,inner);}
      catch (ErrorMessage e){
        throw v;
        //throw new ErrorMessage.UnlockImpossible(e);
        }
      NormType tPromNoMut=Functions.sharedToLent((NormType)tProm);
      if(suggested instanceof FreeType){return tPromNoMut;}
      if(Functions.isSubtype(p, tPromNoMut,(NormType)suggested)){return tPromNoMut;}
      throw v;
      //throw new ErrorMessage.UnlockImpossible(new ErrorMessage.TypesNotSubtype(tPromNoMut,suggested, inner, null));
     // }
//    catch(StackOverflowError over){throw v;}
    //catch (ErrorMessage.TypeError e){throw new ErrorMessage.UnlockImpossible(e);}
    //catch (ErrorMessage e){throw e;}
    }
  private Type collectEnvs(Position pos,Supplier<Type> t){
    try{return t.get();}
    catch(ErrorMessage.TypeError typeE){
      typeE.envs.add(this);
      throw ErrorMessage.PosImprove.improve(typeE, pos);
    }
  }
  public static Type _typecheckTollerant(Program p,HashMap<String, NormType> varEnv, SealEnv sealEnv, ThrowEnv throwEnv, Type suggested,ExpCore inner) {
    //ok i have to duplicate this in typechecksure
    sealEnv=SealEnv.addLentSingletons(varEnv,sealEnv);
    varEnv=removeLents(varEnv);
    TypeSystem newTs=new TypeSystem(p, varEnv, sealEnv, throwEnv, suggested,inner);
    return inner.accept(newTs);
  }

  private static NormType typecheckPromotion(
      boolean unlocking,Program p,HashMap<String, NormType> varEnv,
      ThrowEnv throwEnv,ExpCore inner, NormType nts, NormType nt,
      SealEnv newSealEnv, SealEnv oldSealEnv) {
    try{
     Type tProm= typecheckSure(unlocking,p,varEnv, newSealEnv,throwEnv,nts,inner);
     return (NormType)tProm;
    }
    catch (ErrorMessage.LentShuldBeMutable v){
      String x=v.getVar().getInner();
      assert newSealEnv.xInXss(x):x;
      if(oldSealEnv.xInXss(x)){v.fillInStackTrace();throw v;}
      throw new ErrorMessage.PromotionImpossible(nt,nts,v,inner,null);
    }
  }

  private static HashMap<String, NormType> removeLents(
      HashMap<String, NormType> varEnv2) {
    HashMap<String, NormType> result=new HashMap<String, NormType>();
    for(String s:varEnv2.keySet()){
      NormType nt=varEnv2.get(s);
      if(nt.getMdf()!=Mdf.Lent){result.put(s,nt);}
      else {result.put(s, nt.withMdf(Mdf.Mutable));}
      }
    return result;
  }


  static HashMap<String,NormType> splitVarEnv(Set<String> fv, HashMap<String,NormType>varEnv){
    HashMap<String,NormType> result=new HashMap<String,NormType>();
    for(String s:fv){
      if(!varEnv.containsKey(s)){continue;}
      //is it ok for the former to happen for newly introduced vars
      result.put(s,varEnv.get(s));
      }
    return result;
  }
  public static HashMap<String,NormType> splitVarEnv(ExpCore e, HashMap<String,NormType>varEnv){
    return splitVarEnv(FreeVariables.of(e),varEnv);
  }

  public static void checkDisjointCapsule(List<ExpCore> es,List<HashMap<String, NormType>> result) {
    for(HashMap<String,NormType> a:result){
      for(HashMap<String,NormType> b:result){
        if(a==b){continue;}
        for(String s:a.keySet()){
          assert a!=null;
          assert a.get(s)!=null: a+" "+s;
          if(a.get(s).getMdf()!=Mdf.Capsule){continue;}
          if(!b.containsKey(s)){continue;}
          // just for debug assert false: a+" -- "+b;
          throw new ErrorMessage.CapsuleUsedMoreThenOne(es, s,null);
          }
        }
      }
  }

  public static boolean noFreeVar(List<ExpCore> es, List<HashMap<String, NormType>> varEnvs,HashMap<String, NormType> original) {
    {int i=-1;for(ExpCore ei:es){i+=1;
     if(varEnvs.get(i).keySet().containsAll(FreeVariables.of(ei))){continue;}
     String forEi=ToFormattedText.of(ei);
     Set<String> freeVarEi = FreeVariables.of(ei);
     throw new AssertionError(forEi+"\n freeVars: "+freeVarEi+" "+varEnvs.get(i)+"\n original:"+original);
     }}
    return true;
    }
  private static boolean noFreeVar(ExpCore e,HashMap<String, NormType> original){
    if(original.keySet().containsAll(FreeVariables.of(e))){return true;}
    throw new AssertionError(ToFormattedText.of(e)+" "+FreeVariables.of(e)+" "+original);
  }

  @Override
  public Type visit(MCall s) {
    return collectEnvs(s.getP(),()->{
      assert noFreeVar(s,varEnv);
      List<HashMap<String, NormType>> varEnvs = TypeCheckMethod.splitAllVarEnvForMethod(s.getInner(), s.getEs(),varEnv);
      //Type recTOpt = _typecheckTollerant(p,varEnvs.get(0),sealEnv,throwEnv,new Ast.FreeType(),s.getReceiver());
      Path recOpt=null;
      if(!p.executablePlus() && !p.isExecutableStar()){//Note: guesstype can return null as a form of free type
        try{recOpt=GuessTypeCore.of(p, new HashMap<String,Ast.Type>(varEnv), s.getInner());}
        catch(ErrorMessage.NormImpossible ni){return methodUnknownT(varEnvs,s);}
        if(recOpt==null){return methodUnknownT(varEnvs,s);}
        if(recOpt.isPrimitive()){
          throw new ErrorMessage.MethodNotPresent(recOpt,s.getS(),s,p.getInnerData(),s.getP());
          }
        if(p.isNotClassB(recOpt)){return methodUnknownT(varEnvs,s);}
        }
      if(recOpt==null){recOpt=GuessTypeCore.of(p, new HashMap<String,Ast.Type>(varEnv), s.getInner());}
      if(recOpt==null){return methodUnknownT(varEnvs,s);}
      if(recOpt.isPrimitive()){//TODO: method not present thrown only for primitives?
        throw new ErrorMessage.MethodNotPresent(recOpt,s.getS(),s,p.getInnerData(),s.getP());}
      MethodWithType mwt;
      try{mwt= p.method(recOpt,s.getS(),s,true);}
      catch(ErrorMessage err){
       throw ErrorMessage.PosImprove.improve(err,s.getP());
        }
      NormType recExpected=Functions.toComplete(new NormType(mwt.getMt().getMdf(),recOpt,Doc.empty()));
      return TypeCheckMethod.methCallT(this,varEnvs,s,recExpected,mwt);
    });
  }

  private Type methodUnknownT(List<HashMap<String, NormType>> varEnvs, MCall s) {
    int i=0;
    for(ExpCore e:s.getEs()){
      i+=1;//correct to start from 1
      _typecheckTollerant(p,varEnvs.get(i),sealEnv,throwEnv,new Ast.FreeType(),e);
    }
    return new Ast.FreeType();
  }

  @Override
  public Type visit(Block s) {
    return collectEnvs(s.getP(),()->{
      Type t= TypecheckBlock.typecheckBlock(this,s);
      assert t!=null;
      return t;
    });
  }
  @Override
  public Type visit(Using s) {
    return collectEnvs(null,()->{
      if (s.getPath().isPrimitive()){
        throw new ErrorMessage.InvalidURL("No plug-in url present for primitive path "+s.getPath(),null);
      }
      List<NormType> lt = platformSpecific.fakeInternet.OnLineCode.pluginType(p, s);
      assert s.getEs().size()==lt.size()-1;
      {int i=-1;for(ExpCore ei:s.getEs()){i+=1;
        Type ti=lt.get(i+1);
        typecheckSure(false, p, varEnv, sealEnv, throwEnv, ti, ei);
        }}
      typecheckSure(false, p, varEnv, sealEnv, throwEnv, lt.get(0), s.getInner());
      return lt.get(0);
      });
    }

  @Override
  public Type visit(Loop s) {
    return collectEnvs(null,()->{
      NormType v=NormType.immVoid;
      checkSuggested(p,v,suggested,s);
      Type preciseTOpt=typecheckSure(false,p,varEnv,sealEnv,throwEnv,v,s.getInner());
      assert Functions.isSubtype(p, (NormType)preciseTOpt,v);
      return v;
    });
  }
  private static void checkSuggested(Program p,NormType actual,Type expected,ExpCore inner){
    if(expected instanceof FreeType){return;}
    if(Functions.isSubtype(p, actual,(NormType)expected)){return;};
    throw new ErrorMessage.TypesNotSubtype(actual,expected,inner,null,null);
  }
  @Override
  public String toReport(ArrayList<Ast.Position>ps) {
    return ErrorFormatter.errorFormat(submittedExp,ps);
  }
  @Override
  public Position getP() {
    return CollapsePositions.of(this.submittedExp);
  }
}



