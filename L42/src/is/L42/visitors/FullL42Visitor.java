package is.L42.visitors;

import static is.L42.tools.General.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import is.L42.generated.L42Parser.*;
import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.PTails;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.generated.*;
import is.L42.generated.Core.EVoid;
import is.L42.generated.Full.VarTx;

class ParserFailed extends RuntimeException{}

public class FullL42Visitor implements L42Visitor<Object>{
  public Path fileName;
  public StringBuilder errors=new StringBuilder();
  public EVoid eVoid=new Core.EVoid(new Pos(Constants.temp.toUri(),0,0));
  public FullL42Visitor(Path fileName){this.fileName=fileName;}
  Pos pos(ParserRuleContext prc){
    return new Pos(fileName.toUri(),prc.getStart().getLine(),prc.getStart().getCharPositionInLine()); 
    }
  void check(ParserRuleContext ctx){  
    if(ctx.children!=null){return;}
    throw new ParserFailed();
    }
  @Override public Void visit(ParseTree arg0) {throw bug();}
  @Override public Void visitChildren(RuleNode arg0) {throw bug();}
  @Override public Void visitErrorNode(ErrorNode arg0) {throw bug();}
  @Override public Void visitTerminal(TerminalNode arg0) {throw bug();}
  @Override public Void visitOR(ORContext ctx) {throw bug();}
  @Override public Void visitHeader(HeaderContext ctx) {throw bug();}

