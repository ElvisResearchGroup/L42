package sugarVisitors;

import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlrGenerated.L42Lexer;
import antlrGenerated.L42Parser.*;
import ast.Ast;
import ast.Expression;
import ast.Ast.*;
import tools.*;
import ast.Ast.BlockContent;
import ast.Ast.Catch;
import ast.Ast.ConcreteHeader;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.InterfaceHeader;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodSelectorX;
import ast.Ast.NormType;
import ast.Ast.On;
import ast.Ast.Op;
import ast.Ast.Parameters;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.Ast.TraitHeader;
import ast.Ast.Type;
import ast.Ast.VarDec;
import ast.Ast.VarDecCE;
import ast.Ast.VarDecE;
import ast.Ast.VarDecXE;
import ast.Expression.BinOp;
import ast.Expression.ClassB;
import ast.Expression.CurlyBlock;
import ast.Expression.DocE;
import ast.Expression.DotDotDot;
import ast.Expression.FCall;
import ast.Expression.If;
import ast.Expression.MCall;
import ast.Expression.RoundBlock;
import ast.Expression.Signal;
import ast.Expression.SquareCall;
import ast.Expression.SquareWithCall;
import ast.Expression.Literal;
import ast.Expression.UnOp;
import ast.Expression.Using;
import ast.Expression.While;
import ast.Expression.With;
import ast.Expression.X;
import ast.Expression._void;
import ast.Expression.ClassB.*;

public class ToAst extends AbstractVisitor<Expression>{

  private final class ToAstForMembers extends AbstractVisitor<Member> {
    @Override public Member visitMember(MemberContext ctx) {
      return ctx.children.get(0).accept(this);}

    @Override public Member visitMethodWithType(MethodWithTypeContext ctx) {
      MhtContext h = ctx.mht();
      Doc doc1=(h.docsOpt().get(0)==null)?Doc.empty():comm(h.docsOpt().get(0));
      Doc doc2=(ctx.docsOpt()==null)?Doc.empty():comm(ctx.docsOpt());
      String name=(h.mDec()==null)?"":h.mDec().getText();
      Mdf mdf=Ast.Mdf.fromString((h.Mdf()==null)?"":h.Mdf().getText());
      List<Type> ts=new ArrayList<>();
      List<Doc> tdocs=new ArrayList<>();
      List<String> names=new ArrayList<>();
      Iterator<TContext> tit=h.t().iterator();
      Iterator<DocsOptContext> dit=h.docsOpt().iterator();
      dit.next();//jump first, called doc1 already
      Type returnType=parseType(tit.next());
      for(XContext x : h.x()){
        names.add(x.getText());
        ts.add(parseType(tit.next()));
        tdocs.add(comm(dit.next()));
      }
      MethodSelector s=new MethodSelector(name, names);
      List<Path> exceptions=new ArrayList<Path>();
      for(TerminalNode p:h.Path()){exceptions.add(Path.parse(p.getText()));}
      Optional<Expression> inner=Optional.empty();
      if(ctx.eTopForMethod()!=null){inner=Optional.of(ctx.eTopForMethod().accept(ToAst.this));}
      MethodType mt=new MethodType(doc2,mdf,ts,tdocs,returnType,exceptions);
      return new MethodWithType(doc1,s, mt, inner,position(ctx));
    }

    @Override public Member visitNestedClass(NestedClassContext ctx) {
      Doc doc=comm(ctx.docsOpt());
      if(ctx.Path().getText().contains("::")){throw Assertions.userErrorAssert("no ::");}//TODO:improve
      String name=ctx.Path().getText();
      Expression inner=ctx.eTop().accept(ToAst.this);
      return new NestedClass(doc, name, inner,position(ctx));
    }

