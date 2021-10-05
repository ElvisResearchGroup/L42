package is.L42.flyweight;

import static is.L42.tools.General.L;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import is.L42.generated.Core.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import is.L42.common.Constants;
import is.L42.flyweight.P.NCs;
import is.L42.generated.*;
import is.L42.generated.Full;
import is.L42.generated.LDom.HasKey;
import is.L42.perftests.PerfCounters;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;

public final class CoreL implements LL, Leaf, Half.Leaf, Visitable<CoreL> {
  @Override public Visitable<CoreL> visitable(){ return this; }

  @Override public CoreL accept(CloneVisitor cv){ return cv.visitL(this); }

  @Override public void accept(CollectorVisitor cv){ cv.visitL(this); }

  @Override public String toString(){ return Constants.toS.apply(this); }

  @Override public boolean wf(){ return Constants.wf.test(this); }

  private final List<Pos> poss;
  public Pos pos(){ return poss.get(0); }
  private final boolean isInterface;
  private final List<T> ts;
  private final List<MWT> mwts;
  private final List<NC> ncs;
  private final Info info;
  private final List<Doc> docs;
  
  @Override public CoreL withCs(List<C> cs, Function<Full.L.NC, Full.L.NC> fullF, Function<NC, NC> coreF){
    assert !cs.isEmpty();
    assert domNC().contains(cs.get(0));
    return this.withNcs(L(ncs,nc -> {
      if (!nc.key().equals(cs.get(0))){ return nc; }
      if (cs.size() == 1) { return coreF.apply(nc); }
      return nc.withL(nc.l().withCs(popL(cs), fullF, coreF));
    }));
  }
  //Strangely, having this lambda live inside the method was causing millions of allocs 
  final Function<C, Boolean> inDomFun;//TODO: we could instead having a LinkedHashSet of ncs
  HashMap<C, Boolean> inDomCache = new HashMap<>();
  @Override public boolean inDom(C c) {
    if(PerfCounters.isEnabled()){ PerfCounters.inc("invoke.L.inDom(C).total"); }
    return inDomCache.computeIfAbsent(c, inDomFun);
    }
  public CoreL _cs(List<C> cs){
    if (cs.isEmpty()){ return this; }
    C c = cs.get(0);
    var res = LDom._elem(ncs, c);
    if (res == null){ return null; }
    return res.l()._cs(cs.subList(1, cs.size()));
    }
  public boolean inDom(List<C> cs){ return _cs(cs) != null; }
  List<C> cacheDomNC = null;
  @Override public List<C> domNC(){ return this.cacheDomNC; }
  @Override public CoreL c(C c){
    NC res = LDom._elem(ncs, c);
    if (res == null){ throw new LL.NotInDom(this, c); }
    return res.l();
    }
  @Override public CoreL cs(List<C> cs){
    if (cs.isEmpty()){ return this; }
    if (cs.size() == 1){ return this.c(cs.get(0)); }
    return this.c(cs.get(0)).cs(cs.subList(1, cs.size()));
    }
  public static CoreL parse(String s){
    var r = is.L42.common.Parse.e(Constants.dummy, s);
    assert !r.hasErr() : r;
    assert r.res != null;
    return (CoreL) r.res;
    }
  public void visitInnerL(CoreL.InnerLAction a){ a.start(this); }
  public void visitInnerLNoPrivate(CoreL.InnerLActionNoPrivate a){ a.start(this); }
  public static interface InnerLAction {
    void innerL(CoreL li, List<C> cs);
    default void start(CoreL l) { step(l, L()); }
    default boolean filterOut(NC nc) { return false; }
    default void step(CoreL li, List<C> cs) {
      innerL(li, cs);
      for (var ncj : li.ncs()) {
        if (filterOut(ncj)) { continue; }
        var newCs = pushL(cs, ncj.key());
        step(ncj.l(), newCs);
        }
      }
    }
  public static interface InnerLActionNoPrivate extends CoreL.InnerLAction {
    default boolean filterOut(NC nc){ return nc.key().hasUniqueNum(); }
    }
  
