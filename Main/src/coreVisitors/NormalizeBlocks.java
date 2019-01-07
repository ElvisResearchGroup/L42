package coreVisitors;

import java.util.Optional;
import java.util.Set;

import tools.Map;
import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;

public class NormalizeBlocks extends CloneVisitor{
  
  private Dec isTxEx(Block s){
    if(!s.getOns().isEmpty()){return null;}
    if(s.getDecs().size()!=1){return null;}
    Dec dec=s.getDecs().get(0);
    if(!(s.getInner() instanceof ExpCore.X)){return null;}
    String x=((ExpCore.X)s.getInner()).getInner();
    if(!x.equals(dec.getX())){return null;}
    return dec;       
    }
  public ExpCore visit(Block s) {
    //I think reduction take care of catch already
    if(s.getOns().isEmpty()&& s.getDecs().size()==0){ //(e)-->e
      return s.getInner().accept(this);
    }
    Dec decOut=this.isTxEx(s);
    if(decOut!=null && decOut.getInner() instanceof Block){//(T x=(T y= e y) x)->(T y= e y)
      Dec decIn=this.isTxEx((Block)decOut.getInner());
      if(validDoubleWrap(decOut,decIn)){return decIn.getInner().accept(this);}
    }
    return super.visit(s);
    }

  private boolean validDoubleWrap(Dec decOut, Dec decIn) {
    if(decIn==null){return false;}
    if(decOut.getT().equals(decIn.getT())){
      Set<String> fvIn = coreVisitors.FreeVariables.of(decIn.getInner());
      if(fvIn.contains(decOut.getX())){return false;}
      return true;//to put breakpoint
    }
    return false;
  }
  public static ExpCore of(ExpCore e) {
    return e.accept(new NormalizeBlocks());
  }  
}
