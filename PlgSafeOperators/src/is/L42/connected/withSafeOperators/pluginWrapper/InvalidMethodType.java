package is.L42.connected.withSafeOperators.pluginWrapper;

/*
 TODO:
 to make enumerations going from java to 42:
 1 make a pluginMethod in Alu that an enum object return its ordinal
 2 lift Enumeration out of its test and then add a #from using the plugin method of before
 3 allow internal reference that have the right method
 
 * */


public class InvalidMethodType extends PlgWrapperException{
    static String ret="return";
    static String par="parameter";
    static String exc="exception";
    String invalidKind;

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
