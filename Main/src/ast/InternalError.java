// Generated by delombok at Sat Oct 03 15:00:07 CEST 2015
package ast;

@SuppressWarnings("serial")
public abstract class InternalError extends RuntimeException {

//stuff that should not get out

  public static final class InterfaceNotFullyProcessed extends InternalError {

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public InterfaceNotFullyProcessed() {
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof InternalError.InterfaceNotFullyProcessed)) return false;
      final InterfaceNotFullyProcessed other = (InterfaceNotFullyProcessed)o;
      if (!other.canEqual((java.lang.Object)this)) return false;
      return true;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    protected boolean canEqual(final java.lang.Object other) {
      return other instanceof InternalError.InterfaceNotFullyProcessed;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public int hashCode() {
      int result = 1;
      return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public java.lang.String toString() {
      return "InternalError.InterfaceNotFullyProcessed(super=" + super.toString() + ")";
    }
  }

  public static final class FurtherAnnotationImpossible extends InternalError {

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public FurtherAnnotationImpossible() {
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof InternalError.FurtherAnnotationImpossible)) return false;
      final FurtherAnnotationImpossible other = (FurtherAnnotationImpossible)o;
      if (!other.canEqual((java.lang.Object)this)) return false;
      return true;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    protected boolean canEqual(final java.lang.Object other) {
      return other instanceof InternalError.FurtherAnnotationImpossible;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public int hashCode() {
      int result = 1;
      return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public java.lang.String toString() {
      return "InternalError.FurtherAnnotationImpossible(super=" + super.toString() + ")";
    }
  }

  public static final class ETDeepNotApplicable extends InternalError {

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public ETDeepNotApplicable() {
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public boolean equals(final java.lang.Object o) {
      if (o == this) return true;
      if (!(o instanceof InternalError.ETDeepNotApplicable)) return false;
      final ETDeepNotApplicable other = (ETDeepNotApplicable)o;
      if (!other.canEqual((java.lang.Object)this)) return false;
      return true;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    protected boolean canEqual(final java.lang.Object other) {
      return other instanceof InternalError.ETDeepNotApplicable;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public int hashCode() {
      int result = 1;
      return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public java.lang.String toString() {
      return "InternalError.ETDeepNotApplicable(super=" + super.toString() + ")";
    }
  }
}