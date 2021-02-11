package is.L42.platformSpecific.javaTranslation;

@SuppressWarnings("serial")
public class L42Throwable extends RuntimeException{
  public final Object unbox; public L42Throwable(Object u){
    unbox=u;
    assert u!=null;
    }
  public final Object obj42(){return unbox;}
  }