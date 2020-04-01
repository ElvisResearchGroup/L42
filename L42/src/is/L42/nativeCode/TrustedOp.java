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
    
public enum TrustedOp {
  //booleans
  And("OP&",Map.of(Bool,use("return %s & %s;",sigI(Bool,Bool)))),
  OR("OP|",Map.of(Bool,use("return %s | %s;",sigI(Bool,Bool)))),
  NOT("OP!",Map.of(Bool,use("return !%s;",sigI(Bool)))),
  CheckTrue("checkTrue",Map.of(Bool,use(
    "if(%s){return L42£Void.instance;}"+
    "throw new L42Exception(L42£Void.instance);",sigI(Void)))),
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
  SNewLine("newLine",append("\\n")),
  AddAll("addAll",Map.of(StringBuilder,use("%s.append(%s);return L42£Void.instance;",
    sig(Mutable,Immutable,Void,Immutable,String)))),
   
  //toString
  ToS("toS",useToS(StringBuilder,Int,Bool,Name,Nested,Doc,Type,BigRational)),//String is pre added
  ToInt("toInt",Map.of(String,use("""
    try{return Integer.parseInt(%1$s);}
    catch(NumberFormatException nfe){
      throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
        "The string \\""+%1$s+"\\" is not a valid number"
        )));
      }
    """,sig(Readable,Immutable,Int)
    ))),
  ToNum("toNum",Map.of(String,use("""
    try{return L42£BigRational.from(%1$s);}
    catch(NumberFormatException nfe){
      throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
        "The string \\""+%1$s+"\\" is not a valid number"
        )));
      }
    """,sig(Readable,Immutable,BigRational)
    ))),
  ToName("toName",Map.of(String,use("""
    try{return L42£Name.parse(%1$s);}
    catch(NumberFormatException nfe){
      throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
        "The string \\""+%1$s+"\\" is not a valid name"
        )));
      }
    """,sig(Readable,Immutable,Name)
    ))),
  X("x",Map.of(Name,use("return %s.x();",sig(Readable,Immutable,String)))),
  Selector("selector",Map.of(Name,use("return %s.selector();",sig(Readable,Immutable,String)))),
  Path("path",Map.of(Name,use("return %s.path();",sig(Readable,Immutable,String)))),
  WithPath("withPath",Map.of(Name,use("return %s.withPath(%s);",sig(Readable,Immutable,Name,Immutable,String)))),
  WithSelector("withSelector",Map.of(Name,use("return %s.withSelector(%s);",sig(Readable,Immutable,Name,Immutable,String)))),
  WithX("withX",Map.of(Name,use("return %s.withX(%s);",sig(Readable,Immutable,Name,Immutable,String)))),
  StrDebug("strDebug",Map.of(
    String,use("Resources.out(%s); return L42£Void.instance;",sigI(Void)),
    TrustedIO,use("return %s.strDebug(%s);",sigI(Void,String))
    )),
  AddToLog("addToLog",Map.of(
    TrustedIO,use("return %s.addToLog(%s,%s);",sigI(Void,String,String))
    )),
  ClearLog("clearLog",Map.of(
    TrustedIO,use("return %s.clearLog(%s);",sigI(Void,String))
    )),
  ReadLog("#$readLog",Map.of(
    TrustedIO,use("return %s.readLog(%s);",sigI(String,String))
    )),
  TestCondition("testCondition",Map.of(
  //Library pos,This1.S name,B,This1.S message
    TrustedIO,use("return %s.testCondition(%s,%s,%s,%s);",sigI(Void,
      Lib,String,Bool,String))
    )),
  TestActualExpected("testActualExpected",Map.of(
    TrustedIO,use("return %s.testActualExpected(%s,%s,%s,%s,%s,%s);",sigI(Void,
      Lib,String,String,String,String,String))
    )),  
  DeployLibrary("deployLibrary",Map.of(
    TrustedIO,use("return %s.deployLibrary(%s,%s);",sigI(Void,String,Lib)))),
  //####META####
  SimpleSum("simpleSum",Map.of(
    Meta,use("return %s.simpleSum(%s,%s,%Gen1::wrap,%Gen2::wrap);",sigI(Lib,Lib,Lib)))),
  Resource("resource",Map.of(
    Meta,use("return %s.resource(%s);",sigI(Lib,Lib)))),
  Close("close",Map.of(
    Meta,use("return %s.close(%s,%s,%Gen1::wrap);",sigI(Lib,Lib,String)))),
  AddConstructors("addConstructors",Map.of(
    Meta,use("return %s.addConstructors(%s,%s,%Gen1::wrap,%s,%s);",sigI(Lib,Lib,String,String,String)))),
  Wither("wither",Map.of(
    Meta,use("return %s.wither(%s,%s,%Gen1::wrap,%s);",sigI(Lib,Lib,String,String)))),
  AddMapP("addMapP",Map.of(
    Meta,use("return %s.addMapP(%s,%s);",sig(Immutable,Immutable,Meta,
      Immutable,Name,Class,Any))
    )),
  AddMapDoubleArrow("addMapDoubleArrow",Map.of(
    Meta,use("return %s.addMapDoubleArrow(%s,%s);",sigI(Meta,Name,Name)))),
  AddMapSingleArrow("addMapSingleArrow",Map.of(
    Meta,use("return %s.addMapSingleArrow(%s,%s);",sigI(Meta,Name,Name)))),
  MergeMap("mergeMap",Map.of(
    Meta,use("return %s.mergeMap(%s);",sigI(Meta,Meta)))),
  ApplyMap("applyMap",Map.of(
    Meta,use("return %s.applyMap(%s,%Gen3::wrap,%Gen4::wrap,%Gen1::wrap,%Gen2::wrap);",sigI(Lib,Lib)))),
  PathName("pathName",Map.of(
    Meta,use("return %s.pathName(%s);",sig(Immutable,Immutable,String,
      Class,Any))
    )),  
  //####Nested####
  FromClass("fromClass",all(
    nested("L42£Nested.fromClass(%2$s)",sig(Class,Immutable,Nested,Class,Any)),
    type("L42£Type.fromClass(%2$s,%3$s,%4$s)",sig(Class,Immutable,Type,Immutable,String,Immutable,Doc,Class,Any))
    )),
  FromLibrary("fromLibrary",all(
    nested("L42£Nested.fromLibrary(%2$s)",sig(Class,Immutable,Nested,Immutable,Lib)),
    type("L42£Type.fromLibrary(%2$s,%3$s,%4$s)",sig(Class,Immutable,Type,Immutable,String,Immutable,Doc,Immutable,Name)))
    ),
  NestedByName("nestedByName",nested("%s.nestedByName(%s)",sigI(Nested,Name),1)),
  HasOuter("hasOuter",nested("%s.hasOuter()",sigI(Bool))),
  OuterC("outerName",nested("%s.outerName()",sigI(String),3)),
  Outer("outer",nested("%s.outer()",sigI(Nested),3)),
  OuterDoc("outerDoc",nested("%s.outerDoc()",sigI(Doc),3)),
  InnerDoc("innerDoc",nested("%s.innerDoc()",sigI(Doc))),
  NestedNum("nestedNum",nested("%s.nestedNum()",sigI(Int))),
  NestedIn("nestedIn",nested("%s.nestedIn(%s)",sigI(Nested,Int),2)),
  MethodNum("methodNum",nested("%s.methodNum()",sigI(Int))),
  ImplementedNum("implementedNum",nested("%s.implementedNum()",sigI(Int))),
  ImplementedIn("implementedIn",nested("%s.implementedIn(%s)",sigI(Type,Int),2)),
  HasHiddenImplements("hasHiddenImplements",nested("%s.hasHiddenImplements()",sigI(Bool))),
  IsClose("isClose",nested("%s.isClose()",sigI(Bool))),
  IsInterface("isInterface",nested("%s.isInterface()",sigI(Bool))),
  IsBinded("isBinded",nested("%s.isBinded()",sigI(Bool))),
  Root("root",all(
    nested("%s.root()",sigI(Nested)),
    doc("%s.root()",sigI(Nested))
    )),
  NameFromRoot("nameFromRoot",all(
    nested("%s.nameFromRoot()",sigI(Name)),
    doc("%s.nameFromRoot()",sigI(Name))
    )),  
  Position("position",nested("%s.position()",sigI(String))),
  ClassAny("classAny",nested("%s.classAny()",sig(Immutable,Class,Any),3)),
  //####Type####
  Mdf("mdf",type("%s.mdf()",sigI(String))),
  DocOp("doc",type("%s.doc()",sigI(Doc))),
  NestedOp("nested",all(
    type("%s.nested()",sigI(Nested)),
    doc("%s.nested()",sigI(Nested),2)
    )),
  //####Doc####
  DocNum("docNum",doc("%s.docNum()",sigI(Int))),
  DocIn("docIn",doc("%s.docIn(%s)",sigI(Doc,Int),1)),
  TextIn("textIn",doc("%s.textIn(%s)",sigI(String,Int),1)),
  HasAnnotation("hasAnnotation",doc("%s.hasAnnotation()",sigI(Bool))),
  NameOp("name",doc("%s.name()",sigI(Name),2)),
  //####Method####
  //TODO: method operations

