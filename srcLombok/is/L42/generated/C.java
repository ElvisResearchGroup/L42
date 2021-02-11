package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;
import is.L42.common.Constants;

@Value @Wither public class 
C implements LDom,Visitable<C>{@Override public C accept(CloneVisitor cv){return cv.visitC(this);}@Override public void accept(CollectorVisitor cv){cv.visitC(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
  String inner; int uniqueNum;}//-1 for no unique num