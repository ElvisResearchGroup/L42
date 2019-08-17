package is.L42.generated;
import lombok.Value;
import lombok.EqualsAndHashCode;
import lombok.experimental.Wither;
import java.util.List;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;
import is.L42.common.Constants;
import static is.L42.tools.General.*;

public class P{
  public static final P pAny=new P();
  public static final P pVoid=new P();
  public static final P pLibrary=new P();
  private P(){}
  public static P of(int n,List<C>cs){return new NCs(n,cs);}
  public NCs toNCs(){throw bug();}
  @EqualsAndHashCode(callSuper=false) @Value @Wither
  public static class NCs extends P{
    int n;List<C>cs;
    @Override public NCs toNCs(){return this;}    
    }
  }