    @Override public Member visitMethodImplemented(MethodImplementedContext ctx) {
      Doc doc=(ctx.mhs().docsOpt()==null)?Doc.empty():comm(ctx.mhs().docsOpt());
      return new MethodImplemented(doc,parseMethSelector(ctx.mhs().methSelector()),ctx.eTopForMethod().accept(ToAst.this),position(ctx));
      }
  }
  @Override public Expression visitW(WContext ctx) {
    return ctx.children.get(0).accept(this);
  }
  @Override public Expression visitWSimple(WSimpleContext ctx) {
    List<String> xs = new ArrayList<String>();
    List<VarDecXE> is = new ArrayList<VarDecXE>();
    for( IContext i:ctx.i()){is.add(parseI(i));}
    List<VarDecXE> decs = new ArrayList<VarDecXE>();
    List<On> ons=new ArrayList<On>();
    Optional<Expression> defaultE=Optional.of(ctx.block().accept(this));
    return new Expression.With(position(ctx),xs, is, decs, ons, defaultE);
  }
  public Position position(ParserRuleContext ctx) {
    //assert facade.Parser.fileName!=null;
    Position p=new Position(facade.Parser.getFileName(),ctx.start.getLine(),ctx.start.getCharPositionInLine(),ctx.stop.getLine(),ctx.stop.getCharPositionInLine());
    return p;
  }

  @Override public Expression visitWSwitch(WSwitchContext ctx) {
    List<String> xs = new ArrayList<String>();
    for( XContext x:ctx.x()){xs.add(x.getText());}
    List<VarDecXE> is = new ArrayList<VarDecXE>();
    for( IContext i:ctx.i()){is.add(parseI(i));}
    List<VarDecXE> decs = new ArrayList<VarDecXE>();
    for( VarDecContext vd:ctx.varDec()){decs.add(parseRealVDec(vd));}
    List<On> ons=new ArrayList<On>();
    for( OnPlusContext on:ctx.onPlus()){ons.add(parseOnPlus(on));}
    Optional<Expression> defaultE=Optional.empty();
    if(ctx.eTop()!=null)defaultE=Optional.of(ctx.eTop().accept(this));
    return new Expression.With(position(ctx),xs, is, decs, ons, defaultE);
  }


  public static String nameK(TerminalNode s){
    return nameK(s.getText());
  }
  public static String nameK(String c){
    return c;
  }

  public static String nameL(ParseTree s){
    return nameL(s.getText());
  }
  public static String nameL(Token s){
    return nameL(s.getText());
  }
  public static String nameL(String c){
    return c;
  }
  public static String nameU(ParseTree s){
    return nameU(s.getText());
  }
  public static String nameU(String c){
    return c;
  }
  public static Doc comm(ParseTree s){
    if(s==null){return Doc.empty();}//as for empty comment string
    return comm(s.getText());
  }
  public static Doc comm(String c){
    if(c.isEmpty()){return Doc.empty();}
    assert c.startsWith("\'"):c;
    StringBuffer res=new StringBuffer();
    boolean skip=false;
    for(char cc:c.substring(1).toCharArray()){
      if(!skip){res.append(cc);}
      if(cc=='\n'){skip=true;}
      if(cc=='\''){skip=false;}
    }
    String result=res.toString();
    assert result.charAt(result.length()-1)=='\n':result;
//    res.append("\n");
    return Doc.factory(res.toString());
  }

  //TODO: check if is needed
  @Override public Expression visitTerminal(TerminalNode arg0) {
    Token t=(Token)arg0.getPayload();
    switch(t.getType()){
      case L42Lexer.X: return new Expression.X(nameL(t));
      case L42Lexer.UnOp:
      case L42Lexer.BoolOp:throw tools.Assertions.codeNotReachable(t.toString());
      default:throw tools.Assertions.codeNotReachable(t.toString());
    }
  }
  @Override public Expression visitBlock(BlockContext ctx) {
    return ctx.children.get(0).accept(this);
    }
  @Override public Expression visitCurlyBlock(CurlyBlockContext ctx) {
    Doc doc=comm(ctx.docsOpt());
    List<Ast.BlockContent> contents=new ArrayList<Ast.BlockContent>();
    for( BbContext b:ctx.bb()){
      List<VarDec> decs=new ArrayList<VarDec>();
      Optional<Catch> _catch=Optional.empty();
      for(DContext d:b.d()){decs.add(parseVDec(d));}
      if(b.k()!=null)_catch=Optional.of(parseK(b.k()));
      contents.add(new Ast.BlockContent(decs,_catch));
      }
    return new Expression.CurlyBlock(position(ctx),doc, contents);
  }
  private Expression visitRoundBlockAux(ParserRuleContext ctx,DocsOptContext docsOpt, List<BbContext> bB,ETopContext eTop) {
    Doc doc=comm(docsOpt);
    List<Ast.BlockContent> contents=new ArrayList<Ast.BlockContent>();
    for( BbContext b:bB){
      List<VarDec> decs=new ArrayList<VarDec>();
      Optional<Catch> _catch=Optional.empty();
      for(DContext d:b.d()){decs.add(parseVDec(d));}
      if(b.k()!=null)_catch=Optional.of(parseK(b.k()));
      contents.add(new Ast.BlockContent(decs,_catch));
    }
    Expression inner=eTop.accept(this);
    return new Expression.RoundBlock(position(ctx),doc, inner, contents);
  }
  @Override public Expression visitRoundBlock(RoundBlockContext ctx) {
    return visitRoundBlockAux(ctx,ctx.docsOpt(),ctx.bb(),ctx.eTop());
    }

