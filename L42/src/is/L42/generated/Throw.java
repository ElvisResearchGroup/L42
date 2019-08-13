package is.L42.generated;
import static is.L42.tools.General.*;

public enum Throw {
  Error("error"),
  Exception("exception"),
  Return("return");
  public final String content;
  Throw(String content){this.content = content;}
  public static Throw fromString(String s) {
   for (Throw t : Throw.values()) {
    if (t.content.equals(s))return t;
    }
   throw bug();
  }
 }
