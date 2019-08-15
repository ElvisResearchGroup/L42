package is.L42.generated;
import lombok.Value;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Wither;
import java.util.List;
import java.util.stream.Collectors;

public class Half {
  public static interface E{Pos pos();}
  public static interface Leaf extends E{}
  public static interface Wrapper extends E{ E inner();}
  public static interface XP extends E{}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  PCastT implements Leaf, XP{Pos pos;P p; T t;
    public String toString() {return this.p+"<:"+this.t;}}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  Slash implements Leaf{Pos pos; List<ST> stz; public String toString() {return "%"+stz+"%";}}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  BinOp implements E{Pos pos; Op op;List<XP> es;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  MCall implements E{Pos pos; E inner; S s;List<E> es;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  Block implements E{Pos pos; List<D> ds; List<K>ks; E e;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  Loop implements Wrapper{Pos pos;E inner;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  Throw implements Wrapper{Pos pos; E inner;}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  OpUpdate implements Wrapper{Pos pos; X x; E inner;}
  //---
  @Value @Wither public static class 
  D{T t; X x; E e;}
  @Value @Wither public static class 
  K{Throw thr; T t; X x; E e;}    
  @Value @Wither public static class 
  T {Mdf _mdf; List<ST> stz;}
  }