  @Override public Full.E visitE(EContext ctx) {
    check(ctx);
    Optional<Full.E> res=Stream.of(
      opt(ctx.sIf(),null,this::visitSIf),
      opt(ctx.sWhile(),null,this::visitSWhile),
      opt(ctx.sFor(),null,this::visitSFor),
      opt(ctx.sLoop(),null,this::visitSLoop),
      opt(ctx.sThrow(),null,this::visitSThrow),
      opt(ctx.sUpdate(),null,this::visitSUpdate),
      opt(ctx.eBinary3(),null,this::visitEBinary3))
      .filter(a->a!=null)
      .findFirst();
    return res.get();
    }
  @Override public Full.Par visitPar(ParContext ctx) {
    //check(ctx);//Would be wrong
    List<X> xs=L(ctx.x(),(c,x)->c.add(visitX(x)));
    List<Full.E> es=L(ctx.e(),(c,x)->c.add(visitE(x)));
    if (es.size()==xs.size()){
      if(es.isEmpty()){return Full.Par.empty;}
      return new Full.Par(null, xs, es);
      }
    assert es.size()==xs.size()+1;
    return new Full.Par(es.get(0), xs, popL(es));
    }
  @Override public Full.EString visitString(StringContext ctx) {
    check(ctx);
    String s=ctx.getText();
    boolean multi=s.startsWith("\"\"\"");
    int escapeSize=1;
    if(multi){
      int i=3;while(s.startsWith("%",i)){i+=1;}
      escapeSize=i-3;
      assert s.startsWith("\n",i);
      int last=s.lastIndexOf("\n");
      assert last>i;
      s=s.substring(i+1,last);
      s=s.lines().map(l->l.substring(l.indexOf("|")+1)).collect(Collectors.joining("\n"))+"\n";
      }
    else{s=s.substring(1,s.length()-1);}
    
    return new StringInterpolation(escapeSize,fileName,eVoid,pos(ctx),errors).supParse(s);
    }
  @Override public Full.D visitD(DContext ctx) {
    check(ctx);
    List<Full.VarTx> dx=opt(ctx.dX(),L(),this::visitDX);
    var e=visitE(ctx.e());
    return varTxEToD(dx,e);
    }
  Full.D varTxEToD(List<Full.VarTx> dx,Full.E e){
    Full.VarTx first=null;
    if(!dx.isEmpty()){
      first=dx.get(0);
      dx=popL(dx);
      }
    return new Full.D(first,dx,e);
    }
  @Override public Full.E visitEAtomic(EAtomicContext ctx) {
    check(ctx);
    Pos pos=pos(ctx);
    Optional<Full.E> res=Stream.of(
      opt(ctx.x(),null,a->new Core.EX(pos,visitX(a))),
      opt(ctx.csP(),null,this::visitCsP),
      opt(ctx.voidE(),null,this::visitVoidE),
      opt(ctx.fullL(),null,this::visitFullL),
      opt(ctx.block(),null,this::visitBlock),
      opt(ctx.slash(),null,this::visitSlash),
      opt(ctx.pathSel(),null,this::visitPathSel),
      opt(ctx.slashX(),null,this::visitSlashX))
      .filter(a->a!=null).findFirst();
    return res.get();
    }
  @Override public X visitX(XContext ctx) {
    check(ctx);
    return new X(ctx.getText());
    }
  @Override public Full.Call visitFCall(FCallContext ctx) {
    check(ctx);
    S s=opt(ctx.m(),null,this::visitM);
    Full.Par par=visitPar(ctx.par());
    if(par!=Full.Par.empty){
      return new Full.Call(pos(ctx), eVoid, s, false, L(par));
      }
    return new Full.Call(pos(ctx), eVoid, s, false, Full.Par.emptys);    
    }
  @Override public Full.E visitNudeE(NudeEContext ctx) {
    check(ctx);
    return visitE(ctx.e());
    }
  @Override public Full.Block visitBlock(BlockContext ctx) {
    check(ctx);
    boolean isCurly=ctx.oR()==null;
    List<Full.D> ds=L(ctx.d(),(c,d)->c.add(visitD(d)));
    List<Full.K> ks=L(ctx.k(),(c,k)->c.add(visitK(k)));
    List<Full.T> whoopsed=opt(ctx.whoops(),L(),this::visitWhoops);
    Full.E e=opt(ctx.e(),null,this::visitE);
    assert isCurly || e!=null;
    int dsAfter=ds.size();
    for(int i:range(ctx.children)){
      var c=ctx.children.get(i);
      if( (c instanceof KContext) || (c instanceof WhoopsContext)){
        dsAfter=i-1;break;
        }
      }
    if (dsAfter>ds.size()){
      assert !isCurly;
      assert dsAfter==ds.size()+1;
      ds=pushL(ds,new Full.D(null,L(),e));
      e=null;
      }
    boolean parserAmbiguity=true;
    parserAmbiguity &=e!=null && e instanceof Full.Block && !ctx.e().start.getText().contains("\n");
    parserAmbiguity &=ks.isEmpty() && whoopsed.isEmpty() && !isCurly;
    parserAmbiguity &=!ds.isEmpty() && ds.get(ds.size()-1)._varTx()!=null;
    if(parserAmbiguity){
      //make sure that '(A a=A()(c)   )' does not get parsed as '(A a=A()  (c))'
      //also care about '(A a=A(c)   )' and '(A a=A""(c)   )' 
      int lineOR=ctx.e().start.getLine();
      int posOR=ctx.e().start.getCharPositionInLine();
      int lineDs=ctx.d(ctx.d().size()-1).stop.getLine();
      int posDs=ctx.d(ctx.d().size()-1).stop.getCharPositionInLine();
      if(lineOR==lineDs && posOR<=posDs+3 && posOR>=posDs){//now it ask for 4 spaces to make clear is not an #apply
        throw new EndError.NotWellFormed(e.poss(),Err.parserAmbiguityBlockTerminator());
        }
      }
    return new Full.Block(pos(ctx), isCurly, ds, dsAfter, ks, whoopsed, e);
    }
  @Override public S visitM(MContext ctx) {
    check(ctx);
    appendFwdErrorOnM(ctx);
    return parseM(ctx.getText().replace(" ",""));
    }   
  static S parseM(String s) {
    assert !s.contains("fwd ");
    int un=s.indexOf("::");
    if(un==-1){return new S(s,L(),-1);}
    int n=Integer.parseInt(s.substring(un+2));
    s=s.substring(0, un);
    return new S(s.substring(0, un),L(),n); 
    }
  @Override public Full.CsP visitCsP(CsPContext ctx) {
    check(ctx);
    Pos pos=pos(ctx);
    String s=ctx.CsP().getText();
    var res=Parse.ctxCsP(Paths.get(pos.fileName()),s);
    if(res.hasErr()){
      this.errors.append(pos+ Err.notValidC(
        s.contains("Any")?"Any":
        s.contains("Void")?"Void":        
        s.contains("Library")?"Library":
        s.contains("This")?"This":s
        ));
      return new Full.CsP(pos,L(),P.pAny);
      }
    return new AuxVisitor(pos).visitNudeCsP(res.res);
    }
  @Override public Core.EVoid visitVoidE(VoidEContext ctx) {
    check(ctx);
    return new Core.EVoid(pos(ctx));
    }
  @Override public Full.T visitT(TContext ctx) {
    check(ctx);
    var csP=visitCsP(ctx.csP());
    var mdf=ctx.Mdf()==null?null:Mdf.fromString(ctx.Mdf().getText());
    List<Full.Doc> docs=L(ctx.doc(),(c,d)->c.add(visitDoc(d)));
    return new Full.T(mdf, docs, csP.cs(),csP._p());
    }
  @Override public Full.VarTx visitTLocal(TLocalContext ctx) {
    //check(ctx);//Would be wrong
    if(ctx.Mdf()!=null){return new Full.VarTx(false,null,Mdf.fromString(ctx.Mdf().getText()),null);}
    if(ctx.t()!=null){return new Full.VarTx(false,visitT(ctx.t()),null,null);}
    return new Full.VarTx(false, null, null,null);
    }
  @Override public List<Full.VarTx> visitDX(DXContext ctx) {
    //sadly, tLocal can be the empty text, and 
    //ANTLR would not generate empty text nonterminals
    check(ctx);
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
    check(ctx);
    String s="@"+ctx.getText();
    Pos pos=pos(ctx);
    var res=Parse.ctxDoc(Paths.get(pos.fileName()),s);
    if (res.hasErr()){
      this.errors.append(pos+Err.malformedAtInDocs());
      return new Full.Doc(null, L(), L());
      }
    return new AuxVisitor(pos).visitTopDoc(res.res);
    }
  @Override public Full.K visitK(KContext ctx) {
    check(ctx);
    ThrowKind thr=opt(ctx.Throw(),null,t->ThrowKind.fromString(t.getText()));
    Full.T t=visitT(ctx.t());
    X x=opt(ctx.x(),null,this::visitX);
    Full.E e=visitE(ctx.e());
    return new Full.K(thr, t, x, e);
    }
  @Override public List<Full.T> visitWhoops(WhoopsContext ctx) {
    check(ctx);
    return L(ctx.t(),(c,ti)->c.add(visitT(ti)));
    }
  @Override public Full.E visitFullL(FullLContext ctx) {
    check(ctx);
    Pos pos=pos(ctx);
    boolean isDots=ctx.DotDotDot()!=null;
    String reuseUrl=opt(ctx.ReuseURL(),"",r->parseReuseNative(r.getText(),"[","]"));
    boolean isInterface=opt(ctx.header(),false,h->h.InterfaceKw()!=null);
    List<Full.T>empty=L();
    List<Full.T>ts=opt(ctx.header(),empty,h->checkAllEmptyMdf(pos,L(h.t(),(c,ti)->c.add(visitT(ti)))));
    List<Full.L.M> ms=L(ctx.fullM(),(c,mi)->c.add(visitFullM(mi)));
    List<Full.Doc> docs=L(ctx.doc(),(c,di)->c.add(visitDoc(di)));
    Core.L.Info info=opt(ctx.info(),null,this::visitInfo);
    Full.L res=new Full.L(pos, isDots, reuseUrl, isInterface, ts, ms, docs);
    if(info!=null){return new InjectionToCore(errors,eVoid)._inject(res,info);}
    return res; 
    }
  private String parseReuseNative(String s,String starts,String ends) { 
    assert s.endsWith(ends):s;
    int index = s.indexOf(starts);
    assert index!=-1;
    return s.substring(index+1,s.length()-1);
    }
  @Override public Full.L.M visitFullM(FullMContext ctx) {
    check(ctx);
    Full.L.M m;
    m=opt(ctx.fullF(),null,(this::visitFullF));
    if(m!=null){return m;}
    m=opt(ctx.fullMi(),null,(this::visitFullMi));
    if(m!=null){return m;}
    m=opt(ctx.fullMWT(),null,(this::visitFullMWT));
    if(m!=null){return m;}
    m=opt(ctx.fullNC(),null,(this::visitFullNC));
    if(m!=null){return m;}
    throw unreachable();
    }
  static <A,B> B opt(A a,B def,Function<A,B>f){
    if(a==null){return def;}
    return f.apply(a);
    }
  @Override public Full.L.F visitFullF(FullFContext ctx) {
    check(ctx);
    List<Full.Doc> docs = L(ctx.doc(),(c,di)->c.add(visitDoc(di)));
    S s = parseM(ctx.x().getText());
    boolean isVar=ctx.VarKw()!=null;
    return new Full.L.F(pos(ctx),docs,isVar,visitT(ctx.t()),s);
    }
  @Override public Full.L.MI visitFullMi(FullMiContext ctx) {
    check(ctx);
    List<Full.Doc> docs = L(ctx.doc(),(c,di)->c.add(visitDoc(di)));
    List<X> xs=L(ctx.x(),(c,xi)->c.add(visitX(xi)));
    appendFwdErrorOnM(ctx.mOp().m());
    S s=opt(ctx.mOp().m(),new S("",xs,-1),s0->parseM(s0.getText().replace(" " ,"")).withXs(xs));
    var _op=visitMOp(ctx.mOp());
    Full.E e=visitE(ctx.e());
    Pos pos=pos(ctx);
    int n = stringToInt(ctx.mOp().Number(), pos);
    return new Full.L.MI(pos, docs,_op,n,s, e);
    }
  private int stringToInt(TerminalNode ctx, Pos pos) {
    int n=opt(ctx,-1,n0->stringToInt(n0.getText(),()->{
      this.errors.append(pos + Err.invalidNumber(n0.getText()));
      }));
    return n; 
    }
  private int stringToInt(String n,Runnable err){
    if(n.contains(".") || n.contains("_") || n.contains("-")){
      err.run();
      return -1;
      }
    if(n.startsWith("0")){
      err.run();
      return -1;
      }
    try{return Integer.parseInt(n);}
    catch(NumberFormatException nfe){
      err.run();
      return -1;
      }
    }
  @Override public Full.L.MWT visitFullMWT(FullMWTContext ctx) {
    check(ctx);
    List<Full.Doc> docs=L(ctx.doc(),(c,di)->c.add(visitDoc(di)));
    Full.MH mh=visitFullMH(ctx.fullMH());
    String nativeUrl=opt(ctx.NativeURL(),"",r->parseReuseNative(r.getText(),"{","}"));
    Full.E _e=opt(ctx.e(),null,this::visitE);
    return new Full.L.MWT(pos(ctx),docs,mh,nativeUrl,_e);
    }
  @Override public Full.MH visitFullMH(FullMHContext ctx) {
    check(ctx);
    Pos pos=pos(ctx);
    Mdf _mdf=opt(ctx.Mdf(),null,m->Mdf.fromString(m.getText()));
    List<Full.Doc> docs=L(ctx.doc(),(c,di)->c.add(visitDoc(di)));
    List<Full.T> ts0=L(ctx.t(),(c,ti)->c.add(visitT(ti)));
    Full.T t=ts0.get(0);
    List<X> xs=L(ctx.x(),(c,xi)->c.add(visitX(xi)));
    int excStart=xs.size()+1;
    List<Full.T> pars=L(ts0.subList(1, excStart).stream());
    List<Full.T> exceptions=checkAllEmptyMdf(pos,ts0.subList(excStart,ts0.size()));
    appendFwdErrorOnM(ctx.mOp().m());
    S s=opt(ctx.mOp().m(),new S("",xs,-1),s0->parseM(s0.getText().replace(" ", "")).withXs(xs));
    var _op = visitMOp(ctx.mOp());
    assert _op==null || s.m().isEmpty();
    int n=stringToInt(ctx.mOp().Number(), pos);
    return new Full.MH(_mdf, docs, t, _op, n, s, pars, exceptions);
    }
  private List<Full.T> checkAllEmptyMdf(Pos pos, List<Full.T> ts){
    return L(ts,ti->{
      if(ti._mdf()!=null){return ti;}
      return ti.with_mdf(Mdf.Immutable);
      });
    }
  private void appendFwdErrorOnM(MContext ctx) {
    if(ctx!=null && ctx.getText().contains("fwd ")){
      Pos pos=pos(ctx);
      this.errors.append(pos+ Err.invalidMethodName(ctx.getText()));
      }
    }
  @Override public Op visitMOp(MOpContext ctx) {
    //check(ctx);//Would be wrong
    Op _uop=opt(ctx.Uop(),null,o->Op.fromString(o.getText()));
    Op _op0=opt(ctx.OP0(),null,o->Op.fromString(o.getText()));
    Op _op1=opt(ctx.OP1(),null,o->Op.fromString(o.getText()));
    Op _op2=opt(ctx.OP2(),null,o->Op.fromString(o.getText()));
    Op _op3=opt(ctx.OP3(),null,o->Op.fromString(o.getText()));
    var op=Stream.of(_uop,_op0,_op1,_op2,_op3).filter(o->o!=null).findFirst();
    return op.orElse(null);
    }
  @Override public Full.L.NC visitFullNC(FullNCContext ctx) {
    check(ctx);
    Pos pos=pos(ctx);
    List<Full.Doc> docs=L(ctx.doc(),(c,di)->c.add(visitDoc(di)));
    Full.CsP csP=visitCsP(ctx.csP());
    Full.E e=visitE(ctx.e());
    C c=null;
    if(csP.cs().size()==1){c=csP.cs().get(0);}
    if(c==null){
      this.errors.append(pos+Err.notValidC(csP));
      c=new C("InvalidName",-1);
      }
    return new Full.L.NC(pos,docs,c,e);
    }
  @Override public Full.Slash visitSlash(SlashContext ctx) {
    check(ctx);
    return new Full.Slash(pos(ctx));
    }
  @Override public Full.EPathSel visitPathSel(PathSelContext ctx) {
    check(ctx);
    Pos pos=pos(ctx);
    String s=ctx.getText();
    s=s.substring(1);
    var res=Parse.ctxPathSelX(Paths.get(pos.fileName()),s);
    assert !res.hasErr():
      "";
    Full.PathSel ps=new AuxVisitor(pos).visitPathSelX(res.res.pathSelX());
    assert ps!=null;
    return new Full.EPathSel(pos, ps);
    }
  @Override public Full.Cast visitCast(CastContext ctx) {
    check(ctx);
    return new Full.Cast(pos(ctx),eVoid, visitT(ctx.t()));
    }
  @Override public Full.SlashX visitSlashX(SlashXContext ctx) {
    String x=ctx.SlashX().getText().substring(1);
    return new Full.SlashX(pos(ctx), new X(x));
    }

