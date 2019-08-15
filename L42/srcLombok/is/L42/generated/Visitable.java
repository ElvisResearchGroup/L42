package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;

interface Visitable<Kind>{
  Kind accept(CloneVisitor v);
  void accept(CollectorVisitor v);
  }