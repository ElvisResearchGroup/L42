package sugarVisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import tools.Map;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Parameters;
import ast.Ast;
import ast.Ast.ConcreteHeader;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.Ast.VarDec;
import ast.Expression;
import ast.Expression.ClassB.Member;
import ast.Expression.*;
import ast.Expression.ClassB.*;

public class CloneVisitor implements Visitor<Expression>{
  protected <T extends Expression>T lift(T e){
    @SuppressWarnings("unchecked")
    T result= (T)e.accept(this);
    //assert result.getClass().equals(e.getClass());
    //no, I would like to assert on T, that is a supertype of getClass()
    //assert L42.checkWellFormedness(e);
    //assert L42.checkWellFormedness(result);
    return result;
    }
  protected Path liftP(Path p){return p;}
  protected Type liftT(Type t){
    assert t!=null;
    return new NormType(t.getMdf(),liftP(t.getPath()),liftDoc(t.getDoc()));
        
    }
  protected Expression.Catch liftK(Expression.Catch k){
    if (k instanceof DesugarCatchDefault.CatchToComplete){
      k = this.liftK(((DesugarCatchDefault.CatchToComplete) k).catch1);
      return new DesugarCatchDefault.CatchToComplete((Catch1) k);
      }
    return k.match(k1->new Expression.Catch1(k1.getP(),
      k1.getKind(),
      liftT(k1.getT()),
      k1.getX(),
      lift(k1.getInner())
      ),
      kM->new Expression.CatchMany(kM.getP(),kM.getKind(),
          Map.of(this::liftT,kM.getTs()) , lift(kM.getInner())),
      kP->new Expression.CatchProp(kP.getP(),kP.getKind(),
          Map.of(this::liftT,kP.getTs()) , lift(kP.getInner()))
      );
    }
  protected Expression.With.On liftO(Expression.With.On on){
    return new Expression.With.On(Map.of(this::liftT,on.getTs()),lift(on.getInner()));
    }
  protected Header liftH(Header h) {
    return h.match(ch->new ConcreteHeader(
        ch.getMdf(),ch.getName(), Map.of(this::liftF,ch.getFs()),ch.getP()
        ), th->th, ih->ih);
  }
  protected FieldDec liftF(FieldDec f) {
    return new FieldDec(f.isVar(),liftT(f.getT()),f.getName(),liftDoc(f.getDoc()));
  }
  protected Doc liftDoc(Doc doc) {
    return doc.withAnnotations(Map.of(ann->{
      if(ann instanceof Path){return this.liftP((Path)ann);}
      return ann;},doc.getAnnotations()));
  }
  protected Expression.BlockContent liftBC(Expression.BlockContent c) {
    List<VarDec> liftVarDecs = liftVarDecs(c.getDecs());
    List<Expression.Catch> liftK = this.liftKs(c.get_catch());
    return new Expression.BlockContent(liftVarDecs,liftK);
  }
  protected List<Catch> liftKs(List<Catch> ks) {
    List<Catch> result=new ArrayList<>();
    for(Catch k:ks){result.add(liftK(k));}
    return result;
  }
  protected List<ast.Ast.VarDec> liftVarDecs(List<ast.Ast.VarDec> ds) {
    return Map.of(this::liftVarDec, ds);
  }
  protected ast.Ast.VarDec liftVarDec(ast.Ast.VarDec d) {
    return d.match(
      this::liftVarDecXE,
      this::liftVarDecE,
      this::liftVarDecCE);
  }
  protected ast.Ast.VarDecXE liftVarDecXE(ast.Ast.VarDecXE d) {
    return new ast.Ast.VarDecXE(d.isVar(),Map.of(this::liftT,d.getT()),d.getX(),lift(d.getInner()));
  }
  protected ast.Ast.VarDecE liftVarDecE(ast.Ast.VarDecE d) {
    return new ast.Ast.VarDecE(lift(d.getInner()));
  }
  protected ast.Ast.VarDecCE liftVarDecCE(ast.Ast.VarDecCE d) {
    return new ast.Ast.VarDecCE(this.visit(d.getInner()));
  }
  protected Member liftM(Member m) {
    return m.match(
      nc->visit(nc),
      mi->visit(mi),
      mt->visit(mt)
      );
  }
  public NestedClass visit(NestedClass nc){return new NestedClass(liftDoc(nc.getDoc()),nc.getName(),lift(nc.getInner()),null);}
  public MethodImplemented visit(MethodImplemented mi){return new ClassB.MethodImplemented(liftDoc(mi.getDoc()), liftMs(mi.getS()), lift(mi.getInner()),null);}
  public MethodWithType visit(MethodWithType mt){
    return new ClassB.MethodWithType(liftDoc(mt.getDoc()),
      liftMs(mt.getMs()),
      liftMT(mt.getMt()),
      Map.of(this::lift,mt.getInner()),mt.getP());}

