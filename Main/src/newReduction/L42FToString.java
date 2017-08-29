package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.L42F.Block;
import ast.L42F.Body;
import ast.L42F.BreakLoop;
import ast.L42F.CD;
import ast.L42F.Call;
import ast.L42F.Cast;
import ast.L42F.Cn;
import ast.L42F.D;
import ast.L42F.E;
import ast.L42F.If;
import ast.L42F.K;
import ast.L42F.Loop;
import ast.L42F.M;
import ast.L42F.Null;
import ast.L42F.T;
import ast.L42F.Throw;
import ast.L42F.Update;
import ast.L42F.Use;
import ast.L42F.X;
import ast.L42F._void;
import l42FVisitors.ToFormattedText;
import l42FVisitors.Visitor;

public class L42FToString extends ToFormattedText implements Visitor<Void>{
  public L42FToString(ClassTable ct){this.ct = ct;}
  ClassTable ct;

  public static String visitCT(ClassTable ct) {
    L42FToString ts=new L42FToString(ct);
    List<Integer> ks=new ArrayList<>(ct.keySet());
    Collections.sort(ks);
    for(Integer i:ks){
      ts.visit(ct.get(i).cd);
    }
    return ts.result.toString();
  }
  public void visit(CD cd) {
    if(cd.getKind()==null) {//just a stub for libs
      c("Stub for cn: "+cd.getCn()+"\n");
      return;
      }
    c(cd.getKind()+" "+cd.dbgName()+" implements ");
    tools.StringBuilders.formatSequence(result,cd.getCns().iterator(),",",x->liftCn(x));
    c("{");
    indent();nl();
    for(M mi:cd.getMs()) {
      if(mi.isRefine()) {c("refine ");}
      c("method ");
      liftT(mi.getReturnType());
      c(" "+mi.getSelector());
      c("(");
      tools.StringBuilders.formatSequence(result,mi.getTxs().iterator(),",",
          x->{liftT(x.getT());sp();liftX(x.getX());});
      c(")");
      indent();
      nl();
      liftB(mi.getBody());
      deIndent();
      nl();
      }
    deIndent();
    c("}");
    nl();
  }
  private void liftB(Body body) {
    if(body instanceof E) {((E)body).accept(this);}
    else {c(""+body);}
  }
  @Override
  public Void visit(Block s) {
    c("(");
    if(!s.getDs().isEmpty() || !s.getKs().isEmpty()) {indent();nl();}
    for(D di:s.getDs()) {
      liftT(di.getT());
      sp();
      liftX(di.getX());
      c("=");
      di.getE().accept(this);
      nl();
      }
    for(K ki:s.getKs()) {
      c("catch "+ki.getKind()+" ");
      liftT(ki.getT());
      sp();
      liftX(ki.getX());
      sp();
      ki.getE().accept(this);
      nl();
      }
    s.getE().accept(this);
    if(!s.getDs().isEmpty() || !s.getKs().isEmpty()) {deIndent();nl();}
    c(")");
    return null;
  }

  private void liftX(String x) {c(x);}
  private void liftCn(int i) {c(ct.dbgNameOf(i));}
  private void liftT(T t) {
    c(""+t.getMdf());
    sp();
    liftCn(t.getCn());
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
  public Void visit(_void s) {
    c("void");
    return null;
    }

  @Override
  public Void visit(Null s) {
    c("null");
    return null;
  }

  @Override
  public Void visit(BreakLoop s) {
    c("breakLoop");
    return null;
  }

  @Override
  public Void visit(Throw s) {
    c(""+s.getKind());
    sp();
    liftX(s.getX());
    return null;
  }

  @Override
  public Void visit(Loop s) {
    c("loop ");
    s.getInner().accept(this);
    return null;
  }

  @Override
  public Void visit(Call s) {
    liftCn(s.getCn());
    c(".");
    c(""+s.getMs());
    c("(");
    tools.StringBuilders.formatSequence(result,s.getXs().iterator(),",",x->c(x));
    c(")");
    return null;
  }

  @Override
  public Void visit(Use s) {
    c("use ");
    s.getDoc().toStringWeak();
    c(" check "+s.getMs());
    c("(");
    tools.StringBuilders.formatSequence(result,s.getXs().iterator(),",",x->c(x));
    c(")");
    return null;
  }

  @Override
  public Void visit(If s) {
    c("if ");
    liftX(s.getCondition());
    nl();
    s.getThen().accept(this);
    nl();
    s.get_else().accept(this);
    return null;
  }

  @Override
  public Void visit(Update s) {
    liftX(s.getX1());
    c(":=");
    liftX(s.getX2());
    return null;
  }

  @Override
  public Void visit(Cast s) {
    c("((");
    liftT(s.getT());
    c(")");
    liftX(s.getX());
    c(")");
    return null;
  }
}
