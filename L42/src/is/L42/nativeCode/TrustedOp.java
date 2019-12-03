package is.L42.nativeCode;

import static is.L42.nativeCode.OpUtils.*;
import static is.L42.nativeCode.Signature.*;
import static is.L42.nativeCode.TrustedKind.*;
import static is.L42.nativeCode.TT.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;
import static is.L42.generated.Mdf.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Core.T;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.P;
import is.L42.translationToJava.J;
import is.L42.translationToJava.NativeDispatch;
import lombok.NonNull;
    
interface TrustedT{}
enum TT implements TrustedT{Lib,Void,Any,Gen1,Gen2,Gen3,Gen4,This}
class Signature{
  Mdf methMdf;
  Mdf retMdf;
  TrustedT retT;
  List<Mdf>parMdfs;
  List<TrustedT>parTs;
  List<TrustedT>exceptions;
  Signature(Mdf methMdf,Mdf retMdf,TrustedT retT,List<Mdf>parMdfs,List<TrustedT>parTs,List<TrustedT>exceptions){
    this.methMdf=methMdf;
    this.retMdf=retMdf;
    this.retT=retT;
    this.parMdfs=parMdfs;
    this.parTs=parTs;
    this.exceptions=exceptions;
    }
  public static Signature sig(Mdf methMdf,Mdf retMdf,TrustedT retT){
    return new Signature(methMdf,retMdf,retT,L(),L(),L());
    }
  public static Signature sig(Mdf methMdf,Mdf retMdf,TrustedT retT,Mdf p1Mdf,TrustedT p1T){
    return new Signature(methMdf,retMdf,retT,L(p1Mdf),L(p1T),L());
    }
  public static Signature sig(Mdf methMdf,Mdf retMdf,TrustedT retT,Mdf p1Mdf,TrustedT p1T,Mdf p2Mdf,TrustedT p2T){
    return new Signature(methMdf,retMdf,retT,List.of(p1Mdf,p2Mdf),List.of(p1T,p2T),L());
    }
  public static Signature sig(Mdf methMdf,Mdf retMdf,TrustedT retT,Mdf p1Mdf,TrustedT p1T,Mdf p2Mdf,TrustedT p2T,Mdf p3Mdf,TrustedT p3T){
    return new Signature(methMdf,retMdf,retT,List.of(p1Mdf,p2Mdf,p3Mdf),List.of(p1T,p2T,p3T),L());
    }

  public static Signature sigI(TrustedT retT){
    return new Signature(Immutable,Immutable,retT,L(),L(),L());
    }
  public static Signature sigI(TrustedT retT,TrustedT ...pTs){
    var pts=List.of(pTs);
    List<Mdf> mdfs=L(pts,(c,e)->c.add(Mdf.Immutable));
    return new Signature(Immutable,Immutable,retT,mdfs,pts,L());
    }
  }
