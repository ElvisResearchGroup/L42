package is.L42.tests;

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

import is.L42.generated.L42Lexer;
import is.L42.generated.L42Parser;
import is.L42.generated.L42Visitor;
import is.L42.generated.L42Parser.BlockContext;
import is.L42.generated.L42Parser.CsPContext;
import is.L42.generated.L42Parser.DContext;
import is.L42.generated.L42Parser.DXContext;
import is.L42.generated.L42Parser.DocContext;
import is.L42.generated.L42Parser.EAtomicContext;
import is.L42.generated.L42Parser.EContext;
import is.L42.generated.L42Parser.FCallContext;
import is.L42.generated.L42Parser.KContext;
import is.L42.generated.L42Parser.MContext;
import is.L42.generated.L42Parser.NudeEContext;
import is.L42.generated.L42Parser.ORContext;
import is.L42.generated.L42Parser.ParContext;
import is.L42.generated.L42Parser.StringContext;
import is.L42.generated.L42Parser.TContext;
import is.L42.generated.L42Parser.TLocalContext;
import is.L42.generated.L42Parser.VoidEContext;
import is.L42.generated.L42Parser.WhoopsContext;
import is.L42.generated.L42Parser.XContext;

public class TestHelpers {
  public static String parseStructure(ParserRuleContext ctx){
    var visitor=new L42Visitor<StringBuilder>(){
      StringBuilder logVisit(ParserRuleContext prc){
        var sb=new StringBuilder();
        var name=prc.getClass().getSimpleName();
        name=name.substring(0,name.length()-"Context".length());
        sb.append(name+"(");
        if(prc.children!=null)
          prc.children.forEach(c->sb.append(c.accept(this)));
        sb.append(")");
        return sb;
        }
      @Override public StringBuilder visit(ParseTree arg0) {return null;}
      @Override public StringBuilder visitChildren(RuleNode arg0) {return null;}
      @Override public StringBuilder visitErrorNode(ErrorNode arg0) {return new StringBuilder("ERROR");}
      @Override public StringBuilder visitTerminal(TerminalNode arg0) {return new StringBuilder("|");}
      @Override public StringBuilder visitE(EContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitPar(ParContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitOR(ORContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitString(StringContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitD(DContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitEAtomic(EAtomicContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitX(XContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitFCall(FCallContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitNudeE(NudeEContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitBlock(BlockContext ctx) {var r=logVisit(ctx);r.append("ENDBLOCK");return r;}
      @Override public StringBuilder visitM(MContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitCsP(CsPContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitVoidE(VoidEContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitT(TContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitTLocal(TLocalContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitDX(DXContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitDoc(DocContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitK(KContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitWhoops(WhoopsContext ctx) {return logVisit(ctx);}
      };
    return ctx.accept(visitor).toString();
    }
  public static NudeEContext parseWithException(String s){
    StringBuilder errorst=new StringBuilder();
    StringBuilder errorsp=new StringBuilder();
    var in = CharStreams.fromString(s);
    var l=new L42Lexer(in);
    l.removeErrorListener(ConsoleErrorListener.INSTANCE);
    l.addErrorListener(new FailConsole(errorst));
    var t = new CommonTokenStream(l);
    var p=new L42Parser(t);
    p.removeErrorListener(ConsoleErrorListener.INSTANCE);
    p.addErrorListener(new FailConsole(errorsp));
    try{return p.nudeE();}
    finally{
      if(errorst.length()!=0)throw new RuntimeException("Tokenizer errors:\n"+errorst);
      if(errorsp.length()!=0)throw new RuntimeException("Parsing errors:\n"+errorsp);
      }
    }
  }
class FailConsole extends ConsoleErrorListener{
  public final StringBuilder sb;
  public FailConsole(StringBuilder sb){this.sb=sb;}
  @Override public void syntaxError(Recognizer<?, ?> r,Object o,int line,int charPos,String msg,RecognitionException e){
    sb.append("line " + line + ":" + charPos + " " + msg);
    }
  }