package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import tools.Map;
import ast.Ast;
import ast.Expression;
import ast.Ast.BlockContent;
import ast.Ast.Catch;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.On;
import ast.Ast.Op;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Position;
import ast.Ast.SignalKind;
import ast.Ast.Type;
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
  public static Expression of(Expression e){
    DesugarW d=new DesugarW();
    d.usedVars.addAll(CollectDeclaredVarsAndCheckNoDeclaredTwice.of(e));
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
    usedVars.addAll(CollectDeclaredVarsAndCheckNoDeclaredTwice.of(nc.getInner()));
    return super.visit(nc);
  }
  public MethodImplemented visit(MethodImplemented mi){
    this.usedVars=new HashSet<String>();
    for(String name:mi.getS().getNames()){ usedVars.add(name); }
    usedVars.add("this");
    usedVars.addAll(CollectDeclaredVarsAndCheckNoDeclaredTwice.of(mi.getInner()));
    return super.visit(mi);
    }
  public MethodWithType visit(MethodWithType mt){
    this.usedVars=new HashSet<String>();
    if(!mt.getInner().isPresent()){return super.visit(mt);}
    for(String name:mt.getMs().getNames()){this.usedVars.add(name);}
    usedVars.add("this");
    usedVars.addAll(CollectDeclaredVarsAndCheckNoDeclaredTwice.of(mt.getInner().get()));
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
  return e.withDefaultE(Optional.of(new _void()));
  }
  public Expression visit(SquareWithCall s) {
    String x=Functions.freshName("accumulator", this.usedVars);
    X xX=new X(x);
    //var dec
    VarDecXE xe=new VarDecXE(true,Optional.empty(),x,
        Desugar.getMCall(s,s.getP(),s.getReceiver(),"#begin",Desugar.getPs())
        );
    //new with
    List<On> ons = s.getWith().getOns();
    List<On> onsPrime = new ArrayList<>();
    for(On on:ons){
      onsPrime.add(on.withInner(withSquareAdd(s.getP(),xX,on.getInner())));
    }
    Optional<Expression> def = s.getWith().getDefaultE();
    Optional<Expression> defPrime =Map.of(e->withSquareAdd(s.getP(),xX,e), def);
    With w=s.getWith().withOns(onsPrime).withDefaultE(defPrime);
    List<VarDec> decs=new ArrayList<VarDec>();
    decs.add(xe);
    decs.add(new VarDecE(w));
    return Desugar.getBlock(s.getP(),decs,Desugar.appendEndMethod(s.getP(),xX,s)).accept(this);
    }

  private VarDecXE castT(Position pos,Type t, String y, String x) {
    assert t instanceof NormType;
    NormType nt=(NormType)t;
    String z=Functions.freshName("casted", usedVars);
    
    List<On> ons=new ArrayList<On>();
    List<Type> ts1=new ArrayList<Type>();
    ts1.add(t);
    List<Type> ts2=new ArrayList<Type>();
    ts2.add(new NormType(nt.getMdf(),Path.Any(),Ph.None));
    ons.add(new On(ts1,Optional.empty(),new X(z)));
    ons.add(new On(ts2,Optional.empty(),new Signal(SignalKind.Exception,new _void())));
    Catch k=new Catch(SignalKind.Return,z,ons,Optional.empty());
    RoundBlock block=Desugar.getBlock(pos,
      new Signal(SignalKind.Return,new X(x)),
      k,Desugar.errorMsg("CastT-Should be unreachable code"));
    return new VarDecXE(false,Optional.of(t),y,block);
  }
  private static Expression renameT(Expression e, Expression.X x, Type t, Expression.X y) {
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

  private Expression with_C_resolveXsBaseCase(Position pos,List<String> xs,List<On> ons, Expression def) {//case c
    if(ons.isEmpty()){return def;}//case cc
    On on0 = ons.get(0);
    List<On> ons2 = ons.subList(1, ons.size());
    if(on0.getTs().isEmpty()){
      assert on0.get_if().isPresent();
      return with_C_B(pos,on0.get_if().get(),on0.getInner(),with_C_resolveXsBaseCase(pos,xs, ons2, def));
      }
    return with_C_A(pos, xs, on0, with_C_resolveXsBaseCase(pos,xs, ons2, def));
  }
  private Expression with_C_A(Position pos, List<String> xs, On on0,Expression continuation) {
    List<String> ys=new ArrayList<String>();
    for(String x:xs){ys.add(Functions.freshName(x, usedVars));}    
    //(
    List<VarDec> decs=new ArrayList<>();
    //if ->e
    if(on0.get_if().isPresent()){
      List<On> ifOns=new ArrayList<>();
      Position extractPos = Desugar.getPosition(on0.get_if().get());
      Expression ifBody=new If(extractPos,on0.get_if().get(),new _void(),Optional.of(new Signal(SignalKind.Exception,new _void())));
      ifOns.add(new On(on0.getTs(),Optional.empty(),ifBody));
      Expression eIf=new With(extractPos,xs,Collections.emptyList(),Collections.emptyList(),ifOns,Optional.empty());
      decs.add(new Ast.VarDecE(eIf));
    }
    //casts: every cast is a block content e+catch
    {int i=-1;for(Type ti:on0.getTs()){i+=1;
    String xi=xs.get(i);
    String yi=ys.get(i);
    decs.add(castT(pos,ti,yi,xi));}}
    //catch exception Void recursive call
    Catch k=Desugar.getK(
      SignalKind.Exception, "",
      new NormType(Mdf.Immutable, Path.Void(),Ph.None),
      continuation);
    //main expression with 'T' renaming
    Expression e0=on0.getInner();
    {int i=-1;for(Type ti:on0.getTs()){i+=1;
      String xi=xs.get(i);
      String yi=ys.get(i);
      e0=renameT(e0,new Expression.X(xi), ti, new Expression.X(yi));
    }}
    BlockContent content=new BlockContent(decs,Optional.of(k));
    List<BlockContent> contents=new ArrayList<BlockContent>();
    contents.add(content);
    contents.add(Desugar.getBlockContent(e0));
    //void)  
    return new RoundBlock(pos,Doc.empty(),new _void(),contents);
  }

  private static Expression with_C_B(Position p,Expression cond, Expression then, Expression _else) {
    return new Expression.If(p,cond,then,Optional.of(_else));
  }
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
      b=XInE.of(new X(xi),Desugar.getMCall(new X(xi),pos,new X(xi),"#inner", Desugar.getPs()),b);
      }
    RoundBlock inner=new RoundBlock(pos,Doc.empty(),b,bc);
    Catch k=Desugar.getK(SignalKind.Exception, "",
        new NormType(Mdf.Immutable,Path.Void(),Ph.None),
        new _void());
    inner=Desugar.getBlock(pos,new Loop(inner), k, new _void());
    Expression result=withDeclareIts(is,inner);
    //accept
    return result;
  }

  private Expression withDeclareIts(List<VarDecXE> is, RoundBlock inner) {
    if(is.isEmpty()){return inner;}
    VarDecXE i0 = is.get(0);
    List<VarDecXE> is2 = is.subList(1, is.size());
    List<VarDec> decs=new ArrayList<VarDec>();
    decs.add(i0.withVar(false));
    RoundBlock conclusive = withDeclareItsNestedBlock(inner, i0, is2);
    return Desugar.getBlock(inner.getP(),decs,conclusive);
  }

  private RoundBlock withDeclareItsNestedBlock(RoundBlock inner, VarDecXE i0, List<VarDecXE> is2) {
    Expression recursive=withDeclareIts(is2,inner);
    Expression eClose=Desugar.getMCall(new X(i0.getX()),inner.getP(),new X(i0.getX()),"#close",Desugar.getPs());
    Catch k1 = withDesugarGetDefaultCatch(inner.getP(),SignalKind.Exception,eClose);
    Catch k2 = withDesugarGetDefaultCatch(inner.getP(),SignalKind.Return,eClose);
    RoundBlock conclusive1=Desugar.getBlock(inner.getP(),recursive, k1, new _void());
    RoundBlock conclusive2=Desugar.getBlock(inner.getP(),conclusive1, k2, eClose);
    return conclusive2;
  }

  private Catch withDesugarGetDefaultCatch(Position pos,SignalKind kind,Expression eClose) {
    String propagated1=Functions.freshName("propagated",usedVars);
    Expression blockPropagate1=Desugar.getBlock(pos,eClose,new Signal(kind,new X(propagated1)));
    Catch k1=new Catch(kind, propagated1, new ArrayList<>(),Optional.of(blockPropagate1));
    return k1;
  }

  private static RoundBlock withE1CatchExceptionOnVoidE2elseE3(Position pos,Expression e1,Expression e2,Expression e3) {
    Catch k=Desugar.getK(SignalKind.Exception, "", new NormType(Mdf.Immutable,Path.Void(),Ph.None),e2);
    List<BlockContent> cs=new ArrayList<BlockContent>(); 
    cs.add(Desugar.getBlockContent(e1,k));
    return new RoundBlock(pos,Doc.empty(),e3,cs);
  }


  private static BlockContent withNext(Position pos,int index,List<String> xs) {
    Expression eStart=Desugar.getMCall(new X(xs.get(index)),pos,new X(xs.get(index)),"#next",Desugar.getPs());
    List<VarDec> decs=new ArrayList<>();
    for(String x:xs.subList(index+1, xs.size())){
      Expression ei=Desugar.getMCall(new X(x),pos,new X(x),"#next",Desugar.getPs());
      ei=withE1CatchExceptionOnVoidE2elseE3(pos,ei,new _void(),new _void());
      decs.add(new VarDecE(ei));
    }
    for(String x:xs){
      Expression ei=Desugar.getMCall(new X(x),pos,new X(x),"#checkEnd",Desugar.getPs());
      ei=withE1CatchExceptionOnVoidE2elseE3(pos,ei,new _void(),new _void());
      decs.add(new VarDecE(ei));
    }
    Expression eCatch=Desugar.getBlock(pos,decs, new Signal(SignalKind.Exception,new _void()));
    Catch k=Desugar.getK(SignalKind.Exception, "", new NormType(Mdf.Immutable,Path.Void(),Ph.None),eCatch);
    return Desugar.getBlockContent(eStart,k);
  }

  private static Expression withSquareAdd(Position pos,X x,Expression inner) {
  Expression result=new BinOp(pos,x,Op.ColonEqual,new MCall(inner,pos,x,"#add",Doc.empty(),Desugar.getPs(inner)));
  return result;
  }
}