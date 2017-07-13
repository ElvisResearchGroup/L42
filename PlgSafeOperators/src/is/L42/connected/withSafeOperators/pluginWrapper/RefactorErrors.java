package is.L42.connected.withSafeOperators.pluginWrapper;

import is.L42.connected.withSafeOperators.location.Lib;
import is.L42.connected.withSafeOperators.location.Method;
import is.L42.connected.withSafeOperators.refactor.MembersUtils;
import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Resources.Error;
import tools.Assertions;

import java.util.List;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.PathAux;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB.Member;
import ast.Util.CsMx;
import ast.Util.CsPath;
import ast.Util.PathPath;
import ast.Util.CsSPath;
import ast.Util.PathMx;
/* fluent setter for errors, a good idea to avoid duplicating constructors, but
 * the main issue is that we need to support enriching the message while try-catching  
 */
interface FluentSetter<T>{
  void setMessage(String msg);
  
  @SuppressWarnings("unchecked")
  default T msg(String msg){
    setMessage(msg);
    return (T)this;
    }
  }

public class RefactorErrors{
  static String msgMapping(List<CsPath>verified,List<CsSPath>ambiguities,List<Ast.C> incoSrc,List<Path> incoDest) {
    assert incoSrc!=null || !ambiguities.isEmpty();
    String res="\n";
    if(!verified.isEmpty()){
      res+="verified:[";
      for(CsPath v:verified){
        res+=PathAux.as42Path(v.getCs())+"->"+v.getPath()+", ";
        }
      res=res.substring(0,res.length()-2)+"]\n";
      }
    if(!ambiguities.isEmpty()){
      res+="ambiguities:[";
      for(CsSPath a:ambiguities){
        res+=PathAux.as42Path(a.getCs())+"->[";
        if(a.getPathsSet().isEmpty()){res+="], ";}
        else {
          for(Path pij:a.getPathsSet()){res+=pij+", ";}
          res=res.substring(0,res.length()-2)+"], ";
          }
        }
      res=res.substring(0,res.length()-2)+"]\n";
      }
    
    if(incoSrc!=null){
      res+="incoherent destinations for "+PathAux.as42Path(incoSrc) +":[";
      for(Path pi:incoDest){res+=pi+", ";}
      res=res.substring(0,res.length()-2)+"]\n";
      }
  return res.substring(0,res.length()-1);
  }
  static String formatPairs(List<List<Ast.C>> srcs,List<Path> dests){
  StringBuffer res=new StringBuffer();
  res.append("[");
  assert srcs.size()==dests.size();
  {int i=-1;for(List<C> cs:srcs){i+=1;
    Path pi=dests.get(i);
    res.append(PathAux.as42Path(cs)+"->"+pi);
    res.append(", ");
    }}
  return res.substring(0,res.length()-2)+"]";
  }

  static String formatImplI(List<Path> extra){
    StringBuffer res=new StringBuffer();
    res.append("[");
    for(Path p:extra){
      assert p.isCore();
      if(p.outerNumber()==0){
        res.append(PathAux.as42Path(p.getCBar()));
        }
      else{res.append(p.toString());}
      res.append(", ");
      }
    return res.substring(0,res.length()-2)+"]";
    }
  static String formatMembers(List<ExpCore.ClassB.Member> extra){
    StringBuffer res=new StringBuffer();
    res.append("[");
    for(Member m:extra){
      m.match(
        nc->res.append(nc.getName()),
        mi->{throw Assertions.codeNotReachable();},
        mt->res.append(mt.getMs())
        );
      res.append(", ");
      }
    return res.substring(0,res.length()-2)+"]";
    }
  
  @SuppressWarnings("serial") public static class 
  ParseFail extends MutMsgExc implements
    FluentSetter<ParseFail>{
    String string;
    static public enum Format{Id,Selector,Path,Number};
    Format expected;
    public ParseFail(String text, Format expected){this.string=text;this.expected=expected;}
    public String string(){return string;}
    public String expected(){return expected.toString();}
    public String toString(){return "ParseFail[expected: "+expected()+", received: '"+string+"']";}
    }
  
  @SuppressWarnings("serial") public static class 
  SelectorUnfit extends MutMsgExc implements
    FluentSetter<SelectorUnfit>{
    List<ast.Ast.C> path; MethodSelector sel;
    public SelectorUnfit(List<ast.Ast.C> path, MethodSelector sel){this.path=path;this.sel=sel;}
    public List<ast.Ast.C> path(){return path;}
    public MethodSelector selector(){return sel;}
    public String toString(){return "SelectorUnfit:"+PathAux.as42Path(path())+"::"+selector();}
  }

  @SuppressWarnings("serial") public static class 
  PathUnfit extends MutMsgExc implements
    FluentSetter<PathUnfit>{
    List<ast.Ast.C> path;
    public PathUnfit(List<ast.Ast.C> path){this.path=path;}
    public List<ast.Ast.C> path(){return path;}
    }
  
  @SuppressWarnings("serial") public static class 
  MethodClash extends MutMsgExc implements
    FluentSetter<MethodClash>{
    Method left;Method right;
    public MethodClash(Method left,Method right){this.left=left;this.right=right;}
    public Method left(){return left;}
    public Method right(){return right;}
    public String toString(){return "MethodClash: "+left.location().path()+"::"
      +left.selector()+"->"
      +right.selector()+"\n"
      +"Left: "+left.formatOrigins()+"\n"     
      +"Right: "+right.formatOrigins()     
      ;}
    }
  
  @SuppressWarnings("serial") public static class 
  MethodUnfit extends MutMsgExc implements
    FluentSetter<MethodUnfit>{}
  
