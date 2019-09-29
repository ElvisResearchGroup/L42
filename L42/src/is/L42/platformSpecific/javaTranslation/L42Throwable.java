package is.L42.platformSpecific.javaTranslation;

@SuppressWarnings("serial")
public class L42Throwable extends RuntimeException{
  public final Object unbox; public L42Throwable(Object u){unbox=u;}
  public final Object inner(){return unbox;}
  }