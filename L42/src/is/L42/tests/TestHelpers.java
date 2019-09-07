package is.L42.tests;

import static is.L42.tools.General.bug;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import is.L42.common.Parse;
import is.L42.generated.L42Lexer;
import is.L42.generated.L42Parser;
import is.L42.generated.L42Visitor;
import is.L42.generated.L42Parser.BlockContext;
import is.L42.generated.L42Parser.CastContext;
import is.L42.generated.L42Parser.CsPContext;
import is.L42.generated.L42Parser.DContext;
import is.L42.generated.L42Parser.DXContext;
import is.L42.generated.L42Parser.DocContext;
import is.L42.generated.L42Parser.EAtomicContext;
import is.L42.generated.L42Parser.EBinary0Context;
import is.L42.generated.L42Parser.EBinary1Context;
import is.L42.generated.L42Parser.EBinary2Context;
import is.L42.generated.L42Parser.EBinary3Context;
import is.L42.generated.L42Parser.EContext;
import is.L42.generated.L42Parser.EPostfixContext;
import is.L42.generated.L42Parser.FCallContext;
import is.L42.generated.L42Parser.FullFContext;
import is.L42.generated.L42Parser.FullLContext;
import is.L42.generated.L42Parser.FullMContext;
import is.L42.generated.L42Parser.FullMHContext;
import is.L42.generated.L42Parser.FullMWTContext;
import is.L42.generated.L42Parser.FullMiContext;
import is.L42.generated.L42Parser.FullNCContext;
import is.L42.generated.L42Parser.HeaderContext;
import is.L42.generated.L42Parser.InfoContext;
import is.L42.generated.L42Parser.KContext;
import is.L42.generated.L42Parser.MContext;
import is.L42.generated.L42Parser.MOpContext;
import is.L42.generated.L42Parser.MatchContext;
import is.L42.generated.L42Parser.NudeCsPContext;
import is.L42.generated.L42Parser.NudeEContext;
import is.L42.generated.L42Parser.NudePContext;
import is.L42.generated.L42Parser.ORContext;
import is.L42.generated.L42Parser.ParContext;
import is.L42.generated.L42Parser.PathSelContext;
import is.L42.generated.L42Parser.SForContext;
import is.L42.generated.L42Parser.SIfContext;
import is.L42.generated.L42Parser.SLoopContext;
import is.L42.generated.L42Parser.SThrowContext;
import is.L42.generated.L42Parser.SUpdateContext;
import is.L42.generated.L42Parser.SWhileContext;
import is.L42.generated.L42Parser.SlashContext;
import is.L42.generated.L42Parser.SlashXContext;
import is.L42.generated.L42Parser.SquareCallContext;
import is.L42.generated.L42Parser.StringContext;
import is.L42.generated.L42Parser.TContext;
import is.L42.generated.L42Parser.TLocalContext;
import is.L42.generated.L42Parser.VoidEContext;
import is.L42.generated.L42Parser.WhoopsContext;
import is.L42.generated.L42Parser.XContext;

public class TestHelpers {
  public static String parseStructure(ParserRuleContext ctx){
    var visitor=new L42Visitor<String>(){
      String n(ParserRuleContext prc){
        var name=prc.getClass().getSimpleName();
        return name.substring(0,name.length()-"Context".length());
      }
      String c(ParserRuleContext prc){
        var sb=new StringBuilder();
        if(prc.children!=null)
          prc.children.forEach(c->sb.append(c.accept(this)));
        return sb.toString();
        }
      @Override public String visit(ParseTree arg0) {return null;}
      @Override public String visitChildren(RuleNode arg0) {return null;}
      @Override public String visitErrorNode(ErrorNode arg0) {return "ERROR";}
      @Override public String visitTerminal(TerminalNode arg0) {return "|";}
      @Override public String visitE(EContext ctx) {return c(ctx);}
      @Override public String visitPar(ParContext ctx) {return c(ctx);}
      @Override public String visitOR(ORContext ctx) {return "|";}
      @Override public String visitString(StringContext ctx) {return "|";}
      @Override public String visitD(DContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitEAtomic(EAtomicContext ctx) {return c(ctx);}
      @Override public String visitX(XContext ctx) {return "x";}
      @Override public String visitFCall(FCallContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitNudeE(NudeEContext ctx) {return c(ctx);}
      @Override public String visitBlock(BlockContext ctx) {return "["+c(ctx)+"]";}
      @Override public String visitM(MContext ctx) {return "m";}
      @Override public String visitCsP(CsPContext ctx) {return "P";}
      @Override public String visitVoidE(VoidEContext ctx) {return "void";}
      @Override public String visitT(TContext ctx) {return "t("+c(ctx)+")";}
      @Override public String visitTLocal(TLocalContext ctx) {return c(ctx);}
      @Override public String visitDX(DXContext ctx) {return c(ctx);}
      @Override public String visitDoc(DocContext ctx) {return "doc";}
      @Override public String visitK(KContext ctx) {return n(ctx)+"("+c(ctx)+")";}
      @Override public String visitWhoops(WhoopsContext ctx) {return n(ctx)+"("+c(ctx)+")";}
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
      @Override public String visitNudeP(NudePContext ctx) { throw bug();}
      @Override public String visitNudeCsP(NudeCsPContext ctx) { throw bug();}
      };
    return ctx.accept(visitor).toString();
    }
  public static NudeEContext parseWithException(String s){
    var res=Parse.ctxE("--dummy--",s);
    try{return res.res;}
    finally{
      if(res.errorsTokenizer.length()!=0)throw new RuntimeException("Tokenizer errors:\n"+res.errorsTokenizer);
      if(res.errorsParser.length()!=0)throw new RuntimeException("Parsing errors:\n"+res.errorsParser);
      }
    }
  }