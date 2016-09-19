package ast;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;
import ast.Expression;
import ast.Ast.*;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import auxiliaryGrammar.Program;

public interface ExpCore {
  <T> T accept(coreVisitors.Visitor<T> v);

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") @Wither public static class MCall implements ExpCore,HasPos, WithInner<MCall>{
    ExpCore inner;
    MethodSelector s;
    Doc doc;
    List<ExpCore> es;
    Position p;
    public MCall withEsi(int i,ExpCore ei){
      List<ExpCore> es2=new ArrayList<>(es);
      es2.set(i,ei);
      return this.withEs(es2);
      }
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value public static class X implements ExpCore, Ast.Atom {
    String inner;
    public String toString() {
      return this.inner;
    }
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") @Wither public static class Block implements ExpCore,HasPos,WithInner<Block> {
    Doc doc;
    List<Dec> decs;
    ExpCore inner;
    List<On> ons;
    Position p;
    public ExpCore getE(){return inner;}
    public Block withE(ExpCore e){return this.withInner(e);}
    public Block withDeci(int i,Dec di){
      List<Dec> decs2=new ArrayList<>(decs);
      decs2.set(i,di);
      return this.withDecs(decs2);
      }
    public Block withDeci(int i,On oi){
    List<On> ons2=new ArrayList<>(ons);
    ons2.set(i,oi);
    return this.withOns(ons2);
    }  
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
    @Value @Wither public static class Dec implements WithInner<Dec>{
      Type t;
      String x;
      ExpCore inner;

      public ast.Ast.NormType getNT() {
        assert this.t instanceof ast.Ast.NormType : t;
        return (ast.Ast.NormType) this.t;
      }
    }
    public List<String> domDecs() {
      List<String> dom = new java.util.ArrayList<String>();
      for (Dec d : this.decs) {
        dom.add(d.x);
      }
      return dom;
    }

    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class On implements HasPos, WithInner<On>{
      SignalKind kind;
      String x;
      Type t;
      ExpCore inner;
      Position p;
      public ExpCore getE(){return inner;}
      public On withE(ExpCore e){return this.withInner(e);}

    }
  }

