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
import ast.Ast.Type;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.SignalKind;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.ExpCore.ClassB;
import ast.ExpCore.EPath;
import ast.ExpCore.Using;
import ast.ExpCore._void;


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
      if(i+5>=s.length()){throw Assertions.codeNotReachable("Invalid string encoding:\n"+s);}
      if(s.charAt(i+1)!='u'){throw Assertions.codeNotReachable("Invalid string encoding:\n"+s);}
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
    String result=u.getS().nameToS()+" ";
    for(String s:u.getS().getNames()){result+=s+" ";}
    return result;
  }
  public static ExpCore wrapError(String err) {
    return new ExpCore.Signal(SignalKind.Error,wrapStringU(err),Path.Library().toImmNT(),Path.Library().toImmNT());
  }
  public static ClassB wrapInt32(int i) {
    return wrapInt32(""+i);
  }
  public static ClassB wrapInt32(String i) {
    return ClassB.docClass(Doc.factory(true,"@int32\n"+i+"\n"));
  }
  public static ClassB wrapBool(boolean b) {
    String s="@boolTrue\n";
    if(!b)s="@boolFalse\n";
    return ClassB.docClass(Doc.factory(true,s));
  }
  public static ClassB wrapStringU(String s) {
    return ClassB.docClass(Doc.factory(true,"@stringU\n"+produceStringUnicode(s)+"\n"));
  }


  public static Doc ensureExtractDoc(Object e) {
    if(e instanceof Doc){return (Doc)e;}
    ClassB cb=ensureExtractClassB(e);
    return cb.getDoc1();
    }

  public static ClassB ensureExtractClassB(Object e) {
    ClassB res=_extractClassB(e);
    if (res==null ){
      throw new Resources.Error("InvalidClassB for ensureExtractClassB");
      }
    return res;
    }
  public static ClassB _extractClassB(Object e) {
    if(e instanceof ClassB){return (ClassB)e;}
    if(e instanceof Integer){return wrapInt32((Integer)e);}
    if(e instanceof String){return wrapStringU((String)e);}
    return null;
    }
  public static <T> T ensureExtract(Class<T> clazz,Object e){
    assert clazz!=null;
    if (e==null ){throw new Resources.Error("Invalid null "+clazz);}
    try{
    return clazz.cast(e);
    }
    catch(ClassCastException cce){throw new Resources.Error("Invalid:"+clazz+" expected, but was: "+e.getClass());}
    }
  public static Integer ensureExtractInt32(Object e) {
    Integer res=_extractInt32(e);
    if (res==null ){throw new Resources.Error("InvalidInt32");}
    return res;
    }
  public static Integer _extractInt32(Object e) {
    if(e instanceof Integer){return (Integer)e;}
    if(!(e instanceof ClassB)){return null;}
    ClassB cb=(ClassB)e;
    String code=extractCode(cb,"@int32\n");
    if(code==null){return null;}
    try{return Integer.parseInt(code);}
    catch(NumberFormatException nfe){return null;}
    }

  public static Boolean ensureExtractBool(Object e) {
    Boolean res=_extractBool(e);
    if (res==null ){throw new Resources.Error("InvalidBool");}
    return res;
    }
  public static Boolean _extractBool(Object e) {
    if(e instanceof Boolean){return (Boolean)e;}
    if(!(e instanceof ClassB)){return null;}
    ClassB cb=(ClassB)e;
    Object ann = cb.getDoc1().getAnnotations().get(0);
    if(ann.equals("boolTrue")){return true;}
    if(ann.equals("boolFalse")){return false;}
    return null;
    }

  public static Path ensureExtractPathFromJava(Object e) {
    Path res=_extractPathFromJava(e);
    if (res==null ){throw new Resources.Error("InvalidPath");}
    return res;
    }
  public static Ast.Path _extractPathFromJava(Object e) {
    if(e instanceof Ast.Path){return (Ast.Path)e;}
    if(e instanceof Resources.Any){return Path.Any();}
    if(e instanceof Resources.Library){return Path.Library();}
    if(e instanceof Resources.Void){return Path.Void();}
    assert e instanceof Resources.Revertable;
    return ((ExpCore.EPath) ((Resources.Revertable)e).revert()).getInner();
    }
  public static String ensureExtractStringU(Object e) {
    String res=_extractStringU(e);
    if (res==null ){
      System.out.println("Doh, invalid string"+e);
      throw new Resources.Error("InvalidStringU");
      }
    return res;
    }
  public static String _extractStringU(Object e) {
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
    String result=cb.getDoc1().toString();
    if(!result.startsWith(prefix)){return null;}
    if(result.length()==prefix.length()){return "";}
    result=result.substring(prefix.length(),result.length()-1);
    return result;
  }
  public static Ast.MethodType mtType(Path result){
    List<Type> ts=new ArrayList<>();
    ts.add(Type.classAny);
    return new Ast.MethodType(false,Mdf.Immutable,ts,result.toImmNT(),Collections.emptyList());
  }

  public static Ast.MethodType mt(Path result,Path ...paths){
    List<Type> ts=new ArrayList<>();
    for(Path pi:paths){
      ts.add(pi.toImmNT());      }
    return new Ast.MethodType(false,Mdf.Immutable,ts,result.toImmNT(),Collections.emptyList());
  }
  public static ExpCore wrapResource(Object o) {
    if(o instanceof ClassB){return (ClassB)o;}
    if(o instanceof ExpCore){return (ExpCore)o;}
    if(o instanceof Integer){return wrapInt32((Integer)o);}
    if(o instanceof String){return wrapStringU((String)o);}
    if(o instanceof Resources.Void){return new ExpCore._void();}
    if(o instanceof Resources.Error){
      ExpCore inside=wrapResource(((Resources.Error)o).unbox);
      return new ExpCore.Signal(SignalKind.Error,inside,null,null);
      }
    if(o instanceof Resources.Exception){
      ExpCore inside=wrapResource(((Resources.Exception)o).unbox);
      return new ExpCore.Signal(SignalKind.Exception,inside,null,null);
      }
    if(o instanceof Resources.Return){
      ExpCore inside=wrapResource(((Resources.Return)o).unbox);
      return new ExpCore.Signal(SignalKind.Return,inside,null,null);
      }
    if( o instanceof Resources.Library){
      return EPath.wrap(Ast.Path.Library());
    }
    if( o instanceof Resources.Void){
      return EPath.wrap(Ast.Path.Void());
    }
    if( o instanceof Resources.Any){
      return EPath.wrap(Ast.Path.Any());
    }
    if(o instanceof Resources.Revertable){return ((Resources.Revertable)o).revert();}
    return wrapResource(o.toString());
  }
  
  
 //javaFullName can contains dots but not %
 //"." converted in "%" and unicode weirdos converted in "%%u..."
 //since no two dots in a row are allowed

  public static String javaClassToX(int parNum,String that){
    that=produceStringUnicode(that);//now all \\u inside, no other \ inside
    that=that.replace("\\u", "%%");
    return "_"+parNum+"_"+that.replace(".","%");
  }
  public static String xToJavaClass(String that){
    assert that.charAt(0)=='_';
    that=that.substring(1);
    assert that.indexOf("_")!=-1:that;
    that=that.substring(that.indexOf("_")+1);
    if(that.equals("Library")){return ExpCore.ClassB.class.getName();}
    that=that.replace("%%%", ".%%");
    that=that.replace("%%", "\\u");
    that=that.replace("%", ".");
    that=parseStringUnicode(that);
    return that; 
  }

}
