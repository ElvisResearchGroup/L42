package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import tools.Map;
import ast.Ast;
import ast.Expression;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Op;
import ast.Ast.Parameters;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.SignalKind;
import ast.Ast.NormType;
import ast.Expression.Catch;
import ast.Expression.Catch1;
import ast.Expression.With.On;
import ast.Expression.BlockContent;
import ast.Ast.VarDec;
import ast.Ast.VarDecE;
import ast.Ast.VarDecXE;
import ast.Expression.BinOp;
import ast.Expression.ClassB;
import ast.Expression.If;
import ast.Expression.Loop;
import ast.Expression.MCall;
import ast.Expression.RoundBlock;
import ast.Expression.Signal;
import ast.Expression.SquareCall;
import ast.Expression.SquareWithCall;
import ast.Expression.With;
import ast.Expression.X;
import ast.Expression._void;
import ast.Expression.ClassB.MethodImplemented;
import ast.Expression.ClassB.MethodWithType;
import ast.Expression.ClassB.NestedClass;
import auxiliaryGrammar.Functions;

class DesugarW extends CloneVisitor{
  Set<String> usedVars=new HashSet<String>();
  public static Expression of(Set<String> usedVars,Expression e){
    DesugarW d=new DesugarW();
    d.usedVars=usedVars;
    Expression result= e.accept(d);
    return result;
  }
  public Expression visit(ClassB s) {
    Set<String> oldUsedVars = this.usedVars;
    try{ return (ClassB)super.visit(s);}
    finally{ this.usedVars=oldUsedVars; }// it reset but not replace, membes replace
  }
  public NestedClass visit(NestedClass nc){
    this.usedVars=new HashSet<String>();
    usedVars.addAll(CollectDeclaredVars.of(nc.getInner()));
    return super.visit(nc);
  }
  public MethodImplemented visit(MethodImplemented mi){
    this.usedVars=new HashSet<String>();
    for(String name:mi.getS().getNames()){ usedVars.add(name); }
    usedVars.add("this");
    usedVars.addAll(CollectDeclaredVars.of(mi.getInner()));
    return super.visit(mi);
    }
  public MethodWithType visit(MethodWithType mt){
    this.usedVars=new HashSet<String>();
    if(!mt.getInner().isPresent()){return super.visit(mt);}
    for(String name:mt.getMs().getNames()){this.usedVars.add(name);}
    usedVars.add("this");
    usedVars.addAll(CollectDeclaredVars.of(mt.getInner().get()));
    return super.visit(mt);
      }

  public Expression visit(With e){
    Position pos=e.getP();
    e=with_A_applyDefault(e);//case a
    if(e.getIs().isEmpty() && e.getDecs().isEmpty()){//case c
      return with_C_resolveXsBaseCase(pos,e.getXs(),e.getOns(),e.getDefaultE().get()).accept(this);
      }
    if(e.getIs().isEmpty()){return with_B_noI_makeBlock(e).accept(this);}  //case b
    //assert !e.getIs().isEmpty();
    if(e.getXs().isEmpty() && e.getDecs().isEmpty() && e.getOns().isEmpty()){//case e
      Expression b=e.getDefaultE().get();
      b=b.accept(this);//NOTE WELL: needed to avoid xs that can not be replaced in with xs... this is also the reason we need DesugarW in the first place.
      return with_E_handleIs(pos,e.getIs(),b).accept(this);
    }
    return with_D_replace_XID_with_InestedDwithX(e).accept(this);//case d
    }
  private static With with_A_applyDefault(With e) {//a (case a in 6 pages)
  if (e.getDefaultE().isPresent()){return e;}
  return e.withDefaultE(Optional.of(Expression._void.instance));
  }
  public Expression visit(SquareWithCall s) {
    //we can assumethe receivers are normalized after DesugarContext
    assert s.getReceiver() instanceof Ast.Atom: s;
    //(b=r.builder() b.a() b.b() b.c() .... b)
    List<VarDec> vd=new ArrayList<>();
    Expression k=Desugar.getMCall(s.getP(),s.getReceiver(),"#seqBuilder",Desugar.getPs());
    String x=Functions.freshName("b", usedVars);
    X b=new X(s.getP(),x);
    vd.add(new VarDecXE(false, Optional.empty(), x, k));
    Expression ew=s.getWith().accept(this);
    ew=ew.accept(new CloneVisitor(){
      @Override public Expression visit(Expression.UseSquare u){
        SquareCall sq=(SquareCall)u.getInner();
        List<VarDec> vd=new ArrayList<>();//inner, hide external one to prevent use (was a bug before)
        for(Parameters ps:sq.getPss()){
          vd.add(new VarDecE(Desugar.getMCall(s.getP(),b,"#add",ps)));
        }
        return Desugar.getBlock(s.getP(),vd,Expression._void.instance);
    }});
   vd.add(new VarDecE(ew));
    Expression inner=Desugar.getBlock(s.getP(), vd, b);
    Parameters ps=new Parameters(Optional.empty(), Collections.singletonList("seqBuilder"),Collections.singletonList(inner));
    return Desugar.getMCall(s.getP(),s.getReceiver(),"#from",ps);
    }
  private void oldWith_noUseKw(SquareWithCall s, X xX, List<VarDec> decs) {
    List<With.On> ons = s.getWith().getOns();
    List<With.On> onsPrime = new ArrayList<>();
    for(With.On on:ons){
      onsPrime.add(on.withInner(withSquareAdd(s.getP(),xX,on.getInner())));
    }
    Optional<Expression> def = s.getWith().getDefaultE();
    Optional<Expression> defPrime =Map.of(e->withSquareAdd(s.getP(),xX,e), def);
    With w=s.getWith().withOns(onsPrime).withDefaultE(defPrime);
    decs.add(new VarDecE(w));
  }

