package is.L42.visitors;

import static is.L42.tools.General.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import is.L42.generated.L42Parser.*;
import is.L42.common.Parse;
import is.L42.generated.*;
import is.L42.generated.Full.UOp;
import is.L42.generated.L42AuxParser.NudeCsPContext;

public class FullL42Visitor extends L42BaseVisitor<Object>{
  private final static class AuxVisitor extends L42AuxBaseVisitor<Object> {
    private final Pos pos;

    private AuxVisitor(Pos pos) { this.pos = pos; }

    @Override public Full.CsP visitNudeCsP(NudeCsPContext ctx) {
      return visitCsP(ctx.csP());
      }
    @Override public Full.CsP visitCsP(L42AuxParser.CsPContext ctx) {
      Full.CsP r;
      if((r=opt(ctx.cs(),null,this::visitCs))!=null){return r;}
      if((r=opt(ctx.path(),null,this::visitPath))!=null){return r;}
      if(ctx.anyKw()!=null){return new Full.CsP(pos,L(),P.pAny);}
      if(ctx.voidKw()!=null){return new Full.CsP(pos,L(),P.pVoid);}
      if(ctx.libraryKw()!=null){return new Full.CsP(pos,L(),P.pLibrary);}
      throw unreachable();
      }
      
    @Override public Full.CsP visitCs(L42AuxParser.CsContext ctx) {
      List<C> cs=L(ctx.c(),(r,c)->r.add(visitC(c)));
      return new Full.CsP(pos,cs, null);
      }

    @Override public Full.CsP visitPath(L42AuxParser.PathContext ctx) {
      int n=visitThisKw(ctx.thisKw());
      List<C> cs=L(ctx.c(),(r,c)->r.add(visitC(c)));
      return new Full.CsP(pos,L(),P.of(n,cs)); 
      }

    @Override public C visitC(L42AuxParser.CContext ctx) {
      String s=ctx.getText();
      int i=s.indexOf("::");
      if(i==-1){return new C(s,-1);}
      int u=Integer.parseInt(s.substring(i+2));
      return new C(s.substring(0,i),u);
      }

    @Override public Integer visitThisKw(L42AuxParser.ThisKwContext ctx) {
      String s=ctx.getText().substring(4);
      if(s.isEmpty()){return 0;}
      return Integer.parseInt(s);
      }
     @Override public Full.Doc visitTopDoc(L42AuxParser.TopDocContext ctx) {
        Full.PathSel p=opt(ctx.pathSelX(),null,this::visitPathSelX);
        if(ctx.topDocText()==null){return new Full.Doc(p, L(),L());}
        return visitTopDocText(ctx.topDocText()).with_pathSel(p);
        }
      @Override public Full.PathSel visitPathSelX(L42AuxParser.PathSelXContext ctx) {
        Full.CsP p=opt(ctx.pathSel().csP(),null,this::visitCsP);
        S s=opt(ctx.pathSel().selector(),null,this::visitSelector);
        X x=opt(ctx.x(),null,this::visitX);
        return new Full.PathSel(p,s,x); 
        }
      @Override public S visitSelector(L42AuxParser.SelectorContext ctx) {
        List<X>xs=L(ctx.x(),(c,xi)->c.add(visitX(xi)));
        if(ctx.m()==null){return new S("",xs,-1);}
        return parseM(ctx.m().getText()).withXs(xs);
        }
        
      @Override public X visitX(L42AuxParser.XContext ctx) {
        return new X(ctx.getText());
        }

