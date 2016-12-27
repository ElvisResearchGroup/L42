package is.L42.connected.withSafeOperators.pluginWrapper;

/*
 TODO:
 to make enumerations going from java to 42:
 1 make a pluginMethod in Alu that an enum object return its ordinal
 2 lift Enumeration out of its test and then add a #from using the plugin method of before
 3 allow internal reference that have the right method
 
 
 For exceptions, may be is better to actually declare the hierarky
 
 GeneralExc is interface
 GeneralExc.Specific1 all specific as nested
 catch GeneralExc can access all common fields
 catch GeneralExc.Specificn to access the specific ones
 error on GeneralExc X"" to assert we believe we capture all the options
 
 for plugins: make a #method that throw all the Specific
 and a method that throw the General, if you really want...
 * */


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
