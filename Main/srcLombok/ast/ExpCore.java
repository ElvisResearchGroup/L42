package ast;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;
import ast.Expression;
import ast.Ast.*;

public interface ExpCore {
  <T> T accept(coreVisitors.Visitor<T> v);

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") @Wither public static class MCall implements ExpCore,HasPos {
    ExpCore receiver;
    MethodSelector s;
    Doc doc;
    List<ExpCore> es;
    Position p;
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

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") @Wither public static class Block implements ExpCore,HasPos {
    Doc doc;
    List<Dec> decs;
    ExpCore inner;
    Optional<Catch> _catch;
    Position p;
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
    @Value @Wither public static class Dec {
      Type t;
      String x;
      ExpCore e;
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
    public Block(Doc doc, ExpCore inner,Position p) {
      this(doc, Collections.emptyList(), inner, Optional.<Catch> empty(),p);
    }
    public Block(Doc doc, Dec dec, ExpCore inner,Position p) {
      this(doc, Collections.singletonList(dec), inner, Optional.<Catch> empty(),p);
    }
    public Block(Doc doc, List<Dec> decs, ExpCore inner,Position p) {
      this(doc, decs, inner, Optional.<Catch> empty(),p);
    }
    public Block(Doc doc, List<Dec> decs, ExpCore inner, Optional<Catch> _catch,Position p) {
      this.doc = doc;
      this.decs = decs;
      this.inner = inner;
      this._catch = _catch;
      this.p=p;
    }
    @Value @Wither public static class Catch {
      SignalKind kind;
      String x;
      List<On> ons;
    }
    @Value @Wither public static class On {
      Type t;
      ExpCore inner;
    }
  }

  @Value @Wither @EqualsAndHashCode(exclude = "stage") public static class ClassB implements ExpCore, Ast.Atom {
    public ClassB(Doc doc1, Doc doc2, boolean isInterface, List<Path> supertypes, List<Member> ms) {
      this.doc1 = doc1;
      this.doc2 = doc2;
      this.isInterface = isInterface;
      this.supertypes = supertypes;
      this.ms = ms;
      isConsistent();
    }//lombock fails me here :-(
    Doc doc1;
    Doc doc2;
    boolean isInterface;
    List<Ast.Path> supertypes;
    List<Member> ms;
    ast.Util.CachedStage stage=new ast.Util.CachedStage();
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
      auxiliaryGrammar.Program.replaceIfInDom(newMs, m);
      ClassB result = this.withMs(newMs);
      result.isConsistent();
      return result;
    }
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
    public interface Member extends HasPos {
      public Member withBody(ExpCore e);
      <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt);
    }
    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p")public static class NestedClass implements Member {
      @NonNull Doc doc;
      @NonNull String name;
      @NonNull ExpCore inner;
      Position p;
      public Member withBody(ExpCore e) {
        return this.withInner(e);
      }
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return nc.apply(this);
      }
    }
    @Value @Wither @EqualsAndHashCode(exclude = {"p","mt"}) @ToString(exclude = {"p","mt"})public static class MethodImplemented implements Member {
      @NonNull Doc doc;
      @NonNull MethodSelector s;
      @NonNull ExpCore inner;
      ast.Util.CachedMt mt=new ast.Util.CachedMt();
      Position p;
      public Member withBody(ExpCore e) {
        return this.withInner(e);
      }
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return mi.apply(this);
      }
    }
    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p")public static class MethodWithType implements Member {
      @NonNull Doc doc;
      @NonNull MethodSelector ms;
      @NonNull MethodType mt;
      @NonNull Optional<ExpCore> inner;
      Position p;
      public Member withBody(ExpCore e) {
        return this.withInner(Optional.of(e));
      }
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

  @Value @Wither public static class Using implements ExpCore {
    Path path;
    MethodSelector s;
    Doc doc;
    List<ExpCore> es;
    ExpCore inner;
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @Wither public static class Signal implements ExpCore {
    SignalKind kind;
    ExpCore inner;
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @Wither public static class Loop implements ExpCore {
    ExpCore inner;
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

}
