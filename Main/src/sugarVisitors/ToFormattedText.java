package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import org.antlr.v4.codegen.model.chunk.TokenRef;
import coreVisitors.InjectionOnSugar;
import tools.Assertions;
import tools.StringBuilders;
import ast.Ast;
import ast.Ast.ConcreteHeader;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.MethodSelector;
import ast.Ast.Parameters;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.Ast.VarDec;
import ast.ExpCore.ClassB.Phase;
import ast.ExpCore;
import ast.Expression;
import ast.Expression.BinOp;
import ast.Expression.ClassB;
import ast.Expression.ClassB.Member;
import ast.Expression.ClassB.MethodWithType;
import ast.Expression.ClassB.NestedClass;
import ast.Expression.ClassReuse;
import ast.Expression.CurlyBlock;
import ast.Expression.DocE;
import ast.Expression.DotDotDot;
import ast.Expression.FCall;
import ast.Expression.ContextId;
import ast.Expression.If;
import ast.Expression.Loop;
import ast.Expression.MCall;
import ast.Expression.RoundBlock;
import ast.Expression.Signal;
import ast.Expression.SquareCall;
import ast.Expression.SquareWithCall;
import ast.Expression.Literal;
import ast.Expression.UnOp;
import ast.Expression.Using;
import ast.Expression.WalkBy;
import ast.Expression.While;
import ast.Expression.With;
import ast.Expression.X;
import ast.Expression._void;

import auxiliaryGrammar.Functions;

public class ToFormattedText extends l42FVisitors.ToFormattedText implements Visitor<Void>{
  private ToFormattedText(){}
  public static String of(Expression e){
    ToFormattedText tft=new ToFormattedText();
    e.accept(tft);
    return tft.result.toString();
  }
  public static String of(Type t){
    ToFormattedText tft=new ToFormattedText();
    tft.formatType(t);
    return tft.result.toString();
  }
  public static String of(Path path){
    ToFormattedText tft=new ToFormattedText();
    tft.liftP(path);
    return tft.result.toString();
  }
  public static String of(Doc doc){
    ToFormattedText tft=new ToFormattedText();
    tft.formatDoc(doc);
    return tft.result.toString();
  }

  public static String of(ExpCore e){
    return of(e.accept(new InjectionOnSugar()));
  }

  public static String of(ExpCore.ClassB.Member m){
    List<ExpCore.ClassB.Member> ms=new ArrayList<>();
    ms.add(m);
    ExpCore e=ExpCore.ClassB.membersClass(ms,m.getP(),Phase.None);
    Expression.ClassB es=(ClassB) e.accept(new InjectionOnSugar());
    ToFormattedText tft=new ToFormattedText();
    tft.formatMembers(es.getMs());//for the injection
    return tft.result.toString();
  }
  public static String of(Expression.ClassB.Member m){
      List<Expression.ClassB.Member> ms=new ArrayList<>();
      ms.add(m);
      ToFormattedText tft=new ToFormattedText();
      tft.formatMembers(ms);
      return tft.result.toString();
    }

  private Void separeFromChar(){
    char last=result.charAt(result.length()-1);
    if(Character.isLetter(last) || Character.isDigit(last) || last=='$' || last=='%'){
    result.append(" ");}return null;
    }
  @Override
  public Void visit(Signal arg0) {
    c(arg0.getKind().content);
    separeFromChar();
    return arg0.getInner().accept(this);
  }
  @Override
  public Void visit(Loop arg0) {
    c("loop ");
    return arg0.getInner().accept(this);

  }

  @Override
  public Void visit(If arg0) {
    c("if ");
    arg0.getCond().accept(this);
    sp();
    arg0.getThen().accept(this);
    if(!arg0.get_else().isPresent())return null;
    c("else ");
    arg0.get_else().get().accept(this);
    return null;
  }

  @Override
  public Void visit(While arg0) {
    c("while ");
    arg0.getCond().accept(this);
    sp();
    arg0.getThen().accept(this);
    return null;
  }

