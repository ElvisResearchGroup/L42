package is.L42.generated;
import lombok.Value;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Wither;
import java.util.List;
import java.util.stream.Collectors;




@Value @Wither
public class Core {
  public static interface E{Pos pos();}
  public static interface Leaf extends E{}
  public static interface Wrapper extends E{ E inner();}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  EX implements Leaf,Full.E,Full.Leaf{Pos pos; X inner;
    public String toString() {return this.inner.toString();}}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  PCastT implements Leaf{Pos pos;P p; T t;
    public String toString() {return this.p+"<:"+this.t;}}
  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class 
  Evoid implements Leaf,Full.E,Full.Leaf{Pos pos;
    public String toString() {return "void";}}
  @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
  L implements Leaf,Full.E,Full.Leaf{Pos pos;
    boolean isInterface;List<T> ts; List<MWT>mwts;List<NC>ncs;List<Doc>docs;
    @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
    MWT{Pos pos;List<Doc>docs; MH mh; String nativeUrl;E _e;
      public S key(){return mh.s();}}
    @EqualsAndHashCode(exclude={"pos"})@ToString(exclude={"pos"})@Value @Wither public static class 
    NC{Pos pos;List<Doc>docs; C key;  L l;}
    }
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
  T {Mdf mdf;List<Doc> docs;P p;}
  @Value @Wither public static class 
  Doc {PathSel pathSel;List<String>texts;List<Doc> docs;}
  @Value @Wither public static class 
  PathSel {P p; S _s; X _x;}
  @Value @Wither public static class 
  MH {Mdf _mdf; List<Doc> docs; T t; S s; List<T> pars; List<T> exceptions;}
  }