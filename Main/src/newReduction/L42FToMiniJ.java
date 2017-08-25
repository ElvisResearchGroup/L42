package newReduction;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.SignalKind;
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
import ast.MiniJ;
import ast.MiniJ.*;
import auxiliaryGrammar.Functions;
import facade.L42;
import l42FVisitors.Visitor;

public class L42FToMiniJ implements Visitor<MiniJ.S>{
  @Override
  public MiniJ.S visit(Block s) {
    String label=Functions.freshName("label",L42.usedNames);
    List<S>ds=new ArrayList<>();
    for(D di:s.getDs()){ds.add(liftDsTX(di));}
    if(s.getKs().isEmpty()){
      for(D di:s.getDs()){ds.add(liftDsXE(di));}
      }
    else{
      List<S>dsTry=new ArrayList<>();
      if(s.getKs().isEmpty()){
        for(D di:s.getDs()){dsTry.add(liftDsXE(di));}
        }
      List<MiniJ.K> ks=liftKs(s.getKs());
      Try t=new Try(new B(null,dsTry),ks);
      ds.add(t);
      }
    ds.add(s.getE().accept(this));
    return new B(label,ds);
    }
  private List<MiniJ.K> liftKs(List<K> ks) {
    List<K> errs=new ArrayList<K>();
    List<K> excs=new ArrayList<K>();
    List<K> rets=new ArrayList<K>();
    for(K k:ks) {
      if (k.getKind()==SignalKind.Error) {errs.add(k);}
      if (k.getKind()==SignalKind.Exception) {excs.add(k);}
      if (k.getKind()==SignalKind.Return) {rets.add(k);}
      }
    }
  private MiniJ.K _liftKs(SignalKind kind,List<K> ks) {
    String catchX=Functions.freshName("catchX",L42.usedNames);
    MiniJ.K res=new MiniJ.K(kind, catchX, b)

    }

  private S liftDsXE(D di) {

  // TODO Auto-generated method stub

  }

  private S liftDsTX(D di) {
  // TODO Auto-generated method stub

  }

  @Override
  public MiniJ.S visit(X s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(Cn s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(_void s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(Null s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(BreakLoop s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(Throw s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(Loop s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(Call s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(Use s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(If s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(Update s) {
  // TODO Auto-generated method stub
  return null;
  }

  @Override
  public MiniJ.S visit(Cast s) {
  // TODO Auto-generated method stub
  return null;
  }


}
