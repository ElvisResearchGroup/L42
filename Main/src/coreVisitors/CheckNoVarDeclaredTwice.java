package coreVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import ast.ExpCore;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.*;

public class CheckNoVarDeclaredTwice{
  public static boolean of(ClassB e){
    of(e,Collections.emptyList());
    return true;
  }
  public static void of(ExpCore e,List<String>names){
    Local cdv=new Local();
    cdv.xs.addAll(names);
    cdv.xs.add("this");
    e.accept(cdv);
  }

  private static class Local extends HB{
    Local() {
      super(false);
    }
    public ExpCore visit(ClassB s) {
      for(Member m:s.getMs()){
        m.match(
          nc->{CheckNoVarDeclaredTwice.of(nc.getInner(),Collections.emptyList());return null;},
          mi->{CheckNoVarDeclaredTwice.of(mi.getInner(),mi.getS().getNames());;return null;},
          mt->{if(mt.getInner().isPresent()){CheckNoVarDeclaredTwice.of(mt.getInner().get(),mt.getMs().getNames());};return null;}
          );
        }
      return s;
    }
  }  
}
