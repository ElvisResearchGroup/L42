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
  public static String nativeFactory(String nativeKind, List<String> xs) {
    var k=TrustedKind.fromString(nativeKind);
    return k.factory(xs);
    }

}

enum TrustedKind {
  Int("int"){public String factory(List<String> xs){
    assert xs.size()==1;//just this
    return "return 0;";
    }},
  String("String"){public String factory(List<String> xs){
    assert xs.size()==1;
    return "return \"\";";
    }};
  public final String inner;
  TrustedKind(String inner){this.inner = inner;}
  public abstract String factory(List<String> xs);
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

 