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
import ast.L42F;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.ExpCore;
import ast.L42F.Body;
import ast.L42F.CD;
import ast.L42F.E;
import ast.L42F.Kind;
import ast.L42F.M;
import coreVisitors.CloneVisitor;
import newTypeSystem.TypeManipulation;
import programReduction.Program;

public class NtoF {
  public static List<L42F.CD> libToCDs(List<String> topName,Program p){
    List<L42F.CD> acc=new ArrayList<>();
    List<String> top=new ArrayList<>(topName);
    libToCDs(top,p,acc);
    return acc;
    }
  public static void libToCDs(List<String> topName,Program p,List<L42F.CD> acc){
    ClassB top=p.top();
    topName.add(null);
    for(NestedClass nc:top.ns()){
      topName.set(topName.size()-1,nc.getName().toString());
      libToCDs(topName,p.navigate(Collections.singletonList(nc.getName())),acc);
      }
    topName.remove(topName.size()-1);
    Kind k=L42F.SimpleKind.Class;
    if (top.isInterface()){k=L42F.SimpleKind.Interface;}
    List<Integer>supert=tools.Map.of(t->PG.liftP(p,t.getPath()), top.getSupertypes());
    List<M> ms=top.mwts().stream()
      .flatMap(m->liftM(top.isInterface(),p,(MethodWithType)m))
      .collect(Collectors.toList());
    CD res=new CD(k,top.getUniqueId(),new ArrayList<>(topName),supert,ms);
    acc.add(res);
    }
private static Stream<M> liftM(boolean isInterface,Program p, MethodWithType mwt) {
  M h=PG.header(p, mwt);
  if(isInterface){return Stream.of(h);}//case 0
  MethodType mt=mwt.getMt();
  MethodSelector ms=mwt.getMs();
  boolean hasE=mwt.get_inner().isPresent();
  boolean isClass=mt.getMdf()==Mdf.Class;
  
  if(hasE){//cases 1 and 2
    ExpCore e=mwt.getInner();
    if(isClass){
      e=e.accept(new CloneVisitor(){public ExpCore visit(ExpCore.X s){
        return new ExpCore.EPath(s.getP(),Path.outer(0));
        }});
      }
    E b=PG.body(p,mwt.with_inner(Optional.of(e)));
    return Stream.of(h.withBody(b));       
    }
  //case 3
  Body b=L42F.SimpleBody.Setter;
  if(ms.getNames().isEmpty()){b=L42F.SimpleBody.Getter;}
  if(!isClass){return Stream.of(h.withBody(b));}
  //case 4
  MethodWithType mwtNoF=mwt.withMt(mt.withTs(TypeManipulation.noFwd(mt.getTs())));
  mwtNoF=mwtNoF.withMs(ms.withName("New_"+ms.getName()));
  M k1=h.withBody(L42F.SimpleBody.NewFwd);
  M k2=PG.header(p,mwtNoF).withBody(L42F.SimpleBody.New);
  return Stream.of(k1,k2);
  }
}