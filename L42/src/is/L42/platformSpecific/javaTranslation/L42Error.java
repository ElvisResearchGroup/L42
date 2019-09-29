package is.L42.platformSpecific.javaTranslation;

@SuppressWarnings("serial")
public class L42Error extends L42Throwable{
  public String toString() {return "Error["+ unbox +"]";}
  public L42Error(Object u){super(u);}
  }