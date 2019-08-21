package is.L42.common;
import java.util.function.Function;
import java.util.function.Predicate;

import is.L42.visitors.ToSVisitor;
import is.L42.visitors.Visitable;

public class Constants{
  static{
    System.out.println("Initializing constants");
    try{assert false; throw new Error("assertions disabled");}
    catch(AssertionError e){}
    }
  public static Function<Visitable<?>,String> toS=ToSVisitor::of;
  public static Predicate<Visitable<?>> wf;
  }