  @Value @Wither @EqualsAndHashCode(exclude = {"stage","p"}) public static class ClassB implements ExpCore, Ast.Atom,HasPos {
	  /*public ClassB(Doc doc1, Doc doc2, boolean isInterface, List<Path> supertypes, List<Member> ms) {
		  this(doc1,doc2,isInterface,supertypes,ms,new ast.Util.CachedStage());
		  }*/
	  public ClassB(Doc doc1, Doc doc2, boolean isInterface, List<Path> supertypes, List<Member> ms,Position p,ast.Util.CachedStage stage) {
      this.doc1 = doc1;
      this.doc2 = doc2;
      this.isInterface = isInterface;
      this.supertypes = supertypes;
      this.ms = ms;
      this.stage=stage;
      this.p=p;
      assert stage!=null;
      isConsistent();
    }//lombock fails me here :-(
    Doc doc1;
    Doc doc2;
    boolean isInterface;
    List<Ast.Path> supertypes;
    List<Member> ms;
    Position p;
    ast.Util.CachedStage stage;
    public String toString() {
      return sugarVisitors.ToFormattedText.of(this);
    }
    public boolean isConsistent() {
      int countWalkBy = 0;
      HashSet<String> keys = new HashSet<String>();
      for (Member m : this.ms) {
        if (m instanceof MethodWithType) {
          MethodWithType mwt = (MethodWithType) m;
          String key = mwt.getMs().toString();
          assert !keys.contains(key);
          keys.add(key);

          assert mwt.mt.getTDocs().size() == mwt.mt.getTs().size();
        }
        if (m instanceof NestedClass) {
          NestedClass nc = (NestedClass) m;
          String key = nc.getName();
          assert !keys.contains(key);
          keys.add(key);

          if (nc.inner instanceof WalkBy) {
            countWalkBy += 1;
          }
        }
        if (m instanceof MethodImplemented) {
          MethodImplemented mi = (MethodImplemented) m;
          String key = mi.getS().toString();
          assert !keys.contains(key);
          keys.add(key);
        }
      }
      assert countWalkBy <= 1 : this;
      return true;
    }
    public ClassB withMember(Member m) {
      isConsistent();
      List<Member> newMs = new java.util.ArrayList<Member>(this.getMs());
      int index=Aux.getIndex(newMs,m);
      if(index==-1){newMs.add(m);}
      else {newMs.set(index, m);}
      ClassB result = this.withMs(newMs);
      result.isConsistent();
      return result;
      }
    public ClassB onClassNavigateToPathAndDo(List<String>cs,Function<ClassB,ClassB>op){
      return Aux.onClassNavigateToPathAndDo(this, cs, op);
      }
    public ClassB onNestedNavigateToPathAndDo(List<String>cs,Function<ClassB.NestedClass,Optional<ClassB.NestedClass>>op){
      return Aux.onNestedNavigateToPathAndDo(this, cs, op);
      }
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
    public interface Member extends HasPos, WithInner<Member> {
      //public Member withBody(ExpCore e);
      <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt);
    }
    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p")public static class NestedClass implements Member {
      @NonNull Doc doc;
      @NonNull String name;
      @NonNull ExpCore inner;
      Position p;
      public ExpCore getE(){return inner;}
      public NestedClass withE(ExpCore e) {return this.withInner(e);}
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return nc.apply(this);
      }
    }
    @Value @Wither @EqualsAndHashCode(exclude = {"p"/*,"mt"*/}) @ToString(exclude = {"p"/*,"mt"*/})public static class MethodImplemented implements Member {
      @NonNull Doc doc;
      @NonNull MethodSelector s;
      @NonNull ExpCore inner;
      //ast.Util.CachedMt mt=new ast.Util.CachedMt();
      Position p;
      public ExpCore getE(){return inner;}
      public MethodImplemented withE(ExpCore e) {return this.withInner(e);}
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return mi.apply(this);
      }
    }
    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p")public static class MethodWithType implements Member {
      @NonNull Doc doc;
      @NonNull MethodSelector ms;
      @NonNull MethodType mt;
      @NonNull Optional<ExpCore> _inner;
      Position p;
      public ExpCore getInner(){return _inner.get();}//and boom if there is not
      public MethodWithType withInner(ExpCore e) {return this.with_inner(Optional.of(e));}

      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return mt.apply(this);
      }
    }
  }

  @Value public static class _void implements ExpCore, Ast.Atom {
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }
  @Value public static class WalkBy implements ExpCore {
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @Wither public static class Using implements ExpCore, WithInner<Using> {
    Path path;
    MethodSelector s;
    Doc doc;
    List<ExpCore> es;
    ExpCore inner;
    public Using withEsi(int i,ExpCore ei){
    List<ExpCore> es2=new ArrayList<>(es);
    es2.set(i,ei);
    return this.withEs(es2);
    }
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @Wither public static class Signal implements ExpCore, WithInner<Signal>{
    SignalKind kind;
    ExpCore inner;
    
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @Wither public static class Loop implements ExpCore, WithInner<Loop> {
    ExpCore inner;
    
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }
  interface WithInner<T>{
    ExpCore getInner(); T withInner(ExpCore e);
  }
  static class Aux{
    public static ClassB onNestedNavigateToPathAndDo(ClassB cb,List<String>cs,Function<NestedClass,Optional<NestedClass>>op){
      assert !cs.isEmpty();
      assert cb!=null;
      List<Member> newMs=new ArrayList<>(cb.getMs());
      String nName=cs.get(0);
      int index=getIndex(newMs, nName);
      NestedClass nc=(NestedClass)newMs.get(index);
      if(cs.size()>1){
        nc=nc.withInner(onNestedNavigateToPathAndDo((ClassB)nc.getInner(),cs.subList(1,cs.size()),op));
        newMs.set(index, nc);
        return cb.withMs(newMs);
        }
      assert cs.size()==1;
      Optional<NestedClass> optNc = op.apply(nc);
      if(optNc.isPresent()){
        newMs.set(index,optNc.get());
        }
      else{newMs.remove(index);}
      return cb.withMs(newMs);
      }

    public static ClassB onClassNavigateToPathAndDo(ClassB cb,List<String>cs,Function<ClassB,ClassB>op){
      if(cs.isEmpty()){return op.apply(cb);}
      List<Member> newMs=new ArrayList<>(cb.getMs());
      String nName=cs.get(0);
      int index=getIndex(newMs, nName);
      NestedClass nc=(NestedClass)newMs.get(index);
      if(cs.size()>1){
        nc=nc.withInner(onClassNavigateToPathAndDo((ClassB)nc.getInner(),cs.subList(1,cs.size()),op));
        newMs.set(index, nc);
        return cb.withMs(newMs);
        }
      assert cs.size()==1;
      ClassB newCb = op.apply((ClassB)nc.getInner());
      newMs.set(index, nc.withInner(newCb));
      return cb.withMs(newMs);
      }
    
  public static int getIndex(List<ExpCore.ClassB.Member> map, ast.Ast.MethodSelector elem){
    int i=-1;for(ExpCore.ClassB.Member m: map){i++;
    if(m.match(nc->false,mi->mi.getS().equals(elem) ,mt->mt.getMs().equals(elem))){return i;}
    }
    return -1;  
  }
  public static int getIndex(List<ExpCore.ClassB.Member> map, String elem){
    int i=-1;for(ExpCore.ClassB.Member m: map){i++;
      if(m.match(nc->nc.getName().equals(elem), mi->false, mt->false)){return i;}
      }
    return -1;
    }
  public static int getIndex(List<ExpCore.ClassB.Member> map, ExpCore.ClassB.Member elem){
    return elem.match(nc->getIndex(map,nc.getName()), mi->getIndex(map,mi.getS()),mt->getIndex(map,mt.getMs()));
    }
  }
}
