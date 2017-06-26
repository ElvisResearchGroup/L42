package is.L42.connected.withSafeOperators.pluginWrapper;

import is.L42.connected.withSafeOperators.location.Lib;
import is.L42.connected.withSafeOperators.location.Location;
import is.L42.connected.withSafeOperators.location.Method;

import java.util.List;

import ast.Ast.MethodSelector;
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

@SuppressWarnings("serial")
abstract class MutMsgExc extends Exception{
  String mutMsg;//since java do not want setMessage  :(
  public @Override String getMessage(){return mutMsg;}
  public void setMessage(String msg){mutMsg=msg;}
  public String text(){return mutMsg;}
}

public class RefactorErrors{
  @SuppressWarnings("serial") public static class 
  SelectorUnfit extends MutMsgExc implements
    FluentSetter<SelectorUnfit>{
    List<ast.Ast.C> path; MethodSelector sel;
    public SelectorUnfit(List<ast.Ast.C> path, MethodSelector sel){this.path=path;this.sel=sel;}
    public String path(){return Location.as42Path(path);}
    public MethodSelector selector(){return sel;}
    
  }

  @SuppressWarnings("serial") public static class 
  PathUnfit extends MutMsgExc implements
    FluentSetter<PathUnfit>{
    List<ast.Ast.C> path;public PathUnfit(List<ast.Ast.C> path){this.path=path;}
    public String path(){return Location.as42Path(path);}
    }
  
  @SuppressWarnings("serial") public static class 
  MethodClash extends MutMsgExc implements
    FluentSetter<MethodClash>{
    Method left;Method right;
    public MethodClash(Method left,Method right){this.left=left;this.right=right;}
    public Method left(){return left;}
    public Method right(){return right;}
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
    FluentSetter<ClassUnfit>{}
  
  @SuppressWarnings("serial") public static class 
  PrivacyCoupuled extends MutMsgExc implements
    FluentSetter<PrivacyCoupuled>{}
  
  @SuppressWarnings("serial") public static class 
  IncoherentMapping extends MutMsgExc implements
    FluentSetter<IncoherentMapping>{}
  
  @SuppressWarnings("serial") public static class 
  SubtleSubtypeViolation extends MutMsgExc implements
    FluentSetter<SubtleSubtypeViolation>{}
  
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