      @Override public Full.Doc visitTopDocText(L42AuxParser.TopDocTextContext ctx) {
        //topDocText:charInDoc* doc topDocText | charInDoc* '{' topDocText '}' topDocText;
        if(ctx.doc()==null && ctx.topDocText().isEmpty()){ 
          return new Full.Doc(null, L(ctx.getText()), L());
          }
        String firstText=ctx.charInDoc().stream()
          .map(ci->ci.getText()).collect(Collectors.joining(""));
        if(ctx.doc()==null){
          assert ctx.topDocText().size()==2;
          Full.Doc text1=visitTopDocText(ctx.topDocText(0));
          Full.Doc text2=visitTopDocText(ctx.topDocText(1));
          assert !text1.texts().isEmpty();
          assert !text2.texts().isEmpty();
          List<Full.Doc> docs=L(c->{c.addAll(text1.docs());c.addAll(text2.docs());});
          List<String> texts=L(c->{
            int s1=text1.texts().size();
            int s2=text2.texts().size();
            if(s1==1){
              c.add(firstText+"{"+text1.texts().get(0)+"}"+text2.texts().get(0));
              }
            else{
              c.add(firstText+"{"+text1.texts().get(0));
              c.addAll(text1.texts().subList(1,s1-1));
              c.add(text1.texts().get(s1-1)+"}"+text2.texts().get(0));
              }
            c.addAll(text2.texts().subList(1,s2)); 
            });
          return new Full.Doc(null, texts, docs);
          }
        assert ctx.topDocText().size()==1;
        Full.Doc text1=visitTopDocText(ctx.topDocText(0));
        var res=Parse.doc("@"+ctx.doc().getText());
        assert !res.hasErr();
        Full.Doc doc=visitTopDoc(res.res);
        return text1
          .withDocs(pushTopL(doc,text1.docs()))
          .withTexts(pushTopL(firstText,text1.texts()));
        }  
    }
  String fileName;
  public StringBuilder errors=new StringBuilder();
  Core.EVoid eVoid=new Core.EVoid(null);//will not be in the final result
  public FullL42Visitor(String fileName){this.fileName=fileName;}
  Object c(ParserRuleContext prc){
    assert prc.children.size()==1;
    return prc.children.get(0).accept(this); 
    }
  Pos pos(ParserRuleContext prc){
    return new Pos(fileName,prc.getStart().getLine(),prc.getStart().getCharPositionInLine()); 
    }
//  @Override public Void visit(ParseTree arg0) {throw bug();}
//  @Override public Void visitChildren(RuleNode arg0) {throw bug();}
//  @Override public Void visitErrorNode(ErrorNode arg0) {throw bug();}
//  @Override public Void visitTerminal(TerminalNode arg0) {throw bug();}
  @Override public Full.E visitE(EContext ctx) {
    var res=c(ctx);
    assert res!=null;
    return (Full.E)res;
    }
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
    List<Full.VarTx> dx=opt(ctx.dX(),L(),this::visitDX);
    var e=visitE(ctx.e());
    Full.VarTx first=null;
    if(!dx.isEmpty()){
      first=dx.get(0);
      dx=popL(dx);
      }
    return new Full.D(first,dx,e);
    }
  @Override public Full.E visitEAtomic(EAtomicContext ctx) {
    Object inner=c(ctx);
    if(inner instanceof X){return new Core.EX(pos(ctx), (X)inner);}
    return (Full.E)inner;
    }
  @Override public X visitX(XContext ctx) {return new X(ctx.getText());}
  @Override public Full.Call visitFCall(FCallContext ctx) {
    S s=opt(ctx.m(),null,this::visitM);
    return new Full.Call(pos(ctx), eVoid, s, false, L(visitPar(ctx.par())));
    }
  @Override public Full.E visitNudeE(NudeEContext ctx) {return (Full.E)ctx.children.get(0).accept(this);}
  @Override public Full.Block visitBlock(BlockContext ctx) {
    boolean isCurly=ctx.oR()==null;
    List<Full.D> ds=L(ctx.d(),(c,d)->c.add(visitD(d)));
    List<Full.K> ks=L(ctx.k(),(c,k)->c.add(visitK(k)));
    List<Full.T> whoopsed=opt(ctx.whoops(),L(),this::visitWhoops);
    Full.E e=opt(ctx.e(),null,this::visitE);
    int dsAfter=ds.size();
    for(int i:range(ctx.children)){
      if(ctx.children.get(i) instanceof KContext){dsAfter=i-1;break;}
      }
    return new Full.Block(pos(ctx), isCurly, ds, dsAfter, ks, whoopsed, e);
    }
  @Override public S visitM(MContext ctx) {
    return parseM(ctx.getText());
    }
    
  private static S parseM(String s) {
    int un=s.indexOf("::");
    if(un==-1){return new S(s,L(),-1);}
    int n=Integer.parseInt(s.substring(un+2));
    s=s.substring(0, un);
    return new S(s,L(),n); 
    }
    
  @Override public Full.CsP visitCsP(CsPContext ctx) {
    Pos pos=pos(ctx);
    String s=ctx.CsP().getText();
    var res=Parse.csP(s);
    if(res.hasErr()){
      String msg="Error: "+s+" is not a class name";
      if(s.contains("Any")){msg+="; a class name can not be Any";}
      if(s.contains("Void")){msg+="; a class name can not be Void";}
      if(s.contains("Void")){msg+="; a class name can not be Library";}
      if(s.contains("This")){msg+="; a class name can not be This";}
      this.errors.append("line " + pos.line() + ":" + pos.column() + " " + msg);
      return new Full.CsP(pos,L(),P.pAny);
      }
    return new AuxVisitor(pos).visitNudeCsP(res.res);
    }
  @Override public Core.EVoid visitVoidE(VoidEContext ctx) {
    return new Core.EVoid(pos(ctx));
    }
  @Override public Full.T visitT(TContext ctx) {
    var csP=visitCsP(ctx.csP());
    var mdf=ctx.Mdf()==null?null:Mdf.fromString(ctx.Mdf().getText());
    List<Full.Doc> docs=L(ctx.doc(),(c,d)->c.add(visitDoc(d)));
    return new Full.T(mdf, docs, csP);
    }
  @Override public Full.VarTx visitTLocal(TLocalContext ctx) {
    if(ctx.Mdf()!=null){return new Full.VarTx(false,null,Mdf.fromString(ctx.Mdf().getText()),null);}
    if(ctx.t()!=null){return new Full.VarTx(false,visitT(ctx.t()),null,null);}
    return new Full.VarTx(false, null, null,null);
    }
  @Override public List<Full.VarTx> visitDX(DXContext ctx) {
    //sadly, tLocal can be the empty text, and 
    //ANTLR would not generate empty text nonterminals
    if(ctx.oR()==null && ctx.UnderScore()==null){
      X x=visitX(ctx.x(0));
      boolean isVar=!ctx.VarKw().isEmpty();
      if(ctx.tLocal().isEmpty()){return L(new Full.VarTx(isVar,null,null,x));}
      assert ctx.tLocal().size()==1;
      var res=visitTLocal(ctx.tLocal(0)).withVar(isVar).with_x(x);
      return L(res);
      }
    if(ctx.oR()==null){ assert ctx.UnderScore()!=null;
      if(ctx.tLocal().isEmpty()){return L(Full.VarTx.emptyInstance);}
      assert ctx.tLocal().size()==1;
      return L(visitTLocal(ctx.tLocal(0)));
      }
    return L(c->{
      Full.VarTx tL0=Full.VarTx.emptyInstance;
      if(ctx.getChild(0)!=ctx.oR()){tL0=visitTLocal(ctx.tLocal(0));}
      c.add(tL0);
      for(var cxi: ctx.x()){
        var xi=visitX(cxi);
        int j=ctx.children.indexOf(cxi)-1;
        var tLi=Full.VarTx.emptyInstance;
        if(ctx.getChild(j) instanceof TLocalContext){
          tLi=visitTLocal((TLocalContext)ctx.getChild(j));
          j-=1;
          } 
        boolean isVar=ctx.getChild(j) instanceof TerminalNode;
        c.add(tLi.withVar(isVar).with_x(xi));
        }
      });
    }
  @Override public Full.Doc visitDoc(DocContext ctx) {
    String s="@"+ctx.getText();
    Pos pos=pos(ctx);
    var res=Parse.doc(s);
    if (res.hasErr()){
      String msg="Error: malformed @ in docs";
      this.errors.append("line " + pos.line() + ":" + pos.column() + " " + msg);
      return new Full.Doc(null, L(), L());
      }
    return new AuxVisitor(pos).visitTopDoc(res.res);
    }
  @Override public Full.K visitK(KContext ctx) {
    ThrowKind thr=opt(ctx.Throw(),null,t->ThrowKind.fromString(t.getText()));
    Full.T t=visitT(ctx.t());
    X x=opt(ctx.x(),null,this::visitX);
    Full.E e=visitE(ctx.e());
    return new Full.K(thr, t, x, e);
    }
  @Override public List<Full.T> visitWhoops(WhoopsContext ctx) {
    return L(ctx.t(),(c,ti)->c.add(visitT(ti)));
    }
  @Override public Full.L visitFullL(FullLContext ctx) {
    boolean isDots=ctx.DotDotDot()!=null;
    String reuseUrl=opt(ctx.ReuseURL(),"",r->parseReuseNative(r.getText()));
    boolean isInterface=opt(ctx.header(),false,h->h.InterfaceKw()!=null);
    List<Full.T>empty=L();
    List<Full.T>ts=opt(ctx.header(),empty,h->L(h.t(),(c,ti)->c.add(visitT(ti))));
    List<Full.L.M> ms=L(ctx.fullM(),(c,mi)->visitFullM(mi));
    List<Full.Doc> docs=L(ctx.doc(),(c,di)->visitDoc(di));
    return new Full.L(pos(ctx), isDots, reuseUrl, isInterface, ts, ms, docs);
    }
  private String parseReuseNative(String s) { 
    assert s.endsWith("]");
    int index = s.indexOf("[");
    assert index!=-1;
    return s.substring(index,s.length()-1);
    }
  @Override public Full.L.M visitFullM(FullMContext ctx) {
    var fi=opt(ctx.fullF(),null,(this::visitFullF));
    throw bug();
    }
  static private <A,B> B opt(A a,B def,Function<A,B>f){
    if(a==null){return def;}
    return f.apply(a);
    }
    
  @Override public Full.L.F visitFullF(FullFContext ctx) {throw bug();}
  @Override public String visitHeader(HeaderContext ctx) {throw bug();}
  @Override public Full.L.MI visitFullMi(FullMiContext ctx) {throw bug();}
  @Override public Full.L.MWT visitFullMWT(FullMWTContext ctx) {throw bug();}
  @Override public Full.MH visitFullMH(FullMHContext ctx) {throw bug();}
  @Override public String visitMOp(MOpContext ctx) {throw bug();}
  @Override public Full.L.NC visitFullNC(FullNCContext ctx) {throw bug();}
  //@Override public String visitSlash(SlashContext ctx) {throw bug();}
  //@Override public String visitPathSel(PathSelContext ctx) {throw bug();}
  @Override public Full.Cast visitCast(CastContext ctx) {
    return new Full.Cast(pos(ctx),eVoid, visitT(ctx.t()));
    }
  //@Override public String visitSlashX(SlashXContext ctx) {throw bug();}

  @Override public Full.E visitEPostfix(EPostfixContext ctx) {
    var res=visitEAtomic(ctx.eAtomic());
    var uOpList=ctx.children.stream().takeWhile(c->c instanceof TerminalNodeImpl).collect(Collectors.toList());
    Collections.reverse(uOpList);
    assert ctx.getChild(uOpList.size())==ctx.eAtomic();
    for(int i: range(uOpList.size()+1,ctx.children.size())){
      ParseTree current=ctx.getChild(i);
      if(current instanceof FCallContext){
        res=visitFCall((FCallContext)current).withE(res);}
      if(current instanceof CastContext){
        res=visitCast((CastContext)current).withE(res);}
      if(current instanceof SquareCallContext){
        res=visitSquareCall((SquareCallContext)current).withE(res);}
      //TODO: add other cases
      }
    for(var uOp:uOpList){
      String s=uOp.getText();
      if(s.equals("!")){res=new Full.UOp(res.pos(),Op.Bang,null,res);}
      else if(s.equals("~")){res=new Full.UOp(res.pos(),Op.Tilde,null,res);}
      else{
        assert !s.contains("~");
        assert !s.contains("!");
        res=new Full.UOp(res.pos(),null,s,res);
        }
      }
    return res;
    }
  @Override public Full.Call visitSquareCall(SquareCallContext ctx) {
    S s=opt(ctx.m(),null,this::visitM);
    List<Full.Par> ps=L(ctx.par(),(c,p)->c.add(visitPar(p)));
    return new Full.Call(pos(ctx), eVoid, s, true, ps);
    }
  Full.E parseBinOp(Pos pos,List<? extends ParserRuleContext> es0,List<TerminalNode>ops0) {
    if(es0.size()==1){return (Full.E)es0.get(0).accept(this);}
    List<Full.E> es=L(es0,(c,ei)->c.add((Full.E)ei.accept(this)));
    Set<Op> ops=new LinkedHashSet<>();
    for( var oi:ops0){ops.add(Op.fromString(oi.getText()));}
    Op op=ops.iterator().next();
    if (ops.size()!=1){
      String msg="Error: sequence of binary operators "+ops+" need to be disambiguated with parenthesis";
      this.errors.append("line " + pos.line() + ":" + pos.column() + " " + msg);
      }
    return new Full.BinOp(pos, op, es);
    }
  @Override public Full.E visitEBinary0(EBinary0Context ctx) {return parseBinOp(pos(ctx),ctx.ePostfix(),ctx.OP0());}
  @Override public Full.E visitEBinary1(EBinary1Context ctx) {return parseBinOp(pos(ctx),ctx.eBinary0(),ctx.OP1());}
  @Override public Full.E visitEBinary2(EBinary2Context ctx) {return parseBinOp(pos(ctx),ctx.eBinary1(),ctx.OP2());}
  @Override public Full.E visitEBinary3(EBinary3Context ctx) {return parseBinOp(pos(ctx),ctx.eBinary2(),ctx.OP3());}  
  
  
  /*@Override public String visitSIf(SIfContext ctx) {throw bug();}
  @Override public String visitMatch(MatchContext ctx) {throw bug();}
  @Override public String visitSWhile(SWhileContext ctx) {throw bug();}
  @Override public String visitSFor(SForContext ctx) {throw bug();}
  @Override public String visitSLoop(SLoopContext ctx) {throw bug();}
  @Override public String visitSThrow(SThrowContext ctx) {throw bug();}
  @Override public String visitSUpdate(SUpdateContext ctx) {throw bug();}
  @Override public String visitInfo(InfoContext ctx) {throw bug();}*/
  }
