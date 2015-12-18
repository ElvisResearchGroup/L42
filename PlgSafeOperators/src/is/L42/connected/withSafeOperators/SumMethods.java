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
  public static ClassB sumMethods(ClassB lib, List<String> path, MethodSelector m1,MethodSelector m2){
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
    boolean parDisj=true;
    for(int i=1;i<m2.getNames().size();i++){
      String e2=m2.getNames().get(i);
      for( String e1:m1.getNames()){
      if(e1.equals(e2)){parDisj=false;}
    }}
    if(wrongFirstPar||mdfU==null || !parDisj){
      throw Errors42.errorParameterMismatch(path, mem1,mem2, !wrongFirstPar,mdfU!=null,parDisj);
    }
    ArrayList<Path> exU = new ArrayList<>(mt1.getExceptions());
    exU.addAll(mt2.getExceptions());
    String nameU;
    if(m1.isOperator()){nameU=m2.getName();}
    else if(m2.isOperator()){nameU=m1.getName();}
    else {nameU=m1.getName()+m2.getName();}
    ArrayList<String> psU = new ArrayList<>(m1.getNames());
    ArrayList<Type> tsU = new ArrayList<>(mt1.getTs());
    ArrayList<Doc> tdsU = new ArrayList<>(mt1.getTDocs());
    for(int i=1;i<m2.getNames().size();i++){
      psU.add(m2.getNames().get(i));
      tsU.add(mt2.getTs().get(i));
      tdsU.add(mt2.getTDocs().get(i));
    }
    MethodSelector msU=new MethodSelector(nameU,psU);
     MethodType mtU =new MethodType(
         mt1.getDocExceptions().sum(mt2.getDocExceptions()),
         mdfU,tsU,tdsU,mt2.getReturnType(),exU); 
    ClassB cbPath = Program.extractCBar(path,lib);
    MethodWithType mtConflict = null;
    for(PathMwt e:cbPath.getStage().getInherited()){
      if(e.getMwt().getMs().equals(msU)){mtConflict=e.getMwt();}
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
    MethodWithType mwtU=new MethodWithType(Doc.empty(),msU,mtU,Optional.of(eU),mem2.getP() );
    if(mtConflict!=null){
      //hard. Is satisfy one interface triky and risk to be buggy?
      Errors42.checkMethodClash(path, mwtU,mtConflict,true);//always throws
      throw  Assertions.codeNotReachable();
    }
    Optional<Member> optConflict = Program.getIfInDom(cbPath.getMs(),msU);    
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
