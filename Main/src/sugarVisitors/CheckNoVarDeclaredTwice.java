package sugarVisitors;

import java.util.Collections;
import java.util.List;
import ast.Expression;
import ast.Expression.ClassB;
import ast.Expression.ClassB.Member;

//TODO: also check no varDeclaredTwice over desugared terms!

public class CheckNoVarDeclaredTwice{
  public static boolean of(Expression e){
    of(e,Collections.emptyList());
    return true;
  }
  public static void of(Expression e,List<String>names){
    Local cdv=new Local();
    cdv.xs.addAll(names);
    cdv.xs.add("this");
    e.accept(cdv);
  }
  private static class Local extends CollectDeclaredVarsAndCheckNoDeclaredTwice{
    public Expression visit(ClassB s) {//difference from superclass, now we propagate the check
      for(Member m:s.getMs()){
        m.match(
          nc->{CheckNoVarDeclaredTwice.of(nc.getInner(),Collections.emptyList());return null;},
          mi->{CheckNoVarDeclaredTwice.of(mi.getInner(),mi.getS().getNames());return null;},
          mt->{if(mt.getInner().isPresent()){CheckNoVarDeclaredTwice.of(mt.getInner().get(),mt.getMs().getNames());};return null;}
          );
      }
      return s;
    }  
  }
}
