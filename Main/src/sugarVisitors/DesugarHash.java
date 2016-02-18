package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import ast.Expression.If;
import ast.Expression.Loop;
import ast.Expression.MCall;
import ast.Expression.RoundBlock;
import ast.Expression.Signal;
import ast.Expression.SquareCall;
import ast.Expression.SquareWithCall;
import ast.Expression.With;
import ast.Expression.X;
import ast.Expression._void;
import ast.Expression.ClassB.MethodImplemented;
import ast.Expression.ClassB.MethodWithType;
import ast.Expression.ClassB.NestedClass;
import auxiliaryGrammar.Functions;

class HashDirectlyIn extends CloneVisitor{
  boolean found=false;
  public Expression visit(ClassB s) {return s;}
  public Expression visit(Expression.MCall s) {
    return s.withReceiver(s.getReceiver().accept(this));
    }
  public Expression visit(Expression.HashId s) {found=true;return s;}
  public static boolean of(Expression e){
    HashDirectlyIn v=new HashDirectlyIn();
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
}
class HashReplace extends CloneVisitor{
  Position pos;Expression receiver; String mName; String fName;
  public Expression visit(ClassB s) {return s;}
  public Expression visit(Expression.MCall s) {
    return s.withReceiver(s.getReceiver().accept(this));
    }
  public Expression visit(Expression.HashId s) {
    assert s.getInner().startsWith("#");
    if(s.getInner()=="#"){
      return Desugar.getMCall(pos, receiver,"#default"+mName, Desugar.getPs(fName, Expression._void.instance));
    }
    return  Desugar.getMCall(pos, receiver,s.getInner().substring(1),Desugar.getPs());
    }
  public static Expression of(Position pos,Expression receiver,String mName, String fName, Expression e){
    HashReplace v=new HashReplace();
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


class DesugarHash extends CloneVisitor{
  Set<String> usedVars=new HashSet<String>();
  public static Expression of(Set<String> usedVars,Expression e){
    DesugarHash d=new DesugarHash();
    d.usedVars=usedVars;
    Expression result= e.accept(d);
    return result;
    //TODO: need to check there is no #out of scope, that would be ill formed
  }

  public Expression visit(Expression.MCall s) {
    if(!(s.getReceiver() instanceof Ast.Atom)){
      if(!HashDirectlyIn.of(s.getPs())){return s;}
      String x=Functions.freshName("rcv", usedVars);
      return visit(Desugar.getBlock(s.getP(),x, s.getReceiver(),s.withReceiver(new X(x))));
      }
    //what is faster? to replace anyway?
    if(!HashDirectlyIn.of(s.getPs())){return s;}
    Parameters newPs = HashReplace.of(s.getP(),s.getReceiver(),s.getName(),s.getPs());
    return s.withPs(newPs);
  }
  
}