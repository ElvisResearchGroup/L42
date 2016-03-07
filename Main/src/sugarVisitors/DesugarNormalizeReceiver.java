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

class DesugarNormalizeReceiver extends CloneVisitor{
  Set<String> usedVars=new HashSet<String>();
  public static Expression of(Set<String> usedVars,Expression e){
    DesugarNormalizeReceiver d=new DesugarNormalizeReceiver();
    d.usedVars=usedVars;
    Expression result= e.accept(d);
    return result;
  }

  private Expression normalizeReceiver(Ast.HasReceiver s){
    assert !(s.getReceiver() instanceof Ast.Atom);
    String x=Functions.freshName("rcv", usedVars);
    return Desugar.getBlock(s.getP(),x, s.getReceiver(),s.withReceiver(new X(x)));
    }
  public Expression visit(MCall s) {
    if (s.getReceiver() instanceof Ast.Atom){ return super.visit(s);}
    if (!ContextDirectlyIn.ofRestOf(s)){ return super.visit(s);}
    return normalizeReceiver(s).accept(this);
  }
  public Expression visit(FCall s) {
    if (s.getReceiver() instanceof Ast.Atom){ return super.visit(s);}
    return normalizeReceiver(s).accept(this);
  }
  public Expression visit(SquareCall s) {
    if (s.getReceiver() instanceof Ast.Atom){ return super.visit(s);}
    return normalizeReceiver(s).accept(this);
  }
  public Expression visit(SquareWithCall s) {
    if (s.getReceiver() instanceof Ast.Atom){ return super.visit(s);}
    return normalizeReceiver(s).accept(this);
    }
  public Expression visit(Literal s) {
    if (s.getReceiver() instanceof Ast.Atom){ return super.visit(s);}
    return normalizeReceiver(s).accept(this);
    }
}
