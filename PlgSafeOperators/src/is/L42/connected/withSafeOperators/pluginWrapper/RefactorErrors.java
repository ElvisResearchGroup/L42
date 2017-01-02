package is.L42.connected.withSafeOperators.pluginWrapper;


interface FluentSetter<T>{
  void setMessage(String msg);
  
  @SuppressWarnings("unchecked")
  default T msg(String msg){
    setMessage(msg);
    return (T)this;
    }
  }

@SuppressWarnings("serial")
abstract class RedirectExc extends Exception{
  //TODO: add location/internalLocation?
  String mutMsg;//since java do not want setMessage  :(
  public @Override String getMessage(){return mutMsg;}
  public void setMessage(String msg){mutMsg=msg;}
}

public class RefactorErrors{
  @SuppressWarnings("serial") public static class 
  SelectorNotFound extends RedirectExc implements
    FluentSetter<SelectorNotFound>{}

  @SuppressWarnings("serial") public static class 
  PathNotFound extends RedirectExc implements
    FluentSetter<PathNotFound>{}
  
  @SuppressWarnings("serial") public static class 
  MethodClash extends RedirectExc implements
    FluentSetter<MethodClash>{}
  
  @SuppressWarnings("serial") public static class 
  MethodUnfit extends RedirectExc implements
    FluentSetter<MethodUnfit>{}
  
  @SuppressWarnings("serial") public static class 
  ClassClash extends RedirectExc implements
    FluentSetter<ClassClash>{}
  
  @SuppressWarnings("serial") public static class 
  ClassUnfit extends RedirectExc implements
    FluentSetter<ClassUnfit>{}
  
  @SuppressWarnings("serial") public static class 
  PrivacyCoupuled extends RedirectExc implements
    FluentSetter<PrivacyCoupuled>{}
  
  @SuppressWarnings("serial") public static class 
  IncoherentMapping extends RedirectExc implements
    FluentSetter<IncoherentMapping>{}
  
  @SuppressWarnings("serial") public static class 
  InducedCircularImplement extends RedirectExc implements
    FluentSetter<InducedCircularImplement>{}
  
  @SuppressWarnings("serial") public static class 
  UnresolvedOverloading extends RedirectExc implements
    FluentSetter<UnresolvedOverloading>{}
  
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
 NestedClass/Type represent a internal/external location already,
 so all exceptions can have a NestedClass/Type field
 and a Location allowing with-case on Location.InterfaceNum, ...Location.methodReturnType

so
NestedClass/Method/TypeAnnotation(with mdf, doc, Role)
Role: implInterf n NestedClass,retType Method,par n Method, thisT Method, exception n Method
or TypeAnnotation is interface and "roles" are concrete classes?
again Class can be interface and ClassAsLibrary/ClassAsObject can be concrete?

Introspection
Refactor
Selector
ClassPath
Location
  Class .Free .Named
  Method
  Type
  .Interface 
  .ReturnType ...
 
Use.Fail Location (will be either Class.Named or Method of a Class.Named) 



public class InvalidMethodType extends PlgWrapperException{
    static String ret="return";
    static String par="parameter";
    static String exc="exception";
    public static enum Kind{Primitive,MethodNotPresent,InvalidModifier,NoPluginWithPart}
    Kind invalidKind
    public static enum Tag{Primitive,MethodNotPresent,InvalidModifier,NoPluginWithPart}
    Tag tag;
    
    static String noPrimitive2="primitive %s can not mapped as a %s"; //t kind
    static String meth2="method %s not present in external path %s";//ms t
    static String mdf2="%s %s has invalid modifier (class/fwd)";//kind t
    static String internalRef1="internal path %s is not annotated as a plugin with part";//t     
    public static String 
    String why;
    InvalidMethodType(String internalPath, String selector, String kind,String t,String ms) {
    super(internalPath, selector);
    this.ge
    );
    }
}

class PlgWrapperException extends Exception{
  String internalPath;
  String selector;
  String mutMsg;//since java do not want setMessage  :(
  public @Override String getMessage(){return mutMsg;}
  protected void setMessage(String msg){mutMsg=msg;}
  PlgWrapperException(String internalPath,String selector){
    super();
    this.internalPath=internalPath;this.selector=selector;
    }
}
*/