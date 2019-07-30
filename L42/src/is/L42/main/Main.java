package is.L42.main;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import is.L42.generated.L42Parser.BlockContext;
import is.L42.generated.L42Parser.DContext;
import is.L42.generated.L42Parser.EAtomicContext;
import is.L42.generated.L42Parser.EContext;
import is.L42.generated.L42Parser.FCallContext;
import is.L42.generated.L42Parser.MContext;
import is.L42.generated.L42Parser.NudeEContext;
import is.L42.generated.L42Parser.ORContext;
import is.L42.generated.L42Parser.ParContext;
import is.L42.generated.L42Parser.StringContext;
import is.L42.generated.L42Parser.XContext;
import is.L42.generated.L42Visitor;

public class Main {
  public static void main(String[] args) {
    var visitor=new L42Visitor<StringBuilder>(){
      StringBuilder logVisit(ParserRuleContext prc){
        var sb=new StringBuilder();
        sb.append(prc.getClass().getSimpleName());
        prc.children.forEach(c->sb.append(c.accept(this)));
        return sb;
      }
      @Override public StringBuilder visit(ParseTree arg0) {return null;}
      @Override public StringBuilder visitChildren(RuleNode arg0) {return null;}
      @Override public StringBuilder visitErrorNode(ErrorNode arg0) {return null;}
      @Override public StringBuilder visitTerminal(TerminalNode arg0) {return null;}
      @Override public StringBuilder visitE(EContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitPar(ParContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitOR(ORContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitString(StringContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitD(DContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitEAtomic(EAtomicContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitX(XContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitFCall(FCallContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitNudeE(NudeEContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitBlock(BlockContext ctx) {return logVisit(ctx);}
      @Override public StringBuilder visitM(MContext ctx) {return logVisit(ctx);}
      };
    System.out.println(getParser("Cacca (Babba Bu(C ( $)))").eTop().accept(visitor));
  }
  public static antlrGenerated.L42Parser getParser(String s){
    CharStream in = CharStreams.fromString(s);
    antlrGenerated.L42Lexer l=new antlrGenerated.L42Lexer(in);
    //l.removeErrorListener(ConsoleErrorListener.INSTANCE);
    CommonTokenStream t = new CommonTokenStream(l);
    antlrGenerated.L42Parser p=new antlrGenerated.L42Parser(t);
    //p.removeErrorListener(ConsoleErrorListener.INSTANCE);
    return p;
  }
}
