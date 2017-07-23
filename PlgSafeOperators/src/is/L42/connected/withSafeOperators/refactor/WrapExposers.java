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
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.MCall;
import ast.ExpCore.X;
import ast.Util.CsMx;
import ast.Util.CsMxMx;
import auxiliaryGrammar.Functions;
import facade.L42;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import newTypeSystem.GuessTypeCore;
import newTypeSystem.TsLibrary;
import programReduction.Program;
import tools.LambdaExceptionUtil;

public class WrapExposers {

static MethodSelector ms(MethodSelector s1,Map<String,String> nameMap){
  assert !s1.isUnique();
  String name=s1.getName();
  boolean hash=name.startsWith("#");
  if(hash){name=name.substring(1,name.length());}
  String n2=nameMap.get(name);
  assert n2!=null;
  if(hash){n2="#"+n2;}
  return s1.withName(n2);
  }

public static ClassB wrapExposers(PData p,List<Ast.C>path,ClassB top,MethodSelector freshK) throws PathUnfit, ClassUnfit{
  if(!MembersUtils.isPathDefined(top, path)){throw new RefactorErrors.PathUnfit(path);}
  if(MembersUtils.isPrivate(path)){throw new RefactorErrors.PathUnfit(path);}
  Program pPath=p.p.navigate(path);
  ClassB lPath=pPath.top();
  //discover constructor
  MethodWithType k=(MethodWithType) lPath._getMember(freshK);
  assert k!=null;
  //collect capsule names
  List<String> capsNames=new ArrayList<>();
  //map state selectos->fresh selectors
  Map<String,String> nameMap=new HashMap<>();
  for(int i=0;i<freshK.nameSize();i++){
    String ni=freshK.name(i);
    if(k.getMt().getTs().get(i).getMdf()==Mdf.MutableFwd){capsNames.add(ni);}
    nameMap.put(ni,Functions.freshName(ni, L42.usedNames));
    }
  //collect getters for capsule names
  Set<MethodSelector>msk=new HashSet<>();
  msk.add(freshK);
  List<CsMxMx> sel=new ArrayList<>();
  for(MethodWithType mwti:lPath.mwts()){
    if(!TsLibrary.coherentF(pPath,k,mwti)){continue;}
    assert !mwti.get_inner().isPresent();
    msk.add(mwti.getMs());
    // with non read result, assert is lent
    Mdf mdf=mwti.getMt().getReturnType().getMdf();
    if(mdf==Mdf.Readable){continue;}
    assert mdf!=Mdf.Immutable;//since capsule field
    if(mdf!=Mdf.Lent){throw new RefactorErrors.ClassUnfit().msg("Exposer not lent: '"+mwti.getMs()+"' in "+mwti.getP());}    
    sel.add(new CsMxMx(path,false,mwti.getMs(),ms(mwti.getMs(),nameMap)));
    } 
  WrapAux w=new WrapAux(p.p,sel,top);
  //call and return aux
  ClassB newTop=(ClassB)top.accept(w);
  return delegateState(msk,nameMap,newTop,p.p,path,freshK);
  }

private static ClassB delegateState(
        Set<MethodSelector> msk,
        Map<String, String> nameMap,
        ClassB newTop, Program p, List<C> path,
        MethodSelector freshK) {
  ClassB lPath=newTop.getClassB(path);
  List<MethodWithType> newMwts=new ArrayList<>();
  for(MethodWithType mwt:lPath.mwts()){
    if(!msk.contains(mwt.getMs())){newMwts.add(mwt);}
    Mdf mdf=mwt.getMt().getMdf();
    Mdf resMdf=mwt.getMt().getReturnType().getMdf();
    boolean isGet=mwt.getMs().getNames().isEmpty();
    if(mdf==Mdf.Class){
      //  replace delc freshK ->decl freshK(freshx1..freshxn) +delegator freshK
      continue;
      }
    if(isGet &&(resMdf==Mdf.Readable || resMdf==Mdf.Immutable )){
      //  replace decl get() ->decl freshXi()+ delegator get() this.freshXi()  
      continue;
      }
    if(isGet){
      // Exposer: should be already fixed:
      //rename .exposer() ->freshExposer()
      //  rename decl exposer() ->decl freshExposer()  
      continue;
     }
  //  replace decl set() ->decl freshXi(that)+ delegator set(that) (this.freshXi(that) invariant())
    
    }
//  close class, make freshK private.

//return newTop with newLPath
return null;
}
void delegator(boolean callInvariant,List<MethodWithType> newMwts, MethodWithType original,MethodSelector delegate){
  assert !original.get_inner().isPresent();
  assert original.getMs().nameSize()==delegate.nameSize();
  Position p=original.getP();
  MethodWithType delegateM=original.withMs(delegate);
  ExpCore.MCall delegateMCall=new ExpCore.MCall(
          new ExpCore.EPath(p, Path.outer(0)), delegate,Doc.empty(),
          tools.Map.of(s->new ExpCore.X(p,s), original.getMs().getNames()),p);
  if(!callInvariant){original=original.withInner(delegateMCall);}
  else{
    ExpCore.Block b=WrapAux.e.withDeci(0,WrapAux.e.getDecs().get(0).withInner(delegateMCall));
    original=original.withInner(b);
    }
  newMwts.add(original);
  newMwts.add(delegateM);
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
    ExpCore myE=e.withDeci(0,new ExpCore.Block.Dec(
      false,Optional.empty(),
      Functions.freshName("wrapExposer",L42.usedNames),
      mwt.getInner()
      ));
    return res.withInner(myE);
    }
}