  private Catch parseK(KContext k) {
    List<On> ons=new ArrayList<On>();
    for(OnContext on:k.on()){ons.add(parseOns(on));}
    String xk="";
    if(k.X()!=null){xk=nameL(k.X());}
    Optional<Expression> def=Optional.empty();
    if(k.eTop()!=null){def=Optional.of(k.eTop().accept(this));}
    return new Catch(
      SignalKind.fromString(nameK(k.S())),
      xk, ons, def);
  }
  private On parseOns(OnContext on) {
    Type t=parseType(on.t());
    Optional<Expression> _if=(on.Case()==null)?Optional.empty():
      Optional.of(on.eTop(0).accept(this));
    Expression inner=on.eTop((on.Case()==null)?0:1).accept(this);
    return new On(Collections.singletonList(t),_if,inner);
  }
  private On parseOnPlus(OnPlusContext on) {
    List<Type> ts=new ArrayList<Type>();
    for(TContext t: on.t()){ts.add(parseType(t));}
    Optional<Expression> _if=(on.Case()==null)?Optional.empty():
      Optional.of(on.eTop(0).accept(this));
    Expression inner=on.eTop((on.Case()==null)?0:1).accept(this);
    return new On(ts,_if,inner);
  }
  private VarDec parseVDec(DContext d) {
    if(d.nestedClass()!=null){
      return new Ast.VarDecCE((NestedClass)
          d.nestedClass().accept(this.new ToAstForMembers()));
      }
    if(d.eTop()!=null){
      return new Ast.VarDecE(d.eTop().accept(this));
      }
    assert d.varDec()!=null;
    return parseRealVDec(d.varDec());
    }
  private VarDecXE parseRealVDec(VarDecContext vd) {
    TContext tt=vd.t();
    Optional<Type> t=(tt==null)?Optional.<Type>empty():Optional.of(parseType(tt));
    return new Ast.VarDecXE(vd.Var()!=null,t,nameL(vd.x()),vd.eTop().accept(this));
    }
  private VarDecXE parseI(IContext vd) {
    TContext tt=vd.t();
    Optional<Type> t=(tt==null)?Optional.<Type>empty():Optional.of(parseType(tt));
    return new Ast.VarDecXE(vd.Var()!=null,t,nameL(vd.x()),vd.eTop().accept(this));
    }
  private Type parseType(TContext t) {
    if(t.concreteT()!=null){
      ConcreteTContext tt = t.concreteT();
      return new Ast.NormType(
        Mdf.fromString((tt.Mdf()==null)?"":nameK(tt.Mdf())),
        Ast.Path.parse(nameU(tt.Path())),
        (tt.Ph()==null)?Ph.None:Ph.Ph);
    }
    if(t.historicalT()!=null){
       HistoricalTContext tt = t.historicalT();
       Path p=ast.Ast.Path.parse(nameU(tt.Path()));
       List<MethodSelectorX> mss=new ArrayList<MethodSelectorX>();
       for(HistoricalSeqContext ms:tt.historicalSeq()){
         mss.add(parseMethSelectorX(ms));
       }
       return new ast.Ast.HistoricType(p,mss,false);
    }
    throw Assertions.codeNotReachable();
  }
  private MethodSelectorX parseMethSelectorX(HistoricalSeqContext ms) {
    String x="";if(ms.x()!=null) x=nameL(ms.x());
    return new MethodSelectorX(parseMethSelector(ms.methSelector()),x);
  }
  @Override public Expression visitNudeE(NudeEContext ctx) {
      return ctx.eTop().accept(this);}
  @Override public Expression visitX(XContext ctx) {
    assert ctx.children.size()==1: ctx.children.get(1).getClass();
    if(nameK(ctx.X()).equals("void")){return new Expression._void();}
    return new Expression.X(nameL(ctx.X()));
    }

