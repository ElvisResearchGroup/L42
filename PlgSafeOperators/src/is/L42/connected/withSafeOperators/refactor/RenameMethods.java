package is.L42.connected.withSafeOperators.refactor;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.PathAux;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Type;
import ast.ErrorMessage.NormImpossible;
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import ast.Util.CsMx;
import ast.Util.CsMxMx;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Locator;
import programReduction.Program;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import facade.L42;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import newTypeSystem.G;
import newTypeSystem.GuessTypeCore;
import newTypeSystem.StaticDispatch;
import newTypeSystem.TIn;
import tools.Assertions;
import tools.LambdaExceptionUtil;
import tools.Map;

/*

Refactor[
  hide:\"foo()" of:\"Bar";
  hide:\"foo()";
  rename:\"foo()" of:\"Bar" into:\"bar()";
  rename:\"foo()" into:\"bar()";
  closeState:\"Bar";
  abstract:\"foo()" of:\"Bar";
  abstract:\"foo()" of:\"Bar" alias:\"..";
  abstract:\"foo()";
  abstract:\"foo()" alias:\"..";
  ]
the idea is that ToAbstract is a kind of rename
where you leave the header, now there is a bug in toAbstract,
if the xs change name, there is no rename in e.
We can make toabstract in a single pass together with renaming here
It is good to have an option to let "Unift tests"
just out of the multi operation.
It is good for Use, that can create a single map and apply to all superlibraries.

main operations
sum
renameClass/hideClass/ToAbstractClass
renameMetod/hide/toAbs/closeState
redirect


*/
public class RenameMethods{
  boolean ignoreUnfit=false;
  List<CsMxMx> commands;
  public RenameMethods(){
    commands=Collections.emptyList();
    }
  public RenameMethods(List<CsMxMx> commands){
    this.commands=commands;
    }
  public RenameMethods addRenameJ(List<Ast.C> cs, MethodSelector ms1,MethodSelector ms2){
    return new RenameMethods(Functions.push(commands, new CsMxMx(cs,false,ms1,ms2)));
    }
  public RenameMethods addHideJ(List<Ast.C> cs, MethodSelector ms){
    return new RenameMethods(Functions.push(commands,new CsMxMx(cs,false,ms,null)));
    }
  public RenameMethods addCloseJ(List<Ast.C> cs){
    return new RenameMethods(Functions.push(commands,new CsMxMx(cs,false,null,null)));
    }
  public RenameMethods addAbstractAliasJ(List<Ast.C> cs, MethodSelector ms1,MethodSelector ms2){
    return new RenameMethods(Functions.push(commands, new CsMxMx(cs,true,ms1,ms2)));
    }
  public RenameMethods addAbstractJ(List<Ast.C> cs, MethodSelector ms){
    return new RenameMethods(Functions.push(commands,new CsMxMx(cs,true,ms,null)));
    }


public RenameMethods addRenameS(String cs, String ms1,String ms2){
  return addRenameJ(PathAux.parseValidCs(cs),MethodSelector.parse(ms1),MethodSelector.parse(ms2));
  }
public RenameMethods addHideS(String cs, String ms){
  return addHideJ(PathAux.parseValidCs(cs),MethodSelector.parse(ms));
  }
public RenameMethods addCloseS(String cs){
  return addCloseJ(PathAux.parseValidCs(cs));
  }
public RenameMethods addAbstractAliasS(String cs, String ms1,String ms2){
  return addAbstractAliasJ(PathAux.parseValidCs(cs),MethodSelector.parse(ms1),MethodSelector.parse(ms2));
  }
public RenameMethods addAbstractS(String cs, String ms){
  return addAbstractJ(PathAux.parseValidCs(cs),MethodSelector.parse(ms));
  }



