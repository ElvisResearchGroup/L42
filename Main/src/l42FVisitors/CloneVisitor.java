package l42FVisitors;

import ast.L42F;
import ast.L42F.Block;
import ast.L42F.BreakLoop;
import ast.L42F.Call;
import ast.L42F.Cast;
import ast.L42F.Cn;
import ast.L42F.D;
import ast.L42F.E;
import ast.L42F.If;
import ast.L42F.K;
import ast.L42F.Loop;
import ast.L42F.Null;
import ast.L42F.T;
import ast.L42F.Throw;
import ast.L42F.Update;
import ast.L42F.Use;
import ast.L42F.X;
import ast.L42F._void;

import java.util.List;

import ast.Ast.MethodSelector;

public class CloneVisitor implements Visitor<L42F.E>{
  protected D liftD(D s){
    return new D(s.isVar(),liftT(s.getT()),liftX(s.getX()),s.getE().accept(this));
    }
  protected K liftK(K s){
    return new K(s.getKind(),liftT(s.getT()),liftX(s.getX()),s.getE().accept(this));
    }
  protected T liftT(T s){
    return new T(s.getMdf(),liftCn(s.getCn()));
    }
  protected String liftX(String s){
    return s;
    }
  protected int liftCn(int s){
    return s;
    }
  
  protected MethodSelector liftMs(MethodSelector s){return s;}
  @Override
  public E visit(Block s) {
    return new Block(
      tools.Map.of(this::liftD,s.getDs()), 
      tools.Map.of(this::liftK,s.getKs()),
      s.getE().accept(this),
      liftT(s.getType()));
    }

  @Override
  public E visit(X s) {
    return s.withInner(liftX(s.getInner()));
    }

  @Override
  public E visit(_void s) {
    return s;
    }

  @Override
  public E visit(Null s) {
    return s;
    }

  @Override
  public E visit(BreakLoop s) {
    return s;
    }

    @Override
  public E visit(Throw s) {
    return s.withX(liftX(s.getX()));
    }

  @Override
  public E visit(Loop s) {
    return s.withInner(s.getInner().accept(this));
    }

  @Override
  public E visit(Call s) {
    int cn=liftCn(s.getCn());
    MethodSelector ms=liftMs(s.getMs());
    List<String> xs=tools.Map.of(this::liftX,s.getXs());
    return new Call(cn,ms,xs);
    }

  @Override
  public E visit(Use s) {
    MethodSelector ms=liftMs(s.getMs());
    List<String> xs=tools.Map.of(this::liftX,s.getXs());
    return new Use(s.getDoc(),s.getUi(),ms,xs,s.getInner().accept(this));
    }

  @Override
  public E visit(If s) {
    return new If(liftX(s.getCondition()),
      s.getThen().accept(this),s.get_else().accept(this));
    }

  @Override
  public E visit(Update s) {
    return new Update(liftX(s.getX1()),liftX(s.getX2()));
    }

  @Override
  public E visit(Cast s) {
    return new Cast(liftT(s.getT()),liftX(s.getX()));
    }
  @Override
  public E visit(Cn s) {
    return new Cn(s.getInner());
    }
  }
