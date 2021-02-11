package is.L42.visitors;

import java.io.Serializable;

public interface Visitable<Kind> extends Serializable{
  Kind accept(CloneVisitor v);
  void accept(CollectorVisitor v);
  boolean wf();
  }