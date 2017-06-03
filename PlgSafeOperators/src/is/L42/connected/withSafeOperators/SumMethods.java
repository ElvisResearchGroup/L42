package is.L42.connected.withSafeOperators;
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
import ast.Util.PathMwt;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import coreVisitors.From;
import newTypeSystem.TypeManipulation;
import tools.Assertions;

import java.util.*;
import java.util.stream.Collectors;
public class SumMethods {
  public static ClassB sumMethods(ClassB lib, List<Ast.C> path, MethodSelector m1,MethodSelector m2,MethodSelector mRes,String name){
    ClassB pathCb = pathCb(lib, path);
    Member mem1=Errors42.checkExistsPathMethod(lib, path, Optional.of(m1));
    Member mem2=Errors42.checkExistsPathMethod(lib, path, Optional.of(m2));
    MethodType mt1=((MethodWithType)pathCb._getMember(m1)).getMt();
    MethodType mt2=((MethodWithType)pathCb._getMember(m2)).getMt();
    int index=m2.getNames().indexOf(name);
    if(index==-1){
      throw Errors42.errorParameterMismatch(path, mem1,mem2, false,false,false);
      }
    checkParSize(index,path, m1, m2, mRes, mem1, mem2, mt1, mt2);
    MethodType mtU=mtU(index,mt1,mt2);
    if(mtU==null){throw Errors42.errorParameterMismatch(path, mem1,mem2, isReplacedParOk(index,mt1,mt2),false,true);}
    ExpCore eU=eU(index,mem2.getP(),mt1,mt2,m1,m2, mRes);
    MethodWithType mwtU=new MethodWithType(Doc.empty(),mRes,mtU,Optional.of(eU),mem2.getP() );
    checkConflict(path, mRes, pathCb, mwtU);
    boolean replOk=isReplacedParOk(index,mt1,mt2);
    if(!replOk){throw Errors42.errorParameterMismatch(path, mem1,mem2, false,true,true);}
    return finalResult(lib, path, mwtU);
  }
  private static ClassB finalResult(ClassB lib, List<Ast.C> path, MethodWithType mwtU) {
    if(path.isEmpty()){
      return lib.withMember(mwtU);
      }//if may be omitted?
    return lib.onClassNavigateToPathAndDo(path,cbi->cbi.withMember(mwtU));
  }
  private static void checkParSize(int index,List<Ast.C> path, MethodSelector m1, MethodSelector m2, MethodSelector mRes, Member mem1, Member mem2, MethodType mt1, MethodType mt2) {
    if(m1.getNames().size()+m2.getNames().size()-1!=mRes.getNames().size()){
      throw Errors42.errorParameterMismatch(path, mem1,mem2, isReplacedParOk(index,mt1,mt2),mdfU(mt1.getMdf(),mt2.getMdf())!=null,false);
    }
  }
  private static ClassB pathCb(ClassB lib, List<Ast.C> path) {
    ClassB pathCb=lib;
    if(!path.isEmpty()){
      pathCb=(ClassB)((NestedClass)Errors42.checkExistsPathMethod(lib, path, Optional.empty())).getInner();
    }
    return pathCb;
  }
  private static void checkConflict(List<Ast.C> path, MethodSelector mRes, ClassB pathCb, MethodWithType mwtU) {
    /*for(PathMwt e:pathCb.getStage().getInherited()){
      if(e.getMwt().getMs().equals(mRes)){//method declared in an interface and not implemented
        MethodWithType mtConflict=e.getMwt();
        mtConflict=From.from(mtConflict, From.fromP(e.getOriginal(),Path.outer(0,path)));
        Errors42.checkMethodClash(path, mwtU,mtConflict,false);
        }
    }*/ //unneded for normalized classb
    Optional<Member> optConflict = Functions.getIfInDom(pathCb.getMs(),mRes);
    if(optConflict.isPresent()){
      if(optConflict.get() instanceof MethodImplemented){
        throw Errors42.errorMethodClash(path,mwtU, optConflict.get(), true,Collections.emptyList(),true,true,false);
      }
      MethodWithType mwtC=(MethodWithType)optConflict.get();
      Errors42.checkMethodClash(path, mwtU,mwtC,false);
    }
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
  static boolean isReplacedParOk(int index,MethodType mt1,MethodType mt2){
    if(mt2.getTs().isEmpty()){return false;}
    Type p1 = mt2.getTs().get(index);
    Type r = mt1.getReturnType();
    return p1.equals(r);
    }


  static ExpCore eU(int index,Position pos,MethodType mt1,MethodType mt2,MethodSelector m1,MethodSelector m2,MethodSelector mRes){
    ExpCore r1=(mt1.getMdf()==Mdf.Class)?ExpCore.EPath.wrap(Path.outer(0)):new ExpCore.X(pos,"this");
    ExpCore r2=(mt2.getMdf()==Mdf.Class)?ExpCore.EPath.wrap(Path.outer(0)):new ExpCore.X(pos,"this");
    //this/outer0 . m2(this/outer0 .m1(ps1),ps2)
    List<ExpCore> ps1=new ArrayList<>();
    for(String x:mRes.getNames().subList(0,m1.getNames().size())){ps1.add(new ExpCore.X(pos,x));}
    ExpCore eInner=new ExpCore.MCall(r1, m1,Doc.empty(), ps1, pos);

    ArrayList<ExpCore> ps2=new ArrayList<>();
    for(int i=1;i<m2.getNames().size();i++){
      String x=mRes.getNames().get(m1.getNames().size()+i-1);
      ps2.add(new ExpCore.X(pos,x));
      }
    ps2.add(index,eInner);
    ExpCore eU=new ExpCore.MCall(r2, m2, Doc.empty(),ps2 , pos);
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
