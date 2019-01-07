package ast;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import ast.Ast.Atom;
import ast.Ast.HasPos;
import ast.Ast.Position;
import ast.Ast.SignalKind;
import ast.Ast.Type;
import ast.Ast.VarDec;
import lombok.EqualsAndHashCode;
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

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") @Wither public static class If implements Expression, HasPos<If> {
    Position p;
    Expression cond;
    Expression then;
    Optional<Expression> _else;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Wither @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class While implements Expression, HasPos<While> {
    Position p;
    Expression cond;
    Expression then;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") @Wither public static class With implements Expression, HasPos<With> {
    Position p;
    List<String> xs;
    List<VarDecXE> is;
    List<VarDecXE> decs;
    List<On> ons;
    Optional<Expression> defaultE;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
    @Value
    @Wither
    public static class On {
      List<Type> ts;
      Expression inner;
    }

  }

  @Wither @Value @EqualsAndHashCode(exclude = "p")
  public static class X implements Expression, Ast.Atom, HasPos<X>{
    Position p;
    String inner;
    public String toString() {
      return this.inner;
    }
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }
  @Value public static class ContextId implements Expression, Ast.Atom {
    String inner;//contains the "\"
    public String toString() {
      return this.inner;
    }
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }


  @Wither @Value @EqualsAndHashCode(exclude = "p") /*@ToString(exclude = "p")*/ public static class BinOp implements Expression, HasPos<BinOp> {
    Position p;
    Expression left;
    Ast.Op op;
    Ast.Doc doc;
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

  @Wither @Value @EqualsAndHashCode(exclude = "p") /*@ToString(exclude = "p")*/ public static class UnOp implements Expression, HasPos<UnOp> {
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

  @Value @Wither @EqualsAndHashCode(exclude = {"p"}) @ToString(exclude = {"p"}) public static class MCall implements Expression, HasReceiver {
    Expression receiver;
    String name;
    Doc doc;
    Parameters ps;
    Position p;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }
  @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class OperationDispatch  implements Expression {
    String name;
    Doc doc;
    Parameters ps;
    Position p;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }
  @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class FCall implements Expression, HasReceiver {
    @NonNull Position p;
    Expression receiver;
    Doc doc;
    Parameters ps;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class SquareCall implements Expression,HasReceiver {
    Position p;
    Expression receiver;
    Doc doc;
    List<Doc> docs;
    List<Parameters> pss;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class SquareWithCall implements Expression, HasReceiver {
    Position p;
    Expression receiver;
    With with;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }
  @Value public static class UseSquare implements Expression{
    Expression inner;//is either SquareCall with void receiver or With
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  public static interface Catch extends HasPos<Catch>{
    <T> T match(Function<Catch1, T> k1,Function<CatchMany, T> kM,Function<CatchProp, T> kP);
    String getX();
    Expression getInner();
  }
  @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class Catch1 implements Catch{
    Position p;
    SignalKind kind;
    Type t;
    String x;
    Expression inner;
    public <T> T match(Function<Catch1, T> k1,Function<CatchMany, T> kM,Function<CatchProp, T> kP){return k1.apply(this);}
  }
  @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class CatchMany implements Catch{
    Position p;
    SignalKind kind;
    List<Type> ts;
    Expression inner;
    public String getX(){return "";}
    public <T> T match(Function<Catch1, T> k1,Function<CatchMany, T> kM,Function<CatchProp, T> kP){return kM.apply(this);}
  }
  @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class CatchProp implements Catch{
    Position p;
    SignalKind kind;
    List<Type> ts;
    Expression inner;
    public String getX(){return "";}
    public <T> T match(Function<Catch1, T> k1,Function<CatchMany, T> kM,Function<CatchProp, T> kP){return kP.apply(this);}
  }
  @Value
  public class BlockContent {
    List<VarDec> decs;
    List<Catch> _catch;
  }

  @Wither @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class RoundBlock implements Expression, HasPos<RoundBlock> {
    Position p;
    Doc doc;
    Expression inner;
    List<BlockContent> contents;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Wither @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class CurlyBlock implements Expression, HasPos<CurlyBlock> {
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

  @Value @Wither public static class ClassReuse implements Expression, Ast.Atom {
    ClassB inner;
    String url;
    ExpCore.ClassB urlFetched;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  //TODO: for decent error messages, eventually we have to admit duplicated members in Expression, so that the well formedess function can have an input
  @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class ClassB implements Expression, Ast.Atom, HasPos<ClassB>{
    public ClassB(Doc doc1, Header h, List<Ast.FieldDec> fields, List<Type> supertypes, List<Member> ms,Position p) {
      this.doc1 = doc1;
      this.h = h;
      this.fields=fields;
      this.supertypes = supertypes;
      this.ms = ms;
      this.p=p;
      isConsistent();
    }

    Doc doc1;
    Header h;
    List<Ast.FieldDec> fields;
    List<Type> supertypes;
    List<Member> ms;
    Position p;
    public boolean isConsistent() {
      HashSet<String> keys = new HashSet<String>();
      int countWalkBy = 0;
      for (Member m : this.ms) {
        if (m instanceof MethodWithType) {
          MethodWithType mwt = (MethodWithType) m;
          String key=mwt.getMs().toString();
          //For better error messages we would like to finish parsing
          //assert !keys.contains(key);
          keys.add(key);
        }
        if (m instanceof NestedClass) {
          NestedClass nc = (NestedClass) m;
          String key=nc.getName().toString();
          //For better error messages we would like to finish parsing
          //assert !keys.contains(key);
          keys.add(key);
          if (nc.inner instanceof WalkBy) {
            countWalkBy += 1;
          }
        }
      }
      assert countWalkBy <= 1 : this;
      return true;
    }
    //public String toString() {
    //  return sugarVisitors.ToFormattedText.of(this);
    //}

    public interface Member extends HasPos<Member>{
      <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt);
    }
    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p")public static class NestedClass implements Member {
      Doc doc;
      Ast.C name;
      Expression inner;
      Ast.Position p;
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return nc.apply(this);
      }
    }
    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p")public static class MethodImplemented implements Member {
      Doc doc;
      MethodSelector s;
      Expression inner;
      Ast.Position p;
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return mi.apply(this);
      }
    }
    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p")public static class MethodWithType implements Member {
      Doc doc;
      MethodSelector ms;
      MethodType mt;
      Optional<Expression> inner;
      Ast.Position p;
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

  @Value @Wither @EqualsAndHashCode(exclude = "p")
  public static class EPath implements Expression,HasPos<EPath>,Atom{
    Position p;
    Ast.Path inner;
    public String toString(){return this.getInner().toString();}
    public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
      }
    }
  
  @Value public static class _void implements Expression, Ast.Atom {
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
    public static final _void instance=new _void();
  }

  @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class Literal implements Expression, HasReceiver {
    Position p;
    Expression receiver;
    String inner;
    boolean isNumber;
    @Override public <T> T accept(sugarVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }
}