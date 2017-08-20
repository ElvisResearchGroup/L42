package is.L42.connected.withSafeOperators.refactor;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Type;
import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.PathAux;
import ast.Util.PathMwt;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import coreVisitors.From;
import facade.PData;
import is.L42.connected.withSafeOperators.Errors42;
import is.L42.connected.withSafeOperators.location.Method;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import newTypeSystem.TypeManipulation;
import tools.Assertions;

import java.util.*;
import java.util.stream.Collectors;
public class SumMethods {
  public static ClassB sumMethodsJ(PData pData,ClassB lib, List<Ast.C> path,MethodSelector m1,MethodSelector m2,MethodSelector mRes,String name) throws MethodClash, PathUnfit, SelectorUnfit{
    return sumMethodsP(pData.p,lib,path,m1,m2,mRes,name);
    }
  public static ClassB sumMethodsS(PData pData,ClassB lib, String path,String m1,String m2,String mRes,String name) throws MethodClash, PathUnfit, SelectorUnfit{
    return sumMethodsP(pData.p,lib,PathAux.parseValidCs(path),MethodSelector.parse(m1),MethodSelector.parse(m2),MethodSelector.parse(mRes),name);
    }
  public static ClassB sumMethodsP(Program p,ClassB lib, List<Ast.C> path, MethodSelector m1,MethodSelector m2,MethodSelector mRes,String name) throws MethodClash, PathUnfit, SelectorUnfit{
    if(MembersUtils.isPrivate(path)){throw new RefactorErrors.PathUnfit(path);}
    ClassB pathCb;try{pathCb=lib.getClassB(path);}
    catch(ast.ErrorMessage.PathMetaOrNonExistant unused){throw new RefactorErrors.PathUnfit(path);}
    if(MembersUtils.isPrivate(m1)){throw new RefactorErrors.SelectorUnfit(path,m1);}
    if(MembersUtils.isPrivate(m2)){throw new RefactorErrors.SelectorUnfit(path,m2);}
    if(MembersUtils.isPrivate(mRes)){throw new RefactorErrors.SelectorUnfit(path,mRes);}
    if(m1.getNames().size()+m2.getNames().size()-1!=mRes.getNames().size()){throw new RefactorErrors.SelectorUnfit(path,mRes);}
    MethodWithType mwt1=(MethodWithType) pathCb._getMember(m1);
    MethodWithType mwt2=(MethodWithType) pathCb._getMember(m2);
    if (mwt1==null){throw new RefactorErrors.SelectorUnfit(path,m1);}
    if (mwt2==null){throw new RefactorErrors.SelectorUnfit(path,m2);}
    int index=m2.getNames().indexOf(name);
    if(index==-1){throw new RefactorErrors.SelectorUnfit(path,m2);}
    MethodType mt1=mwt1.getMt();
    MethodType mt2=mwt2.getMt();
    MethodType mtU=mtU(index,mt1,mt2);
    if(mtU==null){throw makeMethodClash(lib, path, mwt1,mwt2).msg("Incompatible method modifiers");}
    ExpCore eU=eU(index,mwt2.getP(),mt1,mt2,m1,m2, mRes);
    MethodWithType mwtU=new MethodWithType(Doc.empty(),mRes,mtU,Optional.of(eU),mwt2.getP() );
    mwtU=getComposedMwt(p, path, lib, mRes, pathCb, mwtU);
    boolean replOk=isReplacedParOk(p,index,mt1,mt2);
    if(!replOk){throw makeMethodClash(lib, path, mwt1,mwt2).msg("Replaced parameter not a supertype of return type");}
    return finalResult(lib, path, mwtU);
  }
private static MethodClash makeMethodClash(ClassB lib, List<Ast.C> path, MethodWithType mwt1, MethodWithType mwt2) {
return new RefactorErrors.MethodClash(
  Method.of(mwt1,lib,path), Method.of(mwt2,lib,path)
  );
}
  private static ClassB finalResult(ClassB lib, List<Ast.C> path, MethodWithType mwtU) {
    if(path.isEmpty()){
      return lib.withMember(mwtU);
      }//if may be omitted?
    return lib.onClassNavigateToPathAndDo(path,cbi->cbi.withMember(mwtU));
  
  }

