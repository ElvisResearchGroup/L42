package is.L42.connected.withSafeOperators.refactor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ast.ExpCore;
import ast.Ast;
import ast.Ast.C;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.MCall;
import ast.ExpCore.X;
import ast.PathAux;
import ast.Util.CsMx;
import ast.Util.CsMxMx;
import auxiliaryGrammar.Functions;
import facade.L42;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ParseFail;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import newTypeSystem.GuessTypeCore;
import newTypeSystem.TsLibrary;
import newTypeSystem.TypeManipulation;
import programReduction.Methods;
import programReduction.Program;
import tools.LambdaExceptionUtil;

public class InvariantClose {

public static ClassB closeJ(PData p,List<Ast.C>path,ClassB top,String mutK,String immK) throws PathUnfit, ClassUnfit, ParseFail{
  return close(p.p,path,top,mutK,immK);
  }
public static ClassB close(Program p,List<Ast.C>path,ClassB top,String mutK,String immK) throws PathUnfit, ClassUnfit, ParseFail{
  if(!MembersUtils.isPathDefined(top, path)){throw new RefactorErrors.PathUnfit(path);}
  if(MembersUtils.isPrivate(path)){throw new RefactorErrors.PathUnfit(path);}
  Program pPath=p.evilPush(top).navigate(path);
  ClassB lPath=pPath.top();
  MethodWithType inv=(MethodWithType)lPath._getMember(MethodSelector.of("#invariant", Collections.emptyList()));
  if(inv==null){throw new ClassUnfit().msg("selector #invariant() missing into "+PathAux.as42Path(path));}
  MethodType invMt=new MethodType(false,Mdf.Readable,Collections.emptyList(),Type.immVoid,Collections.emptyList()).withRefine(inv.getMt().isRefine());
  if(!inv.getMt().equals(invMt)){throw new ClassUnfit().msg("selector #invariant() into "+PathAux.as42Path(path)+" has invalid type "+inv.getMt());}
  Ks ks=new Ks(lPath,mutK,immK);
  List<CsMxMx> sel=new ArrayList<>();
  Set<MethodSelector>state=new HashSet<>();
  collectStateMethodsAndExposers(path, pPath, lPath, ks.candidateK,sel, state);
  ClassB newTop=(ClassB)top.accept(new WrapAux(p,sel,top));
  lPath=delegateState(ks,state,newTop,path);
  //return newTop with newLPath
  if(path.isEmpty()){return lPath;}
  final ClassB lP=lPath;
  return newTop.onClassNavigateToPathAndDo(path, l->lP);
  }

private static void collectStateMethodsAndExposers(
    List<Ast.C> path, Program pPath, ClassB lPath,
    MethodWithType k,
    List<CsMxMx> sel, Set<MethodSelector> state
    ) throws ClassUnfit {
  for(MethodWithType mwti:lPath.mwts()){
    if(!TsLibrary.coherentF(pPath,k,mwti)){continue;}
    assert !mwti.get_inner().isPresent();
    state.add(mwti.getMs());
    // with non read result, assert is lent
    Mdf mdf=mwti.getMt().getReturnType().getMdf();
    if(mdf==Mdf.Readable || mdf==Mdf.Immutable){continue;}
    if(mdf!=Mdf.Lent){throw new RefactorErrors.ClassUnfit().msg("Exposer not lent: '"+mwti.getMs()+"' in "+mwti.getP());}    
    sel.add(new CsMxMx(path,false,mwti.getMs(),mwti.getMs().withUniqueNum(k.getMs().getUniqueNum())));
    }
}

private static class Ks{
  MethodWithType candidateK;
  MethodWithType fwdK;
  MethodWithType mutK;
  MethodWithType immK;
  Ks(ClassB l,String mutKName,String immKName) throws ParseFail, ClassUnfit{
    List<String> fields = MakeK.collectFieldNames(l);
    candidateK = MakeK.candidateK("k", l, fields, true);
    MethodSelector candidateMs=candidateK.getMs();
    MethodType candidateMt=candidateK.getMt();
    mutK=candidateK.withMs(candidateMs.withName(mutKName));
    immK=candidateK.withMs(candidateMs.withName(immKName));
    fwdK=candidateK.withMs(candidateMs.withUniqueNum(L42.freshPrivate()));
    List<Ast.Type>fwdT=tools.Map.of(TypeManipulation::capsuleToFwdMut,fwdK.getMt().getTs());
    List<Ast.Type>mutT=tools.Map.of(TypeManipulation::noFwd,fwdK.getMt().getTs());
    List<Ast.Type>immT=tools.Map.of(t->t.withMdf(Mdf.Immutable),fwdK.getMt().getTs());
    fwdK=fwdK.withMt(candidateMt.withTs(fwdT));
    mutK=mutK.withMt(candidateMt.withTs(mutT));
    immK=immK.withMt(candidateMt.withTs(immT));
    }
  }
private static ClassB delegateState(Ks ks,
    Set<MethodSelector> state,
    ClassB newTop, List<C> path
    ) throws ParseFail, ClassUnfit {
  ClassB lPath=newTop.getClassB(path);
  List<ClassB.Member> newMwts=new ArrayList<>();
  for(MethodWithType mwt:lPath.mwts()){
    if(!state.contains(mwt.getMs())){newMwts.add(mwt);continue;}
    Mdf mdf=mwt.getMt().getMdf();
    Mdf resMdf=mwt.getMt().getReturnType().getMdf();
    boolean isGet=mwt.getMs().getNames().isEmpty();
    if(mdf==Mdf.Class){
      newMwts.add(mwt);
      continue;
      }
    MethodSelector uniqueMs=mwt.getMs().withUniqueNum(ks.fwdK.getMs().getUniqueNum());
    if(!isGet){
      //replace decl set() ->decl freshXi(that)+ delegator set(that) (this.freshXi(that) invariant())
      delegator(true,newMwts,mwt,uniqueMs);  
      continue;
    }
    if(resMdf==Mdf.Readable || resMdf==Mdf.Immutable ){
      //  replace decl get() ->decl freshXi()+ delegator get() this.freshXi()  
      delegator(false,newMwts,mwt,uniqueMs);
      continue;
      }
    //last case is exposer, and Exposer: should be already fixed:
      //rename .exposer() ->freshExposer()
      //  rename decl exposer() ->decl freshExposer()  
    }
  //delegate mutK,immK to candidtateK
  delegator(true,newMwts,ks.mutK,ks.fwdK);
  delegator(true,newMwts,ks.immK,ks.fwdK);
  newMwts.addAll(lPath.ns());
  return lPath.withMs(newMwts);
  }
static void delegator(boolean callInvariant,List<ClassB.Member> newMwts, MethodWithType original,MethodSelector delegate) throws ClassUnfit{
  delegator(callInvariant,newMwts,original,original.withMs(delegate));
  }
static void delegator(boolean callInvariant,List<ClassB.Member> newMwts, MethodWithType original,MethodWithType delegate) throws ClassUnfit{
  assert !original.get_inner().isPresent();
  assert original.getMs().nameSize()==delegate.getMs().nameSize();
  Position p=original.getP();
  ExpCore.MCall delegateMCall=new ExpCore.MCall(
          new ExpCore.EPath(p, Path.outer(0)), delegate.getMs(),Doc.empty(),
          tools.Map.of(s->new ExpCore.X(p,s), original.getMs().getNames()),p);
  if(!callInvariant){original=original.withInner(delegateMCall);}
  else{
    ExpCore.Block b=WrapAux.e.withDeci(0,WrapAux.e.getDecs().get(0).withInner(delegateMCall));
    original=original.withInner(b);
    }
  addCheck(original,newMwts);
  addCheck(delegate,newMwts);
  }
static void addCheck(MethodWithType mwt,List<ClassB.Member> newMwts) throws ClassUnfit{
  // delegator when adding to newMs check for old and override if identical mt
  Optional<Member> pre=Functions.getIfInDom(newMwts,mwt);
  if(!pre.isPresent()){newMwts.add(mwt);return;}
  MethodWithType mwtPre=(MethodWithType)pre.get();
  if(mwtPre.getMt().equals(mwt.getMt())){
    Functions.replaceIfInDom(newMwts,mwt);return;}
  throw new RefactorErrors.ClassUnfit().msg("Incompatible factory type: "+mwt.getMs()+": "+mwt.getMt()+" and "+mwtPre.getMt());
  }
}
class WrapAux extends RenameMethodsAux{
  int count=0;
  static X thisX=new X(Position.noInfo,"this");
  static ExpCore.Block e=(ExpCore.Block)Functions.parseAndDesugar("WrapExposer", 
    "{method m() (r=void this.#invariant() r)}"
    ).getMs().get(0).getInner();
  public WrapAux(Program p, List<CsMxMx> renames, ClassB top) {
    super(p, renames, top);
    }
  @Override
  public ExpCore visit(MCall s) {
    Type guessed=GuessTypeCore._of(p, g, s.getInner(),false);
    if(guessed==null){return super.visit(s);}
    MethodSelector ms2=mSToReplaceOrNull(s.getS(),guessed.getPath());
    if(ms2==null){return super.visit(s);}
    if(!s.getInner().equals(thisX)){
      LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
        "Exposer '"+s.getS()+"' called on non 'this' receiver in "+s.getP())
      );}
    count+=1;
    assert s.getS().equals(ms2);
    return super.visit(s);
    }
  @Override public 
  ClassB.MethodWithType visit(ClassB.MethodWithType mwt){
    count=0;
    ClassB.MethodWithType res=super.visit(mwt);
    if(count==0){return res;}
    if(mwt.getMt().getMdf()==Mdf.Capsule){
      //ok to leave untouched the (stupid) ones with mdf==capsule
      //inded, if this used to call exposer, can not be used to open capsule
      return res;
      }
    assert mwt.getMt().getMdf()==Mdf.Lent || mwt.getMt().getMdf()==Mdf.Mutable;
    //else, replace with the pattern
    if(mwt.getMt().getReturnType().getMdf()==Mdf.Lent){
      LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
        "Exposer called on lent returning method '"+mwt.getMs()+"' in "+mwt.getP())
        );}
    ExpCore myE=e.withDeci(0,e.getDecs().get(0)
      .withInner(mwt.getInner()));
    return res.withInner(myE);
    }
}