package is.L42.nativeCode;

import static is.L42.nativeCode.OpUtils.*;
import static is.L42.nativeCode.TrustedKind.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.T;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.P;
import is.L42.translationToJava.J;
import is.L42.translationToJava.NativeDispatch;
    
class OpUtils{
  static void checkParCount(Program p,MWT mwt,int expected){
    if(mwt.key().xs().size()==expected){return;}
    throw new EndError.TypeError(mwt._e().poss(),Err.nativeParameterCountInvalid(mwt.nativeUrl(),mwt.key(),expected));
    }
  static Map<TrustedKind,TrustedOp.Generator> append(String s){
    return Map.of(StringBuilder,(type,p,mwt)->{
      if(type){
        checkParCount(p,mwt,0);
        return "";
        }
      var xs=NativeDispatch.xs(mwt);
      return ""+xs.get(0)+".append(\""+s+"\");return L42Void.instance;";
      });
    }
    
  static boolean typingUse(TrustedKind[] kinds,Program p, MWT mwt,int libPars,int classAnyPars){
    if(kinds.length+libPars+classAnyPars!=mwt.mh().pars().size()){
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterCountInvalid(mwt.nativeUrl(),mwt.key(),kinds.length));
      }
    for(int i:range(mwt.mh().pars().size())){
      var pi=mwt.mh().pars().get(i).p();
      var mdfi=mwt.mh().pars().get(i).mdf();
      if(i<kinds.length){
        var ki=kinds[i];
        var kind=p._ofCore(pi).info().nativeKind();
        if(kind.isEmpty() || ki!=TrustedKind.fromString(kind)){
          throw new EndError.TypeError(mwt._e().poss(),
            Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,ki));            
          }
        }
      else if(i<kinds.length+libPars){if(pi!=P.pLibrary){
        throw new EndError.TypeError(mwt._e().poss(),
          Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,"Library"));            
        }}
      else if(pi!=P.pAny || !mdfi.isClass()){
        throw new EndError.TypeError(mwt._e().poss(),
          Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,"class Any"));            
        }
      }
    return true;
    }
  static TrustedOp.Generator use(String s,TrustedKind ...kinds){
    return use(s,kinds,0,0);
    }
  @SuppressWarnings("removal")//String.formatted is "preview feature" so triggers warnings
  static TrustedOp.Generator use(String s,TrustedKind[]kinds,int libPars,int classAnyPars){
    return (type,p,mwt)->{
      if(type && typingUse(kinds,p,mwt,libPars,classAnyPars)){return "";}
      return s.formatted(NativeDispatch.xs(mwt).toArray());
      };
    }
  @SuppressWarnings("removal")//String.formatted is "preview feature" so triggers warnings
  static TrustedOp.Generator use(String s,Class<?> errKind,int errNum,String err,int[] msgs,TrustedKind ...kinds){
    return (typed,p,mwt)->{
      if(typed && typingUse(kinds,p,mwt,0,0)){return "";}
      var xs=NativeDispatch.xs(mwt);
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
public enum TrustedOp {
  //booleans
  And("OP&",Map.of(Bool,use("return %s & %s;",Bool))),
  OR("OP|",Map.of(Bool,use("return %s | %s;",Bool))),
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
    "The string %s is not a valid number",new int[]{0}
    ))),
  StrDebug("strDebug",Map.of(
    String,use("Resources.out(%s); return L42Void.instance;"),
    TrustedIO,use("return %s.strDebug(%s);",String)
    )),
  DeployLibrary("deployLibrary",Map.of(
    TrustedIO,use("return %s.deployLibrary(%s,%s);",new TrustedKind[]{String},1,0))),
  Plus("OP+",Map.of(
    Int,use("return %s + %s;",Int),
    String,use("return %s + %s;",String)
    )),
  Mul("OP*",Map.of(Int,use("return %s * %s;",Int)));
  public interface Generator{String of(boolean type,Program p,MWT mwt);}
  public final String inner;
  Map<TrustedKind,Generator>code;
  public Generator _of(TrustedKind k){return code.get(k);}
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