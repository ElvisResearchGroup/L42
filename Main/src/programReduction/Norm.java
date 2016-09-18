package programReduction;

import ast.Ast.NormType;
import ast.Ast.Ph;

import java.util.List;

import ast.ExpCore;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Ast;
import ast.Ast.HistoricType;
import ast.Ast.Type;
public class Norm {

  static Type nextT(Ast.Path p,MethodWithType mwt,Ast.MethodSelectorX msx){
    String x=msx.getX();
    if(x.isEmpty()){return mwt.getMt().getReturnType();}
    if(x.equals("this")){
      return new NormType(mwt.getMt().getMdf(),p,Ph.None);
      }
    int i=mwt.getMs().getNames().indexOf(x);
    assert i!=-1:
    mwt.getMs()+" but searched for "+ x+" in "+p;
    return mwt.getMt().getTs().get(i);
    }
  static ExpCore.ClassB.MethodWithType select(Ast.MethodSelector ms, List<ExpCore.ClassB.MethodWithType> list){
    for(MethodWithType mwt:list){if(mwt.getMs().equals(ms)){return mwt;}}
    return null;
  }
  static NormType resolve(Program p,Type t){
    try{
      return _resolve(p,t);//TODO: if needed check/fix force placeholders here
    }
    catch(StackOverflowError err){
      throw new ast.ErrorMessage.HistoricTypeCircularDefinition((HistoricType)t);
      }
    }
  static NormType _resolve(Program p,Type t){//may stack overflow
//-resolve(p,mdf P)=mdf P
    if( t instanceof NormType){return (NormType) t;}
    HistoricType ht=(HistoricType) t;
//-resolve(p,P::ms)=resolve(p,T)//and avoid circularity
//  methods(p,P)(ms)=refine? _ method T ms e?
    List<MethodWithType> methods = p.methods(ht.getPath());
    MethodWithType mwt = select(ht.getSelectors().get(0).getMs(),methods);
    if(mwt==null){
      //throw new ast.ErrorMessage.NormImpossible(t, cause, null);
      throw new ast.ErrorMessage.HistoricTypeNoTarget(ht,methods );
      //TODO: what about the name targetType
      }
    Type nextT=nextT(ht.getPath(),mwt,ht.getSelectors().get(0));
    NormType nextNT= _resolve(p,nextT);
    if (ht.getSelectors().size()==1){return nextNT;}
    return _resolve(p,new HistoricType(nextNT.getPath(),ht.getSelectors().subList(1, ht.getSelectors().size()) , ht.isForcePlaceholder()/*may disappear?*/));
//-resolve(p,P::ms::x)=resolve(p,T)//and avoid circularity
//  methods(p,P)(ms)=refine? _ method _ _( _ T x _) e? 
//-resolve(p,P::msx::msxs)=resolve(p,P'::msxs) //here be carefull for possible infinite recursion 
//  resolve(p,P::msx)= _ P'              
  }
}
