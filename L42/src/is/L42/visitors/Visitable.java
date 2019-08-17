package is.L42.visitors;

public interface Visitable<Kind>{
  Kind accept(CloneVisitor v);
  void accept(CollectorVisitor v);
  boolean wf();
  }