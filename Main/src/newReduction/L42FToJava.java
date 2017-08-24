package newReduction;

import java.util.List;

import ast.L42F.Block;
import ast.L42F.BreakLoop;
import ast.L42F.Call;
import ast.L42F.Cast;
import ast.L42F.Cn;
import ast.L42F.D;
import ast.L42F.If;
import ast.L42F.K;
import ast.L42F.Loop;
import ast.L42F.Null;
import ast.L42F.Throw;
import ast.L42F.Update;
import ast.L42F.Use;
import ast.L42F.X;
import ast.L42F._void;
import auxiliaryGrammar.Functions;
import facade.L42;

public class L42FToJava extends L42FStringer{
  public L42FToJava(ClassTable ct) {super(ct);}

@Override
public Void visit(Block s) {
  String label=Functions.freshName("label",L42.usedNames);
  c(label+":{");
  for(D di:s.getDs()){liftDsTX(di);}
  if(s.getKs().isEmpty()){
    for(D di:s.getDs()){liftDsXE(di);}
    }
  else{
    c("try{");
    for(D di:s.getDs()){liftDsXE(di);}
    c("}");
    liftKs(s.getKs());
    }
  s.getE().accept(this);
  return c("}");
  }
private void liftKs(List<K> ks) {
// TODO Auto-generated method stub

}

private void liftDsXE(D di) {
// TODO Auto-generated method stub

}

private void liftDsTX(D di) {
// TODO Auto-generated method stub

}

@Override
public Void visit(X s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(Cn s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(_void s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(Null s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(BreakLoop s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(Throw s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(Loop s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(Call s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(Use s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(If s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(Update s) {
// TODO Auto-generated method stub
return null;
}

@Override
public Void visit(Cast s) {
// TODO Auto-generated method stub
return null;
}

}