  @Override public Expression visitEAtom(EAtomContext ctx) {
    if(ctx.Path()!=null){
      return addNumParse(ctx,Ast.Path.parse(nameU(ctx.Path())));
        }
    if(ctx.DotDotDot()!=null){
      return new Expression.DotDotDot();
        }
    if(ctx.WalkBy()!=null){
      return new Expression.WalkBy();
        }
    int i=0;
    if(ctx.numParse()!=null){i=1;}
    return addNumParse(ctx,ctx.children.get(i).accept(this));
    }

  @Override public Expression visitClassBReuse(ClassBReuseContext ctx) {
    Doc doc1=Doc.empty();
    if(ctx.docsOpt().size()>=1){doc1=comm(ctx.docsOpt().get(0));}
    Doc doc2=Doc.empty();
    if(ctx.docsOpt().size()>=2){doc2=comm(ctx.docsOpt().get(1));}
    assert ctx.getChild(0).getText().equals("{");
    assert ctx.getChild(2).getText().startsWith("reuse");
    String url=ctx.getChild(2).getText();
    url=url.trim();
    List<Member> ms=visitMembers(ctx.member());
    ClassB inner=new ClassB(doc1, doc2, new Ast.TraitHeader(),Collections.emptyList(), ms, Stage.None);
    return new Expression.ClassReuse(inner,url,null);
  }
  @Override public Expression visitClassB(ClassBContext ctx) {
    Doc doc1=comm(ctx.docsOpt().get(0));
    Doc doc2=comm(ctx.docsOpt().get(1));
    Header h=parseHeader(ctx.header());
    List<Path> supertypes= new ArrayList<Path>();
    for(TerminalNode p: ctx.Path()){supertypes.add(Path.parse(nameU(p)));}
    List<Member> ms=visitMembers(ctx.member());
    Stage s=Stage.None;
    if(ctx.classBExtra()!=null){
      if(ctx.classBExtra().Stage()!=null){
        s=Stage.fromString(ctx.classBExtra().Stage().toString());
        }
      //for( TerminalNode sp:ctx.classBExtra().Path()){allP.add(Path.parse(nameU(sp)));}
    }
    return new Expression.ClassB(doc1,doc2, h,supertypes, ms,s);
  }
  public List<Member> visitMembers(List<MemberContext> ctxms){
    List<Member> members=new ArrayList<Member>();
    for(MemberContext m:ctxms){members.add(m.accept(
      new ToAstForMembers()));}
    return members;
  }
  private MethodSelector parseMethSelector(MethSelectorContext ctx) {
    List<String> xs=new ArrayList<String>();
    for(XContext x:ctx.x()){xs.add(nameL(x));}
    String name="";
    if(ctx.mDec()!=null){name=nameL(ctx.mDec());}
    return new MethodSelector(name,xs);
  }
  private Header parseHeader(HeaderContext header) {
    if(header.Interface()!=null){return new Ast.InterfaceHeader();}
    if(header.CRound()==null){return new Ast.TraitHeader();}
    ast.Ast.Mdf mdf=ast.Ast.Mdf.fromString((header.Mdf()==null)?"":nameK(header.Mdf()));
    String name=(header.mDec()==null)?"":nameL(header.mDec());
    List<FieldDec> fields=new ArrayList<FieldDec>();
    for( FieldDecContext f:header.fieldDec()){fields.add(parseFieldDec(f));}
    return new Ast.ConcreteHeader(mdf,name, fields,position(header));
  }
  private FieldDec parseFieldDec(FieldDecContext f) {
    return new Ast.FieldDec(f.Var()!=null, parseType(f.t()),nameL(f.x()),comm(f.docsOpt()));
  }
  @Override public Expression visitSignalExpr(SignalExprContext ctx) {
    Expression inner=ctx.eTop().accept(this);
    SignalKind kind=SignalKind.fromString(nameK(ctx.S()));
    return new Expression.Signal(kind, inner);
    }
  @Override public Expression visitLoopExpr(LoopExprContext ctx) {
    Expression inner=ctx.eTop().accept(this);
    return new Expression.Loop(inner);
    }