  public ClassB act(PData pData,ClassB that) throws PathUnfit, SelectorUnfit,MethodClash, ClassUnfit{
    return actP(pData.p,that);
    }
  public ClassB actP(Program p,ClassB that) throws PathUnfit, SelectorUnfit,MethodClash, ClassUnfit{
    List<CsMxMx> renamesLoc=new ArrayList<>();
    //check all paths/methods exists and are not private
    //check all methods are not refine!
    fillRenames(p,that, renamesLoc);
    checkConsistency(renamesLoc);
    //call aux
    if(renamesLoc.isEmpty()){
      return that;}
    ClassB res=(ClassB)that.accept(new RenameMethodsAux(p,renamesLoc,that));
    return res;
    //somehow throw exeptions,
    //why it seams java is happy with declaring an sneakly unthrow error?
    }
private void checkConsistency(List<CsMxMx> renamesLoc) {
// no two mapping for same path.ms
//TODO: what to do for multiple mapping to the same path.ms2?
//is it ok? need to fix the code?
//never two alias on the same name?
}
//flag forToAbstract
//ms2=null for hide/noAlias
//ms1=null, ms2=null for close state
private void fillRenames(Program p,ClassB that, List<CsMxMx> renamesLoc) throws PathUnfit, SelectorUnfit, ClassUnfit {
  for(CsMxMx r:this.commands){
    if(MembersUtils.isPrivate(r.getCs())){throw new RefactorErrors.PathUnfit(r.getCs()).msg("Path is private");}
    if(r.getMs1()!=null && MembersUtils.isPrivate(r.getMs1())){throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs1()).msg("Selector is private");}
    if(r.getMs2()!=null && MembersUtils.isPrivate(r.getMs2())){throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs2()).msg("Selector is private");}
    if(r.getMs1()!=null && r.getMs2()!=null && r.getMs1().getNames().size()!=r.getMs2().getNames().size()){throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs2()).msg("Selector have different number of arguments w.r.t "+r.getMs1());}
    ClassB cb;try{cb=that.getClassB(r.getCs());}
    catch(ast.ErrorMessage.PathMetaOrNonExistant unused){
      if(this.ignoreUnfit){continue;}
      throw new RefactorErrors.PathUnfit(r.getCs()).msg("Path not found");
      }
    if(r.getMs1()==null){
      assert r.getMs2()==null;
      assert !r.isFlag();
      fillRenamesCoherent(r,p.evilPush(that).navigate(r.getCs()),renamesLoc);
      return;
      }
    assert r.getMs1()!=null;
    Member m= cb._getMember(r.getMs1());
    if(m==null){
      if(this.ignoreUnfit){continue;}
      throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs1()).msg("Selector not found");
      }
    fillRenamesOne(r,(MethodWithType)m,renamesLoc);
    }
  }
private void fillRenamesCoherent(CsMxMx r, Program p, List<CsMxMx> renamesLoc) throws ClassUnfit {
  boolean alreadyClosed=false;
  List<MethodSelector> sel=new ArrayList<>();
  for(MethodWithType mwt:p.top().mwts()){
    boolean abs=mwt.get_inner()==null;
    if(abs && mwt.getMs().isUnique()){alreadyClosed=true;}
    if(abs){sel.add(mwt.getMs());}
    }
  if(alreadyClosed && this.ignoreUnfit){return;}
  if(alreadyClosed){throw new RefactorErrors.ClassUnfit().msg("Class is already closed");}
  String msg=null;
  boolean coherent;try{coherent=newTypeSystem.TsLibrary.coherent(p,true);}
  catch(ErrorMessage.NotOkToStar em){
    msg=em.getReason();
    coherent=false;
    }
  if(!coherent){
    throw new RefactorErrors.ClassUnfit().msg("Incoherent class can not be closed:\n"+msg);}
  long prN=L42.freshPrivate();
  for(MethodSelector msi :sel){
    renamesLoc.add(new CsMxMx(r.getCs(),false,msi,msi.withUniqueNum(prN)));
    }
  }
