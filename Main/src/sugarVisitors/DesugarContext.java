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
import ast.Ast.Type;
import ast.Ast.Op;
import ast.Ast.Parameters;
import ast.Ast.Path;
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
  public static Expression of(Expression e){
    DesugarContext d=new DesugarContext();
    Expression result= e.accept(d);
    return result;
    //TODO: need to check there is no \ out of scope, that would be ill formed
    //it is a subset of what is asserted now with checkRemoved.
  }

  public Expression visit(Expression.MCall s) {
    s=s.withPs(ContextReplace.of(s.getP(),s.getReceiver(), s.getName(), s.getPs()));
    return super.visit(s);
    }
  public Expression visit(FCall s) {
    s=s.withPs(ContextReplace.of(s.getP(),s.getReceiver(), "#apply", s.getPs()));
    return super.visit(s);
    }
  public Expression visit(SquareCall s) {
    List<Parameters> pss=new ArrayList<>();
    for(Parameters ps:s.getPss()){pss.add(ContextReplace.of(s.getP(), s.getReceiver(),"#square", ps));}
    s=s.withPss(pss);
    return super.visit(s);
    }
  public Expression visit(SquareWithCall s) {
    s=s.withWith((With)ContextReplace.of(s.getP(),s.getReceiver(),"#square","#square", s.getWith()));
    return super.visit(s);
    }
}