  @Override public Expression visitIfExpr(IfExprContext ctx) {
    Expression cond=ctx.eTop(0).accept(this);
    Expression then=ctx.block().accept(this);
    Optional<Expression> _else=Optional.empty();
    assert ctx.eTop().size()<=2;
    if(ctx.eTop().size()==2)_else=Optional.of(ctx.eTop(1).accept(this));
    return new Expression.If(position(ctx),cond, then, _else);
  }
  @Override public Expression visitWhileExpr(WhileExprContext ctx) {
    Expression cond=ctx.eTop().accept(this);
    Expression then=ctx.block().accept(this);
    return new Expression.While(position(ctx),cond, then);

  }

  private Expression addNumParse(EAtomContext ctx,Expression e){
    if(ctx.numParse()!=null){
      e=new Expression.Literal(position(ctx),e, ctx.numParse().getText(),true);
      }
    return e;
  }
  @Override public Expression visitEUnOp(EUnOpContext ctx) {
      Expression e=ctx.ePost().accept(this);
      Token t=null;
      try{
        t=(Token)((TerminalNode)ctx.children.get(0)).getPayload();
      }catch(ClassCastException ignored){}
      if (t!=null&&t.getType()==L42Lexer.UnOp){
        e=new Expression.UnOp(position(ctx),Op.fromString(t.getText()), e);
        }
      return e;
      }
  @Override public Expression visitEL2(EL2Context ctx) {
    return visitBinOp(ctx);
  }

  @Override public Expression visitEL1(EL1Context ctx) {
    return visitBinOp(ctx);
  }

  @Override public Expression visitEL3(EL3Context ctx) {
    return visitBinOp(ctx);
  }

  @Override public Expression visitETop(ETopContext ctx) {
    return visitBinOp(ctx);
  }
  private Expression visitBinOp(ParserRuleContext ctx) {
    LinkedList<ParseTree> stack=this.getStack(ctx);
    Expression current=stack.pop().accept(this);
    while(!stack.isEmpty()){
      current=new Expression.BinOp(position(ctx),current,
        Op.fromString(stack.pop().getText()),
        stack.pop().accept(this));
      }
    if(current instanceof Expression.BinOp){
      current=onNeedMakeRightAssociative((Expression.BinOp)current);
      }
    return current;
    }
  private Expression onNeedMakeRightAssociative(Expression.BinOp bop){
    if(bop.getOp().leftAssociative){return bop;}
    if(!(bop.getLeft() instanceof Expression.BinOp)){return bop;}
    Expression.BinOp left=(Expression.BinOp)bop.getLeft();
    return left.withRight(bop.withLeft(left.getRight()));
  }
  private LinkedList<ParseTree> getStack(ParserRuleContext ctx){
      return (ctx.children!=null)?new LinkedList<ParseTree>(ctx.children):new LinkedList<ParseTree>();
  }
  @Override public Expression visitUsing(UsingContext ctx) {
    Path path= Ast.Path.parse(nameU(ctx.Path()));
    Expression inner=ctx.eTop().accept(this);
    String name=nameL(ctx.mCall().m());
    Parameters parameters=this.parseMParameters(ctx.mCall().round().ps());
    Doc docs=comm(ctx.mCall().round().docsOpt());
    return new Expression.Using(path, name, docs,parameters, inner);
  }

