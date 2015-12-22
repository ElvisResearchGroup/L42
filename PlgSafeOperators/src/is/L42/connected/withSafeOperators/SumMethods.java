package is.L42.connected.withSafeOperators;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.PathMwt;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;
import tools.Assertions;

import java.util.*;
public class SumMethods {
  public static ClassB sumMethods(ClassB lib, List<String> path, MethodSelector m1,MethodSelector m2,MethodSelector mRes){
    ClassB pathCb=lib;
    if(!path.isEmpty()){
      pathCb=(ClassB)((NestedClass)Errors42.checkExistsPathMethod(lib, path, Optional.empty())).getInner();
    }
    Member mem1=Errors42.checkExistsPathMethod(lib, path, Optional.of(m1));
    Member mem2=Errors42.checkExistsPathMethod(lib, path, Optional.of(m2));
    MethodWithType mwt1 = Program.extractMwt(mem1,(ClassB) pathCb);
    MethodWithType mwt2 = Program.extractMwt(mem2,(ClassB) pathCb);
    MethodType mt1=mwt1.getMt();
    MethodType mt2=mwt2.getMt();
    boolean wrongFirstPar=true;
    if(!mt2.getTs().isEmpty()){
       Type p1 = mt2.getTs().get(0);
       Type r = mt1.getReturnType();
       if(p1.equals(r)){wrongFirstPar=false;}
    }
    Mdf mdfU=mdfU(mt1.getMdf(),mt2.getMdf());
    List<String> totPars=new ArrayList<>();
    List<Type> totTypes=new ArrayList<>();
    List<Doc> totDocs=new ArrayList<>();
    for(int i=1;i<m2.getNames().size();i++){
      totPars.add(m2.getNames().get(i));
      totTypes.add(mt2.getTs().get(i));
      totDocs.add(mt2.getTDocs().get(i));
      }
    assert totPars.size()==totTypes.size();
    assert totPars.size()==totDocs.size();
    for(int i=0;i<m1.getNames().size();i++){
      String e1=m1.getNames().get(i);
      Type t1=mt1.getTs().get(i);
      int index=totPars.indexOf(e1);
      if(index==-1){
        totPars.add(e1);
        totTypes.add(t1);
        totDocs.add(mt1.getTDocs().get(i));
        }
      else {//check they have the same type
        Type t2=totTypes.get(index);
        if(!t1.equals(t2)){
          throw Errors42.errorParameterMismatch(path, mem1,mem2, !wrongFirstPar,mdfU!=null,false);
        }
        totDocs.set(index,totDocs.get(index).sum(mt1.getTDocs().get(i)));
        //totDocs(index, val:#+di)
      }
      }
    assert totPars.size()==totTypes.size();
    assert totPars.size()==totDocs.size();
    boolean parAll=true;
    List<Type> totTypesInOrder=new ArrayList<>();
    List<Doc> totDocsInOrder=new ArrayList<>();
    if(totPars.size()!=mRes.getNames().size()){parAll=false;}
    else for(String nRes:mRes.getNames()){
      int index=totPars.indexOf(nRes);
      if(index==-1){parAll=false; break;}
      totTypesInOrder.add(totTypes.get(index));
      totDocsInOrder.add(totDocs.get(index));
    }
    if(wrongFirstPar||mdfU==null || !parAll){
      throw Errors42.errorParameterMismatch(path, mem1,mem2, !wrongFirstPar,mdfU!=null,parAll);
    }
    ArrayList<Path> exU = new ArrayList<>(mt1.getExceptions());
    exU.addAll(mt2.getExceptions());
    MethodType mtU =new MethodType(
         mt1.getDocExceptions().sum(mt2.getDocExceptions()),
         mdfU,totTypesInOrder,totDocsInOrder,mt2.getReturnType(),exU); 
    ClassB cbPath = Program.extractCBar(path,lib);
    MethodWithType mtConflict = null;
    for(PathMwt e:cbPath.getStage().getInherited()){
      if(e.getMwt().getMs().equals(mRes)){mtConflict=e.getMwt();}
    }
    ExpCore r1=(mt1.getMdf()==Mdf.Type)?Path.outer(0):new ExpCore.X("this");
    ExpCore r2=(mt2.getMdf()==Mdf.Type)?Path.outer(0):new ExpCore.X("this");
    //this/outer0 . m2(this/outer0 .m1(ps1),ps2)
    ArrayList<ExpCore> ps1=new ArrayList<>();
    for(String x:m1.getNames()){ps1.add(new ExpCore.X(x));}
    ExpCore eInner=new ExpCore.MCall(r1, m1,Doc.empty(), ps1, mem2.getP());
    
    ArrayList<ExpCore> ps2=new ArrayList<>();
    ps2.add(eInner);
    for(int i=1;i<m2.getNames().size();i++){ps2.add(new ExpCore.X(m2.getNames().get(i)));}
    ExpCore eU=new ExpCore.MCall(r2, m2, Doc.empty(),ps2 ,  mem2.getP());
    MethodWithType mwtU=new MethodWithType(Doc.empty(),mRes,mtU,Optional.of(eU),mem2.getP() );
    if(mtConflict!=null){
      //hard. Is satisfy one interface triky and risk to be buggy?
      Errors42.checkMethodClash(path, mwtU,mtConflict,true);//always throws
      throw  Assertions.codeNotReachable();
    }
    Optional<Member> optConflict = Program.getIfInDom(cbPath.getMs(),mRes);    
    if(optConflict.isPresent()){
      if(optConflict.get() instanceof MethodImplemented){
        throw Errors42.errorMethodClash(path,mwtU, optConflict.get(), true,Collections.emptyList(),true,true,false);    
      }
      MethodWithType mwtC=(MethodWithType)optConflict.get();
      Errors42.checkMethodClash(path, mwtU,mwtC,false);//same as for sum/rename   
    } 
    //simple
    //TODO: is it a problem if m1+m2 is a private name? is that possible?
    if(path.isEmpty()){
      return lib.withMember(mwtU);
      }
    return ClassOperations.onClassNavigateToPathAndDo(lib,path,cbi->cbi.withMember(mwtU));
    
  }

  private static Mdf mdfU(Mdf mdf1, Mdf mdf2) {
    if(mdf1==Mdf.Capsule && mdf2==Mdf.Capsule){return null;}
    if(mdf1==mdf2){return mdf1;}
    if(mdf1==Mdf.Type){return mdf2;}
    if(mdf2==Mdf.Type){return mdf1;}
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
