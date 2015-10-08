package auxiliaryGrammar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import facade.Configuration;
import platformSpecific.javaTranslation.Resources;
import tools.Assertions;
import ast.Ast;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.SignalKind;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.Using;
import ast.ExpCore._void;
import ast.Util.CachedStage;

public class EncodingHelper{
  //encoding
  //'@int32"
  //     '0"
  //'@stringU"
  //     'fuffa\u0b12bar"
  //only works for unicode16bit
  public static String/*with unicode inside*/ parseStringUnicode(String s/*with \uFFFF inside*/){
    StringBuilder result=new StringBuilder();
    for(int i=0;i<s.length();i++){
      char c=s.charAt(i);
      if(c!='\\'){result.append(c);continue;}
      if(i+5>=s.length()){throw Assertions.codeNotReachable("Better error here");}
      if(s.charAt(i+1)!='u'){throw Assertions.codeNotReachable("Better error here");}
      char uc = (char)Integer.parseInt( s.substring(i+2,i+6), 16 );
      result.append(uc);i=i+5;
      }
    return result.toString();
  }
  static boolean isValid42Char(char c){
    String symbols=" ~!@#$%^&*()_-=+{}[]:;\"\'\\<>,.?/";
    if(symbols.indexOf(c)!=-1){return true;}
    if(c>='a'&& c<='z'){return true;}
    if(c>='A'&& c<='Z'){return true;}
    if(Character.isDigit(c)){return true;}
    return false;
  }
  public static String/*with \uFFFF inside*/ produceStringUnicode(String s/*with unicode inside*/) {
    StringBuilder result=new StringBuilder();
    for(int i=0;i<s.length();i++){
      char c=s.charAt(i);
      if(c=='\\'){addAsUnicode(result,c);continue;}
      if(c=='@'){addAsUnicode(result,c);continue;}//otherwise, the special meaning of @ in comments mess up with string encoding
      if(isValid42Char(c)){result.append(c);continue;}
      addAsUnicode(result,c);
      }
    return result.toString();
  }

  static void addAsUnicode(StringBuilder result, char c) {
    result.append("\\u");
    result.append(String.format("%04x", (int) c));
  }
  public static String composedName(Using u) {
    String result=u.getS().getName()+" ";
    for(String s:u.getS().getNames()){result+=s+" ";}
    return result;
  }
  public static ExpCore wrapError(String err) {
    return new ExpCore.Signal(SignalKind.Error,wrapStringU(err));
  }
  public static ClassB wrapInt32(int i) {
    return wrapInt32(""+i);
  }
  public static ClassB wrapInt32(String i) {
    return new ClassB(Doc.factory("@int32\n"+i+"\n"),Doc.empty(),false,Collections.emptyList(),Collections.emptyList(),verifiedStage.copyMostStableInfo());
  }
  public static ClassB wrapStringU(String s) {
    return new ClassB(Doc.factory("@stringU\n"+produceStringUnicode(s)+"\n"),Doc.empty(),false,Collections.emptyList(),Collections.emptyList(),verifiedStage.copyMostStableInfo());
  }

  public static ClassB ensureExtractCt(Object e,Program p) {
    ClassB cb=ensureExtractClassB(e);
    //ClassB res = Configuration.typeSystem.typeExtraction(p,cb);
    return cb;
    }
 
  public static Doc ensureExtractDoc(Object e) {
    Doc res=extractDoc(e);
    if (res==null ){throw new Resources.Error("InvalidClassB");}
    return res;
    }
  public static Doc extractDoc(Object e) {
    if(e instanceof Doc){return (Doc)e;}
    if(e instanceof ClassB){return ((ClassB)e).getDoc1();}
    return null;
    }
  public static ClassB ensureExtractClassB(Object e) {
    ClassB res=extractClassB(e);
    if (res==null ){throw new Resources.Error("InvalidClassB");}
    return res;
    }
  public static ClassB extractClassB(Object e) {
    if(e instanceof ClassB){return (ClassB)e;}
    if(e instanceof Integer){return wrapInt32((Integer)e);}
    if(e instanceof String){return wrapStringU((String)e);}
    return null;
    }
  public static Integer ensureExtractInt32(Object e) {
    Integer res=extractInt32(e);
    if (res==null ){throw new Resources.Error("InvalidInt32");}
    return res;
    }
  public static Integer extractInt32(Object e) {
    if(e instanceof Integer){return (Integer)e;}
    if(!(e instanceof ClassB)){return null;}
    ClassB cb=(ClassB)e;
    String code=extractCode(cb,"@int32\n");
    if(code==null){return null;}
    try{return Integer.parseInt(code);}
    catch(NumberFormatException nfe){return null;}
    }
  
