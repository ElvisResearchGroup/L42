package is.L42.connected.withAlu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import facade.L42;
import static auxiliaryGrammar.EncodingHelper.*;

public class Plugin implements PluginType {
  // +
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MsumInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    return i1+i2;
  }
  //*
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MmulInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    return i1*i2;
  }
  // /
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MdivInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    return i1/i2;
  }
  //-
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MsubInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    return i1-i2;
  }
  //==
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public  Resources.Void MifInt32EqualDo£xn1£xn2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    if(!i1.equals(i2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
  //<
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public  Resources.Void MifInt32GrtDo£xn1£xn2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    if(!(i1<i2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
  //<=
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public  Resources.Void MifInt32GEqDo£xn1£xn2(Object cb1,Object cb2){
    Integer i1=extractInt32(cb1);
    Integer i2=extractInt32(cb2);
    if (i1==null ||i2==null){throw new Resources.Error("InvalidInt32");}
    if(!(i1<=i2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }

  @ActionType({ActionType.Type.Library,ActionType.Type.Library})
  public  Object MstringToInt32£xthat(Object cb){
    String s=extractStringU(cb);
    if (s==null){throw new Resources.Error("InvalidStringU");}
    try{return new Integer(s);}
    catch(NumberFormatException nfe){throw new Resources.Error("InvalidEncodingForI32: "+s);    }
  }
  
  @ActionType({ActionType.Type.Library,ActionType.Type.Library})
  public  Object Mint32ToString£xthat(Object cb){
    Integer i=extractInt32(cb);
    if (i==null){throw new Resources.Error("InvalidStringU");}
    return ""+i;
  }
  @ActionType({ActionType.Type.Void,ActionType.Type.Library})
  public Resources.Void MstringDebug£xthat(Object cb){
    String s=extractStringU(cb);
    if (s==null){throw new Resources.Error ("InvalidStringU");}
    L42.printDebug(s);
    return Resources.Void.instance;
  }
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public Resources.Void MfileDebug£xfileName£xcontent(Object _fName,Object _content){
    String fName=ensureExtractStringU(_fName);
    String content=ensureExtractStringU(_content);
    java.nio.file.Path p=Paths.get(fName);
    try {Files.write(p, content.getBytes());}
    catch (IOException e) {throw new Error(e);}
    return Resources.Void.instance;
  }
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MstringConcat£xs1£xs2(Object cb1,Object cb2){
    String s1=extractStringU(cb1);
    String s2=extractStringU(cb2);
    if (s1==null||s2==null){throw new Resources.Error("InvalidStringU");}
    return s1+s2;
  }
  @ActionType({ActionType.Type.Library,ActionType.Type.Library})
  public  Object MstringSize£xthat(Object cb1){
    String s1=extractStringU(cb1);
    if (s1==null){throw new Resources.Error("InvalidStringU");}
    return s1.length();
  }
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public  Object MstringCharAt£xthat£xpos(Object cb1,Object cb2){
    String s1=extractStringU(cb1);
    Integer pos=extractInt32(cb2);
    if (s1==null||pos==null){throw new Resources.Error("InvalidStringU");}
    if(pos<0||s1.length()<=pos){throw new Resources.Error("InvalidIndexForStringCharAt:"+pos+" max acceptable is:"+s1.length());}
    String r = s1.substring(pos, pos+1);
    return r;
  }
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public Resources.Void MifStringEqualDo£xs1£xs2(Object cb1,Object cb2){
    String s1=extractStringU(cb1);
    String s2=extractStringU(cb2);
    if (s1==null||s2==null){throw new Resources.Error("InvalidStringU");}
    if(!s1.equals(s2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
}
