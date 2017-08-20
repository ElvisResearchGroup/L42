package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ast.Ast;
import ast.ExpCore;
import ast.L42F;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.EPath;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.UpdateVar;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import ast.L42F.E;
import ast.L42F.M;
import ast.L42F.T;
import ast.L42F.TX;
import auxiliaryGrammar.Functions;
import coreVisitors.Visitor;
import facade.L42;
import newTypeSystem.GuessTypeCore;
import newTypeSystem.GuessTypeCore.G;
import programReduction.Program;
import tools.Assertions;

class PG implements Visitor<E>{
  Program p;
  G gamma;
  PG(Program p,G gamma){this.p=p;this.gamma=gamma;}
  public static M header(Program p,MethodWithType mwt){
    MethodType mt=mwt.getMt();
    
    List<TX>ts=new ArrayList<>();
    if(mt.getMdf()!=Mdf.Class){
      T t = new T(mt.getMdf(),PG.liftP(p,Path.outer(0)));
      ts.add(new TX(t,"this"));
      } 
    {int i=-1;for(String n : mwt.getMs().getNames()){i+=1;
      Ast.Type t=mt.getTs().get(i);
      ts.add(new TX(PG.liftT(p,t),n));
    }}
    return new M(mt.isRefine(),PG.liftT(p,mt.getReturnType()),mwt.getMs(),ts,L42F.SimpleBody.Empty);
  }

  public static E body(Program p, MethodWithType mwt) {
  PG pg=new PG(p,G.of(GuessTypeCore.mapForMwt(mwt))); 
  E res=mwt.getInner().accept(pg);
  return res;
  }
  
  public static int liftP(Program p,Ast.Path path){
    ClassB cb=p.extractClassB(path);
    return cb.getUniqueId();
    }
  public static T liftT(Program p,Ast.Type t){
    return new T(t.getMdf(),liftP(p,t.getPath()));
    }
  public static ExpCore.Block blockX(Ast.Type t,String x, ExpCore e,ExpCore e0){
    Dec d=new Dec(false,Optional.of(t),x,e);
    return new ExpCore.Block(Doc.empty(),Collections.singletonList(d), e0,Collections.emptyList(), Position.noInfo);
    }

@Override
public E visit(EPath s) {
  return new L42F.Cn(PG.liftP(p,s.getInner()));
  }
@Override
public E visit(X s) {
  return new L42F.X(s.getInner());
}
@Override
public E visit(_void s) {
  return new L42F._void();
}
@Override
public E visit(WalkBy s) {
  throw Assertions.codeNotReachable();
}
@Override
public E visit(Signal s) {
  if(s.getInner() instanceof X){
    String x = ((X)s.getInner()).getInner();
    T t = liftT(p,s.getTypeOut());
    return new L42F.Throw(s.getKind(), x,t);
    }
  String x = Functions.freshName("throwX",L42.usedNames);
  Signal sx = s.withInner(new X(Position.noInfo,x));
  return blockX(s.getTypeIn(),x, s.getInner(),sx).accept(this);
  }

@Override
public E visit(ClassB s) {
//( class Any x=L.CN//yes, ok even if not compiled as for Collections.vector(of:P)
//Resource.LoadLib( x ) )

return null;
}
@Override
public E visit(Loop s) {
  return new L42F.Loop(s.getInner().accept(this));
  }
@Override
public E visit(UpdateVar s) {
  if(s.getInner() instanceof X){
    String x = ((X)s.getInner()).getInner();
    return new L42F.Update(s.getVar(),x);
    }
  String x = Functions.freshName("throwX",L42.usedNames);
  UpdateVar sx = s.withInner(new X(Position.noInfo,x));
  return blockX(gamma._g(s.getVar()),x, s.getInner(),sx).accept(this);
  }

@Override
public E visit(MCall s) {
// TODO Auto-generated method stub
return null;
}
@Override
public E visit(Using s) {
// TODO Auto-generated method stub
return null;
}
@Override
public E visit(Block s) {
// TODO Auto-generated method stub
return null;
}

}