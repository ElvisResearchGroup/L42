package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import tools.Map;
import ast.Ast.VarDecXE;
import ast.ErrorMessage;
import ast.Expression;
import ast.Ast.BlockContent;
import ast.Ast.Catch;
import ast.Ast.VarDec;
import ast.Expression.*;
import ast.Expression.ClassB.Member;


public class CheckVarUsedAreInScope extends CloneVisitor{
  public static boolean of(Expression e){
    of(e,Collections.emptyList());
    return true;
  }
  public static void of(Expression e,List<String>names){
    CheckVarUsedAreInScope cdv=new CheckVarUsedAreInScope();
    cdv.xs.addAll(names);
    e.accept(cdv);
  }
  Set<String> xs=new HashSet<String>();
  public Expression visit(Expression.X s){
    if(this.xs.contains(s.getInner())){return super.visit(s);}
    throw new ErrorMessage.NotWellFormed(s, null,"Variable used not in scope");
    }
  protected ast.Ast.BlockContent liftBC(ast.Ast.BlockContent c) {
    Set<String> aux = this.xs;
    Set<String> aux2=new HashSet<String>(aux);
    for(VarDec vd:c.getDecs()){vd.match(
      xe->{aux2.add(xe.getX());return null;},
      e->{return null;},
      ce->{return null;});}
    this.xs=aux2;
    List<VarDec> liftVarDecs = liftVarDecs(c.getDecs());
    this.xs=aux;
    Optional<Catch> liftK = Map.of(this::liftK,c.get_catch());
    this.xs=aux2;//add again for later contents
    return new ast.Ast.BlockContent(liftVarDecs,liftK);
  }
  protected ast.Ast.Catch liftK(ast.Ast.Catch k){
    Set<String> aux = this.xs;
    Set<String> aux2=new HashSet<String>(aux);
    aux2.add(k.getX());
    this.xs=aux2;
    try{return super.liftK(k);}
    finally{this.xs=aux;}
    }
  
  public Expression visit(CurlyBlock s) {
    Set<String> aux = this.xs;
    this.xs=new HashSet<String>(aux);
    try{return super.visit(s);}
    finally{this.xs=aux;}
    }
  
  public Expression visit(RoundBlock s) {
    Set<String> aux = this.xs;
    this.xs=new HashSet<String>(aux);
    List<BlockContent> content = Map.of(this::liftBC,s.getContents());
    //here should have all the accumulated vardecs
    Expression inner = lift(s.getInner());
    this.xs=aux;
    Expression result= new RoundBlock(s.getP(),s.getDoc(), inner,content);
    return result;
    }
  public Expression visit(With s) {
    Set<String> aux = this.xs;
    this.xs=new HashSet<String>(aux);
    List<VarDecXE> is = Map.of(this::liftVarDecXE, s.getIs());
    for(VarDecXE vd:is){this.xs.add(vd.getX());}
    List<VarDecXE> es = Map.of(this::liftVarDecXE, s.getDecs());
    for(VarDecXE vd:es){this.xs.add(vd.getX());}
    try{return new With(s.getP(),s.getXs(),is,es,Map.of(this::liftO,s.getOns()),Map.of(this::lift, s.getDefaultE()));}
    finally{this.xs=aux;}
  }
  public Expression visit(ClassB s) {
    for(Member m:s.getMs()){
      m.match(
        nc->{CheckVarUsedAreInScope.of(nc.getInner(),Collections.emptyList());return null;},
        mi->{
          List<String> names = new ArrayList<>(mi.getS().getNames());
          names.add("this");
          CheckVarUsedAreInScope.of(mi.getInner(),names);
          return null;},
        mt->{
          if(mt.getInner().isPresent()){
            List<String> names =new ArrayList<>( mt.getMs().getNames());
            names.add("this");
            CheckVarUsedAreInScope.of(mt.getInner().get(),names);
            };
          return null;
          }
        );
      }
    return s;
    }  
  }