  @Override
  public Void visit(With s) {
    c("with");//ok no space
    if (!s.getXs().isEmpty()){c(" ");}
    StringBuilders.formatSequence(this.result,s.getXs().iterator(),", ",x->c(x));

    if (!s.getIs().isEmpty()){c(" ");}
    StringBuilders.formatSequence(this.result,s.getIs().iterator(),", ",is->{
      if(is.isVar()){c("var ");}
      formatType(is.getT());
      separeFromChar();
      c(is.getX());
      c(" in ");
      is.getInner().accept(this);
    });
    if (!s.getDecs().isEmpty()){c(" ");}
    StringBuilders.formatSequence(this.result,s.getDecs().iterator(),", ",xe->{
      if(xe.isVar()){c("var ");}
      formatType(xe.getT());
      separeFromChar();
      c(xe.getX());
      c("= ");
      xe.getInner().accept(this);
    });
    c(" (");nl();
    for(With.On on:s.getOns()){
      c("on ");
      StringBuilders.formatSequence(this.result,on.getTs().iterator(),", ",t->formatType(t));
      sp();
      on.getInner().accept(this);
      nl();
    }
    if(s.getDefaultE().isPresent()){
      separeFromChar();
      if(!s.getOns().isEmpty()){c("default");separeFromChar();}
      s.getDefaultE().get().accept(this);
    }
    return c(")");//throw Assertions.codeNotReachable();
  }

  @Override
  public Void visit(X arg0) {
    return c(arg0.getInner());
  }

  @Override
  public Void visit(BinOp arg0) {
    arg0.getLeft().accept(this);
    sp();
    c(arg0.getOp().inner);
    sp();
    return arg0.getRight().accept(this);

  }

  @Override
  public Void visit(DocE arg0) {
    arg0.getInner().accept(this);
    return formatDoc(arg0.getDoc());
    }

  @Override
  public Void visit(UnOp arg0) {
    c(arg0.getOp().inner);
    return arg0.getInner().accept(this);
    }

  @Override
  public Void visit(MCall arg0) {
    if(arg0.getReceiver() instanceof Expression.Signal){c("(");}
    arg0.getReceiver().accept(this);
    if(arg0.getReceiver() instanceof Expression.Signal){c(")");}
    c(".");
    assert !arg0.getName().isEmpty();
    c(arg0.getName());
    c("(");
    formatDoc(arg0.getDoc());
    formatParameters(arg0.getPs());
    return c(")");
    }

   @Override
  public Void visit(FCall arg0) {
    arg0.getReceiver().accept(this);
    c("(");
    formatDoc(arg0.getDoc());
    formatParameters(arg0.getPs());
    return c(")");
    }
   private void formatParameters(Parameters ps) {
     assert ps.getEs().size()==ps.getXs().size();
     if(ps.getE().isPresent()){ps.getE().get().accept(this);
       if(!ps.getEs().isEmpty()){c(", ");}
       }
     StringBuilders.formatSequence(this.result,ps.getXs().iterator(),ps.getEs().iterator(),", ",
       (x,e)->{
       c(x);
       c(":");
       e.accept(this);
       });
     }

  @Override
  public Void visit(SquareCall arg0) {
    arg0.getReceiver().accept(this);
    return formatSquarePart(arg0);
    }
  private Void formatSquarePart(SquareCall arg0) {
    c("[");
    arg0.getDoc();
    indent();
    StringBuilders.formatSequence(result, arg0.getPss().iterator(), arg0.getDocs().iterator(),
        "\n",(ps,doc)->{
          formatParameters(ps);
          c(";");
          formatDoc(doc);
        });
    c("]");
    return deIndent();
  }
  @Override
  public Void visit(SquareWithCall arg0) {
    arg0.getReceiver().accept(this);
    c("[");
    arg0.getWith().accept(this);
    return c("]");
    }
  @Override
  public Void visit(Expression.UseSquare arg0) {
    Expression inner=arg0.getInner();
    if (inner instanceof Expression.With){
      c("use [");
      inner.accept(this);
      return c("]");
    }
    assert inner instanceof Expression.SquareCall;
    c("use");
    return formatSquarePart((Expression.SquareCall)inner);
    }

  @Override
  public Void visit(RoundBlock arg0) {
    c("(");
    formatDoc(arg0.getDoc());
    generateContent(arg0.getContents());
    arg0.getInner().accept(this);
    if(!arg0.getContents().isEmpty()){
      nl();c(")");deIndent();}
    else  c(")");
    return null;
  }