  private VarDecXE castT(Position pos,NormType t, String y, String x) {
    assert t instanceof NormType;
    NormType nt=t;
    String z=Functions.freshName("casted", usedVars);

    List<Catch> ks=new ArrayList<>();
    NormType t2=new NormType(nt.getMdf(),Path.Any(),Doc.empty());
    ks.add(new Expression.Catch1(pos,SignalKind.Return,t,z,//case return captured
        new X(pos,z)));//return it
    ks.add(new Expression.Catch1(pos,SignalKind.Return,t2,z,//else
        new Signal(SignalKind.Exception,Expression._void.instance)));// exception void
    RoundBlock block=Desugar.getBlock(pos,
      new Signal(SignalKind.Return,new X(pos,x)),
      ks,Desugar.errorMsg("CastT-Should be unreachable code"));
    return new VarDecXE(false,Optional.of(t),y,block);
  }
  private static Expression renameT(Expression e, Expression.X x, NormType t, Expression.X y) {
    assert t instanceof NormType;
    NormType nt=(NormType)t;
    if(nt.getPath().equals(Path.Any())){return e;}
    return XInE.of(x,y,e);
  }
  private static Expression innerWithXs(With e) {//right side of case b and part of right side of case d
    Expression inner=e.getDefaultE().get();
    List<String> xs=new ArrayList<String>(e.getXs());
    for(VarDecXE d:e.getDecs()){xs.add(d.getX());}
    for(VarDecXE d:e.getIs()){xs.add(d.getX());}
    inner=new With(e.getP(),xs, Collections.emptyList(),Collections.emptyList(), e.getOns(),e.getDefaultE());
    return inner;
  }
  private static  Expression with_B_noI_makeBlock(With e) {
    if(e.getDecs().isEmpty()){
      assert !e.getXs().isEmpty();
      return e;
      }
    return Desugar.getBlock(e.getP(),e.getDecs(),innerWithXs(e));
  }

  private Expression with_C_resolveXsBaseCase(Position pos,List<String> xs,List<With.On> ons, Expression def) {//case c
    if(ons.isEmpty()){return def;}//case cc
    With.On on0 = ons.get(0);
    List<With.On> ons2 = ons.subList(1, ons.size());
   assert !on0.getTs().isEmpty();
   return with_C_A(pos, xs, on0, with_C_resolveXsBaseCase(pos,xs, ons2, def));
  }
  private Expression with_C_A(Position pos, List<String> xs, With.On on0,Expression continuation) {
    List<String> ys=new ArrayList<String>();
    for(String x:xs){ys.add(Functions.freshName(x, usedVars));}
    //(
    List<VarDec> decs=new ArrayList<>();
    //TODO:?? now is very strange and requires the mdf to propagate it to the locally introduced var?
    //casts: every cast is a block content e+catch
    {int i=-1;for(NormType ti:on0.getTs()){i+=1;
    String xi=xs.get(i);
    String yi=ys.get(i);
    decs.add(castT(pos,ti,yi,xi));}}
    //catch exception Void recursive call
    Catch k=Desugar.getK(pos,
      SignalKind.Exception, "",
      NormType.immVoid,
      continuation);
    //main expression with 'T' renaming
    Expression e0=on0.getInner();
    {int i=-1;for(NormType ti:on0.getTs()){i+=1;
      String xi=xs.get(i);
      String yi=ys.get(i);
      e0=renameT(e0,new Expression.X(pos,xi), ti, new Expression.X(pos,yi));
    }}
    BlockContent content=new BlockContent(decs,Collections.singletonList(k));
    List<BlockContent> contents=new ArrayList<BlockContent>();
    contents.add(content);
    contents.add(Desugar.getBlockContent(e0));
    //void)
    return new RoundBlock(pos,Doc.empty(),Expression._void.instance,contents);
  }

  /*private static Expression with_C_B(Position p,Expression cond, Expression then, Expression _else) {
    return new Expression.If(p,cond,then,Optional.of(_else));
  }*/
  private static Expression with_D_replace_XID_with_InestedDwithX(With e) {//case d
  assert e.getDefaultE().isPresent();
  assert !e.getIs().isEmpty();
  return new With(e.getP(),Collections.emptyList(),e.getIs(),Collections.emptyList(),Collections.emptyList(),
    Optional.of(Desugar.getBlock(e.getP(),e.getDecs(),innerWithXs(e))));
  }

