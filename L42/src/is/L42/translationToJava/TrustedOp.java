package is.L42.translationToJava;

import static is.L42.tools.General.todo;
import static is.L42.translationToJava.TrustedKind.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import is.L42.generated.Core;
import is.L42.generated.Core.E;
import static is.L42.translationToJava.OpUtils.*;

class OpUtils{
  static Map<TrustedKind, BiFunction<List<String>, E, String>> append(String s){
    return Map.of(StringBuilder,(xs,e)->""+xs.get(0)+".append(\""+s+"\");return L42Void.instance;");
    }
  }
enum TrustedOp {
  //booleans
  And("OP&",Map.of(Bool,(xs,e)->"return "+xs.get(0)+" & "+xs.get(1)+";")),
  OR("OP|",Map.of(Bool,(xs,e)->"return "+xs.get(0)+" | "+xs.get(1)+";")),
  NOT("OP!",Map.of(Bool,(xs,e)->"return !"+xs.get(0)+";")),
  CheckTrue("checkTrue",Map.of(Bool,(xs,e)->
    "if("+xs.get(0)+"){return L42Void.instance;}"+
    "throw new L42Exception(L42Void.instance);")),
  //StringBuilder
  _a("_a",append("a")),
  _b("_b",append("b")),
  //toString
  ToS("toS",Map.of(
    StringBuilder,(xs,e)->"return "+xs.get(0)+".toString();",
    String,(xs,e)->"return "+xs.get(0)+";",
    Int,(xs,e)->"return "+xs.get(0)+".toString();",
    Bool,(xs,e)->"return "+xs.get(0)+".toString();"
    )),
  StrDebug("strDebug",Map.of(String,(xs,e)->
    "Resources.out("+xs.get(0)+"); return L42Void.instance;"
    )),
  LimitTime("limitTime",Map.of(Limit,(xs,e)->
    "System.out.println("+xs.get(1)+"); return L42Void.instance;"
    )),
  Plus("OP+",Map.of(
    Int,(xs,e)->"return "+xs.get(0)+" + "+xs.get(1)+";",
    String,(xs,e)->"return "+xs.get(0)+" + "+xs.get(1)+";"
    )),
  Mul("OP*",Map.of(Int,(xs,e)->"return "+xs.get(0)+" * "+xs.get(1)+";"));
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