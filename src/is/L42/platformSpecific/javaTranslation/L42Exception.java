package is.L42.platformSpecific.javaTranslation;

@SuppressWarnings("serial")
public class L42Exception extends L42Throwable{
  public String toString() {return "Exception["+ unbox +"]";}
  public L42Exception(Object u){super(u);}
  }