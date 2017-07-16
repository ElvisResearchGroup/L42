package is.L42.connected.withSafeOperators.refactor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ast.ExpCore;
import ast.Ast;
import ast.Ast.C;
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
public static ClassB wrapExposers(PData p,List<Ast.C>path,ClassB top,String freshK,String mutK) throws PathUnfit, ClassUnfit{
  if(!MembersUtils.isPathDefined(top, path)){throw new RefactorErrors.PathUnfit(path);}
  if(MembersUtils.isPrivate(path)){throw new RefactorErrors.PathUnfit(path);}
  ClassB lPath=top.getClassB(path);
  //discover constructor
  Optional<MethodWithType> k1 = lPath.mwts().stream().filter(m->m.getMs().getName().equals(freshK)).findAny();
  MethodSelector kSel=k1.get().getMs().withName(mutK);//now I have field names
  MethodWithType k=(MethodWithType) lPath._getMember(kSel);
  assert k!=null;
  //collect capsule names
  List<String> capsNames=new ArrayList<>();
  for(int i=0;i<kSel.nameSize();i++){
    if(k.getMt().getTs().get(i).getMdf()==Mdf.Capsule){capsNames.add(kSel.name(i));}
    }
  //collect getters for capsula names
  List<CsMxMx> sel=new ArrayList<>();
  for(MethodWithType mwti:lPath.mwts()){
    if(!TsLibrary.coherentF(p.p,k1.get(),mwti)){continue;} 
    // with non read result, assert is lent
    Mdf mdf=mwti.getMt().getReturnType().getMdf();
    if(mdf==Mdf.Readable){continue;}
    assert mdf!=Mdf.Immutable;//since capsule field
    if(mdf!=Mdf.Lent){throw new RefactorErrors.ClassUnfit().msg("Exposer not lent: '"+mwti.getMs()+"' in "+mwti.getP());}
    sel.add(new CsMxMx(path,false,mwti.getMs(),mwti.getMs()));
    }
  //fill map
  WrapAux w=new WrapAux(p.p,sel,top);
  //call and return aux
  return (ClassB)top.accept(w);
  }
}
class WrapAux extends RenameMethodsAux{
  int count=0;
  private static X thisX=new X(Position.noInfo,"this");
  private static ExpCore.Block e=(ExpCore.Block)Functions.parseAndDesugar("WrapExposer", 
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