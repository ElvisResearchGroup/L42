package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import tools.Assertions;
import tools.Map;
import ast.Ast;
import ast.Expression;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Op;
import ast.Ast.Parameters;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Position;
import ast.Ast.SignalKind;
import ast.Ast.Type;
import ast.Expression.Catch;
import ast.Expression.Catch1;
import ast.Expression.With.On;
import ast.Expression.BlockContent;
import ast.Ast.VarDec;
import ast.Ast.VarDecE;
import ast.Ast.VarDecXE;
import ast.Expression.BinOp;
import ast.Expression.ClassB;
import ast.Expression.ClassReuse;
import ast.Expression.ContextId;
import ast.Expression.CurlyBlock;
import ast.Expression.DocE;
import ast.Expression.DotDotDot;
import ast.Expression.FCall;
import ast.Expression.If;
import ast.Expression.Literal;
import ast.Expression.Loop;
import ast.Expression.MCall;
import ast.Expression.RoundBlock;
import ast.Expression.Signal;
import ast.Expression.SquareCall;
import ast.Expression.SquareWithCall;
import ast.Expression.UnOp;
import ast.Expression.UseSquare;
import ast.Expression.Using;
import ast.Expression.WalkBy;
import ast.Expression.While;
import ast.Expression.With;
import ast.Expression.X;
import ast.Expression._void;
import ast.Expression.ClassB.MethodImplemented;
import ast.Expression.ClassB.MethodWithType;
import ast.Expression.ClassB.NestedClass;
import auxiliaryGrammar.Functions;

abstract class ContextLocator extends CloneVisitor{
  public Expression visit(ClassB s) {return s;}
  public Expression visit(Expression.MCall s) {
    return s.withReceiver(s.getReceiver().accept(this));
    }
  public Expression visit(Expression.FCall s) {
    return s.withReceiver(s.getReceiver().accept(this));
    }
  public Expression visit(Expression.SquareCall s) {
    return s.withReceiver(s.getReceiver().accept(this));
    }
  public Expression visit(Expression.SquareWithCall s) {
    return s.withReceiver(s.getReceiver().accept(this));
    }
}
abstract class ReceiverExcluder<T> implements Visitor<T>{
  public T visit(Signal s){throw Assertions.codeNotReachable();}
  public T visit(If s){throw Assertions.codeNotReachable();}
  public T visit(While s){throw Assertions.codeNotReachable();}
  public T visit(With s){throw Assertions.codeNotReachable();}
  public T visit(X s){throw Assertions.codeNotReachable();}
  public T visit(ContextId s){throw Assertions.codeNotReachable();}
  public T visit(BinOp s){throw Assertions.codeNotReachable();}
  public T visit(DocE s){throw Assertions.codeNotReachable();}
  public T visit(UnOp s){throw Assertions.codeNotReachable();}
  public T visit(RoundBlock s){throw Assertions.codeNotReachable();}
  public T visit(CurlyBlock s){throw Assertions.codeNotReachable();}
  public T visit(Using s){throw Assertions.codeNotReachable();}
  public T visit(ClassB s){throw Assertions.codeNotReachable();}
  public T visit(DotDotDot s){throw Assertions.codeNotReachable();}
  public T visit(WalkBy s){throw Assertions.codeNotReachable();}
  public T visit(_void s){throw Assertions.codeNotReachable();}
  public T visit(Literal s){throw Assertions.codeNotReachable();}
  public T visit(Ast.Path s){throw Assertions.codeNotReachable();}
  public T visit(Loop s){throw Assertions.codeNotReachable();}
  public T visit(ClassReuse s){throw Assertions.codeNotReachable();}
  public T visit(UseSquare s){throw Assertions.codeNotReachable();}
}

class ContextDirectlyIn extends ContextLocator{
  boolean found=false;

  public Expression visit(Expression.ContextId s) {found=true;return s;}
  public static boolean of(Expression e){
    ContextDirectlyIn v=new ContextDirectlyIn();
    e.accept(v);
    return v.found;
  }
  public static boolean of(Ast.Parameters ps){
    if(ps.getE().isPresent()){
      if(of(ps.getE().get())){return true;}
    }
    for (Expression e:ps.getEs()){
      if(of(e)){return true;}
    }
    return false;
  }
  public static boolean ofRestOf(Ast.HasReceiver s){
    return (s).accept(new ReceiverExcluder<Boolean>() {
    public Boolean visit(MCall s) {return of(s.getPs());}
    public Boolean visit(FCall s) {return of(s.getPs());}
    public Boolean visit(SquareCall s) {
      for(Parameters ps:s.getPss()){if(of(ps)){return true;}}
      return false;
    }
    public Boolean visit(SquareWithCall s) { return of(s.getWith());}
  });
  }
}
class ContextReplace extends ContextLocator{
  Position pos;Expression receiver; String mName; String fName;

