package sugarVisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import sugarVisitors.Visitor;
import tools.Assertions;
import tools.Match;
import ast.Ast;
import ast.Ast.BlockContent;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.On;
import ast.ExpCore;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.Ast.Type;
import ast.Expression.ClassReuse;
import ast.Expression.WalkBy;
import ast.ExpCore.*;
import ast.ExpCore.Block.*;
import ast.ExpCore.Block.Catch;
import ast.ExpCore.ClassB.Member;
import ast.Expression;
public class InjectionOnCore implements Visitor<ExpCore> {
  public ExpCore visit(Expression.Signal s){
    return new Signal(s.getKind(),s.getInner().accept(this));}
  public ExpCore visit(Expression.Loop s) {
    return new Loop(s.getInner().accept(this));}

  public ExpCore visit(Expression.X s){return new X(s.getInner());}
    public ExpCore visit(Expression._void s){return new _void();}
  public ExpCore visit(Ast.Path s){return s;}
  public ExpCore visit(Expression.RoundBlock s){
    Doc doc = s.getDoc();
    assert s.getContents().size()<=1:s.getContents();
    List<Dec> decs=new ArrayList<Dec>();
    Optional<Catch> _catch=Optional.empty();
    ExpCore inner=s.getInner().accept(this);
    if(s.getContents().size()==1){
      BlockContent c = s.getContents().get(0);
      for(Ast.VarDec d:c.getDecs()){
        assert d instanceof Ast.VarDecXE:d;
        Ast.VarDecXE sugarDec=(Ast.VarDecXE)d;
        assert sugarDec.getT().isPresent() :sugarDec;
        Type t=sugarDec.getT().get();
        String x=sugarDec.getX();
        ExpCore e=sugarDec.getInner().accept(this);
        decs.add(new Dec(t,x,e));
        };
      if(c.get_catch().isPresent()){
        ast.Ast.Catch c1 = c.get_catch().get();
        SignalKind kind=c1.getKind();
        String x=c1.getX();
        assert x.length()>=1;
        assert c1.getOns().size()>=1;
        List<ExpCore.Block.On> ons=new ArrayList<ExpCore.Block.On>();
        for(On on:c1.getOns()){
          assert on.getTs().size()==1;
          assert !on.get_if().isPresent();
          ons.add(new ExpCore.Block.On(on.getTs().get(0),lift(on.getInner())));
        }
      _catch=Optional.of(new Catch(kind,x,ons));
      }
    }
    return new Block(doc,decs,inner,_catch,s.getP());
  }
  public ExpCore visit(Expression.ClassB s){
    Doc doc1=s.getDoc1();
    Doc doc2=s.getDoc2();
    List<Ast.Path> supertypes=s.getSupertypes();
    boolean isInterface=false;
    if(s.getH() instanceof Ast.ConcreteHeader){throw Assertions.codeNotReachable();}
    if(s.getH() instanceof Ast.InterfaceHeader){isInterface=true;}
    List<Member> members=new ArrayList<>();
    for(ast.Expression.ClassB.Member mi: s.getMs()){
      members.add(mi.match(
      m-> new ClassB.NestedClass(m.getDoc(),m.getName(),lift(m.getInner()),m.getP()),
      m-> new ClassB.MethodImplemented(m.getDoc(),m.getS(),lift(m.getInner()),m.getP()),
      m->{  Doc mdoc=m.getDoc();
            Ast.MethodSelector ms=m.getMs();
            MethodType mt=m.getMt();
            return new ClassB.MethodWithType(mdoc,ms,mt,lift(m.getInner()),m.getP());
          })
       );
    }
    ClassB result=new ClassB(doc1,doc2,isInterface,supertypes,members);
    result.getStage().setStage(s.getStage());
    return result;
    }
  public ExpCore visit(Expression.MCall s){
    assert !s.getPs().getE().isPresent():s;
    ExpCore receiver=s.getReceiver().accept(this);
    String name=s.getName();
    Doc doc=s.getDoc();
    List<String> xs=s.getPs().getXs();
    List<ExpCore>es=new ArrayList<>();
    for(Expression e:s.getPs().getEs()){es.add(e.accept(this));}
    return new MCall(receiver, new MethodSelector(name,xs),doc,es,s.getP());
  }
  public ExpCore visit(Expression.Using s){
    assert !s.getPs().getE().isPresent();
    List<String>xs=s.getPs().getXs();
    List<ExpCore>es=new ArrayList<>();
    for(Expression e : s.getPs().getEs()){es.add(e.accept(this));}
    return new Using(s.getPath(),new MethodSelector(s.getName(),xs),s.getDocs(),es,s.getInner().accept(this));
    }
  public ExpCore visit(WalkBy s) {
    return new ExpCore.WalkBy();
  }
  private ast.ExpCore lift(Expression e){return e.accept(this);}
//private Expression lift(ast.ExpCore e){return e.accept(this);}
  private Optional<ExpCore> lift(Optional<Expression> e){
    if(e.isPresent()){return Optional.of(e.get().accept(this));}
    return Optional.empty();
    }

  public ExpCore visit(Expression.If s){throw Assertions.codeNotReachable();}
  public ExpCore visit(Expression.While s){throw Assertions.codeNotReachable();}
  public ExpCore visit(Expression.With s){throw Assertions.codeNotReachable();}
  public ExpCore visit(Expression.BinOp s){throw Assertions.codeNotReachable();}
  public ExpCore visit(Expression.DocE s){throw Assertions.codeNotReachable();}
  public ExpCore visit(Expression.UnOp s){throw Assertions.codeNotReachable();}
  public ExpCore visit(Expression.FCall s){throw Assertions.codeNotReachable(s.toString());}
  public ExpCore visit(Expression.SquareCall s){throw Assertions.codeNotReachable();}
  public ExpCore visit(Expression.SquareWithCall s){throw Assertions.codeNotReachable();}
  public ExpCore visit(Expression.CurlyBlock s){throw Assertions.codeNotReachable();}
  public ExpCore visit(Expression.DotDotDot s){
    throw Assertions.codeNotReachable();}
  public ExpCore visit(Expression.Literal s){throw Assertions.codeNotReachable();}
  public ExpCore visit(ClassReuse s) {throw Assertions.codeNotReachable();}
}
