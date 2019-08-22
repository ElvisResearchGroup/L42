package is.L42.visitors;

import static is.L42.tools.General.*;

import java.util.Collections;
import java.util.List;
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

public class FullL42Visitor extends L42BaseVisitor<Object>{
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
  @Override public Full.E visitE(EContext ctx) {return (Full.E)c(ctx);}
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
    var dx=visitDX(ctx.dX());
    var e=visitE(ctx.e());
    var first=dx.get(0);
    return new Full.D(first,popL(dx),e);
    }
  @Override public Full.E visitEAtomic(EAtomicContext ctx) {
    Object inner=c(ctx);
    if(inner instanceof X){return new Core.EX(pos(ctx), (X)inner);}
    return (Full.E)inner;
    }
  @Override public X visitX(XContext ctx) {return new X(ctx.getText());}
  @Override public Full.Call visitFCall(FCallContext ctx) {
    S s=null;
    if(ctx.m()!=null){s=visitM(ctx.m());}
    return new Full.Call(pos(ctx), eVoid, s, false, L(visitPar(ctx.par())));
    }
  @Override public Full.E visitNudeE(NudeEContext ctx) {return (Full.E)ctx.children.get(0).accept(this);}
  @Override public Full.Block visitBlock(BlockContext ctx) {
    //oR d*? e ')' | oR d+ k* whoops? (d* e)? ')' | '{' d+ (k+ whoops? d* | whoops d*)? '}';
    boolean isCurly=ctx.oR()==null;
    List<Full.D> ds=L(ctx.d(),(c,d)->visitD(d));
    List<Full.K> ks=L(ctx.k(),(c,k)->visitK(k));
    List<Full.T> whoopsed=visitWhoops(ctx.whoops());
    Full.E e=visitE(ctx.e());
    int dsAfter=ds.size();
    for(int i:range(ctx.children)){
      if(ctx.children.get(i) instanceof KContext){dsAfter=i-1;break;}
      }
    return new Full.Block(pos(ctx), isCurly, ds, dsAfter, ks, whoopsed, e);
    }
  @Override public S visitM(MContext ctx) {
    String s=ctx.toString();
    int un=s.indexOf("::");
    if(un==-1){return new S(s,L(),-1);}
    int n=Integer.parseInt(s.substring(un+2));
    s=s.substring(0, un);
    return new S(s,L(),n);
    }
  @Override public Full.CsP visitCsP(CsPContext ctx) {
    Pos pos=pos(ctx);
    String s=ctx.CsP().toString();
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
    return new L42AuxBaseVisitor<Object>(){
      @Override public Full.CsP visitCsP(L42AuxParser.CsPContext ctx) {
        if(ctx.cs()!=null){return visitCs(ctx.cs());}
        if(ctx.path()!=null){return visitPath(ctx.path());}
        if(ctx.anyKw()!=null){return new Full.CsP(pos,L(),P.pAny);}
        if(ctx.cs()!=null){return new Full.CsP(pos,L(),P.pVoid);}
        if(ctx.cs()!=null){return new Full.CsP(pos,L(),P.pLibrary);}
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
      }.visitCsP(res.res);
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
    //dX:VarKw? tLocal x | tLocal UnderScore | tLocal oR (VarKw? tLocal x)+ ')';
    List<Full.VarTx> tLocals=L(ctx.tLocal(),(c,t)->visitTLocal(t));
    assert ctx.oR()!=null || tLocals.size()==1;
    if(ctx.oR()==null && ctx.UnderScore()==null){
      X x=visitX(ctx.x(0));
      var res=tLocals.get(0).withVar(ctx.VarKw()!=null).with_x(x);
      return L(res);
      }
    if(ctx.oR()==null){ assert ctx.UnderScore()!=null;
      return tLocals;
      }
    return L(c->{
      c.add(tLocals.get(0));
      for(int i :range(1,tLocals.size())){
        var ti=tLocals.get(i);
        var tL=ctx.tLocal(i);
        int j=ctx.children.indexOf(tL);
        boolean isVar=ctx.getChild(j-1) instanceof TerminalNode;
        c.add(ti.withVar(isVar).with_x(visitX(ctx.x(i-1))));
        }
      });
    }
  @Override public Full.Doc visitDoc(DocContext ctx) {throw bug();}
  @Override public Full.K visitK(KContext ctx) {throw bug();}
  @Override public List<Full.T> visitWhoops(WhoopsContext ctx) {throw bug();}
  /*@Override public String visitFullL(FullLContext ctx) {throw bug();}
  @Override public String visitFullM(FullMContext ctx) {throw bug();}
  @Override public String visitFullF(FullFContext ctx) {throw bug();}
  @Override public String visitHeader(HeaderContext ctx) {throw bug();}
  @Override public String visitFullMi(FullMiContext ctx) {throw bug();}
  @Override public String visitFullMWT(FullMWTContext ctx) {throw bug();}
  @Override public String visitFullMH(FullMHContext ctx) {throw bug();}
  @Override public String visitMOp(MOpContext ctx) {throw bug();}
  @Override public String visitFullNC(FullNCContext ctx) {throw bug();}
  @Override public String visitSlash(SlashContext ctx) {throw bug();}
  @Override public String visitPathSel(PathSelContext ctx) {throw bug();}
  @Override public String visitCast(CastContext ctx) {throw bug();}
  @Override public String visitSlashX(SlashXContext ctx) {throw bug();}
  */
  @Override public Full.E visitEPostfix(EPostfixContext ctx) {
    var res=visitEAtomic(ctx.eAtomic());
    var uOpList=ctx.children.stream().takeWhile(c->c instanceof TerminalNodeImpl).collect(Collectors.toList());
    Collections.reverse(uOpList);
    assert ctx.getChild(uOpList.size())==ctx.eAtomic();
    for(int i: range(uOpList.size()+1,ctx.children.size())){
      ParseTree current=ctx.getChild(i);
      if(current instanceof FCallContext){
        res=visitFCall((FCallContext)current).withE(res);}
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
  /*@Override public String visitSquareCall(SquareCallContext ctx) {throw bug();}
  @Override public String visitEBinary0(EBinary0Context ctx) {throw bug();}
  @Override public String visitEBinary1(EBinary1Context ctx) {throw bug();}
  @Override public String visitEBinary2(EBinary2Context ctx) {throw bug();}
  @Override public String visitEBinary3(EBinary3Context ctx) {throw bug();}  
  @Override public String visitSIf(SIfContext ctx) {throw bug();}
  @Override public String visitMatch(MatchContext ctx) {throw bug();}
  @Override public String visitSWhile(SWhileContext ctx) {throw bug();}
  @Override public String visitSFor(SForContext ctx) {throw bug();}
  @Override public String visitSLoop(SLoopContext ctx) {throw bug();}
  @Override public String visitSThrow(SThrowContext ctx) {throw bug();}
  @Override public String visitSUpdate(SUpdateContext ctx) {throw bug();}
  @Override public String visitInfo(InfoContext ctx) {throw bug();}*/
  }
