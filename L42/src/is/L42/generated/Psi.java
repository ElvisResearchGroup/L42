// Generated by delombok at Sun Feb 02 15:54:10 NZDT 2020
package is.L42.generated;

public final class Psi {
  private final P.NCs p;
  private final S s;
  private final int i;

  @java.lang.SuppressWarnings("all")
  public Psi(final P.NCs p, final S s, final int i) {
    this.p = p;
    this.s = s;
    this.i = i;
  }

  @java.lang.SuppressWarnings("all")
  public P.NCs p() {
    return this.p;
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
    if (!(o instanceof Psi)) return false;
    final Psi other = (Psi) o;
    final java.lang.Object this$p = this.p();
    final java.lang.Object other$p = other.p();
    if (this$p == null ? other$p != null : !this$p.equals(other$p)) return false;
    final java.lang.Object this$s = this.s();
    final java.lang.Object other$s = other.s();
    if (this$s == null ? other$s != null : !this$s.equals(other$s)) return false;
    if (this.i() != other.i()) return false;
    return true;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final java.lang.Object $p = this.p();
    result = result * PRIME + ($p == null ? 43 : $p.hashCode());
    final java.lang.Object $s = this.s();
    result = result * PRIME + ($s == null ? 43 : $s.hashCode());
    result = result * PRIME + this.i();
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "Psi(p=" + this.p() + ", s=" + this.s() + ", i=" + this.i() + ")";
  }
}
