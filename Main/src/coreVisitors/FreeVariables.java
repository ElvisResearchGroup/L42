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
  public static Set<String> of(ExpCore e){
    return e.accept(new FreeVariables());
  } 
  public Set<String> visit(X s) {
    collected.add(s.getInner());
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
    return s.getReceiver().accept(this);
  }

  private  void visitK(List<On> k) {
    for(On on:k){
      on.getInner().accept(this);
      collected.remove(on.getX());//TODO: bug, could remove a x used before
      }    
    }
    
  @Override
  public Set<String> visit(Block s) {
    for(Block.Dec di:s.getDecs()){
      di.getE().accept(this);
      }
    if(!s.getOns().isEmpty()){visitK(s.getOns());}
    Set<String> res = s.getInner().accept(this);
    for(Block.Dec di:s.getDecs()){
      collected.remove(di.getX());
      }
    return res;
  }
  
  public Set<String> visit(WalkBy s) {throw Assertions.codeNotReachable();}
  public Set<String> visit(_void s) {return collected;}
  public Set<String> visit(Path s) {return collected;}
  public Set<String> visit(ClassB s) {return collected;}
  
}
