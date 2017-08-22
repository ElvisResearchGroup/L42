package coreVisitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import tools.Assertions;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.On;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;

public class RenameVars extends CloneVisitor{
  Map<String,String> toRename;
  RenameVars(Map<String,String> toRename){this.toRename=toRename;}

  public ExpCore visit(WalkBy s) {throw Assertions.codeNotReachable();}
  public ExpCore visit(ExpCore.ClassB s) {return s;}
  public static ExpCore of(ExpCore e,Map<String,String> toRename){
    return e.accept(new RenameVars(toRename));
  }
  public ExpCore visit(X s) {
    String alt=toRename.get(s.getInner());
    if(alt==null){return s;}
    return new X(s.getP(),alt);
    }
  public ExpCore visit(Block s) {
    List<On> k = tools.Map.of(this::liftO,s.getOns());
    List<On>newK=new ArrayList<>();
    for (On on : k){
      String altK=toRename.get(on.getX());
      if(altK!=null){newK.add(on.withX(altK));}
      else{newK.add(on);}
    }
    List<Block.Dec> decs = new ArrayList<>();
    for(Block.Dec dec:s.getDecs()){
      String altxi=toRename.get(dec.getX());
      if(altxi==null){decs.add(dec);}
      else {decs.add(dec.withX(altxi));}
    }
    return new Block(s.getDoc(),tools.Map.of(this::liftDec,decs),
      lift(s.getInner()),newK,s.getP(),liftTNull(s.getTypeOut()));
  }



}
