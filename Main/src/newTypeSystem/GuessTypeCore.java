package newTypeSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodType;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.OperationDispatch;
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
import platformSpecific.fakeInternet.PluginWithPart.UsingInfo;
import programReduction.Program;
import tools.Assertions;

public class GuessTypeCore implements Visitor<Type>{
G in;
Program p;
boolean forceError;
private GuessTypeCore(Program p,G in,boolean forceError) {
  this.p=p;
  this.in=in;
  this.forceError=forceError;
}
public static Type _of(Program p,G in,ExpCore e,boolean forceError) {
  return e.accept(new GuessTypeCore(p,in,forceError));
}
@Override
public Type visit(ExpCore.EPath s) {
  return new Type(Mdf.Class,s.getInner(),Doc.empty());
}
@Override
public Type visit(X s) {
  Type t= in._g(s.getInner());
  return t;
}
@Override
public Type visit(_void s) {
  return Path.Void().toImmNT();
}
@Override
public Type visit(WalkBy s) {
  throw Assertions.codeNotReachable();
}
@Override
public Type visit(Using s) {
  try{List<Type> lt = platformSpecific.fakeInternet.OnLineCode.pluginType(p, s);      
  return lt.get(0);}
  catch(UsingInfo.NonExistantMethod nem){
    if(forceError)throw nem;
    return null;
    }
  }
@Override
public Type visit(Signal s) {
  throw Assertions.codeNotReachable();
}
@Override
public Type visit(MCall s) {
  Type former = s.getInner().accept(this);
  if(former==null){return null;}
  Path path=former.getPath();
  assert path!=null;
  if (!path.isCore()){
    if(!forceError){return null;}
    throw new ErrorMessage.MethodNotPresent(path,s.getS(),s,s.getP(),
      Collections.emptyList()
      );
    } 
  ClassB l=p.extractClassB(path);
  MethodWithType meth = (MethodWithType)l._getMember(s.getS());
  if(meth==null){
    if(!forceError){return null;}
    throw new ErrorMessage.MethodNotPresent(path,s.getS(),s,s.getP(),
      tools.Map.of(m->((MethodWithType)m).getMs(),l.mwts())
      );
    }
  return (Type) From.fromT(meth.getMt().getReturnType(),path);
  
}

public static G freshGFromMt(Program p,MethodWithType mwt){
  Map<String,Type>newG=GuessTypeCore.mapForMwt(mwt);
  return G.of(newG);
  }
public static Map<String,Type> mapForMwt(MethodWithType mwt){
  MethodType mt=mwt.getMt();
  Map<String, Type> res=new HashMap<>();
  res.put("this",new Type(mt.getMdf(),Path.outer(0),Doc.empty()));
  {int i=-1;for(String x:mwt.getMs().getNames()){i+=1;
    Type ntx=mt.getTs().get(i);
    res.put(x,ntx);
    }}
  return res;
  }
/*
public static TOutDs guessedDs(Program p, TIn in,List<Dec> toGuess){
List<Dec> res=new ArrayList<>();//G'
for(Dec di:toGuess){
  if(di.getT().isPresent()){res.add(di);continue;}
  Type nti = _guessDecType(p,in, di,true);
  assert nti!=null;
  res.add(di.with_t(nti)); 
  }
return new TOkDs(null,res,null);
}*/
public static Type _guessDecType(Program p,G in, Dec di,boolean forceError) {
  if(di.getT().isPresent()){return di.getT().get();}
  Type nti=GuessTypeCore._of(p,in, di.getInner(),forceError);
  assert nti!=null ||
          !forceError;
  if(nti==null){return null;}
  if(nti.getMdf()==Mdf.Capsule){nti=nti.withMdf(Mdf.Mutable);}
  else if(di.isVar() && TypeManipulation.fwd_or_fwdP_in(nti.getMdf())){
    if(!forceError){return null;}
    throw Assertions.codeNotReachable("d is var and inferred is fwd");
    }
  return nti;
  }


@Override
public Type visit(Block s) {
  if (!s.getOns().isEmpty()){
    throw Assertions.codeNotReachable();
    }
  G oldIn=in;
  G in2=in.add(p,s.getDecs());
  in=in2;
  try{return s.getInner().accept(this);}
  finally{in=oldIn;}
}
@Override
public Type visit(ClassB s) {
  return Path.Library().toImmNT();
}
@Override
public Type visit(Loop s) {
  throw Assertions.codeNotReachable();
}
@Override
public Type visit(UpdateVar s) {
  return Path.Void().toImmNT();
  }
@Override
public Type visit(OperationDispatch s) {
  throw Assertions.codeNotReachable();//TODO:
  }
}

