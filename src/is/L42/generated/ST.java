// Generated by delombok at Tue Sep 28 20:54:10 NZDT 2021
package is.L42.generated;

import java.util.List;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;
import is.L42.common.Constants;

public interface ST extends HasWf, HasVisitable {
  Visitable<? extends ST> visitable();


  final class STMeth implements ST, Visitable<ST> {
    @Override
    public Visitable<ST> visitable() {
      return this;
    }

    @Override
    public ST accept(CloneVisitor cv) {
      return cv.visitSTMeth(this);
    }

    @Override
    public void accept(CollectorVisitor cv) {
      cv.visitSTMeth(this);
    }

    @Override
    public String toString() {
      return Constants.toS.apply(this);
    }

    @Override
    public boolean wf() {
      return Constants.wf.test(this);
    }

    private final ST st;
    private final S s;
    private final int i;/*-1 for no i*/

    @java.lang.SuppressWarnings("all")
    public STMeth(final ST st, final S s, final int i) {
      this.st = st;
      this.s = s;
      this.i = i;
    }

    @java.lang.SuppressWarnings("all")
    public ST st() {
      return this.st;
    }

    @java.lang.SuppressWarnings("all")
    public S s() {
      return this.s;
    }

    @java.lang.SuppressWarnings("all")
    public int i() {
      return this.i;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof ST.STMeth)) return false;
      final ST.STMeth other = (ST.STMeth) o;
      if (this.i() != other.i()) return false;
      final java.lang.Object this$st = this.st();
      final java.lang.Object other$st = other.st();
      if (this$st == null ? other$st != null : !this$st.equals(other$st)) return false;
      final java.lang.Object this$s = this.s();
      final java.lang.Object other$s = other.s();
      if (this$s == null ? other$s != null : !this$s.equals(other$s)) return false;
      return true;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      result = result * PRIME + this.i();
      final java.lang.Object $st = this.st();
      result = result * PRIME + ($st == null ? 43 : $st.hashCode());
      final java.lang.Object $s = this.s();
      result = result * PRIME + ($s == null ? 43 : $s.hashCode());
      return result;
    }

    @java.lang.SuppressWarnings("all")
    public ST.STMeth withSt(final ST st) {
      return this.st == st ? this : new ST.STMeth(st, this.s, this.i);
    }

    @java.lang.SuppressWarnings("all")
    public ST.STMeth withS(final S s) {
      return this.s == s ? this : new ST.STMeth(this.st, s, this.i);
    }

    @java.lang.SuppressWarnings("all")
    public ST.STMeth withI(final int i) {
      return this.i == i ? this : new ST.STMeth(this.st, this.s, i);
    }
  }


  final class STOp implements ST, Visitable<ST> {
    @Override
    public Visitable<ST> visitable() {
      return this;
    }

    @Override
    public ST accept(CloneVisitor cv) {
      return cv.visitSTOp(this);
    }

    @Override
    public void accept(CollectorVisitor cv) {
      cv.visitSTOp(this);
    }

    @Override
    public String toString() {
      return Constants.toS.apply(this);
    }

    @Override
    public boolean wf() {
      return Constants.wf.test(this);
    }

    private final Op op;
    private final List<List<ST>> stzs;

    @java.lang.SuppressWarnings("all")
    public STOp(final Op op, final List<List<ST>> stzs) {
      this.op = op;
      this.stzs = stzs;
    }

    @java.lang.SuppressWarnings("all")
    public Op op() {
      return this.op;
    }

    @java.lang.SuppressWarnings("all")
    public List<List<ST>> stzs() {
      return this.stzs;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof ST.STOp)) return false;
      final ST.STOp other = (ST.STOp) o;
      final java.lang.Object this$op = this.op();
      final java.lang.Object other$op = other.op();
      if (this$op == null ? other$op != null : !this$op.equals(other$op)) return false;
      final java.lang.Object this$stzs = this.stzs();
      final java.lang.Object other$stzs = other.stzs();
      if (this$stzs == null ? other$stzs != null : !this$stzs.equals(other$stzs)) return false;
      return true;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final java.lang.Object $op = this.op();
      result = result * PRIME + ($op == null ? 43 : $op.hashCode());
      final java.lang.Object $stzs = this.stzs();
      result = result * PRIME + ($stzs == null ? 43 : $stzs.hashCode());
      return result;
    }

    @java.lang.SuppressWarnings("all")
    public ST.STOp withOp(final Op op) {
      return this.op == op ? this : new ST.STOp(op, this.stzs);
    }

    @java.lang.SuppressWarnings("all")
    public ST.STOp withStzs(final List<List<ST>> stzs) {
      return this.stzs == stzs ? this : new ST.STOp(this.op, stzs);
    }
  }
}
