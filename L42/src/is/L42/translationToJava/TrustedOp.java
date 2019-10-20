package is.L42.translationToJava;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;
import static is.L42.translationToJava.TrustedKind.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.T;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.P;

import static is.L42.translationToJava.OpUtils.*;

interface Generator{String of(Program p,MWT mwt);}    
class OpUtils{
  static List<String>xs(MWT mwt){
    return L(Stream.concat(Stream.of("£xthis"), mwt.mh().s().xs().stream().map(x->"£x"+x.inner())));
    }
  static Map<TrustedKind,Generator> append(String s){
    return Map.of(StringBuilder,(p,mwt)->{
      var xs=xs(mwt);
      return ""+xs.get(0)+".append(\""+s+"\");return L42Void.instance;";
      });
    }
  @SuppressWarnings("removal")//String.formatted is "preview feature" so triggers warnings
  static Generator use(String s){
    return (p,mwt)->s.formatted(xs(mwt).toArray());
    }
  @SuppressWarnings("removal")//String.formatted is "preview feature" so triggers warnings
  static Generator use(String s,Class<?> errKind,int errNum,String err,int ... msgs){
    return (p,mwt)->{
      var xs=xs(mwt);
      List<String>errs=L(range(msgs.length),(c,i)->{
        var xi=xs.get(msgs[i]);
        xi="\"+"+xi+"+\"";
        xi="\\\""+xi+"\\\"";
        c.add(xi);
        });
      String tryBody="try{"+s.formatted(xs.toArray())+"}";
      String catchBody="catch("+errKind.getCanonicalName()+" _unusedErr){throw new L42Error(";
      T t=mwt.mh().parsWithThis().get(errNum);
      String name=J.classNameStr(p.navigate(t.p().toNCs()));
      catchBody+=name+".wrap(\""+err.formatted(errs.toArray())+"\")";
      catchBody+=");}";
      return tryBody+catchBody;
      };
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
  D0("'0'",append("0")),
  D1("'1'",append("1")),
  D2("'2'",append("2")),
  D3("'3'",append("3")),
  D4("'4'",append("4")),
  D5("'5'",append("5")),
  D6("'6'",append("6")),
  D7("'7'",append("7")),
  D8("'8'",append("8")),
  D9("'9'",append("9")),
  
  Lq("'q'",append("q")),
  Lw("'w'",append("w")),
  Le("'e'",append("e")),
  Lr("'r'",append("r")),
  Lt("'t'",append("t")),  
  Ly("'y'",append("y")),
  Lu("'u'",append("u")),
  Li("'i'",append("i")),
  Lo("'o'",append("o")),
  Lp("'p'",append("p")),  
  La("'a'",append("a")),
  Ls("'s'",append("s")),
  Ld("'d'",append("d")),
  Lf("'f'",append("f")),
  Lg("'g'",append("g")),  
  Lh("'h'",append("h")),
  Lj("'j'",append("j")),
  Lk("'k'",append("k")),
  Ll("'l'",append("l")),
  Lz("'z'",append("z")),  
  Lx("'x'",append("x")),
  Lc("'c'",append("c")),
  Lv("'v'",append("v")),
  Lb("'b'",append("b")),  
  Ln("'n'",append("n")),
  Lm("'m'",append("m")),
  
  UQ("'Q'",append("Q")),
  UW("'W'",append("W")),
  UE("'E'",append("E")),
  UR("'R'",append("R")),
  UT("'T'",append("T")),  
  UY("'Y'",append("Y")),
  UU("'U'",append("U")),
  UI("'I'",append("I")),
  UO("'O'",append("O")),
  UP("'P'",append("P")),  
  UA("'A'",append("A")),
  US("'S'",append("S")),
  UD("'D'",append("D")),
  UF("'F'",append("F")),
  UG("'G'",append("G")),  
  UH("'H'",append("H")),
  UJ("'J'",append("J")),
  UK("'K'",append("K")),
  UL("'L'",append("L")),
  UZ("'Z'",append("Z")),  
  UX("'X'",append("X")),
  UC("'C'",append("C")),
  UV("'V'",append("V")),
  UB("'B'",append("B")),  
  UN("'N'",append("N")),
  UM("'M'",append("M")),

  SPlus("'+'",append("+")),
  SLess("'-'",append("-")),
  STilde("'~'",append("~")),
  SBang("'!'",append("!")),
  SAnd("'&'",append("&")),
  SOr("'|'",append("|")),
  SLeft("'<'",append("<")),
  SRight("'>'",append(">")),
  SEqual("'='",append("=")),
  STimes("'*'",append("*")),
  SDivide("'/'",append("/")),
  SORound("'('",append("(")),
  SCRound("')'",append(")")),
  SOSquare("'['",append("[")),
  SCSquare("']'",append("]")),
  SOCurly("oCurly",append("{")),
  SCCurly("cCurly",append("}")),
  SDQuote("'\"'",append("\\\"")),
  SSQuote("'\''",append("'")),
  SHQuote("'`'",append("`")),
  SQMark("'?'",append("?")),
  SHat("'^'",append("^")),
  SComma("','",append(",")),
  SSemicolon("';'",append(";")),
  SColon("':'",append(":")),
  SDot("'.'",append(".")),
  SUnderscore("'_'",append("_")),
  SHash("'#'",append("#")),
  SAt("'@'",append("@")),
  SDollar("'$'",append("$")),
  SPercent("'%'",append("%")),
  SBackSlash("'\\'",append("\\\\")),
  SSpace("space",append(" ")),
  SNewLine("newLine",append("\\\\n")),
   
  //toString
  ToS("toS",Map.of(
    StringBuilder,use("return %s.toString();"),
    String,use("return %s;"),
    Int,use("return ((Object)%s).toString();"),
    Bool,use("return ((Object)%s).toString();")
    )),
    
  ToInt("toInt",Map.of(String,use(
    "return Integer.parseInt(%s);",
    NumberFormatException.class,0,
    "The string %s is not a valid number",0
    ))),
  StrDebug("strDebug",Map.of(
    String,use("Resources.out(%s); return L42Void.instance;"),
    TrustedIO,use("return %s.strDebug(%s);")
    )),
  DeployLibrary("deployLibrary",Map.of(
    TrustedIO,use("return %s.deployLibrary(%s,%s);"))),
  Plus("OP+",Map.of(
    Int,use("return %s + %s;"),
    String,use("return %s + %s;")
    )),
  Mul("OP*",Map.of(Int,use("return %s * %s;")));
  public final String inner;
  Map<TrustedKind,Generator>code;
  TrustedOp(String inner,Map<TrustedKind,Generator>code){
    this.inner = inner;
    this.code=code;
    }
  public static TrustedOp fromString(String s) {
   for (TrustedOp t : TrustedOp.values()) {
    if (t.inner.equals(s))return t;
    }
   assert false:"#"+s+"#";
   throw todo();
  }
 }