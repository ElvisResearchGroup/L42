package coreVisitors;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tools.Map;
import ast.ExpCore;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.*;
import ast.Ast.*;
import ast.Ast.Type.*;

public class CloneVisitor implements Visitor<ExpCore>{
  protected <T extends ExpCore>T lift(T e){
    @SuppressWarnings("unchecked")
    T result= (T)e.accept(this);
    //assert result.getClass().equals(e.getClass());
    //no, I would like to assert on T, that is a supertype of getClass()
    return result;
    }
  protected Path liftP(Path p){return p;}
  protected Optional<Type> liftTOpt(Optional<Type> t){
    if(!t.isPresent()){return t;}
    return Optional.of(liftT(t.get()));
  }
  protected Type liftT(Type t){
    return new Type(t.getMdf(),liftP(t.getPath()),liftDoc(t.getDoc()));
    }
  protected MethodSelector liftMs(MethodSelector ms){return ms;}

  protected ExpCore.Block.On liftO(ExpCore.Block.On on){
    return new ExpCore.Block.On(on.getKind(),on.getX(),liftT(on.getT()),lift(on.getInner()),on.getP());
    }
  /*protected Header liftH(Header h) {
    return h.match(ch->new ConcreteHeader(
        ch.getMdf(),ch.getName(), Map.of(this::liftF,ch.getFs())
        ), th->th, ih->ih);
  }*/
  /*protected FieldDec liftF(FieldDec f) {
    return new FieldDec(f.isVar(),liftT(f.getT()),f.getName(),f.getDocs());
  }*/
  protected Block.Dec liftDec(Block.Dec f) {
    return new Block.Dec(f.isVar(),liftTOpt(f.getT()),f.getX(),lift(f.getInner()));
  }
  protected Doc liftDoc(Doc doc) {
    return doc.withAnnotations(Map.of(ann->{
      if(ann instanceof Path){return liftP((Path)ann);}
      return ann;},doc.getAnnotations()));
  }
  protected Member liftM(Member m) {
    return m.match(
      nc->visit(nc),
      mi->visit(mi),
      mt->visit(mt)
      );
  }
  public ClassB.NestedClass visit(ClassB.NestedClass nc){return new ClassB.NestedClass(liftDoc(nc.getDoc()),nc.getName(),lift(nc.getInner()),nc.getP());}
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    return new ClassB.MethodImplemented(liftDoc(mi.getDoc()), liftMsInMetDec(mi.getS()), lift(mi.getInner()),mi.getP());
    }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    return new ClassB.MethodWithType(liftDoc(mt.getDoc()),liftMsInMetDec(this.liftMs(mt.getMs())),liftMT(mt.getMt()), Map.of(this::lift,mt.get_inner()),mt.getP());
    }

  protected MethodType liftMT(MethodType mt) {
    return new MethodType(mt.isRefine(),mt.getMdf(),Map.of(this::liftT,mt.getTs()),liftT(mt.getReturnType()),Map.of(this::liftT,mt.getExceptions()));
  }
  @Override public ExpCore visit(EPath s) {return s.withInner(liftP(s.getInner()));}
  public ExpCore visit(X s) { return s;}
  public ExpCore visit(_void s) {return s;}
  public ExpCore visit(WalkBy s) {return s;}
  public ExpCore visit(Using s) {
    return new Using(liftP(s.getPath()),liftMs(s.getS()),liftDoc(s.getDoc()),Map.of(this::lift,s.getEs()),lift(s.getInner()));
  }
  public ExpCore visit(Signal s) {
    return new Signal(s.getKind(),lift(s.getInner()),
            s.getTypeOut()==null?null:liftT(s.getTypeOut()),
            s.getTypeIn()==null?null:liftT(s.getTypeIn())
            );
  }
  public ExpCore visit(MCall s) {
    return new MCall(lift(s.getInner()),liftMs(s.getS()),liftDoc(s.getDoc()),Map.of(this::lift,s.getEs()),s.getP());
  }
  public ExpCore visit(Block s) {
    return new Block(liftDoc(s.getDoc()),liftDecs(s.getDecs()),lift(s.getInner()),Map.of(this::liftO,s.getOns()),s.getP());
  }
  protected List<Dec> liftDecs(List<Dec> s) {
    return Map.of(this::liftDec,s);
  }
  public ExpCore visit(ClassB s) {
    List<Type> sup = liftSup(s.getSupertypes());
    List<Member> ms = liftMembers(s.getMs());
    return new ClassB(
      liftDoc(s.getDoc1()),
      s.isInterface(),
      sup,
      ms,
      s.getP(),
      s.getPhase(),
      s.getUniqueId());
  }
  public List<Member> liftMembers(List<Member> s) {
    return Map.of(this::liftM,s);
  }
  protected List<Type> liftSup(List<Type> supertypes) {
    return Map.of(this::liftT,supertypes);
  }
  @Override
  public ExpCore visit(Loop s) {
    return new Loop(lift(s.getInner()));
  }
  protected MethodSelector liftMsInMetDec(MethodSelector ms) {
    return ms;
  }
@Override
public ExpCore visit(UpdateVar s) {
  return new UpdateVar(lift(s.getInner()),s.getVar(),liftDoc(s.getDoc()),s.getP());
  }
}
