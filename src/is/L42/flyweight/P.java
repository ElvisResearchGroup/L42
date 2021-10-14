// Generated by delombok at Sun Jun 13 15:12:41 NZST 2021
package is.L42.flyweight;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;

import java.io.ObjectStreamException;
import java.util.List;
import java.util.Map;

import com.google.common.cache.CacheBuilder;

import is.L42.common.Constants;
import is.L42.common.Parse;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Mdf;
import is.L42.generated.ST;
import is.L42.perftests.PerfCounters;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;

enum PrimitiveP implements P {
  Void, Library, Any;
  public String toString() { return Constants.toS.apply(this); }
  }

public sealed interface P extends Visitable<P> permits PrimitiveP, P.NCs {
  default P accept(CloneVisitor cv) { return cv.visitP(this); }
  default void accept(CollectorVisitor cv) { cv.visitP(this); }
  default boolean wf() { return Constants.wf.test(this); }
  P pAny = PrimitiveP.Any;
  P pVoid = PrimitiveP.Void;
  P pLibrary = PrimitiveP.Library;
  Core.T coreAny = new Core.T(Mdf.Immutable, L(), P.pAny);
  Core.T coreLibrary = new Core.T(Mdf.Immutable, L(), P.pLibrary);
  Core.T coreVoid = new Core.T(Mdf.Immutable, L(), P.pVoid);
  List<ST> stzCoreVoid = L(new Core.T(Mdf.Immutable, L(), P.pVoid));
  P.NCs pThis0 = P.of(0, L());
  P.NCs pThis1 = P.of(1, L());
  Core.T coreThis0 = new Core.T(Mdf.Immutable, L(), pThis0);
  Core.T coreThis1 = new Core.T(Mdf.Immutable, L(), pThis0);
  Core.T coreClassAny = new Core.T(Mdf.Class, L(), P.pAny);
  Full.T fullThis0 = new Full.T(Mdf.Immutable, L(), L(), coreThis0.p());
  Full.T fullClassAny = new Full.T(Mdf.Class, L(), L(), P.pAny);
  Full.T fullVoid = new Full.T(Mdf.Immutable, L(), L(), P.pVoid);
  static P.NCs of(int n, List<C> cs) {
    assert n >= 0;
    return NCs.of(n, cs);
    }
  static P parse(String s) {
    var csP = Parse.csP(Constants.dummy, s);
    assert !csP.hasErr();
    assert csP.res._p() != null;
    return csP.res._p();
    }
  default boolean isNCs() { return false; }
  default NCs toNCs() { throw bug(); }
  default boolean hasUniqueNum() {
    if(!isNCs()) { return false; }
    var p = toNCs();
    return p.cs.stream().anyMatch(c -> c.hasUniqueNum());
    }
  final class NCs implements P {
    private final int n;
    private final List<C> cs;
    private static final long serialVersionUID = -324234L;
    @Override public NCs toNCs() { return this; }
    @Override public boolean isNCs() { return true; }
    @Override public String toString() { return Constants.toS.apply(this); }
    public boolean hasUniqueNum() {
      for(C ci : cs) {
        if(ci.hasUniqueNum()) { return true; }
        }
      return false;
      }
    private static record NCsI(int n, List<C> cs) {}
    private static final Map<NCsI, NCs> created = CacheBuilder.newBuilder().weakValues().<NCsI, NCs>build().asMap();
    private static void perfCountNCsOf(NCsI ci) {
      PerfCounters.inc("invoke.P.NCs.init.total");
      if(!created.containsKey(ci)) {
        PerfCounters.inc("invoke.P.NCs.init.total.unique");
        }
      }
    public static NCs of(int n, List<C> cs) {
      NCsI ci = new NCsI(n, cs);
      if(PerfCounters.isEnabled()) { perfCountNCsOf(ci); }
      return created.computeIfAbsent(ci, ci2->new NCs(ci2.n, ci2.cs));
      }
    Object readResolve() throws ObjectStreamException {
      return of(this.n, this.cs);
      }
    private NCs(final int n, final List<C> cs) {
      this.n = n;
      //This is necessary for some reason. Serialization exceptions?
      this.cs = List.copyOf(cs);
    }
    public int n() { return this.n; }
    public List<C> cs() { return this.cs; }
    public P.NCs withN(final int n) { return this.n == n ? this : of(n, this.cs); }
    public P.NCs withCs(final List<C> cs) { return this.cs == cs ? this : of(this.n, cs); }
  }
}