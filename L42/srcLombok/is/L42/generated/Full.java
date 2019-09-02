package is.L42.generated;
import lombok.Value;
import lombok.EqualsAndHashCode;
import lombok.experimental.Wither;
import java.util.List;
import java.util.stream.Collectors;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;
import is.L42.common.Constants;

public class Full {
  public static interface E extends HasPos,HasWf,HasVisitable{Visitable<? extends E> visitable();}
  public static interface Leaf extends E{}
  public static interface Wrapper extends E{ E e();}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  CsP implements Leaf,Visitable<CsP>{@Override public Visitable<CsP>visitable(){return this;}@Override public CsP accept(CloneVisitor cv){return cv.visitCsP(this);}@Override public void accept(CollectorVisitor cv){cv.visitCsP(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    List<C> cs; P _p;}//when cs is empty, _p is not null
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  L implements Leaf,Half.E,Visitable<L>{@Override public Visitable<L>visitable(){return this;}@Override public L accept(CloneVisitor cv){return cv.visitL(this);}@Override public void accept(CollectorVisitor cv){cv.visitL(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    boolean isDots; String reuseUrl; boolean isInterface; List<T> ts; List<M>ms; List<Doc>docs;
    public static interface M extends HasWf,HasPos,HasVisitable{List<Doc> docs();LDom key();E _e();Visitable<? extends M> visitable();}
    @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
    F implements M,Visitable<F>{@Override public Visitable<F>visitable(){return this;}@Override public F accept(CloneVisitor cv){return cv.visitF(this);}@Override public void accept(CollectorVisitor cv){cv.visitF(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
      Pos pos; List<Doc>docs; boolean isVar; T t; S key;
      public E _e(){return null;}}
    @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
    MI implements M,Visitable<MI>{@Override public Visitable<MI>visitable(){return this;}@Override public MI accept(CloneVisitor cv){return cv.visitMI(this);}@Override public void accept(CollectorVisitor cv){cv.visitMI(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
      Pos pos; List<Doc>docs; Op _op;int n; S s; E e;
      public E _e(){return e;}
      public S key(){return is.L42.common.NameMangling.keyOf(_op,n,s);}
      }
    @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
    MWT implements M,Visitable<MWT>{@Override public Visitable<MWT>visitable(){return this;}@Override public MWT accept(CloneVisitor cv){return cv.visitMWT(this);}@Override public void accept(CollectorVisitor cv){cv.visitMWT(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
      Pos pos; List<Doc>docs; MH mh; String nativeUrl; E _e;
      public S key(){return mh.key();}}
    @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
    NC implements M,Visitable<NC>{@Override public Visitable<NC>visitable(){return this;}@Override public NC accept(CloneVisitor cv){return cv.visitNC(this);}@Override public void accept(CollectorVisitor cv){cv.visitNC(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
      Pos pos; List<Doc>docs; C key;  E e; public E _e(){return e;}}
    }
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  Slash implements Leaf,Visitable<Slash>{@Override public Visitable<Slash>visitable(){return this;}@Override public Slash accept(CloneVisitor cv){return cv.visitSlash(this);}@Override public void accept(CollectorVisitor cv){cv.visitSlash(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    }
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  SlashX implements Leaf,Visitable<SlashX>{@Override public Visitable<SlashX>visitable(){return this;}@Override public SlashX accept(CloneVisitor cv){return cv.visitSlashX(this);}@Override public void accept(CollectorVisitor cv){cv.visitSlashX(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    X x;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  EString implements E,Visitable<EString>{@Override public Visitable<EString>visitable(){return this;}@Override public EString accept(CloneVisitor cv){return cv.visitEString(this);}@Override public void accept(CollectorVisitor cv){cv.visitEString(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    List<E> es;List<String> strings;}//es(0) is the receiver
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  EPathSel implements Leaf,Visitable<EPathSel>{@Override public Visitable<EPathSel>visitable(){return this;}@Override public EPathSel accept(CloneVisitor cv){return cv.visitEPathSel(this);}@Override public void accept(CollectorVisitor cv){cv.visitEPathSel(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    PathSel pathSel;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  UOp implements Wrapper,Visitable<UOp>{@Override public Visitable<UOp>visitable(){return this;}@Override public UOp accept(CloneVisitor cv){return cv.visitUOp(this);}@Override public void accept(CollectorVisitor cv){cv.visitUOp(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    Op _op;String _num;E e;}//either _op or _num ==null
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  BinOp implements E,Visitable<BinOp>{@Override public Visitable<BinOp>visitable(){return this;}@Override public BinOp accept(CloneVisitor cv){return cv.visitBinOp(this);}@Override public void accept(CollectorVisitor cv){cv.visitBinOp(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    Op op;List<E> es;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  Cast implements Wrapper,Visitable<Cast>{@Override public Visitable<Cast>visitable(){return this;}@Override public Cast accept(CloneVisitor cv){return cv.visitCast(this);}@Override public void accept(CollectorVisitor cv){cv.visitCast(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    E e; T t;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  Call implements E,Visitable<Call>{@Override public Visitable<Call>visitable(){return this;}@Override public Call accept(CloneVisitor cv){return cv.visitCall(this);}@Override public void accept(CollectorVisitor cv){cv.visitCall(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    E e; S _s; boolean isSquare; List<Par> pars;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  Block implements E,Visitable<Block>{@Override public Visitable<Block>visitable(){return this;}@Override public Block accept(CloneVisitor cv){return cv.visitBlock(this);}@Override public void accept(CollectorVisitor cv){cv.visitBlock(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    boolean isCurly; List<D> ds; int dsAfter; List<K>ks; List<T> whoopsed; E _e;}  
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  Loop implements Wrapper,Visitable<Loop>{@Override public Visitable<Loop>visitable(){return this;}@Override public Loop accept(CloneVisitor cv){return cv.visitLoop(this);}@Override public void accept(CollectorVisitor cv){cv.visitLoop(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    E e;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  While implements E,Visitable<While>{@Override public Visitable<While>visitable(){return this;}@Override public While accept(CloneVisitor cv){return cv.visitWhile(this);}@Override public void accept(CollectorVisitor cv){cv.visitWhile(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    E condition; E body;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  For implements E,Visitable<For>{@Override public Visitable<For>visitable(){return this;}@Override public For accept(CloneVisitor cv){return cv.visitFor(this);}@Override public void accept(CollectorVisitor cv){cv.visitFor(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    List<D> ds; E body;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  Throw implements Wrapper,Visitable<Throw>{@Override public Visitable<Throw>visitable(){return this;}@Override public Throw accept(CloneVisitor cv){return cv.visitThrow(this);}@Override public void accept(CollectorVisitor cv){cv.visitThrow(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    ThrowKind thr; E e;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  OpUpdate implements Wrapper,Visitable<OpUpdate>{@Override public Visitable<OpUpdate>visitable(){return this;}@Override public OpUpdate accept(CloneVisitor cv){return cv.visitOpUpdate(this);}@Override public void accept(CollectorVisitor cv){cv.visitOpUpdate(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    X x; Op op; E e;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  If implements E,Visitable<If>{@Override public Visitable<If>visitable(){return this;}@Override public If accept(CloneVisitor cv){return cv.visitIf(this);}@Override public void accept(CollectorVisitor cv){cv.visitIf(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    E _condition; List<D> matches; E then; E _else;}
  //---
  @Value @Wither public static class
  D implements Visitable<D>{@Override public D accept(CloneVisitor cv){return cv.visitD(this);}@Override public void accept(CollectorVisitor cv){cv.visitD(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    VarTx _varTx; List<VarTx> varTxs; E _e;}
  @Value @Wither public static class
  VarTx implements Visitable<VarTx>{@Override public VarTx accept(CloneVisitor cv){return cv.visitVarTx(this);}@Override public void accept(CollectorVisitor cv){cv.visitVarTx(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    boolean isVar; T _t; Mdf _mdf; X _x;
    public static final VarTx emptyInstance=new VarTx(false,null,null,null);
    }//mdf can be present only if _t is absent
  @Value @Wither public static class
  K implements Visitable<K>{@Override public K accept(CloneVisitor cv){return cv.visitK(this);}@Override public void accept(CollectorVisitor cv){cv.visitK(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    ThrowKind _thr; T t; X _x; E e;}
  @Value @Wither public static class
  Par implements Visitable<Par>{@Override public Par accept(CloneVisitor cv){return cv.visitPar(this);}@Override public void accept(CollectorVisitor cv){cv.visitPar(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    E _that; List<X>xs; List<E>es;}
  @Value @Wither public static class
  T implements Visitable<T>{@Override public T accept(CloneVisitor cv){return cv.visitT(this);}@Override public void accept(CollectorVisitor cv){cv.visitT(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    Mdf _mdf; List<Doc> docs; List<C>cs; P _p;}
  @Value @Wither public static class
  Doc implements Visitable<Doc>{@Override public Doc accept(CloneVisitor cv){return cv.visitDoc(this);}@Override public void accept(CollectorVisitor cv){cv.visitDoc(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    PathSel _pathSel; List<String>texts; List<Doc> docs;}
  @Value @Wither public static class
  PathSel implements Visitable<PathSel>{@Override public PathSel accept(CloneVisitor cv){return cv.visitPathSel(this);}@Override public void accept(CollectorVisitor cv){cv.visitPathSel(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    List<C>cs; P _p; S _s; X _x;}
  @Value @Wither public static class
  MH implements Visitable<MH>{@Override public MH accept(CloneVisitor cv){return cv.visitMH(this);}@Override public void accept(CollectorVisitor cv){cv.visitMH(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    Mdf _mdf; List<Doc> docs; T t; Op _op; int n; S s; List<T> pars; List<T> exceptions;
    public S key(){return is.L42.common.NameMangling.keyOf(_op,n,s);}
    public List<T> parsWithThis(){return is.L42.tools.General.pushTopL(P.fullThis0.with_mdf(_mdf),pars);}
    }
 
  }