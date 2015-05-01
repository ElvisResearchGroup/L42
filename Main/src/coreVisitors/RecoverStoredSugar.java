package coreVisitors;

import ast.Expression;
import ast.ExpCore.MCall;

public class RecoverStoredSugar extends InjectionOnSugar{
  @Override public Expression visit(MCall s) {
    if(s.getSource()!=null){
      return s.getSource();
    }
    return super.visit(s);
    }  
}
