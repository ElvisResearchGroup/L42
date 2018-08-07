package is.L42.connected.withSafeOperators.refactor;

/*

 { T1 f1 ..Tn fn }

 ksignature

 add K,

 make state delegates   g()->f()  if setter/constr add invariant check

 for all the method calling this.exposer()
   check method is ok = ...old... restrictions
   call invariant end

 check validate method follow restrictions= ...old...

 make state private

 TODO: understand behaviour of old, what to change to make the new restrictions
 Validate: can use private state (instead of directly fields), can use methods only using private state
   //hard: ( make a private copy of all public methods used inside validate and make validate use such private version )





 * */


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
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.MCall;
import ast.ExpCore.X;
import ast.PathAux;
import ast.Util.CsMx;
import ast.Util.CsMxMx;
import auxiliaryGrammar.Functions;
import coreVisitors.CloneVisitor;
import facade.L42;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ParseFail;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import newTypeSystem.GuessTypeCore;
import newTypeSystem.StaticDispatch;
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
  collectStateMethodsAndExposers(path, pPath, lPath, ks.candidateK,ks.fwdK.getMs().getUniqueNum(),sel, state);
  ClassB newTop=(ClassB)top.accept(new WrapAux(p,sel,top));
  lPath=delegateState(ks,state,newTop,path);
  //return newTop with newLPath
  if(path.isEmpty()){return lPath;}
  final ClassB lP=lPath;
  return newTop.onClassNavigateToPathAndDo(path, l->lP);
  }

