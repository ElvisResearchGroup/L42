package is.L42.common;
import java.util.function.Function;
import java.util.function.Predicate;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.visitors.ToSVisitor;
import is.L42.visitors.Visitable;
import is.L42.visitors.WellFormedness;

public class Constants{
  static{
    System.out.println("Initializing constants");
    try{assert false; throw new Error("assertions disabled");}
    catch(AssertionError e){}
    }
  public static Function<Visitable<?>,String> toS=ToSVisitor::of;
  public static Predicate<Visitable<?>> wf=WellFormedness::of;
  public static Function<PTails,Full.L> readFolder=ReadFolder::of;
  public static Function<String,Core.L> readURL=ReadURL::of;

  }