  private void generateContent(List<Expression.BlockContent> contents) {
    if(!contents.isEmpty()){
      indent();nl();}
    for(Expression.BlockContent bc :contents){
      for(VarDec vd: bc.getDecs()){
        vd.match(
          (v)->{
            if(v.isVar()){c("var ");}
            formatType(v.getT());
            separeFromChar();
            c(v.getX());
            c("=");
            return v.getInner().accept(this);
            },
          (v)->{
            return v.getInner().accept(this);
            },
          (v)->{
            c(v.getInner().getName().toString());
            c(":");
            formatDoc(v.getInner().getDoc());
            return v.getInner().getInner().accept(this);
            });
        nl();
      }
      if(!bc.get_catch().isEmpty()){nl();}
      for(Expression.Catch k : bc.get_catch()){
        generateCatch(k);
      }
    }
  }
  private void generateCatch(Expression.Catch catch1) {
    catch1.match(
        k1->{
          c("catch ");
          c(k1.getKind().content);
          sp();
          formatType(k1.getT());
          sp();
          c(k1.getX());
          sp();
          k1.getInner().accept(this);
          nl();
          return null;},
        kM->{
          c("catch ");
          c(kM.getKind().content);
          sp();
          for(Type t:kM.getTs()){
            formatType(t);
            }
          sp();
          kM.getInner().accept(this);
          return null;
        },
        kP->{
          sp();
          c(kP.getKind().content);
          c(" on ");
          for(Type t:kP.getTs()){
            formatType(t);
            }
          sp();
          kP.getInner().accept(this);
          return null;
        }
        );
    nl();
  }
  @Override
  public Void visit(CurlyBlock arg0) {
    c("{");
    formatDoc(arg0.getDoc());
    generateContent(arg0.getContents());
    if(!arg0.getContents().isEmpty()){
      c("}");deIndent();}
    else  c("}");
    return null;
    }

  @Override
  public Void visit(Using arg0) {
    c("use ");
    this.liftP(arg0.getPath());
    sp();
    c("check ");
    c(arg0.getName());
    c("(");
    formatDoc(arg0.getDocs());
    formatParameters(arg0.getPs());
    c(") ");
    return arg0.getInner().accept(this);
    }

  @Override
  public Void visit(ClassB cb) {
    c(" {");
    formatH(cb);
    cb.getFields().forEach(this::formatField);
    formatMembers(cb.getMs());
    c("}");
    separeFromChar();
    return null;
  }
  private void formatMembers(List<Member> ms) {
    for( Member m:ms){
      m.match(
      nestedClass->{nl();
        c(nestedClass.getName().toString());
        c(":");
        formatDoc(nestedClass.getDoc());
        return nestedClass.getInner().accept(this);
        },
      methImpl->{nl();
        c("method ");
        formatMs(methImpl.getS());
        return methImpl.getInner().accept(this);
        },
      methT->{nl();
        formatMts(methT);
        if(methT.getInner().isPresent()){
          methT.getInner().get().accept(this);
          }
        return null;
        });
    }
  }
  /*private Expression forceBlock(Expression e) {
    if (e instanceof Expression.RoundBlock){return e;}
    if (e instanceof Expression.CurlyBlock){return e;}
    return new Expression.RoundBlock("",e,Collections.emptyList());
  }*/
  private void formatMts(MethodWithType methT) {
    if(methT.getMt().isRefine()){c("refine ");}
    c(methT.getMt().getMdf().inner);
    separeFromChar();
    c("method ");
    if(!methT.getDoc().isEmpty()){formatDoc(methT.getDoc());}
    else{nl();}
    formatType(methT.getMt().getReturnType());
    separeFromChar();
    c(methT.getMs().nameToS());
    c("(");
    StringBuilders.formatSequence(this.result,methT.getMt().getTs().iterator(), methT.getMs().getNames().iterator(),", ",
      (ti,xi)->{
      formatType(ti.withDoc(Doc.empty()));
      separeFromChar();
      c(xi);
      formatDoc(ti.getDoc());
      });
    c(") ");
    if(methT.getMt().getExceptions().isEmpty()){return;}
    c("exception ");
    StringBuilders.formatSequence(this.result,methT.getMt().getExceptions().iterator(),", ",
        p->formatType(p));
    sp();
  }
  private void formatMs(MethodSelector s) {
    c(s.nameToS());
    c("(");
    for( String ss:s.getNames()){
      c(ss);
      sp();
    }
    c(") ");
  }
  private void formatH(ClassB arg0) {
    formatDoc(arg0.getDoc1());
    arg0.getH().match(
      h->{//concrete header
        c(h.getMdf().inner);
        sp();
        c(h.getName());
        c("(");
        StringBuilders.formatSequence(result,h.getFs().iterator(),
            ", ",this::formatField);
        return c(")");},
      //trait
      h->{return null;},
      //inteface
      h->{return c("interface ");});
    if(!arg0.getSupertypes().isEmpty()){
      c("implements ");
      StringBuilders.formatSequence(this.result,arg0.getSupertypes().iterator(),", ",
        p->formatType(p));
      }
  }
  private void formatField(FieldDec v) {
   sp();
   if(v.isVar()){c("var ");}
   formatType(v.getT());
   sp();
   c(v.getName());
   formatDoc(v.getDoc());
   sp();
  }
  private Void formatDoc(Doc d) {return c(d.toCodeFormattedString());}
  private Void formatType(Optional<Type> t) {
  if(t.isPresent()){
    return formatType(t.get());
    }
  return null;
  }
  private Void formatType(Type t) {
    if(ast.Ast.Mdf.Immutable!=t.getMdf()){
      c(t.getMdf().inner);sp();
      }
    liftP(t.getPath());
    formatDoc(t.getDoc());
    return null;
  }
  @Override
  public Void visit(DotDotDot arg0) {
    return c(" ... ");
    }

