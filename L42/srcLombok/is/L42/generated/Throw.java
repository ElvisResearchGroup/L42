package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;
static import tools.General.*;

public enum Throw {
  Error("error"),
  Exception("exception"),
  Return("return");
  public final String content;
  Throw(String content){this.content = content;}
  public static SignalKind fromString(String s) {
   for (SignalKind sk : SignalKind.values()) {
    if (sk.content.equals(s))return sk;
    }
   throw bug();
  }
 }
