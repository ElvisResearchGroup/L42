package is.L42.connected.withSafeOperators;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.PathMwt;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

import java.util.*;
public class SumMethods {
  ClassB sumMethods(ClassB lib, List<String> path, MethodSelector m1,MethodSelector m2){
    NestedClass nc=(NestedClass)Errors42.checkExistsPathMethod(lib, path, Optional.empty());
    Member mem1=Errors42.checkExistsPathMethod(lib, path, Optional.of(m1));
    Member mem2=Errors42.checkExistsPathMethod(lib, path, Optional.of(m2));
    MethodWithType mwt1 = Program.extractMwt(mem1,(ClassB) nc.getInner());
    MethodWithType mwt2 = Program.extractMwt(mem2,(ClassB) nc.getInner());
    MethodType mt1=mwt1.getMt();
    MethodType mt2=mwt2.getMt();
    boolean wrongFirstPar=true;
    if(!mt2.getTs().isEmpty()){
       Type p1 = mt2.getTs().get(0);
       Type r = mt1.getReturnType();
       if(p1.equals(r)){wrongFirstPar=false;}
    }
    Mdf mdfU=mdfU(mt1.getMdf(),mt2.getMdf());
    if(wrongFirstPar||mdfU==null){
      throw Errors42.errorParameterTypeMismach(path, mem1,mem2, !wrongFirstPar,mdfU!=null);
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
    if(mtConflict!=null){
      //hard. Is satisfy one interface triky and risk to be buggy?
      //throw Errors42.errorMethodClash(path, mtConflict, mtb, exc, pars, retType, thisMdf)
      //TODO:test sum sum on not implemented interface method
    }
    Optional<Member> optConflict = Program.getIfInDom(cbPath.getMs(),msU);    
    if(optConflict.isPresent()){
      //hard, should we require exact type? pre existent can have more exceptions?
    } 
    //simple
    //is it a problem if m1+m2 is a private name? is that possible?
    //wrong if
    //first par type meth2 not same a return meth1
    //mdf 1 not type, mdf1 not supertype of mdf2
    //ParameterTypeMismach
    //m1+m2 exists, not abstract or wrong signature.//methodClash

    return lib;//TODO to implement
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
