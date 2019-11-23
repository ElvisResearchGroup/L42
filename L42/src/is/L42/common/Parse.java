package is.L42.common;

import static is.L42.tools.General.L;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;
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
import is.L42.generated.L42Parser.NudePContext;
import is.L42.generated.LL;
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
    @Override public String toString() {
      if (hasErr()) {return errorsTokenizer+"\n"+errorsParser+"\n"+errorsVisitor;}
      return res.toString();
      }
    } 
    
  public static class FailConsole extends ConsoleErrorListener{
    public final StringBuilder sb;
    public final Path fileName;
    public FailConsole(Path fileName,StringBuilder sb){
      this.fileName=fileName;this.sb=sb;}
    @Override public void syntaxError(Recognizer<?, ?> r,Object o,int line,int charPos,String msg,RecognitionException e){
      sb.append(new Pos(fileName.toUri(),line,charPos)+ msg);
      }
    }
  private static <T>Result<T> doResult(Path fileName,Lexer l,Parser p, Supplier<T> s){
    StringBuilder errorst=new StringBuilder();
    StringBuilder errorsp=new StringBuilder();
    l.removeErrorListener(ConsoleErrorListener.INSTANCE);
    l.addErrorListener(new FailConsole(fileName,errorst));
    p.removeErrorListener(ConsoleErrorListener.INSTANCE);
    p.addErrorListener(new FailConsole(fileName,errorsp));
    var res=s.get();
    return new Result<>(errorst.toString(),errorsp.toString(),"",res);   
    }

  public static<A,B> Result<B> aux(Path fileName,String s,Function<L42Parser,A>a,BiFunction<FullL42Visitor,A,B>b){
    var l=new L42Lexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42Parser(t);
    Result<A> res1=doResult(fileName,l,p,()->a.apply(p));
    if(res1.hasErr()){
      return new Result<>(res1.errorsParser,res1.errorsTokenizer,res1.errorsVisitor,null);         
      }
    var v=new FullL42Visitor(fileName);
    B e=b.apply(v, res1.res);
    if(v.errors.length()!=0){
      return new Result<B>("","",v.errors.toString(),null);
      }
    var r=new Result<B>("","","",e);
    return r;    
    }
  public static Result<E> e(Path fileName,String s){
    return aux(fileName,s,p->p.nudeE(),(v,eCtx)->v.visitNudeE(eCtx));
    }
  public static Result<Full.CsP> csP(Path fileName,String s){
    return aux(fileName,s,p->p.nudeCsP(),(v,ctx)->v.visitNudeCsP(ctx));
    }
  public static Result<Program> program(Path fileName,String s){
    return aux(fileName,s,p->p.nudeP(),(v,eCtx)->v.visitNudeP(eCtx));
    }
  public static LL fromPath(Path path) throws IOException{
    if(Files.isDirectory(path)){path=path.resolve("This.L42");}
    String code=Files.readString(path,StandardCharsets.US_ASCII);
    code=code.replace("\r","");
    Program p=Parse.sureProgram(path,"{"+code+"\n}");
    return p.top;
    }
  public static Program sureProgram(Path fileName,String s){
    var res=aux(fileName,s,p->p.nudeP(),(v,eCtx)->v.visitNudeP(eCtx));
    if (res.hasErr()){throw new EndError.NotWellFormed(L(),res.toString());}
    assert res.res!=null;
    return res.res; 
    }
    
  public static Result<NudeEContext> ctxE(Path fileName,String s){
    var l=new L42Lexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42Parser(t);
    return doResult(fileName,l,p,()->p.nudeE());
    }

  public static Result<NudeCsPContext> ctxCsP(Path fileName,String s){
    var l=new L42AuxLexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42AuxParser(t);
    return doResult(fileName,l,p,()->p.nudeCsP());
    }    

  public static Result<TopDocContext> ctxDoc(Path fileName,String s){
    var l=new L42AuxLexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42AuxParser(t);
    return doResult(fileName,l,p,()->p.topDoc());
    }    
  public static Result<InfoContext> ctxInfo(Path fileName,String s){
    var l=new L42AuxLexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42AuxParser(t);
    return doResult(fileName,l,p,()->p.info());
    }
  public static Result<NudePathSelXContext> ctxPathSelX(Path fileName,String s){
    var l=new L42AuxLexer(CharStreams.fromString(s));
    var t = new CommonTokenStream(l);
    var p=new L42AuxParser(t);
    return doResult(fileName,l,p,()->p.nudePathSelX());
    } 
  }