  public static Path ensureExtractPathFromJava(Object e) {
    Path res=extractPathFromJava(e);
    if (res==null ){throw new Resources.Error("InvalidPath");}
    return res;
    }
  public static Ast.Path extractPathFromJava(Object e) {
    if(e instanceof Ast.Path){return (Ast.Path)e;}
    if(e instanceof Resources.Any){return Path.Any();}
    if(e instanceof Resources.Library){return Path.Library();}
    if(e instanceof Resources.Void){return Path.Void();}
    assert e instanceof Resources.Revertable;
    return (Path) ((Resources.Revertable)e).revert();
    }
  public static String ensureExtractStringU(Object e) {
    String res=extractStringU(e);
    if (res==null ){
      System.out.println("Doh, invalid string"+e);
      throw new Resources.Error("InvalidStringU");
      }
    return res;
    }
  public static String extractStringU(Object e) {
    if(e instanceof String){return (String)e;}
    if(!(e instanceof ClassB)){return null;}
    ClassB cb=(ClassB)e;
    String code=extractCode(cb,"@stringU\n");
    if(code==null){return null;}
    return EncodingHelper.parseStringUnicode(code);
    }

  static String extractCode(ClassB cb, String prefix) {
    if(cb.isInterface()){return null;}
    if(!cb.getMs().isEmpty()){return null;}
    if(!cb.getDoc2().isEmpty()){return null;}
    String result=cb.getDoc1().toString();
    if(!result.startsWith(prefix)){return null;}
    if(result.length()==prefix.length()){return "";}
    result=result.substring(prefix.length(),result.length()-1);
    return result;
  }
  public static Ast.MethodType mtType(Path result){
    List<Doc> docs=new ArrayList<>();
    List<Type> ts=new ArrayList<>();
    ts.add(new Ast.NormType(Mdf.Type,Path.Any(),Ph.None));
    docs.add(Doc.empty());
    return new Ast.MethodType(Doc.empty(),Mdf.Immutable,ts,docs,new Ast.NormType(Mdf.Immutable,result,Ph.None),Collections.emptyList());    
  }
  
  public static Ast.MethodType mt(Path result,Path ...paths){
    List<Doc> docs=new ArrayList<>();
    List<Type> ts=new ArrayList<>();
    for(Path pi:paths){
      ts.add(new Ast.NormType(Mdf.Immutable,pi,Ph.None));
      docs.add(Doc.empty());
      }
    return new Ast.MethodType(Doc.empty(),Mdf.Immutable,ts,docs,new Ast.NormType(Mdf.Immutable,result,Ph.None),Collections.emptyList());
  }
  public static ExpCore wrapResource(Object o) {
    if(o instanceof ClassB){return (ClassB)o;}
    if(o instanceof ExpCore){return (ExpCore)o;}
    if(o instanceof Integer){return wrapInt32((Integer)o);}
    if(o instanceof String){return wrapStringU((String)o);}
    if(o instanceof Resources.Void){return new ExpCore._void();}
    if(o instanceof Resources.Error){
      ExpCore inside=wrapResource(((Resources.Error)o).unbox);
      return new ExpCore.Signal(SignalKind.Error,inside);
      }
    if(o instanceof Resources.Exception){
      ExpCore inside=wrapResource(((Resources.Exception)o).unbox);
      return new ExpCore.Signal(SignalKind.Exception,inside);
      }
    if(o instanceof Resources.Return){
      ExpCore inside=wrapResource(((Resources.Return)o).unbox);
      return new ExpCore.Signal(SignalKind.Return,inside);
      }
    if( o instanceof Resources.Library){
      return Ast.Path.Library();
    }
    if( o instanceof Resources.Void){
      return Ast.Path.Void();
    }
    if( o instanceof Resources.Any){
      return Ast.Path.Any();
    }
    if(o instanceof Resources.Revertable){return ((Resources.Revertable)o).revert();}
    throw Assertions.codeNotReachable("I may have to expand to handle paths:"+o);
  }

  
  private static final CachedStage verifiedStage=new CachedStage();
  static{verifiedStage.setVerified(true);}
}
