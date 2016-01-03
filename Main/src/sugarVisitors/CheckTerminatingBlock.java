package sugarVisitors;

import java.util.List;

import ast.Ast;
import ast.Ast.BlockContent;
import ast.Ast.Catch;
import ast.Ast.On;
import ast.Ast.Path;
import ast.Ast.VarDec;
import ast.ErrorMessage;
import ast.Expression;
import ast.Expression.BinOp;
import ast.Expression.ClassB;
import ast.Expression.ClassReuse;
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
import ast.Expression.UseSquare;
import ast.Expression.UnOp;
import ast.Expression.Using;
import ast.Expression.WalkBy;
import ast.Expression.While;
import ast.Expression.With;
import ast.Expression.X;
import ast.Expression._void;

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
    BlockContent bc = s.getContents().get(s.getContents().size()-1);
    VarDec dec = bc.getDecs().get(bc.getDecs().size()-1);
    if(!(dec instanceof Ast.VarDecE)){return fail(s);}
    Ast.VarDecE decE=(Ast.VarDecE)dec;
    return decE.getInner().accept(this);
    }
  public void checkBlockContent(List<BlockContent> l) {
    for( BlockContent c:l){
      if(!c.get_catch().isPresent()){continue;}
      Catch k = c.get_catch().get();
      for(On on:k.getOns()){
        on.getInner().accept(this);
        }
      if(k.get_default().isPresent()){k.get_default().get().accept(this);}
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
  }