  //####VECTOR####
  VectorK("vectorK",Map.of(Vector,vectorKs())),
  IsEmpty("isEmpty",Map.of(Vector,use("return %s.size()==2;",sig(Readable,Immutable,Bool)))),
  Size("size",Map.of(
    Vector,use("return (%s.size()-2)/2;",sig(Readable,Immutable,Int)),
    String,use("return %s.length();",sig(Readable,Immutable,Int))
    )),
  ReadVal("readVal",Map.of(Vector,use(vectorReadGet(),sig(Readable,Readable,Gen1,Immutable,Int)))),
  ImmVal("immVal",Map.of(Vector,use(vectorGet(false),sig(Readable,Immutable,Gen1,Immutable,Int)))),
  HashVal("#val",Map.of(Vector,use(vectorGet(true),sig(Mutable,Mutable,Gen1,Immutable,Int)))),
  SetImm("setImm",Map.of(Vector,use(vectorOp("set",false),sig(Mutable,Immutable,Void,Immutable,Int,Immutable,Gen1)))),
  SetMut("setMut",Map.of(Vector,use(vectorOp("set",true),sig(Mutable,Immutable,Void,Immutable,Int,Mutable,Gen1)))),
  AddImm("addImm",Map.of(Vector,use(vectorOp("add",false),sig(Mutable,Immutable,Void,Immutable,Int,Immutable,Gen1)))),
  AddMut("addMut",Map.of(Vector,use(vectorOp("add",true),sig(Mutable,Immutable,Void,Immutable,Int,Mutable,Gen1)))),  
  Remove("remove",Map.of(Vector,use(vectorOpRemove(),sig(Mutable,Immutable,Void,Immutable,Int)))),
  StartsWith("startsWith",Map.of(String,use("return %s.startsWith(%s);",sigI(Bool,String)))),
  EndsWith("endsWith",Map.of(String,use("return %s.endsWith(%s);",sigI(Bool,String)))),
  SubString("subString",Map.of(String,use("return %s.substring(%s,%s);",sigI(String,Int,Int)))),
  Trim("trim",Map.of(String,use("return %s.trim();",sigI(String)))),
  Plus("OP+",Map.of(
    Int,use("return %s + %s;",sigI(Int,Int)),
    BigRational,use("return %s.sum(%s);",sigI(BigRational,BigRational)),
    String,use("return %s + %s;",sigI(String,String))
    )),
  Times("OP*",Map.of(
    Int,use("return %s * %s;",sigI(Int,Int)),
    BigRational,use("return %s.multiply(%s);",sigI(BigRational,BigRational))
    )),
  Divide("OP/",Map.of(
    Int,use("return %s / %s;",sigI(Int,Int)),
    BigRational,use("return %s.divide(%s);",sigI(BigRational,BigRational))
    )),
  Minus("OP-",Map.of(
    Int,use("return %s - %s;",sigI(Int,Int)),
    BigRational,use("return %s.subtract(%s);",sigI(BigRational,BigRational))
    )),
  LT("OP<",Map.of(
    Int,use("return %s < %s;",sig(Readable,Immutable,Bool,Readable,Int)),
    BigRational,use("return %s.compareTo(%s)==-1;",sig(Readable,Immutable,Bool,Readable,BigRational))
    )),
  LTEqual("OP<=",Map.of(
    Int,use("return %s <= %s;",sig(Readable,Immutable,Bool,Readable,Int)),
    BigRational,use("return %s.compareTo(%s)!=1;",sig(Readable,Immutable,Bool,Readable,BigRational))
    )),
  EqualEqual("OP==",Map.of(
    Int,use("return %s == %s;",sig(Readable, Immutable,Bool, Readable,Int)),
    String,use("return %s.equals(%s);",sig(Readable, Immutable,Bool, Readable,String)),
    Bool,use("return %s == %s;",sig(Readable, Immutable,Bool, Readable,Bool)),
    BigRational,use("return %s.equals(%s);",sig(Readable,Immutable,Bool,Readable,BigRational))
    )),
  Succ("succ",Map.of(Int,use("return %s +1;",sigI(Int)))),
  Pred("pred",Map.of(Int,use("return %s -1;",sigI(Int)))),
  LazyMessageK("lazyMessageK",Map.of(LazyMessage,use("return new L42£LazyMsg(%2$s);",sig(Class,Mutable,LazyMessage,Immutable,String)))),
  SetMsg("setMsg",Map.of(LazyMessage,use("%s.setMsg(%s);return L42£Void.instance;",sig(Mutable,Immutable,Void,Immutable,String)))),
  OptK("optK",Map.of(Opt,use("return (%Gen1)%2$s;",sig(Class,Mutable,This,MutableFwd,Gen1)))), 
  Get("get",  Map.of(Opt,use("""
    if(%1$s!=null){return %1$s;}
    throw new L42Error(%Gen2.wrap(
      new L42£LazyMsg(\"Optional value is empty\")
      ));
    """,sig(Readable,Readable,Gen1)),
    LazyMessage,use("return %s.getMsg();",sig(Readable,Immutable,String))
    )),
  HGet("#get",Map.of(Opt,use("""
    if(%1$s!=null){return %1$s;}
    throw new L42Error(%Gen2.wrap(
      new L42£LazyMsg(\"Optional value is empty\")
      ));
    """,sig(Mutable,Mutable,Gen1)))),
  LazyCache("lazyCache",Map.of(AnyKind,new LazyCacheGenerator())),
  EagerCache("readEagerCache",Map.of(AnyKind,new EagerCacheGenerator())),
  //ClearCache("clearCache",Map.of(AnyKind,new ClearCacheGenerator())),
  MutReferenceEquality("mutReferenceEquality",Map.of(AnyKind,use(
    "return %1$s==%2$s;",sig(Mutable,Immutable,Bool,Mutable,This)))),
  MutStructuralEquality("mutStructuralEquality",Map.of(AnyKind,use(
    "return L42CacheMap.structurallyEqualNoNorm(%1$s,%2$s);",sig(Mutable,Immutable,Bool,Mutable,This)))),
  ImmEquality("immEquality",Map.of(AnyKind,use(
    "return L42CacheMap.structurallyEqualNorm(%1$s,%2$s);",sig(Immutable,Immutable,Bool,Immutable,This)))),
  MutClone("mutClone",Map.of(AnyKind,use(
    "return L42CacheMap.dup(%1$s);",sig(Mutable,Capsule,This)))),
  ReadClone("readClone",Map.of(AnyKind,use(
    "return L42CacheMap.normalizeAndDup(%1$s);",sig(Readable,Immutable,This)))),  
  ImmNorm("immNorm",Map.of(AnyKind,use(
    "return L42CacheMap.normalize(%1$s);",sig(Immutable,Immutable,This)))),    
  MutToString("mutToString",Map.of(
    AnyKind,use("return L42CacheMap.objToString(%1$s);",sig(Mutable,Immutable,String)),
    AnyNativeKind,use("return L42CacheMap.objToString(%This.wrap(%1$s));",sig(Mutable,Immutable,String))
    )),
  ImmToString("immToString",Map.of(
    AnyKind,use("return L42CacheMap.objToString(L42CacheMap.normalize(%1$s));",sig(Immutable,Immutable,String)),
    AnyNativeKind,use("return L42CacheMap.objToString(L42CacheMap.normalize(%This.wrap(%1$s)));",sig(Immutable,Immutable,String))
    ))
  ;
  public final String inner;
  Map<TrustedKind,Generator>code;
  public Generator _of(TrustedKind k){
    assert k!=null;
    var res=code.get(k);
    if(res==null){res=code.get(TrustedKind.AnyNativeKind);}
    if(res==null){res=code.get(TrustedKind.AnyKind);}
    return res;
    }
  TrustedOp(String inner,Map<TrustedKind,Generator>code){
    this.inner = inner;
    this.code=code;
    }
  public static TrustedOp fromString(String s) {
   for (TrustedOp t : TrustedOp.values()) {
    if (t.inner.equals(s))return t;
    }
   assert false:
   "#"+s+"#";
   throw todo();
  }
 }