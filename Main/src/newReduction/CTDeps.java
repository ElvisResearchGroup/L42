package newReduction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.L42F.CD;
import ast.L42F.Cn;
import l42FVisitors.PropagatorVisitor;

public class CTDeps {

  public static Set<Integer> of(ClassTable res, CD cd) {
    Set<Integer> acc=new HashSet<>();
    fix(res,cd,acc);
    return acc;
  }

  private static void fix(ClassTable res, CD cd, Set<Integer> acc) {
    //visit cd, add cn to set, add "new" cn to list. for all in list repeat
    List<CD> oldCd=new ArrayList<>();
    List<CD> newCd=new ArrayList<>();
    oldCd.add(cd);
    do {
      for(CD cdi:oldCd) {
        accCd(res, cdi, acc, newCd);
        }
      oldCd=newCd;
      newCd=new ArrayList<>();
      }
    while(!oldCd.isEmpty());
  }

  private static void accCd(ClassTable res, CD cd, Set<Integer> acc, List<CD> newCn) {
    new PropagatorVisitor() {
      protected void liftCn(int cn) {
        if(cn<=Cn.cnFwd.getInner()) {return;}
        Set<Integer> deps = res.get(cn).deps;
        if(deps!=null) { acc.addAll(deps); return;}
        boolean added=acc.add(cn);
        if(added) {newCn.add(res.get(cn).cd);}
        }
      }.visit(cd);
  }
}
