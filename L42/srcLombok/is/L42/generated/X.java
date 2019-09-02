package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;
import is.L42.common.Constants;

@Value @Wither public class
X implements Visitable<X>{@Override public X accept(CloneVisitor cv){return cv.visitX(this);}@Override public void accept(CollectorVisitor cv){cv.visitX(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
  String inner;
  public static final X thisX=new X("this");
  }
