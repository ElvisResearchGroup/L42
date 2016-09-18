package programReduction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import ast.Ast;
import ast.Ast.MethodSelector;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
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
    List<T>res=new ArrayList<T>();
    if(!after.contains(p)){res.add(p);}
    for(T e:before){if(!after.contains(e)){res.add(e);}}
    res.addAll(after);
    return res;
    }
  static List<Ast.Path> collect(Program p,List<Ast.Path> p0ps){
    return collect(p,p0ps,new ArrayList<>());
    }
  static List<Ast.Path>  collect(Program p,List<Ast.Path> p0ps,List<Ast.Path> visited){
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
  
  static MethodWithType addRefine(MethodWithType mwt){
    return mwt.withMt(mwt.getMt().withRefine(true));
  }
  
//  -methods(p,P0)=M1'..Mk'
//          p(P0)={interface? implements Ps Ms} 
  static List<MethodWithType> methods(Program p,Ast.Path p0){
    ClassB cb0=p.extractClassB(p0);
    List<Ast.Path> ps=cb0.getSupertypes();
//          P1..Pn=collect(p,Ps[from P0]), error unless forall i, p(Pi) is an interface
    List<Ast.Path> p1n=collect(p,Map.of(pi->From.fromP(pi,p0), ps));
    List<ClassB> cb1n=Map.of(pi->p.extractClassB(pi), p1n);
    {int i=-1;for(ClassB cbi:cb1n){i++;if (!cbi.isInterface()){
      throw new ast.ErrorMessage.NonInterfaceImplements(p0,p1n.get(i));}
      }}
//          ms1..msk={ms|p(Pi)(ms) is defined}
    HashMap<Ast.MethodSelector,List<ClassB.Member>> ms1k=new LinkedHashMap<>();
    for(ClassB.Member mij:cb0.getMs()){mij.match(
      nc->null,
      mi->add(true,ms1k,mi.getS(),From.from(mij,p0)),
      mt->add(true,ms1k,mt.getMs(),From.from(mij,p0))
      );}
    for(Ast.Path pi:p1n){//call the memoized methods
      for(ClassB.Member mij:p.methods(pi)){mij.match(
        nc->null,
        mi->add(false,ms1k,mi.getS(),mij),
        mt->add(false,ms1k,mt.getMs(),mij)
        );}
      }
//members in cb0 are now first (may be null), thanks to the add function
//          forall ms in ms1..msk, there is exactly 1 j in 0..n
//            such that p(Pj)(ms)=mh e? //no refine
    for(Entry<MethodSelector, List<Member>> ei: ms1k.entrySet()){
      if(!exactly1NoRefine(ei.getValue())){
        throw new ast.ErrorMessage.NotExaclyOneMethodOrigin(p0,ei.getKey(),ei.getValue());
        }
      }
    List<ClassB.MethodWithType> ms=new ArrayList<>();
//        //i comes from k in ms1..msk
//          Mi= p(P0)(msi)[from P0] if it is of form  refine? mh e?,
//          otherwise
//          Mi=addRefine(methods(p,Pj)(msi))
//            for the smallest j in 1..k such that methods(p,Pj)(msi) of form refine? mh
    for(Entry<MethodSelector, List<Member>> ei : ms1k.entrySet()){
      List<Member> memsi=ei.getValue();
      if(memsi.get(0)!=null && memsi.get(0) instanceof MethodWithType){
        ms.add((MethodWithType)memsi.get(0));continue;
        }
      ClassB.MethodImplemented mem0=(ClassB.MethodImplemented)memsi.get(0);
      for(Member mj:memsi){// 0 will fail instanceof
        if (mj==null || !(mj instanceof MethodWithType)){continue;}
//    Mi'=Mi[with e=p(P0)(msi).e[from P0]] if defined,
//    otherwise
//    Mi'=Mi
        if(mem0!=null){mj=mj.withInner(mem0.getE());}
        ms.add(addRefine((MethodWithType) mj));
        break;
        }
      }
      return ms;
    }
  static private Void add(boolean isFirst,HashMap<Ast.MethodSelector,List<ClassB.Member>> ms1k,Ast.MethodSelector ms,ClassB.Member m){
    List<Member> list = ms1k.get(ms);
    if(list==null){
      ms1k.put(ms,list=new ArrayList<>());
      if(!isFirst){list.add(null);}
      }
    list.add(m);
    return null;
    }
  static private boolean exactly1NoRefine(List<Member> memsAll) {
    int count=0;
    for(Member m:memsAll){
      if(m==null){continue;}
      if(!(m instanceof ClassB.MethodWithType)){continue;}
      ClassB.MethodWithType mwt=(ClassB.MethodWithType)m;
      if(!mwt.getMt().isRefine()){count++;}
      }
    return count==1;
    }
  }