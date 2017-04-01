package coreVisitors;

import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;

public class TestShapeVisitor implements Visitor<Boolean>{
  public Boolean visit(ExpCore.EPath s) {return false;}
  public Boolean visit(X s) {return false;}
  public Boolean visit(_void s)  {return false;}
  public Boolean visit(WalkBy s)  {return false;}
  public Boolean visit(Using s)  {return false;}
  public Boolean visit(Signal s)  {return false;}
  public Boolean visit(MCall s)  {return false;}
  public Boolean visit(Block s)  {return false;}
  public Boolean visit(ClassB s)  {return false;}
  public Boolean visit(Loop s)  {return false;}
}
