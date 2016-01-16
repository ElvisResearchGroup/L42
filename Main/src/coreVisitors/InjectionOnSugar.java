
package coreVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import coreVisitors.Visitor;
import tools.Match;
import ast.Ast;
import ast.Ast.BlockContent;
import ast.Ast.Catch;
import ast.Ast.Doc;
import ast.Ast.Header;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.On;
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
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import ast.Expression.ClassB.Member;
import ast.Expression;
import auxiliaryGrammar.WellFormedness;

public class InjectionOnSugar implements Visitor<ast.Expression> {

  @Override public Expression visit(Path s) {
    return s;
  }

  @Override public Expression visit(X s) {
    return new Expression.X(s.getInner());
  }

  @Override public Expression visit(_void s) {
    return new Expression._void();
  }

  @Override public Expression visit(Using s) {
    List<String> xs=s.getS().getNames();
    List<ast.ExpCore> es1=s.getEs();
    List<ast.Expression> es=new ArrayList<ast.Expression>();
    for( ast.ExpCore e : es1){es.add(lift(e));}
    ast.Ast.Parameters ps=new ast.Ast.Parameters(Optional.<ast.Expression>empty(), xs, es);
    return new Expression.Using(s.getPath(),s.getS().getName(),s.getDoc(),ps,lift(s.getInner()));
  }

  @Override public Expression visit(Signal s) {
    return new Expression.Signal(s.getKind(),lift(s.getInner()));
  }
  @Override public Expression visit(Loop s) {
    return new Expression.Loop(lift(s.getInner()));
  }
  @Override public Expression visit(MCall s) {
    ast.Expression receiver=lift(s.getReceiver());
    String name=s.getS().getName();
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
      Type t=s.getDecs().get(i).getT();
      String x=s.getDecs().get(i).getX();
      Expression e=lift(s.getDecs().get(i).getE());
      decs.add(new VarDecXE(false,Optional.of(t),x,e));
    }
    Optional<Catch> _catch=injectionCatch(s.getOns());
    List<BlockContent> contents=new ArrayList<BlockContent>();
    if(!decs.isEmpty() || _catch.isPresent()){
      contents.add(new BlockContent(decs,_catch));
      }
    Expression.Position pos=s.getP();
    Expression.RoundBlock result=new Expression.RoundBlock(pos,docs,inner,contents);
    //assert WellFormedness.blockCheck(result);, no it can actually get wrong?
    return result;
  }

  private Optional<Catch> injectionCatch(List<ast.ExpCore.Block.On> list) {
    if(list.isEmpty()){return Optional.empty();}// Collections.emptyList();} 
    List<On> ons=new ArrayList<>();
    for( ast.ExpCore.Block.On on:list){
      List<Type> ts=new ArrayList<>();
      ts.add(on.getT());
      Expression inner = lift(on.getInner());
      ons.add(new On(ts,Optional.empty(),inner));
    }
    Catch result=new Catch(list.get(0).getKind(),list.get(0).getX(),ons,Optional.empty());
    return Optional.of(result);
  }

  @Override public Expression visit(ClassB s) {
    Doc doc1=s.getDoc1();
    Doc doc2=s.getDoc2();
    Header h=(s.isInterface())?new Ast.InterfaceHeader():new Ast.TraitHeader();
    List<Path>supertypes=s.getSupertypes();
    List<Member> members=new ArrayList<>();
    for(ast.ExpCore.ClassB.Member mi:s.getMs()){
      members.add(mi.match(
        nc->new Expression.ClassB.NestedClass(nc.getDoc(),nc.getName(),lift(nc.getInner()),nc.getP()),
        mimpl->new Expression.ClassB.MethodImplemented(mimpl.getDoc(),mimpl.getS(),lift(mimpl.getInner()),mimpl.getP()),
        mwt->{
         Doc idoc=mwt.getDoc();
         MethodSelector is=mwt.getMs();
         MethodType mt=mwt.getMt();
         return new Expression.ClassB.MethodWithType(idoc, is, mt, lift(mwt.getInner()),mwt.getP());
         }
        ));    }
    return new Expression.ClassB(doc1,doc2, h, supertypes, members,s.getStage().getStage());
  }
  public Expression visit(WalkBy s) {
    return new Expression.WalkBy();
    }
//  private ast.ExpCore lift(Expression e){return e.accept(this);}
  private Expression lift(ast.ExpCore e){return e.accept(this);}
  private Optional<Expression> lift(Optional<ast.ExpCore> e){
    if(e.isPresent()){return Optional.of(e.get().accept(this));}
    return Optional.empty();
    }
  }
