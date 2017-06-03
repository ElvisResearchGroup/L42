package auxiliaryGrammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.ExpCore;
import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.Ast.NormType;
import ast.ErrorMessage;
import ast.ExpCore.Block;
import ast.ExpCore.X;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import coreVisitors.CollectPaths0;
import newTypeSystem.TypeManipulation;
import tools.Assertions;
import ast.Ast.MethodType;
import ast.Ast.NormType;

public class WellFormednessCore {
  public static boolean methodTypeWellFormed(MethodType mt){
  boolean r1=false;
  boolean r2=false;
  //if exists fwdImm _ in Ts then (return type).mdf in {mut, fwdMut, imm, fwdImm}
  //if exists fwdMut _ in Ts then (return type).mdf in {mut, fwdMut}
  for(NormType t:mt.getTs()){
    Mdf m=t.getNT().getMdf();
    if(m==Mdf.ImmutableFwd){r1=true;}
    if(m==Mdf.MutableFwd){r2=true;}
    }
  Mdf m=mt.getReturnType().getNT().getMdf();
  if(r2){
   if(m!=Mdf.Mutable && m!=Mdf.MutableFwd && m!=Mdf.MutablePFwd){return false;}
  }
  else if(r1){
    if(m!=Mdf.Mutable && m!=Mdf.Immutable && !TypeManipulation.fwd_or_fwdP_in(m)){return false;}
  }
  //TODO: do we want this extra restriction?
  if(!r1&&!r2){//no fwd at all
    if(TypeManipulation.fwd_or_fwdP_in(m)){return false;}
    }
  return true;
  }
  //should replicate some of the other well formedness for sugared? even if already checked??
  public static void capsuleOnlyOnce(ExpCore.ClassB.NestedClass nc){
    countX(nc.getInner());
    }
  public static void capsuleOnlyOnce(ExpCore.ClassB.MethodWithType mwt){
    List<String> res = countX(mwt.getInner());
    {int i=-1;for(NormType ti:mwt.getMt().getTs()){i++;
      if(ti.getNT().getMdf()!=Mdf.Capsule){continue;}
      String capsX=mwt.getMs().getNames().get(i);
      if(Collections.frequency(res,capsX)<=1){continue;}
      throw new ErrorMessage.CapsuleUsedMoreThenOne(null,capsX,mwt.getP());
      }}
    if(mwt.getMt().getMdf()==Mdf.Capsule){
      if(Collections.frequency(res,"this")>1){
        throw new ErrorMessage.CapsuleUsedMoreThenOne(null,"this",mwt.getP());
        }
      }
    }
  public static List<String> countX(ExpCore e){
    return CX.of(e);
    }
  private static class CX extends coreVisitors.PropagatorVisitor{
    protected List<String> res=new ArrayList<>();
    public static List<String> of(ExpCore e){
      CX cp=new CX();
      e.accept(cp);
      return cp.res;
      }
    public Void visit(X s) {
      res.add(s.getInner());
      return super.visit(s);
      }
    public Void visit(ExpCore.Loop s) {
      super.visit(s);//do it twice
      return super.visit(s);
      }
    public Void visit(Block s) {
      List<String> ys=new ArrayList<>();
      for(Dec d:s.getDecs()){ys.addAll(CX.of(d.getInner()));}
      List<List<String>> xsi=new ArrayList<>();
      xsi.add(CX.of(s.getInner()));
      for(On o:s.getOns()){xsi.add(CX.of(o.getInner()));}
      for(Dec d:s.getDecs()){
        if(d.getT().get().getNT().getMdf()!=Mdf.Capsule){continue;}
        String xi=d.getX();
        //xi at most 1 in ys,xs0
        int howMany_xs0=Collections.frequency(xsi.get(0),xi);
        int howMany_ys=Collections.frequency(ys,xi);
        if(howMany_xs0+howMany_ys<=1){continue;}
        throw new ErrorMessage.CapsuleUsedMoreThenOne(null,xi,s.getP());
        }
      for(Dec d:s.getDecs()){
        ys.removeIf(d.getX()::equals);
        xsi.get(0).removeIf(d.getX()::equals);
      }
      ys.addAll(xsi.stream().reduce((xs1,xs2)->max(xs1,xs2)).get());
      this.res.addAll(ys);
      return null;
      }

 
  }
  
  public static List<String> max(List<String> l1,List<String>l2){
    if(l1.isEmpty()){return l2;}
    if(l2.isEmpty()){return l1;}
    List<String>res=new ArrayList<>(l2);
    for(String s:l1){
      res.remove(s);//removes the leftmost if any
      }
    res.addAll(l1);
    return res;
    }
  }