  public CoreL(List<Pos> poss, boolean isInterface, List<T> ts, List<MWT> mwts, List<NC> ncs, Info info, List<Doc> docs){
    this.poss = poss;
    this.isInterface = isInterface;
    this.ts = ts;
    this.mwts = mwts;
    this.ncs = ncs;
    this.info = info;
    this.docs = docs;
    @SuppressWarnings("unchecked")
    var fun=(Function<C, Boolean>&Serializable)c2->this.ncs.stream().anyMatch(m -> c2 == m.key());
    this.inDomFun = fun;
    this.cacheDomNC = L(ncs.stream().map(m -> m.key()));
    }  
  public List<Pos> poss(){ return this.poss; }
  public boolean isInterface(){ return this.isInterface; }
  public List<T> ts(){ return this.ts; }
  public List<MWT> mwts(){ return this.mwts; }
  public List<NC> ncs(){ return this.ncs; }
  public Info info(){ return this.info; }
  public List<Doc> docs(){ return this.docs; }

  @Override public boolean equals( java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof CoreL)) return false;
    final CoreL other = (CoreL) o;
    if (this.isInterface() != other.isInterface()) return false;
    final java.lang.Object this$ts = this.ts();
    final java.lang.Object other$ts = other.ts();
    if (this$ts == null ? other$ts != null : !this$ts.equals(other$ts)) return false;
    final java.lang.Object this$mwts = this.mwts();
    final java.lang.Object other$mwts = other.mwts();
    if (this$mwts == null ? other$mwts != null : !this$mwts.equals(other$mwts)) return false;
    final java.lang.Object this$ncs = this.ncs();
    final java.lang.Object other$ncs = other.ncs();
    if (this$ncs == null ? other$ncs != null : !this$ncs.equals(other$ncs)) return false;
    final java.lang.Object this$info = this.info();
    final java.lang.Object other$info = other.info();
    if (this$info == null ? other$info != null : !this$info.equals(other$info)) return false;
    final java.lang.Object this$docs = this.docs();
    final java.lang.Object other$docs = other.docs();
    if (this$docs == null ? other$docs != null : !this$docs.equals(other$docs)) return false;
    return true;
    }
  @Override public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.isInterface() ? 79 : 97);
    final java.lang.Object $ts = this.ts();
    result = result * PRIME + ($ts == null ? 43 : $ts.hashCode());
    final java.lang.Object $mwts = this.mwts();
    result = result * PRIME + ($mwts == null ? 43 : $mwts.hashCode());
    final java.lang.Object $ncs = this.ncs();
    result = result * PRIME + ($ncs == null ? 43 : $ncs.hashCode());
    final java.lang.Object $info = this.info();
    result = result * PRIME + ($info == null ? 43 : $info.hashCode());
    final java.lang.Object $docs = this.docs();
    result = result * PRIME + ($docs == null ? 43 : $docs.hashCode());
    return result;
    }
  
  public CoreL withPoss(final List<Pos> poss) {
    return this.poss == poss ? this : new CoreL(poss, this.isInterface, this.ts, this.mwts, this.ncs, this.info, this.docs);
    }
  public CoreL withInterface(final boolean isInterface) {
    return this.isInterface == isInterface ? this : new CoreL(this.poss, isInterface, this.ts, this.mwts, this.ncs, this.info, this.docs);
    }
  public CoreL withTs(final List<T> ts) {
    return this.ts == ts ? this : new CoreL(this.poss, this.isInterface, ts, this.mwts, this.ncs, this.info, this.docs);
    }
  public CoreL withMwts(final List<MWT> mwts) {
    return this.mwts == mwts ? this : new CoreL(this.poss, this.isInterface, this.ts, mwts, this.ncs, this.info, this.docs);
    }
  public CoreL withNcs(List<NC> ncs) {
    return this.ncs == ncs ? this : new CoreL(this.poss, this.isInterface, this.ts, this.mwts, ncs, this.info, this.docs);
    }
  public CoreL withInfo(Info info) {
    return this.info == info ? this : new CoreL(this.poss, this.isInterface, this.ts, this.mwts, this.ncs, info, this.docs);
    }
  public CoreL withDocs( List<Doc> docs) {
    return this.docs == docs ? this : new CoreL(this.poss, this.isInterface, this.ts, this.mwts, this.ncs, this.info, docs);
    }
  }