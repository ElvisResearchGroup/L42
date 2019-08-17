package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;
import java.util.List;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;
import is.L42.common.Constants;

@Value @Wither public class
S implements LDom,Visitable<S>{@Override public S accept(CloneVisitor cv){return cv.visitS(this);}@Override public void accept(CollectorVisitor cv){cv.visitS(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
  String m; List<X> xs; int uniqueNum;}//-1 for no unique num
