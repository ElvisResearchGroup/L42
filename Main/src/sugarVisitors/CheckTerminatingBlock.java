package sugarVisitors;

import java.util.List;

import ast.Ast;
import ast.Ast.Path;
import ast.Ast.VarDec;
import ast.ErrorMessage;
import ast.Expression;
import ast.Expression.*;
import ast.Expression.With.On;

public class CheckTerminatingBlock implements Visitor<Void> {
  private CurlyBlock start;
  private CheckTerminatingBlock(CurlyBlock start) {this.start = start;}
  public static void of(CurlyBlock s) {
    s.accept(new CheckTerminatingBlock(s));
    }
  public Void visit(Signal s) {return null;}
  public Void visit(Loop s) {return null;}
  public Void visit(If s) {
    s.getThen().accept(this);
    if(s.get_else().isPresent()){s.get_else().get().accept(this);}
    else{return fail(s);}
    return null;
    }
  public Void visit(With s) {
    if(!s.getIs().isEmpty()){return fail(s);}
    if(s.getDefaultE().isPresent()){s.getDefaultE().get().accept(this);}
    for(On on:s.getOns()){
      on.getInner().accept(this);
      }
    return null;
    }
  public Void visit(RoundBlock s) {
    checkBlockContent(s.getContents());
    return s.getInner().accept(this);
    }
  public Void visit(CurlyBlock s) {
    checkBlockContent(s.getContents());
    Expression.BlockContent bc = s.getContents().get(s.getContents().size()-1);
    VarDec dec = bc.getDecs().get(bc.getDecs().size()-1);
    if(!(dec instanceof Ast.VarDecE)){return fail(s);}
    Ast.VarDecE decE=(Ast.VarDecE)dec;
    return decE.getInner().accept(this);
    }
  public void checkBlockContent(List<Expression.BlockContent> l) {
    for( Expression.BlockContent c:l){
      if(c.get_catch().isEmpty()){continue;}
      for( Expression.Catch ki : c.get_catch()){
       if(ki instanceof Expression.CatchProp){continue;}
       ki.getInner().accept(this);
        }
      }
    }

  private Void fail(Expression s){
    throw new ErrorMessage.NotWellFormedMsk(s, this.start, "The expression is not a valid terminator for the block");
    }


  public Void visit(X s) {return fail(s);}
  public Void visit(BinOp s) {return fail(s);}
  public Void visit(DocE s) {return fail(s);}
  public Void visit(UnOp s) {return fail(s);}
  public Void visit(MCall s) {return fail(s);}
  public Void visit(FCall s) {return fail(s);}
  public Void visit(SquareCall s) {return fail(s);}
  public Void visit(SquareWithCall s) {return fail(s);}
  public Void visit(UseSquare s) {return fail(s);}
  public Void visit(Using s) {return fail(s);}
  public Void visit(ClassB s) {return fail(s);}
  public Void visit(DotDotDot s) {return fail(s);}
  public Void visit(WalkBy s) {return fail(s);}
  public Void visit(_void s) {return fail(s);}
  public Void visit(Literal s) {return fail(s);}
  public Void visit(While s) {return fail(s);}
  public Void visit(Path s) {return fail(s);}
  public Void visit(ClassReuse s) {return fail(s);}
  public Void visit(ContextId s)  {return fail(s);}
  }