  @SuppressWarnings("serial") public static class 
  ClassClash extends MutMsgExc implements
    FluentSetter<ClassClash>{
    Lib left; Lib right;
    public ClassClash(Lib left,Lib right){this.left=left;this.right=right;}
    public Lib left(){return left;}
    public Lib right(){return right;}
    }
  
  @SuppressWarnings("serial") public static class 
  ClassUnfit extends MutMsgExc implements
    FluentSetter<ClassUnfit>{
  //used in many operators,
  //for redirect:"SourceUnfit",
  //msg: Redirecting src to dest
  //msg-a:src notRedirectable/notRedirectable to interface
  //msg-b:"UnexpectedMembers", "[fun(that)]",
  //msg-c:"UnexpectedImplementedInterfaces", "//[]"
    public ClassUnfit(){}
    public ClassUnfit msgRedirectTemplate(List<Ast.C> src,Path dest,boolean interf){
      return this.msg("Redirecting "+PathAux.as42Path(src)+" to "+dest+":\n"+
        (interf?"not redirectable to interface":"not redirectable"));
      }
    public ClassUnfit msgRedirectUnexpectedM(List<Ast.C> src,Path dest,List<ExpCore.ClassB.Member> extra){
      return this.msg("Redirecting "+PathAux.as42Path(src)+" to "+dest+":\n"+
        "Unexpected members in dest:"+formatMembers(extra));
      }
    public ClassUnfit msgRedirectUnexpectedI(List<Ast.C> src,Path dest,List<Path> extra){
      return this.msg("Redirecting "+PathAux.as42Path(src)+" to "+dest+":\n"+
        "Unexpected implemented interface in dest:"+formatImplI(extra));
      }

  }
  
  @SuppressWarnings("serial") public static class 
  PrivacyCoupuled extends MutMsgExc implements
    FluentSetter<PrivacyCoupuled>{
    List<List<Ast.C>> coupuledPaths;
    List<CsMx> coupuledMethods;
    public PrivacyCoupuled(List<List<Ast.C>> coupuledPaths, List<CsMx> coupuledMethods) {
      this.coupuledPaths=coupuledPaths;
      this.coupuledMethods=coupuledMethods;
      }
    public String toString(){
       return "PrivacyCoupuled:\n"+
       "coupuled paths:"+coupuledPaths+"\n"+
       "coupuled selectors:"+coupuledMethods;
      }
    }
  
  @SuppressWarnings("serial") public static class 
  IncoherentMapping extends MutMsgExc implements
    FluentSetter<IncoherentMapping>{

  /*public IncoherentMapping msgRedirect(List<Ast.C> src,Path dest,List<List<Ast.C>> srcs,List<Path> dests){
    return this.msg("Redirecting "+PathAux.as42Path(src)+" to "+dest+":\n"+
      "Incoherent mapping for:"+formatPairs(srcs, dests));      
    }*/
  public IncoherentMapping msgMapping(List<CsPath>verified,List<CsSPath>ambiguities,List<Ast.C> incoSrc,List<Path> incoDest){
    return this.msg(RefactorErrors.msgMapping(verified,ambiguities,incoSrc,incoDest));      
        }
  }
  
  @SuppressWarnings("serial") public static class 
  SubtleSubtypeViolation extends MutMsgExc implements
    FluentSetter<SubtleSubtypeViolation>{
    public String toString(){
      return "SubtleSubtypeViolation:"+this.mutMsg;
      }  
    }
  
  @SuppressWarnings("serial") public static class 
  UnresolvedOverloading extends MutMsgExc implements
    FluentSetter<UnresolvedOverloading>{}
  
  @SuppressWarnings("serial") public static class 
  NotAvailable extends MutMsgExc implements
    FluentSetter<NotAvailable>{}//For Location
  
}
  /*
 TODO:
 to make enumerations going from java to 42:
 1 make a pluginMethod in Alu that an enum object return its ordinal
 2 lift Enumeration out of its test and then add a #from using the plugin method of before
 3 allow internal reference that have just the right method
 
 
 For exceptions, may be is better to actually declare the hierarky
 
 GeneralExc is interface
 GeneralExc.Specific1 all specific as nested
 catch GeneralExc can access all common fields
 catch GeneralExc.Specificn to access the specific ones
 error on GeneralExc X"" to assert we believe we capture all the options
 
 for plugins: make a #method that throw all the Specific
 and a method that throw the General, if you really want...
 
 No, this interact with design of introspection.
 NestedClass/NormType represent a internal/external location already,
 so all exceptions can have a NestedClass/NormType field
 and a Location allowing with-case on Location.InterfaceNum, ...Location.methodReturnType

so
NestedClass/Method/TypeAnnotation(with mdf, doc, Role)
Role: implInterf n NestedClass,retType Method,par n Method, thisT Method, exception n Method
or TypeAnnotation is interface and "roles" are concrete classes?
again Class can be interface and ClassAsLibrary/ClassAsObject can be concrete?

Refactor
  with method creating a nested class with <<
  with nested exceptions 
    MethodClash //2 methods not ok together
    MethodUnfit //1 method not ok shape
    ClassClash
    ClassUnfit  
    privacycoupuled
    incoherentRedirectMapping
    cicular interface implements induced
    overloading 

Selector with exception NotFound
ClassPath with exception NotFound

Location
  Class .Free .Named
  Method
  NormType interface?
  .Interface 
  .ReturnType ...
 
Use.Fail Location (will be either Class.Named or Method of a Class.Named) 
//done: note: x to Java have to use _num to make xs uniques
//TODO: today we do not support arrays in x toJava. Are we happy?
*/