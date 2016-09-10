package programReduction;

import java.util.ArrayList;
import java.util.List;

import ast.Ast;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import coreVisitors.From;
import tools.Map;

class Methods {
  static private <T> List<T> push(List<T>es,T e){
    List<T>res=new ArrayList<T>(es);
    res.add(e);
    return res;
    }
  static private <T> List<T> mergeUnique(T p,List<T>before, List<T>after){
    List<T>res=new ArrayList<T>();//fix, it can be in p too
    res.add(p);
    for(T e:after){if(!before.contains(e)){res.add(e);}}
    res.addAll(after);
    return res;
    }
  List<Ast.Path> collect(Program p,List<Ast.Path> p0ps){
    return collect(p,p0ps,new ArrayList<>());
    }
  List<Ast.Path>  collect(Program p,List<Ast.Path> p0ps,List<Ast.Path> visited){
    if( p0ps.isEmpty()){return p0ps;}
    Ast.Path p0=p0ps.get(0);
    List<Ast.Path> ps=p0ps.subList(1,p0ps.size());
    if (visited.contains(p0)){
      throw new ast.ErrorMessage.CircularImplements(push(visited,p0));
      }
    ClassB l=p.extractClassB(p0);
    List<Ast.Path> recP0=collect(p.navigate(p0),l.getSupertypes(),push(visited,p0));
    recP0=Map.of(pi->From.fromP(pi,p0),recP0);
    List<Ast.Path> recPs=collect(p,ps,visited);
    return mergeUnique(p0, recP0, recPs);    
    }
  
  MethodWithType addRefine(MethodWithType mwt){
    return mwt.withMt(mwt.getMt().withRefine(true));
  }
  
//  -methods(p,P0)=M1'..Mk'
//          p(P0)={interface? implements Ps Ms} 
List<MethodWithType> methods(Program p,Ast.Path p0){
  List<Ast.Path> ps=p.extractClassB(p0).getSupertypes();
  
//          P1..Pn=collect(p,Ps), error unless forall i, p(Pi) is an interface
//          ms1..msk={ms|p(Pi)(ms) is defined}
//          forall ms in ms1..msk, there is exactly 1 j in 0..n
//            such that p(Pj)=mh e? //no refine
//          Mi= p(P0)(msi) if is of form  refine? mh e?,
//          otherwise
//          Mi=addRefine(methods(p,Pj)(msi))
//            for the smallest j in 1..k such that methods(p,Pj)(msi) of form refine? mh
//          Mi'=Mi[with e?=p(P0)(msi).e?] if defined,
//          otherwise
//          Mi'=Mi
    }
  }
