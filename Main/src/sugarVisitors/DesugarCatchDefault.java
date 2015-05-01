package sugarVisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ast.Ast;
import ast.Ast.Mdf;
import ast.Ast.On;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Type;
import ast.Expression;
import ast.Ast.Catch;
import ast.Ast.SignalKind;
import ast.Ast.VarDec;
import ast.Expression.ClassB;

public class DesugarCatchDefault extends CloneVisitor{
  Ast.Type lastReturn=null;
  public static ClassB of(ClassB s) {
    return (ClassB)s.accept(new DesugarCatchDefault());
  }
  protected ast.Ast.BlockContent liftBC(ast.Ast.BlockContent c) {
    if(!c.get_catch().isPresent()){return super.liftBC(c);}
    Catch k = c.get_catch().get();
    Optional<Catch> liftK = Optional.of(this.liftK(k));
    Type oldR=this.lastReturn;
    this.lastReturn=newR(k);
    try{
      List<VarDec> liftVarDecs = liftVarDecs(c.getDecs());
      return new ast.Ast.BlockContent(liftVarDecs,liftK);
      }
    finally{this.lastReturn=oldR;}
  }
    
  private Type newR(Catch k) {
    if(k.getKind()!=SignalKind.Return){return this.lastReturn;}
    if(k.getOns().size()!=1){return this.lastReturn;}
    return k.getOns().get(0).getTs().get(0);
    }
  
  protected ast.Ast.Catch liftK(ast.Ast.Catch k){
    if(k.get_default().isPresent()){
      Expression e=k.get_default().get();
      List<On> ons = new ArrayList<>(k.getOns());
      List<Type>ts=new ArrayList<>();
      if(k.getKind()==SignalKind.Return){
        if(this.lastReturn!=null){
          ts.add(this.lastReturn);
          }
        else{
          ts.add(new Ast.NormType(Mdf.Immutable,Path.Any(),Ph.None));
          }
        }
      else{
        assert k.getKind()==SignalKind.Exception;
        ts.add(new Ast.NormType(Mdf.Immutable,Path.Any(),Ph.None));
      }
      ons.add(new On(ts,Optional.empty(),e));      
      k=new Catch(k.getKind(),k.getX(),ons,Optional.empty());
      }
    return super.liftK(k);
    }
}