private static void collectStateMethodsAndExposers(
    List<Ast.C> path, Program pPath, ClassB lPath,
    MethodWithType k,long uniqueNum,
    List<CsMxMx> sel, Set<MethodSelector> state
    ) throws ClassUnfit {
  for(MethodWithType mwti:lPath.mwts()){
    if(!nonMetaCoherentF(pPath, k, mwti)){continue;}
    assert mwti.get_inner()==null;
    state.add(mwti.getMs());
    // with non read result, assert is lent
    Mdf mdf=mwti.getMt().getReturnType().getMdf();
    if(mdf.isIn(Mdf.Readable,Mdf.Immutable,Mdf.Class)){continue;}
    if(mdf!=Mdf.Lent){throw new RefactorErrors.ClassUnfit().msg("Exposer not lent: '"+mwti.getMs()+"' in "+mwti.getP());}
    sel.add(new CsMxMx(path,false,mwti.getMs(),mwti.getMs().withUniqueNum(uniqueNum)));
    }
}
private static boolean nonMetaCoherentF(Program pPath, MethodWithType k, MethodWithType mwti) {
  try{return TsLibrary.coherentF(pPath,k,mwti);}
  catch(PathMetaOrNonExistant pmne){return false;}
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
    List<Ast.Type>immT=tools.Map.of(t->t.getMdf()==Mdf.Class?t:t.withMdf(Mdf.Immutable),fwdK.getMt().getTs());
    fwdK=fwdK.withMt(candidateMt.withTs(fwdT));
    mutK=mutK.withMt(candidateMt.withTs(mutT));
    immK=immK.withMt(candidateMt.withTs(immT).withReturnType(candidateMt.getReturnType().withMdf(Mdf.Immutable)));
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
    if(resMdf.isIn(Mdf.Readable,Mdf.Immutable,Mdf.Class) ){
      //  replace decl get() ->decl freshXi()+ delegator get() this.freshXi()
      delegator(false,newMwts,mwt,uniqueMs);
      continue;
      }
    //last case is exposer, and Exposer: should be already fixed:
    //newMwts.add(mwt.withMs(uniqueMs));
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
  assert original.get_inner()==null;
  assert original.getMs().nameSize()==delegate.getMs().nameSize();
  Position p=original.getP();
  ExpCore.MCall delegateMCall=new ExpCore.MCall(
    new ExpCore.X(p, "this"), delegate.getMs(),Doc.empty(),
    tools.Map.of(s->new ExpCore.X(p,s), original.getMs().getNames()),
    p,Type.readThis0,original.getMt().getReturnType());
  if(!callInvariant){original=original.withInner(delegateMCall);}
  else{
    ExpCore.Block e=InvariantClose.eThis;
    if(original.getMt().getMdf()==Mdf.Class){
      if(original.getMt().getReturnType().getMdf()==Mdf.Immutable){
        e=InvariantClose.eRImm;
        }
      else {e=InvariantClose.eR;}//I think this also cover Mdf.Class
      }
    String newR=Functions.freshName("r", L42.usedNames);
    String newU=Functions.freshName("unusedInv", L42.usedNames);
    e=(Block) e.accept(new CloneVisitor(){
      public ExpCore visit(X s) {
        if(s.getInner().equals("r")) {return s.withInner(newR);}
        return s;
        }
      protected Block.Dec liftDec(Block.Dec f) {
        String x=f.getX();
        if(x.equals("r")) {x=newR;}
        else if(x.equals("unusedInv")) {x=newU;}
        return super.liftDec(f.withX(x));
        }
    });
    ExpCore.Block.Dec d0=e.getDecs().get(0);
    d0=d0.withInner(delegateMCall);
    ExpCore.Block b=e.withDeci(0,d0);
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
static ExpCore.Block eThis=(ExpCore.Block)Functions.parseAndDesugar("WrapExposer",
  "{method m() (r=void Void unusedInv=this.#invariant() r)}"
  ).getMs().get(0).getInner();

static ExpCore.Block eR=(ExpCore.Block)Functions.parseAndDesugar("WrapExposer",
  "{method m() (r=void Void unusedInv=r.#invariant() r)}"
  ).getMs().get(0).getInner();
static ExpCore.Block eRImm=(ExpCore.Block)Functions.parseAndDesugar("WrapExposer",
  "{method m() (This r=void Void unusedInv=r.#invariant() r)}"
  ).getMs().get(0).getInner();
      }
class WrapAux extends RenameMethodsAux{
  int count=0;
  static X thisX=new X(Position.noInfo,"this");
  public WrapAux(Program p, List<CsMxMx> renames, ClassB top) {
    super(p, renames, top);
    }
  @Override
  public ExpCore visit(MCall s) {
    ExpCore _e=StaticDispatch.of(p,g,s.getInner(),false);
    if(_e==null){return super.visit(s);}
    Type guessed=GuessTypeCore._of(p, g, _e,false);
    if(guessed==null){return super.visit(s);}
    MethodSelector ms2=mSToReplaceOrNull(s.getS(),guessed.getPath());
    if(ms2==null){return super.visit(s);}
    if(!s.getInner().equals(thisX)){
      LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
        "Exposer '"+s.getS()+"' called on non 'this' receiver in "+s.getP())
      );}
    count+=1;
    return super.visit(s);
    }
  @Override public
  ClassB.MethodWithType visit(ClassB.MethodWithType mwt){
    count=0;
    ClassB.MethodWithType res=super.visit(mwt);
    if(count==0){return res;}
    if(res.getMt().getMdf()==Mdf.Capsule){
      //ok to leave untouched the (stupid) ones with mdf==capsule
      //inded, if this used to call exposer, can not be used to open capsule
      return res;
      }
    assert res.getMt().getMdf().isIn(Mdf.Lent,Mdf.Mutable);
    //else, replace with the pattern
    if(res.getMt().getReturnType().getMdf()==Mdf.Lent){
      LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
        "Exposer called on lent returning method '"+res.getMs()+"' in "+res.getP())
        );}
    if(!res.getMt().getExceptions().isEmpty()){
      LambdaExceptionUtil.throwAsUnchecked(new RefactorErrors.ClassUnfit().msg(
        "Exposer called on exceptions leaking method '"+res.getMs()+"' in "+res.getP())
        );}
    ExpCore.Block.Dec d0=InvariantClose.eThis.getDecs().get(0);
    d0=d0.withInner(res.getInner());
    d0.with_t(res.getMt().getReturnType());
    ExpCore myE=InvariantClose.eThis.withDeci(0,d0);
    return res.withInner(myE);
    }
}