void fillRenamesOne(CsMxMx r,MethodWithType mwt, List<CsMxMx> renamesLoc) throws PathUnfit, SelectorUnfit {
  if(mwt.getMt().isRefine()){
    if(!r.isFlag()){//is ok to make refine methods abstract
      throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs1()).msg("Selector is refine. Interface methods can not be hidden.");
      }
    }
  boolean ms1Abs=mwt.get_inner()==null;
  MethodSelector ms2=r.getMs2();
  if(ms2!=null){
    //if(ms1Abs && r.isFlag()) handle in RenameMethodsAux
    //only check for abstract->abstract with destination
    renamesLoc.add(new CsMxMx(r.getCs(),r.isFlag(),r.getMs1(),ms2));
    return;
    }
  assert ms2==null;
  if(ms1Abs && r.isFlag()){return;}//No op for abstract->abstract with no destination
  if(ms1Abs){throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs1()).msg("Selector is abstract");}
  if(r.isFlag()){
    renamesLoc.add(new CsMxMx(r.getCs(),true,r.getMs1(),null));
    return;
    }
  fillRenamesOneHide(r,renamesLoc);
  }
void fillRenamesOneHide(CsMxMx r, List<CsMxMx> renamesLoc) throws PathUnfit, SelectorUnfit {
  assert r.isFlag()==false;
  assert r.getMs2()==null;
  long prN=L42.freshPrivate();
  MethodSelector ms2=r.getMs1();
  ms2=ms2.withName(Functions.freshName(ms2.getName(), L42.usedNames));
  ms2=ms2.withUniqueNum(prN);
  renamesLoc.add(new CsMxMx(r.getCs(),false,r.getMs1(),ms2));
  }
}

class RenameMethodsAux extends coreVisitors.CloneVisitorWithProgram{
  List<CsMxMx> renames;
  ClassB top;
  public RenameMethodsAux(Program p,List<CsMxMx> renames,ClassB top) {
    super(p);
    this.renames = renames;
    this.top=top;
  }
  public G g=null;
  public MethodSelector visitMS(MethodSelector original,Path src){
  MethodSelector result=mSToReplaceOrNull(original,src);
  if(result==null){ return original;}
  return result;
}
  public ExpCore visit(ClassB l){
    G oldG=g;
    g=null;
    try{return super.visit(l);}
    finally{g=oldG;}
    }
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){throw Assertions.codeNotReachable();}
  public ClassB.MethodWithType visit(ClassB.MethodWithType mwt){
    assert g==null;
    g=GuessTypeCore.freshGFromMt(p,mwt);
    ClassB.MethodWithType res;try{res=super.visit(mwt);}
    finally{g=null;}
    if(res.get_inner()==null){return res;}
    if(res.getMs().getNames().equals(mwt.getMs().getNames())){return res;}
    //else, we need to rename variables
    ExpCore body = MembersUtils.renameParNames(
      mwt.getMs().getNames(), res.getMs().getNames(),
      res.getInner());
    return res.withInner(body);
    }

  public ExpCore visit(Block s) {
    G oldG=g;
    try{
      Block sGuessed;try{sGuessed=(Block)StaticDispatch.of(p, g, s, false);}
      catch(PathMetaOrNonExistant pmone){sGuessed=s;}
      G baseG=g.add(p, sGuessed.getDecs());
      g=baseG;
      List<Dec> ds = liftDecs(s.getDecs());
      List<On> ons = Map.of((o)->{
        g=baseG.addTx(o.getX(),o.getT());
        return this.liftO(o);
        },s.getOns());
      g=baseG;
      ExpCore inner = lift(s.getInner());
      return new Block(liftDoc(s.getDoc()),ds,inner,ons,s.getP(),s.getTypeOut());
      }
    finally{g=oldG;}
    }

  public ExpCore visit(MCall s) {
    Type guessed=s.getTypeRec();
    if(guessed==null) {
      ExpCore _e=StaticDispatch.of(p,g,s.getInner(),false);
      if(_e!=null){guessed=GuessTypeCore._of(p, g,_e,false);}
      }
    if(guessed==null){return super.visit(s);}
    MethodSelector ms2=visitMS(s.getS(),guessed.getPath());
    return super.visit(s.withS(ms2));
    }
  @Override protected MethodSelector liftMsInMetDec(MethodSelector ms){
    return visitMS(ms,Path.outer(0));
    }

  @Override public List<Member> liftMembers(List<Member> s) {
    s=new ArrayList<>(s);//TODO: it may be better to make the method do the abstraction
    addToAbstractAliases(s);
    List<Member> res=new ArrayList<>(super.liftMembers(s));
    collapseEqualMs(res);
    return res;
    }
  /**
  Sneakily throw MethodClash
  */
