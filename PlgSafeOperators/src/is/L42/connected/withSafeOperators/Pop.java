package is.L42.connected.withSafeOperators;

import ast.Ast.Path;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;
import coreVisitors.FromInClass;
import platformSpecific.javaTranslation.Resources;

class Pop {
  //error conditions:
  //Outer0 is not a box
  //there is  not exactly 1 member
  static ClassB pop(ClassB cb) throws Resources.Error/*BoxError,AmbiguousPop*/{
    boolean rightSize=cb.getMs().size()==1;
    ExtractInfo.checkBox(cb,Path.outer(0));
    if(!rightSize){throw Resources.Error.multiPartStringError(
        "AmbiguousPop",
        "numberOfNestedClasses",""+cb.getMs().size());
    }
    ClassB.NestedClass nc = (ClassB.NestedClass)cb.getMs().get(0);
    Path p=Path.outer(0).pushC(nc.getName());
    ClassB res=(ClassB)nc.getInner();
    res=(ClassB)FromInClass.of(res, p);
    return res;
  }
}
