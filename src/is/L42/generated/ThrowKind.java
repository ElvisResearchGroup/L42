package is.L42.generated;
import static is.L42.tools.General.*;

public enum ThrowKind {
  Error("error"),
  Exception("exception"),
  Return("return");
  public final String inner;
  ThrowKind(String inner){this.inner = inner;}
  public static ThrowKind fromString(String s) {
   for (ThrowKind t : ThrowKind.values()) {
    if (t.inner.equals(s))return t;
    }
   throw bug();
  }
 }