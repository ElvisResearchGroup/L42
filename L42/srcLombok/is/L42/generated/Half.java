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

public class Half {
  public static interface E extends HasPos,HasWf,HasVisitable{Visitable<? extends E> visitable();}
  public static interface Leaf extends E{}
  public static interface Wrapper extends E{ E e();}
  public static interface XP extends E{Visitable<? extends XP> visitable();}
  
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  PCastT implements Leaf,XP,Visitable<PCastT>{@Override public Visitable<PCastT>visitable(){return this;}@Override public PCastT accept(CloneVisitor cv){return cv.visitPCastT(this);}@Override public void accept(CollectorVisitor cv){cv.visitPCastT(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    P p; List<ST> stz;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  SlashCastT implements Leaf,XP,Visitable<SlashCastT>{@Override public Visitable<SlashCastT>visitable(){return this;}@Override public SlashCastT accept(CloneVisitor cv){return cv.visitSlashCastT(this);}@Override public void accept(CollectorVisitor cv){cv.visitSlashCastT(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    List<ST> stz; List<ST> stz1;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  BinOp implements E,Visitable<BinOp>{@Override public Visitable<BinOp>visitable(){return this;}@Override public BinOp accept(CloneVisitor cv){return cv.visitBinOp(this);}@Override public void accept(CollectorVisitor cv){cv.visitBinOp(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    Op op;List<XP> es;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  MCall implements E,Visitable<MCall>{@Override public Visitable<MCall>visitable(){return this;}@Override public MCall accept(CloneVisitor cv){return cv.visitMCall(this);}@Override public void accept(CollectorVisitor cv){cv.visitMCall(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    XP xP; S s;List<E> es;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  Block implements E,Visitable<Block>{@Override public Visitable<Block>visitable(){return this;}@Override public Block accept(CloneVisitor cv){return cv.visitBlock(this);}@Override public void accept(CollectorVisitor cv){cv.visitBlock(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    List<D> ds; List<K>ks; E e;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  Loop implements Wrapper,Visitable<Loop>{@Override public Visitable<Loop>visitable(){return this;}@Override public Loop accept(CloneVisitor cv){return cv.visitLoop(this);}@Override public void accept(CollectorVisitor cv){cv.visitLoop(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    E e;}  
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  Throw implements Wrapper,Visitable<Throw>{@Override public Visitable<Throw>visitable(){return this;}@Override public Throw accept(CloneVisitor cv){return cv.visitThrow(this);}@Override public void accept(CollectorVisitor cv){cv.visitThrow(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    ThrowKind thr; E e;}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  OpUpdate implements Wrapper,Visitable<OpUpdate>{@Override public Visitable<OpUpdate>visitable(){return this;}@Override public OpUpdate accept(CloneVisitor cv){return cv.visitOpUpdate(this);}@Override public void accept(CollectorVisitor cv){cv.visitOpUpdate(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;
    X x; E e;}
  //---
  @Value @Wither public static class
  D implements Visitable<D>{@Override public D accept(CloneVisitor cv){return cv.visitD(this);}@Override public void accept(CollectorVisitor cv){cv.visitD(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    Mdf _mdf; List<ST> stz; X x; E e;}
  @Value @Wither public static class
  K implements Visitable<K>{@Override public K accept(CloneVisitor cv){return cv.visitK(this);}@Override public void accept(CollectorVisitor cv){cv.visitK(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    ThrowKind thr; List<ST> stz; X x; E e;}
  }