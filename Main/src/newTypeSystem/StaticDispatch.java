package newTypeSystem;

import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.ClassB;
import ast.ExpCore.EPath;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.OperationDispatch;
import ast.ExpCore.Signal;
import ast.ExpCore.UpdateVar;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import auxiliaryGrammar.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ast.Ast.Type;
import coreVisitors.CloneVisitor;
import coreVisitors.Visitor;
import programReduction.Program;
import tools.Assertions;

/*

 guessType(p,G,e)//TODO: change in code about ds
  guessType(p,G,L)=imm Library
  guessType(p,G,void)=guessType(p,G,x:=e)=imm Void
  guessType(p,G,P)=class P //here will be wrong over interfaces
  chain.m(x1:e1..xn:en)=p(P)(m(x1..xn)).T//guaranteed to be a normalized method
    where guessType(p,G,chain)=mdf P
  guessType(p,G,(var?0 T0 x0=e0 ..var?n Tn xn=en e) )=  guessType(p,G[x1:T1,..,xn:Tn],e)
  guessType(p,G,throw _) and guessType(p,G,loop _) undefined
  guessType(p,G,use P check m(x1:e1..xn:en) e) undefined
------
 */
public class StaticDispatch implements Visitor<ExpCore>{
@SuppressWarnings("unchecked")
public static <T extends ExpCore> T of(Program p,G g,T e,boolean forceError){
    StaticDispatch sd=new StaticDispatch(p, g, forceError);
    ExpCore res=e.accept(sd);
    assert res!=null || !forceError;
    return (T)res;
    }
  Program p;
  G g;
  boolean errors=false;
  boolean forceVoid;
  private StaticDispatch(Program p,G g,boolean forceError){this.p=p;this.g=g;this.forceVoid=forceError;}
  
  //at the end, this.g will include some of ds.
  List<ExpCore.Block.Dec> liftDecs(List<ExpCore.Block.Dec>ds){
    this.g=this.g.add(p,ds);
    while(true){
      ds = liftDecsAux(ds); 
      if(!this.errors){return ds;}
      G old=this.g;
      this.g=this.g.add(p,ds);
      if (this.g.dom().size()!=old.dom().size()){continue;}
      assert this.g.dom().equals(old.dom());
      if(forceVoid){
        return ds.stream()
          .map(d->d.get_t()==null?d.with_t(Type.immVoid):d)
          .collect(Collectors.toList());
        }
      return ds;
      }
    }
  ExpCore _liftAllowError(ExpCore e){
    boolean old=this.forceVoid;
    this.forceVoid=false;
    try{return e.accept(this);}
    finally{this.forceVoid=old;}
    }
  //will set this.errors as secondary return value.
  List<ExpCore.Block.Dec> liftDecsAux(List<Dec>ds){
    boolean err=false;
    List<Dec>res=new ArrayList<>();
    for(Dec d:ds){
      ExpCore _e=_liftAllowError(d.getInner());
      if(_e==null){err=true;res.add(d);continue;}
      d=d.withInner(_e);
      if(d.get_t()!=null){res.add(d);continue;}
      Type _t=GuessTypeCore._of(p,g,_e,false);
      if(_t==null){err=true;res.add(d);continue;}
      d=d.with_t(Functions.capsuleToMut(_t));
      res.add(d);
      }
    this.errors=err;
    return res;
    }
  public ExpCore visit(MCall s) {
    ExpCore inner=s.getInner().accept(this);
    if(inner==null){assert !forceVoid;return s;}
    return s.withInner(inner);
    }
  public ExpCore visit(Block s) {
    List<Dec> ds1 = liftDecs(s.getDecs());
    assert ds1!=null;
    assert !forceVoid || ds1.stream().allMatch(d->d.get_t()!=null);
    G old=g;
    g=g.add(p, ds1);
    try{return s.withDecs(ds1).withE(s.getE().accept(this));}
    finally{g=old;}
  }
  public ExpCore visit(OperationDispatch s) {
    throw Assertions.codeNotReachable();
  }

  public ExpCore visit(EPath s) {return s;}
  public ExpCore visit(X s) {return s;}
  public ExpCore visit(_void s) {return s;}
  public ExpCore visit(WalkBy s) {return s;}
  public ExpCore visit(Using s) {return s;}
  public ExpCore visit(Signal s) {return s;}
  public ExpCore visit(ClassB s) {return s;}
  public ExpCore visit(Loop s) {return s;}
  public ExpCore visit(UpdateVar s) {return s;}
}
