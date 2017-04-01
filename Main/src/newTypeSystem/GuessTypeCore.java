package newTypeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelectorX;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import auxiliaryGrammar.Functions;
import coreVisitors.Visitor;
import tools.Assertions;

public class GuessTypeCore implements Visitor<NormType>{
TIn in;
private GuessTypeCore(TIn in) {
  this.in=in;
}
public static NormType of(TIn in,ExpCore e) {
  return e.accept(new GuessTypeCore(in));
}
@Override
public NormType visit(ExpCore.EPath s) {
  return new NormType(Mdf.Class,s.getInner(),Doc.empty());
}
@Override
public NormType visit(X s) {
  NormType t= in.g.get(s.getInner());
  assert t!=null;
  return t;
}
@Override
public NormType visit(_void s) {
  return Path.Void().toImmNT();
}
@Override
public NormType visit(WalkBy s) {
  throw Assertions.codeNotReachable();
}
@Override
public NormType visit(Using s) {
  throw Assertions.codeNotReachable();
}
@Override
public NormType visit(Signal s) {
  throw Assertions.codeNotReachable();
}
@Override
public NormType visit(MCall s) {
  Path path=s.getInner().accept(this).getPath();
  assert path!=null;
  List<MethodSelectorX> msl=new ArrayList<>();
  MethodSelectorX msx=new MethodSelectorX(s.getS(), "");
  msl.add(msx); 
  Type t=new Ast.HistoricType(path,msl,Doc.empty());
  NormType nt=programReduction.Norm.resolve(this.in.p,t);
  return nt;
}
@Override
public NormType visit(Block s) {
  if (!s.getOns().isEmpty()){throw Assertions.codeNotReachable();}
  TIn oldIn=in.addGds(in.p,s.getDecs());
  TIn in2=in.addGds(in.p,s.getDecs());
  in=in2;
  try{return s.getInner().accept(this);}
  finally{in=oldIn;}
}
@Override
public NormType visit(ClassB s) {
  return Path.Library().toImmNT();
}
@Override
public NormType visit(Loop s) {
  throw Assertions.codeNotReachable();
}
}

