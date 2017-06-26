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
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Type;
import ast.ErrorMessage.NormImpossible;
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
import ast.Util.CsMxMx;
import ast.Util.PathMxMx;
import ast.Util.PathPath;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Locator;
import programReduction.Program;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import newTypeSystem.GuessTypeCore;
import newTypeSystem.TIn;
import tools.Assertions;
import tools.LambdaExceptionUtil;
import tools.Map;

import newTypeSystem.GuessTypeCore.G;
public class RenameMethods{
  List<CsMxMx> renames;
  public RenameMethods(){renames=Collections.emptyList();}
  public RenameMethods(List<CsMxMx> renames){this.renames=renames;}
  public RenameMethods add(List<Ast.C> cs, MethodSelector ms1,MethodSelector ms2){
    return new RenameMethods(Functions.push(renames, new CsMxMx(cs,ms1,ms2)));
    }
  public ClassB renameMethods(PData pData,ClassB that) throws PathUnfit, SelectorUnfit,MethodClash{
    return renameMethodsP(pData.p,that);
    }
  public ClassB renameMethodsP(Program p,ClassB that) throws PathUnfit, SelectorUnfit,MethodClash{
    //check all paths/methods exists and are not private
    //check all methods are not refine!
    //check all ms have same number of parameters
    for(CsMxMx r:this.renames){
      if(MembersUtils.isPrivate(r.getCs())){throw new RefactorErrors.PathUnfit(r.getCs()).msg("Path is private");}
      if(MembersUtils.isPrivate(r.getMs1())){throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs1()).msg("Selector is private");}
      if(MembersUtils.isPrivate(r.getMs2())){throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs2()).msg("Selector is private");}
      if(r.getMs1().getNames().size()!=r.getMs2().getNames().size()){throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs2()).msg("Selector have different number of arguments w.r.t "+r.getMs1());}

      try{
        Member m= that.getClassB(r.getCs())._getMember(r.getMs1());
        if(m==null){throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs1()).msg("Selector not found");}
        if(((MethodWithType)m).getMt().isRefine()){throw new RefactorErrors.SelectorUnfit(r.getCs(),r.getMs1()).msg("Selector is refine. Rename needs to work on the source point of a selector.");}
        }
      catch(ast.ErrorMessage.PathMetaOrNonExistant unused){throw new RefactorErrors.PathUnfit(r.getCs()).msg("Path not found");}
    }
    //call aux
    ClassB res=(ClassB)that.accept(new RenameMethodsAux(p,renames,that));
    return res;
    //somehow throw exeptions,
    //why it seams java is happy with declaring an sneakly unthrow error?
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
    if(!res.get_inner().isPresent()){return res;}
    if(res.getMs().getNames().equals(mwt.getMs().getNames())){return res;}
    //else, we need to rename variables
    ExpCore body=res.getInner();
    assert res.getMs().getNames().size()==mwt.getMs().getNames().size();
    java.util.Map<String, String> toRename=tools.Map.list2map(
        mwt.getMs().getNames(),res.getMs().getNames());
    body=coreVisitors.RenameVars.of(body, toRename);
    return res.withInner(body);
    }
  
  public ExpCore visit(Block s) {
    List<Dec> ds = liftDecs(s.getDecs());
    List<On> ons = Map.of(this::liftO,s.getOns());
    G oldG=g;
    try{
      g=g.addGuessing(p, s.getDecs());
      ExpCore inner = lift(s.getInner());
      return new Block(liftDoc(s.getDoc()),ds,inner,ons,s.getP());
      }
    finally{g=oldG;}
    }

  public ExpCore visit(MCall s) {
    MethodSelector ms=s.getS();
    Type guessed=GuessTypeCore._of(p, g, s.getInner());
    if(guessed==null){return super.visit(s);}
    MethodSelector ms2=visitMS(ms,guessed.getPath());
    if(ms2.equals(ms)){return super.visit(s);}
    s=s.withS(ms2);
    return super.visit(s);
    }
  @Override protected MethodSelector liftMsInMetDec(MethodSelector ms){
    return visitMS(ms,Path.outer(0));
    }
  /**
   Sneakily throw MethodClash
   */
  @Override public List<Member> liftMembers(List<Member> s) {
    List<Member> res=new ArrayList<>(super.liftMembers(s));
    for(int i=0;i<res.size();i++){//res may shrink during iteration
      Member resi=res.get(i);
      if(resi instanceof NestedClass){break;}//break since in the end is all nested
      MethodWithType mwti=(MethodWithType)resi;
      MethodWithType other=_other(i,mwti.getMs(),res);//mutate res
      if(other==null){continue;}
      //sum mwti and other
      try {mwti=new Compose(top,top).sumMwt(p, p.top().isInterface(), mwti, p.top().isInterface(), other);}
      catch (MethodClash e) {LambdaExceptionUtil.throwAsUnchecked(e);return null;}
      res.set(i,mwti);
      }
    return res;
    }
   MethodWithType _other(int start,MethodSelector ms,List<Member>mwts){
     for(int i=start+1;i<mwts.size();i++){
       Member resi=mwts.get(i);
       if(resi instanceof NestedClass){break;}//break since in the end is all nested
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
      if(!original.equals(r.getMs1())){continue;}
      Path pathSrc=Path.outer(this.levels,r.getCs());
      if(p.subtypeEq(src, pathSrc)){return r.getMs2();}
      }
    return null;
  }

}
