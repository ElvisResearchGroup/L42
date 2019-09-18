// Generated by delombok at Wed Sep 18 22:39:10 PETT 2019
package is.L42.generated;

import java.util.function.Function;
import java.util.List;
import is.L42.visitors.Visitable;

public interface LL extends Full.Leaf, HasVisitable, HasPos {
  @Override
  Visitable<? extends LL> visitable();

  default boolean isFullL() {
    return this instanceof Full.L;
  }

  LL withCs(List<C> cs, Function<Full.L.NC, Full.L.NC> fullF, Function<Core.L.NC, Core.L.NC> coreF);

  List<C> domNC();//error if FULL.L with reuse/...

  LL c(C c);//error if FULL.L with reuse/...

  LL cs(List<C> cs);//error if FULL.L with reuse/...

  //List<S> domS();//error if FULL.L //should be moved in CORE?
  //MH s(S s);//error if FULL.L //should be moved in CORE?
  @SuppressWarnings("serial")
  final class NotInDom extends RuntimeException {
    private final LL receiver;
    private final LDom parameter;

    @java.lang.SuppressWarnings("all")
    public NotInDom(final LL receiver, final LDom parameter) {
      this.receiver = receiver;
      this.parameter = parameter;
    }

    @java.lang.SuppressWarnings("all")
    public LL receiver() {
      return this.receiver;
    }

    @java.lang.SuppressWarnings("all")
    public LDom parameter() {
      return this.parameter;
    }

    @java.lang.SuppressWarnings("all")
    public NotInDom withReceiver(final LL receiver) {
      return this.receiver == receiver ? this : new NotInDom(receiver, this.parameter);
    }

    @java.lang.SuppressWarnings("all")
    public NotInDom withParameter(final LDom parameter) {
      return this.parameter == parameter ? this : new NotInDom(this.receiver, parameter);
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof LL.NotInDom)) return false;
      final LL.NotInDom other = (LL.NotInDom) o;
      if (!other.canEqual((java.lang.Object) this)) return false;
      final java.lang.Object this$receiver = this.receiver();
      final java.lang.Object other$receiver = other.receiver();
      if (this$receiver == null ? other$receiver != null : !this$receiver.equals(other$receiver)) return false;
      final java.lang.Object this$parameter = this.parameter();
      final java.lang.Object other$parameter = other.parameter();
      if (this$parameter == null ? other$parameter != null : !this$parameter.equals(other$parameter)) return false;
      return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
      return other instanceof LL.NotInDom;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final java.lang.Object $receiver = this.receiver();
      result = result * PRIME + ($receiver == null ? 43 : $receiver.hashCode());
      final java.lang.Object $parameter = this.parameter();
      result = result * PRIME + ($parameter == null ? 43 : $parameter.hashCode());
      return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
      return "LL.NotInDom(super=" + super.toString() + ", receiver=" + this.receiver() + ", parameter=" + this.parameter() + ")";
    }
  }
  /*@SuppressWarnings("serial")@Value @Wither 
  @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true)
  public static class ReuseOrDots extends RuntimeException{
    LL receiver; LDom parameter;
    }*/
}
