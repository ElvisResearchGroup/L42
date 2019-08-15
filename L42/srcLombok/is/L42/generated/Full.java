package is.L42.generated;
import lombok.Value;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Wither;
import java.util.List;
import java.util.stream.Collectors;

public class Full {
  public static interface E{Pos pos();}
  public static interface Leaf extends E{}
  public static interface Wrapper extends E{ E inner();}
  //@EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  //EX implements Leaf{Pos pos; X inner;
  //  public String toString() {return this.inner.toString();}}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  CsP implements Leaf{Pos pos;List<C> inner;
    public String toString() {return this.inner.stream()
      .map(c->c.inner()).collect(Collectors.joining("."));}}
  //@EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  //Evoid implements Leaf{Pos pos;
  //  public String toString() {return "void";}}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  L implements Leaf, Half.E{Pos pos;
    boolean isDots;String reuseUrl;boolean isInterface;List<T> ts; List<M>ms;List<Doc>docs;
    public static interface M{Pos pos();List<Doc> docs();LDom key();E _e();}
    @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
    F implements M{Pos pos;List<Doc>docs; S key;
     public E _e(){return null;}}
    @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
    MI implements M{Pos pos;List<Doc>docs; S key; E e; public E _e(){return e;}}
    @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
    MWT implements M{Pos pos;List<Doc>docs; MH mh; String nativeUrl;E _e;
      public S key(){return mh.s();}}
    @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
    NC implements M{Pos pos;List<Doc>docs; C key;  E e; public E _e(){return e;}}
    }
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  Slash implements Leaf{Pos pos;public String toString() {return "\\";}}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  SlashX implements Leaf{Pos pos; X inner;public String toString() {return inner.toString();}}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  EString implements Wrapper{Pos pos; E inner; String string;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  EPathSel implements Leaf{Pos pos; PathSel inner;public String toString() {return inner.toString();}}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  UOp implements Wrapper{Pos pos; Op _op;String _num;E inner;}//either _op or _num ==null
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  BinOp implements E{Pos pos; Op op;List<E> es;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  Cast implements Wrapper{Pos pos; E inner; T type;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  Call implements E{Pos pos; E inner; S s; boolean isSquare; List<Par> pars;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  Block implements E{Pos pos;
    boolean isCurly; List<D> ds; int dsAfter; List<K>ks; List<T> whoopsed; E _e;
    }
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  Loop implements Wrapper{Pos pos;E inner;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  While implements E{Pos pos; E condition; E inner;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  For implements E{Pos pos; List<D> dxes; E inner;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  Throw implements Wrapper{Pos pos; Throw thr; E inner;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  OpUpdate implements Wrapper{Pos pos; X x; Op op; E inner;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  If implements E{Pos pos; E _condition; List<D> matches; E inner; E _else;}
  //---
  @Value @Wither public static class 
  D{VarTx _varTx; List<VarTx> varTxs;E _e;}
  @Value @Wither public static class 
  VarTx{boolean isVar; T _t; Mdf _mdf; X _x;}//mdf can be present only if _t is absent
  @Value @Wither public static class 
  K{Throw _throw; T t; X _x; E e;}    
  @Value @Wither public static class 
  Par {E _that; List<X>xs;List<E>es;}
  @Value @Wither public static class 
  T {Mdf _mdf;List<Doc> docs;CsP inner;}
  @Value @Wither public static class 
  Doc {PathSel pathSel;List<String>texts;List<Doc> docs;}
  @Value @Wither public static class 
  PathSel {List<C> csP; S s; X _x;}
  @Value @Wither public static class 
  MH {Mdf _mdf; List<Doc> docs; T t; Op _op; S s; List<T> pars; List<T> exceptions;}
  }