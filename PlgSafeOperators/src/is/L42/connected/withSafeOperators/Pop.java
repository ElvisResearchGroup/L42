package is.L42.connected.withSafeOperators;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;
import coreVisitors.CloneWithPath;
import coreVisitors.From;
import coreVisitors.FromInClass;
import is.L42.connected.withSafeOperators.ExtractInfo.IsUsed;
import platformSpecific.javaTranslation.Resources;

class Pop {
  //error conditions:
  //Outer0 is not a box
  //there is  not exactly 1 member
  static ClassB pop(ClassB cb) throws Resources.Error/*BoxError,AmbiguousPop*/{
    boolean rightSize=cb.getMs().size()==1;
    ExtractInfo.checkBox(cb,cb,Collections.emptyList());
    if(!rightSize){throw Errors42.errorAmbiguousPop(cb); }
    return directPop(cb);
  }

  static ClassB directPop(ClassB cb) {
    cb=(ClassB)new PopNFrom(1).visit(cb);
    ClassB.NestedClass nc = (ClassB.NestedClass)cb.getMs().get(0);
    ClassB res=(ClassB)nc.getInner();
    return res;
  }

  static class PopNFrom extends CloneWithPath{
    public PopNFrom(int n){this.n=n;}
    public final int n;
    public ExpCore visit(Path s) {
      if(s.isPrimitive()){return s;}
      return popN(n,s);
      }

    private ExpCore popN(int n,Path s) {
      assert n>=0;
      if (n==0){return s;}
      if (s.outerNumber()==0){return s;}
      int nLessK=s.outerNumber() - getClassNamesPath().size();
      if(nLessK>0){//is looking out
        return popN(n-1,s.setNewOuter(s.outerNumber()-1));
      }
      if(nLessK<0){//is looking in
        return popN(n-1,s);
      }
      //is extacly looking at top level
      assert !s.getCBar().isEmpty():
        s;
      return popN(n-1,Path.outer(s.outerNumber()-1,s.getCBar().subList(1, s.getCBar().size())));
    }
  }
}
