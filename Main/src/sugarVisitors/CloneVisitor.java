package sugarVisitors;

import java.util.List;
import java.util.Optional;
import tools.Map;
import ast.Ast.BlockContent;
import ast.Ast.Catch;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Parameters;
import ast.Ast.ConcreteHeader;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.HistoricType;
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
  protected Type liftT(Type t){
    return t.match(
        nt->(Type)new NormType(nt.getMdf(),lift(nt.getPath()),nt.getPh()),
        ht->(Type)new HistoricType(lift(ht.getPath()),ht.getSelectors(),ht.isForcePlaceholder())
        );
    }
  protected ast.Ast.Catch liftK(ast.Ast.Catch k){
    return new ast.Ast.Catch(
      k.getKind(),
      k.getX(),
      Map.of(this::liftO,k.getOns()),
      Map.of(this::lift, k.get_default())
      );
    }
  protected ast.Ast.On liftO(ast.Ast.On on){
    return new ast.Ast.On(Map.of(this::liftT,on.getTs()),Map.of(this::lift,on.get_if()),lift(on.getInner()));
    }
  protected Header liftH(Header h) {
    return h.match(ch->new ConcreteHeader(
        ch.getMdf(),ch.getName(), Map.of(this::liftF,ch.getFs())        
        ), th->th, ih->ih);
  }
  protected FieldDec liftF(FieldDec f) {
    return new FieldDec(f.isVar(),liftT(f.getT()),f.getName(),liftDoc(f.getDoc()));
  }
  protected Doc liftDoc(Doc doc) {
    return doc.withAnnotations(Map.of(ann->{
      if(ann instanceof Path){return this.visit((Path)ann);}
      return ann;},doc.getAnnotations()));
  }
  protected ast.Ast.BlockContent liftBC(ast.Ast.BlockContent c) {
    List<VarDec> liftVarDecs = liftVarDecs(c.getDecs());
    Optional<Catch> liftK = Map.of(this::liftK,c.get_catch());
    return new ast.Ast.BlockContent(liftVarDecs,liftK);
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
  public NestedClass visit(NestedClass nc){return new NestedClass(liftDoc(nc.getDoc()),nc.getName(),lift(nc.getInner()));}
  public MethodImplemented visit(MethodImplemented mi){return new ClassB.MethodImplemented(liftDoc(mi.getDoc()), liftMs(mi.getS()), lift(mi.getInner()));}
  public MethodWithType visit(MethodWithType mt){
    return new ClassB.MethodWithType(liftDoc(mt.getDoc()),
      liftMs(mt.getMs()),
      liftMT(mt.getMt()),
      Map.of(this::lift,mt.getInner()));}
  
  protected MethodSelector liftMs(MethodSelector ms) {
    return ms;
  }
  protected MethodType liftMT(MethodType mt) {
    return new MethodType(
        liftDoc(mt.getDocExceptions()),
      mt.getMdf(),
      Map.of(this::liftT,mt.getTs()),
      Map.of(this::liftDoc,mt.getTDocs()),
      liftT(mt.getReturnType()),
      Map.of(this::lift,mt.getExceptions()));
  }
  public Expression visit(Path s) {return s;}
  public Expression visit(X s) { return s;}
  public Expression visit(_void s) {return s;}
  public Expression visit(WalkBy s) {return s;}
  public Expression visit(Using s) {
    return new Using(lift(s.getPath()),s.getName(),liftDoc(s.getDocs()),liftPs(s.getPs()),lift(s.getInner()));
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
    return new MCall(s.getSource(),s.getP(),lift(s.getReceiver()),s.getName(),liftDoc(s.getDoc()),liftPs(s.getPs()));
  }
  public Expression visit(ClassB s) {
    Header h = liftH(s.getH());
    List<Path> superT = Map.of(this::lift,s.getSupertypes());
    List<Member> ms = Map.of(this::liftM,s.getMs());
    return new ClassB(liftDoc(s.getDoc1()),liftDoc(s.getDoc2()),h,superT,ms,s.getStage());
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
    return new BinOp(s.getP(),lift(s.getLeft()),s.getOp(),lift(s.getRight()));
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
    return new ClassReuse(lift(s.getInner()),s.getUrl());
  }
  
}

