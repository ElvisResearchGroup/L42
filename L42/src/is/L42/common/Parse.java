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
import is.L42.generated.Full.E;
import is.L42.generated.L42AuxLexer;
import is.L42.generated.L42AuxParser;
import is.L42.generated.L42AuxParser.CsPContext;
import is.L42.generated.L42AuxParser.DocContext;
import is.L42.generated.L42AuxParser.InfoContext;
import is.L42.generated.L42AuxParser.NudeCsPContext;
import is.L42.generated.L42AuxParser.NudePathSelXContext;
import is.L42.generated.L42AuxParser.TopDocContext;
import is.L42.generated.L42Lexer;
import is.L42.generated.L42Parser;
import is.L42.generated.L42Parser.NudeEContext;
import is.L42.generated.Pos;
import is.L42.visitors.FullL42Visitor;

public class Parse {
  static{Constants.toS.getClass();}//initialize Constants class
  
  public static class Result<T>{
    public final String errorsTokenizer;
    public final String errorsParser;
    public final String errorsVisitor;
    public final T res;
    public Result(String errorst, String errorsp, String errorsv, T res){
      this.errorsTokenizer=errorst;
      this.errorsParser=errorsp;
      this.errorsVisitor=errorsv;
      this.res=res;
      }
    public boolean hasErr(){return !errorsTokenizer.isEmpty() || !errorsParser.isEmpty() || !errorsVisitor.isEmpty();}
    } 
    
  public static class FailConsole extends ConsoleErrorListener{
    public final StringBuilder sb;
    public final String fileName;
    public FailConsole(String fileName,StringBuilder sb){
      this.fileName=fileName;this.sb=sb;}
    @Override public void syntaxError(Recognizer<?, ?> r,Object o,int line,int charPos,String msg,RecognitionException e){
      sb.append(new Pos(fileName,line,charPos)+ msg);
      }
    }
    
  private static <T>Result<T> doResult(String fileName,Lexer l,Parser p, Supplier<T> s){
    StringBuilder errorst=new StringBuilder();
    StringBuilder errorsp=new StringBuilder();
    l.removeErrorListener(ConsoleErrorListener.INSTANCE);
    l.addErrorListener(new FailConsole(fileName,errorst));
    p.removeErrorListener(ConsoleErrorListener.INSTANCE);
    p.addErrorListener(new FailConsole(fileName,errorsp));
    var res=s.get();
    return new Result<>(errorst.toString(),errorsp.toString(),"",res);   
    }
    
  public static Result<E> e(String fileName,String s){
    var l=new L42Lexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42Parser(t);
    var v=new FullL42Visitor(fileName);
    Result<NudeEContext> res1=doResult(fileName,l,p,()->p.nudeE());
    if(res1.hasErr()){
      return new Result<>(res1.errorsParser,res1.errorsTokenizer,res1.errorsVisitor,null);         
      }
    E e=v.visitNudeE(res1.res);
    if(v.errors.length()!=0){
      return new Result<E>("","",v.errors.toString(),null);
      }
    return new Result<E>("","","",e);
    }
    
  public static Result<NudeEContext> ctxE(String fileName,String s){
    var l=new L42Lexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42Parser(t);
    return doResult(fileName,l,p,()->p.nudeE());
    }

  public static Result<NudeCsPContext> csP(String fileName,String s){
    var l=new L42AuxLexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42AuxParser(t);
    return doResult(fileName,l,p,()->p.nudeCsP());
    }    

  public static Result<TopDocContext> doc(String fileName,String s){
    var l=new L42AuxLexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42AuxParser(t);
    return doResult(fileName,l,p,()->p.topDoc());
    }    
  public static Result<InfoContext> info(String fileName,String s){
    var l=new L42AuxLexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42AuxParser(t);
    return doResult(fileName,l,p,()->p.info());
    }
  public static Result<NudePathSelXContext> pathSelX(String fileName,String s){
    var l=new L42AuxLexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42AuxParser(t);
    return doResult(fileName,l,p,()->p.nudePathSelX());
    } 

  }