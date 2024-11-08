package is.L42.nativeCode;

import static is.L42.generated.Mdf.*;
import static is.L42.nativeCode.OpUtils.*;
import static is.L42.nativeCode.Signature.sig;
import static is.L42.nativeCode.Signature.sigI;
import static is.L42.nativeCode.TT.*;
import static is.L42.nativeCode.TrustedKind.*;
import static is.L42.tools.General.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import is.L42.flyweight.P;
import is.L42.generated.Core.Info;
import is.L42.generated.Core.MWT;
import is.L42.generated.Core.T;
    
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
  SPercent("'%'",append("%%")),//%% instead of % for the java stringformat
  SBackSlash("'\\'",append("\\\\")),
  SSpace("space",append(" ")),
  SNewLine("newLine",append("\\n")),
  STab("tab",append("\\t")),
  AddAll("addAll",Map.of(StringBuilder,use("%s.append(%s);return L42£Void.instance;",
    sig(Mutable,Immutable,Void,Immutable,String)))),
  FromNum("fromNum",Map.of(
    Int,use("return (int)(%2$s.doubleValue());",sig(Class,Immutable,Int,Immutable,BigRational)),
    Double,use("return %2$s.doubleValue();",sig(Class,Immutable,Double,Immutable,BigRational)),
    Long,use("return (long)(%2$s.doubleValue());",sig(Class,Immutable,Long,Immutable,BigRational))
    )),
  ToS("toS",useToS(StringBuilder,Int,Long,Double,Bool,Name,Nested,Doc,Type,Method,BigRational)),//String is pre added
  ToInt("toInt",Map.of(String,use("""
    try{return Integer.parseInt(%1$s);}
    catch(NumberFormatException nfe){
      throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
        "The string \\""+%1$s+"\\" is not a valid int32 number"
        )));
      }
    """,sigI(Int)
    ))),
  ToLong("toLong",Map.of(String,use("""
      try{return Long.parseLong(%1$s);}
      catch(NumberFormatException nfe){
        throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
          "The string \\""+%1$s+"\\" is not a valid int64 number"
          )));
        }
      """,sigI(Long)
      ))),
  ToDouble("toDouble",Map.of(String,use("""
      try{return Double.parseDouble(%1$s);}
      catch(NumberFormatException nfe){
        throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
          "The string \\""+%1$s+"\\" is not a valid double number"
          )));
        }
      """,sigI(Double)
      ))),  
  ToNum("toNum",Map.of(String,use("""
    try{return L42£BigRational.from(%1$s);}
    catch(NumberFormatException nfe){
      throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
        "The string \\""+%1$s+"\\" is not a valid number"
        )));
      }
    """,sigI(BigRational)
    ))),
  //other toNum also in String, so that they can throw NumberFormatException
  DoubleToNum("doubleToNum",Map.of(String,use("""
    try{return L42£BigRational.from(%2$s);}
    catch(NumberFormatException nfe){
      throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
        "The double "+%2$s+" is not a valid number"
        )));
      }
    """,sig(Class,Immutable,BigRational,Immutable,Double)
    ))),
  LongToNum("longToNum",Map.of(String,use("""
    try{return L42£BigRational.from(%2$s,%3$s);}
    catch(NumberFormatException nfe){
      throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
        "The longs "+%2$s+"/"+%3$s+" are not a valid number"
        )));
      }
    """,sig(Class,Immutable,BigRational,Immutable,Long,Immutable,Long)
    ))),
  IntToNum("intToNum",Map.of(String,use("""
    try{return L42£BigRational.from((long)%2$s,(long)%3$s);}
    catch(NumberFormatException nfe){
        throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
          "The ints "+%2$s+"/"+%3$s+" are not a valid number"
          )));
        }
    """,sig(Class,Immutable,BigRational,Immutable,Int,Immutable,Int)
    ))),
  ToName("toName",Map.of(String,use("""
    try{return L42£Name.parse(%1$s);}
    catch(NumberFormatException nfe){
      throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
        "The string \\""+%1$s+"\\" is not a valid name"
        )));
      }
    """,sigI(Name)
    ))),
  X("x",Map.of(Name,use("return %s.x();",sig(Readable,Immutable,String)))),
  Selector("selector",Map.of(Name,use("return %s.selector();",sig(Readable,Immutable,String)))),
  Path("path",Map.of(Name,use("return %s.path();",sig(Readable,Immutable,String)))),
  WithPath("withPath",Map.of(Name,use("return %s.withPath(%s);",sig(Readable,Immutable,Name,Immutable,String)))),
  WithSelector("withSelector",Map.of(Name,use("return %s.withSelector(%s);",sig(Readable,Immutable,Name,Immutable,String)))),
  WithX("withX",Map.of(Name,use("return %s.withX(%s);",sig(Readable,Immutable,Name,Immutable,String)))),
  //TRUSTEDIO
  StrDebug("strDebug",Map.of(
    String,use("Resources.out(%s); return L42£Void.instance;",sigI(Void)),
    TrustedIO,use("return %s.strDebug(%s);",sigI(Void,String))
    )),
  AddToLog("addToLog",trustedIO(
    "return %s.addToLog(%s,%s);",sigI(Void,String,String))),
  ClearLog("clearLog",trustedIO(
    "return %s.clearLog(%s);",sigI(Void,String))),
  ReadLog("#$readLog",trustedIO(
    "return %s.readLog(%s);",sigI(String,String))),
  StopCacheEager("#$stopCacheEager",trustedIO(
      "is.L42.cache.concurrency.CachedRes.killAll();return L42£Void.instance;",sigI(Void))),  
  SystemMutReferenceEquality("mutReferenceEquality",trustedIO(
    "return %2$s==%3$s;",
    sig(Immutable,Immutable,Bool,Mutable,Any,Readable,Any))),
  SystemMutStructuralEquality("mutStructuralEquality",trustedIO(
    "return L42CacheMap.structurallyEqualNoNorm(%2$s,%3$s);",
    sig(Immutable,Immutable,Bool,Mutable,Any,Readable,Any))),
  SystemImmEquality("immEquality",trustedIO(
    "return L42CacheMap.structurallyEqualNorm(%2$s,%3$s);",
    sig(Immutable,Immutable,Bool,Immutable,Any,Immutable,Any))),
  SystemCapsuleClone("capsuleClone",trustedIO(
    "return L42CacheMap.dup(%2$s);",//Note: any way to turn a read into a capsule would violate object capability restrictions
    sig(Immutable,Capsule,Any,Mutable,Any))),
  SystemImmClone("immClone",trustedIO(
    "return L42CacheMap.dupAndNormalize(%2$s);",
    sig(Immutable,Immutable,Any,Readable,Any))),  
  SystemImmNorm("immNorm",trustedIO(
    "return L42CacheMap.normalize(%2$s);",
    sig(Immutable,Immutable,Any,Immutable,Any))),    
  SystemImmToString("immToString",trustedIO(
    "return L42CacheMap.objToString(L42CacheMap.normalize(%2$s));",
    sig(Immutable,Immutable,String,Immutable,Any))),
  TestCondition("testCondition",Map.of(
  //Library pos,This1.S name,B,This1.S message
    TrustedIO,use("return %s.testCondition(%s,%s,%s,%s);",sigI(Void,
      Lib,String,Bool,String))
    )),
  TestActualExpected("testActualExpected",Map.of(
    TrustedIO,use("return %s.testActualExpected(%s,%s,%s,%s,%s,%s);",sigI(Void,
      Lib,String,String,String,String,String))
    )),
  //####META####
  DeployLibrary("#$deployLibrary",Map.of(
    Meta,use("return %s.deployLibrary(%s,%s,%s,%Gen1::wrap);",sigI(Void,String,String,Lib)))),
  DeployLibraryToBase64("deployLibraryToBase64",Map.of(
    Meta,use("return %s.deployLibraryToBase64(%s,%s,%Gen1::wrap);",sigI(String,String,Lib)))),      
  DeployJarToBase64("deployJarToBase64",Map.of(
      Meta,use("return %s.deployJarToBase64(%s,%s,%Gen1::wrap);",sigI(String,String,Lib)))),

  SimpleSum("simpleSum",Map.of(
    Meta,use("return %s.simpleSum(%s,%s,%Gen1::wrap,%Gen2::wrap);",sigI(Lib,Lib,Lib)))),
  Resource("resource",Map.of(
    Meta,use("return %s.resource(%s,%s);",sigI(Lib,Lib,Name)))),
  Close("close",Map.of(
    Meta,use("return %s.close(%s,%s,%s,%Gen1::wrap);",sigI(Lib,Lib,String,Bool)))),
  Defaults("defaults",Map.of(
    Meta,use("return %s.defaults(%s,%s,%Gen1::wrap);",sigI(Lib,Lib,String)))),  
  AddConstructors("addConstructors",Map.of(
    Meta,use("return %s.addConstructors(%s,%s,%s,%Gen1::wrap,%s,%s);",sigI(Lib,Lib,String,Bool,String,String)))),
  CacheCall("cacheCall",Map.of(
    Meta,use("return %s.cacheCall(%s,%Gen1::wrap);",sigI(Lib,Lib)))),
  ResetDocs("resetDocs",Map.of(
    Meta,use("return %s.resetDocs(%s,%s,%Gen1::wrap);",sigI(Lib,Lib,HIMap)))),
  NativeSlaveRename("nativeSlaveRename",Map.of(
      Meta,use("return %s.nativeSlaveRename(%s,%s,%s,%Gen1::wrap);",sigI(Lib,Lib,String,String)))),
  RemoveUnusedCode("removeUnusedCode",Map.of(
      Meta,use("return %s.removeUnusedCode(%s);",sigI(Lib,Lib)))),
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
  MergeMapDeep("mergeMapDeep",Map.of(
    Meta,use("return %s.mergeMapDeep(%s);",sigI(Meta,Meta)))),
  ApplyMap("applyMap",Map.of(
    Meta,use("return %s.applyMap(%s,%Gen3::wrap,%Gen4::wrap,%Gen1::wrap,%Gen2::wrap);",sigI(Lib,Lib)))),
  PathName("pathName",Map.of(
    Meta,use("return %s.pathName(%s);",sig(Immutable,Immutable,String,
      Class,Any))
    )),
  Sifo("sifo",Map.of(
      Meta,use("return %s.sifo(%s,%s,%Gen1::wrap);",sig(Immutable,Immutable,Lib,Immutable,Lib,Class,Any))
      )),
  IsSelfRename("isSelfRename",Map.of(
      Meta,use("return %s.isSelfRename();",sigI(Bool)))),
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
  MethodIn("methodIn",nested("%s.methodIn(%s)",sigI(Method,Int))),
  ImplementedNum("implementedNum",nested("%s.implementedNum()",sigI(Int))),
  ImplementedIn("implementedIn",nested("%s.implementedIn(%s)",sigI(Type,Int),2)),
  HasHiddenImplements("hasHiddenImplements",nested("%s.hasHiddenImplements()",sigI(Bool))),
  IsClose("isClose",nested("%s.isClose()",sigI(Bool))),
  IsInterface("isInterface",nested("%s.isInterface()",sigI(Bool))),
  IsBinded("isBinded",nested("%s.isBinded()",sigI(Bool))),
  IsCoherent("isCoherent",nested("%s.isCoherent()",sigI(String))),
  ToFullString("toFullString",nested("%s.toFullString()",sigI(String))),
  Root("root",all(
    nested("%s.root()",sigI(Nested)),
    doc("%s.root()",sigI(Nested))
    )),
  NameFromRoot("nameFromRoot",all(
    nested("%s.nameFromRoot()",sigI(Name)),
    doc("%s.nameFromRoot()",sigI(Name)),
    method("%s.nameFromRoot()",sigI(Name))
    )),  
  Position("position",all(
    nested("%s.position()",sigI(String)),
    method("%s.position()",sigI(String))    
    )),
  ClassAny("classAny",nested("%s.classAny()",sig(Immutable,Class,Any),3)),
  //####Type####
  Mdf("mdf",type("%s.mdf()",sigI(String))),
  DocOp("doc",all(
    type("%s.doc()",sigI(Doc)),
    method("%s.doc()",sigI(Doc))
    )),
  NestedOp("nested",all(
    type("%s.nested()",sigI(Nested)),
    doc("%s.nested()",sigI(Nested),2),
    method("%s.nested()",sigI(Nested))
    )),
  //####Doc####
  DocNum("docNum",doc("%s.docNum()",sigI(Int))),
  DocIn("docIn",doc("%s.docIn(%s)",sigI(Doc,Int),1)),
  TextIn("textIn",doc("%s.textIn(%s)",sigI(String,Int),1)),
  HasAnnotation("hasAnnotation",doc("%s.hasAnnotation()",sigI(Bool))),
  NameOp("name",doc("%s.name()",sigI(Name),2)),
  //####Method####
  ReturnType("returnType",method("%s.returnType()",sigI(Type),1)),
  ParNum("parNum",method("%s.parNum()",sigI(Int))),
  ParIn("parIn",method("%s.parIn(%s)",sigI(Type,Int),1)),
  ExcNum("excNum",method("%s.excNum()",sigI(Int))),
  ExcIn("excIn",method("%s.excIn(%s)",sigI(Type,Int),1)),
  IsRefined("isRefined",method("%s.isRefined()",sigI(Bool))),
  IsAbstract("isAbstract",method("%s.isAbstract()",sigI(Bool))),
  //####List####
  ListK("listK",Map.of(
    List,listKs(false),
    SelfList,listKs(true)
    )),
  IsEmpty("isEmpty",Map.of(
    List,use("return %s.size()==1;",sig(Readable,Immutable,Bool)),
    SelfList,use("return %s.size()==1;",sig(Readable,Immutable,Bool))
    )),
  Size("size",Map.of(
    List,use("return (%s.size()-1)/2;",sig(Readable,Immutable,Int)),
    SelfList,use("return (%s.size()-1)/2;",sig(Readable,Immutable,Int)),
    HIMap,use("return  %s.size();",sig(Readable,Immutable,Int)),
    HMMap,use("return  %s.size();",sig(Readable,Immutable,Int)),
    HSet,use("return  %s.size();",sig(Readable,Immutable,Int)),
    String,use("return %s.length();",sig(Readable,Immutable,Int))
    )),
  isMutVal("isMutVal",Map.of(
      List,use(listOpIsMut(),sig(Readable,Immutable,Bool,Immutable,Int)),
      SelfList,use(listOpIsMut(),sig(Readable,Immutable,Bool,Immutable,Int))
      )),
  ReadVal("readVal",Map.of(
    List,use(listReadGet(),sig(Readable,Readable,Gen1,Immutable,Int)),
    SelfList,use(listReadGet(),sig(Readable,Readable,Gen1,Immutable,Int)),
    HMMap,use(mapOutOfBound(
      "return %s.valIndex(%s);","return %s.valIndex(%s).unwrap;"
      ),sig(Readable,Readable,Gen2,Immutable,Int))
    )),
  ImmVal("immVal",Map.of(
    List,use(listGet(false,false),sig(Readable,Immutable,Gen1,Immutable,Int)),
    SelfList,use(listGet(false,true),sig(Readable,Immutable,Gen1,Immutable,Int)),
    HIMap,use(mapOutOfBound(
      "return %s.valIndex(%s);","return %s.valIndex(%s).unwrap;"
      ),sig(Readable,Immutable,Gen2,Immutable,Int))
    )),
  HashVal("#val",Map.of(
    List,use(listGet(true,false),sig(Mutable,Mutable,Gen1,Immutable,Int)),
    SelfList,use(listGet(true,true),sig(Mutable,Mutable,Gen1,Immutable,Int)),
    HMMap,use(mapOutOfBound(
      "return %s.valIndex(%s);","return %s.valIndex(%s).unwrap;"
      ),sig(Mutable,Mutable,Gen2,Immutable,Int))
    )), 
  SetImm("setImm",Map.of(
    List,use(listOp("set",false,false),sig(Mutable,Immutable,Void,Immutable,Int,Immutable,Gen1)),
    SelfList,use(listOp("set",false,true),sig(Mutable,Immutable,Void,Immutable,Int,Immutable,Gen1))
    )),
  SetMut("setMut",Map.of(
    List,use(listOp("set",true,false),sig(Mutable,Immutable,Void,Immutable,Int,Mutable,Gen1)),
    SelfList,use(listOp("set",true,true),sig(Mutable,Immutable,Void,Immutable,Int,Mutable,Gen1))
    )),
  AddImm("addImm",Map.of(
    List,use(listOp("add",false,false),sig(Mutable,Immutable,Void,Immutable,Int,Immutable,Gen1)),
    SelfList,use(listOp("add",false,true),sig(Mutable,Immutable,Void,Immutable,Int,Immutable,Gen1))
    )),
  AddMut("addMut",Map.of(
    List,use(listOp("add",true,false),sig(Mutable,Immutable,Void,Immutable,Int,Mutable,Gen1)),
    SelfList,use(listOp("add",true,true),sig(Mutable,Immutable,Void,Immutable,Int,Mutable,Gen1))
    )),
  Remove("remove",Map.of(
    List,use(listOpRemove(),sig(Mutable,Immutable,Void,Immutable,Int)),
    SelfList,use(listOpRemove(),sig(Mutable,Immutable,Void,Immutable,Int))
    )),
  //MAPs
  ImmKey("immKey",Map.of(
      HIMap,use(mapOutOfBound("return %s.keyIndex(%s);"),sig(Readable,Immutable,Gen1,Immutable,Int)),
      HMMap,use(mapOutOfBound("return %s.keyIndex(%s);"),sig(Readable,Immutable,Gen1,Immutable,Int)),
      HSet,use(setOutOfBound("return %s.keyIndex(%s);"),sig(Readable,Immutable,Gen1,Immutable,Int))
      )),
  MapVal("mapVal",Map.of(
      HIMap,use("return %s.val(%s);",sig(Readable,Immutable,Gen3,Immutable,Gen1)),
      HMMap,use("return %s.val(%s);",sig(Readable,Readable,Gen3,Immutable,Gen1))
      )),
  HashMapVal("#mapVal",Map.of(
      HMMap,use("return %s.val(%s);",sig(Mutable,Mutable,Gen3,Immutable,Gen1))
      )),
  Put("put",Map.of(
      HIMap,use(mapChoice(
        "%s.put(%s,%s);return L42£Void.instance;","%s.put(%s,%Gen2.wrap(%s));return L42£Void.instance;"
        ),sig(Mutable,Immutable,Void,Immutable,Gen1,Immutable,Gen2)),
      HMMap,use(mapChoice(
        "%s.put(%s,%s);return L42£Void.instance;","%s.put(%s,%Gen2.wrap(%s));return L42£Void.instance;"
        ),sig(Mutable,Immutable,Void,Immutable,Gen1,Mutable,Gen2)),
      HSet,use("%s.add(%s);return L42£Void.instance;",sig(Mutable,Immutable,Void,Immutable,Gen1))
      )),
  RemoveKey("removeKey",Map.of(
      HIMap,use("%s.remove(%s);return L42£Void.instance;",sig(Mutable,Immutable,Void,Immutable,Gen1)),
      HMMap,use("%s.remove(%s);return L42£Void.instance;",sig(Mutable,Immutable,Void,Immutable,Gen1)),
      HSet,use("%s.remove(%s);return L42£Void.instance;",sig(Mutable,Immutable,Void,Immutable,Gen1))
      )),

  //val put remove
  
  
  //String
  EncodeBase64("encodeBase64",Map.of(String,use(
    "return Resources.encoder.encodeToString(%s.getBytes())"+
    ".replace(\"\\r\",\"\").replace(\"\\n\",\"\");",
    sigI(String)))),
  DecodeBase64("decodeBase64",Map.of(String,use(
      "return Resources.sanitizeJavaRes(new String(Resources.decoder.decode(%s)));",
      sigI(String)))),
  StartsWith("startsWith",Map.of(String,use("return %s.startsWith(%s);",sigI(Bool,String)))),
  EndsWith("endsWith",Map.of(String,use("return %s.endsWith(%s);",sigI(Bool,String)))),
  StartsWithOff("startsWithOff",Map.of(String,use("return %s.startsWith(%s,%s);",sigI(Bool,String,Int)))),
  EndsWithOff("endsWithOff",Map.of(String,use("""
    String all=%s;
    String sub=%s;
    int index=%s;
    if(index<0 || index>all.length()){return false;}
    String subS=all.substring(0,all.length()-index);
    return subS.endsWith(sub);
    """,sigI(Bool,String,Int)))),
  SubString("subString",Map.of(String,use("""
    try{return %1$s.substring(%2$s,%3$s);}
    catch(StringIndexOutOfBoundsException nfe){
      throw new L42Error(%Gen1.wrap(new L42£LazyMsg(
        "SubString out of bound: start="+%2$s+", end="+%3$s+", size="+%1$s.length()
        )));
      }
    """,sigI(String,Int,Int)
    ))),  
  Contains("contains",Map.of(
    String,use("return %s.contains(%s);",sigI(Bool,String)),
    HSet,use("return %s.contains(%s);",sig(Readable,Immutable,Bool,Immutable,Gen1))
    )),
  Replace("replace",Map.of(String,use("return %s.replace(%s,%s);",sigI(String,String,String)))),
  IndexOf("indexOf",Map.of(String,use("return %s.indexOf(%s,%s);",sigI(Int,String,Int)))),
  LastIndexOf("lastIndexOf",Map.of(String,use("return %s.lastIndexOf(%s,%s);",sigI(Int,String,Int)))),
  Trim("trim",Map.of(String,use("return %s.trim();",sigI(String)))),
  RandomNextSeed("randomNextSeed",Map.of(Long,use(//same algorithm of java.utils.Random in 2020. Should not matter if it changes
    "return (%s * 0x5DEECE66DL + 0xBL) & ((1L<<48)-1);",
    sigI(Long)))),
  RandomNextLong("randomNextLong",Map.of(Long,use(
    "return new java.util.Random(%s).nextLong();",
    sigI(Long)))),
  RandomNextDouble("randomNextDouble",Map.of(Long,use(
    "return new java.util.Random(%s).nextDouble();",
    sigI(Double)))),
  RandomNextSize("randomNextSize",Map.of(Long,use(
    "return new java.util.Random(%s).nextInt(%s);",
    sigI(Int,Int)))),
  RandomSeed("#$randomSeed",Map.of(Long,use(
      "return new java.util.Random().nextLong();",
      sig(Class,Immutable,Long)))),
  Plus("OP+",Map.of(
    Int,use("return %s + %s;",sigI(Int,Int)),
    Long,use("return %s + %s;",sigI(Long,Long)),
    Double,use("return %s + %s;",sigI(Double,Double)),
    BigRational,use("return %s.sum(%s);",sigI(BigRational,BigRational)),
    String,use("return %s + %s;",sigI(String,String))
    )),
  Times("OP*",Map.of(
    Int,use("return %s * %s;",sigI(Int,Int)),
    Long,use("return %s * %s;",sigI(Long,Long)),
    Double,use("return %s * %s;",sigI(Double,Double)),
    BigRational,use("return %s.multiply(%s);",sigI(BigRational,BigRational))
    )),
  Divide("OP/",Map.of(
    Int,use("""
      try{return %s/%s;}catch(ArithmeticException ae)
      { throw new L42Error(%Gen1.wrap(new L42£LazyMsg("Division by zero"))); }
      """,sigI(Int,Int)),
    Long,use("""
      try{return %s/%s;}catch(ArithmeticException ae)
      { throw new L42Error(%Gen1.wrap(new L42£LazyMsg("Division by zero"))); }
      """,sigI(Long,Long)),
    
    Double,use("return %s / %s;",sigI(Double,Double)),
    BigRational,use("""
        try{return %s.divide(%s);}catch(NumberFormatException nfe)
        { throw new L42Error(%Gen1.wrap(new L42£LazyMsg(nfe.getMessage()))); }
        """,sigI(BigRational,BigRational))
    )),
  Minus("OP-",Map.of(
    Int,use("return %s - %s;",sigI(Int,Int)),
    Long,use("return %s - %s;",sigI(Long,Long)),
    Double,use("return %s - %s;",sigI(Double,Double)),
    BigRational,use("return %s.subtract(%s);",sigI(BigRational,BigRational))
    )),
  Mod("mod",Map.of(
      Int,use("return %s %% %s;",sigI(Int,Int)),
      Long,use("return %s %% %s;",sigI(Long,Long))
      )),
  Signum("signum",Map.of(
      Int,use("return Integer.signum(%s);",sigI(Int)),
      Long,use("return Long.signum(%s);",sigI(Int))
      )),
  MathPow("mathPow",Map.of(Double,use("return Math.pow(%s,%s);",sigI(Double,Double)))),
  MathSin("mathSin",Map.of(Double,use("return Math.sin(%s);",sigI(Double)))),
  MathCos("mathCos",Map.of(Double,use("return Math.cos(%s);",sigI(Double)))),
  MathTan("mathTan",Map.of(Double,use("return Math.tan(%s);",sigI(Double)))),
  MathATan2("mathATan2",Map.of(Double,use("return Math.atan2(%s,%s);",sigI(Double,Double)))),
  MathFloorLong("mathFloorLong",Map.of(Double,use("return (long)Math.floor(%s);",sigI(Long)))),
  MathFloorInt("mathFloorInt",Map.of(Double,use("return (int)Math.floor(%s);",sigI(Int)))),
  MathCeilLong("mathCeilLong",Map.of(Double,use("return (long)Math.ceil(%s);",sigI(Long)))),
  MathCeilInt("mathCeilInt",Map.of(Double,use("return (int)Math.ceil(%s);",sigI(Int)))),
  MathRoundLong("mathRoundLong",Map.of(Double,use("return (long)Math.round(%s);",sigI(Long)))),
  MathRoundInt("mathRoundInt",Map.of(Double,use("return (int)Math.round(%s);",sigI(Int)))),
  IntToDouble("intToDouble",Map.of(Int,use("return (double)%s;",sigI(Double)))),
  IntToLong("intToLong",Map.of(Int,use("return (long)%s;",sigI(Long)))),
  LongToDouble("longToDouble",Map.of(Long,use("return (double)%s;",sigI(Double)))),
  LongToInt("longToInt",Map.of(Long,use("return (int)%s;",sigI(Int)))),

  LT("OP<",Map.of(
    Int,use("return %s < %s;",sigI(Bool,Int)),
    Long,use("return %s < %s;",sigI(Bool,Long)),
    Double,use("return %s < %s;",sigI(Bool,Double)),
    BigRational,use("return %s.compareTo(%s)==-1;",sigI(Bool,BigRational))
    )),
  LTEqual("OP<=",Map.of(
    Int,use("return %s <= %s;",sigI(Bool,Int)),
    Long,use("return %s <= %s;",sigI(Bool,Long)),
    Double,use("return %s <= %s;",sigI(Bool,Double)),
    BigRational,use("return %s.compareTo(%s)!=1;",sigI(Bool,BigRational))
    )),
  EqualEqual("OP==",Map.of(
    Int,use("return %s == %s;",sig(Readable, Immutable,Bool, Readable,Int)),
    Long,use("return %s == %s;",sig(Readable, Immutable,Bool, Readable,Long)),
    Double,use("return %s == %s;",sig(Readable, Immutable,Bool, Readable,Double)),
    String,use("return %s.equals(%s);",sig(Readable, Immutable,Bool, Readable,String)),
    Bool,use("return %s == %s;",sig(Readable, Immutable,Bool, Readable,Bool)),
    BigRational,use("return %s.equals(%s);",sig(Readable,Immutable,Bool,Readable,BigRational))
    )),
  Succ("succ",Map.of(
    Int,use("return %s +1;",sigI(Int)),
    Long,use("return %s +1;",sigI(Long)),
    Double,use("return %s +1;",sigI(Double)),
    BigRational,use("return %s.sum(L42£BigRational.ONE);",sigI(BigRational))
    )),
  Pred("pred",Map.of(
    Int,use("return %s -1;",sigI(Int)),
    Long,use("return %s -1;",sigI(Long)),
    Double,use("return %s -1;",sigI(Double)),
    BigRational,use("return %s.subtract(L42£BigRational.ONE);",sigI(BigRational))
    )),
  LazyMessageK("lazyMessageK",Map.of(LazyMessage,use("return new L42£LazyMsg(%2$s);",sig(Class,Mutable,LazyMessage,Immutable,String)))),
  NonDeterministicErrorK("nonDeterministicErrorK",Map.of(NonDeterministicError,use("return new L42£NonDeterministicError(%2$s);",sig(Class,Mutable,NonDeterministicError,Immutable,String)))),
  SetMsg("setMsg",Map.of(
    LazyMessage,use("%s.setMsg(%s);return L42£Void.instance;",sig(Mutable,Immutable,Void,Immutable,String)),
    NonDeterministicError,use("%s.setMsg(%s);return L42£Void.instance;",sig(Mutable,Immutable,Void,Immutable,String))
      )),
  OptK("optK",Map.of(Opt,use(optChoice(
    preOptK+" (%Gen1)%2$s"+postOptK,
    preOptK+" %Gen1.wrap((%Gen1)%2$s)"+postOptK
    ),
    sig(Class,Mutable,This,MutableFwd,Gen1)
    ))),
  Get("get",  Map.of(Opt,use(optChoice("""
    if(%1$s!=null){return %1$s;}
    throw new L42Error(%Gen2.wrap(new L42£LazyMsg(\"Optional value is empty\")));
    ""","""
    if(%1$s!=null){return %1$s.unwrap;}
    throw new L42Error(%Gen2.wrap(new L42£LazyMsg(\"Optional value is empty\")));    
    """),
    sig(Readable,Readable,Gen1)),
    LazyMessage,use("return %s.getMsg();",sig(Readable,Immutable,String)),
    NonDeterministicError,use("return %s.getMsg();",sig(Readable,Immutable,String))
    )),
  HGet("#get",Map.of(Opt,use(optChoice("""
    if(%1$s!=null){return %1$s;}
    throw new L42Error(%Gen2.wrap(new L42£LazyMsg(\"Optional value is empty\")));
    ""","""
    if(%1$s!=null){return %1$s.unwrap;}
    throw new L42Error(%Gen2.wrap(new L42£LazyMsg(\"Optional value is empty\")));    
    """),
    sig(Mutable,Mutable,Gen1)))),
  IsPresent("isPresent",Map.of(Opt,use("return %1$s!=null;",sig(Readable,Immutable,Bool)))),
  CacheLazy("lazyCache",Map.of(AnyKind,new CacheLazyGenerator())),
  CacheEager("eagerCache",Map.of(AnyKind,new CacheEagerGenerator())),
  CacheNow("readNowCache",Map.of(AnyKind,new CacheNowGenerator())),
  ForkJoin("forkJoin",Map.of(AnyKind,new ForkJoinGenerator())),
  ClearCache("invalidateCache",Map.of(AnyKind,new InvalidateCacheGenerator())),
  ;
  public final String inner;
  final Map<TrustedKind,Generator>code;
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
  @SuppressWarnings("unused")
  public List<P.NCs> nativeMustWatch(MWT mwt,Info info){
    if(true) {return L();}//TODO:
    /*
    nativeMustWatch(_) for now return empty,
    otherwise a library can not define new native
    (like List/Map/Opt)
    Likelly, in the future it will stay empty, but
    the native code generation will make possible to map
    arbitrary types to natives, so another towel can define
    a non native S or Bool and we can still load a library using
    an Opt (that used to watch Bool) onto a non native bool
    */
    if(info.nativeKind().isEmpty()){return L();}
    var c=new ArrayList<P.NCs>();
    addT(c,mwt.mh().t());
    for(T t:mwt.mh().pars()){addT(c,t);}
    for(T t:mwt.mh().exceptions()){addT(c,t);}
    c.removeIf(p->p.hasUniqueNum());
    if(info.nativePar().isEmpty()){return andParsFrom(c,info,0);}//to avoid out of order errors
    if(!java.util.List.of("List","SelfList","Opt","HSet","SelfHSet","HIMap","HMMap").contains(info.nativeKind())){return andParsFrom(c,info,0);} 
    c.removeIf(p->p.equals(info.nativePar().get(0)));//remove only removes the first occurence
    if(info.nativePar().size()<2 || !java.util.List.of("HIMap","HMMap").contains(info.nativeKind())){return andParsFrom(c,info,1);}
    c.removeIf(p->p.equals(info.nativePar().get(1)));//remove only removes the first occurence
    return andParsFrom(c,info,2);
    }
  private ArrayList<P.NCs> andParsFrom(ArrayList<P.NCs>c,Info info,int index){
    for(P p:info.nativePar()){
      if(p.isNCs() && index<=0){c.add(p.toNCs());}
      index-=1;
      }
    return c;
    }
  private static void addT(ArrayList<P.NCs>c,T t){
    if(t.p().isNCs()){c.add(t.p().toNCs());}
    }
  public static TrustedOp _fromString(String s) {
   for (TrustedOp t : TrustedOp.values()) {
    if (t.inner.equals(s))return t;
    }
   return null;
   }
 }