class OpUtils{
  static void checkParCount(Program p,MWT mwt,int expected){
    if(mwt.key().xs().size()==expected){return;}
    throw new EndError.TypeError(mwt._e().poss(),Err.nativeParameterCountInvalid(mwt.nativeUrl(),mwt.key(),expected));
    }
  static Map<TrustedKind,TrustedOp.Generator> append(String s){
    String pattern="%s.append(\""+s+"\");return L42Void.instance;";
    var u=use(pattern,sig(Mutable,Immutable,Void));
    return Map.of(StringBuilder,u);
    }
  static boolean typingUse(Program p, MWT mwt,Signature s){
    assert s.parMdfs.size()==s.parTs.size();
    if(s.parTs.size()!=mwt.mh().pars().size()){
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterCountInvalid(mwt.nativeUrl(),mwt.key(),s.parMdfs.size()));
      }
    checkMdf(mwt,mwt.mh().mdf(),s.methMdf);
    var t=mwt.mh().t();
    checkSingle(p,mwt,t.p(),t.mdf(),s.retT,s.retMdf);
    for(int i:range(mwt.mh().pars().size())){
      var pi=mwt.mh().pars().get(i).p();
      var mdfi=mwt.mh().pars().get(i).mdf();
      var tti=s.parTs.get(i);
      var tmdfi=s.parMdfs.get(i);
      checkSingle(p,mwt,pi,mdfi,tti,tmdfi);
      }
    //TODO: check exceptions
    return true;
    }
  private static void checkGen(int i,Program p,MWT mwt,P pi){
    assert p.topCore().info().nativePar().size()>i;
    var pari=p.topCore().info().nativePar().get(i);
    if(pi.equals(pari)){return;}
    throw new EndError.TypeError(mwt._e().poss(),
      Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,pari));            
    }
  private static void checkMdf(MWT mwt,Mdf mdfi,Mdf tmdfi){
    if(mdfi!=tmdfi){
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),mdfi,tmdfi));
      }
    }
  private static void checkSingle(Program p,MWT mwt,P pi,Mdf mdfi,TrustedT tti,Mdf tmdfi){
    checkMdf(mwt,mdfi,tmdfi);
    if(tti instanceof TrustedKind){
        var ki=(TrustedKind)tti;
        var li=p._ofCore(pi);
        assert li!=null: pi+" "+p.pop().topCore();
        var kind=li.info().nativeKind();
        if(!kind.isEmpty() && ki==TrustedKind._fromString(kind)){return;}
        throw new EndError.TypeError(mwt._e().poss(),
          Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,ki));            
        }
    if(tti==Lib){
      if(pi==P.pLibrary){return;}
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,"Library"));            
      }
    if(tti==Void){
      if(pi==P.pVoid){return;}
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,"Void"));            
      }
    if(tti==Any){
      if(pi==P.pAny){return;}
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,"Any"));            
      }
    if(tti==Gen1){checkGen(0,p,mwt,pi);}
    if(tti==Gen2){checkGen(1,p,mwt,pi);}
    if(tti==Gen3){checkGen(2,p,mwt,pi);}
    if(tti==Gen4){checkGen(3,p,mwt,pi);}
    if(tti==This){
      if(pi.equals(P.pThis0)){return;}
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,"This"));            
      }
    }
  @SuppressWarnings("removal")//String.formatted is "preview feature" so triggers warnings
  static TrustedOp.Generator use(String s0,Signature sig){
    return (type,p,mwt)->{
      if(type && typingUse(p,mwt,sig)){return "";}
      String s=s0;
      J j=new J(p, G.empty(), false,new ArrayList<>());
      if(s.contains("%This")){
        String thisS=j.typeNameStr(p);
        s=s.replace("%This",thisS);
        }
      for(int i:range(p.topCore().info().nativePar())){
        P geni=p.topCore().info().nativePar().get(i);
        if(s.contains("%Gen"+(i+1)+".")){
          String geniS=J.classNameStr(p.navigate(geni.toNCs()));
          s=s.replace("%Gen"+(i+1)+".",geniS+".");
          }
        if(s.contains("%Gen"+(i+1))){
          s=s.replace("%Gen"+(i+1),j.typeNameStr(geni));
          }
        }           
      return s.formatted(NativeDispatch.xs(mwt).toArray());
      };
    }
  @SuppressWarnings("removal")//String.formatted is "preview feature" so triggers warnings
  static TrustedOp.Generator use(String s,Signature sig,Class<?> errKind,int errNum,String err,int[] msgs){
    return (typed,p,mwt)->{
      if(typed && typingUse(p,mwt,sig)){return "";}
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
  And("OP&",Map.of(Bool,use("return %s & %s;",sigI(Bool,Bool)))),
  OR("OP|",Map.of(Bool,use("return %s | %s;",sigI(Bool,Bool)))),
  NOT("OP!",Map.of(Bool,use("return !%s;",sigI(Bool)))),
  CheckTrue("checkTrue",Map.of(Bool,use(
    "if(%s){return L42Void.instance;}"+
    "throw new L42Exception(L42Void.instance);",sigI(Void)))),
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
  SPercent("'%'",append("%%")),//%% instead of % for the stringformat
  SBackSlash("'\\'",append("\\\\")),
  SSpace("space",append(" ")),
  SNewLine("newLine",append("\\\\n")),
  AddAll("addAll",Map.of(StringBuilder,use("%s.append(%s);return L42Void.instance;",
    sig(Mutable,Immutable,Void,Immutable,String)))),
   
  //toString
  ToS("toS",Map.of(
    StringBuilder,use("return %s.toString();",sig(Readable,Immutable,String)),
    String,use("return %s;",sig(Readable,Immutable,String)),
    Int,use("return ((Object)%s).toString();",sig(Readable,Immutable,String)),
    Bool,use("return ((Object)%s).toString();",sig(Readable,Immutable,String))
    )),
    
  ToInt("toInt",Map.of(String,use(
    "return Integer.parseInt(%s);",sig(Readable,Immutable,Int),
    NumberFormatException.class,0,
    "The string %s is not a valid number",new int[]{0}
    ))),
  StrDebug("strDebug",Map.of(
    String,use("Resources.out(%s); return L42Void.instance;",sigI(Void)),
    TrustedIO,use("return %s.strDebug(%s);",sigI(Void,String))
    )),
  TestCondition("testCondition",Map.of(
  //Library pos,This1.S name,B,This1.S message
    TrustedIO,use("return %s.testCondition(%s,%s,%s,%s);",sigI(Void,
      Lib,String,Bool,String))
    )),
  DeployLibrary("deployLibrary",Map.of(
    TrustedIO,use("return %s.deployLibrary(%s,%s);",sigI(Void,String,Lib)))),
  SimpleRedirect("simpleRedirect",Map.of(
    Meta,use("return %s.simpleRedirect(%s,%s,%s);",sig(Immutable,Immutable,Lib,
      Immutable,String,  Immutable,Lib,  Class,Any))
    )),
  SimpleSum("simpleSum",Map.of(
    Meta,use("return %s.simpleSum(%s,%s);",sigI(Lib,Lib,Lib)))),
  Resource("resource",Map.of(
    Meta,use("return %s.resource(%s);",sigI(Lib,Lib)))),
  AddMapP("addMapP",Map.of(
    Meta,use("return %s.addMapP(%s,%s);",sig(Immutable,Immutable,Meta,
      Immutable,String,  Class,Any))
    )),
  MergeMap("mergeMap",Map.of(
    Meta,use("return %s.mergeMap(%s);",sigI(Meta,Meta)))),
  ApplyMap("applyMap",Map.of(
    Meta,use("return %s.applyMap(%s);",sigI(Lib,Lib)))),
  
  //Vector
  VectorK("vectorK",Map.of(Vector,use("return new %This(%2$s);",sig(Class,Mutable,This,Immutable,Int)))),
  IsEmpty("isEmpty",Map.of(Vector,use("return %s.isEmpty();",sig(Readable,Immutable,Bool)))),
  Size("size",Map.of(
    Vector,use("return %s.size()/2;",sig(Readable,Immutable,Int)),
    String,use("return %s.length();",sig(Readable,Immutable,Int))
    )),
  ReadVal("readVal",Map.of(Vector,use("return %s.get(%s*2);",sig(Readable,Readable,Gen1,Immutable,Int)))),
  ImmVal("immVal",Map.of(Vector,use("""
    var tmp=%1$s.get(%2$s*2+1);
    if(tmp==null){return %1$s.get(%2$s*2);}
    throw new Error("get immVal but was mut:"+%1$s);
    """,
    sig(Readable,Immutable,Gen1,Immutable,Int)))),
  HashVal("#val",Map.of(Vector,use("""
    var tmp=%1$s.get(%2$s*2+1);
    if(tmp!=null){return tmp;}
    throw new Error("get mut val but was imm");
    """,
    sig(Mutable,Mutable,Gen1,Immutable,Int)))),
  SetImm("setImm",Map.of(Vector,use("%1$s.set(%2$s*2,%3$s);%1$s.set(%2$s*2+1,null);return L42Void.instance;",sig(Mutable,Immutable,Void,Immutable,Int,Immutable,Gen1)))),
  SetMut("setMut",Map.of(Vector,use("%1$s.set(%2$s*2,%3$s);%1$s.set(%2$s*2+1,%3$s);return L42Void.instance;",sig(Mutable,Immutable,Void,Immutable,Int,Mutable,Gen1)))),
  AddImm("addImm",Map.of(Vector,use("%1$s.add(%2$s*2,%3$s);%s.add(%2$s*2+1,null);return L42Void.instance;",sig(Mutable,Immutable,Void,Immutable,Int,Immutable,Gen1)))),
  AddMut("addMut",Map.of(Vector,use("%1$s.add(%2$s*2,%3$s);%1$s.add(%2$s*2+1,%3$s);return L42Void.instance;",sig(Mutable,Immutable,Void,Immutable,Int,Mutable,Gen1)))),
  Remove("remove",Map.of(Vector,use("%1$s.remove(%2$s*2+1);%1$s.remove(%2$s*2);return L42Void.instance;",sig(Mutable,Immutable,Void,Immutable,Int)))),
  //TODO: handle exceptions, immVal/mutVal absent+index out ouf bound
  //arithmetic
  Plus("OP+",Map.of(
    Int,use("return %s + %s;",sigI(Int,Int)),
    String,use("return %s + %s;",sigI(String,String))
    )),
  Times("OP*",Map.of(Int,use("return %s * %s;",sigI(Int,Int)))),
  Divide("OP/",Map.of(Int,use("return %s / %s;",sigI(Int,Int)))),
  Minus("OP-",Map.of(Int,use("return %s - %s;",sigI(Int,Int)))),
  LT("OP<",Map.of(Int,use("return %s < %s;",sig(Readable,Immutable,Bool,Readable,Int)))),
  LTEqual("OP<=",Map.of(Int,use("return %s <= %s;",sig(Readable,Immutable,Bool,Readable,Int)))),
  EqualEqual("OP==",Map.of(
    Int,use("return %s == %s;",sig(Readable, Immutable,Bool, Readable,Int)),
    String,use("return %s.equals(%s);",sig(Readable, Immutable,Bool, Readable,String)),
    Bool,use("return %s == %s;",sig(Readable, Immutable,Bool, Readable,Bool))
    )),
  Succ("succ",Map.of(Int,use("return %s +1;",sigI(Int)))),
  Pred("pred",Map.of(Int,use("return %s -1;",sigI(Int)))),
  LazyMessageK("lazyMessageK",Map.of(LazyMessage,use("return new L42LazyMsg(%2$s);",sig(Class,Mutable,LazyMessage,Immutable,String)))),
  SetMsg("setMsg",Map.of(LazyMessage,use("%s.setMsg(%s);return L42Void.instance;",sig(Mutable,Immutable,Void,Immutable,String)))),
  OptK("optK",Map.of(Opt,use("return (%Gen1)%2$s;",sig(Class,Mutable,This,MutableFwd,Gen1)))), 
  Get("get",  Map.of(Opt,use("""
    if(%1$s!=null){return %1$s;}
    throw new L42Error(%Gen2.wrap(
      new L42LazyMsg(\"Optional value is empty\")
      ));
    """,sig(Readable,Readable,Gen1)),
    LazyMessage,use("return %s.getMsg();",sig(Readable,Immutable,String))
    )),
  HGet("#get",Map.of(Opt,use("""
    if(%1$s!=null){return %1$s;}
    throw new L42Error(%Gen2.wrap(
      new L42LazyMsg(\"Optional value is empty\")
      ));
    """,sig(Mutable,Mutable,Gen1))))  
  ;
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