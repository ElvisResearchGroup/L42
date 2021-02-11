package is.L42.platformSpecific.javaTranslation;

@SuppressWarnings("serial")
public class L42Return extends L42Throwable{
  public String toString() {return "Return["+ unbox +"]";}
  public L42Return(Object u){super(u);}
  }