package coreVisitors;


import java.util.ArrayList;
import java.util.List;
import tools.Map;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.Loop;
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
  protected Type liftT(Type t){
    return t.match(
        nt->(Type)new NormType(nt.getMdf(),lift(nt.getPath()),nt.getPh()),
        ht->(Type)new HistoricType(lift(ht.getPath()),ht.getSelectors(),ht.isForcePlaceholder())
        );
    }
  protected ExpCore.Block.Catch liftK(ExpCore.Block.Catch k){
    return new ExpCore.Block.Catch(k.getKind(),k.getX(),Map.of(this::liftO,k.getOns()));
    }
  protected ExpCore.Block.On liftO(ExpCore.Block.On on){
    return new ExpCore.Block.On(liftT(on.getT()),lift(on.getInner()));
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
    return new Block.Dec(liftT(f.getT()),f.getX(),lift(f.getE()));
  }
  protected Doc liftDoc(Doc doc) {
    return doc.withAnnotations(Map.of(ann->{
      if(ann instanceof Path){return this.visit((Path)ann);}
      return ann;},doc.getAnnotations()));
  }
  protected Member liftM(Member m) {
    return m.match(
      nc->visit(nc),
      mi->visit(mi),
      mt->visit(mt)
      );
  }
  public ClassB.NestedClass visit(ClassB.NestedClass nc){return new ClassB.NestedClass(liftDoc(nc.getDoc()),nc.getName(),lift(nc.getInner()));}
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    return new ClassB.MethodImplemented(liftDoc(mi.getDoc()), mi.getS(), lift(mi.getInner()));
    }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    return new ClassB.MethodWithType(liftDoc(mt.getDoc()),mt.getMs(),liftMT(mt.getMt()), Map.of(this::lift,mt.getInner()));
    }
  
  protected MethodType liftMT(MethodType mt) {
    return new MethodType(liftDoc(mt.getDocExceptions()),mt.getMdf(),Map.of(this::liftT,mt.getTs()),Map.of(this::liftDoc,mt.getTDocs()),liftT(mt.getReturnType()),Map.of(this::lift,mt.getExceptions()));
  }
  public ExpCore visit(Path s) {return s;}
  public ExpCore visit(X s) { return s;}
  public ExpCore visit(_void s) {return s;}
  public ExpCore visit(WalkBy s) {return s;}
  public ExpCore visit(Using s) {
    return new Using(lift(s.getPath()),s.getName(),liftDoc(s.getDoc()),s.getXs(),Map.of(this::lift,s.getEs()),lift(s.getInner()));
  }
  public ExpCore visit(Signal s) {
    return new Signal(s.getKind(),lift(s.getInner()));
  }
  public ExpCore visit(MCall s) {
    return new MCall(s.getSource(),lift(s.getReceiver()),s.getName(),liftDoc(s.getDoc()),s.getXs(),Map.of(this::lift,s.getEs()));
  }
  public ExpCore visit(Block s) {
    return new Block(s.getSource(),liftDoc(s.getDoc()),liftDecs(s.getDecs()),lift(s.getInner()),Map.of(this::liftK,s.get_catch()));
  }
  protected List<Dec> liftDecs(List<Dec> s) {
    return Map.of(this::liftDec,s);
  }
  public ExpCore visit(ClassB s) {
    List<Path> sup = liftSup(s.getSupertypes());
    List<Member> ms = liftMembers(s.getMs());
    return new ClassB(liftDoc(s.getDoc1()),liftDoc(s.getDoc2()),s.isInterface(),sup,ms,s.getStage());
  }
  public List<Member> liftMembers(List<Member> s) {
    return Map.of(this::liftM,s);
  }
  protected List<Path> liftSup(List<Path> supertypes) {
    return Map.of(this::lift,supertypes);
  }
  @Override
  public ExpCore visit(Loop s) {
    return new Loop(lift(s.getInner()));
  }
}