  @Override public Full.E visitEPostfix(EPostfixContext ctx) {
    check(ctx);
    var res=visitEAtomic(ctx.eAtomic());
    var uOpList=L(ctx.children.stream().takeWhile(c->c instanceof TerminalNodeImpl));
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
      if(current instanceof StringContext){
        Full.EString tmp=visitString((StringContext)current);
        Full.E fRes=res;
        var es=pushL(fRes,popL(tmp.es()));
        res=visitString((StringContext)current).withEs(es);}
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
    check(ctx);
    S s=opt(ctx.m(),null,this::visitM);
    List<Full.Par> ps=L(ctx.par(),(c,p)->c.add(visitPar(p)));
    if(ps.size()==1 && ps.get(0)._that()==null && ps.get(0).es().isEmpty()){ps=L();}
    return new Full.Call(pos(ctx), eVoid, s, true, ps);
    }
  Full.E parseBinOp(Pos pos,List<? extends ParserRuleContext> es0,List<TerminalNode>ops0) {
    if(es0.size()==1){return (Full.E)es0.get(0).accept(this);}
    List<Full.E> es=L(es0,(c,ei)->c.add((Full.E)ei.accept(this)));
    Set<Op> ops=new LinkedHashSet<>();
    for(var oi:ops0){ops.add(Op.fromString(oi.getText()));}
    if (ops.size()!=1){
      this.errors.append(pos+Err.needBlockOps(ops));
      }
    Op op=ops.iterator().next();
    return new Full.BinOp(pos, op, es);
    }
  @Override public Full.E visitEBinary0(EBinary0Context ctx) {
    check(ctx);
    return parseBinOp(pos(ctx),ctx.ePostfix(),ctx.OP0());
    }
  @Override public Full.E visitEBinary1(EBinary1Context ctx) {
    check(ctx);
    return parseBinOp(pos(ctx),ctx.eBinary0(),ctx.OP1());
    }
  @Override public Full.E visitEBinary2(EBinary2Context ctx) {
    check(ctx);
    List<TerminalNode> ops=merge(ctx.InKw(),ctx.OP2());
    return parseBinOp(pos(ctx),ctx.eBinary1(),ops);
    }
  @Override public Full.E visitEBinary3(EBinary3Context ctx) {
    return parseBinOp(pos(ctx),ctx.eBinary2(),ctx.OP3());
    }
  @Override public Full.If visitSIf(SIfContext ctx) {
    check(ctx);
    List<Full.E> es=L(ctx.e(),(c,ei)->c.add(visitE(ei)));
    List<Full.D> matches=L(ctx.match(),(c,mi)->c.add(visitMatch(mi)));
    Full.E _condition=null;
    Full.E then=null;
    Full.E _else=null;
    if(matches.isEmpty()){
      _condition=es.get(0);
      then=es.get(1);
      if(es.size()==3){_else=es.get(2);}
      }
    else{
      assert es.size()==1;
      then=es.get(0);
      }
    return new Full.If(pos(ctx), _condition, matches, then, _else);
    }
  @Override public Full.D visitMatch(MatchContext ctx) {
    check(ctx);
    List<Full.T> ts=L(ctx.t(),(c,ti)->c.add(visitT(ti)));
    if(ctx.e()==null){
      var vartx=new VarTx(false,ts.get(0),null,visitX(ctx.x(0)));
      return new Full.D(vartx,L(),null);
      }
    if(ctx.oR()==null){
      assert ctx.e()!=null;
      var vartx=new VarTx(false,ts.get(0),null,visitX(ctx.x(0)));
      return new Full.D(vartx,L(),visitE(ctx.e()));
      }
    List<VarTx> varTxs=L(ctx.x(),(c,cxi)->{
      X xi=visitX(cxi);
      Full.T ti=null;
      int j=ctx.children.indexOf(cxi)-1;
      if(ctx.getChild(j) instanceof TContext){
        ti=visitT((TContext)ctx.getChild(j));
        }
      c.add(new VarTx(false,ti,null,xi));
      });
    VarTx first=null;
    if(ctx.getChild(0) instanceof TContext){
      first=new VarTx(false,visitT((TContext)ctx.getChild(0)),null,null);
      }
    return new Full.D(first, varTxs,visitE(ctx.e()));
    }
  @Override public Full.While visitSWhile(SWhileContext ctx) {
    check(ctx);
    return new Full.While(pos(ctx),visitE(ctx.e(0)),visitE(ctx.e(1)));
    }
  @Override public Full.For visitSFor(SForContext ctx) {
    check(ctx);
    List<Full.E> es=L(ctx.e(),(c,ei)->c.add(visitE(ei)));
    Full.E e=es.get(es.size()-1);
    es=es.subList(0, es.size()-1);
    List<Full.D> ds=L(ctx.dX(),es,(c,dxi,ei)->
      c.add(varTxEToD(visitDX(dxi),ei)));
    return new Full.For(pos(ctx), ds, e);
    }
  @Override public Full.Loop visitSLoop(SLoopContext ctx) {
    check(ctx);
    return new Full.Loop(pos(ctx), visitE(ctx.e()));
    }
  @Override public Full.Throw visitSThrow(SThrowContext ctx) {
    check(ctx);
    return new Full.Throw(pos(ctx),
      ThrowKind.fromString(ctx.Throw().getText()), visitE(ctx.e()));
    }
  @Override public Full.OpUpdate visitSUpdate(SUpdateContext ctx) {
    check(ctx);
    return new Full.OpUpdate(pos(ctx),
      visitX(ctx.x()),
      Op.fromString(ctx.OpUpdate().getText()),
      visitE(ctx.e()));    
    }
  @Override public Core.L.Info visitInfo(InfoContext ctx) {
    check(ctx);
    var pos=pos(ctx);
    var s=fixPos(pos);
    s.append(ctx.getText());
    var r=Parse.ctxInfo(Paths.get(pos.fileName()),s.toString());
    return new InfoSupplier(new InjectionToCore(errors, eVoid), r, pos).get();
    }
  @SuppressWarnings("unused")//i
  static StringBuilder fixPos(Pos pos){
    StringBuilder s=new StringBuilder();
    for(int i :range(pos.line()-1)){s.append("\n");}
    for(int i :range(pos.column()-1)){s.append(" ");}
    return s;
    }
  @Override public Program visitNudeP(NudePContext ctx) {
    Full.E e=visitFullL(ctx.fullL(0));
    PTails tail=PTails.empty;
    for(int i=ctx.children.size()-2;i>0;i-=1){
      var ci=ctx.getChild(i);
      if (!(ci instanceof FullLContext)){continue;}
      if(ctx.getChild(i-1) instanceof FullLContext){
        tail=tail.pTailSingle((Core.L)visitFullL((FullLContext)ci));
        continue;
        }
      Full.CsP csP=visitCsP((CsPContext)ctx.getChild(i-3));
      C c=null;
      if(csP.cs().size()==1){c=csP.cs().get(0);}
      if(c==null){
        this.errors.append(e.pos()+Err.notValidC(csP));
        c=new C("InvalidName",-1);
        }
      tail=tail.pTailC(c,(LL)visitFullL((FullLContext)ci));
      }
    return new Program((LL)e,tail);
    }
  @Override public Full.CsP visitNudeCsP(NudeCsPContext ctx) {return visitCsP(ctx.csP());} 
  }