  protected MethodSelector liftMs(MethodSelector ms) {
    return ms;
  }

  protected MethodType liftMT(MethodType mt) {
    return new MethodType(mt.isRefine(),
      mt.getMdf(),
      Map.of(this::liftT,mt.getTs()),
      liftT(mt.getReturnType()),
      Map.of(this::liftT,mt.getExceptions()));
  }
  public Expression visit(Expression.EPath s) {return s.withInner(liftP(s.getInner()));}
  public Expression visit(X s) { return s;}
  public Expression visit(_void s) {return s;}
  public Expression visit(WalkBy s) {return s;}
  public Expression visit(Using s) {
    return new Using(liftP(s.getPath()),s.getName(),liftDoc(s.getDocs()),liftPs(s.getPs()),lift(s.getInner()));
  }
  protected Parameters liftPs(Parameters ps) {
    return new Parameters(
      Map.of(this::lift, ps.getE()),
      ps.getXs(),
      Map.of(this::lift, ps.getEs())
      );
  }
  public Expression visit(Signal s) {
    return new Signal(s.getKind(),lift(s.getInner()));
  }
  public Expression visit(Loop s) {
    return new Loop(lift(s.getInner()));
  }
  public Expression visit(MCall s) {
    return new MCall(lift(s.getReceiver()),s.getName(),liftDoc(s.getDoc()),liftPs(s.getPs()),s.getP());
  }
  public Expression visit(ClassB s) {
    Header h = liftH(s.getH());
    List<FieldDec> fs=Map.of(this::liftF,s.getFields());
    List<Type> superT = Map.of(this::liftT,s.getSupertypes());
    List<Member> ms = Map.of(this::liftM,s.getMs());
    return new ClassB(liftDoc(s.getDoc1()),h,fs,superT,ms,s.getP());
  }
  @Override
  public Expression visit(If s) {
    return new If(s.getP(),lift(s.getCond()),lift(s.getThen()),Map.of(this::lift,s.get_else()));
  }
  @Override
  public Expression visit(While s) {
    return new While(s.getP(),lift(s.getCond()),lift(s.getThen()));
  }
  @Override
  public Expression visit(With s) {
    return new With(s.getP(),s.getXs(),Map.of(this::liftVarDecXE, s.getIs()),Map.of(this::liftVarDecXE, s.getDecs()),Map.of(this::liftO,s.getOns()),Map.of(this::lift, s.getDefaultE()));
  }
  @Override
  public Expression visit(BinOp s) {
    return new BinOp(s.getP(),lift(s.getLeft()),s.getOp(),liftDoc(s.getDoc()),lift(s.getRight()));
  }
  @Override
  public Expression visit(DocE s) {
    return new DocE(lift(s.getInner()),liftDoc(s.getDoc()));
  }
  @Override
  public Expression visit(UnOp s) {
    return new UnOp(s.getP(),s.getOp(),lift(s.getInner()));
  }
  @Override
  public Expression visit(FCall s) {
    return new FCall(s.getP(),lift(s.getReceiver()),liftDoc(s.getDoc()),liftPs(s.getPs()));
  }
  @Override
  public Expression visit(SquareCall s) {
    return new SquareCall(s.getP(),lift(s.getReceiver()),liftDoc(s.getDoc()),Map.of(this::liftDoc,s.getDocs()),Map.of(this::liftPs,s.getPss()));
  }
  @Override
  public Expression visit(SquareWithCall s) {
    return new SquareWithCall(s.getP(),lift(s.getReceiver()),lift(s.getWith()));
  }
  @Override
  public Expression visit(UseSquare s) {
    return new UseSquare(lift(s.getInner()));
  }
  @Override
  public Expression visit(RoundBlock s) {
    List<BlockContent> content = Map.of(this::liftBC,s.getContents());
    Expression inner = lift(s.getInner());
    Expression result= new RoundBlock(s.getP(),liftDoc(s.getDoc()), inner,content);
    //assert L42.checkWellFormedness(s);
    //assert L42.checkWellFormedness(result);
    return result;
  }
  @Override
  public Expression visit(CurlyBlock s) {
    Expression result=  new CurlyBlock(s.getP(),liftDoc(s.getDoc()),Map.of(this::liftBC,s.getContents()));
    //assert L42.checkWellFormedness(s);
    //assert L42.checkWellFormedness(result);
    return result;
  }
  @Override
  public Expression visit(DotDotDot s) {
    return s;
  }
  @Override
  public Expression visit(Literal s) {
    return new Literal(s.getP(),lift(s.getReceiver()),s.getInner(),s.isNumber());
  }
  @Override
  public Expression visit(ClassReuse s) {
    //assert s.getUrlFetched()==null;
    return new ClassReuse(lift(s.getInner()),s.getUrl(),s.getUrlFetched());
  }
  @Override public Expression visit(ContextId s) {
    return s;
  }

}

