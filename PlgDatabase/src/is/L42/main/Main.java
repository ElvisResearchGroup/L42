package is.L42.main;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import generated.L42Parser.EContext;
import generated.L42Parser.NoSpaceContext;
import generated.L42Parser.SpaceContext;
import generated.L42Visitor;

public class Main {

  public static void main(String[] args) {
    var visitor=new L42Visitor<String>(){
      @Override public String visit(ParseTree arg0) {return null;}
      @Override public String visitChildren(RuleNode arg0) {return null;}
      @Override public String visitErrorNode(ErrorNode arg0) {return null;}
      @Override public String visitTerminal(TerminalNode arg0) {return null;}
      @Override public String visitE(EContext ctx) {
        System.out.println("e");ctx.children.forEach(c->c.accept(this));return null;}
      @Override public String visitSpace(SpaceContext ctx) {System.out.println("space");ctx.children.forEach(c->c.accept(this));return null;}
      @Override public String visitNoSpace(NoSpaceContext ctx) {System.out.println("noSpace");ctx.children.forEach(c->c.accept(this));return null;}
      //@Override public String visitORNoSpace(ORNoSpaceContext ctx) {return null;}
      //@Override public String visitORSpace(ORSpaceContext ctx) {return null;}
      };
    getParser("Cacca (Babba Bu(C ( $)))").e().accept(visitor);
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
