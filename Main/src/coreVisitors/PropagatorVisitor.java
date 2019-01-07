package coreVisitors;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import tools.Assertions;
import tools.Map;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Type;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.UpdateVar;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;

public class PropagatorVisitor implements Visitor<Void>{
  protected void lift(ExpCore e){
    e.accept(this);
    }
  protected void liftP(Path p){}
  protected void liftTOpt(Optional<Type> t){
    if(t.isPresent()){liftT(t.get());}
    }
  protected void liftT(Type t){
    liftDoc(t.getDoc());
    liftP(t.getPath());
    }
  protected void liftMs(MethodSelector ms){}//Do nothing on purpose, can be overridden

  protected void liftO(ExpCore.Block.On on){
    liftT(on.getT());
    lift(on.getInner());
    }
  protected void liftDec(Block.Dec f) {
    liftTOpt(f.getT());
    lift(f.getInner());
    }
  protected void liftDoc(Doc doc) {
    for(Object a:doc.getAnnotations()){
      if(a instanceof ExpCore){lift((ExpCore)a);}}
    }
  protected void liftM(Member m) {
    //I can not do the follow for void return type: m.match(this::visit,this::visit, this::visit);
    if( m instanceof NestedClass){visit((NestedClass)m);}
    else if( m instanceof MethodWithType){visit((MethodWithType)m);}
    else visit((MethodImplemented)m);
    }
  public void visit(ClassB.NestedClass nc){
    liftDoc(nc.getDoc());
    lift(nc.getInner());
    }
  public void visit(ClassB.MethodImplemented mi){
    liftDoc(mi.getDoc());
    liftMsInMetDec(mi.getS());
    lift(mi.getInner());
    }
  public void visit(ClassB.MethodWithType mt){
    liftDoc(mt.getDoc());
    liftMsInMetDec(mt.getMs());
    liftMT(mt.getMt());
    if(mt.get_inner()!=null){lift(mt.getInner());}  
    }
  protected void liftMT(MethodType mt) {
    for(Type t:mt.getTs()){liftT(t);}
    liftT(mt.getReturnType());
    for(Type p:mt.getExceptions()){liftT(p);}
    }
  public Void visit(Using s) {
    liftP(s.getPath());
    liftMs(s.getS());
    liftDoc(s.getDoc());
    for(ExpCore e:s.getEs()){lift(e);}
    lift(s.getInner());
    return null;
    }
  public Void visit(Signal s) {
    lift(s.getInner());
    return null;
    }
  public Void visit(MCall s) {
    lift(s.getInner());
    liftMs(s.getS());
    liftDoc(s.getDoc());
    for(ExpCore e:s.getEs()){lift(e);}
    return null;
    }
  public Void visit(ExpCore.OperationDispatch s) {
    liftMs(s.getS());
    liftDoc(s.getDoc());
    for(ExpCore e:s.getEs()){lift(e);}
    return null;
    }
  public Void visit(Block s) {
    liftDoc(s.getDoc());
    liftDecs(s.getDecs());
    lift(s.getInner());
    for(On o:s.getOns()){liftO(o);}
    return null;
    }
  protected void liftDecs(List<Dec> s) {
    for(Dec e:s){liftDec(e);}
    }
  public Void visit(ClassB s) {
    liftSup(s.getSupertypes());
    liftMembers(s.getMs());
    liftDoc(s.getDoc1());
    return null;
    }
  public void liftMembers(List<Member> s) {
    for(Member m:s){liftM(m);}
    }
  protected void liftSup(List<Type> s) {
    for(Type e:s){liftT(e);}
    }
  public Void visit(Loop s) {
    lift(s.getInner());
    return null;
    }
  protected void liftMsInMetDec(MethodSelector ms) {liftMs(ms);}//delegate on more common lift, to play with overriding better.

  public Void visit(WalkBy s) {throw Assertions.codeNotReachable();}

  public Void visit(ExpCore.EPath s) {liftP(s.getInner());return null;}
  
  public Void visit(X s) {return null;}//Do nothing on purpose, can be overridden
  public Void visit(_void s) {return null;}//Do nothing on purpose, can be overridden
@Override
public Void visit(UpdateVar s) {
  this.liftDoc(s.getDoc());
  this.lift(s.getInner());
  return null;
  }
  }
