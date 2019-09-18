// Generated by delombok at Wed Sep 18 22:39:11 PETT 2019
package is.L42.generated;

import java.util.List;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;
import is.L42.common.Constants;
import is.L42.common.Parse;
import static is.L42.tools.General.*;

public class P implements Visitable<P> {
  @Override
  public P accept(CloneVisitor cv) {
    return cv.visitP(this);
  }

  @Override
  public void accept(CollectorVisitor cv) {
    cv.visitP(this);
  }

  @Override
  public String toString() {
    return Constants.toS.apply(this);
  }

  @Override
  public boolean wf() {
    return Constants.wf.test(this);
  }

  public static final P pAny = new P();
  public static final P pVoid = new P();
  public static final P pLibrary = new P();
  public static final Core.T coreAny = new Core.T(Mdf.Immutable, L(), P.pAny);
  public static final Core.T coreLibrary = new Core.T(Mdf.Immutable, L(), P.pLibrary);
  public static final Core.T coreVoid = new Core.T(Mdf.Immutable, L(), P.pVoid);
  public static final Core.T coreThis0 = new Core.T(Mdf.Immutable, L(), P.of(0, L()));
  public static final Core.T coreThis1 = new Core.T(Mdf.Immutable, L(), P.of(1, L()));
  public static final Full.T fullThis0 = new Full.T(Mdf.Immutable, L(), L(), coreThis0.p());

  private P() {
  }

  public static P.NCs of(int n, List<C> cs) {
    return new NCs(n, cs);
  }

  public static P parse(String s) {
    var csP = Parse.csP("--dummy--", s);
    assert !csP.hasErr();
    assert csP.res._p() != null;
    return csP.res._p();
  }

  public boolean isNCs() {
    return false;
  }

  public NCs toNCs() {
    throw bug();
  }


  public static final class NCs extends P {
    private final int n;
    private final List<C> cs;

    @Override
    public NCs toNCs() {
      return this;
    }

    @Override
    public boolean isNCs() {
      return true;
    }

    @Override
    public String toString() {
      return Constants.toS.apply(this);
    }

    @java.lang.SuppressWarnings("all")
    public NCs(final int n, final List<C> cs) {
      this.n = n;
      this.cs = cs;
    }

    @java.lang.SuppressWarnings("all")
    public int n() {
      return this.n;
    }

    @java.lang.SuppressWarnings("all")
    public List<C> cs() {
      return this.cs;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof P.NCs)) return false;
      final P.NCs other = (P.NCs) o;
      if (!other.canEqual((java.lang.Object) this)) return false;
      if (this.n() != other.n()) return false;
      final java.lang.Object this$cs = this.cs();
      final java.lang.Object other$cs = other.cs();
      if (this$cs == null ? other$cs != null : !this$cs.equals(other$cs)) return false;
      return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
      return other instanceof P.NCs;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      result = result * PRIME + this.n();
      final java.lang.Object $cs = this.cs();
      result = result * PRIME + ($cs == null ? 43 : $cs.hashCode());
      return result;
    }

    @java.lang.SuppressWarnings("all")
    public NCs withN(final int n) {
      return this.n == n ? this : new NCs(n, this.cs);
    }

    @java.lang.SuppressWarnings("all")
    public NCs withCs(final List<C> cs) {
      return this.cs == cs ? this : new NCs(this.n, cs);
    }
  }
}
