package coreVisitors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tools.Assertions;
import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.Block;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;

public class FreeVariables implements Visitor<Set<String>>{
  private HashSet<String> collected=new HashSet<String>();
  private HashSet<String> inScope=new HashSet<String>();
  public static Set<String> of(ExpCore e){
    return e.accept(new FreeVariables());
  } 
  public static Set<String> ofBlock(Block s){
    return new FreeVariables().inBlock(s);
  } 
  public Set<String> visit(X s) {
    if(!inScope.contains(s.getInner())){
      collected.add(s.getInner());
      }
    return collected;
  }
  public Set<String> visit(Using s) {
    for(ExpCore e:s.getEs()){e.accept(this);}
    return s.getInner().accept(this);
  }
  public Set<String> visit(Signal s) {
    return s.getInner().accept(this);
  }
  public Set<String> visit(Loop s) {
    return s.getInner().accept(this);
  }

  @Override
  public Set<String> visit(MCall s) {
    for(ExpCore e:s.getEs()){e.accept(this);}
    return s.getInner().accept(this);
  }

  private  void visitK(List<On> k) {
    for(On on:k){
      inScope.add(on.getX());
      on.getInner().accept(this);
      inScope.remove(on.getX());
      }    
    }
    
  @Override
  public Set<String> visit(Block s) {
    for(Block.Dec di:s.getDecs()){  inScope.add(di.getX()); }
    Set<String> res = inBlock(s);
    for(Block.Dec di:s.getDecs()){  inScope.remove(di.getX()); }
    return res;
  }
  private Set<String> inBlock(Block s) {
    for(Block.Dec di:s.getDecs()){
      di.getInner().accept(this);
      }
    if(!s.getOns().isEmpty()){visitK(s.getOns());}
    Set<String> res = s.getInner().accept(this);
    return res;
  }
  
  public Set<String> visit(WalkBy s) {throw Assertions.codeNotReachable();}
  public Set<String> visit(_void s) {return collected;}
  public Set<String> visit(Path s) {return collected;}
  public Set<String> visit(ClassB s) {return collected;}
  
}