  private Expression with_E_handleIs(Position pos,List<VarDecXE> is, Expression b) {//case e in 6 pages
    //xs is dom(Is)
    List<String>xs=new ArrayList<>();
    for(VarDecXE i:is){xs.add(i.getX());}
    //di ki is block content =nexti(xs)
    List<BlockContent>bc=new ArrayList<>();
    for(int i=0;i<xs.size();i++){bc.add(withNext(pos,i,xs));}
    //inner =di ki s b[xs:=xs.#inner()]
    for(String xi:xs){
      b=XInE.of(new X(pos,xi),Desugar.getMCall(pos,new X(pos,xi),"#inner", Desugar.getPs()),b);
      }
    RoundBlock inner=new RoundBlock(pos,Doc.empty(),b,bc);
    Catch k=Desugar.getK(pos,SignalKind.Exception, "",
        NormType.immVoid,
        Expression._void.instance);
    inner=Desugar.getBlock(pos,new Loop(inner), Collections.singletonList(k), Expression._void.instance);
    Expression result=withDeclareIts(is,inner);
    //accept
    return result;
  }

  private RoundBlock withDeclareIts(List<VarDecXE> is, RoundBlock inner) {
    if(is.isEmpty()){return inner;}
    VarDecXE i0 = is.get(0);
    List<VarDecXE> is2 = is.subList(1, is.size());
    List<VarDec> decs=new ArrayList<VarDec>();
    decs.add(i0.withVar(false));
    RoundBlock conclusive = withDeclareItsNestedBlock(inner, i0, is2);
    return Desugar.getBlock(inner.getP(),decs,conclusive);
  }

  private RoundBlock withDeclareItsNestedBlock(RoundBlock inner, VarDecXE i0, List<VarDecXE> is2) {
    RoundBlock recursive=withDeclareIts(is2,inner);
    /*Expression eClose=Desugar.getMCall(inner.getP(),new X(inner.getP(),i0.getX()),"#close",Desugar.getPs());
    Catch k1 = withDesugarGetDefaultCatch(inner.getP(),SignalKind.Exception,eClose);
    Catch k2 = withDesugarGetDefaultCatch(inner.getP(),SignalKind.Return,eClose);
    RoundBlock conclusive1=Desugar.getBlock(inner.getP(),recursive, Collections.singletonList(k1), Expression._void.instance);
    RoundBlock conclusive2=Desugar.getBlock(inner.getP(),conclusive1, Collections.singletonList(k2), eClose);
    return conclusive2;*/
    return recursive;
  }

  private Catch withDesugarGetDefaultCatch(Position pos,SignalKind kind,Expression eClose) {
    String propagated1=Functions.freshName("propagated",usedVars);
    Expression blockPropagate1=Desugar.getBlock(pos,eClose,new Signal(kind,new X(pos,propagated1)));
    NormType t=NormType.immAny;
    Expression.Catch1 k1=new Expression.Catch1(pos,kind, t,propagated1,blockPropagate1);
    return new DesugarCatchDefault.CatchToComplete(k1);
  }

  private static RoundBlock withE1CatchExceptionOnVoidE2elseE3(Position pos,Expression e1,Expression e2,Expression e3) {
    Expression.Catch k=Desugar.getK(pos,SignalKind.Exception, "", NormType.immVoid,e2);
    List<Expression.BlockContent> cs=new ArrayList<>();
    cs.add(Desugar.getBlockContent(e1,k));
    return new RoundBlock(pos,Doc.empty(),e3,cs);
  }


  private static Expression.BlockContent withNext(Position pos,int index,List<String> xs) {
    Expression eStart=Desugar.getMCall(pos,new X(pos,xs.get(index)),"#next",Desugar.getPs());
    List<VarDec> decs=new ArrayList<>();
    for(String x:xs.subList(index+1, xs.size())){
      Expression ei=Desugar.getMCall(pos,new X(pos,x),"#next",Desugar.getPs());
      ei=withE1CatchExceptionOnVoidE2elseE3(pos,ei,Expression._void.instance,Expression._void.instance);
      decs.add(new VarDecE(ei));
    }
    for(String x:xs){
      Expression ei=Desugar.getMCall(pos,new X(pos,x),"#checkEnd",Desugar.getPs());
      ei=withE1CatchExceptionOnVoidE2elseE3(pos,ei,Expression._void.instance,Expression._void.instance);
      decs.add(new VarDecE(ei));
    }
    Expression eCatch=Desugar.getBlock(pos,decs, new Signal(SignalKind.Exception,Expression._void.instance));
    Expression.Catch k=Desugar.getK(pos,SignalKind.Exception, "", NormType.immVoid,eCatch);
    return Desugar.getBlockContent(eStart,k);
  }

  private static Expression withSquareAdd(Position pos,X x,Expression inner) {
  Expression result=new BinOp(pos,x,Op.ColonEqual,Doc.empty(),new MCall(x,"#add",Doc.empty(),Desugar.getPs(inner),pos));
  return result;
  }
}