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
import auxiliaryGrammar.WellFormednessCore;
import coreVisitors.From;
import tools.Map;

public abstract class Methods implements Program{
  static private  List<Ast.Path> push(List<Ast.Path>ps,Ast.Path newThis){
    List<Ast.Path>res=new ArrayList<>();
    res.add(Path.outer(0));
    for(Ast.Path pi:ps){res.add(From.fromP(pi,newThis));}
    return res;
    }
  static private  List<Ast.Type> mergeUnique(Ast.Type p,List<Ast.Type>before, List<Ast.Type>after){
    List<Ast.Type>res=new ArrayList<>();
    if(!after.contains(p)){res.add(p);}
    for(Ast.Type e:before){
      if(!after.contains(e)){res.add(e);}
      }
    res.addAll(after);
    //Note: we are not coping comment from "before".
    //It would be very hard to do it and keep norm "stable"
    return res;
    }
  public static List<Type> collect(Program p,List<Type> p0ps){
    List<Type> res = collect(p,p0ps,new ArrayList<>());
    List<Type> res2=new ArrayList<>();
    for(Type t:res)out:{
      for(Type t2:res2){
        if(p.equiv(t.getPath(),t2.getPath())){break out;}
        }
      res2.add(t);
      }
    return res2;
    //TODO: do we need somehow to remove duplicates?
    }
  static List<Type>  collect(Program p,List<Type> p0ps,List<Path> visited){
    if( p0ps.isEmpty()){return p0ps;}
    Ast.Type p0=p0ps.get(0);
    List<Ast.Type> ps=p0ps.subList(1,p0ps.size());
    if (visited.contains(p0.getPath())){
      throw new ast.ErrorMessage.CircularImplements(push(visited,p0.getPath()));
      }
    if(p0.getPath().isPrimitive()){return collect(p,ps,visited);}
    ClassB l=p.extractClassB(p0.getPath());
    if(!l.isInterface()) {
      assert false: "should be always discovered by methdos(path)?";
      throw new ast.ErrorMessage.NonInterfaceImplements(Path.outer(0), p0.getPath());
      }
    List<Ast.Type>superPaths=l.getSupertypes();
    List<Ast.Type> recP0=collect(p.navigate(p0.getPath()),superPaths,push(visited,p0.getPath()));
    recP0=Map.of(pi->pi.withPath(From.fromP(pi.getPath(),p0.getPath())),recP0);
    List<Ast.Type> recPs=collect(p,ps,visited);
    return mergeUnique(p0, recP0, recPs);
    }

  static MethodWithType addRefine(MethodWithType mwt){
    return mwt.withMt(mwt.getMt().withRefine(true));
  }

//  -methods(p,P0)=M1'..Mk'
//          p(P0)={interface? implements Ps Ms}
  public List<MethodWithType> methods(Ast.Path p0){
    if (p0.isPrimitive()){return Collections.emptyList();}
    Program p=this;
    ClassB cb0=p.extractClassB(p0);
    List<Ast.Type> ps=cb0.getSupertypes();
//          P1..Pn=collect(p,Ps[from P0]), error unless forall i, p(Pi) is an interface
    List<Ast.Type> p1n=collect(p,Map.of(pi->pi.withPath(From.fromP(pi.getPath(),p0)), ps));
    List<ClassB> cb1n=Map.of(pi->p.extractClassB(pi.getPath()), p1n);
    {int i=-1;for(ClassB cbi:cb1n){i++;if (!cbi.isInterface()){
      throw new ast.ErrorMessage.NonInterfaceImplements(p0,p1n.get(i).getPath());}
      }}
//          ms1..msk={ms|p(Pi)(ms) is defined}
    HashMap<Ast.MethodSelector,List<ClassB.Member>> ms1k=new LinkedHashMap<>();
    for(ClassB.Member mij:cb0.getMs()){mij.match(
      nc->null,
      mi->add(true,ms1k,mi.getS(),From.from(mij,p0)),
      mt->add(true,ms1k,mt.getMs(),From.from(mij,p0))
      );}
    for(Ast.Type pi:p1n){//call the memoized methods
      for(ClassB.Member mij:p.methods(pi.getPath())){mij.match(
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