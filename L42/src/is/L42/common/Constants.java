package is.L42.common;
import java.util.function.Function;
import java.util.function.Predicate;
import is.L42.visitors.Visitable;

public class Constants{
  public static Function<Visitable<?>,String> toS;
  public static Predicate<Visitable<?>> wf;
  }