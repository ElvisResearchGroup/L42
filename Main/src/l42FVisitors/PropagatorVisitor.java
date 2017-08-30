package l42FVisitors;

import java.util.List;

import ast.Ast;
import ast.Ast.MethodSelector;
import ast.L42F.*;

public class PropagatorVisitor implements Visitor<Void>{

  @Override
  public Void visit(Block s) {
    for(D d:s.getDs()) {liftD(d);}
    for(ast.L42F.K k:s.getKs()) {liftK(k);}
    s.getE().accept(this);
    liftT(s.getType());
    return null;
  }

  protected void liftT(T type) {
    liftCn(type.getCn());
    }

  protected void liftCn(int cn) {}

  protected void liftK(K k) {
    liftT(k.getT());
    liftX(k.getX());
    k.getE().accept(this);
  }

  protected void liftX(String x) {}

  protected void liftD(D d) {
    liftT(d.getT());
    liftX(d.getX());
    d.getE().accept(this);
  }

  @Override
  public Void visit(X s) {
    liftX(s.getInner());
    return null;
  }

  @Override
  public Void visit(Cn s) {
    liftCn(s.getInner());
    return null;
  }

  @Override
  public Void visit(_void s) {return null;}

  @Override
  public Void visit(Null s) {return null;}

  @Override
  public Void visit(BreakLoop s) {return null;}

  @Override
  public Void visit(Throw s) {
    liftX(s.getX());
    return null;
  }

  @Override
  public Void visit(Loop s) {
    s.getInner().accept(this);
    return null;
  }

  @Override
  public Void visit(Call s) {
    liftCn(s.getCn());
    liftMs(s.getMs());
    for(String x:s.getXs()) {liftX(x);}
    return null;
  }

  protected void liftMs(MethodSelector ms) {}

  @Override
  public Void visit(Use s) {
    liftMs(s.getMs());
    for(String x:s.getXs()) {liftX(x);}
    s.getInner().accept(this);
    return null;
  }

  @Override
  public Void visit(If s) {
    liftX(s.getCondition());
    s.getThen().accept(this);
    s.get_else().accept(this);
    return null;
  }

  @Override
  public Void visit(Update s) {
    liftX(s.getX1());
    liftX(s.getX2());
    return null;
  }

  @Override
  public Void visit(Cast s) {
    liftT(s.getT());
    liftX(s.getX());
    return null;
  }
  public Void visit(CD s) {
    liftCn(s.getCn());
    for(Integer cni:s.getCns()) {liftCn(cni);}
    for(M mi:s.getMs()) {liftM(mi);}
    return null;
    }

  protected void liftM(M s) {
    liftT(s.getReturnType());
    liftMs(s.getSelector());
    for(TX txi:s.getTxs()){ liftT(txi.getT()); liftX(txi.getX());}
    liftBody(s.getBody());
  }

  protected void liftBody(Body body) {
    if(body instanceof E) {
      ((E)body).accept(this);
    }

  }
}
