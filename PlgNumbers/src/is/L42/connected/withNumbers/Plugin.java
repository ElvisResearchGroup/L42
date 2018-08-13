package is.L42.connected.withNumbers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import facade.L42;
import static auxiliaryGrammar.EncodingHelper.*;

public class Plugin implements PluginType.WellKnown {
  // +
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MsumNum£xn1£xn2(Object cb1,Object cb2){
    BigRational  i1=ensureExtract(BigRational.class,cb1);
    BigRational  i2=ensureExtract(BigRational.class,cb2);
    return i1.sum(i2);
  }
  //*
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MmulNum£xn1£xn2(Object cb1,Object cb2){
    BigRational  i1=ensureExtract(BigRational.class,cb1);
    BigRational  i2=ensureExtract(BigRational.class,cb2);
    return i1.multiply(i2);
  }
  // /
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MdivNum£xn1£xn2(Object cb1,Object cb2){
    BigRational  i1=ensureExtract(BigRational.class,cb1);
    BigRational  i2=ensureExtract(BigRational.class,cb2);
    return i1.divide(i2);
  }
  //-
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MsubNum£xn1£xn2(Object cb1,Object cb2){
    BigRational  i1=ensureExtract(BigRational.class,cb1);
    BigRational  i2=ensureExtract(BigRational.class,cb2);
    return i1.subtract(i2);
  }
  //==
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Resources.Void MifNumEqualDo£xn1£xn2(Object cb1,Object cb2){
    BigRational  i1=ensureExtract(BigRational.class,cb1);
    BigRational  i2=ensureExtract(BigRational.class,cb2);
    if(!i1.equals(i2)){return Resources.Void.instance;}
    throw Resources.notAct;
  }
  //<
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Resources.Void MifNumGrtDo£xn1£xn2(Object cb1,Object cb2){
    BigRational  i1=ensureExtract(BigRational.class,cb1);
    BigRational  i2=ensureExtract(BigRational.class,cb2);
    if(i1.compareTo(i2)<0){
      throw Resources.notAct;
      //return Resources.Void.instance;
      }
    return Resources.Void.instance;
    //throw Resources.notAct;
  }
  //<=
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Resources.Void MifNumGEqDo£xn1£xn2(Object cb1,Object cb2){
    BigRational  i1=ensureExtract(BigRational.class,cb1);
    BigRational  i2=ensureExtract(BigRational.class,cb2);
    if(i1.compareTo(i2)<=0){
      throw Resources.notAct;
      }
    return Resources.Void.instance;
    }

  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MstringToNum£xthat(Object cb){
    String s=ensureExtractStringU(cb);
    try{return BigRational.from(s);}
    catch(NumberFormatException nfe){throw new Resources.Error("InvalidEncodingForI32: "+s);    }
  }

  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MnumToString£xthat(Object cb){
    BigRational  i=ensureExtract(BigRational.class,cb);
    return ""+i;
  }
}
