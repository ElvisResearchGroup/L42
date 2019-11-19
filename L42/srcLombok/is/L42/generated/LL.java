package is.L42.generated;
import java.util.function.Function;
import lombok.Value;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Wither;

import java.util.List;
import is.L42.visitors.Visitable;

public interface LL extends Full.Leaf,Half.E,HasVisitable,HasPos{
  @Override Visitable<? extends LL> visitable();
  default boolean isFullL(){return this instanceof Full.L;}
  LL withCs(List<C>cs,Function<Full.L.NC,Full.L.NC>fullF,Function<Core.L.NC,Core.L.NC>coreF);
  List<C> domNC();//error if FULL.L with reuse/...
  boolean inDom(C c);//error if FULL.L with reuse/... and c not in the locally present ncs
  LL c(C c);//error if FULL.L with reuse/...
  LL cs(List<C> cs);//error if FULL.L with reuse/...
  //List<S> domS();//error if FULL.L //should be moved in CORE?
  //MH s(S s);//error if FULL.L //should be moved in CORE?
  @SuppressWarnings("serial")@Value @Wither 
  @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true)
  public static class NotInDom extends RuntimeException{
    LL receiver; LDom parameter;
    }
  /*@SuppressWarnings("serial")@Value @Wither 
  @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true)
  public static class ReuseOrDots extends RuntimeException{
    LL receiver; LDom parameter;
    }*/

  }