  private static MethodWithType getComposedMwt(Program p,List<Ast.C> path,ClassB lib, MethodSelector mRes, ClassB pathCb, MethodWithType mwtU) throws MethodClash {
    MethodWithType mwtC=(MethodWithType) pathCb._getMember(mRes);
    if(mwtC==null){return mwtU;}
    if(mwtC.get_inner().isPresent()){throw makeMethodClash(lib, path, mwtU,mwtC).msg("Method "+mRes+" already implemented");}
    if(Compose.mtGT(p,mwtU.getMt(),mwtC.getMt())){return Compose.sumMwtij(p,mwtU,mwtU,mwtC);}    
    throw makeMethodClash(lib, path, mwtU,mwtC).msg("Composed type is not a subtype of the other");   
    }
  
  static MethodType mtU(int index,MethodType mt1,MethodType mt2){
    Mdf mdfU=mdfU(mt1.getMdf(),mt2.getMdf());
    if(mdfU==null){return null;}
    Type removed=mt2.getTs().get(index);
    boolean isRemovedPh=!Functions.isComplete(removed);
    List<Type> totTypes;
    if(isRemovedPh){
      totTypes=new ArrayList<>(mt1.getTs());
    }
    else{
      totTypes=mt1.getTs().stream().map(t->TypeManipulation.noFwd(t)).collect(Collectors.toList());
    }
    totTypes.addAll(mt2.getTs());
    int toRemove=mt1.getTs().size()+index;
    totTypes.remove(toRemove);
    List<Type> exU = new ArrayList<>(mt1.getExceptions());
    exU.addAll(mt2.getExceptions());
    MethodType mtU =new MethodType(false,
        mdfU,totTypes,mt2.getReturnType(),exU);
    return mtU;
    }
  static boolean isReplacedParOk(Program p,int index,MethodType mt1,MethodType mt2){
    if(mt2.getTs().isEmpty()){return false;}
    Type p1 = mt2.getTs().get(index);
    Type r = mt1.getReturnType();
    return p.subtypeEq(r,p1);
    }


  static ExpCore eU(int index,Position pos,MethodType mt1,MethodType mt2,MethodSelector m1,MethodSelector m2,MethodSelector mRes){
    ExpCore r1=(mt1.getMdf()==Mdf.Class)?ExpCore.EPath.wrap(Path.outer(0)):new ExpCore.X(pos,"this");
    ExpCore r2=(mt2.getMdf()==Mdf.Class)?ExpCore.EPath.wrap(Path.outer(0)):new ExpCore.X(pos,"this");
    //this/outer0 . m2(this/outer0 .m1(ps1),ps2)
    List<ExpCore> ps1=new ArrayList<>();
    for(String x:mRes.getNames().subList(0,m1.getNames().size())){ps1.add(new ExpCore.X(pos,x));}
    ExpCore eInner=new ExpCore.MCall(r1, m1,Doc.empty(), ps1, pos,Type.immThis0.withMdf(mt1.getMdf()));

    ArrayList<ExpCore> ps2=new ArrayList<>();
    for(int i=1;i<m2.getNames().size();i++){
      String x=mRes.getNames().get(m1.getNames().size()+i-1);
      ps2.add(new ExpCore.X(pos,x));
      }
    ps2.add(index,eInner);
    ExpCore eU=new ExpCore.MCall(r2, m2, Doc.empty(),ps2 , pos,Type.immThis0.withMdf(mt2.getMdf()));
    return eU;
  }
  private static Mdf mdfU(Mdf mdf1, Mdf mdf2) {
    if(mdf1==Mdf.Capsule && mdf2==Mdf.Capsule){return null;}
    if(mdf1==mdf2){return mdf1;}
    if(mdf1==Mdf.Class){return mdf2;}
    if(mdf2==Mdf.Class){return mdf1;}
    if(mdf1==Mdf.Capsule|| mdf2==Mdf.Capsule){return null;}
    if(mdf1==Mdf.Immutable|| mdf2==Mdf.Immutable){
      if(mdf1==Mdf.Readable ||mdf2==Mdf.Readable){return Mdf.Immutable;}
      return null;
    }
    //not immutable
    if(mdf1==Mdf.Mutable|| mdf2==Mdf.Mutable){return Mdf.Mutable;}
    return Mdf.Lent;
  }
}
