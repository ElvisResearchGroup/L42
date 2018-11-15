package programReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;

import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.WellFormednessCore;
import coreVisitors.From;
import facade.L42;
import tools.Map;

public abstract class Methods implements Program{
  static private  List<Ast.Path> push(List<Ast.Path>ps,Ast.Path newThis){
    List<Ast.Path>res=new ArrayList<>();
    res.add(Path.outer(0));
    for(Ast.Path pi:ps){res.add(From.fromP(pi,newThis));}
    return res;
    }

  //We use Type instead of Path to keep the Docs
  public static List<Type> collect(Program p,List<Type> p0ps){
    List<Type> res=collectAux(p,p0ps,new ArrayList<>());
    assert noDuplicates(p,res);
    return res;
    }
  private static boolean noDuplicates(Program p,List<Type> res) {
    List<Type> res2=new ArrayList<>();
    for(Type t:res)out:{
      for(Type t2:res2){
        if(p.equiv(t.getPath(),t2.getPath())){break out;}
        }
      res2.add(t);
      }
    return res2.size()==res.size();
  }
  static List<Type>  collectAux(Program p,List<Type> p0ps,List<Path> visited){
    if( p0ps.isEmpty()){return p0ps;}
    Type p0=p0ps.get(0);
    List<Type> ps=p0ps.subList(1,p0ps.size());
    if (visited.contains(p0.getPath())){
      throw new ast.ErrorMessage.CircularImplements(push(visited,p0.getPath()));
      }
    if(p0.getPath().isPrimitive()){return collectAux(p,ps,visited);}
    return collectAux(p,p0,ps,visited);
    }
  static List<Type>  collectAux(Program p,Type p0, List<Type> ps,List<Path> visited){
    ClassB l=p.extractClassB(p0.getPath());
    if(!l.isInterface()) {
      throw new ast.ErrorMessage.NonInterfaceImplements(Path.outer(0),p0.getPath());
      }
    List<Type>psPrime=collectAux(p,Map.of(pi->pi.withPath(From.fromP(pi.getPath(),p0.getPath())),l.getSupertypes()),Functions.push(visited,p0.getPath()));
    List<Type>collectedPs=collectAux(p,ps,visited);
    List<Type>res=new ArrayList<>();
    if(!equivInList(p,p0,collectedPs)) {res.add(p0);}
    for(Type pi:psPrime) {if(!equivInList(p,pi,collectedPs)) {res.add(pi);}}
    res.addAll(collectedPs);
    return res;
    }
  static boolean equivInList(Program p,Type pi,List<Type>equivCandidates) {
    for(Type pEq:equivCandidates) {if(p.equiv(pi.getPath(), pEq.getPath())) {return true;}}
    return false;
    }

  static MethodWithType addRefine(MethodWithType mwt){
    return mwt.withMt(mwt.getMt().withRefine(true));
    }
  
  private HashMap<Ast.Path,List<MethodWithType>> methodsCache=new HashMap<>();
//  -methods(p,P0)=M1'..Mk'
//          p(P0)={interface? implements Ps Ms}
  public List<MethodWithType> methods(Ast.Path p0){
    if(!L42.memoizeMethods){return methodsNoCache(p0);}
    List<MethodWithType> res=methodsCache.get(p0);
    if(res!=null){return res;}
    res=methodsNoCache(p0);
    methodsCache.put(p0, res);
    return res;
    }

  private List<MethodWithType> methodsNoCache(Ast.Path p0){
    Program p=this;
    ClassB cb0=p.extractClassB(p0);
    List<Ast.Type> ps=cb0.getSupertypes();
//          P1..Pn=collect(p,Ps[from P0]), error unless forall i, p(Pi) is an interface
    List<Ast.Type> p1n=collect(p,Map.of(pi->pi.withPath(From.fromP(pi.getPath(),p0)), ps));
    assert Map.of(pi->p.extractClassB(pi.getPath()), p1n).stream().allMatch(ClassB::isInterface);
//          s1..sk={s|p(Pi)(s) is defined}
    HashMap<Ast.MethodSelector,List<ClassB.Member>> ms1k=new LinkedHashMap<>();
    for(ClassB.Member mij:cb0.getMs()){mij.match(
      nc->null,
      mi->add(true,ms1k,mi.getS(),From.from(mij,p0)),
      mt->add(true,ms1k,mt.getMs(),From.from(mij,p0))
      );}
    for(Ast.Type pi:p1n){//call the memoized methods
      for(ClassB.Member mij:p.methods(pi.getPath())){mij.match(
        nc->null,
        mi->{assert false; return null;},//add(false,s1k,mi.getS(),mij),
        mt->{assert mt.get_inner()==null; return add(false,ms1k,mt.getMs(),mij);}
        );}
      }
//members in cb0 are now first (may be null), thanks to the add function
//          forall s in s1..sk, there is exactly 1 j in 0..n
//            such that p(Pj)(s)=mh e? //no refine
    for(Entry<MethodSelector, List<Member>> ei: ms1k.entrySet()){
      if(!exactly1NoRefine(ei.getValue())){
        throw new ast.ErrorMessage.NotExaclyOneMethodOrigin(p0,ei.getKey(),ei.getValue());
        }
      }
    List<ClassB.MethodWithType> ms=new ArrayList<>();
//        //i comes from k in s1..sk
//          Mi= p(P0)(si)[from P0] if it is of form  refine? mh e?,
//          otherwise
//          Mi=addRefine(methods(p,Pj)(si))
//            for the smallest j in 1..k such that methods(p,Pj)(si) of form refine? mh
    for(Entry<MethodSelector, List<Member>> ei : ms1k.entrySet()){
      List<Member> memsi=ei.getValue();
      if(memsi.get(0)!=null && memsi.get(0) instanceof MethodWithType){
        ms.add((MethodWithType)memsi.get(0));continue;
        }
      ClassB.MethodImplemented mem0=(ClassB.MethodImplemented)memsi.get(0);
      for(Member mjMember:memsi){// 0 will fail instanceof
        if (mjMember==null || !(mjMember instanceof MethodWithType)){continue;}
        MethodWithType mj=(MethodWithType)mjMember;
        if(mem0!=null){mj=addEToAbstractMethod(mj,mem0.getE());}
        ms.add(addRefine((MethodWithType) mj));
        break;
        }
      }
      return ms;
    }
  static private MethodWithType addEToAbstractMethod(MethodWithType mj, ExpCore e){
    //HERE we create a mj with an expression that is not guaranteed to be well formed w.r.t. capsule variables used only onece.
    List<String>capsPar=new ArrayList<>();
    {int i=-1;for(Type ti:mj.getMt().getTs()){i+=1;
      if(!ti.getMdf().equals(Mdf.Capsule)){continue;}
      capsPar.add(mj.getMs().getNames().get(i));
    }}
    if(capsPar.isEmpty()){return mj.withInner(e);}
    List<String> xs=WellFormednessCore.countX(e);
    for(String s:capsPar){
      xs.remove(s);
      if(!xs.contains(s)){continue;}
      throw new ErrorMessage.CapsuleUsedMoreThenOne(null,s,mj.getP());
      }
    return mj.withInner(e);
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