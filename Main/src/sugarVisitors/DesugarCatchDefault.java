package sugarVisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import ast.Ast;
import ast.Ast.Mdf;
import ast.Ast.Type;
import ast.Expression.With.On;
import tools.Map;
import ast.Expression.BlockContent;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Type;
import ast.Expression;
import ast.Expression.Catch;
import ast.Expression.Catch1;
import ast.Expression.CatchMany;
import ast.Ast.SignalKind;
import ast.Ast.VarDec;
import ast.Expression.ClassB;

public class DesugarCatchDefault extends CloneVisitor{
  public static class CatchToComplete implements Expression.Catch{
    public <T> T match(Function<Catch1, T> k1, Function<CatchMany, T> kM, Function<Expression.CatchProp, T> kP) {throw new Error("temporaryInstance");}
    public String getX() {return catch1.getX();}
    public Expression getInner() {return catch1.getInner();}
    public final Catch1 catch1;
    public CatchToComplete(Catch1 catch1){this.catch1=catch1;}
    public Catch1 completeCatch(Type t){
      return catch1.withT(t);
    }
    public Position getP() {return catch1.getP();}
    public Catch withP(Position that) {return catch1.withP(that);}
  }
  Ast.Type lastReturn=null;
  public static ClassB of(ClassB s) {
    return (ClassB)s.accept(new DesugarCatchDefault());
  }
  protected BlockContent liftBC(BlockContent c) {
    if(c.get_catch().isEmpty()){return super.liftBC(c);}
    List<Catch> ks = Map.of(this::liftK, c.get_catch());
    Type oldR=this.lastReturn;
    this.lastReturn=newR(ks);
    try{
      List<VarDec> liftVarDecs = liftVarDecs(c.getDecs());
      return new BlockContent(liftVarDecs,ks);
      }
    finally{this.lastReturn=oldR;}
  }
    
  private Type newR(List<Catch> ks) {
    if (ks.size()!=1){return this.lastReturn;} 
    Expression.Catch1 k=(Expression.Catch1)ks.get(0);
    if(k.getKind()!=SignalKind.Return){return this.lastReturn;}
    return k.getT();
    }
  
  protected Catch liftK(Catch k){
    if(!(k instanceof CatchToComplete)){return super.liftK(k);}
    CatchToComplete ktc=(CatchToComplete)k;
    if(ktc.catch1.getKind()!=SignalKind.Return){return super.liftK(ktc.catch1);}
    if(this.lastReturn!=null){
       return ((CatchToComplete)k).completeCatch(this.lastReturn);
       }
    return ((CatchToComplete)k).completeCatch(Type.immAny);
    }
}
