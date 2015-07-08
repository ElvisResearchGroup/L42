package ast;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;

public interface Expression extends Ast {
  public <T> T accept(sugarVisitors.Visitor<T> v);

  @Value public static class Signal implements Expression {
    SignalKind kind;
    Expression inner;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value public static class Loop implements Expression {
    Expression inner;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") @Wither public static class If implements Expression, HasPos {
    Position p;
    Expression cond;
    Expression then;
    Optional<Expression> _else;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class While implements Expression, HasPos {
    Position p;
    Expression cond;
    Expression then;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") @Wither public static class With implements Expression, HasPos {
    Position p;
    List<String> xs;
    List<VarDecXE> is;
    List<VarDecXE> decs;
    List<On> ons;
    Optional<Expression> defaultE;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value public static class X implements Expression, Ast.Atom {
    String inner;
    public String toString() {
      return this.inner;
    }
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") /*@ToString(exclude = "p")*/ @Wither public static class BinOp implements Expression, HasPos {
    Position p;
    Expression left;
    Ast.Op op;
    Expression right;
    public String toString() {
      return "(" + left + op.inner + right + ")";
    }
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value public static class DocE implements Expression {
    Expression inner;
    Doc doc;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") /*@ToString(exclude = "p")*/ public static class UnOp implements Expression, HasPos {
    Position p;
    Ast.Op op;
    Expression inner;
    public String toString() {
      return "(" + op.inner + inner + ")";
    }
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = {"p", "source"}) @ToString(exclude = {"p", "source"}) public static class MCall implements Expression, HasPos {
    Expression source;
    Position p;
    Expression receiver;
    String name;
    Doc doc;
    Parameters ps;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class FCall implements Expression, HasPos {
    @NonNull Position p;
    Expression receiver;
    Doc doc;
    Parameters ps;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class SquareCall implements Expression, HasPos {
    Position p;
    Expression receiver;
    Doc doc;
    List<Doc> docs;
    List<Parameters> pss;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class SquareWithCall implements Expression, HasPos {
    Position p;
    Expression receiver;
    With with;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") @Wither public static class RoundBlock implements Expression, HasPos {
    Position p;
    Doc doc;
    Expression inner;
    List<BlockContent> contents;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class CurlyBlock implements Expression, HasPos {
    Position p;
    Doc doc;
    List<BlockContent> contents;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value public static class Using implements Expression {
    Path path;
    String name;
    Doc docs;
    Parameters ps;
    Expression inner;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value public static class ClassReuse implements Expression, Ast.Atom {
    ClassB inner;
    String url;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  //TODO: for decent error messages, eventually we have to admit duplicated members in Expression, so that the well formedess function can have an input
  @Value @Wither public static class ClassB implements Expression, Ast.Atom {
    public ClassB(Doc doc1, Doc doc2, Header h, List<Path> supertypes, List<Member> ms, Stage stage) {
      this.doc1 = doc1;
      this.doc2 = doc2;
      this.h = h;
      this.supertypes = supertypes;
      this.ms = ms;
      this.stage = stage;
      isConsistent();
    }

    Doc doc1;
    Doc doc2;
    Header h;
    List<Path> supertypes;
    List<Member> ms;
    Stage stage;
    public boolean isConsistent() {
      int countWalkBy = 0;
      for (Member m : this.ms) {
        if (m instanceof MethodWithType) {
          MethodWithType mwt = (MethodWithType) m;
          assert mwt.mt.getTDocs().size() == mwt.mt.getTs().size();
        }
        if (m instanceof NestedClass) {
          NestedClass nc = (NestedClass) m;
          if (nc.inner instanceof WalkBy) {
            countWalkBy += 1;
          }
        }
      }
      assert countWalkBy <= 1 : this;
      return true;
    }
    public interface Member {
      <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt);
    }
    @Value @Wither public static class NestedClass implements Member {
      Doc doc;
      String name;
      Expression inner;
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return nc.apply(this);
      }
    }
    @Value @Wither public static class MethodImplemented implements Member {
      Doc doc;
      MethodSelector s;
      Expression inner;
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return mi.apply(this);
      }
    }
    @Value @Wither public static class MethodWithType implements Member {
      Doc doc;
      MethodSelector ms;
      MethodType mt;
      Optional<Expression> inner;
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return mt.apply(this);
      }
    }
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }
  @Value public static class DotDotDot implements Expression {
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }
  @Value public static class WalkBy implements Expression {
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value public static class _void implements Expression, Ast.Atom {
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class Literal implements Expression, HasPos {
    Position p;
    Expression receiver;
    String inner;
    boolean isNumber;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  public static interface HasPos {
    Position getP();
  }

}