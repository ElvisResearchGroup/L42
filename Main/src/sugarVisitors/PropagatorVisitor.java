package sugarVisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import tools.Map;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Parameters;
import ast.Ast.ConcreteHeader;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.MethodType;
import ast.Ast.Type;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.Ast.VarDec;
import ast.Ast;
import ast.Expression;
import ast.Expression.ClassB.Member;
import ast.Expression.*;
import ast.Expression.ClassB.*;

public class PropagatorVisitor implements Visitor<Void>{
  protected void lift(Expression e){ e.accept(this); }
  protected void liftP(Path p){}
  protected void liftT(Type t){
      liftP(t.getPath());
      liftDoc(t.getDoc());
      }
  protected void liftK(Expression.Catch k){
    if (k instanceof DesugarCatchDefault.CatchToComplete){
      this.liftK(((DesugarCatchDefault.CatchToComplete) k).catch1);
      }
    k.match(
      k1->{
        liftT(k1.getT());
        lift(k1.getInner());
        return null;},
      kM->{      
        kM.getTs().forEach(this::liftT);
        lift(kM.getInner());
        return null;},
      kP->{
        kP.getTs().forEach(this::liftT);
        lift(kP.getInner());
        return null;}
      );
    }
  protected void liftO(Expression.With.On on){
    on.getTs().forEach(this::liftT);
    lift(on.getInner());
    }
  protected void liftH(Header h) {
    h.match(
      ch->{
        ch.getFs().forEach(this::liftF);
        return null;
        }, th->th, ih->ih);
    }
  protected void liftF(FieldDec f) {
    liftT(f.getT());
    liftDoc(f.getDoc());
    }
  protected void liftDoc(Doc doc) {
    doc.getAnnotations().forEach(ann->{
      if(ann instanceof Path){
        this.liftP((Path)ann);
        }});
    }
  protected void liftBC(Expression.BlockContent c) {
    liftVarDecs(c.getDecs());
    this.liftKs(c.get_catch());
    }
  protected void liftKs(List<Catch> ks) {
    for(Catch k:ks){liftK(k);}
    }
  protected void liftVarDecs(List<ast.Ast.VarDec> ds) {
    for(VarDec d:ds){this.liftVarDec(d);}
  }
  protected void liftVarDec(ast.Ast.VarDec d) {
    d.match(
      _d->{this.liftVarDecXE(_d);return null;},
      _d->{this.liftVarDecE(_d);return null;},
      _d->{this.liftVarDecCE(_d);return null;}
      );
    }
  protected void liftVarDecXE(ast.Ast.VarDecXE d) {
    d.getT().ifPresent(this::liftT);
    lift(d.getInner());
    }
  protected void liftVarDecE(ast.Ast.VarDecE d) {
    lift(d.getInner());
    }
  protected void liftVarDecCE(ast.Ast.VarDecCE d) {
    this.visit(d.getInner());
  }
  protected void liftM(Member m) {
    m.match(
      nc->{visit(nc); return null;},
      mi->{visit(mi); return null;},
      mt->{visit(mt); return null;}
      );
    }
  public void visit(NestedClass nc){
    liftDoc(nc.getDoc());
    lift(nc.getInner());
    }
  public void visit(MethodImplemented mi){
    liftDoc(mi.getDoc());
    liftMs(mi.getS());
    lift(mi.getInner());
    }
  public void visit(MethodWithType mt){
    liftDoc(mt.getDoc());
    liftMs(mt.getMs());
    liftMT(mt.getMt());
    mt.getInner().ifPresent(this::lift);
    }

  protected void liftMs(MethodSelector ms) {}


  protected void liftMT(MethodType mt) {
    mt.getTs().forEach(this::liftT);
    liftT(mt.getReturnType());
    mt.getExceptions().forEach(this::liftT);
    }
  
  public Void visit(Expression.EPath s) {liftP(s.getInner());return null;}
  public Void visit(X s) { return null;}
  public Void visit(_void s) {return null;}
  public Void visit(WalkBy s) {return null;}
  public Void visit(Using s) {
    liftP(s.getPath());
    liftDoc(s.getDocs());
    liftPs(s.getPs());
    lift(s.getInner());
    return null;
    }
  protected void liftPs(Parameters ps) {
    ps.getE().ifPresent(this::lift);
    ps.getEs().forEach(this::lift);
    }
  public Void visit(Signal s) {
    lift(s.getInner());
    return null;
  }
  public Void visit(Loop s) {
    lift(s.getInner());
    return null;
  }
  public Void visit(MCall s) {
    lift(s.getReceiver());
    liftDoc(s.getDoc());
    liftPs(s.getPs());
    return null;
    }
  public Void visit(Expression.OperationDispatch s) {
    liftDoc(s.getDoc());
    liftPs(s.getPs());
    return null;
    }
  public Void visit(ClassB s) {
    liftH(s.getH());
    s.getFields().forEach(this::liftF);
    s.getSupertypes().forEach(this::liftT);
    s.getMs().forEach(this::liftM);
    liftDoc(s.getDoc1());
    return null;
    }
  public Void visit(If s) {
    lift(s.getCond());
    lift(s.getThen());
    s.get_else().ifPresent(this::lift);
    return null;
  }
  public Void visit(While s) {
    lift(s.getCond());
    lift(s.getThen());
    return null;
    }
  public Void visit(With s) {
    s.getIs().forEach(this::liftVarDecXE);
    s.getDecs().forEach(this::liftVarDecXE);
    s.getOns().forEach(this::liftO);
    s.getDefaultE().ifPresent(this::lift);
    return null;
    }
  public Void visit(BinOp s) {
    lift(s.getLeft());
    lift(s.getRight());
    return null;
    }
  public Void visit(DocE s) {
    lift(s.getInner());
    liftDoc(s.getDoc());
    return null;
    }
  public Void visit(UnOp s) {
    lift(s.getInner());
    return null;
    }
  public Void visit(FCall s) {
    lift(s.getReceiver());
    liftDoc(s.getDoc());
    liftPs(s.getPs());
    return null;
    }
  public Void visit(SquareCall s) {
    lift(s.getReceiver());
    liftDoc(s.getDoc());
    s.getDocs().forEach(this::liftDoc);
    s.getPss().forEach(this::liftPs);
    return null;
    }
  public Void visit(SquareWithCall s) {
    lift(s.getReceiver());
    lift(s.getWith());
    return null;
    }
  public Void visit(UseSquare s) {
    lift(s.getInner());
    return null;
    }
  public Void visit(RoundBlock s) {
    s.getContents().forEach(this::liftBC);
    lift(s.getInner());
    liftDoc(s.getDoc());
    return null;
    }
  public Void visit(CurlyBlock s) {
    liftDoc(s.getDoc());
    s.getContents().forEach(this::liftBC);
    return null;
    }
  public Void visit(DotDotDot s) {
    return null;
    }
  public Void visit(Literal s) {
    lift(s.getReceiver());
    return null;
    }
  public Void visit(ClassReuse s) {
    lift(s.getInner());
    return null;
    }
  public Void visit(ContextId s) {
    return null;
    }
  }

