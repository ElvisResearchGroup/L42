package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import tools.Assertions;
import tools.Map;
import ast.Ast;
import ast.ErrorMessage;
import ast.Expression;
import ast.Ast.C;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.Mdf;
import ast.Ast.Type;
import ast.Ast.Op;
import ast.Ast.Parameters;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.SignalKind;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.Expression.Catch;
import ast.Expression.Catch1;
import ast.Expression.With.On;
import ast.Expression.BlockContent;
import ast.Ast.VarDec;
import ast.Ast.VarDecCE;
import ast.Ast.VarDecE;
import ast.Ast.VarDecXE;
import ast.ErrorMessage.NotWellFormedMsk;
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
import facade.L42;

public class DesugarVars extends CloneVisitor{
  public static boolean assertVarsRemoved(Expression _e){
    _e.accept(new CloneVisitor(){
      //Ok liftVarDec since With uses liftVarDecXE
      @Override protected ast.Ast.VarDec liftVarDec(ast.Ast.VarDec d) {
        return d.match(
            vdxe->{
              if(vdxe.isVar()){
                throw new AssertionError("Variable local binding found:"+vdxe.toString());
              }
              return this.liftVarDecXE(vdxe);
            },
            this::liftVarDecE,
            this::liftVarDecCE);
      }
    });
    return true;
  }

  public static Expression of(Expression e){
    DesugarVars d=new DesugarVars();
    Expression result= e.accept(d);
    return result;
  }
  private RoundBlock blockContentSepare(RoundBlock s) {
    if(s.getContents().size()<=1){return s;}
    List<Expression.BlockContent> ctxTop = new ArrayList<>(s.getContents().subList(0,1));
    List<Expression.BlockContent> ctxPop = new ArrayList<>(s.getContents().subList(1, s.getContents().size()));
    RoundBlock next=blockContentSepare(s.withContents(ctxPop));
    return s.withContents(ctxTop).withInner(next);
  }
  public Expression visit(CurlyBlock s) {
    Expression inner;
    if(s.getContents().size()==1 &&
       s.getContents().get(0).get_catch().isEmpty() &&
       s.getContents().get(0).getDecs().size()==1&&
       s.getContents().get(0).getDecs().get(0) instanceof VarDecE
        ){
      inner=((VarDecE)s.getContents().get(0).getDecs().get(0)).getInner();
    }
    else{ inner=new RoundBlock(s.getP(),s.getDoc(),Expression._void.instance,s.getContents());}
    inner=inner.accept(this);
    List<VarDec> vd = Collections.singletonList((VarDec)new VarDecE(inner));
    BlockContent o=new BlockContent(vd,Collections.emptyList());
    return new CurlyBlock(s.getP(),Doc.empty(),Collections.singletonList(o));
  }
  public Expression visit(RoundBlock s) {
    s=blockContentSepare(s);
    //s=blockVarClass(s);
    Expression result= super.visit(s);
    return result;
    }
}

