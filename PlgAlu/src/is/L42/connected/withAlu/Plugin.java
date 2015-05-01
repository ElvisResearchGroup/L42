package is.L42.connected.withAlu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import facade.L42;
import ast.Ast;
import ast.Ast.Path;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.*;
import static auxiliaryGrammar.EncodingHelper.*;
import auxiliaryGrammar.Program;

public class Plugin implements PluginType {
  /*@Override
  public Object dispatch(boolean t,Program p, Using u) {
    ExpCore _1=u.getEs().size()<1?null:u.getEs().get(0);
    ExpCore _2=u.getEs().size()<2?null:u.getEs().get(1);
    //ExpCore _3=u.getEs().size()<3?null:u.getEs().get(2);
    switch(composedName(u)){
      case "sumInt32 n1 n2 ":return t?MsumInt32£n1£n2:MsumInt32£n1£n2(_1,_2);
      case "mulInt32 n1 n2 ":return t?MmulInt32£n1£n2:MmulInt32£n1£n2(_1,_2);
      case "divInt32 n1 n2 ":return t?MdivInt32£n1£n2:MdivInt32£n1£n2(_1,_2);
      case "subInt32 n1 n2 ":return t?MsubInt32£n1£n2:MsubInt32£n1£n2(_1,_2);
      case "ifInt32EqualDo n1 n2 ":return t?MifInt32EqualDo£n1£n2:MifInt32EqualDo£n1£n2(_1,_2);
      case "ifInt32GrtDo n1 n2 ":return t?MifInt32GrtDo£n1£n2:MifInt32GrtDo£n1£n2(_1,_2);
      case "ifInt32GEqDo n1 n2 ":return t?MifInt32GEqDo£n1£n2:MifInt32GEqDo£n1£n2(_1,_2);
      case "stringToInt32 that ":return t?MstringToInt32£that:MstringToInt32£that(_1);
      case "int32ToString that ":return t?Mint32ToString£that:Mint32ToString£that(_1);
      case "stringDebug that ":return t?MstringDebug£that:MstringDebug£that(_1);
      case "fileDebug fileName content ":return t?MfileDebug£fileName£content:MfileDebug£fileName£content(_1,_2);
      case "stringConcat s1 s2 ":return t?MstringConcat£s1£s2:MstringConcat£s1£s2(_1,_2);
      
      case "stringSize that ":return t?MstringSize£that:MstringSize£that(_1);
      case "stringCharAt that pos ":return t?MstringCharAt£that£pos:MstringCharAt£that£pos(_1,_2);
      case "ifStringEqualDo s1 s2 ":return t?MifStringEqualDo£s1£s2:MifStringEqualDo£s1£s2(_1,_2);
      default:throw new ErrorMessage.PluginMethodUndefined(u, p.getInnerData());
    }
  }*/
  // +
  private static Ast.MethodType MsumInt32£n1£n2=mt(Path.Library(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MsumInt32£n1£n2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    return i1+i2;
  }
  //*
  private static Ast.MethodType MmulInt32£n1£n2=mt(Path.Library(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MmulInt32£n1£n2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    return i1*i2;
  }
  // /
  private static Ast.MethodType MdivInt32£n1£n2=mt(Path.Library(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MdivInt32£n1£n2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    return i1/i2;
  }
  //-
  private static Ast.MethodType MsubInt32£n1£n2=mt(Path.Library(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MsubInt32£n1£n2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    return i1-i2;
  }
  //==
  private static Ast.MethodType MifInt32EqualDo£n1£n2=mt(Path.Void(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public  Resources.Void MifInt32EqualDo£n1£n2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    if(!i1.equals(i2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
  //<
  private static Ast.MethodType MifInt32GrtDo£n1£n2=mt(Path.Void(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public  Resources.Void MifInt32GrtDo£n1£n2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    if(!(i1<i2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
  //<=
  private static Ast.MethodType MifInt32GEqDo£n1£n2=mt(Path.Void(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public  Resources.Void MifInt32GEqDo£n1£n2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    if(!(i1<=i2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }

  private static Ast.MethodType MstringToInt32£that=mt(Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Library,ActionType.Type.Library})
  public  Object MstringToInt32£that(Object cb){
    String s=extractStringU(cb);
    if (s==null){throw new Resources.Error("InvalidStringU");}
    try{return new Integer(s);}
    catch(NumberFormatException nfe){throw new Resources.Error("InvalidEncodingForI32: "+s);    }
  }
  
  private static Ast.MethodType Mint32ToString£that=mt(Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Library,ActionType.Type.Library})
  public  Object Mint32ToString£that(Object cb){
    Integer i=extractInt32(cb);
    if (i==null){throw new Resources.Error("InvalidStringU");}
    return ""+i;
  }
  private static Ast.MethodType MstringDebug£that=mt(Path.Void(),Path.Library());
  @ActionType({ActionType.Type.Void,ActionType.Type.Library})
  public Resources.Void MstringDebug£that(Object cb){
    String s=extractStringU(cb);
    if (s==null){throw new Resources.Error ("InvalidStringU");}
    L42.printDebug(s);
    return Resources.Void.instance;
  }
  
  private static Ast.MethodType MfileDebug£fileName£content=mt(Path.Void(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public Resources.Void MfileDebug£fileName£content(Object _fName,Object _content){
    String fName=ensureExtractStringU(_fName);
    String content=ensureExtractStringU(_content);
    java.nio.file.Path p=Paths.get(fName);
    try {Files.write(p, content.getBytes());}
    catch (IOException e) {throw new Error(e);}
    return Resources.Void.instance;
  }
  private static Ast.MethodType MstringConcat£s1£s2=mt(Path.Library(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MstringConcat£s1£s2(Object cb1,Object cb2){
    String s1=extractStringU(cb1);
    String s2=extractStringU(cb2);
    if (s1==null||s2==null){throw new Resources.Error("InvalidStringU");}
    return s1+s2;
  }

  private static Ast.MethodType MstringSize£that=mt(Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Library,ActionType.Type.Library})
  public  Object MstringSize£that(Object cb1){
    String s1=extractStringU(cb1);
    if (s1==null){throw new Resources.Error("InvalidStringU");}
    return s1.length();
  }
  private static Ast.MethodType MstringCharAt£that£pos=mt(Path.Library(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MstringCharAt£that£pos(Object cb1,Object cb2){
    String s1=extractStringU(cb1);
    Integer pos=extractInt32(cb2);
    if (s1==null||pos==null){throw new Resources.Error("InvalidStringU");}
    if(pos<0||s1.length()<=pos){throw new Resources.Error("InvalidIndexForStringCharAt:"+pos+" max acceptable is:"+s1.length());}
    String r = s1.substring(pos, pos+1);
    return r;
  }

  private static Ast.MethodType MifStringEqualDo£s1£s2=mt(Path.Void(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public Resources.Void MifStringEqualDo£s1£s2(Object cb1,Object cb2){
    String s1=extractStringU(cb1);
    String s2=extractStringU(cb2);
    if (s1==null||s2==null){throw new Resources.Error("InvalidStringU");}
    if(!s1.equals(s2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
}