  @Override
  public Void visit(_void arg0) {
    return c("void");  }

  @Override
  public Void visit(Literal arg0) {
    if(!arg0.isNumber()){
      arg0.getReceiver().accept(this);
      c("\"");
      if(!arg0.getInner().contains("\n")){
        c(arg0.getInner());
        return c("\"");
        }
        String[] splitted = arg0.getInner().split("\n", -1);
        for(String s:splitted){c("'");c(s);nl();}
        return c("\"");
      }
    c(arg0.getInner());
    return arg0.getReceiver().accept(this);
    }

  @Override
  public Void visit(Expression.EPath path) {
    return liftP(path.getInner());
  }

  protected Void liftP(Path path) {
    if(path.equals(Path.Any())){return c("Any");}
    if(path.equals(Path.Library())){return c("Library");}
    if(path.equals(Path.Void())){return c("Void");}

    if(path.isCore()){
      c("This"+path.outerNumber());
      for(Ast.C ci:path.getCBar()){
        c("."+ci);
        }
      return null;
      }
    StringBuilders.formatSequence(this.result,
      path.sugarNames().iterator(),".",s->c(s.toString()));
    return null;
  }
  @Override
  public Void visit(WalkBy s) {
    return c("##walkBy");
  }
  @Override
  public Void visit(ClassReuse s) {
    c("{");
    formatDoc(s.getInner().getDoc1());
    c(s.getUrl());
    if(s.getInner().getMs().isEmpty()){return c("}");}
    indent();
    nl();
    formatMembers(s.getInner().getMs());
    nl();
    c("}");
    return deIndent();
  }
  public static String ofCompact(Expression e) { return ofCompact(e,true); }
  public static String ofCompact(Expression e,boolean inline) {
    ToFormattedText tft=new ToFormattedText(){
      public Void visit(ClassB cb){
        //String res="";
        String wb="";
        for(Member m:cb.getMs()){
          if(m instanceof NestedClass){
            NestedClass nc=(NestedClass)m;
            if(nc.getInner() instanceof WalkBy){wb=nc.getName()+":##";}
            }
        }
        return c("{.."+wb+"..}");
      }
    };
    e.accept(tft);
    String res=tft.result.toString();
    if(inline){res=res.replace("\n", " ");}
    //if(res.length()>80){ res=res.substring(0, 34)+" ... "+res.substring(res.length()-34); }
    return res;
  }
  @Override public Void visit(ContextId s) {
    return c(s.getInner());
  }
}
