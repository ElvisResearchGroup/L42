package newTypeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelectorX;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.NormType;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.UpdateVar;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import auxiliaryGrammar.Functions;
import coreVisitors.From;
import coreVisitors.Visitor;
import tools.Assertions;

public class GuessTypeCore implements Visitor<NormType>{
TIn in;
private GuessTypeCore(TIn in) {
  this.in=in;
}
public static NormType _of(TIn in,ExpCore e) {
  return e.accept(new GuessTypeCore(in));
}
@Override
public NormType visit(ExpCore.EPath s) {
  return new NormType(Mdf.Class,s.getInner(),Doc.empty());
}
@Override
public NormType visit(X s) {
  NormType t= in.g(s.getInner());
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
  NormType former = s.getInner().accept(this);
  if(former==null){return null;}
  Path path=former.getPath();
  assert path!=null;
  List<MethodSelectorX> msl=new ArrayList<>();
  MethodSelectorX msx=new MethodSelectorX(s.getS(), "");
  msl.add(msx); 
  MethodWithType meth = (MethodWithType)in.p.extractClassB(path)._getMember(s.getS());
  if(meth==null){return null;}
  return (NormType) From.fromT(meth.getMt().getReturnType(),path);
  
}

public static List<Dec> guessedDs(TIn in,List<Dec> toGuess){
List<Dec> res=new ArrayList<>();//G'
for(Dec di:toGuess){
  if(!di.getT().isPresent()){
    NormType nti=GuessTypeCore._of(in, di.getInner());
    if(di.isVar()){
      if(nti.getMdf()==Mdf.Capsule){nti=nti.withMdf(Mdf.Mutable);}
      else if(TypeManipulation.fwd_or_fwdP_in(nti.getMdf())){
        assert false;
      }
    }
    res.add(di.withT(Optional.of(nti)));
    }
  else{res.add(di.withT(Optional.of(di.getT().get().getNT())));}
  }
return res;
}


@Override
public NormType visit(Block s) {
  if (!s.getOns().isEmpty()){throw Assertions.codeNotReachable();}
  TIn oldIn=in;
  TIn in2=in.addGds(in.p,guessedDs(in,s.getDecs()));
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
@Override
public NormType visit(UpdateVar s) {
  return Path.Void().toImmNT();
  }
}