  @Override public Expression visitEPost(EPostContext ctx) {
    return visitEPostAux(ctx.children);
  }
  private Expression visitEPostAux(List<ParseTree> ctxChildren) {
    ParseTree c = ctxChildren.get(0);
    Expression e0=c.accept(this);
    class VisitEPost extends AbstractVisitor<Expression>{
      Expression e0; VisitEPost(Expression e0){this.e0=e0;}
      @Override public Expression visitDocs(DocsContext ctx) {
        return new Expression.DocE(e0,comm(ctx.Doc()));
      }
      @Override public Expression visitSquare(SquareContext ctx) {
      Doc doc=comm(ctx.docsOpt(0));
      List<Doc> docs=new ArrayList<>();
      List<Parameters> parameterss=new ArrayList<>();
      for(int i=0;i<ctx.ps().size();i++){
        docs.add(comm(ctx.docsOpt(i+1)));
        parameterss.add(parseMParameters(ctx.ps(i)));
      }
      assert parameterss.size()>=1:"last empty one at least should be there";
      Parameters last=parameterss.get(parameterss.size()-1);
      if(!last.getE().isPresent() && last.getEs().isEmpty()){
        parameterss.remove(parameterss.size()-1);
        }
      //parse tree= normal form ending in ";"
      // a]=a;] a;]=a;] a;;]=a;;]   [;]=[;]   [;;]=[;;] [;;;]=[;;;]
      return new Expression.SquareCall(position(ctx),e0, doc, docs, parameterss);
      }
      @Override public Expression visitSquareW(SquareWContext ctx) {
        return new Expression.SquareWithCall(position(ctx),e0, (With)ctx.w().accept(ToAst.this));
      }

      @Override public Expression visitRound(RoundContext ctx) {
        Doc doc=comm(ctx.docsOpt());
        return new Expression.FCall(position(ctx),this.e0,doc,
            parseMParameters(ctx.ps()));
      }

      @Override public Expression visitMCall(MCallContext ctx) {
        Expression.FCall f0=(Expression.FCall)ctx.round().accept(this);
        return new Expression.MCall(this.e0,
          nameL(ctx.m()),f0.getDoc(),f0.getPs(),position(ctx));
      }
      @Override public Expression visitStringParse(StringParseContext ctx) {
        String s=ctx.StringQuote().getText();
        s=s.substring(1,s.length()-1);
        if (!s.contains("\n")){
          return new Expression.Literal(position(ctx),e0,s,false);
          }
        String[] ss=s.split("\n");
        for(int i=1; i<ss.length-1;i++){
          assert ss[i].contains("\'"):"||"+ss[i]+"||"+i;
          ss[i]=ss[i].substring(ss[i].indexOf("\'")+1);
        }
        s="";
        for(int i=1; i<ss.length-1;i++){s+=ss[i]+"\n";}
        return new Expression.Literal(position(ctx),e0,s,false);
      }
    }

      for(int i=1;i<ctxChildren.size();i++){
        ParseTree cc = ctxChildren.get(i);
        if (cc instanceof TerminalNode)continue;//it was a "."
        e0=cc.accept(new VisitEPost(e0));
        }
    return e0;
  }

  private Parameters parseMParameters(PsContext ctx) {
    Optional<Expression> e0=Optional.<Expression>empty();
    List<String> xs=new ArrayList<String>();
    List<Expression> es=new ArrayList<Expression>();
    LinkedList<ParseTree> stack = this.getStack(ctx);
    //TODO: first can be comment
    if(!stack.isEmpty()&& !(stack.getFirst()instanceof TerminalNode)){
      e0=Optional.of(stack.pop().accept(this));
      }
    while(!stack.isEmpty()){
      xs.add(nameL(stack.pop()));
      assert stack.getFirst().getText().equals(":"):"|"+stack.getFirst()+"|";
      stack.pop();
      es.add(stack.pop().accept(this));
    }
    return new Parameters(e0,xs,es);
  }
  public Expression visitXOp(XOpContext ctx) {
    return new BinOp(position(ctx),new X(nameL(ctx.X())),Op.fromString(ctx.EqOp().getText()),ctx.eTop().accept(this));
  }
  @Override
  public Expression visitRoundBlockForMethod(RoundBlockForMethodContext ctx) {
    return visitRoundBlockAux(ctx,ctx.docsOpt(),ctx.bb(),ctx.eTop());
    }

  @Override
  public Expression visitETopForMethod(ETopForMethodContext ctx) {
    if(ctx.eTop()!=null){return visitETop(ctx.eTop());}
    return visitEPostAux(ctx.children);
    }
  }
