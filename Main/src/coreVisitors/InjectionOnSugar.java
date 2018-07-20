
package coreVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import coreVisitors.Visitor;
import tools.Match;
import ast.Ast;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Header;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Type;
import ast.Ast.VarDec;
import ast.Ast.VarDecXE;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.UpdateVar;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import ast.Expression.ClassB.Member;
import ast.Expression;
import auxiliaryGrammar.WellFormedness;

public class InjectionOnSugar implements Visitor<ast.Expression> {

  @Override public Expression visit(ExpCore.EPath s) {
    return new Expression.EPath(s.getP(),s.getInner());
  }

  @Override public Expression visit(X s) {
    return new Expression.X(s.getP(),s.getInner());
  }

  @Override public Expression visit(_void s) {
    return Expression._void.instance;
  }

  @Override public Expression visit(Using s) {
    List<String> xs=s.getS().getNames();
    List<ast.ExpCore> es1=s.getEs();
    List<ast.Expression> es=new ArrayList<ast.Expression>();
    for( ast.ExpCore e : es1){es.add(lift(e));}
    ast.Ast.Parameters ps=new ast.Ast.Parameters(Optional.<ast.Expression>empty(), xs, es);
    return new Expression.Using(s.getPath(),s.getS().nameToS(),s.getDoc(),ps,lift(s.getInner()));
  }

  @Override public Expression visit(Signal s) {
    return new Expression.Signal(s.getKind(),lift(s.getInner()));
  }
  @Override public Expression visit(Loop s) {
    return new Expression.Loop(lift(s.getInner()));
  }
  @Override public Expression visit(MCall s) {
    ast.Expression receiver=lift(s.getInner());
    String name=s.getS().nameToS();
     Doc docs = s.getDoc();
    List<String> xs=s.getS().getNames();
    List<ast.ExpCore> es1=s.getEs();
    List<ast.Expression> es=new ArrayList<ast.Expression>();
    for( ast.ExpCore e : es1){es.add(lift(e));}
    ast.Ast.Parameters ps=new ast.Ast.Parameters(Optional.<ast.Expression>empty(), xs, es);
    Position pos=s.getP();
    return new Expression.MCall(receiver,name,docs,ps,pos);
  }

  @Override public Expression visit(Block s) {
    Doc docs=s.getDoc();
    Expression inner=lift(s.getInner());
    List<VarDec> decs= new ArrayList<VarDec>();
    for(int i=0;i<s.getDecs().size();i++){
      Optional<Type> t=s.getDecs().get(i).getT();
      String x=s.getDecs().get(i).getX();
      Expression e=lift(s.getDecs().get(i).getInner());
      decs.add(new VarDecXE(s.getDecs().get(i).isVar(),t,x,e));
    }
    List<Expression.Catch> _catch=injectionCatch(s.getOns());
    List<Expression.BlockContent> contents=new ArrayList<>();
    if(!decs.isEmpty() || !_catch.isEmpty()){
      contents.add(new Expression.BlockContent(decs,_catch));
      }
    Expression.Position pos=s.getP();
    Expression.RoundBlock result=new Expression.RoundBlock(pos,docs,inner,contents);
    //assert WellFormedness.blockCheck(result);, no it can actually get wrong?
    return result;
  }

  private List<Expression.Catch> injectionCatch(List<ast.ExpCore.Block.On> list) {
    if(list.isEmpty()){return Collections.emptyList();}
    List<Expression.Catch> ks=new ArrayList<>();
    for( ast.ExpCore.Block.On on:list){
      Expression inner = lift(on.getInner());
      assert !on.getT().getMdf().isIn(Mdf.MutablePFwd,Mdf.ImmutablePFwd);
      ks.add(new Expression.Catch1(on.getP(),on.getKind(),on.getT(),on.getX(),inner));
    }
    return ks;
  }

  @Override public Expression visit(ClassB s) {
    Doc doc1=s.getDoc1();
    Header h=(s.isInterface())?new Ast.InterfaceHeader():new Ast.TraitHeader();
    List<Type>supertypes=s.getSupertypes();
    List<Member> members=new ArrayList<>();
    for(ast.ExpCore.ClassB.Member mi:s.getMs()){
      members.add(mi.match(
        nc->new Expression.ClassB.NestedClass(nc.getDoc(),nc.getName(),lift(nc.getInner()),nc.getP()),
        mimpl->new Expression.ClassB.MethodImplemented(mimpl.getDoc(),mimpl.getS(),lift(mimpl.getInner()),mimpl.getP()),
        mwt->{
         Doc idoc=mwt.getDoc();
         MethodSelector is=mwt.getMs();
         MethodType mt=mwt.getMt();
         return new Expression.ClassB.MethodWithType(idoc, is, mt, Optional.ofNullable(liftNullable(mwt.get_inner())),mwt.getP());
         }
        ));    }
    return new Expression.ClassB(doc1, h,Collections.emptyList(), supertypes, members,s.getP());
  }
  public Expression visit(WalkBy s) {
    return new Expression.WalkBy();
    }
//  private ast.ExpCore lift(Expression e){return e.accept(this);}
  private Expression lift(ast.ExpCore e){return e.accept(this);}
  private Expression liftNullable(ast.ExpCore _e){
    if(_e!=null){return _e.accept(this);}
    return null;
    }

  @Override
  public Expression visit(UpdateVar s) {
    return new Expression.BinOp(s.getP(),
              new Expression.X(s.getP(), s.getVar()),
              Ast.Op.ColonEqual,
              s.getDoc(),
              lift(s.getInner()));
    }
  }
