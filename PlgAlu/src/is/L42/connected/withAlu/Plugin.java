 package is.L42.connected.withAlu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import facade.L42;
import static auxiliaryGrammar.EncodingHelper.*;

public class Plugin implements PluginType.WellKnown {
  // +
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MsumInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    return i1+i2;
  }
  //*
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MmulInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    return i1*i2;
  }
  // /
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MdivInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    return i1/i2;
  }
  //-
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MsubInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    return i1-i2;
  }
  //&
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MandInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    return i1 & i2;
  }
  // |
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MorInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    return i1 | i2;
  }
  // ^
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MxorInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    return i1 ^ i2;
  }
  // <<
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MleftShiftInt32£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    if(i2>=32){return 0;}
    return i1 << i2;
  }
//>>
 @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
 public  Object MrightShiftInt32£xn1£xn2(Object cb1,Object cb2){
   Integer i1=ensureExtractInt32(cb1);
   Integer i2=ensureExtractInt32(cb2);
   if(i2>=32){return 0;}
   return i1 >>> i2;
 }
  //~
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MnotInt32£xn1(Object cb1){
    Integer i1=ensureExtractInt32(cb1);
    return ~i1 ;
  }
  //==
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Resources.Void MifInt32EqualDo£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    if(!i1.equals(i2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
  //<
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Resources.Void MifInt32GrtDo£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    if(!(i1<i2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
  //<=
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Resources.Void MifInt32GEqDo£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    if(!(i1<=i2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
//< unsigned
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Resources.Void MifUInt32GrtDo£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    if(Integer.compareUnsigned(i1,i2)>=0){return Resources.Void.instance;}
    throw Resources.notAct;
  }
//<= unsigned
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Resources.Void MifUInt32GEqDo£xn1£xn2(Object cb1,Object cb2){
    Integer i1=ensureExtractInt32(cb1);
    Integer i2=ensureExtractInt32(cb2);
    if(Integer.compareUnsigned(i1,i2)>0){
      return Resources.Void.instance;
      }
    throw Resources.notAct;
  }
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MstringToInt32£xthat(Object cb){
    String s=ensureExtractStringU(cb);
    try{return Integer.valueOf(s);}
    catch(NumberFormatException nfe){throw new Resources.Error("InvalidEncodingForI32: "+s);    }
  }

  @ActionType({ActionType.NormType.Library})
  public  Object MconstStringEmpty(){return "";}


  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object Mint32ToString£xthat(Object cb){
    Integer i=ensureExtractInt32(cb);
    return ""+i;
  }
  //isTrueBool
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library})
  public  Resources.Void MifFalseBoolDo£xthat(Object cb1){
    Boolean b=ensureExtractBool(cb1);
    if(b){return Resources.Void.instance;}
    throw Resources.notAct;
  }
  @ActionType({ActionType.NormType.Library})
  public  Object MconstTrue(){return true;}

  @ActionType({ActionType.NormType.Library})
  public  Object MconstFalse(){return false;}

  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library})
  public Resources.Void MstringDebug£xthat(Object cb){
    String s=ensureExtractStringU(cb);
    L42.printDebug(s);
    return Resources.Void.instance;
  }

  public static HashMap<String, Long> counters = new HashMap<>();
  @ActionType({ActionType.NormType.Void, ActionType.NormType.Library})
  public Resources.Void MincrementCounter£xthat(Object cb){
    String s=ensureExtractStringU(cb);

    // sets counters[s] to 1 if s was not in dom(counters)
    // otherwise adds y (which is set to 1) to the value that is already there
    counters.merge(s, 1L, (x, y) -> x + y);
    return Resources.Void.instance;
  }

  @ActionType({ActionType.NormType.Void, ActionType.NormType.Library})
  public Resources.Void MprintCounter£xthat(Object cb){
    String s = ensureExtractStringU(cb);
    L42.printDebug( s + ": " + counters.getOrDefault(s, 0L));
    return Resources.Void.instance;
  }

  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MstringConcat£xs1£xs2(Object cb1,Object cb2){
    String s1=ensureExtractStringU(cb1);
    String s2=ensureExtractStringU(cb2);
    return s1+s2;
  }
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MstringSize£xthat(Object cb1){
    String s1=ensureExtractStringU(cb1);
    return s1.length();
  }
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MstringSubstring£xthat£xstart£xend(Object cb1,Object _start,Object _end){
    String s1=ensureExtractStringU(cb1);
    int start=ensureExtractInt32(_start);
    int end=ensureExtractInt32(_end);
    if(start<0||end<0){throw new Resources.Error("NegativeIndexNotAllowed:"+start+","+end);}
    end=Math.min(end,s1.length());
    if(start>=s1.length()){return "";}
    String r = s1.substring(start, end);
    return r;
  }

  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public Resources.Void MifStringEqualDo£xs1£xs2(Object cb1,Object cb2){
    String s1=ensureExtractStringU(cb1);
    String s2=ensureExtractStringU(cb2);
    if(!s1.equals(s2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public Object MstringReplace£xthat£xsrc£xdest(Object _that,Object _src,Object _dest){
    String that=ensureExtractStringU(_that);
    String src=ensureExtractStringU(_src);
    String dest=ensureExtractStringU(_dest);
    return that.replace(src, dest);
  }
}
