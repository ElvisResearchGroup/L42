package is.L42.generated;
import lombok.Value;
import lombok.EqualsAndHashCode;
import lombok.experimental.Wither;
import java.util.List;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;
import is.L42.common.Constants;
import is.L42.common.Parse;
import static is.L42.tools.General.*;
enum PrimitiveP implements P{Void,Library,Any;
  public String toString(){return Constants.toS.apply(this);}
  }
public interface P extends Visitable<P>{
  default public P accept(CloneVisitor cv){return cv.visitP(this);}
  default public void accept(CollectorVisitor cv){cv.visitP(this);}
  default public boolean wf(){return Constants.wf.test(this);}
  public static final P pAny=PrimitiveP.Any;
  public static final P pVoid=PrimitiveP.Void;
  public static final P pLibrary=PrimitiveP.Library;
  public static final Core.T coreAny = new Core.T(Mdf.Immutable, L(), P.pAny);
  public static final Core.T coreLibrary = new Core.T(Mdf.Immutable, L(), P.pLibrary);
  public static final Core.T coreVoid=new Core.T(Mdf.Immutable, L(),P.pVoid);
  public static final List<ST> stzCoreVoid=L(new Core.T(Mdf.Immutable, L(),P.pVoid));
  public static final P.NCs pThis0=P.of(0,L());
  public static final P.NCs pThis1=P.of(1,L());
  public static final Core.T coreThis0=new Core.T(Mdf.Immutable, L(),pThis0);
  public static final Core.T coreThis1=new Core.T(Mdf.Immutable, L(),pThis0);
  public static final Core.T coreClassAny = new Core.T(Mdf.Class, L(), P.pAny);
  public static final Full.T fullThis0=new Full.T(Mdf.Immutable, L(),L(),coreThis0.p());
  public static final Full.T fullClassAny = new Full.T(Mdf.Class, L(),L(), P.pAny);
  public static P.NCs of(int n,List<C>cs){return new NCs(n,cs);}
  public static P parse(String s){
    var csP= Parse.csP("--dummy--",s);
    assert !csP.hasErr();
    assert csP.res._p()!=null;
    return csP.res._p();
    }
  default boolean isNCs(){return false;}
  default NCs toNCs(){throw bug();}
  @EqualsAndHashCode(callSuper=false) @Value @Wither
  public static class NCs implements P{
    int n;List<C>cs;
    @Override public NCs toNCs(){return this;}
    @Override public boolean isNCs(){return true;}
    @Override public String toString(){return Constants.toS.apply(this);}
    }
  }