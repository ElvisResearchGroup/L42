package is.L42.generated;
import java.util.function.Function;

import ErrorMessage.PosImprove;

import java.util.List;
import is.L42.visitors.Visitable;

public interface LL extends Full.Leaf, HasVisitable, HasPos{
  @Override Visitable<? extends LL> visitable();
  default boolean isFullL(){return this instanceof Full.L;}
  LL withCs(List<C>cs,Function<Full.L.NC,Full.L.NC>fullF,Function<Core.L.NC,Core.L.NC>coreF);
  List<C> domNC(Program p);
  List<S> domS(Program p);
  List<? extends LL> HERE!!!
  LL c(Program p,C c);
  MH s(Program p,S s);
  LL cs(List<C> cs);
  @SuppressWarnings("serial")@Value @Wither 
  @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true)
  public static class NotInDom extends RuntimeException{
    LL receiver; LDom parameter;
    }
  @SuppressWarnings("serial")@Value @Wither 
  @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true)
  public static class ReuseOrDots extends RuntimeException{
    LL receiver; LDom parameter;
    }

  }
