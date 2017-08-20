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
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ParseFail;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import newTypeSystem.GuessTypeCore;
import newTypeSystem.TsLibrary;
import newTypeSystem.TypeManipulation;
import newTypeSystem.TypeSystem;
import programReduction.Methods;
import programReduction.Program;
import tools.LambdaExceptionUtil;

public class Wither {

public static ClassB witherJ(PData p,List<Ast.C>path,ClassB top,MethodSelector immK) throws PathUnfit, SelectorUnfit, MethodUnfit, ClassUnfit{
  return wither(p.p,path,top,immK);
  }
/**
contract
path, path.k exists, k.mdf=class
for all xi in k.ms.xs
  (0)the type of xi in k Ti, Ti.mdf in {imm/class}
  only one of path.x() or path.#x()
    (1)exists, (2) has read/imm receiver,(3) no exceptions
    (4)return type T, T.mdf in {read/imm/class}
    (5)if T=class P then Ti=class P
    (6)if T=read/imm P, then Ti= imm P
 Then,
 for all xi, we generate method
 imm method T with(getxi.T xi) exc k.exceptionsPs
   This.k(x1:this.f1()..xi:xi..xn:this.fn())
 if 
   not already present in path,
   or present abstract with same method type (modulo refine).
 * @throws PathUnfit
 * @throws SelectorUnfit 
 * @throws MethodUnfit 
 * @throws ClassUnfit 
 
 */
public static ClassB wither(Program p,List<Ast.C>path,ClassB top,MethodSelector immK) throws PathUnfit, SelectorUnfit, MethodUnfit, ClassUnfit{
  if(MembersUtils.isPrivate(path)){throw new RefactorErrors.PathUnfit(path);}
  if(!MembersUtils.isPathDefined(top, path)){throw new RefactorErrors.PathUnfit(path);}
  if(MembersUtils.isPrivate(immK)){throw new RefactorErrors.SelectorUnfit(path,immK);}
  ClassB lPath=top.getClassB(path);
  MethodWithType k=(MethodWithType)lPath._getMember(immK);
  if(k==null){throw new SelectorUnfit(path,immK);}
  if(k.getMt().getMdf()!=Mdf.Class){throw new RefactorErrors.MethodUnfit().msg(immK.toString()+" not class method");}
  List<MethodWithType> getters=new ArrayList<>();
  for(Type ti:k.getMt().getTs()){
    Mdf m=ti.getMdf();
    if(m!=Mdf.Class && m!=Mdf.Immutable){throw new RefactorErrors.MethodUnfit().msg(immK.toString()+" invalid parameter mdf "+m);}
    }
  {int i=-1;for(String n:immK.getNames()){i+=1;
    MethodWithType fi=(MethodWithType)lPath._getMember(MethodSelector.of(n,Collections.emptyList()));  
    MethodWithType hfi=(MethodWithType)lPath._getMember(MethodSelector.of("#"+n,Collections.emptyList()));    
    boolean fiOk = fOk(k.getMt().getTs().get(i), fi);
    boolean hfiOk = fOk(k.getMt().getTs().get(i), hfi);
    if (fiOk && hfiOk){throw new ClassUnfit().msg("ambiguos field getter "+n+"() and #"+n+"()");}
    if (!fiOk && !hfiOk){throw new ClassUnfit().msg("no field getter for "+n+"()");}
    if(hfiOk){fi=hfi;}//fi was not ok
    getters.add(fi);
    }}
  MethodWithType template=template(k,getters);
  List<Member> mwts = new ArrayList<>(lPath.mwts());
  {int i=-1;for(MethodWithType g:getters){i+=1;
    String ni=immK.getNames().get(i);
    MethodSelector msi=template.getMs().withNames(Collections.singletonList(ni));
    Type retg=g.getMt().getReturnType();
    if(retg.getMdf()!=Mdf.Class){retg=retg.withMdf(Mdf.Immutable);}
    MethodType twi=template.getMt().withTs(Collections.singletonList(retg));
    MethodWithType former = (MethodWithType)lPath._getMember(msi);
    if(former!=null){
      if(former.get_inner().isPresent()){continue;}
      MethodType mt=former.getMt();
      twi=twi.withRefine(mt.isRefine());
      if(!twi.equals(mt)){continue;}
      }
    MCall bi=(MCall)template.getInner();
    bi=bi.withEsi(i,new ExpCore.X(g.getP(),ni ));
    MethodWithType wi=new MethodWithType(Doc.empty(),msi,twi,Optional.of(bi),k.getP());
    Functions.replaceIfInDom(mwts,wi);
    }}
  mwts.addAll(lPath.ns());
  lPath=lPath.withMs(mwts);
  //return newTop with newLPath
  if(path.isEmpty()){return lPath;}
  final ClassB lP=lPath;
  return top.onClassNavigateToPathAndDo(path, l->lP);
  }
private static MethodWithType template(MethodWithType k,List<MethodWithType>getters){
//imm method T with(getxi.T xi) exc k.exceptionsPs
//This.k(x1:this.f1()..xi:xi..xn:this.fn())
  MethodType mt=new MethodType(
    false,Mdf.Immutable,
    tools.Map.of(m->m.getMt().getReturnType().withMdf(Mdf.Immutable),getters),
    k.getMt().getReturnType(),k.getMt().getExceptions()
    );
  MCall body=new MCall(new ExpCore.EPath(k.getP(),Path.outer(0)), k.getMs(), Doc.empty(),
    tools.Map.of(Wither::invk,getters),k.getP(),Type.classThis0);
  MethodSelector ms=MethodSelector.of("with",Collections.emptyList());
  return new MethodWithType(Doc.empty(),ms,mt,Optional.of(body),k.getP());
  }
private static MCall invk(MethodWithType g){
  return new MCall(new ExpCore.X(g.getP(), "this"), g.getMs(),Doc.empty(),Collections.emptyList(),g.getP(),Type.immThis0.withMdf(g.getMt().getMdf()));
  }
private static boolean fOk(Type ti, MethodWithType fi) {
  //(1)exists, (2) has read/imm receiver,(3) no exceptions
  if(fi==null){return false;}
  Mdf r=fi.getMt().getMdf();
  if(r!=Mdf.Immutable && r!=Mdf.Readable){return false;}
  if(!fi.getMt().getExceptions().isEmpty()){return false;}   
  //(4)return type T, T.mdf in {read/imm/class}
  Type t=fi.getMt().getReturnType();
  Mdf m=t.getMdf();
  if(m!=Mdf.Immutable && m!=Mdf.Readable && m!=Mdf.Class){return false;} 
  //(5)if T=class P then Ti=class P
  if(m==Mdf.Class){return t.equals(ti);}
  //(6)if T=read/imm P, then Ti= imm P 
  assert m==Mdf.Immutable || m==Mdf.Readable;
  if(ti.getMdf()!=Mdf.Immutable){return false;}
  return t.getPath().equals(ti.getPath());
  }
}