private void collapseEqualMs(List<Member> res) {
  for(int i=0;i<res.size();i++){//res may shrink during iteration
    Member resi=res.get(i);
    if(resi instanceof NestedClass){break;}//break since in the end is all nested
    MethodWithType mwti=(MethodWithType)resi;
    mwti=accumulateOthers(mwti,res,i);
    res.set(i,mwti);
    }
  }
private void addToAbstractAliases(List<Member> res) {
  for(CsMxMx r:this.renames){
    if(!r.isFlag()){continue;}
    if(r.getCs().equals(this.whereFromTop())){
      int index=0;
      MethodWithType mwtA=null;
      for(;index<res.size();index++){
        Member m=res.get(index);
        if( m instanceof NestedClass){break;}
        mwtA=(MethodWithType)m;
        if(mwtA.getMs().equals(r.getMs1())){break;}
        }
      assert mwtA!=null;
      if(mwtA.get_inner()!=null){
        res.set(index,mwtA.with_inner(null));
        if(r.getMs2()!=null){
          MethodWithType mwtB=mwtA.withMt(mwtA.getMt().withRefine(false)).withMs(r.getMs2());
          if(!r.getMs2().getNames().equals(r.getMs1().getNames())){
            mwtB=mwtB.withInner(MembersUtils.renameParNames(
              r.getMs1().getNames(), r.getMs2().getNames(),
              mwtB.getInner()));
            }
          res.add(mwtB);
          }
        }
      }//TODO: else should we check for method clash anyway?
    }
  }
  MethodWithType accumulateOthers(MethodWithType mwti,List<Member> res,int i){
    while(true){//can depend on the order?, the user have to select the most specific one last?
      MethodWithType other=_other(i,mwti.getMs(),res);//mutate res
      if(other==null){return mwti;}
      //sum mwti and other
      try {mwti=new Compose(top,top).sumMwt(p, p.top().isInterface(), mwti, p.top().isInterface(), other);}
      catch (MethodClash e) {LambdaExceptionUtil.throwAsUnchecked(e);return null;}
      }
    }
   MethodWithType _other(int start,MethodSelector ms,List<Member>mwts){
     //for(int i=start+1;i<mwts.size();i++){
     for(int i=mwts.size()-1;i>start;i--){
       //Start from the bottom: so that the mwt originally in place have
       //a strong influence on the final return type
       Member resi=mwts.get(i);
       if(resi instanceof NestedClass){continue;}
       MethodWithType mwti=(MethodWithType)resi;
       if(!mwti.getMs().equals(ms)){continue;}
       mwts.remove(i);
       return mwti;
       }
     return null;
     }

  public MethodSelector mSToReplaceOrNull(MethodSelector original,Path src){
    assert src!=null;
    for(CsMxMx r:this.renames){
      if(r.isFlag()){continue;}
      if(!original.equals(r.getMs1())){continue;}
      Path pathSrc=Path.outer(this.levels,r.getCs());
      if(p.subtypeEq(src, pathSrc)){return r.getMs2();}
      }
    return null;
  }

}
