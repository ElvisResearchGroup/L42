package is.L42.common;

import java.util.function.Supplier;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import is.L42.generated.Full;
import is.L42.generated.L42AuxLexer;
import is.L42.generated.L42AuxParser;
import is.L42.generated.L42AuxParser.CsPContext;
import is.L42.generated.L42Lexer;
import is.L42.generated.L42Parser;
import is.L42.generated.L42Parser.NudeEContext;

public class Parse {
  static{Constants.class.getName();}
  public static class Result<T>{
    public final String errorsTokenizer;
    public final String errorsParser;
    public final T res;
    private Result(String errorst, String errorsp, T res){
      this.errorsTokenizer=errorst;
      this.errorsParser=errorsp;
      this.res=res;
      }
    public boolean hasErr(){return !errorsTokenizer.isEmpty() || !errorsParser.isEmpty();}
    } 
  public static class FailConsole extends ConsoleErrorListener{
    public final StringBuilder sb;
    public FailConsole(StringBuilder sb){this.sb=sb;}
    @Override public void syntaxError(Recognizer<?, ?> r,Object o,int line,int charPos,String msg,RecognitionException e){
      sb.append("line " + line + ":" + charPos + " " + msg);
      }
    }
  private static <T>Result<T> doResult(Lexer l,Parser p, Supplier<T> s){
    StringBuilder errorst=new StringBuilder();
    StringBuilder errorsp=new StringBuilder();
    l.removeErrorListener(ConsoleErrorListener.INSTANCE);
    l.addErrorListener(new FailConsole(errorst));
    p.removeErrorListener(ConsoleErrorListener.INSTANCE);
    p.addErrorListener(new FailConsole(errorsp));
    var res=s.get();
    return new Result<>(errorst.toString(),errorsp.toString(),res);   
    }
  public static Result<NudeEContext> e(String s){
    var l=new L42Lexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42Parser(t);
    return doResult(l,p,()->p.nudeE());
    }
  public static Result<CsPContext> csP(String s){
    var l=new L42AuxLexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42AuxParser(t);
    return doResult(l,p,()->p.csP());
    }    
    
  }