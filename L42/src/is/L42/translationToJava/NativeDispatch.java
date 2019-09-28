package is.L42.translationToJava;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import is.L42.generated.Core;
import is.L42.generated.Core.E;

public class NativeDispatch {
  public static String nativeCode(String nativeKind, String nativeUrl, List<String> xs, E e) {
    if(!nativeUrl.startsWith("trusted:")){throw bug();}
    String nativeOp=nativeUrl.substring("trusted:".length());
    var k=TrustedKind.fromString(nativeKind);
    var op=TrustedOp.fromString(nativeOp);
    return op.code.get(k).apply(xs,e);
    }
}

enum TrustedKind {
  Int("int"),
  String("String");
  public final String inner;
  TrustedKind(String inner){this.inner = inner;}
  public static TrustedKind fromString(String s) {
   for (TrustedKind t : TrustedKind.values()) {
    if (t.inner.equals(s))return t;
    }
   throw todo();
  }
 }
enum TrustedOp {
  Plus("OP+",Map.of(
    TrustedKind.Int,(xs,e)->"return "+xs.get(0)+" + "+xs.get(1)+";"
    )),
  Mul("OP*",Map.of(
    TrustedKind.Int,(xs,e)->""
    ));
  public final String inner;
  Map<TrustedKind,BiFunction<List<String>,Core.E,String>>code;
  TrustedOp(String inner,Map<TrustedKind,BiFunction<List<String>,Core.E,String>>code){
    this.inner = inner;
    this.code=code;
    }
  public static TrustedOp fromString(String s) {
   for (TrustedOp t : TrustedOp.values()) {
    if (t.inner.equals(s))return t;
    }
   throw todo();
  }
 }

 