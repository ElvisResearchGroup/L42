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
  static BiFunction<List<String>, E, String> use(String s){
    return (xs,e)->java.lang.String.format(s,xs.toArray());
    }
  }
enum TrustedOp {
  //booleans
  And("OP&",Map.of(Bool,use("return %s & %s;"))),
  OR("OP|",Map.of(Bool,use("return %s | %s;"))),
  NOT("OP!",Map.of(Bool,use("return !%s;"))),
  CheckTrue("checkTrue",Map.of(Bool,use(
    "if(%s){return L42Void.instance;}"+
    "throw new L42Exception(L42Void.instance);"))),
  //StringBuilder
  _a("_a",append("a")),
  _b("_b",append("b")),
  //toString
  ToS("toS",Map.of(
    StringBuilder,use("return %s.toString();"),
    String,use("return %s;"),
    Int,use("return %s.toString();"),
    Bool,use("return %s.toString();")
    )),
  //
  StrDebug("strDebug",Map.of(
    String,use("Resources.out(%s); return L42Void.instance;"),
    TrustedIO,use("return %s.strDebug(%s);")
    )),
  DeployLibrary("deployLibrary",Map.of(
    TrustedIO,use("return %s.deployLibrary(%s,%s);"))),
  LimitTime("limitTime",Map.of(Limit,(xs,e)->
    "System.out.println("+xs.get(1)+"); return L42Void.instance;"
    )),
  Plus("OP+",Map.of(
    Int,use("return %s + %s;"),
    String,use("return %s + %s;")
    )),
  Mul("OP*",Map.of(Int,use("return %s * %s;")));
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