package sugarVisitors;

import ast.Ast;
import ast.Expression;
import ast.Ast.Parameters;
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
import tools.Assertions;

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
    public Boolean visit(Expression.OperationDispatch s) {return of(s.getPs());}
    public Boolean visit(FCall s) {return of(s.getPs());}
    public Boolean visit(SquareCall s) {
      for(Parameters ps:s.getPss()){if(of(ps)){return true;}}
      return false;
    }
    public Boolean visit(SquareWithCall s) { return of(s.getWith());}
  });
  }
}

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
  public T visit(Expression.EPath s){throw Assertions.codeNotReachable();}
  public T visit(Loop s){throw Assertions.codeNotReachable();}
  public T visit(ClassReuse s){throw Assertions.codeNotReachable();}
  public T visit(UseSquare s){throw Assertions.codeNotReachable();}
}
