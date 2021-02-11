package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;
import java.util.List;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;
import is.L42.common.Constants;

public interface ST extends HasWf,HasVisitable{
  Visitable<? extends ST> visitable();
  @Value @Wither public static class
  STMeth implements ST,Visitable<ST>{@Override public Visitable<ST>visitable(){return this;}@Override public ST accept(CloneVisitor cv){return cv.visitSTMeth(this);}@Override public void accept(CollectorVisitor cv){cv.visitSTMeth(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    ST st; S s; int i;/*-1 for no i*/}  
  @Value @Wither public static class
  STOp implements ST,Visitable<ST>{@Override public Visitable<ST>visitable(){return this;}@Override public ST accept(CloneVisitor cv){return cv.visitSTOp(this);}@Override public void accept(CollectorVisitor cv){cv.visitSTOp(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
    Op op; List<List<ST>> stzs;}
}
