package is.L42.connected.withSafeOperators.pluginWrapper;

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
abstract class RedirectExc extends Exception{
  //TODO: add location/internalLocation?
  String mutMsg;//since java do not want setMessage  :(
  public @Override String getMessage(){return mutMsg;}
  public void setMessage(String msg){mutMsg=msg;}
  public String text(){return mutMsg;}
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
  Type interface?
  .Interface 
  .ReturnType ...
 
Use.Fail Location (will be either Class.Named or Method of a Class.Named) 
//done: note: x to Java have to use _num to make xs uniques
//TODO: today we do not support arrays in x toJava. Are we happy?
*/