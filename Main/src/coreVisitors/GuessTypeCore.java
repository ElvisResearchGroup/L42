package coreVisitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import facade.Configuration;
import tools.Assertions;
import ast.Ast.Doc;
import ast.Ast.FreeType;
import ast.Ast.HistoricType;
import ast.Ast.MethodSelector;
import ast.Ast.MethodSelectorX;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import ast.Ast.Type;

public class GuessTypeCore implements Visitor<Path>{
  Program p;
  HashMap<String, Type> varEnv;
  
  private GuessTypeCore(Program p, HashMap<String, Type> varEnv) {
    this.p = p; this.varEnv = varEnv;
  }
  public static Path of(Program p, HashMap<String, Type> varEnv,ExpCore e) {
    return e.accept(new GuessTypeCore(p,varEnv));
  }
  @Override
  public Path visit(Path s) {
    return s;
  }
  @Override
  public Path visit(X s) {
    assert varEnv.containsKey(s.getInner());
    Type t= varEnv.get(s.getInner());
    return Norm.of(p,t ).getPath();
  }
  @Override
  public Path visit(_void s) {
    return Path.Void();
  }
  @Override
  public Path visit(WalkBy s) {
    throw Assertions.codeNotReachable();
  }
  @Override
  public Path visit(Using s) {
    return s.getInner().accept(this);
  }
  @Override
  public Path visit(Signal s) {
    return null;//ok, it works as a form of FreeType here
  }
  @Override
  public Path visit(MCall s) {
    Path path=s.getInner().accept(this);
    if(path==null){return null;}
    List<MethodSelectorX> msl=new ArrayList<>();
    MethodSelectorX msx=new MethodSelectorX(s.getS(), "");
    msl.add(msx); 
    Type t=new Ast.HistoricType(path,msl,Doc.empty());
    t=Norm.of(p,t);
    assert t instanceof NormType;
    NormType nt=(NormType)t;
    return nt.getPath();
  }
  @Override
  public Path visit(Block s) {
    HashMap<String,Type> aux = new HashMap<>(this.varEnv);
    try{
      for(Dec d:s.getDecs()){
        this.varEnv.put(d.getX(),d.getT());
        }
      HashSet<Path> ps=new HashSet<>();
      if(!s.getOns().isEmpty()){
        for(On on:s.getOns()){
          NormType nti=Functions.forceNormType(p,s,on.getT());
          this.varEnv.put(on.getX(),nti);
          ps.add(on.getInner().accept(this));
          this.varEnv.remove(on.getX());
          }
        }
      ps.add(s.getInner().accept(this));
      ps.remove(null);
      if(ps.isEmpty()){return null;}
      if(ps.size()==1){return ps.iterator().next();}
      Path candidate=null;
      outer:for(Path pc:ps){
        candidate=pc;
        for(Path pi:ps){
          if(pc==pi){continue;}
          if(!Functions.isSubtype(p, pi, pc)){candidate=null;continue outer;}
          }
        break outer;
        }
      if(candidate==null){return Path.Any();}
      return candidate;
      }
    finally{this.varEnv=aux;}
  }
  @Override
  public Path visit(ClassB s) {
    return Path.Library();
  }
  @Override
  public Path visit(Loop s) {
    return Path.Void();
  }
}
