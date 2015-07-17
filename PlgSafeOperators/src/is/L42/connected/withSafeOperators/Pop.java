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
    ExtractInfo.checkBox(cb,Collections.emptyList());
    if(!rightSize){throw Errors42.errorAmbiguousPop(cb); }
    return directPop(cb);
  }

  static ClassB directPop(ClassB cb) {
    cb=(ClassB)new Pop1From().visit(cb);
    ClassB.NestedClass nc = (ClassB.NestedClass)cb.getMs().get(0);
    ClassB res=(ClassB)nc.getInner();
    return res;
  }

  static class Pop1From extends CloneWithPath{
    public ExpCore visit(Path s) {
      if(s.isPrimitive()){return s;}
      int nLessK=s.outerNumber() - getPath().size();
      if(nLessK>0){//is looking out
        return s.setNewOuter(s.outerNumber()-1);
      }
      if(nLessK<0){//is looking in
        return s;
      }
      //is extacly looking at top level
      assert !s.getCBar().isEmpty();
      return Path.outer(s.outerNumber()-1,s.getCBar().subList(1, s.getCBar().size()));
      }
  }
}
