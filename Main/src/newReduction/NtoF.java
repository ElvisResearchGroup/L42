package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.ExpCore.ClassB;
import ast.ExpCore.X;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import ast.L42F;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore;
import ast.L42F.Body;
import ast.L42F.CD;
import ast.L42F.E;
import ast.L42F.Kind;
import ast.L42F.M;
import ast.L42F.SimpleBody;
import ast.L42F.T;
import coreVisitors.CloneVisitor;
import newReduction.ClassTable.Element;
import newTypeSystem.TypeManipulation;
import programReduction.Program;

public class NtoF {
  public static List<Element> libToCDs(ClassTable avoidRepeat,List<String> topName,Program p){
    List<Element> acc=new ArrayList<>();
    List<String> top=new ArrayList<>(topName);
    List<Program> ps=new ArrayList<>();
    libToCDs(avoidRepeat,top,p,acc,ps);
    for(Program pi:ps) {
      acc.add(new Element(pi,new CD(null,pi.top().getUniqueId(),null,null, null)));
      }
    return acc;
    }
  public static void libToCDs(ClassTable avoidRepeat,List<String> topName,Program p,List<Element> acc, List<Program> ps){
    ClassB top=p.top();
    int topNameSize=topName.size();
    try{
      topName.add(null);
      for(NestedClass nc:top.ns()){
        topName.set(topNameSize,nc.getName().toString());
        libToCDs(avoidRepeat,topName,p.navigate(Collections.singletonList(nc.getName())),acc,ps);
        assert topName.size()==topNameSize+1;
        }
      Element old=avoidRepeat._get(top.getUniqueId());
      if(old!=null && old.cd.getKind()!=null){return;}
      assert topName.size()==topNameSize+1;
      }
    finally{topName.remove(topNameSize);}
    Kind k=L42F.SimpleKind.Class;
    if (top.isInterface()){k=L42F.SimpleKind.Interface;}
    List<Integer>supert=tools.Map.of(t->PG.liftP(p,t.getPath()), top.getSupertypes());
    List<M> ms=Stream.concat(newFwdMeth(top.getUniqueId()),
      top.mwts().stream()
      .flatMap(m->liftM(top.isInterface(),p,(MethodWithType)m,ps))
      ).collect(Collectors.toList());
    Element res=new Element(p,new CD(null,top.getUniqueId(),null,null, null));
    if(top.getPhase()==Phase.Coherent){res=new Element(p,new CD(k,top.getUniqueId(),new ArrayList<>(topName),supert,ms));}
    acc.add(res);
    }
private static Stream<M> newFwdMeth(int id) {
MethodSelector ms0=new MethodSelector("NewFwd",-1,Collections.emptyList());
M m0=new M(false,new T(null,id),ms0,Collections.emptyList(),SimpleBody.NewFwd);
Stream<M> a=Stream.of(m0);
return a;
}
private static Stream<M> liftM(boolean isInterface,Program p, MethodWithType mwt, List<Program> ps) {
  M h=PG.header(isInterface,p, mwt);
  if(isInterface){return Stream.of(h);}//case 0
  MethodType mt=mwt.getMt();
  MethodSelector ms=mwt.getMs();
  boolean hasE=mwt.get_inner()!=null;
  boolean isClass=mt.getMdf().isClass();

  if(hasE){//cases 1 and 2
    ExpCore e=mwt.getInner();
    if(isClass){
      e=e.accept(new CloneVisitor(){
        public ExpCore visit(ExpCore.X s){
          if(s.getInner().equals("this")){
            return new ExpCore.EPath(s.getP(),Path.outer(0));
            }
          return super.visit(s);
          }
        public ExpCore visit(ClassB s) {return s;}
        });
      }
    E b=PG.body(p,mwt.with_inner(e),ps);
    return Stream.of(h.withBody(b));
    }
  //case 3
  assert !hasE;
  if(!isClass) {
    var ns=mwt.getMs().getNames();
    if(ns.isEmpty()) {return Stream.of(h.withBody(L42F.SimpleBody.Getter));}
    if(ns.size()==1 && ns.get(0).equals("that") 
        && mwt.getMt().getReturnType().equals(Type.immVoid)) {
      return Stream.of(h.withBody(L42F.SimpleBody.Setter));
      }
    return Stream.of(h.withBody(new L42F.Unreachable()));
    }
  //case 4
  MethodWithType mwtNoF=mwt.withMt(mt.withTs(TypeManipulation.noFwd(mt.getTs())));
  mwtNoF=mwtNoF.withMs(PG.msOptimizedNew(ms));
  M k1=h.withBody(L42F.SimpleBody.NewWithFwd);
  M k2=PG.header(isInterface,p,mwtNoF).withBody(L42F.SimpleBody.New);
  return Stream.of(k1,k2);
  }
}