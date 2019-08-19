package is.L42.visitors;

import static is.L42.tools.General.*;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import is.L42.generated.L42Parser.*;
import is.L42.generated.*;

public class FullL42Visitor implements L42Visitor<Object>{
  String fileName;
  Core.EVoid eVoid=new Core.EVoid(null);//will not be in the final result
  public FullL42Visitor(String fileName){this.fileName=fileName;}
  Object c(ParserRuleContext prc){
    assert prc.children.size()==1;
    return prc.children.get(0).accept(this); 
    }
  Pos pos(ParserRuleContext prc){
    return new Pos(fileName,prc.getStart().getLine(),prc.getStart().getCharPositionInLine()); 
    }
  @Override public Void visit(ParseTree arg0) {throw bug();}
  @Override public Void visitChildren(RuleNode arg0) {throw bug();}
  @Override public Void visitErrorNode(ErrorNode arg0) {throw bug();}
  @Override public Void visitTerminal(TerminalNode arg0) {throw bug();}
  @Override public Full.E visitE(EContext ctx) {return (Full.E)c(ctx);}
  @Override public Full.Par visitPar(ParContext ctx) {
    List<X> xs=L(ctx.x(),(c,x)->c.add(visitX(x)));
    List<Full.E> es=L(ctx.e(),(c,x)->c.add(visitE(x)));
    if (es.size()==xs.size()){return new Full.Par(null, xs, es);}
    assert es.size()==xs.size()+1;
    return new Full.Par(es.get(0), xs, popL(es));
    }
  @Override public Void visitOR(ORContext ctx) {throw bug();}
  @Override public String visitString(StringContext ctx) {
    return ctx.getText().substring(0,ctx.getText().length()-1);
    }
  @Override public Full.D visitD(DContext ctx) {
    var dx=visitDX(ctx.dX());
    var e=visitE(ctx.e());
    var first=dx.get(0);
    return new Full.D(first,popL(dx),e);
    }
  @Override public Full.E visitEAtomic(EAtomicContext ctx) {
    Object inner=c(ctx);
    if(inner instanceof X){return new Core.EX(pos(ctx), (X)inner);}
    return (Full.E)inner;
    }
  @Override public X visitX(XContext ctx) {return new X(ctx.getText());}
  @Override public Full.Call visitFCall(FCallContext ctx) {
    return new Full.Call(pos(ctx), eVoid, visitM(ctx.m()), false, L(visitPar(ctx.par())));
    }
  @Override public Full.E visitNudeE(NudeEContext ctx) {return (Full.E)ctx.children.get(0).accept(this);}
  @Override public Full.Block visitBlock(BlockContext ctx) {
    //oR d*? e ')' | oR d+ k* whoops? (d* e)? ')' | '{' d+ (k+ whoops? d* | whoops d*)? '}';
    boolean isCurly=ctx.oR()==null;
    List<Full.D> ds=L(ctx.d(),(c,d)->visitD(d));
    List<Full.K> ks=L(ctx.k(),(c,k)->visitK(k));
    List<Full.T> whoopsed=visitWhoops(ctx.whoops());
    Full.E e=visitE(ctx.e());
    int dsAfter=ds.size();
    for(int i:range(ctx.children)){
      if(ctx.children.get(i) instanceof KContext){dsAfter=i-1;break;}
      }
    return new Full.Block(pos(ctx), isCurly, ds, dsAfter, ks, whoopsed, e);
    }
  @Override public S visitM(MContext ctx) {
    String s=ctx.toString();
    int un=s.indexOf("::");
    if(un==-1){return new S(s,L(),-1);}
    int n=Integer.parseInt(s.substring(un+2));
    s=s.substring(0, un);
    return new S(s,L(),n);
    }
  @Override public String visitCsP(CsPContext ctx) {
    String s=ctx.CsP().toString();
    
    return "P";
    }
  public static boolean isThisn(String name) {
    if (name.equals("This")) {return true;}
    if (name.equals("This0")) {return true;}
    if (!name.startsWith("This")) {return false;}
    int firstN = "This".length();
    char c = name.charAt(firstN);
    // first is 1--9 and all rest is 0-9
    if ("123456789".indexOf(c) == -1) {return false;}
    for (int i :range(firstN + 1,name.length())) {
      if ("0123456789".indexOf(name.charAt(i)) == -1) {return false;}
      }
    return true;
  }

      @Override public String visitVoidE(VoidEContext ctx) {return "void";}
      @Override public String visitT(TContext ctx) {return "t("+c(ctx)+")";}
      @Override public String visitTLocal(TLocalContext ctx) {return c(ctx);}
      @Override public List<Full.VarTx> visitDX(DXContext ctx) {return c(ctx);}
      @Override public String visitDoc(DocContext ctx) {return "doc";}
      @Override public Full.K visitK(KContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public List<Full.T> visitWhoops(WhoopsContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitFullL(FullLContext ctx) {return "{"+c(ctx)+"}";}
      @Override public String visitFullM(FullMContext ctx) {return c(ctx);}
      @Override public String visitFullF(FullFContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitHeader(HeaderContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitFullMi(FullMiContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitFullMWT(FullMWTContext ctx) {return c(ctx);}
      @Override public String visitFullMH(FullMHContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitMOp(MOpContext ctx) {return "mOp";}
      @Override public String visitFullNC(FullNCContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitSlash(SlashContext ctx) {return "\\";}
      @Override public String visitPathSel(PathSelContext ctx) {return "pathSel";}
      @Override public String visitCast(CastContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitSlashX(SlashXContext ctx) {return "\\x";}
      @Override public String visitEPostfix(EPostfixContext ctx) {return c(ctx);}
      @Override public String visitSquareCall(SquareCallContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitEBinary0(EBinary0Context ctx) {return (ctx.children.size()==1?"":"<")+c(ctx)+(ctx.children.size()==1?"":">");}
      @Override public String visitEBinary1(EBinary1Context ctx) {return (ctx.children.size()==1?"":"<")+c(ctx)+(ctx.children.size()==1?"":">");}
      @Override public String visitEBinary2(EBinary2Context ctx) {return (ctx.children.size()==1?"":"<")+c(ctx)+(ctx.children.size()==1?"":">");}
      @Override public String visitEBinary3(EBinary3Context ctx) {return (ctx.children.size()==1?"":"<")+c(ctx)+(ctx.children.size()==1?"":">");}
      @Override public String visitSIf(SIfContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitMatch(MatchContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitSWhile(SWhileContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitSFor(SForContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitSLoop(SLoopContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitSThrow(SThrowContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitSUpdate(SUpdateContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitInfo(InfoContext ctx) { return "info";}
}