  public Expression visit(Expression.ContextId s) {
    assert s.getInner().startsWith("\\");
    if(s.getInner().equals("\\")){
      return Desugar.getMCall(pos, receiver,"#default#"+mName, Desugar.getPs(fName, Expression._void.instance));
    }
    return  Desugar.getMCall(pos, receiver,s.getInner().substring(1),Desugar.getPs());
    }
  public static Expression of(Position pos,Expression receiver,String mName, String fName, Expression e){
    ContextReplace v=new ContextReplace();
    v.pos=pos;
    v.receiver=receiver;
    v.fName=fName;
    v.mName=mName;
    return e.accept(v);
  }
  public static Ast.Parameters of(Position pos,Expression receiver,String mName, Ast.Parameters ps){
    Optional<Expression> fp=Optional.empty();
    List<String> xs=ps.getXs();
    List<Expression> es=new ArrayList<>();
    if(ps.getE().isPresent()){
      fp=Optional.of(of(pos,receiver,mName,"that",ps.getE().get()));
    }
    for(int i=0;i<xs.size();i++){
      es.add(of(pos,receiver,mName,xs.get(i),ps.getEs().get(i)));
    }
  return new Ast.Parameters(fp,xs, es);
  }
  public static Ast.HasReceiver ofRestOf(Ast.HasReceiver s,DesugarContext v){
      Expression receiver=s.getReceiver();
      Position pos=s.getP();
      String mName=s.accept(new ReceiverExcluder<String>() {
        public String visit(MCall s) {return s.getName();}
        public String visit(FCall s) {return "#apply";}
        public String visit(SquareCall s) { return "#square";}
        public String visit(SquareWithCall s) { return "#square";}
      });
      return (Ast.HasReceiver)s.accept(new ReceiverExcluder<Expression>() {
        public Expression visit(MCall s) {return v.visitS(s.withPs(of(pos, receiver, mName, s.getPs())));}
        public Expression visit(FCall s) {return v.visitS(s.withPs(of(pos, receiver, mName, s.getPs())));}
        public Expression visit(SquareCall s) {
          List<Parameters> pss=new ArrayList<>();
          for(Parameters ps:s.getPss()){pss.add(of(pos, receiver, mName, ps));}
          return v.visitS(s.withPss(pss));
        }
      public Expression visit(SquareWithCall s) { return v.visitS(s.withWith((With)of(pos, receiver, mName, mName, s.getWith())));}
    });
    }
  }


class DesugarContext extends CloneVisitor{
  Set<String> usedVars=new HashSet<String>();
  public static boolean checkRemoved(Expression e){
    class AssertContextNotPresent extends CloneVisitor{ @Override public Expression visit(ContextId s) {
      assert false:s;
      return s; }}
    e.accept(new AssertContextNotPresent());
    return true;
  }
  public static Expression of(Set<String> usedVars,Expression e){
    DesugarContext d=new DesugarContext();
    d.usedVars=usedVars;
    Expression result= e.accept(d);
    return result;
    //TODO: need to check there is no \ out of scope, that would be ill formed
  }

  private Expression normalizeReceiver(Ast.HasReceiver s){
    assert !(s.getReceiver() instanceof Ast.Atom);
    if(!ContextDirectlyIn.ofRestOf(s)){return s;}
    String x=Functions.freshName("rcv", usedVars);
    return visit(Desugar.getBlock(s.getP(),x, s.getReceiver(),s.withReceiver(new X(x))));
      }
  private Expression visitS(Ast.HasReceiver s){
    return s.accept(new ReceiverExcluder<Expression>() {
      public Expression visit(MCall s) {return visitS(s);}
      public Expression visit(FCall s) {return visitS(s);}
      public Expression visit(SquareCall s) {return visitS(s);}
      public Expression visit(SquareWithCall s) { return visitS(s);}
  });
  }
  private Expression visitHasReceiver(Ast.HasReceiver s){
    if(!(s.getReceiver() instanceof Ast.Atom)){   return normalizeReceiver(s);   }
    if(!ContextDirectlyIn.ofRestOf(s)){return visitS(s);}
    s=ContextReplace.ofRestOf(s,this);
    return s;
  }
  public Expression visit(Expression.MCall s) { return visitHasReceiver(s);}
  public Expression visit(FCall s) { return visitHasReceiver(s);}
  public Expression visit(SquareCall s) { return visitHasReceiver(s);}
  public Expression visit(SquareWithCall s) { return visitHasReceiver(s);}

  public Expression visitS(Expression.MCall s) { return super.visit(s);}
  public Expression visitS(FCall s) { return super.visit(s);}
  public Expression visitS(SquareCall s) { return super.visit(s);}
  public Expression visitS(SquareWithCall s) { return super.visit(s);}
}