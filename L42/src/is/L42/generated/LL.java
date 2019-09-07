package is.L42.generated;
import java.util.function.Function;
import java.util.List;
import is.L42.visitors.Visitable;

public interface LL extends Full.Leaf, HasVisitable, HasPos{
  @Override Visitable<? extends LL> visitable();
  default boolean isFullL(){return this instanceof Full.L;}
  LL withCs(List<C>cs,Function<Full.L.NC,Full.L.NC>fullF,Function<Core.L.NC,Core.L.NC>coreF);
  List<LDom> dom();
  LL c(C c);
  LL cs(List<C> cs);
  }
