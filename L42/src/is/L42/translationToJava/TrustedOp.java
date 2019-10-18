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
  _q("_q",append("q")),
  _w("_w",append("w")),
  _e("_e",append("e")),
  _r("_r",append("r")),
  _t("_t",append("t")),  
  _y("_y",append("y")),
  _u("_u",append("u")),
  _i("_i",append("i")),
  _o("_o",append("o")),
  _p("_p",append("p")),  
  _a("_a",append("a")),
  _s("_s",append("s")),
  _d("_d",append("d")),
  _f("_f",append("f")),
  _g("_g",append("g")),  
  _h("_h",append("h")),
  _j("_j",append("j")),
  _k("_k",append("k")),
  _l("_l",append("l")),
  _z("_z",append("z")),  
  _x("_x",append("x")),
  _c("_c",append("c")),
  _v("_v",append("v")),
  _b("_b",append("b")),  
  _n("_n",append("n")),
  _m("_m",append("m")),
  
  
  
  
  
  
  
  
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